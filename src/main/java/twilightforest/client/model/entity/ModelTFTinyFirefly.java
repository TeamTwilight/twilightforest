package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;


public class ModelTFTinyFirefly extends ModelBase {
	public ModelTFTinyFirefly() {
		glow1 = new ModelRenderer(this, 20, 0);
		glow1.addBox(-5F, -5F, 0F, 10, 10, 0, 0F);

	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		glow1.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	//fields
	public ModelRenderer glow1;

}
