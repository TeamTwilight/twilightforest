package twilightforest.data.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

import static twilightforest.TFConstants.MOD_ID;

public class ItemTagGenerator extends ItemTagsProvider {
	public static final TagKey<Item> TWILIGHT_OAK_LOGS = ItemTags.create(TwilightForestMod.prefix("twilight_oak_logs"));
	public static final TagKey<Item> CANOPY_LOGS = ItemTags.create(TwilightForestMod.prefix("canopy_logs"));
	public static final TagKey<Item> MANGROVE_LOGS = ItemTags.create(TwilightForestMod.prefix("mangrove_logs"));
	public static final TagKey<Item> DARKWOOD_LOGS = ItemTags.create(TwilightForestMod.prefix("darkwood_logs"));
	public static final TagKey<Item> TIME_LOGS = ItemTags.create(TwilightForestMod.prefix("timewood_logs"));
	public static final TagKey<Item> TRANSFORMATION_LOGS = ItemTags.create(TwilightForestMod.prefix("transwood_logs"));
	public static final TagKey<Item> MINING_LOGS = ItemTags.create(TwilightForestMod.prefix("mining_logs"));
	public static final TagKey<Item> SORTING_LOGS = ItemTags.create(TwilightForestMod.prefix("sortwood_logs"));

	public static final TagKey<Item> TWILIGHT_LOGS = ItemTags.create(TwilightForestMod.prefix("logs"));
	public static final TagKey<Item> TF_FENCES = ItemTags.create(TwilightForestMod.prefix("fences"));
	public static final TagKey<Item> TF_FENCE_GATES = ItemTags.create(TwilightForestMod.prefix("fence_gates"));

	public static final TagKey<Item> PAPER = ItemTags.create(new ResourceLocation("forge", "paper"));

	public static final TagKey<Item> TOWERWOOD = ItemTags.create(TwilightForestMod.prefix("towerwood"));

	public static final TagKey<Item> FIERY_VIAL = ItemTags.create(TwilightForestMod.prefix("fiery_vial"));

	public static final TagKey<Item> ARCTIC_FUR = ItemTags.create(TwilightForestMod.prefix("arctic_fur"));
	public static final TagKey<Item> CARMINITE_GEMS = ItemTags.create(new ResourceLocation("forge", "gems/carminite"));
	public static final TagKey<Item> FIERY_INGOTS = ItemTags.create(new ResourceLocation("forge", "ingots/fiery"));
	public static final TagKey<Item> IRONWOOD_INGOTS = ItemTags.create(new ResourceLocation("forge", "ingots/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = ItemTags.create(new ResourceLocation("forge", "ingots/knightmetal"));
	public static final TagKey<Item> STEELEAF_INGOTS = ItemTags.create(new ResourceLocation("forge", "ingots/steeleaf"));

	public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = ItemTags.create(new ResourceLocation("forge", "storage_blocks/arctic_fur"));
	public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = ItemTags.create(new ResourceLocation("forge", "storage_blocks/carminite"));
	public static final TagKey<Item> STORAGE_BLOCKS_FIERY = ItemTags.create(new ResourceLocation("forge", "storage_blocks/fiery"));
	public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = ItemTags.create(new ResourceLocation("forge", "storage_blocks/ironwood"));
	public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = ItemTags.create(new ResourceLocation("forge", "storage_blocks/knightmetal"));
	public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = ItemTags.create(new ResourceLocation("forge", "storage_blocks/steeleaf"));

	public static final TagKey<Item> ORES_IRONWOOD = ItemTags.create(new ResourceLocation("forge", "ores/ironwood"));
	public static final TagKey<Item> ORES_KNIGHTMETAL = ItemTags.create(new ResourceLocation("forge", "ores/knightmetal"));

	public static final TagKey<Item> PORTAL_ACTIVATOR = ItemTags.create(TwilightForestMod.prefix("portal/activator"));

	public static final TagKey<Item> WIP = ItemTags.create(TwilightForestMod.prefix("wip"));
	public static final TagKey<Item> NYI = ItemTags.create(TwilightForestMod.prefix("nyi"));

	public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = ItemTags.create(TwilightForestMod.prefix("kobold_pacification_breads"));

	public static final TagKey<Item> TF_MUSIC_DISCS = ItemTags.create(TwilightForestMod.prefix("tf_music_discs"));

	public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = ItemTags.create(TwilightForestMod.prefix("banned_uncrafting_ingredients"));
	public static final TagKey<Item> BANNED_UNCRAFTABLES = ItemTags.create(TwilightForestMod.prefix("banned_uncraftables"));

	private static final TagKey<Item> CHARM = ItemTags.create(new ResourceLocation("curios", "charm"));
	private static final TagKey<Item> HEAD = ItemTags.create(new ResourceLocation("curios", "head"));

	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockprovider, ExistingFileHelper exFileHelper) {
		super(generator, blockprovider, MOD_ID, exFileHelper);
	}

	@Override
	protected void addTags() {
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
		tag(ItemTags.SIGNS).add(TFBlocks.TWILIGHT_OAK_SIGN.get().asItem(), TFBlocks.CANOPY_SIGN.get().asItem(),
				TFBlocks.MANGROVE_SIGN.get().asItem(), TFBlocks.DARKWOOD_SIGN.get().asItem(),
				TFBlocks.TIME_SIGN.get().asItem(), TFBlocks.TRANSFORMATION_SIGN.get().asItem(),
				TFBlocks.MINING_SIGN.get().asItem(), TFBlocks.SORTING_SIGN.get().asItem());

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

		this.copy(BlockTagGenerator.ORES_IRONWOOD, ORES_IRONWOOD);
		this.copy(BlockTagGenerator.ORES_KNIGHTMETAL, ORES_KNIGHTMETAL);

		tag(Tags.Items.ORES).addTag(ORES_IRONWOOD).addTag(ORES_KNIGHTMETAL);

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

		tag(ORES_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		tag(ORES_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());

		tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);

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
				TFItems.CUBE_OF_ANNIHILATION.get()
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
				TFBlocks.TWISTED_STONE.get().asItem(),
				TFBlocks.TWISTED_STONE_PILLAR.get().asItem(),
				TFItems.ORE_METER.get()
		);

		tag(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);

		tag(TF_MUSIC_DISCS).add(
				TFItems.MUSIC_DISC_FINDINGS.get(),
				TFItems.MUSIC_DISC_HOME.get(),
				TFItems.MUSIC_DISC_MAKER.get(),
				TFItems.MUSIC_DISC_MOTION.get(),
				TFItems.MUSIC_DISC_RADIANCE.get(),
				TFItems.MUSIC_DISC_STEPS.get(),
				TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
				TFItems.MUSIC_DISC_THREAD.get(),
				TFItems.MUSIC_DISC_WAYFARER.get());

		tag(ItemTags.MUSIC_DISCS).addTag(TF_MUSIC_DISCS);

		tag(BANNED_UNCRAFTING_INGREDIENTS).add(
				TFBlocks.INFESTED_TOWERWOOD.get().asItem(),
				TFBlocks.HOLLOW_OAK_SAPLING.get().asItem(),
				TFBlocks.TIME_SAPLING.get().asItem(),
				TFBlocks.TRANSFORMATION_SAPLING.get().asItem(),
				TFBlocks.MINING_SAPLING.get().asItem(),
				TFBlocks.SORTING_SAPLING.get().asItem());

		tag(BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.get().asItem());

		tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_2.get(), TFItems.LAMP_OF_CINDERS.get());

		tag(CHARM).add(
				TFItems.CHARM_OF_LIFE_1.get(), TFItems.CHARM_OF_LIFE_2.get(),
				TFItems.CHARM_OF_KEEPING_1.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.CHARM_OF_KEEPING_3.get()
		);

		tag(HEAD).add(
				TFBlocks.NAGA_TROPHY.get().asItem(),
				TFBlocks.LICH_TROPHY.get().asItem(),
				TFBlocks.MINOSHROOM_TROPHY.get().asItem(),
				TFBlocks.HYDRA_TROPHY.get().asItem(),
				TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(),
				TFBlocks.UR_GHAST_TROPHY.get().asItem(),
				TFBlocks.ALPHA_YETI_TROPHY.get().asItem(),
				TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(),
				TFBlocks.QUEST_RAM_TROPHY.get().asItem(),
				TFBlocks.CICADA.get().asItem(),
				TFBlocks.FIREFLY.get().asItem(),
				TFBlocks.MOONWORM.get().asItem());
	}
}
