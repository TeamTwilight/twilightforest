package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFLichHead extends ModelTFGenericHead {

	public final ModelRenderer head;
	public final ModelRenderer crown;

	public ModelTFLichHead() {
		this(0, 0, 64, 64);
	}

	public ModelTFLichHead(int offsetX, int offsetY, int width, int height) {
		this.textureWidth = width;
		this.textureHeight = height;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4F, -8F, -4F, 8, 8, 8);
		this.head.setRotationPoint(0F, -4F, 0F);
		this.crown = new ModelRenderer(this, 32, 0);
		this.crown.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5F);
		this.crown.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.head.addChild(crown);
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
