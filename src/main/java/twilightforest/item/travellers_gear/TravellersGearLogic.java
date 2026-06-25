package twilightforest.item.travellers_gear;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TravellersWingsStatePacket;
import twilightforest.util.TFMathUtil;

import java.util.Collections;
import java.util.function.Consumer;

public class TravellersGearLogic {

	public static final double WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4;
	private static final double AUTO_REPAIR_SUNLIGHT_BOOST = 3;
	private static final double AUTO_REPAIR_TWILIGHT_BOOST = AUTO_REPAIR_SUNLIGHT_BOOST / 2;

	public static void travellersStealth(Player player, Consumer<Player> invisibilityHandler) {
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEALTH_MODIFIER))
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
			double dx = (level.getRandom().nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			double dz = (level.getRandom().nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			Vec3 particlePos = new Vec3(livingEntity.getX() + dx, livingEntity.getY() + WATER_WALKING_MAX_SUBMERGED_HEIGHT, livingEntity.getZ() + dz);
			Vec3 particleVelocity = new Vec3(-livingEntityVelocity.x, 0.5, -livingEntityVelocity.z);
			if (level.isClientSide()) {
				level.addParticle(ParticleTypes.SPLASH, particlePos.x(), particlePos.y(), particlePos.z(), particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
			} else {
				particlePacket.queueParticle(ParticleTypes.SPLASH, particlePos, particleVelocity);
			}
		}

		if (!level.isClientSide())
			PacketDistributor.sendToPlayersTrackingEntity(livingEntity, particlePacket);
	}

	public static boolean isBelowMaxWaterWalkingSubmergedHeight(LivingEntity livingEntity) {
		double waterHeight = livingEntity.getFluidHeight(FluidTags.WATER);
		return waterHeight < WATER_WALKING_MAX_SUBMERGED_HEIGHT;
	}

	public static void travellersBootsStraightAhead(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = leggingsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);
		AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;
		if (multiplier == null)
			multiplier = 1D;
		boolean hasModifier = TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) && multiplier != 1;
		if (hasModifier == attributeInstance.hasModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION))
			return;
		if (hasModifier) {
			attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		} else {
			attributeInstance.removeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION);
		}

	}

	public static void travellersWingsSidestepCooldownSound(Player player) {
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		if (cooldown == null)
			return;
		TravellersWingsAttachment attachment = player.getData(TFDataAttachments.TRAVELLERS_WINGS);
		long dt = player.level().getGameTime() - attachment.lastSidestepTime;
		if (TravellersModifiersManager.isModifierActive(player, leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && dt > cooldown && attachment.shouldPlaySideStepCooldownSound) {
			player.level().playLocalSound(player.blockPosition(), TFSounds.SIDE_STEP_CHARGED.get(), player.getSoundSource(), 1F, player.getVoicePitch(), false);
			attachment.shouldPlaySideStepCooldownSound = false;
		}
	}

	public static void travellersWingsGradualGlide(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Float multiplier = leggingsStack.get(TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER);
		Vec3 deltaMovement = livingEntity.getDeltaMovement();
		if (!TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER) || multiplier == null || deltaMovement.y() >= 0 || livingEntity.isFallFlying())
			return;

		boolean isGraduallyGliding = !(livingEntity instanceof Player player) || player.getData(TFDataAttachments.IS_GRADUALLY_GLIDING);
		if (!isGraduallyGliding)
			return;

		double newDeltaMovementY = deltaMovement.y() * multiplier;
		livingEntity.setDeltaMovement(
			deltaMovement.x(),
			newDeltaMovementY,  // works similar to minecraft air resistance
			deltaMovement.z()
		);

		livingEntity.fallDistance = (float) (Math.pow(newDeltaMovementY, 2) / 2 / livingEntity.getGravity());  // use mv ^ 2 / 2 / mg = h
	}

	public static void travellersGearAutoRepair(LivingEntity livingEntity) {
		long lastHitTime = livingEntity.getData(TFDataAttachments.LAST_DAMAGE_ARMOR_TIME);
		if (livingEntity.level().getGameTime() - lastHitTime <= 10 * 20)  // 10 seconds
			return;

		for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
			ItemStack stack = livingEntity.getItemBySlot(slot);

			Float probability = stack.get(TFDataComponents.AUTO_REPAIR_PROBABILITY);
			if (probability == null || !TravellersModifiersManager.isModifierActive(livingEntity, stack, TravellersModifiersManager.AUTO_REPAIR_MODIFIER))
				return;
			Level level = livingEntity.level();
			double boostedProbability = getAutoRepairChance(probability, level, livingEntity.blockPosition());

			if (boostedProbability > level.getRandom().nextFloat())
				stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));

		}
	}

	private static double getAutoRepairChance(double baseProb, Level level, BlockPos pos) {
		if (!level.canSeeSky(pos))
			return baseProb;

		double boostFactor;  // 1 tick in boost boostFactor ticks without boost
		if (level.dimensionTypeRegistration().is(TFDimensionData.TWILIGHT_DIM_TYPE))
			boostFactor = AUTO_REPAIR_TWILIGHT_BOOST;
		else if (level.isBrightOutside())
			boostFactor = AUTO_REPAIR_SUNLIGHT_BOOST;
		else
			return baseProb;
		return TFMathUtil.probabilityOfAtLeastOneSuccess(baseProb, boostFactor);
	}

	public static void travellersWingsHighJump(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Integer amplifier = leggingsStack.get(TFDataComponents.HIGH_JUMP_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.HIGH_JUMP_ABILITY) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 2, amplifier, false, false, false));
	}

	public static void travellersVestHaste(LivingEntity livingEntity) {
		ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Integer amplifier = chestStack.get(TFDataComponents.HASTE_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity, chestStack, TravellersModifiersManager.HASTE_MODIFIER) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.HASTE, 2, amplifier, false, false, false));
	}

	public static void travellersBootsUnrestrained(LivingEntity livingEntity) {
		if (TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			livingEntity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
	}

	public static boolean tryPerformSidestep(Player player, boolean isLeftSidestep) {
		TravellersWingsAttachment attachment = player.getData(TFDataAttachments.TRAVELLERS_WINGS);
		long lastSidestepTime = attachment.lastSidestepTime;
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		long currentTime = player.level().getGameTime();
		if (TravellersModifiersManager.isModifierActive(player, leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && cooldown != null && currentTime - lastSidestepTime > cooldown && !player.isFallFlying() && player.onGround() && !player.isCrouching()) {
			TravellersGearLogic.performSidestep(player, isLeftSidestep);
			attachment.lastSidestepTime = currentTime;
			attachment.shouldPlaySideStepCooldownSound = true;
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

		TravellersWingsAttachment attachment = player.getData(TFDataAttachments.TRAVELLERS_WINGS);
		TravellersWingsAttachment.WingState newState = TravellersWingsAttachment.WingState.SIDESTEP;
		attachment.state = newState;
		attachment.sidestepLeft = isLeftSidestep;
		attachment.sidestepTimer = 0;

		if (player.level() instanceof ServerLevel) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new TravellersWingsStatePacket(player.getId(), newState, isLeftSidestep, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
	}

	public static boolean performDoubleJump(Player player) {
		boolean hasDoubleJump = player.getData(TFDataAttachments.HAS_DOUBLE_JUMP);
		if (!hasDoubleJump || player.isFallFlying() || player.onClimbable() || player.onGround() || player.isSwimming() || player.getAbilities().flying || player.isInLiquid() || player.isPassenger())
			return false;
		player.jumpFromGround();
		Vec3 velocity = player.getDeltaMovement();
		double boostVelocity = player.getData(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO).doubleJumpBoostVelocity;
		if (boostVelocity != 0) {
			player.setDeltaMovement(velocity.x(), Math.sqrt(Math.pow(velocity.y(), 2) + Math.pow(boostVelocity, 2)), velocity.z());
			player.getData(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO).doubleJumpBoostVelocity = 0;
		}
		player.resetFallDistance();
		float pitchShift = 0.1F;
		player.playSound(TFSounds.DOUBLE_JUMP.get(), 1.5F, (player.getVoicePitch() - 1) * (1 + pitchShift) + (1 - pitchShift * 0.2F));
		player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, false);
		player.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
		AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
		if (instance != null) // Increase safe fall distance so the player can land up to 2 blocks below their starting height after performing a double jump at peak height without taking fall damage
			instance.addOrUpdateTransientModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);

		if (player.getItemBySlot(EquipmentSlot.LEGS).is(TFItems.TRAVELLERS_WINGS)) {
			TravellersWingsAttachment attachment = player.getData(TFDataAttachments.TRAVELLERS_WINGS);
			attachment.state = TravellersWingsAttachment.WingState.DOUBLE_JUMP;
			attachment.doubleJumpTimer = 0;
		}

		if (player.level() instanceof ServerLevel serverLevel && player.getItemBySlot(EquipmentSlot.LEGS).is(TFItems.TRAVELLERS_WINGS)) {
			ParticlePacket particlePacket = new ParticlePacket();
			Vec3 deltaMovement = player.getDeltaMovement();
			for (int particleNumber = 0; particleNumber < 10; particleNumber++) {
				Vec3 particleVelocity = new Vec3(
					(serverLevel.getRandom().nextDouble() - 0.5),
					serverLevel.getRandom().nextDouble() + 1,
					(serverLevel.getRandom().nextDouble() - 0.5)
				);
				ParticleOptions type = TFParticleType.DOUBLE_JUMP.get();
				Vec3 wingsPosition = player.position().add(Math.sin(Math.toRadians(player.yBodyRot)) / 3, 1.2, -Math.cos(Math.toRadians(player.yBodyRot)) / 3);
				particlePacket.queueParticle(type, wingsPosition, particleVelocity.multiply(0.25, -0.5, 0.25).add(deltaMovement));
			}
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, particlePacket);
			TravellersWingsAttachment attachment = player.getData(TFDataAttachments.TRAVELLERS_WINGS);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new TravellersWingsStatePacket(player.getId(), TravellersWingsAttachment.WingState.DOUBLE_JUMP, attachment.sidestepLeft, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
		return true;
	}

	public static void travellersBootsSlimySolesBounce(LivingEntity livingEntity) {
		SlimySolesAttachment slimySolesAttachment = livingEntity.getData(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO);
		if (slimySolesAttachment.bounceVelocity == 0 || slimySolesAttachment.hasBounced)
			return;
		Vec3 velocity = livingEntity.getDeltaMovement();
		livingEntity.playSound(SoundEvents.SLIME_JUMP, 0.5F, 1F);
		travellersBootsSlimySolesParticles(livingEntity, slimySolesAttachment);
		slimySolesAttachment.hasBounced = true;
		livingEntity.setDeltaMovement(velocity.x(), Math.sqrt(Math.pow(velocity.y(), 2) + Math.pow(slimySolesAttachment.bounceVelocity, 2)), velocity.z());
		slimySolesAttachment.forceBounce = Math.abs(livingEntity.getDeltaMovement().y()) > 0.25;
	}

	public static void travellersBootsSlimySolesParticles(LivingEntity entity, SlimySolesAttachment attachment) {
		if (!(entity.level() instanceof ServerLevel level) || attachment.bounceVelocity <= 0)
			return;

		double particleX = entity.getX();
		double particleY = entity.getY();
		double particleZ = entity.getZ();
		double intensity = Math.min(0.2 + attachment.bounceVelocity, 2.5);
		int count = (int) (40 * intensity);

		if (count > 0) {
			level.sendParticles(
				new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
				particleX, particleY, particleZ,
				count,
				0.0, 0.0, 0.0, 0.15F
			);
		}
	}

	private static void validateMovement(ServerPlayer serverPlayer,
										 DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> validator,
										 DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> lastCheck,
										 String movementType) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null || !server.isDedicatedServer())
			return;
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
			serverPlayer.absSnapTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
				serverPlayer.getYRot(), serverPlayer.getXRot());
			serverPlayer.connection.send(ClientboundPlayerPositionPacket.of(serverPlayer.getId(),
				new PositionMoveRotation(serverPlayer.position(), Vec3.ZERO,
				serverPlayer.getYRot(), serverPlayer.getXRot()), Collections.emptySet()));
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

	public static void determineWingState(LivingEntity livingEntity) {
		TravellersWingsAttachment attachment = livingEntity.getData(TFDataAttachments.TRAVELLERS_WINGS);
		TravellersWingsAttachment.WingState newState = TravellersWingsAttachment.WingState.IDLE;

		boolean isLocked = false;
		if (attachment.state == TravellersWingsAttachment.WingState.DOUBLE_JUMP) {
			attachment.doubleJumpTimer++;
			if (attachment.doubleJumpTimer < TravellersWingsAttachment.DOUBLE_JUMP_DURATION) {
				isLocked = true;
				newState = TravellersWingsAttachment.WingState.DOUBLE_JUMP;
			}
		} else if (attachment.state == TravellersWingsAttachment.WingState.SIDESTEP) {
			attachment.sidestepTimer++;
			if (attachment.sidestepTimer < TravellersWingsAttachment.SIDESTEP_DURATION) {
				isLocked = true;
				newState = attachment.state;
			}
		} else {
			attachment.doubleJumpTimer = 0;
			attachment.sidestepTimer = 0;
		}

		if (!isLocked) {
			if (livingEntity.isPassenger()) {
				newState = TravellersWingsAttachment.WingState.RIDE;
			} else if (livingEntity.isSwimming()) {
				newState = TravellersWingsAttachment.WingState.SWIM;
			} else if (!livingEntity.onGround() && !livingEntity.isInLiquid() && livingEntity.fallDistance < 2.3F && (!(livingEntity instanceof Player p) || !p.getAbilities().flying)) {
				newState = TravellersWingsAttachment.WingState.FALL_SLOW;
			} else if (livingEntity.getDeltaMovement().y < 0 && livingEntity.fallDistance > 2.3F) {
				newState = TravellersWingsAttachment.WingState.FALL_FAST;
			} else if (livingEntity.isSprinting()) {
				newState = TravellersWingsAttachment.WingState.SPRINT;
			} else if (livingEntity.walkAnimation.speed() > 0.1) {
				newState = TravellersWingsAttachment.WingState.WALK;
			}
		}

		if (newState != attachment.state) {
			attachment.state = newState;
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new TravellersWingsStatePacket(livingEntity.getId(), newState, attachment.sidestepLeft, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
	}
}
