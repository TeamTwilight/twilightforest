package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.HydraModel;
import twilightforest.entity.boss.Hydra;

public class HydraRenderer extends MobRenderer<Hydra, HydraModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraRenderer(EntityRendererProvider.Context manager, HydraModel modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	protected float getFlipDegrees(Hydra entity) {
		return 0F;
	}

	@Override
	protected void setupRotations(Hydra p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
		if (this.isShaking(p_115317_)) {
			p_115320_ += (float)(Math.cos((double)p_115317_.tickCount * 3.25) * Math.PI * 0.4F);
		}

		if (!p_115317_.hasPose(Pose.SLEEPING)) {
			p_115318_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
		}

		if (p_115317_.deathTime > 0) {
			float f = ((float)p_115317_.deathTime + p_115321_ - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			p_115318_.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(p_115317_)));
		} else if (isEntityUpsideDown(p_115317_)) {
			p_115318_.translate(0.0F, 6.0F, 0.0F);
			p_115318_.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(Hydra entity) {
		return textureLoc;
	}
}
