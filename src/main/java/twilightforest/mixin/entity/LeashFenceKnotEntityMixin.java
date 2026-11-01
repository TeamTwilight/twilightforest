package twilightforest.mixin.entity;

import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashFenceKnotEntityMixin {

	/** Les noeuds de laisse survivent aussi sur les barrieres du mod. */
	@Inject(method = "survives", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
	private void twilightforest$leashFenceKnotSurvives(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BlockHooks.leashFenceKnotSurvives(cir.getReturnValueZ(), (LeashFenceKnotEntity) (Object) this));
	}
}
