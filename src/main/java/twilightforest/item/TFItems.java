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

	public static ItemArmor.ArmorMaterial ARMOR_NAGA = EnumHelper.addArmorMaterial("NAGA_SCALE", "naga_scale", 21, new int[]{3, 6, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_IRONWOOD = EnumHelper.addArmorMaterial("IRONWOOD", "ironwood", 20, new int[]{2, 5, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_FIERY = EnumHelper.addArmorMaterial("FIERY", "fiery", 25, new int[]{4, 7, 9, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_STEELEAF = EnumHelper.addArmorMaterial("STEELEAF", "steeleaf", 10, new int[]{3, 6, 8, 3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_KNIGHTLY = EnumHelper.addArmorMaterial("KNIGHTMETAL", "knightly", 20, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_PHANTOM = EnumHelper.addArmorMaterial("KNIGHTPHANTOM", "phantom", 30, new int[]{3, 6, 8, 3}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_YETI = EnumHelper.addArmorMaterial("YETI", "yetiarmor", 20, new int[]{3, 6, 7, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);
	public static ItemArmor.ArmorMaterial ARMOR_ARCTIC = EnumHelper.addArmorMaterial("ARCTIC", "arcticarmor", 10, new int[]{2, 5, 7, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0f);


	public static Item.ToolMaterial TOOL_IRONWOOD = EnumHelper.addToolMaterial("IRONWOOD", 2, 512, 6.5F, 2, 25);
	public static Item.ToolMaterial TOOL_FIERY = EnumHelper.addToolMaterial("FIERY", 4, 1024, 9F, 4, 10);
	public static Item.ToolMaterial TOOL_STEELEAF = EnumHelper.addToolMaterial("STEELEAF", 3, 131, 8.0F, 3, 9);
	public static Item.ToolMaterial TOOL_KNIGHTLY = EnumHelper.addToolMaterial("KNIGHTMETAL", 3, 512, 8.0F, 3, 8);
	public static Item.ToolMaterial TOOL_GIANT = EnumHelper.addToolMaterial("GIANTSTONE", 1, 1024, 4.0F, 1.0F, 5);
	public static Item.ToolMaterial TOOL_ICE = EnumHelper.addToolMaterial("TFICE", 0, 32, 1.0F, 3.5F, 5);
	public static Item.ToolMaterial TOOL_GLASS = EnumHelper.addToolMaterial("TFGLASS", 0, 1, 1.0F, 36.0F, 30);

	@GameRegistry.ObjectHolder("naga_scale")
	public static final Item nagaScale;
	@GameRegistry.ObjectHolder("naga_chestplate")
	public static final Item plateNaga;
	@GameRegistry.ObjectHolder("naga_leggings")
	public static final Item legsNaga;
	@GameRegistry.ObjectHolder("twilight_scepter")
	public static final Item scepterTwilight;
	@GameRegistry.ObjectHolder("lifedrain_scepter")
	public static final Item scepterLifeDrain;
	@GameRegistry.ObjectHolder("zombie_scepter")
	public static final Item scepterZombie;
	@GameRegistry.ObjectHolder("ore_meter")
	public static final Item oreMeter;
	@GameRegistry.ObjectHolder("magic_map")
	public static final Item magicMap;
	@GameRegistry.ObjectHolder("maze_map")
	public static final Item mazeMap;
	@GameRegistry.ObjectHolder("ore_map")
	public static final Item oreMap;
	@GameRegistry.ObjectHolder("raven_feather")
	public static final Item feather;
	@GameRegistry.ObjectHolder("magic_map_focus")
	public static final Item magicMapFocus;
	@GameRegistry.ObjectHolder("maze_map_focus")
	public static final Item mazeMapFocus;
	@GameRegistry.ObjectHolder("liveroot")
	public static final Item liveRoot;
	@GameRegistry.ObjectHolder("ironwood_raw")
	public static final Item ironwoodRaw;
	@GameRegistry.ObjectHolder("ironwood_ingot")
	public static final Item ironwoodIngot;
	@GameRegistry.ObjectHolder("ironwood_helmet")
	public static final Item ironwoodHelm;
	@GameRegistry.ObjectHolder("ironwood_chestplate")
	public static final Item ironwoodPlate;
	@GameRegistry.ObjectHolder("ironwood_leggings")
	public static final Item ironwoodLegs;
	@GameRegistry.ObjectHolder("ironwood_boots")
	public static final Item ironwoodBoots;
	@GameRegistry.ObjectHolder("ironwood_sword")
	public static final Item ironwoodSword;
	@GameRegistry.ObjectHolder("ironwood_shovel")
	public static final Item ironwoodShovel;
	@GameRegistry.ObjectHolder("ironwood_pickaxe")
	public static final Item ironwoodPick;
	@GameRegistry.ObjectHolder("ironwood_axe")
	public static final Item ironwoodAxe;
	@GameRegistry.ObjectHolder("ironwood_hoe")
	public static final Item ironwoodHoe;
	@GameRegistry.ObjectHolder("torchberries")
	public static final Item torchberries;
	@GameRegistry.ObjectHolder("raw_venison")
	public static final Item venisonRaw;
	@GameRegistry.ObjectHolder("cooked_venison")
	public static final Item venisonCooked;
	@GameRegistry.ObjectHolder("hydra_chop")
	public static final Item hydraChop;
	@GameRegistry.ObjectHolder("fiery_blood")
	public static final Item fieryBlood;
	@GameRegistry.ObjectHolder("trophy")
	public static final Item trophy;
	@GameRegistry.ObjectHolder("fiery_ingot")
	public static final Item fieryIngot;
	@GameRegistry.ObjectHolder("fiery_helmet")
	public static final Item fieryHelm;
	@GameRegistry.ObjectHolder("fiery_chestplate")
	public static final Item fieryPlate;
	@GameRegistry.ObjectHolder("fiery_leggings")
	public static final Item fieryLegs;
	@GameRegistry.ObjectHolder("fiery_boots")
	public static final Item fieryBoots;
	@GameRegistry.ObjectHolder("fiery_sword")
	public static final Item fierySword;
	@GameRegistry.ObjectHolder("fiery_pickaxe")
	public static final Item fieryPick;
	@GameRegistry.ObjectHolder("steeleaf_ingot")
	public static final Item steeleafIngot;
	@GameRegistry.ObjectHolder("steeleaf_helmet")
	public static final Item steeleafHelm;
	@GameRegistry.ObjectHolder("steeleaf_chestplate")
	public static final Item steeleafPlate;
	@GameRegistry.ObjectHolder("steeleaf_leggings")
	public static final Item steeleafLegs;
	@GameRegistry.ObjectHolder("steeleaf_boots")
	public static final Item steeleafBoots;
	@GameRegistry.ObjectHolder("steeleaf_sword")
	public static final Item steeleafSword;
	@GameRegistry.ObjectHolder("steeleaf_shovel")
	public static final Item steeleafShovel;
	@GameRegistry.ObjectHolder("steeleaf_pickaxe")
	public static final Item steeleafPick;
	@GameRegistry.ObjectHolder("steeleaf_axe")
	public static final Item steeleafAxe;
	@GameRegistry.ObjectHolder("steeleaf_hoe")
	public static final Item steeleafHoe;
	@GameRegistry.ObjectHolder("minotaur_axe")
	public static final Item minotaurAxe;
	@GameRegistry.ObjectHolder("mazebreaker_pickaxe")
	public static final Item mazebreakerPick;
	@GameRegistry.ObjectHolder("transformation_powder")
	public static final Item transformPowder;
	@GameRegistry.ObjectHolder("raw_meef")
	public static final Item meefRaw;
	@GameRegistry.ObjectHolder("cooked_meef")
	public static final Item meefSteak;
	@GameRegistry.ObjectHolder("meef_stroganoff")
	public static final Item meefStroganoff;
	@GameRegistry.ObjectHolder("maze_wafer")
	public static final Item mazeWafer;
	@GameRegistry.ObjectHolder("magic_map_empty")
	public static final Item emptyMagicMap;
	@GameRegistry.ObjectHolder("maze_map_empty")
	public static final Item emptyMazeMap;
	@GameRegistry.ObjectHolder("ore_map_empty")
	public static final Item emptyOreMap;
	@GameRegistry.ObjectHolder("ore_magnet")
	public static final Item oreMagnet;
	@GameRegistry.ObjectHolder("crumble_horn")
	public static final Item crumbleHorn;
	@GameRegistry.ObjectHolder("peacock_fan")
	public static final Item peacockFan;
	@GameRegistry.ObjectHolder("moonworm_queen")
	public static final Item moonwormQueen;
	@GameRegistry.ObjectHolder("charm_of_life_1")
	public static final Item charmOfLife1;
	@GameRegistry.ObjectHolder("charm_of_life_2")
	public static final Item charmOfLife2;
	@GameRegistry.ObjectHolder("charm_of_keeping_1")
	public static final Item charmOfKeeping1;
	@GameRegistry.ObjectHolder("charm_of_keeping_2")
	public static final Item charmOfKeeping2;
	@GameRegistry.ObjectHolder("charm_of_keeping_3")
	public static final Item charmOfKeeping3;
	@GameRegistry.ObjectHolder("tower_key")
	public static final Item towerKey;
	@GameRegistry.ObjectHolder("borer_essence")
	public static final Item borerEssence;
	@GameRegistry.ObjectHolder("carminite")
	public static final Item carminite;
	@GameRegistry.ObjectHolder("experiment_115")
	public static final Item experiment115;
	@GameRegistry.ObjectHolder("armor_shard")
	public static final Item armorShard;
	@GameRegistry.ObjectHolder("knightmetal_ingot")
	public static final Item knightMetal;
	@GameRegistry.ObjectHolder("armor_shard_cluster")
	public static final Item shardCluster;
	@GameRegistry.ObjectHolder("knightmetal_helmet")
	public static final Item knightlyHelm;
	@GameRegistry.ObjectHolder("knightmetal_chestplate")
	public static final Item knightlyPlate;
	@GameRegistry.ObjectHolder("knightmetal_leggings")
	public static final Item knightlyLegs;
	@GameRegistry.ObjectHolder("knightmetal_boots")
	public static final Item knightlyBoots;
	@GameRegistry.ObjectHolder("knightmetal_sword")
	public static final Item knightlySword;
	@GameRegistry.ObjectHolder("knightmetal_pickaxe")
	public static final Item knightlyPick;
	@GameRegistry.ObjectHolder("knightmetal_axe")
	public static final Item knightlyAxe;
	@GameRegistry.ObjectHolder("phantom_helmet")
	public static final Item phantomHelm;
	@GameRegistry.ObjectHolder("phantom_chestplate")
	public static final Item phantomPlate;
	@GameRegistry.ObjectHolder("lamp_of_cinders")
	public static final Item lampOfCinders;
	@GameRegistry.ObjectHolder("fiery_tears")
	public static final Item fieryTears;
	@GameRegistry.ObjectHolder("alpha_fur")
	public static final Item alphaFur;
	@GameRegistry.ObjectHolder("yeti_helmet")
	public static final Item yetiHelm;
	@GameRegistry.ObjectHolder("yeti_chestplate")
	public static final Item yetiPlate;
	@GameRegistry.ObjectHolder("yeti_leggings")
	public static final Item yetiLegs;
	@GameRegistry.ObjectHolder("yeti_boots")
	public static final Item yetiBoots;
	@GameRegistry.ObjectHolder("ice_bomb")
	public static final Item iceBomb;
	@GameRegistry.ObjectHolder("arctic_fur")
	public static final Item arcticFur;
	@GameRegistry.ObjectHolder("arctic_helmet")
	public static final Item arcticHelm;
	@GameRegistry.ObjectHolder("arctic_chestplate")
	public static final Item arcticPlate;
	@GameRegistry.ObjectHolder("arctic_leggings")
	public static final Item arcticLegs;
	@GameRegistry.ObjectHolder("arctic_boots")
	public static final Item arcticBoots;
	@GameRegistry.ObjectHolder("magic_beans")
	public static final Item magicBeans;
	@GameRegistry.ObjectHolder("giant_pickaxe")
	public static final Item giantPick;
	@GameRegistry.ObjectHolder("giant_sword")
	public static final Item giantSword;
	@GameRegistry.ObjectHolder("triple_bow")
	public static final Item tripleBow;
	@GameRegistry.ObjectHolder("seeker_bow")
	public static final Item seekerBow;
	@GameRegistry.ObjectHolder("ice_bow")
	public static final Item iceBow;
	@GameRegistry.ObjectHolder("ender_bow")
	public static final Item enderBow;
	@GameRegistry.ObjectHolder("ice_sword")
	public static final Item iceSword;
	@GameRegistry.ObjectHolder("glass_sword")
	public static final Item glassSword;
	@GameRegistry.ObjectHolder("knightmetal_ring")
	public static final Item knightmetalRing;
	@GameRegistry.ObjectHolder("block_and_chain")
	public static final Item chainBlock;
	@GameRegistry.ObjectHolder("cube_talisman")
	public static final Item cubeTalisman;
	@GameRegistry.ObjectHolder("cube_of_annihilation")
	public static final Item cubeOfAnnihilation;
	@GameRegistry.ObjectHolder("miniature_structure")
	public static final Item miniture_structure;
	@GameRegistry.ObjectHolder("castle_door")
	public static final Item castleDoor;

	public static CreativeTabTwilightForest creativeTab = new CreativeTabTwilightForest("twilightForest");

	public static void initRepairMaterials() {
		ARMOR_NAGA.setRepairItem(new ItemStack(nagaScale));
		ARMOR_IRONWOOD.setRepairItem(new ItemStack(ironwoodIngot));
		ARMOR_FIERY.setRepairItem(new ItemStack(fieryIngot));
		ARMOR_STEELEAF.setRepairItem(new ItemStack(steeleafIngot));
		ARMOR_KNIGHTLY.setRepairItem(new ItemStack(knightMetal));
		ARMOR_PHANTOM.setRepairItem(new ItemStack(knightMetal));
		ARMOR_YETI.setRepairItem(new ItemStack(alphaFur));
		ARMOR_ARCTIC.setRepairItem(new ItemStack(arcticFur));

		TOOL_IRONWOOD.setRepairItem(new ItemStack(ironwoodIngot));
		TOOL_FIERY.setRepairItem(new ItemStack(fieryIngot));
		TOOL_STEELEAF.setRepairItem(new ItemStack(steeleafIngot));
		TOOL_KNIGHTLY.setRepairItem(new ItemStack(knightMetal));
		TOOL_GIANT.setRepairItem(new ItemStack(knightMetal));
		TOOL_ICE.setRepairItem(new ItemStack(Blocks.PACKED_ICE));
	}

	static {
		nagaScale = null;
		plateNaga = null;
		legsNaga = null;
		scepterTwilight = null;
		scepterLifeDrain = null;
		scepterZombie = null;
		//items.register(wandPacification, "Wand of Pacification [NYI]");
		oreMeter = null;
		magicMap = null;
		mazeMap = null;
		oreMap = null;
		feather = null;
		magicMapFocus = null;
		mazeMapFocus = null;
		liveRoot = null;
		ironwoodRaw = null;
		ironwoodIngot = null;
		ironwoodHelm = null;
		ironwoodPlate = null;
		ironwoodLegs = null;
		ironwoodBoots = null;
		ironwoodSword = null;
		ironwoodShovel = null;
		ironwoodPick = null;
		ironwoodAxe = null;
		ironwoodHoe = null;
		torchberries = null;
		venisonRaw = null;
		venisonCooked = null;
		hydraChop = null;
		fieryBlood = null;
		trophy = null;
		fieryIngot = null;
		fieryHelm = null;
		fieryPlate = null;
		fieryLegs = null;
		fieryBoots = null;
		fierySword = null;
		fieryPick = null;
		steeleafIngot = null;
		steeleafHelm = null;
		steeleafPlate = null;
		steeleafLegs = null;
		steeleafBoots = null;
		steeleafSword = null;
		steeleafShovel = null;
		steeleafPick = null;
		steeleafAxe = null;
		steeleafHoe = null;
		minotaurAxe = null;
		mazebreakerPick = null;
		transformPowder = null;
		meefRaw = null;
		meefSteak = null;
		meefStroganoff = null;
		mazeWafer = null;
		emptyMagicMap = null;
		emptyMazeMap = null;
		emptyOreMap = null;
		oreMagnet = null;
		crumbleHorn = null;
		peacockFan = null;
		moonwormQueen = null;
		charmOfLife1 = null;
		charmOfLife2 = null;
		charmOfKeeping1 = null;
		charmOfKeeping2 = null;
		charmOfKeeping3 = null;
		towerKey = null;
		borerEssence = null;
		carminite = null;
		experiment115 = null;
		armorShard = null;
		knightMetal = null;
		shardCluster = null;
		knightlyHelm = null;
		knightlyPlate = null;
		knightlyLegs = null;
		knightlyBoots = null;
		knightlySword = null;
		knightlyPick = null;
		knightlyAxe = null;
		phantomHelm = null;
		phantomPlate = null;
		lampOfCinders = null;
		fieryTears = null;
		alphaFur = null;
		yetiHelm = null;
		yetiPlate = null;
		yetiLegs = null;
		yetiBoots = null;
		iceBomb = null;
		arcticFur = null;
		arcticHelm = null;
		arcticPlate = null;
		arcticLegs = null;
		arcticBoots = null;
		magicBeans = null;
		giantPick = null;
		giantSword = null;
		tripleBow = null;
		seekerBow = null;
		iceBow = null;
		enderBow = null;
		iceSword = null;
		glassSword = null;
		knightmetalRing = null;
		chainBlock = null;
		cubeTalisman = null;
		cubeOfAnnihilation = null;
		miniture_structure = null;
		castleDoor = null;
	}
}