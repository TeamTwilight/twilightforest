package twilightforest.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.BlockHooks;
import net.minecraft.world.entity.ExperienceOrb;

/**
 * Idem pour les orbes d'experience.
 *
 * <p>NeoForge laisse detourner le frottement sur l'interface {@code IBlockExtension} elle-meme ;
 * la version de Mixin livree avec Forge refuse d'injecter dans une interface. On detourne donc
 * chaque appel que le jeu fait reellement, ce qui couvre exactement les memes cas.</p>
 */
@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
	private float twilightforest$resetBlockFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return BlockHooks.resetBlockFrictionWithUnrestrained(state.getFriction(level, pos, entity), entity);
	}
}
