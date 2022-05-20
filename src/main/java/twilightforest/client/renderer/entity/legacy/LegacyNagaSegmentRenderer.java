package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.legacy.NagaLegacyModel;
import twilightforest.client.renderer.entity.TFPartRenderer;
import twilightforest.entity.boss.NagaSegment;

public class LegacyNagaSegmentRenderer<T extends NagaSegment> extends TFPartRenderer<T, NagaLegacyModel<T>> {
	private static final ResourceLocation part_TextureLoc = TwilightForestMod.getModelTexture("nagasegment.png");

	public LegacyNagaSegmentRenderer(EntityRendererProvider.Context m) {
		super(m, new NagaLegacyModel<>(m.bakeLayer(TFModelLayers.LEGACY_NAGA_BODY)));
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if(!entityIn.isInvisible()) {
			matrixStackIn.pushPose();

			float yawDiff = entityIn.getYRot() - entityIn.yRotO;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = entityIn.yRotO + yawDiff * partialTicks;

			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(yaw2));
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(entityIn.getXRot()));

			matrixStackIn.scale(2.0F, 2.0F, 2.0F);
			matrixStackIn.translate(0.0D, -1.501F, 0.0D);

			int light = entityRenderDispatcher.getPackedLightCoords(entityIn.getParent(), partialTicks);
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, light);
			matrixStackIn.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return part_TextureLoc;
	}
}
