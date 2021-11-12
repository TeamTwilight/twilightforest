package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.item.TFItems;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class LockedVanishingBlock extends VanishingBlock {

	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	public LockedVanishingBlock(Properties props) {
		super(props);
		this.registerDefaultState(defaultBlockState().setValue(LOCKED, true));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LOCKED);
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
		return state.getValue(LOCKED) ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
		return !state.getValue(LOCKED) && super.canEntityDestroy(state, world, pos, entity);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() && stack.getItem() == TFItems.TOWER_KEY.get() && state.getValue(LOCKED)) {
			if (!world.isClientSide) {
				stack.shrink(1);
				world.setBlockAndUpdate(pos, state.setValue(LOCKED, false));
				world.playSound(null, pos, TFSounds.UNLOCK_VANISHING_BLOCK, SoundSource.BLOCKS, 0.3F, 0.6F);
			}
			return InteractionResult.SUCCESS;
		}
		return super.use(state, world, pos, player, hand, hit);
	}
}
