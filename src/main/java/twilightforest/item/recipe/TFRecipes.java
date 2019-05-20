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

		OreDictionary.registerOre("feather", new ItemStack(TFItems.raven_feather));

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

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.twilight_oak_planks ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.twilight_oak_stairs ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.twilight_oak_slab   ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.twilight_oak_button ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.twilight_oak_fence  ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.twilight_oak_gate   ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.twilight_oak_plate  ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.canopy_planks       ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.canopy_stairs       ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.canopy_slab         ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.canopy_button       ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.canopy_fence        ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.canopy_gate         ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.canopy_plate        ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.mangrove_planks     ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.mangrove_stairs     ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.mangrove_slab       ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.mangrove_button     ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.mangrove_fence      ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.mangrove_gate       ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.mangrove_plate      ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.dark_planks         ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.dark_stairs         ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.dark_slab           ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.dark_button         ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.dark_fence          ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.dark_gate           ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.dark_plate          ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.time_planks         ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.time_stairs         ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.time_slab           ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.time_button         ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.time_fence          ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.time_gate           ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.time_plate          ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.trans_planks        ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.trans_stairs        ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.trans_slab          ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.trans_button        ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.trans_fence         ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.trans_gate          ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.trans_plate         ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.mine_planks         ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.mine_stairs         ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.mine_slab           ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.mine_button         ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.mine_fence          ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.mine_gate           ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.mine_plate          ));

		OreDictionary.registerOre("plankWood"    , new ItemStack(TFBlocks.sort_planks         ));
		OreDictionary.registerOre("stairWood"    , new ItemStack(TFBlocks.sort_stairs         ));
		OreDictionary.registerOre("slabWood"     , new ItemStack(TFBlocks.sort_slab           ));
		//OreDictionary.registerOre("buttonWood"   , new ItemStack(TFBlocks.sort_button         ));
		OreDictionary.registerOre("fenceWood"    , new ItemStack(TFBlocks.sort_fence          ));
		OreDictionary.registerOre("fenceGateWood", new ItemStack(TFBlocks.sort_gate           ));
		//OreDictionary.registerOre("plateWood"    , new ItemStack(TFBlocks.sort_plate          ));

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
