package twilightforest.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTFTrophy extends TileEntitySkull {

	public int ticksExisted;

	@Override
	public void update() {
		super.update();
		this.ticksExisted++;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
