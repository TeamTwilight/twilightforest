package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFMinoshroomHead extends ModelTFGenericHead {

	public ModelRenderer head;

	public ModelTFMinoshroomHead() {

		this.textureWidth = 64;
		this.textureHeight = 64;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.head.addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(0, 16).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(32, 0).addBox(-8.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(32, 5).addBox(-8.0F, -11.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(46, 0).addBox(4.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(46, 5).addBox(6.0F, -11.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);

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
