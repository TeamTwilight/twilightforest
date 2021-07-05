package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.client.ISTER;
import twilightforest.item.*;
import twilightforest.tileentity.TFTileEntities;

import java.util.Objects;
import java.util.concurrent.Callable;

public class TFBlockItems {

	public static void registerBlockItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> r = event.getRegistry();

		r.register(trophyBlock(TFBlocks.naga_trophy, TFBlocks.naga_wall_trophy));
		r.register(trophyBlock(TFBlocks.lich_trophy, TFBlocks.lich_wall_trophy));
		r.register(trophyBlock(TFBlocks.minoshroom_trophy, TFBlocks.minoshroom_wall_trophy));
		r.register(trophyBlock(TFBlocks.hydra_trophy, TFBlocks.hydra_wall_trophy));
		r.register(trophyBlock(TFBlocks.knight_phantom_trophy, TFBlocks.knight_phantom_wall_trophy));
		r.register(trophyBlock(TFBlocks.ur_ghast_trophy, TFBlocks.ur_ghast_wall_trophy));
		r.register(trophyBlock(TFBlocks.yeti_trophy, TFBlocks.yeti_wall_trophy));
		r.register(trophyBlock(TFBlocks.snow_queen_trophy, TFBlocks.snow_queen_wall_trophy));
		r.register(trophyBlock(TFBlocks.quest_ram_trophy, TFBlocks.quest_ram_wall_trophy));

		r.register(blockItem(TFBlocks.twilight_portal_miniature_structure));
//      r.register(blockItem(TFBlocks.hedge_maze_miniature_structure));
//      r.register(blockItem(TFBlocks.hollow_hill_miniature_structure));
//      r.register(blockItem(TFBlocks.mushroom_tower_miniature_structure));
//      r.register(blockItem(TFBlocks.quest_grove_miniature_structure));
		r.register(blockItem(TFBlocks.naga_courtyard_miniature_structure));
		r.register(blockItem(TFBlocks.lich_tower_miniature_structure));
//      r.register(blockItem(TFBlocks.minotaur_labyrinth_miniature_structure));
//      r.register(blockItem(TFBlocks.hydra_lair_miniature_structure));
//      r.register(blockItem(TFBlocks.goblin_stronghold_miniature_structure));
//      r.register(blockItem(TFBlocks.dark_tower_miniature_structure));
//      r.register(blockItem(TFBlocks.yeti_cave_miniature_structure));
//      r.register(blockItem(TFBlocks.aurora_palace_miniature_structure));
//      r.register(blockItem(TFBlocks.troll_cave_cottage_miniature_structure));
//      r.register(blockItem(TFBlocks.final_castle_miniature_structure));

		r.register(blockItem(TFBlocks.boss_spawner_naga));
		r.register(blockItem(TFBlocks.boss_spawner_lich));
		r.register(blockItem(TFBlocks.boss_spawner_minoshroom));
		r.register(blockItem(TFBlocks.boss_spawner_hydra));
		r.register(blockItem(TFBlocks.boss_spawner_knight_phantom));
		r.register(blockItem(TFBlocks.boss_spawner_ur_ghast));
		r.register(blockItem(TFBlocks.boss_spawner_alpha_yeti));
		r.register(blockItem(TFBlocks.boss_spawner_snow_queen));
		r.register(blockItem(TFBlocks.boss_spawner_final_boss));

		r.register(blockItem(TFBlocks.etched_nagastone));
		r.register(blockItem(TFBlocks.etched_nagastone_weathered));
		r.register(blockItem(TFBlocks.etched_nagastone_mossy));
		r.register(blockItem(TFBlocks.nagastone_pillar));
		r.register(blockItem(TFBlocks.nagastone_pillar_weathered));
		r.register(blockItem(TFBlocks.nagastone_pillar_mossy));
		r.register(blockItem(TFBlocks.nagastone_stairs_left));
		r.register(blockItem(TFBlocks.nagastone_stairs_weathered_left));
		r.register(blockItem(TFBlocks.nagastone_stairs_mossy_left));
		r.register(blockItem(TFBlocks.nagastone_stairs_right));
		r.register(blockItem(TFBlocks.nagastone_stairs_weathered_right));
		r.register(blockItem(TFBlocks.nagastone_stairs_mossy_right));
		r.register(blockItem(TFBlocks.naga_stone_head));
		r.register(blockItem(TFBlocks.naga_stone));
		r.register(blockItem(TFBlocks.spiral_bricks));
//      r.register(blockItem(TFBlocks.terrorcotta_circle));
//      r.register(blockItem(TFBlocks.terrorcotta_diagonal));
//      r.register(blockItem(TFBlocks.lapis_block));
		r.register(blockItem(TFBlocks.stone_twist));
		r.register(blockItem(TFBlocks.stone_twist_thin));
		r.register(makeBlockItem(new BlockItem(TFBlocks.keepsake_casket.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
			@Override
			public ItemStackTileEntityRenderer call() {
				return new ISTER(TFTileEntities.KEEPSAKE_CASKET.getId());
			}
		})), TFBlocks.keepsake_casket));
		r.register(makeBlockItem(new ItemBlockTFHugeWaterLily(TFBlocks.huge_waterlily.get(), TFItems.defaultBuilder()), TFBlocks.huge_waterlily));
		r.register(makeBlockItem(new ItemBlockTFHugeLilyPad(TFBlocks.huge_lilypad.get(), TFItems.defaultBuilder()), TFBlocks.huge_lilypad));
		r.register(blockItem(TFBlocks.maze_stone));
		r.register(blockItem(TFBlocks.maze_stone_brick));
		r.register(blockItem(TFBlocks.maze_stone_cracked));
		r.register(blockItem(TFBlocks.maze_stone_mossy));
		r.register(blockItem(TFBlocks.maze_stone_decorative));
		r.register(blockItem(TFBlocks.maze_stone_chiseled));
		r.register(blockItem(TFBlocks.maze_stone_border));
		r.register(blockItem(TFBlocks.maze_stone_mosaic));
		r.register(blockItem(TFBlocks.smoker));
		r.register(blockItem(TFBlocks.encased_smoker));
		r.register(blockItem(TFBlocks.fire_jet));
		r.register(blockItem(TFBlocks.encased_fire_jet));
		r.register(blockItem(TFBlocks.stronghold_shield));
		r.register(blockItem(TFBlocks.trophy_pedestal));
		r.register(blockItem(TFBlocks.underbrick));
		r.register(blockItem(TFBlocks.underbrick_cracked));
		r.register(blockItem(TFBlocks.underbrick_mossy));
		r.register(blockItem(TFBlocks.underbrick_floor));
		r.register(blockItem(TFBlocks.tower_wood));
		r.register(blockItem(TFBlocks.tower_wood_cracked));
		r.register(blockItem(TFBlocks.tower_wood_mossy));
		r.register(blockItem(TFBlocks.tower_wood_infested));
		r.register(blockItem(TFBlocks.tower_wood_encased));
		r.register(blockItem(TFBlocks.vanishing_block));
		r.register(blockItem(TFBlocks.reappearing_block));
		r.register(blockItem(TFBlocks.locked_vanishing_block));
		r.register(blockItem(TFBlocks.carminite_builder));
		r.register(blockItem(TFBlocks.antibuilder));
		r.register(blockItem(TFBlocks.carminite_reactor));
		r.register(blockItem(TFBlocks.ghast_trap));
		r.register(blockItem(TFBlocks.fake_gold));
		r.register(blockItem(TFBlocks.fake_diamond));
		r.register(blockItem(TFBlocks.aurora_block));
		r.register(blockItem(TFBlocks.aurora_pillar));
		r.register(blockItem(TFBlocks.aurora_slab));
		r.register(blockItem(TFBlocks.auroralized_glass));
		r.register(blockItem(TFBlocks.trollsteinn));
		r.register(blockItem(TFBlocks.trollvidr));
		r.register(blockItem(TFBlocks.unripe_trollber));
		r.register(blockItem(TFBlocks.trollber));
		r.register(blockItem(TFBlocks.huge_mushgloom));
		r.register(blockItem(TFBlocks.huge_mushgloom_stem));
		r.register(blockItem(TFBlocks.uberous_soil));
		r.register(blockItem(TFBlocks.huge_stalk));
		r.register(blockItem(TFBlocks.beanstalk_leaves));
		r.register(blockItem(TFBlocks.wispy_cloud));
		r.register(blockItem(TFBlocks.fluffy_cloud));
		r.register(blockItem(TFBlocks.giant_cobblestone));
		r.register(blockItem(TFBlocks.giant_log));
		r.register(blockItem(TFBlocks.giant_leaves));
		r.register(blockItem(TFBlocks.giant_obsidian));
		r.register(blockItem(TFBlocks.deadrock));
		r.register(blockItem(TFBlocks.deadrock_cracked));
		r.register(blockItem(TFBlocks.deadrock_weathered));
		r.register(blockItem(TFBlocks.brown_thorns));
		r.register(blockItem(TFBlocks.green_thorns));
		r.register(blockItem(TFBlocks.burnt_thorns));
		r.register(blockItem(TFBlocks.thorn_rose));
		r.register(blockItem(TFBlocks.thorn_leaves));
		r.register(blockItem(TFBlocks.castle_brick));
		r.register(blockItem(TFBlocks.castle_brick_worn));
		r.register(blockItem(TFBlocks.castle_brick_cracked));
		r.register(blockItem(TFBlocks.castle_brick_mossy));
		r.register(blockItem(TFBlocks.castle_brick_frame));
		r.register(blockItem(TFBlocks.castle_brick_roof));
		r.register(blockItem(TFBlocks.castle_pillar_encased));
		r.register(blockItem(TFBlocks.castle_pillar_encased_tile));
		r.register(blockItem(TFBlocks.castle_pillar_bold));
		r.register(blockItem(TFBlocks.castle_pillar_bold_tile));
		r.register(blockItem(TFBlocks.castle_stairs_brick));
		r.register(blockItem(TFBlocks.castle_stairs_worn));
		r.register(blockItem(TFBlocks.castle_stairs_cracked));
		r.register(blockItem(TFBlocks.castle_stairs_mossy));
		r.register(blockItem(TFBlocks.castle_stairs_encased));
		r.register(blockItem(TFBlocks.castle_stairs_bold));
		r.register(blockItem(TFBlocks.castle_rune_brick_pink));
		r.register(blockItem(TFBlocks.castle_rune_brick_yellow));
		r.register(blockItem(TFBlocks.castle_rune_brick_blue));
		r.register(blockItem(TFBlocks.castle_rune_brick_purple));
		r.register(blockItem(TFBlocks.castle_door_pink));
		r.register(blockItem(TFBlocks.castle_door_yellow));
		r.register(blockItem(TFBlocks.castle_door_blue));
		r.register(blockItem(TFBlocks.castle_door_purple));
		r.register(blockItem(TFBlocks.force_field_pink));
		r.register(blockItem(TFBlocks.force_field_orange));
		r.register(blockItem(TFBlocks.force_field_green));
		r.register(blockItem(TFBlocks.force_field_blue));
		r.register(blockItem(TFBlocks.force_field_purple));

		r.register(blockItem(TFBlocks.uncrafting_table));
		r.register(blockItem(TFBlocks.cinder_furnace));
		r.register(blockItem(TFBlocks.cinder_log));
		r.register(blockItem(TFBlocks.cinder_wood));
		r.register(blockItem(TFBlocks.slider));
		r.register(blockItem(TFBlocks.iron_ladder));

		r.register(blockItem(TFBlocks.ironwood_block));
		r.register(blockItem(TFBlocks.steeleaf_block));
		r.register(blockItem(TFBlocks.fiery_block));
		r.register(blockItem(TFBlocks.knightmetal_block));
		r.register(blockItem(TFBlocks.carminite_block));
		r.register(blockItem(TFBlocks.arctic_fur_block));

		r.register(blockItem(TFBlocks.moss_patch));
		r.register(blockItem(TFBlocks.mayapple));
		r.register(blockItem(TFBlocks.clover_patch));
		r.register(blockItem(TFBlocks.fiddlehead));
		r.register(blockItem(TFBlocks.mushgloom));
		r.register(blockItem(TFBlocks.torchberry_plant));
		r.register(blockItem(TFBlocks.root_strand));
		r.register(blockItem(TFBlocks.fallen_leaves));
		r.register(wearableBlock(TFBlocks.firefly, TFTileEntities.FIREFLY));
		r.register(wearableBlock(TFBlocks.cicada, TFTileEntities.CICADA));
		r.register(wearableBlock(TFBlocks.moonworm, TFTileEntities.MOONWORM));
		r.register(blockItem(TFBlocks.firefly_jar));
		r.register(blockItem(TFBlocks.cicada_jar));
		r.register(blockItem(TFBlocks.hedge));
		r.register(blockItem(TFBlocks.root));
		r.register(blockItem(TFBlocks.liveroot_block));

		r.register(blockItem(TFBlocks.oak_leaves));
		r.register(blockItem(TFBlocks.canopy_leaves));
		r.register(blockItem(TFBlocks.mangrove_leaves));
		r.register(blockItem(TFBlocks.dark_leaves));
		r.register(blockItem(TFBlocks.time_leaves));
		r.register(blockItem(TFBlocks.transformation_leaves));
		r.register(blockItem(TFBlocks.mining_leaves));
		r.register(blockItem(TFBlocks.sorting_leaves));
		r.register(blockItem(TFBlocks.rainboak_leaves));
		r.register(blockItem(TFBlocks.oak_log));
		r.register(blockItem(TFBlocks.canopy_log));
		r.register(blockItem(TFBlocks.mangrove_log));
		r.register(blockItem(TFBlocks.dark_log));
		r.register(blockItem(TFBlocks.time_log));
		r.register(blockItem(TFBlocks.transformation_log));
		r.register(blockItem(TFBlocks.mining_log));
		r.register(blockItem(TFBlocks.sorting_log));
		r.register(blockItem(TFBlocks.stripped_oak_log));
		r.register(blockItem(TFBlocks.stripped_canopy_log));
		r.register(blockItem(TFBlocks.stripped_mangrove_log));
		r.register(blockItem(TFBlocks.stripped_dark_log));
		r.register(blockItem(TFBlocks.stripped_time_log));
		r.register(blockItem(TFBlocks.stripped_transformation_log));
		r.register(blockItem(TFBlocks.stripped_mining_log));
		r.register(blockItem(TFBlocks.stripped_sorting_log));
		r.register(blockItem(TFBlocks.oak_wood));
		r.register(blockItem(TFBlocks.canopy_wood));
		r.register(blockItem(TFBlocks.mangrove_wood));
		r.register(blockItem(TFBlocks.dark_wood));
		r.register(blockItem(TFBlocks.time_wood));
		r.register(blockItem(TFBlocks.transformation_wood));
		r.register(blockItem(TFBlocks.mining_wood));
		r.register(blockItem(TFBlocks.sorting_wood));
		r.register(blockItem(TFBlocks.stripped_oak_wood));
		r.register(blockItem(TFBlocks.stripped_canopy_wood));
		r.register(blockItem(TFBlocks.stripped_mangrove_wood));
		r.register(blockItem(TFBlocks.stripped_dark_wood));
		r.register(blockItem(TFBlocks.stripped_time_wood));
		r.register(blockItem(TFBlocks.stripped_transformation_wood));
		r.register(blockItem(TFBlocks.stripped_mining_wood));
		r.register(blockItem(TFBlocks.stripped_sorting_wood));
		r.register(blockItem(TFBlocks.time_log_core));
		r.register(blockItem(TFBlocks.transformation_log_core));
		r.register(blockItem(TFBlocks.mining_log_core));
		r.register(blockItem(TFBlocks.sorting_log_core));
		r.register(blockItem(TFBlocks.oak_sapling));
		r.register(blockItem(TFBlocks.canopy_sapling));
		r.register(blockItem(TFBlocks.mangrove_sapling));
		r.register(blockItem(TFBlocks.darkwood_sapling));
		r.register(blockItem(TFBlocks.hollow_oak_sapling));
		r.register(blockItem(TFBlocks.time_sapling));
		r.register(blockItem(TFBlocks.transformation_sapling));
		r.register(blockItem(TFBlocks.mining_sapling));
		r.register(blockItem(TFBlocks.sorting_sapling));
		r.register(blockItem(TFBlocks.rainboak_sapling));
		r.register(blockItem(TFBlocks.twilight_oak_planks));
		r.register(blockItem(TFBlocks.twilight_oak_stairs));
		r.register(blockItem(TFBlocks.twilight_oak_slab));
		r.register(blockItem(TFBlocks.twilight_oak_button));
		r.register(burningItem(TFBlocks.twilight_oak_fence, 300));
		r.register(burningItem(TFBlocks.twilight_oak_gate, 300));
		r.register(blockItem(TFBlocks.twilight_oak_plate));
		r.register(blockItem(TFBlocks.twilight_oak_trapdoor));
		r.register(tallBlock(TFBlocks.twilight_oak_door));
		r.register(signBlock(TFBlocks.twilight_oak_sign, TFBlocks.twilight_wall_sign));
		r.register(blockItem(TFBlocks.canopy_planks));
		r.register(blockItem(TFBlocks.canopy_stairs));
		r.register(blockItem(TFBlocks.canopy_slab));
		r.register(blockItem(TFBlocks.canopy_button));
		r.register(burningItem(TFBlocks.canopy_fence, 300));
		r.register(burningItem(TFBlocks.canopy_gate, 300));
		r.register(blockItem(TFBlocks.canopy_plate));
		r.register(blockItem(TFBlocks.canopy_trapdoor));
		r.register(tallBlock(TFBlocks.canopy_door));
		r.register(signBlock(TFBlocks.canopy_sign, TFBlocks.canopy_wall_sign));
		r.register(blockItem(TFBlocks.mangrove_planks));
		r.register(blockItem(TFBlocks.mangrove_stairs));
		r.register(blockItem(TFBlocks.mangrove_slab));
		r.register(blockItem(TFBlocks.mangrove_button));
		r.register(burningItem(TFBlocks.mangrove_fence, 300));
		r.register(burningItem(TFBlocks.mangrove_gate, 300));
		r.register(blockItem(TFBlocks.mangrove_plate));
		r.register(blockItem(TFBlocks.mangrove_trapdoor));
		r.register(tallBlock(TFBlocks.mangrove_door));
		r.register(signBlock(TFBlocks.mangrove_sign, TFBlocks.mangrove_wall_sign));
		r.register(blockItem(TFBlocks.dark_planks));
		r.register(blockItem(TFBlocks.dark_stairs));
		r.register(blockItem(TFBlocks.dark_slab));
		r.register(blockItem(TFBlocks.dark_button));
		r.register(burningItem(TFBlocks.dark_fence, 300));
		r.register(burningItem(TFBlocks.dark_gate, 300));
		r.register(blockItem(TFBlocks.dark_plate));
		r.register(blockItem(TFBlocks.dark_trapdoor));
		r.register(tallBlock(TFBlocks.dark_door));
		r.register(signBlock(TFBlocks.darkwood_sign, TFBlocks.darkwood_wall_sign));
		r.register(blockItem(TFBlocks.time_planks));
		r.register(blockItem(TFBlocks.time_stairs));
		r.register(blockItem(TFBlocks.time_slab));
		r.register(blockItem(TFBlocks.time_button));
		r.register(burningItem(TFBlocks.time_fence, 300));
		r.register(burningItem(TFBlocks.time_gate, 300));
		r.register(blockItem(TFBlocks.time_plate));
		r.register(blockItem(TFBlocks.time_trapdoor));
		r.register(tallBlock(TFBlocks.time_door));
		r.register(signBlock(TFBlocks.time_sign, TFBlocks.time_wall_sign));
		r.register(blockItem(TFBlocks.trans_planks));
		r.register(blockItem(TFBlocks.trans_stairs));
		r.register(blockItem(TFBlocks.trans_slab));
		r.register(blockItem(TFBlocks.trans_button));
		r.register(burningItem(TFBlocks.trans_fence, 300));
		r.register(burningItem(TFBlocks.trans_gate, 300));
		r.register(blockItem(TFBlocks.trans_plate));
		r.register(blockItem(TFBlocks.trans_trapdoor));
		r.register(tallBlock(TFBlocks.trans_door));
		r.register(signBlock(TFBlocks.trans_sign, TFBlocks.trans_wall_sign));
		r.register(blockItem(TFBlocks.mine_planks));
		r.register(blockItem(TFBlocks.mine_stairs));
		r.register(blockItem(TFBlocks.mine_slab));
		r.register(blockItem(TFBlocks.mine_button));
		r.register(burningItem(TFBlocks.mine_fence, 300));
		r.register(burningItem(TFBlocks.mine_gate, 300));
		r.register(blockItem(TFBlocks.mine_plate));
		r.register(blockItem(TFBlocks.mine_trapdoor));
		r.register(tallBlock(TFBlocks.mine_door));
		r.register(signBlock(TFBlocks.mine_sign, TFBlocks.mine_wall_sign));
		r.register(blockItem(TFBlocks.sort_planks));
		r.register(blockItem(TFBlocks.sort_stairs));
		r.register(blockItem(TFBlocks.sort_slab));
		r.register(blockItem(TFBlocks.sort_button));
		r.register(burningItem(TFBlocks.sort_fence, 300));
		r.register(burningItem(TFBlocks.sort_gate, 300));
		r.register(blockItem(TFBlocks.sort_plate));
		r.register(blockItem(TFBlocks.sort_trapdoor));
		r.register(tallBlock(TFBlocks.sort_door));
		r.register(signBlock(TFBlocks.sort_sign, TFBlocks.sort_wall_sign));
	}

	private static <B extends Block> Item blockItem(RegistryObject<B> block) {
		return makeBlockItem(new BlockItem(block.get(), TFItems.defaultBuilder()), block);
	}

	private static <B extends Block> Item burningItem(RegistryObject<B> block, int burntime) {
		return makeBlockItem(new ItemTFFurnaceFuel(block.get(), TFItems.defaultBuilder(), burntime), block);
	}

	private static <B extends Block, W extends Block> Item trophyBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return makeBlockItem(new ItemTFTrophy(block.get(), wallblock.get(), TFItems.defaultBuilder().rarity(TwilightForestMod.getRarity()).setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
			@Override
			public ItemStackTileEntityRenderer call() {
				return new ISTER(TFTileEntities.TROPHY.getId());
			}
		})), block);
	}

	private static <T extends Block, E extends TileEntity> Item wearableBlock(RegistryObject<T> block, RegistryObject<TileEntityType<E>> tileentity) {
		return makeBlockItem(new ItemBlockWearable(block.get(), TFItems.defaultBuilder().setISTER(() -> new Callable<ItemStackTileEntityRenderer>() {
			@Override
			public ItemStackTileEntityRenderer call() {
				return new ISTER(tileentity.getId());
			}
		})), block);
	}

	private static <B extends Block> Item tallBlock(RegistryObject<B> block) {
		return makeBlockItem(new TallBlockItem(block.get(), TFItems.defaultBuilder()), block);
	}

	private static <B extends Block, W extends Block> Item signBlock(RegistryObject<B> block, RegistryObject<W> wallblock) {
		return makeBlockItem(new SignItem(TFItems.defaultBuilder().maxStackSize(16), block.get(), wallblock.get()), block);
	}

	private static Item makeBlockItem(Item blockitem, RegistryObject<? extends Block> block) {
		return blockitem.setRegistryName(Objects.requireNonNull(block.getId()));
	}
}
