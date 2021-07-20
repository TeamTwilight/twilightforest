package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.EntityTFHydra;

/**
 * ModelHydra - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFHydra extends SegmentedModel<EntityTFHydra> {
    public ModelRenderer torso;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer neck;
    public ModelRenderer neck_1;
    public ModelRenderer neck_2;
    public ModelRenderer tail;
    public ModelRenderer neck_3;
    public ModelRenderer neck_4;
    public ModelRenderer head;
    public ModelRenderer mouth;
    public ModelRenderer plate;
    public ModelRenderer neck_5;
    public ModelRenderer neck_6;
    public ModelRenderer head_1;
    public ModelRenderer mouth_1;
    public ModelRenderer plate_1;
    public ModelRenderer neck_7;
    public ModelRenderer neck_8;
    public ModelRenderer head_2;
    public ModelRenderer mouth_2;
    public ModelRenderer plate_2;
    public ModelRenderer tail_1;
    public ModelRenderer tail_2;
    public ModelRenderer tail_3;

    public ModelTFHydra() {
        this.textureWidth = 512;
        this.textureHeight = 256;
        this.tail = new ModelRenderer(this, 0, 0);
        this.tail.setRotationPoint(0.0F, 8.0F, 80.0F);
        this.tail.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck_5 = new ModelRenderer(this, 0, 0);
        this.neck_5.setRotationPoint(-8.0F, -24.0F, -16.0F);
        this.neck_5.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_5.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.tail_3 = new ModelRenderer(this, 0, 0);
        this.tail_3.setRotationPoint(0.0F, 0.0F, 32.0F);
        this.tail_3.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.tail_3.setTextureOffset(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck_2 = new ModelRenderer(this, 0, 0);
        this.neck_2.setRotationPoint(42.0F, -48.0F, 0.0F);
        this.neck_2.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_2.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck_4 = new ModelRenderer(this, 0, 0);
        this.neck_4.setRotationPoint(-16.0F, -24.0F, -16.0F);
        this.neck_4.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_4.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 0, 0);
        this.neck.setRotationPoint(-42.0F, -48.0F, 0.0F);
        this.neck.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck_7 = new ModelRenderer(this, 0, 0);
        this.neck_7.setRotationPoint(16.0F, -24.0F, -16.0F);
        this.neck_7.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_7.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(-8.0F, -24.0F, -16.0F);
        this.head.setTextureOffset(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F, 0.0F, 0.0F, 0.0F);
        this.neck_1 = new ModelRenderer(this, 0, 0);
        this.neck_1.setRotationPoint(0.0F, -58.0F, -16.0F);
        this.neck_1.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_1.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.plate_1 = new ModelRenderer(this, 0, 0);
        this.plate_1.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.plate_1.setTextureOffset(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.plate_1.setTextureOffset(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(plate_1, -0.7853981633974483F, 0.0F, 0.0F);
        this.mouth_1 = new ModelRenderer(this, 0, 0);
        this.mouth_1.setRotationPoint(0.0F, 8.0F, -16.0F);
        this.mouth_1.setTextureOffset(240, 162).addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.tail_2 = new ModelRenderer(this, 0, 0);
        this.tail_2.setRotationPoint(0.0F, 0.0F, 32.0F);
        this.tail_2.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.tail_2.setTextureOffset(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.torso = new ModelRenderer(this, 0, 0);
        this.torso.setRotationPoint(0.0F, -32.0F, 0.0F);
        this.torso.addBox(-45.0F, -12.0F, -20.0F, 90.0F, 96.0F, 40.0F, 0.0F, 0.0F, 0.0F);
        this.torso.setTextureOffset(88, 136).addBox(-2.0F, 20.0F, 20.0F, 4.0F, 16.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.torso.setTextureOffset(120, 136).addBox(-2.0F, 48.0F, 20.0F, 4.0F, 16.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(torso, 1.117010721276371F, 0.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 0);
        this.rightLeg.setRotationPoint(-40.0F, -20.0F, -12.0F);
        this.rightLeg.setTextureOffset(0, 136).addBox(-14.0F, -8.0F, -16.0F, 28.0F, 52.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.rightLeg.setTextureOffset(0, 220).addBox(-14.0F, 36.0F, -22.0F, 28.0F, 8.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.tail_1 = new ModelRenderer(this, 0, 0);
        this.tail_1.setRotationPoint(0.0F, 0.0F, 32.0F);
        this.tail_1.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.tail_1.setTextureOffset(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.head_2 = new ModelRenderer(this, 0, 0);
        this.head_2.setRotationPoint(8.0F, -24.0F, -16.0F);
        this.head_2.setTextureOffset(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.head_2.setTextureOffset(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.head_2.setTextureOffset(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F, 0.0F, 0.0F, 0.0F);
        this.mouth = new ModelRenderer(this, 0, 0);
        this.mouth.setRotationPoint(0.0F, 8.0F, -16.0F);
        this.mouth.setTextureOffset(240, 162).addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.head_1 = new ModelRenderer(this, 0, 0);
        this.head_1.setRotationPoint(0.0F, -32.0F, -16.0F);
        this.head_1.setTextureOffset(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.head_1.setTextureOffset(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.head_1.setTextureOffset(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F, 0.0F, 0.0F, 0.0F);
        this.plate = new ModelRenderer(this, 0, 0);
        this.plate.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.plate.setTextureOffset(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.plate.setTextureOffset(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(plate, -0.7853981633974483F, 0.0F, 0.0F);
        this.neck_6 = new ModelRenderer(this, 0, 0);
        this.neck_6.setRotationPoint(8.0F, -24.0F, -16.0F);
        this.neck_6.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_6.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.mouth_2 = new ModelRenderer(this, 0, 0);
        this.mouth_2.setRotationPoint(0.0F, 8.0F, -16.0F);
        this.mouth_2.setTextureOffset(240, 162).addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F, 0.0F, 0.0F, 0.0F);
        this.neck_8 = new ModelRenderer(this, 0, 0);
        this.neck_8.setRotationPoint(16.0F, -24.0F, -16.0F);
        this.neck_8.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_8.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.setRotationPoint(40.0F, -20.0F, -12.0F);
        this.leftLeg.setTextureOffset(120, 136).addBox(-14.0F, -8.0F, -16.0F, 28.0F, 52.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.leftLeg.setTextureOffset(68, 220).addBox(-14.0F, 36.0F, -22.0F, 28.0F, 8.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.plate_2 = new ModelRenderer(this, 0, 0);
        this.plate_2.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.plate_2.setTextureOffset(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.plate_2.setTextureOffset(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(plate_2, -0.7853981633974483F, 0.0F, 0.0F);
        this.neck_3 = new ModelRenderer(this, 0, 0);
        this.neck_3.setRotationPoint(-16.0F, -24.0F, -16.0F);
        this.neck_3.setTextureOffset(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.neck_3.addBox(-2.0F, -22.0F, 0.0F, 4.0F, 6.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.neck_1.addChild(this.neck_5);
        this.tail_2.addChild(this.tail_3);
        this.neck_3.addChild(this.neck_4);
        this.neck_2.addChild(this.neck_7);
        this.neck_4.addChild(this.head);
        this.head_1.addChild(this.plate_1);
        this.head_1.addChild(this.mouth_1);
        this.tail_1.addChild(this.tail_2);
        this.tail.addChild(this.tail_1);
        this.neck_8.addChild(this.head_2);
        this.head.addChild(this.mouth);
        this.neck_6.addChild(this.head_1);
        this.head.addChild(this.plate);
        this.neck_5.addChild(this.neck_6);
        this.head_2.addChild(this.mouth_2);
        this.neck_7.addChild(this.neck_8);
        this.head_2.addChild(this.plate_2);
        this.neck.addChild(this.neck_3);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                torso,
                leftLeg,
                rightLeg,
                tail
        );
    }

    @Override
    public void setRotationAngles(EntityTFHydra entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;

        leftLeg.rotateAngleY = 0.0F;
        rightLeg.rotateAngleY = 0.0F;
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
