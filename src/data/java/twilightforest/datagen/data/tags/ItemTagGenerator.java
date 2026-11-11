package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

		// FIXME
		new BlockItemTagGenerator() {
			@Override
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new VanillaItemTagsProvider.BlockToItemConverter(ItemTagGenerator.this.tag(itemTag));
			}
		}.run();

		this.tag(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT.getKey(), TFItems.CANOPY_BOAT.getKey(),
			TFItems.MANGROVE_BOAT.getKey(), TFItems.DARK_BOAT.getKey(),
			TFItems.TIME_BOAT.getKey(), TFItems.TRANSFORMATION_BOAT.getKey(),
			TFItems.MINING_BOAT.getKey(), TFItems.SORTING_BOAT.getKey()
		);

		this.tag(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT.getKey(), TFItems.CANOPY_CHEST_BOAT.getKey(),
			TFItems.MANGROVE_CHEST_BOAT.getKey(), TFItems.DARK_CHEST_BOAT.getKey(),
			TFItems.TIME_CHEST_BOAT.getKey(), TFItems.TRANSFORMATION_CHEST_BOAT.getKey(),
			TFItems.MINING_CHEST_BOAT.getKey(), TFItems.SORTING_CHEST_BOAT.getKey()
		);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(TFItemTags.IRONWOOD_INGOTS, TFItemTags.STEELEAF_INGOTS, TFItemTags.KNIGHTMETAL_INGOTS, TFItemTags.FIERY_INGOTS);
		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.getKey(), TFItems.CHARM_OF_KEEPING_3.getKey(), TFItems.CHARM_OF_LIFE_2.getKey(), TFItems.LAMP_OF_CINDERS.getKey());
		this.tag(ItemTags.FOX_FOOD).add(TFItems.TORCHBERRIES.getKey());

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET.getKey(),
			TFItems.FIERY_CHESTPLATE.getKey(),
			TFItems.FIERY_LEGGINGS.getKey(),
			TFItems.FIERY_BOOTS.getKey(),
			TFItems.ARCTIC_HELMET.getKey(),
			TFItems.ARCTIC_CHESTPLATE.getKey(),
			TFItems.ARCTIC_LEGGINGS.getKey(),
			TFItems.ARCTIC_BOOTS.getKey(),
			TFItems.YETI_HELMET.getKey(),
			TFItems.YETI_CHESTPLATE.getKey(),
			TFItems.YETI_LEGGINGS.getKey(),
			TFItems.YETI_BOOTS.getKey(),
			TFItems.TRAVELLERS_VEST.getKey(),
			TFItems.TRAVELLERS_BOOTS.getKey());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.getKey(),
			TFItems.STEELEAF_PICKAXE.getKey(),
			TFItems.KNIGHTMETAL_PICKAXE.getKey(),
			TFItems.MAZEBREAKER_PICKAXE.getKey(),
			TFItems.FIERY_PICKAXE.getKey(),
			TFItems.GIANT_PICKAXE.getKey());

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.getKey(),
			TFItems.STEELEAF_SWORD.getKey(),
			TFItems.KNIGHTMETAL_SWORD.getKey(),
			TFItems.FIERY_SWORD.getKey(),
			TFItems.GIANT_SWORD.getKey(),
			TFItems.ICE_SWORD.getKey(),
			TFItems.GLASS_SWORD.getKey());

		this.tag(ItemTags.AXES).add(
			TFItems.IRONWOOD_AXE.getKey(),
			TFItems.STEELEAF_AXE.getKey(),
			TFItems.KNIGHTMETAL_AXE.getKey(),
			TFItems.GOLDEN_MINOTAUR_AXE.getKey(),
			TFItems.DIAMOND_MINOTAUR_AXE.getKey());

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.getKey(),
			TFItems.STEELEAF_PICKAXE.getKey(),
			TFItems.KNIGHTMETAL_PICKAXE.getKey(),
			TFItems.MAZEBREAKER_PICKAXE.getKey(),
			TFItems.FIERY_PICKAXE.getKey(),
			TFItems.GIANT_PICKAXE.getKey());

		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.getKey(), TFItems.STEELEAF_SHOVEL.getKey());
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.getKey(), TFItems.STEELEAF_HOE.getKey());
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.getKey(),
			TFItems.STEELEAF_BOOTS.getKey(),
			TFItems.KNIGHTMETAL_BOOTS.getKey(),
			TFItems.ARCTIC_BOOTS.getKey(),
			TFItems.YETI_BOOTS.getKey(),
			TFItems.FIERY_BOOTS.getKey(),
			TFItems.TRAVELLERS_BOOTS.getKey());

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.getKey(),
			TFItems.STEELEAF_LEGGINGS.getKey(),
			TFItems.KNIGHTMETAL_LEGGINGS.getKey(),
			TFItems.ARCTIC_LEGGINGS.getKey(),
			TFItems.YETI_LEGGINGS.getKey(),
			TFItems.FIERY_LEGGINGS.getKey(),
			TFItems.NAGA_LEGGINGS.getKey(),
			TFItems.TRAVELLERS_WINGS.getKey(),
			TFItems.TRAVELLERS_BELT.getKey());

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.getKey(),
			TFItems.STEELEAF_CHESTPLATE.getKey(),
			TFItems.KNIGHTMETAL_CHESTPLATE.getKey(),
			TFItems.ARCTIC_CHESTPLATE.getKey(),
			TFItems.YETI_CHESTPLATE.getKey(),
			TFItems.FIERY_CHESTPLATE.getKey(),
			TFItems.PHANTOM_CHESTPLATE.getKey(),
			TFItems.NAGA_CHESTPLATE.getKey(),
			TFItems.TRAVELLERS_VEST.getKey(),
			TFItems.TRAVELLERS_GLOVES.getKey());

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.getKey(),
			TFItems.STEELEAF_HELMET.getKey(),
			TFItems.KNIGHTMETAL_HELMET.getKey(),
			TFItems.ARCTIC_HELMET.getKey(),
			TFItems.YETI_HELMET.getKey(),
			TFItems.FIERY_HELMET.getKey(),
			TFItems.PHANTOM_HELMET.getKey(),
			TFItems.TRAVELLERS_GOGGLES.getKey());

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.getKey(),
			TFItems.SKELETON_SKULL_CANDLE.getKey(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(),
			TFItems.CREEPER_SKULL_CANDLE.getKey(),
			TFItems.PLAYER_SKULL_CANDLE.getKey(),
			TFItems.PIGLIN_SKULL_CANDLE.getKey());

		this.tag(ItemTags.TRIMMABLE_ARMOR)
			.remove(TFItems.YETI_HELMET.getKey())
			.remove(TFItems.TRAVELLERS_GOGGLES.getKey())
			.remove(TFItems.TRAVELLERS_VEST.getKey())
			.remove(TFItems.TRAVELLERS_GLOVES.getKey())
			.remove(TFItems.TRAVELLERS_BELT.getKey())
			.remove(TFItems.TRAVELLERS_WINGS.getKey())
			.remove(TFItems.TRAVELLERS_BOOTS.getKey());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.getKey(), TFItems.STEELEAF_INGOT.getKey(), TFItems.KNIGHTMETAL_INGOT.getKey(), TFItems.NAGA_SCALE.getKey(), TFItems.CARMINITE.getKey(), TFItems.FIERY_INGOT.getKey());

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.getKey(),
			TFItems.SKELETON_SKULL_CANDLE.getKey(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(),
			TFItems.CREEPER_SKULL_CANDLE.getKey(),
			TFItems.PLAYER_SKULL_CANDLE.getKey(),
			TFItems.PIGLIN_SKULL_CANDLE.getKey());

		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.getKey(), TFItems.ICE_SWORD.getKey());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.getKey(),
			TFItems.SEEKER_BOW.getKey(),
			TFItems.ICE_BOW.getKey(),
			TFItems.ENDER_BOW.getKey(),
			TFItems.BLOCK_AND_CHAIN.getKey(),
			TFItems.KNIGHTMETAL_SHIELD.getKey(),
			TFItems.ORE_MAGNET.getKey(),
			TFItems.PEACOCK_FEATHER_FAN.getKey(),
			TFItems.CRUMBLE_HORN.getKey());

		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.getKey(), TFItems.SEEKER_BOW.getKey(), TFItems.ICE_BOW.getKey(), TFItems.ENDER_BOW.getKey());
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());
		this.tag(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(TFItems.ARCTIC_HELMET.getKey(), TFItems.ARCTIC_CHESTPLATE.getKey(), TFItems.ARCTIC_LEGGINGS.getKey(), TFItems.ARCTIC_BOOTS.getKey());
		this.tag(ItemTags.MEAT).add(
			TFItems.RAW_VENISON.getKey(),
			TFItems.COOKED_VENISON.getKey(),
			TFItems.RAW_MEEF.getKey(),
			TFItems.COOKED_MEEF.getKey(),
			TFItems.MEEF_STROGANOFF.getKey(),
			TFItems.EXPERIMENT_115.getKey(),
			TFItems.HYDRA_CHOP.getKey(),
			TFItems.MONSTER_JERKY.getKey(),
			TFItems.BEEF_JERKY.getKey(),
			TFItems.PORK_JERKY.getKey(),
			TFItems.CHICKEN_JERKY.getKey(),
			TFItems.RABBIT_JERKY.getKey(),
			TFItems.MUTTON_JERKY.getKey(),
			TFItems.VENISON_JERKY.getKey(),
			TFItems.MEEF_JERKY.getKey(),
			TFItems.COD_JERKY.getKey(),
			TFItems.SALMON_JERKY.getKey(),
			TFItems.TROPICAL_FISH_JERKY.getKey(),
			TFItems.FUGU_JERKY.getKey()
		);
		this.tag(ItemTags.LOOM_PATTERNS).add(
			TFItems.NAGA_BANNER_PATTERN.getKey(),
			TFItems.LICH_BANNER_PATTERN.getKey(),
			TFItems.MINOSHROOM_BANNER_PATTERN.getKey(),
			TFItems.HYDRA_BANNER_PATTERN.getKey(),
			TFItems.KNIGHT_PHANTOM_BANNER_PATTERN.getKey(),
			TFItems.UR_GHAST_BANNER_PATTERN.getKey(),
			TFItems.ALPHA_YETI_BANNER_PATTERN.getKey(),
			TFItems.SNOW_QUEEN_BANNER_PATTERN.getKey(),
			TFItems.QUEST_RAM_BANNER_PATTERN.getKey());

		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.getKey());
		this.tag(Tags.Items.FOODS).addTag(TFItemTags.FOODS_JERKY).add(TFItems.GELATINOUS_SLIME_DROP.getKey(), TFItems.GELATINOUS_MAZE_SLIME_DROP.getKey(), TFItems.BERRY_MEDLEY.getKey(), TFItems.MAZE_WAFER.getKey());
		this.tag(Tags.Items.FOODS_BERRY).add(
			TFItems.TORCHBERRIES.getKey(),
			TFItems.RASPBERRY.getKey(),
			TFItems.BLACKBERRY.getKey(),
			TFItems.BLUEBERRY.getKey(),
			TFItems.MALOBERRY.getKey(),
			TFItems.DUSKBERRY.getKey(),
			TFItems.SKYBERRY.getKey(),
			TFItems.BLIGHTBERRY.getKey(),
			TFItems.STINGBERRY.getKey());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.getKey(), TFItems.RAW_MEEF.getKey());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.getKey(), TFItems.COOKED_MEEF.getKey(), TFItems.HYDRA_CHOP.getKey());
		this.tag(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.getKey(), TFItems.MOSS_SOUP.getKey());
		this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.getKey());
		this.tag(Tags.Items.GEMS).addTag(TFItemTags.CARMINITE_GEMS);
		this.tag(Tags.Items.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS)
			.addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS)
			.addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder().key());
		this.tag(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.getKey(),
			TFItems.MUSIC_DISC_STEPS.getKey(),
			TFItems.MUSIC_DISC_SUPERSTITIOUS.getKey(),
			TFItems.MUSIC_DISC_HOME.getKey(),
			TFItems.MUSIC_DISC_WAYFARER.getKey(),
			TFItems.MUSIC_DISC_FINDINGS.getKey(),
			TFItems.MUSIC_DISC_MAKER.getKey(),
			TFItems.MUSIC_DISC_THREAD.getKey(),
			TFItems.MUSIC_DISC_MOTION.getKey()
		);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);
		this.tag(Tags.Items.RANGED_WEAPON_TOOLS).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(Tags.Items.SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.getKey());
		this.tag(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.getKey());
		this.tag(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.getKey(), TFItems.SEEKER_BOW.getKey(), TFItems.ICE_BOW.getKey(), TFItems.ENDER_BOW.getKey());

		this.tag(TFItemTags.PAPER).add(ItemIds.PAPER);
		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD.getKey(), TFItems.FIERY_TEARS.getKey());
		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR.getKey());
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE.getKey());
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT.getKey());
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.getKey());
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.getKey());
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.getKey());
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.getKey());
		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.getKey());
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.getKey());
		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
		this.tag(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem().builtInRegistryHolder().key(),
			TFItems.QUEST_RAM_BANNER_PATTERN.getKey(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFItems.CUBE_TALISMAN.getKey(),
			TFItems.CUBE_OF_ANNIHILATION.getKey(),
			TFBlocks.CINDER_FURNACE.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.SLIDER.asItem().builtInRegistryHolder().key(),
			TFBlocks.BRAZIER.asItem().builtInRegistryHolder().key()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(ItemIds.BREAD);
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(ItemIds.APPLE).add(TFItems.SHIKA_SENBEI.getKey());
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
			TFItems.TRANSFORMATION_POWDER.getKey());

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);
		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY.getKey(), TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());
		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.getKey(), TFItems.LIFEDRAIN_SCEPTER.getKey(), TFItems.ZOMBIE_SCEPTER.getKey(), TFItems.FORTIFICATION_SCEPTER.getKey());
		this.tag(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS).add(TFItems.EXANIMATE_ESSENCE.getKey());
		this.tag(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS).add(TFItems.TORCHBERRIES.getKey());
		this.tag(TFItemTags.KEEPSAKE_CASKET_REPAIR_ITEMS).add(TFItems.CHARM_OF_KEEPING_3.getKey());
		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder().key(), TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED).add(TFItems.MOONWORM_QUEEN.getKey());
		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(BlockItemIds.ICE.item(), BlockItemIds.PACKED_ICE.item(), BlockItemIds.BLUE_ICE.item());
		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());

		this.tag(TFItemTags.FOODS_JERKY).add(
			TFItems.MONSTER_JERKY.getKey(),
			TFItems.BEEF_JERKY.getKey(),
			TFItems.PORK_JERKY.getKey(),
			TFItems.CHICKEN_JERKY.getKey(),
			TFItems.RABBIT_JERKY.getKey(),
			TFItems.MUTTON_JERKY.getKey(),
			TFItems.VENISON_JERKY.getKey(),
			TFItems.MEEF_JERKY.getKey(),
			TFItems.COD_JERKY.getKey(),
			TFItems.SALMON_JERKY.getKey(),
			TFItems.TROPICAL_FISH_JERKY.getKey(),
			TFItems.FUGU_JERKY.getKey());

		this.tag(TFItemTags.MAZE_SLIME_BALLS).add(TFItems.MAZE_SLIME_BALL.getKey());
		this.tag(TFItemTags.RENDER_LOWER_ON_DRYING_RACK)
			.add(TFItems.GELATINOUS_SLIME_DROP.getKey(), TFItems.GELATINOUS_MAZE_SLIME_DROP.getKey())
			.add(TFItems.ZOMBIE_SKULL_CANDLE.getKey(), TFItems.SKELETON_SKULL_CANDLE.getKey(), TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(), TFItems.CREEPER_SKULL_CANDLE.getKey(), TFItems.PLAYER_SKULL_CANDLE.getKey(), TFItems.PIGLIN_SKULL_CANDLE.getKey())
			.add(BlockItemIds.POINTED_DRIPSTONE.item(), ItemIds.RECOVERY_COMPASS, ItemIds.CLOCK, ItemIds.SPYGLASS, ItemIds.TRIDENT)
			.addTag(ItemTags.BANNERS)
			.addTag(Tags.Items.TOOLS)
			.remove(Tags.Items.TOOLS_SHIELD);

		this.tag(TFItemTags.TROPHIES).add(
			TFItems.NAGA_TROPHY.getKey(), TFItems.LICH_TROPHY.getKey(),
			TFItems.MINOSHROOM_TROPHY.getKey(), TFItems.HYDRA_TROPHY.getKey(),
			TFItems.KNIGHT_PHANTOM_TROPHY.getKey(), TFItems.UR_GHAST_TROPHY.getKey(),
			TFItems.ALPHA_YETI_TROPHY.getKey(), TFItems.SNOW_QUEEN_TROPHY.getKey());

		this.tag(TFItemTags.EMPERORS_CLOTH_APPLICABLE).addTag(Tags.Items.ARMORS).add(ItemIds.ELYTRA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
