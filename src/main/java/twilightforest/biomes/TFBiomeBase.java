package twilightforest.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.passive.EntityTFMobileFirefly;

public class TFBiomeBase extends Biome  {
	public static final Biome tfLake = new TFBiomeTwilightLake(new BiomeProperties("Twilight Lake").setTemperature(0.66F).setRainfall(1).setBaseHeight(-1.8F).setHeightVariation(0.1F));
	public static final Biome twilightForest = new TFBiomeBase(new BiomeProperties("Twilight Forest"));
	public static final Biome twilightForest2 = new TFBiomeTwilightForestVariant(new BiomeProperties("Dense Twilight Forest").setWaterColor(0x005522).setTemperature(0.7F).setRainfall(0.8F).setBaseHeight(0.2F).setHeightVariation(0.2F));
	public static final Biome highlands = new TFBiomeHighlands(new BiomeProperties("Twilight Highlands").setTemperature(0.4F).setRainfall(0.7F).setBaseHeight(3.5F).setHeightVariation(0.05F));
	public static final Biome mushrooms = new TFBiomeMushrooms(new BiomeProperties("Mushroom Forest").setTemperature(0.8F).setRainfall(0.8F));
	public static final Biome tfSwamp = new TFBiomeSwamp(new BiomeProperties("Twilight Swamp").setTemperature(0.8F).setRainfall(0.9F).setBaseHeight(-0.125F).setHeightVariation(0.125F).setWaterColor(0xE0FFAE));
	public static final Biome stream = new TFBiomeStream(new BiomeProperties("Twilight Stream").setTemperature(0.5F).setRainfall(0.1F).setBaseHeight(-0.5F).setHeightVariation(0));
	public static final Biome tfSnow = new TFBiomeSnow(new BiomeProperties("Snowy Forest").setTemperature(0.125F).setRainfall(0.9F).setBaseHeight(0.2F).setHeightVariation(0.2F));
	public static final Biome glacier = new TFBiomeGlacier(new BiomeProperties("Twilight Glacier").setTemperature(0).setRainfall(0.1F));
	public static final Biome clearing = new TFBiomeClearing(new BiomeProperties("Twilight Clearing").setTemperature(0.8F).setRainfall(0.4F).setBaseHeight(0.125F).setHeightVariation(0.05F));
	public static final Biome oakSavanna = new TFBiomeOakSavanna(new BiomeProperties("Oak Savanna").setTemperature(0.9F).setRainfall(0).setBaseHeight(0.2F).setHeightVariation(0.2F));
	public static final Biome fireflyForest = new TFBiomeFireflyForest(new BiomeProperties("Firefly Forest").setTemperature(0.5F).setRainfall(1).setBaseHeight(0.125F).setHeightVariation(0.05F));
	public static final Biome deepMushrooms = new TFBiomeDeepMushrooms(new BiomeProperties("Deep Mushroom Forest").setTemperature(0.8F).setRainfall(1).setBaseHeight(0.125F).setHeightVariation(0.05F));
	public static final Biome darkForest = new TFBiomeDarkForest(new BiomeProperties("Dark Forest").setTemperature(0.7F).setRainfall(0.8F).setBaseHeight(0.125F).setHeightVariation(0.05F));
	public static final Biome enchantedForest = new TFBiomeEnchantedForest(new BiomeProperties("Enchanted Forest"));
	public static final Biome fireSwamp = new TFBiomeFireSwamp(new BiomeProperties("Fire Swamp").setTemperature(1).setRainfall(0.4F).setWaterColor(0x6C2C2C).setBaseHeight(0.1F).setHeightVariation(0.2F));
	public static final Biome darkForestCenter = new TFBiomeDarkForestCenter(new BiomeProperties("Dark Forest Center").setBaseHeight(0.125F).setHeightVariation(0.05F));
	public static final Biome highlandsCenter = new TFBiomeFinalPlateau(new BiomeProperties("Highlands Center").setTemperature(0.3F).setRainfall(0.2F).setBaseHeight(10.5F).setHeightVariation(0.025F));
	public static final Biome thornlands = new TFBiomeThornlands(new BiomeProperties("Thornlands").setTemperature(0.3F).setRainfall(0.2F).setBaseHeight(6).setHeightVariation(0.1F));

	protected WorldGenBigMushroom bigMushroomGen;
	protected WorldGenBirchTree birchGen;
    protected List<SpawnListEntry> undergroundMonsterList;

	public TFBiomeBase(BiomeProperties props)
	{
		super(props);

		bigMushroomGen = new WorldGenBigMushroom();
		birchGen = new WorldGenBirchTree(false, false);
		
    	// remove normal monster spawns
        spawnableMonsterList.clear();
        // remove squids
        spawnableWaterCreatureList.clear();
        // custom creature list.
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBighorn.class, 12, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBoar.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntityChicken.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFDeer.class, 15, 4, 5));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntityWolf.class, 5, 4, 4));
        
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFTinyBird.class, 15, 4, 8));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFSquirrel.class, 10, 2, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBunny.class, 10, 4, 5));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFRaven.class, 10, 1, 2));
        
        undergroundMonsterList = new ArrayList<SpawnListEntry>();
        
        undergroundMonsterList.add(new SpawnListEntry(EntitySpider.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityTFKobold.class, 10, 4, 8));

		this.spawnableCaveCreatureList.clear();
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
        this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFMobileFirefly.class, 10, 8, 8));
        
        getTFBiomeDecorator().setTreesPerChunk(10);
        getTFBiomeDecorator().setGrassPerChunk(2);
	}

    @Override
    public float getSpawningChance()
    {
    	// okay, 20% more animals
        return 0.12F;
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new TFBiomeDecorator();
    }
    
    protected TFBiomeDecorator getTFBiomeDecorator() {
    	return (TFBiomeDecorator)this.theBiomeDecorator;
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random)
    {
        if(random.nextInt(5) == 0)
        {
        	return birchGen;
        }
        if(random.nextInt(10) == 0)
        {
            return new WorldGenBigTree(false);
        } else
        {
            return TREE_FEATURE;
        }
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
    {
        if (par1Random.nextInt(4) == 0)
        {
            return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
        }
        else
        {
            return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
        }
    }

	public static void registerWithBiomeDictionary()
	{
		BiomeDictionary.registerBiomeType(tfLake, Type.OCEAN);
		BiomeDictionary.registerBiomeType(twilightForest, Type.FOREST);
		BiomeDictionary.registerBiomeType(twilightForest2, Type.FOREST, Type.DENSE);
		BiomeDictionary.registerBiomeType(highlands, Type.FOREST, Type.MOUNTAIN, Type.CONIFEROUS);
		BiomeDictionary.registerBiomeType(mushrooms, Type.FOREST, Type.MUSHROOM);
		BiomeDictionary.registerBiomeType(tfSwamp, Type.SWAMP, Type.WET);
		BiomeDictionary.registerBiomeType(stream, Type.RIVER);
		BiomeDictionary.registerBiomeType(tfSnow, Type.FOREST, Type.SNOWY, Type.COLD, Type.CONIFEROUS);
		BiomeDictionary.registerBiomeType(glacier, Type.COLD, Type.SNOWY, Type.WASTELAND);
		BiomeDictionary.registerBiomeType(clearing, Type.PLAINS, Type.SPARSE);
		BiomeDictionary.registerBiomeType(oakSavanna, Type.FOREST, Type.SPARSE);
		BiomeDictionary.registerBiomeType(fireflyForest, Type.FOREST, Type.LUSH);
		BiomeDictionary.registerBiomeType(deepMushrooms, Type.FOREST, Type.MUSHROOM);
		BiomeDictionary.registerBiomeType(darkForest, Type.FOREST, Type.DENSE, Type.SPOOKY);
		BiomeDictionary.registerBiomeType(enchantedForest, Type.FOREST, Type.MAGICAL);
		BiomeDictionary.registerBiomeType(fireSwamp, Type.SWAMP, Type.WASTELAND, Type.HOT);
		BiomeDictionary.registerBiomeType(darkForestCenter, Type.FOREST, Type.DENSE, Type.SPOOKY, Type.MAGICAL);
		BiomeDictionary.registerBiomeType(highlandsCenter, Type.MESA, Type.DEAD, Type.DRY, Type.WASTELAND);
		BiomeDictionary.registerBiomeType(thornlands, Type.HILLS, Type.DEAD, Type.DRY, Type.WASTELAND);
	}

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal)
    {
        this.genTwilightBiomeTerrain(world, rand, primer, x, z, noiseVal);
    }

    // Copy of super's generateBiomeTerrain, relevant edits noted.
    protected void genTwilightBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        int i = 32; // TF - set sea level to 32
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    // j = -1; TF - commented out? todo 1.9
                }
                else if (iblockstate2.getBlock() == Blocks.STONE)
                {
                    // TF - Replace stone
                    if (getStoneReplacementState() != null) {
                        chunkPrimerIn.setBlockState(i1, j1, l, getStoneReplacementState());
                    }

                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                        {
                            if (this.getFloatTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F)
                            {
                                iblockstate = ICE;
                            }
                            else
                            {
                                iblockstate = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.SAND)
                        {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                        }
                    }
                }
            }
        }
    }

    /**
     * Return a block if you want it to replace stone in the terrain generation
     */
	public IBlockState getStoneReplacementState() {
		return null;
	}
	
	/**
	 * Does the player have the achievement needed to be in this biome?
	 * Defaults to true for no achievement required biomes.
	 */
	public boolean doesPlayerHaveRequiredAchievement(EntityPlayer player) {
		
		if (getRequiredAchievement() != null && player instanceof EntityPlayerMP && ((EntityPlayerMP)player).getStatFile() != null) {
			StatisticsManagerServer stats = ((EntityPlayerMP)player).getStatFile();
			
			return stats.hasAchievementUnlocked(this.getRequiredAchievement());
		} else if (getRequiredAchievement() != null && player instanceof EntityPlayerSP && ((EntityPlayerSP)player).getStatFileWriter() != null) {
			StatisticsManager stats = ((EntityPlayerSP)player).getStatFileWriter();
			
			return stats.hasAchievementUnlocked(this.getRequiredAchievement());
		} else {
			return true;
		}
	}

	/**
	 * If there is a required achievement to be here, return it, otherwise return null
	 */
	protected Achievement getRequiredAchievement() {
		return null;
	}

	/**
	 * Do something bad to a player in the wrong biome.
	 */
	public void enforceProgession(EntityPlayer player, World world) {
		;
	}
	
    /**
     * Returns the list of underground creatures.
     */
	public List<SpawnListEntry> getUndergroundSpawnableList() {
    	return this.undergroundMonsterList;
    }
}

