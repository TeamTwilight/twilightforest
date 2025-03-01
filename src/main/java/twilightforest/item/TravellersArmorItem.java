package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersLeggingsModel;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFArmorMaterials;

import javax.annotation.Nullable;

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

	public static void travellersItemTick(Player player) {
		ItemStack chestArmor = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
		if (!Boolean.TRUE.equals(chestArmor.get(TFDataComponents.STEALTH_CROUCHING_ENABLE)))
			return;

		if (player.isCrouching()) {
			if (player instanceof LocalPlayer)
				player.setInvisible(true);

			else if (player instanceof ServerPlayer)
				player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false));
		} else {
			MobEffectInstance invisibilityEffect = player.getEffect(MobEffects.INVISIBILITY);
			if (invisibilityEffect != null && invisibilityEffect.getDuration() < 2)
				player.setInvisible(false);
		}
	}


	public static void waterWalkingSplashEffect(LivingEntity livingEntity) {
		Long lastTickWaterWalking = livingEntity.getData(TFDataAttachments.LAST_TICK_WATER_WALKING);
		Level level = livingEntity.level();
		Vec3 velocity = livingEntity.getDeltaMovement();
		if (lastTickWaterWalking + 1 != level.getGameTime() || velocity.horizontalDistance() < 0.01)
			return;

		// modified [VanillaCopy] of Entity.doWaterSplashEffect()
		for (int particleNumber = 0; particleNumber < 1.0F + livingEntity.dimensions.width(); particleNumber++) {
			double dx = (level.random.nextDouble() * 2.0 - 1.0) * (double)livingEntity.dimensions.width() / 2D;
			double dz = (level.random.nextDouble() * 2.0 - 1.0) * (double)livingEntity.dimensions.width() / 2D;
			level.addParticle(ParticleTypes.SPLASH,
				livingEntity.getX() + dx,
				livingEntity.getY() + WATER_WALKING_MAX_SUBMERGED_HEIGHT,
				livingEntity.getZ() + dz,
				-velocity.x, 0.5, -velocity.z);
		}
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
			.component(TFDataComponents.HIGH_JUMP_AMPLIFIER, 1);
	}

	public static Properties pantsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_PANTS, true)
			.component(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F);
	}


	public static Properties beltProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_BELT, true);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.WATER_WALK_ENABLE, true)
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

	public static final class ArmorRender implements IClientItemExtensions {
		public static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = switch (slot) {
				case HEAD -> models.bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = models.bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE));
					boolean hasGloves = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_GLOVES));
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;

					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = models.bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasPants = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_PANTS));
					boolean hasWings = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_WINGS));
					boolean hasBelt = Boolean.TRUE.equals(stack.get(TFDataComponents.TRAVELLERS_HAS_BELT));

					TravellersLeggingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersLeggingsModel.skipWings(leggingsLayer, !hasWings);
					TravellersLeggingsModel.skipPants(leggingsLayer, !hasPants);

					yield leggingsLayer;
				}
				case FEET -> models.bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
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
