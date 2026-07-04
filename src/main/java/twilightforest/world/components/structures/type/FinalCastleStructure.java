package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFBiomeTags;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.finalcastle.FinalCastleMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class FinalCastleStructure extends ControlledSpawningStructure {
	public static final MapCodec<FinalCastleStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, FinalCastleStructure::new)
	);

	public FinalCastleStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new FinalCastleMainComponent(0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.FINAL_CASTLE.get();
	}

	public static FinalCastleStructure buildFinalCastleConfig(BootstrapContext<Structure> context) {
		return new FinalCastleStructure( // TODO Re-enable mob spawns when proper castle mobs are created
			ControlledSpawningConfig.create(List.of(WeightedList.<MobSpawnSettings.SpawnerData>builder()
				// plain parts of the castle, like the tower maze
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 2), 10)
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 1, 1), 10)
				//.add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1), 10)
				.build()
			, WeightedList.<MobSpawnSettings.SpawnerData>builder()
				// internal castle
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 2), 10)
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 1, 1), 10)
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT.get(), 1, 1), 10)
				.build()
			, WeightedList.<MobSpawnSettings.SpawnerData>builder()
				// dungeons
				//.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
				.build()
			, WeightedList.<MobSpawnSettings.SpawnerData>builder()
				// forge
				//.add(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 1, 1), 10)
				.build()
			), WeightedList.of(), WeightedList.of()),
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_troll"))),
			// TODO: change this when we make a book for the castle
			Optional.of(new HintConfig(HintConfig.defaultBook(), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(4, false, true, false)),
			true, Optional.of(TFMapDecorations.FINAL_CASTLE),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_FINAL_CASTLE_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.BEARD_BOX
			)
		);
	}
}
