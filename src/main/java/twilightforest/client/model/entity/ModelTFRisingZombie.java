package twilightforest.client.model.entity;


import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelTFRisingZombie extends ModelZombie {
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.pushMatrix();
			{
				GlStateManager.scale(0.75F, 0.75F, 0.75F);
				GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
				this.bipedHead.render(scale);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
				this.bipedBody.render(scale);
				this.bipedRightArm.render(scale);
				this.bipedLeftArm.render(scale);
				this.bipedHeadwear.render(scale);
			}
			GlStateManager.popMatrix();
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			GlStateManager.translate(0F, (80F - Math.min(80F, ageInTicks)) / 80F, 0F);
			GlStateManager.translate(0F, (40F - Math.min(40F, Math.max(0F, ageInTicks - 80F))) / 40F, 0F);
			GlStateManager.pushMatrix();
			{
				final float yOff = 1F;
				GlStateManager.translate(0, yOff, 0);
				GlStateManager.rotate(-120F * (80F - Math.min(80F, ageInTicks)) / 80F, 1F, 0F, 0F);
				GlStateManager.rotate(30F * (40F - Math.min(40F, Math.max(0F, ageInTicks - 80F))) / 40F, 1F, 0F, 0F);
				GlStateManager.translate(0, -yOff, 0);
				this.bipedHead.render(scale);
				this.bipedBody.render(scale);
				this.bipedRightArm.render(scale);
				this.bipedLeftArm.render(scale);
				this.bipedHeadwear.render(scale);
			}
			GlStateManager.popMatrix();
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
		}

		GlStateManager.popMatrix();
	}
}
