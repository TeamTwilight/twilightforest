package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import twilightforest.entity.boss.AlphaYeti;

public class LifedrainScepterItem extends Item {

	protected LifedrainScepterItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
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
	private static void animateTargetShatter(Level world, LivingEntity target) {
		ItemStack itemId = new ItemStack(getTargetDropItem());
		for (int i = 0; i < 50; ++i) {
			double gaussX = world.random.nextGaussian() * 0.02D;
			double gaussY = world.random.nextGaussian() * 0.02D;
			double gaussZ = world.random.nextGaussian() * 0.02D;
			double gaussFactor = 10.0D;
			world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemId), target.getX() + world.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth() - gaussX * gaussFactor, target.getY() + world.random.nextFloat() * target.getBbHeight() - gaussY * gaussFactor, target.getZ() + world.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth() - gaussZ * gaussFactor, gaussX, gaussY, gaussZ);
		}
	}

	private static Item getTargetDropItem() {
		return Items.ROTTEN_FLESH;
	}

	/**
	 * What, if anything, is the player currently looking at?
	 */
	@Nullable
	private Entity getPlayerLookTarget(Level world, LivingEntity living) {
		Entity pointedEntity = null;
		double range = 20.0D;
		Vec3 srcVec = new Vec3(living.getX(), living.getY() + living.getEyeHeight(), living.getZ());
		Vec3 lookVec = living.getViewVector(1.0F);
		Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		float var9 = 1.0F;
		List<Entity> possibleList = world.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(var9, var9, var9));
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
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		Level world = living.level;

		if (stack.getDamageValue() == this.getMaxDamage(stack)) {
			// do not use
			living.stopUsingItem();
			return;
		}

		if (count % 5 == 0) {

			// is the player looking at an entity
			Entity pointedEntity = getPlayerLookTarget(world, living);

			if (pointedEntity instanceof LivingEntity target && !(target instanceof ArmorStand)) {
				if(target instanceof AlphaYeti alpha && !alpha.isRampaging() && !alpha.isTired()) return;

				if (target.getEffect(MobEffects.MOVEMENT_SLOWDOWN) != null || target.getHealth() < 1) {

					if (target.getHealth() <= 3) {
						// make it explode

						makeRedMagicTrail(world, living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), target.getX(), target.getY() + target.getEyeHeight(), target.getZ());
						if (target instanceof Mob) {
							((Mob) target).spawnAnim();
						}
						target.playSound(TFSounds.SCEPTER_DRAIN, 1.0F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						animateTargetShatter(world, target);
						if (!world.isClientSide) {
							target.spawnAtLocation(new ItemStack(getTargetDropItem(), world.random.nextInt(3)));
							target.die(DamageSource.indirectMagic(living, living));
							target.discard();
						}
						living.stopUsingItem();
					} else {
						// we have hit this creature recently
						if (!world.isClientSide) {
							target.hurt(DamageSource.indirectMagic(living, living), 3);

							// only do lifting effect on creatures weaker than the player
							if (getMaxHealth(target) <= getMaxHealth(living)) {
								target.setDeltaMovement(0, 0.2, 0);
							}

							target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));

							if (count % 10 == 0) {
								// heal the player
								living.heal(1);
								// and give foods
								if (living instanceof Player)
									((Player) living).getFoodData().eat(1, 0.1F);
							}
						}
					}
				} else {
					// this is a new creature to start draining
					makeRedMagicTrail(world, living.getX(), living.getY() + living.getEyeHeight(), living.getZ(), target.getX(), target.getY() + target.getEyeHeight(), target.getZ());

					living.playSound(TFSounds.SCEPTER_USE, 1.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);

					if (!world.isClientSide) {
						target.hurt(DamageSource.indirectMagic(living, living), 1);

						// only do lifting effect on creatures weaker than the player
						if (getMaxHealth(target) <= getMaxHealth(living)) {
							target.setDeltaMovement(0, 0.2, 0);
						}

						target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
					}
				}

				if (!world.isClientSide && living instanceof Player && !((Player)living).isCreative()) {
					stack.hurt(1, world.random, (ServerPlayer) null);
				}
			}
		}
	}

	private float getMaxHealth(LivingEntity target) {
		return (float) target.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
	}

	private void makeRedMagicTrail(Level world, double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 1.0F;
			float f1 = 0.5F;
			float f2 = 0.5F;
			double tx = srcX + (destX - srcX) * trailFactor + world.random.nextGaussian() * 0.005;
			double ty = srcY + (destY - srcY) * trailFactor + world.random.nextGaussian() * 0.005;
			double tz = srcZ + (destZ - srcZ) * trailFactor + world.random.nextGaussian() * 0.005;
			world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
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
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}
