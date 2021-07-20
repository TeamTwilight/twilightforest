package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFHydraHeadTrophy extends ModelTFGenericHead {

	public ModelRenderer head;
	public ModelRenderer plate;
	public ModelRenderer mouth;

	public ModelTFHydraHeadTrophy() {
		textureWidth = 512;
		textureHeight = 256;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0F, 0F, 0F);
		this.head.setTextureOffset(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F, 0.0F, 0.0F, 0.0F);


		this.plate = new ModelRenderer(this, 0, 0);
		this.plate.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.plate.setTextureOffset(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F, 0.0F, 0.0F, 0.0F);
		this.plate.setTextureOffset(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(plate, -0.7853981633974483F, 0.0F, 0.0F);

		head.addChild(plate);

		this.mouth = new ModelRenderer(this, 0, 0);
		this.mouth.setRotationPoint(0.0F, 10.0F, -14.0F);
		this.mouth.setTextureOffset(240, 162).addBox(-15.0F, -2.0F, -24.0F, 30.0F, 8.0F, 24.0F, 0.0F, 0.0F, 0.0F);

		head.addChild(mouth);
	}
	
	private void setRotateAngle(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		super.setRotations(x, y, z);
		this.head.rotateAngleY = y * ((float) Math.PI / 180F);
		this.head.rotateAngleX = x * ((float) Math.PI / 180F);
	}
	
	public void openMouthForTrophy(float mouthOpen) {
		head.rotateAngleY = 0;
		head.rotateAngleX = 0;

		head.rotateAngleX -= (float) (mouthOpen * (Math.PI / 12.0));
		mouth.rotateAngleX = (float) (mouthOpen * (Math.PI / 3.0));
	}
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
