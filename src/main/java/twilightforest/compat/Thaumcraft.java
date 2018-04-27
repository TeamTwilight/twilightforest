package twilightforest.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import twilightforest.item.TFItems;

public class Thaumcraft {
    // Use the thaumcraft API to register our things with aspects and biomes with values
    static void init() {
        try {
            // TODO Redo list, this all has to
            registerTCObjectTag(TFItems.naga_scale, 0, new AspectList()
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.BEAST, 4)
                    .add(Aspect.MOTION, 2));

            registerTCObjectTag(TFItems.naga_chestplate, 0, new AspectList()
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.BEAST, 12)
                    .add(Aspect.MOTION, 15));

            registerTCObjectTag(TFItems.naga_leggings, 0, new AspectList()
                    .add(Aspect.PROTECT, 24)
                    .add(Aspect.BEAST, 10)
                    .add(Aspect.MOTION, 12));

            registerTCObjectTag(TFItems.twilight_scepter, 0, new AspectList()
                    .add(Aspect.MAGIC, 28)
                    .add(Aspect.DARKNESS, 30)
                    .add(Aspect.VOID, 15));

            registerTCObjectTag(TFItems.lifedrain_scepter, 0, new AspectList()
                    .add(Aspect.MAGIC,  28)
                    .add(Aspect.LIFE, 26)
                    .add(Aspect.EXCHANGE,18));

            registerTCObjectTag(TFItems.zombie_scepter, 0, new AspectList()
                    .add(Aspect.MAGIC, 28)
                    .add(Aspect.DEATH, 25)
                    .add(Aspect.SOUL, 25)
                    .add(Aspect.TRAP, 22));

            registerTCObjectTag(TFItems.ore_meter, 0, new AspectList()
                    .add(Aspect.SENSES, 17)
                    .add(Aspect.DESIRE, 8)
                    .add(Aspect.MECHANISM, 15)
                    .add(Aspect.MIND, 10));

            registerTCObjectTag(TFItems.magic_map, 0, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 4)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.LIGHT, 5));

            registerTCObjectTag(TFItems.maze_map, 0, new AspectList()
                    .add(Aspect.PLANT, 1)
                    .add(Aspect.MIND, 1)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 1)
                    .add(Aspect.TRAP, 10));

            registerTCObjectTag(TFItems.ore_map, 0, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 4)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.DESIRE, 72));

            registerTCObjectTag(TFItems.raven_feather, 0, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.DARKNESS, 1));

            registerTCObjectTag(TFItems.magic_map_focus, 0, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.LIGHT, 14));

            registerTCObjectTag(TFItems.maze_map_focus, 0, new AspectList()
                    .add(Aspect.TRAP, 10)
                    .add(Aspect.MECHANISM, 15));

            registerTCObjectTag(TFItems.liveroot, 0, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7));

            registerTCObjectTag(TFItems.ironwood_raw, 0, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7)
                    .add(Aspect.METAL, 16)
                    .add(Aspect.DESIRE, 1));

            registerTCObjectTag(TFItems.ironwood_ingot, 0, new AspectList()
                    .add(Aspect.METAL, 8)
                    .add(Aspect.PLANT, 3));

            registerTCObjectTag(TFItems.ironwood_helmet, 0, new AspectList()
                    .add(Aspect.PROTECT, 8)
                    .add(Aspect.METAL, 52)
                    .add(Aspect.PLANT, 34));

            registerTCObjectTag(TFItems.ironwood_chestplate, 0, new AspectList()
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.METAL, 86)
                    .add(Aspect.PLANT, 57));

            registerTCObjectTag(TFItems.ironwood_leggings, 0, new AspectList()
                    .add(Aspect.PROTECT, 20)
                    .add(Aspect.METAL, 72)
                    .add(Aspect.PLANT, 48));

            registerTCObjectTag(TFItems.ironwood_boots, 0, new AspectList()
                    .add(Aspect.PROTECT, 8)
                    .add(Aspect.METAL, 38)
                    .add(Aspect.PLANT, 25));

            registerTCObjectTag(TFItems.ironwood_sword, 0, new AspectList()
                    .add(Aspect.AVERSION, 12)
                    .add(Aspect.METAL, 22)
                    .add(Aspect.PLANT, 14));

            registerTCObjectTag(TFItems.ironwood_shovel, 0, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 11)
                    .add(Aspect.PLANT, 7));

            registerTCObjectTag(TFItems.ironwood_pickaxe, 0, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 33)
                    .add(Aspect.PLANT, 22));

            registerTCObjectTag(TFItems.ironwood_axe, 0, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 33)
                    .add(Aspect.PLANT, 22));

            registerTCObjectTag(TFItems.ironwood_hoe, 0, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 22)
                    .add(Aspect.PLANT, 14));

            registerTCObjectTag(TFItems.torchberries, 0, new AspectList()
                    .add(Aspect.LIGHT, 7)
                    .add(Aspect.PLANT, 8));

            registerTCObjectTag(TFItems.raw_venison, 0, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EARTH, 5));

            registerTCObjectTag(TFItems.cooked_venison, 0, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.CRAFT, 1));

            registerTCObjectTag(TFItems.hydra_chop, 0, new AspectList()
                    .add(Aspect.BEAST, 25)
                    .add(Aspect.LIFE, 25)
                    .add(Aspect.FIRE, 5));

            registerTCObjectTag(TFItems.fiery_blood, 0, new AspectList()
                    .add(Aspect.FIRE, 20)
                    .add(Aspect.LIFE, 20));

            registerTCObjectTag(TFItems.trophy, 0, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 30)
                    .add(Aspect.PROTECT, 40)
                    .add(Aspect.MOTION, 10));

            registerTCObjectTag(TFItems.trophy, 1, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.UNDEAD, 100)
                    .add(Aspect.MAN, 5)
                    .add(Aspect.ELDRITCH, 40));

            registerTCObjectTag(TFItems.trophy, 2, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 30)
                    .add(Aspect.FIRE, 100));

            registerTCObjectTag(TFItems.trophy, 3, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20));

            registerTCObjectTag(TFItems.trophy, 4, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.SOUL, 100)
                    .add(Aspect.MAN, 40));

            registerTCObjectTag(TFItems.trophy, 5, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.COLD, 100)
                    .add(Aspect.MAN, 40));

            registerTCObjectTag(TFItems.trophy, 6, new AspectList()
                    .add(Aspect.DEATH, 50)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 60)
                    .add(Aspect.MAN, 40));

            registerTCObjectTag(TFItems.trophy, 7, new AspectList()
                    .add(Aspect.LIFE, 50)
                    .add(Aspect.DESIRE, 40)
                    .add(Aspect.EXCHANGE, 40));

            //TwilightForestMod.LOGGER.info("Loaded ThaumcraftApi integration.");
        } catch (Exception e) {
            //TwilightForestMod.LOGGER.warn("Had an %s error while trying to register with ThaumcraftApi.", e.getLocalizedMessage());
            // whatever.
        }
    }

    // Register a block with Thaumcraft aspects
    private static void registerTCObjectTag(Block block, int meta, AspectList list) {
        if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
        ThaumcraftApi.registerObjectTag(new ItemStack(block, 1, meta), list);
    }

    // Register an item with Thaumcraft aspects
    private static void registerTCObjectTag(Item item, int meta, AspectList list) {
        if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
        ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), list);
    }
}
