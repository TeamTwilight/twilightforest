package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CurioHeadRenderer implements ICurioRenderer {

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, S renderState, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
		if (renderLayerParent.getModel() instanceof HeadedModel headModel) {
			poseStack.pushPose();
			headModel.getHead().translateAndRotate(poseStack);
			poseStack.translate(0.0D, -0.25D, 0.0D);
			poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
			poseStack.scale(0.625F, -0.625F, -0.625F);
			ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForLiving(itemStackRenderState, stack, ItemDisplayContext.HEAD, slotContext.entity());
			itemStackRenderState.submit(poseStack, submitNodeCollector, packedLight, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}
}
