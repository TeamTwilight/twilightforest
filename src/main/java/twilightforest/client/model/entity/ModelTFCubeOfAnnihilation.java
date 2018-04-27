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
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {

		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

		box.render(p_78088_7_);
		boxX.render(p_78088_7_);
		boxY.render(p_78088_7_);
		boxZ.render(p_78088_7_);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float time, float f5, Entity entity) {

		boxX.rotateAngleX = (float) Math.sin((entity.ticksExisted + time)) / 5F;
		boxY.rotateAngleY = (float) Math.sin((entity.ticksExisted + time)) / 5F;
		boxZ.rotateAngleZ = (float) Math.sin((entity.ticksExisted + time)) / 5F;

	}

}
