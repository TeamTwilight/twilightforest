package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import twilightforest.util.RootPlacer;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public interface TFTreeFeature extends Feature {

	@Override
	default boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BiConsumer<BlockPos, BlockState> biconsumer = (pos, state) -> {
			set.add(pos.immutable());
			level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer1 = (pos, state) -> {
			set1.add(pos.immutable());
			level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer2 = (pos, state) -> {
			set2.add(pos.immutable());
			level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer3 = (pos, state) -> {
			set3.add(pos.immutable());
			level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		boolean flag = this.generate(level, random, origin, biconsumer, biconsumer1, new RootPlacer(biconsumer2, 1));
		if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
			if (!decorators().isEmpty()) {
				TreeDecorator.Context treeDecoratorContext = new TreeDecorator.Context(level, biconsumer3, random, set1, set2, set);
				decorators().forEach(decorator -> decorator.place(treeDecoratorContext));
			}
			return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map((boundingBox) -> {
				DiscreteVoxelShape discretevoxelshape = TreeFeature.updateLeaves(level, boundingBox, set1, set3, set);
				StructureTemplate.updateShapeAtEdge(level, 3, discretevoxelshape, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
				return true;
			}).orElse(false);
		} else {
			return false;
		}
	}

	Holder<BlockStateProvider> trunkProvider();

	Holder<BlockStateProvider> leavesProvider();

	Holder<BlockStateProvider> branchProvider();

	Holder<BlockStateProvider> rootsProvider();

	int minHeight();

	int chanceAddFiveFirst();

	int chanceAddFiveSecond();

	boolean hasLeaves();

	boolean checkWater();

	List<TreeDecorator> decorators();

	boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer);
}
