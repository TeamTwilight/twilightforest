package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.projectile.EntityTFMoonwormShot;
import twilightforest.entity.TFEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTFMoonwormQueen extends ItemTF {

	private static final int FIRING_TIME = 12;

	protected ItemTFMoonwormQueen(Rarity rarity, Properties props) {
		super(rarity, props.maxDamage(256));
		addPropertyOverride(TwilightForestMod.prefix("alt"), new IItemPropertyGetter() {
			@OnlyIn(Dist.CLIENT)
			@Override
			public float call(@Nonnull ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
				if (entityIn != null && entityIn.getActiveItemStack() == stack) {
					int useTime = stack.getUseDuration() - entityIn.getItemInUseCount();
					if (useTime >= FIRING_TIME && (useTime >>> 1) % 2 == 0) {
						return 1;
					}
				}

				return 0;
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getDamage() >= stack.getMaxDamage() - 1) {
			return new ActionResult<>(ActionResultType.FAIL, stack);
		} else {
			player.setActiveHand(hand);
			return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
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

		if (itemstack.getDamage() < itemstack.getMaxDamage() && player.canPlayerEdit(pos, context.getFace(), itemstack) && worldIn.canPlace(TFBlocks.moonworm.get().getDefaultState(), pos, ISelectionContext.dummy())) {
			BlockState iblockstate1 = TFBlocks.moonworm.get().getStateForPlacement(blockItemUseContext);

			if (placeMoonwormAt(itemstack, player, worldIn, pos, iblockstate1)) {
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				// TF - damage stack instead of shrinking
				itemstack.damageItem(1, player, (user) -> user.sendBreakAnimation(context.getHand()));
				player.resetActiveHand();
			}

			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.FAIL;
		}
	}

	//	[VanillaCopy] ItemBlock.placeBlockAt
	private boolean placeMoonwormAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, BlockState state) {
		if (!world.setBlockState(pos, state, 11)) return false;

		BlockState real = world.getBlockState(pos);
		if (real.getBlock() == TFBlocks.moonworm.get()) {
			TFBlocks.moonworm.get().onBlockPlacedBy(world, pos, state, player, stack);
			if (player instanceof ServerPlayerEntity) {
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
			}
		}

		return true;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (!world.isRemote && useTime > FIRING_TIME && (stack.getDamage() + 1) < stack.getMaxDamage()) {
			boolean fired = world.addEntity(new EntityTFMoonwormShot(TFEntities.moonworm_shot.get(), world, living));

			if (fired) {
				stack.damageItem(2, living, (user) -> user.sendBreakAnimation(living.getActiveHand()));

				world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_HIT, living instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, 1, 1);
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
}
