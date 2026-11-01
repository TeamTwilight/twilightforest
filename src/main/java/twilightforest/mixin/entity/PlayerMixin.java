package twilightforest.mixin.entity;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asmhooks.PlayerHooks;

@Mixin(Player.class)
public abstract class PlayerMixin {

	/** Bottes du Voyageur : sauter epuise moins. */
	@ModifyArg(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
	private float twilightforest$reduceJumpExhaustion(float exhaustion) {
		return PlayerHooks.getFoodExhaustion(exhaustion, (Player) (Object) this);
	}
}
