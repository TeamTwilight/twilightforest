package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.client.model.tileentity.ModelTFGenericHead;

public class ModelTFQuestRamHeadLegacy extends ModelTFGenericHead {

	public ModelRenderer neck;
	public ModelRenderer nose;
	public ModelRenderer head;

	public ModelTFQuestRamHeadLegacy() {
		textureWidth = 128;
		textureHeight = 128;
		
		neck = new ModelRenderer(this, 66, 37);
		neck.addBox(-5.5F, -8F, 0F, 11, 14, 12);
		neck.setRotationPoint(0F, -8F, -7F);

		setRotation(neck, 0.2617994F, 0F, 0F);

		head = new ModelRenderer(this/*, "head"*/);
		head.setRotationPoint(0F, -4F, -0F);

		head.setTextureOffset(0, 70).addBox(-6F, -4.5F, -7F, 12, 9, 15);
		head.setTextureOffset(0, 94).addBox(5F, -9F, 1F, 4, 4, 6);
		head.setTextureOffset(20, 96).addBox(7F, -8F, 6F, 3, 4, 4);
		head.setTextureOffset(34, 95).addBox(8F, -6F, 8F, 3, 6, 3);
		head.setTextureOffset(46, 98).addBox(9.5F, -2F, 6F, 3, 3, 3);
		head.setTextureOffset(58, 95).addBox(11F, 0F, 1F, 3, 3, 6);
		head.setTextureOffset(76, 95).addBox(12F, -4F, -1F, 3, 6, 3);
		head.setTextureOffset(88, 97).addBox(13F, -6F, 1F, 3, 3, 4);
		head.setTextureOffset(0, 94).addBox(-9F, -9F, 1F, 4, 4, 6);
		head.setTextureOffset(20, 96).addBox(-10F, -8F, 6F, 3, 4, 4);
		head.setTextureOffset(34, 95).addBox(-11F, -6F, 8F, 3, 6, 3);
		head.setTextureOffset(46, 98).addBox(-12.5F, -2F, 6F, 3, 3, 3);
		head.setTextureOffset(58, 95).addBox(-14F, 0F, 1F, 3, 3, 6);
		head.setTextureOffset(76, 95).addBox(-15F, -4F, -1F, 3, 6, 3);
		head.setTextureOffset(88, 97).addBox(-16F, -6F, 1F, 3, 3, 4);

		nose = new ModelRenderer(this, 54, 73);
		nose.addBox(-5.5F, -1F, -6F, 11, 9, 12);
		nose.setRotationPoint(0F, -7F, -1F);
		nose.setTextureSize(128, 128);
		setRotation(nose, 0.5235988F, 0F, 0F);
		head.addChild(nose);
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
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
