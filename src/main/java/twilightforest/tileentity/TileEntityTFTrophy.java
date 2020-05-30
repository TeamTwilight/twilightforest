package twilightforest.tileentity;

import net.minecraft.tileentity.SkullTileEntity;

public class TileEntityTFTrophy extends SkullTileEntity {

	public int ticksExisted;

	@Override
	public void tick() {
		super.tick();
		this.ticksExisted++;
	}

//	@Override
//	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
//		return oldState.getBlock() != newState.getBlock();
//	}
}
