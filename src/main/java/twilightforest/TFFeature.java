package twilightforest;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import twilightforest.biomes.TFBiomes;
import twilightforest.entity.*;
import twilightforest.world.TFBiomeProvider;
import twilightforest.world.TFWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TFFeature {

	public static final TFFeature[] featureList = new TFFeature[256];

	public static final TFFeature nothing = new TFFeature(0, 0, "No Feature").enableDecorations().disableStructure();
	public static final TFFeature hill1 = new TFFeature(1, 1, "Small Hollow Hill").enableDecorations().enableTerrainAlterations();
	public static final TFFeature hill2 = new TFFeature(2, 2, "Medium Hollow Hill").enableDecorations().enableTerrainAlterations();
	public static final TFFeature hill3 = new TFFeature(3, 3, "Large Hollow Hill").enableDecorations().enableTerrainAlterations();
	public static final TFFeature hedgeMaze = new TFFeature(4, 2, "Hedge Maze").enableTerrainAlterations();
	public static final TFFeature nagaCourtyard = new TFFeature(5, 3, "Naga Courtyard").enableTerrainAlterations();
	public static final TFFeature lichTower = new TFFeature(6, 1, "Lich Tower").setRequiredAchievement(TFAchievementPage.twilightKillNaga);
	public static final TFFeature iceTower = new TFFeature(7, 2, "Ice Tower").setRequiredAchievement(TFAchievementPage.twilightProgressYeti);
	public static final TFFeature questIsland = new TFFeature(8, 1, "Quest Island").disableStructure();
	public static final TFFeature questGrove = new TFFeature(9, 1, "Quest Grove").enableTerrainAlterations();
	public static final TFFeature druidGrove = new TFFeature(10, 1, "Druid Grove").disableStructure();
	public static final TFFeature floatRuins = new TFFeature(11, 3, "Floating Ruins").disableStructure();
	public static final TFFeature hydraLair = new TFFeature(12, 2, "Hydra Lair").setRequiredAchievement(TFAchievementPage.twilightProgressLabyrinth).enableTerrainAlterations();
	public static final TFFeature labyrinth = new TFFeature(13, 3, "Labyrinth").enableDecorations().setRequiredAchievement(TFAchievementPage.twilightKillLich);
	public static final TFFeature darkTower = new TFFeature(14, 1, "Dark Tower").setRequiredAchievement(TFAchievementPage.twilightProgressKnights);
	public static final TFFeature tfStronghold = new TFFeature(15, 3, "Knight Stronghold").enableDecorations().setRequiredAchievement(TFAchievementPage.twilightProgressTrophyPedestal).disableProtectionAura();
	public static final TFFeature worldTree = new TFFeature(16, 3, "World Tree").disableStructure();
	public static final TFFeature yetiCave = new TFFeature(17, 2, "Yeti Lairs").enableDecorations().enableTerrainAlterations().setRequiredAchievement(TFAchievementPage.twilightProgressUrghast);
	public static final TFFeature trollCave = new TFFeature(18, 3, "Troll Lairs").enableDecorations().enableTerrainAlterations().setRequiredAchievement(TFAchievementPage.twilightProgressGlacier).disableProtectionAura();
	public static final TFFeature finalCastle = new TFFeature(19, 3, "Final Castle");
	public static final TFFeature mushroomTower = new TFFeature(20, 2, "Mushroom Tower");

	static {
		// spawn lists!
		lichTower.addMonster(EntityZombie.class, 10, 4, 4);
		lichTower.addMonster(EntitySkeleton.class, 10, 4, 4);
		lichTower.addMonster(EntityCreeper.class, 1, 4, 4);
		lichTower.addMonster(EntityEnderman.class, 1, 1, 4);
		lichTower.addMonster(EntityTFDeathTome.class, 10, 4, 4);
		lichTower.addMonster(EntityWitch.class, 1, 1, 1);

		hill1.addMonster(EntitySpider.class, 10, 4, 4);
		hill1.addMonster(EntityZombie.class, 10, 4, 4);
		hill1.addMonster(EntityTFRedcap.class, 10, 4, 4);
		hill1.addMonster(EntityTFSwarmSpider.class, 10, 4, 4);
		hill1.addMonster(EntityTFKobold.class, 10, 4, 8);

		hill2.addMonster(EntityTFRedcap.class, 10, 4, 4);
		hill2.addMonster(EntityTFRedcapSapper.class, 1, 1, 4);
		hill2.addMonster(EntityTFKobold.class, 10, 4, 8);
		hill2.addMonster(EntitySkeleton.class, 10, 4, 4);
		hill2.addMonster(EntityTFSwarmSpider.class, 10, 4, 4);
		hill2.addMonster(EntitySpider.class, 10, 4, 4);
		hill2.addMonster(EntityCreeper.class, 10, 4, 4);
		hill2.addMonster(EntityTFFireBeetle.class, 5, 4, 4);
		hill2.addMonster(EntityTFSlimeBeetle.class, 5, 4, 4);
		hill2.addMonster(EntityWitch.class, 1, 1, 1);

		hill3.addMonster(EntityTFRedcap.class, 10, 4, 4);
		hill3.addMonster(EntityTFRedcapSapper.class, 2, 1, 4);
		hill3.addMonster(EntitySkeleton.class, 10, 4, 4);
		hill3.addMonster(EntityCaveSpider.class, 10, 4, 4);
		hill3.addMonster(EntityCreeper.class, 10, 4, 4);
		hill3.addMonster(EntityEnderman.class, 1, 1, 4);
		hill3.addMonster(EntityTFWraith.class, 2, 1, 4);
		hill3.addMonster(EntityTFFireBeetle.class, 10, 4, 4);
		hill3.addMonster(EntityTFSlimeBeetle.class, 10, 4, 4);
		hill3.addMonster(EntityTFPinchBeetle.class, 10, 2, 4);
		hill3.addMonster(EntityWitch.class, 1, 1, 1);

		labyrinth.addMonster(EntityTFMinotaur.class, 20, 2, 4);
		labyrinth.addMonster(EntityCaveSpider.class, 10, 4, 4);
		labyrinth.addMonster(EntityCreeper.class, 10, 4, 4);
		labyrinth.addMonster(EntityTFMazeSlime.class, 10, 4, 4);
		labyrinth.addMonster(EntityEnderman.class, 1, 1, 4);
		labyrinth.addMonster(EntityTFFireBeetle.class, 10, 4, 4);
		labyrinth.addMonster(EntityTFSlimeBeetle.class, 10, 4, 4);
		labyrinth.addMonster(EntityTFPinchBeetle.class, 10, 2, 4);

		darkTower.addMonster(EntityTFTowerGolem.class, 10, 4, 4);
		darkTower.addMonster(EntitySkeleton.class, 10, 4, 4);
		darkTower.addMonster(EntityCreeper.class, 10, 4, 4);
		darkTower.addMonster(EntityEnderman.class, 2, 1, 4);
		darkTower.addMonster(EntityWitch.class, 1, 1, 1);
		darkTower.addMonster(EntityTFMiniGhast.class, 10, 1, 4);
		darkTower.addMonster(EntityTFTowerBroodling.class, 10, 8, 8);
		darkTower.addMonster(EntityTFPinchBeetle.class, 10, 2, 4);
		// roof ghasts
		darkTower.addMonster(1, EntityTFTowerGhast.class, 10, 1, 4);
		// aquarium squids (only in aquariums between y = 35 and y = 64. :/
		darkTower.addWaterCreature(EntitySquid.class, 10, 4, 4);

		tfStronghold.addMonster(EntityTFBlockGoblin.class, 10, 4, 4);
		tfStronghold.addMonster(EntityTFGoblinKnightLower.class, 5, 1, 2);
		tfStronghold.addMonster(EntityTFHelmetCrab.class, 10, 4, 4);
		tfStronghold.addMonster(EntityTFSlimeBeetle.class, 10, 4, 4);
		tfStronghold.addMonster(EntityTFRedcapSapper.class, 2, 1, 4);
		tfStronghold.addMonster(EntityTFKobold.class, 10, 4, 8);
		tfStronghold.addMonster(EntityCreeper.class, 10, 4, 4);
		tfStronghold.addMonster(EntitySlime.class, 5, 4, 4);

		yetiCave.addMonster(EntityTFYeti.class, 10, 4, 4);

		iceTower.addMonster(EntityTFSnowGuardian.class, 10, 4, 4);
		iceTower.addMonster(EntityTFIceShooter.class, 10, 4, 4);
		iceTower.addMonster(EntityTFIceExploder.class, 5, 4, 4);

		trollCave.addMonster(EntityCreeper.class, 5, 4, 4);
		trollCave.addMonster(EntitySkeleton.class, 10, 4, 4);
		trollCave.addMonster(EntityTFTroll.class, 20, 4, 4);
		trollCave.addMonster(EntityWitch.class, 5, 1, 1);
		// cloud monsters
		trollCave.addMonster(1, EntityTFGiantMiner.class, 10, 1, 4);
		trollCave.addMonster(1, EntityTFArmoredGiant.class, 10, 1, 4);

		// plain parts of the castle, like the tower maze
		finalCastle.addMonster(EntityTFKobold.class, 10, 4, 4);
		finalCastle.addMonster(EntityTFAdherent.class, 10, 1, 1);
		finalCastle.addMonster(EntityTFHarbingerCube.class, 10, 1, 1);
		finalCastle.addMonster(EntityEnderman.class, 10, 1, 1);

		// internal castle
		finalCastle.addMonster(1, EntityTFKobold.class, 10, 4, 4);
		finalCastle.addMonster(1, EntityTFAdherent.class, 10, 1, 1);
		finalCastle.addMonster(1, EntityTFHarbingerCube.class, 10, 1, 1);
		finalCastle.addMonster(1, EntityTFArmoredGiant.class, 10, 1, 1);

		// dungeons
		finalCastle.addMonster(2, EntityTFAdherent.class, 10, 1, 1);

		// forge
		finalCastle.addMonster(3, EntityBlaze.class, 10, 1, 1);

	}

	public int featureID;
	public int size;
	public String name;
	public boolean areChunkDecorationsEnabled;
	public boolean isStructureEnabled;
	public boolean isTerrainAltered;
	private List<List<SpawnListEntry>> spawnableMonsterLists;
	private List<SpawnListEntry> ambientCreatureList;
	private List<SpawnListEntry> waterCreatureList;
	private Achievement requiredAchievement = null;
	public boolean hasProtectionAura;

	private long lastSpawnedHintMonsterTime;

	public TFFeature(int parID, int parSize, String parName) {
		this.featureID = parID;
		TFFeature.featureList[parID] = this;
		this.size = parSize;
		this.name = parName;
		this.areChunkDecorationsEnabled = false;
		this.isStructureEnabled = true;
		this.isTerrainAltered = false;
		this.spawnableMonsterLists = new ArrayList<List<SpawnListEntry>>();
		this.ambientCreatureList = new ArrayList<SpawnListEntry>();
		this.waterCreatureList = new ArrayList<SpawnListEntry>();
		this.hasProtectionAura = true;

		ambientCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
	}

	public static int getFeatureID(int mapX, int mapZ, World world) {
		return getFeatureAt(mapX, mapZ, world).featureID;
	}

	public static TFFeature getFeatureAt(int mapX, int mapZ, World world) {
		return generateFeature(mapX >> 4, mapZ >> 4, world);
	}

	public static boolean isInFeatureChunk(World world, int mapX, int mapZ) {
		int chunkX = mapX >> 4;
		int chunkZ = mapZ >> 4;
		BlockPos cc = getNearestCenterXYZ(chunkX, chunkZ, world);

		return chunkX == (cc.getX() >> 4) && chunkZ == (cc.getZ() >> 4);
	}

	/**
	 * Turns on biome-specific decorations like grass and trees near this feature.
	 */
	public TFFeature enableDecorations() {
		this.areChunkDecorationsEnabled = true;
		return this;
	}

	/**
	 * Tell the chunkgenerator that we don't have an associated structure.
	 */
	public TFFeature disableStructure() {
		this.isStructureEnabled = false;
		return this;
	}

	/**
	 * Tell the chunkgenerator that we want the terrain changed nearby.
	 */
	public TFFeature enableTerrainAlterations() {
		this.isTerrainAltered = true;
		return this;
	}

	public TFFeature disableProtectionAura() {
		this.hasProtectionAura = false;
		return this;
	}

	/**
	 * Add a monster to spawn list 0
	 */
	public TFFeature addMonster(Class<? extends EntityLiving> monsterClass, int weight, int minGroup, int maxGroup) {
		this.addMonster(0, monsterClass, weight, minGroup, maxGroup);
		return this;
	}

	/**
	 * Add a monster to a specific spawn list
	 */
	public TFFeature addMonster(int listIndex, Class<? extends EntityLiving> monsterClass, int weight, int minGroup, int maxGroup) {
		List<SpawnListEntry> monsterList;
		if (this.spawnableMonsterLists.size() > listIndex) {
			monsterList = this.spawnableMonsterLists.get(listIndex);
		} else {
			monsterList = new ArrayList<SpawnListEntry>();
			this.spawnableMonsterLists.add(listIndex, monsterList);
		}

		monsterList.add(new SpawnListEntry(monsterClass, weight, minGroup, maxGroup));
		return this;
	}

	/**
	 * Add a water creature
	 */
	public TFFeature addWaterCreature(Class<? extends EntityLiving> monsterClass, int weight, int minGroup, int maxGroup) {
		this.waterCreatureList.add(new SpawnListEntry(monsterClass, weight, minGroup, maxGroup));
		return this;
	}

	/**
	 * @return The type of feature directly at the specified Chunk coordinates
	 */
	public static TFFeature getFeatureDirectlyAt(int chunkX, int chunkZ, World world) {

		if (world != null && world.getBiomeProvider() instanceof TFBiomeProvider) {
			TFBiomeProvider tfManager = (TFBiomeProvider) world.getBiomeProvider();

			if (isInFeatureChunk(world, chunkX << 4, chunkZ << 4)) {
				return getFeatureAt(chunkX << 4, chunkZ << 4, world);
			} else {
				return nothing;
			}
		} else {
			return nothing;
		}
	}

	/**
	 * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
	 * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
	 */
	public static TFFeature generateFeature(int chunkX, int chunkZ, World world) {
		// set the chunkX and chunkZ to the center of the biome
		chunkX = Math.round(chunkX / 16F) * 16;
		chunkZ = Math.round(chunkZ / 16F) * 16;

		// what biome is at the center of the chunk?
		Biome biomeAt = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));

		// get random value
		Random hillRNG = new Random(world.getSeed() + chunkX * 25117 + chunkZ * 151121);
		int randnum = hillRNG.nextInt(16);

		// glaciers have ice towers
		if (biomeAt == TFBiomes.glacier) {
			return iceTower;
		}
		// snow has yeti lair
		if (biomeAt == TFBiomes.snowy_forest) {
			return yetiCave;
		}

		// lakes have quest islands
		if (biomeAt == TFBiomes.tfLake) {
			return questIsland;
		}

		// enchanted forests have groves
		if (biomeAt == TFBiomes.enchantedForest) {
			return questGrove;
		}

		// fire swamp has hydra lair
		if (biomeAt == TFBiomes.fireSwamp) {
			return hydraLair;
		}
		// swamp has labyrinth
		if (biomeAt == TFBiomes.tfSwamp) {
			return labyrinth;
		}

		// dark forests have their own things
		if (biomeAt == TFBiomes.darkForest) {
			return tfStronghold;
		}
		if (biomeAt == TFBiomes.darkForestCenter) {
			return darkTower;
		}

		// highlands center has castle
		if (biomeAt == TFBiomes.highlandsCenter) {
			return finalCastle;
		}
		// highlands has trolls
		if (biomeAt == TFBiomes.highlands) {
			return trollCave;
		}

		// deep mushrooms has mushroom tower
		if (biomeAt == TFBiomes.deepMushrooms) {
			return mushroomTower;
		}

		int regionOffsetX = Math.abs((chunkX + 64 >> 4) % 8);
		int regionOffsetZ = Math.abs((chunkZ + 64 >> 4) % 8);

		// plant two lich towers near the center of each 2048x2048 map area
		if ((regionOffsetX == 4 && regionOffsetZ == 5) || (regionOffsetX == 4 && regionOffsetZ == 3)) {
			return lichTower;
		}

		// also two nagas
		if ((regionOffsetX == 5 && regionOffsetZ == 4) || (regionOffsetX == 3 && regionOffsetZ == 4)) {
			return nagaCourtyard;
		}

		// okay, well that takes care of most special cases
		switch (randnum) {
			default:
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				return hill1;
			case 6:
			case 7:
			case 8:
				return hill2;
			case 9:
				return hill3;
			case 10:
			case 11:
				return hedgeMaze;
			case 12:
			case 13:
				return nagaCourtyard;
			case 14:
			case 15:
				return lichTower;
		}
	}

	/**
	 * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
	 * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
	 */
	public static TFFeature generateFeaturePreset5x5(int chunkX, int chunkZ, World world) {
		int cf = 16;

		if (chunkX % cf != 0 || chunkZ % cf != 0) {
			return TFFeature.nothing;
		}

		int mx = (chunkX / cf) + 4;
		int mz = (chunkZ / cf) + 4;


		int[][] map = {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 19, 18, 8, 15, 14, 0},
				{0, 0, 18, 18, 2, 3, 15, 0},
				{0, 0, 4, 4, 5, 16, 9, 0},
				{0, 0, 13, 6, 1, 2, 17, 0},
				{0, 0, 12, 13, 3, 17, 7, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};

		if (mx >= 0 && mx < 8 && mz >= 0 && mz < 8) {
			return TFFeature.featureList[map[mz][mx]];
		} else {
			return TFFeature.nothing;
		}
	}

	/**
	 * What feature would go in this chunk.  Called when we know there is a feature, but there is no cache data,
	 * either generating this chunk for the first time, or using the magic map to forecast beyond the edge of the world.
	 */
	public static TFFeature generateFeaturePreset6x6(int chunkX, int chunkZ, World world) {
		int cf = 16;

		if (chunkX % cf != 0 || chunkZ % cf != 0) {
			return TFFeature.nothing;
		}

		int mx = (chunkX / cf) + 3;
		int mz = (chunkZ / cf) + 3;


		int[][] map = {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 19, 19, 18, 15, 0, 0, 0},
				{0, 18, 18, 18, 0, 14, 0, 0},
				{0, 0, 4, 1, 2, 3, 15, 0},
				{0, 4, 1, 5, 16, 9, 17, 0},
				{0, 0, 13, 2, 3, 17, 17, 0},
				{0, 0, 12, 13, 6, 17, 7, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};

		if (mx >= 0 && mx < 8 && mz >= 0 && mz < 8) {
			return TFFeature.featureList[map[mz][mx]];
		} else {
			return TFFeature.nothing;
		}
	}

	/**
	 * @return The feature nearest to the specified chunk coordinates
	 */
	public static TFFeature getNearestFeature(int cx, int cz, World world) {
		for (int rad = 1; rad <= 3; rad++) {
			for (int x = -rad; x <= rad; x++) {
				for (int z = -rad; z <= rad; z++) {
					TFFeature directlyAt = getFeatureDirectlyAt(x + cx, z + cz, world);
					if (directlyAt.size == rad) {
						return directlyAt;
					}
				}
			}
		}

		return nothing;
	}

	/**
	 * @return The feature in the chunk "region"
	 */
	public static TFFeature getFeatureForRegion(int chunkX, int chunkZ, World world) {
		//just round to the nearest multiple of 16 chunks?
		int featureX = Math.round(chunkX / 16F) * 16;
		int featureZ = Math.round(chunkZ / 16F) * 16;

		return TFFeature.generateFeature(featureX, featureZ, world);

    	/* old version
    	for (int rad = 1; rad <= 3; rad++)
    	{
    		for (int x = -rad; x <= rad; x++)
    		{
    			for (int z = -rad; z <= rad; z++)
    			{
    				TFFeature directlyAt = getFeatureDirectlyAt(x + chunkX, z + chunkZ, world);
    				if (directlyAt != TFFeature.nothing)
    				{
    					return directlyAt;
    				}
    			}
    		}
    	}
    	
    	return nothing;
    	*/
	}

	/**
	 * If we're near a hollow hill, this returns relative block coordinates indicating the center of that hill relative to the current chunk block coordinate system.
	 *
	 * @param cx
	 * @param cz
	 * @param seed
	 * @return
	 */
	public static int[] getNearestCenter(int cx, int cz, World world) {
		for (int rad = 1; rad <= 3; rad++) {
			for (int x = -rad; x <= rad; x++) {
				for (int z = -rad; z <= rad; z++) {
					if (getFeatureDirectlyAt(x + cx, z + cz, world).size == rad) {
						int[] center = {x * 16 + 8, z * 16 + 8};
						return center;
					}
				}
			}
		}
		int[] no = {0, 0};
		return no;
	}

	/**
	 * Given some coordinates, return the center of the nearest feature.
	 * <p>
	 * At the moment, with how features are distributed, just get the closest multiple of 256 and add +8 in both directions.
	 * <p>
	 * Maybe in the future we'll have to actually search for a feature chunk nearby, but for now this will work.
	 */
	public static BlockPos getNearestCenterXYZ(int cx, int cz, World world) {
		int chunkX = cx;
		int chunkZ = cz;

		// generate random number for the whole biome area
		int regionX = (chunkX + 8) >> 4;
		int regionZ = (chunkZ + 8) >> 4;

		long seed = (long) (regionX * 3129871) ^ (long) regionZ * 116129781L;
		seed = seed * seed * 42317861L + seed * 7L;

		int num0 = (int) (seed >> 12 & 3L);
		int num1 = (int) (seed >> 15 & 3L);
		int num2 = (int) (seed >> 18 & 3L);
		int num3 = (int) (seed >> 21 & 3L);

		// slightly randomize center of biome (+/- 3)
		int centerX = 8 + num0 - num1;
		int centerZ = 8 + num2 - num3;

		// centers are offset strangely depending on +/-
		int ccz;
		if (regionZ >= 0) {
			ccz = (regionZ * 16 + centerZ - 8) * 16 + 8;
		} else {
			ccz = (regionZ * 16 + (16 - centerZ) - 8) * 16 + 9;
		}

		int ccx;
		if (regionX >= 0) {
			ccx = (regionX * 16 + centerX - 8) * 16 + 8;
		} else {
			ccx = (regionX * 16 + (16 - centerX) - 8) * 16 + 9;
		}

		return new BlockPos(ccx, TFWorld.SEALEVEL, ccz);//  Math.abs(chunkX % 16) == centerX && Math.abs(chunkZ % 16) == centerZ;
	}

	/**
	 * Returns a list of hostile monsters.  Are we ever going to need passive or water creatures?
	 */
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType par1EnumCreatureType) {
		if (par1EnumCreatureType == EnumCreatureType.MONSTER) {
			return this.getSpawnableList(EnumCreatureType.MONSTER, 0);
		} else if (par1EnumCreatureType == EnumCreatureType.AMBIENT) {
			return this.ambientCreatureList;
		} else if (par1EnumCreatureType == EnumCreatureType.WATER_CREATURE) {
			return this.waterCreatureList;
		} else {
			return ImmutableList.of();
		}
	}

	/**
	 * Returns a list of hostile monsters in the specified indexed category
	 */
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType par1EnumCreatureType, int index) {
		if (par1EnumCreatureType == EnumCreatureType.MONSTER) {
			if (index >= 0 && index < this.spawnableMonsterLists.size()) {
				return this.spawnableMonsterLists.get(index);
			} else {
				return ImmutableList.of();
			}
		} else {
			return getSpawnableList(par1EnumCreatureType);
		}
	}

	private TFFeature setRequiredAchievement(Achievement required) {
		this.requiredAchievement = required;

		return this;
	}

	public boolean doesPlayerHaveRequiredAchievement(EntityPlayer player) {
		if (this.requiredAchievement != null) {
			// can we get the player's stats here at all?
			if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).getStatFile() != null) {
				StatisticsManagerServer stats = ((EntityPlayerMP) player).getStatFile();

				return stats.hasAchievementUnlocked(this.requiredAchievement);
			} else {
				return false; // cannot get stats
			}
		} else {
			return true; // no required achievement
		}
	}


	/**
	 * Try to spawn a hint monster near the specified player
	 */
	public void trySpawnHintMonster(World world, EntityPlayer player) {
		this.trySpawnHintMonster(world, player, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
	}

	/**
	 * Try several times to spawn a hint monster
	 */
	public void trySpawnHintMonster(World world, EntityPlayer player, int x, int y, int z) {
		// check if the timer is valid
		long currentTime = world.getTotalWorldTime();

		// if someone set the time backwards, fix the spawn timer
		if (currentTime < this.lastSpawnedHintMonsterTime) {
			this.lastSpawnedHintMonsterTime = 0;
		}

		if (currentTime - this.lastSpawnedHintMonsterTime > 1200) {
			// okay, time is good, try several times to spawn one
			for (int i = 0; i < 20; i++) {
				if (didSpawnHintMonster(world, player, x, y, z)) {
					this.lastSpawnedHintMonsterTime = currentTime;
					break;
				}
			}
		} else {
			//System.out.println("Can't spawn hint monster because of timer");
		}

	}

	/**
	 * Try once to spawn a hint monster near the player.  Return true if we did.
	 * <p>
	 * We could change up the monster depending on what feature this is, but we currently are not doing that
	 */
	private boolean didSpawnHintMonster(World world, EntityPlayer player, int x, int y, int z) {
		// find a target point
		int dx = x + world.rand.nextInt(16) - world.rand.nextInt(16);
		int dy = y + world.rand.nextInt(4) - world.rand.nextInt(4);
		int dz = z + world.rand.nextInt(16) - world.rand.nextInt(16);

		// make our hint monster
		EntityTFKobold hinty = new EntityTFKobold(world);
		hinty.setPosition(dx, dy, dz);

		// check if the bounding box is clear
		if (hinty.isNotColliding() && hinty.getEntitySenses().canSee(player)) {

			// add items and hint book
			ItemStack book = this.createHintBook();

			hinty.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, book);
			hinty.setDropChance(EntityEquipmentSlot.MAINHAND, 1.0F);

			world.spawnEntity(hinty);
			return true;
		} else {
			//System.out.println("Spawn point no go");
			//System.out.println("Spawn point clear? " + isClearSpawn);
			//System.out.println("Spawn point can see player? " + skel.canEntityBeSeen(player));
			return false;
		}
	}


	/**
	 * Create a hint book for the specified feature.  Only features with block protection will need this.
	 */
	public ItemStack createHintBook() {
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);

		NBTTagList bookPages = new NBTTagList();

		if (this == TFFeature.lichTower) {

			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, gnawed on by monsters]]\u00A70\n\nI have begun examining the strange aura surrounding this tower. The bricks of the tower are protected by a curse, stronger than any I've seen before. The magic from the curse"));
			bookPages.appendTag(new NBTTagString("is boiling off into the surrounding area.\n\nIn my homeland I would have many options for dealing with this magic, but here my supplies are limited. I shall have to research..."));
			bookPages.appendTag(new NBTTagString("\u00A78[[Many entries later]]\u00A70\n\nA breakthrough!  In my journeys I sighted a huge snake-like monster in a decorated courtyard. Nearby, I picked up a worn down, discarded green scale.\n\nThe magic in the scale seems to have the"));
			bookPages.appendTag(new NBTTagString("curse-breaking properties I need, but the magic is too dim. I may need to acquire a fresher specimen, directly from the creature."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Pointy Tower"));
		} else if (this == TFFeature.labyrinth) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, written on waterproof paper]]\u00A70\n\nThe mosquitoes in this swamp are vexing, but strange. The vast majority of them seem to have no natural source, nor do they seem to have a role in the local ecology. I have begun to"));
			bookPages.appendTag(new NBTTagString("suspect that they are some kind of magical curse.\n\n\u00A78[[Next entry]]\u00A70\n\nNow that I have encountered a protection spell on the ruined labyrinth here, I consider my suspicions confirmed. Both the protection"));
			bookPages.appendTag(new NBTTagString("spell and the mosquitoes are a curse. This curse seems to have a different source from the others I have encountered. I will have to research further...\n\n\u00A78[[Next entry]]\u00A70\n\nThe curse seems to"));
			bookPages.appendTag(new NBTTagString("be of a type too powerful for one being alone to produce. Several wizards working in combination would be necessary.\n\nIf one of the wizards stopped contributing, the whole of the curse over the entire swamp would fall. Strangely, "));
			bookPages.appendTag(new NBTTagString("my divinations do not show signs of any nearby living wizards. I did see something interesting in one of the nearby pointy-roofed towers though..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Swampy Labyrinth"));

		} else if (this == TFFeature.hydraLair) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, written on fireproof paper]]\u00A70\n\nFire is a trivial obstacle for a master explorer such as myself. I have traversed seas of fire, and swam through oceans of lava. The burning air here is an interesting variation,"));
			bookPages.appendTag(new NBTTagString("but ultimately no hinderance.\n\nWhat does stop me though is that I have encountered another protection spell, this time surrounding a mighty creature that must be king of this fire swamp. This is not the first protection spell I have"));
			bookPages.appendTag(new NBTTagString("encountered, and I am beginning to unravel the mysteries of how they work.\n\nIf this spell is like the others, it will be sustained by a powerful creature nearby. Surrounding the fire swamp are several wet swamps, and under those"));
			bookPages.appendTag(new NBTTagString("swamps are labyrinths full of minotaurs. The logical choice to bind such a spell to would be some sort of powerful minotaur, different in some way from the others that surround it..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on the Fire Swamp"));

		} else if (this == TFFeature.tfStronghold) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, written on faintly glowing paper]]\u00A70\n\nThe tendrils of darkness surrounding this area are just a manifestation of a protective spell over the entire dark forest. The spell causes blindness, which is quite vexing. I have"));
			bookPages.appendTag(new NBTTagString("seen several interesting things in the area and would like to keep exploring.\n\n\u00A78[[Next entry]]\u00A70\n\nI have found ruins in the dark forest.  They belong to a stronghold, of a type usually inhabited by knights. Rather than"));
			bookPages.appendTag(new NBTTagString("knights though, this stronghold is full of goblins. They wear knightly armor, but their behavior is most un-knightly.\n\n\u00A78[[Next entry]]\u00A70\n\nDeep in the ruins, I have found a pedestal. The pedestal seems to be of a type that"));
			bookPages.appendTag(new NBTTagString("knights would place trophies on to prove their strength.\n\nKilling a powerful creature would seem to weaken the curse on the dark forest, and placing a trophy associated with the creature on the pedestal would likely grant access into the"));
			bookPages.appendTag(new NBTTagString("main part of the stronghold.\n\nThe only creature I have seen so far seen so far of sufficient power is the many-headed beast in the fire swamp. How vexing..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Stronghold"));

		} else if (this == TFFeature.darkTower) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook that seems to have survived an explosion]]\u00A70\n\nThis tower clearly has mechanisms that are not responding to me. Their magic almost yearns to acknowledge my touch, but it cannot. It is if the devices of the tower are being"));
			bookPages.appendTag(new NBTTagString("suppressed by a powerful group of beings nearby.\n\n\u00A78[[Next entry]]\u00A70\n\nThe magic seems to emanate from deep within the strongholds nearby. It can't come from the goblins, as their magic is charming, but unfocused. There"));
			bookPages.appendTag(new NBTTagString("must still be some force still active in the strongholds.\n\n\u00A78[[Next entry]]\u00A70\n\nMy analysis indicates that it comes from several sources, operating as a group. I will head back to the stronghold after I resupply..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Wooden Tower"));

		} else if (this == TFFeature.yetiCave) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, covered in frost]]\u00A70\n\nThe blizzard surrounding these snowy lands is unceasing. This is no ordinary snowfall--this is a magical phenomenon. I will have to conduct experiments to find"));
			bookPages.appendTag(new NBTTagString("what is capable of causing such an effect.\n\n\u00A78[[Next entry]]\u00A70\n\nAt the center of the dark forest, where the leaves turn red and the grass dies, there is a wooden tower. The tops of the tower are affixed with"));
			bookPages.appendTag(new NBTTagString("structures acting as antennae. The antennae are not the source of the snowfall, but serve merely to boost the power of the curse causing it.\n\nA blizzard this intense must be caused by a powerful creature, most likely found"));
			bookPages.appendTag(new NBTTagString("near the top of the dark forest tower. Stop the creature, and the blizzard will fade."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on an Icy Cave"));

		} else if (this == TFFeature.iceTower) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, caked in ice]]\u00A70\n\nI overcame one blizzard, only to run into this terrible ice storm atop the glacier. My explorations have shown me the splendor of an ice palace, shining with the colors of the polar aurora. It"));
			bookPages.appendTag(new NBTTagString("all seems protected by some sort of curse.\n\n\u00A78[[Next entry]]\u00A70\n\nI am no novice.  This curse is fed by the power of a creature nearby.  The cause of the curse surrounding the fire swamp was built off the power of the leader of the "));
			bookPages.appendTag(new NBTTagString("minotaurs nearby.\n\nSurrounding this glacier, there are masses of yetis.  Perhaps the yetis have some sort of leader..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on an Auroral Fortification"));

		} else if (this == TFFeature.trollCave) {
			bookPages.appendTag(new NBTTagString("\u00A78[[An explorer's notebook, damaged by acid]]\u00A70\n\nThere seems to be no way to protect myself from the toxic rainstorm surrounding this area. In my brief excursions, I have also encountered another protection spell, similar to the"));
			bookPages.appendTag(new NBTTagString("others I have witnessed. The spell must be connected to the toxic storm in some way. Further research to follow...\n\n\u00A78[[Next entry]]\u00A70\n\nSuch supreme weather magic must be the result of an unequaled weather"));
			bookPages.appendTag(new NBTTagString("magician. Such a person would likely hide themselves in an extreme environment, far away.\n\nBased on my logic, I would expect to find such a person somewhere on the glacier, perhaps in some sort of fortress there..."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on an the Highlands"));

		} else {
			bookPages.appendTag(new NBTTagString("\u00A78[[This book shows signs of having been copied many times]]\u00A70\n\nI cannot explain the field surrounding this structure, but the magic is powerful.  If this curse is like the others, than the answer to unlocking it lies elsewhere.  Perhaps there is "));
			bookPages.appendTag(new NBTTagString("something I have left undone, or some monster I have yet to defeat. I will have to turn back. I will return to this place later, to see if anything has changed."));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on the Unexplained"));
		}


		return book;
	}

}
