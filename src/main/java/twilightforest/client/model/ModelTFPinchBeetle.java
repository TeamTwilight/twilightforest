// Date: 11/5/2012 7:35:56 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX
package twilightforest.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFPinchBeetle;

public class ModelTFPinchBeetle extends ModelBase {
	//fields
	ModelRenderer thorax;
	ModelRenderer head;
	ModelRenderer connector2;
	ModelRenderer RearEnd;
	ModelRenderer Leg6;
	ModelRenderer Leg4;
	ModelRenderer Leg2;
	ModelRenderer Leg5;
	ModelRenderer Leg3;
	ModelRenderer Leg1;
	ModelRenderer connector1;
	ModelRenderer jaw1a;
	ModelRenderer jaw1b;
	ModelRenderer jaw2a;
	ModelRenderer jaw2b;
	ModelRenderer antenna1;
	ModelRenderer antenna2;
	ModelRenderer eye1;
	ModelRenderer eye2;
	ModelRenderer tooth1a;
	ModelRenderer tooth1b;
	ModelRenderer tooth1c;
	ModelRenderer tooth2a;
	ModelRenderer tooth2b;
	ModelRenderer tooth2c;

	public ModelTFPinchBeetle() {
		textureWidth = 64;
		textureHeight = 32;

		thorax = new ModelRenderer(this, 0, 22);
		thorax.addBox(-4.5F, -4F, 0F, 9, 8, 2);
		thorax.setRotationPoint(0F, 18F, -4.5F);

		connector1 = new ModelRenderer(this, 0, 12);
		connector1.addBox(-3F, -3F, 0F, 6, 6, 1);
		connector1.setRotationPoint(0F, 18F, -3F);

		connector2 = new ModelRenderer(this, 0, 12);
		connector2.addBox(-3F, -3F, -1F, 6, 6, 1);
		connector2.setRotationPoint(0F, 18F, -4F);

		RearEnd = new ModelRenderer(this, 28, 14);
		RearEnd.addBox(-5F, -9F, -4F, 10, 10, 8);
		RearEnd.setRotationPoint(0F, 18F, 7F);
		setRotation(RearEnd, 1.570796F, 0F, 0F);

		Leg6 = new ModelRenderer(this, 40, 0);
		Leg6.addBox(-1F, -1F, -1F, 10, 2, 2);
		Leg6.setRotationPoint(4F, 21F, -4F);
		setRotation(Leg6, 0F, 0.2792527F, 0.3490659F);

		Leg5 = new ModelRenderer(this, 40, 0);
		Leg5.mirror = true;
		Leg5.addBox(-9F, -1F, -1F, 10, 2, 2);
		Leg5.setRotationPoint(-4F, 21F, -4F);
		setRotation(Leg5, 0F, -0.2792527F, -0.3490659F);

		Leg4 = new ModelRenderer(this, 40, 0);
		Leg4.addBox(-1F, -1F, -1F, 10, 2, 2);
		Leg4.setRotationPoint(4F, 21F, -1F);
		setRotation(Leg4, 0F, -0.2792527F, 0.3490659F);

		Leg2 = new ModelRenderer(this, 40, 0);
		Leg2.addBox(-1F, -1F, -1F, 10, 2, 2);
		Leg2.setRotationPoint(4F, 21F, 4F);
		setRotation(Leg2, 0F, -0.6981317F, 0.3490659F);

		Leg3 = new ModelRenderer(this, 40, 0);
		Leg3.mirror = true;
		Leg3.addBox(-9F, -1F, -1F, 10, 2, 2);
		Leg3.setRotationPoint(-4F, 21F, -1F);
		setRotation(Leg3, 0F, 0.2792527F, -0.3490659F);

		Leg1 = new ModelRenderer(this, 40, 0);
		Leg1.mirror = true;
		Leg1.addBox(-9F, -1F, -1F, 10, 2, 2);
		Leg1.setRotationPoint(-4F, 21F, 4F);
		Leg1.setTextureSize(64, 32);
		setRotation(Leg1, 0F, 0.6981317F, -0.3490659F);

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -4F, -6F, 8, 6, 6);
		head.setRotationPoint(0F, 19F, -5F);

		jaw1a = new ModelRenderer(this, 40, 6);
		jaw1a.addBox(-1F, -1F, -1.5F, 8, 2, 3);
		jaw1a.setRotationPoint(-3F, 1F, -6F);
		setRotation(jaw1a, 0F, 2.6354471F, 0F);

		jaw1b = new ModelRenderer(this, 40, 10);
		jaw1b.addBox(-1F, -1F, -1F, 10, 2, 2);
		jaw1b.setRotationPoint(7F, 0F, 0F);
		setRotation(jaw1b, 0F, -1.047197F, 0F);

		jaw2a = new ModelRenderer(this, 40, 6);
		jaw2a.addBox(-1F, -1F, -1.5F, 8, 2, 3);
		jaw2a.setRotationPoint(3F, 1F, -6F);
		setRotation(jaw2a, 0F, 0.5410520F, 0F);

		jaw2b = new ModelRenderer(this, 40, 10);
		jaw2b.addBox(-1F, -1F, -1F, 10, 2, 2);
		jaw2b.setRotationPoint(7F, 0F, 0F);
		setRotation(jaw2b, 0F, 1.047197F, 0F);

		antenna1 = new ModelRenderer(this, 42, 4);
		antenna1.addBox(0F, -0.5F, -0.5F, 10, 1, 1);
		antenna1.setRotationPoint(1F, -3F, -5F);
		setRotation(antenna1, 0F, 1.047198F, -0.296706F);

		antenna2 = new ModelRenderer(this, 42, 4);
		antenna2.addBox(0F, -0.5F, -0.5F, 10, 1, 1);
		antenna2.setRotationPoint(-1F, -3F, -5F);
		setRotation(antenna2, 0F, 2.094395F, 0.296706F);

		eye1 = new ModelRenderer(this, 15, 12);
		eye1.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		eye1.setRotationPoint(-3F, -2F, -5F);

		eye2 = new ModelRenderer(this, 15, 12);
		eye2.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		eye2.setRotationPoint(3F, -2F, -5F);

		tooth1a = new ModelRenderer(this, 0, 0);
		tooth1a.addBox(0F, -0.5F, -0F, 2, 1, 1);
		tooth1a.setRotationPoint(9F, 0F, 0F);
		setRotation(tooth1a, 0F, -0.5235987F, 0);

		tooth1b = new ModelRenderer(this, 0, 0);
		tooth1b.addBox(-2.5F, -0.5F, -0F, 2, 1, 1);
		tooth1b.setRotationPoint(6F, 0F, 0F);
		setRotation(tooth1b, 0F, 1.5707963F, 0);

		tooth1c = new ModelRenderer(this, 0, 0);
		tooth1c.addBox(-2.5F, -0.5F, -0F, 2, 1, 1);
		tooth1c.setRotationPoint(3F, 0F, 0F);
		setRotation(tooth1c, 0F, 1.5707963F, 0);

		tooth2a = new ModelRenderer(this, 0, 0);
		tooth2a.addBox(0F, -0.5F, -1F, 2, 1, 1);
		tooth2a.setRotationPoint(9F, 0F, 0F);
		setRotation(tooth2a, 0F, 0.5235987F, 0);

		tooth2b = new ModelRenderer(this, 0, 0);
		tooth2b.addBox(-2.5F, -0.5F, -1F, 2, 1, 1);
		tooth2b.setRotationPoint(6F, 0F, 0F);
		setRotation(tooth2b, 0F, -1.5707963F, 0);

		tooth2c = new ModelRenderer(this, 0, 0);
		tooth2c.addBox(-2.5F, -0.5F, -1F, 2, 1, 1);
		tooth2c.setRotationPoint(3F, 0F, 0F);
		setRotation(tooth2c, 0F, -1.5707963F, 0);

		head.addChild(jaw1a);
		jaw1a.addChild(jaw1b);
		jaw1b.addChild(tooth1a);
		jaw1b.addChild(tooth1b);
		jaw1b.addChild(tooth1c);
		jaw2b.addChild(tooth2a);
		jaw2b.addChild(tooth2b);
		jaw2b.addChild(tooth2c);
		head.addChild(jaw2a);
		jaw2a.addChild(jaw2b);
		head.addChild(antenna1);
		head.addChild(antenna2);
		head.addChild(eye1);
		head.addChild(eye2);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		thorax.render(f5);
		head.render(f5);
		connector2.render(f5);
		RearEnd.render(f5);
		Leg6.render(f5);
		Leg4.render(f5);
		Leg2.render(f5);
		Leg5.render(f5);
		Leg3.render(f5);
		Leg1.render(f5);
		connector1.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		this.head.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float) Math.PI);

		float legZ = ((float) Math.PI / 11F);
		this.Leg1.rotateAngleZ = -legZ;
		this.Leg2.rotateAngleZ = legZ;
		this.Leg3.rotateAngleZ = -legZ * 0.74F;
		this.Leg4.rotateAngleZ = legZ * 0.74F;
		this.Leg5.rotateAngleZ = -legZ;
		this.Leg6.rotateAngleZ = legZ;

		float var9 = -0.0F;
		float var10 = 0.3926991F;
		this.Leg1.rotateAngleY = var10 * 2.0F + var9;
		this.Leg2.rotateAngleY = -var10 * 2.0F - var9;
		this.Leg3.rotateAngleY = var10 * 1.0F + var9;
		this.Leg4.rotateAngleY = -var10 * 1.0F - var9;
		this.Leg5.rotateAngleY = -var10 * 2.0F + var9;
		this.Leg6.rotateAngleY = var10 * 2.0F - var9;

		float var11 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
		float var12 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * par2;
		float var14 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * par2;

		float var15 = Math.abs(MathHelper.sin(par1 * 0.6662F + 0.0F) * 0.4F) * par2;
		float var16 = Math.abs(MathHelper.sin(par1 * 0.6662F + (float) Math.PI) * 0.4F) * par2;
		float var18 = Math.abs(MathHelper.sin(par1 * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * par2;

		this.Leg1.rotateAngleY += var11;
		this.Leg2.rotateAngleY += -var11;
		this.Leg3.rotateAngleY += var12;
		this.Leg4.rotateAngleY += -var12;
		this.Leg5.rotateAngleY += var14;
		this.Leg6.rotateAngleY += -var14;

		this.Leg1.rotateAngleZ += var15;
		this.Leg2.rotateAngleZ += -var15;

		this.Leg3.rotateAngleZ += var16;
		this.Leg4.rotateAngleZ += -var16;

		this.Leg5.rotateAngleZ += var18;
		this.Leg6.rotateAngleZ += -var18;


	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float partialTick) {
		EntityTFPinchBeetle beetle = (EntityTFPinchBeetle) par1EntityLiving;

		if (beetle.isBeingRidden()) {
			// open jaws
			this.jaw1a.rotateAngleY = 2.96705972839036F;
			this.jaw2a.rotateAngleY = 0.3490658503988659F;
		} else {
			// close jaws
			this.jaw1a.rotateAngleY = 2.356194490192345F;
			this.jaw2a.rotateAngleY = 0.7853981633974483F;
		}
	}


}
