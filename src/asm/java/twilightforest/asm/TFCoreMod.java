package twilightforest.asm;

import net.neoforged.neoforgespi.transformation.ClassProcessorProvider;
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

public class TFCoreMod implements ClassProcessorProvider {

	@Override
	public void createProcessors(Context context, Collector collector) {
		// armor
		collector.add(new ArmorVisibilityRenderingTransformer());
		collector.add(new CancelArmorRenderingTransformer());
		collector.add(new CancelElytraRenderingTransformer());
		collector.add(new FixCapeUnrenderingTransformer());

		// beardifier
		collector.add(new BeardifierClassTransformer());
		collector.add(new BeardifierComputeTransformer());
		collector.add(new InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer());

		// book
		collector.add(new ModifyWrittenBookNameTransformer());

		//block
		collector.add(new SlimeBlockMomentumTransformer());
		collector.add(new SlimeBlockBounceUpTransformer());
		collector.add(new UnrestrainedFrictionTransformer());

		// chunk
		collector.add(new ChunkStatusTaskTransformer());

		// cloud
		collector.add(new IsRainingAtTransformer());

		// conquered
		collector.add(new StructureStartLoadStaticTransformer());

		// damagesources
		collector.add(new DamageSourcesTransformer());

		// entity
		collector.add(new WaterWalkTransformer());
		collector.add(new WaterSprintTransformer());
		collector.add(new PathFinderUnrestrainedByLeashTransformer());
		collector.add(new UnrestrainedBlockSpeedAndJumpFactorTransformer());
		collector.add(new ResetStuckUnrestrainedTransformer());

		// foliage
		collector.add(new FoliageColorResolverTransformer());

		// lead
		collector.add(new LeashFenceKnotSurvivesTransformer());

		// map
		collector.add(new ResolveNearestNonRandomSpreadMapStructureTransformer());
		collector.add(new UpdateMapsInGogglesTransformer());

		// multipart
		collector.add(new ResolveEntitiesForRendereringTransformer());
		collector.add(new ResolveEntityRendererTransformer());
		collector.add(new SendDirtyEntityDataTransformer());

		// player
		collector.add(new GetFieldOfViewModifierTransformer());
		collector.add(new ReduceMovementFoodExhaustionTransformer());

		// shroom
		collector.add(new ModifySoilDecisionForMushroomBlockSurvivabilityTransformer());

		//snow
		collector.add(new KeepGrassSnowyForSnowloggableBlocksTransformer());
	}
}