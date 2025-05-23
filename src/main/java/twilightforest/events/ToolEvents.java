package twilightforest.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.tags.TFBlockTags;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFItems;
import twilightforest.item.EnderBowItem;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.MinotaurAxeItem;
import twilightforest.loot.TFLootTables;

import java.util.List;

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
		if (event.getState().is(TFBlockTags.MAZESTONE) || event.getState().is(TFBlockTags.CASTLE_BLOCKS)) {
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

	@SubscribeEvent
	public static void onItemFishedEvent(ItemFishedEvent event) {
		FishingHook hook = event.getHookEntity();
		if (hook.getPlayerOwner() instanceof ServerPlayer player && player.serverLevel().getBiome(hook.blockPosition()).is(TFBiomes.SPOOKY_FOREST)) {
			ServerLevel level = player.serverLevel();
			ItemStack stack = checkHandsForRod(player);

			LootParams.Builder builder = (new LootParams.Builder(level))
				.withParameter(LootContextParams.ORIGIN, hook.position())
				.withParameter(LootContextParams.TOOL, stack)
				.withParameter(LootContextParams.ATTACKING_ENTITY, player)
				.withParameter(LootContextParams.THIS_ENTITY, hook)
				.withLuck((float) hook.luck + player.getLuck());
			LootTable loottable = level.getServer().reloadableRegistries().getLootTable(TFLootTables.SPOOKY_FOREST_FISHING);
			List<ItemStack> list = loottable.getRandomItems(builder.create(LootContextParamSets.FISHING));
			CriteriaTriggers.FISHING_ROD_HOOKED.trigger(player, stack, hook, list);

            for (ItemStack itemstack : list) {
                ItemEntity itementity = new ItemEntity(level, hook.getX(), hook.getY(), hook.getZ(), itemstack);
                double d0 = player.getX() - hook.getX();
                double d1 = player.getY() - hook.getY();
                double d2 = player.getZ() - hook.getZ();
                itementity.setDeltaMovement(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
				level.addFreshEntity(itementity);
                player.level().addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5, hook.getRandom().nextInt(6) + 1));
                if (itemstack.is(ItemTags.FISHES)) {
                    player.awardStat(Stats.FISH_CAUGHT, 1);
                }
            }
			event.setCanceled(true);
		}
	}

	private static ItemStack checkHandsForRod(Player player) {
		if (player.getMainHandItem().canPerformAction(ItemAbilities.FISHING_ROD_CAST)) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().canPerformAction(ItemAbilities.FISHING_ROD_CAST)) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}
}
