package twilightforest.asmhooks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
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
	 * {@link twilightforest.asm.transformers.armor.CancelElytraRenderingTransformer}<p/>
	 *
	 * Injection Points:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel, float, float, float, float, float, float)}
	 * {@link net.minecraft.client.renderer.entity.layers.ElytraLayer#shouldRender(ItemStack, LivingEntity)}
	 */
	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			return false;
		}
		return o;
	}

	/**
	 * {@link twilightforest.asm.transformers.armor.FixCapeUnrenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.CapeLayer#render(PoseStack, MultiBufferSource, int, AbstractClientPlayer, float, float, float, float, float, float)}
	 */
	public static boolean fixCapeRendering(boolean o, ItemStack stack) {
		return o && !stack.has(TFDataComponents.EMPERORS_CLOTH);
	}
}
