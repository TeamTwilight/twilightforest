package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;


public class ModelTFFirefly extends ModelBase {
	public ModelTFFirefly() {
		legs = new ModelRenderer(this, 0, 21);
		legs.addBox(-4F, 7.9F, -5F, 8, 1, 10, 0F);
		//legs.setRotationPoint(0F, 16F, 0F);

		fatbody = new ModelRenderer(this, 0, 11);
		fatbody.addBox(-2F, 6F, -4F, 4, 2, 6, 0F);
		//fatbody.setRotationPoint(0F, 16F, 0F);

		skinnybody = new ModelRenderer(this, 0, 0);
		skinnybody.addBox(-1F, 7F, -5F, 2, 1, 8, 0F);
		//skinnybody.setRotationPoint(0F, 16F, 0F);

		glow = new ModelRenderer(this, 20, 0);
		glow.addBox(-5F, 5.9F, -9F, 10, 0, 10, 0F);
		//glow.setRotationPoint(0F, 16F, 0F);

	}

	public void render(float f5) {
		//super.render(f, f1, f2, f3, f4, f5);
		//setRotationAngles(f, f1, f2, f3, f4, f5);
		legs.render(f5);
		fatbody.render(f5);
		skinnybody.render(f5);
//		glow.render(f5);

	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	//fields
	public ModelRenderer legs;
	public ModelRenderer fatbody;
	public ModelRenderer skinnybody;
	public ModelRenderer glow;

}
