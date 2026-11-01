package twilightforest.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.BlockHooks;

@Mixin(SlimeBlock.class)
public abstract class SlimeBlockMixin {

	/** Les bottes de gelee coupent le rebond. */
	@Inject(method = "bounceUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	private void twilightforest$stopBouncing(Entity entity, CallbackInfo ci) {
		BlockHooks.stopBouncing(entity);
	}

	/** Le Debride garde son elan sur la gelee, meme accroupi. */
	@Redirect(method = "stepOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSteppingCarefully()Z"))
	private boolean twilightforest$resetSlimeMomentum(Entity entity) {
		return BlockHooks.resetSlimeMomentumWithUnrestrained(entity.isSteppingCarefully(), entity);
	}
}
