package twilightforest.structures;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFTreasure;
import twilightforest.entity.TFCreatures;
import twilightforest.world.TFGenCaveStalactite;



public class ComponentTFHollowHill extends StructureTFComponent {
	
	int hillSize;
	int radius;

	public ComponentTFHollowHill(World world, Random rand, int i, int size, int x, int y, int z) {
		super(i);

		this.setCoordBaseMode(EnumFacing.SOUTH);
		
		// get the size of this hill?
		this.hillSize = size;
		radius = ((hillSize * 2 + 1) * 8) - 6;

		// can we determine the size here?
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -radius, -3, -radius, radius * 2, 10, radius * 2, EnumFacing.SOUTH);
	}
	
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("hillSize", this.hillSize);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readStructureFromNBT(par1NBTTagCompound);
        this.hillSize = par1NBTTagCompound.getInteger("hillSize");
        this.radius = ((hillSize * 2 + 1) * 8) - 6;

	}

	/**
	 * Add on any other components we need.  In this case we add the maze below the hill
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
		
//		// add a maze
		ComponentTFHillMaze maze = new ComponentTFHillMaze(1, boundingBox.minX + ((boundingBox.maxX - boundingBox.minX) / 2), boundingBox.minY - 20, boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2), hillSize);
		list.add(maze);
		maze.buildComponent(this, list, random);
		
		
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
//		int area = (int)(Math.PI * radius * radius);
//		int sn = area / 16; // number of stalactites (there will actually be around twice this number)
		int[] sna = {0, 128, 256, 512};
		int sn = sna[hillSize]; // number of stalactites mga = {0, 3, 9, 18}
		int[] mga = {0, 1, 4, 9};
		int mg = mga[hillSize]; // number of monster generators mga = {0, 3, 9, 18} (reduced due to "natural" spawning)
		int[] tca = {0, 2, 6, 12};
		int tc = tca[hillSize];  // number of treasure chests tca = {0, 2, 6, 12};

		// fill in features
		
		// monster generators!
		for (int i = 0; i < mg; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			String mobID = getMobID(rand);
			
			setSpawner(world, dest[0], rand.nextInt(4), dest[1], sbb, mobID);
//			placeMobSpawner(dest[0], hy + rand.nextInt(4), dest[1]);
		}
		// treasure chests!!
		for (int i = 0; i < tc; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateTreasureChest(world, dest[0], 0, dest[1], sbb);
		}

		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
		}
		// stone stalactites!
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 0.9F, false, dest[0], 1, dest[1], sbb);
		}
		
		// level 3 hills get 2 mid-air wraith spawners
		if (hillSize == 3)
		{
//			int[] dest = getEmptyCoordsInHill(hy + 10, 20);
//			placeWraithSpawner(dest[0], hy + 10, dest[1]);
//			dest = getEmptyCoordsInHill(hy + 10, 20);
//			placeWraithSpawner(dest[0], hy + 10, dest[1]);
		}


		
		return true;
	}
	
	/**
	 * Make an RNG and attempt to use it to place a treasure chest
	 */
	protected void generateTreasureChest(World world, int x, int y, int z, StructureBoundingBox sbb) {
		// generate an RNG for this chest
    	//TODO: MOAR RANDOM!
    	Random chestRNG = new Random(world.getSeed() + x * z);
    	
    	// try placing it
    	placeTreasureAtCurrentPosition(world, chestRNG, x, y, z, this.hillSize == 3 ? TFTreasure.hill3 : (this.hillSize == 2 ? TFTreasure.hill2 : TFTreasure.hill1), sbb);
    	
    	// make something for it to stand on, if necessary
        setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), x, y - 1, z, sbb);

	}

	/**
	 * Generate a random ore stalactite
	 */
	protected void generateOreStalactite(World world, int x, int y, int z, StructureBoundingBox sbb) {
		// are the coordinates in our bounding box?
        int dx = getXWithOffset(x, z);
        int dy = getYWithOffset(y);
        int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
        if(sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.MOB_SPAWNER)
        {
        	// generate an RNG for this stalactite
        	//TODO: MOAR RANDOM!
        	Random stalRNG = new Random(world.getSeed() + dx * dz);
        	
        	// make the actual stalactite
			TFGenCaveStalactite stalag = TFGenCaveStalactite.makeRandomOreStalactite(stalRNG, hillSize);
			stalag.generate(world, stalRNG, pos);
        }
	}

	/**
	 * Make a random stone stalactite
	 */
	protected void generateBlockStalactite(World world, Block blockToGenerate, float length, boolean up, int x, int y, int z, StructureBoundingBox sbb) {
		// are the coordinates in our bounding box?
        int dx = getXWithOffset(x, z);
        int dy = getYWithOffset(y);
        int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
        if(sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.MOB_SPAWNER)
        {
        	// generate an RNG for this stalactite
        	//TODO: MOAR RANDOM!
        	Random stalRNG = new Random(world.getSeed() + dx * dz);
        	
        	if (hillSize == 1) {
        		length *= 1.9F;
        	}
        	
        	// make the actual stalactite
        	(new TFGenCaveStalactite(blockToGenerate, length, up)).generate(world, stalRNG, pos);
        }
	}


	/**
	 * 
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	boolean isInHill(int cx, int cz)
	{
		int dx = radius - cx;
		int dz = radius - cz;
		int dist = (int) Math.sqrt(dx * dx + dz * dz);
		
		return dist < radius;
	}
	
	/**
	 * @return true if the coordinates are inside the hill in 3D
	 */
	boolean isInHill(int mapX, int mapY, int mapZ)
	{
		int dx = boundingBox.minX + radius - mapX;
		int dy = (boundingBox.minY - mapY) * 2; // hill is half as high as it is wide, thus we just double y distance from center.  I *think* that math works!
		int dz = boundingBox.minZ + radius - mapZ;
		int dist = dx * dx + dy * dy + dz * dz;
		return dist < radius * radius;
	}
	
	int[] getCoordsInHill2D(Random rand)
	{
		return getCoordsInHill2D(rand, radius);
	}
	
	/**
	 * 
	 * @return a two element array containing some coordinates in the hill
	 */
	int[] getCoordsInHill2D(Random rand, int rad)
	{
		int rx, rz;
		do {
			rx = rand.nextInt(2 * rad);
			rz = rand.nextInt(2 * rad);
		} while (!isInHill(rx, rz));
		
		int[] coords = {rx, rz};
		return coords;
	}
	
	/**
	 * Gets the id of a mob appropriate to the current hill size.
	 */
	protected String getMobID(Random rand)
	{
		return getMobID(rand, this.hillSize);
	}

	
	/**
	 * Gets the id of a mob appropriate to the specified hill size.
	 * 
	 * @param level
	 * @return
	 */
	protected String getMobID(Random rand, int level)
	{
		if (level == 1)
		{
			return getLevel1Mob(rand);
		}
		if (level == 2)
		{
			return getLevel2Mob(rand);
		}
		if (level == 3)
		{
			return getLevel3Mob(rand);
		}
		
		/// aaah, default: spider!
		return "Spider";
	}
	
	/**
	 * Returns a mob string appropriate for a level 1 hill
	 */
	public String getLevel1Mob(Random rand)
	{
		switch (rand.nextInt(10))
		{
		case 0:
		case 1:
		case 2:
			return TFCreatures.getSpawnerNameFor("Swarm Spider");
		case 3:
		case 4:
		case 5:
			return "Spider";
		case 6:
		case 7:
			return "Zombie";
		case 8:
			return "Silverfish";
		case 9:
			return TFCreatures.getSpawnerNameFor("Redcap");
		default:
			return TFCreatures.getSpawnerNameFor("Swarm Spider");
		}
	}
	
	/**
	 * Returns a mob string appropriate for a level 2 hill
	 */
	public String getLevel2Mob(Random rand)
	{
		switch (rand.nextInt(10))
		{
		case 0:
		case 1:
		case 2:
			return TFCreatures.getSpawnerNameFor("Redcap");
		case 3:
		case 4:
		case 5:
			return "Zombie";
		case 6:
		case 7:
			return "Skeleton";
		case 8:
			return TFCreatures.getSpawnerNameFor("Swarm Spider");
		case 9:
			return "CaveSpider";
		default:
			return TFCreatures.getSpawnerNameFor("Redcap");
		}
	}
	
	/**
	 * Returns a mob string appropriate for a level 3 hill.  The level 3 also has 2 mid-air wraith spawners.
	 */
	public String getLevel3Mob(Random rand)
	{
		switch (rand.nextInt(11))
		{
		case 0:
			return TFCreatures.getSpawnerNameFor("Slime Beetle");
		case 1:
			return TFCreatures.getSpawnerNameFor("Fire Beetle");
		case 2:
			return TFCreatures.getSpawnerNameFor("Pinch Beetle");
		case 3:
		case 4:
		case 5:
			return "Skeleton";
		case 6:
		case 7:
		case 8:
			return "CaveSpider";
		case 9:
			return "Creeper";
		case 10:
		default:
			return TFCreatures.getSpawnerNameFor("Twilight Wraith");
		}
	}

}
