package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import twilightforest.block.TFBlocks;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */

//Lots of things from TreeFeature, but we're checking for dirt to place on
public class TFGenDarkCanopyTree extends Feature<TreeConfiguration> {

	int difference = 0;
	BlockPos validPos = new BlockPos(0, 0, 0);

	public TFGenDarkCanopyTree(Codec<TreeConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<TreeConfiguration> ctx) {
		WorldGenLevel reader = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();
		TreeConfiguration config = ctx.config();

		// if we are given leaves as a starting position, seek dirt or grass underneath
		boolean foundDirt = false;
		Material materialUnder;
		for (int dy = pos.getY(); dy >= reader.getMinBuildHeight(); dy--) {
			materialUnder = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ())).getMaterial();
			if (materialUnder == Material.GRASS || materialUnder == Material.DIRT) {
				// yes!
				foundDirt = true;
				pos = new BlockPos(pos.getX(), dy, pos.getZ());
				validPos = pos;
				break;
			} else if (materialUnder == Material.STONE || materialUnder == Material.SAND) {
				// nope
				break;
			}
		}

		if (!foundDirt) {
			return false;
		}

		// do not grow next to another tree
		for (Direction e : Direction.Plane.HORIZONTAL) {
			if (reader.getBlockState(pos.relative(e)).getMaterial() == Material.WOOD)
				return false;
		}

		//Taken from TreeFeature.generate, adjusting our BoundingBox to fit where the dirt is
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BoundingBox mutableboundingbox = BoundingBox.infinite();
		BiConsumer<BlockPos, BlockState> biConsumer = (p_160555_, p_160556_) -> {
			set1.add(p_160555_.immutable());
			reader.setBlock(p_160555_, p_160556_, 19);
		};
		BiConsumer<BlockPos, BlockState> biConsumer1 = (p_160548_, p_160549_) -> {
			set2.add(p_160548_.immutable());
			reader.setBlock(p_160548_, p_160549_, 19);
		};
		BiConsumer<BlockPos, BlockState> biConsumer2 = (p_160543_, p_160544_) -> {
			set3.add(p_160543_.immutable());
			reader.setBlock(p_160543_, p_160544_, 19);
		};
		boolean flag = this.doPlace(reader, rand, pos, biConsumer, biConsumer1, config);
		difference = mutableboundingbox.minY() - pos.getY();
		mutableboundingbox.move(0, pos.getY(), 0);
		if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
			if (!config.decorators.isEmpty()) {
				List<BlockPos> list1 = Lists.newArrayList(set1);
				List<BlockPos> list2 = Lists.newArrayList(set2);
				list1.sort(Comparator.comparingInt(Vec3i::getY));
				list2.sort(Comparator.comparingInt(Vec3i::getY));
				config.decorators.forEach((p_160528_) -> p_160528_.place(reader, biConsumer2, rand, list1, list2));
			}

			return BoundingBox.encapsulatingPositions(Iterables.concat(set1, set2, set3)).map((p_160521_) -> {
				DiscreteVoxelShape shape = updateLeaves(reader, p_160521_, set1, set3);
				StructureTemplate.updateShapeAtEdge(reader, 3, shape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
				return true;
			}).orElse(false);
		} else {
			return false;
		}
	}

	//Mostly [VanillaCopy] of TreeFeature.doPlace, edits noted
	private boolean doPlace(WorldGenLevel level, Random p_160512_, BlockPos pos, BiConsumer<BlockPos, BlockState> consumer1, BiConsumer<BlockPos, BlockState> consumer2, TreeConfiguration config) {
		int i = config.trunkPlacer.getTreeHeight(p_160512_);
		int j = config.foliagePlacer.foliageHeight(p_160512_, i, config);
		int k = i - j;
		int l = config.foliagePlacer.foliageRadius(p_160512_, k);
		//set our blockpos to the valid dirt pos, not highest ground
		pos = new BlockPos(pos.getX(), validPos.getY(), pos.getZ());
		if (pos.getY() >= level.getMinBuildHeight() + 1 && pos.getY() + i + 1 <= level.getMaxBuildHeight()) {
			if (!TFBlocks.DARKWOOD_SAPLING.get().canSurvive(level.getBlockState(pos), level, pos)) {
				return false;
			} else {
				OptionalInt optionalint = config.minimumSize.minClippedHeight();
				int i1 = this.getMaxFreeTreeHeight(level, i, pos, config);
				if (i1 >= i || optionalint.isPresent() && i1 >= optionalint.getAsInt()) {
					List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer.placeTrunk(level, consumer1, p_160512_, i1, pos, config);
					list.forEach((p_160539_) -> {
						config.foliagePlacer.createFoliage(level, consumer2, p_160512_, config, i1, p_160539_, j, l);
					});
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	//everything beyond this point is a [VanillaCopy] of TreeFeature
	private int getMaxFreeTreeHeight(LevelSimulatedReader level, int trunkHeight, BlockPos pos, TreeConfiguration config) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for(int i = 0; i <= trunkHeight + 1; ++i) {
			int j = config.minimumSize.getSizeAtHeight(trunkHeight, i);

			for(int k = -j; k <= j; ++k) {
				for(int l = -j; l <= j; ++l) {
					mutable.setWithOffset(pos, k, i, l);
					if (!isFree(level, mutable) || !config.ignoreVines) {
						return i - 2;
					}
				}
			}
		}

		return trunkHeight;
	}

	protected void setBlock(LevelWriter world, BlockPos pos, BlockState state) {
		setBlockKnownShape(world, pos, state);
	}

	public static void setBlockKnownShape(LevelWriter p_236408_0_, BlockPos p_236408_1_, BlockState p_236408_2_) {
		p_236408_0_.setBlock(p_236408_1_, p_236408_2_, 19);
	}

	private static DiscreteVoxelShape updateLeaves(LevelAccessor p_67203_, BoundingBox p_67204_, Set<BlockPos> p_67205_, Set<BlockPos> p_67206_) {
		List<Set<BlockPos>> list = Lists.newArrayList();
		DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(p_67204_.getXSpan(), p_67204_.getYSpan(), p_67204_.getZSpan());
		int i = 6;

		for(int j = 0; j < 6; ++j) {
			list.add(Sets.newHashSet());
		}

		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for(BlockPos blockpos : Lists.newArrayList(p_67206_)) {
			if (p_67204_.isInside(blockpos)) {
				discretevoxelshape.fill(blockpos.getX() - p_67204_.minX(), blockpos.getY() - p_67204_.minY(), blockpos.getZ() - p_67204_.minZ());
			}
		}

		for(BlockPos blockpos1 : Lists.newArrayList(p_67205_)) {
			if (p_67204_.isInside(blockpos1)) {
				discretevoxelshape.fill(blockpos1.getX() - p_67204_.minX(), blockpos1.getY() - p_67204_.minY(), blockpos1.getZ() - p_67204_.minZ());
			}

			for(Direction direction : Direction.values()) {
				blockpos$mutableblockpos.setWithOffset(blockpos1, direction);
				if (!p_67205_.contains(blockpos$mutableblockpos)) {
					BlockState blockstate = p_67203_.getBlockState(blockpos$mutableblockpos);
					if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
						list.get(0).add(blockpos$mutableblockpos.immutable());
						setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(1)));
						if (p_67204_.isInside(blockpos$mutableblockpos)) {
							discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
						}
					}
				}
			}
		}

		for(int l = 1; l < 6; ++l) {
			Set<BlockPos> set = list.get(l - 1);
			Set<BlockPos> set1 = list.get(l);

			for(BlockPos blockpos2 : set) {
				if (p_67204_.isInside(blockpos2)) {
					discretevoxelshape.fill(blockpos2.getX() - p_67204_.minX(), blockpos2.getY() - p_67204_.minY(), blockpos2.getZ() - p_67204_.minZ());
				}

				for(Direction direction1 : Direction.values()) {
					blockpos$mutableblockpos.setWithOffset(blockpos2, direction1);
					if (!set.contains(blockpos$mutableblockpos) && !set1.contains(blockpos$mutableblockpos)) {
						BlockState blockstate1 = p_67203_.getBlockState(blockpos$mutableblockpos);
						if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
							int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
							if (k > l + 1) {
								BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(l + 1));
								setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate2);
								if (p_67204_.isInside(blockpos$mutableblockpos)) {
									discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
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

	public static boolean isFree(LevelSimulatedReader pLevel, BlockPos pPos) {
		return validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, (p_67281_) -> p_67281_.is(BlockTags.LOGS));
	}

	private static boolean isBlockWater(LevelSimulatedReader pLevel, BlockPos pPos) {
		return pLevel.isStateAtPosition(pPos, (p_67271_) -> p_67271_.is(Blocks.WATER));
	}

	public static boolean isAirOrLeaves(LevelSimulatedReader pLevel, BlockPos pPos) {
		return pLevel.isStateAtPosition(pPos, (p_67266_) -> p_67266_.isAir() || p_67266_.is(BlockTags.LEAVES) || p_67266_.is(TFBlocks.HARDENED_DARK_LEAVES.get()));
	}

	private static boolean isReplaceablePlant(LevelSimulatedReader pLevel, BlockPos pPos) {
		return pLevel.isStateAtPosition(pPos, (p_160551_) -> {
			Material material = p_160551_.getMaterial();
			return material == Material.REPLACEABLE_PLANT;
		});
	}

	public static boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
		return isAirOrLeaves(pLevel, pPos) || isReplaceablePlant(pLevel, pPos) || isBlockWater(pLevel, pPos);
	}
}
