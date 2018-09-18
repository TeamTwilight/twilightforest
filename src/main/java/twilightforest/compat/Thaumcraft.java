package twilightforest.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class Thaumcraft {
    // Use the thaumcraft API to register our things with aspects and biomes with values
    @SubscribeEvent
    public static void registerAspects(AspectRegistryEvent event) {
        TwilightForestMod.LOGGER.debug("Attempting to register Thaumcraft Aspects for Twilight Forest items!");

        try {
            TFAspectRegisterHelper helper = new TFAspectRegisterHelper(event);

            helper.registerTCObjectTag(TFBlocks.twilight_log, new int[]{0, 1}, new AspectList()
                    .add(Aspect.PLANT, 20));

            helper.registerTCObjectTag(TFBlocks.twilight_log, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.WATER, 5));

            helper.registerTCObjectTag(TFBlocks.twilight_log, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DARKNESS, 5));

            helper.registerTCObjectTag(TFBlocks.root, 0, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.EARTH, 10));

            helper.registerTCObjectTag(TFBlocks.root, 1, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.EARTH, 10)
                    .add(Aspect.LIFE, 10));

            helper.registerTCObjectTag(TFBlocks.twilight_leaves, -1, new AspectList()
                    .add(Aspect.PLANT, 5));

            helper.registerTCObjectTag(TFBlocks.firefly, -1, new AspectList()
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.LIFE, 2));

            helper.registerTCObjectTag(TFBlocks.cicada, -1, new AspectList()
                    .add(Aspect.SENSES, 6)
                    .add(Aspect.LIFE, 2));

            helper.registerTCObjectTag(TFBlocks.maze_stone, 0, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.TRAP, 1));

            helper.registerTCObjectTag(TFBlocks.maze_stone, 1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1));

            helper.registerTCObjectTag(TFBlocks.maze_stone, new int[]{2, 3, 6, 7}, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.maze_stone, 4, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.maze_stone, 5, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.TRAP, 1)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.hedge, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AVERSION, 2));

            helper.registerTCObjectTag(TFBlocks.firefly_jar, new AspectList()
                    .add(Aspect.CRYSTAL, 35)
                    .add(Aspect.LIGHT, 20)
                    .add(Aspect.SENSES, 10));

            helper.registerTCObjectTag(TFBlocks.twilight_plant, 0, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.WATER, 2));

            helper.registerTCObjectTag(TFBlocks.twilight_plant, new int[]{1, 2, 3, 5}, new AspectList()
                    .add(Aspect.PLANT, 5));

            helper.registerTCObjectTag(TFBlocks.twilight_plant, 4, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.AIR, 5));

            helper.registerTCObjectTag(TFBlocks.fire_jet, 0, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.AIR,16));

            helper.registerTCObjectTag(TFBlocks.fire_jet, 3, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.FIRE, 16));

            helper.registerTCObjectTag(TFBlocks.fire_jet, 1, new AspectList()
                    .add(Aspect.PLANT, 33)
                    .add(Aspect.AIR, 16)
                    .add(Aspect.ENERGY, 30)
                    .add(Aspect.MECHANISM, 20));

            helper.registerTCObjectTag(TFBlocks.fire_jet, 6, new AspectList()
                    .add(Aspect.PLANT, 16)
                    .add(Aspect.FIRE, 46)
                    .add(Aspect.ENERGY, 22)
                    .add(Aspect.MECHANISM, 20));

            helper.registerTCObjectTag(TFBlocks.naga_stone, -1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.twilight_sapling, 5, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.AURA, 40));

            helper.registerTCObjectTag(TFBlocks.twilight_sapling, 6, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EXCHANGE, 40));

            helper.registerTCObjectTag(TFBlocks.twilight_sapling, 7, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.DESIRE, 40));

            helper.registerTCObjectTag(TFBlocks.twilight_sapling, 8, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.ORDER, 40));

            helper.registerTCObjectTag(TFBlocks.moonworm, -1, new AspectList()
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.LIFE, 2));

            helper.registerTCObjectTag(TFBlocks.magic_log, 0, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.AURA, 3));

            helper.registerTCObjectTag(TFBlocks.magic_log, 1, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.EXCHANGE, 3));

            helper.registerTCObjectTag(TFBlocks.magic_log, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DESIRE, 3));

            helper.registerTCObjectTag(TFBlocks.magic_log, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.ORDER, 3));

            helper.registerTCObjectTag(TFBlocks.magic_leaves, 0, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AURA, 1));

            helper.registerTCObjectTag(TFBlocks.magic_leaves, 1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.EXCHANGE, 1));

            helper.registerTCObjectTag(TFBlocks.magic_leaves, 2, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DESIRE, 1));

            helper.registerTCObjectTag(TFBlocks.magic_leaves, 3, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.magic_log_core, 0, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.AURA, 55));

            helper.registerTCObjectTag(TFBlocks.magic_log_core, 1, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.EXCHANGE, 55));

            helper.registerTCObjectTag(TFBlocks.magic_log_core, 2, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.DESIRE, 55));

            helper.registerTCObjectTag(TFBlocks.magic_log_core, 3, new AspectList()
                    .add(Aspect.PLANT, 20)
                    .add(Aspect.ORDER, 55));

            helper.registerTCObjectTag(TFBlocks.tower_wood, 2, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.ENTROPY, 3));

            helper.registerTCObjectTag(TFBlocks.tower_wood, 3, new AspectList()
                    .add(Aspect.PLANT, 18));

            helper.registerTCObjectTag(TFBlocks.tower_wood, 4, new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.ENTROPY, 3)
                    .add(Aspect.BEAST, 5));

            helper.registerTCObjectTag(TFBlocks.stronghold_shield, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.PROTECT, 5));

            helper.registerTCObjectTag(TFBlocks.trophy_pedestal, -1, new AspectList()
                    .add(Aspect.MECHANISM, 35)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.EARTH, 5));

            helper.registerTCObjectTag(TFBlocks.aurora_block, -1, new AspectList()
                    .add(Aspect.COLD, 6)
                    .add(Aspect.AURA, 1));

            helper.registerTCObjectTag(TFBlocks.underbrick, new int[]{0, 3}, new AspectList()
                    .add(Aspect.EARTH, 3));

            helper.registerTCObjectTag(TFBlocks.underbrick, 1, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.underbrick, 2, new AspectList()
                    .add(Aspect.EARTH, 3)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.thorns, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AVERSION, 4));

            helper.registerTCObjectTag(TFBlocks.burnt_thorns, -1, new AspectList()
                    .add(Aspect.ENTROPY, 5));

            helper.registerTCObjectTag(TFBlocks.twilight_leaves_3, -1, new AspectList()
                    .add(Aspect.PLANT, 5));

            helper.registerTCObjectTag(TFBlocks.deadrock, new int[]{0, 2}, new AspectList()
                    .add(Aspect.EARTH, 1)
                    .add(Aspect.VOID, 1));

            helper.registerTCObjectTag(TFBlocks.deadrock, 1, new AspectList()
                    .add(Aspect.EARTH, 1)
                    .add(Aspect.VOID, 1)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.dark_leaves, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 1));

            helper.registerTCObjectTag(TFBlocks.aurora_pillar, -1, new AspectList()
                    .add(Aspect.COLD, 6)
                    .add(Aspect.ORDER, 2)
                    .add(Aspect.AURA, 1));

            helper.registerTCObjectTag(TFBlocks.aurora_slab, -1, new AspectList()
                    .add(Aspect.COLD, 3)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.trollsteinn, new AspectList()
                    .add(Aspect.EARTH, 5));

            helper.registerTCObjectTag(TFBlocks.wispy_cloud, new AspectList()
                    .add(Aspect.AIR, 20)
                    .add(Aspect.FLIGHT, 5));

            helper.registerTCObjectTag(TFBlocks.fluffy_cloud, new AspectList()
                    .add(Aspect.AIR, 20)
                    .add(Aspect.FLIGHT, 5));

            helper.registerTCObjectTag(TFBlocks.giant_cobblestone, new AspectList()
                    .add(Aspect.EARTH, 320)
                    .add(Aspect.ENTROPY, 64));

            helper.registerTCObjectTag(TFBlocks.giant_log, new AspectList()
                    .add(Aspect.PLANT, 1280));

            helper.registerTCObjectTag(TFBlocks.giant_leaves, new AspectList()
                    .add(Aspect.PLANT, 320));

            helper.registerTCObjectTag(TFBlocks.giant_obsidian, new AspectList()
                    .add(Aspect.EARTH, 320)
                    .add(Aspect.FIRE, 320)
                    .add(Aspect.DARKNESS, 320));

            helper.registerTCObjectTag(TFBlocks.uberous_soil, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.PLANT, 2)
                    .add(Aspect.LIFE, 15));

            helper.registerTCObjectTag(TFBlocks.huge_stalk, new AspectList()
                    .add(Aspect.PLANT, 25));

            helper.registerTCObjectTag(TFBlocks.huge_mushgloom, -1, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 5)
                    .add(Aspect.AIR, 5));

            helper.registerTCObjectTag(TFBlocks.trollvidr, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 3));

            helper.registerTCObjectTag(TFBlocks.unripe_trollber, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.DARKNESS, 6));

            helper.registerTCObjectTag(TFBlocks.trollber, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIGHT, 25)
                    .add(Aspect.DARKNESS, 3));

            helper.registerTCObjectTag(TFBlocks.knightmetal_block, new AspectList()
                    .add(Aspect.METAL, 121)
                    .add(Aspect.CRAFT, 40)
                    .add(Aspect.AVERSION, 6));

            helper.registerTCObjectTag(TFBlocks.huge_lilypad, -1, new AspectList()
                    .add(Aspect.PLANT, 25)
                    .add(Aspect.WATER, 5));

            helper.registerTCObjectTag(TFBlocks.huge_waterlily, new AspectList()
                    .add(Aspect.PLANT, 10)
                    .add(Aspect.WATER, 2));

            helper.registerTCObjectTag(TFBlocks.castle_brick, new int[]{0, 1, 3, 5}, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            helper.registerTCObjectTag(TFBlocks.castle_brick, 2, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.castle_brick, 4, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.castle_pillar, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            helper.registerTCObjectTag(TFBlocks.castle_stairs, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1));

            helper.registerTCObjectTag(TFBlocks.castle_rune_brick, -1, new AspectList()
                    .add(Aspect.ORDER, 5)
                    .add(Aspect.MECHANISM, 1)
                    .add(Aspect.AURA, 3));

            helper.registerTCObjectTag(TFBlocks.force_field, -1, new AspectList()
                    .add(Aspect.AURA, 40));

            helper.registerTCObjectTag(TFBlocks.cinder_log, -1, new AspectList()
                    .add(Aspect.FIRE, 4)
                    .add(Aspect.ENTROPY, 12));

            helper.registerTCObjectTag(TFBlocks.castle_door, -1, new AspectList()
                    .add(Aspect.AURA, 20)
                    .add(Aspect.MECHANISM, 10)
                    .add(Aspect.TRAP, 5));

            helper.registerTCObjectTag(TFBlocks.spiral_bricks, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.etched_nagastone, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_stairs, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_pillar, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1));

            helper.registerTCObjectTag(TFBlocks.etched_nagastone_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_stairs_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_pillar_mossy, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ORDER, 1)
                    .add(Aspect.PLANT, 1));

            helper.registerTCObjectTag(TFBlocks.etched_nagastone_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_stairs_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFBlocks.nagastone_pillar_weathered, -1, new AspectList()
                    .add(Aspect.EARTH, 5)
                    .add(Aspect.ENTROPY, 1));

            helper.registerTCObjectTag(TFItems.naga_scale, new AspectList()
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.BEAST, 4)
                    .add(Aspect.MOTION, 2));

            helper.registerTCObjectTag(TFItems.naga_chestplate, -1, new AspectList()
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.BEAST, 12)
                    .add(Aspect.MOTION, 15));

            helper.registerTCObjectTag(TFItems.naga_leggings, -1, new AspectList()
                    .add(Aspect.PROTECT, 24)
                    .add(Aspect.BEAST, 10)
                    .add(Aspect.MOTION, 12));

            helper.registerTCObjectTag(TFItems.twilight_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC, 60)
                    .add(Aspect.DARKNESS, 30)
                    .add(Aspect.VOID, 15));

            helper.registerTCObjectTag(TFItems.lifedrain_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC,  60)
                    .add(Aspect.LIFE, 26)
                    .add(Aspect.EXCHANGE,18));

            helper.registerTCObjectTag(TFItems.zombie_scepter, -1, new AspectList()
                    .add(Aspect.MAGIC, 60)
                    .add(Aspect.DEATH, 25)
                    .add(Aspect.SOUL, 25)
                    .add(Aspect.TRAP, 22));

            helper.registerTCObjectTag(TFItems.ore_meter, new AspectList()
                    .add(Aspect.SENSES, 17)
                    .add(Aspect.DESIRE, 8)
                    .add(Aspect.MECHANISM, 15)
                    .add(Aspect.MIND, 10));

            helper.registerTCObjectTag(TFItems.magic_map, -1, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.LIGHT, 5));

            helper.registerTCObjectTag(TFItems.maze_map, -1, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.TRAP, 10));

            helper.registerTCObjectTag(TFItems.ore_map, -1, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.DESIRE, 72));

            helper.registerTCObjectTag(TFItems.raven_feather, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.DARKNESS, 1));

            helper.registerTCObjectTag(TFItems.magic_map_focus, new AspectList()
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.AIR, 5)
                    .add(Aspect.LIGHT, 14));

            helper.registerTCObjectTag(TFItems.maze_map_focus, new AspectList()
                    .add(Aspect.TRAP, 10)
                    .add(Aspect.MECHANISM, 15));

            helper.registerTCObjectTag(TFItems.liveroot, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7));

            helper.registerTCObjectTag(TFItems.ironwood_raw, new AspectList()
                    .add(Aspect.EARTH, 9)
                    .add(Aspect.LIFE, 6)
                    .add(Aspect.PLANT, 7)
                    .add(Aspect.METAL, 16)
                    .add(Aspect.DESIRE, 1));

            helper.registerTCObjectTag(TFItems.ironwood_ingot, new AspectList()
                    .add(Aspect.METAL, 8)
                    .add(Aspect.PLANT, 3));

            helper.registerTCObjectTag(TFItems.torchberries, new AspectList()
                    .add(Aspect.LIGHT, 8)
                    .add(Aspect.PLANT, 3));

            helper.registerTCObjectTag(TFItems.raw_venison, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EARTH, 5));

            helper.registerTCObjectTag(TFItems.cooked_venison, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.CRAFT, 1));

            helper.registerTCObjectTag(TFItems.hydra_chop, new AspectList()
                    .add(Aspect.BEAST, 25)
                    .add(Aspect.LIFE, 25)
                    .add(Aspect.FIRE, 5));

            helper.registerTCObjectTag(TFItems.fiery_blood, new AspectList()
                    .add(Aspect.FIRE, 40)
                    .add(Aspect.LIFE, 22));

            helper.registerTCObjectTag(TFItems.trophy, 0, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 80)
                    .add(Aspect.MOTION, 40));

            helper.registerTCObjectTag(TFItems.trophy, 1, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.UNDEAD, 75)
                    .add(Aspect.MAN, 5)
                    .add(Aspect.ELDRITCH, 40));

            helper.registerTCObjectTag(TFItems.trophy, 2, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 30)
                    .add(Aspect.FIRE, 80));

            helper.registerTCObjectTag(TFItems.trophy, 3, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.FLUX, 80)
                    .add(Aspect.ALCHEMY, 40));

            helper.registerTCObjectTag(TFItems.trophy, 4, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.SOUL, 80)
                    .add(Aspect.MAN, 40));

            helper.registerTCObjectTag(TFItems.trophy, 5, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.COLD, 80)
                    .add(Aspect.MAN, 40));

            helper.registerTCObjectTag(TFItems.trophy, 6, new AspectList()
                    .add(Aspect.DEATH, 45)
                    .add(Aspect.SENSES, 20)
                    .add(Aspect.BEAST, 70)
                    .add(Aspect.MAN, 50));

            helper.registerTCObjectTag(TFItems.trophy, 8, new AspectList()
                    .add(Aspect.LIFE, 45)
                    .add(Aspect.DESIRE, 40)
                    .add(Aspect.EXCHANGE, 40));

            helper.registerTCObjectTag(TFItems.steeleaf_ingot, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.METAL, 4));

            helper.registerTCObjectTag(TFItems.minotaur_axe, -1, new AspectList()
                    .add(Aspect.AVERSION, 20)
                    .add(Aspect.TOOL, 16)
                    .add(Aspect.DESIRE, 44)
                    .add(Aspect.CRYSTAL, 44));

            helper.registerTCObjectTag(TFItems.mazebreaker_pickaxe, -1, new AspectList()
                    .add(Aspect.TOOL, 16)
                    .add(Aspect.TRAP, 8)
                    .add(Aspect.METAL, 33));

            helper.registerTCObjectTag(TFItems.transformation_powder, new AspectList()
                    .add(Aspect.EXCHANGE, 30)
                    .add(Aspect.MAGIC, 12));

            helper.registerTCObjectTag(TFItems.raw_meef, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.EARTH, 5));

            helper.registerTCObjectTag(TFItems.cooked_meef, new AspectList()
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.LIFE, 5)
                    .add(Aspect.CRAFT, 1));

            helper.registerTCObjectTag(TFItems.meef_stroganoff, new AspectList()
                    .add(Aspect.BEAST, 25)
                    .add(Aspect.MAN, 15)
                    .add(Aspect.LIFE, 25)
                    .add(Aspect.CRAFT, 5));

            helper.registerTCObjectTag(TFItems.maze_wafer, new AspectList()
                    .add(Aspect.PLANT, 2));

            helper.registerTCObjectTag(TFItems.ore_magnet, -1, new AspectList()
                    .add(Aspect.METAL, 24)
                    .add(Aspect.DESIRE, 30));

            helper.registerTCObjectTag(TFItems.crumble_horn, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.SENSES, 30)
                    .add(Aspect.MAGIC, 32));

            helper.registerTCObjectTag(TFItems.peacock_fan, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.MOTION, 48)
                    .add(Aspect.FLIGHT, 60)
                    .add(Aspect.MAGIC, 32));

            helper.registerTCObjectTag(TFItems.moonworm_queen, -1, new AspectList()
                    .add(Aspect.BEAST, 35)
                    .add(Aspect.LIGHT, 54)
                    .add(Aspect.LIFE, 36)
                    .add(Aspect.EXCHANGE, 28));

            helper.registerTCObjectTag(TFItems.charm_of_life_1, new AspectList()
                    .add(Aspect.LIFE, 10)
                    .add(Aspect.DEATH, 10)
                    .add(Aspect.EXCHANGE, 16 )
                    .add(Aspect.MAGIC, 8));

            helper.registerTCObjectTag(TFItems.charm_of_keeping_1, new AspectList()
                    .add(Aspect.EXCHANGE, 16)
                    .add(Aspect.MAGIC, 8));

            helper.registerTCObjectTag(TFItems.tower_key, new AspectList()
                    .add(Aspect.TRAP, 16));

            helper.registerTCObjectTag(TFItems.armor_shard, new AspectList()
                    .add(Aspect.METAL, 2));

            helper.registerTCObjectTag(TFItems.knightmetal_ingot, new AspectList()
                    .add(Aspect.METAL, 18)
                    .add(Aspect.CRAFT, 6));

            helper.registerTCObjectTag(TFItems.phantom_helmet, -1, new AspectList()
                    .add(Aspect.PROTECT, 12)
                    .add(Aspect.SOUL, 24)
                    .add(Aspect.DEATH, 7)
                    .add(Aspect.ELDRITCH, 8)
                    .add(Aspect.TRAP, 22));

            helper.registerTCObjectTag(TFItems.phantom_chestplate, -1, new AspectList()
                    .add(Aspect.PROTECT, 32)
                    .add(Aspect.SOUL, 62)
                    .add(Aspect.DEATH, 19)
                    .add(Aspect.ELDRITCH, 22)
                    .add(Aspect.TRAP, 48));

            helper.registerTCObjectTag(TFItems.lamp_of_cinders, -1, new AspectList()
                    .add(Aspect.MAGIC, 53)
                    .add(Aspect.FIRE, 87)
                    .add(Aspect.ENERGY, 42));

            helper.registerTCObjectTag(TFItems.fiery_tears, new AspectList()
                    .add(Aspect.FIRE, 40)
                    .add(Aspect.WATER, 22));

            helper.registerTCObjectTag(TFItems.alpha_fur, new AspectList()
                    .add(Aspect.BEAST, 48)
                    .add(Aspect.COLD, 28));

            helper.registerTCObjectTag(TFItems.ice_bomb, new AspectList()
                    .add(Aspect.COLD, 22)
                    .add(Aspect.MOTION, 8));

            helper.registerTCObjectTag(TFItems.arctic_fur, new AspectList()
                    .add(Aspect.BEAST, 12)
                    .add(Aspect.COLD, 7));

            helper.registerTCObjectTag(TFItems.magic_beans, new AspectList()
                    .add(Aspect.PLANT, 8)
                    .add(Aspect.MAGIC, 36));

            helper.registerTCObjectTag(TFItems.triple_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.EXCHANGE, 30));

            helper.registerTCObjectTag(TFItems.seeker_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.MECHANISM, 30));

            helper.registerTCObjectTag(TFItems.ice_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.COLD, 30));

            helper.registerTCObjectTag(TFItems.ender_bow, -1, new AspectList()
                    .add(Aspect.AVERSION, 10)
                    .add(Aspect.FLIGHT, 5)
                    .add(Aspect.ELDRITCH, 30));

            helper.registerTCObjectTag(TFItems.ice_sword, -1, new AspectList()
                    .add(Aspect.AVERSION, 16)
                    .add(Aspect.COLD, 24));

            helper.registerTCObjectTag(TFItems.glass_sword, -1, new AspectList()
                    .add(Aspect.AVERSION, 148)
                    .add(Aspect.CRYSTAL, 96));

            helper.registerTCObjectTag(TFItems.fiery_ingot, new AspectList()
                    .add(Aspect.FIRE, 40)
                    .add(Aspect.METAL, 15)
                    .add(Aspect.AURA, 12));

            helper.registerTCObjectTag(TFItems.fiery_boots, -1, new AspectList()
                    .add(Aspect.METAL, 45)
                    .add(Aspect.FIRE, 120)
                    .add(Aspect.PROTECT, 16)
                    .add(Aspect.AURA, 36));

            helper.registerTCObjectTag(TFItems.fiery_leggings, -1, new AspectList()
                    .add(Aspect.METAL, 78)
                    .add(Aspect.FIRE, 210)
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.AURA, 63));

            helper.registerTCObjectTag(TFItems.fiery_chestplate, -1, new AspectList()
                    .add(Aspect.METAL, 90)
                    .add(Aspect.FIRE, 240)
                    .add(Aspect.PROTECT, 36)
                    .add(Aspect.AURA, 72));

            helper.registerTCObjectTag(TFItems.fiery_helmet, -1, new AspectList()
                    .add(Aspect.METAL, 56)
                    .add(Aspect.FIRE, 150)
                    .add(Aspect.PROTECT, 16)
                    .add(Aspect.AURA, 45));

            helper.registerTCObjectTag(TFItems.yeti_boots, -1, new AspectList()
                    .add(Aspect.BEAST, 144)
                    .add(Aspect.COLD, 84)
                    .add(Aspect.PROTECT, 12)
                    .add(Aspect.AURA, 36));

            helper.registerTCObjectTag(TFItems.yeti_leggings, -1, new AspectList()
                    .add(Aspect.BEAST, 252)
                    .add(Aspect.COLD, 147)
                    .add(Aspect.PROTECT, 24)
                    .add(Aspect.AURA, 63));

            helper.registerTCObjectTag(TFItems.yeti_chestplate, -1, new AspectList()
                    .add(Aspect.BEAST, 288)
                    .add(Aspect.COLD, 168)
                    .add(Aspect.PROTECT, 28)
                    .add(Aspect.AURA, 72));

            helper.registerTCObjectTag(TFItems.yeti_helmet, -1, new AspectList()
                    .add(Aspect.BEAST, 180)
                    .add(Aspect.COLD, 105)
                    .add(Aspect.PROTECT, 16)
                    .add(Aspect.AURA, 45));

            helper.registerTCObjectTag(TFItems.borer_essence, new AspectList()
                    .add(Aspect.ALCHEMY, 10));

            helper.registerTCObjectTag(TFItems.carminite, new AspectList()
                    .add(Aspect.ALCHEMY, 38)
                    .add(Aspect.ENERGY, 30)
                    .add(Aspect.FLUX, 26)
                    .add(Aspect.SOUL, 7)
                    .add(Aspect.UNDEAD, 3));

            helper.registerTCObjectTag(TFItems.experiment_115, new AspectList()
                    .add(Aspect.FLUX, 15)
                    .add(Aspect.ALCHEMY, 2));

            helper.registerTCObjectTag(TFItems.magic_map_empty, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.LIGHT, 5));

            helper.registerTCObjectTag(TFItems.maze_map_empty, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.TRAP, 10));

            helper.registerTCObjectTag(TFItems.ore_map_empty, new AspectList()
                    .add(Aspect.PLANT, 18)
                    .add(Aspect.MIND, 16)
                    .add(Aspect.MAGIC, 12)
                    .add(Aspect.SENSES, 10)
                    .add(Aspect.DESIRE, 72));

            helper.registerTCObjectTag(TFItems.block_and_chain, -1, new AspectList()
                    .add(Aspect.METAL, 171)
                    .add(Aspect.CRAFT, 57)
                    .add(Aspect.TOOL, 16)
                    .add(Aspect.AVERSION, 16));

            helper.registerTCObjectTag(TFBlocks.twilight_plant, 7, new AspectList()
                    .add(Aspect.LIGHT, 10)
                    .add(Aspect.PLANT, 5));

            helper.registerTCObjectTag(TFBlocks.thorn_rose, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.AVERSION, 2)
                    .add(Aspect.SENSES, 2));

            //TwilightForestMod.LOGGER.info("Loaded ThaumcraftApi integration.");
        } catch (Exception e) {
            //TwilightForestMod.LOGGER.warn("Had an %s error while trying to register with ThaumcraftApi.", e.getLocalizedMessage());
            // whatever.
        }
    }

    private static class TFAspectRegisterHelper {

        private final AspectRegistryEvent event;

        private TFAspectRegisterHelper(AspectRegistryEvent event) {
            this.event = event;
        }

        private void registerTCObjectTag(Block block, AspectList list) {
            registerTCObjectTag(new ItemStack(block), list);
        }

        // Register a block with Thaumcraft aspects
        private void registerTCObjectTag(Block block, int meta, AspectList list) {
            if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
            registerTCObjectTag(new ItemStack(block, 1, meta), list);
        }

        // Register blocks with Thaumcraft aspects
        private void registerTCObjectTag(Block block, int[] metas, AspectList list) {
            for (int meta : metas)
                this.registerTCObjectTag(block, meta, list);
        }

        private void registerTCObjectTag(Item item, AspectList list) {
            registerTCObjectTag(new ItemStack(item), list);
        }

        // Register an item with Thaumcraft aspects
        private void registerTCObjectTag(Item item, int meta, AspectList list) {
            if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
            registerTCObjectTag(new ItemStack(item, 1, meta), list);
        }

        // Register item swith Thaumcraft aspects
        private void registerTCObjectTag(Item item, int[] metas, AspectList list) {
            for (int meta : metas)
                this.registerTCObjectTag(item, meta, list);
        }

        private void registerTCObjectTag(ItemStack stack, AspectList list) {
            event.register.registerObjectTag(stack, list);
        }
    }
}
