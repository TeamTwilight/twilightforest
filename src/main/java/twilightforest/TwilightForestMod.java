package twilightforest;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.entity.TFEntities;
import twilightforest.item.ItemTFArcticArmor;
import twilightforest.item.ItemTFFieryArmor;
import twilightforest.item.ItemTFKnightlyArmor;
import twilightforest.item.ItemTFPhantomArmor;
import twilightforest.item.ItemTFYetiArmor;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFPotions;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.TFGenCaveStalactite;
import twilightforest.world.surfacebuilders.TFSurfaceBuilders;

@Mod(TwilightForestMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TwilightForestMod {

	// TODO: might be a good idea to find proper spots for all of these? also remove redundants
	public static final String ID = "twilightforest";

	private static final String MODEL_DIR  = "textures/model/";
	private static final String GUI_DIR    = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR  = ID + ":textures/armor/";

	public static final String ENFORCED_PROGRESSION_RULE = "tfEnforcedProgression";

	public static final int GUI_ID_UNCRAFTING = 1;
	public static final int GUI_ID_FURNACE = 2;

	// public static DimensionType dimType; // TODO: move this

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = Rarity.create("TWILIGHT", TextFormatting.DARK_GREEN);

	private static boolean compat = true;

	// TODO: PROXIES ARE DEAD!
	// @SidedProxy(clientSide = "twilightforest.client.TFClientProxy", serverSide = "twilightforest.TFCommonProxy")
	// public static TFCommonProxy proxy;

	public TwilightForestMod() {
		{
			final Pair<TFConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Common::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
			TFConfig.COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TFConfig.Client::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
			TFConfig.CLIENT_CONFIG = specPair.getLeft();
		}

		MinecraftForge.EVENT_BUS.addListener(this::startServer);

		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
		TFBlocks.BLOCKS.register(modbus);
		TFItems.ITEMS.register(modbus);
		TFPotions.POTIONS.register(modbus);
		TFBiomes.BIOMES.register(modbus);
		TFEntities.ENTITIES.register(modbus);
		TFTileEntities.TILE_ENTITIES.register(modbus);
		TFParticleType.PARTICLE_TYPES.register(modbus);
		TFSurfaceBuilders.SURFACE_BUILDERS.register(modbus);
		TFBiomeFeatures.FEATURES.register(modbus);

		if (ModList.get().isLoaded("sponge")) {
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"If so, please update Sponge to resolve this issue. Have fun!"
			);
		}

		// TODO: move these to proper spots
		// dimType = DimensionType.register("twilight_forest", "_twilightforest", TFConfig.dimension.dimensionID, WorldProviderTwilightForest.class, false);
		// WorldProviderTwilightForest.syncFromConfig();

		// sounds on client, and whatever else needs to be registered pre-load

		CapabilityList.registerCapabilities();

		// just call this so that we register structure IDs correctly
		TFFeature.init(); // TODO: move?
		LOGGER.debug("There are {} entries in TFFeature enum. Maximum structure size is {}", TFFeature.getCount(), TFFeature.getMaxSize());

		// MapGenStructureIO.registerStructure(StructureStartNothing.class,                  				 "TFNothing"); // TODO: move, (also wtf is the giant whitespace)
		// TFHollowTreePieces.registerPieces(); TODO: structures are now a real registry

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

	@SubscribeEvent
	public void init(FMLCommonSetupEvent evt) {
		// NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy); // TODO: IGuiHandler is fugly and dead
		TFPacketHandler.init();
		TFAdvancements.init();
		TFTreasure.init();
		TFBiomes.addBiomeTypes();

		if (compat) {
			try {
				TFCompat.initCompat();
			} catch (Exception e) {
				compat = false;
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		// registerDimension(); // TODO: deferred registry
		// checkOriginDimension(); // TODO: move

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

	@SubscribeEvent
	public void clientSetup(FMLClientSetupEvent evt) {
		ItemTFKnightlyArmor.initArmorModel();
		ItemTFPhantomArmor.initArmorModel();
		ItemTFYetiArmor.initArmorModel();
		ItemTFArcticArmor.initArmorModel();
		ItemTFFieryArmor.initArmorModel();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> TFEntities::registerEntityRenderer);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> TFTileEntities::registerTileEntityRenders);
	}

	public void startServer(FMLServerStartingEvent event) {
		TFCommand.register(event.getCommandDispatcher());
	}

	/*private static void registerDimension() { TODO: move all this to a deferred registry
		if (DimensionManager.isDimensionRegistered(TFConfig.dimension.dimensionID)) {
			LOGGER.warn("Detected that the configured dimension ID '{}' is being used. Using backup ID ({}). It is recommended that you configure this mod to use a unique dimension ID.", TFConfig.dimension.dimensionID, backupDimensionID);
			TFConfig.dimension.dimensionID = backupDimensionID;
		}
		DimensionManager.registerDimension(TFConfig.dimension.dimensionID, dimType);
	}

	static void checkOriginDimension() {
		if (!DimensionManager.isDimensionRegistered(TFConfig.originDimension)) {
			LOGGER.warn("Detected that the configured origin dimension ID ({}) is not registered. Defaulting to the overworld.", TFConfig.originDimension);
			TFConfig.originDimension = 0;
		} else if (TFConfig.originDimension == TFConfig.dimension.dimensionID) {
			LOGGER.warn("Detected that the configured origin dimension ID ({}) is already used for the Twilight Forest. Defaulting to the overworld.", TFConfig.originDimension);
			TFConfig.originDimension = 0;
		}
	}*/

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

	public static Rarity getRarity() {
		return rarity != null ? rarity : Rarity.EPIC;
	}
}
