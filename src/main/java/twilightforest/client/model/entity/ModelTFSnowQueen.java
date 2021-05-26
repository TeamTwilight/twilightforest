package twilightforest.client.model.entity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.EntityTFSnowQueen;

/**
 * ModelSnowQueen - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFSnowQueen extends BipedModel<EntityTFSnowQueen> {

    public ModelRenderer crownFront;
    public ModelRenderer crownBack;
    public ModelRenderer crownRight;
    public ModelRenderer crownLeft;

    public ModelTFSnowQueen() {
        super(0, 0, 64, 64);
        this.crownRight = new ModelRenderer(this, 0, 0);
        this.crownRight.setRotationPoint(-4.0F, -6.0F, 0.0F);
        this.crownRight.setTextureOffset(24, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(crownRight, 0.39269908169872414F, 1.5707963267948966F, 0.0F);
        this.crownBack = new ModelRenderer(this, 0, 0);
        this.crownBack.setRotationPoint(0.0F, -6.0F, 4.0F);
        this.crownBack.setTextureOffset(44, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(crownBack, -0.39269908169872414F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody.setTextureOffset(32, 45).addBox(-4.5F, 10.0F, -2.5F, 9.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(14, 32).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.crownLeft = new ModelRenderer(this, 0, 0);
        this.crownLeft.setRotationPoint(4.0F, -6.0F, 0.0F);
        this.crownLeft.setTextureOffset(44, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(crownLeft, -0.39269908169872414F, 1.5707963267948966F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedRightLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(0, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.crownFront = new ModelRenderer(this, 0, 0);
        this.crownFront.setRotationPoint(0.0F, -6.0F, -4.0F);
        this.crownFront.setTextureOffset(24, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(crownFront, 0.39269908169872414F, 0.0F, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.addBox(0, 0, 0, 0, 0, 0);

        this.bipedHeadwear.addChild(this.crownRight);
        this.bipedHeadwear.addChild(this.crownBack);
        this.bipedHeadwear.addChild(this.crownLeft);
        this.bipedHeadwear.addChild(this.crownFront);
    }

    @Override
    public void setRotationAngles(EntityTFSnowQueen entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // in beam phase, arms forwards
        if (entity.getCurrentPhase() == EntityTFSnowQueen.Phase.BEAM) {
            if (entity.isBreathing()) {
                float f6 = MathHelper.sin(this.swingProgress * (float) Math.PI);
                float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
                this.bipedRightArm.rotateAngleZ = 0.0F;
                this.bipedLeftArm.rotateAngleZ = 0.0F;
                this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
                this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
                this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
                this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
                this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
                this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
                this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            } else {
                // arms up
                this.bipedRightArm.rotateAngleX += Math.PI;
                this.bipedLeftArm.rotateAngleX += Math.PI;
            }
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
