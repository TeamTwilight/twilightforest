package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.common.collect.Streams;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.UtilityPiece;
import twilightforest.world.components.structures.util.SortablePiece;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class LichPerimeterFence extends TwilightJigsawPiece implements PieceBeardifierModifier, SortablePiece, SpawnIndexProvider.Deny {
	private final @Nullable BlockPos leashPos;

	public LichPerimeterFence(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_PERIMETER_FENCE.value(), compoundTag, ctx, readSettings(compoundTag));

		this.placeSettings.addProcessor(JigsawReplacementProcessor.INSTANCE);
		this.placeSettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		long leashPosVal = compoundTag.getLong("leash_pos").orElse(0L);
		this.leashPos = leashPosVal == 0L ? null : BlockPos.of(leashPosVal);
	}

	public LichPerimeterFence(StructureTemplateManager structureManager, JigsawPlaceContext jigsawContext, Identifier templateId, RandomSource random) {
		super(TFStructurePieceTypes.LICH_PERIMETER_FENCE.value(), 0, structureManager, templateId, jigsawContext);

		this.placeSettings.addProcessor(JigsawReplacementProcessor.INSTANCE);
		this.placeSettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);

		List<StructureTemplate.StructureBlockInfo> fenceBlocks = random.nextFloat() > 0.25 ? List.of() : this.template.filterBlocks(BlockPos.ZERO, this.placeSettings, TFBlocks.WROUGHT_IRON_FENCE.value(), true);
		if (!fenceBlocks.isEmpty()) {
			fenceBlocks.removeIf(info -> info.state().getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.POST);
			Util.shuffle(fenceBlocks, random);
		}
		this.leashPos = fenceBlocks.isEmpty() ? null : this.templatePosition.offset(fenceBlocks.getFirst().pos());
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		if (this.leashPos != null) {
			structureTag.putLong("leash_pos", this.leashPos.asLong());
		}
	}

	@Override
	public void addJigsaws(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context) {
		super.addJigsaws(parent, pieceAccessor, context);

		Direction ladderDirection = this.getSourceJigsaw().orientation().top().getOpposite();
		BlockPos ladderColumnPos = this.getSourcePosition().relative(ladderDirection, 2).above(2);

		if (this.getSpareJigsaws().size() == 1 && ladderDirection.getAxis() != Direction.Axis.Y) {
			BoundingBox box = new BoundingBox(ladderColumnPos).inflatedBy(3);
			UtilityPiece treeClearance = new UtilityPiece(this.genDepth + 1, box, false);
			pieceAccessor.addPiece(treeClearance);
			treeClearance.addChildren(this, pieceAccessor, context.random());
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
		return 2;
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
	}

	public List<JigsawRecord> getLeftJunctions() {
		return this.matchSpareJigsaws(r -> "twilightforest:lich_tower/fence_edge_left".equals(r.name()));
	}

	public List<JigsawRecord> getRightJunctions() {
		return this.matchSpareJigsaws(r -> "twilightforest:lich_tower/fence_edge_right".equals(r.name()));
	}

	public static void generateFence(TwilightJigsawPiece startingPiece, Structure.GenerationContext context, StructurePiecesBuilder structurePiecesBuilder, StructureTemplateManager structureManager, WorldgenRandom random, Direction direction, BlockPos fenceCenter) {
		LichPerimeterFence frontFence = startPerimeterFence(startingPiece, context, structurePiecesBuilder, structureManager, random, direction, fenceCenter);
		if (frontFence == null) return;

		Optional<StructurePiece> towerBase = structurePiecesBuilder.pieces.stream().filter(piece -> piece instanceof LichTowerBase).findFirst();
		if (towerBase.isEmpty() || !(towerBase.get() instanceof LichTowerBase base)) return;

		BlockPos baseBottomCenter = BoundingBoxUtils.bottomCenterOf(base.getBoundingBox());
		Direction sourceJigsawFront = base.getSourceJigsaw().orientation().front();

		BoundingBox leftDest = getClosestTrimOnGround(structurePiecesBuilder.pieces, baseBottomCenter.relative(sourceJigsawFront.getClockWise(), 64));
		BoundingBox rightDest = getClosestTrimOnGround(structurePiecesBuilder.pieces, baseBottomCenter.relative(sourceJigsawFront.getCounterClockWise(), 64));

		if (leftDest == null || rightDest == null) return;

		generatePerimeter(frontFence, structureManager, structurePiecesBuilder, random, context, leftDest.inflatedBy(-1), rightDest.inflatedBy(-1));
	}

	@Nullable
	private static BoundingBox getClosestTrimOnGround(List<StructurePiece> pieces, BlockPos pos) {
		LichTowerWingBeard closestPiece = null;
		float minDistSq = Float.MAX_VALUE;

		for (StructurePiece piece : pieces) {
			if (!(piece instanceof LichTowerWingBeard beard) || !beard.isTrim())
				continue;

			BlockPos bottomCenter = BoundingBoxUtils.bottomCenterOf(piece.getBoundingBox());
			float distSqr = horizontalDist(bottomCenter, pos);
			if (distSqr < minDistSq) {
				minDistSq = distSqr;
				closestPiece = beard;
			}
		}

		return closestPiece == null ? null : closestPiece.getBoundingBox();
	}

	private static float horizontalDist(BlockPos first, BlockPos second) {
		int dX = second.getX() - first.getX();
		int dZ = second.getZ() - first.getZ();
		return Mth.sqrt(dX * dX + dZ * dZ);
	}

	// Provides the first fence piece, that can be used for calling generatePerimeter()
	@Nullable
	public static LichPerimeterFence startPerimeterFence(TwilightJigsawPiece vestibule, Structure.GenerationContext context, StructurePiecesBuilder structurePiecesBuilder, StructureTemplateManager structureManager, WorldgenRandom random, Direction direction, BlockPos fenceCenter) {
		FrontAndTop orientation = FrontAndTop.fromFrontAndTop(Direction.UP, direction);
		int baseY = fenceCenter.getY(); // context.chunkGenerator().getBaseHeight(fenceCenter.getX(), fenceCenter.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(fenceCenter.atY(baseY - 2), BlockPos.ZERO, orientation, structureManager, TwilightForestMod.prefix("lich_tower/outer_fence_7"), "twilightforest:lich_tower/fence_source", random);

		if (placeableJunction == null) return null;

		LichPerimeterFence fenceStarter = new LichPerimeterFence(structureManager, placeableJunction, TwilightForestMod.prefix("lich_tower/outer_fence_7"), random);
		structurePiecesBuilder.addPiece(fenceStarter);
		fenceStarter.addJigsaws(vestibule, structurePiecesBuilder, context);

		return fenceStarter;
	}

	// Bifurcated fence generation, using frontFence as the starting piece
	public static void generatePerimeter(LichPerimeterFence frontFence, StructureTemplateManager structureManager, StructurePiecesBuilder structurePiecesBuilder, WorldgenRandom random, Structure.GenerationContext context, BoundingBox leftDest, BoundingBox rightDest) {
		Identifier fullFenceId = TwilightForestMod.prefix("lich_tower/outer_fence_7");

		generateSidedPerimeter(frontFence, structureManager, structurePiecesBuilder, random, context, fullFenceId, leftDest, LichPerimeterFence::getLeftJunctions, Rotation.CLOCKWISE_90);
		generateSidedPerimeter(frontFence, structureManager, structurePiecesBuilder, random, context, fullFenceId, rightDest, LichPerimeterFence::getRightJunctions, Rotation.COUNTERCLOCKWISE_90);
	}

	private static void generateSidedPerimeter(LichPerimeterFence frontFence, StructureTemplateManager structureManager, StructurePiecesBuilder structurePiecesBuilder, WorldgenRandom random, Structure.GenerationContext context, Identifier fullFenceId, BoundingBox destination, Function<LichPerimeterFence, List<JigsawRecord>> junctionGetter, Rotation rotation) {
		LichPerimeterFence fence = nextFence(frontFence, structureManager, structurePiecesBuilder, random, junctionGetter.apply(frontFence), Rotation.NONE, context, fullFenceId, destination);
		if (fence == null) return;

		fence = generateUntilNearDest(structureManager, structurePiecesBuilder, random, context, destination, 4, fence, rotation, junctionGetter, fullFenceId);
		List<JigsawRecord> fenceJunctions = junctionGetter.apply(fence);
		if (fence == null || fenceJunctions.isEmpty()) return;

		// Generate until collision
		JigsawRecord first = fenceJunctions.getFirst();
		BlockPos fencePostPos = fence.templatePosition.offset(first.pos());
		// Since the fencepost is directly in front of one of the box's faces; this should the same as a regular distance function. If not, then a deeper issue has happened inside generateUntilNearDest()

		for (int distance = Math.min(BoundingBoxUtils.greatestAxalDistance(destination, fencePostPos) + 1, 32); distance > 2;) {
			int stepSize = Math.min(distance, 7);
			distance -= stepSize;

			fence = nextFence(fence, structureManager, structurePiecesBuilder, random, junctionGetter.apply(fence), rotation, context, TwilightForestMod.prefix("lich_tower/outer_fence_" + stepSize), destination);
			if (fence == null) return;
			rotation = Rotation.NONE;
		}
	}

	/**
	 * Generates fences from the starting piece up until the furthest fencepost sits directly in front of the destination sidetower's box
	 */
	@Nullable
	private static LichPerimeterFence generateUntilNearDest(StructureTemplateManager structureManager, StructurePiecesBuilder structurePiecesBuilder, WorldgenRandom random, Structure.GenerationContext context, BoundingBox destBox, int turnAtIndex, LichPerimeterFence fence, Rotation turn, Function<LichPerimeterFence, List<JigsawRecord>> junctionGetter, Identifier templateId) {
		int infoldedPieces = 0;
		int counterRotatedPieces = 0;
		boolean foldNext = false;
		int foldAt = random.nextInt(turnAtIndex - 1);
		int maximumPosts = 16;
		for (int idx = 0; idx < maximumPosts; idx++) {
			boolean makeTurn = idx == turnAtIndex;
			boolean marchTowardsDest = idx > turnAtIndex;

			if (fence == null)
				break; // This shouldn't happen ever

			List<JigsawRecord> junctions = junctionGetter.apply(fence);

			if (junctions.isEmpty())
				break; // This shouldn't happen either

			if (marchTowardsDest) {
				// Tests if the current spare fencepost aligns with a sidetower
				JigsawRecord first = junctions.getFirst();
				BlockPos checkPos = fence.templatePosition.offset(first.pos());
				BlockPos destPos = BoundingBoxUtils.clampedInside(destBox, checkPos);

				BlockPos directionOffset = destPos.subtract(checkPos);
				// Rotate to test for orthogonality via dot product (to check if dot() == 0)
				var targetDirecton = turn.rotate(first.orientation().top()).getUnitVec3i();

				// 0 from (horizontal) dot product means orthogonal, can approach from a right-angle turn now
				if (directionOffset.getX() * targetDirecton.getX() + directionOffset.getZ() * targetDirecton.getZ() == 0)
					break; // The spare fence-post has aligned with the grounded sidetower, break loop
			} else if (idx == foldAt) {
				infoldedPieces = random.nextIntBetweenInclusive(1, idx + 1);
				counterRotatedPieces = turnAtIndex - infoldedPieces;
				turnAtIndex += infoldedPieces;
				foldNext = true;
			}

			if (infoldedPieces > 0) {
				Rotation nextTurn = foldNext ? turn : Rotation.NONE;
				foldNext = false;
				fence = nextFence(fence, structureManager, structurePiecesBuilder, random, junctions, nextTurn, context, templateId, destBox);

				infoldedPieces--;
				if (infoldedPieces == 0) {
					foldNext = true;
				}
			} else if (counterRotatedPieces > 0) {
				Rotation nextTurn = foldNext ? Rotation.CLOCKWISE_180.getRotated(turn) : (makeTurn ? turn : Rotation.NONE);
				foldNext = false;
				fence = nextFence(fence, structureManager, structurePiecesBuilder, random, junctions, nextTurn, context, templateId, destBox);

				counterRotatedPieces--;
			} else {
				Rotation nextTurn = makeTurn ? turn : Rotation.NONE;
				fence = nextFence(fence, structureManager, structurePiecesBuilder, random, junctions, nextTurn, context, templateId, destBox);
			}
		}

		return fence;
	}

	@Nullable
	public static LichPerimeterFence nextFence(LichPerimeterFence parentFence, StructureTemplateManager structureManager, StructurePiecesBuilder structurePiecesBuilder, WorldgenRandom random, List<JigsawRecord> junctions, Rotation rotation, Structure.GenerationContext context, Identifier templateId, BoundingBox destination) {
		if (junctions.isEmpty()) return null;

		JigsawRecord junction = junctions.getFirst();
		FrontAndTop orientation = junction.orientation();
		FrontAndTop connectOrientation = FrontAndTop.fromFrontAndTop(orientation.front().getOpposite(), rotation.rotate(orientation.top()));
		BlockPos postPos = parentFence.templatePosition.offset(junction.pos());

		int horizontalAxalDistance = BoundingBoxUtils.horizontalManhattanDistance(destination, postPos);

		int dY;
		if (horizontalAxalDistance <= 20) {
			// Begin "homing" onto Fence's vertical height if close enough to the trim
			dY = Mth.clamp(parentFence.templatePosition().getY() - 1, destination.minY() + 2, destination.maxY() - 2) - 1 - parentFence.templatePosition().getY();
		} else {
			dY = 0;
		}
		BlockPos parentPos = parentFence.templatePosition().above(Mth.sign(dY) - 1);

		JigsawPlaceContext placeContext = JigsawPlaceContext.pickPlaceableJunction(parentPos, junction.pos(), connectOrientation, structureManager, templateId, junction.target(), random);

		if (placeContext == null) return null;

		LichPerimeterFence nextFence = new LichPerimeterFence(structureManager, placeContext, templateId, random);
		structurePiecesBuilder.addPiece(nextFence);
		nextFence.addJigsaws(parentFence, structurePiecesBuilder, context);

		return nextFence;
	}

	@Override
	public int getSortKey() {
		// Make higher-ups generate later so the lower fences' slabs don't replace full blocks
		return this.boundingBox.maxY();
	}

	public Stream<BlockPos> fencePostPositions() {
		return Streams.concat(this.getLeftJunctions().stream(), this.getRightJunctions().stream()).map(r -> this.templatePosition.offset(r.pos()));
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		this.generateBoundZombie(level, chunkBounds);
		this.generateEscapeLadder(level, chunkBounds);
	}

	private void generateBoundZombie(WorldGenLevel level, BoundingBox chunkBounds) {
		Direction fenceFacing = this.getSourceJigsaw().orientation().top();
		if (this.leashPos == null || !chunkBounds.isInside(this.leashPos))
			return;

		BlockPos zombiePos = this.leashPos.relative(fenceFacing, 1);
		if (!chunkBounds.isInside(zombiePos))
			return;

		var knot = EntityUtil.createEntityIgnoreException(level, EntityType.LEASH_KNOT);
		var boundedEntity = EntityUtil.createEntityIgnoreException(level, EntityType.ZOMBIE);
		if (knot == null || boundedEntity == null)
			return;

		knot.snapTo(this.leashPos.getX() + 0.5, this.leashPos.getY(), this.leashPos.getZ() + 0.5);

		boundedEntity.setPersistenceRequired();
		boundedEntity.setLeashedTo(knot, false);
		boundedEntity.snapTo(zombiePos.getX() + 0.5, zombiePos.getY() - 1, zombiePos.getZ() + 0.5);
		boundedEntity.setData(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE, Unit.INSTANCE);
		level.addFreshEntity(boundedEntity);
	}

	private void generateEscapeLadder(WorldGenLevel level, BoundingBox chunkBounds) {
		Direction ladderDirection = this.getSourceJigsaw().orientation().top().getOpposite();
		BlockPos ladderColumnPos = this.getSourcePosition().relative(ladderDirection);

		if (!chunkBounds.isInside(ladderColumnPos) || this.getSpareJigsaws().size() != 1 || ladderDirection.getAxis() == Direction.Axis.Y)
			return;

		BlockState ladderState = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, ladderDirection);
		for (BlockPos ladderPos : BlockPos.betweenClosed(ladderColumnPos.above(), ladderColumnPos.above(4))) {
			level.setBlock(ladderPos, ladderState, Block.UPDATE_ALL);
		}
	}
}
