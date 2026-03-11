package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

//[VanillaCopy] of TreeFeature but allows trees on snow
public class SnowTreeFeature extends Feature<TreeConfiguration> {
	public SnowTreeFeature(Codec<TreeConfiguration> codec) {
		super(codec);
	}

	private static boolean isVine(LevelSimulatedReader reader, BlockPos pos) {
		return reader.isStateAtPosition(pos, (state) -> state.is(Blocks.VINE));
	}

	private static void setBlockKnownShape(LevelWriter p_67257_, BlockPos p_67258_, BlockState p_67259_) {
		p_67257_.setBlock(p_67258_, p_67259_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
	}

	public static boolean validTreePos(LevelSimulatedReader reader, BlockPos pos) {
		return reader.isStateAtPosition(pos, (state) -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES));
	}

	private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> consumer, BiConsumer<BlockPos, BlockState> consumer1, FoliagePlacer.FoliageSetter setter, TreeConfiguration config) {
		int i = config.trunkPlacer.getTreeHeight(random);
		int j = config.foliagePlacer.foliageHeight(random, i, config);
		int k = i - j;
		int l = config.foliagePlacer.foliageRadius(random, k);
		BlockPos blockpos = config.rootPlacer.map((p_225286_) -> p_225286_.getTrunkOrigin(pos, random)).orElse(pos);
		int i1 = Math.min(pos.getY(), blockpos.getY());
		int j1 = Math.max(pos.getY(), blockpos.getY()) + i + 1;
		if (i1 >= level.getMinBuildHeight() + 1 && j1 <= level.getMaxBuildHeight()) {
			OptionalInt optionalint = config.minimumSize.minClippedHeight();
			int k1 = this.getMaxFreeTreeHeight(level, i, blockpos, config);
			if (k1 >= i || optionalint.isPresent() && k1 >= optionalint.getAsInt()) {
				if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(level, consumer, random, pos, blockpos, config)) {
					return false;
				} else {
					List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer.placeTrunk(level, consumer1, random, k1, blockpos, config);
					list.forEach((p_225279_) -> {
						config.foliagePlacer.createFoliage(level, setter, random, config, k1, p_225279_, j, l);
					});
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private int getMaxFreeTreeHeight(LevelSimulatedReader reader, int height, BlockPos pos, TreeConfiguration config) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int i = 0; i <= height + 1; ++i) {
			int j = config.minimumSize.getSizeAtHeight(height, i);

			for (int k = -j; k <= j; ++k) {
				for (int l = -j; l <= j; ++l) {
					blockpos$mutableblockpos.setWithOffset(pos, k, i, l);
					if (!config.trunkPlacer.isFree(reader, blockpos$mutableblockpos) || !config.ignoreVines && isVine(reader, blockpos$mutableblockpos)) {
						return i - 2;
					}
				}
			}
		}

		return height;
	}

	@Override
	protected void setBlock(LevelWriter writer, BlockPos pos, BlockState state) {
		setBlockKnownShape(writer, pos, state);
	}

	@Override
	public final boolean place(FeaturePlaceContext<TreeConfiguration> context) {
		WorldGenLevel worldgenlevel = context.level();
		RandomSource randomsource = context.random();
		BlockPos blockpos = context.origin();
		TreeConfiguration treeconfiguration = context.config();
		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
			set.add(p_160555_.immutable());
			worldgenlevel.setBlock(p_160555_, p_160556_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
			set1.add(p_160548_.immutable());
			worldgenlevel.setBlock(p_160548_, p_160549_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		FoliagePlacer.FoliageSetter setter = new FoliagePlacer.FoliageSetter() {
			@Override
			public void set(BlockPos pos, BlockState state) {
				set2.add(pos.immutable());
				worldgenlevel.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			}

			@Override
			public boolean isSet(BlockPos p_272999_) {
				return set2.contains(p_272999_);
			}
		};
		BiConsumer<BlockPos, BlockState> biconsumer3 = (p_225290_, p_225291_) -> {
			set3.add(p_225290_.immutable());
			worldgenlevel.setBlock(p_225290_, p_225291_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		boolean flag = this.doPlace(worldgenlevel, randomsource, blockpos, biconsumer, biconsumer1, setter, treeconfiguration);
		if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
			if (!treeconfiguration.decorators.isEmpty()) {
				TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biconsumer3, randomsource, set1, set2, set);
				treeconfiguration.decorators.forEach((p_225282_) -> {
					p_225282_.place(treedecorator$context);
				});
			}

			return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map((p_225270_) -> {
				DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, p_225270_, set1, set3, set);
				StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, p_225270_.minX(), p_225270_.minY(), p_225270_.minZ());
				return true;
			}).orElse(false);
		} else {
			return false;
		}
	}

	public static DiscreteVoxelShape updateLeaves(LevelAccessor accessor, BoundingBox box, Set<BlockPos> posSet, Set<BlockPos> posSet1, Set<BlockPos> posSet2) {
		List<Set<BlockPos>> list = Lists.newArrayList();
		DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(box.getXSpan(), box.getYSpan(), box.getZSpan());
		int i = 6;

		for (int j = 0; j < 6; ++j) {
			list.add(Sets.newHashSet());
		}

		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (BlockPos blockpos : Lists.newArrayList(Sets.union(posSet1, posSet2))) {
			if (box.isInside(blockpos)) {
				discretevoxelshape.fill(blockpos.getX() - box.minX(), blockpos.getY() - box.minY(), blockpos.getZ() - box.minZ());
			}
		}

		for (BlockPos blockpos1 : Lists.newArrayList(posSet)) {
			if (box.isInside(blockpos1)) {
				discretevoxelshape.fill(blockpos1.getX() - box.minX(), blockpos1.getY() - box.minY(), blockpos1.getZ() - box.minZ());
			}

			for (Direction direction : Direction.values()) {
				blockpos$mutableblockpos.setWithOffset(blockpos1, direction);
				if (!posSet.contains(blockpos$mutableblockpos)) {
					BlockState blockstate = accessor.getBlockState(blockpos$mutableblockpos);
					if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
						list.get(0).add(blockpos$mutableblockpos.immutable());
						setBlockKnownShape(accessor, blockpos$mutableblockpos, blockstate.setValue(BlockStateProperties.DISTANCE, 1));
						if (box.isInside(blockpos$mutableblockpos)) {
							discretevoxelshape.fill(blockpos$mutableblockpos.getX() - box.minX(), blockpos$mutableblockpos.getY() - box.minY(), blockpos$mutableblockpos.getZ() - box.minZ());
						}
					}
				}
			}
		}

		for (int l = 1; l < 6; ++l) {
			Set<BlockPos> set = list.get(l - 1);
			Set<BlockPos> set1 = list.get(l);

			for (BlockPos blockpos2 : set) {
				if (box.isInside(blockpos2)) {
					discretevoxelshape.fill(blockpos2.getX() - box.minX(), blockpos2.getY() - box.minY(), blockpos2.getZ() - box.minZ());
				}

				for (Direction direction1 : Direction.values()) {
					blockpos$mutableblockpos.setWithOffset(blockpos2, direction1);
					if (!set.contains(blockpos$mutableblockpos) && !set1.contains(blockpos$mutableblockpos)) {
						BlockState blockstate1 = accessor.getBlockState(blockpos$mutableblockpos);
						if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
							int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
							if (k > l + 1) {
								BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, l + 1);
								setBlockKnownShape(accessor, blockpos$mutableblockpos, blockstate2);
								if (box.isInside(blockpos$mutableblockpos)) {
									discretevoxelshape.fill(blockpos$mutableblockpos.getX() - box.minX(), blockpos$mutableblockpos.getY() - box.minY(), blockpos$mutableblockpos.getZ() - box.minZ());
								}

								set1.add(blockpos$mutableblockpos.immutable());
							}
						}
					}
				}
			}
		}

		return discretevoxelshape;
	}
}