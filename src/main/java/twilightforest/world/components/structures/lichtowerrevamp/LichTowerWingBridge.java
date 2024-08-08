package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.SortablePiece;

public final class LichTowerWingBridge extends TwilightJigsawPiece implements PieceBeardifierModifier, SortablePiece {
	private final boolean generateGround;

	public LichTowerWingBridge(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_WING_BRIDGE.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);

		this.generateGround = compoundTag.getBoolean("gen_ground");
	}

	public LichTowerWingBridge(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, ResourceLocation templateLocation, boolean generateGround) {
		super(TFStructurePieceTypes.LICH_WING_BRIDGE.get(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);

		this.generateGround = generateGround; // Only true for bridge entryway covers on the ground
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord record, int jigsawIndex) {
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.generateGround ? TerrainAdjustment.BEARD_THIN : TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 1;
	}

	public static void tryRoomAndBridge(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, StructureTemplateManager structureManager, boolean fromCentralTower, int roomMaxSize, boolean generateGround, int newDepth, boolean magicGallery) {
		if (magicGallery) {
			LichTowerMagicGallery.tryPlaceGallery(random, pieceAccessor, LichTowerUtil.rollTowerGallery(random), connection, parent, newDepth, structureManager, "twilightforest:lich_tower/bridge_center");
			return;
		}

		boolean shouldGenerateGround = generateGround && connection.pos().getY() < 6;

		if (!generateGround) {
			if (fromCentralTower || random.nextBoolean()) {
				Iterable<ResourceLocation> bridges = fromCentralTower ? LichTowerUtil.shuffledCenterBridges(random) : LichTowerUtil.shuffledRoomBridges(random);
				for (ResourceLocation bridgeId : bridges) {
					if (tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, fromCentralTower, roomMaxSize, false, newDepth, bridgeId, fromCentralTower)) {
						return;
					}
				}
			}
		}

		if (fromCentralTower) {
			tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, true, roomMaxSize, shouldGenerateGround, newDepth, LichTowerPieces.ENCLOSED_BRIDGE_CENTRAL, true);
		} else if (!tryBridge(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, false, roomMaxSize, shouldGenerateGround, newDepth, LichTowerPieces.DIRECT_ATTACHMENT, true)) {
			// This here is reached only if a room was not successfully generated - now a wall must be placed to cover where the bridge would have been
			putCover(parent, pieceAccessor, random, connection.pos(), connection.orientation(), structureManager, shouldGenerateGround, newDepth);
		}
	}

	private static boolean tryBridge(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, boolean fromCentralTower, int roomMaxSize, boolean generateGround, int newDepth, ResourceLocation bridgeId, boolean allowClipping) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), sourceJigsawPos, sourceOrientation, structureManager, bridgeId, fromCentralTower ? "twilightforest:lich_tower/bridge_center" : "twilightforest:lich_tower/bridge", random);

		if (placeableJunction != null) {
			LichTowerWingBridge bridge = new LichTowerWingBridge(structureManager, newDepth, placeableJunction, bridgeId, false);

			if ((allowClipping || pieceAccessor.findCollisionPiece(bridge.boundingBox) == null) && bridge.tryGenerateRoom(random, pieceAccessor, roomMaxSize, generateGround, fromCentralTower)) {
				// If the bridge & room can be fitted, then also add bridge to list then exit this function
				pieceAccessor.addPiece(bridge);
				bridge.addChildren(parent, pieceAccessor, random);
				return true;
			}
		}
		return false;
	}

	public static void putCover(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, boolean generateGround, int newDepth) {
		ResourceLocation bridgeCoverLocation = pieceAccessor.findCollisionPiece(BoundingBox.fromCorners(sourceJigsawPos.relative(sourceOrientation.front(), 1), sourceJigsawPos.relative(sourceOrientation.front(), 3))) == null ? LichTowerUtil.rollRandomCover(random) : LichTowerPieces.COBBLESTONE_WALL;
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), sourceJigsawPos, sourceOrientation, structureManager, bridgeCoverLocation, "twilightforest:lich_tower/bridge", random);

		if (placeableJunction != null) {
			StructurePiece bridgeCoverPiece = new LichTowerWingBridge(structureManager, newDepth, placeableJunction, bridgeCoverLocation, generateGround);
			pieceAccessor.addPiece(bridgeCoverPiece);
			bridgeCoverPiece.addChildren(parent, pieceAccessor, random);
		}
	}

	public boolean tryGenerateRoom(final RandomSource random, final StructurePieceAccessor structureStart, final int roomMaxSize, boolean generateGround, boolean fromCentralTower) {
		int minSize = (fromCentralTower || generateGround) ? 1 : 0;
		for (JigsawRecord generatingPoint : this.getSpareJigsaws()) {
			for (int roomSize = Math.max(0, roomMaxSize - 1); roomSize >= minSize; roomSize--) {
				boolean roomSuccess = tryPlaceRoom(random, structureStart, LichTowerUtil.rollRandomRoom(random, roomSize), generatingPoint, roomSize, generateGround, false, this, this.genDepth + 1, this.structureManager, "twilightforest:lich_tower/room");

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

		boolean doLadder = generateGround || random.nextBoolean();
		StructurePiece room = new LichTowerWingRoom(structureManager, newDepth, placeableJunction, roomId, roomSize, generateGround, doLadder);

		if (allowClipping || pieceAccessor.findCollisionPiece(room.getBoundingBox()) == null) {
			pieceAccessor.addPiece(room);
			room.addChildren(parent, pieceAccessor, random);

			return true;
		}

		return false;
	}

	@Override
	public int getSortKey() {
		return 1;
	}
}
