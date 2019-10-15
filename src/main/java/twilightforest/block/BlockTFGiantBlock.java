package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public abstract class BlockTFGiantBlock extends Block implements ModelRegisterCallback {

	private boolean isSelfDestructing;

	//Atomic: Suppressing deprecation because this seems like a reasonable use for it.
	@SuppressWarnings("deprecation")
	public BlockTFGiantBlock(IBlockState state) {
		super(state.getMaterial());
		this.setSoundType(state.getBlock().getSoundType());
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		for (BlockPos dPos : getVolume(pos)) {
			IBlockState state = world.getBlockState(dPos);
			if (!state.getBlock().isReplaceable(world, dPos)) {
				return false;
			}
		}
		return super.canPlaceBlockAt(world, pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemStack) {
		if (!world.isRemote) {
			for (BlockPos dPos : getVolume(pos)) {
				world.setBlockState(dPos, getDefaultState(), 2);
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		if (!this.isSelfDestructing && !canBlockStay(world, pos)) {
			this.setGiantBlockToAir(world, pos);
		}
	}

	private void setGiantBlockToAir(World world, BlockPos pos) {
		// this flag is maybe not totally perfect
		this.isSelfDestructing = true;

		for (BlockPos iterPos : getVolume(pos)) {
			if (!pos.equals(iterPos)) {
				if (world.getBlockState(iterPos).getBlock() == this) {
					world.destroyBlock(iterPos, false);
				}
			}
		}

		this.isSelfDestructing = false;
	}

	private boolean canBlockStay(World world, BlockPos pos) {
		for (BlockPos dPos : getVolume(pos)) {
			if (world.getBlockState(dPos).getBlock() != this) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Deprecated
	public EnumPushReaction getPushReaction(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

	public static BlockPos roundCoords(BlockPos pos) {
		return new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
	}

	public static Iterable<BlockPos> getVolume(BlockPos pos) {
		return BlockPos.getAllInBox(
				pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11,
				pos.getX() |  0b11, pos.getY() |  0b11, pos.getZ() |  0b11
		);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
