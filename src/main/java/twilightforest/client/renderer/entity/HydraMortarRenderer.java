package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraMortarModel;
import twilightforest.entity.boss.HydraMortar;

public class HydraMortarRenderer extends EntityRenderer<HydraMortar> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydramortar.png");
	private final HydraMortarModel mortarModel;

	public HydraMortarRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.5F;
		mortarModel = new HydraMortarModel(manager.bakeLayer(TFModelLayers.HYDRA_MORTAR));
	}

	@Override
	public void render(HydraMortar mortar, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light) {
		stack.pushPose();
		float blink;
		// [VanillaCopy] TNTRenderer
		if (mortar.fuse - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - (mortar.fuse - partialTicks + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			stack.scale(f1, f1, f1);
		}

		float alpha = (1.0F - (mortar.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;

		VertexConsumer builder = buffers.getBuffer(mortarModel.renderType(textureLoc));
		mortarModel.renderToBuffer(stack, builder, light, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(0.075F, 1.0F, 1.0F, 1.0F));

		if (mortar.fuse / 5 % 2 == 0) {
			builder = buffers.getBuffer(RenderType.entityTranslucent(textureLoc));
			mortarModel.renderToBuffer(stack, builder, light, OverlayTexture.pack(OverlayTexture.u(1), 10), FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
		}

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(HydraMortar entity) {
		return textureLoc;
	}
}
