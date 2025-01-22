package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellerLeggingsModel;
import twilightforest.init.TFItems;

public class TravellerArmorItem extends ArmorItem {
	public TravellerArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material, type, properties);
	}


	public static final class ArmorRender implements IClientItemExtensions {
		public static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = switch (slot) {
				case HEAD -> models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_HELMET);
				case CHEST -> {
					ModelPart chestLayer = models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					if (stack.is(TFItems.TRAVELLER_CHESTPLATE) || stack.is(TFItems.TRAVELLER_CHESTPLATE_GLOVES)) {
						chestLayer.getChild("body").skipDraw = false;
					}
					if (stack.is(TFItems.TRAVELLER_GLOVES) || stack.is(TFItems.TRAVELLER_CHESTPLATE_GLOVES)) {
						chestLayer.getChild("left_arm").skipDraw = false;
						chestLayer.getChild("right_arm").skipDraw = false;
					}
					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasPants = stack.is(TFItems.TRAVELLER_LEGGINGS_BELT) || stack.is(TFItems.TRAVELLER_LEGGINGS);
					boolean hasWings = hasPants && stack.getDisplayName().getString().equalsIgnoreCase("[traveller's wings]");
					boolean hasBelt = stack.is(TFItems.TRAVELLER_LEGGINGS_BELT) || stack.is(TFItems.TRAVELLER_BELT);

					TravellerLeggingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellerLeggingsModel.skipWings(leggingsLayer, !hasWings);
					TravellerLeggingsModel.skipPants(leggingsLayer, !hasPants || hasWings);

					yield leggingsLayer;
				}
				case FEET -> models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected slot: " + slot + ": " + stack + ". Please report to https://github.com/TeamTwilight/twilightforest/issues");
			};

			if (slot == EquipmentSlot.LEGS)
				return new TravellerLeggingsModel(root);
			return new TFArmorModel(root);
		}

		@Override
		public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellerLeggingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}
