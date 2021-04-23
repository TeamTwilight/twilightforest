package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFMinotaur;

/**
 * ModelMinotaur - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFMinotaur extends BipedModel<EntityTFMinotaur> {

    public ModelTFMinotaur() {
        super(0, 0, 64, 64);
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        this.bipedRightLeg.setRotationPoint(-2.5F, 12.0F, 0.0F);
        this.bipedRightLeg.setTextureOffset(0, 26).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-7.5F, -4.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(20, 26).addBox(-3.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.bipedBody.setTextureOffset(34, 0).addBox(-5.0F, -2.0F, -2.5F, 10.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(25, 1).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(0, 16).addBox(-8.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(0, 20).addBox(-8.0F, -11.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(12, 16).addBox(4.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(12, 20).addBox(6.0F, -11.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        this.bipedLeftLeg.setRotationPoint(2.5F, 12.0F, 0.0F);
        this.bipedLeftLeg.setTextureOffset(0, 43).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(7.5F, -4.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(20, 45).addBox(0.0F, -4.0F, -2.5F, 4.0F, 14.0F, 5.0F, 0.0F, 0.0F, 0.0F);

        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
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
