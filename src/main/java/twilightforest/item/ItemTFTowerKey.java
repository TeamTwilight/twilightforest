package twilightforest.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFTowerDevice;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerDeviceVariant;

import javax.annotation.Nonnull;

public class ItemTFTowerKey extends ItemTF {
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float fx, float fy, float fz) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == TFBlocks.tower_device && state.getValue(BlockTFTowerDevice.VARIANT) == TowerDeviceVariant.VANISH_LOCKED) {
			if (!world.isRemote) {
				BlockTFTowerDevice.unlockBlock(world, pos);
				player.getHeldItem(hand).shrink(1);
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
}
