package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.NagaSegment;

public class NagaSegmentRenderer<T extends NagaSegment, M extends NagaModel<T>> extends TFPartRenderer<T, M> {
	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("nagasegment.png");

	public NagaSegmentRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		if (!entity.isInvisible()) {
			stack.pushPose();

			float yawDiff = entity.getYRot() - entity.yRotO;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = entity.yRotO + yawDiff * partialTicks;

			stack.mulPose(Axis.YP.rotationDegrees(yaw2));
			stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));

			if (!JappaPackReloadListener.INSTANCE.isJappaPackLoaded()) {
				stack.scale(2.0F, 2.0F, 2.0F);
			}
			stack.translate(0.0D, -1.25F, 0.0D);

			int realLight = this.entityRenderDispatcher.getPackedLightCoords(entity.getParent(), partialTicks);
			super.render(entity, entityYaw, partialTicks, stack, buffer, realLight);
			stack.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
}
