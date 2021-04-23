package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFTinyBird;

/**
 * ModelTinyBird - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFTinyBird extends AgeableModel<EntityTFTinyBird> {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightFoot;
    public ModelRenderer leftFoot;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer tail;

    public ModelTFTinyBird() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.setTextureOffset(12, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftFoot = new ModelRenderer(this, 0, 0);
        this.leftFoot.setRotationPoint(1.0F, 23.0F, 2.0F);
        this.leftFoot.setTextureOffset(0, 11).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tail = new ModelRenderer(this, 0, 0);
        this.tail.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.tail.setTextureOffset(1, 6).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tail, 0.4363323129985824F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.head.addBox(-1.5F, -2.0F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(9, 0).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 6).addBox(-1.5F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.rightWing = new ModelRenderer(this, 0, 0);
        this.rightWing.setRotationPoint(-2.0F, 0.0F, 1.0F);
        this.rightWing.setTextureOffset(24, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftWing = new ModelRenderer(this, 0, 0);
        this.leftWing.setRotationPoint(2.0F, 0.0F, 1.0F);
        this.leftWing.setTextureOffset(24, 5).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightFoot = new ModelRenderer(this, 0, 0);
        this.rightFoot.setRotationPoint(-1.0F, 23.0F, 2.0F);
        this.rightFoot.setTextureOffset(0, 9).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.tail);
        this.body.addChild(this.rightWing);
        this.body.addChild(this.leftWing);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(
                head,
                body,
                rightFoot,
                leftFoot
        );
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
        if (isChild) {
            float f = 2.0F;
            stack.push();
            stack.translate(0.0F, 5F * scale, 0.75F * scale);
            this.getHeadParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.pop();
            stack.push();
            stack.scale(1.0F / f, 1.0F / f, 1.0F / f);
            stack.translate(0.0F, 24F * scale, 0.0F);
            this.getBodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            stack.pop();
        } else {
            this.getHeadParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
            this.getBodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(EntityTFTinyBird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.rotateAngleX = headPitch / (180F / (float) Math.PI);
        head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);

        rightFoot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftFoot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        rightWing.rotateAngleZ = ageInTicks;
        leftWing.rotateAngleZ = -ageInTicks;

        if (entity.isBirdLanded()) {
            rightFoot.rotationPointY = 23;
            leftFoot.rotationPointY = 23;
        } else {
            rightFoot.rotationPointY = 22.5F;
            leftFoot.rotationPointY = 22.5F;
        }
    }
}
