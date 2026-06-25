package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.tags.TFBlockTags;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);

		// Log tags - defined directly because HolderLookup.Provider does not have tag data at datagen time
		this.tag(TFItemTags.TWILIGHT_OAK_LOGS).add(
			TFBlocks.TWILIGHT_OAK_LOG.get().asItem(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get().asItem(),
			TFBlocks.TWILIGHT_OAK_WOOD.get().asItem(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get().asItem()
		);
		this.tag(TFItemTags.CANOPY_LOGS).add(
			TFBlocks.CANOPY_LOG.get().asItem(),
			TFBlocks.STRIPPED_CANOPY_LOG.get().asItem(),
			TFBlocks.CANOPY_WOOD.get().asItem(),
			TFBlocks.STRIPPED_CANOPY_WOOD.get().asItem()
		);
		this.tag(TFItemTags.MANGROVE_LOGS).add(
			TFBlocks.MANGROVE_LOG.get().asItem(),
			TFBlocks.STRIPPED_MANGROVE_LOG.get().asItem(),
			TFBlocks.MANGROVE_WOOD.get().asItem(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.get().asItem()
		);
		this.tag(TFItemTags.DARKWOOD_LOGS).add(
			TFBlocks.DARK_LOG.get().asItem(),
			TFBlocks.STRIPPED_DARK_LOG.get().asItem(),
			TFBlocks.DARK_WOOD.get().asItem(),
			TFBlocks.STRIPPED_DARK_WOOD.get().asItem()
		);
		this.tag(TFItemTags.TIME_LOGS).add(
			TFBlocks.TIME_LOG.get().asItem(),
			TFBlocks.STRIPPED_TIME_LOG.get().asItem(),
			TFBlocks.TIME_WOOD.get().asItem(),
			TFBlocks.STRIPPED_TIME_WOOD.get().asItem()
		);
		this.tag(TFItemTags.TRANSFORMATION_LOGS).add(
			TFBlocks.TRANSFORMATION_LOG.get().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.get().asItem(),
			TFBlocks.TRANSFORMATION_WOOD.get().asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get().asItem()
		);
		this.tag(TFItemTags.MINING_LOGS).add(
			TFBlocks.MINING_LOG.get().asItem(),
			TFBlocks.STRIPPED_MINING_LOG.get().asItem(),
			TFBlocks.MINING_WOOD.get().asItem(),
			TFBlocks.STRIPPED_MINING_WOOD.get().asItem()
		);
		this.tag(TFItemTags.SORTING_LOGS).add(
			TFBlocks.SORTING_LOG.get().asItem(),
			TFBlocks.STRIPPED_SORTING_LOG.get().asItem(),
			TFBlocks.SORTING_WOOD.get().asItem(),
			TFBlocks.STRIPPED_SORTING_WOOD.get().asItem()
		);

		this.tag(TFItemTags.TWILIGHT_LOGS)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS)
			.addTag(TFItemTags.MANGROVE_LOGS).addTag(TFItemTags.DARKWOOD_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS)
			.addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);
		this.tag(ItemTags.LOGS).addTag(TFItemTags.TWILIGHT_LOGS);
		this.tag(ItemTags.LOGS_THAT_BURN)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS).addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS).addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);

		// Vanilla block to item tag copies - defined directly for consistency
		this.tag(ItemTags.SAPLINGS)
			.add(TFBlocks.TWILIGHT_OAK_SAPLING.get().asItem())
			.add(TFBlocks.CANOPY_SAPLING.get().asItem())
			.add(TFBlocks.MANGROVE_SAPLING.get().asItem())
			.add(TFBlocks.DARKWOOD_SAPLING.get().asItem())
			.add(TFBlocks.TIME_SAPLING.get().asItem())
			.add(TFBlocks.TRANSFORMATION_SAPLING.get().asItem())
			.add(TFBlocks.MINING_SAPLING.get().asItem())
			.add(TFBlocks.SORTING_SAPLING.get().asItem())
			.add(TFBlocks.HOLLOW_OAK_SAPLING.get().asItem())
			.add(TFBlocks.RAINBOW_OAK_SAPLING.get().asItem());

		this.tag(ItemTags.LEAVES)
			.add(TFBlocks.RAINBOW_OAK_LEAVES.get().asItem())
			.add(TFBlocks.TWILIGHT_OAK_LEAVES.get().asItem())
			.add(TFBlocks.CANOPY_LEAVES.get().asItem())
			.add(TFBlocks.MANGROVE_LEAVES.get().asItem())
			.add(TFBlocks.DARK_LEAVES.get().asItem())
			.add(TFBlocks.TIME_LEAVES.get().asItem())
			.add(TFBlocks.TRANSFORMATION_LEAVES.get().asItem())
			.add(TFBlocks.MINING_LEAVES.get().asItem())
			.add(TFBlocks.SORTING_LEAVES.get().asItem())
			.add(TFBlocks.THORN_LEAVES.get().asItem())
			.add(TFBlocks.BEANSTALK_LEAVES.get().asItem());

		this.tag(ItemTags.PLANKS)
			.add(TFBlocks.TWILIGHT_OAK_PLANKS.get().asItem())
			.add(TFBlocks.CANOPY_PLANKS.get().asItem())
			.add(TFBlocks.MANGROVE_PLANKS.get().asItem())
			.add(TFBlocks.DARK_PLANKS.get().asItem())
			.add(TFBlocks.TIME_PLANKS.get().asItem())
			.add(TFBlocks.TRANSFORMATION_PLANKS.get().asItem())
			.add(TFBlocks.MINING_PLANKS.get().asItem())
			.add(TFBlocks.SORTING_PLANKS.get().asItem())
			.add(TFBlocks.TOWERWOOD.get().asItem())
			.add(TFBlocks.MOSSY_TOWERWOOD.get().asItem())
			.add(TFBlocks.CRACKED_TOWERWOOD.get().asItem())
			.add(TFBlocks.INFESTED_TOWERWOOD.get().asItem());

		this.tag(ItemTags.WOODEN_FENCES)
			.add(TFBlocks.TWILIGHT_OAK_FENCE.get().asItem())
			.add(TFBlocks.CANOPY_FENCE.get().asItem())
			.add(TFBlocks.MANGROVE_FENCE.get().asItem())
			.add(TFBlocks.DARK_FENCE.get().asItem())
			.add(TFBlocks.TIME_FENCE.get().asItem())
			.add(TFBlocks.TRANSFORMATION_FENCE.get().asItem())
			.add(TFBlocks.MINING_FENCE.get().asItem())
			.add(TFBlocks.SORTING_FENCE.get().asItem());

		this.tag(ItemTags.FENCE_GATES)
			.add(TFBlocks.TWILIGHT_OAK_GATE.get().asItem())
			.add(TFBlocks.CANOPY_GATE.get().asItem())
			.add(TFBlocks.MANGROVE_GATE.get().asItem())
			.add(TFBlocks.DARK_GATE.get().asItem())
			.add(TFBlocks.TIME_GATE.get().asItem())
			.add(TFBlocks.TRANSFORMATION_GATE.get().asItem())
			.add(TFBlocks.MINING_GATE.get().asItem())
			.add(TFBlocks.SORTING_GATE.get().asItem());

		this.tag(Tags.Items.FENCE_GATES_WOODEN)
			.add(TFBlocks.TWILIGHT_OAK_GATE.get().asItem())
			.add(TFBlocks.CANOPY_GATE.get().asItem())
			.add(TFBlocks.MANGROVE_GATE.get().asItem())
			.add(TFBlocks.DARK_GATE.get().asItem())
			.add(TFBlocks.TIME_GATE.get().asItem())
			.add(TFBlocks.TRANSFORMATION_GATE.get().asItem())
			.add(TFBlocks.MINING_GATE.get().asItem())
			.add(TFBlocks.SORTING_GATE.get().asItem());

		this.tag(ItemTags.WOODEN_SLABS)
			.add(TFBlocks.TWILIGHT_OAK_SLAB.get().asItem())
			.add(TFBlocks.CANOPY_SLAB.get().asItem())
			.add(TFBlocks.MANGROVE_SLAB.get().asItem())
			.add(TFBlocks.DARK_SLAB.get().asItem())
			.add(TFBlocks.TIME_SLAB.get().asItem())
			.add(TFBlocks.TRANSFORMATION_SLAB.get().asItem())
			.add(TFBlocks.MINING_SLAB.get().asItem())
			.add(TFBlocks.SORTING_SLAB.get().asItem());

		this.tag(ItemTags.SLABS).add(TFBlocks.AURORA_SLAB.get().asItem());

		this.tag(ItemTags.WOODEN_STAIRS)
			.add(TFBlocks.TWILIGHT_OAK_STAIRS.get().asItem())
			.add(TFBlocks.CANOPY_STAIRS.get().asItem())
			.add(TFBlocks.MANGROVE_STAIRS.get().asItem())
			.add(TFBlocks.DARK_STAIRS.get().asItem())
			.add(TFBlocks.TIME_STAIRS.get().asItem())
			.add(TFBlocks.TRANSFORMATION_STAIRS.get().asItem())
			.add(TFBlocks.MINING_STAIRS.get().asItem())
			.add(TFBlocks.SORTING_STAIRS.get().asItem());

		this.tag(ItemTags.STAIRS)
			.add(TFBlocks.CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get().asItem())
			.add(TFBlocks.NAGASTONE_STAIRS_LEFT.get().asItem())
			.add(TFBlocks.NAGASTONE_STAIRS_RIGHT.get().asItem())
			.add(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get().asItem())
			.add(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get().asItem())
			.add(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get().asItem())
			.add(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get().asItem());

		this.tag(ItemTags.WOODEN_BUTTONS)
			.add(TFBlocks.TWILIGHT_OAK_BUTTON.get().asItem())
			.add(TFBlocks.CANOPY_BUTTON.get().asItem())
			.add(TFBlocks.MANGROVE_BUTTON.get().asItem())
			.add(TFBlocks.DARK_BUTTON.get().asItem())
			.add(TFBlocks.TIME_BUTTON.get().asItem())
			.add(TFBlocks.TRANSFORMATION_BUTTON.get().asItem())
			.add(TFBlocks.MINING_BUTTON.get().asItem())
			.add(TFBlocks.SORTING_BUTTON.get().asItem());

		this.tag(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TFBlocks.TWILIGHT_OAK_PLATE.get().asItem())
			.add(TFBlocks.CANOPY_PLATE.get().asItem())
			.add(TFBlocks.MANGROVE_PLATE.get().asItem())
			.add(TFBlocks.DARK_PLATE.get().asItem())
			.add(TFBlocks.TIME_PLATE.get().asItem())
			.add(TFBlocks.TRANSFORMATION_PLATE.get().asItem())
			.add(TFBlocks.MINING_PLATE.get().asItem())
			.add(TFBlocks.SORTING_PLATE.get().asItem());

		this.tag(ItemTags.WOODEN_TRAPDOORS)
			.add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get().asItem())
			.add(TFBlocks.CANOPY_TRAPDOOR.get().asItem())
			.add(TFBlocks.MANGROVE_TRAPDOOR.get().asItem())
			.add(TFBlocks.DARK_TRAPDOOR.get().asItem())
			.add(TFBlocks.TIME_TRAPDOOR.get().asItem())
			.add(TFBlocks.TRANSFORMATION_TRAPDOOR.get().asItem())
			.add(TFBlocks.MINING_TRAPDOOR.get().asItem())
			.add(TFBlocks.SORTING_TRAPDOOR.get().asItem());

		this.tag(ItemTags.WOODEN_DOORS)
			.add(TFBlocks.TWILIGHT_OAK_DOOR.get().asItem())
			.add(TFBlocks.CANOPY_DOOR.get().asItem())
			.add(TFBlocks.MANGROVE_DOOR.get().asItem())
			.add(TFBlocks.DARK_DOOR.get().asItem())
			.add(TFBlocks.TIME_DOOR.get().asItem())
			.add(TFBlocks.TRANSFORMATION_DOOR.get().asItem())
			.add(TFBlocks.MINING_DOOR.get().asItem())
			.add(TFBlocks.SORTING_DOOR.get().asItem());

		this.tag(ItemTags.HANGING_SIGNS)
			.add(TFItems.TWILIGHT_OAK_HANGING_SIGN.get())
			.add(TFItems.CANOPY_HANGING_SIGN.get())
			.add(TFItems.MANGROVE_HANGING_SIGN.get())
			.add(TFItems.DARK_HANGING_SIGN.get())
			.add(TFItems.TIME_HANGING_SIGN.get())
			.add(TFItems.TRANSFORMATION_HANGING_SIGN.get())
			.add(TFItems.MINING_HANGING_SIGN.get())
			.add(TFItems.SORTING_HANGING_SIGN.get());

		this.tag(ItemTags.SIGNS)
			.add(TFItems.TWILIGHT_OAK_SIGN.get())
			.add(TFItems.CANOPY_SIGN.get())
			.add(TFItems.MANGROVE_SIGN.get())
			.add(TFItems.DARK_SIGN.get())
			.add(TFItems.TIME_SIGN.get())
			.add(TFItems.TRANSFORMATION_SIGN.get())
			.add(TFItems.MINING_SIGN.get())
			.add(TFItems.SORTING_SIGN.get());

		this.tag(Tags.Items.CHESTS_WOODEN)
			.add(TFBlocks.TWILIGHT_OAK_CHEST.get().asItem())
			.add(TFBlocks.CANOPY_CHEST.get().asItem())
			.add(TFBlocks.MANGROVE_CHEST.get().asItem())
			.add(TFBlocks.DARK_CHEST.get().asItem())
			.add(TFBlocks.TIME_CHEST.get().asItem())
			.add(TFBlocks.TRANSFORMATION_CHEST.get().asItem())
			.add(TFBlocks.MINING_CHEST.get().asItem())
			.add(TFBlocks.SORTING_CHEST.get().asItem());

		this.tag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.get().asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.get().asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.get().asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.get().asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.get().asItem());
		this.tag(TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.get().asItem());

		this.tag(Tags.Items.STORAGE_BLOCKS)
			.addTag(TFItemTags.STORAGE_BLOCKS_FIERY).addTag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(TFItemTags.STORAGE_BLOCKS_CARMINITE).addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(TFItemTags.TOWERWOOD)
			.add(TFBlocks.TOWERWOOD.get().asItem())
			.add(TFBlocks.MOSSY_TOWERWOOD.get().asItem())
			.add(TFBlocks.CRACKED_TOWERWOOD.get().asItem())
			.add(TFBlocks.INFESTED_TOWERWOOD.get().asItem());

		this.tag(TFItemTags.BANISTERS)
			.add(TFBlocks.OAK_BANISTER.get().asItem())
			.add(TFBlocks.SPRUCE_BANISTER.get().asItem())
			.add(TFBlocks.BIRCH_BANISTER.get().asItem())
			.add(TFBlocks.JUNGLE_BANISTER.get().asItem())
			.add(TFBlocks.ACACIA_BANISTER.get().asItem())
			.add(TFBlocks.DARK_OAK_BANISTER.get().asItem())
			.add(TFBlocks.CRIMSON_BANISTER.get().asItem())
			.add(TFBlocks.WARPED_BANISTER.get().asItem())
			.add(TFBlocks.VANGROVE_BANISTER.get().asItem())
			.add(TFBlocks.BAMBOO_BANISTER.get().asItem())
			.add(TFBlocks.CHERRY_BANISTER.get().asItem())
			.add(TFBlocks.PALE_OAK_BANISTER.get().asItem())
			.add(TFBlocks.TWILIGHT_OAK_BANISTER.get().asItem())
			.add(TFBlocks.CANOPY_BANISTER.get().asItem())
			.add(TFBlocks.MANGROVE_BANISTER.get().asItem())
			.add(TFBlocks.DARK_BANISTER.get().asItem())
			.add(TFBlocks.TIME_BANISTER.get().asItem())
			.add(TFBlocks.TRANSFORMATION_BANISTER.get().asItem())
			.add(TFBlocks.MINING_BANISTER.get().asItem())
			.add(TFBlocks.SORTING_BANISTER.get().asItem());

		this.tag(TFItemTags.PAPER).add(Items.PAPER);
		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.get());

		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());

		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.get());

		this.tag(Tags.Items.GEMS).addTag(TFItemTags.CARMINITE_GEMS);

		this.tag(Tags.Items.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		this.tag(Tags.Items.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);

		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);

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

		this.tag(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN.get(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN.get(),
			TFItems.CUBE_OF_ANNIHILATION.get(),
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.BRAZIER.asItem(),
			TFBlocks.MAZE_SLIME_BLOCK.asItem()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(Items.APPLE);
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);

		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.get().asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.get().asItem(),
			TFBlocks.TIME_SAPLING.get().asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.get().asItem(),
			TFBlocks.MINING_SAPLING.get().asItem(),
			TFBlocks.SORTING_SAPLING.get().asItem(),
			TFItems.TRANSFORMATION_POWDER.get());

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.get().asItem());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);

		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY.get(), TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());

		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.get(), TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get());

		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());

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

		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		this.tag(ItemTags.MEAT).add(TFItems.RAW_VENISON.get(), TFItems.COOKED_VENISON.get(), TFItems.RAW_MEEF.get(), TFItems.COOKED_MEEF.get(), TFItems.MEEF_STROGANOFF.get(), TFItems.EXPERIMENT_115.get(), TFItems.HYDRA_CHOP.get());
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(TFItemTags.IRONWOOD_INGOTS, TFItemTags.STEELEAF_INGOTS, TFItemTags.KNIGHTMETAL_INGOTS, TFItemTags.FIERY_INGOTS);

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

		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
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

	private void copy(HolderLookup.Provider provider, TagKey<Block> blockTag, TagKey<Item> itemTag) {
		TagAppender<Item, Item> appender = this.tag(itemTag);
		provider.lookupOrThrow(Registries.BLOCK).listElements()
			.filter(holder -> holder.is(blockTag))
			.forEach(holder -> appender.add(holder.value().asItem()));
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
