package twilightforest.world.components.structures.fallentrunk;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.block.state.properties.ChestType;
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

public class FallenTrunkPiece extends StructurePiece {
	public static final BlockStateProvider DEFAULT_LOG = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get());
	public static final Holder<EntityType<?>> DEFAULT_DUNGEON_MONSTER = TFEntities.SWARM_SPIDER;

	public static final int ERODED_LENGTH = 3;
	protected static final float MOSS_CHANCE = 0.44F;
	protected final BlockStateProvider log;
	public final int length;
	public final int radius;
	protected final ResourceKey<LootTable> chestLootTable;
	protected final Holder<EntityType<?>> spawnerMonster;
	private final long holeSeed;
	protected final Hole hole;

	public FallenTrunkPiece(int length, int radius, BlockStateProvider log, ResourceKey<LootTable> chestLootTable, Holder<EntityType<?>> spawnerMonster, Direction orientation, BoundingBox boundingBox, long seed) {
		super(TFStructurePieceTypes.TFFallenTrunk.value(), 0, boundingBox);
		this.length = length;
		this.radius = radius;
		this.log = log;
		this.chestLootTable = chestLootTable;
		this.spawnerMonster = spawnerMonster;
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
		ResourceKey<EntityType<?>> dungeonMonster = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(tag.getString("spawner_monster")));
		this.spawnerMonster = context.registryAccess().registry(Registries.ENTITY_TYPE)
			.<Holder<EntityType<?>>>flatMap(reg -> reg.getHolder(dungeonMonster))
			.orElse(DEFAULT_DUNGEON_MONSTER);
		this.holeSeed = tag.getInt("hole_seed");
		this.hole = new Hole(this, RandomSource.create(holeSeed));
	}

	@Override
	protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("length", this.length);
		tag.putInt("radius", this.radius);
		tag.put("log", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.log).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.putString("chest_loot_table", this.chestLootTable.location().toString());
		tag.putString("spawner_monster", BuiltInRegistries.ENTITY_TYPE.getKey(this.spawnerMonster.value()).toString());
		tag.putLong("hole_seed", this.holeSeed);
	}

	@Override
	public void addChildren(@NotNull StructurePiece parent, StructurePieceAccessor list, @NotNull RandomSource rand) {
		StructurePiece terraformingPiece = new TerraformingPiece(TFStructurePieceTypes.TFFallenTrunk.value(), 0, boundingBox.inflatedBy(16));
		list.addPiece(terraformingPiece);
	}

	@Override
	public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureManager structureManager, @NotNull ChunkGenerator generator, @NotNull RandomSource random,
							@NotNull BoundingBox box, @NotNull ChunkPos chunkPos, @NotNull BlockPos pos) {
		if (radius == FallenTrunkStructure.radiuses.get(0))
			generateSmallFallenTrunk(level, RandomSource.create(pos.asLong()), box, pos, random.nextBoolean());
		if (radius == FallenTrunkStructure.radiuses.get(1))
			generateFallenTrunk(level, RandomSource.create(pos.asLong()), box, pos, random.nextBoolean(), false);
		if (radius == FallenTrunkStructure.radiuses.get(2))
			generateFallenTrunk(level, RandomSource.create(pos.asLong()), box, pos, false, true);
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
		if (box.isInside(worldSpawnerPos.getX(), worldSpawnerPos.getY(), worldSpawnerPos.getZ()) && level.getBlockEntity(worldSpawnerPos) instanceof SpawnerBlockEntity spawner)
			spawner.setEntityId(spawnerMonster.value(), random);

		Direction orientation = this.getOrientation().getClockWise();
		if (this.mirror == Mirror.LEFT_RIGHT)
			orientation = orientation.getOpposite();
		BlockState singleChestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, orientation);
		BlockPos singleChestPos = getWorldPos(spawnerPos.getX() - 1, spawnerPos.getY(), spawnerPos.getZ() + 2);
		this.createChest(level, box, random, singleChestPos, chestLootTable, singleChestState);

		ChestType chestType = mirror != Mirror.NONE ? ChestType.RIGHT : ChestType.LEFT;
		BlockState doubleChest0 = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, orientation.getOpposite()).setValue(ChestBlock.TYPE, chestType);
		BlockPos doubleChestPos0 = getWorldPos(spawnerPos.getX() + 2, spawnerPos.getY() + 1, spawnerPos.getZ() - 3);
		BlockState doubleChest1 = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, orientation.getOpposite()).setValue(ChestBlock.TYPE, chestType.getOpposite());
		BlockPos doubleChestPos1 = getWorldPos(spawnerPos.getX() + 2, spawnerPos.getY() + 1, spawnerPos.getZ() - 2);
		this.createChest(level, box, random, doubleChestPos0, chestLootTable, doubleChest0);
		this.createChest(level, box, random, doubleChestPos1, chestLootTable, doubleChest1);
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
		int holeCoordinates = convertXYtoLength(x, y);
		if (hasHole && z > ERODED_LENGTH && z < length - 1 - ERODED_LENGTH - 1 && hole.isInHole(holeCoordinates, z - ERODED_LENGTH - 1)) {
			return;
		}
		BlockState blockState = this.getBlock(level, x, y, z, boundingbox);
		if (blockState.is(BlockTags.REPLACEABLE_BY_TREES) || blockState.is(BlockTags.FLOWERS) || blockState.isEmpty() || random.nextBoolean()) {
			placeBlock(level, blockstate, x, y, z, boundingbox);
			if (random.nextFloat() <= MOSS_CHANCE && this.getBlock(level, x, y + 1, z, boundingbox).is(BlockTags.REPLACEABLE)) {
				placeBlock(level, TFBlocks.MOSS_PATCH.get().defaultBlockState(), x, y + 1, z, boundingbox);
				level.blockUpdated(getWorldPos(x, y + 1, z), TFBlocks.MOSS_PATCH.get());  // to connect moss patches
				level.getChunk(getWorldPos(x, y + 1, z)).markPosForPostprocessing(getWorldPos(x, y + 1, z));
			}
		}
	}

	@NotNull
	@Override
	public Direction getOrientation() {
		return MoreObjects.firstNonNull(orientation, Direction.NORTH);  // orientation is always not null, just to remove warnings
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
				if (checkForMoundAroundTheBlock(x, y, z, hollowHillFunction))
					return true;
			}
		}
		return false;
	}

	private boolean checkForMoundAroundTheBlock(int x, int y, int z, HollowHillFunction hollowHillFunction) {
		float hillX = x + boundingBox.minX() - hollowHillFunction.centerX();
		float hillY = y + boundingBox.minY() - hollowHillFunction.bottomY();
		float hillZ = z + boundingBox.minZ() - hollowHillFunction.centerZ();
		for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (hollowHillFunction.compute(hillX + dx, hillY, hillZ + dz) > 0)
						return true;
				}
			}
		return false;
	}
}
