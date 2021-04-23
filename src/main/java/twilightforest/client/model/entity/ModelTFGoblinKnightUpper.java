package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFGoblinKnightUpper;

/**
 * ModelTFGoblinKnightUpper - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFGoblinKnightUpper extends BipedModel<EntityTFGoblinKnightUpper> {
    public ModelRenderer breastplate;
    public ModelRenderer helmet;
    public ModelRenderer head;
    public ModelRenderer spear;
    public ModelRenderer shield;

    public ModelTFGoblinKnightUpper() {
        super(0, 0, 128, 64);
        this.bipedRightLeg = new ModelRenderer(this, 30, 24);
        this.bipedRightLeg.setRotationPoint(-4.0F, 20.0F, 0.0F);
        this.bipedRightLeg.addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(28, 0).addBox(-8.0F, -14.0F, -1.9F, 16.0F, 14.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(116, 0).addBox(-6.0F, -12.0F, -0.9F, 4.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(116, 4).addBox(2.0F, -12.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.0F, -0.7853981633974483F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 44, 16);
        this.bipedRightArm.setRotationPoint(-5.5F, 14.0F, 0.0F);
        this.bipedRightArm.addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bipedRightArm, -2.3876104699914644F, 0.0F, 0.10000736647217022F);
        this.spear = new ModelRenderer(this, 108, 0);
        this.spear.setRotationPoint(-2.0F, 8.5F, 0.0F);
        this.spear.addBox(-1.0F, -19.0F, -1.0F, 2.0F, 40.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(spear, 1.5707963267948966F, 0.0F, 0.0F);
        this.shield = new ModelRenderer(this, 63, 36);
        this.shield.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.shield.addBox(-6.0F, -6.0F, -2.0F, 12.0F, 20.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(shield, 6.083185105107944F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 18);
        this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedBody.addBox(-5.5F, 0.0F, -2.0F, 11.0F, 8.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedHead.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 44, 32);
        this.bipedLeftArm.setRotationPoint(5.5F, 14.0F, 0.0F);
        this.bipedLeftArm.addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bipedLeftArm, 0.20001473294434044F, 0.0F, 0.10000736647217022F);
        this.bipedLeftLeg = new ModelRenderer(this, 30, 16);
        this.bipedLeftLeg.setRotationPoint(4.0F, 20.0F, 0.0F);
        this.bipedLeftLeg.addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.helmet = new ModelRenderer(this, 0, 0);
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet.addBox(-3.5F, -11.0F, -3.5F, 7.0F, 11.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(helmet, 0.0F, 0.7853981633974483F, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedHeadwear.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.breastplate = new ModelRenderer(this, 64, 0);
        this.breastplate.setRotationPoint(0.0F, 11.5F, 0.0F);
        this.breastplate.addBox(-6.5F, 0.0F, -3.0F, 13.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.helmet.addChild(this.head);
        this.bipedRightArm.addChild(this.spear);
        this.bipedLeftArm.addChild(this.shield);
        this.bipedHeadwear.addChild(this.helmet);
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
        super.render(stack, builder, light, overlay, red, green, blue, scale);

        this.breastplate.render(stack, builder, light, overlay, red, green, blue, scale);
    }

    @Override
    public void setRotationAngles(EntityTFGoblinKnightUpper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean hasShield = entity.hasShield();

        this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.bipedHead.rotateAngleZ = 0;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;

        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;

        float leftConstraint = hasShield ? 0.2F : limbSwingAmount;

        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * leftConstraint * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;

        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isSitting) {
            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = 0;
            this.bipedLeftLeg.rotateAngleX = 0;
//            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
//            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        if (this.leftArmPose != ArmPose.EMPTY) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        this.rightArmPose = ArmPose.ITEM;

        if (this.rightArmPose != ArmPose.EMPTY) {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        bipedRightArm.rotateAngleX -= (Math.PI * 0.66);

        // during swing move arm forward
        if (entity.heavySpearTimer > 0) {
            bipedRightArm.rotateAngleX -= this.getArmRotationDuringSwing(60 - entity.heavySpearTimer) / (180F / (float) Math.PI);
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        // shield arm points somewhat inward
        this.bipedLeftArm.rotateAngleZ = -this.bipedLeftArm.rotateAngleZ;

        // fix shield so that it's always perpendicular to the floor
        this.shield.rotateAngleX = (float) (Math.PI * 2 - this.bipedLeftArm.rotateAngleX);

        this.breastplate.showModel = entity.hasArmor();
        this.shield.showModel = entity.hasShield();
    }

    /**
     *
     */
    private float getArmRotationDuringSwing(float attackTime) {
        if (attackTime <= 10) {
            // rock back
            return attackTime * 1.0F;
        }
        if (attackTime > 10 && attackTime <= 30) {
            // hang back
            return 10F;
        }
        if (attackTime > 30 && attackTime <= 33) {
            // slam forward
            return (attackTime - 30) * -8F + 10F;
        }
        if (attackTime > 33 && attackTime <= 50) {
            // stay forward
            return -15F;
        }
        if (attackTime > 50 && attackTime <= 60) {
            // back to normal
            return (10 - (attackTime - 50)) * -1.5F;
        }

        return 0;
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
