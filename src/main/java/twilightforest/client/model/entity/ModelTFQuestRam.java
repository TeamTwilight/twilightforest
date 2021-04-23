package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.passive.EntityTFQuestRam;

/**
 * ModelQuestingRam - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFQuestRam extends SegmentedModel<EntityTFQuestRam> {
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;
    public ModelRenderer frontTorso;
    public ModelRenderer horns;
    public ModelRenderer neck;
    public ModelRenderer backtorso;
    public ModelRenderer head;

    ModelRenderer[] segments;
    int[] colorOrder = new int[]{0, 8, 7, 15, 14, 1, 4, 5, 13, 3, 9, 11, 10, 2, 6, 12};

    public ModelTFQuestRam() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, -4.0F, 3.0F);
        this.head.setTextureOffset(74, 70).addBox(-6.0F, -2.0F, -13.0F, 12.0F, 8.0F, 15.0F, 0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(42, 71).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 3.0F, 11.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.4363323129985824F, 0.0F, 0.0F);
        this.frontTorso = new ModelRenderer(this, 0, 0);
        this.frontTorso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.frontTorso.addBox(-8.0F, -7.0F, -6.0F, 16.0F, 14.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.rightFrontLeg = new ModelRenderer(this, 0, 0);
        this.rightFrontLeg.setRotationPoint(-5.0F, 6.0F, 0.0F);
        this.rightFrontLeg.setTextureOffset(0, 60).addBox(-3.0F, 2.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.rightFrontLeg.setTextureOffset(54, 20).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 0, 0);
        this.neck.setRotationPoint(0.0F, 2.0F, -3.0F);
        this.neck.setTextureOffset(84, 93).addBox(-5.0F, -11.0F, -2.0F, 10.0F, 12.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.6108652381980153F, 0.0F, 0.0F);
        this.leftBackLeg = new ModelRenderer(this, 0, 0);
        this.leftBackLeg.setRotationPoint(16.0F, 6.0F, 0.0F);
        this.leftBackLeg.setTextureOffset(24, 82).addBox(-13.0F, 2.0F, -5.0F, 6.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.leftBackLeg.setTextureOffset(90, 50).addBox(-14.0F, -4.0F, -7.0F, 8.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.leftFrontLeg = new ModelRenderer(this, 0, 0);
        this.leftFrontLeg.setRotationPoint(5.0F, 6.0F, 0.0F);
        this.leftFrontLeg.setTextureOffset(24, 60).addBox(-3.0F, 2.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.leftFrontLeg.setTextureOffset(90, 20).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.backtorso = new ModelRenderer(this, 0, 0);
        this.backtorso.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.backtorso.setTextureOffset(0, 30).addBox(-8.0F, -7.0F, 8.0F, 16.0F, 14.0F, 16.0F, 0.0F, 0.0F, 0.0F);
        this.horns = new ModelRenderer(this, 0, 0);
        this.horns.setRotationPoint(0.0F, -10.0F, -8.0F);
        this.horns.setTextureOffset(64, 0).addBox(-9.0F, -11.0F, -1.0F, 4.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.horns.setTextureOffset(48, 0).addBox(-13.0F, -11.0F, 5.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.horns.setTextureOffset(92, 0).addBox(5.0F, -11.0F, -1.0F, 4.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        this.horns.setTextureOffset(110, 0).addBox(9.0F, -11.0F, 5.0F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.rightBackLeg = new ModelRenderer(this, 0, 0);
        this.rightBackLeg.setRotationPoint(-16.0F, 6.0F, 0.0F);
        this.rightBackLeg.setTextureOffset(0, 82).addBox(7.0F, 2.0F, -5.0F, 6.0F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.rightBackLeg.setTextureOffset(54, 50).addBox(6.0F, -4.0F, -7.0F, 8.0F, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F);

        segments = new ModelRenderer[16];
        for (int i = 0; i < 16; i++) {
            segments[i] = new ModelRenderer(this, 0, 112);
            segments[i].addBox(-8.0F, -7.0F, 8.0F, 16.0F, 14.0F, 2.0F, 0.0F, 0.0F, 0.0F);
            segments[i].setRotationPoint(0F, 0F, 10F);
            segments[i].showModel = false;
            this.frontTorso.addChild(this.segments[i]);
        }

        this.horns.addChild(this.head);
        this.frontTorso.addChild(this.neck);
    }

    @Override
    public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(stack, builder, light, overlay, red, green, blue, alpha);

        for (int i = 0; i < 16; i++) {
            final float[] dyeRgb = SheepEntity.getDyeRgb(DyeColor.byId(i));
            segments[i].render(stack, builder, light, overlay, dyeRgb[0], dyeRgb[1], dyeRgb[2], alpha);
        }
    }

    @Override
    public void setRotationAngles(EntityTFQuestRam entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.horns.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.horns.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);

        //this.neck.rotateAngleY = this.head.rotateAngleY;

        this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
    }

    @Override
    public void setLivingAnimations(EntityTFQuestRam entity, float limbSwing, float limbSwingAmount, float partialTicks) {

        // how many colors should we display?
        int count = entity.countColorsSet();

        this.backtorso.rotationPointZ = 2 + 2 * count;
        this.leftBackLeg.rotationPointZ = 25 + 2 * count;
        this.rightBackLeg.rotationPointZ = 25 + 2 * count;

        // set up the colors displayed in color order
        int segmentOffset = 2;
        for (int color : colorOrder) {
            if (entity.isColorPresent(DyeColor.byId(color))) {
                segments[color].showModel = true;
                segments[color].rotationPointZ = segmentOffset;

                segmentOffset += 2;
            } else {
                segments[color].showModel = false;
            }
        }
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                horns,
                frontTorso,
                backtorso,
                leftFrontLeg,
                rightFrontLeg,
                leftBackLeg,
                rightBackLeg
        );
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
