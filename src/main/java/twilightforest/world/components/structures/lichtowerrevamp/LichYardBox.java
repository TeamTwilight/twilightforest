package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.common.collect.Streams;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import org.joml.SimplexNoise;
import tamaized.beanification.Autowired;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.SortablePiece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LichYardBox extends StructurePiece implements PieceBeardifierModifier, SortablePiece, SpawnIndexProvider {
	@Autowired
	private static LichTowerUtil lichTowerUtil;

	private final float edgeFeatheringRange;
	private final Direction direction;
	private final boolean doDirtMotley;
	private final float scale;
	private final float offset;
	@Nullable
	private Set<BoundingBox> pathBoxes;

	public LichYardBox(BoundingBox boundingBox, float edgeFeatheringRange, Direction direction, boolean doDirtMotley, float scale, float offset) {
		super(TFStructurePieceTypes.LICH_YARD_PATH.value(), 0, boundingBox);

		this.edgeFeatheringRange = edgeFeatheringRange;
		this.direction = direction;
		this.doDirtMotley = doDirtMotley;
		this.scale = scale;
		this.offset = offset;
	}

	public LichYardBox(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(TFStructurePieceTypes.LICH_YARD_PATH.value(), tag);

		this.edgeFeatheringRange = tag.getFloatOr("feather", 0.0F);
		this.direction = tag.contains("direction") ? Direction.values()[tag.getIntOr("direction", 0)] : Direction.UP;
		this.doDirtMotley = tag.getBooleanOr("dirt_mix", false);
		this.scale = tag.getFloatOr("dirt_scale", 0.0F);
		this.offset = tag.getFloatOr("offset", 0.0F);
	}

	public void setPathBoxes(Set<BoundingBox> pathBoxes) {
		this.pathBoxes = pathBoxes;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putFloat("feather", this.edgeFeatheringRange);
		tag.putInt("direction", this.direction.ordinal());
		tag.putBoolean("dirt_mix", this.doDirtMotley);
		tag.putFloat("dirt_scale", this.scale);
		tag.putFloat("offset", this.offset);
	}

	private BlockState pickDirt(int x, int y, int z, RandomSource random) {
		float scale = this.scale * 2.5f;
		float randF = random.nextFloat();
		float noise = randF < 0.25f ? (randF * 4f) : SimplexNoise.noise(x * scale, y * scale + 1024f, z * scale) * 0.5f + 0.5f;

		if (noise > 0.6f) {
			return Blocks.COARSE_DIRT.defaultBlockState();
		} else if (noise > 0.4f) {
			return Blocks.DIRT.defaultBlockState();
		} else {
			return Blocks.ROOTED_DIRT.defaultBlockState();
		}
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		BoundingBox boxIntersection = BoundingBoxUtils.getIntersectionOfSBBs(this.boundingBox, chunkBounds);

		if (boxIntersection == null || this.scale == 0)
			return;

		ChunkAccess chunk = level.getChunk(chunkPos.getWorldPosition());

		// Just pass the regular bounding box instead of doing the extra computation if the fence won't even place
		BoundingBox fenceBounds = this.generateFence() ? BoundingBoxUtils.safeRetract(this.boundingBox, this.direction.getOpposite(), 4) : this.boundingBox;

		for (int z = boxIntersection.minZ(); z <= boxIntersection.maxZ(); z++) {
			for (int x = boxIntersection.minX(); x <= boxIntersection.maxX(); x++) {
				this.processPos(level, random, x, z, chunk, fenceBounds);
			}
		}

		if (this.doDirtMotley) {
			this.placeYardTrees(level, chunkGen, random, boxIntersection);
		}
	}

	private void processPos(WorldGenLevel level, RandomSource random, int x, int z, ChunkAccess chunk, BoundingBox fenceBounds) {
		int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
		BlockPos placeAt = new BlockPos(x, y, z);

		BlockState stateAt = level.getBlockState(placeAt);
		if (!stateAt.is(BlockTags.DIRT) && !stateAt.is(Blocks.GRASS_BLOCK))
			return;

		int xBorderDist = Math.min(x - this.boundingBox.minX(), this.boundingBox.maxX() - x);
		int zBorderDist = Math.min(z - this.boundingBox.minZ(), this.boundingBox.maxZ() - z);
		float borderDist = Math.min(xBorderDist, zBorderDist) + this.offset;

		float featherLevel = borderDist > this.edgeFeatheringRange ? 1f : Mth.clamp(borderDist / this.edgeFeatheringRange, 0, 1);
		float noise = SimplexNoise.noise(x * this.scale, y * this.scale, z * this.scale) * 0.5f - 0.5f;
		float featheredNoise = noise + featherLevel;
		if (featheredNoise < 0) {
			if (this.generateFence() && fenceBounds.intersects(x, z, x, z)) {
				float fenceNoise = SimplexNoise.noise(x * 0.15f, y * 0.15f - 1024f, z * 0.15f) * 0.5f;
				if (Math.abs(fenceNoise) > 0.15f) {
					int noiseRounded = Math.round(fenceNoise + 0.5f);
					if (this.direction.getAxis() == Direction.Axis.Z ? x == this.boundingBox.minX() + noiseRounded || x == this.boundingBox.maxX() - noiseRounded : z == this.boundingBox.minZ() + noiseRounded || z == this.boundingBox.maxZ() - noiseRounded) {
						BlockPos fenceAt = placeAt.above();
						level.setBlock(fenceAt, Blocks.SPRUCE_FENCE.defaultBlockState(), Block.UPDATE_ALL);
						chunk.markPosForPostprocessing(fenceAt);
					}
				}
			}

			return;
		}

		BlockState state = this.doDirtMotley ? this.pickDirt(x, y, z, random) : Blocks.DIRT_PATH.defaultBlockState();

		level.setBlock(placeAt, state, Block.UPDATE_ALL);
		// Remove the darned plants
		if (this.doDirtMotley && random.nextFloat() < 0.0125f) {
			// Place dead plant instead
			level.setBlock(placeAt.above(), Blocks.DEAD_BUSH.defaultBlockState(), Block.UPDATE_ALL);
		} else {
			level.setBlock(placeAt.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
		}
		level.setBlock(placeAt.above(2), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
	}

	private boolean generateFence() {
		return !this.doDirtMotley;
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.doDirtMotley ? TerrainAdjustment.BEARD_BOX : TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}

	public static void beginYard(LichTowerFoyer foyerPiece, Structure.GenerationContext context, StructurePiecesBuilder pieces) {
		WorldgenRandom random = context.random();
		StructureTemplateManager structureManager = context.structureTemplateManager();
		int ySurface = foyerPiece.getBoundingBox().minY() + foyerPiece.getGroundLevelDelta();

		List<JigsawRecord> pathRecords = foyerPiece.matchSpareJigsaws(r -> "twilightforest:lich_tower/path".equals(r.target()));
		if (pathRecords.isEmpty()) return;
		JigsawRecord path = pathRecords.getFirst();

		int pathLength = random.nextInt(24, 32);
		Direction direction = foyerPiece.getRotation().rotate(Direction.SOUTH);

		BlockPos generatePos = foyerPiece.templatePosition().offset(path.pos());
		BlockPos fenceCenter = generatePos.relative(direction, pathLength);
		LichPerimeterFence.generateFence(foyerPiece, context, pieces, structureManager, random, direction, fenceCenter.atY(ySurface));

		BlockPos nearVestibule = generatePos.relative(direction, 1).above(4);
		BlockPos nearFence = fenceCenter.relative(direction.getOpposite(), 6).below(4);
		Set<BoundingBox> pathBoxes = new HashSet<>();
		generateYard(foyerPiece, pieces, nearVestibule, nearFence, random, direction, context, pathBoxes);

		Stream<BlockPos> foyerRootPos = Stream.of(foyerPiece.getBoundingBox().getCenter().above(10), BoundingBoxUtils.bottomCenterOf(foyerPiece.getBoundingBox()).below(10));
		Stream<BlockPos> fencePostPos = pieces.pieces.stream().filter(p -> p instanceof LichPerimeterFence).flatMap(f -> ((LichPerimeterFence) f).fencePostPositions());
		Optional<BoundingBox> fullYard = BoundingBox.encapsulatingPositions(Streams.concat(foyerRootPos, fencePostPos).collect(Collectors.toUnmodifiableSet()));
		if (fullYard.isEmpty()) return;

		BoundingBox yardBox = BoundingBoxUtils.safeRetract(BoundingBoxUtils.setY(fullYard.get().inflatedBy(3), ySurface, ySurface + 10), foyerPiece.getSourceJigsaw().orientation().top().getOpposite(), 5);

		LichYardBox lichYardDirt = new LichYardBox(yardBox, 8, Direction.UP, true, 0.1f, 0);
		lichYardDirt.setPathBoxes(pathBoxes);
		pieces.addPiece(lichYardDirt);
		lichYardDirt.addDecoration(foyerPiece, pieces, random, context);
	}

	private static void generateYard(LichTowerFoyer foyerPiece, StructurePiecesBuilder pieces, BlockPos nearVestibule, BlockPos nearFence, WorldgenRandom random, Direction dirFromVestibule, Structure.GenerationContext context, Set<BoundingBox> pathBoxes) {
		int ySurface = foyerPiece.getBoundingBox().minY() + foyerPiece.getGroundLevelDelta();
		List<LichYardBox> paths = new ArrayList<>(); // Add all pieces to a list instead of immediately adding children, so that paths can generate before graves check for overlap

		// First path, from the vestibule
		BoundingBox firstPathBox = BoundingBoxUtils.setY(BoundingBoxUtils.wrappedCoordinates(3, nearVestibule, nearFence).inflatedBy(1), ySurface, ySurface + 10);
		pathBoxes.add(firstPathBox);

		Direction.Axis axisFromVestibule = dirFromVestibule.getAxis();
		LichYardBox lichYardBox = new LichYardBox(firstPathBox, 2.5f, dirFromVestibule, false, 0.35f, -1);
		pieces.addPiece(lichYardBox);
		paths.add(lichYardBox);

		// Second path, crossing the path from the vestibule
		BlockPos randomPos = lerpBlockPos(Mth.lerp(random.nextFloat(), 0.2f, 0.8f), nearVestibule, nearFence);
		int crossPathSpan = 24;
		BlockPos pathLeft = randomPos.relative(dirFromVestibule.getClockWise(), crossPathSpan);
		BlockPos pathRight = randomPos.relative(dirFromVestibule.getCounterClockWise(), crossPathSpan);

		BoundingBox crossPathBox = BoundingBoxUtils.setY(BoundingBoxUtils.wrappedCoordinates(1, pathLeft, pathRight), ySurface, ySurface + 10);
		pathBoxes.add(crossPathBox);

		LichYardBox crossPath = new LichYardBox(crossPathBox, -1, dirFromVestibule.getClockWise(), false, 0, 0);
		pieces.addPiece(crossPath);
		paths.add(crossPath);

		// Last two paths, to the sides of the vestibule
		paths.add(putSidePath(pieces, nearVestibule.atY(ySurface), dirFromVestibule, dirFromVestibule.getClockWise(), pathLeft, crossPathSpan, pathBoxes));
		paths.add(putSidePath(pieces, nearVestibule.atY(ySurface), dirFromVestibule, dirFromVestibule.getCounterClockWise(), pathRight, crossPathSpan, pathBoxes));

		// Put lights before beginning grave placements
		BoundingBox boxLightPlace = firstPathBox.inflatedBy(axisFromVestibule == Direction.Axis.Z ? 3 : 0, 0, axisFromVestibule == Direction.Axis.X ? 3 : 0);
		LichYardLights lichYardLights = new LichYardLights(boxLightPlace, axisFromVestibule);
		pieces.addPiece(lichYardLights);
		lichYardLights.addChildren(foyerPiece, pieces, random);

		// Now that all paths are generated, call addChildren on each so graves are placed
		for (LichYardBox piece : paths) {
			piece.addDecoration(foyerPiece, pieces, random, context);
		}
	}

	private static LichYardBox putSidePath(StructurePiecesBuilder structurePiecesBuilder, BlockPos nearVestibule, Direction dirFromVestibule, Direction sideDirection, BlockPos pathEnd, int spread, Set<BoundingBox> pathBoxes) {
		BlockPos fromVestibule = nearVestibule.relative(sideDirection, 24);

		BoundingBox pathBox = BoundingBoxUtils.setY(BoundingBoxUtils.wrappedCoordinates(1, pathEnd, fromVestibule.relative(dirFromVestibule.getOpposite(), spread)), nearVestibule.getY(), nearVestibule.getY() + 10);
		pathBoxes.add(pathBox);
		LichYardBox path = new LichYardBox(pathBox, -1, dirFromVestibule, false, 0, 0);
		structurePiecesBuilder.addPiece(path);
		return path;
	}

	private static BlockPos lerpBlockPos(float delta, BlockPos first, BlockPos second) {
		return new BlockPos(Mth.lerpDiscrete(delta, first.getX(), second.getX()), Mth.lerpDiscrete(delta, first.getY(), second.getY()), Mth.lerpDiscrete(delta, first.getZ(), second.getZ()));
	}

	public void addDecoration(TwilightJigsawPiece parent, StructurePieceAccessor pieces, RandomSource random, Structure.GenerationContext context) {
		this.addChildren(parent, pieces, random);

		Direction.Axis axis = this.direction.getAxis();
		if (axis == Direction.Axis.Y || this.scale != 0) return;

		int baseY = this.boundingBox.minY();

		for (int i = 0; i < 5; i++) {
			Direction side = Direction.fromAxisAndDirection(axis, random.nextBoolean() ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE).getClockWise();

			BlockPos randomPos = BoundingBoxUtils.lerpPosInside(this.boundingBox, axis, Mth.lerp(random.nextFloat(), 0.05f, 0.95f)).relative(side, random.nextIntBetweenInclusive(2, 4));

			FrontAndTop orientation = FrontAndTop.fromFrontAndTop(side, Direction.UP);
			// int baseY = context.chunkGenerator().getBaseHeight(randomPos.getX(), randomPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

			Identifier templateId = lichTowerUtil.rollGrave(random);
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(randomPos.atY(baseY - 1), BlockPos.ZERO, orientation, context.structureTemplateManager(), templateId, "twilightforest:lich_tower/grave", random);

			if (placeableJunction == null) continue;

			LichYardGrave grave = new LichYardGrave(context.structureTemplateManager(), placeableJunction, templateId);
			if (pieces.findCollisionPiece(grave.getBoundingBox()) == null) {
				pieces.addPiece(grave);
				grave.addJigsaws(parent, pieces, context);
			}
		}
	}

	private void placeYardTrees(WorldGenLevel level, ChunkGenerator chunkGen, RandomSource random, BoundingBox area) {
		Registry<ConfiguredFeature<?, ?>> featureRegistry = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);

		for (int i = 0; i < 6; i++) {
			int treeX = area.minX() + random.nextInt(area.getXSpan());
			int treeZ = area.minZ() + random.nextInt(area.getZSpan());
			int treeY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, treeX, treeZ);
			BlockPos pos = new BlockPos(treeX, treeY, treeZ);

			if (this.pathBoxes != null) {
				boolean onPath = false;
				for (BoundingBox pathBox : this.pathBoxes) {
					if (treeX >= pathBox.minX() && treeX <= pathBox.maxX() && treeZ >= pathBox.minZ() && treeZ <= pathBox.maxZ()) {
						onPath = true;
						break;
					}
				}
				if (onPath) continue;
			}

			if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).is(BlockTags.DIRT)) {
				ResourceKey<ConfiguredFeature<?, ?>> treeKey = random.nextBoolean()
					? TFConfiguredFeatures.TWILIGHT_OAK_TREE
					: TFConfiguredFeatures.CANOPY_TREE;

				if (featureRegistry.get(treeKey).isPresent()) {
					featureRegistry.get(treeKey).get().value().place(level, chunkGen, random, pos);
				}
			}
		}
	}

	@Override
	public int getSortKey() {
		return this.doDirtMotley ? Integer.MIN_VALUE : Integer.MIN_VALUE + 255;
	}

	@Override
	public int getSpawnIndex() {
		return LichTowerPieces.YARD_SPAWNS;
	}
}
