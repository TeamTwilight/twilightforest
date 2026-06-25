package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import tamaized.beanification.Autowired;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.custom.MagicPaintingVariants;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFPaintingVariantTags;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.Optional;

public class LichTowerMagicGallery extends TwilightJigsawPiece implements PieceBeardifierModifier, SpawnIndexProvider {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	public LichTowerMagicGallery(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_MAGIC_GALLERY.value(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
	}

	public LichTowerMagicGallery(int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext) {
		super(TFStructurePieceTypes.LICH_MAGIC_GALLERY.value(), genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getRoomSpawnerProcessor()));
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
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		this.removeBanisters(level, chunkBounds);

		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);
	}

	private void removeBanisters(WorldGenLevel level, BoundingBox chunkBounds) {
		JigsawRecord sourceJigsaw = this.getSourceJigsaw();
		BlockPos sourcePos = this.templatePosition.offset(sourceJigsaw.pos());
		Direction front = sourceJigsaw.orientation().front();

		BlockPos leftPos = sourcePos.relative(front.getClockWise(Direction.Axis.Y));

		Direction counterClockWise = front.getCounterClockWise(Direction.Axis.Y);
		// Special shifting for if this gallery has an entrance that is 2 blocks wide
		int span = BoundingBoxUtils.getSpan(this.boundingBox, counterClockWise.getAxis());
		int evenShift = (span + 1) % 2;
		BlockPos rightPos = sourcePos.relative(counterClockWise, 1 + evenShift);

		removeIfBanister(level, leftPos, chunkBounds);
		removeIfBanister(level, leftPos.above(), chunkBounds);
		removeIfBanister(level, rightPos, chunkBounds);
		removeIfBanister(level, rightPos.below(), chunkBounds);
		if (evenShift == 1) // Got another banister to remove
			removeIfBanister(level, sourcePos.relative(counterClockWise, 1).below(), chunkBounds);
	}

	private static void removeIfBanister(WorldGenLevel level, BlockPos pos, BoundingBox chunkBounds) {
		if (chunkBounds.isInside(pos)) {
			if (level.getBlockState(pos).is(TFBlockTags.BANISTERS)) {
				level.removeBlock(pos, false);
			}
		}
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
		if ("twilightforest:lich_tower/roof".equals(connection.target())) {
			Identifier fallbackRoof = lichTowerUtil.rollGalleryRoof(context.random(), this.boundingBox);
			FrontAndTop orientationToMatch = LichTowerWingRoom.getVerticalOrientation(connection, Direction.UP, this);
			LichTowerWingRoom.tryRoof(pieceAccessor, context, connection, fallbackRoof, orientationToMatch, true, this, this.genDepth + 1, this.structureManager);
		}
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		level.removeBlock(pos, false);

		if ("painting".equals(label)) {
			Direction direction = this.placeSettings.getRotation().rotate(Direction.SOUTH);

			Optional<Holder.Reference<MagicPaintingVariant>> variantHolderOpt = variantForGallery(level, this.templateName);
			MagicPainting galleryPainting = TFEntities.MAGIC_PAINTING.value().create(level.getLevel(), EntitySpawnReason.STRUCTURE);
			if (variantHolderOpt.isPresent() && galleryPainting != null) {
				galleryPainting.setDirection(direction);
				galleryPainting.setVariant(variantHolderOpt.get());

				variantHolderOpt.get().value();
				this.placeSettings.getRotation();
				galleryPainting.moveOrInterpolateTo(pos.getBottomCenter(), 0, 0);

				level.addFreshEntityWithPassengers(galleryPainting);
			}
		} else {
			LichBossRoom.placePainting(label, pos, level, random, chunkBounds, this.placeSettings.getRotation(), 2, 1, TFPaintingVariantTags.LICH_TOWER_PAINTINGS);
		}
	}

	private static Optional<Holder.Reference<MagicPaintingVariant>> variantForGallery(ServerLevelAccessor level, String roomId) {
		ResourceKey<MagicPaintingVariant> variantId = switch (roomId) {
			case "twilightforest:lich_tower/gallery/castaway_paradise" -> MagicPaintingVariants.CASTAWAY_PARADISE;
			case "twilightforest:lich_tower/gallery/darkness" -> MagicPaintingVariants.DARKNESS;
			case "twilightforest:lich_tower/gallery/lucid_lands" -> MagicPaintingVariants.LUCID_LANDS;
			case "twilightforest:lich_tower/gallery/music_in_the_mire" -> MagicPaintingVariants.MUSIC_IN_THE_MIRE;
			case "twilightforest:lich_tower/gallery/the_hostile_paradise" -> MagicPaintingVariants.THE_HOSTILE_PARADISE;
			default -> null;
		};

		if (variantId == null) return Optional.empty();

		return level.registryAccess().lookupOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS).get(variantId);
	}

	public static void tryPlaceGallery(Structure.GenerationContext context, StructurePieceAccessor pieceAccessor, @Nullable Identifier roomId, JigsawRecord connection, TwilightJigsawPiece parent, int newDepth, StructureTemplateManager structureManager, String jigsawLabel) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), connection.orientation(), structureManager, roomId, jigsawLabel, context.random());
		if (placeableJunction != null) {
			LichTowerMagicGallery room = new LichTowerMagicGallery(newDepth, structureManager, roomId, placeableJunction);
			pieceAccessor.addPiece(room);
			room.addJigsaws(parent, pieceAccessor, context);
		}
	}

	@Override
	public int getSpawnIndex() {
		return LichTowerPieces.INTERIOR_SPAWNS;
	}
}
