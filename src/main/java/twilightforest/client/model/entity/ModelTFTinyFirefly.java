package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFTinyFirefly extends ModelBase {
	public ModelTFTinyFirefly() {
		glow1 = new ModelRenderer(this, 20, 0);
		glow1.addBox(-5F, -5F, 0F, 10, 10, 0, 0F);

	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		glow1.render(scale);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	//fields
	public ModelRenderer glow1;

}
