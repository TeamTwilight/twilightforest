package twilightforest.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.ItemTFExperiment115;
import twilightforest.item.ItemTFTrophy;

import static twilightforest.TwilightForestMod.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (Item i : Registry.ITEM) {
			if (i instanceof SpawnEggItem && i.getRegistryName().getNamespace().equals(TwilightForestMod.ID)) {
				getBuilder(i.getRegistryName().getPath())
								.parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
			}
			if (i instanceof ItemTFTrophy && i.getRegistryName().getNamespace().equals(TwilightForestMod.ID)) {
				getBuilder(i.getRegistryName().getPath())
				.parent(getExistingFile(new ResourceLocation("item/template_skull")));
}
		}
		toBlock(TFBlocks.tower_wood.get());
		toBlock(TFBlocks.tower_wood_encased.get());
		toBlock(TFBlocks.tower_wood_cracked.get());
		toBlock(TFBlocks.tower_wood_mossy.get());
		toBlock(TFBlocks.tower_wood_infested.get());
		toBlock(TFBlocks.carminite_builder.get());
		toBlock(TFBlocks.antibuilder.get());
		toBlock(TFBlocks.ghast_trap.get());
		toBlock(TFBlocks.vanishing_block.get());
		toBlock(TFBlocks.locked_vanishing_block.get());
		toBlock(TFBlocks.reappearing_block.get());
		toBlock(TFBlocks.carminite_reactor.get());
		toBlockModel(TFBlocks.fake_gold.get(), new ResourceLocation("block/gold_block"));
		toBlockModel(TFBlocks.fake_diamond.get(), new ResourceLocation("block/diamond_block"));
		toBlock(TFBlocks.stronghold_shield.get());
		toBlock(TFBlocks.trophy_pedestal.get());
		//toBlock(TFBlocks.terrorcotta_circle.get());
		//toBlock(TFBlocks.terrorcotta_diagonal.get());
		toBlockModel(TFBlocks.aurora_block.get(), prefix("block/aurora_block_0"));
		toBlock(TFBlocks.aurora_pillar.get());
		toBlock(TFBlocks.aurora_slab.get());
		toBlock(TFBlocks.auroralized_glass.get());
		toBlock(TFBlocks.underbrick.get());
		toBlock(TFBlocks.underbrick_cracked.get());
		toBlock(TFBlocks.underbrick_mossy.get());
		toBlock(TFBlocks.underbrick_floor.get());
		toBlock(TFBlocks.green_thorns.get());
		toBlock(TFBlocks.brown_thorns.get());
		toBlock(TFBlocks.burnt_thorns.get());
		generated(TFBlocks.thorn_rose.getId().getPath(), prefix("block/" + TFBlocks.thorn_rose.getId().getPath()));
		toBlockModel(TFBlocks.thorn_leaves.get(), new ResourceLocation("block/oak_leaves"));
		toBlockModel(TFBlocks.beanstalk_leaves.get(), new ResourceLocation("block/spruce_leaves"));
		toBlock(TFBlocks.deadrock.get());
		toBlock(TFBlocks.deadrock_cracked.get());
		toBlock(TFBlocks.deadrock_weathered.get());
		getBuilder(TFBlocks.trollsteinn.getId().getPath()).parent(getExistingFile(new ResourceLocation("block/cube_all")))
						.texture("all", prefix("block/trollsteinn"));
		toBlock(TFBlocks.wispy_cloud.get());
		toBlock(TFBlocks.fluffy_cloud.get());
		toBlockModel(TFBlocks.giant_cobblestone.get(), new ResourceLocation("block/cobblestone"));
		toBlockModel(TFBlocks.giant_log.get(), new ResourceLocation("block/oak_log"));
		toBlockModel(TFBlocks.giant_leaves.get(), new ResourceLocation("block/oak_leaves"));
		toBlockModel(TFBlocks.giant_obsidian.get(), new ResourceLocation("block/obsidian"));
		toBlock(TFBlocks.uberous_soil.get());
		toBlock(TFBlocks.huge_stalk.get());
		getBuilder(TFBlocks.huge_mushgloom.getId().getPath()).parent(getExistingFile(new ResourceLocation("block/cube_all")))
						.texture("all", prefix("block/huge_gloom_cap"));
		toBlock(TFBlocks.huge_mushgloom_stem.get());
		generated(TFBlocks.trollvidr.getId().getPath(), prefix("block/" + TFBlocks.trollvidr.getId().getPath()));
		generated(TFBlocks.unripe_trollber.getId().getPath(), prefix("block/" + TFBlocks.unripe_trollber.getId().getPath()));
		generated(TFBlocks.trollber.getId().getPath(), prefix("block/" + TFBlocks.trollber.getId().getPath()));
		generated(TFBlocks.huge_lilypad.getId().getPath(), prefix("block/" + TFBlocks.huge_lilypad.getId().getPath()));
		generated(TFBlocks.huge_waterlily.getId().getPath(), prefix("block/" + TFBlocks.huge_waterlily.getId().getPath()));
		toBlock(TFBlocks.castle_brick.get());
		toBlock(TFBlocks.castle_brick_worn.get());
		toBlock(TFBlocks.castle_brick_cracked.get());
		toBlock(TFBlocks.castle_brick_roof.get());
		toBlock(TFBlocks.castle_brick_mossy.get());
		toBlock(TFBlocks.castle_brick_frame.get());
		toBlock(TFBlocks.castle_pillar_encased.get());
		toBlock(TFBlocks.castle_pillar_encased_tile.get());
		toBlock(TFBlocks.castle_pillar_bold.get());
		toBlock(TFBlocks.castle_pillar_bold_tile.get());
		toBlock(TFBlocks.castle_stairs_brick.get());
		toBlock(TFBlocks.castle_stairs_worn.get());
		toBlock(TFBlocks.castle_stairs_cracked.get());
		toBlock(TFBlocks.castle_stairs_mossy.get());
		toBlock(TFBlocks.castle_stairs_encased.get());
		toBlock(TFBlocks.castle_stairs_bold.get());
		toBlockModel(TFBlocks.castle_rune_brick_yellow.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.castle_rune_brick_purple.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.castle_rune_brick_pink.get(), "castle_rune_brick_0");
		toBlockModel(TFBlocks.castle_rune_brick_blue.get(), "castle_rune_brick_0");
		generated(TFBlocks.force_field_pink.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.force_field_blue.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.force_field_green.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.force_field_purple.getId().getPath(), prefix("block/forcefield_white"));
		generated(TFBlocks.force_field_orange.getId().getPath(), prefix("block/forcefield_white"));
		toBlock(TFBlocks.cinder_log.get());
		toBlock(TFBlocks.cinder_wood.get());
		toBlockModel(TFBlocks.cinder_furnace.get(), new ResourceLocation("block/furnace"));
		toBlock(TFBlocks.castle_door_yellow.get());
		toBlock(TFBlocks.castle_door_purple.get());
		toBlock(TFBlocks.castle_door_pink.get());
		toBlock(TFBlocks.castle_door_blue.get());
		ModelFile think115 = generated("item/think115", prefix("items/think115"));
		ModelFile fullBlockSprinkle = getExistingFile(prefix("block/experiment115_8_8_regenerating"));
		generated(TFBlocks.experiment_115.getId().getPath(), prefix("items/experiment_115"))
						.override().predicate(ItemTFExperiment115.THINK, 1).model(think115).end()
						.override().predicate(ItemTFExperiment115.FULL, 1).model(fullBlockSprinkle).end();
		toBlockModel(TFBlocks.twilight_portal_miniature_structure.get(), "miniature/portal");
		toBlockModel(TFBlocks.naga_courtyard_miniature_structure.get(), "miniature/naga_courtyard");
		toBlockModel(TFBlocks.lich_tower_miniature_structure.get(), "miniature/lich_tower");
		toBlock(TFBlocks.knightmetal_block.get());
		toBlock(TFBlocks.ironwood_block.get());
		toBlock(TFBlocks.fiery_block.get());
		toBlock(TFBlocks.arctic_fur_block.get());
		toBlock(TFBlocks.steeleaf_block.get());
		toBlock(TFBlocks.carminite_block.get());
		toBlock(TFBlocks.maze_stone.get());
		toBlock(TFBlocks.maze_stone_brick.get());
		toBlock(TFBlocks.maze_stone_chiseled.get());
		toBlock(TFBlocks.maze_stone_decorative.get());
		toBlock(TFBlocks.maze_stone_cracked.get());
		toBlock(TFBlocks.maze_stone_mossy.get());
		toBlock(TFBlocks.maze_stone_mosaic.get());
		toBlock(TFBlocks.maze_stone_border.get());
		toBlock(TFBlocks.hedge.get());
		toBlock(TFBlocks.root.get());
		toBlock(TFBlocks.liveroot_block.get());
		toBlock(TFBlocks.uncrafting_table.get());
		toBlockModel(TFBlocks.boss_spawner.get(), new ResourceLocation("block/spawner"));
		toBlock(TFBlocks.firefly_jar.get());
		generated(TFBlocks.moss_patch.getId().getPath(), prefix("block/patch/moss"));
		generated(TFBlocks.mayapple.getId().getPath(), prefix("block/mayapple"));
		generated(TFBlocks.clover_patch.getId().getPath(), prefix("block/patch/clover"));
		generated(TFBlocks.fiddlehead.getId().getPath(), prefix("block/fiddlehead"));
		generated(TFBlocks.mushgloom.getId().getPath(), prefix("block/mushgloom"), prefix("block/mushgloom_head"));
		generated(TFBlocks.torchberry_plant.getId().getPath(), prefix("block/torchberry_plant"), prefix("block/torchberry_plant_glow"));
		generated(TFBlocks.root_strand.getId().getPath(), prefix("block/root_strand"));
		generated(TFBlocks.fallen_leaves.getId().getPath(), new ResourceLocation("block/spruce_leaves"));
		toBlockModel(TFBlocks.smoker.get(), prefix("block/jet"));
		toBlockModel(TFBlocks.fire_jet.get(), prefix("block/jet"));
		toBlock(TFBlocks.encased_smoker.get());
		toBlock(TFBlocks.encased_fire_jet.get());
		toBlock(TFBlocks.naga_stone.get());
		toBlock(TFBlocks.naga_stone_head.get());
		toBlock(TFBlocks.nagastone_pillar.get());
		toBlock(TFBlocks.nagastone_pillar_mossy.get());
		toBlock(TFBlocks.nagastone_pillar_weathered.get());
		toBlock(TFBlocks.etched_nagastone.get());
		toBlock(TFBlocks.etched_nagastone_mossy.get());
		toBlock(TFBlocks.etched_nagastone_weathered.get());
		toBlock(TFBlocks.nagastone_stairs_left.get());
		toBlock(TFBlocks.nagastone_stairs_right.get());
		toBlock(TFBlocks.nagastone_stairs_mossy_left.get());
		toBlock(TFBlocks.nagastone_stairs_mossy_right.get());
		toBlock(TFBlocks.nagastone_stairs_weathered_left.get());
		toBlock(TFBlocks.nagastone_stairs_weathered_right.get());
		toBlockModel(TFBlocks.spiral_bricks.get(), new ResourceLocation("twilightforest:block/spiral_bricks/x_spiral_bottom_right"));
		toBlock(TFBlocks.stone_twist.get());
		//toBlock(TFBlocks.lapis_block.get());
		toBlock(TFBlocks.oak_log.get());
		toBlock(TFBlocks.oak_wood.get());
		toBlock(TFBlocks.oak_leaves.get());
		toBlock(TFBlocks.rainboak_leaves.get());
		generated(TFBlocks.rainboak_sapling.getId().getPath(), prefix("block/" + TFBlocks.rainboak_sapling.getId().getPath()));
		generated(TFBlocks.oak_sapling.getId().getPath(), prefix("block/" + TFBlocks.oak_sapling.getId().getPath()));
		toBlock(TFBlocks.twilight_oak_planks.get());
		toBlock(TFBlocks.twilight_oak_stairs.get());
		toBlock(TFBlocks.twilight_oak_slab.get());
		woodenButton(TFBlocks.twilight_oak_button.get(), "twilight_oak");
		woodenFence(TFBlocks.twilight_oak_fence.get(), "twilight_oak");
		toBlock(TFBlocks.twilight_oak_gate.get());
		toBlock(TFBlocks.twilight_oak_plate.get());
		toBlockModel(TFBlocks.twilight_oak_trapdoor.get(), "twilight_oak_trapdoor_bottom");
		toBlock(TFBlocks.canopy_log.get());
		toBlock(TFBlocks.canopy_wood.get());
		toBlock(TFBlocks.canopy_leaves.get());
		generated(TFBlocks.canopy_sapling.getId().getPath(), prefix("block/" + TFBlocks.canopy_sapling.getId().getPath()));
		toBlock(TFBlocks.canopy_planks.get());
		toBlock(TFBlocks.canopy_stairs.get());
		toBlock(TFBlocks.canopy_slab.get());
		woodenButton(TFBlocks.canopy_button.get(), "canopy");
		woodenFence(TFBlocks.canopy_fence.get(), "canopy");
		toBlock(TFBlocks.canopy_gate.get());
		toBlock(TFBlocks.canopy_plate.get());
		toBlockModel(TFBlocks.canopy_trapdoor.get(), "canopy_trapdoor_bottom");
		toBlock(TFBlocks.mangrove_log.get());
		toBlock(TFBlocks.mangrove_wood.get());
		toBlock(TFBlocks.mangrove_leaves.get());
		generated(TFBlocks.mangrove_sapling.getId().getPath(), prefix("block/" + TFBlocks.mangrove_sapling.getId().getPath()));
		toBlock(TFBlocks.mangrove_planks.get());
		toBlock(TFBlocks.mangrove_stairs.get());
		toBlock(TFBlocks.mangrove_slab.get());
		woodenButton(TFBlocks.mangrove_button.get(), "mangrove");
		woodenFence(TFBlocks.mangrove_fence.get(), "mangrove");
		toBlock(TFBlocks.mangrove_gate.get());
		toBlock(TFBlocks.mangrove_plate.get());
		toBlockModel(TFBlocks.mangrove_trapdoor.get(), "mangrove_trapdoor_bottom");
		toBlock(TFBlocks.dark_log.get());
		toBlock(TFBlocks.dark_wood.get());
		toBlock(TFBlocks.dark_leaves.get());
		generated(TFBlocks.darkwood_sapling.getId().getPath(), prefix("block/" + TFBlocks.darkwood_sapling.getId().getPath()));
		toBlock(TFBlocks.dark_planks.get());
		toBlock(TFBlocks.dark_stairs.get());
		toBlock(TFBlocks.dark_slab.get());
		woodenButton(TFBlocks.dark_button.get(), "darkwood");
		woodenFence(TFBlocks.dark_fence.get(), "darkwood");
		toBlock(TFBlocks.dark_gate.get());
		toBlock(TFBlocks.dark_plate.get());
		toBlockModel(TFBlocks.dark_trapdoor.get(), "dark_trapdoor_bottom");
		generated(TFBlocks.hollow_oak_sapling.getId().getPath(), prefix("block/" + TFBlocks.hollow_oak_sapling.getId().getPath()));
		
		toBlock(TFBlocks.time_log.get());
		toBlock(TFBlocks.time_wood.get());
		toBlock(TFBlocks.time_log_core.get());
		toBlock(TFBlocks.time_leaves.get());
		generated(TFBlocks.time_sapling.getId().getPath(), prefix("block/" + TFBlocks.time_sapling.getId().getPath()));
		toBlock(TFBlocks.time_planks.get());
		toBlock(TFBlocks.time_stairs.get());
		toBlock(TFBlocks.time_slab.get());
		woodenButton(TFBlocks.time_button.get(), "time");
		woodenFence(TFBlocks.time_fence.get(), "time");
		toBlock(TFBlocks.time_gate.get());
		toBlock(TFBlocks.time_plate.get());
		toBlockModel(TFBlocks.time_trapdoor.get(), "time_trapdoor_bottom");

		toBlock(TFBlocks.transformation_log.get());
		toBlock(TFBlocks.transformation_wood.get());
		toBlock(TFBlocks.transformation_log_core.get());
		toBlock(TFBlocks.transformation_leaves.get());
		generated(TFBlocks.transformation_sapling.getId().getPath(), prefix("block/" + TFBlocks.transformation_sapling.getId().getPath()));
		toBlock(TFBlocks.trans_planks.get());
		toBlock(TFBlocks.trans_stairs.get());
		toBlock(TFBlocks.trans_slab.get());
		woodenButton(TFBlocks.trans_button.get(), "trans");
		woodenFence(TFBlocks.trans_fence.get(), "trans");
		toBlock(TFBlocks.trans_gate.get());
		toBlock(TFBlocks.trans_plate.get());
		toBlockModel(TFBlocks.trans_trapdoor.get(), "trans_trapdoor_bottom");

		toBlock(TFBlocks.mining_log.get());
		toBlock(TFBlocks.mining_wood.get());
		toBlock(TFBlocks.mining_log_core.get());
		toBlock(TFBlocks.mining_leaves.get());
		generated(TFBlocks.mining_sapling.getId().getPath(), prefix("block/" + TFBlocks.mining_sapling.getId().getPath()));
		toBlock(TFBlocks.mine_planks.get());
		toBlock(TFBlocks.mine_stairs.get());
		toBlock(TFBlocks.mine_slab.get());
		woodenButton(TFBlocks.mine_button.get(), "mine");
		woodenFence(TFBlocks.mine_fence.get(), "mine");
		toBlock(TFBlocks.mine_gate.get());
		toBlock(TFBlocks.mine_plate.get());
		toBlockModel(TFBlocks.mine_trapdoor.get(), "mine_trapdoor_bottom");

		toBlock(TFBlocks.sorting_log.get());
		toBlock(TFBlocks.sorting_wood.get());
		toBlock(TFBlocks.sorting_log_core.get());
		toBlock(TFBlocks.sorting_leaves.get());
		generated(TFBlocks.sorting_sapling.getId().getPath(), prefix("block/" + TFBlocks.sorting_sapling.getId().getPath()));
		toBlock(TFBlocks.sort_planks.get());
		toBlock(TFBlocks.sort_stairs.get());
		toBlock(TFBlocks.sort_slab.get());
		woodenButton(TFBlocks.sort_button.get(), "sort");
		woodenFence(TFBlocks.sort_fence.get(), "sort");
		toBlock(TFBlocks.sort_gate.get());
		toBlock(TFBlocks.sort_plate.get());
		toBlockModel(TFBlocks.sort_trapdoor.get(), "sort_trapdoor_bottom");
	}

	private ItemModelBuilder generated(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

	private void woodenButton(Block button, String variant) {
		getBuilder(button.getRegistryName().getPath())
						.parent(getExistingFile(mcLoc("block/button_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void woodenFence(Block fence, String variant) {
		getBuilder(fence.getRegistryName().getPath())
						.parent(getExistingFile(mcLoc("block/fence_inventory")))
						.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	private void toBlock(Block b) {
		toBlockModel(b, b.getRegistryName().getPath());
	}

	private void toBlockModel(Block b, String model) {
		toBlockModel(b, prefix("block/" + model));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(b.getRegistryName().getPath(), model);
	}

	@Override
	public String getName() {
		return "TwilightForest item and itemblock models";
	}
}
