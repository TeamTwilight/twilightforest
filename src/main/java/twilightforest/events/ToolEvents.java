package twilightforest.events;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFItems;
import twilightforest.item.EnderBowItem;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.MinotaurAxeItem;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	@SubscribeEvent
	public static void onEnderBowHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
			&& evt.getRayTraceResult() instanceof EntityHitResult result
			&& result.getEntity() instanceof LivingEntity living
			&& arrow.getOwner() != result.getEntity() && !result.getEntity().getType().is(Tags.EntityTypes.BOSSES)) {

			if (arrow.getPersistentData().contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
				player.level().broadcastEntityEvent(player, (byte) 46);
				if (living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle(), true);
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.level().broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle, true);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void fieryToolSetFire(LivingIncomingDamageEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD.get()) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE.get())) && !event.getEntity().fireImmune()) {
			event.getEntity().igniteForSeconds(1);
		}
	}

	@SubscribeEvent
	@SuppressWarnings("UnstableApiUsage")
	public static void onKnightmetalToolDamage(LivingDamageEvent.Pre event) {
		LivingEntity target = event.getEntity();

		DamageContainer container = event.getContainer();
		if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living) {
			ItemStack weapon = living.getMainHandItem();

			if (!weapon.isEmpty()) {
				if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
					if (target.getArmorCoverPercentage() > 0) {
						int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
						container.setNewDamage(container.getOriginalDamage() + moreBonus);
					} else {
						container.setNewDamage(container.getOriginalDamage() + KNIGHTMETAL_BONUS_DAMAGE);
					}
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
					container.setNewDamage(container.getOriginalDamage() + KNIGHTMETAL_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}

	@SubscribeEvent
	@SuppressWarnings("UnstableApiUsage")
	public static void onMinotaurAxeCharge(LivingDamageEvent.Pre event) {
		LivingEntity target = event.getEntity();
		DamageContainer container = event.getContainer();
		if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living && living.isSprinting() && (container.getSource().getMsgId().equals("player") || container.getSource().getMsgId().equals("mob"))) {
			ItemStack weapon = living.getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				container.setNewDamage(container.getOriginalDamage() + MINOTAUR_AXE_BONUS_DAMAGE);
				// enchantment attack sparkles
				((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
			}
		}
	}

	@SubscribeEvent
	public static void damageToolsExtra(BlockEvent.BreakEvent event) {
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (event.getState().is(BlockTagGenerator.MAZESTONE) || event.getState().is(BlockTagGenerator.CASTLE_BLOCKS)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, event.getPlayer(), EquipmentSlot.MAINHAND);
			}
		}
	}

	@SubscribeEvent
	public static void onMobEffectApplicableEvent(MobEffectEvent.Applicable event) {
		if (event.getApplicationResult() && event.getEffectInstance().is(MobEffects.DIG_SLOWDOWN) && event.getEntity().isHolding(TFItems.POCKET_WATCH.get())) {
			event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
		}
	}
}
