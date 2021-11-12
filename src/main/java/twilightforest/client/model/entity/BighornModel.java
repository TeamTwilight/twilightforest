package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Bighorn;

/**
 * BighornModel - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BighornModel<T extends Bighorn> extends SheepModel<T> {
    public BighornModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = SheepModel.createBodyMesh(0, CubeDeformation.NONE);
        PartDefinition partRoot = mesh.getRoot();

        var head = partRoot.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 7.0F),
                PartPose.offset(0.0F, 5.0F, -8.0F));
        head.addOrReplaceChild("right_horn",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -1.0F, -7.0F, 3.0F, 3.0F, 5.0F)
                        .texOffs(0, 8)
                        .addBox(-4.0F, 2.0F, -9.0F, 3.0F, 2.0F, 5.0F)
                        .texOffs(4, 15)
                        .addBox(-4.0F, 0.0F, -11.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, -3.0F, -1.0F,
                        0.0F, 0.39269908169872414F, 0.2181661564992912F));
        head.addOrReplaceChild("left_horn",
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(0.0F, -1.0F, -7.0F, 3.0F, 3.0F, 5.0F)
                        .texOffs(16, 8)
                        .addBox(1.0F, 2.0F, -9.0F, 3.0F, 2.0F, 5.0F)
                        .texOffs(20, 15)
                        .addBox(2.0F, 0.0F, -11.0F, 2.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, -3.0F, -1.0F,
                        0.0F, -0.39269908169872414F, -0.2181661564992912F));
        partRoot.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(34, 13)
                        .addBox(-4.5F, -14.0F, -3.0F, 9.0F, 16.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F,
                        1.5707963267948966F, 0.0F, 0.0F));
        partRoot.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, 7.0F));
        partRoot.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, 7.0F));
        partRoot.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, -5.0F));
        partRoot.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create()
                        .texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, -5.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.young) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, 0.5, 0.25);
            ImmutableList.of(this.head).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.popPose();

            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            ImmutableList.of(this.leftHindLeg, this.rightHindLeg, this.body, this.leftFrontLeg, this.rightFrontLeg).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
            matrixStackIn.popPose();
        } else {
            ImmutableList.of(this.leftHindLeg, this.rightHindLeg, this.body, this.leftFrontLeg, this.rightFrontLeg, this.head).forEach((modelRenderer) -> {
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            });
        }
    }
}
