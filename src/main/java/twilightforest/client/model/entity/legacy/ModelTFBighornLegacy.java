package twilightforest.client.model.entity.legacy;

import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.entity.passive.EntityTFBighorn;

public class ModelTFBighornLegacy<T extends EntityTFBighorn> extends SheepModel<T> {

	public ModelTFBighornLegacy() {
		super();
		headModel = new ModelRenderer(this, 0, 0);
		headModel.addBox(-3F, -4F, -6F, 6, 6, 7, 0F);
		headModel.setRotationPoint(0F, 6F, -8F);

		body = new ModelRenderer(this, 36, 10);
		body.addBox(-4F, -9F, -7F, 8, 15, 6, 0F);
		body.setRotationPoint(0F, 5F, 2F);

		legBackRight = new ModelRenderer(this, 0, 16);
		legBackRight.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0F);
		legBackRight.setRotationPoint(-3F, 12F, 7F);

		legBackLeft = new ModelRenderer(this, 0, 16);
		legBackLeft.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0F);
		legBackLeft.setRotationPoint(3F, 12F, 7F);

		legFrontRight = new ModelRenderer(this, 0, 16);
		legFrontRight.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0F);
		legFrontRight.setRotationPoint(-3F, 12F, -5F);

		legFrontLeft = new ModelRenderer(this, 0, 16);
		legFrontLeft.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0F);
		legFrontLeft.setRotationPoint(3F, 12F, -5F);

		// curly horn 1
		headModel.setTextureOffset(28, 16).addBox(-5F, -4F, -4F, 2, 2, 2, 0F);
		headModel.setTextureOffset(16, 13).addBox(-6F, -5F, -3F, 2, 2, 4, 0F);
		headModel.setTextureOffset(16, 19).addBox(-7F, -4F, 0F, 2, 5, 2, 0F);
		headModel.setTextureOffset(18, 27).addBox(-8F, 0F, -2F, 2, 2, 3, 0F);
		headModel.setTextureOffset(28, 27).addBox(-9F, -1F, -3F, 2, 2, 1, 0F);

		// curly horn 2
		headModel.setTextureOffset(28, 16).addBox(3F, -4F, -4F, 2, 2, 2, 0F);
		headModel.setTextureOffset(16, 13).addBox(4F, -5F, -3F, 2, 2, 4, 0F);
		headModel.setTextureOffset(16, 19).addBox(5F, -4F, 0F, 2, 5, 2, 0F);
		headModel.setTextureOffset(18, 27).addBox(6F, 0F, -2F, 2, 2, 3, 0F);
		headModel.setTextureOffset(28, 27).addBox(7F, -1F, -3F, 2, 2, 1, 0F);
	}
}
