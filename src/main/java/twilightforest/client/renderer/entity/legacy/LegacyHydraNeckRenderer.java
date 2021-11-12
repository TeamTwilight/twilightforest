package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.legacy.HydraNeckLegacyModel;
import twilightforest.client.renderer.entity.TFPartRenderer;
import twilightforest.entity.boss.HydraHeadContainer;
import twilightforest.entity.boss.HydraNeck;

public class LegacyHydraNeckRenderer extends TFPartRenderer<HydraNeck, HydraNeckLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public LegacyHydraNeckRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HydraNeckLegacyModel(manager.bakeLayer(TFModelLayers.LEGACY_HYDRA_NECK)));
	}

	@Override
	public void render(HydraNeck entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int light) {
		HydraHeadContainer headCon = LegacyHydraHeadRenderer.getHeadObject(entityIn.head);
		if (headCon != null)
			if (headCon.shouldRenderHead()) {
				float yawDiff = entityIn.getYRot() - entityIn.yRotO;
				if (yawDiff > 180) {
					yawDiff -= 360;
				} else if (yawDiff < -180) {
					yawDiff += 360;
				}
				float yaw2 = entityIn.yRotO + yawDiff * partialTicks;

				matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(yaw2 + 180));
				super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, light);
			}
	}

	@Override
	public ResourceLocation getTextureLocation(HydraNeck entity) {
		return textureLoc;
	}
}
