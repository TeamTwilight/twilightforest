package twilightforest.item.travellers_gear;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TwilightForestMod;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.network.ParticlePacket;
import twilightforest.util.TFMathUtil;

import java.util.Collections;
import java.util.function.Consumer;

public class TravellersGearLogic {

	public static final double WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4;
	private static final double AUTO_REPAIR_SUNLIGHT_BOOST = 3;
	private static final double AUTO_REPAIR_TWILIGHT_BOOST = AUTO_REPAIR_SUNLIGHT_BOOST / 2;

	public static void travellersStealth(Player player, Consumer<Player> invisibilityHandler) {
		ItemStack chestArmor = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
		if (!TravellersModifiersManager.isModifierActive(player.registryAccess(), chestArmor, TravellersModifiersManager.STEALTH_MODIFIER))
			return;

		if (player.isCrouching()) {
			invisibilityHandler.accept(player);
		} else {
			MobEffectInstance invisibilityEffect = player.getEffect(MobEffects.INVISIBILITY);
			if (invisibilityEffect != null && invisibilityEffect.getDuration() < 2)
				player.setInvisible(false);
		}
	}

	public static void waterWalkingSplashEffect(LivingEntity livingEntity) {
		Long lastTickWaterWalking = livingEntity.getData(TFDataAttachments.LAST_TICK_WATER_WALKING);
		Level level = livingEntity.level();
		Vec3 livingEntityVelocity = livingEntity.getKnownMovement();
		if (lastTickWaterWalking + 1 == level.getGameTime() || livingEntityVelocity.horizontalDistance() < 0.01)
			return;

		livingEntity.setData(TFDataAttachments.LAST_TICK_WATER_WALKING, livingEntity.level().getGameTime());

		ParticlePacket particlePacket = new ParticlePacket();  // we have to create it on client to avoid networking delays
		for (int particleNumber = 0; particleNumber < livingEntity.dimensions.width(); particleNumber++) {
			double dx = (level.random.nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			double dz = (level.random.nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			Vec3 particlePos = new Vec3(livingEntity.getX() + dx, livingEntity.getY() + WATER_WALKING_MAX_SUBMERGED_HEIGHT, livingEntity.getZ() + dz);
			Vec3 particleVelocity = new Vec3(-livingEntityVelocity.x, 0.5, -livingEntityVelocity.z);
			if (level.isClientSide()) {
				level.addParticle(ParticleTypes.SPLASH, false, particlePos.x(), particlePos.y(), particlePos.z(), particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
			} else {
				particlePacket.queueParticle(ParticleTypes.SPLASH, false, particlePos, particleVelocity);
			}
		}

		if (!level.isClientSide())
			PacketDistributor.sendToPlayersTrackingEntity(livingEntity, particlePacket);
	}

	public static void travellersBootsForwardBoost(LivingEntity livingEntity) {
		if (livingEntity instanceof Player)
			return;
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = leggingsStack.get(TFDataComponents.FORWARD_BOOST_MULTIPLIER);
		AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;
		if (multiplier == null)
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.FORWARD_BOOTS_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}

	public static void travellersWingsSidestepCooldownSound(Player player) {
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		Long dt = player.level().getGameTime() - player.getData(TFDataAttachments.LAST_SIDESTEP_TIME);
		if (TravellersModifiersManager.isModifierActive(player.registryAccess(), leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && dt.equals(cooldown))
			player.playSound(TFSounds.SIDE_STEP_CHARGED.get(), 1F, player.getVoicePitch());
	}

	public static void travellersWingsControlledFall(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Float multiplier = leggingsStack.get(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER);
		Vec3 deltaMovement = livingEntity.getDeltaMovement();
		if (!TravellersModifiersManager.isModifierActive(livingEntity.registryAccess(), leggingsStack, TravellersModifiersManager.CONTROLLED_FALL_MODIFIER) || multiplier == null || deltaMovement.y() >= 0 || livingEntity.isFallFlying())
			return;

		if (livingEntity.isShiftKeyDown())
			multiplier = 1 - (1 - multiplier) / 3F;

		double newDeltaMovementY = deltaMovement.y() * multiplier;
		livingEntity.setDeltaMovement(
			deltaMovement.x(),
			newDeltaMovementY,  // works similar to minecraft air resistance
			deltaMovement.z()
		);

		livingEntity.fallDistance = (float) (Math.pow(newDeltaMovementY, 2) / 2 / livingEntity.getGravity());  // use mv ^ 2 / 2 / mg = h
	}

	public static void travellersGearAutoRepair(LivingEntity livingEntity) {
		livingEntity.getArmorSlots().forEach(slot -> {
			Float probability = slot.get(TFDataComponents.AUTO_REPAIR_PROBABILITY);
			if (probability == null || !TravellersModifiersManager.isModifierActive(livingEntity.registryAccess(), slot, TravellersModifiersManager.AUTO_REPAIR_MODIFIER))
				return;
			Level level = livingEntity.level();
			double boostedProbability = getAutoRepairChance(probability, level, livingEntity.blockPosition());

			if (boostedProbability > level.random.nextFloat())
				slot.setDamageValue(Math.max(slot.getDamageValue() - 1, 0));
		});
	}

	private static double getAutoRepairChance(double baseProb, Level level, BlockPos pos) {
		if (!level.canSeeSky(pos))
			return baseProb;

		double boostFactor;  // 1 tick in boost ≈ boostFactor ticks without boost
		if (level.dimensionTypeRegistration().is(TFDimensionData.TWILIGHT_DIM_TYPE))
			boostFactor = AUTO_REPAIR_TWILIGHT_BOOST;
		else if (level.isDay())
			boostFactor = AUTO_REPAIR_SUNLIGHT_BOOST;
		else
			return baseProb;
		return TFMathUtil.probabilityOfAtLeastOneSuccess(baseProb, boostFactor);
	}

	public static void travellersWingsHighJump(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Integer amplifier = leggingsStack.get(TFDataComponents.HIGH_JUMP_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity.registryAccess(), leggingsStack, TravellersModifiersManager.HIGH_JUMP_ABILITY) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2, amplifier, false, false, false));
	}

	public static void travellersVestHaste(LivingEntity livingEntity) {
		ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Integer amplifier = chestStack.get(TFDataComponents.HASTE_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity.registryAccess(), chestStack, TravellersModifiersManager.HASTE_MODIFIER) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 2, amplifier, false, false, false));
	}

	public static boolean tryPerformSidestep(Player player, boolean isLeftSidestep) {
		long lastSidestepTime = player.getData(TFDataAttachments.LAST_SIDESTEP_TIME);
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		long currentTime = player.level().getGameTime();
		if (TravellersModifiersManager.isModifierActive(player.registryAccess(), leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && cooldown != null && currentTime - lastSidestepTime > cooldown && !player.isFallFlying() && player.onGround() && !player.isCrouching()) {
			TravellersGearLogic.performSidestep(player, isLeftSidestep);
			player.setData(TFDataAttachments.LAST_SIDESTEP_TIME, currentTime);
			return true;
		}
		return false;
	}

	public static void performSidestep(Player player, boolean isLeftSidestep) {
		float angle = player.getYRot();
		double rot = isLeftSidestep ? -Math.PI / 2 : Math.PI / 2;
		Vec3 dashDirection = new Vec3(-Math.sin(Math.toRadians(angle) + rot), 0, Math.cos(Math.toRadians(angle) + rot));
		player.push(dashDirection.scale(1.6));  // 5 blocks
		player.playSound(TFSounds.SIDE_STEP.get(), 1.0F, player.getVoicePitch());
	}

	public static boolean performDoubleJump(Player player) {
		boolean hasDoubleJump = player.getData(TFDataAttachments.HAS_DOUBLE_JUMP);
		if (hasDoubleJump && !player.isFallFlying() && !player.onGround()) {
			player.jumpFromGround();
			player.fallDistance = 0;
			player.playSound(TFSounds.DOUBLE_JUMP.get(), 1.5F, player.getVoicePitch());
			player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, false);
			player.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			return true;
		}
		return false;
	}

	private static void validateMovement(ServerPlayer serverPlayer,
										 DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> validator,
										 DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> lastCheck,
										 String movementType) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null && server.isDedicatedServer()) {
			int count = serverPlayer.getData(validator);
			int lastTick = serverPlayer.getData(lastCheck);
			int currentTick = serverPlayer.tickCount;
			int diff = currentTick - lastTick;
			TwilightForestMod.LOGGER.debug("{} {} check: count={}, lastTick={}, currentTick={}, diff={}",
				serverPlayer.getName().getString(), movementType, count, lastTick, currentTick, diff);

			if (diff >= 45 && !serverPlayer.isFallFlying()) {
				count = -1;
			}

			serverPlayer.setData(lastCheck, currentTick);

			if (count >= 5) {
				serverPlayer.connection.disconnect(new DisconnectionDetails(Component.translatable("multiplayer.disconnect.flying")));
				return;
			}

			serverPlayer.setData(validator, count + 1);

			if (count > 1) {
				TwilightForestMod.LOGGER.warn("{} illegal {}", serverPlayer.getName().getString(), movementType);
				serverPlayer.absMoveTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
					serverPlayer.getYRot(), serverPlayer.getXRot());
				serverPlayer.connection.send(new ClientboundPlayerPositionPacket(
					serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
					serverPlayer.getYRot(), serverPlayer.getXRot(), Collections.emptySet(), 0));
			}
		}
	}

	public static void handleSidestepAbuse(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			validateMovement(serverPlayer,
				TFDataAttachments.SIDESTEP_VALIDATOR,
				TFDataAttachments.SIDESTEP_VALIDATOR_LAST_CHECK,
				"sidestep");
		}
	}

	public static void handleDoubleJumpAbuse(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			validateMovement(serverPlayer,
				TFDataAttachments.DOUBLE_JUMP_VALIDATOR,
				TFDataAttachments.DOUBLE_JUMP_VALIDATOR_LAST_CHECK,
				"double jump");
		}
	}
}
