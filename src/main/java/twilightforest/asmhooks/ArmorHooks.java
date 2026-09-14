package twilightforest.asmhooks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tamaized.beanification.Autowired;
import twilightforest.init.TFDataComponents;
import twilightforest.util.ArmorUtil;

@SuppressWarnings({"JavadocReference", "unused"})
public class ArmorHooks {

	@Autowired
	private static ArmorUtil armorUtil;

	/**
	 * {@link twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#getVisibilityPercent(Entity)}
	 * Targets: {@link net.minecraft.world.entity.LivingEntity.getArmorCoverPercentage()}
	 */
	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}

	/**
	 * {@link twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer#renderArmorPiece(PoseStack, SubmitNodeCollector, ItemStack, EquipmentSlot, int, HumanoidRenderState)}<br/>
	 * Targets: {@code shouldRender(equippable, slot)}.
	 */
	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			return false;
		}
		return o;
	}

	/**
	 * {@link twilightforest.asm.transformers.armor.CancelWingsRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.WingsLayer#submit(PoseStack, SubmitNodeCollector, int, HumanoidRenderState, float, float)}<br/>
	 * Targets: {@code equippable.assetId().isEmpty()};
	 */
	public static boolean cancelWingsRendering(boolean o, ItemStack stack) {
		return o || stack.has(TFDataComponents.EMPERORS_CLOTH);
	}

	/**
	 * {@link twilightforest.asm.transformers.armor.FixCapeUnrenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.CapeLayer#hasLayer(ItemStack, EquipmentClientInfo.LayerType)}<br/>
	 * Targets: IRETURN
	 */
	public static boolean fixCapeRendering(boolean o, ItemStack stack) {
		return o && !stack.has(TFDataComponents.EMPERORS_CLOTH);
	}
}
