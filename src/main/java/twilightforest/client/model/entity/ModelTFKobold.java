package twilightforest.client.model.entity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFKobold;

/**
 * ModelKobold - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFKobold extends BipedModel<EntityTFKobold> {

    public ModelRenderer mouth;
    public ModelRenderer rightEar;
    public ModelRenderer leftEar;

    boolean isJumping;

    public ModelTFKobold() {
        super(0, 0, 64, 32);
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-4.5F, 13.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(34, 12).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedHead.addBox(-3.5F, -6.0F, -3.0F, 7.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(20, 0).addBox(-1.5F, -3.0F, -6.0F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedBody.setTextureOffset(12, 12).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        this.bipedLeftLeg.setRotationPoint(1.9F, 19.0F, 0.0F);
        this.bipedLeftLeg.setTextureOffset(0, 20).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.mouth = new ModelRenderer(this, 0, 0);
        this.mouth.setRotationPoint(0.0F, -1.0F, -3.0F);
        this.mouth.setTextureOffset(26, 5).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(mouth, 0.2181661564992912F, 0.0F, 0.0F);
        this.rightEar = new ModelRenderer(this, 0, 0);
        this.rightEar.setRotationPoint(-3.0F, -4.0F, 0.0F);
        this.rightEar.setTextureOffset(32, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightEar, 0.0F, 0.0F, -1.3089969389957472F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        this.bipedRightLeg.setRotationPoint(-1.9F, 19.0F, 0.0F);
        this.bipedRightLeg.setTextureOffset(0, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(4.5F, 13.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(34, 22).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftEar = new ModelRenderer(this, 0, 0);
        this.leftEar.setRotationPoint(3.0F, -4.0F, 0.0F);
        this.leftEar.setTextureOffset(42, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftEar, 0.0F, 0.0F, 1.3089969389957472F);
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.addBox(0, 0, 0, 0, 0, 0);
        this.bipedHead.addChild(this.mouth);
        this.bipedHead.addChild(this.rightEar);
        this.bipedHead.addChild(this.leftEar);
    }

    @Override
    public void setLivingAnimations(EntityTFKobold entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        // check if entity is jumping
        this.isJumping = entity.getMotion().getY() > 0;
    }

    @Override
    public void setRotationAngles(EntityTFKobold entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);

        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;

        this.bipedRightArm.rotateAngleX = -((float) Math.PI * .15F);
        this.bipedLeftArm.rotateAngleX = -((float) Math.PI * .15F);

        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.19F) * 0.15F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.19F) * 0.15F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.267F) * 0.25F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.267F) * 0.25F;

        if (this.isJumping) {
            // open jaw
            this.mouth.rotateAngleX = 0.6F;
        } else {
            this.mouth.rotateAngleX = 0.20944F;
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
