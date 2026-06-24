package twilightforest.asm;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer;
import twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer;
import twilightforest.asm.transformers.armor.CancelElytraRenderingTransformer;
import twilightforest.asm.transformers.armor.FixCapeUnrenderingTransformer;
import twilightforest.asm.transformers.beardifier.BeardifierClassTransformer;
import twilightforest.asm.transformers.beardifier.BeardifierComputeTransformer;
import twilightforest.asm.transformers.beardifier.InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer;
import twilightforest.asm.transformers.block.SlimeBlockBounceUpTransformer;
import twilightforest.asm.transformers.block.SlimeBlockMomentumTransformer;
import twilightforest.asm.transformers.block.UnrestrainedFrictionTransformer;
import twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer;
import twilightforest.asm.transformers.chunk.ChunkStatusTaskTransformer;
import twilightforest.asm.transformers.cloud.IsRainingAtTransformer;
import twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer;
import twilightforest.asm.transformers.damagesources.DamageSourcesTransformer;
import twilightforest.asm.transformers.entity.PathFinderUnrestrainedByLeashTransformer;
import twilightforest.asm.transformers.entity.ResetStuckUnrestrainedTransformer;
import twilightforest.asm.transformers.entity.UnrestrainedBlockSpeedAndJumpFactorTransformer;
import twilightforest.asm.transformers.entity.WaterSprintTransformer;
import twilightforest.asm.transformers.entity.WaterWalkTransformer;
import twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer;
import twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer;
import twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer;
import twilightforest.asm.transformers.map.UpdateMapsInGogglesTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer;
import twilightforest.asm.transformers.multipart.SendDirtyEntityDataTransformer;
import twilightforest.asm.transformers.player.GetFieldOfViewModifierTransformer;
import twilightforest.asm.transformers.player.ReduceMovementFoodExhaustionTransformer;
import twilightforest.asm.transformers.shroom.ModifySoilDecisionForMushroomBlockSurvivabilityTransformer;
import twilightforest.asm.transformers.snow.KeepGrassSnowyForSnowloggableBlocksTransformer;

import java.util.List;

public class TFCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(
			// armor
			new ArmorVisibilityRenderingTransformer(),
			new CancelArmorRenderingTransformer(),
			new CancelElytraRenderingTransformer(),
			new FixCapeUnrenderingTransformer(),

			// beardifier
			new BeardifierClassTransformer(),
			new BeardifierComputeTransformer(),
			new InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer(),

			// book
			new ModifyWrittenBookNameTransformer(),

			//block
			new SlimeBlockMomentumTransformer(),
			new SlimeBlockBounceUpTransformer(),
			new UnrestrainedFrictionTransformer(),

			// chunk
			new ChunkStatusTaskTransformer(),

			// cloud
			new IsRainingAtTransformer(),

			// conquered
			new StructureStartLoadStaticTransformer(),

			// damagesources
			new DamageSourcesTransformer(),

			// entity
			new WaterWalkTransformer(),
			new WaterSprintTransformer(),
			new PathFinderUnrestrainedByLeashTransformer(),
			new UnrestrainedBlockSpeedAndJumpFactorTransformer(),
			new ResetStuckUnrestrainedTransformer(),

			// foliage
			new FoliageColorResolverTransformer(),

			// lead
			new LeashFenceKnotSurvivesTransformer(),

			// map
			new ResolveNearestNonRandomSpreadMapStructureTransformer(),
			new UpdateMapsInGogglesTransformer(),

			// multipart
			new ResolveEntitiesForRendereringTransformer(),
			new ResolveEntityRendererTransformer(),
			new SendDirtyEntityDataTransformer(),

			// player
			new GetFieldOfViewModifierTransformer(),
			new ReduceMovementFoodExhaustionTransformer(),

			// shroom
			new ModifySoilDecisionForMushroomBlockSurvivabilityTransformer(),

			//snow
			new KeepGrassSnowyForSnowloggableBlocksTransformer()
		);
	}
}