package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.client.model.tileentity.ModelTFGenericHead;

public class ModelTFHydraHeadTrophyLegacy extends ModelTFGenericHead {

	public ModelRenderer head;
	public ModelRenderer jaw;
	public ModelRenderer frill;

	public ModelTFHydraHeadTrophyLegacy() {
		textureWidth = 512;
		textureHeight = 256;

		head = new ModelRenderer(this);
		head.setTextureOffset(272, 0).addBox(-16F, -14F, -16F, 32, 24, 32);
		head.setTextureOffset(272, 56).addBox(-15F, -2F, -40F, 30, 12, 24);
		head.setTextureOffset(272, 132).addBox(-15F, 10F, -4F, 30, 8, 16);
		head.setTextureOffset(128, 200).addBox(-2F, -30F, 4F, 4, 24, 24);
		head.setTextureOffset(272, 156).addBox(-12F, 10, -33F, 2, 5, 2);
		head.setTextureOffset(272, 156).addBox(10F, 10, -33F, 2, 5, 2);
		head.setTextureOffset(280, 156).addBox(-8F, 9, -33F, 16, 2, 2);
		head.setTextureOffset(280, 160).addBox(-10F, 9, -29F, 2, 2, 16);
		head.setTextureOffset(280, 160).addBox(8F, 9, -29F, 2, 2, 16);
		head.setRotationPoint(0F, 0F, 0F);

		jaw = new ModelRenderer(this);
		jaw.setRotationPoint(0F, 10F, -20F);
		jaw.setTextureOffset(272, 92).addBox(-15F, 0F, -16F, 30, 8, 32);
		jaw.setTextureOffset(272, 156).addBox(-10F, -5, -13F, 2, 5, 2);
		jaw.setTextureOffset(272, 156).addBox(8F, -5, -13F, 2, 5, 2);
		jaw.setTextureOffset(280, 156).addBox(-8F, -1, -13F, 16, 2, 2);
		jaw.setTextureOffset(280, 160).addBox(-10F, -1, -9F, 2, 2, 16);
		jaw.setTextureOffset(280, 160).addBox(8F, -1, -9F, 2, 2, 16);
		setRotation(jaw, 0F, 0F, 0F);
		head.addChild(jaw);

		frill = new ModelRenderer(this);
		frill.setRotationPoint(0F, 0F, -14F);
		frill.setTextureOffset(272, 200).addBox(-24F, -48.0F, 12F, 48, 48, 4);
		setRotation(frill, -0.5235988F, 0F, 0F);
		head.addChild(frill);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
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
		jaw.rotateAngleX = (float) (mouthOpen * (Math.PI / 3.0));
	}
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
