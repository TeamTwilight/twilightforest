package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import javax.annotation.Nonnull;

public class ItemTFLampOfCinders extends Item {
	private static final int FIRING_TIME = 12;

	ItemTFLampOfCinders(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();

		if (burnBlock(world, pos)) {
			if (player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, player.getHeldItem(context.getHand()));

			player.playSound(TFSounds.LAMP_BURN, 0.5F, 1.5F);

			// spawn flame particles
			for (int i = 0; i < 10; i++) {
				float dx = pos.getX() + 0.5F + (random.nextFloat() - random.nextFloat()) * 0.75F;
				float dy = pos.getY() + 0.5F + (random.nextFloat() - random.nextFloat()) * 0.75F;
				float dz = pos.getZ() + 0.5F + (random.nextFloat() - random.nextFloat()) * 0.75F;
				world.addParticle(ParticleTypes.SMOKE, dx, dy, dz, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.FLAME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
			}

			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}

	private boolean burnBlock(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == TFBlocks.brown_thorns.get() || state.getBlock() == TFBlocks.green_thorns.get()) {
			world.setBlockState(pos, TFBlocks.burnt_thorns.get().getDefaultState().with(RotatedPillarBlock.AXIS, state.get(RotatedPillarBlock.AXIS)));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack) - useRemaining;

		if (useTime > FIRING_TIME && (stack.getDamage() + 1) < this.getMaxDamage(stack)) {
			doBurnEffect(world, living);
		}
	}

	private void doBurnEffect(World world, LivingEntity living) {
		BlockPos pos = new BlockPos(
				MathHelper.floor(living.lastTickPosX),
				MathHelper.floor(living.lastTickPosY + living.getEyeHeight()),
				MathHelper.floor(living.lastTickPosZ)
		);

		int range = 4;

		if (!world.isRemote) {
			world.playSound(null, living.getPosX(), living.getPosY(), living.getPosZ(), TFSounds.LAMP_BURN, living.getSoundCategory(), 1.5F, 0.8F);

			// set nearby thorns to burnt
			for (int dx = -range; dx <= range; dx++) {
				for (int dy = -range; dy <= range; dy++) {
					for (int dz = -range; dz <= range; dz++) {
						this.burnBlock(world, pos.add(dx, dy, dz));
					}
				}
			}
		}

		if (living instanceof PlayerEntity) {
			for (int i = 0; i < 6; i++) {
				BlockPos rPos = pos.add(
						random.nextInt(range) - random.nextInt(range),
						random.nextInt(2),
						random.nextInt(range) - random.nextInt(range)
				);

				world.playEvent((PlayerEntity) living, 2004, rPos, 0);
			}

			//burn mobs!
			for(LivingEntity targets : world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(new BlockPos(living.getPosX(), living.getPosYEye(), living.getPosZ())).grow(4.0D))) {
				if(!(targets instanceof PlayerEntity)) {
					targets.setFire(5);
				}
			}
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}
}
