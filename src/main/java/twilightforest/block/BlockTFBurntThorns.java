package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

public class BlockTFBurntThorns extends BlockTFThorns {

	protected BlockTFBurntThorns() {
		this.setHardness(0.01F);
		this.setResistance(0.0F);
		this.setSoundType(SoundType.SAND);
		this.setCreativeTab(TFItems.creativeTab);

		this.setDefaultState(blockState.getBaseState()
				.withProperty(AXIS, EnumFacing.Axis.Y)
				.withProperty(NORTH, false).withProperty(SOUTH, false)
				.withProperty(WEST, false).withProperty(EAST, false));
	}

	@Override
	protected boolean hasVariant() {
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		// dissolve
		if (!world.isRemote && entity instanceof EntityLivingBase) {
			world.destroyBlock(pos, false);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean harvest) {
		world.setBlockToAir(pos);
		return true;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToState(this, 0, this.getDefaultState());
	}
}
