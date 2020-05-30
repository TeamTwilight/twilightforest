package twilightforest.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.ItemTFExperiment115;

import static twilightforest.TwilightForestMod.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		toBlock(TFBlocks.tower_wood.get());
		toBlock(TFBlocks.tower_wood_encased.get());
		toBlock(TFBlocks.tower_wood_cracked.get());
		toBlock(TFBlocks.tower_wood_mossy.get());
		toBlock(TFBlocks.tower_wood_infested.get());
		toBlockModel(TFBlocks.fake_gold.get(), new ResourceLocation("block/gold_block"));
		toBlockModel(TFBlocks.fake_diamond.get(), new ResourceLocation("block/diamond_block"));
		toBlock(TFBlocks.underbrick.get());
		toBlock(TFBlocks.underbrick_cracked.get());
		toBlock(TFBlocks.underbrick_mossy.get());
		toBlock(TFBlocks.underbrick_floor.get());
		toBlock(TFBlocks.wispy_cloud.get());
		toBlock(TFBlocks.fluffy_cloud.get());
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
		toBlock(TFBlocks.firefly_jar.get());
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

		toBlock(TFBlocks.time_log.get());
		toBlock(TFBlocks.time_wood.get());
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
