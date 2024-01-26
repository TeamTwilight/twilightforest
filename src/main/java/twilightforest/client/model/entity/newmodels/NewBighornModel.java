package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Bighorn;

/**
 * BighornModel - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewBighornModel<T extends Bighorn> extends SheepModel<T> {
    public NewBighornModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = SheepModel.createBodyMesh(0, CubeDeformation.NONE);
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head",
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
        definition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(34, 13)
                        .addBox(-4.5F, -14.0F, -3.0F, 9.0F, 16.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F,
                        1.5707963267948966F, 0.0F, 0.0F));
        definition.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, 7.0F));
        definition.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, 7.0F));
        definition.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, -5.0F));
        definition.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create()
                        .texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, -5.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.young) {
            stack.pushPose();
            stack.translate(0.0D, 0.5D, 0.25D);
            ImmutableList.of(this.head).forEach((modelRenderer) ->
                    modelRenderer.render(stack, consumer, light, overlay, red, green, blue, alpha));
            stack.popPose();

            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.0D, 1.5D, 0.0D);
            ImmutableList.of(this.leftHindLeg, this.rightHindLeg, this.body, this.leftFrontLeg, this.rightFrontLeg).forEach((modelRenderer) ->
                    modelRenderer.render(stack, consumer, light, overlay, red, green, blue, alpha));
            stack.popPose();
        } else {
            ImmutableList.of(this.leftHindLeg, this.rightHindLeg, this.body, this.leftFrontLeg, this.rightFrontLeg, this.head).forEach((modelRenderer) ->
                    modelRenderer.render(stack, consumer, light, overlay, red, green, blue, alpha));
        }
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.getChild("left_horn").visible = !entity.isBaby();
        this.head.getChild("right_horn").visible = !entity.isBaby();
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
