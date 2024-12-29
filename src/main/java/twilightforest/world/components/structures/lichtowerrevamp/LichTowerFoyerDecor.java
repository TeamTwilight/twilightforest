package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.TwilightForestMod;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.components.item.CandelabraData;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.List;

public class LichTowerFoyerDecor extends TwilightJigsawPiece implements PieceBeardifierModifier {
	public LichTowerFoyerDecor(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_FOYER_DECORATION.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public LichTowerFoyerDecor(int genDepth, StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_FOYER_DECORATION.get(), genDepth, structureManager, TwilightForestMod.prefix("lich_tower/foyer_decor"), jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		BlockPos placePos = this.getSourceJigsaw().pos().offset(this.templatePosition);
		if (chunkBounds.isInside(placePos)) {
			Rotation rotation = this.placeSettings.getRotation();

			random.setSeed(placePos.asLong() + placePos.getY());

			int noChest = level.getBlockState(placePos.above()).is(Blocks.CHISELED_STONE_BRICKS) ? -1 : 0;

			switch (random.nextInt(5) + noChest) {
				case 4 -> {
					BlockState chest = Blocks.CHEST.defaultBlockState().rotate(rotation.getRotated(Rotation.CLOCKWISE_180));

					level.setBlock(placePos, chest, Block.UPDATE_ALL);

					if (level.getBlockEntity(placePos) instanceof ChestBlockEntity chestBE) {
						chestBE.setLootTable(TFLootTables.TOWER_FOYER, random.nextLong());
					}

					BlockState chestGap = Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.HALF, Half.TOP).rotate(rotation.getRotated(Rotation.CLOCKWISE_180));
					level.setBlock(placePos.above(), chestGap, Block.UPDATE_ALL);
				}
				case 3 -> {
					BlockState candelabra = TFBlocks.CANDELABRA.value().defaultBlockState().rotate(rotation).setValue(CandelabraBlock.LIGHTING, LightableBlock.Lighting.DIM);

					for (BooleanProperty prop : CandelabraBlock.CANDLES) {
						candelabra = candelabra.setValue(prop, true);
					}

					level.setBlock(placePos, candelabra, Block.UPDATE_ALL);

					if (level.getBlockEntity(placePos) instanceof CandelabraBlockEntity candelabraBE) {
						// Manually set candles instead of calling setCandle() so that method's setBlockState calls won't crash the game
						candelabraBE.setData(new CandelabraData(List.of(Blocks.CANDLE, Blocks.CANDLE, Blocks.CANDLE)));
					}
				}
				default -> {
					BlockState decorBlock = switch (random.nextInt(5)) {
						case 3 -> TFBlocks.SKELETON_SKULL_CANDLE.value().defaultBlockState()
							.setValue(SkullCandleBlock.LIGHTING, LightableBlock.Lighting.NORMAL)
							//.setValue(CandleBlock.CANDLES, random.nextIntBetweenInclusive(1, 3)) TODO Move Skull Candles count to BlockState
							.setValue(SkullCandleBlock.ROTATION, random.nextIntBetweenInclusive(7, 9));
						case 1, 2 -> Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, random.nextIntBetweenInclusive(7, 9));
						default -> Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.CANDLES, random.nextIntBetweenInclusive(1, 3)).setValue(CandleBlock.LIT, true);
					};

					level.setBlock(placePos, decorBlock.rotate(rotation), Block.UPDATE_ALL);
				}
			}
		}

		BlockPos behind = placePos.relative(this.getSourceJigsaw().orientation().front().getOpposite());
		if (chunkBounds.isInside(placePos)) {
			level.getChunk(behind).markPosForPostprocessing(behind);
		}
	}
}
