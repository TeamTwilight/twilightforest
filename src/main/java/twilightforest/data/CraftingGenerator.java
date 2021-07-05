package twilightforest.data;

import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.item.recipe.UncraftingEnabledCondition;

import java.nio.file.Path;
import java.util.function.Consumer;

public class CraftingGenerator extends CraftingDataHelper {
	public CraftingGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void saveRecipeAdvancement(DirectoryCache p_208310_1_, JsonObject p_208310_2_, Path p_208310_3_) {
		//Silence. This just makes it so that we don't gen advancements
		//TODO Recipe advancements control the unlock of a recipe, so if we ever consider actually making them, recipes should unlock based on also possible prerequisite conditions, instead of ONLY obtaining the item itself
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		// The Recipe Builder currently doesn't support enchantment-resulting recipes, those must be manually created.
		blockCompressionRecipes(consumer);
		equipmentRecipes(consumer);
		emptyMapRecipes(consumer);
		woodRecipes(consumer);
		fieryConversions(consumer);

		nagastoneRecipes(consumer);
		darkTowerRecipes(consumer);
		castleRecipes(consumer);

		slabBlock(consumer, "aurora_slab", TFBlocks.aurora_slab, TFBlocks.aurora_block);
		ShapedRecipeBuilder.shapedRecipe(TFBlocks.aurora_pillar.get(), 2)
				.patternLine("#")
				.patternLine("#")
				.key('#', Ingredient.fromItems(TFBlocks.aurora_block.get()))
				.addCriterion("has_" + TFBlocks.aurora_pillar.getId().getPath(), hasItem(TFBlocks.aurora_pillar.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.iron_ladder.get(), 3)
				.patternLine("-#-")
				.patternLine("-#-")
				.key('#', Ingredient.fromItems(Blocks.IRON_BARS))
				.key('-', Tags.Items.NUGGETS_IRON)
				.addCriterion("has_" + TFBlocks.iron_ladder.getId().getPath(), hasItem(TFBlocks.iron_ladder.get()))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.firefly_jar.get())
				.addIngredient(Ingredient.fromItems(TFBlocks.firefly.get()))
				.addIngredient(Ingredient.fromItems(Items.GLASS_BOTTLE))
				.addCriterion("has_item", hasItem(TFBlocks.firefly.get()))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.cicada_jar.get())
				.addIngredient(Ingredient.fromItems(TFBlocks.cicada.get()))
				.addIngredient(Ingredient.fromItems(Items.GLASS_BOTTLE))
				.addCriterion("has_item", hasItem(TFBlocks.cicada.get()))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(Items.STICK)
				.addIngredient(Ingredient.fromItems(TFBlocks.root_strand.get()))
				.addCriterion("has_item", hasItem(TFBlocks.root_strand.get()))
				.build(consumer, TwilightForestMod.prefix("root_stick"));

		ShapedRecipeBuilder.shapedRecipe(Blocks.TORCH, 5)
				.patternLine("∴")
				.patternLine("|")
				.key('∴', Ingredient.fromItems(TFItems.torchberries.get()))
				.key('|', Tags.Items.RODS_WOODEN)
				.addCriterion("has_item", hasItem(TFItems.torchberries.get()))
				.build(consumer, TwilightForestMod.prefix("berry_torch"));

		ConditionalRecipe.builder()
				.addCondition(new UncraftingEnabledCondition())
				.addRecipe(ShapedRecipeBuilder.shapedRecipe(TFBlocks.uncrafting_table.get())
						.patternLine("###")
						.patternLine("#X#")
						.patternLine("###")
						.key('#', Blocks.CRAFTING_TABLE)
						.key('X', TFItems.maze_map_focus.get())
						.addCriterion("has_uncrafting_table", hasItem(TFBlocks.uncrafting_table.get()))
						::build)
				.build(consumer, TwilightForestMod.prefix("uncrafting_table"));

		// Patchouli books would also go here, except they also must craft-result with NBT data.

		cookingRecipes(consumer, "smelted", IRecipeSerializer.SMELTING, 200);
		cookingRecipes(consumer, "smoked", IRecipeSerializer.SMOKING, 100);
		cookingRecipes(consumer, "campfired", IRecipeSerializer.CAMPFIRE_COOKING, 600);

		ingotRecipes(consumer, "smelted", IRecipeSerializer.SMELTING, 200);
		ingotRecipes(consumer, "blasted", IRecipeSerializer.BLASTING, 100);
		
		crackedWoodRecipes(consumer, "smoked", IRecipeSerializer.SMOKING, 100);
		crackedStoneRecipes(consumer, "smelted", IRecipeSerializer.SMELTING, 200);

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.armor_shard_cluster.get())
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addIngredient(Ingredient.fromItems(TFItems.armor_shard.get()))
				.addCriterion("has_item", hasItem(TFItems.armor_shard.get()))
				.build(consumer, TwilightForestMod.prefix("material/" + TFItems.armor_shard_cluster.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.underbrick_mossy.get(), 1)
				.addIngredient(Ingredient.fromItems(Blocks.VINE))
				.addIngredient(Ingredient.fromItems(TFBlocks.underbrick.get()))
				.addCriterion("has_item", hasItem(TFBlocks.underbrick.get()))
				.build(consumer, TwilightForestMod.prefix("underbrick_mossy"));

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.maze_stone_mossy.get(), 1)
				.addIngredient(Ingredient.fromItems(Blocks.VINE))
				.addIngredient(Ingredient.fromItems(TFBlocks.maze_stone_brick.get()))
				.addCriterion("has_item", hasItem(TFBlocks.maze_stone_brick.get()))
				.build(consumer, TwilightForestMod.prefix("maze_stone/maze_stone_mossy"));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.carminite.get())
				.addIngredient(Ingredient.fromItems(TFItems.borer_essence.get()))
				.addIngredient(Ingredient.fromItems(TFItems.borer_essence.get()))
				.addIngredient(Ingredient.fromItems(TFItems.borer_essence.get()))
				.addIngredient(Ingredient.fromItems(TFItems.borer_essence.get()))
				.addIngredient(Ingredient.fromItems(Items.GHAST_TEAR))
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addCriterion("has_item", hasItem(TFItems.borer_essence.get()))
				.build(consumer, TwilightForestMod.prefix("material/" + TFItems.carminite.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.ironwood_raw.get())
				.addIngredient(Ingredient.fromItems(TFItems.liveroot.get()))
				.addIngredient(Tags.Items.INGOTS_IRON)
				.addIngredient(Tags.Items.NUGGETS_GOLD)
				.addCriterion("has_item", hasItem(TFItems.liveroot.get()))
				.build(consumer, TwilightForestMod.prefix("material/" + TFItems.ironwood_raw.getId().getPath()));
	}

	private void darkTowerRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(TFBlocks.encased_fire_jet.get())
				.patternLine("#∴#")
				.patternLine("∴^∴")
				.patternLine("uuu")
				.key('∴', Tags.Items.DUSTS_REDSTONE)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('^', Ingredient.fromItems(TFBlocks.fire_jet.get()))
				.key('u', Ingredient.fromItems(Items.LAVA_BUCKET))
				.addCriterion("has_item", hasItem(TFBlocks.fire_jet.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.encased_smoker.get())
				.patternLine("#∴#")
				.patternLine("∴^∴")
				.patternLine("#∴#")
				.key('∴', Tags.Items.DUSTS_REDSTONE)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('^', Ingredient.fromItems(TFBlocks.smoker.get()))
				.addCriterion("has_item", hasItem(TFBlocks.smoker.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.carminite_builder.get())
				.patternLine("#6#")
				.patternLine("6o6")
				.patternLine("#6#")
				.key('6', ItemTagGenerator.CARMINITE_GEMS)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('o', Ingredient.fromItems(Blocks.DISPENSER))
				.addCriterion("has_item", hasItem(TFItems.carminite.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.carminite_reactor.get())
				.patternLine("#6#")
				.patternLine("6%6")
				.patternLine("#6#")
				.key('6', ItemTagGenerator.CARMINITE_GEMS)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('%', Tags.Items.ORES_REDSTONE)
				.addCriterion("has_item", hasItem(TFBlocks.carminite_reactor.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.reappearing_block.get(), 2)
				.patternLine("#∴#")
				.patternLine("∴6∴")
				.patternLine("#∴#")
				.key('∴', Tags.Items.DUSTS_REDSTONE)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('6', ItemTagGenerator.CARMINITE_GEMS)
				.addCriterion("has_item", hasItem(TFBlocks.reappearing_block.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFBlocks.vanishing_block.get(), 8)
				.patternLine("#w#")
				.patternLine("w6w")
				.patternLine("#w#")
				.key('w', ItemTagGenerator.TOWERWOOD)
				.key('#', Ingredient.fromItems(TFBlocks.tower_wood_encased.get()))
				.key('6', ItemTagGenerator.CARMINITE_GEMS)
				.addCriterion("has_item", hasItem(TFBlocks.reappearing_block.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.tower_wood_mossy.get())
			.addIngredient(Ingredient.fromItems(TFBlocks.tower_wood.get()))
			.addIngredient(Items.VINE)
			.addCriterion("has_item", hasItem(TFBlocks.tower_wood.get()))
			.build(consumer, TwilightForestMod.prefix("wood/" + TFBlocks.tower_wood_mossy.getId().getPath()));

	}

	private void equipmentRecipes(Consumer<IFinishedRecipe> consumer) {
		bootsItem(consumer, "arctic_boots", TFItems.arctic_boots, ItemTagGenerator.ARCTIC_FUR);
		chestplateItem(consumer, "arctic_chestplate", TFItems.arctic_chestplate, ItemTagGenerator.ARCTIC_FUR);
		helmetItem(consumer, "arctic_helmet", TFItems.arctic_helmet, ItemTagGenerator.ARCTIC_FUR);
		leggingsItem(consumer, "arctic_leggings", TFItems.arctic_leggings, ItemTagGenerator.ARCTIC_FUR);

		bootsItem(consumer, "fiery_boots", TFItems.fiery_boots, ItemTagGenerator.FIERY_INGOTS);
		chestplateItem(consumer, "fiery_chestplate", TFItems.fiery_chestplate, ItemTagGenerator.FIERY_INGOTS);
		helmetItem(consumer, "fiery_helmet", TFItems.fiery_helmet, ItemTagGenerator.FIERY_INGOTS);
		leggingsItem(consumer, "fiery_leggings", TFItems.fiery_leggings, ItemTagGenerator.FIERY_INGOTS);
		swordItem(consumer, "fiery_sword", TFItems.fiery_sword, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);
		pickaxeItem(consumer, "fiery_pickaxe", TFItems.fiery_pickaxe, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);

		bootsItem(consumer, "knightmetal_boots", TFItems.knightmetal_boots, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		chestplateItem(consumer, "knightmetal_chestplate", TFItems.knightmetal_chestplate, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		helmetItem(consumer, "knightmetal_helmet", TFItems.knightmetal_helmet, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		leggingsItem(consumer, "knightmetal_leggings", TFItems.knightmetal_leggings, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		pickaxeItem(consumer, "knightmetal_pickaxe", TFItems.knightmetal_pickaxe, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		swordItem(consumer, "knightmetal_sword", TFItems.knightmetal_sword, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		axeItem(consumer, "knightmetal_axe", TFItems.knightmetal_axe, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shapedRecipe(TFItems.giant_pickaxe.get())
				.patternLine("###")
				.patternLine(" X ")
				.patternLine(" X ")
				.key('#', TFBlocks.giant_cobblestone.get())
				.key('X', TFBlocks.giant_log.get())
				.addCriterion("has_item", hasItem(TFBlocks.giant_cobblestone.get()))
				.build(consumer, locEquip(TFItems.giant_pickaxe.getId().getPath()));

		ShapedRecipeBuilder.shapedRecipe(TFItems.giant_sword.get())
				.patternLine(" # ")
				.patternLine(" # ")
				.patternLine(" X ")
				.key('#', TFBlocks.giant_cobblestone.get())
				.key('X', TFBlocks.giant_log.get())
				.addCriterion("has_item", hasItem(TFBlocks.giant_cobblestone.get()))
				.build(consumer, locEquip(TFItems.giant_sword.getId().getPath()));

		charmRecipe(consumer, "charm_of_keeping_2", TFItems.charm_of_keeping_2, TFItems.charm_of_keeping_1);
		charmRecipe(consumer, "charm_of_keeping_3", TFItems.charm_of_keeping_3, TFItems.charm_of_keeping_2);
		charmRecipe(consumer, "charm_of_life_2", TFItems.charm_of_life_2, TFItems.charm_of_life_1);

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.moonworm_queen.get())
				.addIngredient(TFItems.moonworm_queen.get())
				.addIngredient(TFItems.torchberries.get(), 3)
				.addCriterion("has_item", hasItem(TFItems.moonworm_queen.get()))
				.build(consumer, TwilightForestMod.prefix("moonworm_queen"));

		ShapelessRecipeBuilder.shapelessRecipe(Blocks.COBBLESTONE, 64)
				.addIngredient(TFBlocks.giant_cobblestone.get())
				.addCriterion("has_item", hasItem(TFBlocks.giant_cobblestone.get()))
				.build(consumer, TwilightForestMod.prefix(TFBlocks.giant_cobblestone.getId().getPath() + "_to_" + Blocks.COBBLESTONE.asItem().getRegistryName().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_PLANKS, 64)
				.addIngredient(TFBlocks.giant_log.get())
				.addCriterion("has_item", hasItem(TFBlocks.giant_log.get()))
				.build(consumer, TwilightForestMod.prefix(TFBlocks.giant_log.getId().getPath() + "_to_" + Blocks.OAK_PLANKS.asItem().getRegistryName().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_LEAVES, 64)
				.addIngredient(TFBlocks.giant_leaves.get())
				.addCriterion("has_item", hasItem(TFBlocks.giant_log.get()))
				.build(consumer, TwilightForestMod.prefix(TFBlocks.giant_leaves.getId().getPath() + "_to_" + Blocks.OAK_LEAVES.asItem().getRegistryName().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.block_and_chain.get())
				.addIngredient(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL)
				.addIngredient(ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.addIngredient(ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.addIngredient(ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.addIngredient(Ingredient.fromItems(TFItems.knightmetal_ring.get()))
				.addCriterion("has_item", hasItem(TFBlocks.knightmetal_block.get()))
				.build(consumer, locEquip(TFItems.block_and_chain.getId().getPath()));

		ShapedRecipeBuilder.shapedRecipe(TFItems.knightmetal_ring.get())
				.patternLine(" - ")
				.patternLine("- -")
				.patternLine(" - ")
				.key('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.addCriterion("has_item", hasItem(TFItems.knightmetal_ingot.get()))
				.build(consumer, locEquip(TFItems.knightmetal_ring.getId().getPath()));

		ShapedRecipeBuilder.shapedRecipe(TFItems.knightmetal_shield.get())
				.patternLine("-#")
				.patternLine("-o")
				.patternLine("-#")
				.key('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.key('#', ItemTagGenerator.TOWERWOOD)
				.key('o', Ingredient.fromItems(TFItems.knightmetal_ring.get()))
				.addCriterion("has_item", hasItem(TFItems.knightmetal_ingot.get()))
				.build(consumer, locEquip(TFItems.knightmetal_shield.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.lifedrain_scepter.get())
				.addIngredient(itemWithNBT(TFItems.lifedrain_scepter, nbt -> nbt.putInt("Damage", TFItems.lifedrain_scepter.get().getMaxDamage())))
				.addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE))
				.addCriterion("has_item", hasItem(TFItems.lifedrain_scepter.get()))
				.build(consumer, locEquip(TFItems.lifedrain_scepter.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.shield_scepter.get())
				.addIngredient(itemWithNBT(TFItems.shield_scepter, nbt -> nbt.putInt("Damage", TFItems.shield_scepter.get().getMaxDamage())))
				.addIngredient(Ingredient.fromItems(Items.GOLDEN_APPLE))
				.addCriterion("has_item", hasItem(TFItems.shield_scepter.get()))
				.build(consumer, locEquip(TFItems.shield_scepter.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.twilight_scepter.get())
				.addIngredient(itemWithNBT(TFItems.twilight_scepter, nbt -> nbt.putInt("Damage", TFItems.twilight_scepter.get().getMaxDamage())))
				.addIngredient(Tags.Items.ENDER_PEARLS)
				.addCriterion("has_item", hasItem(TFItems.twilight_scepter.get()))
				.build(consumer, locEquip(TFItems.twilight_scepter.getId().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.zombie_scepter.get())
				.addIngredient(multipleIngredients(
						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:strength")),
						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:strong_strength")),
						itemWithNBT(Items.POTION, nbt -> nbt.putString("Potion", "minecraft:long_strength"))
				))
				.addIngredient(itemWithNBT(TFItems.zombie_scepter, nbt -> nbt.putInt("Damage", TFItems.zombie_scepter.get().getMaxDamage())))
				.addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH))
				.addCriterion("has_item", hasItem(TFItems.zombie_scepter.get()))
				.build(consumer, locEquip(TFItems.zombie_scepter.getId().getPath()));

		// Testing
		//ShapelessRecipeBuilder.shapelessRecipe(TFItems.zombie_scepter.get())
		//		.addIngredient(multipleIngredients(
		//				Ingredient.fromTag(Tags.Items.GEMS_DIAMOND),
		//				Ingredient.fromItems(Items.BEDROCK)
		//		))
		//		.addIngredient(itemWithNBT(TFItems.zombie_scepter, nbt -> nbt.putInt("Damage", TFItems.zombie_scepter.get().getMaxDamage())))
		//		.addIngredient(Ingredient.fromItems(Items.ROTTEN_FLESH))
		//		.addIngredient(Tags.Items.GEMS_EMERALD)
		//		.addCriterion("has_item", hasItem(TFItems.zombie_scepter.get()))
		//		.build(consumer, locEquip(TFItems.zombie_scepter.getId().getPath() + "_rv"));
	}

	private void blockCompressionRecipes(Consumer<IFinishedRecipe> consumer) {
		reverseCompressBlock(consumer, "arctic_block_to_item", TFItems.arctic_fur, ItemTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR);
		reverseCompressBlock(consumer, "carminite_block_to_item", TFItems.carminite, ItemTagGenerator.STORAGE_BLOCKS_CARMINITE);
		reverseCompressBlock(consumer, "fiery_block_to_ingot", TFItems.fiery_ingot, ItemTagGenerator.STORAGE_BLOCKS_FIERY);
		reverseCompressBlock(consumer, "ironwood_block_ingot", TFItems.ironwood_ingot, ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD);
		reverseCompressBlock(consumer, "knightmetal_block_ingot", TFItems.knightmetal_ingot,ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL);
		reverseCompressBlock(consumer, "steeleaf_block_ingot", TFItems.steeleaf_ingot, ItemTagGenerator.STORAGE_BLOCKS_STEELEAF);

		compressedBlock(consumer, "arctic_block", TFBlocks.arctic_fur_block, ItemTagGenerator.ARCTIC_FUR);
		compressedBlock(consumer, "carminite_block", TFBlocks.carminite_block, ItemTagGenerator.CARMINITE_GEMS);
		compressedBlock(consumer, "fiery_block", TFBlocks.fiery_block, ItemTagGenerator.FIERY_INGOTS);
		compressedBlock(consumer, "ironwood_block", TFBlocks.ironwood_block, ItemTagGenerator.IRONWOOD_INGOTS);
		compressedBlock(consumer, "knightmetal_block", TFBlocks.knightmetal_block, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		compressedBlock(consumer, "steeleaf_block", TFBlocks.steeleaf_block, ItemTagGenerator.STEELEAF_INGOTS);
	}

	private void emptyMapRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(TFItems.magic_map_focus.get())
				.addIngredient(TFItems.raven_feather.get())
				.addIngredient(TFItems.torchberries.get())
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addCriterion("has_item", hasItem(TFItems.torchberries.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFItems.magic_map_empty.get())
				.patternLine("###")
				.patternLine("#•#")
				.patternLine("###")
				.key('#', ItemTagGenerator.PAPER)
				.key('•', Ingredient.fromItems(TFItems.magic_map_focus.get()))
				.addCriterion("has_item", hasItem(TFItems.magic_map_focus.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(TFItems.maze_map_empty.get())
				.patternLine("###")
				.patternLine("#•#")
				.patternLine("###")
				.key('#', ItemTagGenerator.PAPER)
				.key('•', Ingredient.fromItems(TFItems.maze_map_focus.get()))
				.addCriterion("has_item", hasItem(TFItems.maze_map_focus.get()))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.ore_map_empty.get())
				.addIngredient(TFItems.maze_map_empty.get())
				.addIngredient(Tags.Items.STORAGE_BLOCKS_DIAMOND)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_GOLD)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_IRON)
				.addCriterion("has_item", hasItem(TFItems.ore_magnet.get()))
				.build(consumer);
	}

	private void woodRecipes(Consumer<IFinishedRecipe> consumer) {
		buttonBlock(consumer, "canopy", TFBlocks.canopy_button, TFBlocks.canopy_planks);
		buttonBlock(consumer, "darkwood", TFBlocks.dark_button, TFBlocks.dark_planks);
		buttonBlock(consumer, "mangrove", TFBlocks.mangrove_button, TFBlocks.mangrove_planks);
		buttonBlock(consumer, "mine", TFBlocks.mine_button, TFBlocks.mine_planks);
		buttonBlock(consumer, "sort", TFBlocks.sort_button, TFBlocks.sort_planks);
		buttonBlock(consumer, "time", TFBlocks.time_button, TFBlocks.time_planks);
		buttonBlock(consumer, "trans", TFBlocks.trans_button, TFBlocks.trans_planks);
		buttonBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_button, TFBlocks.twilight_oak_planks);

		doorBlock(consumer, "canopy", TFBlocks.canopy_door, TFBlocks.canopy_planks);
		doorBlock(consumer, "darkwood", TFBlocks.dark_door, TFBlocks.dark_planks);
		doorBlock(consumer, "mangrove", TFBlocks.mangrove_door, TFBlocks.mangrove_planks);
		doorBlock(consumer, "mine", TFBlocks.mine_door, TFBlocks.mine_planks);
		doorBlock(consumer, "sort", TFBlocks.sort_door, TFBlocks.sort_planks);
		doorBlock(consumer, "time", TFBlocks.time_door, TFBlocks.time_planks);
		doorBlock(consumer, "trans", TFBlocks.trans_door, TFBlocks.trans_planks);
		doorBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_door, TFBlocks.twilight_oak_planks);

		fenceBlock(consumer, "canopy", TFBlocks.canopy_fence, TFBlocks.canopy_planks);
		fenceBlock(consumer, "darkwood", TFBlocks.dark_fence, TFBlocks.dark_planks);
		fenceBlock(consumer, "mangrove", TFBlocks.mangrove_fence, TFBlocks.mangrove_planks);
		fenceBlock(consumer, "mine", TFBlocks.mine_fence, TFBlocks.mine_planks);
		fenceBlock(consumer, "sort", TFBlocks.sort_fence, TFBlocks.sort_planks);
		fenceBlock(consumer, "time", TFBlocks.time_fence, TFBlocks.time_planks);
		fenceBlock(consumer, "trans", TFBlocks.trans_fence, TFBlocks.trans_planks);
		fenceBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_fence, TFBlocks.twilight_oak_planks);

		gateBlock(consumer, "canopy", TFBlocks.canopy_gate, TFBlocks.canopy_planks);
		gateBlock(consumer, "darkwood", TFBlocks.dark_gate, TFBlocks.dark_planks);
		gateBlock(consumer, "mangrove", TFBlocks.mangrove_gate, TFBlocks.mangrove_planks);
		gateBlock(consumer, "mine", TFBlocks.mine_gate, TFBlocks.mine_planks);
		gateBlock(consumer, "sort", TFBlocks.sort_gate, TFBlocks.sort_planks);
		gateBlock(consumer, "time", TFBlocks.time_gate, TFBlocks.time_planks);
		gateBlock(consumer, "trans", TFBlocks.trans_gate, TFBlocks.trans_planks);
		gateBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_gate, TFBlocks.twilight_oak_planks);

		planksBlock(consumer, "canopy", TFBlocks.canopy_planks, TFBlocks.canopy_log);
		planksBlock(consumer, "darkwood", TFBlocks.dark_planks, TFBlocks.dark_log);
		planksBlock(consumer, "mangrove", TFBlocks.mangrove_planks, TFBlocks.mangrove_log);
		planksBlock(consumer, "mine", TFBlocks.mine_planks, TFBlocks.mining_log);
		planksBlock(consumer, "sort", TFBlocks.sort_planks, TFBlocks.sorting_log);
		planksBlock(consumer, "time", TFBlocks.time_planks, TFBlocks.time_log);
		planksBlock(consumer, "trans", TFBlocks.trans_planks, TFBlocks.transformation_log);
		planksBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_planks, TFBlocks.oak_log);

		planksBlock(consumer, "canopy_from_stripped", TFBlocks.canopy_planks, TFBlocks.stripped_canopy_log);
		planksBlock(consumer, "darkwood_from_stripped", TFBlocks.dark_planks, TFBlocks.stripped_dark_log);
		planksBlock(consumer, "mangrove_from_stripped", TFBlocks.mangrove_planks, TFBlocks.stripped_mangrove_log);
		planksBlock(consumer, "mine_from_stripped", TFBlocks.mine_planks, TFBlocks.stripped_mining_log);
		planksBlock(consumer, "sort_from_stripped", TFBlocks.sort_planks, TFBlocks.stripped_sorting_log);
		planksBlock(consumer, "time_from_stripped", TFBlocks.time_planks, TFBlocks.stripped_time_log);
		planksBlock(consumer, "trans_from_stripped", TFBlocks.trans_planks, TFBlocks.stripped_transformation_log);
		planksBlock(consumer, "twilight_oak_from_stripped", TFBlocks.twilight_oak_planks, TFBlocks.stripped_oak_log);

		planksBlock(consumer, "canopy_from_wood", TFBlocks.canopy_planks, TFBlocks.canopy_wood);
		planksBlock(consumer, "darkwood_from_wood", TFBlocks.dark_planks, TFBlocks.dark_wood);
		planksBlock(consumer, "mangrove_from_wood", TFBlocks.mangrove_planks, TFBlocks.mangrove_wood);
		planksBlock(consumer, "mine_from_wood", TFBlocks.mine_planks, TFBlocks.mining_wood);
		planksBlock(consumer, "sort_from_wood", TFBlocks.sort_planks, TFBlocks.sorting_wood);
		planksBlock(consumer, "time_from_wood", TFBlocks.time_planks, TFBlocks.time_wood);
		planksBlock(consumer, "trans_from_wood", TFBlocks.trans_planks, TFBlocks.transformation_wood);
		planksBlock(consumer, "twilight_oak_from_wood", TFBlocks.twilight_oak_planks, TFBlocks.oak_wood);

		planksBlock(consumer, "canopy_from_stripped_wood", TFBlocks.canopy_planks, TFBlocks.stripped_canopy_wood);
		planksBlock(consumer, "darkwood_from_stripped_wood", TFBlocks.dark_planks, TFBlocks.stripped_dark_wood);
		planksBlock(consumer, "mangrove_from_stripped_wood", TFBlocks.mangrove_planks, TFBlocks.stripped_mangrove_wood);
		planksBlock(consumer, "mine_from_stripped_wood", TFBlocks.mine_planks, TFBlocks.stripped_mining_wood);
		planksBlock(consumer, "sort_from_stripped_wood", TFBlocks.sort_planks, TFBlocks.stripped_sorting_wood);
		planksBlock(consumer, "time_from_stripped_wood", TFBlocks.time_planks, TFBlocks.stripped_time_wood);
		planksBlock(consumer, "trans_from_stripped_wood", TFBlocks.trans_planks, TFBlocks.stripped_transformation_wood);
		planksBlock(consumer, "twilight_oak_from_stripped_wood", TFBlocks.twilight_oak_planks, TFBlocks.stripped_oak_wood);

		woodBlock(consumer, "canopy", TFBlocks.canopy_wood, TFBlocks.canopy_log);
		woodBlock(consumer, "darkwood", TFBlocks.dark_wood, TFBlocks.dark_log);
		woodBlock(consumer, "mangrove", TFBlocks.mangrove_wood, TFBlocks.mangrove_log);
		woodBlock(consumer, "mine", TFBlocks.mining_wood, TFBlocks.mining_log);
		woodBlock(consumer, "sort", TFBlocks.sorting_wood, TFBlocks.sorting_log);
		woodBlock(consumer, "time", TFBlocks.time_wood, TFBlocks.time_log);
		woodBlock(consumer, "trans", TFBlocks.transformation_wood, TFBlocks.transformation_log);
		woodBlock(consumer, "twilight_oak", TFBlocks.oak_wood, TFBlocks.oak_log);

		strippedWoodBlock(consumer, "canopy", TFBlocks.stripped_canopy_wood, TFBlocks.stripped_canopy_log);
		strippedWoodBlock(consumer, "darkwood", TFBlocks.stripped_dark_wood, TFBlocks.stripped_dark_log);
		strippedWoodBlock(consumer, "mangrove", TFBlocks.stripped_mangrove_wood, TFBlocks.stripped_mangrove_log);
		strippedWoodBlock(consumer, "mine", TFBlocks.stripped_mining_wood, TFBlocks.stripped_mining_log);
		strippedWoodBlock(consumer, "sort", TFBlocks.stripped_sorting_wood, TFBlocks.stripped_sorting_log);
		strippedWoodBlock(consumer, "time", TFBlocks.stripped_time_wood, TFBlocks.stripped_time_log);
		strippedWoodBlock(consumer, "trans", TFBlocks.stripped_transformation_wood, TFBlocks.stripped_transformation_log);
		strippedWoodBlock(consumer, "twilight_oak", TFBlocks.stripped_oak_wood, TFBlocks.stripped_oak_log);

		plateBlock(consumer, "canopy", TFBlocks.canopy_plate, TFBlocks.canopy_planks);
		plateBlock(consumer, "darkwood", TFBlocks.dark_plate, TFBlocks.dark_planks);
		plateBlock(consumer, "mangrove", TFBlocks.mangrove_plate, TFBlocks.mangrove_planks);
		plateBlock(consumer, "mine", TFBlocks.mine_plate, TFBlocks.mine_planks);
		plateBlock(consumer, "sort", TFBlocks.sort_plate, TFBlocks.sort_planks);
		plateBlock(consumer, "time", TFBlocks.time_plate, TFBlocks.time_planks);
		plateBlock(consumer, "trans", TFBlocks.trans_plate, TFBlocks.trans_planks);
		plateBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_plate, TFBlocks.twilight_oak_planks);

		slabBlock(consumer, "canopy", TFBlocks.canopy_slab, TFBlocks.canopy_planks);
		slabBlock(consumer, "darkwood", TFBlocks.dark_slab, TFBlocks.dark_planks);
		slabBlock(consumer, "mangrove", TFBlocks.mangrove_slab, TFBlocks.mangrove_planks);
		slabBlock(consumer, "mine", TFBlocks.mine_slab, TFBlocks.mine_planks);
		slabBlock(consumer, "sort", TFBlocks.sort_slab, TFBlocks.sort_planks);
		slabBlock(consumer, "time", TFBlocks.time_slab, TFBlocks.time_planks);
		slabBlock(consumer, "trans", TFBlocks.trans_slab, TFBlocks.trans_planks);
		slabBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_slab, TFBlocks.twilight_oak_planks);

		stairsBlock(consumer, locWood("canopy_stairs"), TFBlocks.canopy_stairs, TFBlocks.canopy_planks, TFBlocks.canopy_planks.get());
		stairsBlock(consumer, locWood("darkwood_stairs"), TFBlocks.dark_stairs, TFBlocks.dark_planks, TFBlocks.dark_planks.get());
		stairsBlock(consumer, locWood("mangrove_stairs"), TFBlocks.mangrove_stairs, TFBlocks.mangrove_planks, TFBlocks.mangrove_planks.get());
		stairsBlock(consumer, locWood("mine_stairs"), TFBlocks.mine_stairs, TFBlocks.mine_planks, TFBlocks.mine_planks.get());
		stairsBlock(consumer, locWood("sort_stairs"), TFBlocks.sort_stairs, TFBlocks.sort_planks, TFBlocks.sort_planks.get());
		stairsBlock(consumer, locWood("time_stairs"), TFBlocks.time_stairs, TFBlocks.time_planks, TFBlocks.time_planks.get());
		stairsBlock(consumer, locWood("trans_stairs"), TFBlocks.trans_stairs, TFBlocks.trans_planks, TFBlocks.trans_planks.get());
		stairsBlock(consumer, locWood("twilight_oak_stairs"), TFBlocks.twilight_oak_stairs, TFBlocks.twilight_oak_planks, TFBlocks.twilight_oak_planks.get());

		trapdoorBlock(consumer, "canopy", TFBlocks.canopy_trapdoor, TFBlocks.canopy_planks);
		trapdoorBlock(consumer, "darkwood", TFBlocks.dark_trapdoor, TFBlocks.dark_planks);
		trapdoorBlock(consumer, "mangrove", TFBlocks.mangrove_trapdoor, TFBlocks.mangrove_planks);
		trapdoorBlock(consumer, "mine", TFBlocks.mine_trapdoor, TFBlocks.mine_planks);
		trapdoorBlock(consumer, "sort", TFBlocks.sort_trapdoor, TFBlocks.sort_planks);
		trapdoorBlock(consumer, "time", TFBlocks.time_trapdoor, TFBlocks.time_planks);
		trapdoorBlock(consumer, "trans", TFBlocks.trans_trapdoor, TFBlocks.trans_planks);
		trapdoorBlock(consumer, "twilight_oak", TFBlocks.twilight_oak_trapdoor, TFBlocks.twilight_oak_planks);

		signBlock(consumer, "canopy_sign", TFBlocks.canopy_sign, TFBlocks.canopy_planks);
		signBlock(consumer, "darkwood_sign", TFBlocks.darkwood_sign, TFBlocks.dark_planks);
		signBlock(consumer, "mangrove_sign", TFBlocks.mangrove_sign, TFBlocks.mangrove_planks);
		signBlock(consumer, "mine_sign", TFBlocks.mine_sign, TFBlocks.mine_planks);
		signBlock(consumer, "sort_sign", TFBlocks.sort_sign, TFBlocks.sort_planks);
		signBlock(consumer, "time_sign", TFBlocks.time_sign, TFBlocks.time_planks);
		signBlock(consumer, "trans_sign", TFBlocks.trans_sign, TFBlocks.trans_planks);
		signBlock(consumer, "twilight_oak_sign", TFBlocks.twilight_oak_sign, TFBlocks.twilight_oak_planks);

	}

	private void nagastoneRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(TFBlocks.spiral_bricks.get(), 8)
				.patternLine("BSS")
				.patternLine("BSS")
				.patternLine("BBB")
				.key('B', Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
				.key('S', Ingredient.fromItems(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
				.addCriterion("has_item", hasItem(TFBlocks.spiral_bricks.get()))
				.build(consumer, locNaga("nagastone_spiral"));

		stairsBlock(consumer, locNaga("nagastone_stairs_left"), TFBlocks.nagastone_stairs_left, TFBlocks.etched_nagastone, TFBlocks.etched_nagastone.get());
		stairsRightBlock(consumer, locNaga("nagastone_stairs_right"), TFBlocks.nagastone_stairs_right, TFBlocks.etched_nagastone, TFBlocks.etched_nagastone.get());

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.etched_nagastone.get(), 3)
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_left.get(), TFBlocks.nagastone_stairs_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_left.get(), TFBlocks.nagastone_stairs_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_left.get(), TFBlocks.nagastone_stairs_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_left.get(), TFBlocks.nagastone_stairs_right.get()))
				.addCriterion("has_item", hasItem(TFBlocks.etched_nagastone.get()))
				.build(consumer, locNaga("nagastone_stairs_reverse"));

		stairsBlock(consumer, locNaga("nagastone_stairs_left_mossy"), TFBlocks.nagastone_stairs_mossy_left, TFBlocks.etched_nagastone_mossy, TFBlocks.etched_nagastone_mossy.get());
		stairsRightBlock(consumer, locNaga("nagastone_stairs_right_mossy"), TFBlocks.nagastone_stairs_mossy_right, TFBlocks.etched_nagastone_mossy, TFBlocks.etched_nagastone_mossy.get());

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.etched_nagastone_mossy.get(), 3)
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_mossy_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_mossy_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_mossy_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_mossy_left.get(), TFBlocks.nagastone_stairs_mossy_right.get()))
				.addCriterion("has_item", hasItem(TFBlocks.etched_nagastone_mossy.get()))
				.build(consumer, locNaga("nagastone_stairs_mossy_reverse"));
		
		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.etched_nagastone_mossy.get(), 1)
			.addIngredient(Ingredient.fromItems(Blocks.VINE))
			.addIngredient(Ingredient.fromItems(TFBlocks.etched_nagastone.get()))
			.addCriterion("has_item", hasItem(TFBlocks.etched_nagastone.get()))
			.build(consumer, locNaga("etched_nagastone_mossy"));
		
		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.nagastone_pillar_mossy.get(), 1)
			.addIngredient(Ingredient.fromItems(Blocks.VINE))
			.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_pillar.get()))
			.addCriterion("has_item", hasItem(TFBlocks.nagastone_pillar.get()))
			.build(consumer, locNaga("nagastone_pillar_mossy"));

		stairsBlock(consumer, locNaga("nagastone_stairs_left_weathered"), TFBlocks.nagastone_stairs_weathered_left, TFBlocks.etched_nagastone_weathered, TFBlocks.etched_nagastone_weathered.get());
		stairsRightBlock(consumer, locNaga("nagastone_stairs_right_weathered"), TFBlocks.nagastone_stairs_weathered_right, TFBlocks.etched_nagastone_weathered, TFBlocks.etched_nagastone_weathered.get());

		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.etched_nagastone_weathered.get(), 3)
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_weathered_left.get(), TFBlocks.nagastone_stairs_weathered_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_weathered_left.get(), TFBlocks.nagastone_stairs_weathered_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_weathered_left.get(), TFBlocks.nagastone_stairs_weathered_right.get()))
				.addIngredient(Ingredient.fromItems(TFBlocks.nagastone_stairs_weathered_left.get(), TFBlocks.nagastone_stairs_weathered_right.get()))
				.addCriterion("has_item", hasItem(TFBlocks.etched_nagastone_weathered.get()))
				.build(consumer, locNaga("nagastone_stairs_weathered_reverse"));
	}

	private void castleRecipes(Consumer<IFinishedRecipe> consumer) {
		castleBlock(consumer, "castle_paver", TFBlocks.castle_brick_frame, TFBlocks.castle_brick, TFBlocks.castle_brick.get(), TFBlocks.castle_brick_worn.get(), TFBlocks.castle_brick_cracked.get(), TFBlocks.castle_brick_mossy.get());
		castleBlock(consumer, "castle_pillar_bold", TFBlocks.castle_pillar_bold, TFBlocks.castle_brick_frame, TFBlocks.castle_brick_frame.get());
		castleBlock(consumer, "castle_pillar_bold_none", TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold_tile.get());
		ShapedRecipeBuilder.shapedRecipe(TFBlocks.castle_pillar_encased.get(), 6)
				.patternLine("#H#")
				.patternLine("#H#")
				.key('#', Ingredient.fromItems(TFBlocks.castle_brick.get(), TFBlocks.castle_brick_worn.get(), TFBlocks.castle_brick_cracked.get(), TFBlocks.castle_brick_mossy.get(), TFBlocks.castle_brick_frame.get()))
				.key('H', Ingredient.fromItems(TFBlocks.castle_pillar_encased.get(), TFBlocks.castle_pillar_encased_tile.get(), TFBlocks.castle_pillar_bold_tile.get()))
				.addCriterion("has_" + TFBlocks.castle_pillar_encased.get().getRegistryName().getPath(), hasItem(TFBlocks.castle_pillar_encased.get()))
				.build(consumer, locCastle("castle_pillar_encased"));
		castleBlock(consumer, "castle_pillar_encased_none", TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold_tile, TFBlocks.castle_pillar_bold.get(), TFBlocks.castle_pillar_bold_tile.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_bold"), TFBlocks.castle_stairs_bold, TFBlocks.castle_pillar_bold, TFBlocks.castle_pillar_bold.get(), TFBlocks.castle_pillar_bold_tile.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_bold_reverse"), TFBlocks.castle_pillar_bold, TFBlocks.castle_stairs_bold, TFBlocks.castle_stairs_bold.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_brick"), TFBlocks.castle_stairs_brick, TFBlocks.castle_brick, TFBlocks.castle_brick.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_brick_reverse"), TFBlocks.castle_brick, TFBlocks.castle_stairs_brick, TFBlocks.castle_stairs_brick.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_cracked"), TFBlocks.castle_stairs_cracked, TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_cracked.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_cracked_reverse"), TFBlocks.castle_brick_cracked, TFBlocks.castle_brick_cracked, TFBlocks.castle_stairs_cracked.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_encased"), TFBlocks.castle_stairs_encased, TFBlocks.castle_pillar_encased, TFBlocks.castle_pillar_encased.get(), TFBlocks.castle_pillar_encased_tile.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_encased_reverse"), TFBlocks.castle_pillar_encased, TFBlocks.castle_stairs_encased, TFBlocks.castle_stairs_encased.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_mossy"), TFBlocks.castle_stairs_mossy, TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_mossy.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_mossy_reverse"), TFBlocks.castle_brick_mossy, TFBlocks.castle_brick_mossy, TFBlocks.castle_stairs_mossy.get());
		stairsBlock(consumer, locCastle("castleblock_stairs_worn"), TFBlocks.castle_stairs_worn, TFBlocks.castle_brick_worn, TFBlocks.castle_brick_worn.get());
		reverseStairsBlock(consumer, locCastle("castleblock_stairs_worn_reverse"), TFBlocks.castle_brick_worn, TFBlocks.castle_brick_worn, TFBlocks.castle_stairs_worn.get());
		ShapelessRecipeBuilder.shapelessRecipe(TFBlocks.castle_brick_mossy.get(), 1)
		.addIngredient(Ingredient.fromItems(Blocks.VINE))
		.addIngredient(Ingredient.fromItems(TFBlocks.castle_brick.get()))
		.addCriterion("has_item", hasItem(TFBlocks.castle_brick.get()))
		.build(consumer, locCastle("castle_brick_mossy"));
	}

	private void fieryConversions(Consumer<IFinishedRecipe> consumer) {
		SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromTag(ItemTagGenerator.FIERY_VIAL), TFItems.fiery_ingot.get()).addCriterion("has_item", hasItem(TFItems.fiery_ingot.get())).build(consumer, TwilightForestMod.prefix("material/fiery_iron_ingot"));

		fieryConversion(consumer, TFItems.fiery_helmet, Items.IRON_HELMET, 5);
		fieryConversion(consumer, TFItems.fiery_chestplate, Items.IRON_CHESTPLATE, 8);
		fieryConversion(consumer, TFItems.fiery_leggings, Items.IRON_LEGGINGS, 7);
		fieryConversion(consumer, TFItems.fiery_boots, Items.IRON_BOOTS, 4);
		ShapelessRecipeBuilder.shapelessRecipe(TFItems.fiery_sword.get())
				.addIngredient(Items.IRON_SWORD)
				.addIngredient(Ingredient.fromTag(ItemTagGenerator.FIERY_VIAL), 2)
				.addIngredient(Ingredient.fromTag(Tags.Items.RODS_BLAZE))
				.addCriterion("has_item", hasItem(ItemTagGenerator.FIERY_VIAL))
				.build(consumer, locEquip("fiery_" + Items.IRON_SWORD.getRegistryName().getPath()));

		ShapelessRecipeBuilder.shapelessRecipe(TFItems.fiery_pickaxe.get())
				.addIngredient(Items.IRON_PICKAXE)
				.addIngredient(Ingredient.fromTag(ItemTagGenerator.FIERY_VIAL), 3)
				.addIngredient(Ingredient.fromTag(Tags.Items.RODS_BLAZE), 2)
				.addCriterion("has_item", hasItem(ItemTagGenerator.FIERY_VIAL))
				.build(consumer, locEquip("fiery_" + Items.IRON_PICKAXE.getRegistryName().getPath()));
	}
	private void cookingRecipes(Consumer<IFinishedRecipe> consumer, String processName, CookingRecipeSerializer<?> process, int smeltingTime) {
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFItems.raw_meef.get()), TFItems.cooked_meef.get(), 0.3f, smeltingTime, process).addCriterion("has_food", hasItem(TFItems.raw_meef.get())).build(consumer, TwilightForestMod.prefix("food/" + processName + "_meef").toString());
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFItems.raw_venison.get()), TFItems.cooked_venison.get(), 0.3f, smeltingTime, process).addCriterion("has_food", hasItem(TFItems.raw_venison.get())).build(consumer, TwilightForestMod.prefix("food/" + processName + "_venison").toString());
	}

	private void ingotRecipes(Consumer<IFinishedRecipe> consumer, String processName, CookingRecipeSerializer<?> process, int smeltingTime) {
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFItems.armor_shard_cluster.get()), TFItems.knightmetal_ingot.get(), 1.0f, smeltingTime, process).addCriterion("has_item", hasItem(TFItems.armor_shard_cluster.get())).build(consumer, TwilightForestMod.prefix( "material/" + processName + "_knightmetal_ingot").toString());
	}
	
	private void crackedWoodRecipes(Consumer<IFinishedRecipe> consumer, String processName, CookingRecipeSerializer<?> process, int smeltingTime) {
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.tower_wood.get()), TFBlocks.tower_wood_cracked.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.tower_wood.get())).build(consumer, TwilightForestMod.prefix("wood/" + processName + "_cracked_towerwood").toString());
	}
	
	private void crackedStoneRecipes(Consumer<IFinishedRecipe> consumer, String processName, CookingRecipeSerializer<?> process, int smeltingTime) {
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.nagastone_pillar.get()), TFBlocks.nagastone_pillar_weathered.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.nagastone_pillar.get())).build(consumer, TwilightForestMod.prefix("nagastone/" + processName + "_cracked_nagastone_pillar").toString());
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.etched_nagastone.get()), TFBlocks.etched_nagastone_weathered.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.etched_nagastone.get())).build(consumer, TwilightForestMod.prefix("nagastone/" + processName + "_cracked_etched_nagastone").toString());
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.maze_stone_brick.get()), TFBlocks.maze_stone_cracked.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.maze_stone_brick.get())).build(consumer, TwilightForestMod.prefix("maze_stone/" + processName + "_maze_stone_cracked").toString());
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.castle_brick.get()), TFBlocks.castle_brick_cracked.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.castle_brick.get())).build(consumer, TwilightForestMod.prefix("castleblock/" + processName + "_cracked_castle_brick").toString());
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(TFBlocks.underbrick.get()), TFBlocks.underbrick_cracked.get(), 0.3f, smeltingTime, process).addCriterion("has_item", hasItem(TFBlocks.underbrick.get())).build(consumer, TwilightForestMod.prefix(processName + "_cracked_underbrick").toString());
	}
}
