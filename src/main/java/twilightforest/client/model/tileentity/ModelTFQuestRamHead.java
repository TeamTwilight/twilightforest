package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTFQuestRamHead extends ModelTFGenericHead {

	public ModelRenderer horns;
	public ModelRenderer head;
	
	public ModelTFQuestRamHead() {
		textureWidth = 128;
		textureHeight = 128;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.head.setTextureOffset(74, 70).addBox(-6.0F, -4.0F, -10.0F, 12.0F, 8.0F, 15.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(42, 71).addBox(-6.0F, -7.0F, -6.0F, 12.0F, 3.0F, 11.0F, 0.0F, 0.0F, 0.0F);
		this.horns = new ModelRenderer(this, 0, 0);
		this.horns.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.horns.setTextureOffset(64, 0).addBox(-9.0F, -6.0F, -1.0F, 4.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
		this.horns.setTextureOffset(48, 0).addBox(-13.0F, -6.0F, 5.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
		this.horns.setTextureOffset(92, 0).addBox(5.0F, -6.0F, -1.0F, 4.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
		this.horns.setTextureOffset(110, 0).addBox(9.0F, -6.0F, 5.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(horns, -0.4363323129985824F, 0.0F, 0.0F);
		this.head.addChild(this.horns);
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
	
	@Override
	public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
