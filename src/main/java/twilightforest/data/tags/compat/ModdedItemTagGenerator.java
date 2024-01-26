package twilightforest.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ModdedItemTagGenerator extends ItemTagsProvider {
	public static final TagKey<Item> AC_FERNS = createTagFor("alexscaves", "ferns");
	public static final TagKey<Item> AC_FERROMAGNETIC_ITEMS = createTagFor("alexscaves", "ferromagnetic_items");
	public static final TagKey<Item> AC_RAW_MEATS = createTagFor("alexscaves", "raw_meats");

	public static final TagKey<Item> CURIOS_CHARM = createTagFor("curios", "charm");
	public static final TagKey<Item> CURIOS_HEAD = createTagFor("curios", "head");

	public static final TagKey<Item> CA_PLANTS = createTagFor("createaddition", "plants");
	public static final TagKey<Item> CA_PLANT_FOODS = createTagFor("createaddition", "plant_foods");

	public static final TagKey<Item> FD_CABBAGE_ROLL_INGREDIENTS = createTagFor("farmersdelight", "cabbage_roll_ingredients");

	public static final TagKey<Item> RANDOMIUM_BLACKLIST = createTagFor("randomium", "blacklist");

	public ModdedItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(AC_FERNS).add(TFBlocks.FIDDLEHEAD.get().asItem());
		tag(AC_FERROMAGNETIC_ITEMS)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_STEELEAF)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL)
				.add(TFBlocks.CANDELABRA.get().asItem(), TFBlocks.WROUGHT_IRON_FENCE.get().asItem())
				.add(TFItems.RAW_IRONWOOD.get(), TFItems.IRONWOOD_INGOT.get(), TFItems.STEELEAF_INGOT.get(),
						TFItems.ARMOR_SHARD.get(), TFItems.ARMOR_SHARD_CLUSTER.get(), TFItems.KNIGHTMETAL_INGOT.get(), TFItems.KNIGHTMETAL_RING.get(),
						TFItems.FIERY_INGOT.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.ORE_MAGNET.get(),
						TFItems.IRONWOOD_HELMET.get(), TFItems.IRONWOOD_CHESTPLATE.get(), TFItems.IRONWOOD_LEGGINGS.get(), TFItems.IRONWOOD_BOOTS.get(),
						TFItems.STEELEAF_HELMET.get(), TFItems.STEELEAF_CHESTPLATE.get(), TFItems.STEELEAF_LEGGINGS.get(), TFItems.STEELEAF_BOOTS.get(),
						TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get(),
						TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get(),
						TFItems.IRONWOOD_SWORD.get(), TFItems.IRONWOOD_PICKAXE.get(), TFItems.IRONWOOD_AXE.get(), TFItems.IRONWOOD_SHOVEL.get(), TFItems.IRONWOOD_HOE.get(),
						TFItems.STEELEAF_SWORD.get(), TFItems.STEELEAF_PICKAXE.get(), TFItems.STEELEAF_AXE.get(), TFItems.STEELEAF_SHOVEL.get(), TFItems.STEELEAF_HOE.get(),
						TFItems.KNIGHTMETAL_SWORD.get(), TFItems.KNIGHTMETAL_PICKAXE.get(), TFItems.KNIGHTMETAL_AXE.get(), TFItems.BLOCK_AND_CHAIN.get(), TFItems.KNIGHTMETAL_SHIELD.get(),
						TFItems.FIERY_SWORD.get(), TFItems.FIERY_PICKAXE.get(), TFItems.MAZEBREAKER_PICKAXE.get());

		tag(AC_RAW_MEATS).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());

		tag(CURIOS_CHARM).add(
				TFItems.CHARM_OF_LIFE_1.get(), TFItems.CHARM_OF_LIFE_2.get(),
				TFItems.CHARM_OF_KEEPING_1.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.CHARM_OF_KEEPING_3.get()
		);

		tag(CURIOS_HEAD).add(
				TFItems.NAGA_TROPHY.get(),
				TFItems.LICH_TROPHY.get(),
				TFItems.MINOSHROOM_TROPHY.get(),
				TFItems.HYDRA_TROPHY.get(),
				TFItems.KNIGHT_PHANTOM_TROPHY.get(),
				TFItems.UR_GHAST_TROPHY.get(),
				TFItems.ALPHA_YETI_TROPHY.get(),
				TFItems.SNOW_QUEEN_TROPHY.get(),
				TFItems.QUEST_RAM_TROPHY.get(),
				TFBlocks.CICADA.get().asItem(),
				TFBlocks.FIREFLY.get().asItem(),
				TFBlocks.MOONWORM.get().asItem(),
				TFItems.CREEPER_SKULL_CANDLE.get(),
				TFItems.PIGLIN_SKULL_CANDLE.get(),
				TFItems.PLAYER_SKULL_CANDLE.get(),
				TFItems.SKELETON_SKULL_CANDLE.get(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
				TFItems.ZOMBIE_SKULL_CANDLE.get());

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

		tag(FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());

		tag(RANDOMIUM_BLACKLIST).addTag(ItemTagGenerator.NYI).addTag(ItemTagGenerator.WIP).add(TFItems.GLASS_SWORD.get(), //this one is here because the ore can give the unbreakable one
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
	}

	private static TagKey<Item> createTagFor(String modid, String tagName) {
		return ItemTags.create(new ResourceLocation(modid, tagName));
	}
}
