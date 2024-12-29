package twilightforest.asm;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer;
import twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer;
import twilightforest.asm.transformers.beardifier.BeardifierClassTransformer;
import twilightforest.asm.transformers.beardifier.BeardifierComputeTransformer;
import twilightforest.asm.transformers.beardifier.InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer;
import twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer;
import twilightforest.asm.transformers.chunk.ChunkStatusTaskTransformer;
import twilightforest.asm.transformers.cloud.IsRainingAtTransformer;
import twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer;
import twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer;
import twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer;
import twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer;
import twilightforest.asm.transformers.multipart.SendDirtytEntityDataTransformer;
import twilightforest.asm.transformers.shroom.ModifySoilDecisionForMushroomBlockSurvivabilityTransformer;

import java.util.List;

public class TFCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(
			// armor
			new ArmorVisibilityRenderingTransformer(),
			new CancelArmorRenderingTransformer(),

			// beardifier
			new BeardifierClassTransformer(),
			new BeardifierComputeTransformer(),
			new InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer(),

			// book
			new ModifyWrittenBookNameTransformer(),

			// chunk
			new ChunkStatusTaskTransformer(),

			// cloud
			new IsRainingAtTransformer(),

			// conquered
			new StructureStartLoadStaticTransformer(),

			// foliage
			new FoliageColorResolverTransformer(),

			// lead
			new LeashFenceKnotSurvivesTransformer(),

			// map
			new ResolveNearestNonRandomSpreadMapStructureTransformer(),

			// multipart
			new ResolveEntitiesForRendereringTransformer(),
			new ResolveEntityRendererTransformer(),
			new SendDirtytEntityDataTransformer(),

			// shroom
			new ModifySoilDecisionForMushroomBlockSurvivabilityTransformer()
		);
	}
}