package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFBighorn;

/**
 * ModelBighornSheep - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFBighorn<T extends EntityTFBighorn> extends SheepModel<T> {

    public ModelRenderer rightHorn;
    public ModelRenderer leftHorn;

    public ModelTFBighorn() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.legFrontRight = new ModelRenderer(this, 0, 0);
        this.legFrontRight.setRotationPoint(3.0F, 12.0F, -3.0F);
        this.legFrontRight.setTextureOffset(16, 32).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.legBackRight = new ModelRenderer(this, 0, 0);
        this.legBackRight.setRotationPoint(-3.5F, 12.0F, 9.0F);
        this.legBackRight.setTextureOffset(0, 48).addBox(-1.5F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 10.0F, 6.0F);
        this.body.setTextureOffset(34, 13).addBox(-4.5F, -14.0F, -3.0F, 9.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 0);
        this.legBackLeft.setRotationPoint(3.5F, 12.0F, 9.0F);
        this.legBackLeft.setTextureOffset(16, 48).addBox(-2.5F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 0);
        this.legFrontRight.setRotationPoint(-3.0F, 12.0F, -3.0F);
        this.legFrontRight.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftHorn = new ModelRenderer(this, 0, 0);
        this.leftHorn.setRotationPoint(2.0F, -3.0F, -1.0F);
        this.leftHorn.setTextureOffset(16, 0).addBox(0.0F, -1.0F, -7.0F, 3.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftHorn.setTextureOffset(16, 8).addBox(1.0F, 2.0F, -9.0F, 3.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftHorn.setTextureOffset(20, 15).addBox(2.0F, 0.0F, -11.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftHorn, 0.0F, -0.39269908169872414F, -0.2181661564992912F);
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.setRotationPoint(0.0F, 5.0F, -8.0F);
        this.headModel.setTextureOffset(38, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.rightHorn = new ModelRenderer(this, 0, 0);
        this.rightHorn.setRotationPoint(-2.0F, -3.0F, -1.0F);
        this.rightHorn.addBox(-3.0F, -1.0F, -7.0F, 3.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.rightHorn.setTextureOffset(0, 8).addBox(-4.0F, 2.0F, -9.0F, 3.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.rightHorn.setTextureOffset(4, 15).addBox(-4.0F, 0.0F, -11.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightHorn, 0.0F, 0.39269908169872414F, 0.2181661564992912F);
        this.headModel.addChild(this.leftHorn);
        this.headModel.addChild(this.rightHorn);
    }



    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.isChild) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0, 0.5, 0.25);
            ImmutableList.of(this.headModel).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            ImmutableList.of(this.legBackLeft, this.legBackRight, this.body, this.legFrontLeft, this.legFrontRight).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.pop();
        } else {
            ImmutableList.of(this.legBackLeft, this.legBackRight, this.body, this.legFrontLeft, this.legFrontRight, this.headModel).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
