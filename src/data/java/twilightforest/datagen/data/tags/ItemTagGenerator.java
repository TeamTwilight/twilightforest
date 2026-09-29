package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagsProvider.TagLookup<Block>> provider) {
		super(output, future, provider);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);

		new BlockItemTagGenerator() {
			@Override
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new VanillaItemTagsProvider.BlockToItemConverter(ItemTagGenerator.this.tag(itemTag));
			}
		}.run();

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
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(TFItemTags.IRONWOOD_INGOTS, TFItemTags.STEELEAF_INGOTS, TFItemTags.KNIGHTMETAL_INGOTS, TFItemTags.FIERY_INGOTS);
		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_2.get(), TFItems.LAMP_OF_CINDERS.get());
		this.tag(ItemTags.FOX_FOOD).add(TFItems.TORCHBERRIES.get());

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
			TFItems.YETI_BOOTS.get(),
			TFItems.TRAVELLERS_VEST.get(),
			TFItems.TRAVELLERS_BOOTS.get());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.get(),
			TFItems.STEELEAF_SWORD.get(),
			TFItems.KNIGHTMETAL_SWORD.get(),
			TFItems.FIERY_SWORD.get(),
			TFItems.GIANT_SWORD.get(),
			TFItems.ICE_SWORD.get(),
			TFItems.GLASS_SWORD.get());

		this.tag(ItemTags.AXES).add(
			TFItems.IRONWOOD_AXE.get(),
			TFItems.STEELEAF_AXE.get(),
			TFItems.KNIGHTMETAL_AXE.get(),
			TFItems.GOLDEN_MINOTAUR_AXE.get(),
			TFItems.DIAMOND_MINOTAUR_AXE.get());

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.get(),
			TFItems.STEELEAF_PICKAXE.get(),
			TFItems.KNIGHTMETAL_PICKAXE.get(),
			TFItems.MAZEBREAKER_PICKAXE.get(),
			TFItems.FIERY_PICKAXE.get(),
			TFItems.GIANT_PICKAXE.get());

		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.get(), TFItems.STEELEAF_SHOVEL.get());
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.get(), TFItems.STEELEAF_HOE.get());
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.get(),
			TFItems.STEELEAF_BOOTS.get(),
			TFItems.KNIGHTMETAL_BOOTS.get(),
			TFItems.ARCTIC_BOOTS.get(),
			TFItems.YETI_BOOTS.get(),
			TFItems.FIERY_BOOTS.get(),
			TFItems.TRAVELLERS_BOOTS.get());

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.get(),
			TFItems.STEELEAF_LEGGINGS.get(),
			TFItems.KNIGHTMETAL_LEGGINGS.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.YETI_LEGGINGS.get(),
			TFItems.FIERY_LEGGINGS.get(),
			TFItems.NAGA_LEGGINGS.get(),
			TFItems.TRAVELLERS_WINGS.get(),
			TFItems.TRAVELLERS_BELT.get());

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.get(),
			TFItems.STEELEAF_CHESTPLATE.get(),
			TFItems.KNIGHTMETAL_CHESTPLATE.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.YETI_CHESTPLATE.get(),
			TFItems.FIERY_CHESTPLATE.get(),
			TFItems.PHANTOM_CHESTPLATE.get(),
			TFItems.NAGA_CHESTPLATE.get(),
			TFItems.TRAVELLERS_VEST.get(),
			TFItems.TRAVELLERS_GLOVES.get());

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.get(),
			TFItems.STEELEAF_HELMET.get(),
			TFItems.KNIGHTMETAL_HELMET.get(),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.YETI_HELMET.get(),
			TFItems.FIERY_HELMET.get(),
			TFItems.PHANTOM_HELMET.get(),
			TFItems.TRAVELLERS_GOGGLES.get());

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		this.tag(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET.get())
			.remove(TFItems.TRAVELLERS_GOGGLES.get())
			.remove(TFItems.TRAVELLERS_VEST.get())
			.remove(TFItems.TRAVELLERS_GLOVES.get())
			.remove(TFItems.TRAVELLERS_BELT.get())
			.remove(TFItems.TRAVELLERS_WINGS.get())
			.remove(TFItems.TRAVELLERS_BOOTS.get());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.NAGA_SCALE.get(), TFItems.CARMINITE.get(), TFItems.FIERY_INGOT.get());

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.get(),
			TFItems.SKELETON_SKULL_CANDLE.get(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(),
			TFItems.PLAYER_SKULL_CANDLE.get(),
			TFItems.PIGLIN_SKULL_CANDLE.get());

		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.get(), TFItems.ICE_SWORD.get());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.get(),
			TFItems.SEEKER_BOW.get(),
			TFItems.ICE_BOW.get(),
			TFItems.ENDER_BOW.get(),
			TFItems.BLOCK_AND_CHAIN.get(),
			TFItems.KNIGHTMETAL_SHIELD.get(),
			TFItems.ORE_MAGNET.get(),
			TFItems.PEACOCK_FEATHER_FAN.get(),
			TFItems.CRUMBLE_HORN.get());

		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		this.tag(ItemTags.MEAT).add(
			TFItems.RAW_VENISON.get(),
			TFItems.COOKED_VENISON.get(),
			TFItems.RAW_MEEF.get(),
			TFItems.COOKED_MEEF.get(),
			TFItems.MEEF_STROGANOFF.get(),
			TFItems.EXPERIMENT_115.get(),
			TFItems.HYDRA_CHOP.get(),
			TFItems.MONSTER_JERKY.get(),
			TFItems.BEEF_JERKY.get(),
			TFItems.PORK_JERKY.get(),
			TFItems.CHICKEN_JERKY.get(),
			TFItems.RABBIT_JERKY.get(),
			TFItems.MUTTON_JERKY.get(),
			TFItems.VENISON_JERKY.get(),
			TFItems.MEEF_JERKY.get(),
			TFItems.COD_JERKY.get(),
			TFItems.SALMON_JERKY.get(),
			TFItems.TROPICAL_FISH_JERKY.get(),
			TFItems.FUGU_JERKY.get()
		);

		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.get());
		this.tag(Tags.Items.FOODS).addTag(TFItemTags.FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get(), TFItems.BERRY_MEDLEY.get(), TFItems.MAZE_WAFER.get());
		this.tag(Tags.Items.FOODS_BERRY).add(
			TFItems.TORCHBERRIES.get(),
			TFItems.RASPBERRY.get(),
			TFItems.BLACKBERRY.get(),
			TFItems.BLUEBERRY.get(),
			TFItems.MALOBERRY.get(),
			TFItems.DUSKBERRY.get(),
			TFItems.SKYBERRY.get(),
			TFItems.BLIGHTBERRY.get(),
			TFItems.STINGBERRY.get());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.get(), TFItems.COOKED_MEEF.get(), TFItems.HYDRA_CHOP.get());
		this.tag(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.get(), TFItems.MOSS_SOUP.get());
		this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.get());
		this.tag(Tags.Items.GEMS).addTag(TFItemTags.CARMINITE_GEMS);
		this.tag(Tags.Items.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.get().asItem());
		this.tag(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.get(),
			TFItems.MUSIC_DISC_STEPS.get(),
			TFItems.MUSIC_DISC_SUPERSTITIOUS.get(),
			TFItems.MUSIC_DISC_HOME.get(),
			TFItems.MUSIC_DISC_WAYFARER.get(),
			TFItems.MUSIC_DISC_FINDINGS.get(),
			TFItems.MUSIC_DISC_MAKER.get(),
			TFItems.MUSIC_DISC_THREAD.get(),
			TFItems.MUSIC_DISC_MOTION.get()
		);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);
		this.tag(Tags.Items.RANGED_WEAPON_TOOLS).add(TFItems.BLOCK_AND_CHAIN.get());
		this.tag(Tags.Items.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.get());
		this.tag(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.get());
		this.tag(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.get(), TFItems.SEEKER_BOW.get(), TFItems.ICE_BOW.get(), TFItems.ENDER_BOW.get());

		this.tag(TFItemTags.PAPER).add(Items.PAPER);
		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD.get(), TFItems.FIERY_TEARS.get());
		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR.get());
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE.get());
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT.get());
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.get());
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.get());
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.get());
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.get());
		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.get());
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.get());
		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
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
			TFBlocks.BRAZIER.asItem()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(Items.APPLE).add(TFItems.SHIKA_SENBEI.get());
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
		this.tag(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS).add(TFItems.EXANIMATE_ESSENCE.get());
		this.tag(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS).add(TFItems.TORCHBERRIES.get());
		this.tag(TFItemTags.KEEPSAKE_CASKET_REPAIR_ITEMS).add(TFItems.CHARM_OF_KEEPING_3.get());
		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());
		this.tag(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED).add(TFItems.MOONWORM_QUEEN.get());
		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());
		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.get());

		this.tag(TFItemTags.FOODS_JERKY).add(
			TFItems.MONSTER_JERKY.get(),
			TFItems.BEEF_JERKY.get(),
			TFItems.PORK_JERKY.get(),
			TFItems.CHICKEN_JERKY.get(),
			TFItems.RABBIT_JERKY.get(),
			TFItems.MUTTON_JERKY.get(),
			TFItems.VENISON_JERKY.get(),
			TFItems.MEEF_JERKY.get(),
			TFItems.COD_JERKY.get(),
			TFItems.SALMON_JERKY.get(),
			TFItems.TROPICAL_FISH_JERKY.get(),
			TFItems.FUGU_JERKY.get());

		this.tag(TFItemTags.MAZE_SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.get());
		this.tag(TFItemTags.RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP.get(), TFItems.GELATINOUS_MAZE_SLIME_DROP.get())
			.add(TFItems.ZOMBIE_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get())
			.add(Items.POINTED_DRIPSTONE, Items.RECOVERY_COMPASS, Items.CLOCK, Items.SPYGLASS, Items.TRIDENT)
			.addTag(ItemTags.BANNERS)
			.addTag(Tags.Items.TOOLS)
			.remove(Tags.Items.TOOLS_SHIELD);

		this.tag(TFItemTags.TROPHIES).add(
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(),
			TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(),
			TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(),
			TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get());

		this.tag(TFItemTags.EMPERORS_CLOTH_APPLICABLE).addTag(Tags.Items.ARMORS).add(Items.ELYTRA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
