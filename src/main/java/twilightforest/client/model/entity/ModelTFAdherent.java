package twilightforest.client.model.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFAdherent;

public class ModelTFAdherent<T extends EntityTFAdherent> extends BipedModel<T> {

	RendererModel leftSleeve;
	RendererModel rightSleeve;

	public ModelTFAdherent() {

		this.bipedHeadwear = new RendererModel(this, 0, 0);
		this.bipedLeftLeg = new RendererModel(this, 0, 0);
		this.bipedRightLeg = new RendererModel(this, 0, 0);

		this.bipedHead = new RendererModel(this, 0, 0);
		this.bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);

		this.bipedBody = new RendererModel(this, 32, 0);
		this.bipedBody.addBox(-4F, 0F, -2F, 8, 24, 4);
		this.bipedBody.setRotationPoint(0F, 0F, 0F);

		this.bipedRightArm = new RendererModel(this, 0, 16);
		this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
		this.bipedRightArm.setRotationPoint(-5F, 2F, 0F);

		this.bipedLeftArm = new RendererModel(this, 0, 16);
		this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
		this.bipedLeftArm.setRotationPoint(5F, 2F, 0F);

		this.leftSleeve = new RendererModel(this, 16, 16);
		this.leftSleeve.addBox(-1F, -2F, 2F, 4, 12, 4);
		this.leftSleeve.setRotationPoint(0F, 0F, 0F);

		this.bipedLeftArm.addChild(this.leftSleeve);

		this.rightSleeve = new RendererModel(this, 16, 16);
		this.rightSleeve.addBox(-3F, -2F, 2F, 4, 12, 4);
		this.rightSleeve.setRotationPoint(0F, 0F, 0F);

		this.bipedRightArm.addChild(this.rightSleeve);

	}

	/**
	 * Sets the model's various rotation angles. For bipeds, limbSwing and limbSwingAmount are used for animating the movement of arms
	 * and legs, where limbSwing represents the time(so that arms and legs swing back and forth) and limbSwingAmount represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		//super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

		// rotate head normally
		this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);

		// wave arms more
		this.bipedRightArm.rotateAngleX = 0.0F;//MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedLeftArm.rotateAngleX = 0.0F;//MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;


		this.bipedRightArm.rotateAngleZ += MathHelper.cos((ageInTicks + 10F) * 0.133F) * 0.3F + 0.3F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos((ageInTicks + 10F) * 0.133F) * 0.3F + 0.3F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

    /**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		float bounce = entity.ticksExisted + partialTicks;

		// this is where we add the floating
		GlStateManager.translatef(0F, -0.125F - MathHelper.sin((bounce) * 0.133F) * 0.1F, 0F);
	}
}
