package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFShield extends Block implements ModelRegisterCallback {

	public BlockTFShield() {
		super(Material.ROCK);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).getIndex();
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	@Deprecated
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		// why can't we just pass the side to this method?  This is annoying and failure-prone
		RayTraceResult mop = getPlayerPointVec(world, player, 6.0);

		EnumFacing hitFace = mop != null ? mop.sideHit : null;
		EnumFacing blockFace = state.getValue(BlockDirectional.FACING);

		//System.out.printf("Determining relative hardness; facing = %d, meta = %d\n", facing, meta);

		if (hitFace == blockFace) {
			return player.getDigSpeed(Blocks.STONE.getDefaultState(), pos) / 1.5F / 100F;
		} else {
			return super.getPlayerRelativeBlockHardness(state, player, world, pos);
		}
	}

	/**
	 * What block is the player pointing at?
	 * <p>
	 * This very similar to player.rayTrace, but that method is not available on the server.
	 *
	 * @return
	 */
	private RayTraceResult getPlayerPointVec(World world, EntityPlayer player, double range) {
		Vec3d position = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d look = player.getLook(1.0F);
		Vec3d dest = position.addVector(look.x * range, look.y * range, look.z * range);
		return world.rayTraceBlocks(position, dest);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
}
