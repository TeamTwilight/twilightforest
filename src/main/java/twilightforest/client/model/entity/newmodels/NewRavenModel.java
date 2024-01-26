package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Raven;

/**
 * ModelForestRaven - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewRavenModel extends HierarchicalModel<Raven> {
    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart rightWing;
    public final ModelPart leftWing;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public final ModelPart tail;

    public NewRavenModel(ModelPart root) {
        this.root = root;

        this.head = root.getChild("head");

        var body = root.getChild("torso");
        this.rightWing = body.getChild("right_wing");
        this.leftWing = body.getChild("left_wing");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.tail = body.getChild("tail");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F)
                        .texOffs(9, 0)
                        .addBox(-0.5F, 0.0F, -3.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 18.5F, -2.0F));

        var body = definition.addOrReplaceChild("torso", CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 18.5F, -2.0F, -0.4363323129985824F, 0.0F, 0.0F));

        body.addOrReplaceChild("right_wing", CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-2.0F, -1.0F, 2.0F, 0.2617993877991494F, 0.0F, 0.0F));

        body.addOrReplaceChild("left_wing", CubeListBuilder.create()
                        .texOffs(14, 15)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(2.0F, -1.0F, 2.0F, 0.2617993877991494F, 0.0F, 0.0F));

        definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(8, 15)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.7853981633974483F, 0.0F, 0.0F));

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(14, 15)
                        .addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.7853981633974483F, 0.0F, 0.0F));

        body.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -1.5F, 6.0F, -0.4363323129985824F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Raven entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch / (180F / (float) Math.PI);
        head.yRot = netHeadYaw / (180F / (float) Math.PI);
        head.zRot = netHeadYaw > 5 ? -0.2617994F : 0;

        leftLeg.xRot = Mth.cos (limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightWing.zRot = ageInTicks;
        leftWing.zRot = -ageInTicks;

        if (entity.isBirdLanded()) {
            rightLeg.y = 21;
            leftLeg.y = 21;
        } else {
            rightLeg.y = 20F;
            leftLeg.y = 20F;
        }
    }
}
