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
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.icetower.IceTowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuroraPalaceStructure extends ControlledSpawningStructure {
	public static final MapCodec<AuroraPalaceStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, AuroraPalaceStructure::new)
	);

	public AuroraPalaceStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new IceTowerMainComponent(random, 0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.AURORA_PALACE.get();
	}

	public static AuroraPalaceStructure buildAuroraPalaceConfig(BootstrapContext<Structure> context) {
		return new AuroraPalaceStructure(
			ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
				.add(new MobSpawnSettings.SpawnerData(TFEntities.SNOW_GUARDIAN.get(), 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.STABLE_ICE_CORE.get(), 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.UNSTABLE_ICE_CORE.get(), 1, 2), 5)
				.build()
			),
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_yeti"))),
			Optional.of(new HintConfig(HintConfig.book("icetower", 3), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(2, false, true, false)),
			true, Optional.of(TFMapDecorations.AURORA_PALACE),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_AURORA_PALACE_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.of()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.NONE
			)
		);
	}
}
