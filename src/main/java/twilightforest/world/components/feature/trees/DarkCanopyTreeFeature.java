package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import twilightforest.util.features.FeaturePlacers;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */

//Lots of things from TreeFeature, but we're checking for dirt to place on
public class DarkCanopyTreeFeature extends Feature<TreeConfiguration> {

	public DarkCanopyTreeFeature(Codec<TreeConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<TreeConfiguration> ctx) {
		WorldGenLevel reader = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();

		// if we are given leaves as a starting position, seek dirt or grass underneath
		boolean foundDirt = false;
		for (int dy = pos.getY(); dy >= reader.getMinY(); dy--) {
			BlockState state = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ()));
			if (state.is(BlockTags.SUBSTRATE_OVERWORLD)) {
				// yes!
				foundDirt = true;
				pos = new BlockPos(pos.getX(), dy, pos.getZ());
				break;
			} else if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND)) {
				// nope
				break;
			}
		}

		if (!foundDirt) {
			return false;
		}

		for (int i = 0; i < 4; i++) {
			//We check against the TreeFeature's validTreePos method, to see if the tree can grow here, cuz the trunk placer uses this as well
			//If we don't, some trees end up growing only one or two blocks tall
			if (!FeaturePlacers.validTreePos(reader, pos.relative(Direction.UP, i))) return false;
		}

		// do not grow next to another tree
		for (Direction e : Direction.Plane.HORIZONTAL) {
			if (reader.getBlockState(pos.relative(e)).is(BlockTags.LOGS))
				return false;
		}

		//Taken from TreeFeature.generate, adjusting our BoundingBox to fit where the dirt is
		TreeConfiguration treeconfiguration = ctx.config();
		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
			set.add(p_160555_.immutable());
			reader.setBlock(p_160555_, p_160556_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
			set1.add(p_160548_.immutable());
			reader.setBlock(p_160548_, p_160549_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		FoliagePlacer.FoliageSetter setter = new FoliagePlacer.FoliageSetter() {
			@Override
			public void set(BlockPos pos, BlockState state) {
				set2.add(pos.immutable());
				reader.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			}

			@Override
			public boolean isSet(BlockPos p_272999_) {
				return set2.contains(p_272999_);
			}
		};
		BiConsumer<BlockPos, BlockState> biconsumer3 = (p_225290_, p_225291_) -> {
			set3.add(p_225290_.immutable());
			reader.setBlock(p_225290_, p_225291_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		boolean flag = this.doPlace(reader, rand, pos, biconsumer, biconsumer1, setter, treeconfiguration);
		if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
			if (!treeconfiguration.decorators.isEmpty()) {
				TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(reader, biconsumer3, rand, set1, set2, set);
				treeconfiguration.decorators.forEach((p_225282_) -> {
					p_225282_.place(treedecorator$context);
				});
			}

			return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map((p_160521_) -> {
				DiscreteVoxelShape shape = TreeFeature.updateLeaves(reader, p_160521_, set1, set3, set);
				StructureTemplate.updateShapeAtEdge(reader, 3, shape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
				return true;
			}).orElse(false);
		} else {
			return false;
		}
	}

	//Mostly [VanillaCopy] of TreeFeature.doPlace, edits noted
	private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> consumer, BiConsumer<BlockPos, BlockState> consumer1, FoliagePlacer.FoliageSetter foliageSetter, TreeConfiguration config) {
		int i = config.trunkPlacer.getTreeHeight(random);
		int j = config.foliagePlacer.foliageHeight(random, i, config);
		int k = i - j;
		int l = config.foliagePlacer.foliageRadius(random, k);
		BlockPos blockpos = config.rootPlacer.map((placer) -> placer.getTrunkOrigin(pos, random)).orElse(pos);
		int i1 = Math.min(pos.getY(), blockpos.getY());
		int j1 = Math.max(pos.getY(), blockpos.getY()) + i + 1;
		if (i1 >= level.getMinY() + 1 && j1 <= level.getMaxY() + 1) {
			OptionalInt optionalint = config.minimumSize.minClippedHeight();
			int k1 = this.getMaxFreeTreeHeight(level, i, blockpos, config);
			if (k1 >= i || optionalint.isPresent() && k1 >= optionalint.getAsInt()) {
				if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(level, consumer, random, pos, blockpos, config)) {
					return false;
				} else {
					List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer.placeTrunk(level, consumer1, random, k1, blockpos, config);
					list.forEach((attachment) -> {
						config.foliagePlacer.createFoliage(level, foliageSetter, random, config, k1, attachment, j, l);
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

	//everything beyond this point is a [VanillaCopy] of TreeFeature
	private int getMaxFreeTreeHeight(WorldGenLevel level, int trunkHeight, BlockPos pos, TreeConfiguration config) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int i = 0; i <= trunkHeight + 1; ++i) {
			int j = config.minimumSize.getSizeAtHeight(trunkHeight, i);

			for (int k = -j; k <= j; ++k) {
				for (int l = -j; l <= j; ++l) {
					mutable.setWithOffset(pos, k, i, l);
					if (!config.trunkPlacer.isFree(level, mutable) || !config.ignoreVines && TreeFeature.isVine(level, mutable)) {
						return i - 2;
					}
				}
			}
		}

		return trunkHeight;
	}

	@Override
	protected void setBlock(LevelWriter world, BlockPos pos, BlockState state) {
		setBlockKnownShape(world, pos, state);
	}

	private static void setBlockKnownShape(LevelWriter level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
	}
}
