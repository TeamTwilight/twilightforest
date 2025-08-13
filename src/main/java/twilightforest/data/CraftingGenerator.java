package twilightforest.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.*;
import twilightforest.data.custom.CartesianShapelessRecipeBuilder;
import twilightforest.data.helpers.CraftingDataHelper;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersBeltWingsMergeRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

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

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.WROUGHT_IRON_FENCE.get(), 3)
			.pattern("###")
			.pattern("###")
			.define('#', ItemTagGenerator.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(ItemTagGenerator.WROUGHT_IRON_INGOTS))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA.get(), 2)
			.pattern("###")
			.pattern(" # ")
			.define('#', ItemTagGenerator.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(ItemTagGenerator.WROUGHT_IRON_INGOTS))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.ROPE.get(), 8)
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.ROOT_STRAND.get()))
			.unlockedBy("has_root_strand", has(TFBlocks.ROOT_STRAND.get()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW.get(), 4)
			.pattern("GPG")
			.pattern("PPP")
			.pattern("GPG")
			.define('G', Ingredient.of(Tags.Items.GLASS_BLOCKS))
			.define('P', Ingredient.of(TFBlocks.CANOPY_PLANKS.value()))
			.unlockedBy("has_planks", has(TFBlocks.CANOPY_PLANKS.value()))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW_PANE.get(), 16)
			.pattern("GGG")
			.pattern("GGG")
			.define('G', Ingredient.of(TFBlocks.CANOPY_WINDOW.value()))
			.unlockedBy("has_windows", has(TFBlocks.CANOPY_WINDOW.value()))
			.save(output);

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

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TFItems.MOSS_SOUP)
			.requires(TFBlocks.MOSS_PATCH)
			.requires(Items.BOWL)
			.requires(Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.WATER)))
			.unlockedBy("has_moss", has(TFBlocks.MOSS_PATCH))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TFItems.BERRY_MEDLEY)
			.requires(Items.BOWL)
			.requires(TFItems.RASPBERRY)
			.requires(TFItems.BLUEBERRY)
			.requires(TFItems.BLACKBERRY)
			.requires(TFItems.MALOBERRY)
			.unlockedBy("has_raspberry", has(TFItems.RASPBERRY))
			.unlockedBy("has_blueberry", has(TFItems.BLUEBERRY))
			.unlockedBy("has_blackberry", has(TFItems.BLACKBERRY))
			.unlockedBy("has_maloberry", has(TFItems.MALOBERRY))
			.save(output);

		cookingRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200);
		cookingRecipes(output, "smoked", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100);
		cookingRecipes(output, "campfired", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600);

		ingotRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200);
		ingotRecipes(output, "blasted", RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, 100);

		oreberryRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, 200);
		oreberryRecipes(output, "blasted", RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, 100);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_INGOT)
			.requires(Ingredient.of(ItemTagGenerator.COPPER_NUGGETS), 9)
			.unlockedBy("has_item", has(ItemTagGenerator.COPPER_NUGGETS))
			.save(output, TwilightForestMod.prefix("copper_nuggets_to_ingot"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.COPPER_NUGGET, 9)
			.requires(Ingredient.of(Tags.Items.INGOTS_COPPER))
			.unlockedBy("has_item", has(Tags.Items.INGOTS_COPPER))
			.save(output, TwilightForestMod.prefix("copper_ingot_to_nuggets"));

		crackedWoodRecipes(output);
		crackedStoneRecipes(output);

		//TODO 1.21.4 use vanilla recipe with higher priority
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

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.MYSTIC_CROWN, 1)
			.pattern("ttt")
			.pattern("t t")
			.pattern("ttt")
			.define('t', Ingredient.of(TFItems.CROWN_SPLINTER))
			.unlockedBy("has_item", has(TFItems.CROWN_SPLINTER))
			.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
			.define('~', Items.STRING)
			.define('O', TFItems.MAZE_SLIME_BALL)
			.pattern("~~ ")
			.pattern("~O ")
			.pattern("  ~")
			.unlockedBy("has_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(output, TwilightForestMod.prefix("lead_maze_ver"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, Items.MAGMA_CREAM)
			.requires(Items.BLAZE_POWDER)
			.requires(TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
			.save(output, TwilightForestMod.prefix("magma_cream_maze_ver"));

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.STICKY_PISTON)
			.define('P', Blocks.PISTON)
			.define('S', TFItems.MAZE_SLIME_BALL)
			.pattern("S")
			.pattern("P")
			.unlockedBy("has_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(output, TwilightForestMod.prefix("sticky_piston_maze_ver"));
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

		this.travellersGearRecipes(output);

		charmRecipe(output, "charm_of_keeping_2", TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_1);
		charmRecipe(output, "charm_of_keeping_3", TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_KEEPING_2);
		charmRecipe(output, "charm_of_life_2", TFItems.CHARM_OF_LIFE_2, TFItems.CHARM_OF_LIFE_1);

		SpecialRecipeBuilder.special(MoonwormQueenRepairRecipe::new).save(output, TwilightForestMod.prefix("moonworm_queen_repair_recipe").toString());
		SpecialRecipeBuilder.special(MagicMapCloningRecipe::new).save(output, TwilightForestMod.prefix("magic_map_cloning_recipe").toString());
		SpecialRecipeBuilder.special(MazeMapCloningRecipe::new).save(output, TwilightForestMod.prefix("maze_map_cloning_recipe").toString());
		SpecialRecipeBuilder.special(EmperorsClothRecipe::new).save(output, TwilightForestMod.prefix("emperors_cloth_recipe").toString());
		SpecialRecipeBuilder.special(CasketRepairRecipe::new).save(output, TwilightForestMod.prefix("casket_repair_recipe").toString());
		SpecialRecipeBuilder.special(EssenceRepairRecipe::new).save(output, TwilightForestMod.prefix("essence_repair_recipe").toString());
		SpecialRecipeBuilder.special(TravellersBeltWingsMergeRecipe::new).save(output, TwilightForestMod.prefix("travellers_belt_wings_merge_recipe").toString());
		SpecialRecipeBuilder.special(TravellersVestGlovesMergeRecipe::new).save(output, TwilightForestMod.prefix("travellers_vest_gloves_merge_recipe").toString());

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

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.TANNIN)
			.requires(potionIngredient(Potions.WATER))
			.requires(TFBlocks.TWILIGHT_OAK_SAPLING)
			.requires(TFBlocks.ROOT_STRAND)
			.requires(TFBlocks.TWILIGHT_OAK_LEAVES)
			.unlockedBy("has_block", has(TFBlocks.TWILIGHT_OAK_SAPLING))
			.unlockedBy("has_block", has(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_block", has(TFBlocks.TWILIGHT_OAK_LEAVES))
			.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.TREATED_LEATHER)
			.requires(TFItems.TANNIN)
			.requires(Tags.Items.LEATHERS)
			.unlockedBy("has_tannin", has(TFItems.TANNIN))
			.save(output);

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
			.addRepairIngredient(potionsIngredient(
				Potions.STRENGTH,
				Potions.LONG_STRENGTH,
				Potions.STRONG_STRENGTH
			))
			.addRepairIngredient(Items.ROTTEN_FLESH)
			.save(output, locEquip(TFItems.ZOMBIE_SCEPTER.getId().getPath()));

		Predicate<Ingredient> splitTravellersModifiersRecipes = ingredient -> Arrays.stream(ingredient.getItems()).allMatch(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR));

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" R ")
				.pattern("RGR")
				.define('R', Ingredient.of(TFBlocks.RED_THREAD))
				.define('G', Ingredient.of(TFItems.TRAVELLERS_GOGGLES))
				.build(),
			TravellersModifiersManager.RED_THREAD_VISION_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" E ")
				.pattern("PVP")
				.pattern(" S ")
				.define('E', Ingredient.of(Items.ENDER_EYE))
				.define('P', Ingredient.of(Items.ENDER_PEARL))
				.define('V', Ingredient.of(TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES))
				.define('S', Ingredient.of(Items.SUGAR))
				.build(),
			TravellersModifiersManager.PERFECT_DODGE_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShapeless(CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(potionsIngredient(Potions.INVISIBILITY, Potions.LONG_INVISIBILITY))
				.ingredient(Items.ENDER_EYE)
				.ingredient(Items.SPIDER_EYE)
				.ingredient(Items.GOLDEN_CARROT)
				.ingredient(Ingredient.of(TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES))
				.build(),
			TravellersModifiersManager.STEALTH_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("RRR")
				.pattern("RVR")
				.pattern("OOO")
				.define('R', Ingredient.of(Items.REDSTONE_BLOCK))
				.define('V', Ingredient.of(TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES))
				.define('O', Ingredient.of(Items.OBSIDIAN))
				.build(),
			TravellersModifiersManager.HASTE_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShapeless(CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(TFItems.EXANIMATE_ESSENCE)
				.ingredient(TFItems.ORE_MAGNET)
				.ingredient(TFItems.LIVEROOT)
				.ingredient(Items.CHICKEN)
				.ingredient(Ingredient.of(TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES))
				.build(),
			TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShapeless(CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(Items.BUNDLE)
				.ingredient(Ingredient.of(TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES))
				.build(),
			TravellersModifiersManager.FOOD_EFFICIENCY_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("FCF")
				.pattern("FWF")
				.pattern("FEF")
				.define('F', Ingredient.of(Items.FEATHER))
				.define('C', Ingredient.of(Items.COBWEB))
				.define('W', Ingredient.of(TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT))
				.define('E', Ingredient.of(TFItems.BORER_ESSENCE))
				.build(),
			TravellersModifiersManager.CONTROLLED_FALL_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" L ")
				.pattern("SWS")
				.pattern("P P")
				.define('L', potionsIngredient(Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING))
				.define('S', Ingredient.of(Tags.Items.STRINGS))
				.define('W', Ingredient.of(TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT))
				.define('P', Ingredient.of(Items.PISTON))
				.build(),
			TravellersModifiersManager.DOUBLE_JUMP_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" P ")
				.pattern("HWH")
				.pattern("F F")
				.define('P', potionsIngredient(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS))
				.define('H', Ingredient.of(Items.RABBIT_HIDE))
				.define('W', Ingredient.of(TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT))
				.define('F', Ingredient.of(TFItems.RAVEN_FEATHER))
				.build(),
			TravellersModifiersManager.AGILE_RANGER_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("SBS")
				.pattern("PWP")
				.pattern("SBS")
				.define('S', Ingredient.of(Tags.Items.STRINGS))
				.define('B', Ingredient.of(Tags.Items.BONES))
				.define('P', Ingredient.of(Items.PISTON))
				.define('W', Ingredient.of(TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT))
				.build(),
			TravellersModifiersManager.SIDESTEP_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("SBS")
				.pattern("L L")
				.define('B', Ingredient.of(TFItems.TRAVELLERS_BOOTS))
				.define('S', Ingredient.of(TFItems.MAZE_SLIME_BALL))
				.define('L', Ingredient.of(Items.LILY_PAD))
				.build(),
			TravellersModifiersManager.WATER_WALK_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("WBW")
				.pattern("S S")
				.define('W', Ingredient.of(Tags.Items.STRINGS))
				.define('B', Ingredient.of(TFItems.TRAVELLERS_BOOTS))
				.define('S', Ingredient.of(Items.SLIME_BLOCK))
				.build(),
			TravellersModifiersManager.SLIMY_SOLES_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("MPM")
				.pattern("HBH")
				.define('M', Ingredient.of(TFItems.RAW_MEEF))
				.define('P', potionsIngredient(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS))
				.define('B', Ingredient.of(TFItems.TRAVELLERS_BOOTS))
				.define('H', Ingredient.of(Items.RABBIT_HIDE))
				.build(),
			TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" E ")
				.pattern("MTM")
				.pattern(" E ")
				.define('E', Ingredient.of(Items.EXPERIENCE_BOTTLE))
				.define('M', Ingredient.of(TFBlocks.MOSS_PATCH))
				.define('T', Ingredient.of(TFItems.TRAVELLERS_GOGGLES, TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES, TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT, TFItems.TRAVELLERS_BOOTS))
				.build(),
			TravellersModifiersManager.AUTO_REPAIR_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" M ")
				.pattern("ETE")
				.pattern(" M ")
				.define('M', Ingredient.of(TFBlocks.MOSS_PATCH))
				.define('E', Ingredient.of(Items.EXPERIENCE_BOTTLE))
				.define('T', Ingredient.of(TFItems.TRAVELLERS_GOGGLES, TFItems.TRAVELLERS_CHESTPLATE, TFItems.TRAVELLERS_CHESTPLATE_GLOVES, TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_WINGS_BELT, TFItems.TRAVELLERS_BOOTS))
				.build(),
			TravellersModifiersManager.AUTO_REPAIR_MODIFIER, true).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("M M")
				.pattern("YGY")
				.pattern("M M")
				.define('M', Ingredient.of(Items.PHANTOM_MEMBRANE))
				.define('Y', Ingredient.of(Items.YELLOW_STAINED_GLASS_PANE))
				.define('G', Ingredient.of(TFItems.TRAVELLERS_GOGGLES))
				.build(),
			TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern("sss")
				.pattern("igi")
				.pattern("sss")
				.define('i', Ingredient.of(Items.ITEM_FRAME))
				.define('s', Ingredient.of(Tags.Items.RODS_WOODEN))
				.define('g', Ingredient.of(TFItems.TRAVELLERS_GOGGLES))
				.build(),
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER).save(output);

		TravellersGearComponentModifierBuilder.buildShaped(CartesianShapedRecipeBuilder.create(splitTravellersModifiersRecipes)
				.pattern(" b")
				.pattern("gb")
				.pattern("sw")
				.define('b', Ingredient.of(Items.BAMBOO))
				.define('w', potionsIngredient(Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING))
				.define('s', Ingredient.of(Tags.Items.SLIME_BALLS))
				.define('g', Ingredient.of(TFItems.TRAVELLERS_GOGGLES))
				.build(),
			TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER).save(output);

		DryingRecipeBuilder.drying(Ingredient.of(Tags.Items.FOODS_COOKED_MEAT), new ItemStack(Items.LEATHER), 8.5F)
			.unlockedBy("has_meat", has(Tags.Items.FOODS_COOKED_MEAT))
			.save(output, TwilightForestMod.prefix("drying/cooked_meat_to_leather"));

		DryingRecipeBuilder.drying(Ingredient.of(ItemTags.SAPLINGS), new ItemStack(Items.DEAD_BUSH), 6)
			.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
			.save(output, TwilightForestMod.prefix("drying/sapling_to_dead_bush"));

		DryingRecipeBuilder.drying(Items.MUD, Items.CLAY, 6)
			.unlockedBy("has_mud", has(Items.MUD))
			.save(output, TwilightForestMod.prefix("drying/mud_to_clay"));

		DryingRecipeBuilder.drying(Items.WET_SPONGE, Items.SPONGE, 2)
			.unlockedBy("has_wet_sponge", has(Items.WET_SPONGE))
			.save(output, TwilightForestMod.prefix("drying/sponge"));

		DryingRecipeBuilder.drying(Items.KELP, Items.DRIED_KELP, 3)
			.unlockedBy("has_kelp", has(Items.KELP))
			.save(output, TwilightForestMod.prefix("drying/kelp"));

		DryingRecipeBuilder.drying(Items.ROTTEN_FLESH, TFItems.MONSTER_JERKY)
			.unlockedBy("has_meat", has(Items.ROTTEN_FLESH))
			.save(output);

		DryingRecipeBuilder.drying(Items.BEEF, TFItems.BEEF_JERKY)
			.unlockedBy("has_meat", has(Items.BEEF))
			.save(output);

		DryingRecipeBuilder.drying(Items.CHICKEN, TFItems.CHICKEN_JERKY)
			.unlockedBy("has_meat", has(Items.CHICKEN))
			.save(output);

		DryingRecipeBuilder.drying(Items.PORKCHOP, TFItems.PORK_JERKY)
			.unlockedBy("has_meat", has(Items.PORKCHOP))
			.save(output);

		DryingRecipeBuilder.drying(Items.MUTTON, TFItems.MUTTON_JERKY)
			.unlockedBy("has_meat", has(Items.MUTTON))
			.save(output);

		DryingRecipeBuilder.drying(Items.RABBIT, TFItems.RABBIT_JERKY)
			.unlockedBy("has_meat", has(Items.RABBIT))
			.save(output);

		DryingRecipeBuilder.drying(Items.COD, TFItems.COD_JERKY)
			.unlockedBy("has_meat", has(Items.COD))
			.save(output);

		DryingRecipeBuilder.drying(Items.SALMON, TFItems.SALMON_JERKY)
			.unlockedBy("has_meat", has(Items.SALMON))
			.save(output);

		DryingRecipeBuilder.drying(Items.TROPICAL_FISH, TFItems.CLOWNFISH_JERKY)
			.unlockedBy("has_meat", has(Items.TROPICAL_FISH))
			.save(output);

		DryingRecipeBuilder.drying(Items.PUFFERFISH, TFItems.FUGU_JERKY)
			.unlockedBy("has_meat", has(Items.PUFFERFISH))
			.save(output);

		DryingRecipeBuilder.drying(TFItems.RAW_VENISON, TFItems.VENISON_JERKY)
			.unlockedBy("has_meat", has(TFItems.RAW_VENISON))
			.save(output);

		DryingRecipeBuilder.drying(TFItems.RAW_MEEF, TFItems.MEEF_JERKY)
			.unlockedBy("has_meat", has(TFItems.RAW_MEEF))
			.save(output);

		DryingRecipeBuilder.drying(Items.SLIME_BALL, TFItems.GELATINOUS_SLIME_DROP)
			.unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
			.save(output);

		DryingRecipeBuilder.drying(TFItems.MAZE_SLIME_BALL, TFItems.GELATINOUS_MAZE_SLIME_DROP)
			.unlockedBy("has_maze_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(output);

		DryingRecipeBuilder.drying(TFItems.TREATED_LEATHER, TFItems.TANNED_LEATHER)
			.unlockedBy("has_treated", has(TFItems.TREATED_LEATHER))
			.save(output);
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

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MAZE_SLIME_BLOCK)
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_item", has(TFItems.MAZE_SLIME_BALL))
			.save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.MAZE_SLIME_BALL, 9)
			.requires(TFBlocks.MAZE_SLIME_BLOCK)
			.unlockedBy("has_item", has(TFBlocks.MAZE_SLIME_BLOCK))
			.save(output);
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

		dryingRackBlock(output, "canopy", TFBlocks.CANOPY_DRYING_RACK, TFBlocks.CANOPY_SLAB);
		dryingRackBlock(output, "dark", TFBlocks.DARK_DRYING_RACK, TFBlocks.DARK_SLAB);
		dryingRackBlock(output, "mangrove", TFBlocks.MANGROVE_DRYING_RACK, TFBlocks.MANGROVE_SLAB);
		dryingRackBlock(output, "mining", TFBlocks.MINING_DRYING_RACK, TFBlocks.MINING_SLAB);
		dryingRackBlock(output, "sorting", TFBlocks.SORTING_DRYING_RACK, TFBlocks.SORTING_SLAB);
		dryingRackBlock(output, "time", TFBlocks.TIME_DRYING_RACK, TFBlocks.TIME_SLAB);
		dryingRackBlock(output, "transformation", TFBlocks.TRANSFORMATION_DRYING_RACK, TFBlocks.TRANSFORMATION_SLAB);
		dryingRackBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_DRYING_RACK, TFBlocks.TWILIGHT_OAK_SLAB);

		dryingRackBlock(output, "oak", TFBlocks.OAK_DRYING_RACK, Blocks.OAK_SLAB);
		dryingRackBlock(output, "spruce", TFBlocks.SPRUCE_DRYING_RACK, Blocks.SPRUCE_SLAB);
		dryingRackBlock(output, "birch", TFBlocks.BIRCH_DRYING_RACK, Blocks.BIRCH_SLAB);
		dryingRackBlock(output, "jungle", TFBlocks.JUNGLE_DRYING_RACK, Blocks.JUNGLE_SLAB);
		dryingRackBlock(output, "acacia", TFBlocks.ACACIA_DRYING_RACK, Blocks.ACACIA_SLAB);
		dryingRackBlock(output, "dark_oak", TFBlocks.DARK_OAK_DRYING_RACK, Blocks.DARK_OAK_SLAB);
		dryingRackBlock(output, "crimson", TFBlocks.CRIMSON_DRYING_RACK, Blocks.CRIMSON_SLAB);
		dryingRackBlock(output, "warped", TFBlocks.WARPED_DRYING_RACK, Blocks.WARPED_SLAB);
		dryingRackBlock(output, "vangrove", TFBlocks.VANGROVE_DRYING_RACK, Blocks.MANGROVE_SLAB);
		dryingRackBlock(output, "bamboo", TFBlocks.BAMBOO_DRYING_RACK, Blocks.BAMBOO_SLAB);
		dryingRackBlock(output, "cherry", TFBlocks.CHERRY_DRYING_RACK, Blocks.CHERRY_SLAB);

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

	private <T extends AbstractCookingRecipe> void oreberryRecipes(RecipeOutput output, String processName, RecipeSerializer<T> process, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.IRON_BERRY.get()), RecipeCategory.MISC, Items.IRON_NUGGET, 1.0F, smeltingTime, process, factory).unlockedBy("has_item", has(TFItems.IRON_BERRY.get())).group("iron_nugget").save(output, TwilightForestMod.prefix("material/" + processName + "_iron_nugget").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.GOLD_BERRY.get()), RecipeCategory.MISC, Items.GOLD_NUGGET, 1.0F, smeltingTime, process, factory).unlockedBy("has_item", has(TFItems.GOLD_BERRY.get())).group("gold_nugget").save(output, TwilightForestMod.prefix("material/" + processName + "_gold_nugget").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.COPPER_BERRY.get()), RecipeCategory.MISC, TFItems.COPPER_NUGGET, 1.0F, smeltingTime, process, factory).unlockedBy("has_item", has(TFItems.COPPER_BERRY.get())).group("copper_nugget").save(output, TwilightForestMod.prefix("material/" + processName + "_copper_nugget").toString());

	}

	private void travellersGearRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GOGGLES)
			.pattern("l l")
			.pattern("plp")
			.pattern("g g")
			.define('l', TFItems.TANNED_LEATHER)
			.define('p', Tags.Items.GLASS_PANES_COLORLESS)
			.define('g', Tags.Items.INGOTS_GOLD)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_GOGGLES.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_CHESTPLATE)
			.pattern("l l")
			.pattern("lwl")
			.pattern("lwl")
			.define('l', TFItems.TANNED_LEATHER)
			.define('w', ItemTags.WOOL)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_WINGS)
			.pattern("g g")
			.pattern("ili")
			.pattern("ici")
			.define('l', TFItems.TANNED_LEATHER)
			.define('g', Tags.Items.INGOTS_GOLD)
			.define('i', Tags.Items.INGOTS_COPPER)
			.define('c', ItemTagGenerator.CARMINITE_GEMS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_WINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BOOTS)
			.pattern("s s")
			.pattern("l l")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BELT)
			.pattern("lll")
			.pattern("ici")
			.pattern("lll")
			.define('l', TFItems.TANNED_LEATHER)
			.define('c', Tags.Items.CHESTS_WOODEN)
			.define('i', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_BELT.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GLOVES)
			.pattern("s s")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(output, locEquip(TFItems.TRAVELLERS_GLOVES.getId().getPath()));
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

	@SafeVarargs
	private static @NotNull Ingredient potionsIngredient(Holder<Potion> @NotNull ... potions) {
		Ingredient[] ingredients = new Ingredient[potions.length];
		for (int i = 0; i < potions.length; i++) {
			ingredients[i] = potionIngredient(potions[i]);
		}
		return CompoundIngredient.of(ingredients);
	}

	private static @NotNull Ingredient potionIngredient(@NotNull Holder<Potion> potion) {
		return DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(potion), Items.POTION);
	}
}
