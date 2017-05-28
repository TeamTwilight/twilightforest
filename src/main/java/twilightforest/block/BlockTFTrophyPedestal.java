package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFAchievementPage;
import twilightforest.TwilightForestMod;
import twilightforest.block.enums.BossVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFTrophy;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFTrophyPedestal extends Block implements ModelRegisterCallback {
	private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static final PropertyEnum<BossVariant> BOSS = PropertyEnum.create("boss", BossVariant.class, BossVariant::hasTrophy);
	private static final PropertyBool LATENT = PropertyBool.create("latent");

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);

	public BlockTFTrophyPedestal() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setResistance(2000.0F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(getDefaultState().withProperty(LATENT, true).withProperty(FACING, EnumFacing.NORTH).withProperty(BOSS, BossVariant.NAGA));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LATENT, BOSS);
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
	public IBlockState getStateFromMeta(int meta) {
		IBlockState ret = getDefaultState();
		ret = ret.withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
		if ((meta & 0b100) > 0) {
			ret = ret.withProperty(LATENT, true);
		}
		return ret;
	}

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IBlockState stateAbove = world.getBlockState(pos.up());
        TileEntity tile = world.getTileEntity(pos);

        if (stateAbove.getBlock() != TFBlocks.trophy || (!state.getValue(LATENT)) || tile == null || (!(tile instanceof TileEntityTFTrophy)))
            return state;

        TileEntityTFTrophy trophy = (TileEntityTFTrophy)tile;

        BossVariant variant;

        switch (trophy.getSkullType()) {
            case 0:
                variant = BossVariant.HYDRA;
                break;
            default: variant = BossVariant.UR_GHAST;
        }

        return state.withProperty(BOSS, variant);
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
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
		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).expand(16.0D, 16.0D, 16.0D)))
			if (!isPlayerEligible(player)) player.sendMessage(new TextComponentString("You are unworthy."));
	}

	private boolean areNearbyPlayersEligible(World world, BlockPos pos) {
		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).expand(16.0D, 16.0D, 16.0D)))
			if (isPlayerEligible(player))
				return true;
		
		return false;
	}

	private boolean isPlayerEligible(EntityPlayer player) {
		return ((player instanceof EntityPlayerMP) && ((EntityPlayerMP)player).getStatFile().hasAchievementUnlocked(TFAchievementPage.twilightProgressTrophyPedestal.parentAchievement)) ||
				((player instanceof EntityPlayerSP) && ((EntityPlayerSP)player).getStatFileWriter().hasAchievementUnlocked(TFAchievementPage.twilightProgressTrophyPedestal.parentAchievement));
	}

	private void doPedestalEffect(World world, BlockPos pos, IBlockState state) {
		world.setBlockState(pos, state.withProperty(LATENT, false));
		removeNearbyShields(world, pos);
		world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.BLOCKS, 4.0F, 0.1F);
	}

	private void rewardNearbyPlayers(World world, BlockPos pos) {
		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).expand(16.0D, 16.0D, 16.0D)))
			player.addStat(TFAchievementPage.twilightProgressTrophyPedestal);
	}

	private void removeNearbyShields(World world, BlockPos pos) {
		for (int sx = -5; sx <= 5; sx++)
			for (int sy = -5; sy <= 5; sy++)
				for (int sz = -5; sz <= 5; sz++)
					if (world.getBlockState(pos.add(sx, sy, sz)) == TFBlocks.shield) {
						world.destroyBlock(pos.add(sx, sy, sz), false);
					}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	// todo ambiguous in 1.7, what was this supposed to be?
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
		return state.getValue(LATENT) ? -1 : super.getPlayerRelativeBlockHardness(state, player, world, pos);
	}

}
