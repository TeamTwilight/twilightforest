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
import twilightforest.entity.monster.PinchBeetle;

/**
 * ModelPinchBeetle - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NewPinchBeetleModel extends HierarchicalModel<PinchBeetle> {
    public final ModelPart root;
	public final ModelPart head;
    public final ModelPart rightLeg1;
    public final ModelPart rightLeg2;
    public final ModelPart rightLeg3;
    public final ModelPart leftLeg1;
    public final ModelPart leftLeg2;
    public final ModelPart leftLeg3;
    public final ModelPart rightPincer;
    public final ModelPart leftPincer;

    public NewPinchBeetleModel(ModelPart root) {
        this.root = root;

        this.head = root.getChild("head");
        
        this.leftPincer = head.getChild("left_pincher");
        this.rightPincer = head.getChild("right_pincher");

        this.rightLeg1 = root.getChild("right_leg_1");
        this.rightLeg2 = root.getChild("right_leg_2");
        this.rightLeg3 = root.getChild("right_leg_3");

        this.leftLeg1 = root.getChild("left_leg_1");
        this.leftLeg2 = root.getChild("left_leg_2");
        this.leftLeg3 = root.getChild("left_leg_3");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition definition = mesh.getRoot();

        var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));

        head.addOrReplaceChild("left_antenna", CubeListBuilder.create()
                        .texOffs(52, 0)
                        .addBox(0.0F, 0.0F, -10.0F, 1.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(1.0F, -3.0F, -6.0F, -0.4363323129985824F, -0.4363323129985824F, 0.0F));

        head.addOrReplaceChild("right_antenna", CubeListBuilder.create()
                        .texOffs(48, 0).addBox(-1.0F, 0.0F, -10.0F, 1.0F, 0.0F, 10.0F),
                PartPose.offsetAndRotation(-1.0F, -3.0F, -6.0F, -0.4363323129985824F, 0.4363323129985824F, 0.0F));

        head.addOrReplaceChild("left_pincher", CubeListBuilder.create()
                        .texOffs(16, 14)
                        .addBox(0.0F, 0.0F, -12.0F, 12.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(4.0F, 2.0F, -4.0F, 0.08726646259971647F, 0.6108652381980153F, 0.0F));

        head.addOrReplaceChild("right_pincher", CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-12.0F, 0.0F, -12.0F, 12.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(-4.0F, 2.0F, -4.0F, 0.08726646259971647F, -0.6108652381980153F, 0.0F));

        definition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-5.0F, -8.0F, -3.0F, 10.0F, 10.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 8.0F, 1.5707963267948966F, 0.0F, 0.0F));

        definition.addOrReplaceChild("right_leg_1", CubeListBuilder.create()
                        .texOffs(40, 28)
                        .addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, 6.0F, 0.0F, 0.6108652381980153F, -0.17453292519943295F));

        definition.addOrReplaceChild("right_leg_2", CubeListBuilder.create()
                        .texOffs(40, 32)
                        .addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, 4.0F, 0.0F, 0.20943951023931953F, -0.17453292519943295F));

        definition.addOrReplaceChild("right_leg_3", CubeListBuilder.create()
                        .texOffs(40, 36)
                        .addBox(-10.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, 2.0F, 0.0F, -0.20943951023931953F, -0.17453292519943295F));

        definition.addOrReplaceChild("left_leg_1", CubeListBuilder.create()
                        .texOffs(40, 42)
                        .addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, 6.0F, 0.0F, -0.6108652381980153F, 0.17453292519943295F));

        definition.addOrReplaceChild("left_leg_2", CubeListBuilder.create()
                        .texOffs(40, 46)
                        .addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, 4.0F, 0.0F, -0.20943951023931953F, 0.17453292519943295F));

        definition.addOrReplaceChild("left_leg_3", CubeListBuilder.create()
                        .texOffs(40, 50)
                        .addBox(0.0F, 0.0F, -1.0F, 10.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, 2.0F, 0.0F, 0.20943951023931953F, 0.17453292519943295F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(PinchBeetle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);

        float legZ = ((float) Math.PI / 11F);
        this.leftLeg1.zRot = legZ;
        this.rightLeg1.zRot = -legZ;
        this.leftLeg2.zRot = legZ * 0.74F;
        this.rightLeg2.zRot = -legZ * 0.74F;
        this.leftLeg3.zRot = legZ;
        this.rightLeg3.zRot = -legZ;

        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.leftLeg1.yRot = -var10 * 2.0F + var9;
        this.rightLeg1.yRot = var10 * 2.0F - var9;
        this.leftLeg2.yRot = var10 + var9;
        this.rightLeg2.yRot = -var10 - var9;
        this.leftLeg3.yRot = var10 * 2.0F + var9;
        this.rightLeg3.yRot = -var10 * 2.0F - var9;

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
    }

    @Override
    public void prepareMobModel(PinchBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        if (entity.isVehicle()) {
            // open jaws
            this.rightPincer.yRot = -0.3490658503988659F;
            this.leftPincer.yRot = 0.3490658503988659F;
        } else {
            // close jaws
            this.rightPincer.yRot = -0.7853981633974483F;
            this.leftPincer.yRot = 0.7853981633974483F;
        }
    }
}
