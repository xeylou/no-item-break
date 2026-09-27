package xeylou.noitembreak;

import net.minecraft.world.item.ItemStack;

// lives outside the mixin package: classes there cannot be referenced directly.
public final class WornItems {

	public static final int THRESHOLD = 10;

	private WornItems() {
	}

	// isDamageableItem() excludes items flagged Unbreakable & items without durability.
	public static boolean isWorn(ItemStack stack) {
		return stack.isDamageableItem() && stack.getMaxDamage() - stack.getDamageValue() <= THRESHOLD;
	}
}
