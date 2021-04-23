package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFDeer;

/**
 * ModelWildDeer - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFDeer extends QuadrupedModel<EntityTFDeer> {
    public ModelRenderer neck;
    public ModelRenderer rightAntler;
    public ModelRenderer leftAntler;

    public ModelTFDeer() {
        super(12, 0.0F, false, 4.0F, 4.0F, 2.0F, 2.0F, 10);
        this.textureWidth = 64;
        this.textureHeight = 48;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 10.0F, 7.0F);
        this.body.setTextureOffset(36, 6).addBox(-3.0F, -14.0F, -2.0F, 6.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.rightAntler = new ModelRenderer(this, 0, 0);
        this.rightAntler.setRotationPoint(-1.0F, -4.0F, 0.0F);
        this.rightAntler.setTextureOffset(0, 16).addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightAntler, 0.0F, -0.39269908169872414F, -0.39269908169872414F);
        this.leftAntler = new ModelRenderer(this, 0, 0);
        this.leftAntler.setRotationPoint(1.0F, -4.0F, 0.0F);
        this.leftAntler.setTextureOffset(32, 16).addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftAntler, 0.0F, 0.39269908169872414F, 0.39269908169872414F);
        this.legFrontLeft = new ModelRenderer(this, 0, 0);
        this.legFrontLeft.setRotationPoint(2.0F, 12.0F, -4.5F);
        this.legFrontLeft.setTextureOffset(10, 0).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 0);
        this.legFrontRight.setRotationPoint(-2.0F, 12.0F, -4.5F);
        this.legFrontRight.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 0);
        this.legBackLeft.setRotationPoint(2.0F, 12.0F, 9.5F);
        this.legBackLeft.setTextureOffset(10, 15).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.headModel.setTextureOffset(24, 2).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.headModel.setTextureOffset(52, 0).addBox(-1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 22, 14);
        this.neck.setRotationPoint(0.0F, 8.0F, -5.0F);
        this.neck.addBox(-1.5F, -8.0F, -2.0F, 3.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.4363323129985824F, 0.0F, 0.0F);
        this.setRotateAngle(headModel, -0.4363323129985824F, 0.0F, 0.0F);
        this.legBackRight = new ModelRenderer(this, 0, 0);
        this.legBackRight.setRotationPoint(-2.0F, 12.0F, 9.5F);
        this.legBackRight.setTextureOffset(0, 15).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.headModel.addChild(this.rightAntler);
        this.headModel.addChild(this.leftAntler);
        this.neck.addChild(this.headModel);
    }

    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.neck);
    }

    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft);
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
        if (isChild) {
            stack.push();
            stack.scale(0.75F, 0.75F, 0.75F);
            stack.translate(0F, 0.95F, 0.15F);
            this.getHeadParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.pop();

            stack.push();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0F, 1.5F, 0F);
            this.getBodyParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.pop();
        } else {
            this.getHeadParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            this.getBodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
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
