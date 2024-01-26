package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.monster.Kobold;

/**
 * ModelKobold - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewKoboldModel extends HumanoidModel<Kobold> {

    public final ModelPart mouth;

    boolean isJumping;

    public NewKoboldModel(ModelPart root) {
        super(root);
        this.mouth = getHead().getChild("mouth");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.5F, -6.0F, -3.0F, 7.0F, 6.0F, 6.0F)
                        .texOffs(20, 0)
                        .addBox(-1.5F, -3.0F, -6.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        definition.addOrReplaceChild("hat", CubeListBuilder.create(),
                PartPose.ZERO);

        head.addOrReplaceChild("mouth", CubeListBuilder.create()
                        .texOffs(26, 5)
                        .addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, 0.2181661564992912F, 0.0F, 0.0F));

        head.addOrReplaceChild("right_ear", CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(-3.0F, -4.0F, 0.0F, 0.0F, 0.0F, -1.3089969389957472F));

        head.addOrReplaceChild("left_ear", CubeListBuilder.create()
                        .texOffs(42, 0)
                        .addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(3.0F, -4.0F, 0.0F, 0.0F, 0.0F, 1.3089969389957472F));

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(12, 12)
                        .addBox(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(34, 12)
                        .addBox(-2.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F),
                PartPose.offset(-4.5F, 13.0F, 0.0F));

        definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(34, 22)
                        .addBox(-1.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F),
                PartPose.offset(4.5F, 13.0F, 0.0F));

        definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-1.9F, 19.0F, 0.0F));

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(1.9F, 19.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void prepareMobModel(Kobold entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        // check if entity is jumping
        this.isJumping = entity.getDeltaMovement().y() > 0;
    }

    @Override
    public void setupAnim(Kobold entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;

        this.rightArm.xRot = -((float) Math.PI * .15F);
        this.leftArm.xRot = -((float) Math.PI * .15F);

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;

        this.rightArm.zRot += Mth.cos(ageInTicks * 0.19F) * 0.15F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.19F) * 0.15F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.267F) * 0.25F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.267F) * 0.25F;

        if (this.isJumping) {
            // open jaw
            this.mouth.xRot = 0.6F;
        } else {
            this.mouth.xRot = 0.20944F;
        }
    }
}
