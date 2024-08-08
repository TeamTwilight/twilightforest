package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.commons.lang3.StringUtils;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;

public final class LichTowerBase extends TwilightJigsawPiece implements PieceBeardifierModifier {
	public LichTowerBase(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_BASE.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	public LichTowerBase(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_TOWER_BASE.get(), 1, structureManager, TwilightForestMod.prefix("lich_tower/tower_base"), jigsawContext);

		this.boundingBox = BoundingBoxUtils.cloneWithAdjustments(this.boundingBox, 0, 0, 0, 0, 30,0);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/tower_below" -> LichTowerSegment.buildTowerBySegments(pieceAccessor, random, connection.pos(), connection.orientation(), this, this.structureManager, random.nextInt(9, 11));
			case "twilightforest:lich_tower/bridge" -> LichTowerWingBridge.tryRoomAndBridge(this, pieceAccessor, random, connection, this.structureManager, true, 4, true, this.genDepth + 1, false);
			case "twilightforest:lich_tower/decor" -> {
				ResourceLocation decorId = LichTowerUtil.rollRandomDecor(random, true);
				JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), structureManager, decorId, "twilightforest:lich_tower/decor", random);

				if (placeableJunction != null) {
					StructurePiece decor = new LichTowerRoomDecor(this.genDepth + 1, this.structureManager, decorId, placeableJunction, false);
					pieceAccessor.addPiece(decor);
					decor.addChildren(this, pieceAccessor, random);
				}
			}
		}
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		String[] splitLabel = label.split(":");
		if (splitLabel.length == 2 && "candle".equals(splitLabel[0]) && StringUtils.isNumeric(splitLabel[1])) {
			level.removeBlock(pos, false); // Clears block entity data left by Data Marker

			boolean majorCandle = Integer.parseInt(splitLabel[1]) == 2;

			if (!majorCandle && random.nextInt(3) != 0) {
				return;
			}

			int candleCount = majorCandle ? 3 : 1 + random.nextInt(2);
			BlockState candleBlock = Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.LIT, true).setValue(CandleBlock.CANDLES, candleCount);

			level.setBlock(pos, candleBlock, 3);
		}
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BEARD_BOX;
	}

	@Override
	public int getGroundLevelDelta() {
		return 1;
	}
}
