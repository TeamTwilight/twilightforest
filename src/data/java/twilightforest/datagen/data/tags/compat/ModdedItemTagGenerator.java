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
			.add(TFItems.RAW_IRONWOOD.key(), TFItems.IRONWOOD_INGOT.key(), TFItems.STEELEAF_INGOT.key(),
				TFItems.ARMOR_SHARD.key(), TFItems.ARMOR_SHARD_CLUSTER.key(), TFItems.KNIGHTMETAL_INGOT.key(), TFItems.KNIGHTMETAL_RING.key(),
				TFItems.FIERY_INGOT.key(), TFItems.CHARM_OF_KEEPING_2.key(), TFItems.ORE_MAGNET.key(),
				TFItems.IRONWOOD_HELMET.key(), TFItems.IRONWOOD_CHESTPLATE.key(), TFItems.IRONWOOD_LEGGINGS.key(), TFItems.IRONWOOD_BOOTS.key(),
				TFItems.STEELEAF_HELMET.key(), TFItems.STEELEAF_CHESTPLATE.key(), TFItems.STEELEAF_LEGGINGS.key(), TFItems.STEELEAF_BOOTS.key(),
				TFItems.KNIGHTMETAL_HELMET.key(), TFItems.KNIGHTMETAL_CHESTPLATE.key(), TFItems.KNIGHTMETAL_LEGGINGS.key(), TFItems.KNIGHTMETAL_BOOTS.key(),
				TFItems.FIERY_HELMET.key(), TFItems.FIERY_CHESTPLATE.key(), TFItems.FIERY_LEGGINGS.key(), TFItems.FIERY_BOOTS.key(),
				TFItems.IRONWOOD_SWORD.key(), TFItems.IRONWOOD_PICKAXE.key(), TFItems.IRONWOOD_AXE.key(), TFItems.IRONWOOD_SHOVEL.key(), TFItems.IRONWOOD_HOE.key(),
				TFItems.STEELEAF_SWORD.key(), TFItems.STEELEAF_PICKAXE.key(), TFItems.STEELEAF_AXE.key(), TFItems.STEELEAF_SHOVEL.key(), TFItems.STEELEAF_HOE.key(),
				TFItems.KNIGHTMETAL_SWORD.key(), TFItems.KNIGHTMETAL_PICKAXE.key(), TFItems.KNIGHTMETAL_AXE.key(), TFItems.BLOCK_AND_CHAIN.key(), TFItems.KNIGHTMETAL_SHIELD.key(),
				TFItems.FIERY_SWORD.key(), TFItems.FIERY_PICKAXE.key(), TFItems.MAZEBREAKER_PICKAXE.key());

		this.tag(TFItemTags.AC_RAW_MEATS).add(TFItems.RAW_VENISON.key(), TFItems.RAW_MEEF.key());

		this.tag(TFItemTags.CURIOS_CHARM).add(
			TFItems.CHARM_OF_LIFE_1.key(), TFItems.CHARM_OF_LIFE_2.key(),
			TFItems.CHARM_OF_KEEPING_1.key(), TFItems.CHARM_OF_KEEPING_2.key(), TFItems.CHARM_OF_KEEPING_3.key()
		);

		this.tag(TFItemTags.CURIOS_HEAD).add(
			TFItems.NAGA_TROPHY.key(),
			TFItems.LICH_TROPHY.key(),
			TFItems.MINOSHROOM_TROPHY.key(),
			TFItems.HYDRA_TROPHY.key(),
			TFItems.KNIGHT_PHANTOM_TROPHY.key(),
			TFItems.UR_GHAST_TROPHY.key(),
			TFItems.ALPHA_YETI_TROPHY.key(),
			TFItems.SNOW_QUEEN_TROPHY.key(),
			TFItems.QUEST_RAM_TROPHY.key(),
			TFBlocks.CICADA.asItem().builtInRegistryHolder().key(),
			TFBlocks.FIREFLY.asItem().builtInRegistryHolder().key(),
			TFBlocks.MOONWORM.asItem().builtInRegistryHolder().key(),
			TFItems.CREEPER_SKULL_CANDLE.key(),
			TFItems.PIGLIN_SKULL_CANDLE.key(),
			TFItems.PLAYER_SKULL_CANDLE.key(),
			TFItems.SKELETON_SKULL_CANDLE.key(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.key(),
			TFItems.ZOMBIE_SKULL_CANDLE.key());

		this.tag(TFItemTags.CA_PLANT_FOODS).add(TFItems.TORCHBERRIES.key());

		this.tag(TFItemTags.CA_PLANTS).add(TFItems.LIVEROOT.key(), TFItems.MAGIC_BEANS.key(),
			TFBlocks.HUGE_WATER_LILY.asItem().builtInRegistryHolder().key(), TFBlocks.HUGE_LILY_PAD.asItem().builtInRegistryHolder().key(),
			TFBlocks.TROLLVIDR.asItem().builtInRegistryHolder().key(), TFBlocks.UNRIPE_TROLLBER.asItem().builtInRegistryHolder().key(),
			TFBlocks.TROLLBER.asItem().builtInRegistryHolder().key(), TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder().key(),
			TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder().key(), TFBlocks.MAYAPPLE.asItem().builtInRegistryHolder().key(),
			TFBlocks.CLOVER_PATCH.asItem().builtInRegistryHolder().key(), TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder().key(),
			TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder().key(), TFBlocks.TORCHBERRY_PLANT.asItem().builtInRegistryHolder().key(),
			TFBlocks.ROOT_STRAND.asItem().builtInRegistryHolder().key(), TFBlocks.FALLEN_LEAVES.asItem().builtInRegistryHolder().key(),
			TFBlocks.HEDGE.asItem().builtInRegistryHolder().key(), TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder().key(), TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder().key());

		this.tag(TFItemTags.FA_MODIFIER_ETERNAL_INCOMPATIBLE).add(TFItems.GLASS_SWORD.key());

		this.tag(TFItemTags.FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON.key(), TFItems.RAW_MEEF.key());

		this.tag(TFItemTags.RANDOMIUM_BLACKLIST).addTag(TFItemTags.WIP).add(TFItems.GLASS_SWORD.key(), //this one is here because the ore can give the unbreakable one
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
