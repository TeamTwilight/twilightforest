package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.monster.HelmetCrab;

/**
 * ModelHelmetCrab - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewHelmetCrabModel extends HierarchicalModel<HelmetCrab> {
    public final ModelPart root;
	public final ModelPart body;
	public final ModelPart leftClaw;
	public final ModelPart rightClaw;
    public final ModelPart rightLeg1;
    public final ModelPart rightLeg2;
    public final ModelPart leftLeg1;
    public final ModelPart leftLeg2;

    public NewHelmetCrabModel(ModelPart root) {
        this.root = root;

        this.body = root.getChild("body");

        this.rightClaw = body.getChild("right_claw");
        this.leftClaw = body.getChild("left_claw");

        this.rightLeg1 = root.getChild("right_leg_1");
        this.rightLeg2 = root.getChild("right_leg_2");
        this.leftLeg1 = root.getChild("left_leg_1");
        this.leftLeg2 = root.getChild("left_leg_2");

    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F)
                        .texOffs(58, 0)
                        .addBox(-1.5F, -5.0F, -3.5F, 1.0F, 2.0F, 1.0F)
                        .texOffs(58, 3)
                        .addBox(0.5F, -5.0F, -3.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));

        var helmet = body.addOrReplaceChild("helmet", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 6.0F, 8.0F, 6.0F)
                        .texOffs(16, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.5F, -1.3089969389957472F, -0.2617993877991494F, 0.7463027588580033F));

        helmet.addOrReplaceChild("horns", CubeListBuilder.create()
                        .texOffs(18, 23)
                        .addBox(-11.5F, -12.0F, -0.67F, 23.0F, 9.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7853981633974483F, 0.0F));

        body.addOrReplaceChild("right_claw", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(-3.0F, 0.0F, -3.0F, 0.0F, 0.39269908169872414F, 0.0F));

        body.addOrReplaceChild("left_claw", CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, -3.0F, 0.0F, -0.39269908169872414F, 0.0F));

        definition.addOrReplaceChild("right_leg_1", CubeListBuilder.create()
                        .texOffs(32, 15)
                        .addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, 0.0F, 0.2181661564992912F, 0.4363323129985824F, -0.4363323129985824F));

        definition.addOrReplaceChild("left_leg_1", CubeListBuilder.create()
                        .texOffs(48, 19)
                        .addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, 0.0F, 0.2181661564992912F, -0.4363323129985824F, 0.4363323129985824F));

        definition.addOrReplaceChild("right_leg_2", CubeListBuilder.create()
                        .texOffs(32, 19)
                        .addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, -1.5F, 0.2181661564992912F, 0.0F, -0.4363323129985824F));

        definition.addOrReplaceChild("left_leg_2", CubeListBuilder.create()
                        .texOffs(48, 15)
                        .addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, -1.5F, 0.2181661564992912F, 0.0F, 0.4363323129985824F));


        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(HelmetCrab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        this.body.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.body.xRot = headPitch / (180F / (float) Math.PI);
        float f6 = ((float) Math.PI / 4F);
        this.rightLeg1.zRot = -f6 * 0.74F;
        this.leftLeg1.zRot = f6 * 0.74F;
        this.rightLeg2.zRot = -f6 * 0.74F;
        this.leftLeg2.zRot = f6 * 0.74F;
        float f7 = -0.0F;
        float f8 = 0.3926991F;
        this.rightLeg1.yRot = f8 + f7;
        this.leftLeg1.yRot = -f8 - f7;
        this.rightLeg2.yRot = -f8 + f7;
        this.leftLeg2.yRot = f8 - f7;
        float f10 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f11 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f14 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f15 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        this.rightLeg1.yRot += f10;
        this.leftLeg1.yRot -= f10;
        this.rightLeg2.yRot += f11;
        this.leftLeg2.yRot -= f11;
        this.rightLeg1.zRot += f14;
        this.leftLeg1.zRot -= f14;
        this.rightLeg2.zRot += f15;
        this.leftLeg2.zRot -= f15;

        // swing right arm as if it were an arm, not a leg
        this.leftClaw.yRot = -0.319531F;
        this.leftClaw.yRot += -(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F) / 2;
        this.rightClaw.yRot = 0.319531F;
        this.rightClaw.yRot += (Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F) / 2;

    }
}
