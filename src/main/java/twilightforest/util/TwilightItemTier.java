package twilightforest.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.function.Supplier;

public class TwilightItemTier {
	// harvestLevel, maxUses, efficiency, damage, enchantability

	public static final Tier IRONWOOD = TierSortingRegistry.registerTier(
			new ForgeTier(2, 512, 6.5F, 2, 25, BlockTags.createOptional(TwilightForestMod.prefix("needs_ironwood_tool")), () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get())),
			TwilightForestMod.prefix("ironwood"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier FIERY = TierSortingRegistry.registerTier(
			new ForgeTier(4, 1024, 9F, 4, 10, BlockTags.createOptional(TwilightForestMod.prefix("needs_fiery_tool")), () -> Ingredient.of(TFItems.FIERY_INGOT.get())),
			TwilightForestMod.prefix("fiery"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));

	public static final Tier STEELEAF = TierSortingRegistry.registerTier(
			new ForgeTier(3, 131, 8.0F, 3, 9, BlockTags.createOptional(TwilightForestMod.prefix("needs_steeleaf_tool")), () -> Ingredient.of(TFItems.STEELEAF_INGOT.get())),
			TwilightForestMod.prefix("steeleaf"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier KNIGHTMETAL = TierSortingRegistry.registerTier(
			new ForgeTier(3, 512, 8.0F, 3, 8, BlockTags.createOptional(TwilightForestMod.prefix("needs_knightmetal_tool")), () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
			TwilightForestMod.prefix("knightmetal"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier GIANT = TierSortingRegistry.registerTier(
			new ForgeTier(1, 1024, 4.0F, 1.0F, 5, BlockTags.createOptional(TwilightForestMod.prefix("needs_giant_tool")), () -> Ingredient.of(TFBlocks.GIANT_COBBLESTONE.get())),
			TwilightForestMod.prefix("giant"), List.of(Tiers.STONE), List.of(Tiers.IRON));

	public static final Tier ICE = TierSortingRegistry.registerTier(
			new ForgeTier(0, 32, 1.0F, 3.5F, 5, BlockTags.createOptional(TwilightForestMod.prefix("needs_ice_tool")), () -> Ingredient.of(Blocks.PACKED_ICE)),
			TwilightForestMod.prefix("ice"), List.of(), List.of(Tiers.WOOD));

	public static final Tier GLASS = TierSortingRegistry.registerTier(
			new ForgeTier(0, 1, 1.0F, 36.0F, 30, BlockTags.createOptional(TwilightForestMod.prefix("needs_glass_tool")), () -> Ingredient.EMPTY),
			TwilightForestMod.prefix("glass"), List.of(), List.of(Tiers.WOOD));

}
