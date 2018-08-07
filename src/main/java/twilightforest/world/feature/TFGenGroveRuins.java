package twilightforest.world.feature;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


/**
 * Several ruin types that look like the quest grove
 *
 * @author Ben
 */
public class TFGenGroveRuins extends TFGenerator {

	private static final IBlockState MOSSY_STONEBRICK = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
	private static final IBlockState CHISELED_STONEBRICK = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		if (rand.nextBoolean()) {
			return generateLargeArch(world, rand, pos);
		} else {
			return generateSmallArch(world, rand, pos);
		}
	}

	/**
	 * Generate a ruin with the larger arch
	 */
	private boolean generateLargeArch(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos, 2, 7, 6)) {
			return false;
		}

		// pillar
		for (int dy = -2; dy <= 7; dy++) {
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 1), MOSSY_STONEBRICK);
			this.setBlockAndNotifyAdequately(world, pos.add(1, dy, 1), MOSSY_STONEBRICK);
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 2), MOSSY_STONEBRICK);
			this.setBlockAndNotifyAdequately(world, pos.add(1, dy, 2), MOSSY_STONEBRICK);
		}

		// broken floor part
		this.setBlockAndNotifyAdequately(world, pos.add(0, -1, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, -1, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, -2, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, -2, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, -1, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, -1, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, -2, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, -2, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, -1, 5), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, -2, 5), MOSSY_STONEBRICK);

		// broken top part
		this.setBlockAndNotifyAdequately(world, pos.add(0, 6, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 6, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 7, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 7, 3), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 6, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 6, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 7, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 7, 4), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 7, 5), MOSSY_STONEBRICK);

		// small piece of chiseled stone brick
		this.setBlockAndNotifyAdequately(world, pos.add(0, 5, 0), CHISELED_STONEBRICK);

		return true;
	}

	/**
	 * Generate a ruin with the smaller arch
	 */
	private boolean generateSmallArch(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos, 7, 5, 9)) {
			return false;
		}

		// corner
		this.setBlockAndNotifyAdequately(world, pos.add(0, 4, 0), CHISELED_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 3, 0), CHISELED_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(1, 4, 0), CHISELED_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(2, 4, 0), CHISELED_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 4, 1), CHISELED_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(0, 4, 2), CHISELED_STONEBRICK);

		// broken arch in x direction
		for (int dy = -1; dy <= 5; dy++) {
			this.setBlockAndNotifyAdequately(world, pos.add(3, dy, 0), MOSSY_STONEBRICK);
		}
		this.setBlockAndNotifyAdequately(world, pos.add(4, -1, 0), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(5, -1, 0), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(6, -1, 0), MOSSY_STONEBRICK);

		this.setBlockAndNotifyAdequately(world, pos.add(4, 5, 0), MOSSY_STONEBRICK);
		this.setBlockAndNotifyAdequately(world, pos.add(5, 5, 0), MOSSY_STONEBRICK);

		// full arch in z direction
		for (int dy = -1; dy <= 5; dy++) {
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 3), MOSSY_STONEBRICK);
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 7), MOSSY_STONEBRICK);
		}
		for (int dz = 4; dz < 7; dz++) {
			this.setBlockAndNotifyAdequately(world, pos.add(0, -1, dz), MOSSY_STONEBRICK);
			this.setBlockAndNotifyAdequately(world, pos.add(0, 5, dz), MOSSY_STONEBRICK);
		}

		// small piece of chiseled stone brick
		this.setBlockAndNotifyAdequately(world, pos.add(0, 4, 8), CHISELED_STONEBRICK);


		return true;
	}

}

