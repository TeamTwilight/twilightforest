package twilightforest;

import com.google.common.collect.Lists;
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
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.MapGenBase;
import twilightforest.biomes.TFBiomes;
import twilightforest.entity.*;
import twilightforest.world.TFBiomeProvider;
import twilightforest.world.TFWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TFFeature {

	public static final TFFeature[] featureList = new TFFeature[256];

	public static final TFFeature nothing       = new TFFeature( 0, 0, "no_feature"        ).enableDecorations().disableStructure();
	public static final TFFeature hill1         = new TFFeature( 1, 1, "small_hollow_hill" ).enableDecorations().enableTerrainAlterations();
	public static final TFFeature hill2         = new TFFeature( 2, 2, "medium_hollow_hill").enableDecorations().enableTerrainAlterations();
	public static final TFFeature hill3         = new TFFeature( 3, 3, "large_hollow_hill" ).enableDecorations().enableTerrainAlterations();
	public static final TFFeature hedgeMaze     = new TFFeature( 4, 2, "hedge_maze"        ).enableTerrainAlterations();
	public static final TFFeature nagaCourtyard = new TFFeature( 5, 3, "naga_courtyard"    ).enableTerrainAlterations();
	public static final TFFeature lichTower     = new TFFeature( 6, 1, "lich_tower"        , new ResourceLocation(TwilightForestMod.ID, "progress_naga"           ));
	public static final TFFeature iceTower      = new TFFeature( 7, 2, "ice_tower"         , new ResourceLocation(TwilightForestMod.ID, "progress_yeti"           ));
	public static final TFFeature questIsland   = new TFFeature( 8, 1, "quest_island"      ).disableStructure();
	public static final TFFeature questGrove    = new TFFeature( 9, 1, "quest_grove"       ).enableTerrainAlterations();
	public static final TFFeature druidGrove    = new TFFeature(10, 1, "druid_grove"       ).disableStructure();
	public static final TFFeature floatRuins    = new TFFeature(11, 3, "floating_ruins"    ).disableStructure();
	public static final TFFeature hydraLair     = new TFFeature(12, 2, "hydra_lair"        , new ResourceLocation(TwilightForestMod.ID, "progress_labyrinth"      )).enableTerrainAlterations();
	public static final TFFeature labyrinth     = new TFFeature(13, 3, "labyrinth"         , new ResourceLocation(TwilightForestMod.ID, "progress_lich"           )).enableDecorations();
	public static final TFFeature darkTower     = new TFFeature(14, 1, "dark_tower"        , new ResourceLocation(TwilightForestMod.ID, "progress_knights"        ));
	public static final TFFeature tfStronghold  = new TFFeature(15, 3, "knight_stronghold" , new ResourceLocation(TwilightForestMod.ID, "progress_trophy_pedestal")).enableDecorations().disableProtectionAura();
	public static final TFFeature worldTree     = new TFFeature(16, 3, "world_tree"        ).disableStructure();
	public static final TFFeature yetiCave      = new TFFeature(17, 2, "yeti_lairs"        , new ResourceLocation(TwilightForestMod.ID, "progress_lich"           )).enableDecorations().enableTerrainAlterations();
	public static final TFFeature trollCave     = new TFFeature(18, 3, "troll_lairs"       , new ResourceLocation(TwilightForestMod.ID, "progress_merge"          )).enableDecorations().enableTerrainAlterations().disableProtectionAura();
	public static final TFFeature finalCastle   = new TFFeature(19, 3, "final_castle"      );
	public static final TFFeature mushroomTower = new TFFeature(20, 2, "mushroom_tower"    );

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
	private final ResourceLocation[] requiredAdvancements;
	public boolean hasProtectionAura;

	private long lastSpawnedHintMonsterTime;

	private static int maxSize;

	public TFFeature(int parID, int parSize, String parName, ResourceLocation... requiredAdvancements) {
		this.featureID = parID;
		TFFeature.featureList[parID] = this;
		this.size = parSize;
		this.name = parName;
		this.areChunkDecorationsEnabled = false;
		this.isStructureEnabled = true;
		this.isTerrainAltered = false;
		this.spawnableMonsterLists = new ArrayList<>();
		this.ambientCreatureList = new ArrayList<>();
		this.waterCreatureList = new ArrayList<>();
		this.hasProtectionAura = true;

		ambientCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));

		maxSize = Math.max(maxSize, parSize);

		this.requiredAdvancements = requiredAdvancements;
	}

	public static int getMaxSize() {
		return maxSize;
	}

	/**
	 * doesn't require modid
	 */
	public static TFFeature getFeatureByName(String name) {
		for (TFFeature feature : featureList) {
			if (feature != null && feature.name.equalsIgnoreCase(name))
				return feature;
		}
		return nothing;
	}

	/**
	 * modid sensitive
	 */
	public static TFFeature getFeatureByName(ResourceLocation name) {
		if (name.getResourceDomain().equalsIgnoreCase(TwilightForestMod.ID))
			return getFeatureByName(name.getResourcePath());
		return nothing;
	}

	public static TFFeature getFeatureByID(int id){
		for (TFFeature feature : featureList) {
			if (feature != null && feature.featureID == id)
				return feature;
		}
		return nothing;
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

		/*
		// Remove above block comment start marker to enable debug
		// noinspection ConstantConditions
		if (true) {
			return nagaCourtyard;
		}//*/

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

		// get random value
		Random hillRNG = new Random(world.getSeed() + chunkX * 25117 + chunkZ * 151121);
		int randnum = hillRNG.nextInt(16);

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
		for (int rad = 1; rad <= 4; rad++) {
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

	// [Vanilla Copy] from MapGenStructure#findNearestStructurePosBySpacing; changed 2nd param to be TFFeature instead of MapGenStructure
	public static BlockPos findNearestFeaturePosBySpacing(World worldIn, TFFeature feature, BlockPos blockPos, int p_191069_3_, int p_191069_4_, int p_191069_5_, boolean p_191069_6_, int p_191069_7_, boolean findUnexplored) {
		int i = blockPos.getX() >> 4;
		int j = blockPos.getZ() >> 4;
		int k = 0;

		for (Random random = new Random(); k <= p_191069_7_; ++k) {
			for (int l = -k; l <= k; ++l) {
				boolean flag = l == -k || l == k;

				for (int i1 = -k; i1 <= k; ++i1) {
					boolean flag1 = i1 == -k || i1 == k;

					if (flag || flag1) {
						int j1 = i + p_191069_3_ * l;
						int k1 = j + p_191069_3_ * i1;

						if (j1 < 0) {
							j1 -= p_191069_3_ - 1;
						}

						if (k1 < 0) {
							k1 -= p_191069_3_ - 1;
						}

						int l1 = j1 / p_191069_3_;
						int i2 = k1 / p_191069_3_;
						Random random1 = worldIn.setRandomSeed(l1, i2, p_191069_5_);
						l1 = l1 * p_191069_3_;
						i2 = i2 * p_191069_3_;

						if (p_191069_6_) {
							l1 = l1 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
							i2 = i2 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
						} else {
							l1 = l1 + random1.nextInt(p_191069_3_ - p_191069_4_);
							i2 = i2 + random1.nextInt(p_191069_3_ - p_191069_4_);
						}

						MapGenBase.setupChunkSeed(worldIn.getSeed(), random, l1, i2);
						random.nextInt();

						// Check changed for TFFeature
						if (TFFeature.getFeatureAt(l1 << 4, i2 << 4, worldIn) == feature) {
							if (!findUnexplored || !worldIn.isChunkGeneratedAt(l1, i2)) {
								return new BlockPos((l1 << 4) + 8, 64, (i2 << 4) + 8);
							}
						} else if (k == 0) {
							break;
						}
					}
				}

				if (k == 0) {
					break;
				}
			}
		}

		return null;
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
	 */
	public static int[] getNearestCenter(int cx, int cz, World world) {
		for (int rad = 1; rad <= 4; rad++) {
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
			return Lists.newArrayList();
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
				return Lists.newArrayList();
			}
		} else {
			return getSpawnableList(par1EnumCreatureType);
		}
	}

	public boolean doesPlayerHaveRequiredAdvancements(EntityPlayer player) {
		if (this.requiredAdvancements.length > 0)
			for (ResourceLocation advancement : requiredAdvancements)
				if (!TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, advancement))
					return false;

		return true; // no required achievement
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
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.lichtower.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.lichtower.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.lichtower.3"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.lichtower.4"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Pointy Tower"));
		} else if (this == TFFeature.labyrinth) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.labyrinth.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.labyrinth.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.labyrinth.3"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.labyrinth.4"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.labyrinth.5"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Swampy Labyrinth"));

		} else if (this == TFFeature.hydraLair) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.hydralair.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.hydralair.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.hydralair.3"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.hydralair.4"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on the Fire Swamp"));

		} else if (this == TFFeature.tfStronghold) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.tfstronghold.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.tfstronghold.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.tfstronghold.3"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.tfstronghold.4"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.tfstronghold.5"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Stronghold"));

		} else if (this == TFFeature.darkTower) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.darktower.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.darktower.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.darktower.3"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on a Wooden Tower"));

		} else if (this == TFFeature.yetiCave) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.yeticave.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.yeticave.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.yeticave.3"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.yeticave.4"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on an Icy Cave"));

		} else if (this == TFFeature.iceTower) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.icetower.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.icetower.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.icetower.3"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on Auroral Fortification"));

		} else if (this == TFFeature.trollCave) {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.trollcave.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.trollcave.2"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.trollcave.3"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on an the Highlands"));

		} else {
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.unknown.1"))));
			bookPages.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(new TextComponentTranslation(TwilightForestMod.ID + ".book.unknown.2"))));

			book.setTagInfo("pages", bookPages);
			book.setTagInfo("author", new NBTTagString("A Forgotten Explorer"));
			book.setTagInfo("title", new NBTTagString("Notes on the Unexplained"));
		}


		return book;
	}

}
