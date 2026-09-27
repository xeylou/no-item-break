package xeylou.noitembreak.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xeylou.noitembreak.WornItems;

// durability is server-authoritative, so a client cannot cancel wear. instead, watch the equipped slots every
// tick and, right after the server reports wear that leaves 10 or less, send the same click as a shift-click.
// ONLY plain player inventory clicks are sent, so this works on any server, including vanilla ones.
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

	@Unique
	private static final EquipmentSlot[] WATCHED_SLOTS = {
			EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};
	// Matching InventoryMenu slot numbers. The main hand one is offset by the selected hotbar slot.
	@Unique
	private static final int[] MENU_SLOTS = {
			InventoryMenu.USE_ROW_SLOT_START, InventoryMenu.SHIELD_SLOT,
			InventoryMenu.ARMOR_SLOT_START, InventoryMenu.ARMOR_SLOT_START + 1,
			InventoryMenu.ARMOR_SLOT_START + 2, InventoryMenu.ARMOR_SLOT_START + 3
	};

	@Shadow
	@Final
	protected Minecraft minecraft;

	// item type & damage seen last tick, per watched slot. server sends a new ItemStack instance on every
	// slot update, so values are remembered rather than references. LocalPlayer is recreated on respawn and
	// dimension change, which resets this memory.
	@Unique
	private final Item[] xeylou$lastItems = new Item[WATCHED_SLOTS.length];
	@Unique
	private final int[] xeylou$lastDamage = new int[WATCHED_SLOTS.length];
	@Unique
	private int xeylou$lastSelectedSlot = -1;

	@Inject(method = "tick()V", at = @At("TAIL"))
	private void xeylou$rescueWornItems(CallbackInfo ci) {
		LocalPlayer player = (LocalPlayer) (Object) this;

		// server rejects clicks on the player inventory while another container is open. keep old
		// memory so wear that happened meanwhile is still detected once it is closed.
		if (player.containerMenu != player.inventoryMenu) {
			return;
		}

		int selectedSlot = player.getInventory().getSelectedSlot();
		boolean canAct = !player.hasInfiniteMaterials() && !player.isSpectator();

		for (int i = 0; i < WATCHED_SLOTS.length; i++) {
			ItemStack stack = player.getItemBySlot(WATCHED_SLOTS[i]);
			boolean wore = stack.getItem() == this.xeylou$lastItems[i]
					&& stack.getDamageValue() > this.xeylou$lastDamage[i]
					// Switching from a fresh pickaxe to a worn one in the hotbar is not wear.
					&& (WATCHED_SLOTS[i] != EquipmentSlot.MAINHAND || selectedSlot == this.xeylou$lastSelectedSlot);

			if (canAct && wore && WornItems.isWorn(stack)) {
				int menuSlot = MENU_SLOTS[i] + (WATCHED_SLOTS[i] == EquipmentSlot.MAINHAND ? selectedSlot : 0);
				this.xeylou$moveToInventory(player, menuSlot);
				stack = player.getItemBySlot(WATCHED_SLOTS[i]);
			}

			this.xeylou$lastItems[i] = stack.getItem();
			this.xeylou$lastDamage[i] = stack.getDamageValue();
		}
		this.xeylou$lastSelectedSlot = selectedSlot;
	}

	@Unique
	private void xeylou$moveToInventory(LocalPlayer player, int menuSlot) {
		// Curse of Binding: the game does not let the item be taken off, leave it there.
		if (!player.inventoryMenu.getSlot(menuSlot).mayPickup(player)) {
			return;
		}
		int containerId = player.inventoryMenu.containerId;
		this.minecraft.gameMode.handleContainerInput(containerId, menuSlot, 0, ContainerInput.QUICK_MOVE, player);

		// The click is applied locally before being sent, so a slot that is still filled means the
		// inventory is full: throw the whole stack instead, like Ctrl+Q.
		if (!player.inventoryMenu.getSlot(menuSlot).getItem().isEmpty()) {
			this.minecraft.gameMode.handleContainerInput(containerId, menuSlot, 1, ContainerInput.THROW, player);
		}
	}
}
