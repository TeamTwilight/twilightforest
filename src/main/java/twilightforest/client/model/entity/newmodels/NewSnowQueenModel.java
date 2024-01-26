package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.boss.SnowQueen;

/**
 * ModelSnowQueen - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewSnowQueenModel extends HumanoidModel<SnowQueen> {
    public NewSnowQueenModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                        .texOffs(32, 45)
                        .addBox(-4.5F, 10.0F, -2.5F, 9.0F, 14.0F, 5.0F),
                PartPose.ZERO);

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.ZERO);

        definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(14, 32)
                        .addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(1.9F, 12.0F, 0.0F));

        definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-1.9F, 12.0F, 0.0F));

        definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        var hat = definition.addOrReplaceChild("hat", CubeListBuilder.create()
                        .addBox(0, 0, 0, 0, 0, 0),
                PartPose.ZERO);

        hat.addOrReplaceChild("crown_front", CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, -4.0F, 0.39269908169872414F, 0.0F, 0.0F));

        hat.addOrReplaceChild("crown_right", CubeListBuilder.create()
                        .texOffs(24, 4)
                        .addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.39269908169872414F, 1.5707963267948966F, 0.0F));

        hat.addOrReplaceChild("crown_left", CubeListBuilder.create()
                        .texOffs(44, 4)
                        .addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, -0.39269908169872414F, 1.5707963267948966F, 0.0F));

        hat.addOrReplaceChild("crown_back", CubeListBuilder.create()
                        .texOffs(44, 0)
                        .addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, -6.0F, 4.0F, -0.39269908169872414F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SnowQueen entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // in beam phase, arms forwards
        if (entity.getCurrentPhase() == SnowQueen.Phase.BEAM) {
            if (entity.isBreathing()) {
                float f6 = Mth.sin(this.attackTime * Mth.PI);
                float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * Mth.PI);
                this.rightArm.zRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightArm.yRot = -(0.1F - f6 * 0.6F);
                this.leftArm.yRot = 0.1F - f6 * 0.6F;
                this.rightArm.xRot = -Mth.HALF_PI;
                this.leftArm.xRot = -Mth.HALF_PI;
                this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
                this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
                this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
                this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
            } else {
                // arms up
                this.rightArm.xRot += Mth.PI;
                this.leftArm.xRot += Mth.PI;
            }
        }
    }
}
