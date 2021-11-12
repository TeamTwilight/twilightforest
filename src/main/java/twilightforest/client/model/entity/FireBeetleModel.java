package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.monster.FireBeetle;

/**
 * ModelFireBeetle - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class FireBeetleModel extends HierarchicalModel<FireBeetle> {
    private final ModelPart root, head, rightLeg1, rightLeg2, rightLeg3, leftLeg1, leftLeg2, leftLeg3;

    public FireBeetleModel(ModelPart root) {
        this.root = root;

        this.head = this.root.getChild("head");

        this.rightLeg1 = this.root.getChild("right_leg_1");
        this.rightLeg2 = this.root.getChild("right_leg_2");
        this.rightLeg3 = this.root.getChild("right_leg_3");

        this.leftLeg1 = this.root.getChild("left_leg_1");
        this.leftLeg2 = this.root.getChild("left_leg_2");
        this.leftLeg3 = this.root.getChild("left_leg_3");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partRoot = mesh.getRoot();

        var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
                        .addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 18.0F, -4.0F));

        head.addOrReplaceChild("jaws", CubeListBuilder.create()
                        .texOffs(-6, 18)
                        .addBox(-3.0F, 0.0F, -6.0F, 6.0F, 0.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -6.0F, 0.39269908169872414F, 0.0F, 0.0F));

        head.addOrReplaceChild("right_eye", CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-2.5F, -1.0F, -4.5F));

        head.addOrReplaceChild("left_eye", CubeListBuilder.create()
                        .texOffs(16, 12)
                        .addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.5F, -1.0F, -4.5F));

        head.addOrReplaceChild("right_antenna", CubeListBuilder.create()
                        .texOffs(38, 4)
                        .addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, -1.5F, -5.0F, 0.0F, -0.7853981633974483F, 0.2181661564992912F));

        head.addOrReplaceChild("left_antenna", CubeListBuilder.create()
                        .texOffs(38, 6)
                        .addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, -1.5F, -5.0F, 0.0F, 0.7853981633974483F, -0.2181661564992912F));

        partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(22, 9)
                        .addBox(-6.0F, 0.0F, -4.0F, 12.0F, 14.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, -4.0F, 1.5707963267948966F, 0.0F, 0.0F));

        partRoot.addOrReplaceChild("right_leg_1", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 21.0F, -3.0F, 0.0F, -0.39269908169872414F, -0.2181661564992912F));

        partRoot.addOrReplaceChild("right_leg_2", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 21.0F, 0.0F, 0.0F, 0.2181661564992912F, -0.2181661564992912F));

        partRoot.addOrReplaceChild("right_leg_3", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, 21.0F, 4.0F, 0.0F, 0.39269908169872414F, -0.2181661564992912F));

        partRoot.addOrReplaceChild("left_leg_1", CubeListBuilder.create()
                        .mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 21.0F, -3.0F, 0.0F, 0.39269908169872414F, 0.2181661564992912F));

        partRoot.addOrReplaceChild("left_leg_2", CubeListBuilder.create()
                        .mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 21.0F, 0.0F, 0.0F, -0.2181661564992912F, 0.2181661564992912F));

        partRoot.addOrReplaceChild("left_leg_3", CubeListBuilder.create()
                        .mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 21.0F, 4.0F, 0.0F, -0.39269908169872414F, 0.2181661564992912F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(FireBeetle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;

        float legZ = Mth.PI / 11F;
        this.leftLeg1.zRot = legZ;
        this.rightLeg1.zRot = -legZ;
        this.leftLeg2.zRot = legZ * 0.74F;
        this.rightLeg2.zRot = -legZ * 0.74F;
        this.leftLeg3.zRot = legZ;
        this.rightLeg3.zRot = -legZ;

        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.leftLeg1.yRot = var10 * 2.0F + var9;
        this.rightLeg1.yRot = -var10 * 2.0F - var9;
        this.leftLeg2.yRot = var10 + var9;
        this.rightLeg2.yRot = -var10 - var9;
        this.leftLeg3.yRot = -var10 * 2.0F + var9;
        this.rightLeg3.yRot = var10 * 2.0F - var9;

        float var11 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float var12 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + Mth.PI) * 0.4F) * limbSwingAmount;
        float var14 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (Mth.PI * 1.5F)) * 0.4F) * limbSwingAmount;

        float var15 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float var16 = Math.abs(Mth.sin(limbSwing * 0.6662F + Mth.PI) * 0.4F) * limbSwingAmount;
        float var18 = Math.abs(Mth.sin(limbSwing * 0.6662F + (Mth.PI * 1.5F)) * 0.4F) * limbSwingAmount;

        this.leftLeg1.yRot += var11;
        this.rightLeg1.yRot += -var11;
        this.leftLeg2.yRot += var12;
        this.rightLeg2.yRot += -var12;
        this.leftLeg3.yRot += var14;
        this.rightLeg3.yRot += -var14;

        this.leftLeg1.zRot += var15;
        this.rightLeg1.zRot += -var15;

        this.leftLeg2.zRot += var16;
        this.rightLeg2.zRot += -var16;

        this.leftLeg3.zRot += var18;
        this.rightLeg3.zRot += -var18;
    }
}
