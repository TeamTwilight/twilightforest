package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.TinyBird;

/**
 * ModelTinyBird - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewTinyBirdModel extends AgeableListModel<TinyBird> {
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightFoot;
    public final ModelPart leftFoot;
    public final ModelPart rightWing;
    public final ModelPart leftWing;
    public final ModelPart tail;

    public NewTinyBirdModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.rightFoot = root.getChild("right_foot");
        this.leftFoot = root.getChild("left_foot");
        this.rightWing = body.getChild("right_wing");
        this.leftWing = body.getChild("left_wing");
        this.tail = body.getChild("tail");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -2.0F, -2.0F, 3.0F, 3.0F, 3.0F)
                        .texOffs(9, 0)
                        .addBox(-0.5F, 0.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                        .texOffs(0, 6)
                        .addBox(-1.5F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));

        var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));

        definition.addOrReplaceChild("right_foot", CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, 23.0F, 2.0F));

        definition.addOrReplaceChild("left_foot", CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.0F, 23.0F, 2.0F));

        body.addOrReplaceChild("right_wing", CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(-2.0F, 0.0F, 1.0F));

        body.addOrReplaceChild("left_wing", CubeListBuilder.create()
                        .texOffs(24, 5)
                        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(2.0F, 0.0F, 1.0F));

        body.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(1, 6)
                        .addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.4363323129985824F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(
                head,
                body,
                rightFoot,
                leftFoot
        );
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
        if (young) {
            float f = 2.0F;
            stack.pushPose();
            stack.translate(0.0F, 5F * scale, 0.75F * scale);
            this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.popPose();
            stack.pushPose();
            stack.scale(1.0F / f, 1.0F / f, 1.0F / f);
            stack.translate(0.0F, 24F * scale, 0.0F);
            this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.popPose();
        } else {
            this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
        }
    }

    @Override
    public void setupAnim(TinyBird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch / (180F / (float) Math.PI);
        head.yRot = netHeadYaw / (180F / (float) Math.PI);

        rightFoot.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftFoot.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightWing.zRot = ageInTicks;
        leftWing.zRot = -ageInTicks;

        if (entity.isBirdLanded()) {
            rightFoot.y = 23;
            leftFoot.y = 23;
        } else {
            rightFoot.y = 22.5F;
            leftFoot.y = 22.5F;
        }
    }
}
