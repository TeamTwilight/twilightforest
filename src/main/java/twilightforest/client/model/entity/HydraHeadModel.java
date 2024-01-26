package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraPart;

public class HydraHeadModel extends ListModel<HydraHead> {

    final ModelPart head;
    final ModelPart jaw;

    public HydraHeadModel(ModelPart root) {
        this.head = root.getChild("head");
        this.jaw = head.getChild("jaw");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(272, 0)
                        .addBox(-16F, -14F, -32F, 32, 24, 32)
                        .texOffs(272, 56)
                        .addBox(-15F, -2F, -56F, 30, 12, 24)
                        .texOffs(272, 132)
                        .addBox(-15F, 10F, -20F, 30, 8, 16)
                        .texOffs(128, 200)
                        .addBox(-2F, -30F, -12F, 4, 24, 24)
                        .texOffs(272, 156)
                        .addBox(-12F, 10, -49F, 2, 5, 2)
                        .texOffs(272, 156)
                        .addBox(10F, 10, -49F, 2, 5, 2)
                        .texOffs(280, 156)
                        .addBox(-8F, 9, -49F, 16, 2, 2)
                        .texOffs(280, 160)
                        .addBox(-10F, 9, -45F, 2, 2, 16)
                        .texOffs(280, 160)
                        .addBox(8F, 9, -45F, 2, 2, 16),
                PartPose.ZERO);

        head.addOrReplaceChild("jaw", CubeListBuilder.create()
                        .texOffs(272, 92)
                        .addBox(-15F, 0F, -32F, 30, 8, 32)
                        .texOffs(272, 156)
                        .addBox(-10F, -5, -29F, 2, 5, 2)
                        .texOffs(272, 156)
                        .addBox(8F, -5, -29F, 2, 5, 2)
                        .texOffs(280, 156)
                        .addBox(-8F, -1, -29F, 16, 2, 2)
                        .texOffs(280, 160)
                        .addBox(-10F, -1, -25F, 2, 2, 16)
                        .texOffs(280, 160)
                        .addBox(8F, -1, -25F, 2, 2, 16),
                PartPose.offset(0F, 10F, -20F));

        head.addOrReplaceChild("frill", CubeListBuilder.create()
                        .texOffs(272, 200)
                        .addBox(-24F, -40.0F, 0F, 48, 48, 4),
                PartPose.offsetAndRotation(0F, 0F, -14F, -0.5235988F, 0F, 0F));

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(head);
    }

    @Override
    public void setupAnim(HydraHead entity, float v, float v1, float v2, float v3, float v4) { }

    @Override
    public void prepareMobModel(HydraHead entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        head.yRot = getRotationY(entity, partialTicks);
        head.xRot = getRotationX(entity, partialTicks);

        float mouthOpenLast = entity.getMouthOpenLast();
        float mouthOpenReal = entity.getMouthOpen();
        float mouthOpen = Mth.lerp(partialTicks, mouthOpenLast, mouthOpenReal);
        head.xRot -= (float) (mouthOpen * (Math.PI / 12.0));
        jaw.xRot = (float) (mouthOpen * (Math.PI / 3.0));
    }

    public void openMouthForTrophy(float mouthOpen) {
        head.yRot = 0;
        head.xRot = 0;

        head.xRot -= (float) (mouthOpen * (Math.PI / 12.0));
        jaw.xRot = (float) (mouthOpen * (Math.PI / 3.0));
    }

    public float getRotationY(HydraPart whichHead, float time) {
        //float yawOffset = hydra.prevRenderYawOffset + (hydra.renderYawOffset - hydra.prevRenderYawOffset) * time;
        float yaw = whichHead.yRotO + (whichHead.getYRot() - whichHead.yRotO) * time;

        return yaw / 57.29578F;
    }

    public float getRotationX(HydraPart whichHead, float time) {
        return (whichHead.xRotO + (whichHead.getXRot() - whichHead.xRotO) * time) / 57.29578F;
    }
}