package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Deer;

/**
 * ModelWildDeer - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewDeerModel extends QuadrupedModel<Deer> {
    private final ModelPart neck;
    private final ModelPart realHead;

    public NewDeerModel(ModelPart root) {
        super(root, false, 4.0F, 4.0F, 2.0F, 2.0F, 10);

        this.neck = root.getChild("neck");
        //cant assign a value to the head since its final so lets make a workaround
        this.realHead = neck.getChild("head");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = QuadrupedModel.createBodyMesh(12, CubeDeformation.NONE);
        PartDefinition definition = mesh.getRoot();

        var neck = definition.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(22, 14)
                        .addBox(-1.5F, -8.0F, -2.0F, 3.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, -5.0F, 0.4363323129985824F, 0.0F, 0.0F));

        var head = neck.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(24, 2)
                        .addBox(-2.0F, -4.0F, -4.0F, 4.0F, 6.0F, 6.0F)
                        .texOffs(52, 0)
                        .addBox(-1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.4363323129985824F, 0.0F, 0.0F));

        head.addOrReplaceChild("right_antler", CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(-1.0F, -4.0F, 0.0F, 0.0F, -0.39269908169872414F, -0.39269908169872414F));

        head.addOrReplaceChild("left_antler", CubeListBuilder.create()
                        .texOffs(32, 16)
                        .addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(1.0F, -4.0F, 0.0F, 0.0F, 0.39269908169872414F, 0.39269908169872414F));

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(36, 6)
                        .addBox(-3.0F, -14.0F, -2.0F, 6.0F, 18.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, 7.0F, 1.5707963267948966F, 0.0F, 0.0F));

        definition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 15)
                        .addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
                PartPose.offset(-2.0F, 12.0F, 9.5F));

        definition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                        .texOffs(10, 15)
                        .addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
                PartPose.offset(2.0F, 12.0F, 9.5F));

        definition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                        .addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
                PartPose.offset(-2.0F, 12.0F, -4.5F));

        definition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
                PartPose.offset(2.0F, 12.0F, -4.5F));

        return LayerDefinition.create(mesh, 64, 48);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.neck);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
        if (this.young) {
            stack.pushPose();
            stack.scale(0.75F, 0.75F, 0.75F);
            stack.translate(0.0F, 0.95F, 0.15F);
            this.headParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.popPose();

            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.0F, 1.5F, 0.0F);
            this.bodyParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.popPose();
        } else {
            this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
        }
    }

    public void setupAnim(Deer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.realHead.getChild("right_antler").visible = !entity.isBaby();
        this.realHead.getChild("left_antler").visible = !entity.isBaby();
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.realHead.xRot = head.xRot;
        this.realHead.yRot = head.yRot;
    }
}
