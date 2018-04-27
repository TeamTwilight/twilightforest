package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;


public class ModelTFBoar extends ModelQuadruped {
	public ModelTFBoar() {
		super(6, 0.0F);

		// head height for baby
		childYOffset = 4F;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -2F, -6F, 8, 7, 6, 0F);
		head.setRotationPoint(0F, 12F, -6F);

		body = new ModelRenderer(this, 28, 10);
		body.addBox(-5F, -8F, -7F, 10, 14, 8, 0F);
		body.setRotationPoint(0F, 11F, 2F);

		body.rotateAngleX = 1.570796F;

		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2F, 0F, -2F, 4, 6, 4, 0F);
		leg1.setRotationPoint(-3F, 18F, 7F);

		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2F, 0F, -2F, 4, 6, 4, 0F);
		leg2.setRotationPoint(3F, 18F, 7F);

		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2F, 0F, -2F, 4, 6, 4, 0F);
		leg3.setRotationPoint(-3F, 18F, -5F);

		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2F, 0F, -2F, 4, 6, 4, 0F);
		leg4.setRotationPoint(3F, 18F, -5F);

		head.setTextureOffset(28, 0).addBox(-3F, 1F, -9F, 6, 4, 3, 0F); // snout

		head.setTextureOffset(17, 17).addBox(3F, 2F, -9F, 1, 2, 1, 0F); // tusk1
		head.setTextureOffset(17, 17).addBox(-4F, 2F, -9F, 1, 2, 1, 0F); // tusk2
	}

}
