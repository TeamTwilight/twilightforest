package twilightforest.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

@Mixin(Level.class)
public abstract class LevelMixin {

	/** Sous la canopee de la Foret Crepusculaire, il ne pleut pas. */
	@Inject(method = "isRainingAt", at = @At("RETURN"), cancellable = true)
	private void twilightforest$isRainingAt(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BlockHooks.isRainingAt(cir.getReturnValueZ(), (Level) (Object) this, pos));
	}
}
