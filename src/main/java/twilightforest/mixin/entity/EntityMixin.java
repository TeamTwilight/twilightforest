package twilightforest.mixin.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.EntityHooks;

@Mixin(Entity.class)
public abstract class EntityMixin {

	/** Le Debride ignore le ralentissement des blocs (toiles, baies…) et leur elan de saut. */
	@Inject(method = {"getBlockJumpFactor", "getBlockSpeedFactor"}, at = @At("RETURN"), cancellable = true)
	private void twilightforest$resetFactorWithUnrestrained(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(EntityHooks.resetFactorWithUnrestrained(cir.getReturnValueF(), (Entity) (Object) this));
	}

	/**
	 * Idem pour l'englumement (« stuck »).
	 *
	 * <p>Le coremod remettait le multiplicateur a zero devant chaque lecture du champ ; rien ne
	 * l'ecrit entre l'entree de la methode et la premiere lecture, donc le faire a l'entree revient
	 * au meme.</p>
	 */
	@Inject(method = "move", at = @At("HEAD"))
	private void twilightforest$resetStuckUnrestrained(MoverType type, Vec3 movement, CallbackInfo ci) {
		EntityHooks.resetStuckUnrestrained((Entity) (Object) this);
	}
}
