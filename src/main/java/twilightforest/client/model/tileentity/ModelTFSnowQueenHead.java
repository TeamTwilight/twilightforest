package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFSnowQueenHead extends ModelTFGenericHead {

	public ModelRenderer head;
	public ModelRenderer crownFront;
	public ModelRenderer crownBack;
	public ModelRenderer crownRight;
	public ModelRenderer crownLeft;

	public ModelTFSnowQueenHead() {
		textureWidth = 64;
		textureHeight = 64;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);

		this.crownRight = new ModelRenderer(this, 0, 0);
		this.crownRight.setRotationPoint(-4.0F, -6.0F, 0.0F);
		this.crownRight.setTextureOffset(24, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(crownRight, 0.39269908169872414F, 1.5707963267948966F, 0.0F);
		this.crownBack = new ModelRenderer(this, 0, 0);
		this.crownBack.setRotationPoint(0.0F, -6.0F, 4.0F);
		this.crownBack.setTextureOffset(44, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(crownBack, -0.39269908169872414F, 0.0F, 0.0F);
		this.crownLeft = new ModelRenderer(this, 0, 0);
		this.crownLeft.setRotationPoint(4.0F, -6.0F, 0.0F);
		this.crownLeft.setTextureOffset(44, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(crownLeft, -0.39269908169872414F, 1.5707963267948966F, 0.0F);
		this.crownFront = new ModelRenderer(this, 0, 0);
		this.crownFront.setRotationPoint(0.0F, -6.0F, -4.0F);
		this.crownFront.setTextureOffset(24, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(crownFront, 0.39269908169872414F, 0.0F, 0.0F);

		this.head.addChild(this.crownRight);
		this.head.addChild(this.crownBack);
		this.head.addChild(this.crownLeft);
		this.head.addChild(this.crownFront);

	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		super.setRotations(x, y, z);
		this.head.rotateAngleY = y * ((float) Math.PI / 180F);
		this.head.rotateAngleX = x * ((float) Math.PI / 180F);
	}
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
