package twilightforest.client.renderer.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

	// getHumanoidArmorModel removed from IClientItemExtensions in 26.1.2.
	// The model is still fetched from the layer location via getModelPart().
	public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity living, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> model) {
		return slot == EquipmentSlot.LEGS ?
			CREATE_MODEL_INSTANCE.apply(getModelPart(INNER_ARMOR_MODEL)) :
			CREATE_MODEL_INSTANCE.apply(getModelPart(OUTER_ARMOR_MODEL));
	}
}
