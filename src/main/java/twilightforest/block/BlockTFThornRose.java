package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.item.TFItems;

public class BlockTFThornRose extends Block {

	private static final float RADIUS = 0.4F;
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.5F - RADIUS, 0.5F - RADIUS, 0.5F - RADIUS, 0.5F + RADIUS, .5F + RADIUS, 0.5F + RADIUS);

	protected BlockTFThornRose() {
		super(Material.PLANTS);

		this.setHardness(10.0F);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return canBlockStay(world, pos);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (!canBlockStay(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}

	private boolean canBlockStay(World world, BlockPos pos) {
		boolean supported = false;

		for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos pos_ = pos.offset(dir);
			IBlockState state = world.getBlockState(pos_);

			if (state.getBlock().canSustainLeaves(state, world, pos_)) {
				supported = true;
			}
		}
		return supported;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
