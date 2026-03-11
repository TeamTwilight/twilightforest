package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.HydraHeadContainer;
import twilightforest.entity.boss.HydraNeck;

public class HydraNeckRenderer<T extends HydraNeck, M extends ListModel<T>> extends TFPartRenderer<T, M> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraNeckRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		HydraHeadContainer headCon = HydraHeadRenderer.getHeadObject(entity.head);
		if (headCon != null)
			if (entity.isActive()) {
				float yawDiff = entity.getYRot() - entity.yRotO;
				if (yawDiff > 180) {
					yawDiff -= 360;
				} else if (yawDiff < -180) {
					yawDiff += 360;
				}
				float yaw2 = entity.yRotO + yawDiff * partialTicks;

				stack.mulPose(Axis.YN.rotationDegrees(yaw2 + 180));
				super.render(entity, entityYaw, partialTicks, stack, buffer, light);
			}
	}

	@Override
	protected float getFlipDegrees(T entity) {
		return 0.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
}
