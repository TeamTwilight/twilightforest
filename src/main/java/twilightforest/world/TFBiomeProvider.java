package twilightforest.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.layer.GenLayerTF;


public class TFBiomeProvider extends BiomeProvider
{
    private GenLayer unzoomedBiomes;

    /** A GenLayer containing the indices into Biome.biomeList[] */
    private GenLayer zoomedBiomes;

    /** The BiomeCache object for this world. */
    private BiomeCache myBiomeCache;

    /** A list of biomes that the player can spawn in. */
    private List<Biome> myBiomesToSpawnIn;

    protected TFBiomeProvider()
    {
        myBiomeCache = new BiomeCache(this);
        myBiomesToSpawnIn = new ArrayList<Biome>();
        myBiomesToSpawnIn.add(TFBiomes.twilightForest);
        myBiomesToSpawnIn.add(TFBiomes.denseTwilightForest);
        myBiomesToSpawnIn.add(TFBiomes.clearing);
        myBiomesToSpawnIn.add(TFBiomes.tfSwamp);
        myBiomesToSpawnIn.add(TFBiomes.mushrooms);
    }

    public TFBiomeProvider(long par1, WorldType par3WorldType)
    {
        this();
        GenLayer[] agenlayer = GenLayerTF.makeTheWorld(par1);
        unzoomedBiomes = agenlayer[0];
        zoomedBiomes = agenlayer[1];
    }

    public TFBiomeProvider(World par1World)
    {
        this(par1World.getSeed(), par1World.getWorldInfo().getTerrainType());
    }

    @Override
	public List<Biome> getBiomesToSpawnIn()
    {
        return myBiomesToSpawnIn;
    }

    @Override
    public float getTemperatureAtHeight(float par1, int par2)
    {
        return par1;
    }

    @Override
    public Biome[] getBiomesForGeneration(Biome par1ArrayOfBiome[], int x, int z, int length, int width)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < length * width)
        {
            par1ArrayOfBiome = new Biome[length * width];
        }

        int arrayOfInts[] = unzoomedBiomes.getInts(x, z, length, width);

        for (int i = 0; i < length * width; i++)
        {
        	// biome validity check
        	if (arrayOfInts[i] >= 0)
        	{
                par1ArrayOfBiome[i] = Biome.getBiome(arrayOfInts[i]);
        	}
        	else
        	{
        		//System.err.println("Got bad biome data : " + ai[i]);
        		par1ArrayOfBiome[i] = TFBiomes.twilightForest;
        	}
        }

        return par1ArrayOfBiome;
    }

    @Override
    public Biome[] getBiomes(Biome par1ArrayOfBiome[], int x, int y, int width, int length, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < width * length)
        {
            par1ArrayOfBiome = new Biome[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 0xf) == 0 && (y & 0xf) == 0)
        {
            Biome abiomegenbase[] = myBiomeCache.getCachedBiomes(x, y);
            System.arraycopy(abiomegenbase, 0, par1ArrayOfBiome, 0, width * length);
            return par1ArrayOfBiome;
        }

        int ai[] = zoomedBiomes.getInts(x, y, width, length);

        for (int i = 0; i < width * length; i++)
        {
        	// biome validity check
        	if (ai[i] >= 0)
        	{
                par1ArrayOfBiome[i] = Biome.getBiome(ai[i]);
        	}
        	else
        	{
        		//System.err.println("Got bad biome data : " + ai[i]);
        		par1ArrayOfBiome[i] = TFBiomes.twilightForest;
        	}
        }

        return par1ArrayOfBiome;
    }

    @Override
	public boolean areBiomesViable(int par1, int par2, int par3, List<Biome> par4List)
    {
        int i = par1 - par3 >> 2;
        int j = par2 - par3 >> 2;
        int k = par1 + par3 >> 2;
        int l = par2 + par3 >> 2;
        int i1 = (k - i) + 1;
        int j1 = (l - j) + 1;
        int ai[] = unzoomedBiomes.getInts(i, j, i1, j1);

        for (int k1 = 0; k1 < i1 * j1; k1++)
        {
            Biome biomegenbase = Biome.getBiome(ai[k1]);

            if (!par4List.contains(biomegenbase))
            {
                return false;
            }
        }

        return true;
    }

    @Override
	public BlockPos findBiomePosition(int par1, int par2, int par3, List<Biome> par4List, Random par5Random)
    {
        int i = par1 - par3 >> 2;
        int j = par2 - par3 >> 2;
        int k = par1 + par3 >> 2;
        int l = par2 + par3 >> 2;
        int i1 = (k - i) + 1;
        int j1 = (l - j) + 1;
        int ai[] = unzoomedBiomes.getInts(i, j, i1, j1);
        BlockPos chunkposition = null;
        int k1 = 0;

        for (int l1 = 0; l1 < ai.length; l1++)
        {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            Biome biomegenbase = Biome.getBiome(ai[l1]);

            if (par4List.contains(biomegenbase) && (chunkposition == null || par5Random.nextInt(k1 + 1) == 0))
            {
                chunkposition = new BlockPos(i2, 0, j2);
                k1++;
            }
        }

        return chunkposition;
    }

    @Override
    public void cleanupCache()
    {
        myBiomeCache.cleanupCache();
    }
    
    /**
     * Returns feature ID at the specified location
     */
    public int getFeatureID(int mapX, int mapZ, World world)
    {
		return getFeatureAt(mapX, mapZ, world).featureID;
	}

    /**
     * Returns feature at the specified location
     */
    public TFFeature getFeatureAt(int mapX, int mapZ, World world)
    {
		return TFFeature.generateFeatureFor1Point7(mapX >> 4, mapZ >> 4, world);
    }

	/**
     * Checks if the coordinates are in a feature chunk.
	 * @param world 
     */
    public boolean isInFeatureChunk(World world, int mapX, int mapZ) {
		int chunkX = mapX >> 4;
		int chunkZ = mapZ >> 4;
		BlockPos cc = TFFeature.getNearestCenterXYZ(chunkX, chunkZ, world);
		
		return chunkX == (cc.getX() >> 4) && chunkZ == (cc.getZ() >> 4);
    	
//    	logic moved to TFFeature.getNearestCenterXYZ :
		
//    	int chunkX = (mapX + 128) >> 4; 
//    	int chunkZ = (mapZ + 128) >> 4;
//    	
//    	// generate random number for the whole biome area
//    	int regionX = chunkX >> 4;
//    	int regionZ = chunkZ >> 4;
//    	
//	    long seed = (long)(regionX * 3129871) ^ (long)regionZ * 116129781L;
//	    seed = seed * seed * 42317861L + seed * 7L;
//	    
//	    int num0 = (int) (seed >> 12 & 3L);
//	    int num1 = (int) (seed >> 15 & 3L);
//	    int num2 = (int) (seed >> 18 & 3L);
//	    int num3 = (int) (seed >> 21 & 3L);
//
//	    // slightly randomize center of biome (+/- 3)
//	    int centerX = 8 + num0 - num1;
//	    int centerZ = 8 + num2 - num3;
//    	
//    	return Math.abs(chunkX % 16) == centerX && Math.abs(chunkZ % 16) == centerZ;

    }
}
