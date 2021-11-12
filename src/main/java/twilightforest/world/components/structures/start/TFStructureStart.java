package twilightforest.world.components.structures.start;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.components.structures.TFStructureComponentTemplate;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFStructures;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TFStructureStart<C extends FeatureConfiguration> extends StructureFeature<C> {

	private final TFFeature feature;
	private final boolean template;

	public TFStructureStart(Codec<C> codec, TFFeature feature) {
		this(codec, feature, false);
	}

	public TFStructureStart(Codec<C> codec, TFFeature feature, boolean template) {
		super(codec);
		this.feature = feature;
		this.template = template;
	}

	public TFFeature getFeature() {
		return feature;
	}

	@Override
	public boolean getDefaultRestrictsSpawnsToInside() {
		return true;
	}

	@Override
	public GenerationStep.Decoration step() {
		return getFeature().getDecorationStage();
	}

	@Override
	public StructureStartFactory<C> getStartFactory() {
		return template ? TemplateStart::new : Start::new;
	}

	private StructureStart<C> createStructureStart(ChunkPos pos, int refCount, long seed) {
		return this.getStartFactory().create(this, pos, refCount, seed);
	}

	@Override
	public StructureStart<?> generate(RegistryAccess dynamicRegistries, ChunkGenerator generator, BiomeSource provider, StructureManager templateManager, long seed, ChunkPos pos, Biome biome, int refCount, WorldgenRandom rand, StructureFeatureConfiguration settings, C config, LevelHeightAccessor accessor) {
		ChunkPos chunkpos = this.getPotentialFeatureChunk(settings, seed, rand, pos.x, pos.z);
		if (this.isFeatureChunk(generator, provider, seed, rand, pos, biome, chunkpos, config, accessor)) {
			StructureStart<C> structurestart = this.createStructureStart(pos, refCount, seed);
			structurestart.generatePieces(dynamicRegistries, generator, templateManager, pos, biome, config, accessor);
			if (structurestart.isValid()) {
				return structurestart;
			}
		}

		return StructureStart.INVALID_START;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource provider, long seed, WorldgenRandom random, ChunkPos pos, Biome biome, ChunkPos structurePos, C config, LevelHeightAccessor accessor) {
		return TFFeature.isInFeatureChunk(pos.x << 4, pos.z << 4) && TFFeature.generateFeature(pos.x, pos.z, biome, seed) == feature;
	}

	private static int getSpawnListIndexAt(StructureStart<?> start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos)) {
				if (component instanceof TFStructureComponent) {
					TFStructureComponent tfComponent = (TFStructureComponent) component;
					if (tfComponent.spawnListIndex > highestFoundIndex)
						highestFoundIndex = tfComponent.spawnListIndex;
				} else
					return 0;
			}
		}
		return highestFoundIndex;
	}

	@Nullable
	public static List<MobSpawnSettings.SpawnerData> gatherPotentialSpawns(StructureFeatureManager structureManager, MobCategory classification, BlockPos pos) {
		for (StructureFeature<?> structure : TFStructures.SEPARATION_SETTINGS.keySet()) {
			StructureStart<?> start = structureManager.getStructureAt(pos, true, structure);
			if (!start.isValid())
				continue;
			TFFeature feature = ((TFStructureStart<?>) structure).feature;
			if (classification != MobCategory.MONSTER)
				return feature.getSpawnableList(classification);
			if (start instanceof TFStructureStart.Start s && s.conquered)
				return null;
			final int index = getSpawnListIndexAt(start, pos);
			if (index < 0)
				return null;
			return feature.getSpawnableMonsterList(index);
		}
		return null;
	}

	public class Start extends StructureStart<C> {

		private boolean conquered;

		public Start(StructureFeature<C> p_i225876_1_, ChunkPos p_i225876_2_, int p_i225876_3_, long p_i225876_4_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_);
		}

		@Override
		public CompoundTag createTag(ServerLevel pLevel, ChunkPos pChunkPos) {
			CompoundTag tag = super.createTag(pLevel, pChunkPos);
			if (isValid())
				tag.putBoolean("conquered", conquered);
			return tag;
		}

		public void load(CompoundTag nbt) {
			conquered = nbt.getBoolean("conquered");
		}

		public final void setConquered(boolean flag) {
			conquered = flag;
		}

		public final boolean isConquered() {
			return conquered;
		}

		@Override
		public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, C config, LevelHeightAccessor levelHeightAccessor) {
			boolean dontCenter = feature == TFFeature.LICH_TOWER || feature == TFFeature.TROLL_CAVE || feature == TFFeature.YETI_CAVE;
			int x = (chunkPos.x << 4) + (dontCenter ? 0 : 7);
			int z = (chunkPos.z << 4) + (dontCenter ? 0 : 7);
			int y = feature.shouldAdjustToTerrain() ? Math.max(chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, levelHeightAccessor), chunkGenerator.getSeaLevel() + 1) : chunkGenerator.getSeaLevel();
			StructurePiece start = feature.provideStructureStart(structureManager, chunkGenerator, random, x, y, z);
			if(start == null)
				return;
			this.addPiece(start);
			start.addChildren(start, this, random);
			createBoundingBox();
		}
	}

	private class TemplateStart extends Start {

		public TemplateStart(StructureFeature<C> p_i225876_1_, ChunkPos p_i225876_2_, int p_i225876_3_, long p_i225876_4_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_);
		}

		@Override
		public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, C config, LevelHeightAccessor levelHeightAccessor) {
			super.generatePieces(registryAccess, chunkGenerator, structureManager, chunkPos, biome, config, levelHeightAccessor);
			pieces.stream().filter(piece -> piece instanceof TFStructureComponentTemplate).map(TFStructureComponentTemplate.class::cast).forEach(piece -> piece.setup(structureManager));
			createBoundingBox();
		}

		@Override
		public void placeInChunk(WorldGenLevel p_230366_1_, StructureFeatureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, BoundingBox p_230366_5_, ChunkPos p_230366_6_) {
			pieces.stream().filter(TFStructureComponentTemplate.class::isInstance).map(TFStructureComponentTemplate.class::cast).filter(component -> component.LAZY_TEMPLATE_LOADER != null).
					forEach(component -> component.LAZY_TEMPLATE_LOADER.run());
			super.placeInChunk(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
		}

	}
}