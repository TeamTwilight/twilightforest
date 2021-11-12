package twilightforest.world.registration;

import net.minecraft.core.Direction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import twilightforest.block.FireflyBlock;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.feature.trees.treeplacers.TreeRootsDecorator;
import twilightforest.world.components.feature.trees.treeplacers.TrunkSideDecorator;

public final class TreeDecorators {
    public static final TreeRootsDecorator LIVING_ROOTS = new TreeRootsDecorator(3, 1, 5, (new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(BlockConstants.ROOTS, 6).add(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), 1).build())));
    public static final TrunkSideDecorator FIREFLY = new TrunkSideDecorator(1, 1.0f, new SimpleStateProvider(TFBlocks.FIREFLY.get().defaultBlockState().setValue(FireflyBlock.FACING, Direction.NORTH)));
}
