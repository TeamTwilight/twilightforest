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
import twilightforest.client.model.armor.TravellersLeggingsModel;
import twilightforest.init.TFItems;

public class TravellersArmorItem extends ArmorItem {
	public TravellersArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material, type, properties);
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
					boolean hasWings = hasPants && stack.getDisplayName().getString().equalsIgnoreCase("[traveller's wings]");
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
}
