package twilightforest.worldgen;

import net.minecraft.util.Direction;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import twilightforest.block.BlockTFFirefly;
import twilightforest.block.TFBlocks;
import twilightforest.worldgen.treeplacers.TreeRootsDecorator;
import twilightforest.worldgen.treeplacers.TrunkSideDecorator;

public final class TreeDecorators {
    public static final TreeRootsDecorator LIVING_ROOTS = new TreeRootsDecorator(3, 1, 5, (new WeightedBlockStateProvider())
            .addWeightedBlockstate(BlockConstants.ROOTS, 6)
            .addWeightedBlockstate(TFBlocks.liveroot_block.get().getDefaultState(), 1));

    public static final TrunkSideDecorator FIREFLY = new TrunkSideDecorator(1, 1.0f, new SimpleBlockStateProvider(TFBlocks.firefly.get().getDefaultState().with(BlockTFFirefly.FACING, Direction.NORTH)));
}
