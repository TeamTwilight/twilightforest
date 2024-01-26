package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Squirrel;

/**
 * ModelForestSquirrel - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewSquirrelModel extends QuadrupedModel<Squirrel> {

    public final ModelPart tail1;
    public final ModelPart tail2;

    public NewSquirrelModel(ModelPart root) {
        super(root, false, 4.0F, 4.0F, 2.0F, 2.0F, 24);

        this.tail1 = body.getChild("tail_1");
        this.tail2 = tail1.getChild("tail_2");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = QuadrupedModel.createBodyMesh(1, CubeDeformation.NONE);
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F)
                        .addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F)
                        .texOffs(0, 2)
                        .addBox(1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));

        var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        definition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.5F, 23.0F, -2.5F));

        definition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                        .texOffs(4, 16)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.5F, 23.0F, -2.5F));

        definition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.5F, 23.0F, 1.5F));

        definition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                        .texOffs(4, 18)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.5F, 23.0F, 1.5F));

        var tail1 = body.addOrReplaceChild("tail_1", CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, 2.530727415391778F, 0.0F, 0.0F));

        tail1.addOrReplaceChild("tail_2", CubeListBuilder.create()
                        .texOffs(13, 11)
                        .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 4.0F, 0.5F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(Squirrel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if (limbSwingAmount > 0.2) {
            float wiggle = Math.min(limbSwingAmount, 0.6F);
            this.tail2.xRot = (Mth.cos(ageInTicks * 0.6662F) - (float) Math.PI / 3) * wiggle;
            this.tail1.xRot = 2.5F + Mth.cos(ageInTicks * 0.7774F) * 1.2F * wiggle;
        } else {
            this.tail2.xRot = 0.2F + Mth.cos(ageInTicks * 0.3335F) * 0.25F;
            this.tail1.xRot = 2.5F + Mth.cos(ageInTicks * 0.4445F) * 0.20F;
        }
    }
}
