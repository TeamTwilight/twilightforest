package twilightforest.client.model.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.Boar;

/**
 * ModelWildBoar - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BoarModel<T extends Boar> extends PigModel<T> {
    public BoarModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = QuadrupedModel.createBodyMesh(6, CubeDeformation.NONE);
        PartDefinition partRoot = mesh.getRoot();

        partRoot.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-2.0F, 0.0F, -1.9F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(-2.9F, 18.0F, -2.0F));

        partRoot.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
                        .texOffs(0, 23)
                        .addBox(-2.0F, 0.0F, -1.9F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(2.9F, 18.0F, -2.0F));

        partRoot.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 33)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(-3.1F, 18.0F, 9.0F));

        partRoot.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
                        .texOffs(0, 43)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(3.1F, 18.0F, 9.0F));

        // Snout and tusks included
        partRoot.addOrReplaceChild("head", CubeListBuilder.create()
                        .addBox(-4.0F, -4.0F, -5.0F, 8.0F, 7.0F, 6.0F)
                        .texOffs(46, 22)
                        .addBox(-3.0F, -1.0F, -8.0F, 6.0F, 4.0F, 3.0F)
                        .texOffs(28, 0)
                        .addBox(-4.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F)
                        .texOffs(28, 3)
                        .addBox(3.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(0.0F, 15.5F, -5.0F));

        partRoot.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-5.0F, -6.0F, 0.0F, 10.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 1.6580627893946132F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = ((float)Math.PI / 2F);
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
