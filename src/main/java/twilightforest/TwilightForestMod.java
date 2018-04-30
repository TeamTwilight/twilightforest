package twilightforest;

import net.minecraft.world.DimensionType;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
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
import twilightforest.advancements.TFAdvancements;
import twilightforest.client.TFClientProxy;
import twilightforest.compat.TFCompat;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.structures.StructureTFMajorFeatureStart;
import twilightforest.tileentity.*;
import twilightforest.tileentity.critters.TileEntityTFCicadaTicking;
import twilightforest.tileentity.critters.TileEntityTFFireflyTicking;
import twilightforest.tileentity.critters.TileEntityTFMoonwormTicking;
import twilightforest.util.TFEntityNames;
import twilightforest.world.WorldProviderTwilightForest;

@Mod( modid = TwilightForestMod.ID,
		name = "The Twilight Forest",
		version = TwilightForestMod.VERSION,
		acceptedMinecraftVersions = "[1.12.2]",
		dependencies = "after:ctm@[MC1.12-0.2.3.12,);required-after:forge@[14.23.3.2675,)",
		updateJSON = "https://raw.githubusercontent.com/TeamTwilight/twilightforest/1.12.x/update.json"
)
public class TwilightForestMod {
	public static final String ID = "twilightforest";
	public static final String VERSION = "@VERSION@";

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

	private static boolean compat = true;

	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

	@SuppressWarnings("unused")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("sponge"))
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"Open the `config/sponge/global.conf` file, and set the `max-bounding-box-size` to `6000` or higher, or `0`.\n" +
					"Sponge's default maximum bounding box is too low for the Hydra. Have fun!");

		registerCreatures();
		registerTileEntities();
		dimType = DimensionType.register("twilight_forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);

		// sounds on client, and whatever else needs to be registered pre-load
		proxy.doPreLoadRegistration();

		TFTreasure.init();
		LootFunctionManager.registerFunction(new LootFunctionEnchant.Serializer());
		LootConditionManager.registerCondition(new LootConditionIsMinion.Serializer());

		// just call this so that we register structure IDs correctly
		new StructureTFMajorFeatureStart();

		compat = TFConfig.doCompat;

		if (compat) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				compat = false;
				TwilightForestMod.LOGGER.info(ID + " had an error loading preInit compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void load(FMLInitializationEvent evt) {
		TFItems.initRepairMaterials();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		TFPacketHandler.init();
		proxy.doOnLoadRegistration();
		TFAdvancements.init();

		if (compat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				compat = false;
				TwilightForestMod.LOGGER.info(ID + " had an error loading init compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		if (!DimensionManager.isDimensionRegistered(TFConfig.dimension.dimensionID)) {
			DimensionManager.registerDimension(TFConfig.dimension.dimensionID, TwilightForestMod.dimType);
		} else {
			TwilightForestMod.LOGGER.warn("Detected that the configured dimension id '{}' is being used.  Using backup ID.  It is recommended that you configure this mod to use a unique dimension ID.", TFConfig.dimension.dimensionID);
			DimensionManager.registerDimension(TwilightForestMod.backupdimensionID, TwilightForestMod.dimType);
			TFConfig.dimension.dimensionID = TwilightForestMod.backupdimensionID;
		}

		if (compat) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TwilightForestMod.LOGGER.info(ID + " had an error loading postInit compatibility!");
				TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
			}
		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTFFeature());
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
		TFEntities.registerEntity(TFEntityNames.QUEST_RAM, twilightforest.entity.passive.EntityTFQuestRam.class, id++, 0xfefeee, 0x33aadd);
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

		TFEntities.registerEntity(TFEntityNames.HYDRA_HEAD, twilightforest.entity.boss.EntityTFHydraHead.class, id++, 150, 3, false);

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
		TFEntities.registerEntity(TFEntityNames.BOGGARD, twilightforest.entity.EntityTFBoggard.class, id++);
	}


	private void registerTileEntities() {
		proxy.registerCritterTileEntities();

		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner.class, "naga_spawner");
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner.class, "lich_spawner");
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner.class, "hydra_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSmoker.class, "smoker");
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet.class, "popping_jet");
		GameRegistry.registerTileEntity(TileEntityTFFlameJet.class, "flame_jet");
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder.class, "tower_builder");
		GameRegistry.registerTileEntity(TileEntityTFAntibuilder.class, "tower_reverter");
		GameRegistry.registerTileEntity(TileEntityTFTrophy.class, "trophy");
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner.class, "tower_boss_spawner");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive.class, "ghast_trap_inactive");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive.class, "ghast_trap_active");
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive.class, "carminite_reactor_active");
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, "knight_phantom_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner.class, "snow_queen_spawner");
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace.class, "cinder_furnace");
		GameRegistry.registerTileEntity(TileEntityTFMinoshroomSpawner.class, "minoshroom_spawner");
		GameRegistry.registerTileEntity(TileEntityTFAlphaYetiSpawner.class, "alpha_yeti_spawner");
	}
}
