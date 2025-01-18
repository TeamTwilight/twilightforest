package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellerWingsModel;
import twilightforest.init.TFItems;

public class TravellerArmorItem extends ArmorItem {
	public TravellerArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	@Nullable
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		if (stack.is(TFItems.TRAVELLER_WINGS))
			return ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "textures/models/armor/traveller_wings.png");
//		if (stack.is(TFItems.TRAVELLER_WINGS))
//			return ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "textures/armor/traveller_wings.png");
		return null;
	}


	public static final class ArmorRender implements IClientItemExtensions {
		public static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = switch (slot) {
				case HEAD -> models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_HELMET_CHEST_GLOVES);
				case CHEST -> {
					ModelPart chestLayer = models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_HELMET_CHEST_GLOVES);
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
				case LEGS -> models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_WINGS);
				case FEET -> models.bakeLayer(TFModelLayers.TRAVELLER_ARMOR_BOOTS);
				default -> throw new IllegalArgumentException("Unexpected slot: " + slot + ": " + stack + ". Please report to https://github.com/TeamTwilight/twilightforest/issues");
			};

			if (stack.is(TFItems.TRAVELLER_WINGS))
				return new TravellerWingsModel(root);
			return new TFArmorModel(root);
		}

		@Override
		public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
			if (model instanceof TravellerWingsModel wingsModel)
				wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}
