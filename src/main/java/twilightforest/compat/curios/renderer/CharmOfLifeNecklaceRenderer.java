package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer implements ICurioRenderer {

	private final CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		this.necklaceColor = necklaceColor;
	}

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, S state, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
		if (renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
			poseStack.pushPose();
			model.body.translateAndRotate(poseStack);
			poseStack.translate(-0.0D, 0.23D, -0.135D);
			poseStack.rotate(Axis.YP.rotationDegrees(0.0F));
			poseStack.scale(-0.4F, -0.4F, 0.4F);
			ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForLiving(itemStackRenderState, stack, ItemDisplayContext.FIXED, slotContext.entity());
			itemStackRenderState.submit(poseStack, submitNodeCollector, packedLight, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
		ICurioRenderer.setupHumanoidAnimations(this.model, state);
	}
}
