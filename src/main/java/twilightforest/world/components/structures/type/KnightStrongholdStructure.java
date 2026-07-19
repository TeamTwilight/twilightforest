package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFBiomeTags;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.stronghold.StrongholdEntranceComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KnightStrongholdStructure extends ControlledSpawningStructure {
	public static final MapCodec<KnightStrongholdStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, KnightStrongholdStructure::new)
	);

	public KnightStrongholdStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new StrongholdEntranceComponent(0, x, y + random.nextInt(3) == 0 ? 5 : 1, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.KNIGHT_STRONGHOLD.get();
	}

	public static KnightStrongholdStructure buildKnightStrongholdConfig(BootstrapContext<Structure> context) {
		return new KnightStrongholdStructure(
			ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
				.add(new MobSpawnSettings.SpawnerData(TFEntities.BLOCKCHAIN_GOBLIN.get(), 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.LOWER_GOBLIN_KNIGHT.get(), 1, 2), 5)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.HELMET_CRAB.get(), 2, 4), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 2, 3), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.REDCAP_SAPPER.get(), 1, 2), 2)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 2, 4), 10)
				.add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 2), 5)
				.add(new MobSpawnSettings.SpawnerData(EntityType.SLIME, 4, 4), 5)
				.build()
			),
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_trophy_pedestal"))),
			Optional.of(new HintConfig(HintConfig.book("tfstronghold", 4), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(3, true, false, false)),
			true, Optional.of(TFMapDecorations.KNIGHT_STRONGHOLD),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_KNIGHT_STRONGHOLD_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
				TerrainAdjustment.BURY
			)
		);
	}

	@Override
	protected StructureStart createStart(ChunkPos chunkPos, int reference, GenerationStub generationStub) {
		KnightStructureStart start = new KnightStructureStart(this, chunkPos, reference, generationStub.getPiecesBuilder().build());
		start.setStartY(generationStub.position().getY() - 1);
		return start;
	}

	public static class KnightStructureStart extends TFStructureStart {
		private int startY = 0;

		public KnightStructureStart(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieces) {
			super(structure, chunkPos, references, pieces);
		}

		public void setStartY(int startY) {
			this.startY = startY;
		}

		@Override
		public BoundingBox getBoundingBox() {
			BoundingBox boundingbox = this.cachedBoundingBox;
			if (boundingbox == null) {
				BoundingBox bBox = super.getBoundingBox();
				boundingbox = new BoundingBox(bBox.minX(), bBox.minY(), bBox.minZ(), bBox.maxX(), Math.min(bBox.maxY(), this.startY), bBox.maxZ());
				this.cachedBoundingBox = boundingbox; // Cache that shit since it may get called like every tick
			}
			return boundingbox;
		}

		@Override
		public CompoundTag createTag(StructurePieceSerializationContext level, ChunkPos chunkPos) {
			CompoundTag tag = super.createTag(level, chunkPos);
			tag.putInt("knight_y", this.startY);
			return tag;
		}

		@Override
		public void loadFromTag(CompoundTag nbt) {
			super.loadFromTag(nbt);
			this.startY = nbt.getIntOr("knight_y", 0);
		}
	}
}
