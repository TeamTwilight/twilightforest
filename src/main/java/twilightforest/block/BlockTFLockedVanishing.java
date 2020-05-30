package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

//TODO: This might be broken
public class BlockTFLockedVanishing extends Block {

	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	public BlockTFLockedVanishing() {
		super(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(-1.0F, 35.0F).sound(SoundType.WOOD));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(stateContainer.getBaseState().with(LOCKED, true));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LOCKED);
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 15;
	}

	@Override
	public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		return state.get(LOCKED) ? 6000000.0F : super.getExplosionResistance(state, world, pos, exploder, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return !state.get(LOCKED) && super.canEntityDestroy(state, world, pos, entity);
	}

	/**
	 * Change this block into an different device block
	 */
	public static void unlockBlock(World world, BlockPos pos) {
		BlockState thereState = world.getBlockState(pos);

		if (thereState.getBlock() == TFBlocks.locked_vanishing_block.get() || thereState.get(LOCKED)) {
			changeToBlockState(world, pos, thereState.with(LOCKED, false));
			world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
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

	//TODO: Move to loot table
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
