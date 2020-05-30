package twilightforest.structures;

import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.BlockSnapshot;
import twilightforest.TFFeature;
import twilightforest.loot.TFTreasure;
import twilightforest.util.StructureBoundingBoxUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Deprecated
public abstract class StructureTFComponentOld extends StructureTFComponent {

	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final StructureTFStrongholdStones strongholdStones = new StructureTFStrongholdStones();

	public StructureTFComponentOld(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public StructureTFComponentOld(IStructurePieceType type, TFFeature feature, int i) {
		super(type, i);
		this.feature = feature;
	}

	@Override
	public TFFeature getFeatureType() {
		return feature;
	}

	//Let's not use vanilla's weird rotation+mirror thing...
	@Override
	public void setCoordBaseMode(@Nullable Direction facing) {
		this.field_74885_f = facing;
		this.mirror = Mirror.NONE;

		if (facing == null) {
			this.rotation = Rotation.NONE;
		} else {
			switch (facing) {
				case SOUTH:
					this.rotation = Rotation.CLOCKWISE_180;
					break;
				case WEST:
					this.rotation = Rotation.COUNTERCLOCKWISE_90;
					break;
				case EAST:
					this.rotation = Rotation.CLOCKWISE_90;
					break;
				default:
					this.rotation = Rotation.NONE;
			}
		}
	}

	public static MutableBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, @Nullable Direction dir) {
		switch (dir) {
			default:
				return new MutableBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case SOUTH: // '\0'
				return new MutableBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case WEST: // '\001'
				return new MutableBoundingBox(x - maxZ + minZ, y + minY, z + minX, x + minZ, y + maxY + minY, z + maxX + minX);

			case NORTH: // '\002'
				return new MutableBoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);

			case EAST: // '\003'
				return new MutableBoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z + minX);
		}
	}

	/**
	 * Fixed a bug with direction 1 and -z values, but I'm not sure if it'll break other things
	 */
	public static MutableBoundingBox getComponentToAddBoundingBox2(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Direction dir) {
		switch (dir) {
			default:
				return new MutableBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case SOUTH: // '\0'
				return new MutableBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case WEST: // '\001'
				return new MutableBoundingBox(x - maxZ - minZ, y + minY, z + minX, x - minZ, y + maxY + minY, z + maxX + minX);

			case NORTH: // '\002'
				return new MutableBoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);

			case EAST: // '\003'
				return new MutableBoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z - minX);
		}
	}

	// [VanillaCopy] Keep pinned to signature of setBlockState (no state arg)
	protected MobSpawnerTileEntity setSpawner(World world, int x, int y, int z, MutableBoundingBox sbb, EntityType<?> monsterID) {
		MobSpawnerTileEntity tileEntitySpawner = null;

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
			world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);
			tileEntitySpawner = (MobSpawnerTileEntity) world.getTileEntity(pos);
			if (tileEntitySpawner != null) {
				tileEntitySpawner.getSpawnerBaseLogic().setEntityType(monsterID);
			}
		}

		return tileEntitySpawner;
	}

	protected void surroundBlockCardinal(World world, BlockState block, int x, int y, int z, MutableBoundingBox sbb) {
		setBlockState(world, block, x + 0, y, z - 1, sbb);
		setBlockState(world, block, x + 0, y, z + 1, sbb);
		setBlockState(world, block, x - 1, y, z + 0, sbb);
		setBlockState(world, block, x + 1, y, z + 0, sbb);
	}

	protected void surroundBlockCardinalRotated(World world, BlockState block, int x, int y, int z, MutableBoundingBox sbb) {
		setBlockState(world, block.with(StairsBlock.FACING, Direction.NORTH), x + 0, y, z - 1, sbb);
		setBlockState(world, block.with(StairsBlock.FACING, Direction.SOUTH), x + 0, y, z + 1, sbb);
		setBlockState(world, block.with(StairsBlock.FACING, Direction.WEST), x - 1, y, z + 0, sbb);
		setBlockState(world, block.with(StairsBlock.FACING, Direction.EAST), x + 1, y, z + 0, sbb);
	}

	protected void surroundBlockCorners(World world, BlockState block, int x, int y, int z, MutableBoundingBox sbb) {
		setBlockState(world, block, x - 1, y, z - 1, sbb);
		setBlockState(world, block, x - 1, y, z + 1, sbb);
		setBlockState(world, block, x + 1, y, z - 1, sbb);
		setBlockState(world, block, x + 1, y, z + 1, sbb);
	}

	protected MobSpawnerTileEntity setSpawnerRotated(World world, int x, int y, int z, Rotation rotation, EntityType<?> monsterID, MutableBoundingBox sbb) {
		Direction oldBase = fakeBaseMode(rotation);
		MobSpawnerTileEntity ret = setSpawner(world, x, y, z, sbb, monsterID);
		setCoordBaseMode(oldBase);
		return ret;
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, MutableBoundingBox sbb) {
		this.placeTreasureAtCurrentPosition(world, rand, x, y, z, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 * TODO: Parameter "rand" is unused. Remove?
	 */
	protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, boolean trapped, MutableBoundingBox sbb) {
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST)) {
			treasureType.generateChest(world, pos, trapped);
		}
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureRotated(World world, int x, int y, int z, Rotation rotation, TFTreasure treasureType, MutableBoundingBox sbb) {
		this.placeTreasureRotated(world, x, y, z, rotation, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureRotated(World world, int x, int y, int z, Rotation rotation, TFTreasure treasureType, boolean trapped, MutableBoundingBox sbb) {
		int dx = getXWithOffsetRotated(x, z, rotation);
		int dy = getYWithOffset(y);
		int dz = getZWithOffsetRotated(x, z, rotation);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST)) {
			treasureType.generateChest(world, pos, trapped);
		}
	}

	/**
	 * Places a tripwire.
	 *
	 * Tries to delay notifying tripwire blocks of placement so they won't
	 * scan unloaded chunks looking for connections.
	 *
	 */
	protected void placeTripwire(World world, int x, int y, int z, int size, Direction facing, MutableBoundingBox sbb) {

		int dx = facing.getXOffset();
		int dz = facing.getZOffset();

		world.captureBlockSnapshots = true;

		// add tripwire hooks
		BlockState tripwireHook = Blocks.TRIPWIRE_HOOK.getDefaultState();
		setBlockState(world, tripwireHook.with(TripWireHookBlock.FACING, facing.getOpposite()), x, y, z, sbb);
		setBlockState(world, tripwireHook.with(TripWireHookBlock.FACING, facing), x + dx * size, y, z + dz * size, sbb);

		// add string
		BlockState tripwire = Blocks.TRIPWIRE.getDefaultState();
		for (int i = 1; i < size; i++) {
			setBlockState(world, tripwire, x + dx * i, y, z + dz * i, sbb);
		}

		world.captureBlockSnapshots = false;

		@SuppressWarnings("unchecked")
		List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>) world.capturedBlockSnapshots.clone();
		world.capturedBlockSnapshots.clear();

		for (BlockSnapshot snap : blockSnapshots) {
			int updateFlag = snap.getFlag();
			BlockState oldBlock = snap.getReplacedBlock();
			BlockState newBlock = world.getBlockState(snap.getPos());

			newBlock.getBlock().onBlockAdded(oldBlock, world, snap.getPos(), newBlock, false);
			world.markAndNotifyBlock(snap.getPos(), null, oldBlock, newBlock, updateFlag);
		}
	}

	protected void placeSignAtCurrentPosition(World world, int x, int y, int z, String string0, String string1, MutableBoundingBox sbb) {
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.OAK_SIGN) {
			world.setBlockState(pos, Blocks.OAK_SIGN.getDefaultState().with(StandingSignBlock.ROTATION, this.getCoordBaseMode().getHorizontalIndex() * 4), 2);

			SignTileEntity teSign = (SignTileEntity) world.getTileEntity(pos);
			if (teSign != null) {
				teSign.signText[1] = new StringTextComponent(string0);
				teSign.signText[2] = new StringTextComponent(string1);
			}
		}
	}

	//TODO: Method unused. Remove?
	protected void placeSignAtCurrentPosition(World world, int x, int y, int z, MutableBoundingBox sbb, String... text) {
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.OAK_SIGN) {
			world.setBlockState(pos, Blocks.OAK_SIGN.getDefaultState().with(StandingSignBlock.ROTATION, this.getCoordBaseMode().getHorizontalIndex() * 4), 2);

			SignTileEntity teSign = (SignTileEntity) world.getTileEntity(pos);
			if (teSign != null) {
				int min = Math.min(text.length, teSign.signText.length);

				for (int i = 0; i < min; i++) {
					teSign.signText[i] = new StringTextComponent(text[i]);
				}
			}
		}
	}


//	
//	public boolean makeTowerWing2(List list, Random rand, int index, int x, int y, int z, int size, int height, int direction) {
//		
//		ComponentTFTowerWing wing = new ComponentTFTowerWing(index, x, y, z, size, height, direction);
//		// check to see if it intersects something already there
//		StructurePiece intersect = StructurePiece.getIntersectingStructurePiece(list, wing.boundingBox);
//		if (intersect == null || intersect == this) {
//			list.add(wing);
//			wing.buildComponent(this, list, rand);
//			return true;
//		} else {
//			return false;
//		}
//	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	protected int[] offsetTowerCoords(int x, int y, int z, int towerSize, Direction direction) {

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

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

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

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
	protected int getXWithOffset(int x, int z) {
		//return super.getXWithOffset(x, z);
		// [VanillaCopy] of super, edits noted.
		Direction enumfacing = this.getCoordBaseMode();

		if (enumfacing == null) {
			return x;
		} else {
			switch (enumfacing) {
				case SOUTH:
					return this.boundingBox.minX + x;
				case WEST:
					return this.boundingBox.maxX - z;
				case NORTH:
					return this.boundingBox.maxX - x; // TF - Add case for NORTH todo 1.9 is this correct?
				case EAST:
					return this.boundingBox.minX + z;
				default:
					return x;
			}
		}
	}

	@Override
	protected int getZWithOffset(int x, int z) {
		//return super.getZWithOffset(x, z);
		// [VanillaCopy] of super, edits noted.
		Direction enumfacing = this.getCoordBaseMode();

		if (enumfacing == null) {
			return z;
		} else {
			switch (enumfacing) {
				case SOUTH:
					return this.boundingBox.minZ + z;
				case WEST:
					return this.boundingBox.minZ + x;
				case NORTH:
					return this.boundingBox.maxZ - z;
				case EAST:
					return this.boundingBox.maxZ - x;
				default:
					return z;
			}
		}
	}

	private Direction fakeBaseMode(Rotation rotationsCW) {
		final Direction oldBaseMode = getCoordBaseMode();

		if (oldBaseMode != null) {
			Direction pretendBaseMode = oldBaseMode;
			pretendBaseMode = rotationsCW.rotate(pretendBaseMode);

			setCoordBaseMode(pretendBaseMode);
		}

		return oldBaseMode;
	}

	// [VanillaCopy] Keep pinned to the signature of getXWithOffset
	protected int getXWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		int ret = getXWithOffset(x, z);
		setCoordBaseMode(oldMode);
		return ret;
	}

	// [VanillaCopy] Keep pinned to the signature of getZWithOffset
	protected int getZWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		int ret = getZWithOffset(x, z);
		setCoordBaseMode(oldMode);
		return ret;
	}

	protected void setBlockStateRotated(World world, BlockState state, int x, int y, int z, Rotation rotationsCW, MutableBoundingBox sbb) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		setBlockState(world, state, x, y, z, sbb);
		setCoordBaseMode(oldMode);
	}

	@Override
	protected BlockState getBlockStateFromPos(IBlockReader world, int x, int y, int z, MutableBoundingBox sbb) {
		// Making public
		return super.getBlockStateFromPos(world, x, y, z, sbb);
	}

	@Override
	protected void setBlockState(IWorld worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox sbb) {
		// Making public
		super.setBlockState(worldIn, blockstateIn, x, y, z, sbb);
	}

	// [VanillaCopy] Keep pinned to the signature of getBlockStateFromPos
	public BlockState getBlockStateFromPosRotated(World world, int x, int y, int z, MutableBoundingBox sbb, Rotation rotationsCW) {
		Direction oldMode = fakeBaseMode(rotationsCW);
		BlockState ret = getBlockStateFromPos(world, x, y, z, sbb);
		setCoordBaseMode(oldMode);
		return ret;
	}

	protected void fillBlocksRotated(World world, MutableBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState state, Rotation rotation) {
		Direction oldBase = fakeBaseMode(rotation);
		fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, state, state, false);
		setCoordBaseMode(oldBase);
	}

	// [VanillaCopy] Keep pinned on signature of fillWithBlocksRandomly (though passing false for excludeAir)
	protected void randomlyFillBlocksRotated(World worldIn, MutableBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockstate1, BlockState blockstate2, Rotation rotation) {
		Direction oldBase = fakeBaseMode(rotation);
		final boolean minimumLightLevel = true;
		generateMaybeBox(worldIn, boundingboxIn, rand, chance, minX, minY, minZ, maxX, maxY, maxZ, blockstate1, blockstate2, false, minimumLightLevel);
		setCoordBaseMode(oldBase);
	}

	// [VanillaCopy] Keep pinned to signature of replaceAirAndLiquidDownwards
	public void replaceAirAndLiquidDownwardsRotated(World world, BlockState state, int x, int y, int z, Rotation rotation, MutableBoundingBox sbb) {
		Direction oldBaseMode = fakeBaseMode(rotation);
		replaceAirAndLiquidDownwards(world, state, x, y, z, sbb);
		setCoordBaseMode(oldBaseMode);
	}

	protected void fillAirRotated(World world, MutableBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Rotation rotation) {
		Direction oldBaseMode = fakeBaseMode(rotation);
		fillWithAir(world, sbb, minX, minY, minZ, maxX, maxY, maxZ);
		setCoordBaseMode(oldBaseMode);
	}

	protected void fillWithAir(World world, MutableBoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Predicate<BlockState> predicate) {
		fillWithBlocks(world, boundingBox, xMin, yMin, zMin, xMax, yMax, zMax, Blocks.AIR.getDefaultState(), predicate);
	}

	protected void fillWithBlocks(World world, MutableBoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState state, Predicate<BlockState> predicate) {
		fillWithBlocks(world, boundingBox, xMin, yMin, zMin, xMax, yMax, zMax, state, state, predicate);
	}

	protected void fillWithBlocks(World world, MutableBoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState borderState, BlockState interiorState, Predicate<BlockState> predicate) {
		for (int y = yMin; y <= yMax; ++y) {
			for (int x = xMin; x <= xMax; ++x) {
				for (int z = zMin; z <= zMax; ++z) {

					if (predicate.test(this.getBlockStateFromPos(world, x, y, z, boundingBox))) {

						boolean isBorder = yMin != yMax && (y == yMin || y == yMax)
								|| xMin != xMax && (x == xMin || x == xMax)
								|| zMin != zMax && (z == zMin || z == zMax);

						this.setBlockState(world, isBorder ? borderState : interiorState, x, y, z, boundingBox);
					}
				}
			}
		}
	}

	protected static StructurePiece.BlockSelector getStrongholdStones() {
		return strongholdStones;
	}

	protected Direction getStructureRelativeRotation(Rotation rotationsCW) {
		return rotationsCW.rotate(getCoordBaseMode());
	}

	/**
	 * Nullify all the sky light in this component bounding box
	 */
//	public void nullifySkyLightForBoundingBox(World world) {
//		this.nullifySkyLight(world, boundingBox.minX - 1, boundingBox.minY - 1, boundingBox.minZ - 1, boundingBox.maxX + 1, boundingBox.maxY + 1, boundingBox.maxZ + 1);
//	}

	/**
	 * Nullify all the sky light at the specified positions, using local coordinates
	 */
//	protected void nullifySkyLightAtCurrentPosition(World world, int sx, int sy, int sz, int dx, int dy, int dz) {
//		// resolve all variables to their actual in-world positions
//		nullifySkyLight(world, getXWithOffset(sx, sz), getYWithOffset(sy), getZWithOffset(sx, sz), getXWithOffset(dx, dz), getYWithOffset(dy), getZWithOffset(dx, dz));
//	}

	/**
	 * Nullify all the sky light at the specified positions, using world coordinates
	 */
	//TODO: Probably can't set light anymore
//	protected void nullifySkyLight(IWorld world, int sx, int sy, int sz, int dx, int dy, int dz) {
//		for (int x = sx; x <= dx; x++) {
//			for (int z = sz; z <= dz; z++) {
//				for (int y = sy; y <= dy; y++) {
//					world.setLightFor(LightType.SKY, new BlockPos(x, y, z), 0);
//				}
//			}
//		}
//	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 * <p>
	 * This is basically copied from ComponentVillage
	 */
	protected int getAverageGroundLevel(World world, MutableBoundingBox sbb) {
		int totalHeight = 0;
		int heightCount = 0;

		for (int bz = this.boundingBox.minZ; bz <= this.boundingBox.maxZ; ++bz) {
			for (int by = this.boundingBox.minX; by <= this.boundingBox.maxX; ++by) {
				BlockPos pos = new BlockPos(by, 64, bz);
				if (sbb.isVecInside(pos)) {
					totalHeight += Math.max(world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY(), ((ServerWorld) world).getChunkProvider().getChunkGenerator().getGroundHeight());
					++heightCount;
				}
			}
		}

		if (heightCount == 0) {
			return -1;
		} else {
			return totalHeight / heightCount;
		}
	}

	/**
	 * Find what y-level the ground is. Just check the center of the chunk we're given.
	 */
	protected int findGroundLevel(World world, MutableBoundingBox sbb, int start, Predicate<BlockState> predicate) {

		Vec3i center = StructureBoundingBoxUtils.getCenter(sbb);
		BlockPos.Mutable pos = new BlockPos.Mutable(center.getX(), 0, center.getZ());

		for (int y = start; y > 0; y--) {
			pos.setY(y);
			if (predicate.test(world.getBlockState(pos))) {
				return y;
			}
		}

		return 0;
	}

	//TODO: Parameter "sbb" is unused. Remove?
	protected boolean isBoundingBoxOutsideBiomes(World world, MutableBoundingBox sbb, Predicate<Biome> predicate) {

		int minX = this.boundingBox.minX - 1;
		int minZ = this.boundingBox.minZ - 1;
		int maxX = this.boundingBox.maxX + 1;
		int maxZ = this.boundingBox.maxZ + 1;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (!predicate.test(world.getBiome(pos.setPos(x, 0, z)))) {
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
	public static StructurePiece findIntersectingExcluding(List<StructurePiece> list, MutableBoundingBox toCheck, StructurePiece exclude) {
		Iterator<StructurePiece> iterator = list.iterator();
		StructurePiece structurecomponent;

		do {
			if (!iterator.hasNext()) {
				return null;
			}

			structurecomponent = (StructurePiece) iterator.next();
		}
		while (structurecomponent == exclude || structurecomponent.getBoundingBox() == null || !structurecomponent.getBoundingBox().intersectsWith(toCheck));

		return structurecomponent;
	}

	public BlockPos getBlockPosWithOffset(int x, int y, int z) {
		return new BlockPos(
				getXWithOffset(x, z),
				getYWithOffset(y),
				getZWithOffset(x, z)
		);
	}

	/* BlockState Helpers */
	//TODO: Parameter "rotation" is unused. Remove?
	protected static BlockState getStairState(BlockState stairState, Direction direction, Rotation rotation, boolean isTopHalf) {
		return stairState
				.with(StairsBlock.FACING, direction)
				.with(StairsBlock.HALF, isTopHalf ? Half.TOP : Half.BOTTOM);
	}

	protected static BlockState getSlabState(BlockState inputBlockState, SlabType half) {
		return inputBlockState
				.with(SlabBlock.TYPE, half);
	}
}
