package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

//[VanillaCopy] of GenericHeadModel, but modified so our classes can use it
public abstract class ModelTFGenericHead extends Model {

	protected final ModelRenderer head;

	public ModelTFGenericHead() {
		super(RenderType::getEntityTranslucent);
		this.textureWidth = 32;
		this.textureHeight = 512;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		this.head.setRotationPoint(0.0F, -4.0F, 0.0F);
	}

	public void setRotations(float x, float y, float z) {
		this.head.rotateAngleY = y * ((float) Math.PI / 180F);
		this.head.rotateAngleX = x * ((float) Math.PI / 180F);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}
