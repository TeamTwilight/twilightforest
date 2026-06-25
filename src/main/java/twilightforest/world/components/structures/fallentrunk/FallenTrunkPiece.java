package twilightforest.world.components.structures.fallentrunk;

import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.UtilityPiece;
import twilightforest.world.components.structures.type.FallenTrunkStructure;

import java.util.*;

public class FallenTrunkPiece extends StructurePiece {
	public static final BlockStateProvider DEFAULT_LOG = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get());

	public static final int ERODED_LENGTH = 2;
	protected static final float MOSS_CHANCE = 0.44F;
	protected static final List<EntityType<?>> SPAWNER_MONSTERS = List.of(TFEntities.SWARM_SPIDER.get(), TFEntities.HOSTILE_WOLF.get(), EntityType.CAVE_SPIDER);
	public static final int TERRAFORM_PIECE_SIZE = 10;
	public static final int FEATURE_PIECE_SIZE = 3;
	protected final BlockStateProvider log;
	public final int length;
	public final int radius;
	protected final ResourceKey<LootTable> chestLootTable;
	private final long holeSeed;
	protected final Hole hole;

	public FallenTrunkPiece(int length, int radius, BlockStateProvider log, ResourceKey<LootTable> chestLootTable, Direction orientation, BoundingBox boundingBox, long seed) {
		super(TFStructurePieceTypes.TFFallenTrunk.value(), 0, boundingBox);
		this.length = length;
		this.radius = radius;
		this.log = log;
		this.chestLootTable = chestLootTable;
		this.holeSeed = seed;
		this.hole = new Hole(this, RandomSource.create(holeSeed));
		setOrientation(orientation);
	}

	public FallenTrunkPiece(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFFallenTrunk.value(), tag);
		this.length = tag.getIntOr("length", 0);
		this.radius = tag.getIntOr("radius", 0);

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
		log = BlockStateProvider.CODEC.parse(ops, tag.getCompoundOrEmpty("log")).result().orElse(DEFAULT_LOG);
		chestLootTable = ResourceKey.create(Registries.LOOT_TABLE, Identifier.parse(tag.getStringOr("chest_loot_table", "")));
		this.holeSeed = tag.getIntOr("hole_seed", 0);
		this.hole = new Hole(this, RandomSource.create(holeSeed));
	}

	@Override
	protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("length", this.length);
		tag.putInt("radius", this.radius);
		tag.put("log", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.log).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.putString("chest_loot_table", this.chestLootTable.identifier().toString());
		tag.putLong("hole_seed", this.holeSeed);
	}

	@Override
	public void addChildren(@NotNull StructurePiece parent, StructurePieceAccessor list, @NotNull RandomSource rand) {
		StructurePiece terraformingPiece = new UtilityPiece(0, boundingBox.inflatedBy(TERRAFORM_PIECE_SIZE));
		list.addPiece(terraformingPiece);

		StructurePiece featurePiece = new UtilityPiece(0, boundingBox.inflatedBy(FEATURE_PIECE_SIZE), false);
		list.addPiece(featurePiece);
	}

	@Override
	public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureManager structureManager, @NotNull ChunkGenerator generator, @NotNull RandomSource randomSource,
							@NotNull BoundingBox box, @NotNull ChunkPos chunkPos, @NotNull BlockPos pos) {
		RandomSource random = RandomSource.create(pos.asLong());
		if (radius == FallenTrunkStructure.radiuses.get(0))
			generateSmallFallenTrunk(level, random, box, pos, random.nextBoolean());
		if (radius == FallenTrunkStructure.radiuses.get(1))
			generateFallenTrunk(level, random, box, pos, random.nextBoolean(), false);
		if (radius == FallenTrunkStructure.radiuses.get(2))
			generateFallenTrunk(level, random, box, pos, false, random.nextBoolean());
	}

	private void generateSmallFallenTrunk(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, boolean hasHole) {
		for (int dx = 0; dx <= 3; dx++) {
			for (int dy = 0; dy <= 3; dy++) {
				if (Math.abs(dx - 1.5) + Math.abs(dy - 1.5) != 2)
					continue;

				generateTrunkRod(level, random, box, pos, dx, dy, hasHole);
			}
		}
	}

	private void generateFallenTrunk(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, boolean hasHole, boolean hasSpawnerAndChests) {
		generateTrunk(level, random, box, pos, hasHole);
		if (hasSpawnerAndChests)
			generateSpawnerAndChests(level, random, box);
	}

	private void generateTrunk(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, boolean hasHole) {
		int hollow = radius / 2;
		int diameter = radius * 2;

		for (int dx = 0; dx <= diameter; dx++) {
			for (int dy = 0; dy <= diameter; dy++) {
				int dist = getDist(dx, dy);
				if (dist > radius || dist <= hollow)
					continue;

				generateTrunkRod(level, random, box, pos, dx, dy, hasHole);
			}
		}
	}

	private void generateTrunkRod(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole) {
		generateTrunkMainRod(level, random, box, pos, dx, dy, hasHole);
		generateErodedEnds(level, random, box, pos, dx, dy, hasHole);
	}

	private void generateTrunkMainRod(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole) {
		for (int dz = ERODED_LENGTH; dz < length - 1 - ERODED_LENGTH; dz++) {
			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(level, random, offsetPos), dx, dy, dz, box, random, hasHole);
		}
	}

	private void generateErodedEnds(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole) {
		for (int dz = ERODED_LENGTH - 1; dz >= 0; dz--) {
			if (random.nextBoolean())
				break;

			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(level, random, offsetPos), dx, dy, dz, box, random, hasHole);
		}

		for (int dz = length - 1 - ERODED_LENGTH; dz < length - 1; dz++) {
			if (random.nextBoolean())
				break;

			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(level, random, offsetPos), dx, dy, dz, box, random, hasHole);
		}
	}


	private void generateSpawnerAndChests(WorldGenLevel level, RandomSource random, BoundingBox box) {
		BlockPos spawnerPos = new BlockPos(radius, 2, (length - 1) / 2);
		this.placeBlock(level, Blocks.SPAWNER.defaultBlockState(), spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ(), box);
		BlockPos worldSpawnerPos = getWorldPos(spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ());

		// Don't use main random for spawner due to desyncs in random.next() calls between chunks, causing 2 chests or other bugs
		RandomSource spawnerRandom = RandomSource.create(random.nextLong());

		if (box.isInside(worldSpawnerPos.getX(), worldSpawnerPos.getY(), worldSpawnerPos.getZ()) && level.getBlockEntity(worldSpawnerPos) instanceof SpawnerBlockEntity spawner)
			spawner.setEntityId(Util.getRandom(SPAWNER_MONSTERS, spawnerRandom), spawnerRandom);

		Direction orientation = this.getOrientation().getClockWise();
		if (this.mirror == Mirror.LEFT_RIGHT)
			orientation = orientation.getOpposite();


		Set<Vec3i> possibleChestsOffsets = new HashSet<>();
		for (int i = 0; i <= 6; i++) {
			possibleChestsOffsets.add(new Vec3i(2, 1, -3 + i));
		}
		possibleChestsOffsets.add(new Vec3i(1, 0, -3));
		possibleChestsOffsets.add(new Vec3i(1, 0, 2));
		for(Vec3i vec3i : possibleChestsOffsets.stream().toList()) {
			possibleChestsOffsets.add(new Vec3i(-vec3i.getX(), vec3i.getY(), vec3i.getZ()));
		}

		Vec3i chestOffset = Util.getRandom(possibleChestsOffsets.stream().toList(), random);
		BlockPos chestSpawnerPos = spawnerPos.offset(chestOffset);

		BlockState chestState = TFBlocks.TWILIGHT_OAK_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, chestOffset.getX() < 0 ? orientation : orientation.getOpposite());
		BlockPos chestPos = getWorldPos(chestSpawnerPos.getX(), chestSpawnerPos.getY(), chestSpawnerPos.getZ());
		RandomSource chestRandom = RandomSource.create(random.nextLong());
		this.createChest(level, box, chestRandom, chestPos, chestLootTable, chestState);
	}

	private int getDist(int dx, int dy) {
		int ax = Math.abs(dx - this.radius);
		int az = Math.abs(dy - this.radius);
		return (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));
	}

	private BlockState getLogState(WorldGenLevel level, RandomSource random, BlockPos pos) {
		return log.getState(level, random, pos).trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
	}

	private void placeLog(WorldGenLevel level, BlockState blockstate, int x, int y, int z, BoundingBox boundingbox, RandomSource random, boolean hasHole) {
		// getBlock() returns air outside the chunk we are generating, we create new random to call random.next() constant amount of times in this function
		RandomSource randomChild = RandomSource.create(random.nextLong());
		if (hasHole && z > ERODED_LENGTH && z < length - 1 - ERODED_LENGTH - 1 && getAllAbsoluteHoleBlockPos().contains(getWorldPos(x, y, z)))
			return;
		BlockState blockState = this.getBlock(level, x, y, z, boundingbox);
		if (blockState.is(BlockTags.REPLACEABLE_BY_TREES) || blockState.is(BlockTags.FLOWERS) || blockState.isEmpty() || randomChild.nextBoolean()) {
			placeBlock(level, blockstate, x, y, z, boundingbox);
			if (randomChild.nextFloat() <= MOSS_CHANCE && boundingbox.isInside(getWorldPos(x, y + 1, z)) && this.getBlock(level, x, y + 1, z, boundingbox).is(BlockTags.REPLACEABLE)) {
				placeBlock(level, TFBlocks.MOSS_PATCH.get().defaultBlockState(), x, y + 1, z, boundingbox);
				level.updateNeighborsAt(getWorldPos(x, y + 1, z), TFBlocks.MOSS_PATCH.get());  // to connect moss patches
				level.getChunk(getWorldPos(x, y + 1, z)).markPosForPostprocessing(getWorldPos(x, y + 1, z));
			}
		}
	}

	@NotNull
	@Override
	public Direction getOrientation() {
		return Objects.requireNonNull(orientation);  // orientation is always not null, just to remove warnings
	}

	protected int getSideLength() {
		return radius == 1 ? 2 : this.radius * 2 - 1;
	}

	protected int convertXYtoLength(int x, int y) {
		return convertXYtoLength(getSideLength(), x, y);
	}

	protected int convertXYtoLength(int sideLength, int x, int y) {
		int length = 0;

		if (x == 0) {
			length = y;
		} else if (y == sideLength + 1) {
			length = sideLength + x;
		} else if (x == sideLength + 1) {
			length = 3 * sideLength - y + 1;
		} else if (y == 0) {
			length = 4 * sideLength - x;
		}

		return length - 1;
	}

	public int[] convertLengthToXY(int length) {
		return convertLengthToXY(getSideLength(), length);
	}

	public int[] convertLengthToXY(int sideLength, int length) {
		if (length < sideLength) {
			return new int[] {0, length + 1};  // Left edge
		} else if (length < 2 * sideLength) {
			return new int[] {length + 1 - sideLength, sideLength + 1};  // Top edge
		} else if (length < 3 * sideLength) {
			return new int[] {sideLength + 1, 3 * sideLength - length};  // Right edge
		} else {
			return new int[] {4 * sideLength - (length + 1), 0};  // Bottom edge
		}
	}

	private Set<BlockPos> getAllAbsoluteHoleBlockPos() {
		int zOffset = ERODED_LENGTH + 1;  // hole z coordinate has offset because of eroded ends
		Set<BlockPos> holeBlockPos = new HashSet<>();
		for (int xy = 0; xy < hole.sizeXY; xy++) {
			for (int z = 0; z < hole.sizeZ; z++) {
				if (hole.isInHole(xy, z)) {
					int[] separatedXY = convertLengthToXY(xy);
					holeBlockPos.add(getWorldPos(separatedXY[0], separatedXY[1], z + zOffset));
				}
			}
		}
		return holeBlockPos;
	}

	// returns all blockPos adjacent hole
	private Set<BlockPos> getAllForbiddenMoundBaseBlockPos() {
		Set<BlockPos> allAbsoluteHoleBlockPos = getAllAbsoluteHoleBlockPos();
		Set<BlockPos> forbiddenBlockPos = new HashSet<>(allAbsoluteHoleBlockPos);
		for (BlockPos blockPos : allAbsoluteHoleBlockPos) {
			for (Direction direction : Direction.values()) {
				forbiddenBlockPos.add(blockPos.relative(direction).atY(boundingBox.minY()));
			}
		}

		return forbiddenBlockPos;
	}

	public Set<BlockPos> getAllPotentialBaseMoundBlockPos(boolean isOnRightSide) {
		Set<BlockPos> potentialBlockPos = new HashSet<>();
		// It's difficult to calculate the size of the mound because of different biases. So, use just "safe" distance (determined experimentally)
		int forbiddenLength = Math.ceilDiv(length, 4) + ERODED_LENGTH;
		for (int offset = forbiddenLength; offset <= length - forbiddenLength; offset++) {
			if (isOnRightSide)
				potentialBlockPos.add(getWorldPos(Math.min(boundingBox.getXSpan(), boundingBox.getZSpan()) - 1, 0, offset));
			else
				potentialBlockPos.add(getWorldPos(0, 0, offset));
		}
		return potentialBlockPos;
	}

	public Set<BlockPos> getAllowedBaseMoundBlockPos(boolean isOnRightSide) {
		Set<BlockPos> allowedBlockPos = getAllPotentialBaseMoundBlockPos(isOnRightSide);
		allowedBlockPos.removeAll(getAllForbiddenMoundBaseBlockPos());
		return allowedBlockPos;
	}
}
