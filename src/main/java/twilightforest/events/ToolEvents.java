package twilightforest.events;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFItems;
import twilightforest.item.EnderBowItem;
import twilightforest.item.GiantItem;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.MinotaurAxeItem;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	public static InteractionHand INTERACTION_HAND;

	@SubscribeEvent
	public static void onEnderBowHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
				&& evt.getRayTraceResult() instanceof EntityHitResult result
				&& result.getEntity() instanceof LivingEntity living
				&& arrow.getOwner() != result.getEntity()) {

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
	public static void fieryToolSetFire(LivingAttackEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE)) && !event.getEntity().fireImmune()) {
			event.getEntity().setSecondsOnFire(1);
		}
	}

	@SubscribeEvent
	public static void onKnightmetalToolDamage(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();

		if (!target.level().isClientSide() && event.getSource().getDirectEntity() instanceof LivingEntity living) {
			ItemStack weapon = living.getMainHandItem();

			if (!weapon.isEmpty()) {
				if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE) || weapon.is(TFItems.KNIGHTMETAL_SWORD))) {
					if (target.getArmorCoverPercentage() > 0) {
						int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
						event.setAmount(event.getAmount() + moreBonus);
					} else {
						event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
					}
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE)) {
					event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onMinotaurAxeCharge(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();
		Entity source = event.getSource().getDirectEntity();
		if (!target.level().isClientSide() && source instanceof LivingEntity living && source.isSprinting() && (event.getSource().getMsgId().equals("player") || event.getSource().getMsgId().equals("mob"))) {
			ItemStack weapon = living.getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				event.setAmount(event.getAmount() + MINOTAUR_AXE_BONUS_DAMAGE);
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
				stack.hurtAndBreak(16, event.getPlayer(), (user) -> user.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			}
		}
	}

	@SubscribeEvent
	public static void giantToolEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
		checkEntityTooFar(event, event.getTarget(), event.getEntity(), event.getHand());
	}

	@SubscribeEvent
	public static void giantToolEntityInteract(PlayerInteractEvent.EntityInteract event) {
		checkEntityTooFar(event, event.getTarget(), event.getEntity(), event.getHand());
	}

	@SubscribeEvent
	public static void giantToolBlockInteract(PlayerInteractEvent.RightClickBlock event) {
		checkBlockTooFar(event, event.getEntity(), event.getHand());
	}

	@SubscribeEvent
	public static void giantToolItemInteract(PlayerInteractEvent.RightClickItem event) {
		INTERACTION_HAND = event.getHand();
	}

	private static void checkEntityTooFar(PlayerInteractEvent event, Entity target, Player player, InteractionHand hand) {
		if (event instanceof ICancellableEvent cancellable && !cancellable.isCanceled()) {
			ItemStack heldStack = player.getItemInHand(hand);
			if (hasGiantItemInOneHand(player) && !(heldStack.getItem() instanceof GiantItem) && hand == InteractionHand.OFF_HAND) {
				UUID uuidForOppositeHand = GiantItem.GIANT_RANGE_MODIFIER;
				AttributeInstance attackRange = player.getAttribute(NeoForgeMod.ENTITY_REACH.value());
				if (attackRange != null) {
					AttributeModifier giantModifier = attackRange.getModifier(uuidForOppositeHand);
					if (giantModifier != null) {
						attackRange.removeModifier(giantModifier.getId());
						double range = player.getAttributeValue(NeoForgeMod.ENTITY_REACH.value());
						double trueReach = range == 0 ? 0 : range + (player.isCreative() ? 3 : 0); // Copied from IForgePlayer#getAttackRange().
						boolean tooFar = !player.isCloseEnough(target, trueReach);
						attackRange.addTransientModifier(giantModifier);
						cancellable.setCanceled(tooFar);
					}
				}
			}
		}
	}

	private static void checkBlockTooFar(PlayerInteractEvent event, Player player, InteractionHand hand) {
		if (event instanceof ICancellableEvent cancellable && !cancellable.isCanceled()) {
			ItemStack heldStack = player.getItemInHand(hand);
			if (hasGiantItemInOneHand(player) && !(heldStack.getItem() instanceof GiantItem) && hand == InteractionHand.OFF_HAND) {
				UUID uuidForOppositeHand = GiantItem.GIANT_REACH_MODIFIER;
				AttributeInstance reachDistance = player.getAttribute(NeoForgeMod.BLOCK_REACH.value());
				if (reachDistance != null) {
					AttributeModifier giantModifier = reachDistance.getModifier(uuidForOppositeHand);
					if (giantModifier != null) {
						reachDistance.removeModifier(giantModifier.getId());
						double reach = player.getAttributeValue(NeoForgeMod.BLOCK_REACH.value());
						double trueReach = reach == 0 ? 0 : reach + (player.isCreative() ? 0.5 : 0); // Copied from IForgePlayer#getReachDistance().
						boolean tooFar = player.pick(trueReach, 0.0F, false).getType() != HitResult.Type.BLOCK;
						reachDistance.addTransientModifier(giantModifier);
						cancellable.setCanceled(tooFar);
					}
				}
			}
		}
	}

	public static boolean hasGiantItemInOneHand(Player player) {
		ItemStack mainHandStack = player.getMainHandItem();
		ItemStack offHandStack = player.getOffhandItem();
		return (mainHandStack.getItem() instanceof GiantItem && !(offHandStack.getItem() instanceof GiantItem));
	}
}
