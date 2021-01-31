package twilightforest.world.feature;

import com.mojang.serialization.Codec;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.block.TFBlocks;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;

public class TFGenTreeOfTransformation extends TFGenCanopyTree {

//	public TFGenTreeOfTransformation() {
//		this(false);
//	}
//
//	public TFGenTreeOfTransformation(boolean notify) {
//		super(notify);
//
//		this.treeState = TFBlocks.magic_log.getDefaultState().with(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS);
//		this.branchState = treeState.with(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
//		this.leafState = TFBlocks.magic_leaves.getDefaultState().with(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS).with(LeavesBlock.CHECK_DECAY, false);
//
//		this.minHeight = 11;
//		this.chanceAddFirstFive = Integer.MAX_VALUE;
//		this.chanceAddSecondFive = Integer.MAX_VALUE;
//	}

	public TFGenTreeOfTransformation(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(IWorld world, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (super.generate(world, random, pos, trunk, leaves, branch, root, mbb, config)) {
			// heart of transformation
			world.setBlockState(pos.up(3), TFBlocks.transformation_log_core.get().getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y), 3);
			return true;
		} else {
			return false;
		}
	}
}
