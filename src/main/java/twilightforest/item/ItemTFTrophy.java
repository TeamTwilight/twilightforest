package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.BlockTFTrophy;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.BossVariant;

import javax.annotation.Nonnull;

public class ItemTFTrophy extends ItemTF {
	public ItemTFTrophy() {
		this.setCreativeTab(TFItems.creativeTab);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			for (BossVariant v : BossVariant.values()) {
				if (v.hasTrophy()) {
					list.add(new ItemStack(this, 1, v.ordinal()));
				}
			}
		}
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	// [VanillaCopy] ItemSkull, with own block and no player heads
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing == EnumFacing.DOWN) {
			return EnumActionResult.FAIL;
		} else {
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				facing = EnumFacing.UP;
				pos = pos.down();
			}
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag) {
				if (!worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.isSideSolid(pos, facing, true)) {
					return EnumActionResult.FAIL;
				}

				pos = pos.offset(facing);
			}

			ItemStack itemstack = playerIn.getHeldItem(hand);

			if (playerIn.canPlayerEdit(pos, facing, itemstack) && TFBlocks.trophy.canPlaceBlockAt(worldIn, pos)) {
				if (worldIn.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					worldIn.setBlockState(pos, TFBlocks.trophy.getDefaultState().withProperty(BlockTFTrophy.FACING, facing), 11);
					int i = 0;

					if (facing == EnumFacing.UP) {
						i = MathHelper.floor((double) (playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
					}

					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntitySkull) {
						TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;

						tileentityskull.setType(itemstack.getMetadata());

						tileentityskull.setSkullRotation(i);
					}

					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = MathHelper.clamp(stack.getItemDamage(), 0, BossVariant.values().length);
		return super.getUnlocalizedName() + "." + BossVariant.values()[meta].getName();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		for (int i = 0; i < BossVariant.values().length; i++) {
			String variant = "inventory_" + BossVariant.values()[i].getName();
			ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), variant);
			ModelLoader.setCustomModelResourceLocation(this, i, mrl);
		}
	}
}
