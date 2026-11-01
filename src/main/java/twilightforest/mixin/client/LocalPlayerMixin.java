package twilightforest.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.EntityHooks;

import java.util.function.BiPredicate;

/**
 * Le Debride court et nage librement dans l'eau.
 *
 * <p>{@code aiStep} teste plusieurs fois si le joueur est dans un liquide, pour decider s'il peut
 * sprinter ou s'il doit nager. Les deux crochets couvrent tous ces tests.</p>
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
	private boolean twilightforest$unrestrainedSprintingInWater(LocalPlayer self) {
		return EntityHooks.unrestrainedSprintingInWater(self.isInWater(), self);
	}

	@ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInFluidType(Ljava/util/function/BiPredicate;)Z"))
	private BiPredicate<FluidType, Double> twilightforest$unrestrainedSwimPredicate(BiPredicate<FluidType, Double> predicate) {
		return EntityHooks.unrestrainedSwimPredicate(predicate, (LocalPlayer) (Object) this);
	}
}
