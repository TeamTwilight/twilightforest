package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.datagen.helpers.BlockToItemConverter;
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
			protected TagAppender<Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new BlockToItemConverter(ItemTagGenerator.this.tag(itemTag));
			}
		}.run();

		this.tag(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT.key(), TFItems.CANOPY_BOAT.key(),
			TFItems.MANGROVE_BOAT.key(), TFItems.DARK_BOAT.key(),
			TFItems.TIME_BOAT.key(), TFItems.TRANSFORMATION_BOAT.key(),
			TFItems.MINING_BOAT.key(), TFItems.SORTING_BOAT.key()
		);

		this.tag(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT.key(), TFItems.CANOPY_CHEST_BOAT.key(),
			TFItems.MANGROVE_CHEST_BOAT.key(), TFItems.DARK_CHEST_BOAT.key(),
			TFItems.TIME_CHEST_BOAT.key(), TFItems.TRANSFORMATION_CHEST_BOAT.key(),
			TFItems.MINING_CHEST_BOAT.key(), TFItems.SORTING_CHEST_BOAT.key()
		);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(TFItemTags.IRONWOOD_INGOTS, TFItemTags.STEELEAF_INGOTS, TFItemTags.KNIGHTMETAL_INGOTS, TFItemTags.FIERY_INGOTS);
		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.key(), TFItems.CHARM_OF_KEEPING_3.key(), TFItems.CHARM_OF_LIFE_2.key(), TFItems.LAMP_OF_CINDERS.key());
		this.tag(ItemTags.FOX_FOOD).add(TFItems.TORCHBERRIES.key());

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET.key(),
			TFItems.FIERY_CHESTPLATE.key(),
			TFItems.FIERY_LEGGINGS.key(),
			TFItems.FIERY_BOOTS.key(),
			TFItems.ARCTIC_HELMET.key(),
			TFItems.ARCTIC_CHESTPLATE.key(),
			TFItems.ARCTIC_LEGGINGS.key(),
			TFItems.ARCTIC_BOOTS.key(),
			TFItems.YETI_HELMET.key(),
			TFItems.YETI_CHESTPLATE.key(),
			TFItems.YETI_LEGGINGS.key(),
			TFItems.YETI_BOOTS.key(),
			TFItems.TRAVELLERS_VEST.key(),
			TFItems.TRAVELLERS_BOOTS.key());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.key(),
			TFItems.STEELEAF_PICKAXE.key(),
			TFItems.KNIGHTMETAL_PICKAXE.key(),
			TFItems.MAZEBREAKER_PICKAXE.key(),
			TFItems.FIERY_PICKAXE.key(),
			TFItems.GIANT_PICKAXE.key());

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.key(),
			TFItems.STEELEAF_SWORD.key(),
			TFItems.KNIGHTMETAL_SWORD.key(),
			TFItems.FIERY_SWORD.key(),
			TFItems.GIANT_SWORD.key(),
			TFItems.ICE_SWORD.key(),
			TFItems.GLASS_SWORD.key());

		this.tag(ItemTags.AXES).add(
			TFItems.IRONWOOD_AXE.key(),
			TFItems.STEELEAF_AXE.key(),
			TFItems.KNIGHTMETAL_AXE.key(),
			TFItems.GOLDEN_MINOTAUR_AXE.key(),
			TFItems.DIAMOND_MINOTAUR_AXE.key());

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.key(),
			TFItems.STEELEAF_PICKAXE.key(),
			TFItems.KNIGHTMETAL_PICKAXE.key(),
			TFItems.MAZEBREAKER_PICKAXE.key(),
			TFItems.FIERY_PICKAXE.key(),
			TFItems.GIANT_PICKAXE.key());

		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.key(), TFItems.STEELEAF_SHOVEL.key());
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.key(), TFItems.STEELEAF_HOE.key());
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.key());
		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.key(),
			TFItems.STEELEAF_BOOTS.key(),
			TFItems.KNIGHTMETAL_BOOTS.key(),
			TFItems.ARCTIC_BOOTS.key(),
			TFItems.YETI_BOOTS.key(),
			TFItems.FIERY_BOOTS.key(),
			TFItems.TRAVELLERS_BOOTS.key());

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.key(),
			TFItems.STEELEAF_LEGGINGS.key(),
			TFItems.KNIGHTMETAL_LEGGINGS.key(),
			TFItems.ARCTIC_LEGGINGS.key(),
			TFItems.YETI_LEGGINGS.key(),
			TFItems.FIERY_LEGGINGS.key(),
			TFItems.NAGA_LEGGINGS.key(),
			TFItems.TRAVELLERS_WINGS.key(),
			TFItems.TRAVELLERS_BELT.key());

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.key(),
			TFItems.STEELEAF_CHESTPLATE.key(),
			TFItems.KNIGHTMETAL_CHESTPLATE.key(),
			TFItems.ARCTIC_CHESTPLATE.key(),
			TFItems.YETI_CHESTPLATE.key(),
			TFItems.FIERY_CHESTPLATE.key(),
			TFItems.PHANTOM_CHESTPLATE.key(),
			TFItems.NAGA_CHESTPLATE.key(),
			TFItems.TRAVELLERS_VEST.key(),
			TFItems.TRAVELLERS_GLOVES.key());

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.key(),
			TFItems.STEELEAF_HELMET.key(),
			TFItems.KNIGHTMETAL_HELMET.key(),
			TFItems.ARCTIC_HELMET.key(),
			TFItems.YETI_HELMET.key(),
			TFItems.FIERY_HELMET.key(),
			TFItems.PHANTOM_HELMET.key(),
			TFItems.TRAVELLERS_GOGGLES.key());

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.key(),
			TFItems.SKELETON_SKULL_CANDLE.key(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.key(),
			TFItems.CREEPER_SKULL_CANDLE.key(),
			TFItems.PLAYER_SKULL_CANDLE.key(),
			TFItems.PIGLIN_SKULL_CANDLE.key());

		this.tag(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET.key())
			.remove(TFItems.TRAVELLERS_GOGGLES.key())
			.remove(TFItems.TRAVELLERS_VEST.key())
			.remove(TFItems.TRAVELLERS_GLOVES.key())
			.remove(TFItems.TRAVELLERS_BELT.key())
			.remove(TFItems.TRAVELLERS_WINGS.key())
			.remove(TFItems.TRAVELLERS_BOOTS.key());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.key(), TFItems.STEELEAF_INGOT.key(), TFItems.KNIGHTMETAL_INGOT.key(), TFItems.NAGA_SCALE.key(), TFItems.CARMINITE.key(), TFItems.FIERY_INGOT.key());

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.key(),
			TFItems.SKELETON_SKULL_CANDLE.key(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.key(),
			TFItems.CREEPER_SKULL_CANDLE.key(),
			TFItems.PLAYER_SKULL_CANDLE.key(),
			TFItems.PIGLIN_SKULL_CANDLE.key());

		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.key(), TFItems.ICE_SWORD.key());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.key());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.key());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.key(),
			TFItems.SEEKER_BOW.key(),
			TFItems.ICE_BOW.key(),
			TFItems.ENDER_BOW.key(),
			TFItems.BLOCK_AND_CHAIN.key(),
			TFItems.KNIGHTMETAL_SHIELD.key(),
			TFItems.ORE_MAGNET.key(),
			TFItems.PEACOCK_FEATHER_FAN.key(),
			TFItems.CRUMBLE_HORN.key());

		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.key(), TFItems.SEEKER_BOW.key(), TFItems.ICE_BOW.key(), TFItems.ENDER_BOW.key());
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.key(), TFItems.PHANTOM_CHESTPLATE.key());
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.key(), TFItems.PHANTOM_CHESTPLATE.key());
		this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(TFItems.ARCTIC_HELMET.key(), TFItems.ARCTIC_CHESTPLATE.key(), TFItems.ARCTIC_LEGGINGS.key(), TFItems.ARCTIC_BOOTS.key());
		this.tag(ItemTags.MEAT).add(
			TFItems.RAW_VENISON.key(),
			TFItems.COOKED_VENISON.key(),
			TFItems.RAW_MEEF.key(),
			TFItems.COOKED_MEEF.key(),
			TFItems.MEEF_STROGANOFF.key(),
			TFItems.EXPERIMENT_115.key(),
			TFItems.HYDRA_CHOP.key(),
			TFItems.MONSTER_JERKY.key(),
			TFItems.BEEF_JERKY.key(),
			TFItems.PORK_JERKY.key(),
			TFItems.CHICKEN_JERKY.key(),
			TFItems.RABBIT_JERKY.key(),
			TFItems.MUTTON_JERKY.key(),
			TFItems.VENISON_JERKY.key(),
			TFItems.MEEF_JERKY.key(),
			TFItems.COD_JERKY.key(),
			TFItems.SALMON_JERKY.key(),
			TFItems.TROPICAL_FISH_JERKY.key(),
			TFItems.FUGU_JERKY.key()
		);
		this.tag(ItemTags.LOOM_PATTERNS).add(
			TFItems.NAGA_BANNER_PATTERN.key(),
			TFItems.LICH_BANNER_PATTERN.key(),
			TFItems.MINOSHROOM_BANNER_PATTERN.key(),
			TFItems.HYDRA_BANNER_PATTERN.key(),
			TFItems.KNIGHT_PHANTOM_BANNER_PATTERN.key(),
			TFItems.UR_GHAST_BANNER_PATTERN.key(),
			TFItems.ALPHA_YETI_BANNER_PATTERN.key(),
			TFItems.SNOW_QUEEN_BANNER_PATTERN.key(),
			TFItems.QUEST_RAM_BANNER_PATTERN.key());

		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.key());
		this.tag(Tags.Items.FOODS).addTag(TFItemTags.FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP.key(), TFItems.GELATINOUS_MAZE_SLIME_DROP.key(), TFItems.BERRY_MEDLEY.key(), TFItems.MAZE_WAFER.key());
		this.tag(Tags.Items.FOODS_BERRY).add(
			TFItems.TORCHBERRIES.key(),
			TFItems.RASPBERRY.key(),
			TFItems.BLACKBERRY.key(),
			TFItems.BLUEBERRY.key(),
			TFItems.MALOBERRY.key(),
			TFItems.DUSKBERRY.key(),
			TFItems.SKYBERRY.key(),
			TFItems.BLIGHTBERRY.key(),
			TFItems.STINGBERRY.key());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.key(), TFItems.RAW_MEEF.key());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.key(), TFItems.COOKED_MEEF.key(), TFItems.HYDRA_CHOP.key());
		this.tag(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.key(), TFItems.MOSS_SOUP.key());
		this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.key());
		this.tag(Tags.Items.GEMS).addTag(TFItemTags.CARMINITE_GEMS);
		this.tag(Tags.Items.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder().key());
		this.tag(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.key(),
			TFItems.MUSIC_DISC_STEPS.key(),
			TFItems.MUSIC_DISC_SUPERSTITIOUS.key(),
			TFItems.MUSIC_DISC_HOME.key(),
			TFItems.MUSIC_DISC_WAYFARER.key(),
			TFItems.MUSIC_DISC_FINDINGS.key(),
			TFItems.MUSIC_DISC_MAKER.key(),
			TFItems.MUSIC_DISC_THREAD.key(),
			TFItems.MUSIC_DISC_MOTION.key()
		);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);
		this.tag(Tags.Items.RANGED_WEAPON_TOOLS).add(TFItems.BLOCK_AND_CHAIN.key());
		this.tag(Tags.Items.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.key());
		this.tag(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.key());
		this.tag(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.key(), TFItems.SEEKER_BOW.key(), TFItems.ICE_BOW.key(), TFItems.ENDER_BOW.key());

		this.tag(TFItemTags.PAPER).add(ItemIds.PAPER);
		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD.key(), TFItems.FIERY_TEARS.key());
		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR.key());
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE.key());
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT.key());
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.key());
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.key());
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.key());
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.key());
		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.key());
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.key());
		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
		this.tag(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem().builtInRegistryHolder().key(),
			TFItems.QUEST_RAM_BANNER_PATTERN.key(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFItems.CUBE_TALISMAN.key(),
			TFItems.CUBE_OF_ANNIHILATION.key(),
			TFBlocks.CINDER_FURNACE.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.SLIDER.asItem().builtInRegistryHolder().key(),
			TFBlocks.BRAZIER.asItem().builtInRegistryHolder().key()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(ItemIds.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(ItemIds.APPLE).add(TFItems.SHIKA_SENBEI.key());
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).add(ItemIds.GOLDEN_CARROT).add(BlockItemIds.DANDELION.item());
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder().key(),
			TFItems.TRANSFORMATION_POWDER.key());

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);
		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY.key(), TFItems.PHANTOM_HELMET.key(), TFItems.PHANTOM_CHESTPLATE.key());
		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.key(), TFItems.LIFEDRAIN_SCEPTER.key(), TFItems.ZOMBIE_SCEPTER.key(), TFItems.FORTIFICATION_SCEPTER.key());
		this.tag(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS).add(TFItems.EXANIMATE_ESSENCE.key());
		this.tag(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS).add(TFItems.TORCHBERRIES.key());
		this.tag(TFItemTags.KEEPSAKE_CASKET_REPAIR_ITEMS).add(TFItems.CHARM_OF_KEEPING_3.key());
		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder().key(), TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED).add(TFItems.MOONWORM_QUEEN.key());
		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(BlockItemIds.ICE.item(), BlockItemIds.PACKED_ICE.item(), BlockItemIds.BLUE_ICE.item());
		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.key());

		this.tag(TFItemTags.FOODS_JERKY).add(
			TFItems.MONSTER_JERKY.key(),
			TFItems.BEEF_JERKY.key(),
			TFItems.PORK_JERKY.key(),
			TFItems.CHICKEN_JERKY.key(),
			TFItems.RABBIT_JERKY.key(),
			TFItems.MUTTON_JERKY.key(),
			TFItems.VENISON_JERKY.key(),
			TFItems.MEEF_JERKY.key(),
			TFItems.COD_JERKY.key(),
			TFItems.SALMON_JERKY.key(),
			TFItems.TROPICAL_FISH_JERKY.key(),
			TFItems.FUGU_JERKY.key());

		this.tag(TFItemTags.MAZE_SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.key());
		this.tag(TFItemTags.RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP.key(), TFItems.GELATINOUS_MAZE_SLIME_DROP.key())
			.add(TFItems.ZOMBIE_SKULL_CANDLE.key(), TFItems.SKELETON_SKULL_CANDLE.key(), TFItems.WITHER_SKELETON_SKULL_CANDLE.key(), TFItems.CREEPER_SKULL_CANDLE.key(), TFItems.PLAYER_SKULL_CANDLE.key(), TFItems.PIGLIN_SKULL_CANDLE.key())
			.add(BlockItemIds.POINTED_DRIPSTONE.item(), ItemIds.RECOVERY_COMPASS, ItemIds.CLOCK, ItemIds.SPYGLASS, ItemIds.TRIDENT)
			.addTag(ItemTags.BANNERS)
			.addTag(Tags.Items.TOOLS)
			.remove(Tags.Items.TOOLS_SHIELD);

		this.tag(TFItemTags.TROPHIES).add(
			TFItems.NAGA_TROPHY.key(), TFItems.LICH_TROPHY.key(),
			TFItems.MINOSHROOM_TROPHY.key(), TFItems.HYDRA_TROPHY.key(),
			TFItems.KNIGHT_PHANTOM_TROPHY.key(), TFItems.UR_GHAST_TROPHY.key(),
			TFItems.ALPHA_YETI_TROPHY.key(), TFItems.SNOW_QUEEN_TROPHY.key());

		this.tag(TFItemTags.EMPERORS_CLOTH_APPLICABLE).addTag(Tags.Items.ARMORS).add(ItemIds.ELYTRA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
