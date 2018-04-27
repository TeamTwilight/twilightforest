package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;


public class ModelTFDeer extends ModelQuadruped {
	public ModelTFDeer() {
		super(12, 0.0F);

		// head height for baby
		childYOffset = 10F;

		head = new ModelRenderer(this, 0, 5);
		head.addBox(-2F, -8F, -6F, 4, 6, 6, 0F);
		head.setRotationPoint(0F, 4F, -7F);

		body = new ModelRenderer(this, 36, 6);
		body.addBox(-4F, -10F, -7F, 6, 18, 8, 0F);
		body.setRotationPoint(1F, 5F, 2F);

		body.rotateAngleX = 1.570796F;
		leg1 = new ModelRenderer(this, 0, 17);
		leg1.addBox(-3F, 0F, -2F, 2, 12, 3, 0F);
		leg1.setRotationPoint(0F, 12F, 9F);

		leg2 = new ModelRenderer(this, 0, 17);
		leg2.addBox(-1F, 0F, -2F, 2, 12, 3, 0F);
		leg2.setRotationPoint(2F, 12F, 9F);

		leg3 = new ModelRenderer(this, 0, 17);
		leg3.addBox(-3F, 0F, -3F, 2, 12, 3, 0F);
		leg3.setRotationPoint(0F, 12F, -5F);

		leg4 = new ModelRenderer(this, 0, 17);
		leg4.addBox(-1F, 0F, -3F, 2, 12, 3, 0F);
		leg4.setRotationPoint(2F, 12F, -5F);

		// neck
		neck = new ModelRenderer(this, 10, 19);
		neck.addBox(-2.5F, -8, -11F, 3, 9, 4, 0F);
//		neck.setRotationPoint(1F, 5F, 2F);

		neck.rotateAngleX = 4.974188f;

		body.addChild(neck);

		// nose
		head.setTextureOffset(52, 0).addBox(-1.5F, -5F, -9F, 3, 3, 3, 0F);

		// antler 1
		head.setTextureOffset(20, 0);
		head.addBox(-3F, -10F, -2F, 2, 2, 2, 0F);
		head.addBox(-3F, -10F, -2F, 2, 2, 2, 0F);
		head.addBox(-4F, -10F, -1F, 1, 1, 3, 0F);
		head.addBox(-5F, -11F, 1F, 1, 1, 5, 0F);
		head.addBox(-5F, -14F, 2F, 1, 4, 1, 0F);
		head.addBox(-6F, -17F, 3F, 1, 4, 1, 0F);
		head.addBox(-6F, -13F, 0F, 1, 1, 3, 0F);
		head.addBox(-6F, -14F, -3F, 1, 1, 4, 0F);
		head.addBox(-7F, -15F, -6F, 1, 1, 4, 0F);
		head.addBox(-6F, -16F, -9F, 1, 1, 4, 0F);
		head.addBox(-7F, -18F, -1F, 1, 5, 1, 0F);
		head.addBox(-6F, -19F, -6F, 1, 5, 1, 0F);

		// antler 2
		head.addBox(1F, -10F, -2F, 2, 2, 2, 0F);
		head.addBox(3F, -10F, -1F, 1, 1, 3, 0F);
		head.addBox(4F, -11F, 1F, 1, 1, 5, 0F);
		head.addBox(4F, -14F, 2F, 1, 4, 1, 0F);
		head.addBox(5F, -17F, 3F, 1, 4, 1, 0F);
		head.addBox(5F, -13F, 0F, 1, 1, 3, 0F);
		head.addBox(5F, -14F, -3F, 1, 1, 4, 0F);
		head.addBox(6F, -15F, -6F, 1, 1, 4, 0F);
		head.addBox(5F, -16F, -9F, 1, 1, 4, 0F);
		head.addBox(6F, -18F, -1F, 1, 5, 1, 0F);
		head.addBox(5F, -19F, -6F, 1, 5, 1, 0F);
	}

	//fields
	public ModelRenderer neck;

}
