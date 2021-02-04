package twilightforest.structures.start;

import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentTemplate;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

public class TFStructure<C extends IFeatureConfig> extends Structure<C> {

	private final TFFeature feature;
	private final boolean template;

	public TFStructure(Codec<C> codec, TFFeature feature) {
		this(codec, feature, false);
	}

	public TFStructure(Codec<C> codec, TFFeature feature, boolean template) {
		super(codec);
		this.feature = feature;
		this.template = template;
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return feature.getDecorationStage();
	}

	@Override
	public IStartFactory<C> getStartFactory() {
		return template ? TemplateStart::new : Start::new;
	}

	@Override
	protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos structurePos, C config) {
		return TFFeature.isInFeatureChunk(chunkX << 4, chunkZ << 4) && TFFeature.generateFeature(chunkX, chunkZ, biome, seed) == feature;
	}

	private class Start extends StructureStart<C> {

		public Start(Structure<C> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		@Override
		public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, C p_230364_7_) {
			int x = (p_230364_4_ << 4);
			int z = (p_230364_5_ << 4);
			int y = feature.useSurfaceHeight ? p_230364_2_.getHeight(p_230364_4_, p_230364_5_, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES) : TFGenerationSettings.SEALEVEL + 1;
			StructurePiece start = feature.provideStructureStart(rand, x, y, z);
			if(start == null)
				return;
			components.add(start);
			start.buildComponent(start, components, rand);
			recalculateStructureSize();
		}
	}

	private class TemplateStart extends Start {

		public TemplateStart(Structure<C> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		@Override
		public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, C p_230364_7_) {
			super.func_230364_a_(p_230364_1_, p_230364_2_, p_230364_3_, p_230364_4_, p_230364_5_, p_230364_6_, p_230364_7_);
			components.stream().filter(piece -> piece instanceof StructureTFComponentTemplate).map(StructureTFComponentTemplate.class::cast).forEach(piece -> piece.setup(p_230364_3_));
			recalculateStructureSize();
		}

		@Override
		public void func_230366_a_(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_) {
			components.stream().filter(StructureTFComponentTemplate.class::isInstance).map(StructureTFComponentTemplate.class::cast).filter(component -> component.LAZY_TEMPLATE_LOADER != null).
					forEach(component -> component.LAZY_TEMPLATE_LOADER.run());
			super.func_230366_a_(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
		}

	}
}