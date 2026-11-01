package twilightforest.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.ArmorHooks;
import twilightforest.asmhooks.BlockHooks;
import twilightforest.asmhooks.EntityHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract float getArmorCoverPercentage();

	/** Marcher sur l'eau (bottes du Voyageur). */
	@Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
	private void twilightforest$processWaterWalking(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(EntityHooks.processWaterWalking(cir.getReturnValueZ(), (LivingEntity) (Object) this, fluidState));
	}

	/**
	 * Armure voilee : moins on est couvert, moins les monstres voient.
	 *
	 * <p>Le coremod remplacait la valeur juste avant son rangement dans la variable locale ; ici on
	 * intercepte l'appel qui la produit, ce qui revient au meme et ne depend pas de la table des
	 * variables locales.</p>
	 */
	@Redirect(method = "getVisibilityPercent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getArmorCoverPercentage()F"))
	private float twilightforest$modifyArmorVisibility(LivingEntity self) {
		return ArmorHooks.modifyArmorVisibility(this.getArmorCoverPercentage(), self);
	}

	/**
	 * L'effet Debride empeche de glisser sur la glace et la neige.
	 *
	 * <p>NeoForge laisse detourner le frottement sur l'interface {@code IBlockExtension} elle-meme ;
	 * la version de Mixin livree avec Forge refuse d'injecter dans une interface. On detourne donc
	 * chaque appel que le jeu fait reellement, celui-ci etant le seul qui concerne un joueur.</p>
	 */
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
	private float twilightforest$resetBlockFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return BlockHooks.resetBlockFrictionWithUnrestrained(state.getFriction(level, pos, entity), entity);
	}
}
