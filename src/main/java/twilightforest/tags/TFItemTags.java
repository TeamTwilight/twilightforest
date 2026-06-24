package twilightforest.tags;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import twilightforest.TwilightForestMod;

public class TFItemTags {

	public static final TagKey<Item> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
	public static final TagKey<Item> CANOPY_LOGS = create("canopy_logs");
	public static final TagKey<Item> MANGROVE_LOGS = create("mangrove_logs");
	public static final TagKey<Item> DARKWOOD_LOGS = create("darkwood_logs");
	public static final TagKey<Item> TIME_LOGS = create("timewood_logs");
	public static final TagKey<Item> TRANSFORMATION_LOGS = create("transwood_logs");
	public static final TagKey<Item> MINING_LOGS = create("mining_logs");
	public static final TagKey<Item> SORTING_LOGS = create("sortwood_logs");
	public static final TagKey<Item> TWILIGHT_LOGS = create("logs");

	public static final TagKey<Item> BANISTERS = create("banisters");
	public static final TagKey<Item> DRYING_RACKS = create("drying_racks");

	public static final TagKey<Item> PAPER = makeCommonTag("paper");

	public static final TagKey<Item> TOWERWOOD = create("towerwood");

	public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");

	public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
	public static final TagKey<Item> CARMINITE_GEMS = makeCommonTag("gems/carminite");
	public static final TagKey<Item> FIERY_INGOTS = makeCommonTag("ingots/fiery");
	public static final TagKey<Item> IRONWOOD_INGOTS = makeCommonTag("ingots/ironwood");
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = makeCommonTag("ingots/knightmetal");
	public static final TagKey<Item> STEELEAF_INGOTS = makeCommonTag("ingots/steeleaf");
	public static final TagKey<Item> WROUGHT_IRON_INGOTS = makeCommonTag("ingots/wrought_iron");

	public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur");
	public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite");
	public static final TagKey<Item> STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery");
	public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood");
	public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal");
	public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf");

	public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = makeCommonTag("raw_materials/ironwood");
	public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = makeCommonTag("raw_materials/knightmetal");

	public static final TagKey<Item> PORTAL_ACTIVATOR = create("portal/activator");

	public static final TagKey<Item> WIP = create("wip");

	public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = create("kobold_pacification_breads");
	public static final TagKey<Item> BOAR_TEMPT_ITEMS = create("boar_tempt_items");
	public static final TagKey<Item> DEER_TEMPT_ITEMS = create("deer_tempt_items");
	public static final TagKey<Item> DWARF_RABBIT_TEMPT_ITEMS = create("dwarf_rabbit_tempt_items");
	public static final TagKey<Item> PENGUIN_TEMPT_ITEMS = create("penguin_tempt_items");
	public static final TagKey<Item> RAVEN_TEMPT_ITEMS = create("raven_tempt_items");
	public static final TagKey<Item> SQUIRREL_TEMPT_ITEMS = create("squirrel_tempt_items");
	public static final TagKey<Item> TINY_BIRD_TEMPT_ITEMS = create("tiny_bird_tempt_items");

	public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = create("banned_uncrafting_ingredients");
	public static final TagKey<Item> BANNED_UNCRAFTABLES = create("banned_uncraftables");
	public static final TagKey<Item> UNCRAFTING_IGNORES_COST = create("uncrafting_ignores_cost");

	public static final TagKey<Item> KEPT_ON_DEATH = create("kept_on_death");
	public static final TagKey<Item> BLOCK_AND_CHAIN_ENCHANTABLE = create("enchantable/block_and_chain");

	public static final TagKey<Item> TRAVELLERS_BELT_BLACKLISTED = create("travellers_belt_blacklisted");
	public static final TagKey<Item> TRAVELLERS_AGILE_RANGER_WHITELISTED = create("travellers_agile_ranger_whitelisted");
	public static final TagKey<Item> TRAVELLERS_AGILE_RANGER_BLACKLISTED = create("travellers_agile_ranger_blacklisted");

	public static final TagKey<Item> REPAIRS_IRONWOOD_TOOLS = create("repairs_ironwood_tools");
	public static final TagKey<Item> REPAIRS_STEELEAF_TOOLS = create("repairs_steeleaf_tools");
	public static final TagKey<Item> REPAIRS_KNIGHTMETAL_TOOLS = create("repairs_knightmetal_tools");
	public static final TagKey<Item> REPAIRS_FIERY_TOOLS = create("repairs_fiery_tools");
	public static final TagKey<Item> REPAIRS_GIANT_TOOLS = create("repairs_giant_tools");
	public static final TagKey<Item> REPAIRS_ICE_TOOLS = create("repairs_ice_tools");
	public static final TagKey<Item> REPAIRS_GLASS_TOOLS = create("repairs_glass_tools");

	public static final TagKey<Item> REPAIRS_IRONWOOD_ARMOR = create("repairs_ironwood_armor");
	public static final TagKey<Item> REPAIRS_STEELEAF_ARMOR = create("repairs_steeleaf_armor");
	public static final TagKey<Item> REPAIRS_NAGA_ARMOR = create("repairs_naga_armor");
	public static final TagKey<Item> REPAIRS_FIERY_ARMOR = create("repairs_fiery_armor");
	public static final TagKey<Item> REPAIRS_KNIGHTMETAL_ARMOR = create("repairs_knightmetal_armor");
	public static final TagKey<Item> REPAIRS_PHANTOM_ARMOR = create("repairs_phantom_armor");
	public static final TagKey<Item> REPAIRS_ARCTIC_ARMOR = create("repairs_arctic_armor");
	public static final TagKey<Item> REPAIRS_YETI_ARMOR = create("repairs_yeti_armor");
	public static final TagKey<Item> REPAIRS_TRAVELLERS_GEAR = create("repairs_travellers_gear");

	public static final TagKey<Item> SCEPTERS = create("scepters");
	public static final TagKey<Item> IMMUNE_TO_THORNS = create("immune_to_thorns");

	public static final TagKey<Item> FOODS_JERKY = makeCommonTag("foods/jerky");
	public static final TagKey<Item> RENDER_LOWER_ON_DRYING_RACK = create("lower_on_drying_rack");
	public static final TagKey<Item> TROPHIES = create("trophies");
	public static final TagKey<Item> EMPERORS_CLOTH_APPLICABLE = create("emperors_cloth_applicable");

	public static final TagKey<Item> AC_FERNS = create("alexscaves", "ferns");
	public static final TagKey<Item> AC_FERROMAGNETIC_ITEMS = create("alexscaves", "ferromagnetic_items");
	public static final TagKey<Item> AC_RAW_MEATS = create("alexscaves", "raw_meats");

	public static final TagKey<Item> CURIOS_CHARM = create("curios", "charm");
	public static final TagKey<Item> CURIOS_HEAD = create("curios", "head");

	public static final TagKey<Item> CA_PLANTS = create("createaddition", "plants");
	public static final TagKey<Item> CA_PLANT_FOODS = create("createaddition", "plant_foods");

	public static final TagKey<Item> FD_CABBAGE_ROLL_INGREDIENTS = create("farmersdelight", "cabbage_roll_ingredients");

	public static final TagKey<Item> RANDOMIUM_BLACKLIST = create("randomium", "blacklist");

	private static TagKey<Item> create(String tagName) {
		return ItemTags.create(TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Item> makeCommonTag(String tagName) {
		return create("c", tagName);
	}

	private static TagKey<Item> create(String modid, String tagName) {
		return ItemTags.create(Identifier.fromNamespaceAndPath(modid, tagName));
	}
}
