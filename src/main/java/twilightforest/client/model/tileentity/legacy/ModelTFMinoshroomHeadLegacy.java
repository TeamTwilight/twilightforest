package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.client.model.tileentity.ModelTFGenericHead;

public class ModelTFMinoshroomHeadLegacy extends ModelTFGenericHead {

	public ModelRenderer head;
	public ModelRenderer snout;
	public ModelRenderer righthorn1;
	public ModelRenderer righthorn2;
	public ModelRenderer lefthorn1;
	public ModelRenderer lefthorn2;

	public ModelTFMinoshroomHeadLegacy() {

		this.textureWidth = 128;
		this.textureHeight = 32;
		
		this.head = new ModelRenderer(this, 96, 16);
		this.head.addBox(-4F, -8F, -4F, 8, 8, 8);
		this.head.setRotationPoint(0F, -4F, -0F);

		this.righthorn1 = new ModelRenderer(this, 0, 0);
		this.righthorn1.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3);
		this.righthorn1.setRotationPoint(-2.5F, -6.5F, 0.0F);
		this.righthorn1.rotateAngleY = -25F / (180F / (float) Math.PI);
		this.righthorn1.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.righthorn2 = new ModelRenderer(this, 16, 0);
		this.righthorn2.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2);
		this.righthorn2.setRotationPoint(-4.5F, 0.0F, 0.0F);
		this.righthorn2.rotateAngleY = -15F / (180F / (float) Math.PI);
		this.righthorn2.rotateAngleZ = 45F / (180F / (float) Math.PI);

		this.righthorn1.addChild(righthorn2);

		this.lefthorn1 = new ModelRenderer(this, 0, 0);
		this.lefthorn1.mirror = true;
		this.lefthorn1.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3);
		this.lefthorn1.setRotationPoint(2.5F, -6.5F, 0.0F);
		this.lefthorn1.rotateAngleY = 25F / (180F / (float) Math.PI);
		this.lefthorn1.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.lefthorn2 = new ModelRenderer(this, 16, 0);
		this.lefthorn2.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2);
		this.lefthorn2.setRotationPoint(4.5F, 0.0F, 0.0F);
		this.lefthorn2.rotateAngleY = 15F / (180F / (float) Math.PI);
		this.lefthorn2.rotateAngleZ = -45F / (180F / (float) Math.PI);

		this.lefthorn1.addChild(lefthorn2);

		this.head.addChild(righthorn1);
		this.head.addChild(lefthorn1);

		snout = new ModelRenderer(this, 105, 28);
		snout.addBox(-2, -1, -1, 4, 3, 1);
		snout.setRotationPoint(0F, -2.0F, -4F);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		super.setRotations(x, y, z);
		this.head.rotateAngleY = y * ((float) Math.PI / 180F);
		this.head.rotateAngleX = x * ((float) Math.PI / 180F);
	}
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		matrix.translate(0.0F, .25F, 0.0F);
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
