package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.projectile.EntityTFMoonwormShot;
import twilightforest.entity.TFEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTFMoonwormQueen extends Item {

	protected static final int FIRING_TIME = 12;

	protected ItemTFMoonwormQueen(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getDamage() >= stack.getMaxDamage() - 1) {
			return ActionResult.resultFail(stack);
		} else {
			player.setActiveHand(hand);
			return ActionResult.resultSuccess(player.getHeldItem(hand));
		}
	}

	//	[VanillaCopy] ItemBlock.onItemUse, harcoding the block
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState iblockstate = worldIn.getBlockState(pos);
		PlayerEntity player = context.getPlayer();
		BlockItemUseContext blockItemUseContext = new BlockItemUseContext(context);

		if (!iblockstate.getMaterial().isReplaceable()) {
			pos = pos.offset(context.getFace());
		}

		ItemStack itemstack = player.getHeldItem(context.getHand());

		if (itemstack.getDamage() < itemstack.getMaxDamage() && player.canPlayerEdit(pos, context.getFace(), itemstack) && worldIn.placedBlockCollides(TFBlocks.moonworm.get().getDefaultState(), pos, ISelectionContext.dummy())) {
			BlockState iblockstate1 = TFBlocks.moonworm.get().getStateForPlacement(blockItemUseContext);

			if (this.tryPlace(blockItemUseContext).isSuccess()) {
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				// TF - damage stack instead of shrinking
				player.resetActiveHand();
			}

			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.FAIL;
		}
	}


	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!world.isRemote && useTime > FIRING_TIME && (stack.getDamage() + 1) < stack.getMaxDamage()) {
			boolean fired = world.addEntity(new EntityTFMoonwormShot(TFEntities.moonworm_shot, world, living));

			if (fired) {
				stack.damageItem(2, living, (user) -> user.sendBreakAnimation(living.getActiveHand()));

				world.playSound(null, living.getPosX(), living.getPosY(), living.getPosZ(), TFSounds.MOONWORM_SQUISH, living instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, 1, 1);
			}
		}

	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	//everything from this point on is a [VanillaCopy] of BlockItem, since extending the class doesnt work for this
	public ActionResultType tryPlace(BlockItemUseContext context) {
		if (!context.canPlace()) {
			return ActionResultType.FAIL;
		} else {
			BlockItemUseContext blockitemusecontext = this.getBlockItemUseContext(context);
			if (blockitemusecontext == null) {
				return ActionResultType.FAIL;
			} else {
				BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
				if (blockstate == null) {
					return ActionResultType.FAIL;
				} else if (!this.placeBlock(blockitemusecontext, blockstate)) {
					return ActionResultType.FAIL;
				} else {
					BlockPos blockpos = blockitemusecontext.getPos();
					World world = blockitemusecontext.getWorld();
					PlayerEntity playerentity = blockitemusecontext.getPlayer();
					ItemStack itemstack = blockitemusecontext.getItem();
					BlockState blockstate1 = world.getBlockState(blockpos);
					Block block = blockstate1.getBlock();
					if (block == blockstate.getBlock()) {
						blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
						this.onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
						block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
						if (playerentity instanceof ServerPlayerEntity) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
						}
					}

					SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
					world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					if (playerentity == null || !playerentity.abilities.isCreativeMode) {
						itemstack.damageItem(1, playerentity, (user) -> user.sendBreakAnimation(playerentity.getActiveHand()));
					}

					return ActionResultType.SUCCESS;
				}
			}
		}
	}

	protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
		return state.getSoundType(world, pos, entity).getPlaceSound();
	}

	@Nullable
	public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context) {
		return context;
	}

	protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		return BlockItem.setTileEntityNBT(worldIn, player, pos, stack);
	}

	@Nullable
	protected BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = TFBlocks.moonworm.get().getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}

	protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
		PlayerEntity playerentity = p_195944_1_.getPlayer();
		ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(playerentity);
		return (p_195944_2_.isValidPosition(p_195944_1_.getWorld(), p_195944_1_.getPos())) && p_195944_1_.getWorld().placedBlockCollides(p_195944_2_, p_195944_1_.getPos(), iselectioncontext);
	}

	private BlockState func_219985_a(BlockPos p_219985_1_, World p_219985_2_, ItemStack p_219985_3_, BlockState p_219985_4_) {
		BlockState blockstate = p_219985_4_;
		CompoundNBT compoundnbt = p_219985_3_.getTag();
		if (compoundnbt != null) {
			CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
			StateContainer<Block, BlockState> statecontainer = p_219985_4_.getBlock().getStateContainer();

			for(String s : compoundnbt1.keySet()) {
				Property<?> property = statecontainer.getProperty(s);
				if (property != null) {
					String s1 = compoundnbt1.get(s).getString();
					blockstate = func_219988_a(blockstate, property, s1);
				}
			}
		}

		if (blockstate != p_219985_4_) {
			p_219985_2_.setBlockState(p_219985_1_, blockstate, 2);
		}

		return blockstate;
	}

	private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
		return p_219988_1_.parseValue(p_219988_2_).map((p_219986_2_) -> {
			return p_219988_0_.with(p_219988_1_, p_219986_2_);
		}).orElse(p_219988_0_);
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getPos(), state, 11);
	}
}
