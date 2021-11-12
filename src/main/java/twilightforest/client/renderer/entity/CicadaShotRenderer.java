package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.entity.projectile.CicadaShot;

public class CicadaShotRenderer extends EntityRenderer<CicadaShot> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cicada-model.png");
	private final CicadaModel cicadaModel;

	public CicadaShotRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.25F;

		this.cicadaModel = new CicadaModel(manager.bakeLayer(TFModelLayers.CICADA));
	}

	@Override
	public void render(CicadaShot entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.translate(0.0, 0.5, 0.0);
		stack.scale(-1f, -1f, -1f);

		stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 180.0F));
		stack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

		VertexConsumer builder = buffer.getBuffer(this.cicadaModel.renderType(textureLoc));
		this.cicadaModel.renderToBuffer(stack, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(CicadaShot entity) {
		return textureLoc;
	}
}
