package twilightforest.mixin.entity;

import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.EntityHooks;

@Mixin(PathfinderMob.class)
public abstract class PathfinderMobMixin {

	/** Une bete tenue en laisse par une corde du mod peut s'eloigner de son piquet. */
	@Inject(method = "shouldStayCloseToLeashHolder", at = @At("RETURN"), cancellable = true)
	private void twilightforest$overrideStayCloseToHolder(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(EntityHooks.overrideStayCloseToHolder(cir.getReturnValueZ(), (PathfinderMob) (Object) this));
	}
}
