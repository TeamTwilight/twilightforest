package twilightforest.item;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.FireJetVariant;
import twilightforest.block.enums.PlantVariant;
import twilightforest.block.enums.TowerDeviceVariant;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;


public class TFRecipes {

	//AtomicBlom: I'm not sure why getMaxDamage is deprecated, since it seems to be actively used?
	@SuppressWarnings("deprecation")
	public static void registerRecipes() {

		// ore dictionary
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.log, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.magicLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeSapling", new ItemStack(TFBlocks.sapling, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.magicLeaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankWood", new ItemStack(TFBlocks.towerWood, 1, OreDictionary.WILDCARD_VALUE));

		// register my ores, just for fun
		OreDictionary.registerOre("ingotFiery", new ItemStack(TFItems.fieryIngot));
		OreDictionary.registerOre("oreIronwood", new ItemStack(TFItems.ironwoodRaw));
		OreDictionary.registerOre("ingotIronwood", new ItemStack(TFItems.ironwoodIngot));
		OreDictionary.registerOre("ingotSteeleaf", new ItemStack(TFItems.steeleafIngot));
		OreDictionary.registerOre("oreKnightmetal", new ItemStack(TFItems.shardCluster));
		OreDictionary.registerOre("ingotKnightmetal", new ItemStack(TFItems.knightMetal));

		// recipe sorter
		RecipeSorter.register(TwilightForestMod.ID + ":mapcloning", TFMapCloningRecipe.class, SHAPELESS, "after:minecraft:shapeless");

		// smelting for logs
		GameRegistry.addSmelting(TFBlocks.log, new ItemStack(Items.COAL, 1, 1), 0.1F);
		GameRegistry.addSmelting(TFBlocks.magicLog, new ItemStack(Items.COAL, 1, 1), 0.1F);

		// recipes
		GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 4, 0), "w", 'w', new ItemStack(TFBlocks.log, 1, 0));
		GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 4, 1), "w", 'w', new ItemStack(TFBlocks.log, 1, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 4, 2), "w", 'w', new ItemStack(TFBlocks.log, 1, 2));
		GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 4, 1), "w", 'w', new ItemStack(TFBlocks.log, 1, 3));

		addEnchantedRecipe(TFItems.plateNaga, Enchantments.FIRE_PROTECTION, 3, "# #", "###", "###", '#', TFItems.nagaScale);
		addEnchantedRecipe(TFItems.legsNaga, Enchantments.PROTECTION, 3, "###", "# #", "# #", '#', TFItems.nagaScale);

		GameRegistry.addShapelessRecipe(new ItemStack(TFBlocks.fireflyJar, 1, 0), TFBlocks.firefly, Items.GLASS_BOTTLE);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterTwilight), new ItemStack(TFItems.scepterTwilight, 1, TFItems.scepterTwilight.getMaxDamage()), Items.ENDER_PEARL);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterLifeDrain), new ItemStack(TFItems.scepterLifeDrain, 1, TFItems.scepterLifeDrain.getMaxDamage()), Items.FERMENTED_SPIDER_EYE);
		// aah, why are there so many potions of strength
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 16281));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 16313));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 16345));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 16377));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 8201));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 8265));
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.scepterZombie), new ItemStack(TFItems.scepterZombie, 1, TFItems.scepterZombie.getMaxDamage()), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POTIONITEM, 1, 8233));

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.magicMapFocus), TFItems.feather, TFItems.torchberries, Items.GLOWSTONE_DUST);
		GameRegistry.addRecipe(new ItemStack(TFItems.emptyMagicMap), "###", "#X#", "###", '#', Items.PAPER, 'X', TFItems.magicMapFocus);
		GameRegistry.addRecipe(new ItemStack(TFItems.emptyMazeMap), "###", "#X#", "###", '#', Items.PAPER, 'X', TFItems.mazeMapFocus);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.emptyOreMap), new ItemStack(TFItems.mazeMap, 1, Short.MAX_VALUE), Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.IRON_BLOCK);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.emptyOreMap), new ItemStack(TFItems.emptyMazeMap, 1, Short.MAX_VALUE), Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.IRON_BLOCK);

		GameRegistry.addRecipe(new ItemStack(Items.ARROW, 4), "X", "#", "Y", 'Y', TFItems.feather, 'X', Items.FLINT, '#', Items.STICK);

		GameRegistry.addShapelessRecipe(new ItemStack(Items.STICK), new ItemStack(TFBlocks.plant, 1, PlantVariant.ROOT_STRAND.ordinal()));
		GameRegistry.addRecipe(new ItemStack(Blocks.TORCH, 5), "B", "S", 'B', TFItems.torchberries, 'S', Items.STICK);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.ironwoodRaw), TFItems.liveRoot, Items.IRON_INGOT, Items.GOLD_NUGGET);
		GameRegistry.addSmelting(TFItems.ironwoodRaw, new ItemStack(TFItems.ironwoodIngot, 2), 1.0F);

		addEnchantedRecipe(TFItems.ironwoodHelm, Enchantments.AQUA_AFFINITY, 1, "###", "# #", '#', TFItems.ironwoodIngot);
		addEnchantedRecipe(TFItems.ironwoodPlate, Enchantments.PROTECTION, 1, "# #", "###", "###", '#', TFItems.ironwoodIngot);
		addEnchantedRecipe(TFItems.ironwoodLegs, Enchantments.PROTECTION, 1, "###", "# #", "# #", '#', TFItems.ironwoodIngot);
		addEnchantedRecipe(TFItems.ironwoodBoots, Enchantments.FEATHER_FALLING, 1, "# #", "# #", '#', TFItems.ironwoodIngot);
		addEnchantedRecipe(TFItems.ironwoodSword, Enchantments.KNOCKBACK, 1, "#", "#", "X", '#', TFItems.ironwoodIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.ironwoodShovel, Enchantments.UNBREAKING, 1, "#", "X", "X", '#', TFItems.ironwoodIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.ironwoodPick, Enchantments.EFFICIENCY, 1, "###", " X ", " X ", '#', TFItems.ironwoodIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.ironwoodAxe, Enchantments.FORTUNE, 1, "##", "#X", " X", '#', TFItems.ironwoodIngot, 'X', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(TFItems.ironwoodHoe), "##", " X", " X", '#', TFItems.ironwoodIngot, 'X', Items.STICK);

		GameRegistry.addRecipe(new ItemStack(TFBlocks.uncraftingTable), "###", "#X#", "###", '#', Blocks.CRAFTING_TABLE, 'X', TFItems.mazeMapFocus);

		GameRegistry.addSmelting(TFItems.venisonRaw, new ItemStack(TFItems.venisonCooked), 0.3F);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.fieryIngot), TFItems.fieryBlood, Items.IRON_INGOT);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.fieryIngot), TFItems.fieryTears, Items.IRON_INGOT);
		GameRegistry.addRecipe(new ItemStack(TFItems.fieryHelm), "###", "# #", '#', TFItems.fieryIngot);
		GameRegistry.addRecipe(new ItemStack(TFItems.fieryPlate), "# #", "###", "###", '#', TFItems.fieryIngot);
		GameRegistry.addRecipe(new ItemStack(TFItems.fieryLegs), "###", "# #", "# #", '#', TFItems.fieryIngot);
		GameRegistry.addRecipe(new ItemStack(TFItems.fieryBoots), "# #", "# #", '#', TFItems.fieryIngot);
		addEnchantedRecipe(TFItems.fierySword, Enchantments.FIRE_ASPECT, 2, "#", "#", "X", '#', TFItems.fieryIngot, 'X', Items.BLAZE_ROD);
		GameRegistry.addRecipe(new ItemStack(TFItems.fieryPick), "###", " X ", " X ", '#', TFItems.fieryIngot, 'X', Items.BLAZE_ROD);

		addEnchantedRecipe(TFItems.steeleafHelm, Enchantments.PROJECTILE_PROTECTION, 2, "###", "# #", '#', TFItems.steeleafIngot);
		addEnchantedRecipe(TFItems.steeleafPlate, Enchantments.BLAST_PROTECTION, 2, "# #", "###", "###", '#', TFItems.steeleafIngot);
		addEnchantedRecipe(TFItems.steeleafLegs, Enchantments.FIRE_PROTECTION, 2, "###", "# #", "# #", '#', TFItems.steeleafIngot);
		addEnchantedRecipe(TFItems.steeleafBoots, Enchantments.FEATHER_FALLING, 2, "# #", "# #", '#', TFItems.steeleafIngot);
		addEnchantedRecipe(TFItems.steeleafSword, Enchantments.LOOTING, 2, "#", "#", "X", '#', TFItems.steeleafIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.steeleafShovel, Enchantments.EFFICIENCY, 2, "#", "X", "X", '#', TFItems.steeleafIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.steeleafPick, Enchantments.FORTUNE, 2, "###", " X ", " X ", '#', TFItems.steeleafIngot, 'X', Items.STICK);
		addEnchantedRecipe(TFItems.steeleafAxe, Enchantments.EFFICIENCY, 2, "##", "#X", " X", '#', TFItems.steeleafIngot, 'X', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(TFItems.steeleafHoe), "##", " X", " X", '#', TFItems.steeleafIngot, 'X', Items.STICK);

		GameRegistry.addSmelting(TFItems.meefRaw, new ItemStack(TFItems.meefSteak), 0.3F);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.moonwormQueen), new ItemStack(TFItems.moonwormQueen, 1, Short.MAX_VALUE), TFItems.torchberries, TFItems.torchberries, TFItems.torchberries);

		GameRegistry.addRecipe(new ItemStack(TFItems.emptyMagicMap), "###", "#X#", "###", '#', Items.PAPER, 'X', TFItems.magicMapFocus);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.charmOfKeeping2), TFItems.charmOfKeeping1, TFItems.charmOfKeeping1, TFItems.charmOfKeeping1, TFItems.charmOfKeeping1);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.charmOfKeeping3), TFItems.charmOfKeeping2, TFItems.charmOfKeeping2, TFItems.charmOfKeeping2, TFItems.charmOfKeeping2);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.charmOfLife2), TFItems.charmOfLife1, TFItems.charmOfLife1, TFItems.charmOfLife1, TFItems.charmOfLife1);

		GameRegistry.addRecipe(new TFMapCloningRecipe(TFItems.magicMap, TFItems.emptyMagicMap));
		GameRegistry.addRecipe(new TFMapCloningRecipe(TFItems.mazeMap, TFItems.emptyMazeMap));
		GameRegistry.addRecipe(new TFMapCloningRecipe(TFItems.oreMap, TFItems.emptyOreMap));

		// dark tower recipes
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerWood, 4, 0), "##", "##", '#', new ItemStack(TFBlocks.log, 1, 3));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerWood, 3, 1), "#", "#", "#", '#', new ItemStack(TFBlocks.towerWood, 1, 0));
		GameRegistry.addRecipe(new ItemStack(TFItems.carminite), "brb", "rgr", "brb", 'b', new ItemStack(TFItems.borerEssence), 'r', new ItemStack(Items.REDSTONE), 'g', new ItemStack(Items.GHAST_TEAR));
		GameRegistry.addRecipe(new ItemStack(TFItems.carminite), "rbr", "bgb", "rbr", 'b', new ItemStack(TFItems.borerEssence), 'r', new ItemStack(Items.REDSTONE), 'g', new ItemStack(Items.GHAST_TEAR));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerDevice, 8, TowerDeviceVariant.VANISH_INACTIVE.ordinal()), "ewe", "wcw", "ewe", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'w', new ItemStack(TFBlocks.towerWood, 1, 0), 'c', new ItemStack(TFItems.carminite));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerDevice, 2, TowerDeviceVariant.REAPPEARING_INACTIVE.ordinal()), "ere", "rcr", "ere", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'r', new ItemStack(Items.REDSTONE), 'c', new ItemStack(TFItems.carminite));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerDevice, 1, TowerDeviceVariant.BUILDER_INACTIVE.ordinal()), "ece", "cdc", "ece", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'd', new ItemStack(Blocks.DISPENSER), 'c', new ItemStack(TFItems.carminite));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.towerDevice, 1, TowerDeviceVariant.REACTOR_INACTIVE.ordinal()), "ece", "coc", "ece", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'o', new ItemStack(Blocks.REDSTONE_ORE), 'c', new ItemStack(TFItems.carminite));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.fireJet, 1, FireJetVariant.ENCASED_SMOKER_OFF.ordinal()), "ere", "rsr", "ere", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'r', new ItemStack(Items.REDSTONE), 's', new ItemStack(TFBlocks.fireJet, 1, FireJetVariant.SMOKER.ordinal()));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.fireJet, 1, FireJetVariant.ENCASED_JET_IDLE.ordinal()), "ere", "rjr", "lll", 'e', new ItemStack(TFBlocks.towerWood, 1, 1), 'r', new ItemStack(Items.REDSTONE), 'l', new ItemStack(Items.LAVA_BUCKET), 'j', new ItemStack(TFBlocks.fireJet, 1, FireJetVariant.JET_IDLE.ordinal()));


		GameRegistry.addRecipe(new ItemStack(TFItems.shardCluster), "###", "###", "###", '#', TFItems.armorShard);

		GameRegistry.addSmelting(TFItems.shardCluster, new ItemStack(TFItems.knightMetal), 1.0F);

		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyHelm), "###", "# #", '#', TFItems.knightMetal);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyPlate), "# #", "###", "###", '#', TFItems.knightMetal);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyLegs), "###", "# #", "# #", '#', TFItems.knightMetal);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyBoots), "# #", "# #", '#', TFItems.knightMetal);

		GameRegistry.addRecipe(new ItemStack(TFItems.knightlySword), "#", "#", "X", '#', TFItems.knightMetal, 'X', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyPick), "###", " X ", " X ", '#', TFItems.knightMetal, 'X', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyAxe), "##", "#X", " X", '#', TFItems.knightMetal, 'X', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(TFItems.knightlyAxe), "##", "X#", "X ", '#', TFItems.knightMetal, 'X', Items.STICK);

		addEnchantedRecipe(TFItems.yetiHelm, Enchantments.PROTECTION, 2, "###", "# #", '#', TFItems.alphaFur);
		addEnchantedRecipe(TFItems.yetiPlate, Enchantments.PROTECTION, 2, "# #", "###", "###", '#', TFItems.alphaFur);
		addEnchantedRecipe(TFItems.yetiLegs, Enchantments.PROTECTION, 2, "###", "# #", "# #", '#', TFItems.alphaFur);
		addEnchantedRecipe(TFItems.yetiBoots, Enchantments.PROTECTION, 2, Enchantments.FEATHER_FALLING, 4, "# #", "# #", '#', TFItems.alphaFur);

		GameRegistry.addRecipe(new ItemStack(TFItems.arcticHelm), "###", "# #", '#', TFItems.arcticFur);
		GameRegistry.addRecipe(new ItemStack(TFItems.arcticPlate), "# #", "###", "###", '#', TFItems.arcticFur);
		GameRegistry.addRecipe(new ItemStack(TFItems.arcticLegs), "###", "# #", "# #", '#', TFItems.arcticFur);
		GameRegistry.addRecipe(new ItemStack(TFItems.arcticBoots), "# #", "# #", '#', TFItems.arcticFur);

		GameRegistry.addRecipe(new ItemStack(TFBlocks.auroraSlab, 6, 0), "###", '#', TFBlocks.auroraBlock);
		GameRegistry.addRecipe(new ItemStack(TFBlocks.auroraPillar, 2, 0), "#", "#", '#', TFBlocks.auroraBlock);

		GameRegistry.addRecipe(new ItemStack(TFItems.giantPick), "###", " X ", " X ", '#', TFBlocks.giantCobble, 'X', TFBlocks.giantLog);
		GameRegistry.addRecipe(new ItemStack(TFItems.giantSword), "#", "#", "X", '#', TFBlocks.giantCobble, 'X', TFBlocks.giantLog);

		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE, 64), new ItemStack(TFBlocks.giantCobble));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.PLANKS, 64), new ItemStack(TFBlocks.giantLog));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.LEAVES, 64), new ItemStack(TFBlocks.giantLeaves));

		GameRegistry.addRecipe(new ItemStack(TFItems.knightmetalRing), " # ", "# #", " # ", '#', TFItems.knightMetal);
		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.chainBlock), TFItems.knightmetalRing, TFItems.knightMetal, TFItems.knightMetal, TFItems.knightMetal, TFBlocks.knightmetalStorage);

		GameRegistry.addShapelessRecipe(new ItemStack(TFItems.knightMetal, 9), new ItemStack(TFBlocks.knightmetalStorage));
		GameRegistry.addRecipe(new ItemStack(TFBlocks.knightmetalStorage), "###", "###", "###", '#', TFItems.knightMetal);

	}

	@ParametersAreNonnullByDefault
	private static void addEnchantedRecipe(Item item, Object... inputs) {
		ItemStack result = new ItemStack(item);
		int i = 0;
		while (i < inputs.length && inputs[i] instanceof Enchantment) {
			result.addEnchantment((Enchantment) inputs[i], (Integer) inputs[i + 1]);
			i += 2;
		}

		Object[] ingredients = new Object[inputs.length - i];
		System.arraycopy(inputs, i, ingredients, 0, ingredients.length);
		GameRegistry.addRecipe(result, ingredients);
	}

}
