package twilightforest.structures.stronghold;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class StructureTFStrongholdComponent extends StructureTFComponentOld {

	public List<BlockPos> doors = new ArrayList<BlockPos>();

	public StructureTFStrongholdComponent() {
	}

	public StructureTFStrongholdComponent(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i);
		this.boundingBox = generateBoundingBox(facing, x, y, z);
		this.setCoordBaseMode(facing);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setIntArray("doorInts", this.getDoorsAsIntArray());
	}

	/**
	 * Turn the openings array into an array of ints.
	 */
	private int[] getDoorsAsIntArray() {
		IntBuffer ibuffer = IntBuffer.allocate(this.doors.size() * 3);

		for (BlockPos door : doors) {
			ibuffer.put(door.getX());
			ibuffer.put(door.getY());
			ibuffer.put(door.getZ());
		}

		return ibuffer.array();
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);

		// init doors
		this.readOpeningsFromArray(tagCompound.getIntArray("doorInts"));
	}

	/**
	 * Read in openings from int array
	 */
	private void readOpeningsFromArray(int[] intArray) {
		for (int i = 0; i < intArray.length; i += 3) {
			BlockPos door = new BlockPos(intArray[i], intArray[i + 1], intArray[i + 2]);

			this.doors.add(door);
		}
	}

	public abstract StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z);

	/**
	 * used to project a possible new component Bounding Box - to check if it would cut anything already spawned
	 */
	public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int xOff, int yOff, int zOff, int xSize, int ySize, int zSize, EnumFacing facing) {
		switch (facing) {
			case SOUTH:
				return new StructureBoundingBox(x + xOff, y + yOff, z + zOff, x + xSize - 1 + xOff, y + ySize - 1 + yOff, z + zSize - 1 + zOff);
			case WEST:
				return new StructureBoundingBox(x - zSize + 1 + zOff, y + yOff, z + xOff, x + zOff, y + ySize - 1 + yOff, z + xSize - 1 + xOff);
			case NORTH:
				return new StructureBoundingBox(x - xSize + 1 - xOff, y + yOff, z - zSize + 1 + zOff, x - xOff, y + ySize - 1 + yOff, z + zOff);
			case EAST:
				return new StructureBoundingBox(x + zOff, y + yOff, z - xSize + 1 - xOff, x + zSize - 1 + zOff, y + ySize - 1 + yOff, z - xOff);
			default:
				return new StructureBoundingBox(x + xOff, y + yOff, z + zOff, x + xSize - 1 + xOff, y + ySize - 1 + yOff, z + zSize - 1 + zOff);
		}
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	/**
	 * Add a new component in the specified direction
	 */
	protected void addNewComponent(StructureComponent entrance, List<StructureComponent> list, Random random, Rotation facing, int x, int y, int z) {
		int index = this.componentType + 1;
		EnumFacing nFacing = getStructureRelativeRotation(facing);
		int nx = this.getXWithOffset(x, z);
		int ny = this.getYWithOffset(y);
		int nz = this.getZWithOffset(x, z);

		// limit sprawl to a reasonable amount
		if (index > 50 || isOutOfRange(entrance, nx, ny, nz, 112)) {
			return;
		}

		// are we looking at a point we can possibly break in to?
		StructureTFStrongholdComponent breakIn = (StructureTFStrongholdComponent) this.findBreakInComponent(list, nx, ny, nz);
		if (breakIn != null && breakIn.attemptToBreakIn(nx, ny, nz)) {
			// success!
			this.addDoorwayTo(x, y, z, facing);
			return;
		}

		TFStrongholdPieces pieceList = ((ComponentTFStrongholdEntrance) entrance).lowerPieces;

		StructureComponent nextComponent = pieceList.getNextComponent(entrance, list, random, getFeatureType(), index, nFacing, nx, ny, nz);

		// is it clear?
		if (nextComponent != null) {
			// if so, add it
			list.add(nextComponent);
			nextComponent.buildComponent(entrance, list, random);
			this.addDoorwayTo(x, y, z, facing);
		}
	}

	/**
	 * Check the list for components we can break in to at the specified point
	 */
	protected StructureComponent findBreakInComponent(List<StructureComponent> list, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		for (StructureComponent component : list) {
			if (component.getBoundingBox() != null && component.getBoundingBox().isVecInside(pos)) {
				return component;
			}
		}

		return null;
	}


	protected void addNewUpperComponent(StructureComponent parent, List<StructureComponent> list, Random random, Rotation facing, int x, int y, int z) {
		StructureTFStrongholdComponent attempted = null;

		int index = this.componentType + 1;
		EnumFacing nFacing = getStructureRelativeRotation(facing);
		int nx = this.getXWithOffset(x, z);
		int ny = this.getYWithOffset(y);
		int nz = this.getZWithOffset(x, z);

		// limit sprawl to a reasonable amount
		if (index > 100 || isOutOfRange(parent, nx, ny, nz, 48)) {
			return;
		}

		// find a new component
		switch (random.nextInt(5)) {
			case 0:
			default:
				attempted = new ComponentTFStrongholdUpperTIntersection(getFeatureType(), index, nFacing, nx, ny, nz);
				break;
			case 1:
				attempted = new ComponentTFStrongholdUpperLeftTurn(getFeatureType(), index, nFacing, nx, ny, nz);
				break;
			case 2:
				attempted = new ComponentTFStrongholdUpperRightTurn(getFeatureType(), index, nFacing, nx, ny, nz);
				break;
			case 3:
				attempted = new ComponentTFStrongholdUpperCorridor(getFeatureType(), index, nFacing, nx, ny, nz);
				break;
			case 4:
				attempted = new ComponentTFStrongholdUpperAscender(getFeatureType(), index, nFacing, nx, ny, nz);
				break;
		}


		// is it clear?
		if (attempted != null && StructureComponent.findIntersecting(list, attempted.getBoundingBox()) == null) {
			// if so, add it
			list.add(attempted);
			attempted.buildComponent(parent, list, random);

		}


	}

	/**
	 * Have we strayed more than range blocks away from the center?
	 */
	private boolean isOutOfRange(StructureComponent parent, int nx, int ny, int nz, int range) {

		return Math.abs(nx - parent.getBoundingBox().minX) > range
				|| Math.abs(nz - parent.getBoundingBox().minZ) > range;
	}

	/**
	 * Make a doorway
	 */
	protected void placeDoorwayAt(World world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		if (x == 0 || x == this.getXSize()) {
			this.fillWithBlocks(world, sbb, x, y, z - 2, x, y + 3, z + 2, deco.fenceState, Blocks.AIR.getDefaultState(), false);
			this.fillWithAir(world, sbb, x, y, z - 1, x, y + 3, z + 1);
		} else {
			this.fillWithBlocks(world, sbb, x - 2, y, z, x + 2, y + 3, z, deco.fenceState, Blocks.AIR.getDefaultState(), false);
			this.fillWithAir(world, sbb, x - 1, y, z, x + 1, y + 3, z);
		}

		//this.setBlockState(world, Blocks.WOOL, this.coordBaseMode, x, y, z, sbb);
	}

	protected int getXSize() {
		switch (this.getCoordBaseMode()) {
			default:
			case SOUTH:
			case NORTH:
				return this.boundingBox.getXSize() - 1;
			case WEST:
			case EAST:
				return this.boundingBox.getZSize() - 1;
		}
	}

	/**
	 * Make a smaller doorway
	 */
	protected void placeSmallDoorwayAt(World world, Random rand, int facing, int x, int y, int z, StructureBoundingBox sbb) {
		if (facing == 0 || facing == 2) {
			this.fillWithBlocks(world, sbb, x - 1, y, z, x + 1, y + 1, z, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.AIR.getDefaultState(), true);
			this.fillWithAir(world, sbb, x, y, z, x, y + 1, z);
		} else {
			this.fillWithBlocks(world, sbb, x, y, z - 1, x, y + 1, z + 1, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.AIR.getDefaultState(), true);
			this.fillWithAir(world, sbb, x, y, z, x, y + 1, z);
		}

		//this.setBlockState(world, Blocks.WOOL, this.coordBaseMode, x, y, z, sbb);
	}

	/**
	 * Generate a statue in the corner
	 */
	public void placeCornerStatue(World world, int x, int y, int z, int facing, StructureBoundingBox sbb) {

		// set offsets and stair metas
		int ox = 1;
		int oz = 1;
		EnumFacing smx = EnumFacing.EAST;
		EnumFacing smz = EnumFacing.SOUTH;


		switch (facing) {
			case 0:
				// already set up
				break;
			case 1:
				oz = -1;
				smz = EnumFacing.SOUTH;
				break;
			case 2:
				ox = -1;
				smx = EnumFacing.WEST;
				break;
			case 3:
				ox = -1;
				oz = -1;
				smx = EnumFacing.WEST;
				smz = EnumFacing.NORTH;
				break;
		}

		// the center is always the same
		for (int sy = 0; sy < 5; sy++) {
			this.setBlockState(world, deco.pillarState, x, y + sy, z, sbb);
		}

		// antlers
		this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x + 0, y + 4, z + oz, sbb);
		this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x + ox, y + 4, z + 0, sbb);

		// arms

		this.setBlockState(world, getStairState(deco.stairState, smz, rotation, false), x + 0, y + 3, z + oz, sbb);
		this.setBlockState(world, getStairState(deco.stairState, smx, rotation, false), x + ox, y + 3, z + 0, sbb);
		this.setBlockState(world, getStairState(deco.stairState, smz, rotation, true), x + 0, y + 2, z + oz, sbb);
		this.setBlockState(world, getStairState(deco.stairState, smx, rotation, true), x + ox, y + 2, z + 0, sbb);
		this.setBlockState(world, getStairState(deco.stairState, smx, rotation, true), x + ox, y + 2, z + oz, sbb);

		// sword
		this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x + ox, y + 0, z + oz, sbb);
		this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x + ox, y + 1, z + oz, sbb);

		// feet
		this.setBlockState(world, getStairState(deco.stairState, smz, rotation, false), x + 0, y + 0, z + oz, sbb);
		this.setBlockState(world, getStairState(deco.stairState, smx, rotation, false), x + ox, y + 0, z + 0, sbb);

	}

	/**
	 * Make a statue that faces out from a wall
	 */
	public void placeWallStatue(World world, int x, int y, int z, Rotation facing, StructureBoundingBox sbb) {
		int ox = 1;
		int oz = 1;

		// the center is always the same
		for (int sy = 0; sy < 5; sy++) {
			this.setBlockState(world, deco.pillarState, x, y + sy, z, sbb);
		}

		if (facing == Rotation.NONE || facing == Rotation.CLOCKWISE_180) {
			if (facing == Rotation.CLOCKWISE_180) {
				ox = -ox;
				oz = -oz;
			}

			// antlers
			this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x - ox, y + 4, z, sbb);
			this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x + ox, y + 4, z, sbb);

			// arms

			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, false), x - ox, y + 3, z, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, false), x + ox, y + 3, z, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, false), x - ox, y + 3, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, false), x + ox, y + 3, z - oz, sbb);

			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, true), x - ox, y + 2, z, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, true), x + ox, y + 2, z, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x + 0, y + 2, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x - ox, y + 2, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x + ox, y + 2, z - oz, sbb);

			// sword
			this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x, y + 0, z - oz, sbb);
			this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x, y + 1, z - oz, sbb);

			// feet
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, false), x - ox, y + 0, z + 0, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, false), x + ox, y + 0, z + 0, sbb);
		} else {
			if (facing == Rotation.COUNTERCLOCKWISE_90) {
				oz = -oz;
				ox = -ox;
			}

			// antlers
			this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x, y + 4, z - oz, sbb);
			this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x, y + 4, z + oz, sbb);

			// arms
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, false), x, y + 3, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, false), x, y + 3, z + oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, false), x + ox, y + 3, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, false), x + ox, y + 3, z + oz, sbb);

			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, true), x, y + 2, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, true), x, y + 2, z + oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x + oz, y + 2, z + 0, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x + ox, y + 2, z - oz, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_90).rotate(EnumFacing.WEST), rotation, true), x + ox, y + 2, z + oz, sbb);

			// sword
			this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x + ox, y + 0, z, sbb);
			this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), x + ox, y + 1, z, sbb);

			// feet
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.NONE).rotate(EnumFacing.WEST), rotation, false), x, y + 0, z - ox, sbb);
			this.setBlockState(world, getStairState(deco.stairState, facing.add(Rotation.CLOCKWISE_180).rotate(EnumFacing.WEST), rotation, false), x, y + 0, z + ox, sbb);
		}

	}

	/**
	 * Called curing construction.  If an attempted component collides with this one, try "breaking in".
	 */
	public boolean attemptToBreakIn(int wx, int wy, int wz) {
		if (!isValidBreakInPoint(wx, wy, wz)) {
			return false;
		} else {
			int dx = this.getRelativeX(wx, wz);
			int dy = this.getRelativeY(wy);
			int dz = this.getRelativeZ(wx, wz);

			addDoor(dx, dy, dz);

			return true;
		}
	}

	/**
	 * Add a door to our list
	 */
	public void addDoorwayTo(int dx, int dy, int dz, Rotation facing) {
		switch (facing) {
			case NONE:
				addDoor(dx, dy, dz - 1);
				break;
			case CLOCKWISE_90:
				addDoor(dx + 1, dy, dz);
				break;
			case CLOCKWISE_180:
				addDoor(dx, dy, dz + 1);
				break;
			case COUNTERCLOCKWISE_90:
				addDoor(dx - 1, dy, dz);
				break;
		}
	}

	/**
	 * Add a door to our list
	 */
	public void addDoor(int dx, int dy, int dz) {
		this.doors.add(new BlockPos(dx, dy, dz));
	}

	/**
	 * Is the specified point a valid spot to break in?
	 */
	protected boolean isValidBreakInPoint(int wx, int wy, int wz) {
		if (wy < this.boundingBox.minY || wy > this.boundingBox.maxY) {
			return false;
		} else if (wx == this.boundingBox.minX || wx == this.boundingBox.maxX) {
			return wz > this.boundingBox.minZ && wz < this.boundingBox.maxZ;
		} else if (wz == this.boundingBox.minZ || wz == this.boundingBox.maxZ) {
			return wx > this.boundingBox.minX && wx < this.boundingBox.maxX;
		} else {
			return false;
		}
	}

	protected int getRelativeX(int x, int z) {
		//this.getXWithOffset(x, z);

		switch (getCoordBaseMode()) {
			case SOUTH:
				return x - boundingBox.minX;
			case NORTH:
				return boundingBox.maxX - x;
			case WEST:
				return z - boundingBox.minZ;
			case EAST:
				return boundingBox.maxZ - z;
			default:
				return x;
		}
	}

	protected int getRelativeY(int par1) {
		return par1 - this.boundingBox.minY;
	}

	protected int getRelativeZ(int x, int z) {
		switch (getCoordBaseMode()) {
			case SOUTH:
				return z - boundingBox.minZ;
			case NORTH:
				return boundingBox.maxZ - z;
			case WEST:
				return boundingBox.maxX - x;
			case EAST:
				return x - boundingBox.minX;
			default:
				return z;
		}
	}

	/**
	 * Place any doors on our list
	 */
	public void placeDoors(World world, Random rand, StructureBoundingBox sbb) {
		if (this.doors != null) {
			for (BlockPos doorCoords : doors) {
				this.placeDoorwayAt(world, rand, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);

				//this.setBlockState(world, Blocks.WOOL, doorCoords.posX, doorCoords.posX, doorCoords.posY + 2, doorCoords.posZ, sbb);
			}
		}
	}

	/**
	 * Place stronghold walls in every position except those filled with dirt.
	 */
	protected void placeStrongholdWalls(World world, StructureBoundingBox sbb, int sx, int sy, int sz, int dx, int dy, int dz, Random rand, StructureComponent.BlockSelector randomBlocks) {
		for (int y = sy; y <= dy; ++y) {
			for (int x = sx; x <= dx; ++x) {
				for (int z = sz; z <= dz; ++z) {
					boolean wall = y == sy || y == dy || x == sx || x == dx || z == sz || z == dz;
					Block blockID = this.getBlockStateFromPos(world, x, y, z, sbb).getBlock();

					if (blockID == Blocks.AIR) {
						// cobblestone to "fill in holes"
						if (wall) {
							this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), x, y, z, sbb);
						}
					} else if (y == sy || y == dy) {
						// do stronghold bricks for floor/ceiling
						StructureComponent.BlockSelector strongBlocks = StructureTFComponentOld.getStrongholdStones();
						strongBlocks.selectBlocks(rand, x, y, z, wall);
						this.setBlockState(world, strongBlocks.getBlockState(), x, y, z, sbb);

					} else if (!wall || blockID != Blocks.DIRT)  // leave dirt there
					{
						// and use decorator (with presumably underbricks) for walls
						randomBlocks.selectBlocks(rand, x, y, z, wall);
						this.setBlockState(world, randomBlocks.getBlockState(), x, y, z, sbb);
					}
				}
			}
		}
	}


	/**
	 * Place stronghold walls on dirt/grass/stone
	 */
	protected void placeUpperStrongholdWalls(World world, StructureBoundingBox sbb, int sx, int sy, int sz, int dx, int dy, int dz, Random rand, StructureComponent.BlockSelector randomBlocks) {
		for (int y = sy; y <= dy; ++y) {
			for (int x = sx; x <= dx; ++x) {
				for (int z = sz; z <= dz; ++z) {
					boolean wall = y == sy || y == dy || x == sx || x == dx || z == sz || z == dz;
					IBlockState state = this.getBlockStateFromPos(world, x, y, z, sbb);
					Block blockID = state.getBlock();

					if ((blockID != Blocks.AIR && (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND))
							|| (blockID == Blocks.AIR && rand.nextInt(3) == 0) && this.getBlockStateFromPos(world, x, y - 1, z, sbb).getBlock() == Blocks.STONEBRICK) {
						if (y == sy || y == dy) {
							// do stronghold bricks for floor/ceiling
							StructureComponent.BlockSelector strongBlocks = StructureTFComponentOld.getStrongholdStones();
							strongBlocks.selectBlocks(rand, x, y, z, wall);
							this.setBlockState(world, strongBlocks.getBlockState(), x, y, z, sbb);

						} else {
							// and use decorator (with presumably underbricks) for walls
							randomBlocks.selectBlocks(rand, x, y, z, wall);
							this.setBlockState(world, randomBlocks.getBlockState(), x, y, z, sbb);
						}
					}
				}
			}
		}
	}


}