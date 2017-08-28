package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import static twilightforest.block.BlockTFHugeLilyPad.FACING;
import static twilightforest.block.BlockTFHugeLilyPad.PIECE;
import static twilightforest.block.enums.HugeLilypadPiece.NE;
import static twilightforest.block.enums.HugeLilypadPiece.NW;
import static twilightforest.block.enums.HugeLilypadPiece.SE;
import static twilightforest.block.enums.HugeLilypadPiece.SW;


public class ItemBlockTFHugeLilyPad extends ItemColored {

	public ItemBlockTFHugeLilyPad(Block block) {
		super(block, false);
	}

	// [VanillaCopy] ItemLilyPad.onItemRightClick, with special logic
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

		if (raytraceresult == null) {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		} else {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();
				BlockPos blockpos1 = blockpos.up();

				// TF - check the other 3 spots we touch as well
				if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)
						|| !worldIn.isBlockModifiable(playerIn, blockpos.east()) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit).east(), raytraceresult.sideHit, itemstack)
						|| !worldIn.isBlockModifiable(playerIn, blockpos.south()) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit).south(), raytraceresult.sideHit, itemstack)
						|| !worldIn.isBlockModifiable(playerIn, blockpos.east().south()) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit).east().south(), raytraceresult.sideHit, itemstack)) {
					return new ActionResult<>(EnumActionResult.FAIL, itemstack);
				}


				// TF check 4 blocks here
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				IBlockState iblockstatee = worldIn.getBlockState(blockpos.east());
				IBlockState iblockstatese = worldIn.getBlockState(blockpos.east().south());
				IBlockState iblockstates = worldIn.getBlockState(blockpos.south());
				if (iblockstate.getMaterial() == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0
						&& iblockstatee.getMaterial() == Material.WATER && iblockstatee.getValue(BlockLiquid.LEVEL) == 0
						&& iblockstatee.getMaterial() == Material.WATER && iblockstatee.getValue(BlockLiquid.LEVEL) == 0
						&& iblockstatese.getMaterial() == Material.WATER && iblockstatee.getValue(BlockLiquid.LEVEL) == 0
						&& iblockstates.getMaterial() == Material.WATER && iblockstatee.getValue(BlockLiquid.LEVEL) == 0
						&& worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.east()) && worldIn.isAirBlock(blockpos1.east().south()) && worldIn.isAirBlock(blockpos1.south())) {
					// TF - set 4 of them
					EnumFacing direction = playerIn.getHorizontalFacing();

					final IBlockState lilypad = TFBlocks.hugeLilyPad.getDefaultState().withProperty(FACING, direction);
					worldIn.setBlockState(blockpos1, lilypad.withProperty(PIECE, NW), 10);
					worldIn.setBlockState(blockpos1.east(), lilypad.withProperty(PIECE, NE), 10);
					worldIn.setBlockState(blockpos1.east().south(), lilypad.withProperty(PIECE, SE), 10);
					worldIn.setBlockState(blockpos1.south(), lilypad.withProperty(PIECE, SW), 11);

					if (!playerIn.capabilities.isCreativeMode) {
						itemstack.shrink(1);
					}

					playerIn.addStat(StatList.getObjectUseStats(this));
					worldIn.playSound(playerIn, blockpos, TFBlocks.hugeLilyPad.getSoundType(iblockstate, worldIn, blockpos, playerIn).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
				}
			}

			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
	}
}
