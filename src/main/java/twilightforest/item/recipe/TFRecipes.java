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
import twilightforest.enums.CompressedVariant;
import twilightforest.item.TFItems;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFRecipes {

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.twilight_log, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", new ItemStack(TFBlocks.magic_log, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeSapling", new ItemStack(TFBlocks.twilight_sapling, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.twilight_leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.magic_leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.twilight_leaves_3, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeLeaves", new ItemStack(TFBlocks.dark_leaves, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("plankWood", new ItemStack(TFBlocks.tower_wood, 1, OreDictionary.WILDCARD_VALUE));

		OreDictionary.registerOre("ingotFiery", new ItemStack(TFItems.fiery_ingot));
		OreDictionary.registerOre("blockFiery", new ItemStack(TFBlocks.block_storage, 1, CompressedVariant.FIERY.ordinal()));

		OreDictionary.registerOre("oreIronwood", new ItemStack(TFItems.ironwood_raw));
		OreDictionary.registerOre("ingotIronwood", new ItemStack(TFItems.ironwood_ingot));

		OreDictionary.registerOre("ingotSteeleaf", new ItemStack(TFItems.steeleaf_ingot));
		OreDictionary.registerOre("blockSteeleaf", new ItemStack(TFBlocks.block_storage, 1, CompressedVariant.STEELLEAF.ordinal()));

		OreDictionary.registerOre("oreKnightmetal", new ItemStack(TFItems.armor_shard_cluster));
		OreDictionary.registerOre("ingotKnightmetal", new ItemStack(TFItems.knightmetal_ingot));
		OreDictionary.registerOre("blockKnightmetal", new ItemStack(TFBlocks.knightmetal_block));

		OreDictionary.registerOre("carminite", new ItemStack(TFItems.carminite));
		OreDictionary.registerOre("furArctic", new ItemStack(TFItems.arctic_fur));

		// recipe sorter
		// RecipeSorter.register(TwilightForestMod.ID + ":mapcloning", TFMapCloningRecipe.class, SHAPELESS, "after:minecraft:shapeless");

		GameRegistry.addSmelting(TFBlocks.twilight_log, new ItemStack(Items.COAL, 1, 1), 0.1F);
		GameRegistry.addSmelting(TFBlocks.magic_log, new ItemStack(Items.COAL, 1, 1), 0.1F);
		GameRegistry.addSmelting(TFItems.ironwood_raw, new ItemStack(TFItems.ironwood_ingot, 2), 1.0F);
		GameRegistry.addSmelting(TFItems.raw_venison, new ItemStack(TFItems.cooked_venison), 0.3F);
		GameRegistry.addSmelting(TFItems.raw_meef, new ItemStack(TFItems.cooked_meef), 0.3F);
		GameRegistry.addSmelting(TFItems.armor_shard_cluster, new ItemStack(TFItems.knightmetal_ingot), 1.0F);

		event.getRegistry().register(new TFArmorDyeingRecipe().setRegistryName(TwilightForestMod.ID, "arctic_armor_dyeing"));

		event.getRegistry().register(new TFMapCloningRecipe(TFItems.magic_map, TFItems.magic_map_empty).setRegistryName(TwilightForestMod.ID, "magic_map_cloning"));
		event.getRegistry().register(new TFMapCloningRecipe(TFItems.maze_map, TFItems.maze_map_empty).setRegistryName(TwilightForestMod.ID, "maze_map_cloning"));
		event.getRegistry().register(new TFMapCloningRecipe(TFItems.ore_map, TFItems.ore_map_empty).setRegistryName(TwilightForestMod.ID, "ore_map_cloning"));
	}
}
