package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MoonwormQueenItem extends Item {

	public static final int FIRING_TIME = 12;

	public MoonwormQueenItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getDamageValue() + 1 >= this.getMaxDamage(stack)) {
			return InteractionResultHolder.fail(stack);
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.success(player.getItemInHand(hand));
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
			ItemStack itemstack = player.getItemInHand(context.getHand());

			if (itemstack.getDamageValue() < itemstack.getMaxDamage() && player.mayUseItemAt(pos, context.getClickedFace(), itemstack) && level.isUnobstructed(TFBlocks.MOONWORM.get().defaultBlockState(), pos, CollisionContext.empty())) {
				if (this.tryPlace(blockItemUseContext).shouldSwing()) {
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
	public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!level.isClientSide() && useTime > FIRING_TIME && (stack.getDamageValue() + 1) < stack.getMaxDamage()) {

			if (level.addFreshEntity(new MoonwormShot(TFEntities.MOONWORM_SHOT.get(), level, living))) {
				if (living instanceof Player player && !player.getAbilities().instabuild) stack.hurt(2, level.getRandom(), null);

				level.playSound(null, living.getX(), living.getY(), living.getZ(), TFSounds.MOONWORM_SQUISH.get(), living instanceof Player ? SoundSource.PLAYERS : SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}

	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	//everything from this point on is a [VanillaCopy] of BlockItem, since extending the class doesnt work for this
	public InteractionResult tryPlace(BlockPlaceContext context) {
		if (!context.canPlace()) {
			return InteractionResult.FAIL;
		} else {
			BlockPlaceContext blockitemusecontext = this.getBlockItemUseContext(context);
			if (blockitemusecontext == null) {
				return InteractionResult.FAIL;
			} else {
				BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
				if (blockstate == null) {
					return InteractionResult.FAIL;
				} else if (!this.placeBlock(blockitemusecontext, blockstate)) {
					return InteractionResult.FAIL;
				} else {
					BlockPos blockpos = blockitemusecontext.getClickedPos();
					Level world = blockitemusecontext.getLevel();
					Player playerentity = blockitemusecontext.getPlayer();
					ItemStack itemstack = blockitemusecontext.getItemInHand();
					BlockState blockstate1 = world.getBlockState(blockpos);
					Block block = blockstate1.getBlock();
					if (block == blockstate.getBlock()) {
						blockstate1 = this.updateBlockStateFromTag(blockpos, world, itemstack, blockstate1);
						this.onBlockPlaced(blockpos, world, playerentity, itemstack);
						block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
						if (playerentity instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerentity, blockpos, itemstack);
						}
					}

					SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
					world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, Objects.requireNonNull(context.getPlayer())), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					if (playerentity == null || !playerentity.getAbilities().instabuild) {
						itemstack.hurt(1, world.random, null);
					}

					return InteractionResult.SUCCESS;
				}
			}
		}
	}

	protected SoundEvent getPlaceSound(BlockState state, Level level, BlockPos pos, Player entity) {
		return state.getSoundType(level, pos, entity).getPlaceSound();
	}

	@Nullable
	public BlockPlaceContext getBlockItemUseContext(BlockPlaceContext context) {
		return context;
	}

	protected boolean onBlockPlaced(BlockPos pos, Level level, @Nullable Player player, ItemStack stack) {
		return BlockItem.updateCustomBlockEntityTag(level, player, pos, stack);
	}

	@Nullable
	protected BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = TFBlocks.MOONWORM.get().getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}

	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		Player player = context.getPlayer();
		CollisionContext collision = player == null ? CollisionContext.empty() : CollisionContext.of(player);
		return (state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collision);
	}

	private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
		BlockState blockstate = state;
		CompoundTag compoundnbt = stack.getTag();
		if (compoundnbt != null) {
			CompoundTag compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
			StateDefinition<Block, BlockState> statecontainer = state.getBlock().getStateDefinition();

			for (String s : compoundnbt1.getAllKeys()) {
				Property<?> property = statecontainer.getProperty(s);
				if (property != null) {
					String s1 = compoundnbt1.get(s).getAsString();
					blockstate = updateState(blockstate, property, s1);
				}
			}
		}

		if (blockstate != state) {
			level.setBlock(pos, blockstate, 2);
		}

		return blockstate;
	}

	private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> property, String name) {
		return property.getValue(name).map(value -> state.setValue(property, value)).orElse(state);
	}

	protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
		return context.getLevel().setBlock(context.getClickedPos(), state, 11);
	}
}