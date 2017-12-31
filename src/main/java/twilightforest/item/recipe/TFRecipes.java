package twilightforest.item.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

@Mod.EventBusSubscriber
public class TFRecipes {

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.log, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.magicLog, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeSapling", new ItemStack(TFBlocks.sapling, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.magicLeaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankWood", new ItemStack(TFBlocks.towerWood, 1, OreDictionary.WILDCARD_VALUE));

		OreDictionary.registerOre("ingotFiery", new ItemStack(TFItems.fieryIngot));
		OreDictionary.registerOre("oreIronwood", new ItemStack(TFItems.ironwoodRaw));
		OreDictionary.registerOre("ingotIronwood", new ItemStack(TFItems.ironwoodIngot));
		OreDictionary.registerOre("ingotSteeleaf", new ItemStack(TFItems.steeleafIngot));
		OreDictionary.registerOre("oreKnightmetal", new ItemStack(TFItems.shardCluster));
		OreDictionary.registerOre("ingotKnightmetal", new ItemStack(TFItems.knightMetal));

		OreDictionary.registerOre("carminite", new ItemStack(TFItems.carminite));
		OreDictionary.registerOre("furArctic", new ItemStack(TFItems.arcticFur));

		// recipe sorter
		// RecipeSorter.register(TwilightForestMod.ID + ":mapcloning", TFMapCloningRecipe.class, SHAPELESS, "after:minecraft:shapeless");

		GameRegistry.addSmelting(TFBlocks.log, new ItemStack(Items.COAL, 1, 1), 0.1F);
		GameRegistry.addSmelting(TFBlocks.magicLog, new ItemStack(Items.COAL, 1, 1), 0.1F);
		GameRegistry.addSmelting(TFItems.ironwoodRaw, new ItemStack(TFItems.ironwoodIngot, 2), 1.0F);
		GameRegistry.addSmelting(TFItems.venisonRaw, new ItemStack(TFItems.venisonCooked), 0.3F);
		GameRegistry.addSmelting(TFItems.meefRaw, new ItemStack(TFItems.meefSteak), 0.3F);
		GameRegistry.addSmelting(TFItems.shardCluster, new ItemStack(TFItems.knightMetal), 1.0F);

		event.getRegistry().register(new TFArmorDyeingRecipe().setRegistryName(TwilightForestMod.ID, "arctic_armor_dyeing"));

		event.getRegistry().register(new TFMapCloningRecipe(TFItems.magicMap, TFItems.emptyMagicMap).setRegistryName(TwilightForestMod.ID, "magic_map_cloning"));
		event.getRegistry().register(new TFMapCloningRecipe(TFItems.mazeMap, TFItems.emptyMazeMap).setRegistryName(TwilightForestMod.ID, "maze_map_cloning"));
		event.getRegistry().register(new TFMapCloningRecipe(TFItems.oreMap, TFItems.emptyOreMap).setRegistryName(TwilightForestMod.ID, "ore_map_cloning"));
	}
}
