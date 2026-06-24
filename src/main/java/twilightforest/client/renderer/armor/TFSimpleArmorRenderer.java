package twilightforest.client.renderer.armor;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
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
	public @NotNull TFArmorModel getHumanoidArmorModel(@NotNull ItemStack itemStack, EquipmentClientInfo.@NotNull LayerType layerType, @NotNull Model original) {
		return itemStack.getEquipmentSlot() == EquipmentSlot.LEGS ?
			CREATE_MODEL_INSTANCE.apply(getModelPart(INNER_ARMOR_MODEL)) :
			CREATE_MODEL_INSTANCE.apply(getModelPart(OUTER_ARMOR_MODEL));
	}
}
