package twilightforest;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;
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
import twilightforest.compat.TFCompat;
import twilightforest.entity.*;
import twilightforest.entity.boss.*;
import twilightforest.entity.finalcastle.EntityTFCastleGuardian;
import twilightforest.entity.passive.*;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.structures.StructureTFMajorFeatureStart;
import twilightforest.tileentity.*;
import twilightforest.util.TFEntityNames;
import twilightforest.world.WorldProviderTwilightForest;

@Mod( modid = TwilightForestMod.ID,
		name = "The Twilight Forest",
		version = TwilightForestMod.VERSION,
		acceptedMinecraftVersions = "[1.12.2]",
		dependencies = "after:ctm@[MC1.12-0.3.0.15,);before:immersiveengineering;before:tconstruct;required-after:forge@[14.23.3.2655,)",
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
	public static final int DATA_FIXER_VERSION = 1;

	public static final int GUI_ID_UNCRAFTING = 1;
	public static final int GUI_ID_FURNACE = 2;

	public static DimensionType dimType;
	public static int backupdimensionID = -777;

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final EnumRarity rarity = EnumHelper.addRarity("TWILIGHT", TextFormatting.DARK_GREEN, "Twilight");

	private static boolean compat = true;

	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

	@SuppressWarnings("unused")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("sponge")) {
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"If so, please update Sponge to resolve this issue. Have fun!");
		}

		registerCreatures();
		registerTileEntities();
		dimType = DimensionType.register("twilight_forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);
		WorldProviderTwilightForest.syncFromConfig();

		// sounds on client, and whatever else needs to be registered pre-load
		proxy.preInit();

		TFTreasure.init();

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
	public void init(FMLInitializationEvent evt) {
		TFItems.initRepairMaterials();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		TFPacketHandler.init();
		proxy.init();
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

		TFDataFixers.init();
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
		TFEntities.registerEntity(TFEntityNames.WILD_BOAR, EntityTFBoar.class, id++, 0x83653b, 0xffefca);
		TFEntities.registerEntity(TFEntityNames.BIGHORN_SHEEP, EntityTFBighorn.class, id++, 0xdbceaf, 0xd7c771);
		TFEntities.registerEntity(TFEntityNames.DEER, EntityTFDeer.class, id++, 0x7b4d2e, 0x4b241d);

		TFEntities.registerEntity(TFEntityNames.REDCAP, EntityTFRedcap.class, id++, 0x3b3a6c, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.SWARM_SPIDER, EntityTFSwarmSpider.class, id++, 0x32022e, 0x17251e);
		TFEntities.registerEntity(TFEntityNames.NAGA, EntityTFNaga.class, id++, 0xa4d316, 0x1b380b, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.SKELETON_DRUID, EntityTFSkeletonDruid.class, id++, 0xa3a3a3, 0x2a3b17);
		TFEntities.registerEntity(TFEntityNames.HOSTILE_WOLF, EntityTFHostileWolf.class, id++, 0xd7d3d3, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.WRAITH, EntityTFWraith.class, id++, 0x505050, 0x838383);
		TFEntities.registerEntity(TFEntityNames.HEDGE_SPIDER, EntityTFHedgeSpider.class, id++, 0x235f13, 0x562653);
		TFEntities.registerEntity(TFEntityNames.HYDRA, EntityTFHydra.class, id++, 0x142940, 0x29806b);
		TFEntities.registerEntity(TFEntityNames.LICH, EntityTFLich.class, id++, 0xaca489, 0x360472);
		TFEntities.registerEntity(TFEntityNames.PENGUIN, EntityTFPenguin.class, id++, 0x12151b, 0xf9edd2);
		TFEntities.registerEntity(TFEntityNames.LICH_MINION, EntityTFLichMinion.class, id++);
		TFEntities.registerEntity(TFEntityNames.LOYAL_ZOMBIE, EntityTFLoyalZombie.class, id++);
		TFEntities.registerEntity(TFEntityNames.TINY_BIRD, EntityTFTinyBird.class, id++, 0x33aadd, 0x1188ee);
		TFEntities.registerEntity(TFEntityNames.SQUIRREL, EntityTFSquirrel.class, id++, 0x904f12, 0xeeeeee);
		TFEntities.registerEntity(TFEntityNames.BUNNY, EntityTFBunny.class, id++, 0xfefeee, 0xccaa99);
		TFEntities.registerEntity(TFEntityNames.RAVEN, EntityTFRaven.class, id++, 0x000011, 0x222233);
		TFEntities.registerEntity(TFEntityNames.QUEST_RAM, EntityTFQuestRam.class, id++, 0xfefeee, 0x33aadd);
		TFEntities.registerEntity(TFEntityNames.KOBOLD, EntityTFKobold.class, id++, 0x372096, 0x895d1b);
		TFEntities.registerEntity(TFEntityNames.MOSQUITO_SWARM, EntityTFMosquitoSwarm.class, id++, 0x080904, 0x2d2f21);
		TFEntities.registerEntity(TFEntityNames.DEATH_TOME, EntityTFDeathTome.class, id++, 0x774e22, 0xdbcdbe);
		TFEntities.registerEntity(TFEntityNames.MINOTAUR, EntityTFMinotaur.class, id++, 0x3f3024, 0xaa7d66);
		TFEntities.registerEntity(TFEntityNames.MINOSHROOM, EntityTFMinoshroom.class, id++, 0xa81012, 0xaa7d66);
		TFEntities.registerEntity(TFEntityNames.FIRE_BEETLE, EntityTFFireBeetle.class, id++, 0x1d0b00, 0xcb6f25);
		TFEntities.registerEntity(TFEntityNames.SLIME_BEETLE, EntityTFSlimeBeetle.class, id++, 0x0c1606, 0x60a74c);
		TFEntities.registerEntity(TFEntityNames.PINCH_BEETLE, EntityTFPinchBeetle.class, id++, 0xbc9327, 0x241609);
		TFEntities.registerEntity(TFEntityNames.MAZE_SLIME, EntityTFMazeSlime.class, id++, 0xa3a3a3, 0x2a3b17);
		TFEntities.registerEntity(TFEntityNames.REDCAP_SAPPER, EntityTFRedcapSapper.class, id++, 0x575d21, 0xab1e14);
		TFEntities.registerEntity(TFEntityNames.MIST_WOLF, EntityTFMistWolf.class, id++, 0x3a1411, 0xe2c88a);
		TFEntities.registerEntity(TFEntityNames.KING_SPIDER, EntityTFKingSpider.class, id++, 0x2c1a0e, 0xffc017);
		TFEntities.registerEntity(TFEntityNames.FIREFLY, EntityTFMobileFirefly.class, id++, 0xa4d316, 0xbaee02);
		TFEntities.registerEntity(TFEntityNames.MINI_GHAST, EntityTFMiniGhast.class, id++, 0xbcbcbc, 0xa74343);
		TFEntities.registerEntity(TFEntityNames.TOWER_GHAST, EntityTFTowerGhast.class, id++, 0xbcbcbc, 0xb77878);
		TFEntities.registerEntity(TFEntityNames.TOWER_GOLEM, EntityTFTowerGolem.class, id++, 0x6b3d20, 0xe2ddda);
		TFEntities.registerEntity(TFEntityNames.TOWER_TERMITE, EntityTFTowerTermite.class, id++, 0x5d2b21, 0xaca03a);
		TFEntities.registerEntity(TFEntityNames.TOWER_BROODLING, EntityTFTowerBroodling.class, id++, 0x343c14, 0xbaee02);
		TFEntities.registerEntity(TFEntityNames.UR_GHAST, EntityTFUrGhast.class, id++, 0xbcbcbc, 0xb77878);
		TFEntities.registerEntity(TFEntityNames.BLOCKCHAIN_GOBLIN, EntityTFBlockGoblin.class, id++, 0xd3e7bc, 0x1f3fff);
		TFEntities.registerEntity(TFEntityNames.GOBLIN_KNIGHT_UPPER, EntityTFGoblinKnightUpper.class, id++);
		TFEntities.registerEntity(TFEntityNames.GOBLIN_KNIGHT_LOWER, EntityTFGoblinKnightLower.class, id++, 0x566055, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.HELMET_CRAB, EntityTFHelmetCrab.class, id++, 0xfb904b, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.KNIGHT_PHANTOM, EntityTFKnightPhantom.class, id++, 0xa6673b, 0xd3e7bc);
		TFEntities.registerEntity(TFEntityNames.YETI, EntityTFYeti.class, id++, 0xdedede, 0x4675bb);
		TFEntities.registerEntity(TFEntityNames.YETI_ALPHA, EntityTFYetiAlpha.class, id++, 0xcdcdcd, 0x29486e);
		TFEntities.registerEntity(TFEntityNames.WINTER_WOLF, EntityTFWinterWolf.class, id++, 0xdfe3e5, 0xb2bcca);
		TFEntities.registerEntity(TFEntityNames.SNOW_GUARDIAN, EntityTFSnowGuardian.class, id++, 0xd3e7bc, 0xfefefe);
		TFEntities.registerEntity(TFEntityNames.STABLE_ICE_CORE, EntityTFIceShooter.class, id++, 0xa1bff3, 0x7000f8);
		TFEntities.registerEntity(TFEntityNames.UNSTABLE_ICE_CORE, EntityTFIceExploder.class, id++, 0x9aacf5, 0x9b0fa5);
		TFEntities.registerEntity(TFEntityNames.SNOW_QUEEN, EntityTFSnowQueen.class, id++, 0xb1b2d4, 0x87006e);
		TFEntities.registerEntity(TFEntityNames.TROLL, EntityTFTroll.class, id++, 0x9ea98f, 0xb0948e);
		TFEntities.registerEntity(TFEntityNames.GIANT_MINER, EntityTFGiantMiner.class, id++, 0x211b52, 0x9a9a9a);
		TFEntities.registerEntity(TFEntityNames.ARMORED_GIANT, EntityTFArmoredGiant.class, id++, 0x239391, 0x9a9a9a);
		TFEntities.registerEntity(TFEntityNames.ICE_CRYSTAL, EntityTFIceCrystal.class, id++, 0xdce9fe, 0xadcafb);
		TFEntities.registerEntity(TFEntityNames.HARBINGER_CUBE, EntityTFHarbingerCube.class, id++, 0x00000a, 0x8b0000);
		TFEntities.registerEntity(TFEntityNames.ADHERENT, EntityTFAdherent.class, id++, 0x0a0000, 0x00008b);
		TFEntities.registerEntity(TFEntityNames.ROVING_CUBE, EntityTFRovingCube.class, id++, 0x0a0000, 0x00009b);
		TFEntities.registerEntity(TFEntityNames.CASTLE_GUARDIAN, EntityTFCastleGuardian.class, id++, 80, 3, true);

		TFEntities.registerEntity(TFEntityNames.HYDRA_HEAD, EntityTFHydraHead.class, id++, 150, 3, false);

		TFEntities.registerEntity(TFEntityNames.NATURE_BOLT, EntityTFNatureBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.LICH_BOLT, EntityTFLichBolt.class, id++, 150, 2, true);
		TFEntities.registerEntity(TFEntityNames.WAND_BOLT, EntityTFTwilightWandBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.TOME_BOLT, EntityTFTomeBolt.class, id++, 150, 5, true);
		TFEntities.registerEntity(TFEntityNames.HYDRA_MORTAR, EntityTFHydraMortar.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.LICH_BOMB, EntityTFLichBomb.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.MOONWORM_SHOT, EntityTFMoonwormShot.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.SLIME_BLOB, EntityTFSlimeProjectile.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.CHARM_EFFECT, EntityTFCharmEffect.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.THROWN_WEP, EntityTFThrownWep.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.FALLING_ICE, EntityTFFallingIce.class, id++, 80, 3, true);
		TFEntities.registerEntity(TFEntityNames.THROWN_ICE, EntityTFIceBomb.class, id++, 80, 2, true);
		TFEntities.registerEntity(TFEntityNames.SEEKER_ARROW, EntitySeekerArrow.class, id++, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.ICE_ARROW, EntityIceArrow.class, id++, 150, 1, true);
		TFEntities.registerEntity(TFEntityNames.ICE_SNOWBALL, EntityTFIceSnowball.class, id++, 150, 3, true);
		TFEntities.registerEntity(TFEntityNames.CHAIN_BLOCK, EntityTFChainBlock.class, id++, 80, 1, true);
		TFEntities.registerEntity(TFEntityNames.CUBE_OF_ANNIHILATION, EntityTFCubeOfAnnihilation.class, id++, 80, 1, true);
		TFEntities.registerEntity(TFEntityNames.SLIDER, EntityTFSlideBlock.class, id++, 80, 1, true);
		TFEntities.registerEntity(TFEntityNames.BOGGARD, EntityTFBoggard.class, id++);
	}

	private void registerTileEntities() {
		proxy.registerCritterTileEntities();

		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner.class, "twilightforest:naga_spawner");
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner.class, "twilightforest:lich_spawner");
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner.class, "twilightforest:hydra_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSmoker.class, "twilightforest:smoker");
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet.class, "twilightforest:popping_jet");
		GameRegistry.registerTileEntity(TileEntityTFFlameJet.class, "twilightforest:flame_jet");
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder.class, "twilightforest:tower_builder");
		GameRegistry.registerTileEntity(TileEntityTFAntibuilder.class, "twilightforest:tower_reverter");
		GameRegistry.registerTileEntity(TileEntityTFTrophy.class, "twilightforest:trophy");
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner.class, "twilightforest:tower_boss_spawner");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive.class, "twilightforest:ghast_trap_inactive");
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive.class, "twilightforest:ghast_trap_active");
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive.class, "twilightforest:carminite_reactor_active");
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, "twilightforest:knight_phantom_spawner");
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner.class, "twilightforest:snow_queen_spawner");
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace.class, "twilightforest:cinder_furnace");
		GameRegistry.registerTileEntity(TileEntityTFMinoshroomSpawner.class, "twilightforest:minoshroom_spawner");
		GameRegistry.registerTileEntity(TileEntityTFAlphaYetiSpawner.class, "twilightforest:alpha_yeti_spawner"); //*/
	}

	public static EnumRarity getRarity() {
		return rarity != null ? rarity : EnumRarity.EPIC;
	}
}
