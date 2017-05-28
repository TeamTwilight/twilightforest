package twilightforest.structures;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFTreasure;


public abstract class StructureTFComponent extends StructureComponent {

    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final StructureTFStrongholdStones strongholdStones = new StructureTFStrongholdStones();
    
    public StructureTFDecorator deco = null;
    public int spawnListIndex = 0;

    public StructureTFComponent() {}
    
	public StructureTFComponent(int i) {
		super(i);
	}

	@Override
    protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("si", this.spawnListIndex);
        par1NBTTagCompound.setString("deco", StructureTFDecorator.getDecoString(this.deco));
    }

	@Override
    protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.spawnListIndex = par1NBTTagCompound.getInteger("si");
        this.deco = StructureTFDecorator.getDecoFor(par1NBTTagCompound.getString("deco"));
    }
    
    public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, EnumFacing dir)
    {
        switch(dir)
        {
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
    public static StructureBoundingBox getComponentToAddBoundingBox2(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, EnumFacing dir)
    {
        switch(dir)
        {
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
    protected TileEntityMobSpawner setSpawner(World world, int x, int y, int z, StructureBoundingBox sbb, String monsterID)
    {
    	TileEntityMobSpawner tileEntitySpawner = null;
    	
        int dx = getXWithOffset(x, z);
        int dy = getYWithOffset(y);
        int dz = getZWithOffset(x, z);
        BlockPos pos = new BlockPos(dx, dy, dz);
        if(sbb.isVecInside(pos) && world.getBlockState(pos) != Blocks.MOB_SPAWNER)
        {
            world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            tileEntitySpawner = (TileEntityMobSpawner)world.getTileEntity(pos);
            if(tileEntitySpawner != null)
            {
            	tileEntitySpawner.getSpawnerBaseLogic().setEntityName(monsterID);
            }
        }
        
        return tileEntitySpawner;
    }

    protected TileEntityMobSpawner setSpawnerRotated(World world, int x, int y, int z, int rotation, String monsterID, StructureBoundingBox sbb)
    {
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
    protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, StructureBoundingBox sbb)
    {
        this.placeTreasureAtCurrentPosition(world, rand, x, y, z, treasureType, false, sbb);
    }

    /**
     * Place a treasure chest at the specified coordinates
     * 
     * @param treasureType
     */
    protected void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, boolean trapped, StructureBoundingBox sbb)
    {
        int dx = getXWithOffset(x, z);
        int dy = getYWithOffset(y);
        int dz = getZWithOffset(x, z);
        BlockPos pos = new BlockPos(dx, dy, dz);
        if(sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.CHEST)
        {
            treasureType.generateChest(world, pos);
        }
    }

    /**
     * Place a treasure chest at the specified coordinates
     * 
     * @param treasureType
     */
    protected void placeTreasureRotated(World world, int x, int y, int z, int rotation, TFTreasure treasureType, StructureBoundingBox sbb)
    {
        this.placeTreasureRotated(world, x, y, z, rotation, treasureType, false, sbb);
    }

    /**
     * Place a treasure chest at the specified coordinates
     * 
     * @param treasureType
     */
    protected void placeTreasureRotated(World world, int x, int y, int z, int rotation, TFTreasure treasureType, boolean trapped, StructureBoundingBox sbb)
    {
        int dx = getXWithOffsetRotated(x, z, rotation);
        int dy = getYWithOffset(y);
        int dz = getZWithOffsetRotated(x, z, rotation);
        BlockPos pos = new BlockPos(dx, dy, dz);
        if(sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.CHEST)
        {
            treasureType.generateChest(world, pos);
        }
    }
    
    protected void placeSignAtCurrentPosition(World world, int x, int y, int z, String string0, String string1, StructureBoundingBox sbb) {
        int dx = getXWithOffset(x, z);
        int dy = getYWithOffset(y);
        int dz = getZWithOffset(x, z);
        BlockPos pos = new BlockPos(dx, dy, dz);
        if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.STANDING_SIGN)
        {
            world.setBlockState(pos, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, this.getCoordBaseMode().getHorizontalIndex() * 4), 2);
            
            TileEntitySign teSign = (TileEntitySign)world.getTileEntity(pos);
            if (teSign != null)
            {
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
//			System.out.println("Planned wing intersects with " + intersect);
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
			return new int[] {dx + 1, dy - 1, dz - towerSize / 2};
		}
		else if (direction == EnumFacing.WEST) {
			return new int[] {dx + towerSize / 2, dy - 1, dz + 1};
		}
		else if (direction == EnumFacing.NORTH) {
			return new int[] {dx - 1, dy - 1, dz + towerSize / 2};
		}
		else if (direction == EnumFacing.EAST) {
			return new int[] {dx - towerSize / 2, dy - 1, dz - 1};
		}
		
		
		// ugh?
		return new int[] {x, y, z};
	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int towerSize, EnumFacing direction) {
		
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		switch (direction)
		{
			case SOUTH:
				return new BlockPos(dx + 1, dy - 1, dz - towerSize / 2);
			case WEST:
				return new BlockPos(dx + towerSize / 2, dy - 1, dz + 1);
			case NORTH:
				return new BlockPos(dx - 1, dy - 1, dz + towerSize / 2);
			case EAST:
				return new BlockPos(dx - towerSize / 2, dy - 1, dz - 1);
		}
		
		// ugh?
		return new BlockPos(x, y, z);
	}

	@Override
    protected int getXWithOffset(int x, int z)
    {
        // [VanillaCopy] of super, edits noted.
        EnumFacing enumfacing = this.getCoordBaseMode();

        if (enumfacing == null)
        {
            return x;
        }
        else
        {
            switch (enumfacing)
            {
                case NORTH:
                    return this.boundingBox.maxX - x; // TF - Add case for NORTH todo 1.9 is this correct?
                case SOUTH:
                    return this.boundingBox.minX + x;
                case WEST:
                    return this.boundingBox.maxX - z;
                case EAST:
                    return this.boundingBox.minX + z;
                default:
                    return x;
            }
        }
    }
	
    @Override
    protected int getZWithOffset(int x, int z)
    {
        // [VanillaCopy] of super, edits noted.
        EnumFacing enumfacing = this.getCoordBaseMode();

        if (enumfacing == null)
        {
            return z;
        }
        else
        {
            switch (enumfacing)
            {
                case NORTH:
                    return this.boundingBox.maxZ - z;
                case SOUTH:
                    return this.boundingBox.minZ + z;
                case WEST: // todo 1.9 incomplete - discrepancy vs old code
                case EAST:
                    return this.boundingBox.minZ + x;
                default:
                    return z;
            }
        }
    }

    private EnumFacing fakeBaseMode(int rotationsCW) {
        EnumFacing oldBaseMode = getCoordBaseMode();

        if (oldBaseMode != null) {
            EnumFacing pretendBaseMode = oldBaseMode;
            for (int i = 0; i < rotationsCW; i++) {
                pretendBaseMode = pretendBaseMode.rotateY();
            }

            setCoordBaseMode(pretendBaseMode);
        }

        return oldBaseMode;
    }

    // [VanillaCopy] Keep pinned to the signature of getXWithOffset
	protected int getXWithOffsetRotated(int x, int z, int rotationsCW) {
        EnumFacing oldMode = fakeBaseMode(rotationsCW);
        int ret = getXWithOffset(x, z);
        setCoordBaseMode(oldMode);
        return ret;
	}

    // [VanillaCopy] Keep pinned to the signature of getZWithOffset
    protected int getZWithOffsetRotated(int x, int z, int rotationsCW) {
        EnumFacing oldMode = fakeBaseMode(rotationsCW);
        int ret = getZWithOffset(x, z);
        setCoordBaseMode(oldMode);
        return ret;
    }

    // [VanillaCopy] Keep pinned to the signature of setBlockState
    protected void setBlockStateRotated(World world, IBlockState state, int x, int y, int z, int rotationsCW, StructureBoundingBox sbb) {
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
    public void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox sbb) {
        // Making public
        super.setBlockState(worldIn, blockstateIn, x, y, z, sbb);
    }

    // [VanillaCopy] Keep pinned to the signature of getBlockStateFromPos
    public IBlockState getBlockStateFromPosRotated(World world, int x, int y, int z, StructureBoundingBox sbb, int rotationsCW) {
        EnumFacing oldMode = fakeBaseMode(rotationsCW);
        IBlockState ret = getBlockStateFromPos(world, x, y, z, sbb);
        setCoordBaseMode(oldMode);
        return ret;
    }
    
    // [VanillaCopy] Keep pinned to the signature of fillWithBlocks with false existingOnly arg and repeated state argument
    protected void fillBlocksRotated(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState state, int rotation) {
        EnumFacing oldBase = fakeBaseMode(rotation);
        fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, state, state, false);
        setCoordBaseMode(oldBase);
    }

    // [VanillaCopy] Keep pinned on signature of fillWithBlocksRandomly (though passing false for excludeAir)
    protected void randomlyFillBlocksRotated(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, int rotation) {
        EnumFacing oldBase = fakeBaseMode(rotation);
	    final int minimumLightLevel = 15;
	    func_189914_a(worldIn, boundingboxIn, rand, chance, minX, minY, minZ, maxX, maxY, maxZ, blockstate1, blockstate2, false, minimumLightLevel);
        setCoordBaseMode(oldBase);
    }
    
    // [VanillaCopy] Keep pinned to signature of replaceAirAndLiquidDownwards
	public void replaceAirAndLiquidDownwardsRotated(World world, IBlockState state, int x, int y, int z, int rotation, StructureBoundingBox sbb) {
        EnumFacing oldBaseMode = fakeBaseMode(rotation);
        replaceAirAndLiquidDownwards(world, state, x, y, z, sbb);
        setCoordBaseMode(oldBaseMode);
	}

    // [VanillaCopy] Keep pinned to the signature of fillWithAir
    protected void fillAirRotated(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int rotation)
    {
        EnumFacing oldBaseMode = fakeBaseMode(rotation);
        fillWithAir(world, sbb, minX, minY, minZ, maxX, maxY, maxZ);
        setCoordBaseMode(oldBaseMode);
    }

    protected static StructureComponent.BlockSelector getStrongholdStones() {
    	return strongholdStones;
    }
	
	protected EnumFacing getStructureRelativeRotation(int rotationsCW) {
        EnumFacing base = getCoordBaseMode();
        return EnumFacing.getHorizontal(base == null ? 0 : base.getHorizontalIndex() + rotationsCW);
	}

	/**
	 * Gets the metadata necessary to make stairs facing the proper direction.
	 *
	 * @param dir
	 * @return
	 */
	protected EnumFacing getStairMeta(int dir) {
		//TODO: AtomicBlom Verify this is even needed
		switch (getStructureRelativeRotation(dir)) {
			case SOUTH:
				return EnumFacing.SOUTH;
			case NORTH:
				return EnumFacing.NORTH;
			case EAST:
				return EnumFacing.EAST;
			case WEST:
				return EnumFacing.WEST;
			default:
				return EnumFacing.NORTH; // this is impossible
		}
	}

	/**
	 * Gets the metadata necessary to stick the ladder on the specified wall.
	 *
	 * @param ladderDir
	 * @return
	 */
	protected EnumFacing getLadderMeta(int ladderDir) {
		// ladder data values are... dumb.
		//TODO: AtomicBlom Verify this is even needed
		switch (getStructureRelativeRotation(ladderDir)) {
			case NORTH:
				return EnumFacing.NORTH;
			case SOUTH:
				return EnumFacing.SOUTH;
			case EAST:
				return EnumFacing.EAST;
			case WEST:
				return EnumFacing.WEST;
			default:
				return EnumFacing.NORTH; // this is impossible
		}
	}

	/**
	 * Gets the metadata necessary to stick the ladder on the specified wall.
	 */
	protected EnumFacing getLadderMeta(int ladderDir, int rotation) {
		return getLadderMeta(ladderDir + rotation);
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
    protected void nullifySkyLightAtCurrentPosition(World world, int sx, int sy, int sz, int dx, int dy, int dz)
	{
		// resolve all variables to their actual in-world positions
		nullifySkyLight(world, getXWithOffset(sx, sz), getYWithOffset(sy), getZWithOffset(sx, sz), getXWithOffset(dx, dz), getYWithOffset(dy), getZWithOffset(dx, dz));
	}
	
	/**
	 * Nullify all the sky light at the specified positions, using world coordinates
	 */
    protected void nullifySkyLight(World world, int sx, int sy, int sz, int dx, int dy, int dz)
	{
		for (int x = sx; x <= dx; x++) 
		{
         	for (int z = sz; z <= dz; z++) 
         	{
             	for (int y = sy; y <= dy; y++) 
             	{
             		world.setLightFor(EnumSkyBlock.SKY, new BlockPos(x, y, z), 0);
             	}
         	}
     	}
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 * 
	 * This is basically copied from ComponentVillage
	 */
	protected int getAverageGroundLevel(World world, StructureBoundingBox sbb) {
	    int totalHeight = 0;
	    int heightCount = 0;
	
	    for (int bz = this.boundingBox.minZ; bz <= this.boundingBox.maxZ; ++bz)
	    {
	        for (int by = this.boundingBox.minX; by <= this.boundingBox.maxX; ++by)
	        {
                BlockPos pos = new BlockPos(by, 64, bz);
	            if (sbb.isVecInside(pos))
	            {
	                totalHeight += Math.max(world.getTopSolidOrLiquidBlock(pos).getY(), world.provider.getAverageGroundLevel());
	                ++heightCount;
	            }
	        }
	    }
	
	    if (heightCount == 0)
	    {
	        return -1;
	    }
	    else
	    {
	        return totalHeight / heightCount;
	    }
	}
	
	/**
	 * Find what y level the dirt/grass/stone is.  Just check the center of the chunk we're given
	 */
	protected int getSampledDirtLevel(World world, StructureBoundingBox sbb) {
	    int dirtLevel = 256;

        Vec3i center = sbb.getCenter();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(center.getX(), 0, center.getZ());

        for (int y = 90; y > 0; y--) // is 90 like a good place to start? :)
	    {
            pos.setY(y);
	    	Material material = world.getBlockState(pos).getMaterial();
	    	if (material == Material.GROUND || material == Material.ROCK || material == Material.GRASS)
	    	{
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
    public static StructureComponent findIntersectingExcluding(List list, StructureBoundingBox toCheck, StructureComponent exclude)
    {
        Iterator iterator = list.iterator();
        StructureComponent structurecomponent;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            structurecomponent = (StructureComponent)iterator.next();
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
}