package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.woods.WoodPalette;

public class WoodPaletteGenerator {

    public static void bootstrap(BootstrapContext<WoodPalette> context) {
        context.register(WoodPalettes.OAK, new WoodPalette(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE, TFBlocks.OAK_BANISTER.get()));
        context.register(WoodPalettes.SPRUCE, new WoodPalette(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE, TFBlocks.SPRUCE_BANISTER.get()));
        context.register(WoodPalettes.BIRCH, new WoodPalette(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE, TFBlocks.BIRCH_BANISTER.get()));
        context.register(WoodPalettes.JUNGLE, new WoodPalette(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE, TFBlocks.JUNGLE_BANISTER.get()));
        context.register(WoodPalettes.ACACIA, new WoodPalette(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE, TFBlocks.ACACIA_BANISTER.get()));
        context.register(WoodPalettes.DARK_OAK, new WoodPalette(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE, TFBlocks.DARK_OAK_BANISTER.get()));
        context.register(WoodPalettes.CRIMSON, new WoodPalette(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_PRESSURE_PLATE, TFBlocks.CRIMSON_BANISTER.get()));
        context.register(WoodPalettes.WARPED, new WoodPalette(Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, Blocks.WARPED_SLAB, Blocks.WARPED_BUTTON, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_PRESSURE_PLATE, TFBlocks.WARPED_BANISTER.get()));
        context.register(WoodPalettes.VANGROVE, new WoodPalette(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_BUTTON, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE, Blocks.MANGROVE_PRESSURE_PLATE, TFBlocks.VANGROVE_BANISTER.get()));

        context.register(WoodPalettes.TWILIGHT_OAK, new WoodPalette(TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_BANISTER));
        context.register(WoodPalettes.CANOPY, new WoodPalette(TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_BANISTER));
        context.register(WoodPalettes.MANGROVE, new WoodPalette(TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_BANISTER));
        context.register(WoodPalettes.DARKWOOD, new WoodPalette(TFBlocks.DARK_PLANKS, TFBlocks.DARK_STAIRS, TFBlocks.DARK_SLAB, TFBlocks.DARK_BUTTON, TFBlocks.DARK_FENCE, TFBlocks.DARK_GATE, TFBlocks.DARK_PLATE, TFBlocks.DARK_BANISTER));
        context.register(WoodPalettes.TIMEWOOD, new WoodPalette(TFBlocks.TIME_PLANKS, TFBlocks.TIME_STAIRS, TFBlocks.TIME_SLAB, TFBlocks.TIME_BUTTON, TFBlocks.TIME_FENCE, TFBlocks.TIME_GATE, TFBlocks.TIME_PLATE, TFBlocks.TIME_BANISTER));
        context.register(WoodPalettes.TRANSWOOD, new WoodPalette(TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_BANISTER));
        context.register(WoodPalettes.MINEWOOD, new WoodPalette(TFBlocks.MINING_PLANKS, TFBlocks.MINING_STAIRS, TFBlocks.MINING_SLAB, TFBlocks.MINING_BUTTON, TFBlocks.MINING_FENCE, TFBlocks.MINING_GATE, TFBlocks.MINING_PLATE, TFBlocks.MINING_BANISTER));
        context.register(WoodPalettes.SORTWOOD, new WoodPalette(TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_SLAB, TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_FENCE, TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLATE, TFBlocks.SORTING_BANISTER));
    }

}
