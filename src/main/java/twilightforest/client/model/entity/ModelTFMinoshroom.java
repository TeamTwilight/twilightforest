package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.EntityTFMinoshroom;

/**
 * ModelMinoshroom - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFMinoshroom extends BipedModel<EntityTFMinoshroom> {
    public ModelRenderer cowTorso;
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;

    public ModelTFMinoshroom() {
        super(0, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.rightFrontLeg = new ModelRenderer(this, 0, 0);
        this.rightFrontLeg.setRotationPoint(-4.0F, 12.0F, -6.0F);
        this.rightFrontLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, -6.0F, -9.0F);
        this.bipedBody.setTextureOffset(0, 29).addBox(-5.0F, -3.0F, 0.0F, 10.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(5.0F, -6.0F, -9.0F);
        this.bipedLeftArm.setTextureOffset(46, 15).addBox(0.0F, -3.0F, -0.0F, 4.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, -6.0F, -7.0F);
        this.bipedHead.addBox(-4.0F, -11.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(0, 16).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(32, 0).addBox(-8.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(32, 5).addBox(-8.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(46, 0).addBox(4.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(46, 5).addBox(6.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, -6.0F, -9.0F);
        this.bipedRightArm.setTextureOffset(28, 15).addBox(-4.0F, -3.0F, -0.0F, 4.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftBackLeg = new ModelRenderer(this, 0, 0);
        this.leftBackLeg.setRotationPoint(4.0F, 12.0F, 7.0F);
        this.leftBackLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.cowTorso = new ModelRenderer(this, 0, 0);
        this.cowTorso.setRotationPoint(0.0F, 10.0F, 6.0F);
        this.cowTorso.setTextureOffset(20, 36).addBox(-6.0F, -14.0F, -2.0F, 12.0F, 18.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.cowTorso.setTextureOffset(0, 20).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 6.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(cowTorso, 1.5707963267948966F, 0.0F, 0.0F);
        this.leftFrontLeg = new ModelRenderer(this, 0, 0);
        this.leftFrontLeg.setRotationPoint(4.0F, 12.0F, -6.0F);
        this.leftFrontLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.rightBackLeg = new ModelRenderer(this, 0, 0);
        this.rightBackLeg.setRotationPoint(-4.0F, 12.0F, 7.0F);
        this.rightBackLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.cowTorso, this.leftBackLeg, this.rightBackLeg, this.leftFrontLeg, this.rightFrontLeg);
    }

    @Override
    public void setRotationAngles(EntityTFMinoshroom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // copied from ModelBiped

        this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;

        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;

        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.leftArmPose != ArmPose.EMPTY) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        if (this.rightArmPose != ArmPose.EMPTY) {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }


        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        float var7 = 0.0F;
        float var8 = 0.0F;

        if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
            this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }

        if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.bipedHead.rotateAngleY;
            this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }

        // copied from ModelQuadruped
        this.cowTorso.rotateAngleX = ((float) Math.PI / 2F);
        this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        float f = ageInTicks - entity.ticksExisted;
        float f1 = entity.getChargeAnimationScale(f);
        f1 = f1 * f1;
        float f2 = 1.0F - f1;
        if (f1 > 0) {

            if (entity.getPrimaryHand() == HandSide.RIGHT) {
                this.bipedRightArm.rotateAngleX = f1 * -1.8F;
                this.bipedLeftArm.rotateAngleX = 0.0F;
                this.bipedRightArm.rotateAngleZ = -0.2F;
            } else {
                this.bipedRightArm.rotateAngleX = 0.0F;
                this.bipedLeftArm.rotateAngleX = f1 * -1.8F;
                this.bipedLeftArm.rotateAngleZ = 0.2F;
            }
            this.cowTorso.rotateAngleX = ((float) Math.PI / 2F) - f1 * (float) Math.PI * 0.2F;
            this.leftFrontLeg.rotationPointY = 12.0F + (-5.8F * f1);
            this.leftFrontLeg.rotationPointZ = -4.0F + (-5.8F * f1);
            this.leftFrontLeg.rotateAngleX -= f1 * (float) Math.PI * 0.3F;

            this.rightFrontLeg.rotationPointY = this.leftFrontLeg.rotationPointY;
            this.rightFrontLeg.rotationPointZ = this.leftFrontLeg.rotationPointZ;
            this.rightFrontLeg.rotateAngleX -= f1 * (float) Math.PI * 0.3F;
            this.bipedBody.rotationPointY = -6F + -3.0F * f1;
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
