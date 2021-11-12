package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.Minoshroom;

/**
 * ModelMinoshroom - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class MinoshroomModel extends HumanoidModel<Minoshroom> {
    public ModelPart cowTorso;
    public ModelPart rightFrontLeg;
    public ModelPart leftFrontLeg;
    public ModelPart rightBackLeg;
    public ModelPart leftBackLeg;

    public MinoshroomModel(ModelPart root) {
        super(root);
        this.cowTorso = root.getChild("cow_torso");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightBackLeg = root.getChild("right_back_leg");
        this.leftBackLeg = root.getChild("left_back_leg");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partRoot = mesh.getRoot();

        partRoot.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -11.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(0, 16)
                        .addBox(-3.0F, -6.0F, -5.0F, 6.0F, 3.0F, 1.0F)
                        .texOffs(32, 0)
                        .addBox(-8.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F)
                        .texOffs(32, 5)
                        .addBox(-8.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F)
                        .texOffs(46, 0)
                        .addBox(4.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F)
                        .texOffs(46, 5)
                        .addBox(6.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, -6.0F, -7.0F));

        partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 29)
                .addBox(-5.0F, -3.0F, 0.0F, 10.0F, 12.0F, 5.0F),
                PartPose.offset(0.0F, -6.0F, -9.0F));

        partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(46, 15)
                        .addBox(0.0F, -1.0F, -2.0F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(5.0F, -8.0F, -7.0F));

        partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(28, 15)
                        .addBox(-4.0F, -1.0F, -2.0F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(-5.0F, -8.0F, -7.0F));

        partRoot.addOrReplaceChild("cow_torso", CubeListBuilder.create()
                        .texOffs(20, 36)
                        .addBox(-6.0F, -14.0F, -2.0F, 12.0F, 18.0F, 10.0F)
                        .texOffs(0, 20)
                        .addBox(-2.0F, -2.0F, -3.0F, 4.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F, 1.5707963267948966F, 0.0F, 0.0F));

        partRoot.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, -6.0F));

        partRoot.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, -6.0F));

        partRoot.addOrReplaceChild("right_back_leg", CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, 7.0F));

        partRoot.addOrReplaceChild("left_back_leg", CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, 7.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.cowTorso, this.leftBackLeg, this.rightBackLeg, this.leftFrontLeg, this.rightFrontLeg);
    }

    @Override
    public void setupAnim(Minoshroom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // copied from HumanoidModel

        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.hat.yRot = this.head.yRot;
        this.hat.xRot = this.head.xRot;

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;

        if (this.leftArmPose != ArmPose.EMPTY) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }

        if (this.rightArmPose != ArmPose.EMPTY) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }


        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

        float var7 = 0.0F;
        float var8 = 0.0F;

        if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
            this.leftArm.zRot = 0.0F;
            this.leftArm.yRot = 0.1F - var7 * 0.6F + this.head.yRot + 0.4F;
            this.leftArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
            this.leftArm.xRot -= var7 * 1.2F - var8 * 0.4F;
            this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
        }

        if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
            this.rightArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - var7 * 0.6F) + this.head.yRot;
            this.rightArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
            this.rightArm.xRot -= var7 * 1.2F - var8 * 0.4F;
            this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        }

        // copied from QuadrepedModel
        this.cowTorso.xRot = ((float) Math.PI / 2F);
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftBackLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightBackLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        float f = ageInTicks - entity.tickCount;
        float f1 = entity.getChargeAnimationScale(f);
        f1 = f1 * f1;
        if (f1 > 0) {

            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArm.xRot = f1 * -1.8F;
                this.leftArm.xRot = 0.0F;
                this.rightArm.zRot = -0.2F;
            } else {
                this.rightArm.xRot = 0.0F;
                this.leftArm.xRot = f1 * -1.8F;
                this.leftArm.zRot = 0.2F;
            }
            this.cowTorso.xRot = ((float) Math.PI / 2F) - f1 * (float) Math.PI * 0.2F;
            this.leftFrontLeg.y = 12.0F + (-5.8F * f1);
            this.leftFrontLeg.z = -4.0F + (-5.8F * f1);
            this.leftFrontLeg.xRot -= f1 * (float) Math.PI * 0.3F;

            this.rightFrontLeg.y = this.leftFrontLeg.y;
            this.rightFrontLeg.z = this.leftFrontLeg.z;
            this.rightFrontLeg.xRot -= f1 * (float) Math.PI * 0.3F;
            this.body.y = -6F + -3.0F * f1;
        }
    }
}
