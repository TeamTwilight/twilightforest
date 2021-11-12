package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.HydraHeadContainer;

public class HydraNeckRenderer extends TFPartRenderer<HydraNeck, HydraNeckModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");


	public HydraNeckRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HydraNeckModel(manager.bakeLayer(TFModelLayers.HYDRA_NECK)));
	}

	@Override
	public void render(HydraNeck entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int light) {
		HydraHeadContainer headCon = HydraHeadRenderer.getHeadObject(entityIn.head);
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
