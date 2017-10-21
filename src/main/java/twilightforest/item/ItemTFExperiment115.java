package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

public class ItemTFExperiment115 extends ItemTFFood {

	public ItemTFExperiment115() {
		super(4, 0.3F, false);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		// Let eating take priority over block placement
		if (player.canEat(false))
			return EnumActionResult.PASS;

		IBlockState e115 = TFBlocks.experiment_115.getDefaultState();

		if (!block.isReplaceable(world, pos)) pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);

		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && world.mayPlace(e115.getBlock(), pos, false, facing, null)) {
			if (world.setBlockState(pos, e115, 11)) {
				TFBlocks.experiment_115.onBlockPlacedBy(world, pos, e115, player, itemstack);
				SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
			}

			if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);

			return EnumActionResult.SUCCESS;
		} else return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

}
