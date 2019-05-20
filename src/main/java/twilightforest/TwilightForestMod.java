package twilightforest;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.capabilities.CapabilityList;
import twilightforest.compat.TFCompat;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.structures.hollowtree.TFHollowTreePieces;
import twilightforest.structures.start.StructureStartNothing;
import twilightforest.tileentity.*;
import twilightforest.tileentity.spawner.*;
import twilightforest.world.WorldProviderTwilightForest;
import twilightforest.world.feature.TFGenCaveStalactite;

@Mod( modid = TwilightForestMod.ID,
		name = TwilightForestMod.NAME,
		version = TwilightForestMod.VERSION,
		acceptedMinecraftVersions = "[1.12.2]",
		dependencies = "after:ctm@[MC1.12.2-0.3.2.18,);before:immersiveengineering@[0.12-83,);before:tconstruct;required-after:forge@[14.23.5.2787,);after:thaumcraft@[6.1.BETA21,)",
		updateJSON = "https://raw.githubusercontent.com/TeamTwilight/twilightforest/1.12.x/update.json"
)
public class TwilightForestMod {

	public static final String ID = "twilightforest";
	public static final String NAME = "The Twilight Forest";
	public static final String VERSION = "@VERSION@";

	private static final String MODEL_DIR  = "textures/model/";
	private static final String GUI_DIR    = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR  = ID + ":textures/armor/";

	public static final String ENFORCED_PROGRESSION_RULE = "tfEnforcedProgression";

	public static final int GUI_ID_UNCRAFTING = 1;
	public static final int GUI_ID_FURNACE = 2;

	public static DimensionType dimType;
	public static int backupDimensionID = -777;

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final EnumRarity rarity = EnumHelper.addRarity("TWILIGHT", TextFormatting.DARK_GREEN, "Twilight");

	private static boolean compat = true;

	@Instance(ID)
	public static TwilightForestMod instance;

	@SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	public static TFCommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("sponge")) {
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"If so, please update Sponge to resolve this issue. Have fun!"
			);
		}

		registerTileEntities();
		dimType = DimensionType.register("twilight_forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);
		WorldProviderTwilightForest.syncFromConfig();

		// sounds on client, and whatever else needs to be registered pre-load
		proxy.preInit();

		CapabilityList.registerCapabilities();

		// just call this so that we register structure IDs correctly
		TFFeature.init();
		LOGGER.debug("There are {} entries in TFFeature enum. Maximum structure size is {}", TFFeature.getCount(), TFFeature.getMaxSize());

		MapGenStructureIO.registerStructure(StructureStartNothing.class,                  				 "TFNothing");
		TFHollowTreePieces.registerPieces();

		compat = TFConfig.doCompat;

		if (compat) {
			try {
				TFCompat.preInitCompat();
			} catch (Exception e) {
				compat = false;
				LOGGER.error("Had an error loading preInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			LOGGER.warn("Skipping compatibility!");
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		TFItems.initRepairMaterials();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		TFPacketHandler.init();
		proxy.init();
		TFAdvancements.init();
		TFTreasure.init();

		if (compat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				compat = false;
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFDataFixers.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		if (!DimensionManager.isDimensionRegistered(TFConfig.dimension.dimensionID)) {
			DimensionManager.registerDimension(TFConfig.dimension.dimensionID, dimType);
		} else {
			LOGGER.warn("Detected that the configured dimension ID '{}' is being used. Using backup ID. It is recommended that you configure this mod to use a unique dimension ID.", TFConfig.dimension.dimensionID);
			DimensionManager.registerDimension(backupDimensionID, dimType);
			TFConfig.dimension.dimensionID = backupDimensionID;
		}
		if (!DimensionManager.isDimensionRegistered(TFConfig.originDimension)) {
			LOGGER.warn("Detected that the configured origin dimension ID ({}) is not registered. Defaulting to the overworld.", TFConfig.originDimension);
			TFConfig.originDimension = 0;
		}

		if (compat) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				compat = false;
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFConfig.build();
		TFGenCaveStalactite.loadStalactites();
	}

	@EventHandler
	public void onIMC(FMLInterModComms.IMCEvent event) {
		IMCHandler.onIMC(event);
	}

	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTF());
	}

	private void registerTileEntities() {
		proxy.registerCritterTileEntities();

		GameRegistry.registerTileEntity(TileEntityTFNagaSpawner          .class, prefix("naga_spawner"            ));
		GameRegistry.registerTileEntity(TileEntityTFLichSpawner          .class, prefix("lich_spawner"            ));
		GameRegistry.registerTileEntity(TileEntityTFHydraSpawner         .class, prefix("hydra_spawner"           ));
		GameRegistry.registerTileEntity(TileEntityTFSmoker               .class, prefix("smoker"                  ));
		GameRegistry.registerTileEntity(TileEntityTFPoppingJet           .class, prefix("popping_jet"             ));
		GameRegistry.registerTileEntity(TileEntityTFFlameJet             .class, prefix("flame_jet"               ));
		GameRegistry.registerTileEntity(TileEntityTFTowerBuilder         .class, prefix("tower_builder"           ));
		GameRegistry.registerTileEntity(TileEntityTFAntibuilder          .class, prefix("tower_reverter"          ));
		GameRegistry.registerTileEntity(TileEntityTFTrophy               .class, prefix("trophy"                  ));
		GameRegistry.registerTileEntity(TileEntityTFTowerBossSpawner     .class, prefix("tower_boss_spawner"      ));
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapInactive    .class, prefix("ghast_trap_inactive"     ));
		GameRegistry.registerTileEntity(TileEntityTFGhastTrapActive      .class, prefix("ghast_trap_active"       ));
		GameRegistry.registerTileEntity(TileEntityTFCReactorActive       .class, prefix("carminite_reactor_active"));
		GameRegistry.registerTileEntity(TileEntityTFKnightPhantomsSpawner.class, prefix("knight_phantom_spawner"  ));
		GameRegistry.registerTileEntity(TileEntityTFSnowQueenSpawner     .class, prefix("snow_queen_spawner"      ));
		GameRegistry.registerTileEntity(TileEntityTFCinderFurnace        .class, prefix("cinder_furnace"          ));
		GameRegistry.registerTileEntity(TileEntityTFMinoshroomSpawner    .class, prefix("minoshroom_spawner"      ));
		GameRegistry.registerTileEntity(TileEntityTFAlphaYetiSpawner     .class, prefix("alpha_yeti_spawner"      ));
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(ID, name);
	}

	public static ResourceLocation getModelTexture(String name) {
		return new ResourceLocation(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return new ResourceLocation(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return new ResourceLocation(ID, ENVIRO_DIR + name);
	}

	public static EnumRarity getRarity() {
		return rarity != null ? rarity : EnumRarity.EPIC;
	}
}
