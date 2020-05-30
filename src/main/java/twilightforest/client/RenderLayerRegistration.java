package twilightforest.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import twilightforest.block.TFBlocks;

public class RenderLayerRegistration {
	public static void init() {
		RenderType cutout = RenderType.getCutout();
		RenderType translucent = RenderType.getTranslucent();
		RenderTypeLookup.setRenderLayer(TFBlocks.oak_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.rainboak_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.canopy_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.mangrove_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.time_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.transformation_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.mining_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.sorting_leaves.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.twilight_portal.get(), translucent);
		RenderTypeLookup.setRenderLayer(TFBlocks.experiment_115.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.wispy_cloud.get(), translucent);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_rune_brick_yellow.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_rune_brick_purple.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_rune_brick_pink.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_rune_brick_blue.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_door_yellow.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_door_purple.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_door_pink.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.castle_door_blue.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.twilight_portal_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.hedge_maze_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.hollow_hill_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.quest_grove_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.mushroom_tower_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.naga_courtyard_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.lich_tower_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.minotaur_labyrinth_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.hydra_lair_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.goblin_stronghold_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.dark_tower_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.yeti_cave_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.aurora_palace_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.troll_cave_cottage_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.final_castle_miniature_structure.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.firefly_jar.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.oak_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.rainboak_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.canopy_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.mangrove_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.darkwood_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.time_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.transformation_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.mining_sapling.get(), cutout);
		RenderTypeLookup.setRenderLayer(TFBlocks.sorting_sapling.get(), cutout);
	}
}
