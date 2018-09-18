package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFHydraNeck extends ModelBase {

	ModelRenderer neck;

	public ModelTFHydraNeck() {
		textureWidth = 512;
		textureHeight = 256;

		setTextureOffset("neck.box", 128, 136);
		setTextureOffset("neck.fin", 128, 200);
		neck = new ModelRenderer(this, "neck");
		neck.addBox("box", -16F, -16F, -16F, 32, 32, 32);
		neck.addBox("fin", -2F, -23F, 0F, 4, 24, 24);
		neck.setRotationPoint(0F, 0F, 0F);

	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		neck.render(scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		neck.rotateAngleY = netHeadYaw / 57.29578F;
		neck.rotateAngleX = headPitch / 57.29578F;
	}


}
