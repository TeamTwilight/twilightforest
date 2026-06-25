package twilightforest.init;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.enums.extensions.TFRarityEnumExtension;
import twilightforest.item.*;
import twilightforest.item.food.TFConsumables;
import twilightforest.item.food.TFFoods;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.tags.TFBannerPatternTags;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class TFItems {

	@Autowired
	private static TFRarityEnumExtension tfRarityEnumExtension;

	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TwilightForestMod.ID);

	public static final DeferredItem<Item> NAGA_SCALE = register("naga_scale", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> NAGA_CHESTPLATE = register("naga_chestplate", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.NAGA, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> NAGA_LEGGINGS = register("naga_leggings", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.NAGA, ArmorType.LEGGINGS).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> TWILIGHT_SCEPTER = register("twilight_scepter", TwilightWandItem::new, () -> new Item.Properties().durability(99).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> LIFEDRAIN_SCEPTER = register("lifedrain_scepter", LifedrainScepterItem::new, () -> new Item.Properties().durability(99).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> ZOMBIE_SCEPTER = register("zombie_scepter", ZombieWandItem::new, () -> new Item.Properties().durability(9).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FORTIFICATION_SCEPTER = register("fortification_scepter", FortificationWandItem::new, () -> new Item.Properties().durability(9).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> EXANIMATE_ESSENCE = register("exanimate_essence", ExanimateEssenceItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16));
	public static final DeferredItem<Item> WROUGHT_IRON_BAR = register("wrought_iron_bar", Item::new, Item.Properties::new);
	//items.register("Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final DeferredItem<Item> MAGIC_PAINTING = register("magic_painting", MagicPaintingItem::new, Item.Properties::new);
	public static final DeferredItem<Item> ORE_METER = register("ore_meter", OreMeterItem::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FILLED_MAGIC_MAP = register("filled_magic_map", MagicMapItem::new, Item.Properties::new);
	public static final DeferredItem<Item> FILLED_MAZE_MAP = register("filled_maze_map", properties -> new MazeMapItem(false, properties), Item.Properties::new);
	public static final DeferredItem<Item> FILLED_ORE_MAP = register("filled_ore_map", properties -> new MazeMapItem(true, properties), Item.Properties::new);
	public static final DeferredItem<Item> RAVEN_FEATHER = register("raven_feather", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> MAGIC_MAP_FOCUS = register("magic_map_focus", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> MAZE_MAP_FOCUS = register("maze_map_focus", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> MAGIC_MAP = register("magic_map", EmptyMagicMapItem::new, Item.Properties::new);
	public static final DeferredItem<Item> MAZE_MAP = register("maze_map", properties -> new EmptyMazeMapItem(false, properties), Item.Properties::new);
	public static final DeferredItem<Item> ORE_MAP = register("ore_map", properties -> new EmptyMazeMapItem(true, properties), Item.Properties::new);
	public static final DeferredItem<Item> LIVEROOT = register("liveroot", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> RAW_IRONWOOD = register("raw_ironwood", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> IRONWOOD_INGOT = register("ironwood_ingot", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> IRONWOOD_HELMET = register("ironwood_helmet", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.IRONWOOD, ArmorType.HELMET));
	public static final DeferredItem<Item> IRONWOOD_CHESTPLATE = register("ironwood_chestplate", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.IRONWOOD, ArmorType.CHESTPLATE));
	public static final DeferredItem<Item> IRONWOOD_LEGGINGS = register("ironwood_leggings", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.IRONWOOD, ArmorType.LEGGINGS));
	public static final DeferredItem<Item> IRONWOOD_BOOTS = register("ironwood_boots", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.IRONWOOD, ArmorType.BOOTS));
	public static final DeferredItem<Item> IRONWOOD_SWORD = register("ironwood_sword", Item::new, () -> new Item.Properties().sword(TFToolMaterials.IRONWOOD, 3.0F, -2.4F));
	public static final DeferredItem<Item> IRONWOOD_SHOVEL = register("ironwood_shovel", properties -> new ShovelItem(TFToolMaterials.IRONWOOD, 1.5F, -3.0F, properties), Item.Properties::new);
	public static final DeferredItem<Item> IRONWOOD_PICKAXE = register("ironwood_pickaxe", Item::new, () -> new Item.Properties().pickaxe(TFToolMaterials.IRONWOOD, 1.0F, -2.8F));
	public static final DeferredItem<Item> IRONWOOD_AXE = register("ironwood_axe", properties -> new AxeItem(TFToolMaterials.IRONWOOD, 6.0F, -3.1F, properties), Item.Properties::new);
	public static final DeferredItem<Item> IRONWOOD_HOE = register("ironwood_hoe", properties -> new HoeItem(TFToolMaterials.IRONWOOD, -2, -1.0F, properties), Item.Properties::new);
	public static final DeferredItem<Item> TORCHBERRIES = register("torchberries", Item::new, () -> new Item.Properties().food(TFFoods.TORCHBERRIES, TFConsumables.TORCHBERRIES));
	public static final DeferredItem<Item> RAW_VENISON = register("raw_venison", Item::new, () -> new Item.Properties().food(TFFoods.RAW_VENISON));
	public static final DeferredItem<Item> COOKED_VENISON = register("cooked_venison", Item::new, () -> new Item.Properties().food(TFFoods.VENISON_STEAK));
	public static final DeferredItem<Item> HYDRA_CHOP = register("hydra_chop", HydraChopItem::new, () -> new Item.Properties().fireResistant().food(TFFoods.HYDRA_CHOP, TFConsumables.HYDRA_CHOP).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> TANNIN = register("tannin", Item::new, () -> new Item.Properties().craftRemainder(Items.GLASS_BOTTLE));
	public static final DeferredItem<Item> FIERY_BLOOD = register("fiery_blood", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_TEARS = register("fiery_tears", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_INGOT = register("fiery_ingot", Item::new, () -> new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_HELMET = register("fiery_helmet", FieryArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.FIERY, ArmorType.HELMET).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_CHESTPLATE = register("fiery_chestplate", FieryArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.FIERY, ArmorType.CHESTPLATE).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_LEGGINGS = register("fiery_leggings", FieryArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.FIERY, ArmorType.LEGGINGS).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_BOOTS = register("fiery_boots", FieryArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.FIERY, ArmorType.BOOTS).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_SWORD = register("fiery_sword", FierySwordItem::new, () -> new Item.Properties().sword(TFToolMaterials.FIERY, 3.0F, -2.4F).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FIERY_PICKAXE = register("fiery_pickaxe", FieryPickItem::new, () -> new Item.Properties().pickaxe(TFToolMaterials.FIERY, 1.0F, -2.8F).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> TRAVELLERS_GOGGLES = register("travellers_goggles", properties -> new TravellersGogglesItem(3, properties), () -> TravellersArmorItem.gogglesProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.HELMET)));
	public static final DeferredItem<Item> TRAVELLERS_VEST = register("travellers_vest", properties -> new TravellersArmorItem(3, properties), () -> TravellersArmorItem.chestProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.CHESTPLATE)));
	public static final DeferredItem<Item> TRAVELLERS_GLOVES = register("travellers_gloves", properties -> new TravellersArmorItem(0, properties), () -> TravellersArmorItem.glovesProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.CHESTPLATE).durability(0).stacksTo(1)));
	public static final DeferredItem<Item> TRAVELLERS_WINGS = register("travellers_wings", properties -> new TravellersArmorBeltItem(3, properties), () -> TravellersArmorItem.wingsProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.LEGGINGS)));
	public static final DeferredItem<Item> TRAVELLERS_BELT = register("travellers_belt", properties -> new TravellersArmorBeltItem(0, properties), () -> TravellersArmorBeltItem.beltProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.LEGGINGS).durability(0).stacksTo(1)));
	public static final DeferredItem<Item> TRAVELLERS_BOOTS = register("travellers_boots", properties -> new TravellersArmorItem(3, properties), () -> TravellersArmorItem.bootsProperties(new Item.Properties().humanoidArmor(TFArmorMaterials.TRAVELLERS_GEAR, ArmorType.BOOTS)));
	public static final DeferredItem<Item> STEELEAF_INGOT = register("steeleaf_ingot", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> STEELEAF_HELMET = register("steeleaf_helmet", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.STEELEAF, ArmorType.HELMET));
	public static final DeferredItem<Item> STEELEAF_CHESTPLATE = register("steeleaf_chestplate", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.STEELEAF, ArmorType.CHESTPLATE));
	public static final DeferredItem<Item> STEELEAF_LEGGINGS = register("steeleaf_leggings", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.STEELEAF, ArmorType.LEGGINGS));
	public static final DeferredItem<Item> STEELEAF_BOOTS = register("steeleaf_boots", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.STEELEAF, ArmorType.BOOTS));
	public static final DeferredItem<Item> STEELEAF_SWORD = register("steeleaf_sword", Item::new, () -> new Item.Properties().sword(TFToolMaterials.KNIGHTMETAL, 3.0F, -2.4F));
	public static final DeferredItem<Item> STEELEAF_SHOVEL = register("steeleaf_shovel", properties -> new ShovelItem(TFToolMaterials.STEELEAF, 1.5F, -3.0F, properties), Item.Properties::new);
	public static final DeferredItem<Item> STEELEAF_PICKAXE = register("steeleaf_pickaxe", Item::new, () -> new Item.Properties().pickaxe(TFToolMaterials.STEELEAF, 1.0F, -2.8F));
	public static final DeferredItem<Item> STEELEAF_AXE = register("steeleaf_axe", properties -> new AxeItem(TFToolMaterials.STEELEAF, 6.0F, -3.0F, properties), Item.Properties::new);
	public static final DeferredItem<Item> STEELEAF_HOE = register("steeleaf_hoe", properties -> new HoeItem(TFToolMaterials.STEELEAF, -3.0F, -0.5F, properties), Item.Properties::new);
	public static final DeferredItem<Item> GOLDEN_MINOTAUR_AXE = register("gold_minotaur_axe", properties -> new MinotaurAxeItem(ToolMaterial.GOLD, 6.0F, -3.2F, properties), Item.Properties::new);
	public static final DeferredItem<Item> DIAMOND_MINOTAUR_AXE = register("diamond_minotaur_axe", properties -> new MinotaurAxeItem(ToolMaterial.DIAMOND, 6.0F, -3.2F, properties), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> MAZEBREAKER_PICKAXE = register("mazebreaker_pickaxe", MazebreakerPickItem::new, () -> new Item.Properties().pickaxe(ToolMaterial.DIAMOND, 1.0F, -2.8F).component(DataComponents.REPAIRABLE, new Repairable(HolderSet.empty())).rarity(Rarity.RARE));
	public static final DeferredItem<Item> TRANSFORMATION_POWDER = register("transformation_powder", TransformPowderItem::new, Item.Properties::new);
	public static final DeferredItem<Item> RAW_MEEF = register("raw_meef", Item::new, () -> new Item.Properties().food(TFFoods.RAW_MEEF));
	public static final DeferredItem<Item> COOKED_MEEF = register("cooked_meef", Item::new, () -> new Item.Properties().food(TFFoods.MEEF_STEAK));
	public static final DeferredItem<Item> MEEF_STROGANOFF = register("meef_stroganoff", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant().food(TFFoods.MEEF_STROGANOFF).usingConvertsTo(Items.BOWL));
	public static final DeferredItem<Item> MAZE_WAFER = register("maze_wafer", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
	public static final DeferredItem<Item> MAZE_SLIME_BALL = register("maze_slime_ball", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> ORE_MAGNET = register("ore_magnet", OreMagnetItem::new, () -> new Item.Properties().durability(64));
	public static final DeferredItem<Item> CRUMBLE_HORN = register("crumble_horn", CrumbleHornItem::new, () -> new Item.Properties().durability(1024).rarity(Rarity.RARE));
	public static final DeferredItem<Item> PEACOCK_FEATHER_FAN = register("peacock_feather_fan", PeacockFanItem::new, () -> new Item.Properties().durability(1024).rarity(Rarity.RARE));
	public static final DeferredItem<Item> MOONWORM_QUEEN = register("moonworm_queen", MoonwormQueenItem::new, () -> new Item.Properties().durability(256).rarity(Rarity.RARE));
	public static final DeferredItem<Item> BRITTLE_FLASK = register("brittle_potion_flask", PotionFlaskItem::new, () -> new Item.Properties().component(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY));
	public static final DeferredItem<Item> GREATER_FLASK = register("greater_potion_flask", PotionFlaskItem::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant().component(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY_UNBREAKABLE));
	public static final DeferredItem<Item> CHARM_OF_LIFE_1 = register("charm_of_life_1", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CHARM_OF_LIFE_2 = register("charm_of_life_2", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CHARM_OF_KEEPING_1 = register("charm_of_keeping_1", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CHARM_OF_KEEPING_2 = register("charm_of_keeping_2", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CHARM_OF_KEEPING_3 = register("charm_of_keeping_3", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> IRON_BERRY = register("iron_berry", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> GOLD_BERRY = register("gold_berry", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> COPPER_BERRY = register("copper_berry", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> ESSENCE_BERRY = register("essence_berry", EssenceBerryItem::new, Item.Properties::new);
	public static final DeferredItem<Item> TOWER_KEY = register("tower_key", Item::new, () -> new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> BORER_ESSENCE = register("borer_essence", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> CARMINITE = register("carminite", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> COPPER_NUGGET = register("copper_nugget", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> EXPERIMENT_115 = register("experiment_115", properties -> new Experiment115Item(TFBlocks.EXPERIMENT_115.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().food(TFFoods.EXPERIMENT_115));
	public static final DeferredItem<Item> ROPE = register("rope", properties -> new RopeItem(TFBlocks.ROPE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> MASON_JAR = register("mason_jar", properties -> new JarItem.MasonJarItem(TFBlocks.MASON_JAR.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> FIREFLY_JAR = register("firefly_jar", properties -> new JarItem(TFBlocks.FIREFLY_JAR.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> CICADA_JAR = register("cicada_jar", properties -> new JarItem(TFBlocks.CICADA_JAR.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> ARMOR_SHARD = register("armor_shard", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> ARMOR_SHARD_CLUSTER = register("armor_shard_cluster", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> KNIGHTMETAL_INGOT = register("knightmetal_ingot", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> KNIGHTMETAL_HELMET = register("knightmetal_helmet", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.KNIGHTMETAL, ArmorType.HELMET));
	public static final DeferredItem<Item> KNIGHTMETAL_CHESTPLATE = register("knightmetal_chestplate", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.KNIGHTMETAL, ArmorType.CHESTPLATE));
	public static final DeferredItem<Item> KNIGHTMETAL_LEGGINGS = register("knightmetal_leggings", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.KNIGHTMETAL, ArmorType.LEGGINGS));
	public static final DeferredItem<Item> KNIGHTMETAL_BOOTS = register("knightmetal_boots", Item::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.KNIGHTMETAL, ArmorType.BOOTS));
	public static final DeferredItem<Item> KNIGHTMETAL_SWORD = register("knightmetal_sword", KnightmetalToolItem::new, () -> new Item.Properties().sword(TFToolMaterials.KNIGHTMETAL, 3.0F, -2.4F));
	public static final DeferredItem<Item> KNIGHTMETAL_PICKAXE = register("knightmetal_pickaxe", KnightmetalToolItem::new, () -> new Item.Properties().pickaxe(TFToolMaterials.KNIGHTMETAL, 1.0F, -2.8F));
	public static final DeferredItem<Item> KNIGHTMETAL_AXE = register("knightmetal_axe", properties -> new KnightmetalAxeItem(TFToolMaterials.KNIGHTMETAL, 6.0F, -3.2F, properties), Item.Properties::new);
	public static final DeferredItem<Item> KNIGHTMETAL_RING = register("knightmetal_ring", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> KNIGHTMETAL_SHIELD = register("knightmetal_shield", Item::new, () -> new Item.Properties()
		.durability(1024)
		.equippableUnswappable(EquipmentSlot.OFFHAND)
		.delayedComponent(
			DataComponents.BLOCKS_ATTACKS,
			context -> new BlocksAttacks(
				0.25F,
				1.0F,
				List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
				new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
				Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
				Optional.of(SoundEvents.SHIELD_BLOCK),
				Optional.of(SoundEvents.SHIELD_BREAK)
			)
		)
		.component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));
	public static final DeferredItem<Item> BLOCK_AND_CHAIN = register("block_and_chain", ChainBlockItem::new, () -> new Item.Properties().durability(99));
	public static final DeferredItem<Item> PHANTOM_HELMET = register("phantom_helmet", PhantomArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.PHANTOM, ArmorType.HELMET).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> PHANTOM_CHESTPLATE = register("phantom_chestplate", PhantomArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.PHANTOM, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> ICE_BOMB = register("ice_bomb", IceBombItem::new, () -> new Item.Properties().stacksTo(16));
	public static final DeferredItem<Item> ARCTIC_FUR = register("arctic_fur", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> ARCTIC_HELMET = register("arctic_helmet", ArcticArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.ARCTIC, ArmorType.HELMET));
	public static final DeferredItem<Item> ARCTIC_CHESTPLATE = register("arctic_chestplate", ArcticArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.ARCTIC, ArmorType.CHESTPLATE));
	public static final DeferredItem<Item> ARCTIC_LEGGINGS = register("arctic_leggings", ArcticArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.ARCTIC, ArmorType.LEGGINGS));
	public static final DeferredItem<Item> ARCTIC_BOOTS = register("arctic_boots", ArcticArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.ARCTIC, ArmorType.BOOTS));
	public static final DeferredItem<Item> ALPHA_YETI_FUR = register("alpha_yeti_fur", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> YETI_HELMET = register("yeti_helmet", YetiArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.YETI, ArmorType.HELMET).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> YETI_CHESTPLATE = register("yeti_chestplate", YetiArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.YETI, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> YETI_LEGGINGS = register("yeti_leggings", YetiArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.YETI, ArmorType.LEGGINGS).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> YETI_BOOTS = register("yeti_boots", YetiArmorItem::new, () -> new Item.Properties().humanoidArmor(TFArmorMaterials.YETI, ArmorType.BOOTS).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> TRIPLE_BOW = register("triple_bow", TripleBowItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).durability(384));
	public static final DeferredItem<Item> SEEKER_BOW = register("seeker_bow", SeekerBowItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).durability(384));
	public static final DeferredItem<Item> ICE_BOW = register("ice_bow", IceBowItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).durability(384));
	public static final DeferredItem<Item> ENDER_BOW = register("ender_bow", EnderBowItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).durability(384));
	public static final DeferredItem<Item> ICE_SWORD = register("ice_sword", IceSwordItem::new, () -> new Item.Properties().sword(TFToolMaterials.ICE, 3.0F, -2.4F));
	public static final DeferredItem<Item> GLASS_SWORD = register("glass_sword", GlassSwordItem::new, () -> new Item.Properties().sword(TFToolMaterials.GLASS, 3, -2.4F).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> MAGIC_BEANS = register("magic_beans", MagicBeansItem::new, Item.Properties::new);
	public static final DeferredItem<Item> GIANT_PICKAXE = register("giant_pickaxe", GiantPickItem::new, () -> GiantPickItem.createGiantPickAttributes(new Item.Properties(), TFToolMaterials.GIANT, 8, -3.5F));
	public static final DeferredItem<Item> GIANT_SWORD = register("giant_sword", Item::new, () -> GiantPickItem.createGiantSwordAttributes(new Item.Properties(), TFToolMaterials.GIANT, 10, -3.5F));
	public static final DeferredItem<Item> LAMP_OF_CINDERS = register("lamp_of_cinders", LampOfCindersItem::new, () -> new Item.Properties().fireResistant().durability(1024).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CUBE_TALISMAN = register("cube_talisman", Item::new, () -> new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> CUBE_OF_ANNIHILATION = register("cube_of_annihilation", CubeOfAnnihilationItem::new, () -> new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> MOON_DIAL = register("moon_dial", MoonDialItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> POCKET_WATCH = register("pocket_watch", PocketWatchItem::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> EMPERORS_CLOTH = register("emperors_cloth", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> FOUR_LEAF_CLOVER = register("four_leaf_clover", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> CROWN_SPLINTER = register("crown_splinter", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> MYSTIC_CROWN = register("mystic_crown", Item::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).attributes(ItemAttributeModifiers.builder().add(Attributes.ARMOR, new AttributeModifier(Identifier.withDefaultNamespace("armor." + EquipmentSlot.HEAD.getName()), 2.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD).build()).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> STALE_BREAD = register("stale_bread", properties -> new CustomDamageSwordItem(TFDamageTypes.STALE_SANDWICH, properties), () -> new Item.Properties().stacksTo(1).component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true)).sword(ToolMaterial.WOOD, 3, -2.4F));

	public static final DeferredItem<Item> KEEPSAKE_CASKET = register("keepsake_casket", KeepsakeCasketItem::new, () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<Item> HUGE_LILY_PAD = register("huge_lily_pad", properties -> new HugeLilyPadItem(TFBlocks.HUGE_LILY_PAD.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> HUGE_WATER_LILY = register("huge_water_lily", properties -> new PlaceOnWaterBlockItem(TFBlocks.HUGE_WATER_LILY.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> FALLEN_LEAVES = register("fallen_leaves", properties -> new PlaceOnWaterBlockItem(TFBlocks.FALLEN_LEAVES.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<Item> WROUGHT_IRON_FENCE = register("wrought_iron_fence", properties -> new WroughtIronFenceItem(TFBlocks.WROUGHT_IRON_FENCE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix());

	public static final DeferredItem<Item> ZOMBIE_SKULL_CANDLE = register("zombie_skull_candle", properties -> new SkullCandleItem(TFBlocks.ZOMBIE_SKULL_CANDLE.get(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> SKELETON_SKULL_CANDLE = register("skeleton_skull_candle", properties -> new SkullCandleItem(TFBlocks.SKELETON_SKULL_CANDLE.get(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> WITHER_SKELETON_SKULL_CANDLE = register("wither_skeleton_skull_candle", properties -> new SkullCandleItem(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> CREEPER_SKULL_CANDLE = register("creeper_skull_candle", properties -> new SkullCandleItem(TFBlocks.CREEPER_SKULL_CANDLE.get(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> PLAYER_SKULL_CANDLE = register("player_skull_candle", properties -> new SkullCandleItem(TFBlocks.PLAYER_SKULL_CANDLE.get(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> PIGLIN_SKULL_CANDLE = register("piglin_skull_candle", properties -> new SkullCandleItem(TFBlocks.PIGLIN_SKULL_CANDLE.get(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(Rarity.UNCOMMON).equippable(EquipmentSlot.HEAD));

	public static final DeferredItem<Item> NAGA_TROPHY = register("naga_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> LICH_TROPHY = register("lich_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> MINOSHROOM_TROPHY = register("minoshroom_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> HYDRA_TROPHY = register("hydra_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> KNIGHT_PHANTOM_TROPHY = register("knight_phantom_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> UR_GHAST_TROPHY = register("ur_ghast_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> ALPHA_YETI_TROPHY = register("alpha_yeti_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> SNOW_QUEEN_TROPHY = register("snow_queen_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));
	public static final DeferredItem<Item> QUEST_RAM_TROPHY = register("quest_ram_trophy", properties -> new StandingAndWallBlockItem(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get(), Direction.DOWN, properties), () -> new Item.Properties().useBlockDescriptionPrefix().rarity(tfRarityEnumExtension.TWILIGHT).equippable(EquipmentSlot.HEAD));

	public static final DeferredItem<HollowLogItem> HOLLOW_TWILIGHT_OAK_LOG = register("hollow_twilight_oak_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_CANOPY_LOG = register("hollow_canopy_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_MANGROVE_LOG = register("hollow_mangrove_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_DARK_LOG = register("hollow_dark_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_TIME_LOG = register("hollow_time_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_TRANSFORMATION_LOG = register("hollow_transformation_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_MINING_LOG = register("hollow_mining_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_SORTING_LOG = register("hollow_sorting_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());

	public static final DeferredItem<HollowLogItem> HOLLOW_OAK_LOG = register("hollow_oak_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_SPRUCE_LOG = register("hollow_spruce_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_BIRCH_LOG = register("hollow_birch_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_JUNGLE_LOG = register("hollow_jungle_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_ACACIA_LOG = register("hollow_acacia_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_DARK_OAK_LOG = register("hollow_dark_oak_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_CRIMSON_STEM = register("hollow_crimson_stem", properties -> new HollowLogItem(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_WARPED_STEM = register("hollow_warped_stem", properties -> new HollowLogItem(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_VANGROVE_LOG = register("hollow_vangrove_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_CHERRY_LOG = register("hollow_cherry_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final DeferredItem<HollowLogItem> HOLLOW_PALE_OAK_LOG = register("hollow_pale_oak_log", properties -> new HollowLogItem(TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL, TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE, properties), () -> new Item.Properties().useBlockDescriptionPrefix());

	public static final DeferredItem<Item> TWILIGHT_OAK_SIGN = register("twilight_oak_sign", properties -> new SignItem(TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> TWILIGHT_OAK_HANGING_SIGN = register("twilight_oak_hanging_sign", properties -> new HangingSignItem(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.get(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> CANOPY_SIGN = register("canopy_sign", properties -> new SignItem(TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> CANOPY_HANGING_SIGN = register("canopy_hanging_sign", properties -> new HangingSignItem(TFBlocks.CANOPY_HANGING_SIGN.get(), TFBlocks.CANOPY_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> MANGROVE_SIGN = register("mangrove_sign", properties -> new SignItem(TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> MANGROVE_HANGING_SIGN = register("mangrove_hanging_sign", properties -> new HangingSignItem(TFBlocks.MANGROVE_HANGING_SIGN.get(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> DARK_SIGN = register("dark_sign", properties -> new SignItem(TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> DARK_HANGING_SIGN = register("dark_hanging_sign", properties -> new HangingSignItem(TFBlocks.DARK_HANGING_SIGN.get(), TFBlocks.DARK_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> TIME_SIGN = register("time_sign", properties -> new SignItem(TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> TIME_HANGING_SIGN = register("time_hanging_sign", properties -> new HangingSignItem(TFBlocks.TIME_HANGING_SIGN.get(), TFBlocks.TIME_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> TRANSFORMATION_SIGN = register("transformation_sign", properties -> new SignItem(TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> TRANSFORMATION_HANGING_SIGN = register("transformation_hanging_sign", properties -> new HangingSignItem(TFBlocks.TRANSFORMATION_HANGING_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> MINING_SIGN = register("mining_sign", properties -> new SignItem(TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> MINING_HANGING_SIGN = register("mining_hanging_sign", properties -> new HangingSignItem(TFBlocks.MINING_HANGING_SIGN.get(), TFBlocks.MINING_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> SORTING_SIGN = register("sorting_sign", properties -> new SignItem(TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));
	public static final DeferredItem<Item> SORTING_HANGING_SIGN = register("sorting_hanging_sign", properties -> new HangingSignItem(TFBlocks.SORTING_HANGING_SIGN.get(), TFBlocks.SORTING_WALL_HANGING_SIGN.get(), properties), () -> new Item.Properties().useBlockDescriptionPrefix().stacksTo(16));

	public static final DeferredItem<Item> TWILIGHT_OAK_BOAT = register("twilight_oak_boat", properties -> new BoatItem(TFEntities.TWILIGHT_OAK_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> TWILIGHT_OAK_CHEST_BOAT = register("twilight_oak_chest_boat", properties -> new BoatItem(TFEntities.TWILIGHT_OAK_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> CANOPY_BOAT = register("canopy_boat", properties -> new BoatItem(TFEntities.CANOPY_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> CANOPY_CHEST_BOAT = register("canopy_chest_boat", properties -> new BoatItem(TFEntities.CANOPY_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MANGROVE_BOAT = register("mangrove_boat", properties -> new BoatItem(TFEntities.MANGROVE_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MANGROVE_CHEST_BOAT = register("mangrove_chest_boat", properties -> new BoatItem(TFEntities.MANGROVE_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> DARK_BOAT = register("dark_boat", properties -> new BoatItem(TFEntities.DARK_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> DARK_CHEST_BOAT = register("dark_chest_boat", properties -> new BoatItem(TFEntities.DARK_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> TIME_BOAT = register("time_boat", properties -> new BoatItem(TFEntities.TIME_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> TIME_CHEST_BOAT = register("time_chest_boat", properties -> new BoatItem(TFEntities.TIME_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> TRANSFORMATION_BOAT = register("transformation_boat", properties -> new BoatItem(TFEntities.TRANSFORMATION_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> TRANSFORMATION_CHEST_BOAT = register("transformation_chest_boat", properties -> new BoatItem(TFEntities.TRANSFORMATION_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MINING_BOAT = register("mining_boat", properties -> new BoatItem(TFEntities.MINING_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MINING_CHEST_BOAT = register("mining_chest_boat", properties -> new BoatItem(TFEntities.MINING_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> SORTING_BOAT = register("sorting_boat", properties -> new BoatItem(TFEntities.SORTING_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> SORTING_CHEST_BOAT = register("sorting_chest_boat", properties -> new BoatItem(TFEntities.SORTING_CHEST_BOAT.get(), properties), () -> new Item.Properties().stacksTo(1));

	public static final DeferredItem<Item> MUSIC_DISC_RADIANCE = register("music_disc_radiance", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.RADIANCE));
	public static final DeferredItem<Item> MUSIC_DISC_STEPS = register("music_disc_steps", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.STEPS));
	public static final DeferredItem<Item> MUSIC_DISC_SUPERSTITIOUS = register("music_disc_superstitious", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.SUPERSTITIOUS));
	public static final DeferredItem<Item> MUSIC_DISC_HOME = register("music_disc_home", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.HOME));
	public static final DeferredItem<Item> MUSIC_DISC_WAYFARER = register("music_disc_wayfarer", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.WAYFARER));
	public static final DeferredItem<Item> MUSIC_DISC_FINDINGS = register("music_disc_findings", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.FINDINGS));
	public static final DeferredItem<Item> MUSIC_DISC_MAKER = register("music_disc_maker", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.MAKER));
	public static final DeferredItem<Item> MUSIC_DISC_THREAD = register("music_disc_thread", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.THREAD));
	public static final DeferredItem<Item> MUSIC_DISC_MOTION = register("music_disc_motion", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TFJukeboxSongs.MOTION));

	public static final DeferredItem<Item> NAGA_BANNER_PATTERN = register("naga_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.NAGA_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> LICH_BANNER_PATTERN = register("lich_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.LICH_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> MINOSHROOM_BANNER_PATTERN = register("minoshroom_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.MINOSHROOM_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> HYDRA_BANNER_PATTERN = register("hydra_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.HYDRA_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> KNIGHT_PHANTOM_BANNER_PATTERN = register("knight_phantom_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.KNIGHT_PHANTOM_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> UR_GHAST_BANNER_PATTERN = register("ur_ghast_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.UR_GHAST_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> ALPHA_YETI_BANNER_PATTERN = register("alpha_yeti_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.ALPHA_YETI_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> SNOW_QUEEN_BANNER_PATTERN = register("snow_queen_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.SNOW_QUEEN_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));
	public static final DeferredItem<Item> QUEST_RAM_BANNER_PATTERN = register("quest_ram_banner_pattern", Item::new, () -> new Item.Properties().delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TFBannerPatternTags.QUESTING_RAM_BANNER_PATTERN)).stacksTo(1).rarity(tfRarityEnumExtension.TWILIGHT));

	public static final DeferredItem<Item> RASPBERRY = register("raspberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY));
	public static final DeferredItem<Item> BLUEBERRY = register("blueberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY));
	public static final DeferredItem<Item> BLACKBERRY = register("blackberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY));
	public static final DeferredItem<Item> MALOBERRY = register("maloberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY));
	public static final DeferredItem<Item> BLIGHTBERRY = register("blightberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY, TFConsumables.BLIGHTBERRY));
	public static final DeferredItem<Item> DUSKBERRY = register("duskberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY, TFConsumables.DUSKBERRY));
	public static final DeferredItem<Item> SKYBERRY = register("skyberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY, TFConsumables.SKYBERRY));
	public static final DeferredItem<Item> STINGBERRY = register("stingberry", Item::new, () -> new Item.Properties().food(TFFoods.BERRY, TFConsumables.STINGBERRY));
	public static final DeferredItem<Item> BERRY_MEDLEY = register("berry_medley", Item::new, () -> new Item.Properties().food(TFFoods.BERRY_MEDLEY).usingConvertsTo(Items.BOWL).stacksTo(1));
	public static final DeferredItem<Item> MOSS_SOUP = register("moss_soup", Item::new, () -> new Item.Properties().food(TFFoods.MOSS_SOUP).usingConvertsTo(Items.BOWL).stacksTo(1));
	public static final DeferredItem<Item> SHIKA_SENBEI = register("shika_senbei", Item::new, () -> new Item.Properties().food(TFFoods.SHIKA_SENBEI));

	public static final DeferredItem<Item> MONSTER_JERKY = register("monster_jerky", Item::new, () -> new Item.Properties().food(TFFoods.MONSTER_JERKY));
	public static final DeferredItem<Item> BEEF_JERKY = register("beef_jerky", Item::new, () -> new Item.Properties().food(TFFoods.BEEF_JERKY));
	public static final DeferredItem<Item> CHICKEN_JERKY = register("chicken_jerky", Item::new, () -> new Item.Properties().food(TFFoods.CHICKEN_JERKY));
	public static final DeferredItem<Item> PORK_JERKY = register("pork_jerky", Item::new, () -> new Item.Properties().food(TFFoods.PORK_JERKY));
	public static final DeferredItem<Item> MUTTON_JERKY = register("mutton_jerky", Item::new, () -> new Item.Properties().food(TFFoods.MUTTON_JERKY));
	public static final DeferredItem<Item> RABBIT_JERKY = register("rabbit_jerky", Item::new, () -> new Item.Properties().food(TFFoods.RABBIT_JERKY));
	public static final DeferredItem<Item> COD_JERKY = register("cod_jerky", Item::new, () -> new Item.Properties().food(TFFoods.COD_JERKY));
	public static final DeferredItem<Item> SALMON_JERKY = register("salmon_jerky", Item::new, () -> new Item.Properties().food(TFFoods.SALMON_JERKY));
	public static final DeferredItem<Item> TROPICAL_FISH_JERKY = register("tropical_fish_jerky", Item::new, () -> new Item.Properties().food(TFFoods.TROPICAL_FISH_JERKY));
	public static final DeferredItem<Item> FUGU_JERKY = register("fugu_jerky", Item::new, () -> new Item.Properties().food(TFFoods.FUGU_JERKY));
	public static final DeferredItem<Item> VENISON_JERKY = register("venison_jerky", Item::new, () -> new Item.Properties().food(TFFoods.VENISON_JERKY));
	public static final DeferredItem<Item> MEEF_JERKY = register("meef_jerky", Item::new, () -> new Item.Properties().food(TFFoods.MEEF_JERKY));

	public static final DeferredItem<Item> GELATINOUS_SLIME_DROP = register("gelatinous_slime_drop", Item::new, () -> new Item.Properties().food(TFFoods.SLIME_DROP, TFConsumables.SLIME_DROP));
	public static final DeferredItem<Item> GELATINOUS_MAZE_SLIME_DROP = register("gelatinous_maze_slime_drop", Item::new, () -> new Item.Properties().food(TFFoods.MAZE_SLIME_DROP, TFConsumables.MAZE_SLIME_DROP));

	public static final DeferredItem<Item> TREATED_LEATHER = register("treated_leather", Item::new, Item.Properties::new);
	public static final DeferredItem<Item> TANNED_LEATHER = register("tanned_leather", Item::new, Item.Properties::new);

	public static <T extends Item> DeferredItem<T> register(String name, Function<Item.Properties, T> item, Supplier<Item.Properties> properties) {
		return ITEMS.register(name, () -> item.apply(properties.get().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix(name)))));
	}
}
