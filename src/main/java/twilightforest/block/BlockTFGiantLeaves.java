package twilightforest.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;
import twilightforest.item.TFItems;

public class BlockTFGiantLeaves extends BlockTFGiantBlock {

	public BlockTFGiantLeaves() {
		super(Blocks.LEAVES.getDefaultState());
		this.setHardness(0.2F * 64F);
		this.setLightOpacity(1);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return TFConfig.performance.leavesFullCube;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return TFConfig.performance.leavesLightOpacity;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		switch (side) {
			case DOWN:
				return (pos.getY() & 3) == 0;
			case UP:
				return (pos.getY() & 3) == 3;
			case NORTH:
				return (pos.getZ() & 3) == 0;
			case SOUTH:
				return (pos.getZ() & 3) == 3;
			case WEST:
				return (pos.getX() & 3) == 0;
			case EAST:
				return (pos.getX() & 3) == 3;
		}

		return super.shouldSideBeRendered(state, world, pos, side);
	}
}
