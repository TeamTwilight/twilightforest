package twilightforest.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HugeMushroomBlock;
import twilightforest.block.TFBlocks;

public final class BlockConstants {
    //Blockstates
    public static final BlockState WATER = net.minecraft.block.Blocks.WATER.getDefaultState();
    public static final BlockState LAVA = net.minecraft.block.Blocks.LAVA.getDefaultState();
    public static final BlockState SPRUCE_LOG = net.minecraft.block.Blocks.SPRUCE_LOG.getDefaultState();
    public static final BlockState SPRUCE_WOOD = net.minecraft.block.Blocks.SPRUCE_WOOD.getDefaultState();
    public static final BlockState SPRUCE_LEAVES = net.minecraft.block.Blocks.SPRUCE_LEAVES.getDefaultState();
    public static final BlockState OAK_LOG = TFBlocks.oak_log.get().getDefaultState();
    public static final BlockState OAK_WOOD = TFBlocks.oak_wood.get().getDefaultState();
    public static final BlockState OAK_LEAVES = TFBlocks.oak_leaves.get().getDefaultState();
    public static final BlockState CANOPY_LOG = TFBlocks.canopy_log.get().getDefaultState();
    public static final BlockState CANOPY_WOOD = TFBlocks.canopy_wood.get().getDefaultState();
    public static final BlockState CANOPY_LEAVES = TFBlocks.canopy_leaves.get().getDefaultState();
    public static final BlockState MANGROVE_LOG = TFBlocks.mangrove_log.get().getDefaultState();
    public static final BlockState MANGROVE_WOOD = TFBlocks.mangrove_wood.get().getDefaultState();
    public static final BlockState MANGROVE_LEAVES = TFBlocks.mangrove_leaves.get().getDefaultState();
    public static final BlockState DARKWOOD_LOG = TFBlocks.dark_log.get().getDefaultState();
    public static final BlockState DARKWOOD_WOOD = TFBlocks.dark_wood.get().getDefaultState();
    public static final BlockState DARKWOOD_LEAVES = TFBlocks.dark_leaves.get().getDefaultState();
    public static final BlockState TIME_LOG = TFBlocks.time_log.get().getDefaultState();
    public static final BlockState TIME_WOOD = TFBlocks.time_wood.get().getDefaultState();
    public static final BlockState TIME_LEAVES = TFBlocks.time_leaves.get().getDefaultState();
    public static final BlockState TRANSFORM_LOG = TFBlocks.transformation_log.get().getDefaultState();
    public static final BlockState TRANSFORM_WOOD = TFBlocks.transformation_wood.get().getDefaultState();
    public static final BlockState TRANSFORM_LEAVES = TFBlocks.transformation_leaves.get().getDefaultState();
    public static final BlockState MINING_LOG = TFBlocks.mining_log.get().getDefaultState();
    public static final BlockState MINING_WOOD = TFBlocks.mining_wood.get().getDefaultState();
    public static final BlockState MINING_LEAVES = TFBlocks.mining_leaves.get().getDefaultState();
    public static final BlockState SORT_LOG = TFBlocks.sorting_log.get().getDefaultState();
    public static final BlockState SORT_WOOD = TFBlocks.sorting_wood.get().getDefaultState();
    public static final BlockState SORT_LEAVES = TFBlocks.sorting_leaves.get().getDefaultState();
    public static final BlockState RAINBOW_LEAVES = TFBlocks.rainboak_leaves.get().getDefaultState();
    public static final BlockState ROOTS = TFBlocks.root.get().getDefaultState();
    public static final BlockState GRASS = net.minecraft.block.Blocks.GRASS.getDefaultState();
    public static final BlockState FERN = net.minecraft.block.Blocks.FERN.getDefaultState();
    public static final BlockState MAYAPPLE = TFBlocks.mayapple.get().getDefaultState();
    public static final BlockState FIDDLEHEAD = TFBlocks.fiddlehead.get().getDefaultState();
    public static final BlockState MUSHGLOOM = TFBlocks.mushgloom.get().getDefaultState();
    public static final BlockState DEAD_BUSH = net.minecraft.block.Blocks.DEAD_BUSH.getDefaultState();
    public static final BlockState FOREST_GRASS = net.minecraft.block.Blocks.GRASS.getDefaultState();
    public static final BlockState FIRE_JET = TFBlocks.fire_jet.get().getDefaultState();
    public static final BlockState SMOKER = TFBlocks.smoker.get().getDefaultState();
    public static final BlockState AIR = Blocks.AIR.getDefaultState();
    public static final BlockState MUSHROOM_STEM = Blocks.MUSHROOM_STEM.getDefaultState();//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
    public static final BlockState MUSHROOM_CAP_RED = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(HugeMushroomBlock.DOWN, false);//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
    public static final BlockState MUSHROOM_CAP_BROWN = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().with(HugeMushroomBlock.DOWN, false);//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
}
