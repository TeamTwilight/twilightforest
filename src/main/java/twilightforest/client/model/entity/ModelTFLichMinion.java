package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import twilightforest.entity.boss.EntityTFLichMinion;

public class ModelTFLichMinion extends ModelZombie {

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		EntityTFLichMinion minion = (EntityTFLichMinion) entity;
		// make strong minions greener
		if (minion.isStrong()) {
			GlStateManager.color(0.25F, 2.0F, 0.25F);
		} else {
			GlStateManager.color(0.5F, 1.0F, 0.5F);
		}
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		EntityTFLichMinion minion = (EntityTFLichMinion) entity;
		// make strong minions bigger FIXME: actually do this?
		if (minion.isStrong()) {
			super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		} else {
			super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
}
