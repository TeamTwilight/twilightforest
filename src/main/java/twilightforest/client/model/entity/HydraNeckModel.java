package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.entity.boss.HydraNeck;

public class HydraNeckModel extends ListModel<HydraNeck> {

    final ModelPart neck;

    public HydraNeckModel(ModelPart root) {
        this.neck = root.getChild("neck");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(128, 136)
                        .addBox(-16F, -16F, -16F, 32, 32, 32)
                        .texOffs(128, 200)
                        .addBox(-2F, -23F, 0F, 4, 24, 24),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 512, 256);
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(neck);
    }

    @Override
    public void setupAnim(HydraNeck entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        neck.yRot = netHeadYaw / 57.29578F;
        neck.xRot = headPitch / 57.29578F;
    }
}