package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ModdedItemTagGenerator extends BlockTagCopyingItemTagProvider {

	public ModdedItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider) {
		super(output, future, provider, TwilightForestMod.ID);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFItemTags.AC_FERNS).add(TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.AC_FERROMAGNETIC_ITEMS)
			.addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)
			.add(TFBlocks.CANDELABRA.asItem().builtInRegistryHolder().key(), TFBlocks.WROUGHT_IRON_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFItems.RAW_IRONWOOD.getKey(), TFItems.IRONWOOD_INGOT.getKey(), TFItems.STEELEAF_INGOT.getKey(),
				TFItems.ARMOR_SHARD.getKey(), TFItems.ARMOR_SHARD_CLUSTER.getKey(), TFItems.KNIGHTMETAL_INGOT.getKey(), TFItems.KNIGHTMETAL_RING.getKey(),
				TFItems.FIERY_INGOT.getKey(), TFItems.CHARM_OF_KEEPING_2.getKey(), TFItems.ORE_MAGNET.getKey(),
				TFItems.IRONWOOD_HELMET.getKey(), TFItems.IRONWOOD_CHESTPLATE.getKey(), TFItems.IRONWOOD_LEGGINGS.getKey(), TFItems.IRONWOOD_BOOTS.getKey(),
				TFItems.STEELEAF_HELMET.getKey(), TFItems.STEELEAF_CHESTPLATE.getKey(), TFItems.STEELEAF_LEGGINGS.getKey(), TFItems.STEELEAF_BOOTS.getKey(),
				TFItems.KNIGHTMETAL_HELMET.getKey(), TFItems.KNIGHTMETAL_CHESTPLATE.getKey(), TFItems.KNIGHTMETAL_LEGGINGS.getKey(), TFItems.KNIGHTMETAL_BOOTS.getKey(),
				TFItems.FIERY_HELMET.getKey(), TFItems.FIERY_CHESTPLATE.getKey(), TFItems.FIERY_LEGGINGS.getKey(), TFItems.FIERY_BOOTS.getKey(),
				TFItems.IRONWOOD_SWORD.getKey(), TFItems.IRONWOOD_PICKAXE.getKey(), TFItems.IRONWOOD_AXE.getKey(), TFItems.IRONWOOD_SHOVEL.getKey(), TFItems.IRONWOOD_HOE.getKey(),
				TFItems.STEELEAF_SWORD.getKey(), TFItems.STEELEAF_PICKAXE.getKey(), TFItems.STEELEAF_AXE.getKey(), TFItems.STEELEAF_SHOVEL.getKey(), TFItems.STEELEAF_HOE.getKey(),
				TFItems.KNIGHTMETAL_SWORD.getKey(), TFItems.KNIGHTMETAL_PICKAXE.getKey(), TFItems.KNIGHTMETAL_AXE.getKey(), TFItems.BLOCK_AND_CHAIN.getKey(), TFItems.KNIGHTMETAL_SHIELD.getKey(),
				TFItems.FIERY_SWORD.getKey(), TFItems.FIERY_PICKAXE.getKey(), TFItems.MAZEBREAKER_PICKAXE.getKey());

		this.tag(TFItemTags.AC_RAW_MEATS).add(TFItems.RAW_VENISON.getKey(), TFItems.RAW_MEEF.getKey());

		this.tag(TFItemTags.CURIOS_CHARM).add(
			TFItems.CHARM_OF_LIFE_1.getKey(), TFItems.CHARM_OF_LIFE_2.getKey(),
			TFItems.CHARM_OF_KEEPING_1.getKey(), TFItems.CHARM_OF_KEEPING_2.getKey(), TFItems.CHARM_OF_KEEPING_3.getKey()
		);

		this.tag(TFItemTags.CURIOS_HEAD).add(
			TFItems.NAGA_TROPHY.getKey(),
			TFItems.LICH_TROPHY.getKey(),
			TFItems.MINOSHROOM_TROPHY.getKey(),
			TFItems.HYDRA_TROPHY.getKey(),
			TFItems.KNIGHT_PHANTOM_TROPHY.getKey(),
			TFItems.UR_GHAST_TROPHY.getKey(),
			TFItems.ALPHA_YETI_TROPHY.getKey(),
			TFItems.SNOW_QUEEN_TROPHY.getKey(),
			TFItems.QUEST_RAM_TROPHY.getKey(),
			TFBlocks.CICADA.asItem().builtInRegistryHolder().key(),
			TFBlocks.FIREFLY.asItem().builtInRegistryHolder().key(),
			TFBlocks.MOONWORM.asItem().builtInRegistryHolder().key(),
			TFItems.CREEPER_SKULL_CANDLE.getKey(),
			TFItems.PIGLIN_SKULL_CANDLE.getKey(),
			TFItems.PLAYER_SKULL_CANDLE.getKey(),
			TFItems.SKELETON_SKULL_CANDLE.getKey(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(),
			TFItems.ZOMBIE_SKULL_CANDLE.getKey());

		this.tag(TFItemTags.CA_PLANT_FOODS).add(TFItems.TORCHBERRIES.getKey());

		this.tag(TFItemTags.CA_PLANTS).add(TFItems.LIVEROOT.getKey(), TFItems.MAGIC_BEANS.getKey(),
			TFBlocks.HUGE_WATER_LILY.asItem().builtInRegistryHolder().key(), TFBlocks.HUGE_LILY_PAD.asItem().builtInRegistryHolder().key(),
			TFBlocks.TROLLVIDR.asItem().builtInRegistryHolder().key(), TFBlocks.UNRIPE_TROLLBER.asItem().builtInRegistryHolder().key(),
			TFBlocks.TROLLBER.asItem().builtInRegistryHolder().key(), TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder().key(),
			TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder().key(), TFBlocks.MAYAPPLE.asItem().builtInRegistryHolder().key(),
			TFBlocks.CLOVER_PATCH.asItem().builtInRegistryHolder().key(), TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder().key(),
			TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder().key(), TFBlocks.TORCHBERRY_PLANT.asItem().builtInRegistryHolder().key(),
			TFBlocks.ROOT_STRAND.asItem().builtInRegistryHolder().key(), TFBlocks.FALLEN_LEAVES.asItem().builtInRegistryHolder().key(),
			TFBlocks.HEDGE.asItem().builtInRegistryHolder().key(), TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder().key(), TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder().key());

		this.tag(TFItemTags.FA_MODIFIER_ETERNAL_INCOMPATIBLE).add(TFItems.GLASS_SWORD.getKey());

		this.tag(TFItemTags.FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON.getKey(), TFItems.RAW_MEEF.getKey());

		this.tag(TFItemTags.RANDOMIUM_BLACKLIST).addTag(TFItemTags.WIP).add(TFItems.GLASS_SWORD.getKey(), //this one is here because the ore can give the unbreakable one
			TFBlocks.TIME_LOG_CORE.asItem().builtInRegistryHolder().key(), TFBlocks.TRANSFORMATION_LOG_CORE.asItem().builtInRegistryHolder().key(),
			TFBlocks.MINING_LOG_CORE.asItem().builtInRegistryHolder().key(), TFBlocks.SORTING_LOG_CORE.asItem().builtInRegistryHolder().key(),
			TFBlocks.ANTIBUILDER.asItem().builtInRegistryHolder().key(), TFBlocks.STRONGHOLD_SHIELD.asItem().builtInRegistryHolder().key(),
			TFBlocks.LOCKED_VANISHING_BLOCK.asItem().builtInRegistryHolder().key(), TFBlocks.BROWN_THORNS.asItem().builtInRegistryHolder().key(),
			TFBlocks.GREEN_THORNS.asItem().builtInRegistryHolder().key(), TFBlocks.BURNT_THORNS.asItem().builtInRegistryHolder().key(),
			TFBlocks.PINK_FORCE_FIELD.asItem().builtInRegistryHolder().key(), TFBlocks.ORANGE_FORCE_FIELD.asItem().builtInRegistryHolder().key(),
			TFBlocks.GREEN_FORCE_FIELD.asItem().builtInRegistryHolder().key(), TFBlocks.BLUE_FORCE_FIELD.asItem().builtInRegistryHolder().key(),
			TFBlocks.VIOLET_FORCE_FIELD.asItem().builtInRegistryHolder().key(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFBlocks.NAGA_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(), TFBlocks.LICH_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(), TFBlocks.HYDRA_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(), TFBlocks.UR_GHAST_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.asItem().builtInRegistryHolder().key());
	}
}
