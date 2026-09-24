package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.TwilightForestMod;
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

		// getFirstPiece() call is wrapped in an Optional#ofNullable
		//noinspection DataFlowIssue
		return placeContext == null ? null : new LichTowerFoyer(context.structureTemplateManager(), placeContext, true, random.nextBoolean());
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
