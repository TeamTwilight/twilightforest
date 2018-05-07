package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.advancements.TFAdvancements;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFTrophyPedestal extends Block implements ModelRegisterCallback {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool LATENT = PropertyBool.create("latent");

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);

	public BlockTFTrophyPedestal() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setResistance(2000.0F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(getDefaultState().withProperty(LATENT, true).withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LATENT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(LATENT)) {
			meta |= 1 << 2;
		}
		return meta;
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		IBlockState ret = getDefaultState();
		ret = ret.withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
		if ((meta & 0b100) > 0) {
			ret = ret.withProperty(LATENT, true);
		}
		return ret;
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
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (!world.isRemote && state.getValue(LATENT)) {
			if (isTrophyOnTop(world, pos)) {
				if (world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
					if (this.areNearbyPlayersEligible(world, pos))
						doPedestalEffect(world, pos, state);
					warnIneligiblePlayers(world, pos);
				} else {
					doPedestalEffect(world, pos, state);
				}

				rewardNearbyPlayers(world, pos);
			}
		}
	}

	private boolean isTrophyOnTop(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() == TFBlocks.trophy;
	}

	private void warnIneligiblePlayers(World world, BlockPos pos) {
		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(16.0D, 16.0D, 16.0D)))
			if (!isPlayerEligible(player)) player.sendMessage(new TextComponentTranslation(TwilightForestMod.ID + ".trophy_pedestal.ineligible"));
	}

	private boolean areNearbyPlayersEligible(World world, BlockPos pos) {
		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(16.0D, 16.0D, 16.0D)))
			if (isPlayerEligible(player))
				return true;

		return false;
	}

	private boolean isPlayerEligible(EntityPlayer player) {
		return TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, new ResourceLocation(TwilightForestMod.ID, "progress_hydra"));
	}

	private void doPedestalEffect(World world, BlockPos pos, IBlockState state) {
		world.setBlockState(pos, state.withProperty(LATENT, false));
		removeNearbyShields(world, pos);
		world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.BLOCKS, 4.0F, 0.1F);
	}

	private void rewardNearbyPlayers(World world, BlockPos pos) {
		for (EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).grow(16.0D, 16.0D, 16.0D)))
			TFAdvancements.PLACED_TROPHY_ON_PEDESTAL.trigger(player);
	}

	private void removeNearbyShields(World world, BlockPos pos) {
		for (int sx = -5; sx <= 5; sx++)
			for (int sy = -5; sy <= 5; sy++)
				for (int sz = -5; sz <= 5; sz++)
					if (world.getBlockState(pos.add(sx, sy, sz)).getBlock() == TFBlocks.stronghold_shield) {
						world.destroyBlock(pos.add(sx, sy, sz), false);
					}
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	// todo ambiguous in 1.7, what was this supposed to be?
	@Override
	@Deprecated
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		return state.getValue(LATENT) ? -1 : super.getPlayerRelativeBlockHardness(state, player, world, pos);
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
