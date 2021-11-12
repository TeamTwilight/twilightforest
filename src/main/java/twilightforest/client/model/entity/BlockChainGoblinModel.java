package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.monster.BlockChainGoblin;

/**
 * ModelMaceGoblin - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BlockChainGoblinModel<T extends BlockChainGoblin> extends HumanoidModel<T> {
    private final ModelPart block;

    public BlockChainGoblinModel(ModelPart root) {
        super(root);

        this.block = root.getChild("block");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partRoot = mesh.getRoot();

        partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(52, 2)
                        .addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
                PartPose.offsetAndRotation(-5.0F, 12.0F, 0.0F, 0.0F, 0.0F, 3.0543261909900767F));

        partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(52, 17)
                        .addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
                PartPose.offsetAndRotation(5.0F, 12.0F, 0.0F, 0.0F, 0.0F, -3.0543261909900767F));

        partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 33)
                        .addBox(-1.4F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(-2.0F, 18.0F, 0.0F));

        partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(12, 33)
                        .addBox(-1.6F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(2.0F, 18.0F, 0.0F));

        partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(28, 6)
                        .addBox(-3.5F, 1.0F, -2.0F, 7.0F, 6.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-7.5F, -9.0F, -2.03F, 15.0F, 10.0F, 2.0F),
                PartPose.rotation(0.0F, -0.7853981633974483F, 0.0F));


        partRoot.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        head.addOrReplaceChild("helm", CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(-2.5F, -7.0F, -2.5F, 5.0F, 8.0F, 5.0F),
                PartPose.rotation(0.0F, 0.7853981633974483F, 0.0F));

        var block = partRoot.addOrReplaceChild("block", CubeListBuilder.create()
                        .texOffs(32, 32)
                        .addBox(-4F, -8F, -4F, 8, 8, 8),
                PartPose.ZERO);

        // Rotation constants
        final float QUARTER_PI = 0.25F * Mth.PI;
        final float ANGLE_MINOR = -35F * Mth.DEG_TO_RAD;
        final float ANGLE_MAJOR = -55F * Mth.DEG_TO_RAD;

        block.addOrReplaceChild("spikes_0", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -9, 0, 0, 0, 0));

        block.addOrReplaceChild("spikes_1", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -8, 4, QUARTER_PI, 0, 0));

        block.addOrReplaceChild("spikes_2", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, -8, 4, ANGLE_MAJOR, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_3", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, -8, 0, 0, 0, QUARTER_PI));

        block.addOrReplaceChild("spikes_4", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, -8, -4, ANGLE_MINOR, -QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_5", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -8, -4, QUARTER_PI, 0, 0));

        block.addOrReplaceChild("spikes_6", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, -8, -4, ANGLE_MINOR, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_7", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, -8, 0, 0, 0, QUARTER_PI));

        block.addOrReplaceChild("spikes_8", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, -8, 4, ANGLE_MAJOR, -QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_9", CubeListBuilder.create() // this spike is not really there
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -4, 0, 0, 0, 0));

        block.addOrReplaceChild("spikes_10", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -4, 4, 0, 0, 0));

        block.addOrReplaceChild("spikes_11", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, -4, 5, 0, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_12", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(5, -4, 0, 0, 0, 0));

        block.addOrReplaceChild("spikes_13", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, -4, -4, 0, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_14", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, -4, -5, 0, 0, 0));

        block.addOrReplaceChild("spikes_15", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, -4, -4, 0, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_16", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-5, -4, 0, 0, 0, 0));

        block.addOrReplaceChild("spikes_17", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, -4, 4, 0, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_18", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, 1, 0, 0, 0, 0));

        block.addOrReplaceChild("spikes_19", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, 0, 4, QUARTER_PI, 0, 0));

        block.addOrReplaceChild("spikes_20", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, 0, 4, ANGLE_MINOR, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_21", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, 0, 0, 0, 0, QUARTER_PI));

        block.addOrReplaceChild("spikes_22", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(4, 0, -4, ANGLE_MAJOR, -QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_23", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(0, 0, -4, QUARTER_PI, 0, 0));

        block.addOrReplaceChild("spikes_24", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, 0, -4, ANGLE_MAJOR, QUARTER_PI, 0));

        block.addOrReplaceChild("spikes_25", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, 0, 0, 0, 0, QUARTER_PI));

        block.addOrReplaceChild("spikes_26", CubeListBuilder.create()
                        .texOffs(56, 36)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offsetAndRotation(-4, 0, 4, ANGLE_MINOR, -QUARTER_PI, 0));

        return LayerDefinition.create(mesh, 64, 48);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.head.y = 11.0F;
        this.hat.y = 11.0F;
        this.body.y = 11F;

        this.rightLeg.y = 18F;
        this.leftLeg.y = 18F;

        this.rightArm.setPos(-3.5F, 12F, 0F);
        this.rightArm.xRot += Mth.PI;

        this.leftArm.setPos(3.5F, 12F, 0F);
        this.leftArm.xRot += Mth.PI;

        float angle = ageInTicks / 4F;
        float length = 0;//16F;

        this.block.x = Mth.sin(angle) * length;
        this.block.z = -Mth.cos(angle) * length;

        this.block.yRot = -angle;
    }
}
