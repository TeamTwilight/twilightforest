package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFBoar;

/**
 * ModelWildBoar - Undefined
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFBoar<T extends EntityTFBoar> extends AgeableModel<T> {
    public ModelRenderer part1;
    public ModelRenderer torso;
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;

    public ModelTFBoar() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.rightFrontLeg = new ModelRenderer(this, 0, 0);
        this.rightFrontLeg.setRotationPoint(-2.9F, 18.0F, -2.0F);
        this.rightFrontLeg.setTextureOffset(0, 13).addBox(-2.0F, 0.0F, -1.9F, 4.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftFrontLeg = new ModelRenderer(this, 0, 0);
        this.leftFrontLeg.setRotationPoint(2.9F, 18.0F, -2.0F);
        this.leftFrontLeg.setTextureOffset(0, 23).addBox(-2.0F, 0.0F, -1.9F, 4.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.rightBackLeg = new ModelRenderer(this, 0, 0);
        this.rightBackLeg.setRotationPoint(-3.1F, 18.0F, 9.0F);
        this.rightBackLeg.setTextureOffset(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftBackLeg = new ModelRenderer(this, 0, 0);
        this.leftBackLeg.setRotationPoint(3.1F, 18.0F, 9.0F);
        this.leftBackLeg.setTextureOffset(0, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.part1 = new ModelRenderer(this, 0, 0);
        this.part1.setRotationPoint(0.0F, 15.5F, -5.0F);
        this.part1.addBox(-4.0F, -4.0F, -5.0F, 8.0F, 7.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.part1.setTextureOffset(46, 22).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.part1.setTextureOffset(28, 0).addBox(-4.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.part1.setTextureOffset(28, 3).addBox(3.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.torso = new ModelRenderer(this, 0, 0);
        this.torso.setRotationPoint(0.0F, 19.0F, 2.0F);
        this.torso.setTextureOffset(28, 0).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 14.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(torso, 1.6580627893946132F, 0.0F, 0.0F);
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.part1.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.part1.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.torso.rotateAngleX = ((float)Math.PI / 2F);
        this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.part1);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.torso, this.leftBackLeg, this.rightBackLeg, this.leftFrontLeg, this.rightFrontLeg);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
