package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.ArrayList;

public final class LichTowerSegment extends TwilightJigsawPiece implements PieceBeardifierModifier, SpawnIndexProvider {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final boolean putMobBridge;
	private final boolean putWings;
	private final boolean putGallery;

	public LichTowerSegment(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_SEGMENT.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
		stairDecay(this.genDepth, this.placeSettings);

		this.putMobBridge = compoundTag.getBoolean("put_bridge");
		this.putWings = compoundTag.getBoolean("put_wings");
		this.putGallery = compoundTag.getBoolean("put_gallery");
	}

	public LichTowerSegment(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, boolean putMobBridge, boolean putWings, boolean putGallery) {
		super(TFStructurePieceTypes.LICH_TOWER_SEGMENT.get(), genDepth, structureManager, TwilightForestMod.prefix("lich_tower/tower_slice"), jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);
		stairDecay(this.genDepth, this.placeSettings);

		this.putMobBridge = putMobBridge;
		this.putWings = putWings;
		this.putGallery = putGallery;
	}

	private static void stairDecay(int depth, StructurePlaceSettings settings) {
		int decayLevel = Mth.ceil((depth - 3) * 0.25);

		if (decayLevel >= 0) {
			StructureProcessor[] stairDecayProcessors = lichTowerUtil.getStairDecayProcessors();
			decayLevel = Math.min(decayLevel, stairDecayProcessors.length);
			settings.addProcessor(stairDecayProcessors[decayLevel]);
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("put_bridge", this.putMobBridge);
		structureTag.putBoolean("put_wings", this.putWings);
		structureTag.putBoolean("put_gallery", this.putGallery);
	}

	public static void buildTowerBySegments(StructurePieceAccessor pieceAccessor, RandomSource random, final BlockPos sourceJigsawPos, final FrontAndTop sourceOrientation, final TwilightJigsawPiece parentBase, StructureTemplateManager structureManager, final int segments) {
		ResourceLocation segmentId = TwilightForestMod.prefix("lich_tower/tower_slice");
		ArrayList<TwilightTemplateStructurePiece> pieces = new ArrayList<>();

		TwilightTemplateStructurePiece priorPiece = parentBase;
		BlockPos priorJigsawOffset = sourceJigsawPos;
		FrontAndTop priorOrientation = sourceOrientation;
		int mobBridge = random.nextIntBetweenInclusive(0, 5);
		for (int stackIndex = 0; stackIndex < segments; stackIndex++) {
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(priorPiece.templatePosition(), priorJigsawOffset, priorOrientation, structureManager, segmentId, "twilightforest:lich_tower/tower_below", random);

			if (placeableJunction == null) continue;

			boolean putWings = stackIndex > segments >> 1;
			boolean putGallery = stackIndex == segments - 1;
			LichTowerSegment towerSegment = new LichTowerSegment(structureManager, priorPiece.getGenDepth() + 1, placeableJunction, mobBridge == 0, putWings, putGallery);

			pieceAccessor.addPiece(towerSegment);
			pieces.add(towerSegment); // Add to list for adding children later, must build upwards to the boss room before beginning Sidetowers from the base & upwards too

			JigsawRecord firstJunction = placeableJunction.findFirst("twilightforest:lich_tower/tower_above");

			if (firstJunction == null) break;

			priorPiece = towerSegment;
			priorJigsawOffset = firstJunction.pos();
			priorOrientation = firstJunction.orientation();
			mobBridge = mobBridge == 0 ? random.nextIntBetweenInclusive(2, 5) : (mobBridge - 1);
		}

		// The boss room is wider than Main Tower segments, adding to piece list sooner will help prevent these collisions
		JigsawPlaceContext bossRoomJunction = JigsawPlaceContext.pickPlaceableJunction(priorPiece.templatePosition(), priorJigsawOffset, priorOrientation, structureManager, TwilightForestMod.prefix("lich_tower/tower_boss_room"), "twilightforest:lich_tower/tower_below", random);
		if (bossRoomJunction != null) {
			StructurePiece bossRoom = new LichBossRoom(structureManager, bossRoomJunction);
			pieceAccessor.addPiece(bossRoom);
			bossRoom.addChildren(priorPiece, pieceAccessor, random);
		}

		if (pieces.isEmpty())
			return;

		// Call the topmost segment first, so that guaranteed generation of the Magic Gallery is better deterministic and not competing against side-tower clearances
		priorPiece = pieces.removeLast();
		priorPiece.addChildren(pieces.isEmpty() ? parentBase : pieces.getLast(), pieceAccessor, random);

		// Now call .addChildren of all segment pieces so that the side-towers generate their shapes
		priorPiece = parentBase;
		for (TwilightTemplateStructurePiece piece : pieces) {
			piece.addChildren(priorPiece, pieceAccessor, random);
			priorPiece = piece;
		}
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/bridge" -> {
				if (!this.putWings) return;

				// If this is the top segment, then place only the gallery so that the normal side towers place lower
				//  and thus generate taller without colliding into the boss room
				if (this.putGallery) {
					if (jigsawIndex == 2 && random.nextInt(10) == 0) {
						LichTowerMagicGallery.tryPlaceGallery(random, pieceAccessor, lichTowerUtil.rollTowerGallery(random), connection, this, this.genDepth + 1, this.structureManager, "twilightforest:lich_tower/bridge_center");
					}
				} else {
					LichTowerWingBridge.tryRoomAndBridge(this, pieceAccessor, random, connection, this.structureManager, true, 4, false, this.genDepth + 1, null);
				}
			}
			case "twilightforest:mob_bridge" -> {
				if (this.putMobBridge) {
					ResourceLocation mobBridgeLocation = lichTowerUtil.rollRandomMobBridge(random);
					JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, mobBridgeLocation, "twilightforest:mob_bridge", random);

					if (placeableJunction != null) {
						StructurePiece mobBridgePiece = new LichTowerSpawnerBridge(this.genDepth + 1, this.structureManager, mobBridgeLocation, placeableJunction, random.nextBoolean());
						pieceAccessor.addPiece(mobBridgePiece);
						mobBridgePiece.addChildren(this, pieceAccessor, random);
					}
				}
			}
		}
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		LichBossRoom.placePainting(label, pos, level, random, chunkBounds, this.placeSettings.getRotation(), 2, 10, CustomTagGenerator.PaintingVariantTagGenerator.LICH_TOWER_PAINTINGS);
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
	public int getSpawnIndex() {
		return LichTowerPieces.INTERIOR_SPAWNS;
	}
}
