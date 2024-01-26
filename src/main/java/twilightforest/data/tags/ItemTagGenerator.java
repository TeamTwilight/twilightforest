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

	public static final TagKey<Item> PAPER = makeForgeTag("paper");

	public static final TagKey<Item> TOWERWOOD = create("towerwood");

	public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");

	public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
	public static final TagKey<Item> CARMINITE_GEMS = makeForgeTag("gems/carminite");
	public static final TagKey<Item> FIERY_INGOTS = makeForgeTag("ingots/fiery");
	public static final TagKey<Item> IRONWOOD_INGOTS = makeForgeTag("ingots/ironwood");
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = makeForgeTag("ingots/knightmetal");
	public static final TagKey<Item> STEELEAF_INGOTS = makeForgeTag("ingots/steeleaf");

	public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = makeForgeTag("storage_blocks/arctic_fur");
	public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = makeForgeTag("storage_blocks/carminite");
	public static final TagKey<Item> STORAGE_BLOCKS_FIERY = makeForgeTag("storage_blocks/fiery");
	public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = makeForgeTag("storage_blocks/ironwood");
	public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = makeForgeTag("storage_blocks/knightmetal");
	public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = makeForgeTag("storage_blocks/steeleaf");

	public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = makeForgeTag("raw_materials/ironwood");
	public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = makeForgeTag("raw_materials/knightmetal");

	public static final TagKey<Item> PORTAL_ACTIVATOR = create("portal/activator");

	public static final TagKey<Item> WIP = create("wip");
	public static final TagKey<Item> NYI = create("nyi");

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

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, helper);
	}

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
		tag(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		tag(ItemTags.LOGS_THAT_BURN)
				.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
				.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
		this.copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
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

		tag(Tags.Items.STORAGE_BLOCKS)
				.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
				.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
				.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);

		tag(PAPER).add(Items.PAPER);
		tag(Tags.Items.FEATHERS).add(Items.FEATHER).add(TFItems.RAVEN_FEATHER.get());

		tag(FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());

		tag(ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		tag(CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		tag(FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		tag(IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		tag(KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		tag(STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());

		tag(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		tag(Tags.Items.INGOTS)
				.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
				.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		tag(RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		tag(RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		tag(Tags.Items.RAW_MATERIALS).addTag(RAW_MATERIALS_IRONWOOD).addTag(RAW_MATERIALS_KNIGHTMETAL);

		tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
		
		tag(ItemTags.BOATS).add(
				TFItems.TWILIGHT_OAK_BOAT.get(), TFItems.CANOPY_BOAT.get(),
				TFItems.MANGROVE_BOAT.get(), TFItems.DARK_BOAT.get(),
				TFItems.TIME_BOAT.get(), TFItems.TRANSFORMATION_BOAT.get(),
				TFItems.MINING_BOAT.get(), TFItems.SORTING_BOAT.get()
		);

		tag(ItemTags.CHEST_BOATS).add(
				TFItems.TWILIGHT_OAK_CHEST_BOAT.get(), TFItems.CANOPY_CHEST_BOAT.get(),
				TFItems.MANGROVE_CHEST_BOAT.get(), TFItems.DARK_CHEST_BOAT.get(),
				TFItems.TIME_CHEST_BOAT.get(), TFItems.TRANSFORMATION_CHEST_BOAT.get(),
				TFItems.MINING_CHEST_BOAT.get(), TFItems.SORTING_CHEST_BOAT.get()
		);

		tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
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

		tag(WIP).add(
				TFBlocks.KEEPSAKE_CASKET.get().asItem(),
				TFBlocks.CANDELABRA.get().asItem(),
				TFItems.BRITTLE_FLASK.get(),
				TFItems.GREATER_FLASK.get(),
				TFItems.CUBE_OF_ANNIHILATION.get(),
				TFBlocks.WROUGHT_IRON_FENCE.get().asItem()
		);

		tag(NYI).add(
				TFBlocks.CINDER_FURNACE.get().asItem(),
				TFBlocks.CINDER_LOG.get().asItem(),
				TFBlocks.CINDER_WOOD.get().asItem(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get().asItem(),
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get().asItem(),
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get().asItem(),
				TFBlocks.AURORALIZED_GLASS.get().asItem(),
				TFBlocks.SLIDER.get().asItem(),
				TFItems.MAGIC_PAINTING.get(),
				TFItems.POCKET_WATCH.get(),
				TFItems.QUEST_RAM_BANNER_PATTERN.get()
		);

		tag(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		tag(BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		tag(DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT);
		tag(DWARF_RABBIT_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		tag(PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		tag(RAVEN_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		tag(SQUIRREL_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		tag(TINY_BIRD_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);

		tag(ItemTags.MUSIC_DISCS).add(
				TFItems.MUSIC_DISC_FINDINGS.get(),
				TFItems.MUSIC_DISC_HOME.get(),
				TFItems.MUSIC_DISC_MAKER.get(),
				TFItems.MUSIC_DISC_MOTION.get(),
				TFItems.MUSIC_DISC_RADIANCE.get(),
				TFItems.MUSIC_DISC_STEPS.get(),
				TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
				TFItems.MUSIC_DISC_THREAD.get(),
				TFItems.MUSIC_DISC_WAYFARER.get());

		tag(BANNED_UNCRAFTING_INGREDIENTS).add(
				TFBlocks.INFESTED_TOWERWOOD.get().asItem(),
				TFBlocks.HOLLOW_OAK_SAPLING.get().asItem(),
				TFBlocks.TIME_SAPLING.get().asItem(),
				TFBlocks.TRANSFORMATION_SAPLING.get().asItem(),
				TFBlocks.MINING_SAPLING.get().asItem(),
				TFBlocks.SORTING_SAPLING.get().asItem(),
				TFItems.TRANSFORMATION_POWDER.get());

		tag(BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.get().asItem());
		tag(UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);

		tag(KEPT_ON_DEATH).add(TFItems.TOWER_KEY.get(), TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());

		tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_2.get(), TFItems.LAMP_OF_CINDERS.get());

		tag(Tags.Items.HEADS).add(
				TFItems.ZOMBIE_SKULL_CANDLE.get(),
				TFItems.SKELETON_SKULL_CANDLE.get(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
				TFItems.CREEPER_SKULL_CANDLE.get(),
				TFItems.PLAYER_SKULL_CANDLE.get(),
				TFItems.PIGLIN_SKULL_CANDLE.get());

		tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
				TFItems.ZOMBIE_SKULL_CANDLE.get(),
				TFItems.SKELETON_SKULL_CANDLE.get(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
				TFItems.CREEPER_SKULL_CANDLE.get(),
				TFItems.PLAYER_SKULL_CANDLE.get(),
				TFItems.PIGLIN_SKULL_CANDLE.get());

		tag(Tags.Items.ARMORS_HELMETS).add(
				TFItems.IRONWOOD_HELMET.get(),
				TFItems.STEELEAF_HELMET.get(),
				TFItems.KNIGHTMETAL_HELMET.get(),
				TFItems.PHANTOM_HELMET.get(),
				TFItems.FIERY_HELMET.get(),
				TFItems.ARCTIC_HELMET.get(),
				TFItems.YETI_HELMET.get());
		
		tag(Tags.Items.ARMORS_CHESTPLATES).add(
				TFItems.NAGA_CHESTPLATE.get(),
				TFItems.IRONWOOD_CHESTPLATE.get(),
				TFItems.STEELEAF_CHESTPLATE.get(),
				TFItems.KNIGHTMETAL_CHESTPLATE.get(),
				TFItems.PHANTOM_CHESTPLATE.get(),
				TFItems.FIERY_CHESTPLATE.get(),
				TFItems.ARCTIC_CHESTPLATE.get(),
				TFItems.YETI_CHESTPLATE.get());
		
		tag(Tags.Items.ARMORS_LEGGINGS).add(
				TFItems.NAGA_LEGGINGS.get(),
				TFItems.IRONWOOD_LEGGINGS.get(),
				TFItems.STEELEAF_LEGGINGS.get(),
				TFItems.KNIGHTMETAL_LEGGINGS.get(),
				TFItems.FIERY_LEGGINGS.get(),
				TFItems.ARCTIC_LEGGINGS.get(),
				TFItems.YETI_LEGGINGS.get());
		
		tag(Tags.Items.ARMORS_BOOTS).add(
				TFItems.IRONWOOD_BOOTS.get(),
				TFItems.STEELEAF_BOOTS.get(),
				TFItems.KNIGHTMETAL_BOOTS.get(),
				TFItems.FIERY_BOOTS.get(),
				TFItems.ARCTIC_BOOTS.get(),
				TFItems.YETI_BOOTS.get());
		
		tag(ItemTags.SWORDS).add(
				TFItems.IRONWOOD_SWORD.get(),
				TFItems.STEELEAF_SWORD.get(),
				TFItems.KNIGHTMETAL_SWORD.get(),
				TFItems.FIERY_SWORD.get(),
				TFItems.GIANT_SWORD.get(),
				TFItems.ICE_SWORD.get(),
				TFItems.GLASS_SWORD.get());

		tag(ItemTags.PICKAXES).add(
				TFItems.IRONWOOD_PICKAXE.get(),
				TFItems.STEELEAF_PICKAXE.get(),
				TFItems.KNIGHTMETAL_PICKAXE.get(),
				TFItems.MAZEBREAKER_PICKAXE.get(),
				TFItems.FIERY_PICKAXE.get(),
				TFItems.GIANT_PICKAXE.get());

		tag(ItemTags.AXES).add(TFItems.IRONWOOD_AXE.get(), TFItems.STEELEAF_AXE.get(), TFItems.KNIGHTMETAL_AXE.get());
		tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.get(), TFItems.STEELEAF_SHOVEL.get());
		tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.get(), TFItems.STEELEAF_HOE.get());
		tag(Tags.Items.TOOLS_SHIELDS).add(TFItems.KNIGHTMETAL_SHIELD.get());
		tag(Tags.Items.TOOLS_BOWS).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());

		tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
				TFItems.IRONWOOD_PICKAXE.get(),
				TFItems.STEELEAF_PICKAXE.get(),
				TFItems.KNIGHTMETAL_PICKAXE.get(),
				TFItems.MAZEBREAKER_PICKAXE.get(),
				TFItems.FIERY_PICKAXE.get(),
				TFItems.GIANT_PICKAXE.get());

		tag(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.get().asItem());

		tag(ItemTags.TRIMMABLE_ARMOR)
				.add(TFItems.IRONWOOD_HELMET.get(), TFItems.IRONWOOD_CHESTPLATE.get(), TFItems.IRONWOOD_LEGGINGS.get(), TFItems.IRONWOOD_BOOTS.get())
				.add(TFItems.STEELEAF_HELMET.get(), TFItems.STEELEAF_CHESTPLATE.get(), TFItems.STEELEAF_LEGGINGS.get(), TFItems.STEELEAF_BOOTS.get())
				.add(TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get())
				.add(TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get())
				//due to yeti helmets being bigger than normal helmets trims won't work properly on it. If we ever decide to change the model we can add trim compatibility
				.add(/*TFItems.YETI_HELMET.get(),*/ TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get())
				.add(TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get())
				.add(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get(), TFItems.NAGA_CHESTPLATE.get(), TFItems.NAGA_LEGGINGS.get());

		tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.NAGA_SCALE.get(), TFItems.CARMINITE.get(), TFItems.FIERY_INGOT.get());
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}

	public static TagKey<Item> create(String tagName) {
		return ItemTags.create(TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Item> makeForgeTag(String tagName) {
		return ItemTags.create(new ResourceLocation("forge", tagName));
	}
}
