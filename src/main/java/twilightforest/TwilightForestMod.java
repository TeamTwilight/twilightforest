package twilightforest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.client.LoadingScreenListener;
import twilightforest.client.RenderLayerRegistration;
import twilightforest.client.particle.TFParticleType;
import twilightforest.client.renderer.entity.LayerIce;
import twilightforest.client.renderer.entity.LayerShields;
import twilightforest.command.TFCommand;
import twilightforest.enchantment.TFEnchantments;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.*;
import twilightforest.item.recipe.UncraftingEnabledCondition;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFPotions;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.TFDimensions;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.TFGenCaveStalactite;

@Mod(TwilightForestMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TwilightForestMod {

	// TODO: might be a good idea to find proper spots for all of these? also remove redundants
	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/model/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR = ID + ":textures/armor/";

	public static final GameRules.RuleKey<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRules.func_234903_a_("tfEnforcedProgression", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true)); //Putting it in UPDATES since other world stuff is here

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = Rarity.create("TWILIGHT", TextFormatting.DARK_GREEN);

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
		TFTileEntities.TILE_ENTITIES.register(modbus);
		TFParticleType.PARTICLE_TYPES.register(modbus);
		TFBiomeFeatures.FEATURES.register(modbus);
		TFContainers.CONTAINERS.register(modbus);
		TFEnchantments.ENCHANTMENTS.register(modbus);
//		TFDimensions.BIOME_PROVIDER_TYPES.register(modbus);
//		TFDimensions.CHUNK_GENERATOR_TYPES.register(modbus);
//		TFDimensions.MOD_DIMENSIONS.register(modbus);

		if (ModList.get().isLoaded("sponge")) {
			LOGGER.info("It looks like you have Sponge installed! You may notice Hydras spawning incorrectly with floating heads.\n" +
					"If so, please update Sponge to resolve this issue. Have fun!"
			);
		}

		// TODO: move these to proper spots
		// WorldProviderTwilightForest.syncFromConfig();


		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				// TFCompat.preInitCompat(); TODO
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading preInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		} else {
			LOGGER.warn("Skipping compatibility!");
		}
	}

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent evt) {
		CapabilityList.registerCapabilities();
		TFPacketHandler.init();
		TFAdvancements.init();
		TFFeature.init();
		TFBiomes.addBiomeTypes();
		TFBiomes.addBiomeFeatures();
		TFDimensions.init();
		TFEntities.addEntityAttributes();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				// TFCompat.initCompat(); TODO
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				// TFCompat.postInitCompat(); TODO
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFConfig.build();
		TFGenCaveStalactite.loadStalactites();
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent evt) {
		TFItems.addItemModelProperties();

		ItemTFKnightlyArmor.initArmorModel();
		ItemTFPhantomArmor.initArmorModel();
		ItemTFYetiArmor.initArmorModel();
		ItemTFArcticArmor.initArmorModel();
		ItemTFFieryArmor.initArmorModel();
		MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());
		DistExecutor.runWhenOn(Dist.CLIENT, () -> RenderLayerRegistration::init);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> TFEntities::registerEntityRenderer);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> TFTileEntities::registerTileEntityRenders);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> TFContainers::renderScreens);
	}

	@SubscribeEvent
	public static void loadComplete(FMLLoadCompleteEvent evt) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			Minecraft.getInstance().getRenderManager().renderers.values().forEach(r -> {
				if (r instanceof LivingRenderer) {
					((LivingRenderer) r).addLayer(new LayerShields((LivingRenderer) r));
					((LivingRenderer) r).addLayer(new LayerIce((LivingRenderer) r));
				}
			});
		});
	}

	public void startServer(FMLServerAboutToStartEvent event) {
		TFCommand.register(event.getServer().getCommandManager().getDispatcher());
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

	public static Rarity getRarity() {
		return rarity != null ? rarity : Rarity.EPIC;
	}
}
