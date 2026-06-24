package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jspecify.annotations.Nullable;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class MoonwormQueenItem extends Item {

	public static final int FIRING_TIME = 12;

	public MoonwormQueenItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.nextDamageWillBreak()) {
			return InteractionResult.FAIL;
		} else {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
	}

	//	[VanillaCopy] BlockItem.useOn
	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult placeResult = this.place(new BlockPlaceContext(context));
		return !placeResult.consumesAction() ? super.use(context.getLevel(), context.getPlayer(), context.getHand()) : placeResult;
	}


	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack, living) - useRemaining;

		if (!level.isClientSide() && useTime > FIRING_TIME && !stack.nextDamageWillBreak()) {
			if (level.addFreshEntity(new MoonwormShot(TFEntities.MOONWORM_SHOT.get(), level, living))) {
				if (living instanceof Player player) stack.hurtWithoutBreaking(2, player);

				level.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.MOONWORM_SQUISH.get(), living instanceof Player ? SoundSource.PLAYERS : SoundSource.NEUTRAL, 1.0F, 1.0F);
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	//everything from this point on is a [VanillaCopy] of BlockItem, since extending the class doesnt work for this
	public InteractionResult place(BlockPlaceContext context) {
		if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
			return InteractionResult.FAIL;
		} else if (!context.canPlace()) {
			return InteractionResult.FAIL;
		} else {
			BlockState placementState = this.getPlacementState(context);
			if (placementState == null) {
				return InteractionResult.FAIL;
			} else if (!this.placeBlock(context, placementState)) {
				return InteractionResult.FAIL;
			} else {
				BlockPos pos = context.getClickedPos();
				Level level = context.getLevel();
				Player player = context.getPlayer();
				ItemStack itemStack = context.getItemInHand();
				BlockState placedState = level.getBlockState(pos);
				if (placedState.is(placementState.getBlock())) {
					placedState = this.updateBlockStateFromTag(pos, level, itemStack, placedState);
					BlockItem.updateCustomBlockEntityTag(level, player, pos, itemStack);
					BlockItem.updateBlockEntityComponents(level, pos, itemStack);
					placedState.getBlock().setPlacedBy(level, pos, placedState, player, itemStack);
					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, itemStack);
					}
				}

				SoundType soundType = placedState.getSoundType(level, pos, player);
				level.playSound(player, pos, this.getPlaceSound(placedState, level, pos, player), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
				level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));
				itemStack.hurtWithoutBreaking(1, player);
				return InteractionResult.SUCCESS;
			}
		}
	}

	private SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
		return state.getSoundType(world, pos, entity).getPlaceSound();
	}

	private @Nullable BlockState getPlacementState(BlockPlaceContext context) {
		BlockState stateForPlacement = this.getBlock().getStateForPlacement(context);
		return stateForPlacement != null && this.canPlace(context, stateForPlacement) ? stateForPlacement : null;
	}

	private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack itemStack, BlockState placedState) {
		BlockItemStateProperties blockState = itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
		if (blockState.isEmpty()) {
			return placedState;
		} else {
			BlockState modifiedState = blockState.apply(placedState);
			if (modifiedState != placedState) {
				level.setBlock(pos, modifiedState, 2);
			}

			return modifiedState;
		}
	}

	private boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
		return context.getLevel().setBlock(context.getClickedPos(), placementState, 11);
	}

	private boolean canPlace(BlockPlaceContext context, BlockState stateForPlacement) {
		Player player = context.getPlayer();
		return (stateForPlacement.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(stateForPlacement, context.getClickedPos(), CollisionContext.placementContext(player));
	}

	private Block getBlock() {
		return TFBlocks.MOONWORM.get();
	}
}