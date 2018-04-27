package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelTFAdherent extends ModelBiped {

	ModelRenderer leftSleeve;
	ModelRenderer rightSleeve;

	public ModelTFAdherent() {

		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
		this.bipedRightLeg = new ModelRenderer(this, 0, 0);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4F, 0F, -2F, 8, 24, 4);
		this.bipedBody.setRotationPoint(0F, 0F, 0F);

		this.bipedRightArm = new ModelRenderer(this, 0, 16);
		this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
		this.bipedRightArm.setRotationPoint(-5F, 2F, 0F);

		this.bipedLeftArm = new ModelRenderer(this, 0, 16);
		this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
		this.bipedLeftArm.setRotationPoint(5F, 2F, 0F);

		this.leftSleeve = new ModelRenderer(this, 16, 16);
		this.leftSleeve.addBox(-1F, -2F, 2F, 4, 12, 4);
		this.leftSleeve.setRotationPoint(0F, 0F, 0F);

		this.bipedLeftArm.addChild(this.leftSleeve);

		this.rightSleeve = new ModelRenderer(this, 16, 16);
		this.rightSleeve.addBox(-3F, -2F, 2F, 4, 12, 4);
		this.rightSleeve.setRotationPoint(0F, 0F, 0F);

		this.bipedRightArm.addChild(this.rightSleeve);

	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */

	@Override
	public void setRotationAngles(float f, float f1, float f2, float yaw, float pitch, float time, Entity entity) {
		//super.setRotationAngles(f, f1, f2, yaw, pitch, time, entity);

		// rotate head normally
		this.bipedHead.rotateAngleY = yaw / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = pitch / (180F / (float) Math.PI);

		// wave arms more
		this.bipedRightArm.rotateAngleX = 0.0F;//MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F;
		this.bipedLeftArm.rotateAngleX = 0.0F;//MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;


		this.bipedRightArm.rotateAngleZ += MathHelper.cos((f2 + 10F) * 0.133F) * 0.3F + 0.3F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos((f2 + 10F) * 0.133F) * 0.3F + 0.3F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float partialTick) {
		float bounce = par1EntityLiving.ticksExisted + partialTick;

		// this is where we add the floating
		GL11.glTranslatef(0F, -0.125F - MathHelper.sin((bounce) * 0.133F) * 0.1F, 0F);
	}
}
