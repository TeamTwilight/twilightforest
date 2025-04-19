package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
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
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.util.TFItemStackUtils;

public class MoonwormQueenItem extends Item {

	public static final int FIRING_TIME = 12;

	public MoonwormQueenItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (TFItemStackUtils.isAtZeroDurability(stack)) {
			return InteractionResult.FAIL;
		} else {
			player.startUsingItem(hand);
			return InteractionResult.SUCCESS;
		}
	}

	//	[VanillaCopy] ItemBlock.onItemUse, hardcoding the block
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Player player = context.getPlayer();
		BlockPlaceContext blockItemUseContext = new BlockPlaceContext(context);

		if (!state.canBeReplaced()) {
			pos = pos.relative(context.getClickedFace());
		}

		if (player != null) {
			ItemStack stack = player.getItemInHand(context.getHand());

			if (!TFItemStackUtils.isAtZeroDurability(stack) && player.mayUseItemAt(pos, context.getClickedFace(), stack) && level.isUnobstructed(TFBlocks.MOONWORM.get().defaultBlockState(), pos, CollisionContext.empty())) {
				if (this.tryPlace(blockItemUseContext).consumesAction()) {
					SoundType soundtype = level.getBlockState(pos).getBlock().getSoundType(level.getBlockState(pos), level, pos, player);
					level.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					// TF - damage stack instead of shrinking
					player.stopUsingItem();
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.FAIL;
	}


	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack, living) - useRemaining;

		if (level instanceof ServerLevel serverLevel && useTime > FIRING_TIME && stack.getDamageValue() < stack.getMaxDamage() - 2) {
			Projectile.spawnProjectileFromRotation((lev, owner, stacc) -> new MoonwormShot(lev, owner), serverLevel, stack, living, 0.0F, 1.5F, 1.0F);
			if (living instanceof Player player) TFItemStackUtils.hurtWithoutBreaking(stack, 2, player);
			level.playSound(null, living.blockPosition(), TFSounds.MOONWORM_SQUISH.get(), living instanceof Player ? SoundSource.PLAYERS : SoundSource.NEUTRAL, 1.0F, 1.0F);
			return true;
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
	public InteractionResult tryPlace(BlockPlaceContext context) {
		if (!context.canPlace()) {
			return InteractionResult.FAIL;
		} else {

			BlockState blockstate = this.getPlacementState(context);
			if (blockstate == null) {
				return InteractionResult.FAIL;
			} else if (!context.getLevel().setBlock(context.getClickedPos(), blockstate, Block.UPDATE_ALL_IMMEDIATE)) {
				return InteractionResult.FAIL;
			} else {
				BlockPos blockpos = context.getClickedPos();
				Level level = context.getLevel();
				Player player = context.getPlayer();
				ItemStack stack = context.getItemInHand();
				BlockState blockstate1 = level.getBlockState(blockpos);
				if (blockstate1.is(blockstate.getBlock())) {
					blockstate1 = this.updateBlockStateFromTag(blockpos, level, stack, blockstate1);
					BlockItem.updateCustomBlockEntityTag(level, player, blockpos, stack);
					BlockItem.updateBlockEntityComponents(level, blockpos, stack);
					blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, stack);
					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, stack);
					}
				}

				SoundType soundtype = blockstate1.getSoundType(level, blockpos, player);
				level.playSound(player, blockpos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
				//TF: instead of shrinking the stack here damage it
				TFItemStackUtils.hurtWithoutBreaking(stack, 1, player);
				return InteractionResult.SUCCESS;
			}
		}
	}

	@Nullable
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState blockstate = TFBlocks.MOONWORM.get().getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}

	private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
		BlockItemStateProperties blockitemstateproperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
		if (blockitemstateproperties.isEmpty()) {
			return state;
		} else {
			BlockState blockstate = blockitemstateproperties.apply(state);
			if (blockstate != state) {
				level.setBlock(pos, blockstate, Block.UPDATE_CLIENTS);
			}

			return blockstate;
		}
	}

	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		Player player = context.getPlayer();
		CollisionContext collision = player == null ? CollisionContext.empty() : CollisionContext.of(player);
		return (state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collision);
	}
}