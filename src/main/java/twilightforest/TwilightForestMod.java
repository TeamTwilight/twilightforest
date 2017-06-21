package twilightforest;

import net.minecraft.world.DimensionType;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.item.TFRecipes;
import twilightforest.structures.StructureTFMajorFeatureStart;
import twilightforest.tileentity.*;
import twilightforest.util.TFEntityNames;
import twilightforest.world.WorldProviderTwilightForest;

@Mod(modid = TwilightForestMod.ID, name = "The Twilight Forest", version = TwilightForestMod.VERSION)
public class TwilightForestMod {

	public static final String ID = "twilightforest";
	public static final String VERSION = "2.3.8dev";

	public static final String MODEL_DIR = "twilightforest:textures/model/";
	public static final String GUI_DIR = "twilightforest:textures/gui/";
	public static final String ENVRIO_DIR = "twilightforest:textures/environment/";
	public static final String ARMOR_DIR = "twilightforest:textures/armor/";
	public static final String ENFORCED_PROGRESSION_RULE = "tfEnforcedProgression";

	public static final int GUI_ID_UNCRAFTING = 1;
	public static final int GUI_ID_FURNACE = 2;


	public static DimensionType dimType;
	public static int backupdimensionID = -777;

	public static final Logger LOGGER = LogManager.getLogger(ID);


	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// sounds on client, and whatever else needs to be registered pre-load
		proxy.doPreLoadRegistration();

		// cheevos!
		AchievementPage.registerAchievementPage(new TFAchievementPage());

		TFTreasure.init();
		LootFunctionManager.registerFunction(new LootFunctionEnchant.Serializer());
		LootConditionManager.registerCondition(new LootConditionIsMinion.Serializer());

		// just call this so that we register structure IDs correctly
//FIXME: AtomicBlom: Disabled for Structures

		new StructureTFMajorFeatureStart();

	}

	@EventHandler
	public void load(FMLInitializationEvent evt) {

		TFItems.initRepairMaterials();

		// creatures
		registerCreatures();

		// recipes
		TFRecipes.registerRecipes();

		// tile entities
		registerTileEntities();

		// GUI
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

		// packets
		TFPacketHandler.init();

		// render and other client stuff
		proxy.doOnLoadRegistration();

		// dimension provider
		dimType = DimensionType.register("Twilight Forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// register dimension with Forge
		if (!DimensionManager.isDimensionRegistered(TFConfig.dimension.dimensionID)) {
			DimensionManager.registerDimension(TFConfig.dimension.dimensionID, TwilightForestMod.dimType);
		} else {
			TwilightForestMod.LOGGER.warn("Detected that the configured dimension id '{}' is being used.  Using backup ID.  It is recommended that you configure this mod to use a unique dimension ID.", TFConfig.dimension.dimensionID);
			DimensionManager.registerDimension(TwilightForestMod.backupdimensionID, TwilightForestMod.dimType);
			TFConfig.dimension.dimensionID = TwilightForestMod.backupdimensionID;
		}

		// thaumcraft integration
		if (Loader.isModLoaded("Thaumcraft")) {
			//FIXME: Reenable this once Thaumcraft is available.
			//registerThaumcraftIntegration();
		} else {
			TwilightForestMod.LOGGER.info("Did not find Thaumcraft, did not load ThaumcraftApi integration.");
		}
	}

	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTFFeature());
		event.registerServerCommand(new CommandTFProgress());
	}

	private void registerCreatures() {
		int id = 0;
		TFEntities.registerEntity(TFEntityNames.WILD_BOAR, twilightforest.entity.passive.EntityTFBoar.class, id++, 0x83653b, 0xffefca);
		TFEntities.registerEntity(TFEntityNames.BIGHORN_SHEEP, twilightforest.entity.passive.EntityTFBighorn.class, id++, 0xdbceaf, 0xd7c771);
		TFEntities.registerEntity(TFEntityNames.DEER, twilightforest.entity.passive.EntityTFDeer.class, id++, 0x7b4d2e, 0x4b241d);

		TFEntities.registerEntity(TFEntityNames.REDCAP, twilightforest.entity.EntityTFRedcap.class, id++, 0x3b3a6c, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.SWARM_SPIDER, twilightforest.entity.EntityTFSwarmSpider.class, id++, 0x32022e, 0x17251e);
		TFEntities.registerEntity(TFEntityNames.NAGA, twilightforest.entity.boss.EntityTFNaga.class, id++, 0xa4d316, 0x1b380b, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.SKELETON_DRUID, twilightforest.entity.EntityTFSkeletonDruid.class, id++, 0xa3a3a3, 0x2a3b17);
		TFEntities.registerEntity(TFEntityNames.HOSTILE_WOLF, twilightforest.entity.EntityTFHostileWolf.class, id++, 0xd7d3d3, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.WRAITH, twilightforest.entity.EntityTFWraith.class, id++, 0x505050, 0x838383);
		TFEntities.registerEntity(TFEntityNames.HEDGE_SPIDER, twilightforest.entity.EntityTFHedgeSpider.class, id++, 0x235f13, 0x562653);
		TFEntities.registerEntity(TFEntityNames.HYDRA, twilightforest.entity.boss.EntityTFHydra.class, id++, 0x142940, 0x29806b);
		TFEntities.registerEntity(TFEntityNames.LICH, twilightforest.entity.boss.EntityTFLich.class, id++, 0xaca489, 0x360472);
		TFEntities.registerEntity(TFEntityNames.PENGUIN, twilightforest.entity.passive.EntityTFPenguin.class, id++, 0x12151b, 0xf9edd2);
		TFEntities.registerEntity(TFEntityNames.LICH_MINION, twilightforest.entity.boss.EntityTFLichMinion.class, id++);
		TFEntities.registerEntity(TFEntityNames.LOYAL_ZOMBIE, twilightforest.entity.EntityTFLoyalZombie.class, id++);
		TFEntities.registerEntity(TFEntityNames.TINY_BIRD, twilightforest.entity.passive.EntityTFTinyBird.class, id++, 0x33aadd, 0x1188ee);
		TFEntities.registerEntity(TFEntityNames.SQUIRREL, twilightforest.entity.passive.EntityTFSquirrel.class, id++, 0x904f12, 0xeeeeee);
		TFEntities.registerEntity(TFEntityNames.BUNNY, twilightforest.entity.passive.EntityTFBunny.class, id++, 0xfefeee, 0xccaa99);
		TFEntities.registerEntity(TFEntityNames.RAVEN, twilightforest.entity.passive.EntityTFRaven.class, id++, 0x000011, 0x222233);
		TFEntities.registerEntity(TFEntityNames.QUEST_RAM, twilightforest.entity.passive.EntityTFQuestRam.class, id++);
		TFEntities.registerEntity(TFEntityNames.KOBOLD, twilightforest.entity.EntityTFKobold.class, id++, 0x372096, 0x895d1b);
		TFEntities.registerEntity(TFEntityNames.MOSQUITO_SWARM, twilightforest.entity.EntityTFMosquitoSwarm.class, id++, 0x080904, 0x2d2f21);
		TFEntities.registerEntity(TFEntityNames.DEATH_TOME, twilightforest.entity.EntityTFDeathTome.class, id++, 0x774e22, 0xdbcdbe);
		TFEntities.registerEntity(TFEntityNames.MINOTAUR, twilightforest.entity.EntityTFMinotaur.class, id++, 0x3f3024, 0xaa7d66);
		TFEntities.registerEntity(TFEntityNames.MINOSHROOM, twilightforest.entity.boss.EntityTFMinoshroom.class, id++, 0xa81012, 0xaa7d66);
		TFEntities.registerEntity(TFEntityNames.FIRE_BEETLE, twilightforest.entity.EntityTFFireBeetle.class, id++, 0x1d0b00, 0xcb6f25);
		TFEntities.registerEntity(TFEntityNames.SLIME_BEETLE, twilightforest.entity.EntityTFSlimeBeetle.class, id++, 0x0c1606, 0x60a74c);
		TFEntities.registerEntity(TFEntityNames.PINCH_BEETLE, twilightforest.entity.EntityTFPinchBeetle.class, id++, 0xbc9327, 0x241609);
		TFEntities.registerEntity(TFEntityNames.MAZE_SLIME, twilightforest.entity.EntityTFMazeSlime.class, id++, 0xa3a3a3, 0x2a3b17);
		TFEntities.registerEntity(TFEntityNames.REDCAP_SAPPER, twilightforest.entity.EntityTFRedcapSapper.class, id++, 0x575d21, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.MIST_WOLF, twilightforest.entity.EntityTFMistWolf.class, id++, 0x3a1411, 0xe2c88a);
		TFEntities.registerEntity(TFEntityNames.KING_SPIDER, twilightforest.entity.EntityTFKingSpider.class, id++, 0x2c1a0e, 0xffc017);
		TFEntities.registerEntity(TFEntityNames.FIREFLY, twilightforest.entity.passive.EntityTFMobileFirefly.class, id++, 0xa4d316, 0xbaee02);
		TFEntities.registerEntity(TFEntityNames.MINI_GHAST, twilightforest.entity.EntityTFMiniGhast.class, id++, 0xbcbcbc, 0xa74343);
		TFEntities.registerEntity(TFEntityNames.TOWER_GHAST, twilightforest.entity.EntityTFTowerGhast.class, id++, 0xbcbcbc, 0xb77878);
		TFEntities.registerEntity(TFEntityNames.TOWER_GOLEM, twilightforest.entity.EntityTFTowerGolem.class, id++, 0x6b3d20, 0xe2ddda);
		TFEntities.registerEntity(TFEntityNames.TOWER_TERMITE, twilightforest.entity.EntityTFTowerTermite.class, id++, 0x5d2b21, 0xaca03a);
		TFEntities.registerEntity(TFEntityNames.TOWER_BROODLING, twilightforest.entity.EntityTFTowerBroodling.class, id++, 0x343c14, 0xbaee02);
		TFEntities.registerEntity(TFEntityNames.UR_GHAST, twilightforest.entity.boss.EntityTFUrGhast.class, id++, 0xbcbcbc, 0xb77878);
		TFEntities.registerEntity(TFEntityNames.BLOCKCHAIN_GOBLIN, twilightforest.entity.EntityTFBlockGoblin.class, id++, 0xd3e7bc, 0x1f3fff);
		TFEntities.registerEntity(TFEntityNames.GOBLIN_KNIGHT_UPPER, twilightforest.entity.EntityTFGoblinKnightUpper.class, id++);
		TFEntities.registerEntity(TFEntityNames.GOBLIN_KNIGHT_LOWER, twilightforest.entity.EntityTFGoblinKnightLower.class, id++, 0x566055, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.HELMET_CRAB, twilightforest.entity.EntityTFHelmetCrab.class, id++, 0xfb904b, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.KNIGHT_PHANTOM, twilightforest.entity.boss.EntityTFKnightPhantom.class, id++, 0xa6673b, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.YETI, twilightforest.entity.EntityTFYeti.class, id++, 0xdedede, 0x4675bb);
		TFEntities.registerEntity(TFEntityNames.YETI_ALPHA, twilightforest.entity.boss.EntityTFYetiAlpha.class, id++, 0xcdcdcd, 0x29486e);
		TFEntities.registerEntity(TFEntityNames.WINTER_WOLF, twilightforest.entity.EntityTFWinterWolf.class, id++, 0xdfe3e5, 0xb2bcca);
		TFEntities.registerEntity(TFEntityNames.SNOW_GUARDIAN, twilightforest.entity.EntityTFSnowGuardian.class, id++, 0xd3e7bc, 0xfefefe);
		TFEntities.registerEntity(TFEntityNames.STABLE_ICE_CORE, twilightforest.entity.EntityTFIceShooter.class, id++, 0xa1bff3, 0x7000f8);
		TFEntities.registerEntity(TFEntityNames.UNSTABLE_ICE_CORE, twilightforest.entity.EntityTFIceExploder.class, id++, 0x9aacf5, 0x9b0fa5);
		TFEntities.registerEntity(TFEntityNames.SNOW_QUEEN, twilightforest.entity.boss.EntityTFSnowQueen.class, id++, 0xb1b2d4, 0x87006e);
		TFEntities.registerEntity(TFEntityNames.TROLL, twilightforest.entity.EntityTFTroll.class, id++, 0x9ea98f, 0xb0948e);
		TFEntities.registerEntity(TFEntityNames.GIANT_MINER, twilightforest.entity.EntityTFGiantMiner.class, id++, 0x211b52, 0x9a9a9a);
		TFEntities.registerEntity(TFEntityNames.ARMORED_GIANT, twilightforest.entity.EntityTFArmoredGiant.class, id++, 0x239391, 0x9a9a9a);
		TFEntities.registerEntity(TFEntityNames.ICE_CRYSTAL, twilightforest.entity.boss.EntityTFIceCrystal.class, id++, 0xdce9fe, 0xadcafb);
		TFEntities.registerEntity(TFEntityNames.HARBINGER_CUBE, twilightforest.entity.EntityTFHarbingerCube.class, id++, 0x00000a, 0x8b0000);
		TFEntities.registerEntity(TFEntityNames.ADHERENT, twilightforest.entity.EntityTFAdherent.class, id++, 0x0a0000, 0x00008b);
		TFEntities.registerEntity(TFEntityNames.ROVING_CUBE, twilightforest.entity.EntityTFRovingCube.class, id++, 0x0a0000, 0x00009b);

		TFEntities.registerEntity(TFEntityNames.HYDRA_HEAD, twilightforest.entity.boss.EntityTFHydraHead.class, id++, 11, 3, false);

		TFEntities.registerEntity(TFEntityNames.NATURE_BOLT, twilightforest.entity.EntityTFNatureBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.LICH_BOLT, twilightforest.entity.boss.EntityTFLichBolt.class, id++, 150, 2, true);
		TFEntities.registerEntity(TFEntityNames.WAND_BOLT, twilightforest.entity.EntityTFTwilightWandBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.TOME_BOLT, twilightforest.entity.EntityTFTomeBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.HYDRA_MORTAR, twilightforest.entity.boss.EntityTFHydraMortar.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.LICH_BOMB, twilightforest.entity.boss.EntityTFLichBomb.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.MOONWORM_SHOT, twilightforest.entity.EntityTFMoonwormShot.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.SLIME_BLOB, twilightforest.entity.EntityTFSlimeProjectile.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.CHARM_EFFECT, twilightforest.entity.EntityTFCharmEffect.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.THROWN_AXE, twilightforest.entity.boss.EntityTFThrownAxe.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.THROWN_PICK, twilightforest.entity.boss.EntityTFThrownPick.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.FALLING_ICE, twilightforest.entity.boss.EntityTFFallingIce.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.THROWN_ICE, twilightforest.entity.boss.EntityTFIceBomb.class, id++, 80, 2, true);
		TFEntities.registerEntity(TFEntityNames.SEEKER_ARROW, twilightforest.entity.EntitySeekerArrow.class, id++, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.ICE_ARROW, twilightforest.entity.EntityIceArrow.class, id++, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.ICE_SNOWBALL, twilightforest.entity.EntityTFIceSnowball.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.CHAIN_BLOCK, twilightforest.entity.EntityTFChainBlock.class, id++, 80, 1, true);
		TFEntities.registerEntity(TFEntityNames.CUBE_OF_ANNIHILATION, twilightforest.entity.EntityTFCubeOfAnnihilation.class, id++, 80, 1, true);
		TFEntities.registerEntity(TFEntityNames.SLIDER, twilightforest.entity.EntityTFSlideBlock.class, id++, 80, 1, true);
	}


	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTFFirefly.class, "firefly");
		GameRegistry.registerTileEntity(TileEntityTFCicada.class, "cicada");
		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner.class, "naga_spawner");
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner.class, "lich_spawner");
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner.class, "hydra_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSmoker.class, "smoker");
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet.class, "popping_jet");
		GameRegistry.registerTileEntity(TileEntityTFFlameJet.class, "flame_jet");
		GameRegistry.registerTileEntity(TileEntityTFMoonworm.class, "moonworm");
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder.class, "tower_builder");
		GameRegistry.registerTileEntity(TileEntityTFReverter.class, "tower_reverter");
		GameRegistry.registerTileEntity(TileEntityTFTrophy.class, "trophy");
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner.class, "tower_boss_spawner");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive.class, "ghast_trap_inactive");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive.class, "ghast_trap_active");
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive.class, "carminite_reactor_active");
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, "knight_phantom_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner.class, "snow_queen_spawner");
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace.class, "cinder_furnace");
	}

	/**
	 * Use the thaumcraft API to register our things with aspects and biomes with values
	 */
	//FIXME: Reenable once Thaumcraft API is available
	/*
	private void registerThaumcraftIntegration()
	{
		try {
	
			// items
			registerTCObjectTag(TFItems.nagaScale, -1, (new AspectList()).add(Aspect.MOTION, 2).add(Aspect.ARMOR, 3));
			registerTCObjectTag(TFItems.scepterTwilight, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 8).add(Aspect.WEAPON, 8));
			registerTCObjectTag(TFItems.scepterLifeDrain, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.LIFE, 8).add(Aspect.HUNGER, 8));
			registerTCObjectTag(TFItems.scepterZombie, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.UNDEAD, 8).add(Aspect.ENTROPY, 8));
			registerTCObjectTag(TFItems.magicMapFocus, -1, (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.SENSES, 8));
			registerTCObjectTag(TFItems.mazeMapFocus, -1, (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.SENSES, 8).add(Aspect.ORDER, 4));
			registerTCObjectTag(TFItems.feather, -1, (new AspectList()).add(Aspect.FLIGHT, 2).add(Aspect.AIR, 1).add(Aspect.DARKNESS, 1));
			registerTCObjectTag(TFItems.liveRoot, -1, (new AspectList()).add(Aspect.MAGIC, 1).add(Aspect.TREE, 2).add(Aspect.LIFE, 2));
			registerTCObjectTag(TFItems.ironwoodIngot, -1, (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.TREE, 1).add(Aspect.METAL, 4).add(Aspect.CRAFT, 2));
			registerTCObjectTag(TFItems.torchberries, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 2));
			registerTCObjectTag(TFItems.fieryBlood, -1, (new AspectList()).add(Aspect.FIRE, 8).add(Aspect.LIFE, 8).add(Aspect.MAGIC, 4));
			registerTCObjectTag(TFItems.trophy, -1, (new AspectList()).add(Aspect.LIFE, 6).add(Aspect.BEAST, 6).add(Aspect.SOUL, 6));
			registerTCObjectTag(TFItems.steeleafIngot, -1, (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.METAL, 2));
			registerTCObjectTag(TFItems.minotaurAxe, -1, (new AspectList()).add(Aspect.TOOL, 2).add(Aspect.WEAPON, 4).add(Aspect.CRYSTAL, 6).add(Aspect.GREED, 8));
			registerTCObjectTag(TFItems.mazebreakerPick, -1, (new AspectList()).add(Aspect.CRYSTAL, 6).add(Aspect.TOOL, 8).add(Aspect.MINE, 8));
			registerTCObjectTag(TFItems.oreMagnet, -1, (new AspectList()).add(Aspect.GREED, 10).add(Aspect.TOOL, 6).add(Aspect.METAL, 8).add(Aspect.MOTION, 6));
			registerTCObjectTag(TFItems.crumbleHorn, -1, (new AspectList()).add(Aspect.ENTROPY, 12).add(Aspect.BEAST, 2));
			registerTCObjectTag(TFItems.peacockFan, -1, (new AspectList()).add(Aspect.AIR, 8).add(Aspect.MOTION, 6).add(Aspect.FLIGHT, 10));
			registerTCObjectTag(TFItems.moonwormQueen, -1, (new AspectList()).add(Aspect.LIGHT, 12).add(Aspect.MAGIC, 1));
			registerTCObjectTag(TFItems.charmOfLife1, -1, (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.HEAL, 2).add(Aspect.GREED, 4));
			registerTCObjectTag(TFItems.charmOfKeeping1, -1, (new AspectList()).add(Aspect.DEATH, 1).add(Aspect.TRAVEL, 2).add(Aspect.GREED, 4));
			registerTCObjectTag(TFItems.towerKey, -1, (new AspectList()).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4));
			registerTCObjectTag(TFItems.transformPowder, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.EXCHANGE, 4));
			registerTCObjectTag(TFItems.borerEssence, -1, (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.TREE, 2).add(Aspect.SOUL, 4).add(Aspect.MAGIC, 2));
			registerTCObjectTag(TFItems.armorShard, -1, (new AspectList()).add(Aspect.METAL, 1));
			registerTCObjectTag(TFItems.knightMetal, -1, (new AspectList()).add(Aspect.METAL, 8).add(Aspect.ORDER, 1));
			registerTCObjectTag(TFItems.phantomHelm, -1, (new AspectList()).add(Aspect.METAL, 6).add(Aspect.ARMOR, 6).add(Aspect.UNDEAD, 6));
			registerTCObjectTag(TFItems.phantomPlate, -1, (new AspectList()).add(Aspect.METAL, 8).add(Aspect.ARMOR, 8).add(Aspect.UNDEAD, 8));
			registerTCObjectTag(TFItems.armorShard, -1, (new AspectList()).add(Aspect.METAL, 1));
			registerTCObjectTag(TFItems.lampOfCinders, -1, (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.MAGIC, 4).add(Aspect.TOOL, 4));
			registerTCObjectTag(TFItems.fieryTears, -1, (new AspectList()).add(Aspect.FIRE, 8).add(Aspect.LIFE, 8).add(Aspect.MAGIC, 4));
			registerTCObjectTag(TFItems.alphaFur, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.BEAST, 3).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 1));
			registerTCObjectTag(TFItems.iceBomb, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.AIR, 1));
			registerTCObjectTag(TFItems.arcticFur, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.BEAST, 3));
			registerTCObjectTag(TFItems.tripleBow, -1, (new AspectList()).add(Aspect.TREE, 6).add(Aspect.BEAST, 6).add(Aspect.CLOTH, 6).add(Aspect.WEAPON, 9).add(Aspect.AIR, 3));
			registerTCObjectTag(TFItems.seekerBow, -1, (new AspectList()).add(Aspect.MIND, 3).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
			registerTCObjectTag(TFItems.iceBow, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
			registerTCObjectTag(TFItems.enderBow, -1, (new AspectList()).add(Aspect.TRAVEL, 2).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
			registerTCObjectTag(TFItems.iceSword, -1, (new AspectList()).add(Aspect.WEAPON, 4).add(Aspect.CRYSTAL, 4).add(Aspect.COLD, 4));
			registerTCObjectTag(TFItems.glassSword, -1, (new AspectList()).add(Aspect.WEAPON, 5).add(Aspect.CRYSTAL, 4));
			registerTCObjectTag(TFItems.cubeTalisman, -1, (new AspectList()).add(Aspect.VOID, 4).add(Aspect.MAGIC, 4).add(Aspect.ENTROPY, 4));
			registerTCObjectTag(TFItems.cubeOfAnnihilation, -1, (new AspectList()).add(Aspect.VOID, 7).add(Aspect.MAGIC, 7).add(Aspect.ENTROPY, 7));
			
			// food
			registerTCObjectTag(TFItems.venisonRaw, -1, (new AspectList()).add(Aspect.HUNGER, 2).add(Aspect.FLESH, 4).add(Aspect.BEAST, 2));
			registerTCObjectTag(TFItems.venisonCooked, -1, (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.FLESH, 4).add(Aspect.CRAFT, 1));
			registerTCObjectTag(TFItems.hydraChop, -1, (new AspectList()).add(Aspect.HUNGER, 6).add(Aspect.FLESH, 6).add(Aspect.LIFE, 4));
			registerTCObjectTag(TFItems.meefRaw, -1, (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.FLESH, 4).add(Aspect.LIFE, 2));
			registerTCObjectTag(TFItems.meefSteak, -1, (new AspectList()).add(Aspect.FIRE, 1).add(Aspect.BEAST, 1).add(Aspect.FLESH, 4).add(Aspect.LIFE, 2));
			registerTCObjectTag(TFItems.meefStroganoff, -1, (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.BEAST, 2).add(Aspect.FLESH, 4));
			registerTCObjectTag(TFItems.mazeWafer, -1, (new AspectList()).add(Aspect.HUNGER, 2));
			registerTCObjectTag(TFItems.experiment115, -1, (new AspectList()).add(Aspect.HUNGER, 3).add(Aspect.MECHANISM, 1));

			// blocks
			registerTCObjectTag(TFBlocks.firefly, -1, (new AspectList()).add(Aspect.FLIGHT, 1).add(Aspect.LIGHT, 2));
			registerTCObjectTag(TFBlocks.leaves, -1, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.mazestone, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.TRAP, 1).add(Aspect.ARMOR, 1));
			registerTCObjectTag(TFBlocks.hedge, 0, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.WEAPON, 1));
			registerTCObjectTag(TFBlocks.hedge, 1, (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.DARKNESS, 1));
			registerTCObjectTag(TFBlocks.root, -1, (new AspectList()).add(Aspect.TREE, 2));
			registerTCObjectTag(TFBlocks.cicada, -1, (new AspectList()).add(Aspect.SENSES, 2));
			registerTCObjectTag(TFBlocks.uncraftingTable, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.ENTROPY, 8).add(Aspect.EXCHANGE, 12).add(Aspect.CRAFT, 16));
			registerTCObjectTag(TFBlocks.fireJet, -1, (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.AIR, 2).add(Aspect.MOTION, 2));
			registerTCObjectTag(TFBlocks.nagastone, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.MOTION, 2));
			registerTCObjectTag(TFBlocks.magicLeaves, -1, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.towerWood, -1, (new AspectList()).add(Aspect.TREE, 2).add(Aspect.MECHANISM, 2));
			registerTCObjectTag(TFBlocks.towerDevice, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4));
			registerTCObjectTag(TFBlocks.towerTranslucent, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4).add(Aspect.VOID, 2));
			registerTCObjectTag(TFBlocks.trophy, -1, (new AspectList()).add(Aspect.LIFE, 6).add(Aspect.BEAST, 6).add(Aspect.SOUL, 6));
			registerTCObjectTag(TFBlocks.plant, 3, (new AspectList()).add(Aspect.PLANT, 1));
			registerTCObjectTag(TFBlocks.plant, 4, (new AspectList()).add(Aspect.PLANT, 1));
			registerTCObjectTag(TFBlocks.plant, 5, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.plant, 8, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.MAGIC, 1));
			registerTCObjectTag(TFBlocks.plant, 9, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1).add(Aspect.LIGHT, 1));
			registerTCObjectTag(TFBlocks.plant, 10, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
			registerTCObjectTag(TFBlocks.plant, 11, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
			registerTCObjectTag(TFBlocks.plant, 13, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 2));
			registerTCObjectTag(TFBlocks.plant, 14, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.sapling, -1, (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.TREE, 2));
			registerTCObjectTag(TFBlocks.moonworm, -1, (new AspectList()).add(Aspect.DARKNESS, 2).add(Aspect.LIGHT, 2));
			registerTCObjectTag(TFBlocks.shield, -1, (new AspectList()).add(Aspect.TRAP, 1).add(Aspect.MAGIC, 1).add(Aspect.ARMOR, 1));
			registerTCObjectTag(TFBlocks.trophyPedestal, -1, (new AspectList()).add(Aspect.GREED, 6).add(Aspect.BEAST, 5));
			registerTCObjectTag(TFBlocks.auroraBlock, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.CRYSTAL, 2));
			registerTCObjectTag(TFBlocks.underBrick, -1, (new AspectList()).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 2));
			registerTCObjectTag(TFBlocks.portal, -1, (new AspectList()).add(Aspect.MAGIC, 1).add(Aspect.MOTION, 2));
			registerTCObjectTag(TFBlocks.trophy, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 10));
			registerTCObjectTag(TFBlocks.shield, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ORDER, 2).add(Aspect.ARMOR, 2));
			registerTCObjectTag(TFBlocks.thorns, -1, (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.ENTROPY, 2).add(Aspect.TRAP, 2));
			registerTCObjectTag(TFBlocks.thornRose, -1, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.TRAP, 1).add(Aspect.SENSES, 2));
			registerTCObjectTag(TFBlocks.burntThorns, -1, (new AspectList()).add(Aspect.ENTROPY, 2).add(Aspect.FIRE, 1));
			registerTCObjectTag(TFBlocks.leaves3, -1, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.deadrock, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1).add(Aspect.DEATH, 1));
			registerTCObjectTag(TFBlocks.darkleaves, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 2));
			registerTCObjectTag(TFBlocks.auroraPillar, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
			registerTCObjectTag(TFBlocks.auroraSlab, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
			registerTCObjectTag(TFBlocks.auroraDoubleSlab, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
			registerTCObjectTag(TFBlocks.trollSteinn, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.CRYSTAL, 1));
			registerTCObjectTag(TFBlocks.wispyCloud, -1, (new AspectList()).add(Aspect.WEATHER, 1).add(Aspect.AIR, 1).add(Aspect.FLIGHT, 1));
			registerTCObjectTag(TFBlocks.fluffyCloud, -1, (new AspectList()).add(Aspect.WEATHER, 2).add(Aspect.AIR, 2));
			registerTCObjectTag(TFBlocks.giantCobble, -1, (new AspectList()).add(Aspect.EARTH, 8).add(Aspect.ENTROPY, 8));
			registerTCObjectTag(TFBlocks.giantLog, -1, (new AspectList()).add(Aspect.TREE, 32));
			registerTCObjectTag(TFBlocks.giantLeaves, -1, (new AspectList()).add(Aspect.PLANT, 32));
			registerTCObjectTag(TFBlocks.giantObsidian, -1, (new AspectList()).add(Aspect.FIRE, 16).add(Aspect.DARKNESS, 8).add(Aspect.EARTH, 16));
			registerTCObjectTag(TFBlocks.uberousSoil, -1, (new AspectList()).add(Aspect.EARTH, 4).add(Aspect.SENSES, 2).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.hugeStalk, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.TREE, 2));
			registerTCObjectTag(TFBlocks.hugeGloomBlock, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1).add(Aspect.LIGHT, 1));
			registerTCObjectTag(TFBlocks.trollVidr, -1, (new AspectList()).add(Aspect.PLANT, 2));
			registerTCObjectTag(TFBlocks.unripeTrollBer, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
			registerTCObjectTag(TFBlocks.trollBer, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 4));
			registerTCObjectTag(TFBlocks.knightmetalStorage, -1, (new AspectList()).add(Aspect.METAL, 12).add(Aspect.ORDER, 12));
			registerTCObjectTag(TFBlocks.hugeLilyPad, -1, (new AspectList()).add(Aspect.WATER, 3).add(Aspect.PLANT, 6));
			registerTCObjectTag(TFBlocks.hugeWaterLily, -1, (new AspectList()).add(Aspect.WATER, 2).add(Aspect.PLANT, 2).add(Aspect.SENSES, 2));
			registerTCObjectTag(TFBlocks.slider, -1, (new AspectList()).add(Aspect.MOTION, 4).add(Aspect.TRAP, 6));
			registerTCObjectTag(TFBlocks.castleBlock, -1, (new AspectList()).add(Aspect.ORDER, 2));
			registerTCObjectTag(TFBlocks.castleMagic, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 3).add(Aspect.ENERGY, 2));
			registerTCObjectTag(TFBlocks.forceField, -1, (new AspectList()).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 4));
			
			FMLLog.info("[TwilightForest] Loaded ThaumcraftApi integration.");
			
			
		} 
		catch (Exception e) 
		{
			FMLLog.warning("[TwilightForest] Had an %s error while trying to register with ThaumcraftApi.", e.getLocalizedMessage());
			// whatever.
		}

	}
	*/

	/**
	 * Register a block with Thaumcraft aspects
	 */
	/*
	private void registerTCObjectTag(Block block, int meta, AspectList list) {
		if (meta == -1) {
			meta = OreDictionary.WILDCARD_VALUE;
		}		
		ThaumcraftApi.registerObjectTag(new ItemStack(block, 1, meta), list);
	}
	*/

	/**
	 * Register an item with Thaumcraft aspects
	 */
	/*
	private void registerTCObjectTag(Item item, int meta, AspectList list) {
		if (meta == -1) {
			meta = OreDictionary.WILDCARD_VALUE;
		}	
		ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), list);
	}
	*/
}
