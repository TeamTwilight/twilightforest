package twilightforest.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.BlockHooks;
import twilightforest.forge.TriState;

@Mixin(MushroomBlock.class)
public abstract class MushroomBlockMixin {

	/**
	 * Les champignons poussent sur les sols de la Foret Crepusculaire.
	 *
	 * <p>NeoForge fait repondre {@code canSustainPlant} par un TriState ; Forge par un booleen.
	 * Le crochet du mod raisonne en TriState, on traduit donc dans les deux sens — « pas d'avis »
	 * redonne la reponse du jeu.</p>
	 */
	@Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canSustainPlant(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraftforge/common/IPlantable;)Z"))
	private boolean twilightforest$modifySoilDecision(BlockState soil, net.minecraft.world.level.BlockGetter getter, BlockPos soilPos, net.minecraft.core.Direction facing, net.minecraftforge.common.IPlantable plantable, BlockState state, LevelReader level, BlockPos pos) {
		boolean vanilla = soil.canSustainPlant(getter, soilPos, facing, plantable);
		return BlockHooks.modifySoilDecisionForMushroomBlockSurvivability(TriState.of(vanilla), level, pos).toBoolean(vanilla);
	}
}
