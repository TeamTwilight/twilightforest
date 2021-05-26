package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFSquirrel;

/**
 * ModelForestSquirrel - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFSquirrel extends SegmentedModel<EntityTFSquirrel> {
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;
    public ModelRenderer tail1;
    public ModelRenderer tail2;

    public ModelTFSquirrel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.rightFrontLeg = new ModelRenderer(this, 0, 0);
        this.rightFrontLeg.setRotationPoint(-1.5F, 23.0F, -2.5F);
        this.rightFrontLeg.setTextureOffset(0, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.body.setTextureOffset(0, 8).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftBackLeg = new ModelRenderer(this, 0, 0);
        this.leftBackLeg.setRotationPoint(1.5F, 23.0F, 1.5F);
        this.leftBackLeg.setTextureOffset(4, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 20.0F, -3.0F);
        this.head.addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.head.addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 2).addBox(1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 0, 0);
        this.tail2.setRotationPoint(0.0F, 4.0F, 0.5F);
        this.tail2.setTextureOffset(13, 11).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftFrontLeg = new ModelRenderer(this, 0, 0);
        this.leftFrontLeg.setRotationPoint(1.5F, 23.0F, -2.5F);
        this.leftFrontLeg.setTextureOffset(4, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tail1 = new ModelRenderer(this, 0, 0);
        this.tail1.setRotationPoint(0.0F, -3.0F, 2.0F);
        this.tail1.setTextureOffset(18, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tail1, 2.530727415391778F, 0.0F, 0.0F);
        this.rightBackLeg = new ModelRenderer(this, 0, 0);
        this.rightBackLeg.setRotationPoint(-1.5F, 23.0F, 1.5F);
        this.rightBackLeg.setTextureOffset(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tail1.addChild(this.tail2);
        this.body.addChild(this.tail1);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                body,
                leftFrontLeg,
                rightFrontLeg,
                leftBackLeg,
                rightBackLeg,
                head
        );
    }
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityTFSquirrel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if (limbSwingAmount > 0.2) {
            float wiggle = Math.min(limbSwingAmount, 0.6F);
            this.tail2.rotateAngleX = (MathHelper.cos(ageInTicks * 0.6662F) - (float) Math.PI / 3) * wiggle;
            this.tail1.rotateAngleX = 2.5F + MathHelper.cos(ageInTicks * 0.7774F) * 1.2F * wiggle;
        } else {
            this.tail2.rotateAngleX = 0.2F + MathHelper.cos(ageInTicks * 0.3335F) * 0.25F;
            this.tail1.rotateAngleX = 2.5F + MathHelper.cos(ageInTicks * 0.4445F) * 0.20F;
        }
    }
}
