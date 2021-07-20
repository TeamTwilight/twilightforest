package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFRaven;

/**
 * ModelForestRaven - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFRaven extends SegmentedModel<EntityTFRaven> {
    public ModelRenderer head;
    public ModelRenderer torso;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer tail;

    public ModelTFRaven() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.torso = new ModelRenderer(this, 0, 0);
        this.torso.setRotationPoint(0.0F, 18.5F, -2.0F);
        this.torso.setTextureOffset(0, 6).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(torso, -0.4363323129985824F, 0.0F, 0.0F);
        this.leftWing = new ModelRenderer(this, 0, 0);
        this.leftWing.setRotationPoint(2.0F, -1.0F, 2.0F);
        this.leftWing.setTextureOffset(14, 15).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftWing, 0.2617993877991494F, 0.0F, 0.0F);
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.leftLeg.setTextureOffset(14, 15).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftLeg, 0.7853981633974483F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 18.5F, -2.0F);
        this.head.addBox(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(9, 0).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.rightWing = new ModelRenderer(this, 0, 0);
        this.rightWing.setRotationPoint(-2.0F, -1.0F, 2.0F);
        this.rightWing.setTextureOffset(0, 15).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightWing, 0.2617993877991494F, 0.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 0);
        this.rightLeg.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.rightLeg.setTextureOffset(8, 15).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightLeg, 0.7853981633974483F, 0.0F, 0.0F);
        this.tail = new ModelRenderer(this, 0, 0);
        this.tail.setRotationPoint(0.0F, -1.5F, 6.0F);
        this.tail.setTextureOffset(8, 0).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tail, -0.4363323129985824F, 0.0F, 0.0F);
        this.torso.addChild(this.leftWing);
        this.torso.addChild(this.rightWing);
        this.torso.addChild(this.tail);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                head,
                torso,
                leftLeg,
                rightLeg
        );
    }

    @Override
    public void setRotationAngles(EntityTFRaven entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.rotateAngleX = headPitch / (180F / (float) Math.PI);
        head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        head.rotateAngleZ = netHeadYaw > 5 ? -0.2617994F : 0;

        leftLeg.rotateAngleX = MathHelper.cos (limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightWing.rotateAngleZ = ageInTicks;
        leftWing.rotateAngleZ = -ageInTicks;

        if (entity.isBirdLanded()) {
            rightLeg.rotationPointY = 21;
            leftLeg.rotationPointY = 21;
        } else {
            rightLeg.rotationPointY = 20F;
            leftLeg.rotationPointY = 20F;
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
