package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Deprecated
public abstract class TFStructureComponentOld extends TFStructureComponent implements PieceBeardifierModifier {

	protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
	private static final StrongholdStones strongholdStones = new StrongholdStones();

	public TFStructureComponentOld(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	@Deprecated // Use Below
	public TFStructureComponentOld(StructurePieceType type, int i, int x, int y, int z) {
		super(type, i, new BoundingBox(x, y, z, x, y, z));
	}

	public TFStructureComponentOld(StructurePieceType type, int i, BoundingBox box) {
		super(type, i, box);
	}

	//Let's not use vanilla's weird rotation+mirror thing...
	@Override
	public void setOrientation(@Nullable Direction facing) {
		this.orientation = facing;
		this.mirror = Mirror.NONE;

		if (facing == null) {
			this.rotation = Rotation.NONE;
		} else {
			switch (facing) {
				case SOUTH -> this.rotation = Rotation.CLOCKWISE_180;
				case WEST -> this.rotation = Rotation.COUNTERCLOCKWISE_90;
				case EAST -> this.rotation = Rotation.CLOCKWISE_90;
				default -> this.rotation = Rotation.NONE;
			}
		}
	}

	/**
	 * Fixed a bug with direction 1 and -z values, but I'm not sure if it'll break other things
	 */
	public static BoundingBox getComponentToAddBoundingBox2(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Direction dir) {
		return switch (dir) {
			case WEST -> // '\001'
					new BoundingBox(x - maxZ - minZ, y + minY, z + minX, x - minZ, y + maxY + minY, z + maxX + minX);
			case NORTH -> // '\002'
					new BoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);
			case EAST -> // '\003'
					new BoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z - minX);
			default -> // '\0'
					new BoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);
		};
	}

	protected void setSpawner(WorldGenLevel world, Vec3i pos, BoundingBox sbb, EntityType<?> monsterID) {
		setSpawner(world, pos.getX(), pos.getY(), pos.getZ(), sbb, monsterID, v -> {});
	}

	protected void setSpawner(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, EntityType<?> monsterID) {
		setSpawner(world, x, y, z, sbb, monsterID, v -> {});
	}

	// [VanillaCopy] Keep pinned to signature of setBlockState (no state arg)
	protected void setSpawner(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, EntityType<?> monsterID, Consumer<SpawnerBlockEntity> spawnerModifier) {
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

		BlockPos pos = new BlockPos(dx, dy, dz);

		setSpawnerInWorld(world, sbb, monsterID, spawnerModifier, pos);
	}

	protected static void setSpawnerInWorld(WorldGenLevel world, BoundingBox sbb, EntityType<?> monsterID, Consumer<SpawnerBlockEntity> spawnerModifier, BlockPos pos) {
		if (sbb.isInside(pos)) {
			if (world.getBlockState(pos).getBlock() != Blocks.SPAWNER)
				world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

			BlockEntity tileEntitySpawner = world.getBlockEntity(pos);
			if (tileEntitySpawner instanceof SpawnerBlockEntity spawner) {
				spawner.setEntityId(monsterID, world.getRandom());
				spawnerModifier.accept(spawner);
			}
		}
	}

	protected void surroundBlockCardinal(WorldGenLevel world, BlockState block, int x, int y, int z, BoundingBox sbb) {
		placeBlock(world, block, x, y, z - 1, sbb);
		placeBlock(world, block, x, y, z + 1, sbb);
		placeBlock(world, block, x - 1, y, z, sbb);
		placeBlock(world, block, x + 1, y, z, sbb);
	}

	protected void surroundBlockCardinalRotated(WorldGenLevel world, BlockState block, int x, int y, int z, BoundingBox sbb) {
		placeBlock(world, block.setValue(StairBlock.FACING, Direction.NORTH), x, y, z - 1, sbb);
		placeBlock(world, block.setValue(StairBlock.FACING, Direction.SOUTH), x, y, z + 1, sbb);
		placeBlock(world, block.setValue(StairBlock.FACING, Direction.WEST), x - 1, y, z, sbb);
		placeBlock(world, block.setValue(StairBlock.FACING, Direction.EAST), x + 1, y, z, sbb);
	}

	protected void surroundBlockCorners(WorldGenLevel world, BlockState block, int x, int y, int z, BoundingBox sbb) {
		placeBlock(world, block, x - 1, y, z - 1, sbb);
		placeBlock(world, block, x - 1, y, z + 1, sbb);
		placeBlock(world, block, x + 1, y, z - 1, sbb);
		placeBlock(world, block, x + 1, y, z + 1, sbb);
	}

	protected void setSpawnerRotated(WorldGenLevel world, int x, int y, int z, Rotation rotation, EntityType<?> monsterID, BoundingBox sbb) {
		Direction oldBase = fakeBaseMode(rotation);
		setSpawner(world, x, y, z, sbb, monsterID);
		setOrientation(oldBase);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 */
	protected void placeTreasureAtCurrentPosition(WorldGenLevel world, int x, int y, int z, TFLootTables treasureType, BoundingBox sbb) {
		this.placeTreasureAtCurrentPosition(world, x, y, z, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 */
	protected void placeTreasureAtCurrentPosition(WorldGenLevel world, int x, int y, int z, TFLootTables treasureType, boolean trapped, BoundingBox sbb) {
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);
		this.placeTreasureAtWorldPosition(world, treasureType, trapped, sbb, new BlockPos(dx, dy, dz));
	}

	protected void placeTreasureAtWorldPosition(WorldGenLevel world, TFLootTables treasureType, boolean trapped, BoundingBox sbb, BlockPos pos) {
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST)) {
			treasureType.generateChest(world, pos, this.getOrientation(), trapped);
		}
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 */
	protected void placeTreasureRotated(WorldGenLevel world, int x, int y, int z, Direction facing, Rotation rotation, TFLootTables treasureType, BoundingBox sbb) {
		this.placeTreasureRotated(world, x, y, z, facing, rotation, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 */
	protected void placeTreasureRotated(WorldGenLevel world, int x, int y, int z, Direction facing, Rotation rotation, TFLootTables treasureType, boolean trapped, BoundingBox sbb) {
		if(facing == null) {
			TwilightForestMod.LOGGER.error("Loot Chest at {}, {}, {} has null direction, setting it to north", x, y, z);
			facing = Direction.NORTH;
		}

		int dx = getXWithOffsetRotated(x, z, rotation);
		int dy = getWorldY(y);
		int dz = getZWithOffsetRotated(x, z, rotation);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST)) {
			treasureType.generateChest(world, pos, facing, trapped);
		}
	}

	protected void manualTreaurePlacement(WorldGenLevel world, int x, int y, int z, Direction facing, TFLootTables treasureType, boolean trapped, BoundingBox sbb) {
		int lootx = getWorldX(x, z);
		int looty = getWorldY(y);
		int lootz = getWorldZ(x, z);
		BlockPos lootPos = new BlockPos(lootx, looty, lootz);
		this.placeBlock(world, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.TYPE, ChestType.LEFT).setValue(ChestBlock.FACING, facing), x, y, z, sbb);
		treasureType.generateChestContents(world, lootPos);
	}

	//when adding a loot table to a chest using this method, please be aware it places 2 of the same loot table, one for each chest
	protected void setDoubleLootChest(WorldGenLevel world, int x, int y, int z, int otherx, int othery, int otherz, @Nullable Direction facing, TFLootTables treasureType, BoundingBox sbb, boolean trapped) {
		this.setDoubleLootChest(world, x, y, z, otherx, othery, otherz, facing, treasureType, treasureType, sbb, trapped);
	}

	protected void setDoubleLootChest(WorldGenLevel world, int x, int y, int z, int otherx, int othery, int otherz, @Nullable Direction facing, TFLootTables treasureType, TFLootTables secondaryLootType, BoundingBox sbb, boolean trapped) {
		if (facing == null) {
			TwilightForestMod.LOGGER.error("Loot Chest at {}, {}, {} has null direction, setting it to north", x, y, z);
			facing = Direction.NORTH;
		}

		boolean flipContents = world.getRandom().nextBoolean();
		BlockPos firstChestPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
		BlockPos secondChestPos = new BlockPos(this.getWorldX(otherx, otherz), this.getWorldY(othery), this.getWorldZ(otherx, otherz));

		this.placeBlock(world, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.TYPE, ChestType.LEFT).setValue(ChestBlock.FACING, facing), x, y, z, sbb);
		this.placeBlock(world, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.TYPE, ChestType.RIGHT).setValue(ChestBlock.FACING, facing), otherx, othery, otherz, sbb);
		treasureType.generateChestContents(world, flipContents ? secondChestPos : firstChestPos);
		secondaryLootType.generateChestContents(world, flipContents ? firstChestPos : secondChestPos);
	}

	/**
	 * Places a tripwire.
	 * <p>
	 * Tries to delay notifying tripwire blocks of placement so they won't
	 * scan unloaded chunks looking for connections.
	 *
	 */
	protected void placeTripwire(WorldGenLevel world, int x, int y, int z, int size, Direction facing, BoundingBox sbb) {

		int dx = facing.getStepX();
		int dz = facing.getStepZ();

		// add tripwire hooks
		BlockState tripwireHook = Blocks.TRIPWIRE_HOOK.defaultBlockState();
		placeBlock(world, tripwireHook.setValue(TripWireHookBlock.FACING, facing.getOpposite()), x, y, z, sbb);
		placeBlock(world, tripwireHook.setValue(TripWireHookBlock.FACING, facing), x + dx * size, y, z + dz * size, sbb);

		// add string
		BlockState tripwire = Blocks.TRIPWIRE.defaultBlockState();
		for (int i = 1; i < size; i++) {
			placeBlock(world, tripwire, x + dx * i, y, z + dz * i, sbb);
		}
	}

	protected void placeSignAtCurrentPosition(WorldGenLevel world, int x, int y, int z, String string0, String string1, BoundingBox sbb) {
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos) && world.getBlockState(pos).getBlock() != Blocks.OAK_SIGN) {
			world.setBlock(pos, Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, this.getOrientation().get2DDataValue() * 4), 2);

			if (world.getBlockEntity(pos) instanceof SignBlockEntity sign) {
				sign.frontText = sign.frontText.setMessage(1, Component.literal(string0)).setMessage(2, Component.literal(string1));
			}
		}
	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	protected int[] offsetTowerCoords(int x, int y, int z, int towerSize, Direction direction) {

		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

		if (direction == Direction.SOUTH) {
			return new int[]{dx + 1, dy - 1, dz - towerSize / 2};
		} else if (direction == Direction.WEST) {
			return new int[]{dx + towerSize / 2, dy - 1, dz + 1};
		} else if (direction == Direction.NORTH) {
			return new int[]{dx - 1, dy - 1, dz + towerSize / 2};
		} else if (direction == Direction.EAST) {
			return new int[]{dx - towerSize / 2, dy - 1, dz - 1};
		}


		// ugh?
		return new int[]{x, y, z};
	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int towerSize, Direction direction) {

		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

		switch (direction) {
			case SOUTH:
				return new BlockPos(dx + 1, dy - 1, dz - towerSize / 2);
			case WEST:
				return new BlockPos(dx + towerSize / 2, dy - 1, dz + 1);
			case NORTH:
				return new BlockPos(dx - 1, dy - 1, dz + towerSize / 2);
			case EAST:
				return new BlockPos(dx - towerSize / 2, dy - 1, dz - 1);
			default:
				break;
		}

		// ugh?
		return new BlockPos(x, y, z);
	}

	@Override
	protected int getWorldX(int x, int z) {
		//return super.getXWithOffset(x, z);
		// [VanillaCopy] of super, edits noted.
		Direction enumfacing = this.getOrientation();

		if (enumfacing == null) {
			return x;
		} else {
			return switch (enumfacing) {
				case SOUTH -> this.boundingBox.minX() + x;
				case WEST -> this.boundingBox.maxX() - z;
				case NORTH -> this.boundingBox.maxX() - x; // TF - Add case for NORTH
				case EAST -> this.boundingBox.minX() + z;
				default -> x;
			};
		}
	}

	@Override
	protected int getWorldZ(int x, int z) {
		//return super.getZWithOffset(x, z);
		// [VanillaCopy] of super, edits noted.
		Direction enumfacing = this.getOrientation();

		if (enumfacing == null) {
			return z;
		} else {
			return switch (enumfacing) {
				case SOUTH -> this.boundingBox.minZ() + z;
				case WEST -> this.boundingBox.minZ() + x;
				case NORTH -> this.boundingBox.maxZ() - z;
				case EAST -> this.boundingBox.maxZ() - x;
				default -> z;
			};
		}
	}

	private Direction fakeBaseMode(Rotation rotationsCW) {
		final Direction oldBaseMode = getOrientation();

		if (oldBaseMode != null) {
			Direction pretendBaseMode = oldBaseMode;
			pretendBaseMode = rotationsCW.rotate(pretendBaseMode);

			setOrientation(pretendBaseMode);
		}

		return oldBaseMode;
	}

	// [VanillaCopy] Keep pinned to the signature of getXWithOffset
	protected int getXWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		int ret = getWorldX(x, z);
		setOrientation(oldMode);
		return ret;
	}

	// [VanillaCopy] Keep pinned to the signature of getZWithOffset
	protected int getZWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		int ret = getWorldZ(x, z);
		setOrientation(oldMode);
		return ret;
	}

	protected void setBlockStateRotated(WorldGenLevel world, BlockState state, int x, int y, int z, Rotation rotationsCW, BoundingBox sbb) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		placeBlock(world, state, x, y, z, sbb);
		setOrientation(oldMode);
	}

	@Override
	protected BlockState getBlock(BlockGetter world, int x, int y, int z, BoundingBox sbb) {
		// Making public
		return super.getBlock(world, x, y, z, sbb);
	}

	@Override
	protected void placeBlock(WorldGenLevel worldIn, BlockState blockstateIn, int x, int y, int z, BoundingBox sbb) {
		// Making public
		super.placeBlock(worldIn, blockstateIn, x, y, z, sbb);
	}

	// [VanillaCopy] Keep pinned to the signature of getBlockStateFromPos
	public BlockState getBlockStateFromPosRotated(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		BlockState ret = getBlock(world, x, y, z, sbb);
		setOrientation(oldMode);
		return ret;
	}

	protected void fillBlocksRotated(WorldGenLevel world, BoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState state, Rotation rotation) {
		Direction oldBase = fakeBaseMode(rotation);
		generateBox(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, state, state, false);
		setOrientation(oldBase);
	}

	// [VanillaCopy] Keep pinned on signature of fillWithBlocksRandomly (though passing false for excludeAir)
	protected void randomlyFillBlocksRotated(WorldGenLevel worldIn, BoundingBox boundingboxIn, RandomSource rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockstate1, BlockState blockstate2, Rotation rotation) {
		Direction oldBase = fakeBaseMode(rotation);
		final boolean minimumLightLevel = true;
		generateMaybeBox(worldIn, boundingboxIn, rand, chance, minX, minY, minZ, maxX, maxY, maxZ, blockstate1, blockstate2, false, minimumLightLevel);
		setOrientation(oldBase);
	}

	// [VanillaCopy] Keep pinned to signature of replaceAirAndLiquidDownwards
	public void replaceAirAndLiquidDownwardsRotated(WorldGenLevel world, BlockState state, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		Direction oldBaseMode = fakeBaseMode(rotation);
		fillColumnDown(world, state, x, y, z, sbb);
		setOrientation(oldBaseMode);
	}

	protected void fillAirRotated(WorldGenLevel world, BoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Rotation rotation) {
		Direction oldBaseMode = fakeBaseMode(rotation);
		generateAirBox(world, sbb, minX, minY, minZ, maxX, maxY, maxZ);
		setOrientation(oldBaseMode);
	}

	protected void fillWithAir(WorldGenLevel world, BoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Predicate<BlockState> predicate) {
		fillWithBlocks(world, boundingBox, xMin, yMin, zMin, xMax, yMax, zMax, Blocks.AIR.defaultBlockState(), predicate);
	}

	protected void fillWithBlocks(WorldGenLevel world, BoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState state, Predicate<BlockState> predicate) {
		fillWithBlocks(world, boundingBox, xMin, yMin, zMin, xMax, yMax, zMax, state, state, predicate);
	}

	protected void fillWithBlocks(WorldGenLevel world, BoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState borderState, BlockState interiorState, Predicate<BlockState> predicate) {
		for (int y = yMin; y <= yMax; ++y) {
			for (int x = xMin; x <= xMax; ++x) {
				for (int z = zMin; z <= zMax; ++z) {

					if (predicate.test(this.getBlock(world, x, y, z, boundingBox))) {

						boolean isBorder = yMin != yMax && (y == yMin || y == yMax)
								|| xMin != xMax && (x == xMin || x == xMax)
								|| zMin != zMax && (z == zMin || z == zMax);

						this.placeBlock(world, isBorder ? borderState : interiorState, x, y, z, boundingBox);
					}
				}
			}
		}
	}

	protected static StructurePiece.BlockSelector getStrongholdStones() {
		return strongholdStones;
	}

	protected Direction getStructureRelativeRotation(Rotation rotationsCW) {
		return rotationsCW.rotate(getOrientation());
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 * <p>
	 * This is basically copied from ComponentVillage
	 */
	protected int getAverageGroundLevel(WorldGenLevel world, ChunkGenerator generator, BoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;
		int yStart = Mth.clamp(generator.getSeaLevel(), this.boundingBox.minY(), this.boundingBox.maxY());

		for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
			for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
				BlockPos pos = new BlockPos(x, yStart, z);

				if (boundingBox.isInside(pos)) {
					final BlockPos topPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
					yTotal += Math.max(topPos.getY(), generator.getSpawnHeight(world));
					++count;
				}
			}
		}

		if (count == 0) {
			return Integer.MIN_VALUE;
		} else {
			return yTotal / count;
		}
	}

	/**
	 * Find what y-level the ground is. Just check the center of the chunk we're given.
	 */
	protected int findGroundLevel(WorldGenLevel world, BoundingBox sbb, int start, Predicate<BlockState> predicate) {

		Vec3i center = BoundingBoxUtils.getCenter(sbb);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(center.getX(), 0, center.getZ());

		for (int y = start; y > 0; y--) {
			pos.setY(y);
			if (predicate.test(world.getBlockState(pos))) {
				return y;
			}
		}

		return 0;
	}

	protected boolean isBoundingBoxOutsideBiomes(WorldGenLevel world, Predicate<Biome> predicate) {

		int minX = this.boundingBox.minX() - 1;
		int minZ = this.boundingBox.minZ() - 1;
		int maxX = this.boundingBox.maxX() + 1;
		int maxZ = this.boundingBox.maxZ() + 1;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (!predicate.test(world.getBiome(pos.set(x, 0, z)).value())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Discover if bounding box can fit within the current bounding box object.
	 */
	@Nullable
	public static StructurePiece findIntersectingExcluding(List<StructurePiece> list, BoundingBox toCheck, StructurePiece exclude) {
		Iterator<StructurePiece> iterator = list.iterator();
		StructurePiece structurecomponent;

		do {
			if (!iterator.hasNext()) {
				return null;
			}

			structurecomponent = iterator.next();
		}
		while (structurecomponent == exclude || structurecomponent.getBoundingBox() == null || !structurecomponent.getBoundingBox().intersects(toCheck));

		return structurecomponent;
	}

	public BlockPos getBlockPosWithOffset(int x, int y, int z) {
		return new BlockPos(
				getWorldX(x, z),
				getWorldY(y),
				getWorldZ(x, z)
		);
	}

	/* BlockState Helpers */
	protected static BlockState getStairState(BlockState stairState, Direction direction, boolean isTopHalf) {
		return stairState
				.setValue(StairBlock.FACING, direction)
				.setValue(StairBlock.HALF, isTopHalf ? Half.TOP : Half.BOTTOM);
	}

	protected static BlockState getSlabState(BlockState inputBlockState, SlabType half) {
		return inputBlockState
				.setValue(SlabBlock.TYPE, half);
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
}
