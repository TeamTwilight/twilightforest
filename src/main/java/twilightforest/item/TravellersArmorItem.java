package twilightforest.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersLeggingsModel;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.init.TFArmorMaterials;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.network.ParticlePacket;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Consumer;

public class TravellersArmorItem extends ArmorItem {
	public static final double WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4;
	@Nullable
	private ItemAttributeModifiers attributeModifiers;
	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int durability) {
		super(TFArmorMaterials.TRAVELLERS, equipmentType, properties.durability(equipmentType.getDurability(durability)));
		attributeModifiers = this.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (attributeModifiers == null)
			return;

		for (ItemAttributeModifiers.Entry modifier : this.getDefaultAttributeModifiers().modifiers()) {
			attributeModifiers = attributeModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
		}
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties) {
		this(equipmentType, properties, 4);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.attributeModifiers == null ? super.getDefaultAttributeModifiers() : this.attributeModifiers;
	}

	public static void travellersStealth(Player player, Consumer<Player> invisibilityHandler) {
		ItemStack chestArmor = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
		if (!Boolean.TRUE.equals(chestArmor.get(TFDataComponents.STEALTH_CROUCHING_ENABLE)))
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

		ParticlePacket particlePacket = new ParticlePacket();  // we have to create it on client because of limitations of java
		for (int particleNumber = 0; particleNumber < livingEntity.dimensions.width(); particleNumber++) {
			double dx = (level.random.nextDouble() * 2.0 - 1.0) * (double)livingEntity.dimensions.width() / 2D;
			double dz = (level.random.nextDouble() * 2.0 - 1.0) * (double)livingEntity.dimensions.width() / 2D;
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

	public static void travellersPantsControlFall(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Float multiplier = leggingsStack.get(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER);
		Vec3 deltaMovement = livingEntity.getDeltaMovement();
		if (multiplier == null || deltaMovement.y() >= 0)
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


	public static void travellersWingsHighJump(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Integer amplifier = leggingsStack.get(TFDataComponents.HIGH_JUMP_AMPLIFIER);
		if (amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2, amplifier, false, false, false));
	}

	public static void travellersVestHaste(LivingEntity livingEntity) {
		ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Integer amplifier = chestStack.get(TFDataComponents.HASTE_AMPLIFIER);
		if (amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 2, amplifier, false, false, false));
	}

	public static boolean performDoubleJump(Player player) {
		Boolean hasDoubleJump = player.getData(TFDataAttachments.HAS_DOUBLE_JUMP);
		if (Boolean.TRUE.equals(hasDoubleJump) && !player.isFallFlying() && !player.onGround()) {
			player.jumpFromGround();
			player.fallDistance = 0;
			player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, false);
			player.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			return true;
		} else if (player instanceof ServerPlayer serverPlayer) {
			int count = serverPlayer.getData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR);
			int lastCheck = serverPlayer.getData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR_LAST_CHECK);
			int currentTick = serverPlayer.tickCount;
			int difference = currentTick - lastCheck;
			TwilightForestMod.LOGGER.debug("{} double jump validation. Count: {}, Last tick: {}, Current tick: {}, Tick difference: {}", player.getName().getString(), count, lastCheck, currentTick, difference);
			if (difference >= 45 && !player.isFallFlying())
				count = -1;
			serverPlayer.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR_LAST_CHECK, currentTick);
			if (count >= 5) {
				serverPlayer.connection.disconnect(new DisconnectionDetails(Component.translatable("multiplayer.disconnect.flying")));
			}
			serverPlayer.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, count + 1);
			if (count > 1) {
				TwilightForestMod.LOGGER.warn("{} double jumped when they shouldn't have!", player.getName().getString());
				serverPlayer.absMoveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
				serverPlayer.connection.send(new ClientboundPlayerPositionPacket(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot(), Collections.emptySet(), 0));
			}
		}
		return false;
	}

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F)
			.component(TFDataComponents.RED_THREAD_VISION_ENABLE, true);
	}

	public static Properties chestProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, true)
			.component(TFDataComponents.STEALTH_CROUCHING_ENABLE, true)
			.component(TFDataComponents.HASTE_AMPLIFIER, 1)
			.component(TFDataComponents.ARROW_MAGNETISM, true)
			.component(TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.1F)
			.attributes(defaultArmorProperties(Type.CHESTPLATE)
				.add(Attributes.WATER_MOVEMENT_EFFICIENCY, new AttributeModifier(TwilightForestMod.prefix("travellers_gear.vest_fast_swimming"), 1F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
				.build());
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, true);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, true)
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1)
			.component(TFDataComponents.HAS_DOUBLE_JUMP, true)
			.component(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F);
	}

	public static Properties beltProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, true);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.WATER_WALK_ENABLE, true)
			.component(TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F)
			.attributes(defaultArmorProperties(Type.BOOTS)
				.add(Attributes.STEP_HEIGHT, new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
				.build());
	}

	// [VanillaCopy] modified ArmorItem constructor to just return default attribute modifiers
	public static ItemAttributeModifiers.Builder defaultArmorProperties(Type type) {
		int defense = TFArmorMaterials.TRAVELLERS.value().getDefense(type);
		float toughness = TFArmorMaterials.TRAVELLERS.value().toughness();
		ItemAttributeModifiers.Builder defaultArmorModifiers = ItemAttributeModifiers.builder();
		EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		defaultArmorModifiers.add(
			Attributes.ARMOR, new AttributeModifier(resourcelocation, defense, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		defaultArmorModifiers.add(
			Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughness, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
		);
		float knockbackResistance = TFArmorMaterials.TRAVELLERS.value().knockbackResistance();
		if (knockbackResistance > 0.0F) {
			defaultArmorModifiers.add(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(resourcelocation, knockbackResistance, AttributeModifier.Operation.ADD_VALUE),
				equipmentslotgroup
			);
		}
		return defaultArmorModifiers;
	}

	public static final class ArmorRender extends TFArmorRenderer {

		public ArmorRender() {
			super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		}

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			ModelPart root = switch (slot) {
				case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE));
					boolean hasGloves = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_GLOVES));
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;

					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasWings = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_WINGS));
					boolean hasBelt = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_BELT));

					TravellersLeggingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersLeggingsModel.skipWings(leggingsLayer, !hasWings);

					yield leggingsLayer;
				}
				case FEET -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected slot: " + slot + ": " + stack + ". Please report to https://github.com/TeamTwilight/twilightforest/issues");
			};

			if (slot == EquipmentSlot.LEGS)
				return new TravellersLeggingsModel(root);
			return new TFArmorModel(root);
		}

		@Override
		public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellersLeggingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}
