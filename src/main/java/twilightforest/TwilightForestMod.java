package twilightforest;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.client.particle.TFParticleType;
import twilightforest.command.TFCommand;
import twilightforest.compat.TFCompat;
import twilightforest.dispenser.CrumbleDispenseBehavior;
import twilightforest.dispenser.FeatherFanDispenseBehavior;
import twilightforest.dispenser.MoonwormDispenseBehavior;
import twilightforest.dispenser.TransformationDispenseBehavior;
import twilightforest.entity.projectile.MoonwormShotEntity;
import twilightforest.entity.projectile.TwilightWandBoltEntity;
import twilightforest.inventory.TFContainers;
import twilightforest.item.TFItems;
import twilightforest.item.recipe.UncraftingEnabledCondition;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFPotions;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.feature.TFGenCaveStalactite;
import twilightforest.world.registration.TFBiomeFeatures;
import twilightforest.world.registration.TFDimensions;
import twilightforest.world.registration.TFStructures;
import twilightforest.world.registration.TwilightFeatures;
import twilightforest.world.registration.biomes.BiomeKeys;

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
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> (DistExecutor.SafeRunnable) twilightforest.client.TFClientSetup::addLegacyPack);

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

		ASMHooks.registerMultipartEvents(MinecraftForge.EVENT_BUS);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
		TFBlocks.BLOCKS.register(modbus);
		TFItems.ITEMS.register(modbus);
		TFPotions.POTIONS.register(modbus);
		BiomeKeys.BIOMES.register(modbus);
		modbus.addGenericListener(SoundEvent.class, TFSounds::registerSounds);
		TFTileEntities.TILE_ENTITIES.register(modbus);
		TFParticleType.PARTICLE_TYPES.register(modbus);
		modbus.addGenericListener(StructureFeature.class, TFStructures::register);
		MinecraftForge.EVENT_BUS.addListener(TFStructures::load);
		TFBiomeFeatures.FEATURES.register(modbus);
		TFContainers.CONTAINERS.register(modbus);
//		TFEnchantments.ENCHANTMENTS.register(modbus);
		// Poke these so they exist when we need them FIXME this is probably terrible design
		new TwilightFeatures();
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
	public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> evt) {
		//How do I add a condition serializer as fast as possible? An event that fires really early
		CraftingHelper.register(new UncraftingEnabledCondition.Serializer());
		TFTreasure.init();
	}

	@SubscribeEvent
	public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new TFEventListener.SmeltSerializer().setRegistryName(ID + ":fiery_pick_smelting"));
		evt.getRegistry().register(new TFEventListener.ManipulateSerializer().setRegistryName(ID + ":giant_block_grouping"));
	}

	@SubscribeEvent
	public void sendIMCs(InterModEnqueueEvent evt) {
		TFCompat.IMCSender();
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent evt) {

		CapabilityList.registerCapabilities();
		TFPacketHandler.init();
		TFAdvancements.init();
		BiomeKeys.addBiomeTypes();
		TFDimensions.init();

		if (TFConfig.COMMON_CONFIG.doCompat.get()) {
			try {
				TFCompat.initCompat();
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
		TFGenCaveStalactite.loadStalactites();

		evt.enqueueWork(() -> {
			TFBlocks.tfCompostables();
			TFBlocks.TFBurnables();
			TFBlocks.TFPots();
			TFSounds.registerParrotSounds();

			AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			AxeItem.STRIPPABLES.put(TFBlocks.oak_log.get(), TFBlocks.stripped_oak_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.canopy_log.get(), TFBlocks.stripped_canopy_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.mangrove_log.get(), TFBlocks.stripped_mangrove_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.dark_log.get(), TFBlocks.stripped_dark_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.time_log.get(), TFBlocks.stripped_time_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.transformation_log.get(), TFBlocks.stripped_transformation_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.mining_log.get(), TFBlocks.stripped_mining_log.get());
			AxeItem.STRIPPABLES.put(TFBlocks.sorting_log.get(), TFBlocks.stripped_sorting_log.get());

			AxeItem.STRIPPABLES.put(TFBlocks.oak_wood.get(), TFBlocks.stripped_oak_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.canopy_wood.get(), TFBlocks.stripped_canopy_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.mangrove_wood.get(), TFBlocks.stripped_mangrove_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.dark_wood.get(), TFBlocks.stripped_dark_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.time_wood.get(), TFBlocks.stripped_time_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.transformation_wood.get(), TFBlocks.stripped_transformation_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.mining_wood.get(), TFBlocks.stripped_mining_wood.get());
			AxeItem.STRIPPABLES.put(TFBlocks.sorting_wood.get(), TFBlocks.stripped_sorting_wood.get());

			DispenserBlock.registerBehavior(TFItems.moonworm_queen.get(), new MoonwormDispenseBehavior() {
				@Override
				protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
					return new MoonwormShotEntity(worldIn, position.x(), position.y(), position.z());
				}
			});

			DispenseItemBehavior idispenseitembehavior = new OptionalDispenseItemBehavior() {
				/**
				 * Dispense the specified stack, play the dispense sound and spawn particles.
				 */
				protected ItemStack execute(BlockSource source, ItemStack stack) {
					this.setSuccess(ArmorItem.dispenseArmor(source, stack));
					return stack;
				}
			};
			DispenserBlock.registerBehavior(TFBlocks.naga_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.lich_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.minoshroom_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.hydra_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.knight_phantom_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.ur_ghast_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.snow_queen_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.quest_ram_trophy.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.cicada.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.firefly.get().asItem(), idispenseitembehavior);
			DispenserBlock.registerBehavior(TFBlocks.moonworm.get().asItem(), idispenseitembehavior);

			DispenseItemBehavior pushmobsbehavior = new FeatherFanDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.peacock_fan.get().asItem(), pushmobsbehavior);

			DispenseItemBehavior crumblebehavior = new CrumbleDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.crumble_horn.get().asItem(), crumblebehavior);

			DispenseItemBehavior transformbehavior = new TransformationDispenseBehavior();
			DispenserBlock.registerBehavior(TFItems.transformation_powder.get().asItem(), transformbehavior);

			DispenserBlock.registerBehavior(TFItems.twilight_scepter.get(), new MoonwormDispenseBehavior() {
				@Override
				protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
					return new TwilightWandBoltEntity(worldIn, position.x(), position.y(), position.z());
				}

				@Override
				protected void playSound(BlockSource source) {
					BlockPos pos = source.getPos();
					source.getLevel().playSound(null, pos, TFSounds.SCEPTER_PEARL, SoundSource.BLOCKS, 1, 1);
				}
			});
		});
		WoodType.register(TFBlocks.TWILIGHT_OAK);
		WoodType.register(TFBlocks.CANOPY);
		WoodType.register(TFBlocks.MANGROVE);
		WoodType.register(TFBlocks.DARKWOOD);
		WoodType.register(TFBlocks.TIMEWOOD);
		WoodType.register(TFBlocks.TRANSFORMATION);
		WoodType.register(TFBlocks.MINING);
		WoodType.register(TFBlocks.SORTING);
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
		return rarity != null ? rarity : Rarity.EPIC;
	}
}
