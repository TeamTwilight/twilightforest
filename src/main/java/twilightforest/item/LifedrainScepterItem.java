package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;
import twilightforest.util.EntityUtil;

import java.util.List;
import java.util.Optional;

public class LifedrainScepterItem extends Item {

	public LifedrainScepterItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}
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

	/**
	 * Animates the target falling apart into a rain of shatter particles
	 */
	public static void animateTargetShatter(ServerLevel level, LivingEntity target) {
		ItemStack itemId = Items.ROTTEN_FLESH.getDefaultInstance();
		// 1 in 100 chance of a big pop, you're welcome KD
		double explosionPower = level.getRandom().nextInt(100) == 0 ? 0.5D : 0.15D;

		for (int i = 0; i < 50 + ((int) target.dimensions.width * 75); ++i) {
			double gaussX = level.getRandom().nextGaussian() * 0.01D;
			double gaussY = level.getRandom().nextGaussian() * 0.01D;
			double gaussZ = level.getRandom().nextGaussian() * 0.01D;
			double gaussFactor = 5.0D;
			level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemId),
					target.getX() + level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussX * gaussFactor,
					target.getY() + level.getRandom().nextFloat() * target.getBbHeight() - gaussY * gaussFactor,
					target.getZ() + level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussZ * gaussFactor,
					1, gaussX, gaussY, gaussZ, level.getRandom().nextGaussian() * explosionPower);
		}
	}

	/**
	 * What, if anything, is the player currently looking at?
	 */
	@Nullable
	private Entity getPlayerLookTarget(Level level, LivingEntity living) {
		Entity pointedEntity = null;
		double range = 20.0D;
		Vec3 srcVec = living.getEyePosition();
		Vec3 lookVec = living.getViewVector(1.0F);
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
		float var9 = 1.0F;
		List<Entity> possibleList = level.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(var9, var9, var9));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {

			if (possibleEntity.isPickable()) {
				float borderSize = possibleEntity.getPickRadius();
				AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
				Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos.isPresent()) {
					double possibleDist = srcVec.distanceTo(interceptPos.get());

					if (possibleDist < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = possibleDist;
					}
				}
			}
		}
		return pointedEntity;
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
		if (stack.getDamageValue() == this.getMaxDamage(stack)) {
			// do not use
			living.stopUsingItem();
			return;
		}

		if (count % 5 == 0) {

			// is the player looking at an entity
			Entity pointedEntity = getPlayerLookTarget(level, living);

			if (pointedEntity instanceof LivingEntity target && !(target instanceof ArmorStand)) {
				if (level.isClientSide()) {
					this.makeRedMagicTrail(level, living, target.getEyePosition());
				} else {
					level.playSound(null, living.blockPosition(), TFSounds.SCEPTER_USE.get(), SoundSource.PLAYERS);
				}

				if (target.hurt(TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living), 1)) {
					// make it explode
					if (!level.isClientSide()) {
						if (target.getHealth() <= 1) {
							if (!target.getType().is(EntityTagGenerator.LIFEDRAIN_DROPS_NO_FLESH)) {
								target.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH, level.getRandom().nextInt(3)));
								animateTargetShatter((ServerLevel) level, target);
							}
							if (target instanceof Mob mob) {
								mob.spawnAnim();
							}
							target.playSound(TFSounds.SCEPTER_DRAIN.get(), 1.0F, living.getVoicePitch());
							SoundEvent deathSound = EntityUtil.getDeathSound(target);
							if (deathSound != null) {
								level.playSound(null, target.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, target.getVoicePitch());
							}
							if (!target.isDeadOrDying()) {
								if (target instanceof Player) {
									target.hurt(TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living), Float.MAX_VALUE);
								} else {
									target.die(TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living));
									target.discard();
								}
							}
						} else {
							target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
							if (count % 10 == 0) {
								// heal the player
								living.heal(1.0F);
								// and give foods
								if (living instanceof Player player)
									player.getFoodData().eat(1, 0.1F);
							}
						}

						if (living instanceof Player player && !player.isCreative()) {
							stack.hurt(1, level.getRandom(), null);
						}
					}
				}

				if (!level.isClientSide() && target.getHealth() <= living.getHealth()) {
					// only do lifting effect on creatures weaker than the player
					target.setDeltaMovement(0, 0.15D, 0);
				}
			}
		}
	}

	private void makeRedMagicTrail(Level level, LivingEntity source, Vec3 target) {
		// make particle trail
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 1.0F;
			float f1 = 0.5F;
			float f2 = 0.5F;
			double handOffset = source.getItemInHand(InteractionHand.OFF_HAND).is(this) ? -0.35D : 0.35D;
			double tx = source.getX() + (target.x() - source.getX()) * trailFactor + level.getRandom().nextGaussian() * 0.005D + (handOffset * Direction.fromYRot(source.yBodyRot).get2DDataValue());
			double ty = source.getEyeY() - 0.1D + (target.y() - source.getEyeY()) * trailFactor + level.getRandom().nextGaussian() * 0.005D - 0.1D;
			double tz = source.getZ() + (target.z() - source.getZ()) * trailFactor + level.getRandom().nextGaussian() * 0.005D + (handOffset * Direction.fromYRot(source.yBodyRot).get2DDataValue());
			level.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() == newStack.getItem();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}