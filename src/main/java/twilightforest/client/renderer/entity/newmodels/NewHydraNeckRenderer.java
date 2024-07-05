package twilightforest.client.renderer.entity.newmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.newmodels.NewHydraNeckModel;
import twilightforest.client.renderer.entity.TFPartRenderer;
import twilightforest.entity.boss.HydraHeadContainer;
import twilightforest.entity.boss.HydraNeck;

public class NewHydraNeckRenderer extends TFPartRenderer<HydraNeck, NewHydraNeckModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");


	public NewHydraNeckRenderer(EntityRendererProvider.Context manager) {
		super(manager, new NewHydraNeckModel(manager.bakeLayer(TFModelLayers.HYDRA_NECK)));
	}

	@Override
	public void render(HydraNeck neck, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		HydraHeadContainer headCon = NewHydraHeadRenderer.getHeadObject(neck.head);
		if (headCon != null)
			if (neck.isActive()) {
				float yawDiff = neck.getYRot() - neck.yRotO;
				if (yawDiff > 180) {
					yawDiff -= 360;
				} else if (yawDiff < -180) {
					yawDiff += 360;
				}
				float yaw2 = neck.yRotO + yawDiff * partialTicks;

				stack.mulPose(Axis.YN.rotationDegrees(yaw2 + 180));
				super.render(neck, entityYaw, partialTicks, stack, buffer, light);
			}
	}

	@Override
	protected float getFlipDegrees(HydraNeck entity) {
		return 0.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(HydraNeck entity) {
		return textureLoc;
	}
}
