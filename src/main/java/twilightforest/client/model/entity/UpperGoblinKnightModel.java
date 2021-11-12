package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.monster.UpperGoblinKnight;

/**
 * ModelTFGoblinKnightUpper - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class UpperGoblinKnightModel extends HumanoidModel<UpperGoblinKnight> {
    public ModelPart breastplate;
    public ModelPart spear;
    public ModelPart shield;

    public UpperGoblinKnightModel(ModelPart root) {
        super(root);

        this.breastplate = this.body.getChild("breastplate");
        this.spear = this.rightArm.getChild("spear");
        this.shield = this.leftArm.getChild("shield");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partRoot = mesh.getRoot();

        var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-8.0F, -14.0F, -1.9F, 16.0F, 14.0F, 2.0F)
                        .texOffs(116, 0)
                        .addBox(-6.0F, -12.0F, -0.9F, 4.0F, 2.0F, 2.0F)
                        .texOffs(116, 4)
                        .addBox(2.0F, -12.0F, -1.0F, 4.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0F, -0.7853981633974483F, 0.0F));

        partRoot.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //turns out, putting this as the hat doesnt allow us to rotate it at a 45 degree angle, so we have to make it its own piece
        head.addOrReplaceChild("helm", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.5F, 0.0F, -3.5F, 7.0F, 11.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, 0.7853981633974483F, 0.0F));

        var body = partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-5.5F, 0.0F, -2.0F, 11.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        body.addOrReplaceChild("breastplate", CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(-6.5F, 0.0F, -3.0F, 13.0F, 12.0F, 6.0F),
                PartPose.offset(0.0F, -0.5F, 0.0F));

        var rightArm = partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(44, 16)
                        .addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offsetAndRotation(-5.5F, 14.0F, 0.0F, -2.3876104699914644F, 0.0F, 0.10000736647217022F));

        var leftArm = partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(44, 32)
                        .addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offsetAndRotation(5.5F, 14.0F, 0.0F, 0.20001473294434044F, 0.0F, 0.10000736647217022F));

        rightArm.addOrReplaceChild("spear", CubeListBuilder.create()
                        .texOffs(108, 0)
                        .addBox(-1.0F, -19.0F, -1.0F, 2.0F, 40.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 8.5F, 0.0F, 1.5707963267948966F, 0.0F, 0.0F));

        leftArm.addOrReplaceChild("shield", CubeListBuilder.create()
                        .texOffs(63, 36)
                        .addBox(-6.0F, -6.0F, -2.0F, 12.0F, 20.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 6.083185105107944F, 0.0F, 0.0F));

        partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(30, 24)
                        .addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F),
                PartPose.offset(-4.0F, 20.0F, 0.0F));

        partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(30, 16)
                        .addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F),
                PartPose.offset(4.0F, 20.0F, 0.0F));


        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(UpperGoblinKnight entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean hasShield = entity.hasShield();
        boolean boat = entity.getVehicle() instanceof Boat;

        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.head.zRot = 0;
        this.hat.yRot = this.head.yRot;
        this.hat.xRot = this.head.xRot;
        this.hat.zRot = this.head.zRot;

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;

        float leftConstraint = hasShield ? -0.2F : limbSwingAmount;

        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * leftConstraint * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;

        if (this.riding && boat) {
            this.rightArm.xRot += (-(float)Math.PI / 5F);
            this.leftArm.xRot += (-(float)Math.PI / 5F);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = ((float)Math.PI / 10F);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (-(float)Math.PI / 10F);
            this.leftLeg.zRot = -0.07853982F;
        }

        if (this.leftArmPose != ArmPose.EMPTY) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }

        this.rightArmPose = ArmPose.ITEM;

        if (this.rightArmPose != ArmPose.EMPTY) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }

        this.rightArm.xRot -= (Math.PI * 0.66);

        // during swing move arm forward
        if (entity.heavySpearTimer > 0) {
            this.rightArm.xRot -= this.getArmRotationDuringSwing(60 - entity.heavySpearTimer) / (180F / (float) Math.PI);
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

        // fix shield so that it's always perpendicular to the floor
        this.shield.xRot = (float) (Math.PI * 2 - this.leftArm.xRot);

        this.breastplate.visible = entity.hasArmor();
        this.shield.visible = entity.hasShield();
    }

    /**
     *
     */
    private float getArmRotationDuringSwing(float attackTime) {
        if (attackTime <= 10) {
            // rock back
            return attackTime;
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
}
