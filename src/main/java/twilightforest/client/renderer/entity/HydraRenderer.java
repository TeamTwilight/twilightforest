package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.Hydra;

public class HydraRenderer<T extends Hydra, M extends HierarchicalModel<T>> extends MobRenderer<T, M> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Override
	protected float getFlipDegrees(Hydra entity) {
		return 0.0F;
	}

	@Override
	protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float yaw, float partialTicks, float scale) {
		if (this.isShaking(entity)) {
			yaw += (float) (Math.cos((double) entity.tickCount * 3.25) * Math.PI * 0.4F);
		}

		if (!entity.hasPose(Pose.SLEEPING)) {
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		}

		if (entity.deathTime > 0) {
			float f = ((float) entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			stack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entity)));
		} else if (isEntityUpsideDown(entity)) {
			stack.translate(0.0F, 6.0F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
}
