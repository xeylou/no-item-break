package xeylou.noitembreak.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xeylou.noitembreak.WornItems;

// a worn item can still be held on purpose (mending), but mining or hitting w/ it would wear it further,
// so refuse both before any packet is sent. right-click is left alone: it shares its packet with opening chests,
// doors and trading.
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(
			method = {
					"startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
					"continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
			},
			at = @At("HEAD"),
			cancellable = true
	)
	private void xeylou$refuseMiningWithWornItem(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (this.xeylou$holdsWornItem()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
			method = "attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void xeylou$refuseAttackWithWornItem(Player player, Entity target, CallbackInfo ci) {
		if (this.xeylou$holdsWornItem()) {
			ci.cancel();
		}
	}

	@Unique
	private boolean xeylou$holdsWornItem() {
		Player player = this.minecraft.player;
		return player != null && !player.hasInfiniteMaterials() && WornItems.isWorn(player.getMainHandItem());
	}
}
