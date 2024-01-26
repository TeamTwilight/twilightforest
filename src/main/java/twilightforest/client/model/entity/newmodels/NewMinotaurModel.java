package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.monster.Minotaur;

/**
 * ModelMinotaur - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewMinotaurModel extends HumanoidModel<Minotaur> {

    public NewMinotaurModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(25, 1)
                        .addBox(-3.0F, -5.0F, -5.0F, 6.0F, 3.0F, 1.0F)
                        .texOffs(0, 16)
                        .addBox(-8.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F)
                        .texOffs(0, 20)
                        .addBox(-8.0F, -11.0F, -1.0F, 2.0F, 2.0F, 2.0F)
                        .texOffs(12, 16)
                        .addBox(4.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F)
                        .texOffs(12, 20)
                        .addBox(6.0F, -11.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        definition.addOrReplaceChild("hat", CubeListBuilder.create(),
                PartPose.ZERO);

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(-5.0F, -2.0F, -2.5F, 10.0F, 14.0F, 5.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(20, 26)
                        .addBox(-4.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(-7.5F, -4.0F, 0.0F));

        definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(20, 45)
                        .addBox(0.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(7.5F, -4.0F, 0.0F));

        definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 26).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offset(-2.5F, 12.0F, 0.0F));

        definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 43)
                        .addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offset(2.5F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }
}
