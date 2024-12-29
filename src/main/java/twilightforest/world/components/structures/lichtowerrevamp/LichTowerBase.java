package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.SortablePiece;

public final class LichTowerBase extends TwilightJigsawPiece implements PieceBeardifierModifier, SpawnIndexProvider, SortablePiece {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final int casketWingIndex;

	public LichTowerBase(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_BASE.get(), compoundTag, ctx, readSettings(compoundTag));

		this.casketWingIndex = compoundTag.getInt("CasketWingIdx");

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TrimProcessor.INSTANCE));
	}

	public LichTowerBase(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_TOWER_BASE.get(), 1, structureManager, TwilightForestMod.prefix("lich_tower/tower_base"), jigsawContext);

		this.boundingBox = BoundingBoxUtils.cloneWithAdjustments(this.boundingBox, 0, 0, 0, 0, 30,0);
		this.casketWingIndex = this.firstMatchIndex(r -> "twilightforest:lich_tower/bridge".equals(r.target()));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TrimProcessor.INSTANCE));
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putInt("CasketWingIdx", this.casketWingIndex);
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/tower_below" -> LichTowerSegment.buildTowerBySegments(pieceAccessor, random, connection.pos(), connection.orientation(), this, this.structureManager, random.nextIntBetweenInclusive(12, 15));
			case "twilightforest:lich_tower/bridge" -> {
				ResourceLocation room;
				if (jigsawIndex == this.casketWingIndex) {
					room = lichTowerUtil.getKeepsakeCasketRoom();
				} else {
					room = null;
				}
				if (room != null || connection.pos().getY() < 6) {
					LichTowerWingBridge.tryRoomAndBridge(this, pieceAccessor, random, connection, this.structureManager, true, 4, true, this.genDepth + 1, room);
				}
			}
			case "twilightforest:lich_tower/decor" -> {
				ResourceLocation decorId = lichTowerUtil.rollRandomDecor(random, true);
				JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, decorId, "twilightforest:lich_tower/decor", random);

				if (placeableJunction != null) {
					StructurePiece decor = new LichTowerRoomDecor(this.genDepth + 1, this.structureManager, decorId, placeableJunction);
					pieceAccessor.addPiece(decor);
					decor.addChildren(this, pieceAccessor, random);
				}
			}
			case "twilightforest:lich_tower/tower_trim" -> {
				ResourceLocation decorId = TwilightForestMod.prefix("lich_tower/central_trim");
				JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, decorId, "twilightforest:lich_tower/tower_trim", random);

				if (placeableJunction != null) {
					StructurePiece decor = new LichTowerBaseTrim(this.structureManager, placeableJunction);
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

			level.setBlock(pos, candleBlock, Block.UPDATE_ALL);
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

	@Override
	public int getSpawnIndex() {
		return LichTowerPieces.INTERIOR_SPAWNS;
	}

	@Override
	public int getSortKey() {
		return 1;
	}

	private static class TrimProcessor extends StructureProcessor {
		private static final TrimProcessor INSTANCE = new TrimProcessor();

		@Nullable
		@Override
		public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos centerBottom, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
			if (modifiedBlockInfo.state().is(Blocks.POLISHED_ANDESITE_STAIRS) && level.getBlockState(modifiedBlockInfo.pos()).is(BlockTags.STONE_BRICKS)) {
				// Don't replace trim blocks placed by tower wings
				return null;
			}

			return super.process(level, origin, centerBottom, originalBlockInfo, modifiedBlockInfo, settings, template);
		}

		@Override
		protected StructureProcessorType<?> getType() {
			return null; // not serialized
		}
	}
}
