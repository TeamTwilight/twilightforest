package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFYetiAlphaHead extends ModelTFGenericHead {
	public final ModelRenderer main;

	public ModelTFYetiAlphaHead() {
		textureWidth = 256;
		textureHeight = 128;

		main = new ModelRenderer(this);
		main.setRotationPoint(0.0F, -6.0F, 0.0F);


		ModelRenderer head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		main.addChild(head);
		head.setTextureOffset(80, 0).addBox(-24.0F, -24.0F, -18.0F, 48.0F, 54.0F, 36.0F, 0.0F, false);
		head.setTextureOffset(64, 0).addBox(8.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(121, 50).addBox(-17.0F, -8.0F, -19.5F, 34.0F, 29.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(64, 0).addBox(-20.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F, 0.0F, false);

		ModelRenderer lefthorn1 = new ModelRenderer(this);
		lefthorn1.setRotationPoint(22.0F, 6.0F, -1.0F);
		main.addChild(lefthorn1);
		setRotationAngle(lefthorn1, 0.0F, 0.5236F, 0.0873F);
		lefthorn1.setTextureOffset(0, 108).addBox(1.6981F, -10.9848F, -5.1743F, 10.0F, 10.0F, 10.0F, 0.0F, false);

		ModelRenderer top_r1 = new ModelRenderer(this);
		top_r1.setRotationPoint(11.0F, -2.0F, 0.0F);
		lefthorn1.addChild(top_r1);
		setRotationAngle(top_r1, 0.0F, 0.3491F, 0.0873F);
		top_r1.setTextureOffset(40, 108).addBox(0.4505F, -7.9433F, -4.3855F, 18.0F, 8.0F, 8.0F, 0.0F, false);

		ModelRenderer lefthorn2 = new ModelRenderer(this);
		lefthorn2.setRotationPoint(24.0F, -4.0F, -1.0F);
		main.addChild(lefthorn2);
		setRotationAngle(lefthorn2, 0.0F, 0.5236F, -0.2618F);
		lefthorn2.setTextureOffset(0, 108).addBox(0.8966F, -10.8637F, -4.4824F, 10.0F, 10.0F, 10.0F, 0.0F, false);

		ModelRenderer top_r2 = new ModelRenderer(this);
		top_r2.setRotationPoint(9.0F, -1.0F, 0.0F);
		lefthorn2.addChild(top_r2);
		setRotationAngle(top_r2, 0.0F, 0.3491F, -0.2618F);
		top_r2.setTextureOffset(40, 108).addBox(2.5764F, -8.5F, -2.8753F, 18.0F, 8.0F, 8.0F, 0.0F, false);

		ModelRenderer lefthorn3 = new ModelRenderer(this);
		lefthorn3.setRotationPoint(24.0F, -16.0F, -1.0F);
		main.addChild(lefthorn3);
		setRotationAngle(lefthorn3, 0.0F, 0.5236F, -0.6109F);
		lefthorn3.setTextureOffset(0, 108).addBox(1.9869F, -10.2766F, -3.8528F, 10.0F, 10.0F, 10.0F, 0.0F, false);

		ModelRenderer top_r3 = new ModelRenderer(this);
		top_r3.setRotationPoint(8.0F, -2.0F, 0.0F);
		lefthorn3.addChild(top_r3);
		setRotationAngle(top_r3, 0.0F, 0.3491F, -0.6109F);
		top_r3.setTextureOffset(40, 108).addBox(4.9031F, -5.5443F, -1.7225F, 18.0F, 8.0F, 8.0F, 0.0F, false);

		ModelRenderer righthorn1 = new ModelRenderer(this);
		righthorn1.setRotationPoint(-22.0F, 6.0F, -1.0F);
		main.addChild(righthorn1);
		setRotationAngle(righthorn1, 0.0F, -0.5236F, -0.0873F);
		righthorn1.setTextureOffset(0, 108).addBox(-11.6981F, -10.9848F, -5.1743F, 10.0F, 10.0F, 10.0F, 0.0F, true);

		ModelRenderer top_r4 = new ModelRenderer(this);
		top_r4.setRotationPoint(-11.0F, -2.0F, 0.0F);
		righthorn1.addChild(top_r4);
		setRotationAngle(top_r4, 0.0F, -0.3491F, -0.0873F);
		top_r4.setTextureOffset(40, 108).addBox(-18.4505F, -7.9433F, -4.3855F, 18.0F, 8.0F, 8.0F, 0.0F, true);

		ModelRenderer righthorn2 = new ModelRenderer(this);
		righthorn2.setRotationPoint(-24.0F, -4.0F, -1.0F);
		main.addChild(righthorn2);
		setRotationAngle(righthorn2, 0.0F, -0.5236F, 0.2618F);
		righthorn2.setTextureOffset(0, 108).addBox(-10.8966F, -10.8637F, -4.4824F, 10.0F, 10.0F, 10.0F, 0.0F, true);

		ModelRenderer top_r5 = new ModelRenderer(this);
		top_r5.setRotationPoint(-9.0F, -1.0F, 0.0F);
		righthorn2.addChild(top_r5);
		setRotationAngle(top_r5, 0.0F, -0.3491F, 0.2618F);
		top_r5.setTextureOffset(40, 108).addBox(-20.5764F, -8.5F, -2.8753F, 18.0F, 8.0F, 8.0F, 0.0F, true);

		ModelRenderer righthorn3 = new ModelRenderer(this);
		righthorn3.setRotationPoint(-24.0F, -16.0F, -1.0F);
		main.addChild(righthorn3);
		setRotationAngle(righthorn3, 0.0F, -0.5236F, 0.6109F);
		righthorn3.setTextureOffset(0, 108).addBox(-11.9869F, -10.2766F, -3.8528F, 10.0F, 10.0F, 10.0F, 0.0F, true);

		ModelRenderer top_r6 = new ModelRenderer(this);
		top_r6.setRotationPoint(-8.0F, -2.0F, 0.0F);
		righthorn3.addChild(top_r6);
		setRotationAngle(top_r6, 0.0F, -0.3491F, 0.6109F);
		top_r6.setTextureOffset(40, 108).addBox(-22.9031F, -5.5443F, -1.7225F, 18.0F, 8.0F, 8.0F, 0.0F, true);
	}

	@Override
	public void setRotations(float x, float y, float z) {
		super.setRotations(x, y, z);
		main.rotateAngleY = y * ((float) Math.PI / 180F);
		main.rotateAngleX = x * ((float) Math.PI / 180F);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}