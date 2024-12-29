package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.beans.Autowired;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.SortablePiece;

import java.util.List;

public final class LichTowerWingBridge extends TwilightJigsawPiece implements PieceBeardifierModifier, SortablePiece {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final boolean fromCentral;

	public LichTowerWingBridge(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_BRIDGE.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
		this.fromCentral = compoundTag.getBoolean("from_central");
	}

	public LichTowerWingBridge(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, ResourceLocation templateLocation, boolean fromCentral) {
		super(TFStructurePieceTypes.LICH_WING_BRIDGE.get(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
		this.fromCentral = fromCentral;
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord record, int jigsawIndex) {
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		if (this.fromCentral) {
			JigsawRecord sourceJigsaw = this.getSourceJigsaw();
			BlockPos sourcePos = this.templatePosition.offset(sourceJigsaw.pos());
			BlockPos leftPos = sourcePos.relative(sourceJigsaw.orientation().front().getClockWise(Direction.Axis.Y));
			BlockPos rightPos = sourcePos.relative(sourceJigsaw.orientation().front().getCounterClockWise(Direction.Axis.Y));

			removeIfBanister(level, leftPos, chunkBounds);
			removeIfBanister(level, leftPos.above(), chunkBounds);
			removeIfBanister(level, rightPos, chunkBounds);
			removeIfBanister(level, rightPos.below(), chunkBounds);
		}
	}

	private static void removeIfBanister(WorldGenLevel level, BlockPos pos, BoundingBox chunkBounds) {
		if (chunkBounds.isInside(pos)) {
			if (level.getBlockState(pos).is(BlockTagGenerator.BANISTERS)) {
				level.removeBlock(pos, false);
			}
		}
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
		return 1;
	}

	public static void tryRoomAndBridge(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, StructureTemplateManager structureManager, boolean fromCentralTower, int roomMaxSize, boolean generateGround, int newDepth, @Nullable ResourceLocation override) {
		if (!generateGround) {
			if (fromCentralTower) {
				for (ResourceLocation bridgeId : lichTowerUtil.shuffledCenterBridges(random)) {
					if (tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, true, roomMaxSize, false, newDepth, bridgeId, true, override, false)) {
						return;
					}
				}
			} else {
				for (ResourceLocation bridgeId : lichTowerUtil.shuffledRoomBridges(random)) {
					if (tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, false, roomMaxSize, false, newDepth, bridgeId, false, override, false)) {
						return;
					}
				}
				for (ResourceLocation bridgeId : lichTowerUtil.shuffledEndBridges(random)) {
					if (tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, false, 0, false, newDepth, bridgeId, false, override, true)) {
						return;
					}
				}
			}
		}

		if (fromCentralTower) {
			tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, true, roomMaxSize, generateGround, newDepth, lichTowerUtil.getEnclosedCentralBridge(), true, override, false);
		} else if (!tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, false, roomMaxSize, generateGround, newDepth, lichTowerUtil.getDirectRoomAttachment(), true, override, true)) {
			// This here is reached only if a room was not successfully generated - now a wall must be placed to cover where the bridge would have been
			putCover(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, generateGround, newDepth);
		}
	}

	private static boolean tryBridge(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, boolean fromCentralTower, int roomMaxSize, boolean generateGround, int newDepth, ResourceLocation bridgeId, boolean allowClipping, @Nullable ResourceLocation override, boolean tiny) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), sourceJigsawPos, sourceOrientation, structureManager, bridgeId, fromCentralTower ? "twilightforest:lich_tower/bridge_center" : "twilightforest:lich_tower/bridge", random);

		if (placeableJunction != null) {
			LichTowerWingBridge bridge = new LichTowerWingBridge(structureManager, newDepth, placeableJunction, bridgeId, fromCentralTower);

			if ((allowClipping || pieceAccessor.findCollisionPiece(bridge.boundingBox) == null) && bridge.tryGenerateRoom(random, pieceAccessor, roomMaxSize, generateGround, override, tiny)) {
				// If the bridge & room can be fitted, then also add bridge to list then exit this function
				pieceAccessor.addPiece(bridge);
				bridge.addChildren(parent, pieceAccessor, random);
				return true;
			}
		}
		return false;
	}

	public static void putCover(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, boolean noWindow, int newDepth) {
		boolean onlyCobbleStopper = noWindow || pieceAccessor.findCollisionPiece(BoundingBox.fromCorners(sourceJigsawPos.relative(sourceOrientation.front(), 1), sourceJigsawPos.relative(sourceOrientation.front(), 3))) != null;
		ResourceLocation bridgeCoverLocation = onlyCobbleStopper ? lichTowerUtil.getDefaultBridgeStopper() : lichTowerUtil.rollRandomCover(random);
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), sourceJigsawPos, sourceOrientation, structureManager, bridgeCoverLocation, "twilightforest:lich_tower/bridge", random);

		if (placeableJunction != null) {
			StructurePiece bridgeCoverPiece = new LichTowerWingBridge(structureManager, newDepth, placeableJunction, bridgeCoverLocation, false);
			pieceAccessor.addPiece(bridgeCoverPiece);
			bridgeCoverPiece.addChildren(parent, pieceAccessor, random);
		}
	}

	public boolean tryGenerateRoom(final RandomSource random, final StructurePieceAccessor structureStart, final int roomMaxSize, boolean generateGround, @Nullable ResourceLocation override, boolean tiny) {
		List<JigsawRecord> spareJigsaws = this.getSpareJigsaws();
		if (this.getSpareJigsaws().isEmpty())
			return false;

		if (override != null) {
			return tryPlaceRoom(random, structureStart, override, spareJigsaws.getFirst(), 3, generateGround, false, this, this.genDepth + 1, this.structureManager, "twilightforest:lich_tower/room");
		}

		int minSize = tiny ? 0 : 1;
		for (JigsawRecord generatingPoint : spareJigsaws) {
			for (int roomSize = Math.max(0, roomMaxSize - 1); roomSize >= minSize; roomSize--) {
				boolean roomSuccess = tryPlaceRoom(random, structureStart, lichTowerUtil.rollRandomRoom(random, roomSize), generatingPoint, roomSize, generateGround, false, this, this.genDepth + 1, this.structureManager, "twilightforest:lich_tower/room");

				if (roomSuccess) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean tryPlaceRoom(RandomSource random, StructurePieceAccessor pieceAccessor, @Nullable ResourceLocation roomId, JigsawRecord connection, int roomSize, boolean canPutGround, boolean allowClipping, TwilightJigsawPiece parent, int newDepth, StructureTemplateManager structureManager, String jigsawLabel) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), connection.orientation(), structureManager, roomId, jigsawLabel, random);

		if (placeableJunction == null) {
			return false;
		}

		boolean generateGround = canPutGround && connection.pos().getY() < 4;

		boolean doLadder = placeableJunction.isWithoutCollision(structureManager, pieceAccessor, box -> BoundingBoxUtils.extrusionFrom(box, Direction.UP, Mth.ceil(box.getYSpan() * 1.5f)));
		StructurePiece room = new LichTowerWingRoom(structureManager, newDepth, placeableJunction, roomId, roomSize, generateGround, doLadder, random);

		if (allowClipping || pieceAccessor.findCollisionPiece(room.getBoundingBox()) == null) {
			pieceAccessor.addPiece(room);
			room.addChildren(parent, pieceAccessor, random);

			return true;
		}

		return false;
	}

	@Override
	public int getSortKey() {
		return 2;
	}
}
