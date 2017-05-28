package twilightforest;

import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.entity.TFCreatures;
import twilightforest.item.TFItems;
import twilightforest.item.TFRecipes;
import twilightforest.structures.StructureTFMajorFeatureStart;
import twilightforest.tileentity.*;
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


	public static int dimensionID;
	public static DimensionType dimType;
	public static int backupdimensionID = -777;
    
	// misc options
    public static boolean silentCicadas;
    public static boolean allowPortalsInOtherDimensions;
    public static boolean adminOnlyPortals;
    public static String twilightForestSeed;
    public static boolean disablePortalCreation;
    public static boolean disableUncrafting;
    public static String portalCreationItemString;

    // performance
    public static float canopyCoverage;
    public static int twilightOakChance;
	
	public static final TFEventListener eventListener = new TFEventListener();
	public static final TFTickHandler tickHandler = new TFTickHandler();
	public static final Logger LOGGER = LogManager.getLogger(ID);


	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

    @EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// load config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		loadConfiguration(config);

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
		
		// event listener, for those events that seem worth listening to
		MinecraftForge.EVENT_BUS.register(eventListener);

		// tick listener
		MinecraftForge.EVENT_BUS.register(tickHandler);
		
		// set up portal item
		ResourceLocation loc = new ResourceLocation(portalCreationItemString);

		if (Item.REGISTRY.containsKey(loc)) {
			TwilightForestMod.LOGGER.info("Set Twilight Forest portal item to {}", loc);
		} else {
			TwilightForestMod.LOGGER.info("Config lists portal item as '{}'.  Not found, defaulting to diamond.", loc);
			loc = new ResourceLocation("minecraft", "diamond");
		}
		tickHandler.portalItem = loc;
		
		// packets
		TFPacketHandler.init();
		
		// render and other client stuff
		proxy.doOnLoadRegistration();
		
		// dimension provider
		dimType = DimensionType.register("Twilight Forest", "_twilightforest", dimensionID, WorldProviderTwilightForest.class, false);
	}
	
    @EventHandler
	public void postInit(FMLPostInitializationEvent evt) 
	{
		// register dimension with Forge
		if (!DimensionManager.isDimensionRegistered(TwilightForestMod.dimensionID))
		{
			DimensionManager.registerDimension(TwilightForestMod.dimensionID, TwilightForestMod.dimType);
		}
		else
		{
			TwilightForestMod.LOGGER.warn("Detected that the configured dimension id '{}' is being used.  Using backup ID.  It is recommended that you configure this mod to use a unique dimension ID.", dimensionID);
			DimensionManager.registerDimension(TwilightForestMod.backupdimensionID, TwilightForestMod.dimType);
			TwilightForestMod.dimensionID = TwilightForestMod.backupdimensionID;
		}
		
		// thaumcraft integration
		if (Loader.isModLoaded("Thaumcraft"))
		{
			//FIXME: Reenable this once Thaumcraft is available.
			//registerThaumcraftIntegration();
		}
		else
		{
			TwilightForestMod.LOGGER.info("Did not find Thaumcraft, did not load ThaumcraftApi integration.");
		}
	}
	
    @EventHandler
	public void startServer(FMLServerStartingEvent event)
	{
		// dispenser behaviors
		registerDispenseBehaviors(event.getServer());
		
		event.registerServerCommand(new CommandTFFeature());
		event.registerServerCommand(new CommandTFProgress());
	}

	private void registerCreatures() {
		int id = 0;
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFBoar.class, "Wild Boar", id++, 0x83653b, 0xffefca);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFBighorn.class, "Bighorn Sheep", id++, 0xdbceaf, 0xd7c771);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFDeer.class, "Wild Deer", id++, 0x7b4d2e, 0x4b241d);

		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFRedcap.class, "Redcap", id++, 0x3b3a6c, 0xab1e14);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFSwarmSpider.class, "Swarm Spider", id++, 0x32022e, 0x17251e);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFNaga.class, "Naga", id++, 0xa4d316, 0x1b380b);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFSkeletonDruid.class, "Skeleton Druid", id++, 0xa3a3a3, 0x2a3b17);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFHostileWolf.class, "Hostile Wolf", id++, 0xd7d3d3, 0xab1e14);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFWraith.class, "Twilight Wraith", id++, 0x505050, 0x838383);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFHedgeSpider.class, "Hedge Spider", id++, 0x235f13, 0x562653);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFHydra.class, "Hydra", id++, 0x142940, 0x29806b);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFLich.class, "Twilight Lich", id++, 0xaca489, 0x360472);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFPenguin.class, "Glacier Penguin", id++, 0x12151b, 0xf9edd2);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFLichMinion.class, "Lich Minion", id++);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFLoyalZombie.class, "Loyal Zombie", id++);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFTinyBird.class, "Tiny Bird", id++, 0x33aadd, 0x1188ee);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFSquirrel.class, "Forest Squirrel", id++, 0x904f12, 0xeeeeee);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFBunny.class, "Forest Bunny", id++, 0xfefeee, 0xccaa99);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFRaven.class, "Forest Raven", id++, 0x000011, 0x222233);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFQuestRam.class, "Questing Ram", id++);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFKobold.class, "Twilight Kobold", id++, 0x372096, 0x895d1b);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFMosquitoSwarm.class, "Mosquito Swarm", id++, 0x080904, 0x2d2f21);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFDeathTome.class, "Death Tome", id++, 0x774e22, 0xdbcdbe);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFMinotaur.class, "Minotaur", id++, 0x3f3024, 0xaa7d66);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFMinoshroom.class, "Minoshroom", id++, 0xa81012, 0xaa7d66);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFFireBeetle.class, "Fire Beetle", id++, 0x1d0b00, 0xcb6f25);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFSlimeBeetle.class, "Slime Beetle", id++, 0x0c1606, 0x60a74c);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFPinchBeetle.class, "Pinch Beetle", id++, 0xbc9327, 0x241609);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFMazeSlime.class, "Maze Slime", id++, 0xa3a3a3, 0x2a3b17);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFRedcapSapper.class, "Redcap Sapper", id++, 0x575d21, 0xab1e14);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFMistWolf.class, "Mist Wolf", id++, 0x3a1411, 0xe2c88a);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFKingSpider.class, "King Spider", id++, 0x2c1a0e, 0xffc017);
		TFCreatures.registerTFCreature(twilightforest.entity.passive.EntityTFMobileFirefly.class, "Firefly", id++, 0xa4d316, 0xbaee02);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFMiniGhast.class, "Mini Ghast", id++, 0xbcbcbc, 0xa74343);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFTowerGhast.class, "Tower Ghast", id++, 0xbcbcbc, 0xb77878);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFTowerGolem.class, "Tower Golem", id++, 0x6b3d20, 0xe2ddda);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFTowerTermite.class, "Tower Termite", id++, 0x5d2b21, 0xaca03a);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFTowerBroodling.class, "Redscale Broodling", id++, 0x343c14, 0xbaee02);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFUrGhast.class, "Tower Boss", id++, 0xbcbcbc, 0xb77878);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFBlockGoblin.class, "Block&Chain Goblin", id++, 0xd3e7bc, 0x1f3fff);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFGoblinKnightUpper.class, "Upper Goblin Knight", id++);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFGoblinKnightLower.class, "Lower Goblin Knight", id++, 0x566055, 0xd3e7bc);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFHelmetCrab.class, "Helmet Crab", id++, 0xfb904b, 0xd3e7bc);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFKnightPhantom.class, "Knight Phantom", id++, 0xa6673b, 0xd3e7bc);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFYeti.class, "Yeti", id++, 0xdedede, 0x4675bb);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFYetiAlpha.class, "Yeti Boss", id++, 0xcdcdcd, 0x29486e);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFWinterWolf.class, "WinterWolf", id++, 0xdfe3e5, 0xb2bcca);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFSnowGuardian.class, "SnowGuardian", id++, 0xd3e7bc, 0xfefefe);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFIceShooter.class, "Stable Ice Core", id++, 0xa1bff3, 0x7000f8);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFIceExploder.class, "Unstable Ice Core", id++, 0x9aacf5, 0x9b0fa5);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFSnowQueen.class, "Snow Queen", id++, 0xb1b2d4, 0x87006e);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFTroll.class, "Troll", id++, 0x9ea98f, 0xb0948e);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFGiantMiner.class, "Giant Miner", id++, 0x211b52, 0x9a9a9a);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFArmoredGiant.class, "Armored Giant", id++, 0x239391, 0x9a9a9a);
		TFCreatures.registerTFCreature(twilightforest.entity.boss.EntityTFIceCrystal.class, "Ice Crystal", id++, 0xdce9fe, 0xadcafb);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFHarbingerCube.class, "Harbinger Cube", id++, 0x00000a, 0x8b0000);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFAdherent.class, "Adherent", id++, 0x0a0000, 0x00008b);
		TFCreatures.registerTFCreature(twilightforest.entity.EntityTFRovingCube.class, "RovingCube", id++, 0x0a0000, 0x00009b);
		
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFHydraHead.class, "HydraHead", id++, this, 11, 3, false);
		
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFNatureBolt.class, "tfnaturebolt", id++, this, 150, 5, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFLichBolt.class, "tflichbolt",  id++, this, 150, 2, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFTwilightWandBolt.class, "tftwilightwandbolt", id++, this, 150, 5, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFTomeBolt.class, "tftomebolt", id++, this, 150, 5, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFHydraMortar.class, "tfhydramortar", id++, this, 150, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFLichBomb.class, "tflichbomb", id++, this, 150, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFMoonwormShot.class, "tfmoonwormshot", id++, this, 150, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFSlimeProjectile.class, "tfslimeblob", id++, this, 150, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFCharmEffect.class, "tfcharmeffect", id++, this, 80, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFThrownAxe.class, "tfthrownaxe", id++, this, 80, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFThrownPick.class, "tfthrownpick", id++, this, 80, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFFallingIce.class, "tffallingice", id++, this, 80, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.boss.EntityTFIceBomb.class, "tfthrownice", id++, this, 80, 2, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntitySeekerArrow.class, "tfSeekerArrow", id++, this, 150, 1, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityIceArrow.class, "tfIceArrow", id++, this, 150, 1, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFIceSnowball.class, "tficesnowball", id++, this, 150, 3, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFChainBlock.class, "tfchainBlock", id++, this, 80, 1, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFCubeOfAnnihilation.class, "tfcubeannihilation", id++, this, 80, 1, true);
		EntityRegistry.registerModEntity(twilightforest.entity.EntityTFSlideBlock.class, "tfslideblock", id++, this, 80, 1, true);

	}
	
	
	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTFFirefly.class, "Firefly");
		GameRegistry.registerTileEntity(TileEntityTFCicada.class, "Cicada");
		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner.class, "Naga Spawner");
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner.class, "Lich Spawner");
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner.class, "Hydra Spawner");
		GameRegistry.registerTileEntity(TileEntityTFSmoker.class, "Swamp Smoker");
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet.class, "Popping Flame Jet");
		GameRegistry.registerTileEntity(TileEntityTFFlameJet.class, "Lit Flame Jet");
		GameRegistry.registerTileEntity(TileEntityTFMoonworm.class, "Moonworm");
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder.class, "Tower Builder");
		GameRegistry.registerTileEntity(TileEntityTFReverter.class, "Tower Reverter");
		GameRegistry.registerTileEntity(TileEntityTFTrophy.class, "TF Trophy");
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner.class, "Tower Boss Spawner");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive.class, "Inactive Ghast Trap");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive.class, "Active Ghast Trap");
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive.class, "Active Carminite Reactor");
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, "Knight Phantom Spawner");
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner.class, "Snow Queen Spawner");
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace.class, "Cinder Furnace");
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


	/**
	 * Register all dispenser behaviors.
	 */
	private void registerDispenseBehaviors(MinecraftServer minecraftServer) {}
	
	/**
	 * Load our config file and set default values
	 */
	private void loadConfiguration(Configuration configFile) {
		configFile.load();
		
		dimensionID = configFile.get("dimension", "dimensionID", 7).getInt();
		configFile.get("dimension", "dimensionID", 7).setComment("What ID number to assign to the Twilight Forest dimension.  Change if you are having conflicts with another mod.");

	    // other misc otions
	    silentCicadas = configFile.get(Configuration.CATEGORY_GENERAL, "SilentCicadas", false).getBoolean(false);
		configFile.get(Configuration.CATEGORY_GENERAL, "SilentCicadas", false).setComment("Make cicadas silent  for those having sound library problems, or otherwise finding them annoying");
	    allowPortalsInOtherDimensions = configFile.get(Configuration.CATEGORY_GENERAL, "AllowPortalsInOtherDimensions", false).getBoolean(false);
	    configFile.get(Configuration.CATEGORY_GENERAL, "AllowPortalsInOtherDimensions", false).setComment("Allow portals to the Twilight Forest to be made outside of dimension 0.  May be considered an exploit.");
	    adminOnlyPortals = configFile.get(Configuration.CATEGORY_GENERAL, "AdminOnlyPortals", false).getBoolean(false);
	    configFile.get(Configuration.CATEGORY_GENERAL, "AdminOnlyPortals", false).setComment("Allow portals only for admins (ops).  This severly reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.");
	    twilightForestSeed = configFile.get(Configuration.CATEGORY_GENERAL, "TwilightForestSeed", "").getString();
	    configFile.get(Configuration.CATEGORY_GENERAL, "TwilightForestSeed", "").setComment("If set, this will override the normal world seed when generating parts of the Twilight Forest Dimension.");
	    disablePortalCreation = configFile.get(Configuration.CATEGORY_GENERAL, "DisablePortalCreation", false).getBoolean(false);
	    configFile.get(Configuration.CATEGORY_GENERAL, "DisablePortalCreation", false).setComment("Disable Twilight Forest portal creation entirely.  Provided for server operators looking to restrict action to the dimension.");
	    disableUncrafting = configFile.get(Configuration.CATEGORY_GENERAL, "DisableUncrafting", false).getBoolean(false);
	    configFile.get(Configuration.CATEGORY_GENERAL, "DisableUncrafting", false).setComment("Disable the uncrafting function of the uncrafting table.  Provided as an option when interaction with other mods produces exploitable recipes.");
	    portalCreationItemString = configFile.get(Configuration.CATEGORY_GENERAL, "PortalCreationItem", "diamond").getString();
	    configFile.get(Configuration.CATEGORY_GENERAL, "PortalCreationItem", "diamond").setComment("Item to create the Twilight Forest Portal.  Defaults to 'diamond'");

	    canopyCoverage = (float) (configFile.get("Performance", "CanopyCoverage", 1.7).getDouble(1.7));
		configFile.get("performance", "CanopyCoverage", 1.7).setComment("Amount of canopy coverage, from 0.0 on up.  Lower numbers improve chunk generation speed at the cost of a thinner forest.");
		twilightOakChance = configFile.get("Performance", "TwilightOakChance", 48).getInt(48);
		configFile.get("Performance", "TwilightOakChance", 48).setComment("Chance that a chunk in the Twilight Forest will contain a twilight oak tree.  Higher numbers reduce the number of trees, increasing performance.");

	    if (configFile.hasChanged()) {
	    	configFile.save();
	    }
	}

	/**
	 * Change what dimension ID the Twilight Forest is.  
	 * This is called when we connect to a server that has a different dimensionID set.
	 */
	public static void setDimensionID(int dim) 
	{
		if (TwilightForestMod.dimensionID != dim)
		{
			TwilightForestMod.LOGGER.info("Server has a different dimension ID {} for the Twilight Forest.  Changing this on the client.  This change will not be saved.", dim);

			DimensionManager.unregisterDimension(TwilightForestMod.dimensionID);
			TwilightForestMod.dimensionID = dim;
			DimensionManager.registerDimension(TwilightForestMod.dimensionID, TwilightForestMod.dimType);
		}
	}
}
