package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CharmOfKeepingRenderer implements ICurioRenderer {
	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext, PoseStack ms, SubmitNodeCollector collector, int packedLight, S renderState, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
		if (renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
			ms.pushPose();
			model.rightLeg.translateAndRotate(ms);
			ms.translate(-0.0D, 0.15D, -0.15D);
			ms.mulPose(Axis.YP.rotationDegrees(0.0F));
			ms.scale(0.3F, -0.3F, -0.3F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemModelResolver());
			renderer.renderItem(slotContext.entity(), stack, ItemDisplayContext.FIXED, ms, collector, packedLight);
			ms.popPose();
		}
	}
}
