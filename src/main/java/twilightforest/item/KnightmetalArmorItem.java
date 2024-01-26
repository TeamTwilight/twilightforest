package twilightforest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Consumer;

public class KnightmetalArmorItem extends ArmorItem {

	public KnightmetalArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "knightly_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "knightly_1.png";
		}
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ArmorRender.INSTANCE);
	}

	private static final class ArmorRender implements IClientItemExtensions {
		private static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = models.bakeLayer(slot == EquipmentSlot.LEGS ? TFModelLayers.KNIGHTMETAL_ARMOR_INNER : TFModelLayers.KNIGHTMETAL_ARMOR_OUTER);
			return new TFArmorModel(root);
		}
	}
}