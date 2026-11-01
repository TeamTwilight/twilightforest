package twilightforest.mixin.entity;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asmhooks.PlayerHooks;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

	/** Idem pour la marche, la nage et le reste des deplacements comptabilises. */
	@ModifyArg(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"))
	private float twilightforest$reduceMovementExhaustion(float exhaustion) {
		return PlayerHooks.getFoodExhaustion(exhaustion, (ServerPlayer) (Object) this);
	}
}
