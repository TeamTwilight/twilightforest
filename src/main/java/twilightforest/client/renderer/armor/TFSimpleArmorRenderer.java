package twilightforest.client.renderer.armor;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> CREATE_MODEL_INSTANCE;
	protected final ModelLayerLocation INNER_ARMOR_MODEL;
	protected final ModelLayerLocation OUTER_ARMOR_MODEL;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		super(innerLayerLocation, outerLayerLocation);
		this.INNER_ARMOR_MODEL = innerLayerLocation;
		this.OUTER_ARMOR_MODEL = outerLayerLocation;
		this.CREATE_MODEL_INSTANCE = createModelInstance;
	}

	@Override
	public Model<?> getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, @SuppressWarnings("rawtypes") Model original) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable == null) return super.getHumanoidArmorModel(itemStack, layerType, original);

		EquipmentSlot slot = equippable.slot();
		TFArmorModel model = this.CREATE_MODEL_INSTANCE.apply(this.getModelPart(slot == EquipmentSlot.LEGS ? this.INNER_ARMOR_MODEL : this.OUTER_ARMOR_MODEL));
		model.setSlot(slot);

		return model;
	}
}
