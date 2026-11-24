package twilightforest.datagen.data.reloadable;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.datagen.data.custom.CartesianShapedRecipeBuilder;
import twilightforest.datagen.data.custom.CartesianShapelessRecipeBuilder;
import twilightforest.datagen.data.custom.DryingRecipeBuilder;
import twilightforest.datagen.data.custom.ComponentSmithingRecipeBuilder;
import twilightforest.datagen.data.custom.TravellersGearComponentModifierBuilder;
import twilightforest.datagen.data.custom.UncraftingGenerator;
import twilightforest.datagen.helpers.CraftingDataHelper;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.function.Predicate;

public class TFRecipeProvider extends CraftingDataHelper {

	private final String[] coralSpecies = {"tube", "brain", "bubble", "fire", "horn"};
	private final String[] coralTypes = {"", "_block", "_fan"};

	public TFRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
		super(recipeOutput, advancementOutput);
	}

	@Override
	public void buildRecipes() {
		UncraftingGenerator.buildRecipes(items, this.output);

		stonecuttingRecipes(items);
		blockCompressionRecipes(items);
		equipmentRecipes(items);
		emptyMapRecipes(items);
		woodRecipes(items);
		dryingRecipes(items);
		fieryConversions(items);

		nagastoneRecipes(items);
		darkTowerRecipes(items);
		castleRecipes(items);

		bannerPattern(items, "naga_banner_pattern", TFBlocks.NAGA_TROPHY, TFItems.NAGA_BANNER_PATTERN);
		bannerPattern(items, "lich_banner_pattern", TFBlocks.LICH_TROPHY, TFItems.LICH_BANNER_PATTERN);
		bannerPattern(items, "minoshroom_banner_pattern", TFBlocks.MINOSHROOM_TROPHY, TFItems.MINOSHROOM_BANNER_PATTERN);
		bannerPattern(items, "hydra_banner_pattern", TFBlocks.HYDRA_TROPHY, TFItems.HYDRA_BANNER_PATTERN);
		bannerPattern(items, "knight_phantom_banner_pattern", TFBlocks.KNIGHT_PHANTOM_TROPHY, TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
		bannerPattern(items, "ur_ghast_banner_pattern", TFBlocks.UR_GHAST_TROPHY, TFItems.UR_GHAST_BANNER_PATTERN);
		bannerPattern(items, "alpha_yeti_banner_pattern", TFBlocks.ALPHA_YETI_TROPHY, TFItems.ALPHA_YETI_BANNER_PATTERN);
		bannerPattern(items, "snow_queen_banner_pattern", TFBlocks.SNOW_QUEEN_TROPHY, TFItems.SNOW_QUEEN_BANNER_PATTERN);
//		bannerPattern(items, "questing_ram_banner_pattern", TFBlocks.QUEST_RAM_TROPHY, TFItems.QUEST_RAM_BANNER_PATTERN);

		slabBlock(items, "aurora_slab", TFBlocks.AURORA_SLAB, TFBlocks.AURORA_BLOCK);
		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.AURORA_PILLAR, 2)
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.AURORA_BLOCK))
			.unlockedBy("has_slab", has(TFBlocks.AURORA_SLAB))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.IRON_LADDER, 3)
			.pattern("-#-")
			.pattern("-#-")
			.define('#', Ingredient.of(Blocks.IRON_BARS))
			.define('-', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_iron_bars", has(Blocks.IRON_BARS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.WROUGHT_IRON_FENCE, 3)
			.pattern("###")
			.pattern("###")
			.define('#', TFItemTags.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(TFItemTags.WROUGHT_IRON_INGOTS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA, 2)
			.pattern("###")
			.pattern(" # ")
			.define('#', TFItemTags.WROUGHT_IRON_INGOTS)
			.unlockedBy("has_wrought_iron", has(TFItemTags.WROUGHT_IRON_INGOTS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.ROPE, 8)
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Ingredient.of(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_root_strand", has(TFBlocks.ROOT_STRAND))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW, 4)
			.pattern("GPG")
			.pattern("PPP")
			.pattern("GPG")
			.define('G', Ingredient.of(items.getOrThrow(Tags.BlockItems.GLASS_BLOCKS.item())))
			.define('P', Ingredient.of(TFBlocks.CANOPY_PLANKS))
			.unlockedBy("has_planks", has(TFBlocks.CANOPY_PLANKS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.CANOPY_WINDOW_PANE, 16)
			.pattern("GGG")
			.pattern("GGG")
			.define('G', Ingredient.of(TFBlocks.CANOPY_WINDOW))
			.unlockedBy("has_windows", has(TFBlocks.CANOPY_WINDOW))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFItems.MASON_JAR, 4)
			.pattern("GLG")
			.pattern("G G")
			.pattern("GGG")
			.define('G', Ingredient.of(Items.GLASS))
			.define('L', Ingredient.of(TFBlocks.TWILIGHT_OAK_LOG))
			.unlockedBy("has_tf_oak", has(TFBlocks.TWILIGHT_OAK_LOG.value()))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_JAR)
			.requires(Ingredient.of(TFBlocks.FIREFLY))
			.requires(Ingredient.of(TFItems.MASON_JAR))
			.unlockedBy("has_item", has(TFBlocks.FIREFLY))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_SPAWNER)
			.requires(Ingredient.of(TFBlocks.FIREFLY_JAR))
			.requires(Ingredient.of(TFBlocks.FIREFLY))
			.requires(Ingredient.of(Blocks.POPPY))
			.unlockedBy("has_jar", has(TFBlocks.FIREFLY_JAR))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, TFBlocks.CICADA_JAR)
			.requires(Ingredient.of(TFBlocks.CICADA))
			.requires(Ingredient.of(TFItems.MASON_JAR))
			.unlockedBy("has_item", has(TFBlocks.CICADA))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.DYE.magenta())
			.requires(Ingredient.of(TFBlocks.HUGE_WATER_LILY))
			.unlockedBy("has_item", has(TFBlocks.HUGE_WATER_LILY))
			.save(this.output, this.createKey("waterlily_to_magenta"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.DYE.red())
			.requires(Ingredient.of(TFBlocks.THORN_ROSE))
			.unlockedBy("has_item", has(TFBlocks.THORN_ROSE))
			.save(this.output, this.createKey("thorn_rose_to_red"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.STICK)
			.requires(Ingredient.of(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_item", has(TFBlocks.ROOT_STRAND))
			.group("sticks")
			.save(this.output, this.createKey("root_stick"));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
			.pattern("∴")
			.pattern("|")
			.define('∴', Ingredient.of(TFItems.TORCHBERRIES))
			.define('|', Tags.Items.RODS_WOODEN)
			.unlockedBy("has_item", has(TFItems.TORCHBERRIES))
			.save(this.output, this.createKey("berry_torch"));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.UNCRAFTING_TABLE)
			.pattern("###")
			.pattern("#X#")
			.pattern("###")
			.define('#', Blocks.CRAFTING_TABLE)
			.define('X', TFItems.MAZE_MAP_FOCUS)
			.unlockedBy("has_uncrafting_table", has(TFBlocks.UNCRAFTING_TABLE))
			.save(this.output.withConditions(UncraftingTableCondition.INSTANCE), this.createKey("uncrafting_table"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, TFItems.MOSS_SOUP)
			.requires(TFBlocks.MOSS_PATCH)
			.requires(Items.BOWL)
			.requires(this.potionIngredient(Potions.WATER))
			.unlockedBy("has_moss", this.has(TFBlocks.MOSS_PATCH))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, TFItems.BERRY_MEDLEY)
			.requires(Items.BOWL)
			.requires(TFItems.RASPBERRY)
			.requires(TFItems.BLUEBERRY)
			.requires(TFItems.BLACKBERRY)
			.requires(TFItems.MALOBERRY)
			.unlockedBy("has_raspberry", this.has(TFItems.RASPBERRY))
			.unlockedBy("has_blueberry", this.has(TFItems.BLUEBERRY))
			.unlockedBy("has_blackberry", this.has(TFItems.BLACKBERRY))
			.unlockedBy("has_maloberry", this.has(TFItems.MALOBERRY))
			.save(this.output);

		cookingRecipes("smelted", SmeltingRecipe::new, 200);
		cookingRecipes("smoked", SmokingRecipe::new, 100);
		cookingRecipes("campfired", CampfireCookingRecipe::new, 600);

		ingotRecipes("smelted", SmeltingRecipe::new, 200);
		ingotRecipes("blasted", BlastingRecipe::new, 100);

		oreberryRecipes("smelted", SmeltingRecipe::new, 200);
		oreberryRecipes("blasted", BlastingRecipe::new, 100);

		crackedWoodRecipes();
		crackedStoneRecipes();
		dyedRecipes();

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.CHISELED_CANOPY_BOOKSHELF)
			.pattern("---")
			.pattern("sss")
			.pattern("---")
			.define('-', TFBlocks.CANOPY_PLANKS)
			.define('s', TFBlocks.CANOPY_SLAB)
			.unlockedBy("has_item", has(TFBlocks.CANOPY_SLAB))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.CANOPY_BOOKSHELF)
			.pattern("---")
			.pattern("B B")
			.pattern("---")
			.define('-', TFBlocks.CANOPY_PLANKS)
			.define('B', Items.BOOK)
			.unlockedBy("has_item", has(TFBlocks.CANOPY_PLANKS))
			.save(this.output);

//		ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, TFBlocks.CANDELABRA)
//			.pattern("III")
//			.pattern(" W ")
//			.define('W', TFBlocks.WROUGHT_IRON_FENCE)
//			.define('I', Tags.Items.INGOTS_IRON)
//			.unlockedBy("has_item", has(TFBlocks.WROUGHT_IRON_FENCE))
//			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.ARMOR_SHARD_CLUSTER)
			.requires(Ingredient.of(TFItems.ARMOR_SHARD), 9)
			.unlockedBy("has_item", has(TFItems.ARMOR_SHARD))
			.save(this.output, this.createKey("material/" + TFItems.ARMOR_SHARD_CLUSTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_UNDERBRICK, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.UNDERBRICK))
			.unlockedBy("has_item", has(TFBlocks.UNDERBRICK))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_MAZESTONE, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.MAZESTONE_BRICK))
			.unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK))
			.save(this.output, this.createKey("maze_stone/mossy_mazestone"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.CARMINITE)
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

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.RAW_IRONWOOD, 2)
			.requires(Ingredient.of(TFItems.LIVEROOT))
			.requires(Ingredient.of(Items.RAW_IRON))
			.requires(Tags.Items.NUGGETS_GOLD)
			.unlockedBy("has_item", has(TFItems.LIVEROOT))
			.save(this.output, this.createKey("material/" + TFItems.RAW_IRONWOOD.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.RAINY_CLOUD, 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD))
			.define('b', Ingredient.of(Items.WATER_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD))
			.save(this.output, this.createKey("rainy_cloud"));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.SNOWY_CLOUD, 8)
			.pattern("ccc")
			.pattern("cbc")
			.pattern("ccc")
			.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD))
			.define('b', Ingredient.of(Items.POWDER_SNOW_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD))
			.save(this.output, this.createKey("snowy_cloud"));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.MYSTIC_CROWN)
			.pattern("ttt")
			.pattern("t t")
			.pattern("ttt")
			.define('t', Ingredient.of(TFItems.CROWN_SPLINTER))
			.unlockedBy("has_item", has(TFItems.CROWN_SPLINTER))
			.save(this.output);
	}

	private void darkTowerRecipes(HolderGetter<Item> items) {
		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.ENCASED_FIRE_JET)
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("uuu")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('^', Ingredient.of(TFBlocks.FIRE_JET))
			.define('u', Ingredient.of(Items.LAVA_BUCKET))
			.unlockedBy("has_item", has(TFBlocks.FIRE_JET))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.ENCASED_SMOKER)
			.pattern("#∴#")
			.pattern("∴^∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('^', Ingredient.of(TFBlocks.SMOKER))
			.unlockedBy("has_item", has(TFBlocks.SMOKER))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.CARMINITE_BUILDER)
			.pattern("#6#")
			.pattern("6o6")
			.pattern("#6#")
			.define('6', TFItemTags.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('o', Ingredient.of(Blocks.DISPENSER))
			.unlockedBy("has_item", has(TFItemTags.CARMINITE_GEMS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.CARMINITE_REACTOR)
			.pattern("#6#")
			.pattern("6%6")
			.pattern("#6#")
			.define('6', TFItemTags.CARMINITE_GEMS)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('%', Tags.BlockItems.ORES_REDSTONE.item())
			.unlockedBy("has_item", has(TFItemTags.CARMINITE_GEMS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.REAPPEARING_BLOCK, 2)
			.pattern("#∴#")
			.pattern("∴6∴")
			.pattern("#∴#")
			.define('∴', Tags.Items.DUSTS_REDSTONE)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('6', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, TFBlocks.VANISHING_BLOCK, 8)
			.pattern("#w#")
			.pattern("w6w")
			.pattern("#w#")
			.define('w', TFItemTags.TOWERWOOD)
			.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD))
			.define('6', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_TOWERWOOD)
			.requires(Ingredient.of(TFBlocks.TOWERWOOD))
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.unlockedBy("has_item", has(TFBlocks.TOWERWOOD))
			.save(this.output, this.createKey("wood/" + TFBlocks.MOSSY_TOWERWOOD.getId().getPath()));

	}

	private void equipmentRecipes(HolderGetter<Item> items) {
		HolderGetter<Enchantment> lookup = output.lookup(Registries.ENCHANTMENT);
		bootsItem(items, TFItems.IRONWOOD_BOOTS, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.FEATHER_FALLING, 1)));
		leggingsItem(items, TFItems.IRONWOOD_LEGGINGS, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 1)));
		chestplateItem(items, TFItems.IRONWOOD_CHESTPLATE, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 1)));
		helmetItem(items, TFItems.IRONWOOD_HELMET, TFItemTags.IRONWOOD_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.AQUA_AFFINITY, 1)));
		swordItem(items, TFItems.IRONWOOD_SWORD, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.KNOCKBACK, 1)));
		pickaxeItem(items, TFItems.IRONWOOD_PICKAXE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.EFFICIENCY, 1)));
		axeItem(items, TFItems.IRONWOOD_AXE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.FORTUNE, 1)));
		shovelItem(items, TFItems.IRONWOOD_SHOVEL, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.UNBREAKING, 1)));
		hoeItem(items, TFItems.IRONWOOD_HOE, TFItemTags.IRONWOOD_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.EFFICIENCY, 1)));

		bootsItem(items, TFItems.STEELEAF_BOOTS, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.FEATHER_FALLING, 2)));
		leggingsItem(items, TFItems.STEELEAF_LEGGINGS, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.FIRE_PROTECTION, 2)));
		chestplateItem(items, TFItems.STEELEAF_CHESTPLATE, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.BLAST_PROTECTION, 2)));
		helmetItem(items, TFItems.STEELEAF_HELMET, TFItemTags.STEELEAF_INGOTS, this.buildEnchants(lookup, Pair.of(Enchantments.PROJECTILE_PROTECTION, 2)));
		swordItem(items, TFItems.STEELEAF_SWORD, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.LOOTING, 2)));
		pickaxeItem(items, TFItems.STEELEAF_PICKAXE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.FORTUNE, 2)));
		axeItem(items, TFItems.STEELEAF_AXE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.EFFICIENCY, 2)));
		shovelItem(items, TFItems.STEELEAF_SHOVEL, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.EFFICIENCY, 2)));
		hoeItem(items, TFItems.STEELEAF_HOE, TFItemTags.STEELEAF_INGOTS, Tags.Items.RODS_WOODEN, this.buildEnchants(lookup, Pair.of(Enchantments.FORTUNE, 2)));

		bootsItem(items, TFItems.ARCTIC_BOOTS, TFItemTags.ARCTIC_FUR);
		chestplateItem(items, TFItems.ARCTIC_CHESTPLATE, TFItemTags.ARCTIC_FUR);
		helmetItem(items, TFItems.ARCTIC_HELMET, TFItemTags.ARCTIC_FUR);
		leggingsItem(items, TFItems.ARCTIC_LEGGINGS, TFItemTags.ARCTIC_FUR);

		bootsItem(items, TFItems.KNIGHTMETAL_BOOTS, TFItemTags.KNIGHTMETAL_INGOTS);
		chestplateItem(items, TFItems.KNIGHTMETAL_CHESTPLATE, TFItemTags.KNIGHTMETAL_INGOTS);
		helmetItem(items, TFItems.KNIGHTMETAL_HELMET, TFItemTags.KNIGHTMETAL_INGOTS);
		leggingsItem(items, TFItems.KNIGHTMETAL_LEGGINGS, TFItemTags.KNIGHTMETAL_INGOTS);
		pickaxeItem(items, TFItems.KNIGHTMETAL_PICKAXE, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		swordItem(items, TFItems.KNIGHTMETAL_SWORD, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		axeItem(items, TFItems.KNIGHTMETAL_AXE, TFItemTags.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.FIERY_BOOTS)
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_boots")
			.save(this.output, locEquip(TFItems.FIERY_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.FIERY_LEGGINGS)
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_leggings")
			.save(this.output, locEquip(TFItems.FIERY_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.FIERY_CHESTPLATE)
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_chestplate")
			.save(this.output, locEquip(TFItems.FIERY_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.FIERY_HELMET)
			.pattern("###")
			.pattern("# #")
			.define('#', TFItemTags.FIERY_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_helmet")
			.save(this.output, locEquip(TFItems.FIERY_HELMET.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', TFItemTags.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_pickaxe")
			.save(this.output, locEquip(TFItems.FIERY_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.FIERY_SWORD)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', TFItemTags.FIERY_INGOTS)
			.define('X', Tags.Items.RODS_BLAZE)
			.unlockedBy("has_item", has(TFItemTags.FIERY_INGOTS))
			.group("fiery_sword")
			.save(this.output, locEquip(TFItems.FIERY_SWORD.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.NAGA_CHESTPLATE, 1, this.buildEnchants(lookup, Pair.of(Enchantments.FIRE_PROTECTION, 3)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(this.output, locEquip(TFItems.NAGA_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.NAGA_LEGGINGS, 1, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 3)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.NAGA_SCALE)
			.unlockedBy("has_item", has(TFItems.NAGA_SCALE))
			.save(this.output, locEquip(TFItems.NAGA_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_HELMET, 1, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_HELMET.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_CHESTPLATE, 1, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_CHESTPLATE.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_LEGGINGS, 1, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 2)).build()))
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_LEGGINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, new ItemStackTemplate(TFItems.YETI_BOOTS, 1, this.buildEnchants(lookup, Pair.of(Enchantments.PROTECTION, 2), Pair.of(Enchantments.FEATHER_FALLING, 4)).build()))
			.pattern("# #")
			.pattern("# #")
			.define('#', TFItems.ALPHA_YETI_FUR)
			.unlockedBy("has_item", has(TFItems.ALPHA_YETI_FUR))
			.save(this.output, locEquip(TFItems.YETI_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TOOLS, TFItems.GIANT_PICKAXE)
			.pattern("###")
			.pattern(" X ")
			.pattern(" X ")
			.define('#', TFBlocks.GIANT_COBBLESTONE)
			.define('X', TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, locEquip(TFItems.GIANT_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TOOLS, TFItems.GIANT_SWORD)
			.pattern("#")
			.pattern("#")
			.pattern("X")
			.define('#', TFBlocks.GIANT_COBBLESTONE)
			.define('X', TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, locEquip(TFItems.GIANT_SWORD.getId().getPath()));

		this.travellersGearRecipes(items);

		charmRecipe(items, "charm_of_keeping_2", TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_1);
		charmRecipe(items, "charm_of_keeping_3", TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_KEEPING_2);
		charmRecipe(items, "charm_of_life_2", TFItems.CHARM_OF_LIFE_2, TFItems.CHARM_OF_LIFE_1);

		SpecialRecipeBuilder.special(() -> EmperorsClothRecipe.INSTANCE).save(this.output, this.createKey("emperors_cloth_recipe"));
		SpecialRecipeBuilder.special(() -> CasketRepairRecipe.INSTANCE).save(this.output, this.createKey("casket_repair_recipe"));
		SpecialRecipeBuilder.special(() -> TravellersVestGlovesMergeRecipe.INSTANCE).save(this.output, this.createKey("travellers_vest_gloves_merge_recipe"));
		TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(TFItems.FILLED_MAGIC_MAP), Ingredient.of(TFItems.MAGIC_MAP), new ItemStackTemplate(TFItems.FILLED_MAGIC_MAP))
			.addMaterialCountToOutput()
			.setMaterialCount(TransmuteRecipe.FULL_RANGE_MATERIAL_COUNT)
			.group("map_cloning")
			.unlockedBy("has_filled_map", this.has(TFItems.FILLED_MAGIC_MAP))
			.save(this.output, this.createKey("magic_map_cloning"));
		TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(TFItems.FILLED_MAZE_MAP), Ingredient.of(TFItems.MAZE_MAP), new ItemStackTemplate(TFItems.FILLED_MAZE_MAP))
			.addMaterialCountToOutput()
			.setMaterialCount(TransmuteRecipe.FULL_RANGE_MATERIAL_COUNT)
			.group("map_cloning")
			.unlockedBy("has_filled_map", this.has(TFItems.FILLED_MAZE_MAP))
			.save(this.output, this.createKey("maze_map_cloning"));
		TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(TFItems.FILLED_ORE_MAP), Ingredient.of(TFItems.ORE_MAP), new ItemStackTemplate(TFItems.FILLED_ORE_MAP))
			.addMaterialCountToOutput()
			.setMaterialCount(TransmuteRecipe.FULL_RANGE_MATERIAL_COUNT)
			.group("map_cloning")
			.unlockedBy("has_filled_map", this.has(TFItems.FILLED_ORE_MAP))
			.save(this.output, this.createKey("ore_map_cloning"));
		SpecialRecipeBuilder.special(() -> new SimpleRepairRecipe(Ingredient.of(items.getOrThrow(TFItemTags.SCEPTERS)), Ingredient.of(items.getOrThrow(TFItemTags.SCEPTER_MAX_REPAIR_ITEMS)), 1, 99))
			.unlockedBy("has_scepter", has(TFItemTags.SCEPTERS))
			.save(this.output, locEquip("exanimate_essence_scepter_repair"));
		SpecialRecipeBuilder.special(() -> new SimpleRepairRecipe(Ingredient.of(TFItems.MOONWORM_QUEEN), Ingredient.of(items.getOrThrow(TFItemTags.MOONWORM_QUEEN_REPAIR_ITEMS)), 4, 64))
			.unlockedBy("has_queen", has(TFItems.MOONWORM_QUEEN))
			.save(this.output, locEquip("moonworm_queen_repair"));

		ComponentSmithingRecipeBuilder
			.smithing(null, Ingredient.of(items.getOrThrow(TFItemTags.EMPERORS_CLOTH_APPLICABLE)), Ingredient.of(TFItems.EMPERORS_CLOTH.get()), RecipeCategory.MISC)
			.attachData(TFDataComponents.EMPERORS_CLOTH::value, Unit.INSTANCE)
			.unlocks("has_cloth", has(TFItems.EMPERORS_CLOTH))
			.save(this.output, this.createKey("emperors_cloth_smithing"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 64)
			.requires(TFBlocks.GIANT_COBBLESTONE)
			.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE))
			.save(this.output, this.createKey(TFBlocks.GIANT_COBBLESTONE.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.COBBLESTONE).getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_PLANKS, 64)
			.requires(TFBlocks.GIANT_LOG)
			.unlockedBy("has_item", has(TFBlocks.GIANT_LOG))
			.save(this.output, this.createKey(TFBlocks.GIANT_LOG.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_PLANKS).getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LEAVES, 64)
			.requires(TFBlocks.GIANT_LEAVES)
			.unlockedBy("has_item", has(TFBlocks.GIANT_LEAVES))
			.save(this.output, this.createKey(TFBlocks.GIANT_LEAVES.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_LEAVES).getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, Blocks.OBSIDIAN, 64)
			.requires(TFBlocks.GIANT_OBSIDIAN)
			.unlockedBy("has_item", has(TFBlocks.GIANT_OBSIDIAN))
			.save(this.output, this.createKey(TFBlocks.GIANT_OBSIDIAN.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OBSIDIAN).getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.COMBAT, TFItems.BLOCK_AND_CHAIN)
			.requires(Ingredient.of(items.getOrThrow(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)))
			.requires(Ingredient.of(items.getOrThrow(TFItemTags.KNIGHTMETAL_INGOTS)), 3)
			.requires(Ingredient.of(TFItems.KNIGHTMETAL_RING))
			.unlockedBy("has_block", has(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL))
			.unlockedBy("has_ingot", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING))
			.save(this.output, locEquip(TFItems.BLOCK_AND_CHAIN.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.TANNIN)
			.requires(this.potionIngredient(Potions.WATER))
			.requires(TFBlocks.TWILIGHT_OAK_SAPLING)
			.requires(TFBlocks.ROOT_STRAND)
			.requires(TFBlocks.TWILIGHT_OAK_LEAVES)
			.unlockedBy("has_block", this.has(TFBlocks.TWILIGHT_OAK_SAPLING))
			.unlockedBy("has_block", this.has(TFBlocks.ROOT_STRAND))
			.unlockedBy("has_block", this.has(TFBlocks.TWILIGHT_OAK_LEAVES))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.TREATED_LEATHER)
			.requires(TFItems.TANNIN)
			.requires(Tags.Items.LEATHERS)
			.unlockedBy("has_tannin", this.has(TFItems.TANNIN))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, TFItems.KNIGHTMETAL_RING)
			.pattern(" - ")
			.pattern("- -")
			.pattern(" - ")
			.define('-', TFItemTags.KNIGHTMETAL_INGOTS)
			.unlockedBy("has_item", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.save(this.output, locEquip(TFItems.KNIGHTMETAL_RING.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, TFItems.KNIGHTMETAL_SHIELD)
			.pattern("-#")
			.pattern("-o")
			.pattern("-#")
			.define('-', TFItemTags.KNIGHTMETAL_INGOTS)
			.define('#', TFItemTags.TOWERWOOD)
			.define('o', Ingredient.of(TFItems.KNIGHTMETAL_RING))
			.unlockedBy("has_ingot", has(TFItemTags.KNIGHTMETAL_INGOTS))
			.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING))
			.save(this.output, locEquip(TFItems.KNIGHTMETAL_SHIELD.getId().getPath()));

		SpecialRecipeBuilder.special(() -> new ComplexRepairRecipe(Ingredient.of(TFItems.LIFEDRAIN_SCEPTER),
				List.of(Ingredient.of(Items.FERMENTED_SPIDER_EYE)), 9))
			.unlockedBy("has_scepter", has(TFItems.LIFEDRAIN_SCEPTER))
			.save(this.output, locEquip(TFItems.LIFEDRAIN_SCEPTER.getId().getPath()));

		SpecialRecipeBuilder.special(() -> new ComplexRepairRecipe(Ingredient.of(TFItems.FORTIFICATION_SCEPTER),
				List.of(Ingredient.of(Items.GOLDEN_APPLE)), 9))
			.unlockedBy("has_scepter", has(TFItems.FORTIFICATION_SCEPTER))
			.save(this.output, locEquip(TFItems.FORTIFICATION_SCEPTER.getId().getPath()));

		SpecialRecipeBuilder.special(() -> new ComplexRepairRecipe(Ingredient.of(TFItems.TWILIGHT_SCEPTER),
				List.of(Ingredient.of(items.getOrThrow(Tags.Items.ENDER_PEARLS))), 9))
			.unlockedBy("has_scepter", has(TFItems.TWILIGHT_SCEPTER))
			.save(this.output, locEquip(TFItems.TWILIGHT_SCEPTER.getId().getPath()));

		SpecialRecipeBuilder.special(() -> new ComplexRepairRecipe(Ingredient.of(TFItems.ZOMBIE_SCEPTER),
				List.of(
					Ingredient.of(Items.ROTTEN_FLESH),
					CompoundIngredient.of(
						DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRENGTH), Items.POTION),
						DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_STRENGTH), Items.POTION),
						DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_STRENGTH), Items.POTION)
					)), 9))
			.unlockedBy("has_scepter", has(TFItems.ZOMBIE_SCEPTER))
			.save(this.output, locEquip(TFItems.ZOMBIE_SCEPTER.getId().getPath()));

		this.travellersModifierRecipes(items, output.lookup(TFRegistries.Keys.TRAVELLERS_MODIFIERS));
	}

	private void travellersModifierRecipes(HolderGetter<Item> items, HolderGetter<TravellersModifier> modifiers) {
		Predicate<Ingredient> splitTravellersModifiersRecipes = ingredient -> !ingredient.isCustom() && ingredient.getValues().unwrap().right()
			.map(i -> i.stream().map(Holder::value).allMatch(item -> item instanceof TravellersModifiable modifiable && modifiable.getModifierSlots() > 0))
			.orElse(false);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" R ")
				.pattern("RGR")
				.define('R', TFBlocks.RED_THREAD)
				.define('G', TFItems.TRAVELLERS_GOGGLES)
				.build(),
			TravellersModifiersManager.RED_THREAD_VISION_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" E ")
				.pattern("PVP")
				.pattern(" S ")
				.define('E', Items.ENDER_EYE)
				.define('P', Items.ENDER_PEARL)
				.define('V', TFItems.TRAVELLERS_VEST)
				.define('S', Items.SUGAR)
				.build(),
			TravellersModifiersManager.PERFECT_DODGE_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShapeless(modifiers, CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(potionsIngredient(Potions.INVISIBILITY, Potions.LONG_INVISIBILITY))
				.ingredient(Items.ENDER_EYE)
				.ingredient(Items.SPIDER_EYE)
				.ingredient(Items.GOLDEN_CARROT)
				.ingredient(TFItems.TRAVELLERS_VEST)
				.build(),
			TravellersModifiersManager.STEALTH_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("RRR")
				.pattern("RVR")
				.pattern("OOO")
				.define('R', Items.REDSTONE_BLOCK)
				.define('V', TFItems.TRAVELLERS_VEST)
				.define('O', Items.OBSIDIAN)
				.build(),
			TravellersModifiersManager.HASTE_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShapeless(modifiers, CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(TFItems.EXANIMATE_ESSENCE)
				.ingredient(TFItems.ORE_MAGNET)
				.ingredient(TFItems.LIVEROOT)
				.ingredient(Items.CHICKEN)
				.ingredient(TFItems.TRAVELLERS_VEST)
				.build(),
			TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShapeless(modifiers, CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(TFItems.HYDRA_CHOP)
				.ingredient(Items.BUNDLE)
				.ingredient(TFItems.TRAVELLERS_VEST)
				.build(),
			TravellersModifiersManager.EFFICIENT_EATER_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShapeless(modifiers, CartesianShapelessRecipeBuilder.create(splitTravellersModifiersRecipes)
				.ingredient(TFItems.TRAVELLERS_WINGS)
				.ingredient(TFItems.TRAVELLERS_BELT)
				.build(),
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("FCF")
				.pattern("FWF")
				.pattern("FEF")
				.define('F', Items.FEATHER)
				.define('C', Items.COBWEB)
				.define('W', TFItems.TRAVELLERS_WINGS)
				.define('E', TFItems.BORER_ESSENCE)
				.build(),
			TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" L ")
				.pattern("SWS")
				.pattern("P P")
				.define('L', potionsIngredient(Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING))
				.define('S', Tags.Items.STRINGS)
				.define('W', TFItems.TRAVELLERS_WINGS)
				.define('P', Items.PISTON)
				.build(),
			TravellersModifiersManager.DOUBLE_JUMP_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("FPF")
				.pattern("HWH")
				.define('P', potionsIngredient(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS))
				.define('H', Items.RABBIT_HIDE)
				.define('W', TFItems.TRAVELLERS_WINGS)
				.define('F', TFItems.RAVEN_FEATHER)
				.build(),
			TravellersModifiersManager.AGILE_RANGER_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("SBS")
				.pattern("PWP")
				.pattern("SBS")
				.define('S', Tags.Items.STRINGS)
				.define('B', Tags.Items.BONES)
				.define('P', Items.PISTON)
				.define('W', TFItems.TRAVELLERS_WINGS)
				.build(),
			TravellersModifiersManager.SIDESTEP_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("SBS")
				.pattern("L L")
				.define('B', TFItems.TRAVELLERS_BOOTS)
				.define('S', TFItemTags.MAZE_SLIME_BALLS)
				.define('L', Items.LILY_PAD)
				.build(),
			TravellersModifiersManager.WATER_WALK_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("n n")
				.pattern("kbk")
				.pattern("i i")
				.define('b', TFItems.TRAVELLERS_BOOTS)
				.define('k', TFItemTags.KNIGHTMETAL_INGOTS)
				.define('n', Tags.Items.NUGGETS_IRON)
				.define('i', Tags.Items.INGOTS_IRON)
				.build(),
			TravellersModifiersManager.UNRESTRAINED_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("WBW")
				.pattern("S S")
				.define('W', Tags.Items.STRINGS)
				.define('B', TFItems.TRAVELLERS_BOOTS)
				.define('S', Items.SLIME_BLOCK)
				.build(),
			TravellersModifiersManager.SLIMY_SOLES_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("MPM")
				.pattern("HBH")
				.define('M', TFItems.RAW_MEEF)
				.define('P', potionsIngredient(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS))
				.define('B', TFItems.TRAVELLERS_BOOTS)
				.define('H', Items.RABBIT_HIDE)
				.build(),
			TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" E ")
				.pattern("MTM")
				.pattern(" E ")
				.define('E', Items.EXPERIENCE_BOTTLE)
				.define('M', TFBlocks.MOSS_PATCH)
				.define('T', Ingredient.of(TFItems.TRAVELLERS_GOGGLES, TFItems.TRAVELLERS_VEST, TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_BOOTS))
				.build(),
			TravellersModifiersManager.AUTO_REPAIR_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" M ")
				.pattern("ETE")
				.pattern(" M ")
				.define('M', TFBlocks.MOSS_PATCH)
				.define('E', Items.EXPERIENCE_BOTTLE)
				.define('T', Ingredient.of(TFItems.TRAVELLERS_GOGGLES, TFItems.TRAVELLERS_VEST, TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_BOOTS))
				.build(),
			TravellersModifiersManager.AUTO_REPAIR_MODIFIER, true).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("M M")
				.pattern("YGY")
				.pattern("M M")
				.define('M', Items.PHANTOM_MEMBRANE)
				.define('Y', Items.STAINED_GLASS_PANE.yellow())
				.define('G', TFItems.TRAVELLERS_GOGGLES)
				.build(),
			TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern("sss")
				.pattern("igi")
				.pattern("sss")
				.define('i', Items.ITEM_FRAME)
				.define('s', Tags.Items.RODS_WOODEN)
				.define('g', TFItems.TRAVELLERS_GOGGLES)
				.build(),
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER).save(this.output);

		TravellersGearComponentModifierBuilder.buildShaped(modifiers, CartesianShapedRecipeBuilder.create(items, splitTravellersModifiersRecipes)
				.pattern(" b")
				.pattern("gb")
				.pattern("sw")
				.define('b', Items.BAMBOO)
				.define('w', potionsIngredient(Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING))
				.define('s', Tags.Items.SLIME_BALLS)
				.define('g', TFItems.TRAVELLERS_GOGGLES)
				.build(),
			TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER).save(this.output);
	}

	private void travellersGearRecipes(HolderGetter<Item> items) {
		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GOGGLES)
			.pattern("l l")
			.pattern("plp")
			.pattern("g g")
			.define('l', TFItems.TANNED_LEATHER)
			.define('p', Tags.BlockItems.GLASS_PANES_COLORLESS.item())
			.define('g', Tags.Items.INGOTS_GOLD)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_GOGGLES.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_VEST)
			.pattern("l l")
			.pattern("lwl")
			.pattern("lwl")
			.define('l', TFItems.TANNED_LEATHER)
			.define('w', ItemTags.WOOL)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_VEST.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_WINGS)
			.pattern("g g")
			.pattern("ili")
			.pattern("ici")
			.define('l', TFItems.TANNED_LEATHER)
			.define('g', Tags.Items.INGOTS_GOLD)
			.define('i', Tags.Items.INGOTS_COPPER)
			.define('c', TFItemTags.CARMINITE_GEMS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_WINGS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BOOTS)
			.pattern("s s")
			.pattern("l l")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_BOOTS.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_BELT)
			.pattern("lll")
			.pattern("ixi")
			.pattern("lll")
			.define('l', TFItems.TANNED_LEATHER)
			.define('x', DifferenceIngredient.of(Ingredient.of(items.getOrThrow(Tags.BlockItems.CHESTS_WOODEN.item())), Ingredient.of(items.getOrThrow(Tags.BlockItems.CHESTS_TRAPPED.item()))))
			.define('i', Tags.Items.NUGGETS_IRON)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_BELT.getId().getPath()));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, TFItems.TRAVELLERS_GLOVES)
			.pattern("s s")
			.pattern("l l")
			.define('l', TFItems.TANNED_LEATHER)
			.define('s', Tags.Items.STRINGS)
			.unlockedBy("has_leather", has(TFItems.TANNED_LEATHER))
			.save(this.output, locEquip(TFItems.TRAVELLERS_GLOVES.getId().getPath()));
	}

	@SafeVarargs
	private Ingredient potionsIngredient(Holder<Potion>... potions) {
		Ingredient[] ingredients = new Ingredient[potions.length];
		for (int i = 0; i < potions.length; i++) {
			ingredients[i] = this.potionIngredient(potions[i]);
		}
		return CompoundIngredient.of(ingredients);
	}

	private Ingredient potionIngredient(Holder<Potion> potion) {
		return DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(potion), Items.POTION);
	}

	private void blockCompressionRecipes(HolderGetter<Item> items) {
		reverseCompressBlock(items, "arctic_block_to_item", TFItems.ARCTIC_FUR, TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR);
		reverseCompressBlock(items, "carminite_block_to_item", TFItems.CARMINITE, TFItemTags.STORAGE_BLOCKS_CARMINITE);
		reverseCompressBlock(items, "ironwood_block_ingot", TFItems.IRONWOOD_INGOT, TFItemTags.STORAGE_BLOCKS_IRONWOOD);
		reverseCompressBlock(items, "knightmetal_block_ingot", TFItems.KNIGHTMETAL_INGOT, TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL);
		reverseCompressBlock(items, "maze_slime_block_to_item", TFItems.MAZE_SLIME_BALL, TFItemTags.STORAGE_BLOCKS_MAZE_SLIME);
		reverseCompressBlock(items, "steeleaf_block_ingot", TFItems.STEELEAF_INGOT, TFItemTags.STORAGE_BLOCKS_STEELEAF);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.FIERY_INGOT, 9)
			.requires(TFItemTags.STORAGE_BLOCKS_FIERY)
			.unlockedBy("has_item", has(TFItemTags.STORAGE_BLOCKS_FIERY))
			.group("fiery_ingot")
			.save(this.output, this.createKey("compressed_blocks/reversed/fiery_block_to_ingot"));

		compressedBlock(items, "arctic_block", TFBlocks.ARCTIC_FUR_BLOCK, TFItemTags.ARCTIC_FUR);
		compressedBlock(items, "carminite_block", TFBlocks.CARMINITE_BLOCK, TFItemTags.CARMINITE_GEMS);
		compressedBlock(items, "fiery_block", TFBlocks.FIERY_BLOCK, TFItemTags.FIERY_INGOTS);
		compressedBlock(items, "ironwood_block", TFBlocks.IRONWOOD_BLOCK, TFItemTags.IRONWOOD_INGOTS);
		compressedBlock(items, "knightmetal_block", TFBlocks.KNIGHTMETAL_BLOCK, TFItemTags.KNIGHTMETAL_INGOTS);
		compressedBlock(items, "maze_slime_block", TFBlocks.MAZE_SLIME_BLOCK, TFItemTags.MAZE_SLIME_BALLS);
		compressedBlock(items, "steeleaf_block", TFBlocks.STEELEAF_BLOCK, TFItemTags.STEELEAF_INGOTS);
	}

	private void emptyMapRecipes(HolderGetter<Item> items) {
		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.MAGIC_MAP_FOCUS)
			.requires(TFItems.RAVEN_FEATHER)
			.requires(TFItems.TORCHBERRIES)
			.requires(Tags.Items.DUSTS_GLOWSTONE)
			.unlockedBy("has_berries", has(TFItems.TORCHBERRIES))
			.unlockedBy("has_feather", has(TFItems.RAVEN_FEATHER))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, TFItems.MAGIC_MAP)
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', TFItemTags.PAPER)
			.define('•', Ingredient.of(TFItems.MAGIC_MAP_FOCUS))
			.unlockedBy("has_item", has(TFItems.MAGIC_MAP_FOCUS))
			.save(this.output);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, TFItems.MAZE_MAP)
			.pattern("###")
			.pattern("#•#")
			.pattern("###")
			.define('#', TFItemTags.PAPER)
			.define('•', Ingredient.of(TFItems.MAZE_MAP_FOCUS))
			.unlockedBy("has_item", has(TFItems.MAZE_MAP_FOCUS))
			.save(this.output);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.ORE_MAP)
			.requires(TFItems.MAZE_MAP)
			.requires(Tags.BlockItems.STORAGE_BLOCKS_DIAMOND.item())
			.requires(Tags.BlockItems.STORAGE_BLOCKS_GOLD.item())
			.requires(Tags.BlockItems.STORAGE_BLOCKS_IRON.item())
			.unlockedBy("has_item", has(TFItems.MAZE_MAP))
			.save(this.output);
	}

	private void woodRecipes(HolderGetter<Item> items) {
		buttonBlock(items, "canopy", TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_PLANKS);
		buttonBlock(items, "dark", TFBlocks.DARK_BUTTON, TFBlocks.DARK_PLANKS);
		buttonBlock(items, "mangrove", TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_PLANKS);
		buttonBlock(items, "mining", TFBlocks.MINING_BUTTON, TFBlocks.MINING_PLANKS);
		buttonBlock(items, "sorting", TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_PLANKS);
		buttonBlock(items, "time", TFBlocks.TIME_BUTTON, TFBlocks.TIME_PLANKS);
		buttonBlock(items, "transformation", TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_PLANKS);
		buttonBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_PLANKS);

		doorBlock(items, "canopy", TFBlocks.CANOPY_DOOR, TFBlocks.CANOPY_PLANKS);
		doorBlock(items, "dark", TFBlocks.DARK_DOOR, TFBlocks.DARK_PLANKS);
		doorBlock(items, "mangrove", TFBlocks.MANGROVE_DOOR, TFBlocks.MANGROVE_PLANKS);
		doorBlock(items, "mining", TFBlocks.MINING_DOOR, TFBlocks.MINING_PLANKS);
		doorBlock(items, "sorting", TFBlocks.SORTING_DOOR, TFBlocks.SORTING_PLANKS);
		doorBlock(items, "time", TFBlocks.TIME_DOOR, TFBlocks.TIME_PLANKS);
		doorBlock(items, "transformation", TFBlocks.TRANSFORMATION_DOOR, TFBlocks.TRANSFORMATION_PLANKS);
		doorBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_DOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		fenceBlock(items, "canopy", TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_PLANKS);
		fenceBlock(items, "dark", TFBlocks.DARK_FENCE, TFBlocks.DARK_PLANKS);
		fenceBlock(items, "mangrove", TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_PLANKS);
		fenceBlock(items, "mining", TFBlocks.MINING_FENCE, TFBlocks.MINING_PLANKS);
		fenceBlock(items, "sorting", TFBlocks.SORTING_FENCE, TFBlocks.SORTING_PLANKS);
		fenceBlock(items, "time", TFBlocks.TIME_FENCE, TFBlocks.TIME_PLANKS);
		fenceBlock(items, "transformation", TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_PLANKS);
		fenceBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_PLANKS);

		gateBlock(items, "canopy", TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLANKS);
		gateBlock(items, "dark", TFBlocks.DARK_GATE, TFBlocks.DARK_PLANKS);
		gateBlock(items, "mangrove", TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLANKS);
		gateBlock(items, "mining", TFBlocks.MINING_GATE, TFBlocks.MINING_PLANKS);
		gateBlock(items, "sorting", TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLANKS);
		gateBlock(items, "time", TFBlocks.TIME_GATE, TFBlocks.TIME_PLANKS);
		gateBlock(items, "transformation", TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLANKS);
		gateBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		planksBlock(items, "canopy", TFBlocks.CANOPY_PLANKS, TFItemTags.CANOPY_LOGS);
		planksBlock(items, "dark", TFBlocks.DARK_PLANKS, TFItemTags.DARKWOOD_LOGS);
		planksBlock(items, "mangrove", TFBlocks.MANGROVE_PLANKS, TFItemTags.MANGROVE_LOGS);
		planksBlock(items, "mining", TFBlocks.MINING_PLANKS, TFItemTags.MINING_LOGS);
		planksBlock(items, "sorting", TFBlocks.SORTING_PLANKS, TFItemTags.SORTING_LOGS);
		planksBlock(items, "time", TFBlocks.TIME_PLANKS, TFItemTags.TIME_LOGS);
		planksBlock(items, "transformation", TFBlocks.TRANSFORMATION_PLANKS, TFItemTags.TRANSFORMATION_LOGS);
		planksBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS, TFItemTags.TWILIGHT_OAK_LOGS);

		woodBlock(items, "canopy", TFBlocks.CANOPY_WOOD, TFBlocks.CANOPY_LOG);
		woodBlock(items, "dark", TFBlocks.DARK_WOOD, TFBlocks.DARK_LOG);
		woodBlock(items, "mangrove", TFBlocks.MANGROVE_WOOD, TFBlocks.MANGROVE_LOG);
		woodBlock(items, "mining", TFBlocks.MINING_WOOD, TFBlocks.MINING_LOG);
		woodBlock(items, "sorting", TFBlocks.SORTING_WOOD, TFBlocks.SORTING_LOG);
		woodBlock(items, "time", TFBlocks.TIME_WOOD, TFBlocks.TIME_LOG);
		woodBlock(items, "transformation", TFBlocks.TRANSFORMATION_WOOD, TFBlocks.TRANSFORMATION_LOG);
		woodBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.TWILIGHT_OAK_LOG);

		strippedWoodBlock(items, "canopy", TFBlocks.STRIPPED_CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_LOG);
		strippedWoodBlock(items, "dark", TFBlocks.STRIPPED_DARK_WOOD, TFBlocks.STRIPPED_DARK_LOG);
		strippedWoodBlock(items, "mangrove", TFBlocks.STRIPPED_MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_LOG);
		strippedWoodBlock(items, "mining", TFBlocks.STRIPPED_MINING_WOOD, TFBlocks.STRIPPED_MINING_LOG);
		strippedWoodBlock(items, "sorting", TFBlocks.STRIPPED_SORTING_WOOD, TFBlocks.STRIPPED_SORTING_LOG);
		strippedWoodBlock(items, "time", TFBlocks.STRIPPED_TIME_WOOD, TFBlocks.STRIPPED_TIME_LOG);
		strippedWoodBlock(items, "transformation", TFBlocks.STRIPPED_TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		strippedWoodBlock(items, "twilight_oak", TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		plateBlock(items, "canopy", TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_PLANKS);
		plateBlock(items, "dark", TFBlocks.DARK_PLATE, TFBlocks.DARK_PLANKS);
		plateBlock(items, "mangrove", TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_PLANKS);
		plateBlock(items, "mining", TFBlocks.MINING_PLATE, TFBlocks.MINING_PLANKS);
		plateBlock(items, "sorting", TFBlocks.SORTING_PLATE, TFBlocks.SORTING_PLANKS);
		plateBlock(items, "time", TFBlocks.TIME_PLATE, TFBlocks.TIME_PLANKS);
		plateBlock(items, "transformation", TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_PLANKS);
		plateBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenSlabBlock(items, "canopy", TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_PLANKS);
		woodenSlabBlock(items, "dark", TFBlocks.DARK_SLAB, TFBlocks.DARK_PLANKS);
		woodenSlabBlock(items, "mangrove", TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_PLANKS);
		woodenSlabBlock(items, "mining", TFBlocks.MINING_SLAB, TFBlocks.MINING_PLANKS);
		woodenSlabBlock(items, "sorting", TFBlocks.SORTING_SLAB, TFBlocks.SORTING_PLANKS);
		woodenSlabBlock(items, "time", TFBlocks.TIME_SLAB, TFBlocks.TIME_PLANKS);
		woodenSlabBlock(items, "transformation", TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_PLANKS);
		woodenSlabBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_PLANKS);

		woodenStairsBlock(items, locWood("canopy_stairs"), TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_PLANKS);
		woodenStairsBlock(items, locWood("dark_stairs"), TFBlocks.DARK_STAIRS, TFBlocks.DARK_PLANKS, TFBlocks.DARK_PLANKS);
		woodenStairsBlock(items, locWood("mangrove_stairs"), TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_PLANKS);
		woodenStairsBlock(items, locWood("mining_stairs"), TFBlocks.MINING_STAIRS, TFBlocks.MINING_PLANKS, TFBlocks.MINING_PLANKS);
		woodenStairsBlock(items, locWood("sorting_stairs"), TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_PLANKS);
		woodenStairsBlock(items, locWood("time_stairs"), TFBlocks.TIME_STAIRS, TFBlocks.TIME_PLANKS, TFBlocks.TIME_PLANKS);
		woodenStairsBlock(items, locWood("transformation_stairs"), TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_PLANKS);
		woodenStairsBlock(items, locWood("twilight_oak_stairs"), TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_PLANKS);

		trapdoorBlock(items, "canopy", TFBlocks.CANOPY_TRAPDOOR, TFBlocks.CANOPY_PLANKS);
		trapdoorBlock(items, "dark", TFBlocks.DARK_TRAPDOOR, TFBlocks.DARK_PLANKS);
		trapdoorBlock(items, "mangrove", TFBlocks.MANGROVE_TRAPDOOR, TFBlocks.MANGROVE_PLANKS);
		trapdoorBlock(items, "mining", TFBlocks.MINING_TRAPDOOR, TFBlocks.MINING_PLANKS);
		trapdoorBlock(items, "sorting", TFBlocks.SORTING_TRAPDOOR, TFBlocks.SORTING_PLANKS);
		trapdoorBlock(items, "time", TFBlocks.TIME_TRAPDOOR, TFBlocks.TIME_PLANKS);
		trapdoorBlock(items, "transformation", TFBlocks.TRANSFORMATION_TRAPDOOR, TFBlocks.TRANSFORMATION_PLANKS);
		trapdoorBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_TRAPDOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		signBlock(items, "canopy", TFItems.CANOPY_SIGN, TFBlocks.CANOPY_PLANKS);
		signBlock(items, "dark", TFItems.DARK_SIGN, TFBlocks.DARK_PLANKS);
		signBlock(items, "mangrove", TFItems.MANGROVE_SIGN, TFBlocks.MANGROVE_PLANKS);
		signBlock(items, "mining", TFItems.MINING_SIGN, TFBlocks.MINING_PLANKS);
		signBlock(items, "sorting", TFItems.SORTING_SIGN, TFBlocks.SORTING_PLANKS);
		signBlock(items, "time", TFItems.TIME_SIGN, TFBlocks.TIME_PLANKS);
		signBlock(items, "transformation", TFItems.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_PLANKS);
		signBlock(items, "twilight_oak", TFItems.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_OAK_PLANKS);

		hangingSignBlock(items, "canopy", TFItems.CANOPY_HANGING_SIGN, TFBlocks.STRIPPED_CANOPY_LOG);
		hangingSignBlock(items, "dark", TFItems.DARK_HANGING_SIGN, TFBlocks.STRIPPED_DARK_LOG);
		hangingSignBlock(items, "mangrove", TFItems.MANGROVE_HANGING_SIGN, TFBlocks.STRIPPED_MANGROVE_LOG);
		hangingSignBlock(items, "mining", TFItems.MINING_HANGING_SIGN, TFBlocks.STRIPPED_MINING_LOG);
		hangingSignBlock(items, "sorting", TFItems.SORTING_HANGING_SIGN, TFBlocks.STRIPPED_SORTING_LOG);
		hangingSignBlock(items, "time", TFItems.TIME_HANGING_SIGN, TFBlocks.STRIPPED_TIME_LOG);
		hangingSignBlock(items, "transformation", TFItems.TRANSFORMATION_HANGING_SIGN, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		hangingSignBlock(items, "twilight_oak", TFItems.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		banisterBlock(items, "canopy", TFBlocks.CANOPY_BANISTER, TFBlocks.CANOPY_SLAB);
		banisterBlock(items, "dark", TFBlocks.DARK_BANISTER, TFBlocks.DARK_SLAB);
		banisterBlock(items, "mangrove", TFBlocks.MANGROVE_BANISTER, TFBlocks.MANGROVE_SLAB);
		banisterBlock(items, "mining", TFBlocks.MINING_BANISTER, TFBlocks.MINING_SLAB);
		banisterBlock(items, "sorting", TFBlocks.SORTING_BANISTER, TFBlocks.SORTING_SLAB);
		banisterBlock(items, "time", TFBlocks.TIME_BANISTER, TFBlocks.TIME_SLAB);
		banisterBlock(items, "transformation", TFBlocks.TRANSFORMATION_BANISTER, TFBlocks.TRANSFORMATION_SLAB);
		banisterBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_BANISTER, TFBlocks.TWILIGHT_OAK_SLAB);

		banisterBlock(items, "oak", TFBlocks.OAK_BANISTER, Blocks.OAK_SLAB);
		banisterBlock(items, "spruce", TFBlocks.SPRUCE_BANISTER, Blocks.SPRUCE_SLAB);
		banisterBlock(items, "birch", TFBlocks.BIRCH_BANISTER, Blocks.BIRCH_SLAB);
		banisterBlock(items, "jungle", TFBlocks.JUNGLE_BANISTER, Blocks.JUNGLE_SLAB);
		banisterBlock(items, "acacia", TFBlocks.ACACIA_BANISTER, Blocks.ACACIA_SLAB);
		banisterBlock(items, "dark_oak", TFBlocks.DARK_OAK_BANISTER, Blocks.DARK_OAK_SLAB);
		banisterBlock(items, "crimson", TFBlocks.CRIMSON_BANISTER, Blocks.CRIMSON_SLAB);
		banisterBlock(items, "warped", TFBlocks.WARPED_BANISTER, Blocks.WARPED_SLAB);
		banisterBlock(items, "vangrove", TFBlocks.VANGROVE_BANISTER, Blocks.MANGROVE_SLAB);
		banisterBlock(items, "bamboo", TFBlocks.BAMBOO_BANISTER, Blocks.BAMBOO_SLAB);
		banisterBlock(items, "cherry", TFBlocks.CHERRY_BANISTER, Blocks.CHERRY_SLAB);
		banisterBlock(items, "pale_oak", TFBlocks.PALE_OAK_BANISTER, Blocks.PALE_OAK_SLAB);

		dryingRackBlock(items, "canopy", TFBlocks.CANOPY_DRYING_RACK, TFBlocks.CANOPY_SLAB);
		dryingRackBlock(items, "dark", TFBlocks.DARK_DRYING_RACK, TFBlocks.DARK_SLAB);
		dryingRackBlock(items, "mangrove", TFBlocks.MANGROVE_DRYING_RACK, TFBlocks.MANGROVE_SLAB);
		dryingRackBlock(items, "mining", TFBlocks.MINING_DRYING_RACK, TFBlocks.MINING_SLAB);
		dryingRackBlock(items, "sorting", TFBlocks.SORTING_DRYING_RACK, TFBlocks.SORTING_SLAB);
		dryingRackBlock(items, "time", TFBlocks.TIME_DRYING_RACK, TFBlocks.TIME_SLAB);
		dryingRackBlock(items, "transformation", TFBlocks.TRANSFORMATION_DRYING_RACK, TFBlocks.TRANSFORMATION_SLAB);
		dryingRackBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_DRYING_RACK, TFBlocks.TWILIGHT_OAK_SLAB);

		dryingRackBlock(items, "oak", TFBlocks.OAK_DRYING_RACK, Blocks.OAK_SLAB);
		dryingRackBlock(items, "spruce", TFBlocks.SPRUCE_DRYING_RACK, Blocks.SPRUCE_SLAB);
		dryingRackBlock(items, "birch", TFBlocks.BIRCH_DRYING_RACK, Blocks.BIRCH_SLAB);
		dryingRackBlock(items, "jungle", TFBlocks.JUNGLE_DRYING_RACK, Blocks.JUNGLE_SLAB);
		dryingRackBlock(items, "acacia", TFBlocks.ACACIA_DRYING_RACK, Blocks.ACACIA_SLAB);
		dryingRackBlock(items, "dark_oak", TFBlocks.DARK_OAK_DRYING_RACK, Blocks.DARK_OAK_SLAB);
		dryingRackBlock(items, "crimson", TFBlocks.CRIMSON_DRYING_RACK, Blocks.CRIMSON_SLAB);
		dryingRackBlock(items, "warped", TFBlocks.WARPED_DRYING_RACK, Blocks.WARPED_SLAB);
		dryingRackBlock(items, "vangrove", TFBlocks.VANGROVE_DRYING_RACK, Blocks.MANGROVE_SLAB);
		dryingRackBlock(items, "bamboo", TFBlocks.BAMBOO_DRYING_RACK, Blocks.BAMBOO_SLAB);
		dryingRackBlock(items, "cherry", TFBlocks.CHERRY_DRYING_RACK, Blocks.CHERRY_SLAB);
		dryingRackBlock(items, "pale_oak", TFBlocks.PALE_OAK_DRYING_RACK, Blocks.PALE_OAK_SLAB);

		chestBlock(items, "twilight_oak", TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS);
		chestBlock(items, "canopy", TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_PLANKS);
		chestBlock(items, "mangrove", TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_PLANKS);
		chestBlock(items, "dark", TFBlocks.DARK_CHEST, TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_PLANKS);
		chestBlock(items, "time", TFBlocks.TIME_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_PLANKS);
		chestBlock(items, "transformation", TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_PLANKS);
		chestBlock(items, "mining", TFBlocks.MINING_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_PLANKS);
		chestBlock(items, "sorting", TFBlocks.SORTING_CHEST, TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_PLANKS);

		buildBoats(items, TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT, TFBlocks.TWILIGHT_OAK_PLANKS);
		buildBoats(items, TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT, TFBlocks.CANOPY_PLANKS);
		buildBoats(items, TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT, TFBlocks.MANGROVE_PLANKS);
		buildBoats(items, TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT, TFBlocks.DARK_PLANKS);
		buildBoats(items, TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT, TFBlocks.TIME_PLANKS);
		buildBoats(items, TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT, TFBlocks.TRANSFORMATION_PLANKS);
		buildBoats(items, TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT, TFBlocks.MINING_PLANKS);
		buildBoats(items, TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT, TFBlocks.SORTING_PLANKS);
	}

	private void dryingRecipes(HolderGetter<Item> items) {
		DryingRecipeBuilder.drying(items, Tags.Items.FOODS_COOKED_MEAT, Items.LEATHER, 8.5F)
			.unlockedBy("has_meat", has(Tags.Items.FOODS_COOKED_MEAT))
			.save(this.output, this.createKey("drying/cooked_meat_to_leather"));

		DryingRecipeBuilder.drying(items, ItemTags.SAPLINGS, Items.DEAD_BUSH, 6)
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

		DryingRecipeBuilder.drying(Items.ROTTEN_FLESH, TFItems.MONSTER_JERKY)
			.unlockedBy("has_meat", has(Items.ROTTEN_FLESH))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.BEEF, TFItems.BEEF_JERKY)
			.unlockedBy("has_meat", has(Items.BEEF))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.CHICKEN, TFItems.CHICKEN_JERKY)
			.unlockedBy("has_meat", has(Items.CHICKEN))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.PORKCHOP, TFItems.PORK_JERKY)
			.unlockedBy("has_meat", has(Items.PORKCHOP))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.MUTTON, TFItems.MUTTON_JERKY)
			.unlockedBy("has_meat", has(Items.MUTTON))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.RABBIT, TFItems.RABBIT_JERKY)
			.unlockedBy("has_meat", has(Items.RABBIT))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.COD, TFItems.COD_JERKY)
			.unlockedBy("has_meat", has(Items.COD))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.SALMON, TFItems.SALMON_JERKY)
			.unlockedBy("has_meat", has(Items.SALMON))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.TROPICAL_FISH, TFItems.TROPICAL_FISH_JERKY)
			.unlockedBy("has_meat", has(Items.TROPICAL_FISH))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.PUFFERFISH, TFItems.FUGU_JERKY)
			.unlockedBy("has_meat", has(Items.PUFFERFISH))
			.save(this.output);

		DryingRecipeBuilder.drying(TFItems.RAW_VENISON, TFItems.VENISON_JERKY)
			.unlockedBy("has_meat", has(TFItems.RAW_VENISON))
			.save(this.output);

		DryingRecipeBuilder.drying(TFItems.RAW_MEEF, TFItems.MEEF_JERKY)
			.unlockedBy("has_meat", has(TFItems.RAW_MEEF))
			.save(this.output);

		DryingRecipeBuilder.drying(Items.SLIME_BALL, TFItems.GELATINOUS_SLIME_DROP)
			.unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
			.save(this.output);

		DryingRecipeBuilder.drying(items, TFItemTags.MAZE_SLIME_BALLS, TFItems.GELATINOUS_MAZE_SLIME_DROP, 5)
			.unlockedBy("has_maze_slime_ball", has(TFItemTags.MAZE_SLIME_BALLS))
			.save(this.output);

		DryingRecipeBuilder.drying(TFItems.TREATED_LEATHER, TFItems.TANNED_LEATHER)
			.unlockedBy("has_treated", has(TFItems.TREATED_LEATHER))
			.save(this.output);

		this.dryingRackCoralRecipes();

		DryingRecipeBuilder.drying(DataComponentIngredient.of(DataComponents.ITEM_NAME, Component.translatable("item.minecraft.bread"), Items.BREAD), new ItemStackTemplate(Items.BREAD, 1, DataComponentPatch.builder()
			.set(DataComponents.DAMAGE_TYPE, output.lookup(Registries.DAMAGE_TYPE).getOrThrow(TFDamageTypes.STALE_SANDWICH))
			.set(DataComponents.MAX_STACK_SIZE, 1)
			.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.build())
			.set(DataComponents.WEAPON, new Weapon(1))
			.set(DataComponents.TOOL,
				new Tool(
					List.of(
						Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
						Tool.Rule.overrideSpeed(output.lookup(Registries.BLOCK).getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
						Tool.Rule.overrideSpeed(output.lookup(Registries.BLOCK).getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
					),
					1.0F,
					2,
					false
				))
			.set(DataComponents.ITEM_NAME, Component.translatable("item.twilightforest.stale_bread"))
			.set(DataComponents.ITEM_MODEL, TwilightForestMod.prefix("stale_bread"))
			.remove(DataComponents.FOOD).remove(DataComponents.CONSUMABLE)
			.build()
		)).save(this.output, this.createKey("drying/stale_bread"));
	}

	private void dryingRackCoralRecipes() {
		for (String species : this.coralSpecies) {
			for (String type : this.coralTypes) {
				this.coralDrying(species + "_coral" + type);
			}
		}
	}

	private void coralDrying(String coral) {
		Identifier live = Identifier.withDefaultNamespace(coral);
		Item liveItem = BuiltInRegistries.ITEM.getValue(live);
		DryingRecipeBuilder.drying(liveItem, BuiltInRegistries.ITEM.getValue(live.withPrefix("dead_")), 1 / 30F)
			.unlockedBy("has_coral", has(liveItem))
			.save(this.output, this.createKey("drying/" + coral));
	}

	private void nagastoneRecipes(HolderGetter<Item> items) {
		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.SPIRAL_BRICKS, 8)
			.pattern("BSS")
			.pattern("BSS")
			.pattern("BBB")
			.define('B', Ingredient.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
			.define('S', Ingredient.of(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
			.unlockedBy("has_item", has(TFBlocks.SPIRAL_BRICKS))
			.save(this.output, locNaga("nagastone_spiral"));

		stairsBlock(items, locNaga("nagastone_stairs_left"), TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE);
		stairsRightBlock(items, locNaga("nagastone_stairs_right"), TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE);

		stairsBlock(items, locNaga("mossy_nagastone_stairs_left"), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE);
		stairsRightBlock(items, locNaga("mossy_nagastone_stairs_right"), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_ETCHED_NAGASTONE, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.ETCHED_NAGASTONE))
			.unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE))
			.save(this.output, locNaga("mossy_etched_nagastone"));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_NAGASTONE_PILLAR, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.NAGASTONE_PILLAR))
			.unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR))
			.save(this.output, locNaga("mossy_nagastone_pillar"));

		stairsBlock(items, locNaga("cracked_nagastone_stairs_left"), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE);
		stairsRightBlock(items, locNaga("cracked_nagastone_stairs_right"), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE);
	}

	private void castleRecipes(HolderGetter<Item> items) {
		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_CASTLE_BRICK, 1)
			.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
			.requires(Ingredient.of(TFBlocks.CASTLE_BRICK))
			.unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("mossy_castle_brick"));

		castleBlock(items, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK);
		castleBlock(items, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.THICK_CASTLE_BRICK);
		castleBlock(items, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, 4)
			.pattern("##")
			.pattern("##")
			.define('#', Ingredient.of(TFBlocks.BOLD_CASTLE_BRICK_TILE))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("bold_castle_pillar_from_tile"));

		ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, 6)
			.pattern("#H#")
			.pattern("#H#")
			.define('#', Ingredient.of(TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK))
			.define('H', Ingredient.of(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE))
			.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK))
			.save(this.output, locCastle("encased_castle_pillar"));

		stairsBlock(items, locCastle("bold_castle_brick_stairs"), TFBlocks.BOLD_CASTLE_BRICK_STAIRS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stairsBlock(items, locCastle("castle_brick_stairs"), TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK);
		stairsBlock(items, locCastle("cracked_castle_brick_stairs"), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK);
		stairsBlock(items, locCastle("encased_castle_brick_stairs"), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stairsBlock(items, locCastle("mossy_castle_brick_stairs"), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK);
		stairsBlock(items, locCastle("worn_castle_brick_stairs"), TFBlocks.WORN_CASTLE_BRICK_STAIRS, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK);
	}

	private void fieryConversions(HolderGetter<Item> items) {
		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, TFItems.FIERY_INGOT)
			.requires(Ingredient.of(items.getOrThrow(TFItemTags.FIERY_VIAL)))
			.requires(Ingredient.of(items.getOrThrow(Tags.Items.INGOTS_IRON)))
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group("fiery_ingot")
			.save(this.output, locEquip("fiery_ingot_crafting"));

		fieryConversion(items, TFItems.FIERY_HELMET, Items.IRON_HELMET, 5);
		fieryConversion(items, TFItems.FIERY_CHESTPLATE, Items.IRON_CHESTPLATE, 8);
		fieryConversion(items, TFItems.FIERY_LEGGINGS, Items.IRON_LEGGINGS, 7);
		fieryConversion(items, TFItems.FIERY_BOOTS, Items.IRON_BOOTS, 4);

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.COMBAT, TFItems.FIERY_SWORD)
			.requires(Items.IRON_SWORD)
			.requires(Ingredient.of(items.getOrThrow(TFItemTags.FIERY_VIAL)), 2)
			.requires(Ingredient.of(items.getOrThrow(Tags.Items.RODS_BLAZE)))
			.unlockedBy("has_item", has(TFItemTags.FIERY_VIAL))
			.group("fiery_sword")
			.save(this.output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_SWORD).getPath()));

		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE)
			.requires(Items.IRON_PICKAXE)
			.requires(Ingredient.of(items.getOrThrow(TFItemTags.FIERY_VIAL)), 3)
			.requires(Ingredient.of(items.getOrThrow(Tags.Items.RODS_BLAZE)), 2)
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
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.IRON_BERRY.get()), RecipeCategory.MISC, CookingBookCategory.MISC, Items.IRON_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.IRON_BERRY.get())).group("iron_nugget").save(this.output, this.createKey("material/" + processName + "_iron_nugget"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.GOLD_BERRY.get()), RecipeCategory.MISC, CookingBookCategory.MISC, Items.GOLD_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.GOLD_BERRY.get())).group("gold_nugget").save(this.output, this.createKey("material/" + processName + "_gold_nugget"));
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.COPPER_BERRY.get()), RecipeCategory.MISC, CookingBookCategory.MISC, Items.COPPER_NUGGET, 1.0F, smeltingTime, factory).unlockedBy("has_item", has(TFItems.COPPER_BERRY.get())).group("copper_nugget").save(this.output, this.createKey("material/" + processName + "_copper_nugget"));
	}

	private void crackedWoodRecipes() {
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(TFBlocks.TOWERWOOD), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_TOWERWOOD, 0.1F, 100).unlockedBy("has_item", has(TFBlocks.TOWERWOOD)).save(this.output, this.createKey("wood/" + "smoked" + "_cracked_towerwood"));
	}

	private void crackedStoneRecipes() {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.NAGASTONE_PILLAR), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, TFBlocks.CRACKED_NAGASTONE_PILLAR, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR)).save(this.output, this.createKey("nagastone/" + "smelted" + "_cracked_nagastone_pillar"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.ETCHED_NAGASTONE), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, TFBlocks.CRACKED_ETCHED_NAGASTONE, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE)).save(this.output, this.createKey("nagastone/" + "smelted" + "_cracked_etched_nagastone"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.MAZESTONE_BRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, TFBlocks.CRACKED_MAZESTONE, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK)).save(this.output, this.createKey("maze_stone/" + "smelted" + "_maze_stone_cracked"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.CASTLE_BRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, TFBlocks.CRACKED_CASTLE_BRICK, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK)).save(this.output, this.createKey("castleblock/" + "smelted" + "_cracked_castle_brick"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(TFBlocks.UNDERBRICK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, TFBlocks.CRACKED_UNDERBRICK, 0.1F, 200).unlockedBy("has_item", has(TFBlocks.UNDERBRICK)).save(this.output, this.createKey("smelted" + "_cracked_underbrick"));
	}

	private void dyedRecipes() {
		dyedEquipment(TFItems.ARCTIC_HELMET, "dyed_armor");
		dyedEquipment(TFItems.ARCTIC_CHESTPLATE, "dyed_armor");
		dyedEquipment(TFItems.ARCTIC_LEGGINGS, "dyed_armor");
		dyedEquipment(TFItems.ARCTIC_BOOTS, "dyed_armor");
	}

	private void stonecuttingRecipes(HolderGetter<Item> items) {
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(items, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(items, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(items, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(items, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(items, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.THICK_CASTLE_BRICK);
		stonecutting(items, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(items, output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);

		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.ENCASED_CASTLE_BRICK_STAIRS);
		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.ENCASED_CASTLE_BRICK_TILE, TFBlocks.CASTLE_ROOF_TILE);

		stonecutting(items, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_PILLAR);
		stonecutting(items, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_TILE);
		stonecutting(items, output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.BOLD_CASTLE_BRICK_STAIRS);

		stonecutting(items, output, TFBlocks.ETCHED_NAGASTONE, TFBlocks.NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.ETCHED_NAGASTONE, TFBlocks.NAGASTONE_STAIRS_RIGHT);
		stonecutting(items, output, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
		stonecutting(items, output, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);

		stonecutting(items, output, TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.NAGASTONE_STAIRS_RIGHT);
		stonecutting(items, output, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT);
		stonecutting(items, output, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT);
		stonecutting(items, output, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT);

		stonecutting(items, output, TFBlocks.DARK_LOG, TFBlocks.TOWERWOOD);
		stonecutting(items, output, TFBlocks.DARK_WOOD, TFBlocks.TOWERWOOD);
		stonecutting(items, output, TFBlocks.DARK_LOG, TFBlocks.ENCASED_TOWERWOOD);
		stonecutting(items, output, TFBlocks.DARK_WOOD, TFBlocks.ENCASED_TOWERWOOD);

		stonecutting(items, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(items, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(items, output, TFBlocks.MAZESTONE, TFBlocks.CUT_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(items, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.MAZESTONE_BORDER);
		stonecutting(items, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.CUT_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_BRICK, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(items, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.MAZESTONE_BRICK);
		stonecutting(items, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.CUT_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_BORDER, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(items, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(items, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(items, output, TFBlocks.CUT_MAZESTONE, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(items, output, TFBlocks.CUT_MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(items, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_BORDER);
		stonecutting(items, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.CUT_MAZESTONE);
		stonecutting(items, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_BRICK);
		stonecutting(items, output, TFBlocks.DECORATIVE_MAZESTONE, TFBlocks.MAZESTONE_MOSAIC);

		stonecutting(items, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.MAZESTONE_BORDER);
		stonecutting(items, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.CUT_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.DECORATIVE_MAZESTONE);
		stonecutting(items, output, TFBlocks.MAZESTONE_MOSAIC, TFBlocks.MAZESTONE_BRICK);

		stonecutting(items, output, TFBlocks.TWILIGHT_OAK_LOG, TFItems.HOLLOW_TWILIGHT_OAK_LOG);
		stonecutting(items, output, TFBlocks.CANOPY_LOG, TFItems.HOLLOW_CANOPY_LOG);
		stonecutting(items, output, TFBlocks.MANGROVE_LOG, TFItems.HOLLOW_MANGROVE_LOG);
		stonecutting(items, output, TFBlocks.DARK_LOG, TFItems.HOLLOW_DARK_LOG);
		stonecutting(items, output, TFBlocks.TIME_LOG, TFItems.HOLLOW_TIME_LOG);
		stonecutting(items, output, TFBlocks.TRANSFORMATION_LOG, TFItems.HOLLOW_TRANSFORMATION_LOG);
		stonecutting(items, output, TFBlocks.MINING_LOG, TFItems.HOLLOW_MINING_LOG);
		stonecutting(items, output, TFBlocks.SORTING_LOG, TFItems.HOLLOW_SORTING_LOG);

		stonecutting(items, output, Blocks.OAK_LOG, TFItems.HOLLOW_OAK_LOG);
		stonecutting(items, output, Blocks.SPRUCE_LOG, TFItems.HOLLOW_SPRUCE_LOG);
		stonecutting(items, output, Blocks.BIRCH_LOG, TFItems.HOLLOW_BIRCH_LOG);
		stonecutting(items, output, Blocks.JUNGLE_LOG, TFItems.HOLLOW_JUNGLE_LOG);
		stonecutting(items, output, Blocks.ACACIA_LOG, TFItems.HOLLOW_ACACIA_LOG);
		stonecutting(items, output, Blocks.DARK_OAK_LOG, TFItems.HOLLOW_DARK_OAK_LOG);
		stonecutting(items, output, Blocks.CRIMSON_STEM, TFItems.HOLLOW_CRIMSON_STEM);
		stonecutting(items, output, Blocks.WARPED_STEM, TFItems.HOLLOW_WARPED_STEM);
		stonecutting(items, output, Blocks.MANGROVE_LOG, TFItems.HOLLOW_VANGROVE_LOG);
		stonecutting(items, output, Blocks.CHERRY_LOG, TFItems.HOLLOW_CHERRY_LOG);
		stonecutting(items, output, Blocks.PALE_OAK_LOG, TFItems.HOLLOW_PALE_OAK_LOG);
		stonecutting(items, output, Blocks.STONE, TFBlocks.TWISTED_STONE);
		stonecutting(items, output, Blocks.STONE, TFBlocks.BOLD_STONE_PILLAR);
		stonecutting(items, output, Blocks.STONE, TFBlocks.TWISTED_STONE_PILLAR);
		stonecutting(items, output, Blocks.STONE, TFBlocks.SPIRAL_BRICKS);
		stonecutting(items, output, TFBlocks.TWISTED_STONE, TFBlocks.TWISTED_STONE_PILLAR);
		stonecutting(items, output, Blocks.STONE, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(items, output, Blocks.STONE, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(items, output, Blocks.STONE, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_ARCS, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_ARCS, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_CURVES, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_CURVES, TFBlocks.TERRORCOTTA_LINES);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_LINES, TFBlocks.TERRORCOTTA_ARCS);
		stonecutting(items, output, TFBlocks.TERRORCOTTA_LINES, TFBlocks.TERRORCOTTA_CURVES);
		stonecutting(items, output, TFBlocks.UNDERBRICK, TFBlocks.UNDERBRICK_FLOOR);
	}
}
