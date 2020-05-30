package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFSlimeBeetle;
import twilightforest.entity.EntityTFSlimeBeetle;

public class RenderTFSlimeBeetle<T extends EntityTFSlimeBeetle, M extends ModelTFSlimeBeetle<T>> extends LivingRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

	public RenderTFSlimeBeetle(EntityRendererManager manager, M par1ModelBase, float shadowSize) {
		super(manager, par1ModelBase, shadowSize);
		addLayer(new LayerInner(this));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFSlimeBeetle entity) {
		return textureLoc;
	}

	class LayerInner extends LayerRenderer<T, M> {
		private final Model innerModel = new ModelTFSlimeBeetle(true);

		public LayerInner(IEntityRenderer<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(MatrixStack stack, IRenderTypeBuffer buffer, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.enableRescaleNormal();
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//				this.innerModel.setModelAttributes(RenderTFSlimeBeetle.this.getEntityModel());
//				this.innerModel.render(stack, buffer, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				RenderSystem.disableBlend();
				RenderSystem.disableRescaleNormal();
			}
		}
//
//		@Override
//		public boolean shouldCombineTextures() {
//			return true;
//		}
	}
}
