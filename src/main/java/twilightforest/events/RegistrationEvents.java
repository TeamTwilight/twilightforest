package twilightforest.events;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.command.TFCommand;
import twilightforest.config.ConfigSetup;
//import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.RovingCube;
import twilightforest.entity.boss.*;
import twilightforest.entity.monster.*;
import twilightforest.entity.passive.*;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.*;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.*;
import twilightforest.util.HolidayEvent;
import twilightforest.util.Restriction;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.components.speleothem.StalactiteReloadListener;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

@Component
public class RegistrationEvents {

	@Autowired
	private TFCommand tfCommand;

	@Autowired
	private HolidayEvent holidayEvent;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::init);
		bus.addListener(this::sendIMCs);
		bus.addListener(this::setupPackets);
		bus.addListener(this::createDataMaps);
		bus.addListener(this::registerExtraStuff);
		bus.addListener(this::createNewRegistries);
		bus.addListener(this::addBlockEntityTypes);
		bus.addListener(this::setRegistriesForDatapack);
		bus.addListener(this::registerGenericItemHandlers);
		bus.addListener(this::addEntityAttributes);
		bus.addListener(this::registerSpawnPlacements);
		bus.addListener(TFCreativeTabs::addToTabs);

		bus.addListener(ConfigSetup::loadConfigs);
		bus.addListener(ConfigSetup::reloadConfigs);

		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TwilightForestMod.prefix("quests"), new QuestReloadListener()));
		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TwilightForestMod.prefix("travellers_modifiers"), TravellersModifiersManager.CacheInvalidationReloadListener.INSTANCE));
		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TwilightForestMod.prefix("stalactites"), StalactiteReloadListener.INSTANCE));
		NeoForge.EVENT_BUS.addListener(AddServerReloadListenersEvent.class, event -> event.addListener(TwilightForestMod.prefix("structure_template_definitions"), StructureTemplateDefinitions.INSTANCE));
		NeoForge.EVENT_BUS.addListener(ConfigSetup::syncUncraftingConfig);
	}

	private void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
		IBlockCapabilityProvider<ResourceHandler<ItemResource>, @Nullable Direction> itemHandlerProvider = (level, pos, state, blockEntity, side) -> level.getBlockEntity(pos) instanceof ChestBlockEntity tfChestBlock ? VanillaContainerWrapper.of(tfChestBlock) : null;
		event.registerBlock(
			Capabilities.Item.BLOCK,
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

		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.MASON_JAR.get(), (masonJarBlock, side) ->
			side == Direction.UP ? masonJarBlock.getItemHandler() : null);

		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.DRYING_RACK.get(), (entity, side) -> new DryingRackBlockEntity.DryingRackHandler(entity));
		event.registerBlockEntity(Capabilities.Item.BLOCK, TFBlockEntities.CHISELED_CANOPY_BOOKSHELF.get(), (entity, side) -> entity.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER) ? null : VanillaContainerWrapper.of(entity));
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
		event.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES);
		event.register(TFRegistries.ITEM_DISPLAY_TYPE);
		event.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE);
	}

	public void setRegistriesForDatapack(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC, Restriction.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC, MagicPaintingVariant.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, TemplateMarkerHandlers.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerList.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariant.DIRECT_CODEC, DwarfRabbitVariant.DIRECT_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariant.DIRECT_CODEC, TinyBirdVariant.DIRECT_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifier.CODEC, TravellersModifier.CODEC);
	}

	public void registerExtraStuff(RegisterEvent evt) {
		if (evt.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
		}
	}

	public void createDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TFDataMaps.CRUMBLE_HORN);
		event.register(TFDataMaps.TRANSFORMATION_POWDER);
		event.register(TFDataMaps.OMINOUS_FIRE);
		event.register(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		event.register(TFDataMaps.ORE_MAP_ORE_COLOR);
	}

	public void sendIMCs(InterModEnqueueEvent event) {
		//if (ModList.get().isLoaded("theoneprobe")) InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopCompat::new);
	}

	public void setupPackets(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(TwilightForestMod.ID).versioned("1.0.0").optional();
		registrar.playToClient(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC, AreaProtectionPacket::handle);
		registrar.playToClient(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC, CreateMovingCicadaSoundPacket::handle);
		registrar.playToClient(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC, EnforceProgressionStatusPacket::handle);
		registrar.playToClient(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC, MagicMapPacket::handle);
		registrar.playToClient(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC, MazeMapPacket::handle);
		registrar.playToClient(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC, MissingAdvancementToastPacket::handle);
		registrar.playToClient(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC, MovePlayerPacket::handle);
		registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC, ParticlePacket::handle);
		registrar.playBidirectional(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC, GogglesZoomPacket::handle, GogglesZoomPacket::handle);
		registrar.playBidirectional(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC, GradualGlidePacket::handle, GradualGlidePacket::handle);
		registrar.playToServer(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC, PerformDoubleJumpPacket::handle);
		registrar.playToServer(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC, SwapHotbarPacket::handle);
		registrar.playToServer(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC, PerformSidestepPacket::handle);
		registrar.playToServer(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC, CycleMapSlotPacket::handle);
		registrar.playToClient(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC, SpawnCharmPacket::handle);
		registrar.playToClient(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC, SpawnFallenLeafFromPacket::handle);
		registrar.playToClient(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC, StructureProtectionPacket::handle);
		registrar.playToClient(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC, SyncUncraftingTableConfigPacket::handle);
		registrar.playToServer(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC, UncraftingGuiPacket::handle);
		registrar.playToClient(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC, UpdateTFMultipartPacket::handle);
		registrar.playToClient(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC, UpdateThrownPacket::handle);
		registrar.playToServer(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC, WipeOreMeterPacket::handle);
		registrar.playToClient(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC, LifedrainParticlePacket::handle);
		registrar.playToClient(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC, UpdateDeathTimePacket::handle);
		registrar.playToClient(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC, TFBossBarPacket.AddTFBossBarPacket::handle);
		registrar.playToClient(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC, TFBossBarPacket.UpdateTFBossBarStylePacket::handle);
		registrar.playToClient(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC, SetMasonJarItemPacket::handle);
		registrar.playToClient(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC, SyncQuestsPacket::handle);
		registrar.playToClient(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC, TravellersWingsStatePacket::handle);
	}

	public void init(FMLCommonSetupEvent evt) {
		evt.enqueueWork(() -> {
			TFDispenserBehaviors.init();
			TFStats.init();

			// Arctic armor dye washing in cauldrons (26.1.2: CauldronInteractions.WATER + dyedItemIteration)
			CauldronInteractions.WATER.put(TFItems.ARCTIC_HELMET.get(), CauldronInteractions::dyedItemIteration);
			CauldronInteractions.WATER.put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteractions::dyedItemIteration);
			CauldronInteractions.WATER.put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteractions::dyedItemIteration);
			CauldronInteractions.WATER.put(TFItems.ARCTIC_BOOTS.get(), CauldronInteractions::dyedItemIteration);

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

			fireblock.setFlammable(TFBlocks.RASPBERRY_BUSH.get(), 4, 25);
			fireblock.setFlammable(TFBlocks.BLUEBERRY_BUSH.get(), 4, 25);
			fireblock.setFlammable(TFBlocks.BLACKBERRY_BUSH.get(), 4, 25);
			fireblock.setFlammable(TFBlocks.MALOBERRY_BUSH.get(), 4, 25);
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
			fireblock.setFlammable(TFBlocks.HUGE_STALK.get(), 5, 5);

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
			JarBlockEntity.addLid(Items.PUMPKIN, () -> this.holidayEvent.isHalloweenWeek());
		});
	}

	private void registerCommands(RegisterCommandsEvent event) {
		this.tfCommand.register(event.getDispatcher(), event.getBuildContext());
	}

	private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(TFEntities.BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.BIGHORN_SHEEP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.REDCAP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SKELETON_DRUID.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonDruid::checkDruidSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.WRAITH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wraith::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.HOSTILE_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileWolf::checkWolfSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.HYDRA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.LICH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.PENGUIN.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Penguin::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.LICH_MINION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.LOYAL_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.TINY_BIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SQUIRREL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.DWARF_RABBIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.RAVEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.QUEST_RAM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.KOBOLD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.MOSQUITO_SWARM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.DEATH_TOME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.MINOTAUR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.MINOSHROOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.FIRE_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SLIME_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.PINCH_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.MIST_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.CARMINITE_GHASTLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastling::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.CARMINITE_GOLEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.TOWERWOOD_BORER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.CARMINITE_GHASTGUARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CarminiteGhastguard::ghastSpawnHandler, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.UR_GHAST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.HELMET_CRAB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.KNIGHT_PHANTOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.NAGA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SWARM_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SwarmSpider::getCanSpawnHere, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.KING_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.CARMINITE_BROODLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.HEDGE_SPIDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HedgeSpider::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.REDCAP_SAPPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.MAZE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MazeSlime::getCanSpawnHere, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.YETI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Yeti::yetiSnowyForestSpawnHandler, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.ALPHA_YETI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.WINTER_WOLF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterWolf::canSpawnHere, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SNOW_GUARDIAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.STABLE_ICE_CORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.UNSTABLE_ICE_CORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.SNOW_QUEEN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.TROLL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.GIANT_MINER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.ARMORED_GIANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GiantMiner::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.ICE_CRYSTAL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.HARBINGER_CUBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.ADHERENT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.ROVING_CUBE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(TFEntities.RISING_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	private void addEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(TFEntities.BOAR.get(), Boar.registerAttributes().build());
		event.put(TFEntities.BIGHORN_SHEEP.get(), Sheep.createAttributes().build());
		event.put(TFEntities.DEER.get(), Deer.registerAttributes().build());
		event.put(TFEntities.REDCAP.get(), Redcap.registerAttributes().build());
		event.put(TFEntities.SWARM_SPIDER.get(), SwarmSpider.registerAttributes().build());
		event.put(TFEntities.NAGA.get(), Naga.registerAttributes().build());
		event.put(TFEntities.SKELETON_DRUID.get(), AbstractSkeleton.createAttributes().build());
		event.put(TFEntities.HOSTILE_WOLF.get(), HostileWolf.registerAttributes().build());
		event.put(TFEntities.WRAITH.get(), Wraith.registerAttributes().build());
		event.put(TFEntities.HEDGE_SPIDER.get(), Spider.createAttributes().build());
		event.put(TFEntities.HYDRA.get(), Hydra.registerAttributes().build());
		event.put(TFEntities.LICH.get(), Lich.registerAttributes().build());
		event.put(TFEntities.PENGUIN.get(), Penguin.registerAttributes().build());
		event.put(TFEntities.LICH_MINION.get(), Zombie.createAttributes().build());
		event.put(TFEntities.LOYAL_ZOMBIE.get(), LoyalZombie.registerAttributes().build());
		event.put(TFEntities.TINY_BIRD.get(), TinyBird.registerAttributes().build());
		event.put(TFEntities.SQUIRREL.get(), Squirrel.registerAttributes().build());
		event.put(TFEntities.DWARF_RABBIT.get(), DwarfRabbit.registerAttributes().build());
		event.put(TFEntities.RAVEN.get(), Raven.registerAttributes().build());
		event.put(TFEntities.QUEST_RAM.get(), QuestRam.registerAttributes().build());
		event.put(TFEntities.KOBOLD.get(), Kobold.registerAttributes().build());
		event.put(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarm.registerAttributes().build());
		event.put(TFEntities.DEATH_TOME.get(), DeathTome.registerAttributes().build());
		event.put(TFEntities.MINOTAUR.get(), Minotaur.registerAttributes().build());
		event.put(TFEntities.MINOSHROOM.get(), Minoshroom.registerAttributes().build());
		event.put(TFEntities.FIRE_BEETLE.get(), FireBeetle.registerAttributes().build());
		event.put(TFEntities.SLIME_BEETLE.get(), SlimeBeetle.registerAttributes().build());
		event.put(TFEntities.PINCH_BEETLE.get(), PinchBeetle.registerAttributes().build());
		event.put(TFEntities.MAZE_SLIME.get(), Monster.createMonsterAttributes().build());
		event.put(TFEntities.REDCAP_SAPPER.get(), RedcapSapper.registerAttributes().build());
		event.put(TFEntities.MIST_WOLF.get(), MistWolf.registerAttributes().build());
		event.put(TFEntities.KING_SPIDER.get(), KingSpider.registerAttributes().build());
		event.put(TFEntities.CARMINITE_GHASTLING.get(), CarminiteGhastling.registerAttributes().build());
		event.put(TFEntities.CARMINITE_GHASTGUARD.get(), CarminiteGhastguard.registerAttributes().build());
		event.put(TFEntities.CARMINITE_GOLEM.get(), CarminiteGolem.registerAttributes().build());
		event.put(TFEntities.TOWERWOOD_BORER.get(), TowerwoodBorer.registerAttributes().build());
		event.put(TFEntities.CARMINITE_BROODLING.get(), TowerBroodling.registerAttributes().build());
		event.put(TFEntities.UR_GHAST.get(), UrGhast.registerAttributes().build());
		event.put(TFEntities.BLOCKCHAIN_GOBLIN.get(), BlockChainGoblin.registerAttributes().build());
		event.put(TFEntities.UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnight.registerAttributes().build());
		event.put(TFEntities.LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnight.registerAttributes().build());
		event.put(TFEntities.HELMET_CRAB.get(), HelmetCrab.registerAttributes().build());
		event.put(TFEntities.KNIGHT_PHANTOM.get(), KnightPhantom.registerAttributes().build());
		event.put(TFEntities.YETI.get(), Yeti.registerAttributes().build());
		event.put(TFEntities.ALPHA_YETI.get(), AlphaYeti.registerAttributes().build());
		event.put(TFEntities.WINTER_WOLF.get(), WinterWolf.registerAttributes().build());
		event.put(TFEntities.SNOW_GUARDIAN.get(), SnowGuardian.registerAttributes().build());
		event.put(TFEntities.STABLE_ICE_CORE.get(), StableIceCore.registerAttributes().build());
		event.put(TFEntities.UNSTABLE_ICE_CORE.get(), UnstableIceCore.registerAttributes().build());
		event.put(TFEntities.SNOW_QUEEN.get(), SnowQueen.registerAttributes().build());
		event.put(TFEntities.TROLL.get(), Troll.registerAttributes().build());
		event.put(TFEntities.GIANT_MINER.get(), GiantMiner.registerAttributes().build());
		event.put(TFEntities.ARMORED_GIANT.get(), GiantMiner.registerAttributes().build());
		event.put(TFEntities.ICE_CRYSTAL.get(), IceCrystal.registerAttributes().build());
		event.put(TFEntities.HARBINGER_CUBE.get(), HarbingerCube.registerAttributes().build());
		event.put(TFEntities.ADHERENT.get(), Adherent.registerAttributes().build());
		event.put(TFEntities.ROVING_CUBE.get(), RovingCube.registerAttributes().build());
		event.put(TFEntities.PLATEAU_BOSS.get(), PlateauBoss.registerAttributes().build());

		//event.put(TFEntities.BOGGARD.get(), Boggard.registerAttributes().create());
		event.put(TFEntities.RISING_ZOMBIE.get(), Zombie.createAttributes().build());
	}
}
