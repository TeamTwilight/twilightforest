package twilightforest.init;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.entity.TwilightBoat;
import twilightforest.enums.TwilightArmorMaterial;
import twilightforest.item.*;
import twilightforest.util.TwilightItemTier;

public class TFItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);

	public static final RegistryObject<Item> NAGA_SCALE = ITEMS.register("naga_scale", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NAGA_CHESTPLATE = ITEMS.register("naga_chestplate", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NAGA_LEGGINGS = ITEMS.register("naga_leggings", () -> new NagaArmorItem(TwilightArmorMaterial.ARMOR_NAGA, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TWILIGHT_SCEPTER = ITEMS.register("twilight_scepter", () -> new TwilightWandItem(new Item.Properties().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LIFEDRAIN_SCEPTER = ITEMS.register("lifedrain_scepter", () -> new LifedrainScepterItem(new Item.Properties().durability(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZOMBIE_SCEPTER = ITEMS.register("zombie_scepter", () -> new ZombieWandItem(new Item.Properties().durability(9).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FORTIFICATION_SCEPTER = ITEMS.register("fortification_scepter", () -> new FortificationWandItem(new Item.Properties().durability(9).rarity(Rarity.UNCOMMON)));
	//items.register("Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final RegistryObject<Item> ORE_METER = ITEMS.register("ore_meter", () -> new OreMeterItem(new Item.Properties()));
	public static final RegistryObject<Item> FILLED_MAGIC_MAP = ITEMS.register("filled_magic_map", () -> new MagicMapItem(new Item.Properties()));
	public static final RegistryObject<Item> FILLED_MAZE_MAP = ITEMS.register("filled_maze_map", () -> new MazeMapItem(false, new Item.Properties()));
	public static final RegistryObject<Item> FILLED_ORE_MAP = ITEMS.register("filled_ore_map", () -> new MazeMapItem(true, new Item.Properties()));
	public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MAGIC_MAP_FOCUS = ITEMS.register("magic_map_focus", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MAZE_MAP_FOCUS = ITEMS.register("maze_map_focus", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MAGIC_MAP = ITEMS.register("magic_map", () -> new EmptyMagicMapItem(new Item.Properties()));
	public static final RegistryObject<Item> MAZE_MAP = ITEMS.register("maze_map", () -> new EmptyMazeMapItem(false, new Item.Properties()));
	public static final RegistryObject<Item> ORE_MAP = ITEMS.register("ore_map", () -> new EmptyMazeMapItem(true, new Item.Properties()));
	public static final RegistryObject<Item> LIVEROOT = ITEMS.register("liveroot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAW_IRONWOOD = ITEMS.register("raw_ironwood", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_INGOT = ITEMS.register("ironwood_ingot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_HELMET = ITEMS.register("ironwood_helmet", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_CHESTPLATE = ITEMS.register("ironwood_chestplate", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_LEGGINGS = ITEMS.register("ironwood_leggings", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_BOOTS = ITEMS.register("ironwood_boots", () -> new IronwoodArmorItem(TwilightArmorMaterial.ARMOR_IRONWOOD, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_SWORD = ITEMS.register("ironwood_sword", () -> new SwordItem(TwilightItemTier.IRONWOOD, 3, -2.4F, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_SHOVEL = ITEMS.register("ironwood_shovel", () -> new ShovelItem(TwilightItemTier.IRONWOOD, 1.5F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_PICKAXE = ITEMS.register("ironwood_pickaxe", () -> new PickaxeItem(TwilightItemTier.IRONWOOD, 1, -2.8F, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_AXE = ITEMS.register("ironwood_axe", () -> new AxeItem(TwilightItemTier.IRONWOOD, 6.0F, -3.1F, new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_HOE = ITEMS.register("ironwood_hoe", () -> new HoeItem(TwilightItemTier.IRONWOOD, -2, -1.0F, new Item.Properties()));
	public static final RegistryObject<Item> TORCHBERRIES = ITEMS.register("torchberries", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.GLOWING, 100, 0), 0.75F).build())));
	public static final RegistryObject<Item> RAW_VENISON = ITEMS.register("raw_venison", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> HYDRA_CHOP = ITEMS.register("hydra_chop", () -> new HydraChopItem(new Item.Properties().fireResistant().food(new FoodProperties.Builder().nutrition(18).saturationMod(2.0F).meat().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build()).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_BLOOD = ITEMS.register("fiery_blood", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_TEARS = ITEMS.register("fiery_tears", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_INGOT = ITEMS.register("fiery_ingot", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_HELMET = ITEMS.register("fiery_helmet", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_CHESTPLATE = ITEMS.register("fiery_chestplate", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_LEGGINGS = ITEMS.register("fiery_leggings", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_BOOTS = ITEMS.register("fiery_boots", () -> new FieryArmorItem(TwilightArmorMaterial.ARMOR_FIERY, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_SWORD = ITEMS.register("fiery_sword", () -> new FierySwordItem(TwilightItemTier.FIERY, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIERY_PICKAXE = ITEMS.register("fiery_pickaxe", () -> new FieryPickItem(TwilightItemTier.FIERY, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> STEELEAF_INGOT = ITEMS.register("steeleaf_ingot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_HELMET = ITEMS.register("steeleaf_helmet", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_CHESTPLATE = ITEMS.register("steeleaf_chestplate", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_LEGGINGS = ITEMS.register("steeleaf_leggings", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_BOOTS = ITEMS.register("steeleaf_boots", () -> new SteeleafArmorItem(TwilightArmorMaterial.ARMOR_STEELEAF, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_SWORD = ITEMS.register("steeleaf_sword", () -> new SwordItem(TwilightItemTier.STEELEAF, 3, -2.4F, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_SHOVEL = ITEMS.register("steeleaf_shovel", () -> new ShovelItem(TwilightItemTier.STEELEAF, 1.5F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_PICKAXE = ITEMS.register("steeleaf_pickaxe", () -> new PickaxeItem(TwilightItemTier.STEELEAF, 1, -2.8F, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_AXE = ITEMS.register("steeleaf_axe", () -> new AxeItem(TwilightItemTier.STEELEAF, 6.0F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> STEELEAF_HOE = ITEMS.register("steeleaf_hoe", () -> new HoeItem(TwilightItemTier.STEELEAF, -3, -0.5F, new Item.Properties()));
	public static final RegistryObject<Item> GOLDEN_MINOTAUR_AXE = ITEMS.register("gold_minotaur_axe", () -> new MinotaurAxeItem(Tiers.GOLD, new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> DIAMOND_MINOTAUR_AXE = ITEMS.register("diamond_minotaur_axe", () -> new MinotaurAxeItem(Tiers.DIAMOND, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MAZEBREAKER_PICKAXE = ITEMS.register("mazebreaker_pickaxe", () -> new MazebreakerPickItem(Tiers.DIAMOND, new Item.Properties().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RED_THREAD = ITEMS.register("red_thread", () -> new BlockItem(TFBlocks.RED_THREAD.get(), new Item.Properties()));
	public static final RegistryObject<Item> TRANSFORMATION_POWDER = ITEMS.register("transformation_powder", () -> new TransformPowderItem(new Item.Properties()));
	public static final RegistryObject<Item> RAW_MEEF = ITEMS.register("raw_meef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_MEEF = ITEMS.register("cooked_meef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build())));
	public static final RegistryObject<Item> MEEF_STROGANOFF = ITEMS.register("meef_stroganoff", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).fireResistant().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).alwaysEat().build())));
	public static final RegistryObject<Item> MAZE_WAFER = ITEMS.register("maze_wafer", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build())));
	public static final RegistryObject<Item> ORE_MAGNET = ITEMS.register("ore_magnet", () -> new OreMagnetItem(new Item.Properties().durability(64)));
	public static final RegistryObject<Item> CRUMBLE_HORN = ITEMS.register("crumble_horn", () -> new CrumbleHornItem(new Item.Properties().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> PEACOCK_FEATHER_FAN = ITEMS.register("peacock_feather_fan", () -> new PeacockFanItem(new Item.Properties().durability(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MOONWORM_QUEEN = ITEMS.register("moonworm_queen", () -> new MoonwormQueenItem(new Item.Properties().setNoRepair().durability(256).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> BRITTLE_FLASK = ITEMS.register("brittle_potion_flask", () -> new BrittleFlaskItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GREATER_FLASK = ITEMS.register("greater_potion_flask", () -> new GreaterFlaskItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> CHARM_OF_LIFE_1 = ITEMS.register("charm_of_life_1", () -> new CuriosCharmItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_LIFE_2 = ITEMS.register("charm_of_life_2", () -> new CuriosCharmItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_1 = ITEMS.register("charm_of_keeping_1", () -> new CuriosCharmItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_2 = ITEMS.register("charm_of_keeping_2", () -> new CuriosCharmItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHARM_OF_KEEPING_3 = ITEMS.register("charm_of_keeping_3", () -> new CuriosCharmItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TOWER_KEY = ITEMS.register("tower_key", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BORER_ESSENCE = ITEMS.register("borer_essence", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CARMINITE = ITEMS.register("carminite", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> EXPERIMENT_115 = ITEMS.register("experiment_115", () -> new Experiment115Item(TFBlocks.EXPERIMENT_115.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())));
	public static final RegistryObject<Item> ARMOR_SHARD = ITEMS.register("armor_shard", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ARMOR_SHARD_CLUSTER = ITEMS.register("armor_shard_cluster", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_INGOT = ITEMS.register("knightmetal_ingot", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_HELMET = ITEMS.register("knightmetal_helmet", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_CHESTPLATE = ITEMS.register("knightmetal_chestplate", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_LEGGINGS = ITEMS.register("knightmetal_leggings", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_BOOTS = ITEMS.register("knightmetal_boots", () -> new KnightmetalArmorItem(TwilightArmorMaterial.ARMOR_KNIGHTLY, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_SWORD = ITEMS.register("knightmetal_sword", () -> new KnightmetalSwordItem(TwilightItemTier.KNIGHTMETAL, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_PICKAXE = ITEMS.register("knightmetal_pickaxe", () -> new KnightmetalPickItem(TwilightItemTier.KNIGHTMETAL, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_AXE = ITEMS.register("knightmetal_axe", () -> new KnightmetalAxeItem(TwilightItemTier.KNIGHTMETAL, new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_RING = ITEMS.register("knightmetal_ring", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_SHIELD = ITEMS.register("knightmetal_shield", () -> new KnightmetalShieldItem(new Item.Properties().durability(1024)));
	public static final RegistryObject<Item> BLOCK_AND_CHAIN = ITEMS.register("block_and_chain", () -> new ChainBlockItem(new Item.Properties().durability(99)));
	public static final RegistryObject<Item> PHANTOM_HELMET = ITEMS.register("phantom_helmet", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PHANTOM_CHESTPLATE = ITEMS.register("phantom_chestplate", () -> new PhantomArmorItem(TwilightArmorMaterial.ARMOR_PHANTOM, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ICE_BOMB = ITEMS.register("ice_bomb", () -> new IceBombItem(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> ARCTIC_FUR = ITEMS.register("arctic_fur", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new ArcticArmorItem(TwilightArmorMaterial.ARMOR_ARCTIC, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> ALPHA_YETI_FUR = ITEMS.register("alpha_yeti_fur", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_HELMET = ITEMS.register("yeti_helmet", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_CHESTPLATE = ITEMS.register("yeti_chestplate", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_LEGGINGS = ITEMS.register("yeti_leggings", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> YETI_BOOTS = ITEMS.register("yeti_boots", () -> new YetiArmorItem(TwilightArmorMaterial.ARMOR_YETI, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TRIPLE_BOW = ITEMS.register("triple_bow", () -> new TripleBowItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> SEEKER_BOW = ITEMS.register("seeker_bow", () -> new SeekerBowItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ICE_BOW = ITEMS.register("ice_bow", () -> new IceBowItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ENDER_BOW = ITEMS.register("ender_bow", () -> new EnderBowItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(384)));
	public static final RegistryObject<Item> ICE_SWORD = ITEMS.register("ice_sword", () -> new IceSwordItem(TwilightItemTier.ICE, new Item.Properties()));
	public static final RegistryObject<Item> GLASS_SWORD = ITEMS.register("glass_sword", () -> new GlassSwordItem(TwilightItemTier.GLASS, new Item.Properties().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MAGIC_BEANS = ITEMS.register("magic_beans", () -> new MagicBeansItem(new Item.Properties()));
	public static final RegistryObject<Item> GIANT_PICKAXE = ITEMS.register("giant_pickaxe", () -> new GiantPickItem(TwilightItemTier.GIANT, new Item.Properties()));
	public static final RegistryObject<Item> GIANT_SWORD = ITEMS.register("giant_sword", () -> new GiantSwordItem(TwilightItemTier.GIANT, new Item.Properties()));
	public static final RegistryObject<Item> LAMP_OF_CINDERS = ITEMS.register("lamp_of_cinders", () -> new LampOfCindersItem(new Item.Properties().fireResistant().durability(1024).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CUBE_TALISMAN = ITEMS.register("cube_talisman", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CUBE_OF_ANNIHILATION = ITEMS.register("cube_of_annihilation", () -> new CubeOfAnnihilationItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MOON_DIAL = ITEMS.register("moon_dial", () -> new MoonDialItem(new Item.Properties()));

	public static final RegistryObject<Item> TWILIGHT_OAK_BOAT = ITEMS.register("twilight_oak_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.TWILIGHT_OAK, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TWILIGHT_OAK_CHEST_BOAT = ITEMS.register("twilight_oak_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.TWILIGHT_OAK, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CANOPY_BOAT = ITEMS.register("canopy_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.CANOPY, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CANOPY_CHEST_BOAT = ITEMS.register("canopy_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.CANOPY, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MANGROVE_BOAT = ITEMS.register("mangrove_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.MANGROVE, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MANGROVE_CHEST_BOAT = ITEMS.register("mangrove_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.MANGROVE, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DARKWOOD_BOAT = ITEMS.register("dark_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.DARKWOOD, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> DARKWOOD_CHEST_BOAT = ITEMS.register("dark_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.DARKWOOD, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TIME_BOAT = ITEMS.register("time_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.TIME, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TIME_CHEST_BOAT = ITEMS.register("time_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.TIME, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TRANSFORMATION_BOAT = ITEMS.register("transformation_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.TRANSFORMATION, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TRANSFORMATION_CHEST_BOAT = ITEMS.register("transformation_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.TRANSFORMATION, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MINING_BOAT = ITEMS.register("mining_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.MINING, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MINING_CHEST_BOAT = ITEMS.register("mining_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.MINING, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SORTING_BOAT = ITEMS.register("sorting_boat", () -> new TwilightBoatItem(false, TwilightBoat.Type.SORTING, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SORTING_CHEST_BOAT = ITEMS.register("sorting_chest_boat", () -> new TwilightBoatItem(true, TwilightBoat.Type.SORTING, new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> MUSIC_DISC_RADIANCE = ITEMS.register("music_disc_radiance", () -> new RecordItem(15, TFSounds.MUSIC_DISC_RADIANCE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 123 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_STEPS = ITEMS.register("music_disc_steps", () -> new RecordItem(15, TFSounds.MUSIC_DISC_STEPS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 195 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_SUPERSTITIOUS = ITEMS.register("music_disc_superstitious", () -> new RecordItem(15, TFSounds.MUSIC_DISC_SUPERSTITIOUS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 192 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_HOME = ITEMS.register("music_disc_home", () -> new RecordItem(15, TFSounds.MUSIC_DISC_HOME, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 215 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_WAYFARER = ITEMS.register("music_disc_wayfarer", () -> new RecordItem(15, TFSounds.MUSIC_DISC_WAYFARER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 173 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_FINDINGS = ITEMS.register("music_disc_findings", () -> new RecordItem(15, TFSounds.MUSIC_DISC_FINDINGS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 196 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_MAKER = ITEMS.register("music_disc_maker", () -> new RecordItem(15, TFSounds.MUSIC_DISC_MAKER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 207 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_THREAD = ITEMS.register("music_disc_thread", () -> new RecordItem(15, TFSounds.MUSIC_DISC_THREAD, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 201 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_MOTION = ITEMS.register("music_disc_motion", () -> new RecordItem(15, TFSounds.MUSIC_DISC_MOTION, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 169 * 20));

	public static final RegistryObject<Item> NAGA_BANNER_PATTERN = ITEMS.register("naga_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.NAGA_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> LICH_BANNER_PATTERN = ITEMS.register("lich_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.LICH_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> MINOSHROOM_BANNER_PATTERN = ITEMS.register("minoshroom_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.MINOSHROOM_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> HYDRA_BANNER_PATTERN = ITEMS.register("hydra_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.HYDRA_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> KNIGHT_PHANTOM_BANNER_PATTERN = ITEMS.register("knight_phantom_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.KNIGHT_PHANTOM_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> UR_GHAST_BANNER_PATTERN = ITEMS.register("ur_ghast_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.UR_GHAST_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> ALPHA_YETI_BANNER_PATTERN = ITEMS.register("alpha_yeti_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.ALPHA_YETI_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> SNOW_QUEEN_BANNER_PATTERN = ITEMS.register("snow_queen_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.SNOW_QUEEN_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));
	public static final RegistryObject<Item> QUEST_RAM_BANNER_PATTERN = ITEMS.register("quest_ram_banner_pattern", () -> new BannerPatternItem(CustomTagGenerator.BannerPatternTagGenerator.QUEST_RAM_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(TwilightForestMod.getRarity())));

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
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.ENDER_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ICE_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.ICE_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.SEEKER_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.SEEKER_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), new ResourceLocation("pulling"), (stack, world, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(ORE_MAGNET.get(), new ResourceLocation("pull"), (stack, world, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}
		});

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
