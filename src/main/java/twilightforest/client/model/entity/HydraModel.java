package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import twilightforest.entity.boss.Hydra;

/**
 * ModelHydra - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class HydraModel extends HierarchicalModel<Hydra> {

    public ModelPart root, body, tail, rightLeg, leftLeg;

    public HydraModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("torso");
        this.tail = root.getChild("tail_1");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partRoot = mesh.getRoot();

        var head1 = partRoot.addOrReplaceChild("head_1", CubeListBuilder.create()
                        .texOffs(260, 64)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(236, 128)
                        .addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
                        .texOffs(356, 70)
                        .addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
                PartPose.offset(-8.0F, -24.0F, -16.0F));

        var head2 = partRoot.addOrReplaceChild("head_2", CubeListBuilder.create()
                        .texOffs(260, 64)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(236, 128)
                        .addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
                        .texOffs(356, 70)
                        .addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
                PartPose.offset(0.0F, -32.0F, -16.0F));

        var head3 = partRoot.addOrReplaceChild("head_3", CubeListBuilder.create()
                        .texOffs(260, 64)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(236, 128)
                        .addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
                        .texOffs(356, 70)
                        .addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
                PartPose.offset(8.0F, -24.0F, -16.0F));

        head1.addOrReplaceChild("mouth_1", CubeListBuilder.create()
                        .texOffs(240, 162)
                        .addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F),
                PartPose.offset(0.0F, 8.0F, -16.0F));

        head2.addOrReplaceChild("mouth_2", CubeListBuilder.create()
                        .texOffs(240, 162)
                        .addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F),
                PartPose.offset(0.0F, 8.0F, -16.0F));

        head3.addOrReplaceChild("mouth_3", CubeListBuilder.create()
                        .texOffs(240, 162)
                        .addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F),
                PartPose.offset(0.0F, 8.0F, -16.0F));

        head1.addOrReplaceChild("plate_1", CubeListBuilder.create()
                        .texOffs(388, 0)
                        .addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
                        .texOffs(220, 0)
                        .addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.7853981633974483F, 0.0F, 0.0F));

        head2.addOrReplaceChild("plate_2", CubeListBuilder.create()
                        .texOffs(388, 0)
                        .addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
                        .texOffs(220, 0)
                        .addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.7853981633974483F, 0.0F, 0.0F));

        head3.addOrReplaceChild("plate_3", CubeListBuilder.create()
                        .texOffs(388, 0)
                        .addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
                        .texOffs(220, 0)
                        .addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.7853981633974483F, 0.0F, 0.0F));

        var neck1 = partRoot.addOrReplaceChild("neck_1", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(-42.0F, -48.0F, 0.0F));

        var neck2 = partRoot.addOrReplaceChild("neck_2", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, -58.0F, -16.0F));

        var neck3 = partRoot.addOrReplaceChild("neck_3", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(42.0F, -48.0F, 0.0F));

        var neck4 = neck1.addOrReplaceChild("neck_4", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(-16.0F, -24.0F, -16.0F));

        var neck5 = neck2.addOrReplaceChild("neck_5", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(-16.0F, -24.0F, -16.0F));

        var neck6 = neck3.addOrReplaceChild("neck_6", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(-8.0F, -24.0F, -16.0F));

        neck4.addOrReplaceChild("neck_7", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(8.0F, -24.0F, -16.0F));

        neck5.addOrReplaceChild("neck_8", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(16.0F, -24.0F, -16.0F));

        neck6.addOrReplaceChild("neck_9", CubeListBuilder.create()
                        .texOffs(260, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(16.0F, -24.0F, -16.0F));


        partRoot.addOrReplaceChild("torso", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-45.0F, -12.0F, -20.0F, 90.0F, 96.0F, 40.0F)
                        .texOffs(88, 136)
                        .addBox(-2.0F, 20.0F, 20.0F, 4.0F, 16.0F, 12.0F)
                        .texOffs(120, 136)
                        .addBox(-2.0F, 48.0F, 20.0F, 4.0F, 16.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -32.0F, 0.0F, 1.117010721276371F, 0.0F, 0.0F));

        partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 136)
                        .addBox(-14.0F, -8.0F, -16.0F, 28.0F, 52.0F, 32.0F)
                        .texOffs(0, 220)
                        .addBox(-14.0F, 36.0F, -22.0F, 28.0F, 8.0F, 6.0F),
                PartPose.offset(-40.0F, -20.0F, -12.0F));

        partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(120, 136)
                        .addBox(-14.0F, -8.0F, -16.0F, 28.0F, 52.0F, 32.0F)
                        .texOffs(68, 220)
                        .addBox(-14.0F, 36.0F, -22.0F, 28.0F, 8.0F, 6.0F),
                PartPose.offset(40.0F, -20.0F, -12.0F));

        var tail1 = partRoot.addOrReplaceChild("tail_1", CubeListBuilder.create()
                        .texOffs(260, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0)
                        .addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 8.0F, 80.0F));

        var tail2 = tail1.addOrReplaceChild("tail_2", CubeListBuilder.create()
                        .texOffs(260, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0)
                        .addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 32.0F));

        var tail3 = tail2.addOrReplaceChild("tail_3", CubeListBuilder.create()
                        .texOffs(260, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0)
                        .addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 32.0F));

        tail3.addOrReplaceChild("tail_4", CubeListBuilder.create()
                        .texOffs(260, 0)
                        .addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
                        .texOffs(0, 0)
                        .addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 32.0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack p_170625_, VertexConsumer p_170626_, int p_170627_, int p_170628_, float p_170629_, float p_170630_, float p_170631_, float p_170632_) {
        this.leftLeg.render(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);
        this.rightLeg.render(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);
        this.body.render(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);
        this.tail.render(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);

    }

    @Override
    public void setupAnim(Hydra entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;

        leftLeg.yRot = 0.0F;
        rightLeg.yRot = 0.0F;
    }
}
