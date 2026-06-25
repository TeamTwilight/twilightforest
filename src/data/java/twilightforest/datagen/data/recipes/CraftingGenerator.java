package twilightforest.datagen.data.recipes;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import twilightforest.datagen.data.custom.DryingRecipeBuilder;
import twilightforest.datagen.data.custom.NoSmithingTemplateRecipeBuilder;
import twilightforest.datagen.data.custom.ScepterRecipeBuilder;
import twilightforest.datagen.data.custom.UncraftingGenerator;
import twilightforest.datagen.helpers.CraftingDataHelper;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

public class CraftingGenerator extends CraftingDataHelper {

	public CraftingGenerator(RecipeOutput output, HolderLookup.Provider provider) {
		super(output, provider);
	}

	@Override
	protected void buildRecipes() {
		HolderGetter<Item> getter = this.registries.lookupOrThrow(Registries.ITEM);
		StonecuttingGenerator.buildRecipes(getter, this.output);
		UncraftingGenerator.buildRecipes(getter, this.output);

		blockCompressionRecipes(getter);
		equipmentRecipes(getter, this.registries);
		travellersGearRecipes(getter);
		emptyMapRecipes(getter);
		woodRecipes(getter);
		fieryConversions(getter);

		nagastoneRecipes(getter);
		darkTowerRecipes(getter);
		castleRecipes(getter);

		bannerPattern(getter, "naga_banner_pattern", TFBlocks.NAGA_TROPHY, TFItems.NAGA_BANNER_PATTERN);
		bannerPattern(getter, "lich_banner_pattern", TFBlocks.LICH_TROPHY, TFItems.LICH_BANNER_PATTERN);
		bannerPattern(getter, "minoshroom_banner_pattern", TFBlocks.MINOSHROOM_TROPHY, TFItems.MINOSHROOM_BANNER_PATTERN);
		bannerPattern(getter, "hydra_banner_pattern", TFBlocks.HYDRA_TROPHY, TFItems.HYDRA_BANNER_PATTERN);
		bannerPattern(getter, "knight_phantom_banner_pattern", TFBlocks.KNIGHT_PHANTOM_TROPHY, TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
		bannerPattern(getter, "ur_ghast_banner_pattern", TFBlocks.UR_GHAST_TROPHY, TFItems.UR_GHAST_BANNER_PATTERN);
		bannerPattern(getter, "alpha_yeti_banner_pattern", TFBlocks.ALPHA_YETI_TROPHY, TFItems.ALPHA_YETI_BANNER_PATTERN);
		bannerPattern(getter, "snow_queen_banner_pattern", TFBlocks.SNOW_QUEEN_TROPHY, TFItems.SNOW_QUEEN_BANNER_PATTERN);
//		bannerPattern(getter, "questing_ram_banner_pattern", TFBlocks.QUEST_RAM_TROPHY, TFItems.QUEST_RAM_BANNER_PATTERN);

		slabBlock(getter, "aurora_slab", TFBlocks.AURORA_SLAB, TFBlocks.AURORA_BLOCK);
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.AURORA_PILLAR, 2)
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.AURORA_BLOCK))
			.unlockedBy("has_slab", has(TFBlocks.AURORA_SLAB))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.IRON_LADDER, 3)
			.pattern("-#-")
			.pattern("-#-")
			.define('#', Ingredient.of(Blocks.IRON_BARS))
			.define('-', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_iron_bars", has(Blocks.IRON_BARS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.WROUGHT_IRON_FENCE, 3)
			.pattern("###")
			.pattern("###")
			.define('#', TFItemTags.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(TFItemTags.WROUGHT_IRON_INGOTS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA, 2)
			.pattern("###")
			.pattern(" # ")
			.define('#', TFItemTags.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(TFItemTags.WROUGHT_IRON_INGOTS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.ROPE, 8)
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_root_strand", has(TFBlocks.ROOT_STRAND))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW, 4)
			.pattern("GPG")
			.pattern("PPP")
			.pattern("GPG")
			.define('G', Ingredient.of(getter.getOrThrow(Tags.Items.GLASS_BLOCKS)))
			.define('P', Ingredient.of(TFBlocks.CANOPY_PLANKS))
			.unlockedBy("has_planks", has(TFBlocks.CANOPY_PLANKS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW_PANE, 16)
			.pattern("GGG")
			.pattern("GGG")
			.define('G', Ingredient.of(TFBlocks.CANOPY_WINDOW))
			.unlockedBy("has_windows", has(TFBlocks.CANOPY_WINDOW))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFItems.MASON_JAR, 4)
			.pattern("GLG")
			.pattern("G G")
			.pattern("GGG")
			.define('G', Ingredient.of(Items.GLASS))
			.define('L', Ingredient.of(TFBlocks.TWILIGHT_OAK_LOG))
			.unlockedBy("has_tf_oak", has(TFBlocks.TWILIGHT_OAK_LOG.value()))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_JAR)
			.requires(Ingredient.of(TFBlocks.FIREFLY))
			.requires(Ingredient.of(TFItems.MASON_JAR))
			.unlockedBy("has_item", has(TFBlocks.FIREFLY))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_SPAWNER)
			.requires(Ingredient.of(TFBlocks.FIREFLY_JAR))
			.requires(Ingredient.of(TFBlocks.FIREFLY))
			.requires(Ingredient.of(Blocks.POPPY))
			.unlockedBy("has_jar", has(TFBlocks.FIREFLY_JAR))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.DECORATIONS, TFBlocks.CICADA_JAR)
			.requires(Ingredient.of(TFBlocks.CICADA))
			.requires(Ingredient.of(TFItems.MASON_JAR))
			.unlockedBy("has_item", has(TFBlocks.CICADA))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, Items.MAGENTA_DYE)
			.requires(Ingredient.of(TFBlocks.HUGE_WATER_LILY))
			.unlockedBy("has_item", has(TFBlocks.HUGE_WATER_LILY))
			.save(this.output, this.createKey("waterlily_to_magenta"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, Items.RED_DYE)
			.requires(Ingredient.of(TFBlocks.THORN_ROSE))
			.unlockedBy("has_item", has(TFBlocks.THORN_ROSE))
			.save(this.output, this.createKey("thorn_rose_to_red"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, Items.STICK)
			.requires(Ingredient.of(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_item", has(TFBlocks.ROOT_STRAND))
			.group("sticks")
			.save(this.output, this.createKey("root_stick"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
			.pattern("∴")
			.pattern("|")
			.define('∴', Ingredient.of(TFItems.TORCHBERRIES))
			.define('|', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(TFItems.TORCHBERRIES))
			.save(this.output, this.createKey("berry_torch"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.TANNIN)
			.requires(potionIngredient(Potions.WATER))
			.requires(TFBlocks.TWILIGHT_OAK_SAPLING)
			.requires(TFBlocks.ROOT_STRAND)
			.requires(TFBlocks.TWILIGHT_OAK_LEAVES)
			.unlockedBy("has_sapling", has(TFBlocks.TWILIGHT_OAK_SAPLING))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.TREATED_LEATHER)
			.requires(TFItems.TANNIN)
			.requires(Tags.Items.LEATHERS)
			.unlockedBy("has_tannin", has(TFItems.TANNIN))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.FOOD, TFItems.BERRY_MEDLEY)
			.requires(Items.BOWL)
			.requires(TFItems.RASPBERRY)
			.requires(TFItems.BLUEBERRY)
			.requires(TFItems.BLACKBERRY)
			.requires(TFItems.MALOBERRY)
			.unlockedBy("has_raspberry", has(TFItems.RASPBERRY))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.FOOD, TFItems.MOSS_SOUP)
			.requires(TFBlocks.MOSS_PATCH)
			.requires(Items.BOWL)
			.requires(potionIngredient(Potions.WATER))
			.unlockedBy("has_moss", has(TFBlocks.MOSS_PATCH))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.UNCRAFTING_TABLE)
			.pattern("###")
			.pattern("#X#")
			.pattern("###")
			.define('#', Blocks.CRAFTING_TABLE)
			.define('X', TFItems.MAZE_MAP_FOCUS)
			.unlockedBy("has_uncrafting_table", has(TFBlocks.UNCRAFTING_TABLE))
			.save(this.output.withConditions(UncraftingTableCondition.INSTANCE), this.createKey("uncrafting_table"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.COPPER_NUGGET, 9)
			.requires(Tags.Items.INGOTS_COPPER)
			.unlockedBy("has_item", has(Tags.Items.INGOTS_COPPER))
			.save(this.output, this.createKey("copper_ingot_to_nuggets"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, Items.COPPER_INGOT)
			.requires(Ingredient.of(TFItems.COPPER_NUGGET), 9)
			.unlockedBy("has_item", has(TFItems.COPPER_NUGGET))
			.save(this.output, this.createKey("copper_nuggets_to_ingot"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, Items.LEAD, 2)
			.pattern("~~ ")
			.pattern("~O ")
			.pattern("  ~")
			.define('~', Items.STRING)
			.define('O', TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(this.output, this.createKey("lead_maze_ver"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BREWING, Items.MAGMA_CREAM)
			.requires(Items.BLAZE_POWDER)
			.requires(TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
			.save(this.output, this.createKey("magma_cream_maze_ver"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, Blocks.STICKY_PISTON)
			.pattern("S")
			.pattern("P")
			.define('P', Blocks.PISTON)
			.define('S', TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(this.output, this.createKey("sticky_piston_maze_ver"));

		dryingRecipes(getter);

		cookingRecipes("smelted", SmeltingRecipe::new, 200);
		cookingRecipes("smoked", SmokingRecipe::new, 100);
		cookingRecipes("campfired", CampfireCookingRecipe::new, 600);

		ingotRecipes("smelted", SmeltingRecipe::new, 200);
		ingotRecipes("blasted", BlastingRecipe::new, 100);

		oreberryRecipes("smelted", SmeltingRecipe::new, 200);
		oreberryRecipes("blasted", BlastingRecipe::new, 100);

		crackedWoodRecipes();
		crackedStoneRecipes();

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.CHISELED_CANOPY_BOOKSHELF)
			.pattern("---")
			.pattern("sss")
			.pattern("---")
			.define('-', TFBlocks.CANOPY_PLANKS)
			.define('s', TFBlocks.CANOPY_SLAB)
			.unlockedBy("has_item", has(TFBlocks.CANOPY_SLAB))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.CANOPY_BOOKSHELF)
			.pattern("---")
			.pattern("B B")
			.pattern("---")
			.define('-', TFBlocks.CANOPY_PLANKS)
			.define('B', Items.BOOK)
			.unlockedBy("has_item", has(TFBlocks.CANOPY_PLANKS))
			.save(this.output);

//		ShapedRecipeBuilder.shaped(getter, RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA)
//			.pattern("III")
//			.pattern(" W ")
//			.define('W', TFBlocks.WROUGHT_IRON_FENCE)
//			.define('I', Tags.Items.INGOTS_IRON)
//			.unlockedBy("has_item", has(TFBlocks.WROUGHT_IRON_FENCE))
//			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.ARMOR_SHARD_CLUSTER)
			.requires(Ingredient.of(TFItems.ARMOR_SHARD), 9)
			.unlockedBy("has_item", has(TFItems.ARMOR_SHARD))
			.save(this.output, this.createKey("material/" + TFItems.ARMOR_SHARD_CLUSTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_UNDERBRICK, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.UNDERBRICK))
			.unlockedBy("has_item", has(TFBlocks.UNDERBRICK))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_MAZESTONE, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.MAZESTONE_BRICK))
			.unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK))
			.save(this.output, this.createKey("maze_stone/mossy_mazestone"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.CARMINITE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(Items.GHAST_TEAR))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE))
			.requires(Tags.Items.DUSTS_REDSTONE)
			.requires(Ingredient.of(TFItems.BORER_ESSENCE))
			.unlockedBy("has_item", has(TFItems.BORER_ESSENCE))
			.save(this.output, this.createKey("material/" + TFItems.CARMINITE.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.RAW_IRONWOOD, 2)
			.requires(Ingredient.of(TFItems.LIVEROOT))
			.requires(Ingredient.of(Items.RAW_IRON))
			.requires(Tags.Items.NUGGETS_GOLD)
			.unlockedBy("has_item", has(TFItems.LIVEROOT))
			.save(this.output, this.createKey("material/" + TFItems.RAW_IRONWOOD.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.RAINY_CLOUD, 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD))
			.define('b', Ingredient.of(Items.WATER_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD))
			.save(this.output, this.createKey("rainy_cloud"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.SNOWY_CLOUD, 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD))
			.define('b', Ingredient.of(Items.POWDER_SNOW_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD))
			.save(this.output, this.createKey("snowy_cloud"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.MYSTIC_CROWN)
			.pattern("ttt")
			.pattern("t t")
			.pattern("ttt")
			.define('t', Ingredient.of(TFItems.CROWN_SPLINTER))
			.unlockedBy("has_item", has(TFItems.CROWN_SPLINTER))
			.save(this.output);
	}

	private void darkTowerRecipes(HolderGetter<Item> getter) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.ENCASED_FIRE_JET)
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("uuu")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('^', Ingredient.of(TFBlocks.FIRE_JET))
			.define('u', Ingredient.of(Items.LAVA_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FIRE_JET))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.ENCASED_SMOKER)
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('^', Ingredient.of(TFBlocks.SMOKER))
			.unlockedBy("has_item", has(TFBlocks.SMOKER))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.CARMINITE_BUILDER)
			.pattern("#6#")
			.pattern("6o6")
			.pattern("#6#")
			.define('6', TFItemTags.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('o', Ingredient.of(Blocks.DISPENSER))
			.unlockedBy("has_item", has(TFItemTags.CARMINITE_GEMS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.CARMINITE_REACTOR)
			.pattern("#6#")
			.pattern("6%6")
			.pattern("#6#")
			.define('6', TFItemTags.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('%', Tags.Items.ORES_REDSTONE)
			.unlockedBy("has_item", has(TFItemTags.CARMINITE_GEMS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.REAPPEARING_BLOCK, 2)
			.pattern("#∴#")
			.pattern("∴6∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('6', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.REDSTONE, TFBlocks.VANISHING_BLOCK, 8)
			.pattern("#w#")
			.pattern("w6w")
			.pattern("#w#")
			.define('w', TFItemTags.TOWERWOOD)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('6', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_TOWERWOOD)
			.requires(Ingredient.of(TFBlocks.TOWERWOOD))
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.unlockedBy("has_item", has(TFBlocks.TOWERWOOD))
			.save(this.output, this.createKey("wood/" + TFBlocks.MOSSY_TOWERWOOD.getId().getPath()));

	}

	private void equipmentRecipes(HolderGetter<Item> getter, HolderLookup.Provider provider) {
		bootsItem(getter, TFItems.IRONWOOD_BOOTS, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FEATHER_FALLING, 1)));
		leggingsItem(getter, TFItems.IRONWOOD_LEGGINGS, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 1)));
		chestplateItem(getter, TFItems.IRONWOOD_CHESTPLATE, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 1)));
		helmetItem(getter, TFItems.IRONWOOD_HELMET, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.AQUA_AFFINITY, 1)));
		swordItem(getter, TFItems.IRONWOOD_SWORD, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.KNOCKBACK, 1)));
		pickaxeItem(getter, TFItems.IRONWOOD_PICKAXE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 1)));
		axeItem(getter, TFItems.IRONWOOD_AXE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 1)));
		shovelItem(getter, TFItems.IRONWOOD_SHOVEL, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.UNBREAKING, 1)));
		hoeItem(getter, TFItems.IRONWOOD_HOE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 1)));

		bootsItem(getter, TFItems.STEELEAF_BOOTS, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FEATHER_FALLING, 2)));
		leggingsItem(getter, TFItems.STEELEAF_LEGGINGS, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.FIRE_PROTECTION, 2)));
		chestplateItem(getter, TFItems.STEELEAF_CHESTPLATE, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.BLAST_PROTECTION, 2)));
		helmetItem(getter, TFItems.STEELEAF_HELMET, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(provider, Pair.of(Enchantments.PROJECTILE_PROTECTION, 2)));
		swordItem(getter, TFItems.STEELEAF_SWORD, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.LOOTING, 2)));
		pickaxeItem(getter, TFItems.STEELEAF_PICKAXE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 2)));
		axeItem(getter, TFItems.STEELEAF_AXE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 2)));
		shovelItem(getter, TFItems.STEELEAF_SHOVEL, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.EFFICIENCY, 2)));
		hoeItem(getter, TFItems.STEELEAF_HOE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(provider, Pair.of(Enchantments.FORTUNE, 2)));

		bootsItem(getter, TFItems.ARCTIC_BOOTS, TFItemTags.ARCTIC_FUR);
		chestplateItem(getter, TFItems.ARCTIC_CHESTPLATE, TFItemTags.ARCTIC_FUR);
		helmetItem(getter, TFItems.ARCTIC_HELMET, TFItemTags.ARCTIC_FUR);
		leggingsItem(getter, TFItems.ARCTIC_LEGGINGS, TFItemTags.ARCTIC_FUR);

		bootsItem(getter, TFItems.KNIGHTMETAL_BOOTS, TFItemTags.KNIGHTMETAL_INGOTS);
		chestplateItem(getter, TFItems.KNIGHTMETAL_CHESTPLATE, TFItemTags.KNIGHTMETAL_INGOTS);
		helmetItem(getter, TFItems.KNIGHTMETAL_HELMET, TFItemTags.KNIGHTMETAL_INGOTS);
		leggingsItem(getter, TFItems.KNIGHTMETAL_LEGGINGS, TFItemTags.KNIGHTMETAL_INGOTS);
		pickaxeItem(getter, TFItems.KNIGHTMETAL_PICKAXE, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		swordItem(getter, TFItems.KNIGHTMETAL_SWORD, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		axeItem(getter, TFItems.KNIGHTMETAL_AXE, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.FIERY_BOOTS)
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_boots")
			.save(this.output, locEquip(TFItems.FIERY_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.FIERY_LEGGINGS)
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_leggings")
			.save(this.output, locEquip(TFItems.FIERY_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.FIERY_CHESTPLATE)
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_chestplate")
			.save(this.output, locEquip(TFItems.FIERY_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.FIERY_HELMET)
			.pattern("###")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_helmet")
			.save(this.output, locEquip(TFItems.FIERY_HELMET.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', TFItemTags.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_pickaxe")
			.save(this.output, locEquip(TFItems.FIERY_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.FIERY_SWORD)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', TFItemTags.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_sword")
			.save(this.output, locEquip(TFItems.FIERY_SWORD.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.NAGA_CHESTPLATE, 1, this.buildEnchants(provider, Pair.of(Enchantments.FIRE_PROTECTION, 3)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(this.output, locEquip(TFItems.NAGA_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.NAGA_LEGGINGS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 3)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(this.output, locEquip(TFItems.NAGA_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_HELMET, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_HELMET.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_CHESTPLATE, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_LEGGINGS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_BOOTS, 1, this.buildEnchants(provider, Pair.of(Enchantments.PROTECTION, 2), Pair.of(Enchantments.FEATHER_FALLING, 4)).build()))
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, TFItems.GIANT_PICKAXE)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', TFBlocks.GIANT_COBBLESTONE)
			.define('X', TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, locEquip(TFItems.GIANT_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TOOLS, TFItems.GIANT_SWORD)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', TFBlocks.GIANT_COBBLESTONE)
			.define('X', TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, locEquip(TFItems.GIANT_SWORD.getId().getPath()));

		charmRecipe(getter, "charm_of_keeping_2", TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_1);
		charmRecipe(getter, "charm_of_keeping_3", TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_KEEPING_2);
		charmRecipe(getter, "charm_of_life_2", TFItems.CHARM_OF_LIFE_2, TFItems.CHARM_OF_LIFE_1);

		SpecialRecipeBuilder.special(MoonwormQueenRepairRecipe::new).save(this.output, this.createKey("moonworm_queen_repair_recipe"));
		SpecialRecipeBuilder.special(MagicMapCloningRecipe::new).save(this.output, this.createKey("magic_map_cloning_recipe"));
		SpecialRecipeBuilder.special(MazeMapCloningRecipe::new).save(this.output, this.createKey("maze_map_cloning_recipe"));
		SpecialRecipeBuilder.special(EmperorsClothRecipe::new).save(this.output, this.createKey("emperors_cloth_recipe"));
		SpecialRecipeBuilder.special(CasketRepairRecipe::new).save(this.output, this.createKey("casket_repair_recipe"));
		SpecialRecipeBuilder.special(EssenceRepairRecipe::new).save(this.output, this.createKey("essence_repair_recipe"));
		SpecialRecipeBuilder.special(TravellersVestGlovesMergeRecipe::new).unlockedBy("has_item", has(TFItems.TRAVELLERS_VEST)).save(this.output, this.createKey("travellers_vest_gloves_merge_recipe"));

		NoSmithingTemplateRecipeBuilder
			.noTemplate(Ingredient.of(getter.getOrThrow(Tags.Items.ARMORS)), Ingredient.of(TFItems.EMPERORS_CLOTH), RecipeCategory.MISC)
			.attachData(TFDataComponents.EMPERORS_CLOTH::value, Unit.INSTANCE)
			.unlocks("has_cloth", has(TFItems.EMPERORS_CLOTH))
			.save(this.output, this.createKey("emperors_cloth_smithing"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 64)
			.requires(TFBlocks.GIANT_COBBLESTONE)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, this.createKey(TFBlocks.GIANT_COBBLESTONE.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.COBBLESTONE).getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LOG, 64)
			.requires(TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_LOG))
			.save(this.output, this.createKey(TFBlocks.GIANT_LOG.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_LOG).getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LEAVES, 64)
			.requires(TFBlocks.GIANT_LEAVES)
			.unlockedBy("has_item", has(TFBlocks.GIANT_LEAVES))
			.save(this.output, this.createKey(TFBlocks.GIANT_LEAVES.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_LEAVES).getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, Blocks.OBSIDIAN, 64)
			.requires(TFBlocks.GIANT_OBSIDIAN)
			.unlockedBy("has_item", has(TFBlocks.GIANT_OBSIDIAN))
			.save(this.output, this.createKey(TFBlocks.GIANT_OBSIDIAN.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OBSIDIAN).getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.COMBAT, TFItems.BLOCK_AND_CHAIN)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)))
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.KNIGHTMETAL_INGOTS)), 3)
			.requires(Ingredient.of(TFItems.KNIGHTMETAL_RING))
			.unlockedBy("has_block", has(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL))
			.unlockedBy("has_ingot", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING))
			.save(this.output, locEquip(TFItems.BLOCK_AND_CHAIN.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, TFItems.KNIGHTMETAL_RING)
			.pattern(" - ")
			.pattern("- -")
			.pattern(" - ")
			.define('-', TFItemTags.KNIGHTMETAL_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.save(this.output, locEquip(TFItems.KNIGHTMETAL_RING.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.COMBAT, TFItems.KNIGHTMETAL_SHIELD)
			.pattern("-#")
			.pattern("-o")
			.pattern("-#")
			.define('-', TFItemTags.KNIGHTMETAL_INGOTS)
			.define('#', TFItemTags.TOWERWOOD)
			.define('o', Ingredient.of(TFItems.KNIGHTMETAL_RING))
			.unlockedBy("has_ingot", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING))
			.save(this.output, locEquip(TFItems.KNIGHTMETAL_SHIELD.getId().getPath()));

		ScepterRecipeBuilder.repairFor(getter, TFItems.LIFEDRAIN_SCEPTER, 9)
			.addRepairIngredient(Items.FERMENTED_SPIDER_EYE)
			.save(this.output, locEquip(TFItems.LIFEDRAIN_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(getter, TFItems.FORTIFICATION_SCEPTER, 9)
			.addRepairIngredient(Ingredient.of(Items.GOLDEN_APPLE))
			.save(this.output, locEquip(TFItems.FORTIFICATION_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(getter, TFItems.TWILIGHT_SCEPTER, 9)
			.addRepairIngredient(Tags.Items.ENDER_PEARLS)
			.save(this.output, locEquip(TFItems.TWILIGHT_SCEPTER.getId().getPath()));

		ScepterRecipeBuilder.repairFor(getter, TFItems.ZOMBIE_SCEPTER, 9)
			.addRepairIngredient(CompoundIngredient.of(
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRENGTH), Items.POTION),
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_STRENGTH), Items.POTION),
				DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_STRENGTH), Items.POTION)
			))
			.addRepairIngredient(Items.ROTTEN_FLESH)
			.save(this.output, locEquip(TFItems.ZOMBIE_SCEPTER.getId().getPath()));
	}

	private void travellersGearRecipes(HolderGetter<Item> getter) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GOGGLES)
			.pattern("l l")
			.pattern("plp")
			.pattern("g g")
			.define('l', TFItems.TANNED_LEATHER)
			.define('p', Tags.Items.GLASS_PANES_COLORLESS)
			.define('g', Tags.Items.INGOTS_GOLD)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_GOGGLES.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_VEST)
			.pattern("l l")
			.pattern("lwl")
			.pattern("lwl")
			.define('l', TFItems.TANNED_LEATHER)
			.define('w', ItemTags.WOOL)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_VEST.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_WINGS)
			.pattern("g g")
			.pattern("ili")
			.pattern("ici")
			.define('l', TFItems.TANNED_LEATHER)
			.define('g', Tags.Items.INGOTS_GOLD)
			.define('i', Tags.Items.INGOTS_COPPER)
			.define('c', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_WINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BOOTS)
			.pattern("s s")
			.pattern("l l")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BELT)
			.pattern("lll")
			.pattern("ixi")
			.pattern("lll")
			.define('l', TFItems.TANNED_LEATHER)
			.define('x', DifferenceIngredient.of(Ingredient.of(getter.getOrThrow(Tags.Items.CHESTS_WOODEN)), Ingredient.of(getter.getOrThrow(Tags.Items.CHESTS_TRAPPED))))
			.define('i', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_BELT.getId().getPath()));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GLOVES)
			.pattern("s s")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_GLOVES.getId().getPath()));
	}

	private void blockCompressionRecipes(HolderGetter<Item> getter) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.ARCTIC_FUR, 9)
			.requires(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR)
			.unlockedBy("has_item", has(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR))
			.save(this.output, this.createKey("compressed_blocks/reversed/arctic_fur_block"));
		reverseCompressBlock(getter, "carminite_block_to_item", TFItems.CARMINITE, TFItemTags.STORAGE_BLOCKS_CARMINITE);
		reverseCompressBlock(getter, "ironwood_block_ingot", TFItems.IRONWOOD_INGOT, TFItemTags.STORAGE_BLOCKS_IRONWOOD);
		reverseCompressBlock(getter, "knightmetal_block_ingot", TFItems.KNIGHTMETAL_INGOT, TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL);
		reverseCompressBlock(getter, "steeleaf_block_ingot", TFItems.STEELEAF_INGOT, TFItemTags.STORAGE_BLOCKS_STEELEAF);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.FIERY_INGOT, 9)
			.requires(TFItemTags.STORAGE_BLOCKS_FIERY)
			.unlockedBy("has_item", has(TFItemTags.STORAGE_BLOCKS_FIERY))
			.group("fiery_ingot")
			.save(this.output, this.createKey("compressed_blocks/reversed/fiery_block_to_ingot"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.ARCTIC_FUR_BLOCK.get())
			.pattern("###")
			.pattern("#m#")
			.pattern("###")
			.define('#', TFItemTags.ARCTIC_FUR)
			.define('m', TFItems.ARCTIC_FUR.get())
			.unlockedBy("has_item", has(TFItemTags.ARCTIC_FUR))
			.save(this.output, this.createKey("compressed_blocks/arctic_fur_block"));
		compressedBlock(getter, "carminite_block", TFBlocks.CARMINITE_BLOCK, TFItemTags.CARMINITE_GEMS);
		compressedBlock(getter, "fiery_block", TFBlocks.FIERY_BLOCK, TFItemTags.FIERY_INGOTS);
		compressedBlock(getter, "ironwood_block", TFBlocks.IRONWOOD_BLOCK, TFItemTags.IRONWOOD_INGOTS);
		compressedBlock(getter, "knightmetal_block", TFBlocks.KNIGHTMETAL_BLOCK, TFItemTags.KNIGHTMETAL_INGOTS);
		compressedBlock(getter, "steeleaf_block", TFBlocks.STEELEAF_BLOCK, TFItemTags.STEELEAF_INGOTS);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MAZE_SLIME_BLOCK)
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.MAZE_SLIME_BALL)
			.unlockedBy("has_item", has(TFItems.MAZE_SLIME_BALL))
			.save(this.output, this.createKey("compressed_blocks/maze_slime"));
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.MAZE_SLIME_BALL, 9)
			.requires(TFBlocks.MAZE_SLIME_BLOCK)
			.unlockedBy("has_item", has(TFBlocks.MAZE_SLIME_BLOCK))
			.save(this.output, this.createKey("compressed_blocks/reversed/maze_slime_block"));
	}

	private void emptyMapRecipes(HolderGetter<Item> getter) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.MAGIC_MAP_FOCUS)
			.requires(TFItems.RAVEN_FEATHER)
			.requires(TFItems.TORCHBERRIES)
			.requires(Tags.Items.DUSTS_GLOWSTONE)
			.unlockedBy("has_berries", has(TFItems.TORCHBERRIES))
			.unlockedBy("has_feather", has(TFItems.RAVEN_FEATHER))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, TFItems.MAGIC_MAP)
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', TFItemTags.PAPER)
			.define('•', Ingredient.of(TFItems.MAGIC_MAP_FOCUS))
			.unlockedBy("has_item", has(TFItems.MAGIC_MAP_FOCUS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, TFItems.MAZE_MAP)
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', TFItemTags.PAPER)
			.define('•', Ingredient.of(TFItems.MAZE_MAP_FOCUS))
			.unlockedBy("has_item", has(TFItems.MAZE_MAP_FOCUS))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.ORE_MAP)
			.requires(TFItems.MAZE_MAP)
			.requires(Tags.Items.STORAGE_BLOCKS_DIAMOND)
			.requires(Tags.Items.STORAGE_BLOCKS_GOLD)
			.requires(Tags.Items.STORAGE_BLOCKS_IRON)
			.unlockedBy("has_item", has(TFItems.MAZE_MAP))
			.save(this.output);
	}

	private void woodRecipes(HolderGetter<Item> getter) {
		buttonBlock(getter, "canopy", TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_PLANKS);
		buttonBlock(getter, "dark", TFBlocks.DARK_BUTTON, TFBlocks.DARK_PLANKS);
		buttonBlock(getter, "mangrove", TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_PLANKS);
		buttonBlock(getter, "mining", TFBlocks.MINING_BUTTON, TFBlocks.MINING_PLANKS);
		buttonBlock(getter, "sorting", TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_PLANKS);
		buttonBlock(getter, "time", TFBlocks.TIME_BUTTON, TFBlocks.TIME_PLANKS);
		buttonBlock(getter, "transformation", TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_PLANKS);
		buttonBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_PLANKS);

		doorBlock(getter, "canopy", TFBlocks.CANOPY_DOOR, TFBlocks.CANOPY_PLANKS);
		doorBlock(getter, "dark", TFBlocks.DARK_DOOR, TFBlocks.DARK_PLANKS);
		doorBlock(getter, "mangrove", TFBlocks.MANGROVE_DOOR, TFBlocks.MANGROVE_PLANKS);
		doorBlock(getter, "mining", TFBlocks.MINING_DOOR, TFBlocks.MINING_PLANKS);
		doorBlock(getter, "sorting", TFBlocks.SORTING_DOOR, TFBlocks.SORTING_PLANKS);
		doorBlock(getter, "time", TFBlocks.TIME_DOOR, TFBlocks.TIME_PLANKS);
		doorBlock(getter, "transformation", TFBlocks.TRANSFORMATION_DOOR, TFBlocks.TRANSFORMATION_PLANKS);
		doorBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_DOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		fenceBlock(getter, "canopy", TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_PLANKS);
		fenceBlock(getter, "dark", TFBlocks.DARK_FENCE, TFBlocks.DARK_PLANKS);
		fenceBlock(getter, "mangrove", TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_PLANKS);
		fenceBlock(getter, "mining", TFBlocks.MINING_FENCE, TFBlocks.MINING_PLANKS);
		fenceBlock(getter, "sorting", TFBlocks.SORTING_FENCE, TFBlocks.SORTING_PLANKS);
		fenceBlock(getter, "time", TFBlocks.TIME_FENCE, TFBlocks.TIME_PLANKS);
		fenceBlock(getter, "transformation", TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_PLANKS);
		fenceBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_PLANKS);

		gateBlock(getter, "canopy", TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLANKS);
		gateBlock(getter, "dark", TFBlocks.DARK_GATE, TFBlocks.DARK_PLANKS);
		gateBlock(getter, "mangrove", TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLANKS);
		gateBlock(getter, "mining", TFBlocks.MINING_GATE, TFBlocks.MINING_PLANKS);
		gateBlock(getter, "sorting", TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLANKS);
		gateBlock(getter, "time", TFBlocks.TIME_GATE, TFBlocks.TIME_PLANKS);
		gateBlock(getter, "transformation", TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLANKS);
		gateBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		planksBlock(getter, "canopy", TFBlocks.CANOPY_PLANKS, TFItemTags.CANOPY_LOGS);
		planksBlock(getter, "dark", TFBlocks.DARK_PLANKS, TFItemTags.DARKWOOD_LOGS);
		planksBlock(getter, "mangrove", TFBlocks.MANGROVE_PLANKS, TFItemTags.MANGROVE_LOGS);
		planksBlock(getter, "mining", TFBlocks.MINING_PLANKS, TFItemTags.MINING_LOGS);
		planksBlock(getter, "sorting", TFBlocks.SORTING_PLANKS, TFItemTags.SORTING_LOGS);
		planksBlock(getter, "time", TFBlocks.TIME_PLANKS, TFItemTags.TIME_LOGS);
		planksBlock(getter, "transformation", TFBlocks.TRANSFORMATION_PLANKS, TFItemTags.TRANSFORMATION_LOGS);
		planksBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS, TFItemTags.TWILIGHT_OAK_LOGS);

		woodBlock(getter, "canopy", TFBlocks.CANOPY_WOOD, TFBlocks.CANOPY_LOG);
		woodBlock(getter, "dark", TFBlocks.DARK_WOOD, TFBlocks.DARK_LOG);
		woodBlock(getter, "mangrove", TFBlocks.MANGROVE_WOOD, TFBlocks.MANGROVE_LOG);
		woodBlock(getter, "mining", TFBlocks.MINING_WOOD, TFBlocks.MINING_LOG);
		woodBlock(getter, "sorting", TFBlocks.SORTING_WOOD, TFBlocks.SORTING_LOG);
		woodBlock(getter, "time", TFBlocks.TIME_WOOD, TFBlocks.TIME_LOG);
		woodBlock(getter, "transformation", TFBlocks.TRANSFORMATION_WOOD, TFBlocks.TRANSFORMATION_LOG);
		woodBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.TWILIGHT_OAK_LOG);

		strippedWoodBlock(getter, "canopy", TFBlocks.STRIPPED_CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_LOG);
		strippedWoodBlock(getter, "dark", TFBlocks.STRIPPED_DARK_WOOD, TFBlocks.STRIPPED_DARK_LOG);
		strippedWoodBlock(getter, "mangrove", TFBlocks.STRIPPED_MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_LOG);
		strippedWoodBlock(getter, "mining", TFBlocks.STRIPPED_MINING_WOOD, TFBlocks.STRIPPED_MINING_LOG);
		strippedWoodBlock(getter, "sorting", TFBlocks.STRIPPED_SORTING_WOOD, TFBlocks.STRIPPED_SORTING_LOG);
		strippedWoodBlock(getter, "time", TFBlocks.STRIPPED_TIME_WOOD, TFBlocks.STRIPPED_TIME_LOG);
		strippedWoodBlock(getter, "transformation", TFBlocks.STRIPPED_TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		strippedWoodBlock(getter, "twilight_oak", TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		plateBlock(getter, "canopy", TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_PLANKS);
		plateBlock(getter, "dark", TFBlocks.DARK_PLATE, TFBlocks.DARK_PLANKS);
		plateBlock(getter, "mangrove", TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_PLANKS);
		plateBlock(getter, "mining", TFBlocks.MINING_PLATE, TFBlocks.MINING_PLANKS);
		plateBlock(getter, "sorting", TFBlocks.SORTING_PLATE, TFBlocks.SORTING_PLANKS);
		plateBlock(getter, "time", TFBlocks.TIME_PLATE, TFBlocks.TIME_PLANKS);
		plateBlock(getter, "transformation", TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_PLANKS);
		plateBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenSlabBlock(getter, "canopy", TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_PLANKS);
		woodenSlabBlock(getter, "dark", TFBlocks.DARK_SLAB, TFBlocks.DARK_PLANKS);
		woodenSlabBlock(getter, "mangrove", TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_PLANKS);
		woodenSlabBlock(getter, "mining", TFBlocks.MINING_SLAB, TFBlocks.MINING_PLANKS);
		woodenSlabBlock(getter, "sorting", TFBlocks.SORTING_SLAB, TFBlocks.SORTING_PLANKS);
		woodenSlabBlock(getter, "time", TFBlocks.TIME_SLAB, TFBlocks.TIME_PLANKS);
		woodenSlabBlock(getter, "transformation", TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_PLANKS);
		woodenSlabBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenStairsBlock(getter, locWood("canopy_stairs"), TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_PLANKS);
		woodenStairsBlock(getter, locWood("dark_stairs"), TFBlocks.DARK_STAIRS, TFBlocks.DARK_PLANKS, TFBlocks.DARK_PLANKS);
		woodenStairsBlock(getter, locWood("mangrove_stairs"), TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_PLANKS);
		woodenStairsBlock(getter, locWood("mining_stairs"), TFBlocks.MINING_STAIRS, TFBlocks.MINING_PLANKS, TFBlocks.MINING_PLANKS);
		woodenStairsBlock(getter, locWood("sorting_stairs"), TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_PLANKS);
		woodenStairsBlock(getter, locWood("time_stairs"), TFBlocks.TIME_STAIRS, TFBlocks.TIME_PLANKS, TFBlocks.TIME_PLANKS);
		woodenStairsBlock(getter, locWood("transformation_stairs"), TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_PLANKS);
		woodenStairsBlock(getter, locWood("twilight_oak_stairs"), TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_PLANKS);

		trapdoorBlock(getter, "canopy", TFBlocks.CANOPY_TRAPDOOR, TFBlocks.CANOPY_PLANKS);
		trapdoorBlock(getter, "dark", TFBlocks.DARK_TRAPDOOR, TFBlocks.DARK_PLANKS);
		trapdoorBlock(getter, "mangrove", TFBlocks.MANGROVE_TRAPDOOR, TFBlocks.MANGROVE_PLANKS);
		trapdoorBlock(getter, "mining", TFBlocks.MINING_TRAPDOOR, TFBlocks.MINING_PLANKS);
		trapdoorBlock(getter, "sorting", TFBlocks.SORTING_TRAPDOOR, TFBlocks.SORTING_PLANKS);
		trapdoorBlock(getter, "time", TFBlocks.TIME_TRAPDOOR, TFBlocks.TIME_PLANKS);
		trapdoorBlock(getter, "transformation", TFBlocks.TRANSFORMATION_TRAPDOOR, TFBlocks.TRANSFORMATION_PLANKS);
		trapdoorBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_TRAPDOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		signBlock(getter, "canopy", TFItems.CANOPY_SIGN, TFBlocks.CANOPY_PLANKS);
		signBlock(getter, "dark", TFItems.DARK_SIGN, TFBlocks.DARK_PLANKS);
		signBlock(getter, "mangrove", TFItems.MANGROVE_SIGN, TFBlocks.MANGROVE_PLANKS);
		signBlock(getter, "mining", TFItems.MINING_SIGN, TFBlocks.MINING_PLANKS);
		signBlock(getter, "sorting", TFItems.SORTING_SIGN, TFBlocks.SORTING_PLANKS);
		signBlock(getter, "time", TFItems.TIME_SIGN, TFBlocks.TIME_PLANKS);
		signBlock(getter, "transformation", TFItems.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_PLANKS);
		signBlock(getter, "twilight_oak", TFItems.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_OAK_PLANKS);

		hangingSignBlock(getter, "canopy", TFItems.CANOPY_HANGING_SIGN, TFBlocks.STRIPPED_CANOPY_LOG);
		hangingSignBlock(getter, "dark", TFItems.DARK_HANGING_SIGN, TFBlocks.STRIPPED_DARK_LOG);
		hangingSignBlock(getter, "mangrove", TFItems.MANGROVE_HANGING_SIGN, TFBlocks.STRIPPED_MANGROVE_LOG);
		hangingSignBlock(getter, "mining", TFItems.MINING_HANGING_SIGN, TFBlocks.STRIPPED_MINING_LOG);
		hangingSignBlock(getter, "sorting", TFItems.SORTING_HANGING_SIGN, TFBlocks.STRIPPED_SORTING_LOG);
		hangingSignBlock(getter, "time", TFItems.TIME_HANGING_SIGN, TFBlocks.STRIPPED_TIME_LOG);
		hangingSignBlock(getter, "transformation", TFItems.TRANSFORMATION_HANGING_SIGN, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		hangingSignBlock(getter, "twilight_oak", TFItems.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		banisterBlock(getter, "canopy", TFBlocks.CANOPY_BANISTER, TFBlocks.CANOPY_SLAB);
		banisterBlock(getter, "dark", TFBlocks.DARK_BANISTER, TFBlocks.DARK_SLAB);
		banisterBlock(getter, "mangrove", TFBlocks.MANGROVE_BANISTER, TFBlocks.MANGROVE_SLAB);
		banisterBlock(getter, "mining", TFBlocks.MINING_BANISTER, TFBlocks.MINING_SLAB);
		banisterBlock(getter, "sorting", TFBlocks.SORTING_BANISTER, TFBlocks.SORTING_SLAB);
		banisterBlock(getter, "time", TFBlocks.TIME_BANISTER, TFBlocks.TIME_SLAB);
		banisterBlock(getter, "transformation", TFBlocks.TRANSFORMATION_BANISTER, TFBlocks.TRANSFORMATION_SLAB);
		banisterBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_BANISTER, TFBlocks.TWILIGHT_OAK_SLAB);

		banisterBlock(getter, "oak", TFBlocks.OAK_BANISTER, Blocks.OAK_SLAB);
		banisterBlock(getter, "spruce", TFBlocks.SPRUCE_BANISTER, Blocks.SPRUCE_SLAB);
		banisterBlock(getter, "birch", TFBlocks.BIRCH_BANISTER, Blocks.BIRCH_SLAB);
		banisterBlock(getter, "jungle", TFBlocks.JUNGLE_BANISTER, Blocks.JUNGLE_SLAB);
		banisterBlock(getter, "acacia", TFBlocks.ACACIA_BANISTER, Blocks.ACACIA_SLAB);
		banisterBlock(getter, "dark_oak", TFBlocks.DARK_OAK_BANISTER, Blocks.DARK_OAK_SLAB);
		banisterBlock(getter, "crimson", TFBlocks.CRIMSON_BANISTER, Blocks.CRIMSON_SLAB);
		banisterBlock(getter, "warped", TFBlocks.WARPED_BANISTER, Blocks.WARPED_SLAB);
		banisterBlock(getter, "vangrove", TFBlocks.VANGROVE_BANISTER, Blocks.MANGROVE_SLAB);
		banisterBlock(getter, "bamboo", TFBlocks.BAMBOO_BANISTER, Blocks.BAMBOO_SLAB);
		banisterBlock(getter, "cherry", TFBlocks.CHERRY_BANISTER, Blocks.CHERRY_SLAB);
		banisterBlock(getter, "pale_oak", TFBlocks.PALE_OAK_BANISTER, Blocks.PALE_OAK_SLAB);

		chestBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS);
		chestBlock(getter, "canopy", TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_PLANKS);
		chestBlock(getter, "mangrove", TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_PLANKS);
		chestBlock(getter, "dark", TFBlocks.DARK_CHEST, TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_PLANKS);
		chestBlock(getter, "time", TFBlocks.TIME_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_PLANKS);
		chestBlock(getter, "transformation", TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_PLANKS);
		chestBlock(getter, "mining", TFBlocks.MINING_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_PLANKS);
		chestBlock(getter, "sorting", TFBlocks.SORTING_CHEST, TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_PLANKS);

		buildBoats(getter, TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT, TFBlocks.TWILIGHT_OAK_PLANKS);
		buildBoats(getter, TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT, TFBlocks.CANOPY_PLANKS);
		buildBoats(getter, TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT, TFBlocks.MANGROVE_PLANKS);
		buildBoats(getter, TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT, TFBlocks.DARK_PLANKS);
		buildBoats(getter, TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT, TFBlocks.TIME_PLANKS);
		buildBoats(getter, TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT, TFBlocks.TRANSFORMATION_PLANKS);
		buildBoats(getter, TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT, TFBlocks.MINING_PLANKS);
		buildBoats(getter, TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT, TFBlocks.SORTING_PLANKS);

		dryingRackBlock(getter, "canopy", TFBlocks.CANOPY_DRYING_RACK, TFBlocks.CANOPY_SLAB);
		dryingRackBlock(getter, "dark", TFBlocks.DARK_DRYING_RACK, TFBlocks.DARK_SLAB);
		dryingRackBlock(getter, "mangrove", TFBlocks.MANGROVE_DRYING_RACK, TFBlocks.MANGROVE_SLAB);
		dryingRackBlock(getter, "mining", TFBlocks.MINING_DRYING_RACK, TFBlocks.MINING_SLAB);
		dryingRackBlock(getter, "sorting", TFBlocks.SORTING_DRYING_RACK, TFBlocks.SORTING_SLAB);
		dryingRackBlock(getter, "time", TFBlocks.TIME_DRYING_RACK, TFBlocks.TIME_SLAB);
		dryingRackBlock(getter, "transformation", TFBlocks.TRANSFORMATION_DRYING_RACK, TFBlocks.TRANSFORMATION_SLAB);
		dryingRackBlock(getter, "twilight_oak", TFBlocks.TWILIGHT_OAK_DRYING_RACK, TFBlocks.TWILIGHT_OAK_SLAB);

		dryingRackBlock(getter, "oak", TFBlocks.OAK_DRYING_RACK, Blocks.OAK_SLAB);
		dryingRackBlock(getter, "spruce", TFBlocks.SPRUCE_DRYING_RACK, Blocks.SPRUCE_SLAB);
		dryingRackBlock(getter, "birch", TFBlocks.BIRCH_DRYING_RACK, Blocks.BIRCH_SLAB);
		dryingRackBlock(getter, "jungle", TFBlocks.JUNGLE_DRYING_RACK, Blocks.JUNGLE_SLAB);
		dryingRackBlock(getter, "acacia", TFBlocks.ACACIA_DRYING_RACK, Blocks.ACACIA_SLAB);
		dryingRackBlock(getter, "dark_oak", TFBlocks.DARK_OAK_DRYING_RACK, Blocks.DARK_OAK_SLAB);
		dryingRackBlock(getter, "crimson", TFBlocks.CRIMSON_DRYING_RACK, Blocks.CRIMSON_SLAB);
		dryingRackBlock(getter, "warped", TFBlocks.WARPED_DRYING_RACK, Blocks.WARPED_SLAB);
		dryingRackBlock(getter, "vangrove", TFBlocks.VANGROVE_DRYING_RACK, Blocks.MANGROVE_SLAB);
		dryingRackBlock(getter, "bamboo", TFBlocks.BAMBOO_DRYING_RACK, Blocks.BAMBOO_SLAB);
		dryingRackBlock(getter, "cherry", TFBlocks.CHERRY_DRYING_RACK, Blocks.CHERRY_SLAB);
	}

	private void nagastoneRecipes(HolderGetter<Item> getter) {
		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.SPIRAL_BRICKS, 8)
			.pattern("BSS")
			.pattern("BSS")
			.pattern("BBB")
			.define('B', Ingredient.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
			.define('S', Ingredient.of(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
			.unlockedBy("has_item", has(TFBlocks.SPIRAL_BRICKS))
			.save(this.output, locNaga("nagastone_spiral"));

		stairsBlock(getter, locNaga("nagastone_stairs_left"), TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE);
		stairsRightBlock(getter, locNaga("nagastone_stairs_right"), TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE);

		stairsBlock(getter, locNaga("mossy_nagastone_stairs_left"), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE);
		stairsRightBlock(getter, locNaga("mossy_nagastone_stairs_right"), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_ETCHED_NAGASTONE, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.ETCHED_NAGASTONE))
			.unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE))
			.save(this.output, locNaga("mossy_etched_nagastone"));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_NAGASTONE_PILLAR, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.NAGASTONE_PILLAR))
			.unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR))
			.save(this.output, locNaga("mossy_nagastone_pillar"));

		stairsBlock(getter, locNaga("cracked_nagastone_stairs_left"), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE);
		stairsRightBlock(getter, locNaga("cracked_nagastone_stairs_right"), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE);
	}

	private void castleRecipes(HolderGetter<Item> getter) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_CASTLE_BRICK, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.CASTLE_BRICK))
			.unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("mossy_castle_brick"));

		castleBlock(getter, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK);
		castleBlock(getter, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.THICK_CASTLE_BRICK);
		castleBlock(getter, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, 4)
			.pattern("##")
			.pattern("##")
			.define('#', Ingredient.of(TFBlocks.BOLD_CASTLE_BRICK_TILE))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("bold_castle_pillar_from_tile"));

		ShapedRecipeBuilder.shaped(getter, RecipeCategory.BUILDING_BLOCKS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, 6)
			.pattern("#H#")
			.pattern("#H#")
			.define('#', Ingredient.of(TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK))
			.define('H', Ingredient.of(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("encased_castle_pillar"));

		stairsBlock(getter, locCastle("bold_castle_brick_stairs"), TFBlocks.BOLD_CASTLE_BRICK_STAIRS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stairsBlock(getter, locCastle("castle_brick_stairs"), TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK);
		stairsBlock(getter, locCastle("cracked_castle_brick_stairs"), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK);
		stairsBlock(getter, locCastle("encased_castle_brick_stairs"), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stairsBlock(getter, locCastle("mossy_castle_brick_stairs"), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK);
		stairsBlock(getter, locCastle("worn_castle_brick_stairs"), TFBlocks.WORN_CASTLE_BRICK_STAIRS, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK);
	}

	private void fieryConversions(HolderGetter<Item> getter) {
		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, TFItems.FIERY_INGOT)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.FIERY_VIAL)))
			.requires(Ingredient.of(getter.getOrThrow(Tags.Items.INGOTS_IRON)))
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group("fiery_ingot")
			.save(this.output, locEquip("fiery_ingot_crafting"));

		fieryConversion(getter, TFItems.FIERY_HELMET, Items.IRON_HELMET, 5);
		fieryConversion(getter, TFItems.FIERY_CHESTPLATE, Items.IRON_CHESTPLATE, 8);
		fieryConversion(getter, TFItems.FIERY_LEGGINGS, Items.IRON_LEGGINGS, 7);
		fieryConversion(getter, TFItems.FIERY_BOOTS, Items.IRON_BOOTS, 4);

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.COMBAT, TFItems.FIERY_SWORD)
			.requires(Items.IRON_SWORD)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.FIERY_VIAL)), 2)
			.requires(Ingredient.of(getter.getOrThrow(Tags.Items.RODS_BLAZE)))
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group("fiery_sword")
			.save(this.output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_SWORD).getPath()));

		ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE)
			.requires(Items.IRON_PICKAXE)
			.requires(Ingredient.of(getter.getOrThrow(TFItemTags.FIERY_VIAL)), 3)
			.requires(Ingredient.of(getter.getOrThrow(Tags.Items.RODS_BLAZE)), 2)
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group("fiery_pickaxe")
			.save(this.output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_PICKAXE).getPath()));
	}

	private <T extends AbstractCookingRecipe> void cookingRecipes(String processName, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_MEEF), RecipeCategory.FOOD, CookingBookCategory.FOOD, TFItems.COOKED_MEEF, 0.35F, smeltingTime, factory).unlockedBy("has_food", has(TFItems.RAW_MEEF)).save(this.output, this.createKey("food/" + processName + "_meef"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_VENISON), RecipeCategory.FOOD, CookingBookCategory.FOOD, TFItems.COOKED_VENISON, 0.35F, smeltingTime, factory).unlockedBy("has_food", has(TFItems.RAW_VENISON)).save(this.output, this.createKey("food/" + processName + "_venison"));
	}

	private <T extends AbstractCookingRecipe> void ingotRecipes(String processName, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.ARMOR_SHARD_CLUSTER), RecipeCategory.MISC, CookingBookCategory.MISC, TFItems.KNIGHTMETAL_INGOT, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.ARMOR_SHARD_CLUSTER)).group("knightmetal_ingot").save(this.output, this.createKey("material/" + processName + "_knightmetal_ingot"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_IRONWOOD), RecipeCategory.MISC, CookingBookCategory.MISC, TFItems.IRONWOOD_INGOT, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.RAW_IRONWOOD)).group("ironwood_ingot").save(this.output, this.createKey("material/" + processName + "_ironwood_ingot"));
	}

	private <T extends AbstractCookingRecipe> void oreberryRecipes(String processName, AbstractCookingRecipe.Factory<T> factory, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.IRON_BERRY), RecipeCategory.MISC, CookingBookCategory.MISC, Items.IRON_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.IRON_BERRY)).group("iron_nugget").save(this.output, this.createKey("material/" + processName + "_iron_nugget"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.GOLD_BERRY), RecipeCategory.MISC, CookingBookCategory.MISC, Items.GOLD_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.GOLD_BERRY)).group("gold_nugget").save(this.output, this.createKey("material/" + processName + "_gold_nugget"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.COPPER_BERRY), RecipeCategory.MISC, CookingBookCategory.MISC, TFItems.COPPER_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.COPPER_BERRY)).group("copper_nugget").save(this.output, this.createKey("material/" + processName + "_copper_nugget"));
	}

	private void crackedWoodRecipes() {
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(TFBlocks.TOWERWOOD), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_TOWERWOOD, 0.1F, 100).unlockedBy("has_item", has(TFBlocks.TOWERWOOD)).save(this.output, this.createKey("wood/" + "smoked" + "_cracked_towerwood"));
	}

	private void crackedStoneRecipes() {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.NAGASTONE_PILLAR), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.MISC, TFBlocks.CRACKED_NAGASTONE_PILLAR, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR)).save(this.output, this.createKey("nagastone/" + "smelted" + "_cracked_nagastone_pillar"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.ETCHED_NAGASTONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.MISC, TFBlocks.CRACKED_ETCHED_NAGASTONE, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE)).save(this.output, this.createKey("nagastone/" + "smelted" + "_cracked_etched_nagastone"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.MAZESTONE_BRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.MISC, TFBlocks.CRACKED_MAZESTONE, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK)).save(this.output, this.createKey("maze_stone/" + "smelted" + "_maze_stone_cracked"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.CASTLE_BRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.MISC, TFBlocks.CRACKED_CASTLE_BRICK, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK)).save(this.output, this.createKey("castleblock/" + "smelted" + "_cracked_castle_brick"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.UNDERBRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.MISC, TFBlocks.CRACKED_UNDERBRICK, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.UNDERBRICK)).save(this.output, this.createKey("smelted" + "_cracked_underbrick"));
	}

	private void dryingRecipes(HolderGetter<Item> getter) {
		DryingRecipeBuilder.drying(Items.ROTTEN_FLESH, TFItems.MONSTER_JERKY)
			.unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
			.save(this.output, this.createKey("monster_jerky"));
		DryingRecipeBuilder.drying(Items.MUTTON, TFItems.MUTTON_JERKY)
			.unlockedBy("has_mutton", has(Items.MUTTON))
			.save(this.output, this.createKey("mutton_jerky"));
		DryingRecipeBuilder.drying(Items.PORKCHOP, TFItems.PORK_JERKY)
			.unlockedBy("has_porkchop", has(Items.PORKCHOP))
			.save(this.output, this.createKey("pork_jerky"));
		DryingRecipeBuilder.drying(Items.BEEF, TFItems.BEEF_JERKY)
			.unlockedBy("has_beef", has(Items.BEEF))
			.save(this.output, this.createKey("beef_jerky"));
		DryingRecipeBuilder.drying(Items.CHICKEN, TFItems.CHICKEN_JERKY)
			.unlockedBy("has_chicken", has(Items.CHICKEN))
			.save(this.output, this.createKey("chicken_jerky"));
		DryingRecipeBuilder.drying(Items.COD, TFItems.COD_JERKY)
			.unlockedBy("has_cod", has(Items.COD))
			.save(this.output, this.createKey("cod_jerky"));
		DryingRecipeBuilder.drying(Items.SALMON, TFItems.SALMON_JERKY)
			.unlockedBy("has_salmon", has(Items.SALMON))
			.save(this.output, this.createKey("salmon_jerky"));
		DryingRecipeBuilder.drying(Items.RABBIT, TFItems.RABBIT_JERKY)
			.unlockedBy("has_rabbit", has(Items.RABBIT))
			.save(this.output, this.createKey("rabbit_jerky"));
		DryingRecipeBuilder.drying(Items.COOKED_PORKCHOP, TFItems.MEEF_JERKY)
			.unlockedBy("has_cooked_porkchop", has(Items.COOKED_PORKCHOP))
			.save(this.output, this.createKey("meef_jerky"));
		DryingRecipeBuilder.drying(Items.TROPICAL_FISH, TFItems.TROPICAL_FISH_JERKY)
			.unlockedBy("has_tropical_fish", has(Items.TROPICAL_FISH))
			.save(this.output, this.createKey("tropical_fish_jerky"));
		DryingRecipeBuilder.drying(Items.PUFFERFISH, TFItems.FUGU_JERKY)
			.unlockedBy("has_pufferfish", has(Items.PUFFERFISH))
			.save(this.output, this.createKey("fugu_jerky"));
		DryingRecipeBuilder.drying(TFItems.RAW_VENISON, TFItems.VENISON_JERKY)
			.unlockedBy("has_item", has(TFItems.RAW_VENISON))
			.save(this.output, this.createKey("venison_jerky"));
		DryingRecipeBuilder.drying(Items.BREAD, TFItems.STALE_BREAD)
			.unlockedBy("has_bread", has(Items.BREAD))
			.save(this.output, this.createKey("stale_bread"));
		DryingRecipeBuilder.drying(Items.SLIME_BALL, TFItems.GELATINOUS_SLIME_DROP)
			.unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
			.save(this.output, this.createKey("gelatinous_slime_drop"));
		DryingRecipeBuilder.drying(TFItems.MAZE_SLIME_BALL, TFItems.GELATINOUS_MAZE_SLIME_DROP)
			.unlockedBy("has_maze_slime_ball", has(TFItems.MAZE_SLIME_BALL))
			.save(this.output, this.createKey("gelatinous_maze_slime_drop"));
		DryingRecipeBuilder.drying(Ingredient.of(Items.LEATHER), TFItems.TANNED_LEATHER)
			.unlockedBy("has_leather", has(Items.LEATHER))
			.save(this.output, this.createKey("tanned_leather"));

		DryingRecipeBuilder.drying(Ingredient.of(getter.getOrThrow(ItemTags.SAPLINGS)), Items.DEAD_BUSH, 6)
			.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
			.save(this.output, this.createKey("drying/sapling_to_dead_bush"));
		DryingRecipeBuilder.drying(Items.MUD, Items.CLAY, 6)
			.unlockedBy("has_mud", has(Items.MUD))
			.save(this.output, this.createKey("drying/mud_to_clay"));
		DryingRecipeBuilder.drying(Items.WET_SPONGE, Items.SPONGE, 2)
			.unlockedBy("has_wet_sponge", has(Items.WET_SPONGE))
			.save(this.output, this.createKey("drying/sponge"));
		DryingRecipeBuilder.drying(Items.KELP, Items.DRIED_KELP, 3)
			.unlockedBy("has_kelp", has(Items.KELP))
			.save(this.output, this.createKey("drying/kelp"));
	}

	private static Ingredient potionIngredient(Holder<Potion> potion) {
		return DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(potion), Items.POTION);
	}
}
