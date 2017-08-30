package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFHugeStalk extends Block implements ModelRegisterCallback {

	protected BlockTFHugeStalk() {
		super(Material.WOOD);

		this.setHardness(1.25F);
		this.setResistance(7.0F);

		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	// [VanillaCopy] BlockLog
	@SuppressWarnings("unused")
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		int i = 4;
		int j = 5;

		if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5))) {
			for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
				IBlockState iblockstate = worldIn.getBlockState(blockpos);

				if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos)) {
					iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
				}
			}
		}
	}

}
