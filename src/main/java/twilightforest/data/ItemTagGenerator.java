package twilightforest.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class ItemTagGenerator extends ItemTagsProvider {
	public static final ITag.INamedTag<Item> TWILIGHT_OAK_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("twilight_oak_logs").toString());
	public static final ITag.INamedTag<Item> CANOPY_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("canopy_logs").toString());
	public static final ITag.INamedTag<Item> MANGROVE_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("mangrove_logs").toString());
	public static final ITag.INamedTag<Item> DARKWOOD_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("darkwood_logs").toString());
	public static final ITag.INamedTag<Item> TIME_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("timewood_logs").toString());
	public static final ITag.INamedTag<Item> TRANSFORMATION_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("transwood_logs").toString());
	public static final ITag.INamedTag<Item> MINING_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("mining_logs").toString());
	public static final ITag.INamedTag<Item> SORTING_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("sortwood_logs").toString());

	public static final ITag.INamedTag<Item> TWILIGHT_LOGS = ItemTags.makeWrapperTag(TwilightForestMod.prefix("logs").toString());
	public static final ITag.INamedTag<Item> TF_FENCES = ItemTags.makeWrapperTag(TwilightForestMod.prefix("fences").toString());
	public static final ITag.INamedTag<Item> TF_FENCE_GATES = ItemTags.makeWrapperTag(TwilightForestMod.prefix("fence_gates").toString());

	public static final ITag.INamedTag<Item> PAPER = ItemTags.makeWrapperTag("forge:paper");

	public static final ITag.INamedTag<Item> TOWERWOOD = ItemTags.makeWrapperTag(TwilightForestMod.prefix("towerwood").toString());

	public static final ITag.INamedTag<Item> FIERY_VIAL = ItemTags.makeWrapperTag(TwilightForestMod.prefix("fiery_vial").toString());

	public static final ITag.INamedTag<Item> ARCTIC_FUR = ItemTags.makeWrapperTag(TwilightForestMod.prefix("arctic_fur").toString());
	public static final ITag.INamedTag<Item> CARMINITE_GEMS = ItemTags.makeWrapperTag("forge:gems/carminite");
	public static final ITag.INamedTag<Item> FIERY_INGOTS = ItemTags.makeWrapperTag("forge:ingots/fiery");
	public static final ITag.INamedTag<Item> IRONWOOD_INGOTS = ItemTags.makeWrapperTag("forge:ingots/ironwood");
	public static final ITag.INamedTag<Item> KNIGHTMETAL_INGOTS = ItemTags.makeWrapperTag("forge:ingots/knightmetal");
	public static final ITag.INamedTag<Item> STEELEAF_INGOTS = ItemTags.makeWrapperTag("forge:ingots/steeleaf");

	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_ARCTIC_FUR = ItemTags.makeWrapperTag("forge:storage_blocks/arctic_fur");
	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_CARMINITE = ItemTags.makeWrapperTag("forge:storage_blocks/carminite");
	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_FIERY = ItemTags.makeWrapperTag("forge:storage_blocks/fiery");
	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_IRONWOOD = ItemTags.makeWrapperTag("forge:storage_blocks/ironwood");
	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_KNIGHTMETAL = ItemTags.makeWrapperTag("forge:storage_blocks/knightmetal");
	public static final ITag.INamedTag<Item> STORAGE_BLOCKS_STEELEAF = ItemTags.makeWrapperTag("forge:storage_blocks/steeleaf");

	public static final ITag.INamedTag<Item> ORES_IRONWOOD = ItemTags.makeWrapperTag("forge:ores/ironwood");
	public static final ITag.INamedTag<Item> ORES_KNIGHTMETAL = ItemTags.makeWrapperTag("forge:ores/knightmetal");

	public static final ITag.INamedTag<Item> PORTAL_ACTIVATOR = ItemTags.makeWrapperTag(TwilightForestMod.prefix("portal/activator").toString());

	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockprovider, ExistingFileHelper exFileHelper) {
		super(generator, blockprovider, TwilightForestMod.ID, exFileHelper);
	}

	@Override
	protected void registerTags() {
		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);

		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
		getOrCreateBuilder(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		getOrCreateBuilder(ItemTags.LOGS_THAT_BURN)
				.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
				.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTagGenerator.TF_FENCES, TF_FENCES);
		this.copy(BlockTagGenerator.TF_FENCE_GATES, TF_FENCE_GATES);
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
		getOrCreateBuilder(ItemTags.SIGNS).add(TFBlocks.twilight_oak_sign.get().asItem(), TFBlocks.canopy_sign.get().asItem(),
				TFBlocks.mangrove_sign.get().asItem(), TFBlocks.darkwood_sign.get().asItem(),
				TFBlocks.time_sign.get().asItem(), TFBlocks.trans_sign.get().asItem(),
				TFBlocks.mine_sign.get().asItem(), TFBlocks.sort_sign.get().asItem());

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		getOrCreateBuilder(Tags.Items.STORAGE_BLOCKS)
				.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
				.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
				.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.ORES_IRONWOOD, ORES_IRONWOOD);
		this.copy(BlockTagGenerator.ORES_KNIGHTMETAL, ORES_KNIGHTMETAL);

		getOrCreateBuilder(Tags.Items.ORES).addTag(ORES_IRONWOOD).addTag(ORES_KNIGHTMETAL);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);

		getOrCreateBuilder(PAPER).add(Items.PAPER);
		getOrCreateBuilder(Tags.Items.FEATHERS).add(Items.FEATHER).add(TFItems.raven_feather.get());

		getOrCreateBuilder(FIERY_VIAL).add(TFItems.fiery_blood.get(), TFItems.fiery_tears.get());

		getOrCreateBuilder(ARCTIC_FUR).add(TFItems.arctic_fur.get());
		getOrCreateBuilder(CARMINITE_GEMS).add(TFItems.carminite.get());
		getOrCreateBuilder(FIERY_INGOTS).add(TFItems.fiery_ingot.get());
		getOrCreateBuilder(IRONWOOD_INGOTS).add(TFItems.ironwood_ingot.get());
		getOrCreateBuilder(KNIGHTMETAL_INGOTS).add(TFItems.knightmetal_ingot.get());
		getOrCreateBuilder(STEELEAF_INGOTS).add(TFItems.steeleaf_ingot.get());

		getOrCreateBuilder(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		getOrCreateBuilder(Tags.Items.INGOTS)
				.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
				.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		getOrCreateBuilder(ORES_IRONWOOD).add(TFItems.ironwood_raw.get());
		getOrCreateBuilder(ORES_KNIGHTMETAL).add(TFItems.armor_shard_cluster.get());

		getOrCreateBuilder(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
	}
}
