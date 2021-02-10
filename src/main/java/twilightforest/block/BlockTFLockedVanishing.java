package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.item.TFItems;

public class BlockTFLockedVanishing extends BlockTFVanishingBlock {

	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	public BlockTFLockedVanishing(Properties props) {
		super(props);
		this.setDefaultState(getDefaultState().with(LOCKED, true));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LOCKED);
	}

	@Override
	public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
		return state.get(LOCKED) ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return !state.get(LOCKED) && super.canEntityDestroy(state, world, pos, entity);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && stack.getItem() == TFItems.tower_key.get() && state.get(LOCKED)) {
			if (!world.isRemote) {
				stack.shrink(1);
				world.setBlockState(pos, state.with(LOCKED, false));
				world.playSound(null, pos, TFSounds.UNLOCK_VANISHING_BLOCK, SoundCategory.BLOCKS, 0.3F, 0.6F);
			}
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}
}
