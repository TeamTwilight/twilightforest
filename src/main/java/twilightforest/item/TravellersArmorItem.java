package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersLeggingsModel;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFArmorMaterials;
import twilightforest.init.TFItems;

public class TravellersArmorItem extends ArmorItem {
	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties, int durability) {
		super(TFArmorMaterials.TRAVELLERS, equipmentType, properties.durability(equipmentType.getDurability(durability)));
	}

	public TravellersArmorItem(ArmorItem.Type equipmentType, Properties properties) {
		this(equipmentType, properties, 4);
	}

	public static void travellersItemTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer))
			return;

		ItemStack chestArmor = serverPlayer.getInventory().getArmor(EquipmentSlot.CHEST.getIndex());
		if (serverPlayer.isCrouching() && Boolean.TRUE.equals(chestArmor.get(TFDataComponents.STEALTH_CROUCHING_ENABLE))) {
			serverPlayer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false));
		}
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
					if (stack.is(TFItems.TRAVELLERS_CHESTPLATE) || stack.is(TFItems.TRAVELLERS_CHESTPLATE_GLOVES)) {
						chestLayer.getChild("body").skipDraw = false;
					}
					if (stack.is(TFItems.TRAVELLERS_GLOVES) || stack.is(TFItems.TRAVELLERS_CHESTPLATE_GLOVES)) {
						chestLayer.getChild("left_arm").skipDraw = false;
						chestLayer.getChild("right_arm").skipDraw = false;
					}
					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = models.bakeLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasPants = stack.is(TFItems.TRAVELLERS_LEGGINGS_BELT) || stack.is(TFItems.TRAVELLERS_LEGGINGS);
					boolean hasWings = hasPants && stack.getDisplayName().getString().equalsIgnoreCase("[traveller's wings]"); // FIXME: create actual wings item and use AnvilUpdateEvent to get it
					boolean hasBelt = stack.is(TFItems.TRAVELLERS_LEGGINGS_BELT) || stack.is(TFItems.TRAVELLERS_BELT);

					TravellersLeggingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersLeggingsModel.skipWings(leggingsLayer, !hasWings);
					TravellersLeggingsModel.skipPants(leggingsLayer, !hasPants || hasWings);

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

	public static Properties gogglesProperties(Properties properties) {
		return properties.component(TFDataComponents.ZOOM_ABILITY_MODIFIER, 0.3F);
	}

	public static Properties bootsProperties(Properties properties) {
		return properties.attributes(ItemAttributeModifiers.builder().add(Attributes.STEP_HEIGHT, new AttributeModifier(TwilightForestMod.prefix("travellers_gear.boots_high_step"), 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET).build());
	}
}
