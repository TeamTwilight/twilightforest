package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.enums.TowerTranslucentVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFTowerTranslucent extends Block implements ModelRegisterCallback {
	public static final PropertyEnum<TowerTranslucentVariant> VARIANT = PropertyEnum.create("variant", TowerTranslucentVariant.class);
	private static final AxisAlignedBB REAPPEARING_BB = new AxisAlignedBB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F);

	public BlockTFTowerTranslucent() {
		super(Material.GLASS);
		this.setHardness(50.0F);
		this.setResistance(2000.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, TowerTranslucentVariant.REAPPEARING_INACTIVE));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, TowerTranslucentVariant.values()[meta]);
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState p_isFullCube_1_) {
		return false;
	}

	@Override
	public int tickRate(World world) {
		return 15;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		return null;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess par1World, BlockPos pos) {
		TowerTranslucentVariant variant = state.getValue(VARIANT);

		if (variant == TowerTranslucentVariant.REAPPEARING_INACTIVE || variant == TowerTranslucentVariant.REAPPEARING_ACTIVE) {
			return NULL_AABB;
		} else {
			return super.getCollisionBoundingBox(state, par1World, pos);
		}
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess par1IBlockAccess, BlockPos pos) {
		TowerTranslucentVariant variant = state.getValue(VARIANT);

		if (variant == TowerTranslucentVariant.REAPPEARING_INACTIVE || variant == TowerTranslucentVariant.REAPPEARING_ACTIVE) {
			return REAPPEARING_BB;
		} else {
			return FULL_BLOCK_AABB;
		}
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		TowerTranslucentVariant variant = state.getValue(VARIANT);

		if (variant == TowerTranslucentVariant.REAPPEARING_INACTIVE || variant == TowerTranslucentVariant.REAPPEARING_ACTIVE) {
			return BlockFaceShape.UNDEFINED;
		} else {
			return super.getBlockFaceShape(worldIn, state, pos, face);
		}
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		// reverter replacement is like glass
		TowerTranslucentVariant variant = state.getValue(VARIANT);

		if (variant == TowerTranslucentVariant.REVERTER_REPLACEMENT || variant == TowerTranslucentVariant.REACTOR_DEBRIS) {
			return 0.3F;
		} else {
			return super.getBlockHardness(state, world, pos);
		}
	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		switch (world.getBlockState(pos).getValue(VARIANT)) {
			case REAPPEARING_INACTIVE:
			case REAPPEARING_ACTIVE:
			default:
				return false;
			case BUILT_INACTIVE:
			case BUILT_ACTIVE:
			case REVERTER_REPLACEMENT:
			case REACTOR_DEBRIS:
				return true;
		}
	}

	// todo 1.10 smart model for REACTOR_DEBRIS that randomly chooses sides from portal/netherrack/bedrock/obsidian

	@Override
	public void updateTick(World par1World, BlockPos pos, IBlockState state, Random par5Random) {
		if (!par1World.isRemote) {
			TowerTranslucentVariant variant = state.getValue(VARIANT);

			if (variant == TowerTranslucentVariant.BUILT_ACTIVE) {
				par1World.setBlockToAir(pos);
				par1World.notifyNeighborsRespectDebug(pos, this, false);
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.5F);
				//par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);

				// activate all adjacent inactive vanish blocks
				for (EnumFacing e : EnumFacing.VALUES) {
					BlockTFTowerDevice.checkAndActivateVanishBlock(par1World, pos.offset(e));
				}
			}
			if (variant == TowerTranslucentVariant.REAPPEARING_ACTIVE) {
				par1World.setBlockState(pos, TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE));
				par1World.notifyNeighborsRespectDebug(pos, this, false);
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
				//par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
			} else if (variant == TowerTranslucentVariant.REAPPEARING_INACTIVE) {
				BlockTFTowerDevice.changeToActiveVanishBlock(par1World, pos, TowerTranslucentVariant.REAPPEARING_ACTIVE);
			}

		}
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

}
