package twilightforest.client.model.entity;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.boss.EntityTFLich;


public class ModelTFLich extends ModelBiped {


	ModelRenderer collar;
	ModelRenderer cloak;
	ModelRenderer shieldBelt;

	boolean renderPass = false;

	public ModelTFLich() {
		this.renderPass = false;

		textureWidth = 64;
		textureHeight = 64;


		bipedBody = new ModelRenderer(this, 8, 16);
		bipedBody.addBox(-4F, 0.0F, -2F, 8, 24, 4);
		bipedBody.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedBody.setTextureSize(64, 64);

		bipedRightArm = new ModelRenderer(this, 0, 16);
		bipedRightArm.addBox(-2F, -2F, -1F, 2, 12, 2);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.setRotationPoint(-5F, -2.0F, 0.0F);

		bipedLeftArm = new ModelRenderer(this, 0, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-2F, -2F, -1F, 2, 12, 2);
		bipedLeftArm.setRotationPoint(5F, -2.0F, 0.0F);
		bipedLeftArm.setTextureSize(64, 64);

		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4F, -12F, -4F, 8, 8, 8, 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedHeadwear.setTextureSize(64, 64);

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, -4F, 0F);
		bipedHead.setTextureSize(64, 64);

		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-1F, 0F, -1F, 2, 12, 2);
		bipedRightLeg.setRotationPoint(-2F, 9.5F, 0F);
		bipedRightLeg.setTextureSize(64, 64);

		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.addBox(-1F, 0F, -1F, 2, 12, 2);
		bipedLeftLeg.setRotationPoint(2F, 9.5F, 0F);
		bipedLeftLeg.setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;

		collar = new ModelRenderer(this, 32, 16);
		collar.addBox(-6F, 0F, 0F, 12, 12, 1);
		collar.setRotationPoint(0F, -3F, -1F);
		collar.setTextureSize(64, 64);
		setRotation(collar, 2.164208F, 0F, 0F);


		cloak = new ModelRenderer(this, 0, 44);
		cloak.addBox(-6F, 0F, 0F, 12, 19, 1);
		cloak.setRotationPoint(0F, -4F, 2.5F);
		cloak.setTextureSize(64, 64);
		setRotation(cloak, 0F, 0F, 0F);

		shieldBelt = new ModelRenderer(this);
		shieldBelt.setRotationPoint(0F, 0F, 0F);

	}

	public ModelTFLich(boolean specialRenderModel) {
		this();
		this.renderPass = specialRenderModel;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		EntityTFLich lich = (EntityTFLich) entity;

		// on regular pass, render everything about the master lich except the stronghold_shield
		if (!renderPass) {
			if (!lich.isShadowClone()) {
				super.render(entity, f, f1, f2, f3, f4, f5 * 1.125F);
				collar.render(f5 * 1.125F);
				cloak.render(f5 * 1.125F);
			}
		} else {
			// on the special render pass, render the shadow clone and the stronghold_shield
			if (lich.isShadowClone()) {
//    	        GL11.glDisable(GL11.GL_DEPTH_TEST);

				super.render(entity, f, f1, f2, f3, f4, f5 * 1.125F);
//               	GL11.glEnable(GL11.GL_DEPTH_TEST);

			} else {
				if (lich.getShieldStrength() > 0) {
					shieldBelt.render(f5 * 1.125F);
				}
			}
		}

	}

	@Override
	public void setModelAttributes(ModelBase model) {
		super.setModelAttributes(model);
		if (model instanceof ModelTFLich) {
			shieldBelt = ((ModelTFLich) model).shieldBelt;
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float time) {
		EntityTFLich lich = (EntityTFLich) par1EntityLiving;
		// make the stronghold_shield belt
		int shields = lich.getShieldStrength();
		if (!lich.isShadowClone() && shields > 0) {
			if (shieldBelt.childModels == null || shieldBelt.childModels.size() != shields) {
				// clear shields if we have the wrong number
				if (shieldBelt.childModels != null) {
					shieldBelt.childModels.clear();
				}
				// make or remake a belt of shields around the lich
				Vec3d vec;
				for (int i = 0; i < shields; i++) {
					vec = new Vec3d(11, 0, 0);
					float rotateY = ((i * (360F / shields)) * 3.141593F) / 180F;
					vec = vec.rotateYaw(rotateY);
					ModelRenderer shield = new ModelRenderer(this, 26, 40);
					shield.addBox(0.5F, -6F, -6F, 1, 12, 12);
					shield.setRotationPoint((float) vec.x, (float) vec.y, (float) vec.z);
					shield.setTextureSize(64, 64);
					shield.rotateAngleY = rotateY;
					shieldBelt.addChild(shield);
				}
			}

			// rotate the belt
			shieldBelt.rotateAngleY = (lich.ticksExisted + time) / 5.0F;
			shieldBelt.rotateAngleX = MathHelper.sin((lich.ticksExisted + time) / 5.0F) / 4.0F;
			shieldBelt.rotateAngleZ = MathHelper.cos((lich.ticksExisted + time) / 5.0F) / 4.0F;
		}
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		float ogSin = MathHelper.sin(swingProgress * 3.141593F);
		float otherSin = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * 3.141593F);
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.5F;
		bipedRightArm.rotateAngleY = -(0.1F - ogSin * 0.6F);
		bipedLeftArm.rotateAngleY = 0.1F - ogSin * 0.6F;
		bipedRightArm.rotateAngleX = -1.570796F;
		bipedLeftArm.rotateAngleX = -3.141593F;
		bipedRightArm.rotateAngleX -= ogSin * 1.2F - otherSin * 0.4F;
		bipedLeftArm.rotateAngleX -= ogSin * 1.2F - otherSin * 0.4F;
		bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.26F) * 0.15F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.26F) * 0.15F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.167F) * 0.15F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.167F) * 0.15F;

		bipedHead.rotationPointY = -4.0F;
		bipedHeadwear.rotationPointY = -4.0F;
		bipedRightLeg.rotationPointY = 9.5F;
		bipedLeftLeg.rotationPointY = 9.5F;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}


}
