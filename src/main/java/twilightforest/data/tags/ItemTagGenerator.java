package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {
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

	public static final TagKey<Item> PAPER = makeCommonTag("paper");

	public static final TagKey<Item> TOWERWOOD = create("towerwood");

	public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");

	public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
	public static final TagKey<Item> CARMINITE_GEMS = makeCommonTag("gems/carminite");
	public static final TagKey<Item> FIERY_INGOTS = makeCommonTag("ingots/fiery");
	public static final TagKey<Item> IRONWOOD_INGOTS = makeCommonTag("ingots/ironwood");
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = makeCommonTag("ingots/knightmetal");
	public static final TagKey<Item> STEELEAF_INGOTS = makeCommonTag("ingots/steeleaf");

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

	public static final TagKey<Item> REPAIRS_IRONWOOD_TOOLS = create("repairs_ironwood_tools");
	public static final TagKey<Item> REPAIRS_STEELEAF_TOOLS = create("repairs_steeleaf_tools");
	public static final TagKey<Item> REPAIRS_KNIGHTMETAL_TOOLS = create("repairs_knightmetal_tools");
	public static final TagKey<Item> REPAIRS_FIERY_TOOLS = create("repairs_fiery_tools");
	public static final TagKey<Item> REPAIRS_GIANT_TOOLS = create("repairs_giant_tools");
	public static final TagKey<Item> REPAIRS_ICE_TOOLS = create("repairs_ice_tools");
	public static final TagKey<Item> SCEPTERS = create("scepters");

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, helper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);

		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
		this.tag(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		this.tag(ItemTags.LOGS_THAT_BURN)
			.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
			.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);

		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);

		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
		this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);

		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		this.tag(Tags.Items.STORAGE_BLOCKS)
			.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
			.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);
		this.copy(BlockTagGenerator.BANISTERS, BANISTERS);

		this.tag(PAPER).add(Items.PAPER);
		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.get());

		this.tag(FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());

		this.tag(ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		this.tag(CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		this.tag(FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		this.tag(IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		this.tag(KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		this.tag(STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());

		this.tag(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		this.tag(Tags.Items.INGOTS)
			.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
			.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		this.tag(RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		this.tag(RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		this.tag(Tags.Items.RAW_MATERIALS).addTag(RAW_MATERIALS_IRONWOOD).addTag(RAW_MATERIALS_KNIGHTMETAL);

		this.tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);

		this.tag(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT.get(), TFItems.CANOPY_BOAT.get(),
			TFItems.MANGROVE_BOAT.get(), TFItems.DARK_BOAT.get(),
			TFItems.TIME_BOAT.get(), TFItems.TRANSFORMATION_BOAT.get(),
			TFItems.MINING_BOAT.get(), TFItems.SORTING_BOAT.get()
		);

		this.tag(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT.get(), TFItems.CANOPY_CHEST_BOAT.get(),
			TFItems.MANGROVE_CHEST_BOAT.get(), TFItems.DARK_CHEST_BOAT.get(),
			TFItems.TIME_CHEST_BOAT.get(), TFItems.TRANSFORMATION_CHEST_BOAT.get(),
			TFItems.MINING_CHEST_BOAT.get(), TFItems.SORTING_CHEST_BOAT.get()
		);

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_HELMET.get(),
			TFItems.YETI_CHESTPLATE.get(),
			TFItems.YETI_LEGGINGS.get(),
			TFItems.YETI_BOOTS.get()
		);

		this.tag(WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN.get(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN.get(),
			TFItems.CUBE_OF_ANNIHILATION.get(),
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.TERRORCOTTA_ARCS.asItem(),
			TFBlocks.TERRORCOTTA_CURVES.asItem(),
			TFBlocks.TERRORCOTTA_LINES.asItem(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.asItem(),
			TFBlocks.SKULL_CHEST.asItem(),
			TFBlocks.KEEPSAKE_CASKET.asItem(),
			TFBlocks.SINISTER_SPAWNER.asItem(),
			TFBlocks.CORONATION_CARPET.asItem(),
			TFBlocks.WROUGHT_IRON_FENCE.asItem(),
			TFBlocks.CANOPY_WINDOW.asItem(),
			TFBlocks.CANOPY_WINDOW_PANE.asItem(),
			TFBlocks.CANDELABRA.asItem(),
			TFItems.MAGIC_PAINTING.get(),
			TFItems.BRITTLE_FLASK.get(),
			TFItems.GREATER_FLASK.get(),
			TFItems.MYSTIC_CROWN.get(),
			TFItems.CROWN_SPLINTER.get(),
			TFItems.EXANIMATE_ESSENCE.get(),
			TFItems.WROUGHT_IRON_BAR.get(),
			TFBlocks.BRAZIER.asItem()
		);

		this.tag(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.tag(BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(Items.APPLE);
		this.tag(DWARF_RABBIT_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		this.tag(PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.tag(RAVEN_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(SQUIRREL_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TINY_BIRD_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);

		this.tag(BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.get().asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.get().asItem(),
			TFBlocks.TIME_SAPLING.get().asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.get().asItem(),
			TFBlocks.MINING_SAPLING.get().asItem(),
			TFBlocks.SORTING_SAPLING.get().asItem(),
			TFItems.TRANSFORMATION_POWDER.get());

		this.tag(BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.get().asItem());
		this.tag(UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);

		this.tag(KEPT_ON_DEATH).add(TFItems.TOWER_KEY.get(), TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());

		this.tag(SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.get(), TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get());

		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_2.get(), TFItems.LAMP_OF_CINDERS.get());

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_HELMET.get(),
			TFItems.STEELEAF_HELMET.get(),
			TFItems.KNIGHTMETAL_HELMET.get(),
			TFItems.PHANTOM_HELMET.get(),
			TFItems.FIERY_HELMET.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.YETI_HELMET.get());

		this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_CHESTPLATE.get(),
			TFItems.IRONWOOD_CHESTPLATE.get(),
			TFItems.STEELEAF_CHESTPLATE.get(),
			TFItems.KNIGHTMETAL_CHESTPLATE.get(),
			TFItems.PHANTOM_CHESTPLATE.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.YETI_CHESTPLATE.get());

		this.tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_LEGGINGS.get(),
			TFItems.IRONWOOD_LEGGINGS.get(),
			TFItems.STEELEAF_LEGGINGS.get(),
			TFItems.KNIGHTMETAL_LEGGINGS.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.YETI_LEGGINGS.get());

		this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_BOOTS.get(),
			TFItems.STEELEAF_BOOTS.get(),
			TFItems.KNIGHTMETAL_BOOTS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_BOOTS.get());

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.get(),
			TFItems.STEELEAF_SWORD.get(),
			TFItems.KNIGHTMETAL_SWORD.get(),
			TFItems.FIERY_SWORD.get(),
			TFItems.GIANT_SWORD.get(),
			TFItems.ICE_SWORD.get(),
			TFItems.GLASS_SWORD.get());

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		this.tag(ItemTags.AXES).add(TFItems.IRONWOOD_AXE.get(), TFItems.STEELEAF_AXE.get(), TFItems.KNIGHTMETAL_AXE.get(), TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.DIAMOND_MINOTAUR_AXE.get());
		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.get(), TFItems.STEELEAF_SHOVEL.get());
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.get(), TFItems.STEELEAF_HOE.get());
		this.tag(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.get());
		this.tag(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		this.tag(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.get().asItem());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.NAGA_SCALE.get(), TFItems.CARMINITE.get(), TFItems.FIERY_INGOT.get());

		this.tag(REPAIRS_IRONWOOD_TOOLS).addTag(IRONWOOD_INGOTS);
		this.tag(REPAIRS_STEELEAF_TOOLS).addTag(STEELEAF_INGOTS);
		this.tag(REPAIRS_KNIGHTMETAL_TOOLS).addTag(KNIGHTMETAL_INGOTS);
		this.tag(REPAIRS_FIERY_TOOLS).addTag(FIERY_INGOTS);
		this.tag(REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		this.tag(ItemTags.MEAT).add(TFItems.RAW_VENISON.get(), TFItems.COOKED_VENISON.get(), TFItems.RAW_MEEF.get(), TFItems.COOKED_MEEF.get(), TFItems.MEEF_STROGANOFF.get(), TFItems.EXPERIMENT_115.get(), TFItems.HYDRA_CHOP.get());
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(IRONWOOD_INGOTS, STEELEAF_INGOTS, KNIGHTMETAL_INGOTS, FIERY_INGOTS);

		this.tag(ItemTags.TRIMMABLE_ARMOR).remove(TFItems.YETI_HELMET.get());

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.get(),
			TFItems.STEELEAF_HELMET.get(),
			TFItems.KNIGHTMETAL_HELMET.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.YETI_HELMET.get(),
			TFItems.FIERY_HELMET.get(),
			TFItems.PHANTOM_HELMET.get());

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.get(),
			TFItems.STEELEAF_CHESTPLATE.get(),
			TFItems.KNIGHTMETAL_CHESTPLATE.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.YETI_CHESTPLATE.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.PHANTOM_CHESTPLATE.get(),
			TFItems.NAGA_CHESTPLATE.get());

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.get(),
			TFItems.STEELEAF_LEGGINGS.get(),
			TFItems.KNIGHTMETAL_LEGGINGS.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.YETI_LEGGINGS.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.NAGA_LEGGINGS.get());

		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.get(),
			TFItems.STEELEAF_BOOTS.get(),
			TFItems.KNIGHTMETAL_BOOTS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_BOOTS.get(),
			TFItems.FIERY_BOOTS.get());

		this.tag(ItemTags.DYEABLE).add(TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());

		this.tag(BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get(),
			TFItems.BLOCK_AND_CHAIN.get(), TFItems.KNIGHTMETAL_SHIELD.get(), TFItems.ORE_MAGNET.get(),
			TFItems.PEACOCK_FEATHER_FAN.get(), TFItems.CRUMBLE_HORN.get());
		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.get(), TFItems.ICE_SWORD.get());
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.get());

		this.tag(Tags.Items.FOODS_BERRY).add(TFItems.TORCHBERRIES.get());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.get(), TFItems.COOKED_MEEF.get(), TFItems.HYDRA_CHOP.get());
		this.tag(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.get());
		this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.get());
		this.tag(Tags.Items.ROPES).add(TFItems.ROPE.get());
		this.tag(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.get().asItem());
		this.tag(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.get(), TFItems.MUSIC_DISC_STEPS.get(), TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
			TFItems.MUSIC_DISC_HOME.get(), TFItems.MUSIC_DISC_WAYFARER.get(), TFItems.MUSIC_DISC_FINDINGS.get(),
			TFItems.MUSIC_DISC_MAKER.get(), TFItems.MUSIC_DISC_THREAD.get(), TFItems.MUSIC_DISC_MOTION.get()
		);
	}

	public static TagKey<Item> create(String tagName) {
		return ItemTags.create(TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Item> makeCommonTag(String tagName) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tagName));
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
