package twilightforest.client.model.entity.newmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.monster.LowerGoblinKnight;

/**
 * ModelTFGoblinKnightLower - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewLowerGoblinKnightModel extends HumanoidModel<LowerGoblinKnight> {
    public final ModelPart tunic;

    public NewLowerGoblinKnightModel(ModelPart root) {
        super(root);
        this.tunic = root.getChild("tunic");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 30)
                        .addBox(-2.5F, -5.0F, -3.5F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 8.0F, 1.0F));

        definition.addOrReplaceChild("hat", CubeListBuilder.create(),
                PartPose.ZERO);

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(-3.5F, 0.0F, -2.0F, 7.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        definition.addOrReplaceChild("tunic", CubeListBuilder.create()
                        .texOffs(64, 19)
                        .addBox(-6.0F, 0.0F, -3.0F, 12.0F, 9.0F, 6.0F),
                PartPose.offset(0.0F, 7.5F, 0.0F));

        definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(48, 48)
                        .addBox(-2.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(-3.5F, 10.0F, 0.0F, 0.0F, 0.0F, 0.10000000116728046F));

        definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(38, 48)
                        .addBox(0.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                PartPose.offsetAndRotation(3.5F, 10.0F, 0.0F, 0.0F, 0.0F, -0.10000736647217022F));

        definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-3.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(-2.5F, 16.0F, 0.0F));

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 52)
                        .addBox(-1.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(2.5F, 16.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
        super.renderToBuffer(stack, builder, light, overlay, red, green, blue, scale);
        this.tunic.render(stack, builder, light, overlay, red, green, blue, scale);
    }

    @Override
    public void setupAnim(LowerGoblinKnight entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.zRot = 0.0F;

        if (this.riding) {
            this.rightArm.xRot += (-(float)Math.PI / 5F);
            this.leftArm.xRot += (-(float)Math.PI / 5F);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = ((float)Math.PI / 10F);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (-(float)Math.PI / 10F);
            this.leftLeg.zRot = -0.07853982F;
        }

        if (entity.isVehicle()) {
            this.head.yRot = 0;
            this.head.xRot = 0;
            this.hat.yRot = this.head.yRot;
            this.hat.xRot = this.head.xRot;
        }

        if (this.leftArmPose != ArmPose.EMPTY) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }

        if (this.rightArmPose != ArmPose.EMPTY) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

        this.tunic.visible = entity.hasArmor();

        //dont swing the arms if its still wearing its armor
        if(entity.hasArmor()) {
            this.leftArm.xRot = 0;
            this.rightArm.xRot = 0;
            this.leftArm.zRot = 0;
            this.rightArm.zRot = 0;
        }
    }
}
