package twilightforest.item.travellers_gear;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.init.*;
import twilightforest.item.travellers_gear.modifiers.*;
import twilightforest.network.ParticlePacket;
import twilightforest.util.TFMathUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TravellersArmorItem extends ArmorItem implements TravellersModifiable {
	public static final double WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4;
	private static final double AUTO_REPAIR_SUNLIGHT_BOOST = 3;
	private static final double AUTO_REPAIR_TWILIGHT_BOOST = AUTO_REPAIR_SUNLIGHT_BOOST / 2;
	private final int insertableModifierSlots;
	@Nullable
	private ItemAttributeModifiers attributeModifiers;
	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots, int durability) {
		super(
			TFArmorMaterials.TRAVELLERS,
			equipmentType,
			properties.component(TFDataComponents.IS_TRAVELLERS_GEAR, Unit.INSTANCE).durability(equipmentType.getDurability(durability))
		);
		this.insertableModifierSlots = insertableModifierSlots;
		attributeModifiers = this.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (attributeModifiers == null)
			return;

		for (ItemAttributeModifiers.Entry modifier : this.getDefaultAttributeModifiers().modifiers()) {
			attributeModifiers = attributeModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
		}
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int insertableModifierSlots) {
		this(equipmentType, properties, insertableModifierSlots, 4);
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.attributeModifiers == null ? super.getDefaultAttributeModifiers() : this.attributeModifiers;
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return !innerModel && entity instanceof LocalPlayer && TFKeyBinds.ZOOM_KEY.isDown() ?
			TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png") :
			super.getArmorTexture(stack, entity, slot, layer, innerModel);
	}

	public static void travellersStealth(Player player, Consumer<Player> invisibilityHandler) {
		ItemStack chestArmor = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
		if (!TravellersModifiers.STEALTH_MODIFIER.isActive(chestArmor))
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
		if (TravellersModifiers.SIDESTEP_MODIFIER.isActive(leggingsStack) && dt.equals(cooldown))
			player.playSound(TFSounds.SIDE_STEP_CHARGED.get(), 1F, player.getVoicePitch());
	}

	public static void travellersWingsControlledFall(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Float multiplier = leggingsStack.get(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER);
		Vec3 deltaMovement = livingEntity.getDeltaMovement();
		if (!TravellersModifiers.CONTROLLED_FALL_MODIFIER.isActive(leggingsStack) || multiplier == null || deltaMovement.y() >= 0 || livingEntity.isFallFlying())
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
			if (probability == null || !TravellersModifiers.AUTO_REPAIR_MODIFIER.isActive(slot))
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
		if (TravellersModifiers.HIGH_JUMP_MODIFIER.isActive(leggingsStack) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2, amplifier, false, false, false));
	}

	public static void travellersVestHaste(LivingEntity livingEntity) {
		ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Integer amplifier = chestStack.get(TFDataComponents.HASTE_AMPLIFIER);
		if (TravellersModifiers.HASTE_MODIFIER.isActive(chestStack) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 2, amplifier, false, false, false));
	}

	public static boolean tryPerformSidestep(Player player, boolean isLeftSidestep) {
		long lastSidestepTime = player.getData(TFDataAttachments.LAST_SIDESTEP_TIME);
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		long currentTime = player.level().getGameTime();
		if (TravellersModifiers.SIDESTEP_MODIFIER.isActive(leggingsStack) && cooldown != null && currentTime - lastSidestepTime > cooldown && !player.isFallFlying() && player.onGround() && !player.isCrouching()) {
			TravellersArmorItem.performSidestep(player, isLeftSidestep);
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

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.HELMET).build())
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F);
	}

	public static Properties chestProperties(Properties properties) {
		ItemAttributeModifiers.Entry swiftSwimModifier = TravellersModifiers.SWIFT_SWIM_MODIFIER.getModifier();
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.CHESTPLATE)
				.add(swiftSwimModifier.attribute(), swiftSwimModifier.modifier(), swiftSwimModifier.slot())
				.build());
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.attributes(defaultArmorProperties(Type.LEGGINGS).build())
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, Unit.INSTANCE)
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BOOTS, Unit.INSTANCE)
			.attributes(defaultArmorProperties(Type.BOOTS)
				.add(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP_DEACTIVATED,  EquipmentSlotGroup.FEET)
				.build());
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		List<BuiltinTravellersComponentModifier> builtinModifiers = TravellersModifiers.findAllBuiltinModifiers(stack);
		builtinModifiers.forEach(modifier -> tooltip.add(Component.translatable("travellers_gear.ability").withStyle(ChatFormatting.GOLD).append(getModifierTooltipComponent(modifier))));
		List<TravellersEntryModifier> entryModifier = TravellersModifiers.findAllEntryModifiers(stack);
		entryModifier.forEach(modifier -> tooltip.add(Component.translatable("travellers_gear.ability").withStyle(ChatFormatting.GOLD).append(getModifierTooltipComponent(modifier))));
		List<InsertableTravellersModifier> insertableModifiers = TravellersModifiers.findAllInsertableModifiers(stack);
		insertableModifiers.forEach(modifier -> tooltip.add(getModifierTooltipComponent(modifier)));
		for (int i = insertableModifiers.size(); i < getModifierSlots(); i++) {
			tooltip.add(Component.translatable("travellers_gear.modifier.empty").withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		return true;
	}

	@Override
	public boolean isPrimaryItemFor(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return false;
	}

	@Override
	public boolean isRepairable(@NotNull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
		return this.material.value().repairIngredient().get().test(repair);
	}

	private Component getModifierTooltipComponent(TravellersModifier modifier) {
		return TooltipStringInterpolator.render(modifier.getTooltipTranslationKey()).withStyle(ChatFormatting.GRAY);
	}

	public static boolean isTravellersArmorAndBroken(ItemStack stack) {
		return stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && stack.getMaxDamage() - 1 <= stack.getDamageValue();
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

	public int getModifierSlots() {
		return insertableModifierSlots;
	}

	public static final class ArmorRender extends TFArmorRenderer {

		public ArmorRender() {
			super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
		}

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity living, @NotNull ItemStack stack, EquipmentSlot slot, @NotNull HumanoidModel<?> model) {
			ModelPart root = switch (slot) {
				case HEAD -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
					boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;

					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
					boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT);

					TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersWingsModel.skipWings(leggingsLayer, !hasWings);

					yield leggingsLayer;
				}
				case FEET -> this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected slot: " + slot + ": " + stack + ". Please report to https://github.com/TeamTwilight/twilightforest/issues");
			};

			if (slot == EquipmentSlot.LEGS)
				return new TravellersWingsModel(root);
			return new TFArmorModel(root);
		}

		@Override
		public void setupModelAnimations(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull EquipmentSlot equipmentSlot, @NotNull Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellersWingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return this == TFItems.TRAVELLERS_GOGGLES.get() || stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
	}
}
