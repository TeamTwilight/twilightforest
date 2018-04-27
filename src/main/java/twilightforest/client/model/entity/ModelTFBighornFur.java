package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep2;


public class ModelTFBighornFur extends ModelSheep2 {

	public ModelTFBighornFur() {
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -4F, -4F, 6, 6, 6, 0.6F);
		head.setRotationPoint(0.0F, 6F, -8F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4F, -9F, -7F, 8, 15, 6, 0.5F);
		body.setRotationPoint(0.0F, 5F, 2.0F);
		float f = 0.4F;
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg1.setRotationPoint(-3F, 12F, 7F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg2.setRotationPoint(3F, 12F, 7F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg3.setRotationPoint(-3F, 12F, -5F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, f);
		leg4.setRotationPoint(3F, 12F, -5F);
	}
}
