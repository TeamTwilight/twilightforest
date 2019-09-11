package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelTFTinyFirefly extends ModelBase {
	public ModelTFTinyFirefly() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		glow1 = new ModelRenderer(this, 25, 11);
		glow1.addBox(-5F, -5F, 0F, 10, 10, 0, 0F);
		this.body2 = new ModelRenderer(this, 0, 8);
		this.body2.setRotationPoint(0.0F, -0.1F, 4.0F);
		this.body2.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 6, 0.0F);
		this.setRotateAngle(body2, -0.36425021489121656F, 0.0F, 0.0F);
		this.body3 = new ModelRenderer(this, 38, 0);
		this.body3.setRotationPoint(0.0F, 0.0F, 5.0F);
		this.body3.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
		this.wingL = new ModelRenderer(this, 0, 19);
		this.wingL.setRotationPoint(1.5F, -2.0F, -1.7F);
		this.wingL.addBox(0.0F, -1.0F, -3.0F, 6, 1, 4, 0.0F);
		this.setRotateAngle(wingL, 0.0F, -0.4553564018453205F, -0.36425021489121656F);
		this.wingR = new ModelRenderer(this, 0, 19);
		this.wingR.setRotationPoint(-1.5F, -2.0F, -1.7F);
		this.wingR.addBox(-6.0F, -1.0F, -3.0F, 6, 1, 4, 0.0F);
		this.setRotateAngle(wingR, 0.0F, 0.4553564018453205F, 0.36425021489121656F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 21.0F, -2.0F);
		this.head.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
		this.body = new ModelRenderer(this, 16, 0);
		this.body.setRotationPoint(0.0F, 21.0F, -3.0F);
		this.body.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 6, 0.0F);
		this.setRotateAngle(body, -0.5009094953223726F, 0.0F, 0.0F);
		this.body.addChild(this.body2);
		this.body2.addChild(this.body3);
		this.body2.addChild(this.wingL);
		this.body2.addChild(this.wingR);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.head.render(scale * 0.8F);
		this.body.render(scale * 0.8F);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		this.wingR.rotateAngleY = 0.4553564018453205F + MathHelper.cos(ageInTicks * 3.0F) * 0.15F;
		this.wingL.rotateAngleY = -0.4553564018453205F - MathHelper.cos(ageInTicks * 3.0F) * 0.15F;
		this.wingR.rotateAngleZ = 0.36425021489121656F + MathHelper.cos(ageInTicks * 3.0F) * 0.25F;
		this.wingL.rotateAngleZ = -0.36425021489121656F - MathHelper.cos(ageInTicks * 3.0F) * 0.25F;
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	//fields
	public ModelRenderer glow1;
	//entityModel
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer body2;
	public ModelRenderer body3;
	public ModelRenderer wingL;
	public ModelRenderer wingR;

}
