package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.TowerDeviceVariant;
import twilightforest.block.enums.TowerTranslucentVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFCReactorActive;
import twilightforest.tileentity.TileEntityTFGhastTrapActive;
import twilightforest.tileentity.TileEntityTFGhastTrapInactive;
import twilightforest.tileentity.TileEntityTFReverter;
import twilightforest.tileentity.TileEntityTFTowerBuilder;

import java.util.Arrays;
import java.util.Random;

public class BlockTFTowerDevice extends Block implements ModelRegisterCallback {
	public static final PropertyEnum<TowerDeviceVariant> VARIANT = PropertyEnum.create("variant", TowerDeviceVariant.class);

	public BlockTFTowerDevice() {
		super(Material.WOOD);
		this.setHardness(10F);
		this.setResistance(35F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE));
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
		return getDefaultState().withProperty(VARIANT, TowerDeviceVariant.values()[meta]);
	}

	@Override
	public int tickRate(World world) {
		return 15;
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.REAPPEARING_INACTIVE.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.VANISH_INACTIVE.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.VANISH_LOCKED.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.VANISH_UNLOCKED.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.BUILDER_INACTIVE.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.ANTIBUILDER.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.GHASTTRAP_INACTIVE.ordinal()));
		par3List.add(new ItemStack(this, 1, TowerDeviceVariant.REACTOR_INACTIVE.ordinal()));
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, EnumFacing side, float par7, float par8, float par9) {
		TowerDeviceVariant variant = state.getValue(VARIANT);

		if (variant == TowerDeviceVariant.VANISH_INACTIVE) {
			if (areNearbyLockBlocks(par1World, pos)) {
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 0.3F);
			} else {
				changeToActiveVanishBlock(par1World, pos, TowerDeviceVariant.VANISH_ACTIVE);
			}
			return true;
		}
		if (variant == TowerDeviceVariant.REAPPEARING_INACTIVE) {
			if (areNearbyLockBlocks(par1World, pos)) {
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 0.3F);
			} else {
				changeToActiveVanishBlock(par1World, pos, TowerDeviceVariant.REAPPEARING_ACTIVE);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TowerDeviceVariant variant = world.getBlockState(pos).getValue(VARIANT);

		if (variant == TowerDeviceVariant.VANISH_INACTIVE) {
			return 6000F;
		} else if (variant == TowerDeviceVariant.VANISH_LOCKED) {
			return 6000000.0F;
		} else {
			return super.getExplosionResistance(world, pos, exploder, explosion);
		}
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		// most vanish blocks can't be broken
		TowerDeviceVariant variant = state.getValue(VARIANT);

		switch (variant) {
			case REAPPEARING_ACTIVE:
			case VANISH_INACTIVE:
			case VANISH_ACTIVE:
			case VANISH_LOCKED:
			case VANISH_UNLOCKED:
				return -1;
			default:
				return super.getBlockHardness(state, world, pos);
		}
	}

	/**
	 * Are any of the 26 adjacent blocks a locked vanishing block?
	 */
	public static boolean areNearbyLockBlocks(World world, BlockPos pos) {
		boolean locked = false;

		//TODO: this is hacky.  We really need to determine the exact blocks of the door and check those for locks.
		for (int dx = -2; dx <= 2; dx++) {
			for (int dy = -2; dy <= 2; dy++) {
				for (int dz = -2; dz <= 2; dz++) {
					IBlockState state = world.getBlockState(pos.add(dx, dy, dz));
					if (state.getBlock() == TFBlocks.towerDevice
							&& state.getValue(VARIANT) == TowerDeviceVariant.VANISH_LOCKED) {
						locked = true;
					}
				}
			}
		}

		return locked;
	}

	/**
	 * Change this block into an different device block
	 */
	public static void unlockBlock(World par1World, BlockPos pos) {
		IBlockState thereState = par1World.getBlockState(pos);

		if (thereState.getBlock() == TFBlocks.towerDevice || thereState.getValue(BlockTFTowerDevice.VARIANT) == TowerDeviceVariant.VANISH_LOCKED) {
			changeToBlockMeta(par1World, pos, thereState.withProperty(VARIANT, TowerDeviceVariant.VANISH_UNLOCKED));
			par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}


	/**
	 * Change this block into an different device block
	 */
	private static void changeToBlockMeta(World par1World, BlockPos pos, IBlockState state) {
		Block thereBlockID = par1World.getBlockState(pos).getBlock();

		if (thereBlockID == TFBlocks.towerDevice || thereBlockID == TFBlocks.towerTranslucent) {
			par1World.setBlockState(pos, state, 3);
			par1World.markBlockRangeForRenderUpdate(pos, pos);
			par1World.notifyNeighborsRespectDebug(pos, thereBlockID, false);
		}
	}

	@Override
	public void onBlockAdded(World par1World, BlockPos pos, IBlockState state) {
		if (!par1World.isRemote) {
			if (state.getValue(VARIANT) == TowerDeviceVariant.BUILDER_INACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) > 0) {
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.BUILDER_ACTIVE));
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
			}

		}

	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World par1World, BlockPos pos, Block myBlockID, BlockPos fromPos) {
		TowerDeviceVariant variant = state.getValue(VARIANT);

		if (!par1World.isRemote) {
			if (variant == TowerDeviceVariant.VANISH_INACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) > 0 && !areNearbyLockBlocks(par1World, pos)) {
				changeToActiveVanishBlock(par1World, pos, TowerDeviceVariant.VANISH_ACTIVE);
			}

			if (variant == TowerDeviceVariant.REAPPEARING_INACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) > 0 && !areNearbyLockBlocks(par1World, pos)) {
				changeToActiveVanishBlock(par1World, pos, TowerDeviceVariant.REAPPEARING_ACTIVE);
			}

			if (variant == TowerDeviceVariant.BUILDER_INACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) > 0) {
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.BUILDER_ACTIVE));
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);

				par1World.scheduleUpdate(pos, this, 4);
			}

			if (variant == TowerDeviceVariant.BUILDER_ACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) == 0) {
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.BUILDER_INACTIVE));
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
				par1World.scheduleUpdate(pos, this, 4);
			}

			if (variant == TowerDeviceVariant.BUILDER_TIMEOUT && par1World.isBlockIndirectlyGettingPowered(pos) == 0) {
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.BUILDER_INACTIVE));
			}

			if (variant == TowerDeviceVariant.GHASTTRAP_INACTIVE && isInactiveTrapCharged(par1World, pos) && par1World.isBlockIndirectlyGettingPowered(pos) > 0) {
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.GHASTTRAP_ACTIVE));
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
				par1World.scheduleUpdate(pos, this, 4);
			}

			if (variant == TowerDeviceVariant.REACTOR_INACTIVE && isReactorReady(par1World, pos)) {
				// check if we should fire up the reactor
				changeToBlockMeta(par1World, pos, state.withProperty(VARIANT, TowerDeviceVariant.REACTOR_ACTIVE));
			}
		}
	}

	@Override
	public void updateTick(World par1World, BlockPos pos, IBlockState state, Random par5Random) {
		if (!par1World.isRemote) {
			TowerDeviceVariant variant = state.getValue(VARIANT);

			if (variant == TowerDeviceVariant.VANISH_ACTIVE || variant == TowerDeviceVariant.REAPPEARING_ACTIVE) {
				if (variant == TowerDeviceVariant.VANISH_ACTIVE) {
					par1World.setBlockToAir(pos);
				} else {
					par1World.setBlockState(pos, TFBlocks.towerTranslucent.getDefaultState().withProperty(BlockTFTowerTranslucent.VARIANT, TowerTranslucentVariant.REAPPEARING_INACTIVE));
					par1World.scheduleUpdate(pos, TFBlocks.towerTranslucent, 80);
				}
				par1World.notifyNeighborsRespectDebug(pos, this, false);
				par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.5F);
				//par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);

				// activate all adjacent inactive vanish blocks
				for (EnumFacing e : EnumFacing.VALUES) {
					checkAndActivateVanishBlock(par1World, pos.offset(e));
				}
			}

			if (variant == TowerDeviceVariant.BUILDER_ACTIVE && par1World.isBlockIndirectlyGettingPowered(pos) > 0) {
				this.letsBuild(par1World, pos);
			}

			if (variant == TowerDeviceVariant.BUILDER_INACTIVE || variant == TowerDeviceVariant.BUILDER_TIMEOUT) {
				// activate all adjacent inactive vanish blocks
				for (EnumFacing e : EnumFacing.VALUES) {
					checkAndActivateVanishBlock(par1World, pos.offset(e));
				}
			}
		}
	}

	/**
	 * Start the builder block tileentity building!
	 */
	private void letsBuild(World par1World, BlockPos pos) {
		TileEntityTFTowerBuilder tileEntity = (TileEntityTFTowerBuilder) par1World.getTileEntity(pos);

		if (tileEntity != null && !tileEntity.makingBlocks) {
			tileEntity.startBuilding();
		}

	}

	/**
	 * Check if the inactive trap block is fully charged
	 */
	private boolean isInactiveTrapCharged(World par1World, BlockPos pos) {
		TileEntityTFGhastTrapInactive tileEntity = (TileEntityTFGhastTrapInactive) par1World.getTileEntity(pos);
		return tileEntity != null && tileEntity.isCharged();
	}

	/**
	 * Check if the reactor has all the specified things around it
	 */
	private boolean isReactorReady(World world, BlockPos pos) {
		return Arrays.stream(EnumFacing.VALUES)
				.allMatch(e -> world.getBlockState(pos.offset(e)).getBlock() == Blocks.REDSTONE_BLOCK);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		TowerDeviceVariant variant = state.getValue(VARIANT);

		if (variant == TowerDeviceVariant.VANISH_ACTIVE || variant == TowerDeviceVariant.REAPPEARING_ACTIVE || variant == TowerDeviceVariant.BUILDER_ACTIVE) {
			this.sparkle(par1World, pos);
		}
	}

	// [VanillaCopy] BlockRedstoneOre.spawnParticles. Unchanged.
	public void sparkle(World worldIn, BlockPos pos) {
		Random random = worldIn.rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
				d1 = (double) pos.getX() - d0;
			}

			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void checkAndActivateVanishBlock(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block thereID = state.getBlock();

		if (thereID == TFBlocks.towerDevice && (state.getValue(VARIANT) == TowerDeviceVariant.VANISH_INACTIVE || state.getValue(VARIANT) == TowerDeviceVariant.VANISH_UNLOCKED) && !areNearbyLockBlocks(world, pos)) {
			changeToActiveVanishBlock(world, pos, TowerDeviceVariant.VANISH_ACTIVE);
		} else if (thereID == TFBlocks.towerDevice && state.getValue(VARIANT) == TowerDeviceVariant.REAPPEARING_INACTIVE && !areNearbyLockBlocks(world, pos)) {
			changeToActiveVanishBlock(world, pos, TowerDeviceVariant.REAPPEARING_ACTIVE);
		} else if (thereID == TFBlocks.towerTranslucent && state.getValue(BlockTFTowerTranslucent.VARIANT) == TowerTranslucentVariant.BUILT_INACTIVE) {
			changeToActiveVanishBlock(world, pos, TowerTranslucentVariant.BUILT_ACTIVE);
		}
	}

	public static void changeToActiveVanishBlock(World world, BlockPos pos, TowerDeviceVariant variant) {
		changeToActiveVanishBlock(world, pos, TFBlocks.towerDevice.getDefaultState().withProperty(VARIANT, variant));
	}

	public static void changeToActiveVanishBlock(World world, BlockPos pos, TowerTranslucentVariant variant) {
		changeToActiveVanishBlock(world, pos, TFBlocks.towerTranslucent.getDefaultState().withProperty(BlockTFTowerTranslucent.VARIANT, variant));
	}

	/**
	 * Change this block into an active vanishing block
	 */
	private static void changeToActiveVanishBlock(World par1World, BlockPos pos, IBlockState state) {
		changeToBlockMeta(par1World, pos, state);
		par1World.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.6F);
		par1World.scheduleUpdate(pos, state.getBlock(), getTickRateFor(state, par1World.rand));
	}

	/**
	 * We need variable, metadata-based tick rates
	 */
	private static int getTickRateFor(IBlockState state, Random rand) {
		if (state.getBlock() == TFBlocks.towerDevice && (state.getValue(VARIANT) == TowerDeviceVariant.VANISH_ACTIVE || state.getValue(VARIANT) == TowerDeviceVariant.REAPPEARING_ACTIVE)) {
			return 2 + rand.nextInt(5);
		} else if (state.getBlock() == TFBlocks.towerTranslucent && state.getValue(BlockTFTowerTranslucent.VARIANT) == TowerTranslucentVariant.BUILT_ACTIVE) {
			return 10;
		}

		return 15;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos) != state) {
			// why are you asking me?
			return world.getBlockState(pos).getLightValue(world, pos);
		}

		switch (state.getValue(VARIANT)) {
			case BUILDER_ACTIVE:
			case VANISH_ACTIVE:
			case REAPPEARING_ACTIVE:
				return 4;
			case ANTIBUILDER:
				return 10;
			case GHASTTRAP_ACTIVE:
			case REACTOR_ACTIVE:
				return 15;
			default:
				return 0;
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		TowerDeviceVariant variant = state.getValue(VARIANT);
		return variant == TowerDeviceVariant.BUILDER_ACTIVE || variant == TowerDeviceVariant.ANTIBUILDER || variant == TowerDeviceVariant.REACTOR_ACTIVE
				|| variant == TowerDeviceVariant.GHASTTRAP_INACTIVE || variant == TowerDeviceVariant.GHASTTRAP_ACTIVE;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		TowerDeviceVariant variant = state.getValue(VARIANT);

		if (variant == TowerDeviceVariant.BUILDER_ACTIVE) {
			return new TileEntityTFTowerBuilder();
		} else if (variant == TowerDeviceVariant.ANTIBUILDER) {
			return new TileEntityTFReverter();
		} else if (variant == TowerDeviceVariant.GHASTTRAP_INACTIVE) {
			return new TileEntityTFGhastTrapInactive();
		} else if (variant == TowerDeviceVariant.GHASTTRAP_ACTIVE) {
			return new TileEntityTFGhastTrapActive();
		} else if (variant == TowerDeviceVariant.REACTOR_ACTIVE) {
			return new TileEntityTFCReactorActive();
		} else {
			return null;
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		switch (state.getValue(VARIANT)) {
			case ANTIBUILDER:
				return null;
			default:
				return Item.getItemFromBlock(this);
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case REAPPEARING_ACTIVE:
				state = state.withProperty(VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE);
				break;
			case BUILDER_ACTIVE:
			case BUILDER_TIMEOUT:
				state = state.withProperty(VARIANT, TowerDeviceVariant.BUILDER_INACTIVE);
				break;
			case VANISH_ACTIVE:
				state = state.withProperty(VARIANT, TowerDeviceVariant.VANISH_INACTIVE);
				break;
			case GHASTTRAP_ACTIVE:
				state = state.withProperty(VARIANT, TowerDeviceVariant.GHASTTRAP_INACTIVE);
				break;
			case REACTOR_ACTIVE:
				state = state.withProperty(VARIANT, TowerDeviceVariant.REACTOR_INACTIVE);
				break;
			default:
				break;
		}

		return getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
