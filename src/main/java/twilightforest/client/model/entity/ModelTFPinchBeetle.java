package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFPinchBeetle;

/**
 * ModelPinchBeetle - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFPinchBeetle extends SegmentedModel<EntityTFPinchBeetle> {
    public ModelRenderer Head;
    public ModelRenderer body;
    public ModelRenderer rightLeg1;
    public ModelRenderer rightLeg2;
    public ModelRenderer rightLeg3;
    public ModelRenderer leftLeg1;
    public ModelRenderer leftLeg2;
    public ModelRenderer leftLeg3;
    public ModelRenderer rightPincer;
    public ModelRenderer leftPincer;
    public ModelRenderer rightAntenna;
    public ModelRenderer leftAntenna;

    public ModelTFPinchBeetle() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leftLeg2 = new ModelRenderer(this, 0, 0);
        this.leftLeg2.setRotationPoint(2.0F, 21.0F, 4.0F);
        this.leftLeg2.setTextureOffset(40, 46).addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg2, 0.0F, -0.20943951023931953F, 0.17453292519943295F);
        this.leftAntenna = new ModelRenderer(this, 0, 0);
        this.leftAntenna.setRotationPoint(1.0F, -3.0F, -6.0F);
        this.leftAntenna.setTextureOffset(52, 0).addBox(0.0F, 0.0F, -10.0F, 1.0F, 0.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftAntenna, -0.4363323129985824F, -0.4363323129985824F, 0.0F);
        this.rightLeg3 = new ModelRenderer(this, 0, 0);
        this.rightLeg3.setRotationPoint(-2.0F, 21.0F, 2.0F);
        this.rightLeg3.setTextureOffset(40, 36).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg3, 0.0F, -0.20943951023931953F, -0.17453292519943295F);
        this.rightLeg2 = new ModelRenderer(this, 0, 0);
        this.rightLeg2.setRotationPoint(-2.0F, 21.0F, 4.0F);
        this.rightLeg2.setTextureOffset(40, 32).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg2, 0.0F, 0.20943951023931953F, -0.17453292519943295F);
        this.rightLeg1 = new ModelRenderer(this, 0, 0);
        this.rightLeg1.setRotationPoint(-2.0F, 21.0F, 6.0F);
        this.rightLeg1.setTextureOffset(40, 28).addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg1, 0.0F, 0.6108652381980153F, -0.17453292519943295F);
        this.leftPincer = new ModelRenderer(this, 0, 0);
        this.leftPincer.setRotationPoint(4.0F, 2.0F, -4.0F);
        this.leftPincer.setTextureOffset(16, 14).addBox(0.0F, 0.0F, -12.0F, 12.0F, 2.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftPincer, 0.08726646259971647F, 0.6108652381980153F, 0.0F);
        this.leftLeg1 = new ModelRenderer(this, 0, 0);
        this.leftLeg1.setRotationPoint(2.0F, 21.0F, 6.0F);
        this.leftLeg1.setTextureOffset(40, 42).addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg1, 0.0F, -0.6108652381980153F, 0.17453292519943295F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.Head.addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.body.setTextureOffset(0, 28).addBox(-5.0F, -8.0F, -3.0F, 10.0F, 10.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.leftLeg3 = new ModelRenderer(this, 0, 0);
        this.leftLeg3.setRotationPoint(2.0F, 21.0F, 2.0F);
        this.leftLeg3.setTextureOffset(40, 50).addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg3, 0.0F, 0.20943951023931953F, 0.17453292519943295F);
        this.rightPincer = new ModelRenderer(this, 0, 0);
        this.rightPincer.setRotationPoint(-4.0F, 2.0F, -4.0F);
        this.rightPincer.setTextureOffset(16, 0).addBox(-12.0F, 0.0F, -12.0F, 12.0F, 2.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightPincer, 0.08726646259971647F, -0.6108652381980153F, 0.0F);
        this.rightAntenna = new ModelRenderer(this, 0, 0);
        this.rightAntenna.setRotationPoint(-1.0F, -3.0F, -6.0F);
        this.rightAntenna.setTextureOffset(48, 0).addBox(-1.0F, 0.0F, -10.0F, 1.0F, 0.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightAntenna, -0.4363323129985824F, 0.4363323129985824F, 0.0F);
        this.Head.addChild(this.leftAntenna);
        this.Head.addChild(this.leftPincer);
        this.Head.addChild(this.rightPincer);
        this.Head.addChild(this.rightAntenna);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                Head,
                body,
                leftLeg1,
                leftLeg2,
                leftLeg3,
                rightLeg1,
                rightLeg2,
                rightLeg3
        );
    }

    @Override
    public void setRotationAngles(EntityTFPinchBeetle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.Head.rotateAngleX = headPitch / (180F / (float) Math.PI);

        float legZ = ((float) Math.PI / 11F);
        this.leftLeg1.rotateAngleZ = legZ;
        this.rightLeg1.rotateAngleZ = -legZ;
        this.leftLeg2.rotateAngleZ = legZ * 0.74F;
        this.rightLeg2.rotateAngleZ = -legZ * 0.74F;
        this.leftLeg3.rotateAngleZ = legZ;
        this.rightLeg3.rotateAngleZ = -legZ;

        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.leftLeg1.rotateAngleY = -var10 * 2.0F + var9;
        this.rightLeg1.rotateAngleY = var10 * 2.0F - var9;
        this.leftLeg2.rotateAngleY = var10 * 1.0F + var9;
        this.rightLeg2.rotateAngleY = -var10 * 1.0F - var9;
        this.leftLeg3.rotateAngleY = var10 * 2.0F + var9;
        this.rightLeg3.rotateAngleY = -var10 * 2.0F - var9;

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
    }

    @Override
    public void setLivingAnimations(EntityTFPinchBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        if (entity.isBeingRidden()) {
            // open jaws
            this.rightPincer.rotateAngleY = -0.3490658503988659F;
            this.leftPincer.rotateAngleY = 0.3490658503988659F;
        } else {
            // close jaws
            this.rightPincer.rotateAngleY = -0.7853981633974483F;
            this.leftPincer.rotateAngleY = 0.7853981633974483F;
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
