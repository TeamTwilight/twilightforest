package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.passive.QuestRam;

/**
 * ModelQuestingRam - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewQuestRamModel extends QuadrupedModel<QuestRam> {
    public final ModelPart horns;
    public final ModelPart backtorso;
	public final ModelPart frontTorso;

    public final ModelPart[] segments = new ModelPart[16];
    final int[] colorOrder = new int[]{0, 8, 7, 15, 14, 1, 4, 5, 13, 3, 9, 11, 10, 2, 6, 12};

    public NewQuestRamModel(ModelPart root) {
        super(root, false, 4.0F, 4.0F, 2.0F, 2.0F, 24);

        this.horns = root.getChild("horns");
        this.backtorso = root.getChild("back_torso");

        this.frontTorso = root.getChild("front_torso");

        for(int i = 0; i < 16; i++) {
            this.segments[i] = frontTorso.getChild("segment_" + i);
        }
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = QuadrupedModel.createBodyMesh(16, CubeDeformation.NONE);
        PartDefinition definition = mesh.getRoot();

        var horns = definition.addOrReplaceChild("horns", CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(-9.0F, -11.0F, -1.0F, 4.0F, 10.0F, 10.0F)
                        .texOffs(48, 0)
                        .addBox(-13.0F, -11.0F, 5.0F, 4.0F, 4.0F, 4.0F)
                        .texOffs(92, 0)
                        .addBox(5.0F, -11.0F, -1.0F, 4.0F, 10.0F, 10.0F)
                        .texOffs(110, 0)
                        .addBox(9.0F, -11.0F, 5.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, -10.0F, -8.0F));

        horns.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(74, 70)
                        .addBox(-6.0F, -2.0F, -13.0F, 12.0F, 8.0F, 15.0F)
                        .texOffs(42, 71)
                        .addBox(-6.0F, -5.0F, -9.0F, 12.0F, 3.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, 3.0F, 0.4363323129985824F, 0.0F, 0.0F));

        var frontTorso = definition.addOrReplaceChild("front_torso", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -7.0F, -6.0F, 16.0F, 14.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        frontTorso.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(84, 93)
                        .addBox(-5.0F, -11.0F, -2.0F, 10.0F, 12.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, 0.6108652381980153F, 0.0F, 0.0F));

        definition.addOrReplaceChild("back_torso", CubeListBuilder.create()
                        .texOffs(0, 30)
                        .addBox(-8.0F, -7.0F, 8.0F, 16.0F, 14.0F, 16.0F),
                PartPose.offset(0.0F, 0.0F, 6.0F));

        definition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                        .texOffs(0, 60)
                        .addBox(-3.0F, 2.0F, -3.0F, 6.0F, 16.0F, 6.0F)
                        .texOffs(54, 20)
                        .addBox(-4.0F, -4.0F, -5.0F, 8.0F, 10.0F, 10.0F),
                PartPose.offset(-5.0F, 6.0F, 0.0F));

        definition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                        .texOffs(24, 60)
                        .addBox(-3.0F, 2.0F, -3.0F, 6.0F, 16.0F, 6.0F)
                        .texOffs(90, 20)
                        .addBox(-4.0F, -4.0F, -5.0F, 8.0F, 10.0F, 10.0F),
                PartPose.offset(5.0F, 6.0F, 0.0F));

        definition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 82)
                        .addBox(7.0F, 2.0F, -5.0F, 6.0F, 16.0F, 6.0F)
                        .texOffs(54, 50)
                        .addBox(6.0F, -4.0F, -7.0F, 8.0F, 10.0F, 10.0F),
                PartPose.offset(-16.0F, 6.0F, 0.0F));

        definition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                        .texOffs(24, 82)
                        .addBox(-13.0F, 2.0F, -5.0F, 6.0F, 16.0F, 6.0F)
                        .texOffs(90, 50)
                        .addBox(-14.0F, -4.0F, -7.0F, 8.0F, 10.0F, 10.0F),
                PartPose.offset(16.0F, 6.0F, 0.0F));

        for (int i = 0; i < 16; i++) {

            frontTorso.addOrReplaceChild("segment_" + i, CubeListBuilder.create()
                            .texOffs(0, 112)
                            .addBox(-8.0F, -7.0F, 8.0F, 16.0F, 14.0F, 2.0F),
                    PartPose.offset(0.0F, 0.0F, 10.0F));
        }

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(horns);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(horns, frontTorso, backtorso, rightFrontLeg, leftFrontLeg, rightHindLeg, leftHindLeg);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(stack, builder, light, overlay, red, green, blue, alpha);

        for (int i = 0; i < 16; i++) {
            final float[] dyeRgb = Sheep.getColorArray(DyeColor.byId(i));
            segments[i].render(stack, builder, light, overlay, dyeRgb[0], dyeRgb[1], dyeRgb[2], alpha);
        }
    }

    @Override
    public void setupAnim(QuestRam entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.horns.xRot = headPitch / (180F / (float) Math.PI);
        this.horns.yRot = netHeadYaw / (180F / (float) Math.PI);

        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
    }

    @Override
    public void prepareMobModel(QuestRam entity, float limbSwing, float limbSwingAmount, float partialTicks) {

        // how many colors should we display?
        int count = entity.countColorsSet();

        this.backtorso.z = 2 + 2 * count;
        this.leftHindLeg.z = 25 + 2 * count;
        this.rightHindLeg.z = 25 + 2 * count;

        // set up the colors displayed in color order
        int segmentOffset = 2;
        for (int color : colorOrder) {
            if (entity.isColorPresent(DyeColor.byId(color))) {
                segments[color].visible = true;
                segments[color].z = segmentOffset;

                segmentOffset += 2;
            } else {
                segments[color].visible = false;
            }
        }
    }
}
