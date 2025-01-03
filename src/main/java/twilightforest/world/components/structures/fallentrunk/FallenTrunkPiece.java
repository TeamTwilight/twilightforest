package twilightforest.world.components.structures.fallentrunk;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
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
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.structures.TerraformingPiece;
import twilightforest.world.components.structures.type.FallenTrunkStructure;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FallenTrunkPiece extends StructurePiece {
	public static final BlockStateProvider DEFAULT_LOG = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get());

	public static final int ERODED_LENGTH = 3;
	protected static final float MOSS_CHANCE = 0.44F;
	protected static final List<EntityType<?>> SPAWNER_MONSTERS = List.of(TFEntities.SWARM_SPIDER.get(), TFEntities.HOSTILE_WOLF.get(), EntityType.CAVE_SPIDER);
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
		this.length = tag.getInt("length");
		this.radius = tag.getInt("radius");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
		log = BlockStateProvider.CODEC.parse(ops, tag.getCompound("log")).result().orElse(DEFAULT_LOG);
		chestLootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString("chest_loot_table")));
		this.holeSeed = tag.getInt("hole_seed");
		this.hole = new Hole(this, RandomSource.create(holeSeed));
	}

	@Override
	protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("length", this.length);
		tag.putInt("radius", this.radius);
		tag.put("log", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.log).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.putString("chest_loot_table", this.chestLootTable.location().toString());
		tag.putLong("hole_seed", this.holeSeed);
	}

	@Override
	public void addChildren(@NotNull StructurePiece parent, StructurePieceAccessor list, @NotNull RandomSource rand) {
		StructurePiece terraformingPiece = new TerraformingPiece(0, boundingBox.inflatedBy(16));
		list.addPiece(terraformingPiece);
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

				generateTrunkRod(level, random, box, pos, dx, dy, hasHole, hole);
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

				generateTrunkRod(level, random, box, pos, dx, dy, hasHole, hole);
			}
		}
	}

	private void generateTrunkRod(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole, Hole hole) {
		generateTrunkMainRod(level, random, box, pos, dx, dy, hasHole, hole);
		generateErodedEnds(level, random, box, pos, dx, dy, hasHole, hole);
	}

	private void generateTrunkMainRod(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole, Hole hole) {
		for (int dz = ERODED_LENGTH; dz < length - 1 - ERODED_LENGTH; dz++) {
			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(random, offsetPos), dx, dy, dz, box, random, hasHole, hole);
		}
	}

	private void generateErodedEnds(WorldGenLevel level, RandomSource random, BoundingBox box, BlockPos pos, int dx, int dy, boolean hasHole, Hole hole) {
		for (int dz = ERODED_LENGTH - 1; dz >= 0; dz--) {
			if (random.nextBoolean())
				break;

			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(random, offsetPos), dx, dy, dz, box, random, hasHole, hole);
		}

		for (int dz = length - 1 - ERODED_LENGTH; dz < length - 1; dz++) {
			if (random.nextBoolean())
				break;

			BlockPos offsetPos = pos.offset(dx, dy, dz);
			this.placeLog(level, getLogState(random, offsetPos), dx, dy, dz, box, random, hasHole, hole);
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

	private BlockState getLogState(RandomSource random, BlockPos pos) {
		return log.getState(random, pos).trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
	}

	private void placeLog(WorldGenLevel level, BlockState blockstate, int x, int y, int z, BoundingBox boundingbox, RandomSource random, boolean hasHole, Hole hole) {
		// getBlock() returns air outside the chunk we are generating, we create new random to call random.next() constant amount of times in this function
		RandomSource randomChild = RandomSource.create(random.nextLong());
		int holeCoordinates = convertXYtoLength(x, y);
		if (hasHole && z > ERODED_LENGTH && z < length - 1 - ERODED_LENGTH - 1 && hole.isInHole(holeCoordinates, z - ERODED_LENGTH - 1)) {
			return;
		}
		BlockState blockState = this.getBlock(level, x, y, z, boundingbox);
		if (blockState.is(BlockTags.REPLACEABLE_BY_TREES) || blockState.is(BlockTags.FLOWERS) || blockState.isEmpty() || randomChild.nextBoolean()) {
			placeBlock(level, blockstate, x, y, z, boundingbox);
			if (randomChild.nextFloat() <= MOSS_CHANCE && this.getBlock(level, x, y + 1, z, boundingbox).is(BlockTags.REPLACEABLE)) {
				placeBlock(level, TFBlocks.MOSS_PATCH.get().defaultBlockState(), x, y + 1, z, boundingbox);
				level.blockUpdated(getWorldPos(x, y + 1, z), TFBlocks.MOSS_PATCH.get());  // to connect moss patches
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

	public boolean isHoleCoveredByHill(HollowHillFunction hollowHillFunction) {
		for (int length = 0; length < hole.sizeXY; length++) {
			for (int z = 0; z < hole.sizeZ; z++) {
				if (!hole.isInHole(length, z))
					continue;
				int[] xy = convertLengthToXY(length);
				int x = xy[0];
				int y = xy[1];

				int zOffset = ERODED_LENGTH + 1;  // hole coordinates has offset because of eroded ends
				BlockPos worldPos = getWorldPos(x, y, z + zOffset);
				int worldX = worldPos.getX();
				int worldY = worldPos.getY();
				int worldZ = worldPos.getZ();

				if (checkForMoundAroundTheBlock(worldX, worldY, worldZ, hollowHillFunction))
					return true;
			}
		}
		return false;
	}

	private boolean checkForMoundAroundTheBlock(int x, int y, int z, HollowHillFunction hollowHillFunction) {
		float hillX = x - hollowHillFunction.centerX();
		float hillY = y - hollowHillFunction.bottomY();
		float hillZ = z - hollowHillFunction.centerZ();
		for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (hollowHillFunction.compute(hillX + dx, hillY, hillZ + dz) > 0)
						return true;
				}
			}
		return false;
	}
}
