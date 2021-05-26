package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFSlimeBeetle;

/**
 * ModelSlimeBeetle - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFSlimeBeetle extends SegmentedModel<EntityTFSlimeBeetle> {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer tailBottom;
    public ModelRenderer rightLeg1;
    public ModelRenderer rightLeg2;
    public ModelRenderer rightLeg3;
    public ModelRenderer leftLeg1;
    public ModelRenderer leftLeg2;
    public ModelRenderer leftLeg3;
    public ModelRenderer rightAntenna;
    public ModelRenderer leftAntenna;
    public ModelRenderer rightEye;
    public ModelRenderer leftEye;
    public ModelRenderer tailTop;
    public ModelRenderer slime;
    public ModelRenderer slimeCenter;

    private final boolean translucent;

    public ModelTFSlimeBeetle() {
        this(false);
    }

    public ModelTFSlimeBeetle(boolean translucent) {
        this.translucent = translucent;
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 17.0F, -8.0F);
        this.body.setTextureOffset(32, 8).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.rightLeg1 = new ModelRenderer(this, 0, 0);
        this.rightLeg1.setRotationPoint(-2.0F, 20.0F, -6.0F);
        this.rightLeg1.setTextureOffset(40, 0).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg1, 0.0F, -0.4363323129985824F, -0.4363323129985824F);
        this.tailBottom = new ModelRenderer(this, 0, 0);
        this.tailBottom.setRotationPoint(0.0F, 18.0F, 2.0F);
        this.tailBottom.setTextureOffset(0, 34).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.slime = new ModelRenderer(this, 16, 40);
        this.slime.setRotationPoint(0.0F, -8.0F, 2.0F);
        this.slime.addBox(-6.0F, -12.0F, -7.0F, 12.0F, 12.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.slimeCenter = new ModelRenderer(this, 0, 18);
        this.slimeCenter.setRotationPoint(0.0F, -9.0F, 2.0F);
        this.slimeCenter.addBox(-4.0F, -9.0F, -5.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.rightEye = new ModelRenderer(this, 0, 0);
        this.rightEye.setRotationPoint(-2.5F, -1.0F, -4.5F);
        this.rightEye.setTextureOffset(0, 12).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftEye = new ModelRenderer(this, 0, 0);
        this.leftEye.setRotationPoint(2.5F, -1.0F, -4.5F);
        this.leftEye.setTextureOffset(16, 12).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightAntenna = new ModelRenderer(this, 0, 0);
        this.rightAntenna.setRotationPoint(-0.5F, -1.5F, -5.0F);
        this.rightAntenna.setTextureOffset(38, 4).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightAntenna, 0.0F, -0.7853981633974483F, 0.7853981633974483F);
        this.leftLeg2 = new ModelRenderer(this, 0, 0);
        this.leftLeg2.mirror = true;
        this.leftLeg2.setRotationPoint(2.0F, 20.0F, -4.0F);
        this.leftLeg2.setTextureOffset(40, 0).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg2, 0.0F, -0.2181661564992912F, 0.4363323129985824F);
        this.tailTop = new ModelRenderer(this, 0, 0);
        this.tailTop.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.tailTop.setTextureOffset(32, 28).addBox(-3.0F, -9.0F, -1.0F, 6.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.rightLeg3 = new ModelRenderer(this, 0, 0);
        this.rightLeg3.setRotationPoint(-2.0F, 20.0F, -2.0F);
        this.rightLeg3.setTextureOffset(40, 0).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg3, 0.0F, 0.7853981633974483F, -0.4363323129985824F);
        this.leftAntenna = new ModelRenderer(this, 0, 0);
        this.leftAntenna.setRotationPoint(0.5F, -1.5F, -5.0F);
        this.leftAntenna.setTextureOffset(38, 6).addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftAntenna, 0.0F, 0.7853981633974483F, -0.7853981633974483F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 17.0F, -8.0F);
        this.head.addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.rightLeg2 = new ModelRenderer(this, 0, 0);
        this.rightLeg2.setRotationPoint(-2.0F, 20.0F, -4.0F);
        this.rightLeg2.setTextureOffset(40, 0).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg2, 0.0F, 0.2181661564992912F, -0.4363323129985824F);
        this.leftLeg3 = new ModelRenderer(this, 0, 0);
        this.leftLeg3.mirror = true;
        this.leftLeg3.setRotationPoint(2.0F, 20.0F, -2.0F);
        this.leftLeg3.setTextureOffset(40, 0).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg3, 0.0F, -0.7853981633974483F, 0.4363323129985824F);
        this.leftLeg1 = new ModelRenderer(this, 0, 0);
        this.leftLeg1.mirror = true;
        this.leftLeg1.setRotationPoint(2.0F, 20.0F, -6.0F);
        this.leftLeg1.setTextureOffset(40, 0).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg1, 0.0F, 0.4363323129985824F, 0.4363323129985824F);
        this.tailTop.addChild(translucent ? this.slime : this.slimeCenter);
        this.head.addChild(this.rightEye);
        this.head.addChild(this.leftEye);
        this.head.addChild(this.rightAntenna);
        this.tailBottom.addChild(this.tailTop);
        this.head.addChild(this.leftAntenna);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                head,
                body,
                leftLeg1,
                leftLeg2,
                leftLeg3,
                rightLeg1,
                rightLeg2,
                rightLeg3,
                tailBottom
        );
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        tailBottom.render(stack, builder, light, overlay, red, green, blue, alpha);

        if (!translucent) {
            getParts().forEach((part) -> part.render(stack, builder, light, overlay, red, green, blue, alpha));
        }
    }

    @Override
    public void setRotationAngles(EntityTFSlimeBeetle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);

        // legs!
        float legZ = ((float) Math.PI / 11F);
        this.leftLeg1.rotateAngleZ = legZ;
        this.rightLeg1.rotateAngleZ = -legZ;
        this.leftLeg2.rotateAngleZ = legZ * 0.74F;
        this.rightLeg2.rotateAngleZ = -legZ * 0.74F;
        this.leftLeg3.rotateAngleZ = legZ;
        this.rightLeg3.rotateAngleZ = -legZ;

        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.leftLeg1.rotateAngleY = var10 * 2.0F + var9;
        this.rightLeg1.rotateAngleY = -var10 * 2.0F - var9;
        this.leftLeg2.rotateAngleY = var10 + var9;
        this.rightLeg2.rotateAngleY = -var10 - var9;
        this.leftLeg3.rotateAngleY = -var10 * 2.0F + var9;
        this.rightLeg3.rotateAngleY = var10 * 2.0F - var9;

        float var11 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float var12 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float var14 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;

        float var15 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float var16 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float var18 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;

        this.leftLeg1.rotateAngleY += var11;
        this.rightLeg1.rotateAngleY += -var11;
        this.leftLeg2.rotateAngleY += var12;
        this.rightLeg2.rotateAngleY += -var12;
        this.leftLeg3.rotateAngleY += var14;
        this.rightLeg3.rotateAngleY += -var14;

        this.leftLeg1.rotateAngleZ += var15;
        this.rightLeg1.rotateAngleZ += -var15;

        this.leftLeg2.rotateAngleZ += var16;
        this.rightLeg2.rotateAngleZ += -var16;

        this.leftLeg3.rotateAngleZ += var18;
        this.rightLeg3.rotateAngleZ += -var18;

        // tail wiggle
        this.tailBottom.rotateAngleX = MathHelper.cos(ageInTicks * 0.3335F) * 0.15F;
        this.tailTop.rotateAngleX = MathHelper.cos(ageInTicks * 0.4445F) * 0.20F;
        this.slime.rotateAngleX = MathHelper.cos(ageInTicks * 0.5555F) * 0.25F;
        this.slimeCenter.rotateAngleX = MathHelper.cos(ageInTicks * 0.5555F + 0.25F) * 0.25F;
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
