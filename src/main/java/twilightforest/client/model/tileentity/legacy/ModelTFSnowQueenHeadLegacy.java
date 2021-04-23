package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.client.model.tileentity.ModelTFGenericHead;

public class ModelTFSnowQueenHeadLegacy extends ModelTFGenericHead {

	public ModelRenderer head;
	public ModelRenderer crown;

	public ModelTFSnowQueenHeadLegacy() {
		textureWidth = 64;
		textureHeight = 32;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		this.head.setRotationPoint(0.0F, -4.0F, 0.0F);

		this.crown = new ModelRenderer(this, 0, 0);
		this.crown.addChild(makeFrontCrown(-1, -4, 10F));
		this.crown.addChild(makeFrontCrown(0, 4, -10F));
		this.crown.addChild(makeSideCrown(-1, -4, 10F));
		this.crown.addChild(makeSideCrown(0, 4, -10F));
		this.head.addChild(crown);
	}

	private ModelRenderer makeSideCrown(float spikeDepth, float crownX, float angle) {
		ModelRenderer crownSide = new ModelRenderer(this, 28, 28);
		crownSide.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1);
		crownSide.setRotationPoint(crownX, -6.0F, 0.0F);
		crownSide.rotateAngleY = 3.14159F / 2.0F;

		ModelRenderer spike4 = new ModelRenderer(this, 48, 27);
		spike4.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1);
		spike4.rotateAngleX = angle * 1.5F / 180F * 3.14159F;

		ModelRenderer spike3l = new ModelRenderer(this, 52, 28);
		spike3l.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3l.setRotationPoint(-2.5F, 0.0F, 0.0F);
		spike3l.rotateAngleX = angle / 180F * 3.14159F;
		spike3l.rotateAngleZ = -10F / 180F * 3.14159F;

		ModelRenderer spike3r = new ModelRenderer(this, 52, 28);
		spike3r.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3r.setRotationPoint(2.5F, 0.0F, 0.0F);
		spike3r.rotateAngleX = angle / 180F * 3.14159F;
		spike3r.rotateAngleZ = 10F / 180F * 3.14159F;

		crownSide.addChild(spike4);
		crownSide.addChild(spike3l);
		crownSide.addChild(spike3r);
		return crownSide;
	}

	private ModelRenderer makeFrontCrown(float spikeDepth, float crownZ, float angle) {
		ModelRenderer crownFront = new ModelRenderer(this, 28, 30);
		crownFront.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1);
		crownFront.setRotationPoint(0.0F, -6.0F, crownZ);

		ModelRenderer spike4 = new ModelRenderer(this, 48, 27);
		spike4.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1);
		spike4.rotateAngleX = angle * 1.5F / 180F * 3.14159F;

		ModelRenderer spike3l = new ModelRenderer(this, 52, 28);
		spike3l.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3l.setRotationPoint(-2.5F, 0.0F, 0.0F);
		spike3l.rotateAngleX = angle / 180F * 3.14159F;
		spike3l.rotateAngleZ = -10F / 180F * 3.14159F;

		ModelRenderer spike3r = new ModelRenderer(this, 52, 28);
		spike3r.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3r.setRotationPoint(2.5F, 0.0F, 0.0F);
		spike3r.rotateAngleX = angle / 180F * 3.14159F;
		spike3r.rotateAngleZ = 10F / 180F * 3.14159F;

		crownFront.addChild(spike4);
		crownFront.addChild(spike3l);
		crownFront.addChild(spike3r);
		return crownFront;
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
