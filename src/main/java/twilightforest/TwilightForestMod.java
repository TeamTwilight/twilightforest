package twilightforest;

import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import twilightforest.beans.Autowired;
import twilightforest.beans.ProcessBeanAnnotationsEvent;
import twilightforest.beans.TFBeanContext;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.client.event.ClientEvents;
import twilightforest.client.event.RegistrationEvents;
import twilightforest.command.TFCommand;
import twilightforest.compat.CosmeticArmorCompat;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.ConfigSetup;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.*;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.init.custom.Enforcements;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.*;
import twilightforest.util.HolidayEvent;
import twilightforest.util.Restriction;
import twilightforest.util.TFRemapper;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

import java.util.Locale;

@Mod(TwilightForestMod.ID)
public final class TwilightForestMod {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRules.register("tfEnforcedProgression",
		GameRules.Category.UPDATES,  //Putting it in UPDATES since other world stuff is here
		GameRules.BooleanValue.create(true, (server, enforced) ->
			PacketDistributor.sendToAllPlayers(new EnforceProgressionStatusPacket(enforced.get())))); //sends a packet to every player online when this changes so weather effects update accordingly

	public static final Logger LOGGER = LogManager.getLogger(ID);

	@Nullable
	private static QuestReloadListener QUEST_INSTANCE;

	static { // Load as early as possible
		TFBeanContext.init();
	}

	@Autowired
	private TFCommand tfCommand;

	@Autowired
	private HolidayEvent holidayEvent;

	public TwilightForestMod(IEventBus bus, Dist dist) {
		bus.post(new ProcessBeanAnnotationsEvent(this)); // Enables @Autowired
		Reflection.initialize(ConfigSetup.class);
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
		if (dist.isClient()) {
			RegistrationEvents.initModBusEvents(bus);
			ClientEvents.initGameEvents();
		}
		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		NeoForge.EVENT_BUS.addListener(Stalactite::reloadStalactites);

		TFItems.ITEMS.register(bus);
		TFStats.STATS.register(bus);
		TFLoot.NUMBERS.register(bus);
		TFBlocks.BLOCKS.register(bus);
		TFPOITypes.POIS.register(bus);
		TFSounds.SOUNDS.register(bus);
		TFLoot.FUNCTIONS.register(bus);
		TFLoot.CONDITIONS.register(bus);
		TFEntities.ENTITIES.register(bus);
		TFFeatures.FEATURES.register(bus);
		TFCreativeTabs.TABS.register(bus);
		TFLoot.CONDITIONALS.register(bus);
		TFEntities.SPAWN_EGGS.register(bus);
		TFMenuTypes.CONTAINERS.register(bus);
		TFRecipes.RECIPE_TYPES.register(bus);
		TFAdvancements.TRIGGERS.register(bus);
		TFMobEffects.MOB_EFFECTS.register(bus);
		TFItemSubPredicates.TYPES.register(bus);
		Enforcements.ENFORCEMENTS.register(bus);
		TFCaveCarvers.CARVER_TYPES.register(bus);
		TFDataComponents.COMPONENTS.register(bus);
		TFRecipes.RECIPE_SERIALIZERS.register(bus);
		TFMapDecorations.DECORATIONS.register(bus);
		TFParticleType.PARTICLE_TYPES.register(bus);
		TFBlockEntities.BLOCK_ENTITIES.register(bus);
		TFLootModifiers.LOOT_MODIFIERS.register(bus);
		TFArmorMaterials.ARMOR_MATERIALS.register(bus);
		TFStructureTypes.STRUCTURE_TYPES.register(bus);
		TFFeatureModifiers.TRUNK_PLACERS.register(bus);
		BiomeLayerTypes.BIOME_LAYER_TYPES.register(bus);
		TFDataAttachments.ATTACHMENT_TYPES.register(bus);
		TFDataSerializers.DATA_SERIALIZERS.register(bus);
		TFFeatureModifiers.FOLIAGE_PLACERS.register(bus);
		TFFeatureModifiers.TREE_DECORATORS.register(bus);
		TFEnchantmentEffects.ENTITY_EFFECTS.register(bus);
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register(bus);
		TFDensityFunctions.DENSITY_FUNCTION_TYPES.register(bus);
		TFStructureProcessors.STRUCTURE_PROCESSORS.register(bus);
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		ChunkBlanketProcessors.CHUNK_BLANKETING_TYPES.register(bus);
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);

		TFRemapper.addRegistryAliases();

		bus.addListener(this::init);
		bus.addListener(this::sendIMCs);
		bus.addListener(this::setupPackets);
		bus.addListener(this::createDataMaps);
		bus.addListener(this::registerExtraStuff);
		bus.addListener(this::createNewRegistries);
		bus.addListener(this::addBlockEntityTypes);
		bus.addListener(this::setRegistriesForDatapack);
		bus.addListener(this::registerGenericItemHandlers);

		bus.addListener(ConfigSetup::loadConfigs);
		bus.addListener(ConfigSetup::reloadConfigs);
		NeoForge.EVENT_BUS.addListener(ConfigSetup::syncUncraftingConfig);
		NeoForge.EVENT_BUS.addListener(this::reloadQuests);

		if (ModList.get().isLoaded("curios")) loadCuriosCompat(bus);
		if (ModList.get().isLoaded("cosmeticarmorreworked")) NeoForge.EVENT_BUS.addListener(CosmeticArmorCompat::keepCosmeticArmor);
	}

	private static void loadCuriosCompat(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(CuriosCompat::keepCurios);
		bus.addListener(CuriosCompat::registerCuriosCapabilities);
		bus.addListener(CuriosCompat::registerCurioRenderers);
		bus.addListener(CuriosCompat::registerCurioLayers);
	}

	private void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
		IBlockCapabilityProvider<IItemHandler, @Nullable Direction> itemHandlerProvider = (level, pos, state, blockEntity, side) -> level.getBlockEntity(pos) instanceof ChestBlockEntity tfChestBlock ? new InvWrapper(tfChestBlock) : null;
		event.registerBlock(
			Capabilities.ItemHandler.BLOCK,
			itemHandlerProvider,
			TFBlocks.TWILIGHT_OAK_CHEST.get(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get()
		);

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, TFBlockEntities.MASON_JAR.get(), (masonJarBlock, side) ->
			side == Direction.UP ? masonJarBlock.getItemHandler() : null);
	}

	public void addBlockEntityTypes(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityType.HANGING_SIGN,
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(),
			TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(),
			TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(),
			TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(),
			TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_WALL_HANGING_SIGN.get(),
			TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(),
			TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_WALL_HANGING_SIGN.get(),
			TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get());

		event.modify(BlockEntityType.SIGN,
			TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(),
			TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
			TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(),
			TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(),
			TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(),
			TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
			TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(),
			TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get());
	}

	public void createNewRegistries(NewRegistryEvent event) {
		event.register(TFRegistries.BIOME_LAYER_TYPE);
		event.register(TFRegistries.ENFORCEMENT);
		event.register(TFRegistries.CHUNK_BLANKET_TYPES);
	}

	public void setRegistriesForDatapack(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC, Restriction.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC, MagicPaintingVariant.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariant.DIRECT_CODEC, DwarfRabbitVariant.DIRECT_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariant.DIRECT_CODEC, TinyBirdVariant.DIRECT_CODEC);
	}

	public void registerExtraStuff(RegisterEvent evt) {
		if (evt.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
		}
	}

	public void createDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TFDataMaps.CRUMBLE_HORN);
		event.register(TFDataMaps.TRANSFORMATION_POWDER);
		event.register(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		event.register(TFDataMaps.ORE_MAP_ORE_COLOR);
	}

	public void sendIMCs(InterModEnqueueEvent evt) {
		//if (ModList.get().isLoaded("theoneprobe")) InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopCompat::new);
	}

	public void setupPackets(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(ID).versioned("1.0.0").optional();
		registrar.playToClient(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC, AreaProtectionPacket::handle);
		registrar.playToClient(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC, CreateMovingCicadaSoundPacket::handle);
		registrar.playToClient(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC, EnforceProgressionStatusPacket::handle);
		registrar.playToClient(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC, MagicMapPacket::handle);
		registrar.playToClient(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC, MazeMapPacket::handle);
		registrar.playToClient(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC, MissingAdvancementToastPacket::handle);
		registrar.playToClient(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC, MovePlayerPacket::handle);
		registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC, ParticlePacket::handle);
		registrar.playToClient(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC, SpawnCharmPacket::handle);
		registrar.playToClient(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC, SpawnFallenLeafFromPacket::handle);
		registrar.playToClient(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC, StructureProtectionPacket::handle);
		registrar.playToClient(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC, SyncUncraftingTableConfigPacket::handle);
		registrar.playToServer(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC, UncraftingGuiPacket::handle);
		registrar.playToClient(UpdateFeatherFanFallPacket.TYPE, UpdateFeatherFanFallPacket.STREAM_CODEC, UpdateFeatherFanFallPacket::handle);
		registrar.playToClient(UpdateShieldPacket.TYPE, UpdateShieldPacket.STREAM_CODEC, UpdateShieldPacket::handle);
		registrar.playToClient(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC, UpdateTFMultipartPacket::handle);
		registrar.playToClient(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC, UpdateThrownPacket::handle);
		registrar.playToServer(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC, WipeOreMeterPacket::handle);
		registrar.playToClient(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC, LifedrainParticlePacket::handle);
		registrar.playToClient(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC, UpdateDeathTimePacket::handle);
		registrar.playToClient(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC, TFBossBarPacket.AddTFBossBarPacket::handle);
		registrar.playToClient(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC, TFBossBarPacket.UpdateTFBossBarStylePacket::handle);
		registrar.playToClient(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC, SetMasonJarItemPacket::handle);
		registrar.playToClient(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC, SyncQuestsPacket::handle);
	}

	public void init(FMLCommonSetupEvent evt) {
		evt.enqueueWork(() -> {
			TFDispenserBehaviors.init();
			TFStats.init();

			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.map().put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);

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

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;

			pot.addPlant(TFBlocks.TWILIGHT_OAK_SAPLING.getId(), TFBlocks.POTTED_TWILIGHT_OAK_SAPLING);
			pot.addPlant(TFBlocks.CANOPY_SAPLING.getId(), TFBlocks.POTTED_CANOPY_SAPLING);
			pot.addPlant(TFBlocks.MANGROVE_SAPLING.getId(), TFBlocks.POTTED_MANGROVE_SAPLING);
			pot.addPlant(TFBlocks.DARKWOOD_SAPLING.getId(), TFBlocks.POTTED_DARKWOOD_SAPLING);
			pot.addPlant(TFBlocks.HOLLOW_OAK_SAPLING.getId(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING);
			pot.addPlant(TFBlocks.RAINBOW_OAK_SAPLING.getId(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING);
			pot.addPlant(TFBlocks.TIME_SAPLING.getId(), TFBlocks.POTTED_TIME_SAPLING);
			pot.addPlant(TFBlocks.TRANSFORMATION_SAPLING.getId(), TFBlocks.POTTED_TRANSFORMATION_SAPLING);
			pot.addPlant(TFBlocks.MINING_SAPLING.getId(), TFBlocks.POTTED_MINING_SAPLING);
			pot.addPlant(TFBlocks.SORTING_SAPLING.getId(), TFBlocks.POTTED_SORTING_SAPLING);
			pot.addPlant(TFBlocks.MAYAPPLE.getId(), TFBlocks.POTTED_MAYAPPLE);
			pot.addPlant(TFBlocks.FIDDLEHEAD.getId(), TFBlocks.POTTED_FIDDLEHEAD);
			pot.addPlant(TFBlocks.MUSHGLOOM.getId(), TFBlocks.POTTED_MUSHGLOOM);
			pot.addPlant(TFBlocks.BROWN_THORNS.getId(), TFBlocks.POTTED_THORN);
			pot.addPlant(TFBlocks.GREEN_THORNS.getId(), TFBlocks.POTTED_GREEN_THORN);
			pot.addPlant(TFBlocks.BURNT_THORNS.getId(), TFBlocks.POTTED_DEAD_THORN);

			FireBlock fireblock = (FireBlock) Blocks.FIRE;
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.CANOPY_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.CANOPY_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_CANOPY_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_CANOPY_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.CANOPY_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_BOOKSHELF.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.MANGROVE_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MANGROVE_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_MANGROVE_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_MANGROVE_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MANGROVE_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_GATE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_ROOT.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.DARK_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.DARK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_DARK_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_DARK_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.DARK_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.TIME_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TIME_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TIME_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TIME_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TIME_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.TRANSFORMATION_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.MINING_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MINING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_MINING_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_MINING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.MINING_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.SORTING_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.SORTING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_SORTING_LOG.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.STRIPPED_SORTING_WOOD.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.SORTING_BANISTER.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.SORTING_PLANKS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_SLAB.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_STAIRS.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_FENCE.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_GATE.get(), 5, 20);

			fireblock.setFlammable(TFBlocks.CLOVER_PATCH.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.FALLEN_LEAVES.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.FIDDLEHEAD.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.MAYAPPLE.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.MOSS_PATCH.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.ROOT_STRAND.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.TORCHBERRY_PLANT.get(), 60, 100);
			fireblock.setFlammable(TFBlocks.ROOT_BLOCK.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.ARCTIC_FUR_BLOCK.get(), 20, 20);
			fireblock.setFlammable(TFBlocks.LIVEROOT_BLOCK.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.CHISELED_CANOPY_BOOKSHELF.get(), 30, 20);

			fireblock.setFlammable(TFBlocks.TOWERWOOD.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.CRACKED_TOWERWOOD.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.MOSSY_TOWERWOOD.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.ENCASED_TOWERWOOD.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.INFESTED_TOWERWOOD.get(), 0, 1);

			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.CANOPY_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.MANGROVE_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.DARK_LEAVES.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.HARDENED_DARK_LEAVES.get(), 0, 1);
			fireblock.setFlammable(TFBlocks.TIME_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.MINING_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.SORTING_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.BEANSTALK_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.THORN_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.RAINBOW_OAK_LEAVES.get(), 30, 60);
			fireblock.setFlammable(TFBlocks.HARDENED_DARK_LEAVES.get(), 0, 1);

			fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.OAK_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.SPRUCE_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.BIRCH_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.JUNGLE_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.ACACIA_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.DARK_OAK_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.CRIMSON_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.WARPED_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.VANGROVE_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(), 5, 5);
			fireblock.setFlammable(TFBlocks.CHERRY_BANISTER.get(), 5, 20);
			fireblock.setFlammable(TFBlocks.BAMBOO_BANISTER.get(), 5, 20);

			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.get().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.get().asItem());

			JarBlockEntity.addLid(TFBlocks.MANGROVE_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.CANOPY_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.DARK_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.MINING_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.SORTING_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.TIME_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.TRANSFORMATION_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.TWILIGHT_OAK_LOG.asItem());
			JarBlockEntity.addLid(Items.ACACIA_LOG);
			JarBlockEntity.addLid(Items.BIRCH_LOG);
			JarBlockEntity.addLid(Items.CHERRY_LOG);
			JarBlockEntity.addLid(Items.DARK_OAK_LOG);
			JarBlockEntity.addLid(Items.JUNGLE_LOG);
			JarBlockEntity.addLid(Items.MANGROVE_LOG);
			JarBlockEntity.addLid(Items.OAK_LOG);
			JarBlockEntity.addLid(Items.SPRUCE_LOG);
			JarBlockEntity.addLid(Items.CRIMSON_STEM);
			JarBlockEntity.addLid(Items.WARPED_STEM);
			JarBlockEntity.addLid(TFBlocks.STRIPPED_MANGROVE_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_CANOPY_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_DARK_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_MINING_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_SORTING_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_TIME_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem());
			JarBlockEntity.addLid(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem());
			JarBlockEntity.addLid(Items.STRIPPED_ACACIA_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_BIRCH_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_CHERRY_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_DARK_OAK_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_JUNGLE_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_MANGROVE_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_OAK_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_SPRUCE_LOG);
			JarBlockEntity.addLid(Items.STRIPPED_CRIMSON_STEM);
			JarBlockEntity.addLid(Items.STRIPPED_WARPED_STEM);
			JarBlockEntity.addLid(TFBlocks.CINDER_LOG.asItem());
			JarBlockEntity.addLid(Items.BAMBOO_BLOCK);
			JarBlockEntity.addLid(Items.STRIPPED_BAMBOO_BLOCK);
			JarBlockEntity.addLid(Items.PUMPKIN, () -> holidayEvent.isHalloweenWeek());
		});
	}

	public void registerCommands(RegisterCommandsEvent event) {
		tfCommand.register(event.getDispatcher(), event.getBuildContext());
	}

	public static ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation getModelTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}

	private void reloadQuests(AddReloadListenerEvent event) {
		QUEST_INSTANCE = new QuestReloadListener();
		event.addListener(QUEST_INSTANCE);
	}

	public static QuestReloadListener getQuests() {
		if (QUEST_INSTANCE == null)
			throw new IllegalStateException("Can't retrieve QuestReloadListener until resources have loaded once");
		return QUEST_INSTANCE;
	}
}
