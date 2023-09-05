package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

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

	public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = ItemTags.create(new ResourceLocation("forge", "raw_materials/ironwood"));
	public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = ItemTags.create(new ResourceLocation("forge", "raw_materials/knightmetal"));

	public static final TagKey<Item> PORTAL_ACTIVATOR = ItemTags.create(TwilightForestMod.prefix("portal/activator"));

	public static final TagKey<Item> WIP = ItemTags.create(TwilightForestMod.prefix("wip"));
	public static final TagKey<Item> NYI = ItemTags.create(TwilightForestMod.prefix("nyi"));

	public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = ItemTags.create(TwilightForestMod.prefix("kobold_pacification_breads"));

	public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = ItemTags.create(TwilightForestMod.prefix("banned_uncrafting_ingredients"));
	public static final TagKey<Item> BANNED_UNCRAFTABLES = ItemTags.create(TwilightForestMod.prefix("banned_uncraftables"));
	public static final TagKey<Item> UNCRAFTING_IGNORES_COST = ItemTags.create(TwilightForestMod.prefix("uncrafting_ignores_cost"));

	public static final TagKey<Item> KEPT_ON_DEATH = ItemTags.create(TwilightForestMod.prefix("kept_on_death"));

	private static final TagKey<Item> CHARM = ItemTags.create(new ResourceLocation("curios", "charm"));
	private static final TagKey<Item> HEAD = ItemTags.create(new ResourceLocation("curios", "head"));

	public static final TagKey<Item> CA_PLANTS = ItemTags.create(new ResourceLocation("createaddition", "plants"));
	public static final TagKey<Item> CA_PLANT_FOODS = ItemTags.create(new ResourceLocation("createaddition", "plant_foods"));

	public static final TagKey<Item> RANDOMIUM_BLACKLIST = ItemTags.create(new ResourceLocation("randomium", "blacklist"));


	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
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
		tag(ItemTags.SIGNS).add(TFBlocks.TWILIGHT_OAK_SIGN.get().asItem(), TFBlocks.CANOPY_SIGN.get().asItem(),
				TFBlocks.MANGROVE_SIGN.get().asItem(), TFBlocks.DARK_SIGN.get().asItem(),
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
				TFItems.MANGROVE_BOAT.get(), TFItems.DARKWOOD_BOAT.get(), 
				TFItems.TIME_BOAT.get(), TFItems.TRANSFORMATION_BOAT.get(), 
				TFItems.MINING_BOAT.get(), TFItems.SORTING_BOAT.get()
		);

		tag(ItemTags.CHEST_BOATS).add(
				TFItems.TWILIGHT_OAK_CHEST_BOAT.get(), TFItems.CANOPY_CHEST_BOAT.get(),
				TFItems.MANGROVE_CHEST_BOAT.get(), TFItems.DARKWOOD_CHEST_BOAT.get(),
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
				TFBlocks.MOONWORM.get().asItem(),
				TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(),
				TFBlocks.PLAYER_SKULL_CANDLE.get().asItem(),
				TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(),
				TFBlocks.WITHER_SKELE_SKULL_CANDLE.get().asItem(),
				TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem());

		tag(Tags.Items.HEADS).add(
				TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem(),
				TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(),
				TFBlocks.WITHER_SKELE_SKULL_CANDLE.get().asItem(),
				TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(),
				TFBlocks.PLAYER_SKULL_CANDLE.get().asItem());

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

		tag(CA_PLANT_FOODS).add(TFItems.TORCHBERRIES.get());

		tag(CA_PLANTS).add(TFItems.LIVEROOT.get(), TFItems.MAGIC_BEANS.get(),
				TFBlocks.HUGE_WATER_LILY.get().asItem(), TFBlocks.HUGE_LILY_PAD.get().asItem(),
				TFBlocks.TROLLVIDR.get().asItem(), TFBlocks.UNRIPE_TROLLBER.get().asItem(),
				TFBlocks.TROLLBER.get().asItem(), TFBlocks.HUGE_STALK.get().asItem(),
				TFBlocks.THORN_ROSE.get().asItem(), TFBlocks.MAYAPPLE.get().asItem(),
				TFBlocks.CLOVER_PATCH.get().asItem(), TFBlocks.FIDDLEHEAD.get().asItem(),
				TFBlocks.MUSHGLOOM.get().asItem(), TFBlocks.TORCHBERRY_PLANT.get().asItem(),
				TFBlocks.ROOT_STRAND.get().asItem(), TFBlocks.FALLEN_LEAVES.get().asItem(),
				TFBlocks.HEDGE.get().asItem(), TFBlocks.ROOT_BLOCK.get().asItem(), TFBlocks.LIVEROOT_BLOCK.get().asItem());

		tag(RANDOMIUM_BLACKLIST).addTag(NYI).addTag(WIP).add(TFItems.GLASS_SWORD.get(), //this one is here because the ore can give the unbreakable one
				TFBlocks.TIME_LOG_CORE.get().asItem(), TFBlocks.TRANSFORMATION_LOG_CORE.get().asItem(),
				TFBlocks.MINING_LOG_CORE.get().asItem(), TFBlocks.SORTING_LOG_CORE.get().asItem(),
				TFBlocks.ANTIBUILDER.get().asItem(), TFBlocks.STRONGHOLD_SHIELD.get().asItem(),
				TFBlocks.LOCKED_VANISHING_BLOCK.get().asItem(), TFBlocks.BROWN_THORNS.get().asItem(),
				TFBlocks.GREEN_THORNS.get().asItem(), TFBlocks.BURNT_THORNS.get().asItem(),
				TFBlocks.PINK_FORCE_FIELD.get().asItem(), TFBlocks.ORANGE_FORCE_FIELD.get().asItem(),
				TFBlocks.GREEN_FORCE_FIELD.get().asItem(), TFBlocks.BLUE_FORCE_FIELD.get().asItem(),
				TFBlocks.VIOLET_FORCE_FIELD.get().asItem(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get().asItem(),
				TFBlocks.NAGA_BOSS_SPAWNER.get().asItem(), TFBlocks.LICH_BOSS_SPAWNER.get().asItem(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.get().asItem(), TFBlocks.HYDRA_BOSS_SPAWNER.get().asItem(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get().asItem(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get().asItem(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().asItem(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get().asItem());

		//saved for 1.20
//		tag(ItemTags.TRIMMABLE_ARMOR)
//				.add(TFItems.IRONWOOD_HELMET.get(), TFItems.IRONWOOD_CHESTPLATE.get(), TFItems.IRONWOOD_LEGGINGS.get(), TFItems.IRONWOOD_BOOTS.get())
//				.add(TFItems.STEELEAF_HELMET.get(), TFItems.STEELEAF_CHESTPLATE.get(), TFItems.STEELEAF_LEGGINGS.get(), TFItems.STEELEAF_BOOTS.get())
//				.add(TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get())
//				.add(TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get())
//				.add(TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get())
//				.add(TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get())
//				.add(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get(), TFItems.NAGA_CHESTPLATE.get(), TFItems.NAGA_LEGGINGS.get());
//
//		tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.NAGA_SCALE.get(), TFItems.CARMINITE.get(), TFItems.FIERY_INGOT.get());
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
