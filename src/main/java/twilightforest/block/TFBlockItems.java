package twilightforest.block;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.client.ISTER;
import twilightforest.item.*;
import twilightforest.tileentity.TFTileEntities;

import java.util.Objects;
import java.util.concurrent.Callable;

public class TFBlockItems {

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        r.register(new ItemTFTrophy(TFBlocks.naga_trophy.get(), TFBlocks.naga_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.naga_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.lich_trophy.get(), TFBlocks.lich_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.lich_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.minoshroom_trophy.get(), TFBlocks.minoshroom_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.minoshroom_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.hydra_trophy.get(), TFBlocks.hydra_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.hydra_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.knight_phantom_trophy.get(), TFBlocks.knight_phantom_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.knight_phantom_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.ur_ghast_trophy.get(), TFBlocks.ur_ghast_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.ur_ghast_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.snow_queen_trophy.get(), TFBlocks.snow_queen_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.snow_queen_trophy.get().getRegistryName())));
        r.register(new ItemTFTrophy(TFBlocks.quest_ram_trophy.get(), TFBlocks.quest_ram_wall_trophy.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.TROPHY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.quest_ram_trophy.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.twilight_portal_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_portal_miniature_structure.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.hedge_maze_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.hedge_maze_miniature_structure.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.hollow_hill_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.hollow_hill_miniature_structure.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.mushroom_tower_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mushroom_tower_miniature_structure.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.quest_grove_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.quest_grove_miniature_structure.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.naga_courtyard_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.naga_courtyard_miniature_structure.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.lich_tower_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.lich_tower_miniature_structure.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.minotaur_labyrinth_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.hydra_lair_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.goblin_stronghold_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.dark_tower_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.yeti_cave_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.aurora_palace_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.troll_cave_cottage_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.final_castle_miniature_structure.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks..get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.boss_spawner_naga.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_naga.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_lich.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_lich.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_minoshroom.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_minoshroom.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_hydra.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_hydra.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_knight_phantom.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_knight_phantom.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_ur_ghast.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_ur_ghast.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_alpha_yeti.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_alpha_yeti.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_snow_queen.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_snow_queen.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.boss_spawner_final_boss.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.boss_spawner_final_boss.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.etched_nagastone.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.etched_nagastone.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.etched_nagastone_weathered.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.etched_nagastone_weathered.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.etched_nagastone_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.etched_nagastone_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_pillar.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_pillar.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_pillar_weathered.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_pillar_weathered.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_pillar_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_pillar_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_left.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_left.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_weathered_left.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_weathered_left.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_mossy_left.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_mossy_left.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_right.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_right.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_weathered_right.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_weathered_right.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.nagastone_stairs_mossy_right.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.nagastone_stairs_mossy_right.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.naga_stone_head.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.naga_stone_head.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.naga_stone.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.naga_stone.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.spiral_bricks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.spiral_bricks.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.terrorcotta_circle.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.terrorcotta_circle.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.terrorcotta_diagonal.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.terrorcotta_diagonal.get().getRegistryName())));
//      r.register(new BlockItem(TFBlocks.lapis_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.lapis_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.stone_twist.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.stone_twist.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.stone_twist_thin.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.stone_twist_thin.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.keepsake_casket.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.KEEPSAKE_CASKET.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.keepsake_casket.get().getRegistryName())));
        r.register(new ItemBlockTFHugeWaterLily(TFBlocks.huge_waterlily.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.huge_waterlily.get().getRegistryName())));
        r.register(new ItemBlockTFHugeLilyPad(TFBlocks.huge_lilypad.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.huge_lilypad.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_brick.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_brick.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_decorative.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_decorative.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_chiseled.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_chiseled.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_border.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_border.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.maze_stone_mosaic.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.maze_stone_mosaic.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.smoker.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.smoker.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.encased_smoker.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.encased_smoker.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fire_jet.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fire_jet.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.encased_fire_jet.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.encased_fire_jet.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.stronghold_shield.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.stronghold_shield.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trophy_pedestal.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trophy_pedestal.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.underbrick.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.underbrick.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.underbrick_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.underbrick_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.underbrick_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.underbrick_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.underbrick_floor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.underbrick_floor.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.tower_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.tower_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.tower_wood_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.tower_wood_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.tower_wood_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.tower_wood_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.tower_wood_infested.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.tower_wood_infested.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.tower_wood_encased.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.tower_wood_encased.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.vanishing_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.vanishing_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.reappearing_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.reappearing_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.locked_vanishing_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.locked_vanishing_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.carminite_builder.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.carminite_builder.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.antibuilder.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.antibuilder.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.carminite_reactor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.carminite_reactor.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.ghast_trap.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.ghast_trap.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fake_gold.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fake_gold.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fake_diamond.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fake_diamond.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.aurora_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.aurora_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.aurora_pillar.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.aurora_pillar.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.aurora_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.aurora_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.auroralized_glass.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.auroralized_glass.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trollsteinn.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trollsteinn.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trollvidr.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trollvidr.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.unripe_trollber.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.unripe_trollber.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trollber.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trollber.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.huge_mushgloom.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.huge_mushgloom.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.huge_mushgloom_stem.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.huge_mushgloom_stem.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.uberous_soil.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.uberous_soil.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.huge_stalk.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.huge_stalk.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.beanstalk_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.beanstalk_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.wispy_cloud.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.wispy_cloud.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fluffy_cloud.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fluffy_cloud.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.giant_cobblestone.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.giant_cobblestone.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.giant_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.giant_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.giant_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.giant_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.giant_obsidian.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.giant_obsidian.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.deadrock.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.deadrock.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.deadrock_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.deadrock_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.deadrock_weathered.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.deadrock_weathered.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.brown_thorns.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.brown_thorns.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.green_thorns.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.green_thorns.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.burnt_thorns.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.burnt_thorns.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.thorn_rose.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.thorn_rose.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.thorn_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.thorn_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick_worn.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick_worn.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick_frame.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick_frame.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_brick_roof.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_brick_roof.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_pillar_encased.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_pillar_encased.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_pillar_encased_tile.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_pillar_encased_tile.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_pillar_bold.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_pillar_bold.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_pillar_bold_tile.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_pillar_bold_tile.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_brick.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_brick.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_worn.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_worn.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_cracked.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_cracked.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_mossy.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_mossy.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_encased.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_encased.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_stairs_bold.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_stairs_bold.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_rune_brick_pink.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_rune_brick_pink.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_rune_brick_yellow.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_rune_brick_yellow.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_rune_brick_blue.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_rune_brick_blue.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_rune_brick_purple.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_rune_brick_purple.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_door_pink.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_door_pink.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_door_yellow.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_door_yellow.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_door_blue.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_door_blue.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.castle_door_purple.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.castle_door_purple.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.force_field_pink.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.force_field_pink.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.force_field_orange.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.force_field_orange.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.force_field_green.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.force_field_green.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.force_field_blue.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.force_field_blue.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.force_field_purple.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.force_field_purple.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.uncrafting_table.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.uncrafting_table.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.cinder_furnace.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.cinder_furnace.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.cinder_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.cinder_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.cinder_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.cinder_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.slider.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.slider.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.iron_ladder.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.iron_ladder.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.ironwood_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.ironwood_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.steeleaf_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.steeleaf_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fiery_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fiery_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.knightmetal_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.knightmetal_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.carminite_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.carminite_block.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.arctic_fur_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.arctic_fur_block.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.moss_patch.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.moss_patch.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mayapple.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mayapple.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.clover_patch.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.clover_patch.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fiddlehead.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fiddlehead.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mushgloom.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mushgloom.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.torchberry_plant.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.torchberry_plant.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.root_strand.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.root_strand.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.fallen_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.fallen_leaves.get().getRegistryName())));
        r.register(new ItemBlockWearable(TFBlocks.firefly.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.FIREFLY.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.firefly.get().getRegistryName())));
        r.register(new ItemBlockWearable(TFBlocks.cicada.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.CICADA.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.cicada.get().getRegistryName())));
        r.register(new ItemBlockWearable(TFBlocks.moonworm.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
            @Override
            public ItemStackTileEntityRenderer call() {
                return new ISTER(TFTileEntities.MOONWORM.getId());
            }
        })).setRegistryName(Objects.requireNonNull(TFBlocks.moonworm.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.firefly_jar.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.firefly_jar.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.cicada_jar.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.cicada_jar.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.hedge.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.hedge.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.root.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.root.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.liveroot_block.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.liveroot_block.get().getRegistryName())));

        r.register(new BlockItem(TFBlocks.oak_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.oak_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.transformation_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.transformation_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mining_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mining_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sorting_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sorting_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.rainboak_leaves.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.rainboak_leaves.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.oak_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.oak_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.transformation_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.transformation_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mining_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mining_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sorting_log.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sorting_log.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.oak_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.oak_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.transformation_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.transformation_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mining_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mining_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sorting_wood.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sorting_wood.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_log_core.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_log_core.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.transformation_log_core.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.transformation_log_core.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mining_log_core.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mining_log_core.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sorting_log_core.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sorting_log_core.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.oak_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.oak_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.darkwood_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.darkwood_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.hollow_oak_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.hollow_oak_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.transformation_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.transformation_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mining_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mining_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sorting_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sorting_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.rainboak_sapling.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.rainboak_sapling.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.twilight_oak_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.twilight_oak_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.twilight_oak_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.twilight_oak_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.twilight_oak_sign.get(), TFBlocks.twilight_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.twilight_oak_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.canopy_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.canopy_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.canopy_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.canopy_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.canopy_sign.get(), TFBlocks.canopy_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.canopy_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.mangrove_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.mangrove_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mangrove_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.mangrove_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.mangrove_sign.get(), TFBlocks.mangrove_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.mangrove_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.dark_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.dark_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.dark_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.dark_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.dark_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.dark_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.dark_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.darkwood_sign.get(), TFBlocks.darkwood_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.darkwood_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.time_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.time_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.time_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.time_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.time_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.time_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.time_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.time_sign.get(), TFBlocks.time_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.time_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.trans_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.trans_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.trans_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.trans_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.trans_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.trans_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.trans_sign.get(), TFBlocks.trans_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.trans_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.mine_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.mine_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.mine_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.mine_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.mine_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.mine_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.mine_sign.get(), TFBlocks.mine_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.mine_sign.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_planks.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_planks.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_stairs.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_stairs.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_slab.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_slab.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_button.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_button.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.sort_fence.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.sort_fence.get().getRegistryName())));
        r.register(new ItemTFFurnaceFuel(TFBlocks.sort_gate.get(), TFItems.defaultBuilder(), 300).setRegistryName(Objects.requireNonNull(TFBlocks.sort_gate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_plate.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_plate.get().getRegistryName())));
        r.register(new BlockItem(TFBlocks.sort_trapdoor.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_trapdoor.get().getRegistryName())));
        r.register(new TallBlockItem(TFBlocks.sort_door.get(), TFItems.defaultBuilder()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_door.get().getRegistryName())));
        r.register(new SignItem(TFItems.defaultBuilder().maxStackSize(16), TFBlocks.sort_sign.get(), TFBlocks.sort_wall_sign.get()).setRegistryName(Objects.requireNonNull(TFBlocks.sort_sign.get().getRegistryName())));
    }
}
