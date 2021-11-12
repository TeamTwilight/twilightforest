package twilightforest.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.HollowLogHorizontal;
import twilightforest.block.HollowLogVertical;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TwilightArmorMaterial;
import twilightforest.util.TwilightItemTier;

import javax.annotation.Nullable;
import java.util.UUID;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.food.FoodProperties;

public class TFItems {
	public static final FoodProperties E115 = new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
	public static final FoodProperties CHOP = new FoodProperties.Builder().nutrition(18).saturationMod(2.0F).meat().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodProperties WAFER = new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build();
	public static final FoodProperties MEEF_COOKED = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build();
	public static final FoodProperties MEEF_RAW = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build();
	public static final FoodProperties STROGANOFF = new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).alwaysEat().build();
	public static final FoodProperties VENISON_COOKED = new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build();
	public static final FoodProperties VENISON_RAW = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build();
	public static final FoodProperties TORCHBERRY = new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.GLOWING, 100, 0), 0.75F).build();

	public static final UUID GIANT_REACH_MODIFIER = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);

	public static final RegistryObject<Item> NAGA_SCALE = ITEMS.register("naga_scale", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NAGA_CHESTPLATE = ITEMS.register("naga_chestplate", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NAGA_LEGGINGS = ITEMS.register("naga_leggings", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TWILIGHT_SCEPTER = ITEMS.register("twilight_scepter", () -> new TwilightWandItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LIFEDRAIN_SCEPTER = ITEMS.register("lifedrain_scepter", () -> new LifedrainScepterItem(defaultBuilder().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZOMBIE_SCEPTER = ITEMS.register("zombie_scepter", () -> new ZombieWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FORTIFICATION_SCEPTER = ITEMS.register("fortification_scepter", () -> new FortificationWandItem(defaultBuilder().durability(9).rarity(Rarity.UNCOMMON)));
	//items.register("Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final RegistryObject<Item> ORE_METER = ITEMS.register("ore_meter", () -> new OreMeterItem(defaultBuilder()));
	public static final RegistryObject<Item> FILLED_MAGIC_MAP = ITEMS.register("filled_magic_map", () -> new MagicMapItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> FILLED_MAZE_MAP = ITEMS.register("filled_maze_map", () -> new MazeMapItem(false, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> FILLED_ORE_MAP = ITEMS.register("filled_ore_map", () -> new MazeMapItem(true, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> MAGIC_MAP_FOCUS = ITEMS.register("magic_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> MAZE_MAP_FOCUS = ITEMS.register("maze_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> MAGIC_MAP = ITEMS.register("magic_map", () -> new EmptyMagicMapItem(defaultBuilder()));
	public static final RegistryObject<Item> MAZE_MAP = ITEMS.register("maze_map", () -> new EmptyMazeMapItem(false, defaultBuilder()));
	public static final RegistryObject<Item> ORE_MAP = ITEMS.register("ore_map", () -> new EmptyMazeMapItem(true, defaultBuilder()));
	public static final RegistryObject<Item> LIVEROOT = ITEMS.register("liveroot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> RAW_IRONWOOD = ITEMS.register("raw_ironwood", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_INGOT = ITEMS.register("ironwood_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_HELMET = ITEMS.register("ironwood_helmet", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_CHESTPLATE = ITEMS.register("ironwood_chestplate", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_LEGGINGS = ITEMS.register("ironwood_leggings", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_BOOTS = ITEMS.register("ironwood_boots", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_SWORD = ITEMS.register("ironwood_sword", () -> new IronwoodSwordItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_SHOVEL = ITEMS.register("ironwood_shovel", () -> new IronwoodShovelItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_PICKAXE = ITEMS.register("ironwood_pickaxe", () -> new IronwoodPickItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_AXE = ITEMS.register("ironwood_axe", () -> new IronwoodAxeItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> IRONWOOD_HOE = ITEMS.register("ironwood_hoe", () -> new IronwoodHoeItem(TwilightItemTier.IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> TORCHBERRIES = ITEMS.register("torchberries", () -> new Item(defaultBuilder().food(TORCHBERRY)));
	public static final RegistryObject<Item> RAW_VENISON = ITEMS.register("raw_venison", () -> new Item(defaultBuilder().food(VENISON_RAW)));
	public static final RegistryObject<Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(defaultBuilder().food(VENISON_COOKED)));
	public static final RegistryObject<Item> HYDRA_CHOP = ITEMS.register("hydra_chop", () -> new HydraChopItem(defaultBuilder().fireResistant().food(CHOP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_BLOOD = ITEMS.register("fiery_blood", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_TEARS = ITEMS.register("fiery_tears", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_INGOT = ITEMS.register("fiery_ingot", () -> new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_HELMET = ITEMS.register("fiery_helmet", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.HEAD, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_CHESTPLATE = ITEMS.register("fiery_chestplate", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.CHEST, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_LEGGINGS = ITEMS.register("fiery_leggings", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.LEGS, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_BOOTS = ITEMS.register("fiery_boots", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlot.FEET, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_SWORD = ITEMS.register("fiery_sword", () -> new FierySwordItem(TwilightItemTier.FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_PICKAXE = ITEMS.register("fiery_pickaxe", () -> new FieryPickItem(TwilightItemTier.FIERY, defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> STEELEAF_INGOT = ITEMS.register("steeleaf_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_HELMET = ITEMS.register("steeleaf_helmet", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_CHESTPLATE = ITEMS.register("steeleaf_chestplate", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_LEGGINGS = ITEMS.register("steeleaf_leggings", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_BOOTS = ITEMS.register("steeleaf_boots", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_SWORD = ITEMS.register("steeleaf_sword", () -> new SteeleafSwordItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_SHOVEL = ITEMS.register("steeleaf_shovel", () -> new SteeleafShovelItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_PICKAXE = ITEMS.register("steeleaf_pickaxe", () -> new SteeleafPickItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_AXE = ITEMS.register("steeleaf_axe", () -> new SteeleafAxeItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> STEELEAF_HOE = ITEMS.register("steeleaf_hoe", () -> new SteeleafHoeItem(TwilightItemTier.STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> GOLDEN_MINOTAUR_AXE = ITEMS.register("gold_minotaur_axe", () -> new MinotaurAxeItem(Tiers.GOLD, defaultBuilder().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> DIAMOND_MINOTAUR_AXE = ITEMS.register("diamond_minotaur_axe", () -> new MinotaurAxeItem(Tiers.DIAMOND, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MAZEBREAKER_PICKAXE = ITEMS.register("mazebreaker_pickaxe", () -> new MazebreakerPickItem(Tiers.DIAMOND, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> TRANSFORMATION_POWDER = ITEMS.register("transformation_powder", () -> new TransformPowderItem(defaultBuilder()));
	public static final RegistryObject<Item> RAW_MEEF = ITEMS.register("raw_meef", () -> new Item(defaultBuilder().food(MEEF_RAW)));
	public static final RegistryObject<Item> COOKED_MEEF = ITEMS.register("cooked_meef", () -> new Item(defaultBuilder().food(MEEF_COOKED)));
	public static final RegistryObject<Item> MEEF_STROGANOFF = ITEMS.register("meef_stroganoff", () -> new BowlFoodItem(defaultBuilder().food(STROGANOFF).stacksTo(1)));
	public static final RegistryObject<Item> MAZE_WAFER = ITEMS.register("maze_wafer", () -> new Item(defaultBuilder().food(WAFER)));
	public static final RegistryObject<Item> ORE_MAGNET = ITEMS.register("ore_magnet", () -> new OreMagnetItem(defaultBuilder().durability(12)));
	public static final RegistryObject<Item> CRUMBLE_HORN = ITEMS.register("crumble_horn", () -> new CrumbleHornItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> PEACOCK_FEATHER_FAN = ITEMS.register("peacock_feather_fan", () -> new PeacockFanItem(defaultBuilder().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MOONWORM_QUEEN = ITEMS.register("moonworm_queen", () -> new MoonwormQueenItem(defaultBuilder().setNoRepair().durability(256).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> BRITTLE_FLASK = ITEMS.register("brittle_potion_flask", () -> new BrittleFlaskItem(defaultBuilder().stacksTo(1)));
	public static final RegistryObject<Item> GREATER_FLASK = ITEMS.register("greater_potion_flask", () -> new GreaterFlaskItem(defaultBuilder().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1)));
	public static final RegistryObject<Item> CHARM_OF_LIFE_1 = ITEMS.register("charm_of_life_1", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_LIFE_2 = ITEMS.register("charm_of_life_2", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_1 = ITEMS.register("charm_of_keeping_1", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_2 = ITEMS.register("charm_of_keeping_2", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_3 = ITEMS.register("charm_of_keeping_3", () -> new CuriosCharmItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TOWER_KEY = ITEMS.register("tower_key", () -> new Item(defaultBuilder().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BORER_ESSENCE = ITEMS.register("borer_essence", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> CARMINITE = ITEMS.register("carminite", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> EXPERIMENT_115 = ITEMS.register("experiment_115", () -> new Experiment115Item(TFBlocks.EXPERIMENT_115.get(), defaultBuilder().food(E115)));
	public static final RegistryObject<Item> ARMOR_SHARD = ITEMS.register("armor_shard", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ARMOR_SHARD_CLUSTER = ITEMS.register("armor_shard_cluster", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_INGOT = ITEMS.register("knightmetal_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_HELMET = ITEMS.register("knightmetal_helmet", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_CHESTPLATE = ITEMS.register("knightmetal_chestplate", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_LEGGINGS = ITEMS.register("knightmetal_leggings", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_BOOTS = ITEMS.register("knightmetal_boots", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_SWORD = ITEMS.register("knightmetal_sword", () -> new KnightmetalSwordItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_PICKAXE = ITEMS.register("knightmetal_pickaxe", () -> new KnightmetalPickItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_AXE = ITEMS.register("knightmetal_axe", () -> new KnightmetalAxeItem(TwilightItemTier.KNIGHTMETAL, defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_RING = ITEMS.register("knightmetal_ring", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> KNIGHTMETAL_SHIELD = ITEMS.register("knightmetal_shield", () -> new KnightmetalShieldItem(defaultBuilder().durability(1024)));
	public static final RegistryObject<Item> BLOCK_AND_CHAIN = ITEMS.register("block_and_chain", () -> new ChainBlockItem(defaultBuilder().durability(99)));
	public static final RegistryObject<Item> PHANTOM_HELMET = ITEMS.register("phantom_helmet", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PHANTOM_CHESTPLATE = ITEMS.register("phantom_chestplate", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ICE_BOMB = ITEMS.register("ice_bomb", () -> new IceBombItem(defaultBuilder().stacksTo(16)));
	public static final RegistryObject<Item> ARCTIC_FUR = ITEMS.register("arctic_fur", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlot.FEET, defaultBuilder()));
	public static final RegistryObject<Item> ALPHA_YETI_FUR = ITEMS.register("alpha_yeti_fur", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_HELMET = ITEMS.register("yeti_helmet", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_CHESTPLATE = ITEMS.register("yeti_chestplate", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_LEGGINGS = ITEMS.register("yeti_leggings", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_BOOTS = ITEMS.register("yeti_boots", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlot.FEET, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TRIPLE_BOW = ITEMS.register("triple_bow", () -> new TripleBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> SEEKER_BOW = ITEMS.register("seeker_bow", () -> new SeekerBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ICE_BOW = ITEMS.register("ice_bow", () -> new IceBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ENDER_BOW = ITEMS.register("ender_bow", () -> new EnderBowItem(defaultBuilder().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ICE_SWORD = ITEMS.register("ice_sword", () -> new IceSwordItem(TwilightItemTier.ICE, defaultBuilder()));
	public static final RegistryObject<Item> GLASS_SWORD = ITEMS.register("glass_sword", () -> new GlassSwordItem(TwilightItemTier.GLASS, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MAGIC_BEANS = ITEMS.register("magic_beans", () -> new MagicBeansItem(defaultBuilder()));
	public static final RegistryObject<Item> GIANT_PICKAXE = ITEMS.register("giant_pickaxe", () -> new GiantPickItem(TwilightItemTier.GIANT, defaultBuilder()));
	public static final RegistryObject<Item> GIANT_SWORD = ITEMS.register("giant_sword", () -> new GiantSwordItem(TwilightItemTier.GIANT, defaultBuilder()));
	public static final RegistryObject<Item> LAMP_OF_CINDERS = ITEMS.register("lamp_of_cinders", () -> new LampOfCindersItem(defaultBuilder().fireResistant().durability(1024).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CUBE_TALISMAN = ITEMS.register("cube_talisman", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CUBE_OF_ANNIHILATION = ITEMS.register("cube_of_annihilation", () -> new CubeOfAnnihilationItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MOON_DIAL = ITEMS.register("moon_dial", () -> new Item(defaultBuilder()));

	public static final RegistryObject<Item> MUSIC_DISC_RADIANCE = ITEMS.register("music_disc_radiance", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_RADIANCE, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_STEPS = ITEMS.register("music_disc_steps", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_STEPS, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_SUPERSTITIOUS = ITEMS.register("music_disc_superstitious", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_SUPERSTITIOUS, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_HOME = ITEMS.register("music_disc_home", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_HOME, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_WAYFARER = ITEMS.register("music_disc_wayfarer", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_WAYFARER, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_FINDINGS = ITEMS.register("music_disc_findings", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_FINDINGS, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_MAKER = ITEMS.register("music_disc_maker", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_MAKER, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_THREAD = ITEMS.register("music_disc_thread", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_THREAD, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MUSIC_DISC_MOTION = ITEMS.register("music_disc_motion", () -> new RecordItem(15, () -> TFSounds.MUSIC_DISC_MOTION, defaultBuilder().stacksTo(1).rarity(Rarity.RARE)));

	public static final RegistryObject<Item> NAGA_BANNER_PATTERN = ITEMS.register("naga_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_NAGA", "tf_naga", "tfn", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> LICH_BANNER_PATTERN = ITEMS.register("lich_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_LICH", "tf_lich", "tfl", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> MINOSHROOM_BANNER_PATTERN = ITEMS.register("minoshroom_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_MINOSHROOM", "tf_minoshroom", "tfm", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> HYDRA_BANNER_PATTERN = ITEMS.register("hydra_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_HYDRA", "tf_hydra", "tfh", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> KNIGHT_PHANTOM_BANNER_PATTERN = ITEMS.register("knight_phantom_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_PHANTOMS", "tf_phantoms", "tfp", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> UR_GHAST_BANNER_PATTERN = ITEMS.register("ur_ghast_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_UR_GHAST", "tf_ur_ghast", "tfg", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> ALPHA_YETI_BANNER_PATTERN = ITEMS.register("alpha_yeti_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_ALPHA_YETI", "tf_alpha_yeti", "tfy", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> SNOW_QUEEN_BANNER_PATTERN = ITEMS.register("snow_queen_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_SNOW_QUEEN", "tf_snow_queen", "tfq", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> QUEST_RAM_BANNER_PATTERN = ITEMS.register("quest_ram_banner_pattern", () -> new BannerPatternItem(BannerPattern.create("TF_QUEST_RAM", "tf_quest_ram", "tfr", true), defaultBuilder().stacksTo(1).rarity(TwilightForestMod.getRarity())));

	public static CreativeModeTab creativeTab = new CreativeModeTab(TwilightForestMod.ID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get());
		}
	};

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(creativeTab);
	}

	public static Item.Properties unstackable() {
		return defaultBuilder().stacksTo(1);
	}

	@OnlyIn(Dist.CLIENT)
	public static void addItemModelProperties() {
		ItemProperties.register(CUBE_OF_ANNIHILATION.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity, idk) ->
				CubeOfAnnihilationItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(TFItems.KNIGHTMETAL_SHIELD.get(), new ResourceLocation("blocking"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(MOON_DIAL.get(), new ResourceLocation("phase"), new ItemPropertyFunction() {
			@Override
			public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entityBase, int idk) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getFrame();

				if (world == null && entity != null) world = (ClientLevel) entity.level;

				return world == null ? 0.0F : (float) (world.dimensionType().natural() ? Mth.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
			}

			@OnlyIn(Dist.CLIENT)
			double rotation;
			@OnlyIn(Dist.CLIENT)
			double rota;
			@OnlyIn(Dist.CLIENT)
			long lastUpdateTick;

			@OnlyIn(Dist.CLIENT)
			private double wobble(Level world, double rotation) {
				if (world.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = world.getGameTime();
					double delta = rotation - this.rotation;
					delta = Mth.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
					this.rota += delta * 0.1D;
					this.rota *= 0.9D;
					this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
				}
				return this.rotation;
			}
		});

		ItemProperties.register(MOONWORM_QUEEN.get(), TwilightForestMod.prefix("alt"), (stack, world, entity, idk) -> {
			if (entity != null && entity.getUseItem() == stack) {
				int useTime = stack.getUseDuration() - entity.getUseItemRemainingTicks();
				if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}
			return 0;
		});

		ItemProperties.register(TFItems.ENDER_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ENDER_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ICE_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if(entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.ICE_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.SEEKER_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.SEEKER_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F; });

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(ORE_MAGNET.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}});

		ItemProperties.register(ORE_MAGNET.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(BLOCK_AND_CHAIN.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity, idk) ->
				ChainBlockItem.getThrownUuid(stack) != null ? 1 : 0);

		ItemProperties.register(EXPERIMENT_115.get(), Experiment115Item.THINK, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("think") ? 1 : 0);

		ItemProperties.register(EXPERIMENT_115.get(), Experiment115Item.FULL, (stack, world, entity, idk) ->
				stack.hasTag() && stack.getTag().contains("full") ? 1 : 0);

		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("breakage"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Breakage"));

		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));

		ItemProperties.register(TFItems.GREATER_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, world, entity, i) ->
				stack.getOrCreateTag().getInt("Uses"));
	}
}
