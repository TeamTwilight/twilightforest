package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTFCubeOfAnnihilation extends ModelBase {

	public ModelRenderer box;
	public ModelRenderer boxX;
	public ModelRenderer boxY;
	public ModelRenderer boxZ;

	public ModelTFCubeOfAnnihilation() {
		textureWidth = 64;
		textureHeight = 64;
		box = new ModelRenderer(this, 0, 0);
		box.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		box.setRotationPoint(0F, 0F, 0F);

		boxX = new ModelRenderer(this, 0, 32);
		boxX.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxX.setRotationPoint(0F, 0F, 0F);

		boxY = new ModelRenderer(this, 0, 32);
		boxY.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxY.setRotationPoint(0F, 0F, 0F);

		boxZ = new ModelRenderer(this, 0, 32);
		boxZ.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxZ.setRotationPoint(0F, 0F, 0F);
	}


	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		box.render(scale);
		boxX.render(scale);
		boxY.render(scale);
		boxZ.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {

		boxX.rotateAngleX = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;
		boxY.rotateAngleY = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;
		boxZ.rotateAngleZ = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;

	}

}
