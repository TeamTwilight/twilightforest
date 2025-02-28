package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersLeggingsModel;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFArmorMaterials;

public class TravellersArmorItem extends ArmorItem {
	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int durability) {
		super(TFArmorMaterials.TRAVELLERS, equipmentType, properties.durability(equipmentType.getDurability(durability)));
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties) {
		this(equipmentType, properties, 4);
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

	public static Properties gogglesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F)
			.component(TFDataComponents.RED_THREAD_VISION_ENABLE, true);
	}

	public static Properties chestProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE, true)
			.component(TFDataComponents.STEALTH_CROUCHING_ENABLE, true);
	}

	public static Properties glovesProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_GLOVES, true);
	}

	public static Properties wingsProperties(Properties properties) {
		return properties
			.component(TFDataComponents.TRAVELLERS_HAS_WINGS, true);
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
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.STEP_HEIGHT, new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
				.build());
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
