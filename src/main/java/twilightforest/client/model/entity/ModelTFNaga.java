package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.EntityTFNaga;
import twilightforest.entity.boss.EntityTFNagaSegment;

/**
 * ModelNagaHead - Undefined
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFNaga<T extends Entity> extends SegmentedModel<T> {
    public ModelRenderer head;
    public ModelRenderer tongue;
    public ModelRenderer body;
    private T entity;

    public ModelTFNaga() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.tongue = new ModelRenderer(this, 0, 0);
        this.tongue.setRotationPoint(0.0F, 10.0F, -16.0F);
        this.tongue.setTextureOffset(84, 0).addBox(-6.0F, 0.0F, -12.0F, 12.0F, 0.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tongue, 0.4363323129985824F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.head.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
        this.head.addChild(this.tongue);

        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.body.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);

    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        if (entity instanceof EntityTFNaga) {
            head.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        } else if (entity instanceof EntityTFNagaSegment) {
            body.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        } else {
            head.render(stack, builder, light, overlay, red, green, blue, alpha * 2);
        }
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head, body);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
