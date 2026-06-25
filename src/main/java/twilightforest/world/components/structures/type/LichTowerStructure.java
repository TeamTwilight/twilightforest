package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFBiomeTags;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.WorldUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.world.components.chunkgenerators.BoxDensityFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerBaseTrim;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerFoyer;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerWingBeard;
import twilightforest.world.components.structures.lichtowerrevamp.LichYardBox;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.*;
import java.util.stream.Collectors;

public class LichTowerStructure extends ControlledSpawningStructure implements CustomDensitySource {
	public static final MapCodec<LichTowerStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, LichTowerStructure::new)
	);

	public LichTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		Direction direction = Rotation.getRandom(random).rotate(Direction.SOUTH);
		BlockPos placePos = new BlockPos(x, y, z).relative(direction, 24);
		FrontAndTop oriented = FrontAndTop.fromFrontAndTop(Direction.UP, direction);

		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(placePos, BlockPos.ZERO, oriented, context.structureTemplateManager(), TwilightForestMod.prefix("lich_tower/tower_foyer"), "twilightforest:lich_tower/vestibule", random);

		if (placeContext == null)
			return null;

		// getFirstPiece() call is wrapped in an Optional#ofNullable
		//noinspection DataFlowIssue
		return new LichTowerFoyer(context.structureTemplateManager(), placeContext, true, random.nextBoolean());
	}

	@Override
	protected void generateFromStartingPiece(StructurePiece startingPiece, GenerationContext context, StructurePiecesBuilder structurePiecesBuilder) {
		structurePiecesBuilder.addPiece(startingPiece);
		if (startingPiece instanceof TwilightJigsawPiece jigsaw) {
			jigsaw.addJigsaws(jigsaw, structurePiecesBuilder, context);
		} else {
			startingPiece.addChildren(startingPiece, structurePiecesBuilder, context.random());
		}

		if (startingPiece instanceof LichTowerFoyer foyerPiece) {
			LichYardBox.beginYard(foyerPiece, context, structurePiecesBuilder);
		}
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.LICH_TOWER.get();
	}

	@SuppressWarnings("unchecked")
	public static LichTowerStructure buildLichTowerConfig(BootstrapContext<Structure> context) {
		final ControlledSpawningConfig monsters;
		WeightedList<MobSpawnSettings.SpawnerData> yardSpawns = WeightedList.<MobSpawnSettings.SpawnerData>builder()
			.add(new MobSpawnSettings.SpawnerData(TFEntities.RISING_ZOMBIE.value(), 1, 2), 2)
			.build();
		WeightedList<MobSpawnSettings.SpawnerData> interiorSpawns = WeightedList.<MobSpawnSettings.SpawnerData>builder()
			.add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2), 10)
			.add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2), 10)
			.add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1), 1)
			.add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 1)
			.add(new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.value(), 2, 3), 10)
			.add(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1), 1)
			.build();
		monsters = ControlledSpawningConfig.justMonsters(
			yardSpawns,
			interiorSpawns
		);
		return new LichTowerStructure(
			monsters,
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
			Optional.of(new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(0, false, true, false, true)),
			true, Optional.of(TFMapDecorations.LICH_TOWER),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_LICH_TOWER_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.BEARD_THIN
			)
		);
	}

	@Override
	public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
		List<BoundingBox> trimBoxes = new ArrayList<>();

		int yBase = structurePieceSource.getPieces().getFirst().getBoundingBox().minY();

		DensityFunction activator = DensityFunctions.yClampedGradient(yBase - 2, yBase - 1, 1, 0);

		for (var piece : structurePieceSource.getPieces()) {
			if (piece instanceof LichTowerFoyer || piece instanceof LichTowerBaseTrim || (piece instanceof LichTowerWingBeard beard && beard.isTrim())) {
				trimBoxes.add(piece.getBoundingBox());
			} else if (piece instanceof LichYardBox yard && yard.getTerrainAdjustment() != TerrainAdjustment.NONE) {
				trimBoxes.add(piece.getBoundingBox().moved(0, -5, 0));
			}
		}

		return DensityFunctions.mul(activator, BoxDensityFunction.combine(trimBoxes, -5, -5, TerrainAdjustment.BURY));
	}

	@Override
	public int adjustForTerrain(GenerationContext context, int x, int z) {
		return WorldUtil.adjustForTerrain(context, x, z, 32, 4);
	}
}
