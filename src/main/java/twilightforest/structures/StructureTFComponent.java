package twilightforest.structures;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFTreasure;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public abstract class StructureTFComponent extends StructureComponent {

	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final StructureTFStrongholdStones strongholdStones = new StructureTFStrongholdStones();

	public StructureTFDecorator deco = null;
	public int spawnListIndex = 0;

	public StructureTFComponent() {
	}

	public StructureTFComponent(int i) {
		super(i);
	}

	protected void setDebugEntity(World world, int x, int y, int z, String s) {
		//Uncomment this to spawn sheep with labels at a specific point for debugging purposes
		/*final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

		final EntitySheep sheep = new EntitySheep(world);
		sheep.setCustomNameTag(s);
		sheep.setNoAI(true);
		sheep.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0, 0);
		sheep.setEntityInvulnerable(true);
		sheep.setInvisible(true);
		sheep.setAlwaysRenderNameTag(true);
		sheep.setSilent(true);
		sheep.setNoGravity(true);
		world.spawnEntity(sheep);*/

	}

	//Let's not use vanilla's weird rotation+mirror thing...
	@Override
	public void setCoordBaseMode(@Nullable EnumFacing facing) {
		this.coordBaseMode = facing;
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

	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setInteger("si", this.spawnListIndex);
		par1NBTTagCompound.setString("deco", StructureTFDecorator.getDecoString(this.deco));
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		this.spawnListIndex = par1NBTTagCompound.getInteger("si");
		this.deco = StructureTFDecorator.getDecoFor(par1NBTTagCompound.getString("deco"));
	}

	public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, EnumFacing dir) {
		switch (dir) {
			default:
				return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case SOUTH: // '\0'
				return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case WEST: // '\001'
				return new StructureBoundingBox(x - maxZ + minZ, y + minY, z + minX, x + minZ, y + maxY + minY, z + maxX + minX);

			case NORTH: // '\002'
				return new StructureBoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);

			case EAST: // '\003'
				return new StructureBoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z + minX);
		}
	}

	/**
	 * Fixed a bug with direction 1 and -z values, but I'm not sure if it'll break other things
	 */
	public static StructureBoundingBox getComponentToAddBoundingBox2(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, EnumFacing dir) {
		switch (dir) {
			default:
				return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case SOUTH: // '\0'
				return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);

			case WEST: // '\001'
				return new StructureBoundingBox(x - maxZ - minZ, y + minY, z + minX, x - minZ, y + maxY + minY, z + maxX + minX);

			case NORTH: // '\002'
				return new StructureBoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);

			case EAST: // '\003'
				return new StructureBoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z - minX);
		}
	}

	// [VanillaCopy] Keep pinned to signature of setBlockState (no state arg)
	protected TileEntityMobSpawner setSpawner(World world, int x, int y, int z, StructureBoundingBox sbb, ResourceLocation monsterID) {
		TileEntityMobSpawner tileEntitySpawner = null;

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos) != Blocks.MOB_SPAWNER) {
			world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
			tileEntitySpawner = (TileEntityMobSpawner) world.getTileEntity(pos);
			if (tileEntitySpawner != null) {
				tileEntitySpawner.getSpawnerBaseLogic().setEntityId(monsterID);
			}
		}

		return tileEntitySpawner;
	}

	protected void surroundBlockCardinal(World world, IBlockState block, int x, int y, int z, StructureBoundingBox sbb) {
		setBlockState(world, block, x + 0, y, z - 1, sbb);
		setBlockState(world, block, x + 0, y, z + 1, sbb);
		setBlockState(world, block, x - 1, y, z + 0, sbb);
		setBlockState(world, block, x + 1, y, z + 0, sbb);
	}

	protected void surroundBlockCardinalRotated(World world, IBlockState block, int x, int y, int z, StructureBoundingBox sbb) {
		setBlockState(world, block.withProperty(BlockStairs.FACING, EnumFacing.NORTH), x + 0, y, z - 1, sbb);
		setBlockState(world, block.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), x + 0, y, z + 1, sbb);
		setBlockState(world, block.withProperty(BlockStairs.FACING, EnumFacing.WEST), x - 1, y, z + 0, sbb);
		setBlockState(world, block.withProperty(BlockStairs.FACING, EnumFacing.EAST), x + 1, y, z + 0, sbb);
	}

	protected void surroundBlockCorners(World world, IBlockState block, int x, int y, int z, StructureBoundingBox sbb) {
		setBlockState(world, block, x - 1, y, z - 1, sbb);
		setBlockState(world, block, x - 1, y, z + 1, sbb);
		setBlockState(world, block, x + 1, y, z - 1, sbb);
		setBlockState(world, block, x + 1, y, z + 1, sbb);
	}

	protected TileEntityMobSpawner setSpawnerRotated(World world, int x, int y, int z, Rotation rotation, ResourceLocation monsterID, StructureBoundingBox sbb) {
		EnumFacing oldBase = fakeBaseMode(rotation);
		TileEntityMobSpawner ret = setSpawner(world, x, y, z, sbb, monsterID);
		setCoordBaseMode(oldBase);
		return ret;
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, StructureBoundingBox sbb) {
		this.placeTreasureAtCurrentPosition(world, rand, x, y, z, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, boolean trapped, StructureBoundingBox sbb) {
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
	protected void placeTreasureRotated(World world, int x, int y, int z, Rotation rotation, TFTreasure treasureType, StructureBoundingBox sbb) {
		this.placeTreasureRotated(world, x, y, z, rotation, treasureType, false, sbb);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureRotated(World world, int x, int y, int z, Rotation rotation, TFTreasure treasureType, boolean trapped, StructureBoundingBox sbb) {
		int dx = getXWithOffsetRotated(x, z, rotation);
		int dy = getYWithOffset(y);
		int dz = getZWithOffsetRotated(x, z, rotation);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST)) {
			treasureType.generateChest(world, pos, trapped);
		}
	}

	protected void placeSignAtCurrentPosition(World world, int x, int y, int z, String string0, String string1, StructureBoundingBox sbb) {
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.STANDING_SIGN) {
			world.setBlockState(pos, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, this.getCoordBaseMode().getHorizontalIndex() * 4), 2);

			TileEntitySign teSign = (TileEntitySign) world.getTileEntity(pos);
			if (teSign != null) {
				teSign.signText[1] = new TextComponentString(string0);
				teSign.signText[2] = new TextComponentString(string1);
			}
		}
	}


//	
//	public boolean makeTowerWing2(List list, Random rand, int index, int x, int y, int z, int size, int height, int direction) {
//		
//		ComponentTFTowerWing wing = new ComponentTFTowerWing(index, x, y, z, size, height, direction);
//		// check to see if it intersects something already there
//		StructureComponent intersect = StructureComponent.getIntersectingStructureComponent(list, wing.boundingBox);
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
	protected int[] offsetTowerCoords(int x, int y, int z, int towerSize, EnumFacing direction) {

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		if (direction == EnumFacing.SOUTH) {
			return new int[]{dx + 1, dy - 1, dz - towerSize / 2};
		} else if (direction == EnumFacing.WEST) {
			return new int[]{dx + towerSize / 2, dy - 1, dz + 1};
		} else if (direction == EnumFacing.NORTH) {
			return new int[]{dx - 1, dy - 1, dz + towerSize / 2};
		} else if (direction == EnumFacing.EAST) {
			return new int[]{dx - towerSize / 2, dy - 1, dz - 1};
		}


		// ugh?
		return new int[]{x, y, z};
	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int towerSize, EnumFacing direction) {

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
		EnumFacing enumfacing = this.getCoordBaseMode();

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
		EnumFacing enumfacing = this.getCoordBaseMode();

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

	private EnumFacing fakeBaseMode(Rotation rotationsCW) {
		final EnumFacing oldBaseMode = getCoordBaseMode();

		if (oldBaseMode != null) {
			EnumFacing pretendBaseMode = oldBaseMode;
			pretendBaseMode = rotationsCW.rotate(pretendBaseMode);

			setCoordBaseMode(pretendBaseMode);
		}

		return oldBaseMode;
	}

	// [VanillaCopy] Keep pinned to the signature of getXWithOffset
	protected int getXWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		EnumFacing oldMode = fakeBaseMode(rotationsCW);
		int ret = getXWithOffset(x, z);
		setCoordBaseMode(oldMode);
		return ret;
	}

	// [VanillaCopy] Keep pinned to the signature of getZWithOffset
	protected int getZWithOffsetRotated(int x, int z, Rotation rotationsCW) {
		EnumFacing oldMode = fakeBaseMode(rotationsCW);
		int ret = getZWithOffset(x, z);
		setCoordBaseMode(oldMode);
		return ret;
	}

	protected void setBlockStateRotated(World world, IBlockState state, int x, int y, int z, Rotation rotationsCW, StructureBoundingBox sbb) {
		EnumFacing oldMode = fakeBaseMode(rotationsCW);
		setBlockState(world, state, x, y, z, sbb);
		setCoordBaseMode(oldMode);
	}

	@Override
	public IBlockState getBlockStateFromPos(World world, int x, int y, int z, StructureBoundingBox sbb) {
		// Making public
		return super.getBlockStateFromPos(world, x, y, z, sbb);
	}

	@Override
	protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox sbb) {
		// Making public
		super.setBlockState(worldIn, blockstateIn, x, y, z, sbb);
	}

	// [VanillaCopy] Keep pinned to the signature of getBlockStateFromPos
	public IBlockState getBlockStateFromPosRotated(World world, int x, int y, int z, StructureBoundingBox sbb, Rotation rotationsCW) {
		EnumFacing oldMode = fakeBaseMode(rotationsCW);
		IBlockState ret = getBlockStateFromPos(world, x, y, z, sbb);
		setCoordBaseMode(oldMode);
		return ret;
	}

	protected void fillBlocksRotated(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState state, Rotation rotation) {
		EnumFacing oldBase = fakeBaseMode(rotation);
		fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, state, state, false);
		setCoordBaseMode(oldBase);
	}

	// [VanillaCopy] Keep pinned on signature of fillWithBlocksRandomly (though passing false for excludeAir)
	protected void randomlyFillBlocksRotated(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, Rotation rotation) {
		EnumFacing oldBase = fakeBaseMode(rotation);
		final int minimumLightLevel = 15;
		generateMaybeBox(worldIn, boundingboxIn, rand, chance, minX, minY, minZ, maxX, maxY, maxZ, blockstate1, blockstate2, false, minimumLightLevel);
		setCoordBaseMode(oldBase);
	}

	// [VanillaCopy] Keep pinned to signature of replaceAirAndLiquidDownwards
	public void replaceAirAndLiquidDownwardsRotated(World world, IBlockState state, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb) {
		EnumFacing oldBaseMode = fakeBaseMode(rotation);
		replaceAirAndLiquidDownwards(world, state, x, y, z, sbb);
		setCoordBaseMode(oldBaseMode);
	}

	protected void fillAirRotated(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Rotation rotation) {
		EnumFacing oldBaseMode = fakeBaseMode(rotation);
		fillWithAir(world, sbb, minX, minY, minZ, maxX, maxY, maxZ);
		setCoordBaseMode(oldBaseMode);
	}

	protected static StructureComponent.BlockSelector getStrongholdStones() {
		return strongholdStones;
	}

	protected EnumFacing getStructureRelativeRotation(Rotation rotationsCW) {
		return rotationsCW.rotate(getCoordBaseMode());
	}

	/**
	 * Nullify all the sky light in this component bounding box
	 */
	public void nullifySkyLightForBoundingBox(World world) {
		this.nullifySkyLight(world, boundingBox.minX - 1, boundingBox.minY - 1, boundingBox.minZ - 1, boundingBox.maxX + 1, boundingBox.maxY + 1, boundingBox.maxZ + 1);
	}

	/**
	 * Nullify all the sky light at the specified positions, using local coordinates
	 */
	protected void nullifySkyLightAtCurrentPosition(World world, int sx, int sy, int sz, int dx, int dy, int dz) {
		// resolve all variables to their actual in-world positions
		nullifySkyLight(world, getXWithOffset(sx, sz), getYWithOffset(sy), getZWithOffset(sx, sz), getXWithOffset(dx, dz), getYWithOffset(dy), getZWithOffset(dx, dz));
	}

	/**
	 * Nullify all the sky light at the specified positions, using world coordinates
	 */
	protected void nullifySkyLight(World world, int sx, int sy, int sz, int dx, int dy, int dz) {
		for (int x = sx; x <= dx; x++) {
			for (int z = sz; z <= dz; z++) {
				for (int y = sy; y <= dy; y++) {
					world.setLightFor(EnumSkyBlock.SKY, new BlockPos(x, y, z), 0);
				}
			}
		}
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 * <p>
	 * This is basically copied from ComponentVillage
	 */
	protected int getAverageGroundLevel(World world, StructureBoundingBox sbb) {
		int totalHeight = 0;
		int heightCount = 0;

		for (int bz = this.boundingBox.minZ; bz <= this.boundingBox.maxZ; ++bz) {
			for (int by = this.boundingBox.minX; by <= this.boundingBox.maxX; ++by) {
				BlockPos pos = new BlockPos(by, 64, bz);
				if (sbb.isVecInside(pos)) {
					totalHeight += Math.max(world.getTopSolidOrLiquidBlock(pos).getY(), world.provider.getAverageGroundLevel());
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
	 * Find what y level the dirt/grass/stone is.  Just check the center of the chunk we're given
	 */
	protected int getSampledDirtLevel(World world, StructureBoundingBox sbb) {
		int dirtLevel = 256;

		Vec3i center = new BlockPos(sbb.minX + (sbb.maxX - sbb.minX + 1) / 2, sbb.minY + (sbb.maxY - sbb.minY + 1) / 2, sbb.minZ + (sbb.maxZ - sbb.minZ + 1) / 2);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(center.getX(), 0, center.getZ());

		for (int y = 90; y > 0; y--) // is 90 like a good place to start? :)
		{
			pos.setY(y);
			Material material = world.getBlockState(pos).getMaterial();
			if (material == Material.GROUND || material == Material.ROCK || material == Material.GRASS) {
				dirtLevel = y;
				break;
			}
		}

		return dirtLevel;
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	public boolean isComponentProtected() {
		return true;
	}

	/**
	 * Discover if bounding box can fit within the current bounding box object.
	 */
	public static StructureComponent findIntersectingExcluding(List<StructureComponent> list, StructureBoundingBox toCheck, StructureComponent exclude) {
		Iterator<StructureComponent> iterator = list.iterator();
		StructureComponent structurecomponent;

		do {
			if (!iterator.hasNext()) {
				return null;
			}

			structurecomponent = (StructureComponent) iterator.next();
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
	protected static IBlockState getStairState(IBlockState stairState, EnumFacing direction, Rotation rotation, boolean isTopHalf) {
		return stairState
				.withProperty(BlockStairs.FACING, direction)
				.withProperty(BlockStairs.HALF, isTopHalf ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
	}

	protected static IBlockState getSlabState(IBlockState inputBlockState, BlockPlanks.EnumType type, BlockSlab.EnumBlockHalf half) {
		return inputBlockState
				.withProperty(BlockPlanks.VARIANT, type)
				.withProperty(BlockSlab.HALF, half);
	}
}