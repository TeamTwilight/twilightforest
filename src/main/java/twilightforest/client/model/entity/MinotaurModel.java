package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.monster.Minotaur;

/**
 * ModelMinotaur - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class MinotaurModel extends HumanoidModel<Minotaur> {

    public MinotaurModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partRoot = mesh.getRoot();

        partRoot.addOrReplaceChild("head", CubeListBuilder.create()
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

        partRoot.addOrReplaceChild("hat", CubeListBuilder.create(),
                PartPose.ZERO);

        partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(-5.0F, -2.0F, -2.5F, 10.0F, 14.0F, 5.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(20, 26)
                        .addBox(-3.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(-7.5F, -4.0F, 0.0F));

        partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(20, 45)
                        .addBox(0.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(7.5F, -4.0F, 0.0F));

        partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 26).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offset(-2.5F, 12.0F, 0.0F));

        partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 43)
                        .addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F),
                PartPose.offset(2.5F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.riding) matrixStackIn.translate(0, 0.5F, 0);
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
