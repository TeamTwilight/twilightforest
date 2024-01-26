package twilightforest.client.model.entity.newmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import twilightforest.entity.monster.SlimeBeetle;

/**
 * ModelSlimeBeetle - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewSlimeBeetleModel extends HierarchicalModel<SlimeBeetle> {
    public final ModelPart root;
	public final ModelPart head;
    public final ModelPart rightLeg1;
	public final ModelPart rightLeg2;
	public final ModelPart rightLeg3;
    public final ModelPart leftLeg1;
	public final ModelPart leftLeg2;
	public final ModelPart leftLeg3;
    public final ModelPart rightAntenna;
	public final ModelPart leftAntenna;
    public final ModelPart rightEye;
	public final ModelPart leftEye;
    public final ModelPart tailBottom;
	public final ModelPart tailTop;
	public final ModelPart slime;
	public final ModelPart slimeCenter;

    public NewSlimeBeetleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

        this.rightLeg1 = root.getChild("right_leg_1");
        this.rightLeg2 = root.getChild("right_leg_2");
        this.rightLeg3 = root.getChild("right_leg_3");

        this.leftLeg1 = root.getChild("left_leg_1");
        this.leftLeg2 = root.getChild("left_leg_2");
        this.leftLeg3 = root.getChild("left_leg_3");

        this.rightAntenna = this.head.getChild("right_antenna");
        this.leftAntenna = this.head.getChild("left_antenna");
        this.rightEye = this.head.getChild("right_eye");
        this.leftEye = this.head.getChild("left_eye");

        this.tailBottom = root.getChild("tail_bottom");
        this.tailTop = this.tailBottom.getChild("tail_top");

        this.slimeCenter = this.tailTop.getChild("slime_center");
        this.slime = this.slimeCenter.getChild("slime");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 17.0F, -8.0F));
        
        head.addOrReplaceChild("right_antenna", CubeListBuilder.create()
                        .texOffs(38, 4)
                        .addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, -1.5F, -5.0F, 0.0F, -0.7853981633974483F, 0.7853981633974483F));

        head.addOrReplaceChild("left_antenna", CubeListBuilder.create()
                        .texOffs(38, 6)
                        .addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, -1.5F, -5.0F, 0.0F, 0.7853981633974483F, -0.7853981633974483F));

        head.addOrReplaceChild("right_eye", CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-2.5F, -1.0F, -4.5F));

        head.addOrReplaceChild("left_eye", CubeListBuilder.create()
                        .texOffs(16, 12)
                        .addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.5F, -1.0F, -4.5F));
        
        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(32, 8)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, -8.0F, 1.5707963267948966F, 0.0F, 0.0F));

        definition.addOrReplaceChild("right_leg_1", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 20.0F, -6.0F, 0.0F, -0.4363323129985824F, -0.4363323129985824F));

        definition.addOrReplaceChild("right_leg_2", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 20.0F, -4.0F, 0.0F, 0.2181661564992912F, -0.4363323129985824F));

        definition.addOrReplaceChild("right_leg_3", CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.7853981633974483F, -0.4363323129985824F));

        definition.addOrReplaceChild("left_leg_1", CubeListBuilder.create().mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 20.0F, -6.0F, 0.0F, 0.4363323129985824F, 0.4363323129985824F));

        definition.addOrReplaceChild("left_leg_2", CubeListBuilder.create().mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 20.0F, -4.0F, 0.0F, -0.2181661564992912F, 0.4363323129985824F));

        definition.addOrReplaceChild("left_leg_3", CubeListBuilder.create().mirror()
                        .texOffs(40, 0)
                        .addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, -0.7853981633974483F, 0.4363323129985824F));

        var tailBottom = definition.addOrReplaceChild("tail_bottom", CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 18.0F, 2.0F));

        var tailTop = tailBottom.addOrReplaceChild("tail_top", CubeListBuilder.create()
                        .texOffs(32, 28)
                        .addBox(-3.0F, -9.0F, -1.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 3.0F));

        var center = tailTop.addOrReplaceChild("slime_center", CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-4.0F, -10.0F, -5.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, -9.0F, 2.0F));

        center.addOrReplaceChild("slime", CubeListBuilder.create()
                        .texOffs(16, 40)
                        .addBox(-6.0F, -12.0F, -7.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
        slime.visible =  false;
        root().render(stack, builder, light, overlay, red, green, blue, alpha);
    }

    public void renderTail(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
        this.tailBottom.render(stack, builder, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(SlimeBeetle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);

        // legs!
        float legZ = ((float) Math.PI / 11F);
        this.leftLeg1.zRot = legZ;
        this.rightLeg1.zRot = -legZ;
        this.leftLeg2.zRot = legZ * 0.74F;
        this.rightLeg2.zRot = -legZ * 0.74F;
        this.leftLeg3.zRot = legZ;
        this.rightLeg3.zRot = -legZ;

        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.leftLeg1.yRot = var10 * 2.0F + var9;
        this.rightLeg1.yRot = -var10 * 2.0F - var9;
        this.leftLeg2.yRot = var10 + var9;
        this.rightLeg2.yRot = -var10 - var9;
        this.leftLeg3.yRot = -var10 * 2.0F + var9;
        this.rightLeg3.yRot = var10 * 2.0F - var9;

        float var11 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float var12 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float var14 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;

        float var15 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float var16 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float var18 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;

        this.leftLeg1.yRot += var11;
        this.rightLeg1.yRot -= var11;
        this.leftLeg2.yRot += var12;
        this.rightLeg2.yRot -= var12;
        this.leftLeg3.yRot += var14;
        this.rightLeg3.yRot -= var14;

        this.leftLeg1.zRot += var15;
        this.rightLeg1.zRot -= var15;

        this.leftLeg2.zRot += var16;
        this.rightLeg2.zRot -= var16;

        this.leftLeg3.zRot += var18;
        this.rightLeg3.zRot -= var18;

        // tail wiggle
        this.tailBottom.xRot = Mth.cos(ageInTicks * 0.3335F) * 0.15F;
        this.tailTop.xRot = Mth.cos(ageInTicks * 0.4445F) * 0.20F;
        this.slimeCenter.xRot = Mth.cos(ageInTicks * 0.5555F + 0.25F) * 0.25F;
    }
}
