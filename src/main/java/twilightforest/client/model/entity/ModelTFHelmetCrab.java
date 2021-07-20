package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFHelmetCrab;

/**
 * ModelHelmetCrab - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFHelmetCrab extends SegmentedModel<EntityTFHelmetCrab> {
    public ModelRenderer body;
    public ModelRenderer rightLeg1;
    public ModelRenderer rightLeg2;
    public ModelRenderer leftLeg1;
    public ModelRenderer leftLeg2;
    public ModelRenderer leftClaw;
    public ModelRenderer rightClaw;
    public ModelRenderer helmet;
    public ModelRenderer horns;

    public ModelTFHelmetCrab() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftLeg1 = new ModelRenderer(this, 0, 0);
        this.leftLeg1.setRotationPoint(2.0F, 21.0F, 0.0F);
        this.leftLeg1.setTextureOffset(48, 19).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg1, 0.2181661564992912F, -0.4363323129985824F, 0.4363323129985824F);
        this.helmet = new ModelRenderer(this, 0, 0);
        this.helmet.setRotationPoint(0.0F, -1.0F, 0.5F);
        this.helmet.setTextureOffset(40, 0).addBox(-4.0F, -8.0F, -4.0F, 6.0F, 8.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.helmet.setTextureOffset(16, 0).addBox(-4.0F, -8.0F, -4.0F, 6.0F, 8.0F, 6.0F, -0.25F, -0.25F, -0.25F);
        this.setRotateAngle(helmet, -1.3089969389957472F, -0.2617993877991494F, 0.7463027588580033F);
        this.leftLeg2 = new ModelRenderer(this, 0, 0);
        this.leftLeg2.setRotationPoint(2.0F, 21.0F, -1.5F);
        this.leftLeg2.setTextureOffset(48, 15).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg2, 0.2181661564992912F, 0.0F, 0.4363323129985824F);
        this.leftClaw = new ModelRenderer(this, 0, 0);
        this.leftClaw.setRotationPoint(3.0F, 0.0F, -3.0F);
        this.leftClaw.setTextureOffset(0, 23).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftClaw, 0.0F, -0.39269908169872414F, 0.0F);
        this.horns = new ModelRenderer(this, 0, 0);
        this.horns.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horns.setTextureOffset(18, 23).addBox(-11.5F, -12.0F, -0.67F, 23.0F, 9.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horns, 0.0F, 0.7853981633974483F, 0.0F);
        this.rightLeg1 = new ModelRenderer(this, 0, 0);
        this.rightLeg1.setRotationPoint(-2.0F, 21.0F, 0.0F);
        this.rightLeg1.setTextureOffset(32, 15).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg1, 0.2181661564992912F, 0.4363323129985824F, -0.4363323129985824F);
        this.rightClaw = new ModelRenderer(this, 0, 0);
        this.rightClaw.setRotationPoint(-3.0F, 0.0F, -3.0F);
        this.rightClaw.addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightClaw, 0.0F, 0.39269908169872414F, 0.0F);
        this.rightLeg2 = new ModelRenderer(this, 0, 0);
        this.rightLeg2.setRotationPoint(-2.0F, 21.0F, -1.5F);
        this.rightLeg2.setTextureOffset(32, 19).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg2, 0.2181661564992912F, 0.0F, -0.4363323129985824F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.body.setTextureOffset(0, 9).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.body.setTextureOffset(58, 0).addBox(-1.5F, -5.0F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.body.setTextureOffset(58, 3).addBox(0.5F, -5.0F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.helmet);
        this.body.addChild(this.leftClaw);
        this.body.addChild(this.rightClaw);
        this.helmet.addChild(this.horns);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.isSitting) matrixStackIn.translate(0, -0.25F, 0);
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                body,
                rightLeg1,
                rightLeg2,
                leftLeg1,
                leftLeg2

        );
    }

    @Override
    public void setRotationAngles(EntityTFHelmetCrab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        this.body.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.body.rotateAngleX = headPitch / (180F / (float) Math.PI);
        float f6 = ((float) Math.PI / 4F);
        this.rightLeg1.rotateAngleZ = -f6 * 0.74F;
        this.leftLeg1.rotateAngleZ = f6 * 0.74F;
        this.rightLeg2.rotateAngleZ = -f6 * 0.74F;
        this.leftLeg2.rotateAngleZ = f6 * 0.74F;
        float f7 = -0.0F;
        float f8 = 0.3926991F;
        this.rightLeg1.rotateAngleY = f8 + f7;
        this.leftLeg1.rotateAngleY = -f8 - f7;
        this.rightLeg2.rotateAngleY = -f8 + f7;
        this.leftLeg2.rotateAngleY = f8 - f7;
        float f10 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f11 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f14 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f15 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        this.rightLeg1.rotateAngleY += f10;
        this.leftLeg1.rotateAngleY += -f10;
        this.rightLeg2.rotateAngleY += f11;
        this.leftLeg2.rotateAngleY += -f11;
        this.rightLeg1.rotateAngleZ += f14;
        this.leftLeg1.rotateAngleZ += -f14;
        this.rightLeg2.rotateAngleZ += f15;
        this.leftLeg2.rotateAngleZ += -f15;

        // swing right arm as if it were an arm, not a leg
        this.leftClaw.rotateAngleY = -0.319531F;
        this.leftClaw.rotateAngleY += -(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F) / 2;
        this.rightClaw.rotateAngleY = 0.319531F;
        this.rightClaw.rotateAngleY += (MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F) / 2;

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
