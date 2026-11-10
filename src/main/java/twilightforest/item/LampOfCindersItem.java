package twilightforest.item;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class LampOfCindersItem extends Item {

	private static final int FIRING_TIME = 12;

	public LampOfCindersItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();

		if (this.burnBlock(world, pos)) {
			if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, player.getItemInHand(context.getHand()));

			if (player != null) player.playSound(TFSounds.LAMP_BURN.get(), 0.5F, 1.5F);

			// spawn flame particles
			for (int i = 0; i < 10; i++) {
				float dx = pos.getX() + 0.5F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.75F;
				float dy = pos.getY() + 0.5F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.75F;
				float dz = pos.getZ() + 0.5F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.75F;
				world.addParticle(ParticleTypes.SMOKE, dx, dy, dz, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.FLAME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
			}

			return InteractionResult.SUCCESS;
		} else return InteractionResult.PASS;
	}

	private boolean burnBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.is(TFBlocks.BROWN_THORNS) || state.is(TFBlocks.GREEN_THORNS)) {
			level.setBlockAndUpdate(pos, TFBlocks.BURNT_THORNS.get().withPropertiesOf(state));
			return true;
		} else return false;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int useRemaining) {
		int useTime = this.getUseDuration(stack, living) - useRemaining;

		if (useTime > FIRING_TIME && !stack.nextDamageWillBreak()) {
			return this.doBurnEffect(level, living);
		}
		return false;
	}

	private boolean doBurnEffect(Level level, LivingEntity user) {
		BlockPos pos = BlockPos.containing(user.getEyePosition().add(user.getLookAngle().scale(2.0D)));
		int range = 4;
		boolean burned = false;

		if (!level.isClientSide()) {
			level.playSound(null, user.blockPosition(), TFSounds.LAMP_BURN.get(), user.getSoundSource(), 1.5F, 0.8F);

			// set nearby thorns to burnt
			for (int dx = -range; dx <= range; dx++) {
				for (int dy = -range; dy <= range; dy++) {
					for (int dz = -range; dz <= range; dz++) {
						burned |= this.burnBlock(level, pos.offset(dx, dy, dz));
					}
				}
			}
		}

		if (user instanceof Player player) {
			for (int i = 0; i < 6; i++) {
				BlockPos rPos = pos.offset(
					level.getRandom().nextInt(range) - level.getRandom().nextInt(range),
					level.getRandom().nextInt(2),
					level.getRandom().nextInt(range) - level.getRandom().nextInt(range)
				);

				level.levelEvent(player, LevelEvent.PARTICLES_MOBBLOCK_SPAWN, rPos, 0);
			}

			//burn mobs!
			for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.below(2)).inflate(4.0D))) {
				if (target != user && !target.isAlliedTo(user)) {
					target.igniteForSeconds(5);
					burned = true;
				}
			}
		}
		return burned;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}
}