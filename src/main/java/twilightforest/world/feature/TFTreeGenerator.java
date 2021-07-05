package twilightforest.world.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.template.Template;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class TFTreeGenerator<T extends TFTreeFeatureConfig> extends Feature<T> {

//	protected BlockState treeState = TFBlocks.twilight_log.getDefaultState();
//	protected BlockState branchState = TFBlocks.twilight_log.getDefaultState().with(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE).with(BlockTFLog.VARIANT, WoodVariant.DARK);
//	protected BlockState leafState = TFBlocks.hedge.getDefaultState().with(BlockTFHedge.VARIANT, HedgeVariant.DARKWOOD_LEAVES);
//	protected BlockState rootState = TFBlocks.root.getDefaultState();
//
//	protected IPlantable source = TFBlocks.twilight_sapling;

//	public TFTreeGenerator() {
//		this(false);
//	}
//
//	public TFTreeGenerator(boolean notify) {
//		super(notify);
//	}

	public TFTreeGenerator(Codec<T> configIn) {
		super(configIn);
	}

//	@Override
//	public final void setBlockAndNotify(World world, BlockPos pos, BlockState state) {
//		setBlockAndNotifyAdequately(world, pos, state);
//	}

	//TODO: Figure out how to get this working
//	@Override
//	protected boolean canGrowInto(Block blockType) {
//		return TFGenHollowTree.canGrowInto(blockType);
//	}

	protected boolean generate(IWorld world, Random random, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> leavespos, MutableBoundingBox mbb, T config) {
		Set<BlockPos> branchSet = Sets.newHashSet();
		Set<BlockPos> rootSet = Sets.newHashSet();
		return generate(world, random, pos, logpos, leavespos, branchSet, rootSet, mbb, config);
	}

	//AbstractTreeFeature.place from 1.15, modified for us
	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
		Set<BlockPos> logs = Sets.newHashSet();
		Set<BlockPos> leaves = Sets.newHashSet();
		MutableBoundingBox mutableboundingbox = MutableBoundingBox.getNewBoundingBox();
		boolean flag = this.generate(world, random, pos, logs, leaves, mutableboundingbox, config);
		if (mutableboundingbox.minX <= mutableboundingbox.maxX && flag && !logs.isEmpty()) {
			VoxelShapePart voxelshapepart = this.getVoxelShapePart(world, mutableboundingbox, logs);
			Template.updatePostProcessing(world, 3, voxelshapepart, mutableboundingbox.minX, mutableboundingbox.minY, mutableboundingbox.minZ);
			return true;
		} else {
			return false;
		}
	}

	//TreeFeature.func_227214_a_ copy, modified to remove decorations
	private VoxelShapePart getVoxelShapePart(IWorld world, MutableBoundingBox mbb, Set<BlockPos> logPosSet) {
		List<Set<BlockPos>> list = Lists.newArrayList();
		VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(mbb.getXSize(), mbb.getYSize(), mbb.getZSize());

		for(int j = 0; j < 6; ++j) {
			list.add(Sets.newHashSet());
		}

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(BlockPos logPos : Lists.newArrayList(logPosSet)) {
			if (mbb.isVecInside(logPos)) {
				voxelshapepart.setFilled(logPos.getX() - mbb.minX, logPos.getY() - mbb.minY, logPos.getZ() - mbb.minZ, true, true);
			}

			for(Direction direction : Direction.values()) {
				mutable.setAndMove(logPos, direction);
				if (!logPosSet.contains(mutable)) {
					BlockState blockstate = world.getBlockState(mutable);
					if (blockstate.hasProperty(BlockStateProperties.DISTANCE_1_7)) {
						list.get(0).add(mutable.toImmutable());
						TreeFeature.setBlockStateWithoutUpdate(world, mutable, blockstate.with(BlockStateProperties.DISTANCE_1_7, 1));
						if (mbb.isVecInside(mutable)) {
							voxelshapepart.setFilled(mutable.getX() - mbb.minX, mutable.getY() - mbb.minY, mutable.getZ() - mbb.minZ, true, true);
						}
					}
				}
			}
		}

		for(int l = 1; l < 6; ++l) {
			Set<BlockPos> set = list.get(l - 1);
			Set<BlockPos> set1 = list.get(l);

			for(BlockPos blockpos2 : set) {
				if (mbb.isVecInside(blockpos2)) {
					voxelshapepart.setFilled(blockpos2.getX() - mbb.minX, blockpos2.getY() - mbb.minY, blockpos2.getZ() - mbb.minZ, true, true);
				}

				for(Direction direction1 : Direction.values()) {
					mutable.setAndMove(blockpos2, direction1);
					if (!set.contains(mutable) && !set1.contains(mutable)) {
						BlockState blockstate1 = world.getBlockState(mutable);
						if (blockstate1.hasProperty(BlockStateProperties.DISTANCE_1_7)) {
							int k = blockstate1.get(BlockStateProperties.DISTANCE_1_7);
							if (k > l + 1) {
								BlockState blockstate2 = blockstate1.with(BlockStateProperties.DISTANCE_1_7, l + 1);
								TreeFeature.setBlockStateWithoutUpdate(world, mutable, blockstate2);
								if (mbb.isVecInside(mutable)) {
									voxelshapepart.setFilled(mutable.getX() - mbb.minX, mutable.getY() - mbb.minY, mutable.getZ() - mbb.minZ, true, true);
								}

								set1.add(mutable.toImmutable());
							}
						}
					}
				}
			}
		}

		return voxelshapepart;
	}

	/**
	 * This works akin to the AbstractTreeFeature.generate, but put our branches and roots here
	 */
	protected abstract boolean generate(IWorld world, Random random, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> leavespos, Set<BlockPos> branchpos, Set<BlockPos> rootpos, MutableBoundingBox mbb, T config);

	//AbstractTrunkPlacer.func_236911_a_ copy - Use that one instead when extending that Abstract
	protected boolean setLogBlockState(IWorld world, Random random, BlockPos pos, Set<BlockPos> logPos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (TreeFeature.isReplaceableAt(world, pos)) {
			this.setBlockState(world, pos, config.trunkProvider.getBlockState(random, pos), mbb);
			logPos.add(pos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	//We aren't actually using this, but it is here just in case
	protected boolean setLeavesBlockState(IWorld world, Random random, BlockPos pos, Set<BlockPos> leavesPos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (TreeFeature.isReplaceableAt(world, pos)) {
			this.setBlockState(world, pos, config.leavesProvider.getBlockState(random, pos), mbb);
			leavesPos.add(pos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	public boolean setBranchBlockState(IWorld world, Random random, BlockPos pos, Set<BlockPos> branchpos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (TreeFeature.isReplaceableAt(world, pos)) {
			this.setBlockState(world, pos, config.branchProvider.getBlockState(random, pos), mbb);
			branchpos.add(pos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	protected boolean setRootsBlockState(IWorld world, Random random, BlockPos pos, Set<BlockPos> branchpos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		// XXX: This was originally an IWorld in AbstractTreeFeature.place, so it should be ok to cast it back.
		// If you're here investigating after it blew up, then the above assumption is no longer true.
		if (canRootGrowIn(world, pos)) {
			this.setBlockState(world, pos, config.rootsProvider.getBlockState(random, pos), mbb);
			branchpos.add(pos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	protected final void setBlockState(IWorldWriter world, BlockPos pos, BlockState state, MutableBoundingBox mbb) {
		world.setBlockState(pos, state, 19);
		mbb.expandTo(new MutableBoundingBox(pos, pos));
	}

	/**
	 * Build a root, but don't let it stick out too far into thin air because that's weird
	 */
	protected void buildRoot(IWorld world, Random rand, BlockPos pos, Set<BlockPos> setpos, double offset, int b, MutableBoundingBox mbb, T config) {
		BlockPos dest = FeatureUtil.translate(pos.down(b + 2), 5, 0.3 * b + offset, 0.8);

		// go through block by block and stop drawing when we head too far into open air
		BlockPos[] lineArray = FeatureUtil.getBresenhamArrays(pos.down(), dest);
		for (BlockPos coord : lineArray) {
			this.setRootsBlockState(world, rand, coord, setpos, mbb, config);
		}
	}

	// TODO should move to FeatureUtil
	public static boolean canRootGrowIn(IWorldReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		Block blockID = blockState.getBlock();

		if (blockState.isAir(world, pos)) {
			// roots can grow through air if they are near a solid block
			return FeatureUtil.isNearSolid(world, pos);
		} else {
			return (blockState.getBlockHardness(world, pos) >= 0)
					&& blockID != TFBlocks.stronghold_shield.get()
					&& blockID != TFBlocks.trophy_pedestal.get()
					&& blockID != TFBlocks.boss_spawner_naga.get()
					&& blockID != TFBlocks.boss_spawner_lich.get()
					&& blockID != TFBlocks.boss_spawner_hydra.get()
					&& blockID != TFBlocks.boss_spawner_ur_ghast.get()
					&& blockID != TFBlocks.boss_spawner_knight_phantom.get()
					&& blockID != TFBlocks.boss_spawner_snow_queen.get()
					&& blockID != TFBlocks.boss_spawner_minoshroom.get()
					&& blockID != TFBlocks.boss_spawner_alpha_yeti.get()
					&& (blockState.getMaterial() == Material.ORGANIC || blockState.getMaterial() == Material.EARTH || blockState.getMaterial() == Material.ROCK || blockState.getMaterial() == Material.WATER);
		}
	}

	/**
	 * Add a firefly at the specified height and angle.
	 *
	 * @param height how far up the tree
	 * @param angle  from 0 - 1 rotation around the tree
	 */
	protected void addFirefly(IWorld world, BlockPos pos, int height, double angle) {
		int iAngle = (int) (angle * 4.0);
		if (iAngle == 0) {
			setIfEmpty(world, pos.add( 1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.EAST));
		} else if (iAngle == 1) {
			setIfEmpty(world, pos.add(-1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.WEST));
		} else if (iAngle == 2) {
			setIfEmpty(world, pos.add( 0, height,  1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.SOUTH));
		} else if (iAngle == 3) {
			setIfEmpty(world, pos.add( 0, height, -1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.NORTH));
		}
	}

	private void setIfEmpty(IWorld world, BlockPos pos, BlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state,3);
		}
	}
}
