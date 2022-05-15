package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;
import twilightforest.world.registration.TwilightFeatures;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;

public class TreeRootsDecorator extends TreeDecorator {
    private static final SimpleStateProvider EMPTY = BlockStateProvider.simple(Blocks.AIR.defaultBlockState());

    public static final Codec<TreeRootsDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 16).fieldOf("base_strand_count").forGetter(o -> o.strands),
                    Codec.intRange(0, 16).fieldOf("additional_random_strands").forGetter(o -> o.addExtraStrands),
                    Codec.intRange(0, 32).fieldOf("root_length").forGetter(o -> o.length),
                    BlockStateProvider.CODEC.optionalFieldOf("exposed_roots_provider").forGetter(o -> Optional.ofNullable(o.surfaceBlock != EMPTY ? o.surfaceBlock : null)),
                    BlockStateProvider.CODEC.fieldOf("ground_roots_provider").forGetter(o -> o.rootBlock)
            ).apply(instance, TreeRootsDecorator::new)
    );

    private final int strands;
    private final int addExtraStrands;
    private final int length;
    private final BlockStateProvider surfaceBlock;
    private final BlockStateProvider rootBlock;

    private final boolean hasSurfaceRoots;

    private TreeRootsDecorator(int count, int addExtraStrands, int length, Optional<BlockStateProvider> surfaceBlock, BlockStateProvider rootBlock) {
        this.strands = count;
        this.addExtraStrands = addExtraStrands;
        this.length = length;
        this.rootBlock = rootBlock;
        this.hasSurfaceRoots = surfaceBlock.isPresent();

        if (this.hasSurfaceRoots) {
            this.surfaceBlock = surfaceBlock.get();
        } else {
            this.surfaceBlock = EMPTY;
        }
    }

    public TreeRootsDecorator(int count, int addExtraStrands, int length, BlockStateProvider rootBlock) {
        this.strands = count;
        this.addExtraStrands = addExtraStrands;
        this.length = length;
        this.rootBlock = rootBlock;
        this.hasSurfaceRoots = false;
        this.surfaceBlock = EMPTY;
    }

    public TreeRootsDecorator(int count, int addExtraStrands, int length, BlockStateProvider surfaceBlock, BlockStateProvider rootBlock) {
        this.strands = count;
        this.addExtraStrands = addExtraStrands;
        this.length = length;
        this.rootBlock = rootBlock;
        this.hasSurfaceRoots = true;
        this.surfaceBlock = surfaceBlock;
    }

    @Override
    protected TreeDecoratorType<TreeRootsDecorator> type() {
        return TwilightFeatures.TREE_ROOTS.get();
    }

    @Override
    public void place(LevelSimulatedReader worldReader, BiConsumer<BlockPos, BlockState> worldPlacer, Random random, List<BlockPos> trunkBlocks, List<BlockPos> leafBlocks) {
        if (trunkBlocks.isEmpty())
            return;

        int numBranches = this.strands + random.nextInt(this.addExtraStrands + 1);
        float offset = random.nextFloat();
        BlockPos startPos = trunkBlocks.get(0);

        if (this.hasSurfaceRoots) {
            for (int i = 0; i < numBranches; i++) {
                buildRootExposed(worldReader, worldPlacer, random, startPos, offset, i, this.length, this.surfaceBlock, this.rootBlock);
            }
        } else {
            for (int i = 0; i < numBranches; i++) {
                buildRoot(worldReader, worldPlacer, random, startPos, offset, i, this.length, this.rootBlock);
            }
        }
    }

    protected void buildRootExposed(LevelSimulatedReader worldReader, BiConsumer<BlockPos, BlockState> worldPlacer, Random random, BlockPos pos, double offset, int iteration, int length, BlockStateProvider airRoot, BlockStateProvider dirtRoot) {
        BlockPos dest = FeatureLogic.translate(pos.below(iteration + 2), length, 0.3 * iteration + offset, 0.8);

        // go through block by block and stop drawing when we head too far into open air
        boolean stillAboveGround = true;
        for (BlockPos coord : new VoxelBresenhamIterator(pos.below(), dest)) {
            if (stillAboveGround && FeatureLogic.hasEmptyNeighbor(worldReader, coord)) {
                if (worldReader.isStateAtPosition(coord, FeatureLogic::worldGenReplaceable)) {
                    worldPlacer.accept(coord, airRoot.getState(random, coord));
                } else if (!worldReader.isStateAtPosition(coord, FeatureLogic.SHOULD_SKIP)) break;
            } else {
                stillAboveGround = false;
                if (FeatureLogic.canRootGrowIn(worldReader, coord)) {
                    worldPlacer.accept(coord, dirtRoot.getState(random, coord));
                } else if (!worldReader.isStateAtPosition(coord, FeatureLogic.SHOULD_SKIP)) break;
            }
        }
    }

    // Shortcircuited version of above function
    protected void buildRoot(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> worldPlacer, Random random, BlockPos pos, double offset, int iteration, int length, BlockStateProvider dirtRoot) {
        BlockPos dest = FeatureLogic.translate(pos.below(iteration + 2), length, 0.3 * iteration + offset, 0.8);

        // go through block by block and stop drawing when we head too far into open air
        for (BlockPos coord : new VoxelBresenhamIterator(pos.below(), dest)) {
            if (FeatureLogic.canRootGrowIn(world, coord)) {
                worldPlacer.accept(coord, dirtRoot.getState(random, coord));
            } else if (!world.isStateAtPosition(coord, FeatureLogic.SHOULD_SKIP)) break;
        }
    }
}
