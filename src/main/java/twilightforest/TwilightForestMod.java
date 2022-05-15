package twilightforest;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.Bindings;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathResourcePack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.capabilities.CapabilityList;
import twilightforest.client.ClientInitiator;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.CuriosCompat;
import twilightforest.compat.TConCompat;
import twilightforest.compat.TFCompat;
import twilightforest.compat.UndergardenCompat;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.enchantment.TFEnchantments;
import twilightforest.entity.TFEntities;
import twilightforest.inventory.TFContainers;
import twilightforest.item.FieryPickItem;
import twilightforest.item.TFItems;
import twilightforest.item.recipe.TFRecipes;
import twilightforest.item.recipe.UncraftingEnabledCondition;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFMobEffects;
import twilightforest.util.TFStats;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.biomesources.LandmarkBiomeSource;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.feature.BlockSpikeFeature;
import twilightforest.world.registration.*;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.io.IOException;
import java.util.Locale;

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

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRules.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true)); //Putting it in UPDATES since other world stuff is here

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = Rarity.create("TWILIGHT", ChatFormatting.DARK_GREEN);

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

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitiator::call);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityList::attachEntityCapability);

		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();

		BiomeKeys.BIOMES.register(modbus);
		TFBlockEntities.BLOCK_ENTITIES.register(modbus);
		TFBlocks.BLOCKS.register(modbus);
		TFContainers.CONTAINERS.register(modbus);
		TFEnchantments.ENCHANTMENTS.register(modbus);
		TFEntities.ENTITIES.register(modbus);
		TFBiomeFeatures.FEATURES.register(modbus);
		TwilightFeatures.FOLIAGE_PLACERS.register(modbus);
		TFItems.ITEMS.register(modbus);
		TFMobEffects.MOB_EFFECTS.register(modbus);
		TFNoiseGenerationSettings.NOISE_GENERATORS.register(modbus);
		TFParticleType.PARTICLE_TYPES.register(modbus);
		TwilightFeatures.PLACEMENT_MODIFIERS.register(modbus);
		TFRecipes.RECIPE_SERIALIZERS.register(modbus);
		TFRecipes.RECIPE_TYPES.register(modbus);
		//TFPotions.POTIONS.register(modbus);
		TFEntities.SPAWN_EGGS.register(modbus);
		TFStats.STATS.register(modbus);
		TFStructureProcessors.STRUCTURE_PROCESSORS.register(modbus);
		TwilightFeatures.TREE_DECORATORS.register(modbus);
		TwilightFeatures.TRUNK_PLACERS.register(modbus);

		if(ModList.get().isLoaded(TFCompat.UNDERGARDEN_ID)) {
			UndergardenCompat.ENTITIES.register(modbus);
		}

		if(ModList.get().isLoaded(TFCompat.TCON_ID)) {
			TConCompat.FLUIDS.register(modbus);
			TConCompat.MODIFIERS.register(modbus);
		}

		modbus.addListener(this::sendIMCs);
		modbus.addListener(CapabilityList::registerCapabilities);
		modbus.addGenericListener(SoundEvent.class, TFSounds::registerSounds);
		modbus.addGenericListener(StructureFeature.class, TFStructures::register);
		if(ModList.get().isLoaded(TFCompat.CURIOS_ID)) {
			Bindings.getForgeBus().get().addListener(CuriosCompat::keepCurios);
		}

		// Poke these so they exist when we need them FIXME this is probably terrible design
		new BiomeGrassColors();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.preInitCompat();
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
	public static void addClassicPack(AddPackFindersEvent event) {
		try {
			if (event.getPackType() == PackType.CLIENT_RESOURCES) {
				var resourcePath = ModList.get().getModFileById(TwilightForestMod.ID).getFile().findResource("classic");
				var pack = new PathResourcePack(ModList.get().getModFileById(TwilightForestMod.ID).getFile().getFileName() + ":" + resourcePath, resourcePath);
				var metadataSection = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
				if (metadataSection != null) {
					event.addRepositorySource((packConsumer, packConstructor) ->
							packConsumer.accept(packConstructor.create(
									"builtin/twilight_forest_legacy_resources", new TextComponent("Twilight Classic"), false,
									() -> pack, metadataSection, Pack.Position.TOP, PackSource.BUILT_IN, false)));
				}
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> evt) {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();

		//TODO find a better place for these? they work fine here but idk
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
		Registry.register(Registry.BIOME_SOURCE, TwilightForestMod.prefix("landmarks"), LandmarkBiomeSource.CODEC);

		Registry.register(Registry.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), ChunkGeneratorTwilight.CODEC);
	}

	@SubscribeEvent
	public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new FieryPickItem.Serializer().setRegistryName(ID + ":fiery_pick_smelting"));
		evt.getRegistry().register(new TFEventListener.Serializer().setRegistryName(ID + ":giant_block_grouping"));
	}

	public void sendIMCs(InterModEnqueueEvent evt) {
		TFCompat.sendIMCs();
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent evt) {
		TFPacketHandler.init();
		TFAdvancements.init();
		BiomeKeys.addBiomeTypes();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.initCompat(evt);
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading init compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.postInitCompat();
			} catch (Exception e) {
				TFConfig.COMMON_CONFIG.doCompat.set(false);
				LOGGER.error("Had an error loading postInit compatibility!");
				LOGGER.catching(e.fillInStackTrace());
			}
		}

		TFConfig.build();
		BlockSpikeFeature.loadStalactites();

		evt.enqueueWork(() -> {
			TFBlocks.tfCompostables();
			TFBlocks.tfBurnables();
			TFBlocks.tfPots();
			TFSounds.registerParrotSounds();
			TFDispenserBehaviors.init();

			WoodType.register(TFBlocks.TWILIGHT_OAK);
			WoodType.register(TFBlocks.CANOPY);
			WoodType.register(TFBlocks.MANGROVE);
			WoodType.register(TFBlocks.DARKWOOD);
			WoodType.register(TFBlocks.TIMEWOOD);
			WoodType.register(TFBlocks.TRANSFORMATION);
			WoodType.register(TFBlocks.MINING);
			WoodType.register(TFBlocks.SORTING);

			CauldronInteraction.WATER.put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);

			AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get());
			AxeItem.STRIPPABLES.put(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get());

			AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
			AxeItem.STRIPPABLES.put(TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());
		});
	}

	public void registerCommands(RegisterCommandsEvent event) {
		TFCommand.register(event.getDispatcher());
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
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
		return rarity;
	}
}
