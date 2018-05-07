package twilightforest.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class Thaumcraft {
    // Use the thaumcraft API to register our things with aspects and biomes with values
    static void init() {
        try {
            registerTCObjectTag(TFBlocks.twilight_log, new int[]{0, 1}, new AspectList()
                    .add(Aspect.PLANT, 20));

            registerTCObjectTag(TFBlocks.twilight_log, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.WATER, 5));

            registerTCObjectTag(TFBlocks.twilight_log, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DARKNESS, 5));

            registerTCObjectTag(TFBlocks.root, 0, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.EARTH, 10));

            registerTCObjectTag(TFBlocks.root, 1, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.EARTH, 10)
                    .add(Aspect.LIFE, 10));

            registerTCObjectTag(TFBlocks.twilight_leaves, -1, new AspectList()
                    .add(Aspect.PLANT, 5));

            registerTCObjectTag(TFBlocks.firefly, -1, new AspectList()
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.LIFE, 2));

            registerTCObjectTag(TFBlocks.cicada, -1, new AspectList()
                    .add(Aspect.SENSES, 6)
                    .add(Aspect.LIFE, 2));

            registerTCObjectTag(TFBlocks.maze_stone, 0, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.TRAP, 1));

            registerTCObjectTag(TFBlocks.maze_stone, 1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1));

            registerTCObjectTag(TFBlocks.maze_stone, new int[]{2, 3, 6, 7}, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.maze_stone, 4, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.maze_stone, 5, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.hedge, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AVERSION, 2));

            registerTCObjectTag(TFBlocks.firefly_jar, -1, new AspectList()
                    .add(Aspect.CRYSTAL, 35)
                    .add(Aspect.LIGHT, 20)
                    .add(Aspect.SENSES, 10));

            registerTCObjectTag(TFBlocks.twilight_plant, 0, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.WATER, 2));

            registerTCObjectTag(TFBlocks.twilight_plant, new int[]{1, 2, 3, 5}, new AspectList()
                    .add(Aspect.PLANT, 5));

            registerTCObjectTag(TFBlocks.twilight_plant, 4, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.AIR, 5));

            registerTCObjectTag(TFBlocks.fire_jet, 0, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.AIR,16));

            registerTCObjectTag(TFBlocks.fire_jet, 3, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.FIRE, 16));

            registerTCObjectTag(TFBlocks.fire_jet, 1, new AspectList()
                    .add(Aspect.PLANT, 33)
                    .add(Aspect.AIR, 16)
                    .add(Aspect.ENERGY, 30)
                    .add(Aspect.MECHANISM, 20));

            registerTCObjectTag(TFBlocks.fire_jet, 6, new AspectList()
                    .add(Aspect.PLANT, 16)
                    .add(Aspect.FIRE, 46)
                    .add(Aspect.ENERGY, 22)
                    .add(Aspect.MECHANISM, 20));

            registerTCObjectTag(TFBlocks.naga_stone, -1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.twilight_sapling, 5, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.AURA, 40));

            registerTCObjectTag(TFBlocks.twilight_sapling, 6, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EXCHANGE, 40));

            registerTCObjectTag(TFBlocks.twilight_sapling, 7, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.DESIRE, 40));

            registerTCObjectTag(TFBlocks.twilight_sapling, 8, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.ORDER, 40));

            registerTCObjectTag(TFBlocks.moonworm, -1, new AspectList()
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.LIFE, 2));

            registerTCObjectTag(TFBlocks.magic_log, 0, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.AURA, 3));

            registerTCObjectTag(TFBlocks.magic_log, 1, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.EXCHANGE, 3));

            registerTCObjectTag(TFBlocks.magic_log, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DESIRE, 3));

            registerTCObjectTag(TFBlocks.magic_log, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.ORDER, 3));

            registerTCObjectTag(TFBlocks.magic_leaves, 0, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AURA, 1));

            registerTCObjectTag(TFBlocks.magic_leaves, 1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.EXCHANGE, 1));

            registerTCObjectTag(TFBlocks.magic_leaves, 2, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DESIRE, 1));

            registerTCObjectTag(TFBlocks.magic_leaves, 3, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.magic_log_core, 0, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.AURA, 55));

            registerTCObjectTag(TFBlocks.magic_log_core, 1, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.EXCHANGE, 55));

            registerTCObjectTag(TFBlocks.magic_log_core, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DESIRE, 55));

            registerTCObjectTag(TFBlocks.magic_log_core, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.ORDER, 55));

            registerTCObjectTag(TFBlocks.tower_wood, 2, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.ENTROPY, 3));

            registerTCObjectTag(TFBlocks.tower_wood, 3, new AspectList()
                    .add(Aspect.PLANT, 18));

            registerTCObjectTag(TFBlocks.tower_wood, 4, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.ENTROPY, 3)
                    .add(Aspect.BEAST, 5));

            registerTCObjectTag(TFBlocks.stronghold_shield, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.PROTECT, 5));

            registerTCObjectTag(TFBlocks.trophy_pedestal, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.MECHANISM, 35));

            registerTCObjectTag(TFBlocks.aurora_block, -1, new AspectList()
                    .add(Aspect.COLD, 6)
                    .add(Aspect.AURA, 1));

            registerTCObjectTag(TFBlocks.underbrick, new int[]{0, 3}, new AspectList()
                    .add(Aspect.EARTH, 3));

            registerTCObjectTag(TFBlocks.underbrick, 1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.underbrick, 2, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.thorns, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AVERSION, 4));

            registerTCObjectTag(TFBlocks.burnt_thorns, -1, new AspectList()
                    .add(Aspect.ENTROPY, 5));

            registerTCObjectTag(TFBlocks.twilight_leaves_3, -1, new AspectList()
                    .add(Aspect.PLANT, 5));

            registerTCObjectTag(TFBlocks.deadrock, new int[]{0, 2}, new AspectList()
                    .add(Aspect.EARTH, 1)
                    .add(Aspect.VOID, 1));

            registerTCObjectTag(TFBlocks.deadrock, 1, new AspectList()
                    .add(Aspect.EARTH, 1)
                    .add(Aspect.VOID, 1)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.dark_leaves, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 1));

            registerTCObjectTag(TFBlocks.aurora_pillar, -1, new AspectList()
                    .add(Aspect.COLD, 6)
                    .add(Aspect.ORDER, 2)
                    .add(Aspect.AURA, 1));

            registerTCObjectTag(TFBlocks.aurora_slab, -1, new AspectList()
                    .add(Aspect.COLD, 3)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.trollsteinn, -1, new AspectList()
                    .add(Aspect.EARTH, 5));

            registerTCObjectTag(TFBlocks.wispy_cloud, -1, new AspectList()
                    .add(Aspect.AIR, 20)
                    .add(Aspect.FLIGHT, 5));

            registerTCObjectTag(TFBlocks.fluffy_cloud, -1, new AspectList()
                    .add(Aspect.AIR, 20)
                    .add(Aspect.FLIGHT, 5));

            registerTCObjectTag(TFBlocks.giant_cobblestone, -1, new AspectList()
                    .add(Aspect.EARTH, 320)
                    .add(Aspect.ENTROPY, 64));

            registerTCObjectTag(TFBlocks.giant_log, -1, new AspectList()
                    .add(Aspect.PLANT, 1280));

            registerTCObjectTag(TFBlocks.giant_leaves, -1, new AspectList()
                    .add(Aspect.PLANT, 320));

            registerTCObjectTag(TFBlocks.giant_obsidian, -1, new AspectList()
                    .add(Aspect.EARTH, 320)
                    .add(Aspect.FIRE, 320)
                    .add(Aspect.DARKNESS, 320));

            registerTCObjectTag(TFBlocks.uberous_soil, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.PLANT, 2)
                    .add(Aspect.LIFE, 15));

            registerTCObjectTag(TFBlocks.huge_stalk, -1, new AspectList()
                    .add(Aspect.PLANT, 25));

            registerTCObjectTag(TFBlocks.huge_mushgloom, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.AIR, 5));

            registerTCObjectTag(TFBlocks.trollvidr, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 3));

            registerTCObjectTag(TFBlocks.unripe_trollber, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 6));

            registerTCObjectTag(TFBlocks.trollber, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 25)
                    .add(Aspect.DARKNESS, 1));

            registerTCObjectTag(TFBlocks.knightmetal_block, -1, new AspectList()
                    .add(Aspect.METAL, 121)
                    .add(Aspect.CRAFT, 40)
                    .add(Aspect.AVERSION, 6));

            registerTCObjectTag(TFBlocks.huge_lilypad, -1, new AspectList()
                    .add(Aspect.PLANT, 25)
                    .add(Aspect.WATER, 5));

            registerTCObjectTag(TFBlocks.huge_waterlily, -1, new AspectList()
                    .add(Aspect.PLANT, 10)
                    .add(Aspect.WATER, 2));

            registerTCObjectTag(TFBlocks.castle_brick, new int[]{0, 1, 3, 5}, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            registerTCObjectTag(TFBlocks.castle_brick, 2, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.castle_brick, 4, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.castle_pillar, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            registerTCObjectTag(TFBlocks.castle_stairs, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            registerTCObjectTag(TFBlocks.castle_rune_brick, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.AURA, 3));

            registerTCObjectTag(TFBlocks.force_field, -1, new AspectList()
                    .add(Aspect.AURA, 40));

            registerTCObjectTag(TFBlocks.cinder_log, -1, new AspectList()
                    .add(Aspect.FIRE, 4)
                    .add(Aspect.ENTROPY, 12));

            registerTCObjectTag(TFBlocks.castle_door, -1, new AspectList()
                    .add(Aspect.AURA, 20)
                    .add(Aspect.MECHANISM, 10)
                    .add(Aspect.TRAP, 5));

            registerTCObjectTag(TFBlocks.spiral_bricks, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.etched_nagastone, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.nagastone_stairs, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.nagastone_pillar, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            registerTCObjectTag(TFBlocks.etched_nagastone_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.nagastone_stairs_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.nagastone_pillar_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            registerTCObjectTag(TFBlocks.etched_nagastone_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.nagastone_stairs_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFBlocks.nagastone_pillar_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            registerTCObjectTag(TFItems.naga_scale, -1, new AspectList()
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.BEAST, 4)
                    .add(Aspect.MOTION, 2));

            registerTCObjectTag(TFItems.naga_chestplate, -1, new AspectList()
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.BEAST, 12)
                    .add(Aspect.MOTION, 15));

            registerTCObjectTag(TFItems.naga_leggings, -1, new AspectList()
                    .add(Aspect.PROTECT, 24)
                    .add(Aspect.BEAST, 10)
                    .add(Aspect.MOTION, 12));

            registerTCObjectTag(TFItems.twilight_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC, 28)
                    .add(Aspect.DARKNESS, 30)
                    .add(Aspect.VOID, 15));

            registerTCObjectTag(TFItems.lifedrain_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC,  28)
                    .add(Aspect.LIFE, 26)
                    .add(Aspect.EXCHANGE,18));

            registerTCObjectTag(TFItems.zombie_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC, 28)
                    .add(Aspect.DEATH, 25)
                    .add(Aspect.SOUL, 25)
                    .add(Aspect.TRAP, 22));

            registerTCObjectTag(TFItems.ore_meter, 0, new AspectList()
                    .add(Aspect.SENSES, 17)
                    .add(Aspect.DESIRE, 8)
                    .add(Aspect.MECHANISM, 15)
                    .add(Aspect.MIND, 10));

            registerTCObjectTag(TFItems.magic_map, -1, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 4)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.LIGHT, 5));

            registerTCObjectTag(TFItems.maze_map, -1, new AspectList()
                    .add(Aspect.PLANT, 1)
                    .add(Aspect.MIND, 1)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 1)
                    .add(Aspect.TRAP, 10));

            registerTCObjectTag(TFItems.ore_map, -1, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 4)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.DESIRE, 72));

            registerTCObjectTag(TFItems.raven_feather, -1, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.DARKNESS, 1));

            registerTCObjectTag(TFItems.magic_map_focus, -1, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.LIGHT, 14));

            registerTCObjectTag(TFItems.maze_map_focus, -1, new AspectList()
                    .add(Aspect.TRAP, 10)
                    .add(Aspect.MECHANISM, 15));

            registerTCObjectTag(TFItems.liveroot, -1, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7));

            registerTCObjectTag(TFItems.ironwood_raw, -1, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7)
                    .add(Aspect.METAL, 16)
                    .add(Aspect.DESIRE, 1));

            registerTCObjectTag(TFItems.ironwood_ingot, -1, new AspectList()
                    .add(Aspect.METAL, 8)
                    .add(Aspect.PLANT, 3));

            /*registerTCObjectTag(TFItems.ironwood_helmet, -1, new AspectList()
                    .add(Aspect.PROTECT, 8)
                    .add(Aspect.METAL, 52)
                    .add(Aspect.PLANT, 34));

            registerTCObjectTag(TFItems.ironwood_chestplate, -1, new AspectList()
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.METAL, 86)
                    .add(Aspect.PLANT, 57));

            registerTCObjectTag(TFItems.ironwood_leggings, -1, new AspectList()
                    .add(Aspect.PROTECT, 20)
                    .add(Aspect.METAL, 72)
                    .add(Aspect.PLANT, 48));

            registerTCObjectTag(TFItems.ironwood_boots, -1, new AspectList()
                    .add(Aspect.PROTECT, 8)
                    .add(Aspect.METAL, 38)
                    .add(Aspect.PLANT, 25));

            registerTCObjectTag(TFItems.ironwood_sword, -1, new AspectList()
                    .add(Aspect.AVERSION, 12)
                    .add(Aspect.METAL, 22)
                    .add(Aspect.PLANT, 14));

            registerTCObjectTag(TFItems.ironwood_shovel, -1, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 11)
                    .add(Aspect.PLANT, 7));

            registerTCObjectTag(TFItems.ironwood_pickaxe, -1, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 33)
                    .add(Aspect.PLANT, 22));

            registerTCObjectTag(TFItems.ironwood_axe, -1, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 33)
                    .add(Aspect.PLANT, 22));

            registerTCObjectTag(TFItems.ironwood_hoe, -1, new AspectList()
                    .add(Aspect.TOOL, 12)
                    .add(Aspect.METAL, 22)
                    .add(Aspect.PLANT, 14));*/

            registerTCObjectTag(TFItems.torchberries, -1, new AspectList()
                    .add(Aspect.LIGHT, 7)
                    .add(Aspect.PLANT, 8));

            registerTCObjectTag(TFItems.raw_venison, -1, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EARTH, 5));

            registerTCObjectTag(TFItems.cooked_venison, -1, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.CRAFT, 1));

            registerTCObjectTag(TFItems.hydra_chop, -1, new AspectList()
                    .add(Aspect.BEAST, 25)
                    .add(Aspect.LIFE, 25)
                    .add(Aspect.FIRE, 5));

            registerTCObjectTag(TFItems.fiery_blood, -1, new AspectList()
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

            registerTCObjectTag(TFItems.trophy, 8, new AspectList()
                    .add(Aspect.LIFE, 50)
                    .add(Aspect.DESIRE, 40)
                    .add(Aspect.EXCHANGE, 40));

            registerTCObjectTag(TFItems.steeleaf_ingot, -1, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.METAL, 4));

            registerTCObjectTag(TFItems.minotaur_axe, -1, new AspectList()
                    .add(Aspect.AVERSION, 20)
                    .add(Aspect.TOOL, 16)
                    .add(Aspect.DESIRE, 44)
                    .add(Aspect.CRYSTAL, 44));

            registerTCObjectTag(TFItems.mazebreaker_pickaxe, -1, new AspectList()
                    .add(Aspect.TOOL, 16)
                    .add(Aspect.TRAP, 8)
                    .add(Aspect.METAL, 33));

            registerTCObjectTag(TFItems.transformation_powder, -1, new AspectList()
                    .add(Aspect.EXCHANGE, 30)
                    .add(Aspect.MAGIC, 12));

            registerTCObjectTag(TFItems.raw_meef, -1, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EARTH, 5));

            registerTCObjectTag(TFItems.cooked_meef, -1, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.CRAFT, 1));

            registerTCObjectTag(TFItems.meef_stroganoff, -1, new AspectList()
                    .add(Aspect.BEAST, 25)
                    .add(Aspect.MAN, 15)
                    .add(Aspect.LIFE, 25)
                    .add(Aspect.CRAFT, 5));

            registerTCObjectTag(TFItems.maze_wafer, -1, new AspectList()
                    .add(Aspect.PLANT, 2));

            registerTCObjectTag(TFItems.ore_magnet, -1, new AspectList()
                    .add(Aspect.METAL, 24)
                    .add(Aspect.DESIRE, 30));

            registerTCObjectTag(TFItems.crumble_horn, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.SENSES, 30)
                    .add(Aspect.MAGIC, 32));

            registerTCObjectTag(TFItems.peacock_fan, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.MOTION, 48)
                    .add(Aspect.FLIGHT, 53)
                    .add(Aspect.MAGIC, 32));

            registerTCObjectTag(TFItems.moonworm_queen, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.LIGHT, 54)
                    .add(Aspect.LIFE, 36)
                    .add(Aspect.EXCHANGE, 28));

            registerTCObjectTag(TFItems.charm_of_life_1, -1, new AspectList()
                    .add(Aspect.LIFE, 10)
                    .add(Aspect.DEATH, 10)
                    .add(Aspect.EXCHANGE, 16 )
                    .add(Aspect.MAGIC, 8));

            registerTCObjectTag(TFItems.charm_of_keeping_1, -1, new AspectList()
                    .add(Aspect.EXCHANGE, 16)
                    .add(Aspect.MAGIC, 8));

            registerTCObjectTag(TFItems.tower_key, -1, new AspectList()
                    .add(Aspect.TRAP, 16));

            registerTCObjectTag(TFItems.armor_shard, -1, new AspectList()
                    .add(Aspect.METAL, 2));

            registerTCObjectTag(TFItems.knightmetal_ingot, -1, new AspectList()
                    .add(Aspect.METAL, 18)
                    .add(Aspect.CRAFT, 6));

            registerTCObjectTag(TFItems.phantom_helmet, -1, new AspectList()
                    .add(Aspect.PROTECT, 12)
                    .add(Aspect.SOUL, 24)
                    .add(Aspect.DEATH, 7)
                    .add(Aspect.ELDRITCH, 8)
                    .add(Aspect.TRAP, 22));

            registerTCObjectTag(TFItems.phantom_chestplate, -1, new AspectList()
                    .add(Aspect.PROTECT, 32)
                    .add(Aspect.SOUL, 62)
                    .add(Aspect.DEATH, 19)
                    .add(Aspect.ELDRITCH, 22)
                    .add(Aspect.TRAP, 48));

            registerTCObjectTag(TFItems.lamp_of_cinders, -1, new AspectList()
                    .add(Aspect.MAGIC, 53)
                    .add(Aspect.FIRE, 87)
                    .add(Aspect.ENERGY, 42));

            registerTCObjectTag(TFItems.fiery_tears, -1, new AspectList()
                    .add(Aspect.FIRE, 15)
                    .add(Aspect.WATER, 22));

            registerTCObjectTag(TFItems.alpha_fur, -1, new AspectList()
                    .add(Aspect.BEAST, 48)
                    .add(Aspect.COLD, 28));

            registerTCObjectTag(TFItems.ice_bomb, -1, new AspectList()
                    .add(Aspect.COLD, 22)
                    .add(Aspect.MOTION, 8));

            registerTCObjectTag(TFItems.arctic_fur, -1, new AspectList()
                    .add(Aspect.BEAST, 12)
                    .add(Aspect.COLD, 7));

            registerTCObjectTag(TFItems.magic_beans, -1, new AspectList()
                    .add(Aspect.PLANT, 8)
                    .add(Aspect.MAGIC, 36));

            registerTCObjectTag(TFItems.triple_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.EXCHANGE, 30));

            registerTCObjectTag(TFItems.seeker_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.MECHANISM, 30));

            registerTCObjectTag(TFItems.ice_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.COLD, 30));

            registerTCObjectTag(TFItems.ender_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.ELDRITCH, 30));

            registerTCObjectTag(TFItems.ice_sword, -1, new AspectList()
                    .add(Aspect.AVERSION, 16)
                    .add(Aspect.COLD, 24));

            registerTCObjectTag(TFItems.glass_sword, -1, new AspectList()
                    .add(Aspect.AVERSION, 148)
                    .add(Aspect.CRYSTAL, 96));

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

    // Register blocks with Thaumcraft aspects
    private static void registerTCObjectTag(Block block, int[] metas, AspectList list) {
        for (int meta : metas)
            registerTCObjectTag(block, meta, list);
    }

    // Register an item with Thaumcraft aspects
    private static void registerTCObjectTag(Item item, int meta, AspectList list) {
        if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
        ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), list);
    }

    // Register item swith Thaumcraft aspects
    private static void registerTCObjectTag(Item item, int[] metas, AspectList list) {
        for (int meta : metas)
            registerTCObjectTag(item, meta, list);
    }
}
