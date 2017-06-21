package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTFGiantBlock extends Block {

	private IBlockState baseState;
	private boolean isSelfDestructing;

	//Atomic: Suppressing deprecation because this seems like a reasonable use for it.
	@SuppressWarnings("deprecation")
	public BlockTFGiantBlock(IBlockState state) {
		super(state.getMaterial());
		this.setSoundType(state.getBlock().getSoundType());
		this.baseState = state;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		pos = roundCoords(pos);

		for (int dx = 0; dx < 4; dx++) {
			for (int dy = 0; dy < 4; dy++) {
				for (int dz = 0; dz < 4; dz++) {
					IBlockState state = world.getBlockState(pos.add(dx, dy, dz));
					if (!state.getBlock().isReplaceable(world, pos))
						return false;
				}
			}
		}

		return super.canPlaceBlockAt(world, pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemStack) {
		if (!world.isRemote) {
			pos = roundCoords(pos);

			for (int dx = 0; dx < 4; dx++) {
				for (int dy = 0; dy < 4; dy++) {
					for (int dz = 0; dz < 4; dz++) {
						world.setBlockState(pos.add(dx, dy, dz), getDefaultState(), 2);
					}
				}
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

		BlockPos bPos = roundCoords(pos);

		for (int dx = 0; dx < 4; dx++) {
			for (int dy = 0; dy < 4; dy++) {
				for (int dz = 0; dz < 4; dz++) {
					BlockPos iterPos = bPos.add(dx, dy, dz);
					if (!pos.equals(iterPos)) {
						if (world.getBlockState(iterPos).getBlock() == this) {
							world.destroyBlock(iterPos, false);
						}
					}
				}
			}
		}

		this.isSelfDestructing = false;
	}

	private boolean canBlockStay(World world, BlockPos pos) {
		pos = roundCoords(pos);

		for (int dx = 0; dx < 4; dx++) {
			for (int dy = 0; dy < 4; dy++) {
				for (int dz = 0; dz < 4; dz++) {
					if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() != this)
						return false;
				}
			}
		}

		return true;
	}

	@Override
	@Deprecated
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

	public static BlockPos roundCoords(BlockPos pos) {
		return new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
	}

}
