package twilightforest.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.NoSmithingTemplateRecipeBuilder;
import twilightforest.data.custom.ScepterRecipeBuilder;
import twilightforest.data.custom.UncraftingGenerator;
import twilightforest.data.helpers.CraftingDataHelper;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.*;

import java.util.concurrent.CompletableFuture;

public class CraftingGenerator extends CraftingDataHelper {

	private final HolderLookup.Provider provider;

	public CraftingGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
		this.provider = provider.join();
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		StonecuttingGenerator.buildRecipes(output);
		UncraftingGenerator.buildRecipes(output);

		// The Recipe Builder currently doesn't support enchantment-resulting recipes, those must be manually created.
		// IT DOES NOW WOOOOOOOOO
		blockCompressionRecipes(output);
		equipmentRecipes(this.provider, output);
		emptyMapRecipes(output);
		woodRecipes(output);
		fieryConversions(output);

		nagastoneRecipes(output);
		darkTowerRecipes(output);
		castleRecipes(output);

		bannerPattern(output, "naga_banner_pattern", TFBlocks.NAGA_TROPHY, TFItems.NAGA_BANNER_PATTERN);
		bannerPattern(output, "lich_banner_pattern", TFBlocks.LICH_TROPHY, TFItems.LICH_BANNER_PATTERN);
		bannerPattern(output, "minoshroom_banner_pattern", TFBlocks.MINOSHROOM_TROPHY, TFItems.MINOSHROOM_BANNER_PATTERN);
		bannerPattern(output, "hydra_banner_pattern", TFBlocks.HYDRA_TROPHY, TFItems.HYDRA_BANNER_PATTERN);
		bannerPattern(output, "knight_phantom_banner_pattern", TFBlocks.KNIGHT_PHANTOM_TROPHY, TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
		bannerPattern(output, "ur_ghast_banner_pattern", TFBlocks.UR_GHAST_TROPHY, TFItems.UR_GHAST_BANNER_PATTERN);
		bannerPattern(output, "alpha_yeti_banner_pattern", TFBlocks.ALPHA_YETI_TROPHY, TFItems.ALPHA_YETI_BANNER_PATTERN);
		bannerPattern(output, "snow_queen_banner_pattern", TFBlocks.SNOW_QUEEN_TROPHY, TFItems.SNOW_QUEEN_BANNER_PATTERN);
//		bannerPattern(output, "questing_ram_banner_pattern", TFBlocks.QUEST_RAM_TROPHY, TFItems.QUEST_RAM_BANNER_PATTERN);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSS_BLOCK)
			.pattern("mmm")
			.pattern("mtm")
			.pattern("mmm")
			.define('m', Ingredient.of(TFBlocks.MOSS_PATCH.get()))
			.define('t', Ingredient.of(TFItems.TRANSFORMATION_POWDER.get()))
			.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.get()))
			.save(output, TwilightForestMod.prefix("tf_moss_to_vanilla"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.MOSS_PATCH.get(), 8)
			.requires(Ingredient.of(Items.MOSS_BLOCK))
			.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.get()))
			.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.get()))
			.save(output, TwilightForestMod.prefix("vanilla_to_tf_moss"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.HUGE_LILY_PAD.get())
			.requires(Ingredient.of(Blocks.LILY_PAD), 4)
			.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.get()))
			.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.get()))
			.save(output, TwilightForestMod.prefix("vanilla_to_tf_lilypad"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, Blocks.LILY_PAD, 4)
			.requires(Ingredient.of(TFBlocks.HUGE_LILY_PAD.get()))
			.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.get()))
			.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.get()))
			.save(output, TwilightForestMod.prefix("tf_to_vanilla_lilypad"));

		slabBlock(output, "aurora_slab", TFBlocks.AURORA_SLAB, TFBlocks.AURORA_BLOCK);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.AURORA_PILLAR.get(), 2)
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.AURORA_BLOCK.get()))
			.unlockedBy("has_slab", has(TFBlocks.AURORA_SLAB.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.IRON_LADDER.get(), 3)
			.pattern("-#-")
			.pattern("-#-")
			.define('#', Ingredient.of(Blocks.IRON_BARS))
			.define('-', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_iron_bars", has(Blocks.IRON_BARS))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.ROPE.get(), 8)
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.ROOT_STRAND.get()))
			.unlockedBy("has_root_strand", has(TFBlocks.ROOT_STRAND.get()))
			.save(output);

//		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW.value(), 4)
//			.pattern("GPG")
//			.pattern("PPP")
//			.pattern("GPG")
//			.define('G', Ingredient.of(Tags.Items.GLASS_BLOCKS))
//			.define('P', Ingredient.of(TFBlocks.CANOPY_PLANKS.value()))
//			.unlockedBy("has_planks", has(TFBlocks.CANOPY_PLANKS.value()))
//			.save(output);
//
//		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW_PANE.value(), 16)
//			.pattern("GGG")
//			.pattern("GGG")
//			.define('G', Ingredient.of(TFBlocks.CANOPY_WINDOW.value()))
//			.unlockedBy("has_windows", has(TFBlocks.CANOPY_WINDOW.value()))
//			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFItems.MASON_JAR.get(), 4)
			.pattern("GLG")
			.pattern("G G")
			.pattern("GGG")
			.define('G', Ingredient.of(Items.GLASS))
			.define('L', Ingredient.of(TFBlocks.TWILIGHT_OAK_LOG.get()))
			.unlockedBy("has_tf_oak", has(TFBlocks.TWILIGHT_OAK_LOG.value()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_JAR.get())
			.requires(Ingredient.of(TFBlocks.FIREFLY.get()))
			.requires(Ingredient.of(TFItems.MASON_JAR.get()))
			.unlockedBy("has_item", has(TFBlocks.FIREFLY.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_SPAWNER.get())
			.requires(Ingredient.of(TFBlocks.FIREFLY_JAR.get()))
			.requires(Ingredient.of(TFBlocks.FIREFLY.get()))
			.requires(Ingredient.of(Blocks.POPPY))
			.unlockedBy("has_jar", has(TFBlocks.FIREFLY_JAR.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.CICADA_JAR.get())
			.requires(Ingredient.of(TFBlocks.CICADA.get()))
			.requires(Ingredient.of(TFItems.MASON_JAR.get()))
			.unlockedBy("has_item", has(TFBlocks.CICADA.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE)
			.requires(Ingredient.of(TFBlocks.HUGE_WATER_LILY.get()))
			.unlockedBy("has_item", has(TFBlocks.HUGE_WATER_LILY.get()))
			.save(output, TwilightForestMod.prefix("waterlily_to_magenta"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RED_DYE)
			.requires(Ingredient.of(TFBlocks.THORN_ROSE.get()))
			.unlockedBy("has_item", has(TFBlocks.THORN_ROSE.get()))
			.save(output, TwilightForestMod.prefix("thorn_rose_to_red"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STICK)
			.requires(Ingredient.of(TFBlocks.ROOT_STRAND.get()))
			.unlockedBy("has_item", has(TFBlocks.ROOT_STRAND.get()))
			.group("sticks")
			.save(output, TwilightForestMod.prefix("root_stick"));

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
			.pattern("∴")
			.pattern("|")
			.define('∴', Ingredient.of(TFItems.TORCHBERRIES.get()))
			.define('|', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(TFItems.TORCHBERRIES.get()))
			.save(output, TwilightForestMod.prefix("berry_torch"));

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.UNCRAFTING_TABLE.get())
			.pattern("###")
			.pattern("#X#")
			.pattern("###")
			.define('#', Blocks.CRAFTING_TABLE)
			.define('X', TFItems.MAZE_MAP_FOCUS.get())
			.unlockedBy("has_uncrafting_table", has(TFBlocks.UNCRAFTING_TABLE.get()))
			.save(output.withConditions(UncraftingTableCondition.INSTANCE), TwilightForestMod.prefix("uncrafting_table"));

		cookingRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200);
		cookingRecipes(output, "smoked", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100);
		cookingRecipes(output, "campfired", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600);

		ingotRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200);
		ingotRecipes(output, "blasted", RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, 100);

		crackedWoodRecipes(output);
		crackedStoneRecipes(output);

//		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.CHISELED_CANOPY_BOOKSHELF.get())
//			.pattern("---")
//			.pattern("   ")
//			.pattern("---")
//			.define('-', TFBlocks.CANOPY_SLAB.get())
//			.unlockedBy("has_item", has(TFBlocks.CANOPY_SLAB.get()))
//			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.CANOPY_BOOKSHELF.get())
			.pattern("---")
			.pattern("B B")
			.pattern("---")
			.define('-', TFBlocks.CANOPY_PLANKS.get())
			.define('B', Items.BOOK)
			.unlockedBy("has_item", has(TFBlocks.CANOPY_PLANKS.get()))
			.save(output);

//		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA.get())
//			.pattern("III")
//			.pattern(" W ")
//			.define('W', TFBlocks.WROUGHT_IRON_FENCE)
//			.define('I', TFItems.WROUGHT_IRON_BAR)
//			.unlockedBy("has_fence", has(TFBlocks.WROUGHT_IRON_FENCE))
//			.unlockedBy("has_bar", has(TFItems.WROUGHT_IRON_BAR))
//			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.ARMOR_SHARD_CLUSTER.get())
			.requires(Ingredient.of(TFItems.ARMOR_SHARD.get()), 9)
			.unlockedBy("has_item", has(TFItems.ARMOR_SHARD.get()))
			.save(output, TwilightForestMod.prefix("material/" + TFItems.ARMOR_SHARD_CLUSTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_UNDERBRICK.get(), 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.UNDERBRICK.get()))
			.unlockedBy("has_item", has(TFBlocks.UNDERBRICK.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_MAZESTONE.get(), 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.MAZESTONE_BRICK.get()))
			.unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK.get()))
			.save(output, TwilightForestMod.prefix("maze_stone/mossy_mazestone"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.CARMINITE.get())
			.requires(Ingredient.of(TFItems.BORER_ESSENCE.get()))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE.get()))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(Items.GHAST_TEAR))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE.get()))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE.get()))
			.unlockedBy("has_item", has(TFItems.BORER_ESSENCE.get()))
			.save(output, TwilightForestMod.prefix("material/" + TFItems.CARMINITE.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.RAW_IRONWOOD.get(), 2)
			.requires(Ingredient.of(TFItems.LIVEROOT.get()))
			.requires(Ingredient.of(Items.RAW_IRON))
			.requires(Tags.Items.NUGGETS_GOLD)
			.unlockedBy("has_item", has(TFItems.LIVEROOT.get()))
			.save(output, TwilightForestMod.prefix("material/" + TFItems.RAW_IRONWOOD.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.RAINY_CLOUD.get(), 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD.get()))
			.define('b', Ingredient.of(Items.WATER_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD.get()))
			.save(output, TwilightForestMod.prefix("rainy_cloud"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.SNOWY_CLOUD.get(), 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD.get()))
			.define('b', Ingredient.of(Items.POWDER_SNOW_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD.get()))
			.save(output, TwilightForestMod.prefix("snowy_cloud"));

//		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.LICH_CROWN, 1)
//			.pattern("ttt")
//			.pattern("t t")
//			.pattern("ttt")
//			.define('t', Ingredient.of(TFItems.CROWN_SPLINTER))
//			.unlockedBy("has_item", has(TFItems.CROWN_SPLINTER))
//			.save(output);
	}

	private void darkTowerRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.ENCASED_FIRE_JET.get())
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("uuu")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('^', Ingredient.of(TFBlocks.FIRE_JET.get()))
			.define('u', Ingredient.of(Items.LAVA_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FIRE_JET.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.ENCASED_SMOKER.get())
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('^', Ingredient.of(TFBlocks.SMOKER.get()))
			.unlockedBy("has_item", has(TFBlocks.SMOKER.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.CARMINITE_BUILDER.get())
			.pattern("#6#")
			.pattern("6o6")
			.pattern("#6#")
			.define('6', ItemTagGenerator.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('o', Ingredient.of(Blocks.DISPENSER))
			.unlockedBy("has_item", has(ItemTagGenerator.CARMINITE_GEMS))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.CARMINITE_REACTOR.get())
			.pattern("#6#")
			.pattern("6%6")
			.pattern("#6#")
			.define('6', ItemTagGenerator.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('%', Tags.Items.ORES_REDSTONE)
			.unlockedBy("has_item", has(ItemTagGenerator.CARMINITE_GEMS))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.REAPPEARING_BLOCK.get(), 2)
			.pattern("#∴#")
			.pattern("∴6∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('6', ItemTagGenerator.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.VANISHING_BLOCK.get(), 8)
			.pattern("#w#")
			.pattern("w6w")
			.pattern("#w#")
			.define('w', ItemTagGenerator.TOWERWOOD)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.get()))
			.define('6', ItemTagGenerator.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_TOWERWOOD.get())
			.requires(Ingredient.of(TFBlocks.TOWERWOOD.get()))
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.unlockedBy("has_item", has(TFBlocks.TOWERWOOD.get()))
			.save(output, TwilightForestMod.prefix("wood/" + TFBlocks.MOSSY_TOWERWOOD.getId().getPath()));

	}

	private void equipmentRecipes(HolderLookup.Provider provider, RecipeOutput output) {
		bootsItem(output, TFItems.IRONWOOD_BOOTS, ItemTagGenerator.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FEATHER_FALLING, 1)));
		leggingsItem(output, TFItems.IRONWOOD_LEGGINGS, ItemTagGenerator.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 1)));
		chestplateItem(output, TFItems.IRONWOOD_CHESTPLATE, ItemTagGenerator.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 1)));
		helmetItem(output, TFItems.IRONWOOD_HELMET, ItemTagGenerator.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.AQUA_AFFINITY, 1)));
		swordItem(output, TFItems.IRONWOOD_SWORD, ItemTagGenerator.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.KNOCKBACK, 1)));
		pickaxeItem(output, TFItems.IRONWOOD_PICKAXE, ItemTagGenerator.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 1)));
		axeItem(output, TFItems.IRONWOOD_AXE, ItemTagGenerator.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 1)));
		shovelItem(output, TFItems.IRONWOOD_SHOVEL, ItemTagGenerator.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.UNBREAKING, 1)));
		hoeItem(output, TFItems.IRONWOOD_HOE, ItemTagGenerator.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 1)));

		bootsItem(output, TFItems.STEELEAF_BOOTS, ItemTagGenerator.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FEATHER_FALLING, 2)));
		leggingsItem(output, TFItems.STEELEAF_LEGGINGS, ItemTagGenerator.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FIRE_PROTECTION, 2)));
		chestplateItem(output, TFItems.STEELEAF_CHESTPLATE, ItemTagGenerator.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.BLAST_PROTECTION, 2)));
		helmetItem(output, TFItems.STEELEAF_HELMET, ItemTagGenerator.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROJECTILE_PROTECTION, 2)));
		swordItem(output, TFItems.STEELEAF_SWORD, ItemTagGenerator.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.LOOTING, 2)));
		pickaxeItem(output, TFItems.STEELEAF_PICKAXE, ItemTagGenerator.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 2)));
		axeItem(output, TFItems.STEELEAF_AXE, ItemTagGenerator.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 2)));
		shovelItem(output, TFItems.STEELEAF_SHOVEL, ItemTagGenerator.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 2)));
		hoeItem(output, TFItems.STEELEAF_HOE, ItemTagGenerator.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 2)));

		bootsItem(output, TFItems.ARCTIC_BOOTS, ItemTagGenerator.ARCTIC_FUR);
		chestplateItem(output, TFItems.ARCTIC_CHESTPLATE, ItemTagGenerator.ARCTIC_FUR);
		helmetItem(output, TFItems.ARCTIC_HELMET, ItemTagGenerator.ARCTIC_FUR);
		leggingsItem(output, TFItems.ARCTIC_LEGGINGS, ItemTagGenerator.ARCTIC_FUR);

		bootsItem(output, TFItems.KNIGHTMETAL_BOOTS, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		chestplateItem(output, TFItems.KNIGHTMETAL_CHESTPLATE, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		helmetItem(output, TFItems.KNIGHTMETAL_HELMET, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		leggingsItem(output, TFItems.KNIGHTMETAL_LEGGINGS, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		pickaxeItem(output, TFItems.KNIGHTMETAL_PICKAXE, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		swordItem(output, TFItems.KNIGHTMETAL_SWORD, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		axeItem(output, TFItems.KNIGHTMETAL_AXE, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.FIERY_BOOTS)
			.pattern("# #")
			.pattern("# #")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_boots")
			.save(output, locEquip(TFItems.FIERY_BOOTS.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.FIERY_LEGGINGS)
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_leggings")
			.save(output, locEquip(TFItems.FIERY_LEGGINGS.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.FIERY_CHESTPLATE)
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_chestplate")
			.save(output, locEquip(TFItems.FIERY_CHESTPLATE.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.FIERY_HELMET)
			.pattern("###")
			.pattern("# #")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_helmet")
			.save(output, locEquip(TFItems.FIERY_HELMET.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_pickaxe")
			.save(output, locEquip(TFItems.FIERY_PICKAXE.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.FIERY_SWORD)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', ItemTagGenerator.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_INGOTS))
			.group("fiery_sword")
			.save(output, locEquip(TFItems.FIERY_SWORD.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.NAGA_CHESTPLATE, 1, this.buildEnchants(provider, Pair.of(Enchantments.FIRE_PROTECTION, 3)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(output, locEquip(TFItems.NAGA_CHESTPLATE.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.NAGA_LEGGINGS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 3)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(output, locEquip(TFItems.NAGA_LEGGINGS.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.YETI_HELMET, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(output, locEquip(TFItems.YETI_HELMET.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.YETI_CHESTPLATE, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(output, locEquip(TFItems.YETI_CHESTPLATE.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.YETI_LEGGINGS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(output, locEquip(TFItems.YETI_LEGGINGS.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(TFItems.YETI_BOOTS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2), Pair.of(Enchantments.FEATHER_FALLING, 4)).build()))
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(output, locEquip(TFItems.YETI_BOOTS.getKey().location().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TFItems.GIANT_PICKAXE.get())
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', TFBlocks.GIANT_COBBLESTONE.get())
			.define('X', TFBlocks.GIANT_LOG.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.get()))
			.save(output, locEquip(TFItems.GIANT_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TFItems.GIANT_SWORD.get())
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', TFBlocks.GIANT_COBBLESTONE.get())
			.define('X', TFBlocks.GIANT_LOG.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.get()))
			.save(output, locEquip(TFItems.GIANT_SWORD.getId().getPath()));

		charmRecipe(output, "charm_of_keeping_2", TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_1);
		charmRecipe(output, "charm_of_keeping_3", TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_KEEPING_2);
		charmRecipe(output, "charm_of_life_2", TFItems.CHARM_OF_LIFE_2, TFItems.CHARM_OF_LIFE_1);

		SpecialRecipeBuilder.special(MoonwormQueenRepairRecipe::new).save(output, TwilightForestMod.prefix("moonworm_queen_repair_recipe").toString());
		SpecialRecipeBuilder.special(MagicMapCloningRecipe::new).save(output, TwilightForestMod.prefix("magic_map_cloning_recipe").toString());
		SpecialRecipeBuilder.special(MazeMapCloningRecipe::new).save(output, TwilightForestMod.prefix("maze_map_cloning_recipe").toString());
		SpecialRecipeBuilder.special(EmperorsClothRecipe::new).save(output, TwilightForestMod.prefix("emperors_cloth_recipe").toString());

		NoSmithingTemplateRecipeBuilder
			.noTemplate(Ingredient.of(Tags.Items.ARMORS), Ingredient.of(TFItems.EMPERORS_CLOTH.get()), RecipeCategory.MISC)
			.attachData(TFDataComponents.EMPERORS_CLOTH::value, Unit.INSTANCE)
			.unlocks("has_cloth", has(TFItems.EMPERORS_CLOTH))
			.save(output, TwilightForestMod.prefix("emperors_cloth_smithing"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 64)
			.requires(TFBlocks.GIANT_COBBLESTONE.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.get()))
			.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_COBBLESTONE.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.COBBLESTONE).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_PLANKS, 64)
			.requires(TFBlocks.GIANT_LOG.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_LOG.get()))
			.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_LOG.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_PLANKS).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LEAVES, 64)
			.requires(TFBlocks.GIANT_LEAVES.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_LEAVES.get()))
			.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_LEAVES.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_LEAVES).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OBSIDIAN, 64)
			.requires(TFBlocks.GIANT_OBSIDIAN.get())
			.unlockedBy("has_item", has(TFBlocks.GIANT_OBSIDIAN.get()))
			.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_OBSIDIAN.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OBSIDIAN).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.BLOCK_AND_CHAIN.get())
			.requires(Ingredient.of(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL))
			.requires(Ingredient.of(ItemTagGenerator.KNIGHTMETAL_INGOTS), 3)
			.requires(Ingredient.of(TFItems.KNIGHTMETAL_RING.get()))
			.unlockedBy("has_block", has(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL))
			.unlockedBy("has_ingot", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING.get()))
			.save(output, locEquip(TFItems.BLOCK_AND_CHAIN.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.KNIGHTMETAL_RING.get())
			.pattern(" - ")
			.pattern("- -")
			.pattern(" - ")
			.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
			.unlockedBy("has_item", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
			.save(output, locEquip(TFItems.KNIGHTMETAL_RING.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.KNIGHTMETAL_SHIELD.get())
			.pattern("-#")
			.pattern("-o")
			.pattern("-#")
			.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
			.define('#', ItemTagGenerator.TOWERWOOD)
			.define('o', Ingredient.of(TFItems.KNIGHTMETAL_RING.get()))
			.unlockedBy("has_ingot", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING.get()))
			.save(output, locEquip(TFItems.KNIGHTMETAL_SHIELD.getId().getPath()));

		ScepterRecipeBuilder.repairFor(TFItems.LIFEDRAIN_SCEPTER.get(), 9)
			.addRepairIngredient(Items.FERMENTED_SPIDER_EYE)
			.save(output, locEquip(TFItems.LIFEDRAIN_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(TFItems.FORTIFICATION_SCEPTER.get(), 9)
			.addRepairIngredient(Ingredient.of(Items.GOLDEN_APPLE))
			.save(output, locEquip(TFItems.FORTIFICATION_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(TFItems.TWILIGHT_SCEPTER.get(), 9)
			.addRepairIngredient(Tags.Items.ENDER_PEARLS)
			.save(output, locEquip(TFItems.TWILIGHT_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(TFItems.ZOMBIE_SCEPTER.get(), 9)
			.addRepairIngredient(CompoundIngredient.of(
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRENGTH), Items.POTION),
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_STRENGTH), Items.POTION),
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_STRENGTH), Items.POTION)
			))
			.addRepairIngredient(Items.ROTTEN_FLESH)
			.save(output, locEquip(TFItems.ZOMBIE_SCEPTER.getId().getPath()));
	}

	private void blockCompressionRecipes(RecipeOutput output) {
		reverseCompressBlock(output, "arctic_block_to_item", TFItems.ARCTIC_FUR, ItemTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR);
		reverseCompressBlock(output, "carminite_block_to_item", TFItems.CARMINITE, ItemTagGenerator.STORAGE_BLOCKS_CARMINITE);
		reverseCompressBlock(output, "ironwood_block_ingot", TFItems.IRONWOOD_INGOT, ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD);
		reverseCompressBlock(output, "knightmetal_block_ingot", TFItems.KNIGHTMETAL_INGOT, ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL);
		reverseCompressBlock(output, "steeleaf_block_ingot", TFItems.STEELEAF_INGOT, ItemTagGenerator.STORAGE_BLOCKS_STEELEAF);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.FIERY_INGOT, 9)
			.requires(ItemTagGenerator.STORAGE_BLOCKS_FIERY)
			.unlockedBy("has_item", has(ItemTagGenerator.STORAGE_BLOCKS_FIERY))
			.group("fiery_ingot")
			.save(output, TwilightForestMod.prefix("compressed_blocks/reversed/fiery_block_to_ingot"));

		compressedBlock(output, "arctic_block", TFBlocks.ARCTIC_FUR_BLOCK, ItemTagGenerator.ARCTIC_FUR);
		compressedBlock(output, "carminite_block", TFBlocks.CARMINITE_BLOCK, ItemTagGenerator.CARMINITE_GEMS);
		compressedBlock(output, "fiery_block", TFBlocks.FIERY_BLOCK, ItemTagGenerator.FIERY_INGOTS);
		compressedBlock(output, "ironwood_block", TFBlocks.IRONWOOD_BLOCK, ItemTagGenerator.IRONWOOD_INGOTS);
		compressedBlock(output, "knightmetal_block", TFBlocks.KNIGHTMETAL_BLOCK, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		compressedBlock(output, "steeleaf_block", TFBlocks.STEELEAF_BLOCK, ItemTagGenerator.STEELEAF_INGOTS);
	}

	private void emptyMapRecipes(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.MAGIC_MAP_FOCUS.get())
			.requires(TFItems.RAVEN_FEATHER.get())
			.requires(TFItems.TORCHBERRIES.get())
			.requires(Tags.Items.DUSTS_GLOWSTONE)
			.unlockedBy("has_berries", has(TFItems.TORCHBERRIES.get()))
			.unlockedBy("has_feather", has(TFItems.RAVEN_FEATHER.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.MAGIC_MAP.get())
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', ItemTagGenerator.PAPER)
			.define('•', Ingredient.of(TFItems.MAGIC_MAP_FOCUS.get()))
			.unlockedBy("has_item", has(TFItems.MAGIC_MAP_FOCUS.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.MAZE_MAP.get())
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', ItemTagGenerator.PAPER)
			.define('•', Ingredient.of(TFItems.MAZE_MAP_FOCUS.get()))
			.unlockedBy("has_item", has(TFItems.MAZE_MAP_FOCUS.get()))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.ORE_MAP.get())
			.requires(TFItems.MAZE_MAP.get())
			.requires(Tags.Items.STORAGE_BLOCKS_DIAMOND)
			.requires(Tags.Items.STORAGE_BLOCKS_GOLD)
			.requires(Tags.Items.STORAGE_BLOCKS_IRON)
			.unlockedBy("has_item", has(TFItems.MAZE_MAP.get()))
			.save(output);
	}

	private void woodRecipes(RecipeOutput output) {
		buttonBlock(output, "canopy", TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_PLANKS);
		buttonBlock(output, "dark", TFBlocks.DARK_BUTTON, TFBlocks.DARK_PLANKS);
		buttonBlock(output, "mangrove", TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_PLANKS);
		buttonBlock(output, "mining", TFBlocks.MINING_BUTTON, TFBlocks.MINING_PLANKS);
		buttonBlock(output, "sorting", TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_PLANKS);
		buttonBlock(output, "time", TFBlocks.TIME_BUTTON, TFBlocks.TIME_PLANKS);
		buttonBlock(output, "transformation", TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_PLANKS);
		buttonBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_PLANKS);

		doorBlock(output, "canopy", TFBlocks.CANOPY_DOOR, TFBlocks.CANOPY_PLANKS);
		doorBlock(output, "dark", TFBlocks.DARK_DOOR, TFBlocks.DARK_PLANKS);
		doorBlock(output, "mangrove", TFBlocks.MANGROVE_DOOR, TFBlocks.MANGROVE_PLANKS);
		doorBlock(output, "mining", TFBlocks.MINING_DOOR, TFBlocks.MINING_PLANKS);
		doorBlock(output, "sorting", TFBlocks.SORTING_DOOR, TFBlocks.SORTING_PLANKS);
		doorBlock(output, "time", TFBlocks.TIME_DOOR, TFBlocks.TIME_PLANKS);
		doorBlock(output, "transformation", TFBlocks.TRANSFORMATION_DOOR, TFBlocks.TRANSFORMATION_PLANKS);
		doorBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_DOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		fenceBlock(output, "canopy", TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_PLANKS);
		fenceBlock(output, "dark", TFBlocks.DARK_FENCE, TFBlocks.DARK_PLANKS);
		fenceBlock(output, "mangrove", TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_PLANKS);
		fenceBlock(output, "mining", TFBlocks.MINING_FENCE, TFBlocks.MINING_PLANKS);
		fenceBlock(output, "sorting", TFBlocks.SORTING_FENCE, TFBlocks.SORTING_PLANKS);
		fenceBlock(output, "time", TFBlocks.TIME_FENCE, TFBlocks.TIME_PLANKS);
		fenceBlock(output, "transformation", TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_PLANKS);
		fenceBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_PLANKS);

		gateBlock(output, "canopy", TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLANKS);
		gateBlock(output, "dark", TFBlocks.DARK_GATE, TFBlocks.DARK_PLANKS);
		gateBlock(output, "mangrove", TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLANKS);
		gateBlock(output, "mining", TFBlocks.MINING_GATE, TFBlocks.MINING_PLANKS);
		gateBlock(output, "sorting", TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLANKS);
		gateBlock(output, "time", TFBlocks.TIME_GATE, TFBlocks.TIME_PLANKS);
		gateBlock(output, "transformation", TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLANKS);
		gateBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		planksBlock(output, "canopy", TFBlocks.CANOPY_PLANKS, ItemTagGenerator.CANOPY_LOGS);
		planksBlock(output, "dark", TFBlocks.DARK_PLANKS, ItemTagGenerator.DARKWOOD_LOGS);
		planksBlock(output, "mangrove", TFBlocks.MANGROVE_PLANKS, ItemTagGenerator.MANGROVE_LOGS);
		planksBlock(output, "mining", TFBlocks.MINING_PLANKS, ItemTagGenerator.MINING_LOGS);
		planksBlock(output, "sorting", TFBlocks.SORTING_PLANKS, ItemTagGenerator.SORTING_LOGS);
		planksBlock(output, "time", TFBlocks.TIME_PLANKS, ItemTagGenerator.TIME_LOGS);
		planksBlock(output, "transformation", TFBlocks.TRANSFORMATION_PLANKS, ItemTagGenerator.TRANSFORMATION_LOGS);
		planksBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS, ItemTagGenerator.TWILIGHT_OAK_LOGS);

		woodBlock(output, "canopy", TFBlocks.CANOPY_WOOD, TFBlocks.CANOPY_LOG);
		woodBlock(output, "dark", TFBlocks.DARK_WOOD, TFBlocks.DARK_LOG);
		woodBlock(output, "mangrove", TFBlocks.MANGROVE_WOOD, TFBlocks.MANGROVE_LOG);
		woodBlock(output, "mining", TFBlocks.MINING_WOOD, TFBlocks.MINING_LOG);
		woodBlock(output, "sorting", TFBlocks.SORTING_WOOD, TFBlocks.SORTING_LOG);
		woodBlock(output, "time", TFBlocks.TIME_WOOD, TFBlocks.TIME_LOG);
		woodBlock(output, "transformation", TFBlocks.TRANSFORMATION_WOOD, TFBlocks.TRANSFORMATION_LOG);
		woodBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.TWILIGHT_OAK_LOG);

		strippedWoodBlock(output, "canopy", TFBlocks.STRIPPED_CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_LOG);
		strippedWoodBlock(output, "dark", TFBlocks.STRIPPED_DARK_WOOD, TFBlocks.STRIPPED_DARK_LOG);
		strippedWoodBlock(output, "mangrove", TFBlocks.STRIPPED_MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_LOG);
		strippedWoodBlock(output, "mining", TFBlocks.STRIPPED_MINING_WOOD, TFBlocks.STRIPPED_MINING_LOG);
		strippedWoodBlock(output, "sorting", TFBlocks.STRIPPED_SORTING_WOOD, TFBlocks.STRIPPED_SORTING_LOG);
		strippedWoodBlock(output, "time", TFBlocks.STRIPPED_TIME_WOOD, TFBlocks.STRIPPED_TIME_LOG);
		strippedWoodBlock(output, "transformation", TFBlocks.STRIPPED_TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		strippedWoodBlock(output, "twilight_oak", TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		plateBlock(output, "canopy", TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_PLANKS);
		plateBlock(output, "dark", TFBlocks.DARK_PLATE, TFBlocks.DARK_PLANKS);
		plateBlock(output, "mangrove", TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_PLANKS);
		plateBlock(output, "mining", TFBlocks.MINING_PLATE, TFBlocks.MINING_PLANKS);
		plateBlock(output, "sorting", TFBlocks.SORTING_PLATE, TFBlocks.SORTING_PLANKS);
		plateBlock(output, "time", TFBlocks.TIME_PLATE, TFBlocks.TIME_PLANKS);
		plateBlock(output, "transformation", TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_PLANKS);
		plateBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenSlabBlock(output, "canopy", TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_PLANKS);
		woodenSlabBlock(output, "dark", TFBlocks.DARK_SLAB, TFBlocks.DARK_PLANKS);
		woodenSlabBlock(output, "mangrove", TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_PLANKS);
		woodenSlabBlock(output, "mining", TFBlocks.MINING_SLAB, TFBlocks.MINING_PLANKS);
		woodenSlabBlock(output, "sorting", TFBlocks.SORTING_SLAB, TFBlocks.SORTING_PLANKS);
		woodenSlabBlock(output, "time", TFBlocks.TIME_SLAB, TFBlocks.TIME_PLANKS);
		woodenSlabBlock(output, "transformation", TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_PLANKS);
		woodenSlabBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenStairsBlock(output, locWood("canopy_stairs"), TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_PLANKS.get());
		woodenStairsBlock(output, locWood("dark_stairs"), TFBlocks.DARK_STAIRS, TFBlocks.DARK_PLANKS, TFBlocks.DARK_PLANKS.get());
		woodenStairsBlock(output, locWood("mangrove_stairs"), TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_PLANKS.get());
		woodenStairsBlock(output, locWood("mining_stairs"), TFBlocks.MINING_STAIRS, TFBlocks.MINING_PLANKS, TFBlocks.MINING_PLANKS.get());
		woodenStairsBlock(output, locWood("sorting_stairs"), TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_PLANKS.get());
		woodenStairsBlock(output, locWood("time_stairs"), TFBlocks.TIME_STAIRS, TFBlocks.TIME_PLANKS, TFBlocks.TIME_PLANKS.get());
		woodenStairsBlock(output, locWood("transformation_stairs"), TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_PLANKS.get());
		woodenStairsBlock(output, locWood("twilight_oak_stairs"), TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_PLANKS.get());

		trapdoorBlock(output, "canopy", TFBlocks.CANOPY_TRAPDOOR, TFBlocks.CANOPY_PLANKS);
		trapdoorBlock(output, "dark", TFBlocks.DARK_TRAPDOOR, TFBlocks.DARK_PLANKS);
		trapdoorBlock(output, "mangrove", TFBlocks.MANGROVE_TRAPDOOR, TFBlocks.MANGROVE_PLANKS);
		trapdoorBlock(output, "mining", TFBlocks.MINING_TRAPDOOR, TFBlocks.MINING_PLANKS);
		trapdoorBlock(output, "sorting", TFBlocks.SORTING_TRAPDOOR, TFBlocks.SORTING_PLANKS);
		trapdoorBlock(output, "time", TFBlocks.TIME_TRAPDOOR, TFBlocks.TIME_PLANKS);
		trapdoorBlock(output, "transformation", TFBlocks.TRANSFORMATION_TRAPDOOR, TFBlocks.TRANSFORMATION_PLANKS);
		trapdoorBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_TRAPDOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		signBlock(output, "canopy", TFItems.CANOPY_SIGN, TFBlocks.CANOPY_PLANKS);
		signBlock(output, "dark", TFItems.DARK_SIGN, TFBlocks.DARK_PLANKS);
		signBlock(output, "mangrove", TFItems.MANGROVE_SIGN, TFBlocks.MANGROVE_PLANKS);
		signBlock(output, "mining", TFItems.MINING_SIGN, TFBlocks.MINING_PLANKS);
		signBlock(output, "sorting", TFItems.SORTING_SIGN, TFBlocks.SORTING_PLANKS);
		signBlock(output, "time", TFItems.TIME_SIGN, TFBlocks.TIME_PLANKS);
		signBlock(output, "transformation", TFItems.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_PLANKS);
		signBlock(output, "twilight_oak", TFItems.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_OAK_PLANKS);

		hangingSignBlock(output, "canopy", TFItems.CANOPY_HANGING_SIGN, TFBlocks.STRIPPED_CANOPY_LOG);
		hangingSignBlock(output, "dark", TFItems.DARK_HANGING_SIGN, TFBlocks.STRIPPED_DARK_LOG);
		hangingSignBlock(output, "mangrove", TFItems.MANGROVE_HANGING_SIGN, TFBlocks.STRIPPED_MANGROVE_LOG);
		hangingSignBlock(output, "mining", TFItems.MINING_HANGING_SIGN, TFBlocks.STRIPPED_MINING_LOG);
		hangingSignBlock(output, "sorting", TFItems.SORTING_HANGING_SIGN, TFBlocks.STRIPPED_SORTING_LOG);
		hangingSignBlock(output, "time", TFItems.TIME_HANGING_SIGN, TFBlocks.STRIPPED_TIME_LOG);
		hangingSignBlock(output, "transformation", TFItems.TRANSFORMATION_HANGING_SIGN, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		hangingSignBlock(output, "twilight_oak", TFItems.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		banisterBlock(output, "canopy", TFBlocks.CANOPY_BANISTER, TFBlocks.CANOPY_SLAB);
		banisterBlock(output, "dark", TFBlocks.DARK_BANISTER, TFBlocks.DARK_SLAB);
		banisterBlock(output, "mangrove", TFBlocks.MANGROVE_BANISTER, TFBlocks.MANGROVE_SLAB);
		banisterBlock(output, "mining", TFBlocks.MINING_BANISTER, TFBlocks.MINING_SLAB);
		banisterBlock(output, "sorting", TFBlocks.SORTING_BANISTER, TFBlocks.SORTING_SLAB);
		banisterBlock(output, "time", TFBlocks.TIME_BANISTER, TFBlocks.TIME_SLAB);
		banisterBlock(output, "transformation", TFBlocks.TRANSFORMATION_BANISTER, TFBlocks.TRANSFORMATION_SLAB);
		banisterBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_BANISTER, TFBlocks.TWILIGHT_OAK_SLAB);

		banisterBlock(output, "oak", TFBlocks.OAK_BANISTER, Blocks.OAK_SLAB);
		banisterBlock(output, "spruce", TFBlocks.SPRUCE_BANISTER, Blocks.SPRUCE_SLAB);
		banisterBlock(output, "birch", TFBlocks.BIRCH_BANISTER, Blocks.BIRCH_SLAB);
		banisterBlock(output, "jungle", TFBlocks.JUNGLE_BANISTER, Blocks.JUNGLE_SLAB);
		banisterBlock(output, "acacia", TFBlocks.ACACIA_BANISTER, Blocks.ACACIA_SLAB);
		banisterBlock(output, "dark_oak", TFBlocks.DARK_OAK_BANISTER, Blocks.DARK_OAK_SLAB);
		banisterBlock(output, "crimson", TFBlocks.CRIMSON_BANISTER, Blocks.CRIMSON_SLAB);
		banisterBlock(output, "warped", TFBlocks.WARPED_BANISTER, Blocks.WARPED_SLAB);
		banisterBlock(output, "vangrove", TFBlocks.VANGROVE_BANISTER, Blocks.MANGROVE_SLAB);
		banisterBlock(output, "bamboo", TFBlocks.BAMBOO_BANISTER, Blocks.BAMBOO_SLAB);
		banisterBlock(output, "cherry", TFBlocks.CHERRY_BANISTER, Blocks.CHERRY_SLAB);

		chestBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS);
		chestBlock(output, "canopy", TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_PLANKS);
		chestBlock(output, "mangrove", TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_PLANKS);
		chestBlock(output, "dark", TFBlocks.DARK_CHEST, TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_PLANKS);
		chestBlock(output, "time", TFBlocks.TIME_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_PLANKS);
		chestBlock(output, "transformation", TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_PLANKS);
		chestBlock(output, "mining", TFBlocks.MINING_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_PLANKS);
		chestBlock(output, "sorting", TFBlocks.SORTING_CHEST, TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_PLANKS);

		buildBoats(output, TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT, TFBlocks.TWILIGHT_OAK_PLANKS);
		buildBoats(output, TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT, TFBlocks.CANOPY_PLANKS);
		buildBoats(output, TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT, TFBlocks.MANGROVE_PLANKS);
		buildBoats(output, TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT, TFBlocks.DARK_PLANKS);
		buildBoats(output, TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT, TFBlocks.TIME_PLANKS);
		buildBoats(output, TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT, TFBlocks.TRANSFORMATION_PLANKS);
		buildBoats(output, TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT, TFBlocks.MINING_PLANKS);
		buildBoats(output, TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT, TFBlocks.SORTING_PLANKS);
	}

	private void nagastoneRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.SPIRAL_BRICKS.get(), 8)
			.pattern("BSS")
			.pattern("BSS")
			.pattern("BBB")
			.define('B', Ingredient.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
			.define('S', Ingredient.of(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
			.unlockedBy("has_item", has(TFBlocks.SPIRAL_BRICKS.get()))
			.save(output, locNaga("nagastone_spiral"));

		stairsBlock(output, locNaga("nagastone_stairs_left"), TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE.get());
		stairsRightBlock(output, locNaga("nagastone_stairs_right"), TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE.get());

		stairsBlock(output, locNaga("mossy_nagastone_stairs_left"), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE.get());
		stairsRightBlock(output, locNaga("mossy_nagastone_stairs_right"), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE.get());

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_ETCHED_NAGASTONE.get(), 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.ETCHED_NAGASTONE.get()))
			.unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE.get()))
			.save(output, locNaga("mossy_etched_nagastone"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_NAGASTONE_PILLAR.get(), 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.NAGASTONE_PILLAR.get()))
			.unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR.get()))
			.save(output, locNaga("mossy_nagastone_pillar"));

		stairsBlock(output, locNaga("cracked_nagastone_stairs_left"), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE.get());
		stairsRightBlock(output, locNaga("cracked_nagastone_stairs_right"), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE.get());
	}

	private void castleRecipes(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_CASTLE_BRICK.get(), 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.CASTLE_BRICK.get()))
			.unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK.get()))
			.save(output, locCastle("mossy_castle_brick"));

		castleBlock(output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get());
		castleBlock(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.THICK_CASTLE_BRICK.get());
		castleBlock(output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get());

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), 4)
			.pattern("##")
			.pattern("##")
			.define('#', Ingredient.of(TFBlocks.BOLD_CASTLE_BRICK_TILE.get()))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.get()))
			.save(output, locCastle("bold_castle_pillar_from_tile"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), 6)
			.pattern("#H#")
			.pattern("#H#")
			.define('#', Ingredient.of(TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get(), TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.THICK_CASTLE_BRICK.get()))
			.define('H', Ingredient.of(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get()))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.get()))
			.save(output, locCastle("encased_castle_pillar"));

		stairsBlock(output, locCastle("bold_castle_brick_stairs"), TFBlocks.BOLD_CASTLE_BRICK_STAIRS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), TFBlocks.BOLD_CASTLE_BRICK_TILE.get());
		stairsBlock(output, locCastle("castle_brick_stairs"), TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK.get());
		stairsBlock(output, locCastle("cracked_castle_brick_stairs"), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK.get());
		stairsBlock(output, locCastle("encased_castle_brick_stairs"), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.get());
		stairsBlock(output, locCastle("mossy_castle_brick_stairs"), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK.get());
		stairsBlock(output, locCastle("worn_castle_brick_stairs"), TFBlocks.WORN_CASTLE_BRICK_STAIRS, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK.get());
	}

	private void fieryConversions(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.FIERY_INGOT.get())
			.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL))
			.requires(Ingredient.of(Tags.Items.INGOTS_IRON))
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
			.group("fiery_ingot")
			.save(output, locEquip("fiery_ingot_crafting"));

		fieryConversion(output, TFItems.FIERY_HELMET, Items.IRON_HELMET, 5);
		fieryConversion(output, TFItems.FIERY_CHESTPLATE, Items.IRON_CHESTPLATE, 8);
		fieryConversion(output, TFItems.FIERY_LEGGINGS, Items.IRON_LEGGINGS, 7);
		fieryConversion(output, TFItems.FIERY_BOOTS, Items.IRON_BOOTS, 4);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.FIERY_SWORD.get())
			.requires(Items.IRON_SWORD)
			.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 2)
			.requires(Ingredient.of(Tags.Items.RODS_BLAZE))
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
			.group("fiery_sword")
			.save(output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_SWORD).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE.get())
			.requires(Items.IRON_PICKAXE)
			.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 3)
			.requires(Ingredient.of(Tags.Items.RODS_BLAZE), 2)
			.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
			.group("fiery_pickaxe")
			.save(output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_PICKAXE).getPath()));
	}

	private <T extends AbstractCookingRecipe> void cookingRecipes(RecipeOutput output, String processName, RecipeSerializer<T> process, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_MEEF.get()), RecipeCategory.FOOD, TFItems.COOKED_MEEF.get(), 0.35F, smeltingTime, process, factory).unlockedBy("has_food", has(TFItems.RAW_MEEF.get())).save(output, TwilightForestMod.prefix("food/" + processName + "_meef").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_VENISON.get()), RecipeCategory.FOOD, TFItems.COOKED_VENISON.get(), 0.35F, smeltingTime, process, factory).unlockedBy("has_food", has(TFItems.RAW_VENISON.get())).save(output, TwilightForestMod.prefix("food/" + processName + "_venison").toString());
	}

	private <T extends AbstractCookingRecipe> void ingotRecipes(RecipeOutput output, String processName, RecipeSerializer<T> process, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.ARMOR_SHARD_CLUSTER.get()), RecipeCategory.MISC, TFItems.KNIGHTMETAL_INGOT.get(), 1.0F, smeltingTime, process, factory).unlockedBy("has_item", has(TFItems.ARMOR_SHARD_CLUSTER.get())).group("knightmetal_ingot").save(output, TwilightForestMod.prefix("material/" + processName + "_knightmetal_ingot").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_IRONWOOD.get()), RecipeCategory.MISC, TFItems.IRONWOOD_INGOT.get(), 1.0F, smeltingTime, process, factory).unlockedBy("has_item", has(TFItems.RAW_IRONWOOD.get())).group("ironwood_ingot").save(output, TwilightForestMod.prefix("material/" + processName + "_ironwood_ingot").toString());
	}

	private void crackedWoodRecipes(RecipeOutput output) {
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(TFBlocks.TOWERWOOD.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_TOWERWOOD.get(), 0.1F, 100).unlockedBy("has_item", has(TFBlocks.TOWERWOOD.get())).save(output, TwilightForestMod.prefix("wood/" + "smoked" + "_cracked_towerwood").toString());
	}

	private void crackedStoneRecipes(RecipeOutput output) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.NAGASTONE_PILLAR.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), 0.1F, 200).unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR.get())).save(output, TwilightForestMod.prefix("nagastone/" + "smelted" + "_cracked_nagastone_pillar").toString());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.ETCHED_NAGASTONE.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), 0.1F, 200).unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE.get())).save(output, TwilightForestMod.prefix("nagastone/" + "smelted" + "_cracked_etched_nagastone").toString());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.MAZESTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_MAZESTONE.get(), 0.1F, 200).unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK.get())).save(output, TwilightForestMod.prefix("maze_stone/" + "smelted" + "_maze_stone_cracked").toString());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.CASTLE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_CASTLE_BRICK.get(), 0.1F, 200).unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK.get())).save(output, TwilightForestMod.prefix("castleblock/" + "smelted" + "_cracked_castle_brick").toString());
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.UNDERBRICK.get()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_UNDERBRICK.get(), 0.1F, 200).unlockedBy("has_item", has(TFBlocks.UNDERBRICK.get())).save(output, TwilightForestMod.prefix("smelted" + "_cracked_underbrick").toString());
	}
}
