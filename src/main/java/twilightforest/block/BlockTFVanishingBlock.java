package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//TODO: Breaking and entering, but without the entering and more the breaking
public class BlockTFVanishingBlock extends Block {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public BlockTFVanishingBlock() {
		super(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(-1.0F, 35.0F).sound(SoundType.WOOD).tickRandomly());
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 15;
	}

	@Override
	@Deprecated
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!state.get(ACTIVE)) {
			if (areBlocksLocked(world, pos)) {
				world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 0.3F);
			} else {
				changeToActiveVanishBlock(world, pos, true);
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		return !state.get(ACTIVE) ? 6000F : super.getExplosionResistance(state, world, pos, exploder, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return !state.get(ACTIVE) ? !areBlocksLocked(world, pos) : super.canEntityDestroy(state, world, pos, entity);
	}

	/**
	 * Are any of the connected tower device blocks a locked vanishing block?
	 */
	private static boolean areBlocksLocked(IBlockReader world, BlockPos pos) {
		Set<BlockPos> checked = new HashSet<>();
		checked.add(pos);
		return areBlocksLocked(world, pos, checked);
	}

	private static boolean areBlocksLocked(IBlockReader world, BlockPos pos, Set<BlockPos> checked) {
		for (Direction facing : Direction.values()) {
			BlockPos offset = pos.offset(facing);
			if (!checked.add(offset)) continue;
			BlockState state = world.getBlockState(offset);
			if (state.getBlock() == TFBlocks.locked_vanishing_block.get()) {
				if (state.get(BlockTFLockedVanishing.LOCKED)) {
					return true;
				}
				if (areBlocksLocked(world, offset, checked)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Change this block into an different device block
	 */
	private static void changeToBlockState(World world, BlockPos pos, BlockState state) {
		Block thereBlock = world.getBlockState(pos).getBlock();

		//if (thereBlock == TFBlocks.tower_device || thereBlock == TFBlocks.tower_translucent) {
		world.setBlockState(pos, state, 3);
		//world.markBlockRangeForRenderUpdate(pos, pos);
		//world.notifyNeighborsRespectDebug(pos, thereBlock, false);
		//}
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote) return;

		if (!state.get(ACTIVE) && world.isBlockPowered(pos) && !areBlocksLocked(world, pos)) {
			changeToActiveVanishBlock(world, pos, true);
		}
	}

	@Override
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.isRemote) return;

		if (state.get(ACTIVE)) {
			world.removeBlock(pos, false);
			//world.notifyNeighborsRespectDebug(pos, this, false);
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.5F);
			//world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);

			// activate all adjacent inactive vanish blocks
			for (Direction e : Direction.values()) {
				checkAndActivateVanishBlock(world, pos.offset(e));
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(ACTIVE)) {
			this.sparkle(world, pos);
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

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() + d0 + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube(worldIn, pos)) {
				d2 = (double) pos.getY() - d0;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() + d0 + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube(worldIn, pos)) {
				d3 = (double) pos.getZ() - d0;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() + d0 + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube(worldIn, pos)) {
				d1 = (double) pos.getX() - d0;
			}

			float f1 = 1.0F * 0.6F + 0.4F;
			float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
			float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.addParticle(new RedstoneParticleData(f1, f2, f3, 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void checkAndActivateVanishBlock(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		if (state == TFBlocks.vanishing_block.get().getDefaultState().with(BlockTFVanishingBlock.ACTIVE, false) || state == TFBlocks.locked_vanishing_block.get().getDefaultState().with(BlockTFLockedVanishing.LOCKED, false) && !areBlocksLocked(world, pos)) {
			changeToActiveVanishBlock(world, pos, TFBlocks.vanishing_block.get().getDefaultState().with(BlockTFVanishingBlock.ACTIVE, true));
		} else if (state == TFBlocks.reappearing_block.get().getDefaultState().with(BlockTFReappearingBlock.ACTIVE, false) && !areBlocksLocked(world, pos)) {
			changeToActiveVanishBlock(world, pos, TFBlocks.reappearing_block.get().getDefaultState().with(BlockTFReappearingBlock.ACTIVE, true));
		} else if (state == TFBlocks.builder_translucent.get().getDefaultState().with(BlockTFBuiltTranslucent.ACTIVE, false)) {
			changeToActiveVanishBlock(world, pos, TFBlocks.builder_translucent.get().getDefaultState().with(BlockTFBuiltTranslucent.ACTIVE, true));
		}
	}

	public static void changeToActiveVanishBlock(World world, BlockPos pos, boolean variant) {
		changeToActiveVanishBlock(world, pos, TFBlocks.vanishing_block.get().getDefaultState().with(ACTIVE, variant));
	}

	/**
	 * Change this block into an active vanishing block
	 */
	private static void changeToActiveVanishBlock(World world, BlockPos pos, BlockState state) {
		changeToBlockState(world, pos, state);
		world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.6F);
		//world.scheduleUpdate(pos, state.getBlock(), getTickRateFor(state, world.rand));
	}

	/**
	 * We need variable, metadata-based tick rates
	 */
//	private static int getTickRateFor(BlockState state, Random rand) {
//		if (state.getBlock() == TFBlocks.tower_device && (state.getValue(VARIANT) == TowerDeviceVariant.VANISH_ACTIVE || state.getValue(VARIANT) == TowerDeviceVariant.REAPPEARING_ACTIVE)) {
//			return 2 + rand.nextInt(5);
//		} else if (state.getBlock() == TFBlocks.tower_translucent && state.getValue(BlockTFTowerTranslucent.VARIANT) == TowerTranslucentVariant.BUILT_ACTIVE) {
//			return 10;
//		}
//
//		return 15;
//	}

	@Override
	@Deprecated
	public int getLightValue(BlockState state) {
		return state.get(ACTIVE) ? 4 : 0;
	}

//	@Override
//	public Item getItemDropped(BlockState state, Random random, int fortune) {
//		switch (state.getValue(VARIANT)) {
//			case ANTIBUILDER:
//				return Items.AIR;
//			default:
//				return Item.getItemFromBlock(this);
//		}
//	}
//
//	@Override
//	@Deprecated
//	protected boolean canSilkHarvest() {
//		return false;
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//		return false;
//	}
//
//	@Override
//	public int damageDropped(BlockState state) {
//		switch (state.getValue(VARIANT)) {
//			case REAPPEARING_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE);
//				break;
//			case BUILDER_ACTIVE:
//			case BUILDER_TIMEOUT:
//				state = state.with(VARIANT, TowerDeviceVariant.BUILDER_INACTIVE);
//				break;
//			case VANISH_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.VANISH_INACTIVE);
//				break;
//			case GHASTTRAP_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.GHASTTRAP_INACTIVE);
//				break;
//			case REACTOR_ACTIVE:
//				state = state.with(VARIANT, TowerDeviceVariant.REACTOR_INACTIVE);
//				break;
//			default:
//				break;
//		}
//
//		return getMetaFromState(state);
//	}

	//TODO: Move to client
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
