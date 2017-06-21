package twilightforest.world;

import net.minecraft.block.BlockLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.MagicWoodVariant;

import java.util.Random;


public class TFGenTreeOfTransformation extends TFGenCanopyTree {

	public TFGenTreeOfTransformation() {
		this(false);
	}

	public TFGenTreeOfTransformation(boolean notify) {
		super(notify);

		this.treeState = TFBlocks.magicLog.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS);
		this.branchState = treeState.withProperty(BlockTFMagicLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		this.leafState = TFBlocks.magicLeaves.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS);

		this.minHeight = 11;
		this.chanceAddFirstFive = Integer.MAX_VALUE;
		this.chanceAddSecondFive = Integer.MAX_VALUE;
	}


	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		if (super.generate(world, random, pos)) {
			// heart of transformation
			setBlockAndNotifyAdequately(world, pos.up(3), TFBlocks.magicLogSpecial.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS));
			return true;
		} else {
			return false;
		}
	}


}
