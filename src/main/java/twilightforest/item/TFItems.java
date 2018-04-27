package twilightforest.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twilightforest.TwilightForestMod;

@GameRegistry.ObjectHolder(TwilightForestMod.ID)
public class TFItems {
	public static ItemArmor.ArmorMaterial ARMOR_NAGA = EnumHelper.addArmorMaterial("NAGA_SCALE", "naga_scale", 21, new int[]{3, 6, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5F);
	public static ItemArmor.ArmorMaterial ARMOR_IRONWOOD = EnumHelper.addArmorMaterial("IRONWOOD", "ironwood", 20, new int[]{2, 5, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
	public static ItemArmor.ArmorMaterial ARMOR_FIERY = EnumHelper.addArmorMaterial("FIERY", "fiery", 25, new int[]{4, 7, 9, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.5F);
	public static ItemArmor.ArmorMaterial ARMOR_STEELEAF = EnumHelper.addArmorMaterial("STEELEAF", "steeleaf", 10, new int[]{3, 6, 8, 3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
	public static ItemArmor.ArmorMaterial ARMOR_KNIGHTLY = EnumHelper.addArmorMaterial("KNIGHTMETAL", "knightly", 20, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F);
	public static ItemArmor.ArmorMaterial ARMOR_PHANTOM = EnumHelper.addArmorMaterial("KNIGHTPHANTOM", "phantom", 30, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.5F);
	public static ItemArmor.ArmorMaterial ARMOR_YETI = EnumHelper.addArmorMaterial("YETI", "yetiarmor", 20, new int[]{3, 6, 7, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3F);
	public static ItemArmor.ArmorMaterial ARMOR_ARCTIC = EnumHelper.addArmorMaterial("ARCTIC", "arcticarmor", 10, new int[]{2, 5, 7, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2F);

	// harvestLevel, maxUses, efficiency, damage, enchantability
	public static Item.ToolMaterial TOOL_IRONWOOD = EnumHelper.addToolMaterial("IRONWOOD", 2, 512, 6.5F, 2, 25);
	public static Item.ToolMaterial TOOL_FIERY = EnumHelper.addToolMaterial("FIERY", 4, 1024, 9F, 4, 10);
	public static Item.ToolMaterial TOOL_STEELEAF = EnumHelper.addToolMaterial("STEELEAF", 3, 131, 8.0F, 3, 9);
	public static Item.ToolMaterial TOOL_KNIGHTLY = EnumHelper.addToolMaterial("KNIGHTMETAL", 3, 512, 8.0F, 3, 8);
	public static Item.ToolMaterial TOOL_GIANT = EnumHelper.addToolMaterial("GIANTSTONE", 1, 1024, 4.0F, 1.0F, 5);
	public static Item.ToolMaterial TOOL_ICE = EnumHelper.addToolMaterial("TFICE", 0, 32, 1.0F, 3.5F, 5);
	public static Item.ToolMaterial TOOL_GLASS = EnumHelper.addToolMaterial("TFGLASS", 0, 1, 1.0F, 36.0F, 30);

	@GameRegistry.ObjectHolder("naga_scale")
	public static final Item naga_scale;
	@GameRegistry.ObjectHolder("naga_chestplate")
	public static final Item naga_chestplate;
	@GameRegistry.ObjectHolder("naga_leggings")
	public static final Item naga_leggings;
	@GameRegistry.ObjectHolder("twilight_scepter")
	public static final Item twilight_scepter;
	@GameRegistry.ObjectHolder("lifedrain_scepter")
	public static final Item lifedrain_scepter;
	@GameRegistry.ObjectHolder("zombie_scepter")
	public static final Item zombie_scepter;
	@GameRegistry.ObjectHolder("ore_meter")
	public static final Item ore_meter;
	@GameRegistry.ObjectHolder("magic_map")
	public static final Item magic_map;
	@GameRegistry.ObjectHolder("maze_map")
	public static final Item maze_map;
	@GameRegistry.ObjectHolder("ore_map")
	public static final Item ore_map;
	@GameRegistry.ObjectHolder("raven_feather")
	public static final Item raven_feather;
	@GameRegistry.ObjectHolder("magic_map_focus")
	public static final Item magic_map_focus;
	@GameRegistry.ObjectHolder("maze_map_focus")
	public static final Item maze_map_focus;
	@GameRegistry.ObjectHolder("liveroot")
	public static final Item liveroot;
	@GameRegistry.ObjectHolder("ironwood_raw")
	public static final Item ironwood_raw;
	@GameRegistry.ObjectHolder("ironwood_ingot")
	public static final Item ironwood_ingot;
	@GameRegistry.ObjectHolder("ironwood_helmet")
	public static final Item ironwood_helmet;
	@GameRegistry.ObjectHolder("ironwood_chestplate")
	public static final Item ironwood_chestplate;
	@GameRegistry.ObjectHolder("ironwood_leggings")
	public static final Item ironwood_leggings;
	@GameRegistry.ObjectHolder("ironwood_boots")
	public static final Item ironwood_boots;
	@GameRegistry.ObjectHolder("ironwood_sword")
	public static final Item ironwood_sword;
	@GameRegistry.ObjectHolder("ironwood_shovel")
	public static final Item ironwood_shovel;
	@GameRegistry.ObjectHolder("ironwood_pickaxe")
	public static final Item ironwood_pickaxe;
	@GameRegistry.ObjectHolder("ironwood_axe")
	public static final Item ironwood_axe;
	@GameRegistry.ObjectHolder("ironwood_hoe")
	public static final Item ironwood_hoe;
	@GameRegistry.ObjectHolder("torchberries")
	public static final Item torchberries;
	@GameRegistry.ObjectHolder("raw_venison")
	public static final Item raw_venison;
	@GameRegistry.ObjectHolder("cooked_venison")
	public static final Item cooked_venison;
	@GameRegistry.ObjectHolder("hydra_chop")
	public static final Item hydra_chop;
	@GameRegistry.ObjectHolder("fiery_blood")
	public static final Item fiery_blood;
	@GameRegistry.ObjectHolder("trophy")
	public static final Item trophy;
	@GameRegistry.ObjectHolder("fiery_ingot")
	public static final Item fiery_ingot;
	@GameRegistry.ObjectHolder("fiery_helmet")
	public static final Item fiery_helmet;
	@GameRegistry.ObjectHolder("fiery_chestplate")
	public static final Item fiery_chestplate;
	@GameRegistry.ObjectHolder("fiery_leggings")
	public static final Item fiery_leggings;
	@GameRegistry.ObjectHolder("fiery_boots")
	public static final Item fiery_boots;
	@GameRegistry.ObjectHolder("fiery_sword")
	public static final Item fiery_sword;
	@GameRegistry.ObjectHolder("fiery_pickaxe")
	public static final Item fiery_pickaxe;
	@GameRegistry.ObjectHolder("steeleaf_ingot")
	public static final Item steeleaf_ingot;
	@GameRegistry.ObjectHolder("steeleaf_helmet")
	public static final Item steeleaf_helmet;
	@GameRegistry.ObjectHolder("steeleaf_chestplate")
	public static final Item steeleaf_chestplate;
	@GameRegistry.ObjectHolder("steeleaf_leggings")
	public static final Item steeleaf_leggings;
	@GameRegistry.ObjectHolder("steeleaf_boots")
	public static final Item steeleaf_boots;
	@GameRegistry.ObjectHolder("steeleaf_sword")
	public static final Item steeleaf_sword;
	@GameRegistry.ObjectHolder("steeleaf_shovel")
	public static final Item steeleaf_shovel;
	@GameRegistry.ObjectHolder("steeleaf_pickaxe")
	public static final Item steeleaf_pickaxe;
	@GameRegistry.ObjectHolder("steeleaf_axe")
	public static final Item steeleaf_axe;
	@GameRegistry.ObjectHolder("steeleaf_hoe")
	public static final Item steeleaf_hoe;
	@GameRegistry.ObjectHolder("minotaur_axe")
	public static final Item minotaur_axe;
	@GameRegistry.ObjectHolder("mazebreaker_pickaxe")
	public static final Item mazebreaker_pickaxe;
	@GameRegistry.ObjectHolder("transformation_powder")
	public static final Item transformation_powder;
	@GameRegistry.ObjectHolder("raw_meef")
	public static final Item raw_meef;
	@GameRegistry.ObjectHolder("cooked_meef")
	public static final Item cooked_meef;
	@GameRegistry.ObjectHolder("meef_stroganoff")
	public static final Item meef_stroganoff;
	@GameRegistry.ObjectHolder("maze_wafer")
	public static final Item maze_wafer;
	@GameRegistry.ObjectHolder("magic_map_empty")
	public static final Item magic_map_empty;
	@GameRegistry.ObjectHolder("maze_map_empty")
	public static final Item maze_map_empty;
	@GameRegistry.ObjectHolder("ore_map_empty")
	public static final Item ore_map_empty;
	@GameRegistry.ObjectHolder("ore_magnet")
	public static final Item ore_magnet;
	@GameRegistry.ObjectHolder("crumble_horn")
	public static final Item crumble_horn;
	@GameRegistry.ObjectHolder("peacock_fan")
	public static final Item peacock_fan;
	@GameRegistry.ObjectHolder("moonworm_queen")
	public static final Item moonworm_queen;
	@GameRegistry.ObjectHolder("charm_of_life_1")
	public static final Item charm_of_life_1;
	@GameRegistry.ObjectHolder("charm_of_life_2")
	public static final Item charm_of_life_2;
	@GameRegistry.ObjectHolder("charm_of_keeping_1")
	public static final Item charm_of_keeping_1;
	@GameRegistry.ObjectHolder("charm_of_keeping_2")
	public static final Item charm_of_keeping_2;
	@GameRegistry.ObjectHolder("charm_of_keeping_3")
	public static final Item charm_of_keeping_3;
	@GameRegistry.ObjectHolder("tower_key")
	public static final Item tower_key;
	@GameRegistry.ObjectHolder("borer_essence")
	public static final Item borer_essence;
	@GameRegistry.ObjectHolder("carminite")
	public static final Item carminite;
	@GameRegistry.ObjectHolder("experiment_115")
	public static final Item experiment_115;
	@GameRegistry.ObjectHolder("armor_shard")
	public static final Item armor_shard;
	@GameRegistry.ObjectHolder("knightmetal_ingot")
	public static final Item knightmetal_ingot;
	@GameRegistry.ObjectHolder("armor_shard_cluster")
	public static final Item armor_shard_cluster;
	@GameRegistry.ObjectHolder("knightmetal_helmet")
	public static final Item knightmetal_helmet;
	@GameRegistry.ObjectHolder("knightmetal_chestplate")
	public static final Item knightmetal_chestplate;
	@GameRegistry.ObjectHolder("knightmetal_leggings")
	public static final Item knightmetal_leggings;
	@GameRegistry.ObjectHolder("knightmetal_boots")
	public static final Item knightmetal_boots;
	@GameRegistry.ObjectHolder("knightmetal_sword")
	public static final Item knightmetal_sword;
	@GameRegistry.ObjectHolder("knightmetal_pickaxe")
	public static final Item knightmetal_pickaxe;
	@GameRegistry.ObjectHolder("knightmetal_axe")
	public static final Item knightmetal_axe;
	@GameRegistry.ObjectHolder("phantom_helmet")
	public static final Item phantom_helmet;
	@GameRegistry.ObjectHolder("phantom_chestplate")
	public static final Item phantom_chestplate;
	@GameRegistry.ObjectHolder("lamp_of_cinders")
	public static final Item lamp_of_cinders;
	@GameRegistry.ObjectHolder("fiery_tears")
	public static final Item fiery_tears;
	@GameRegistry.ObjectHolder("alpha_fur")
	public static final Item alpha_fur;
	@GameRegistry.ObjectHolder("yeti_helmet")
	public static final Item yeti_helmet;
	@GameRegistry.ObjectHolder("yeti_chestplate")
	public static final Item yeti_chestplate;
	@GameRegistry.ObjectHolder("yeti_leggings")
	public static final Item yeti_leggings;
	@GameRegistry.ObjectHolder("yeti_boots")
	public static final Item yeti_boots;
	@GameRegistry.ObjectHolder("ice_bomb")
	public static final Item ice_bomb;
	@GameRegistry.ObjectHolder("arctic_fur")
	public static final Item arctic_fur;
	@GameRegistry.ObjectHolder("arctic_helmet")
	public static final Item arctic_helmet;
	@GameRegistry.ObjectHolder("arctic_chestplate")
	public static final Item arctic_chestplate;
	@GameRegistry.ObjectHolder("arctic_leggings")
	public static final Item arctic_leggings;
	@GameRegistry.ObjectHolder("arctic_boots")
	public static final Item arctic_boots;
	@GameRegistry.ObjectHolder("magic_beans")
	public static final Item magic_beans;
	@GameRegistry.ObjectHolder("giant_pickaxe")
	public static final Item giant_pickaxe;
	@GameRegistry.ObjectHolder("giant_sword")
	public static final Item giant_sword;
	@GameRegistry.ObjectHolder("triple_bow")
	public static final Item triple_bow;
	@GameRegistry.ObjectHolder("seeker_bow")
	public static final Item seeker_bow;
	@GameRegistry.ObjectHolder("ice_bow")
	public static final Item ice_bow;
	@GameRegistry.ObjectHolder("ender_bow")
	public static final Item ender_bow;
	@GameRegistry.ObjectHolder("ice_sword")
	public static final Item ice_sword;
	@GameRegistry.ObjectHolder("glass_sword")
	public static final Item glass_sword;
	@GameRegistry.ObjectHolder("knightmetal_ring")
	public static final Item knightmetal_ring;
	@GameRegistry.ObjectHolder("block_and_chain")
	public static final Item chainBlock;
	@GameRegistry.ObjectHolder("cube_talisman")
	public static final Item cube_talisman;
	@GameRegistry.ObjectHolder("cube_of_annihilation")
	public static final Item cube_of_annihilation;
	@GameRegistry.ObjectHolder("miniature_structure")
	public static final Item miniature_structure;
	@GameRegistry.ObjectHolder("castle_door")
	public static final Item castle_door;
	@GameRegistry.ObjectHolder("block_storage")
	public static final Item block_storage;

	public static CreativeTabTwilightForest creativeTab = new CreativeTabTwilightForest("twilightForest");

	public static void initRepairMaterials() {
		ARMOR_NAGA.setRepairItem(new ItemStack(naga_scale));
		ARMOR_IRONWOOD.setRepairItem(new ItemStack(ironwood_ingot));
		ARMOR_FIERY.setRepairItem(new ItemStack(fiery_ingot));
		ARMOR_STEELEAF.setRepairItem(new ItemStack(steeleaf_ingot));
		ARMOR_KNIGHTLY.setRepairItem(new ItemStack(knightmetal_ingot));
		ARMOR_PHANTOM.setRepairItem(new ItemStack(knightmetal_ingot));
		ARMOR_YETI.setRepairItem(new ItemStack(alpha_fur));
		ARMOR_ARCTIC.setRepairItem(new ItemStack(arctic_fur));

		TOOL_IRONWOOD.setRepairItem(new ItemStack(ironwood_ingot));
		TOOL_FIERY.setRepairItem(new ItemStack(fiery_ingot));
		TOOL_STEELEAF.setRepairItem(new ItemStack(steeleaf_ingot));
		TOOL_KNIGHTLY.setRepairItem(new ItemStack(knightmetal_ingot));
		TOOL_GIANT.setRepairItem(new ItemStack(knightmetal_ingot));
		TOOL_ICE.setRepairItem(new ItemStack(Blocks.PACKED_ICE));
	}

	static {
		naga_scale = null;
		naga_chestplate = null;
		naga_leggings = null;
		twilight_scepter = null;
		lifedrain_scepter = null;
		zombie_scepter = null;
		//items.register(wandPacification, "Wand of Pacification [NYI]");
		ore_meter = null;
		magic_map = null;
		maze_map = null;
		ore_map = null;
		raven_feather = null;
		magic_map_focus = null;
		maze_map_focus = null;
		liveroot = null;
		ironwood_raw = null;
		ironwood_ingot = null;
		ironwood_helmet = null;
		ironwood_chestplate = null;
		ironwood_leggings = null;
		ironwood_boots = null;
		ironwood_sword = null;
		ironwood_shovel = null;
		ironwood_pickaxe = null;
		ironwood_axe = null;
		ironwood_hoe = null;
		torchberries = null;
		raw_venison = null;
		cooked_venison = null;
		hydra_chop = null;
		fiery_blood = null;
		trophy = null;
		fiery_ingot = null;
		fiery_helmet = null;
		fiery_chestplate = null;
		fiery_leggings = null;
		fiery_boots = null;
		fiery_sword = null;
		fiery_pickaxe = null;
		steeleaf_ingot = null;
		steeleaf_helmet = null;
		steeleaf_chestplate = null;
		steeleaf_leggings = null;
		steeleaf_boots = null;
		steeleaf_sword = null;
		steeleaf_shovel = null;
		steeleaf_pickaxe = null;
		steeleaf_axe = null;
		steeleaf_hoe = null;
		minotaur_axe = null;
		mazebreaker_pickaxe = null;
		transformation_powder = null;
		raw_meef = null;
		cooked_meef = null;
		meef_stroganoff = null;
		maze_wafer = null;
		magic_map_empty = null;
		maze_map_empty = null;
		ore_map_empty = null;
		ore_magnet = null;
		crumble_horn = null;
		peacock_fan = null;
		moonworm_queen = null;
		charm_of_life_1 = null;
		charm_of_life_2 = null;
		charm_of_keeping_1 = null;
		charm_of_keeping_2 = null;
		charm_of_keeping_3 = null;
		tower_key = null;
		borer_essence = null;
		carminite = null;
		experiment_115 = null;
		armor_shard = null;
		knightmetal_ingot = null;
		armor_shard_cluster = null;
		knightmetal_helmet = null;
		knightmetal_chestplate = null;
		knightmetal_leggings = null;
		knightmetal_boots = null;
		knightmetal_sword = null;
		knightmetal_pickaxe = null;
		knightmetal_axe = null;
		phantom_helmet = null;
		phantom_chestplate = null;
		lamp_of_cinders = null;
		fiery_tears = null;
		alpha_fur = null;
		yeti_helmet = null;
		yeti_chestplate = null;
		yeti_leggings = null;
		yeti_boots = null;
		ice_bomb = null;
		arctic_fur = null;
		arctic_helmet = null;
		arctic_chestplate = null;
		arctic_leggings = null;
		arctic_boots = null;
		magic_beans = null;
		giant_pickaxe = null;
		giant_sword = null;
		triple_bow = null;
		seeker_bow = null;
		ice_bow = null;
		ender_bow = null;
		ice_sword = null;
		glass_sword = null;
		knightmetal_ring = null;
		chainBlock = null;
		cube_talisman = null;
		cube_of_annihilation = null;
		miniature_structure = null;
		castle_door = null;
		block_storage = null;
	}
}