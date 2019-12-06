package twilightforest.client.model.entity;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.EntityModel;


public class ModelTFCicada extends EntityModel {
	public ModelTFCicada() {
		legs = new RendererModel(this, 0, 21);
		legs.addBox(-4F, 7.9F, -5F, 8, 1, 9, 0F);
		//legs.setRotationPoint(0F, 16F, 0F);

		fatbody = new RendererModel(this, 0, 11);
		fatbody.addBox(-2F, 6F, -4F, 4, 2, 6, 0F);
		//fatbody.setRotationPoint(0F, 16F, 0F);

		skinnybody = new RendererModel(this, 0, 0);
		skinnybody.addBox(-1F, 7F, -5F, 2, 1, 8, 0F);
		//skinnybody.setRotationPoint(0F, 16F, 0F);


		eye1 = new RendererModel(this, 20, 15);
		eye1.addBox(1F, 5F, 2F, 2, 2, 2, 0F);
		//eye1.setRotationPoint(0F, 16F, 0F);

		eye2 = new RendererModel(this, 20, 15);
		eye2.addBox(-3F, 5F, 2F, 2, 2, 2, 0F);
		//eye2.setRotationPoint(0F, 16F, 0F);

		wings = new RendererModel(this, 20, 0);
		wings.addBox(-4F, 5F, -7F, 8, 1, 8, 0F);
		//wings.setRotationPoint(0F, 16F, 0F);
	}

	public void render(float f5) {
//		super.render(f, f1, f2, f3, f4, f5);
//		setRotationAngles(f, f1, f2, f3, f4, f5);
		legs.render(f5);
		fatbody.render(f5);
		skinnybody.render(f5);
		eye1.render(f5);
		eye2.render(f5);
		wings.render(f5);

	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
//		super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	//fields
	public RendererModel legs;
	public RendererModel fatbody;
	public RendererModel skinnybody;
	public RendererModel eye1;
	public RendererModel eye2;
	public RendererModel wings;

}
