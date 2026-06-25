package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ModdedItemTagGenerator extends ItemTagsProvider {

	public ModdedItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFItemTags.AC_FERNS).add(TFBlocks.FIDDLEHEAD.get().asItem());
		this.tag(TFItemTags.AC_FERROMAGNETIC_ITEMS)
			.addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)
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

		this.tag(TFItemTags.AC_RAW_MEATS).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());

		this.tag(TFItemTags.CURIOS_CHARM).add(
			TFItems.CHARM_OF_LIFE_1.get(), TFItems.CHARM_OF_LIFE_2.get(),
			TFItems.CHARM_OF_KEEPING_1.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.CHARM_OF_KEEPING_3.get()
		);

		this.tag(TFItemTags.CURIOS_HEAD).add(
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

		this.tag(TFItemTags.CA_PLANT_FOODS).add(TFItems.TORCHBERRIES.get());

		this.tag(TFItemTags.CA_PLANTS).add(TFItems.LIVEROOT.get(), TFItems.MAGIC_BEANS.get(),
			TFBlocks.HUGE_WATER_LILY.get().asItem(), TFBlocks.HUGE_LILY_PAD.get().asItem(),
			TFBlocks.TROLLVIDR.get().asItem(), TFBlocks.UNRIPE_TROLLBER.get().asItem(),
			TFBlocks.TROLLBER.get().asItem(), TFBlocks.HUGE_STALK.get().asItem(),
			TFBlocks.THORN_ROSE.get().asItem(), TFBlocks.MAYAPPLE.get().asItem(),
			TFBlocks.CLOVER_PATCH.get().asItem(), TFBlocks.FIDDLEHEAD.get().asItem(),
			TFBlocks.MUSHGLOOM.get().asItem(), TFBlocks.TORCHBERRY_PLANT.get().asItem(),
			TFBlocks.ROOT_STRAND.get().asItem(), TFBlocks.FALLEN_LEAVES.get().asItem(),
			TFBlocks.HEDGE.get().asItem(), TFBlocks.ROOT_BLOCK.get().asItem(), TFBlocks.LIVEROOT_BLOCK.get().asItem());

		this.tag(TFItemTags.FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON.get(), TFItems.RAW_MEEF.get());

		this.tag(TFItemTags.RANDOMIUM_BLACKLIST).addTag(TFItemTags.WIP).add(TFItems.GLASS_SWORD.get(), //this one is here because the ore can give the unbreakable one
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
}
