package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import twilightforest.init.TFFeatureModifiers;
import twilightforest.util.FeatureLogic;
import twilightforest.util.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.util.VoxelBresenhamIterator;

import java.util.Optional;

public class TreeRootsDecorator extends TreeDecorator {
	private static final SimpleStateProvider EMPTY = BlockStateProvider.simple(Blocks.AIR.defaultBlockState());

	public static final MapCodec<TreeRootsDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.intRange(0, 16).fieldOf("base_strand_count").forGetter(o -> o.strands),
		Codec.intRange(0, 16).fieldOf("additional_random_strands").forGetter(o -> o.addExtraStrands),
		Codec.intRange(0, 32).fieldOf("root_length").forGetter(o -> o.length),
		Codec.INT.fieldOf("y_offset").forGetter(o -> o.yOffset),
		BlockStateProvider.CODEC.optionalFieldOf("exposed_roots_provider").forGetter(o -> Optional.ofNullable(o.surfaceBlock != EMPTY ? o.surfaceBlock : null)),
		BlockStateProvider.CODEC.fieldOf("ground_roots_provider").forGetter(o -> o.rootBlock),
		Codec.INT.fieldOf("rootPenetrability").forGetter(o -> o.rootPenetrability)
	).apply(instance, TreeRootsDecorator::new));

	private final int strands;
	private final int addExtraStrands;
	private final int length;
	private final int yOffset;
	private final BlockStateProvider surfaceBlock;
	private final BlockStateProvider rootBlock;

	private final boolean hasSurfaceRoots;
	private final int rootPenetrability;

	private TreeRootsDecorator(int count, int addExtraStrands, int length, int yOffset, Optional<BlockStateProvider> surfaceBlock, BlockStateProvider rootBlock, int rootPenetrability) {
		this.strands = count;
		this.addExtraStrands = addExtraStrands;
		this.length = length;
		this.yOffset = yOffset;
		this.rootBlock = rootBlock;
		this.rootPenetrability = rootPenetrability;
		this.hasSurfaceRoots = surfaceBlock.isPresent();

		if (this.hasSurfaceRoots) {
			this.surfaceBlock = surfaceBlock.get();
		} else {
			this.surfaceBlock = EMPTY;
		}
	}

	public TreeRootsDecorator(int count, int addExtraStrands, int length, BlockStateProvider rootBlock, int rootPenetrability) {
		this.strands = count;
		this.addExtraStrands = addExtraStrands;
		this.length = length;
		this.yOffset = 0;
		this.rootBlock = rootBlock;
		this.rootPenetrability = rootPenetrability;
		this.hasSurfaceRoots = false;
		this.surfaceBlock = EMPTY;
	}

	public TreeRootsDecorator(int count, int addExtraStrands, int length, int yOffset, BlockStateProvider surfaceBlock, BlockStateProvider rootBlock, int rootPenetrability) {
		this.strands = count;
		this.addExtraStrands = addExtraStrands;
		this.length = length;
		this.yOffset = yOffset;
		this.rootBlock = rootBlock;
		this.rootPenetrability = rootPenetrability;
		this.hasSurfaceRoots = true;
		this.surfaceBlock = surfaceBlock;
	}

	@Override
	protected TreeDecoratorType<TreeRootsDecorator> type() {
		return TFFeatureModifiers.TREE_ROOTS.get();
	}

	@Override
	public void place(Context context) {
		if (context.logs().isEmpty())
			return;

		int numBranches = this.strands + context.random().nextInt(this.addExtraStrands + 1);
		float offset = context.random().nextFloat();
		BlockPos startPos = context.logs().get(0).above(this.yOffset);

		if (this.hasSurfaceRoots) {
			for (int i = 0; i < numBranches; i++) {
				BlockPos dest = FeatureLogic.translate(startPos.below(i + 2), this.length, 0.3 * i + (double) offset, 0.8);
				FeaturePlacers.traceExposedRoot(context.level(), new RootPlacer(context.decorationSetter, this.rootPenetrability), context.random(), this.surfaceBlock, this.rootBlock, new VoxelBresenhamIterator(startPos.below(), dest));
			}
		} else {
			for (int i = 0; i < numBranches; i++) {
				BlockPos dest = FeatureLogic.translate(startPos.below(i + 2), this.length, 0.3 * i + (double) offset, 0.8);
				FeaturePlacers.traceRoot(context.level(), new RootPlacer(context.decorationSetter, this.rootPenetrability), context.random(), this.rootBlock, new VoxelBresenhamIterator(startPos.below(), dest));
			}
		}
	}
}
