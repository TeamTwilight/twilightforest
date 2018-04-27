package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFRedcap extends ModelBiped {
	public ModelTFRedcap() {
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-3.4F, 1F, -4F, 7, 7, 7, 0F);
		bipedHead.setRotationPoint(0F, 0F, 0F);

		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-2F, 0F, -3F, 4, 5, 7, 0F);
		bipedHeadwear.setRotationPoint(0F, 0F, 0F);

		bipedBody = new ModelRenderer(this, 12, 19);
		bipedBody.addBox(-4F, 6F, -2F, 8, 9, 4, 0F);
		bipedBody.setRotationPoint(0F, 0F, 0F);

		bipedRightArm = new ModelRenderer(this, 36, 17);
		bipedRightArm.addBox(-2F, -2F, -2F, 3, 12, 3, 0F);
		bipedRightArm.setRotationPoint(-5F, 8F, 0F);

		bipedLeftArm = new ModelRenderer(this, 36, 17);
		bipedLeftArm.addBox(-1F, -2F, -2F, 3, 12, 3, 0F);
		bipedLeftArm.setRotationPoint(5F, 8F, 0F);

		bipedRightLeg = new ModelRenderer(this, 0, 20);
		bipedRightLeg.addBox(-2F, 2F, -1F, 3, 9, 3, 0F);
		bipedRightLeg.setRotationPoint(-2F, 12F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 0, 20);
		bipedLeftLeg.addBox(-1F, 3F, -1F, 3, 9, 3, 0F);
		bipedLeftLeg.setRotationPoint(2F, 12F, 0F);

		goblinRightEar = new ModelRenderer(this, 48, 20);
		goblinRightEar.addBox(3F, -2F, -1F, 2, 3, 1, 0F);
		goblinRightEar.setRotationPoint(0F, 3F, 0F);

		goblinLeftEar = new ModelRenderer(this, 48, 24);
		goblinLeftEar.addBox(-5F, -2F, -1F, 2, 3, 1, 0F);
		goblinLeftEar.setRotationPoint(0F, 3F, 0F);

		goblinLeftEar.mirror = true;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		goblinRightEar.rotateAngleX = bipedHead.rotateAngleX;
		goblinRightEar.rotateAngleY = bipedHead.rotateAngleY;
		goblinRightEar.rotateAngleZ = bipedHead.rotateAngleZ;

		goblinLeftEar.rotateAngleX = bipedHead.rotateAngleX;
		goblinLeftEar.rotateAngleY = bipedHead.rotateAngleY;
		goblinLeftEar.rotateAngleZ = bipedHead.rotateAngleZ;
	}


	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);

		goblinRightEar.render(f5);
		goblinLeftEar.render(f5);
	}


	ModelRenderer goblinRightEar;
	ModelRenderer goblinLeftEar;
}
