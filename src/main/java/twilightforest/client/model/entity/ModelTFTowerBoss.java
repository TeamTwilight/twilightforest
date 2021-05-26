package twilightforest.client.model.entity;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.boss.EntityTFUrGhast;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ModelTFTowerBoss extends ModelTFGhast<EntityTFUrGhast> {


    protected ModelRenderer[][] subTentacles;
    protected ModelRenderer[][] smallTentacles;

    public ModelTFTowerBoss() {
        super();

        this.smallTentacles = new ModelRenderer[2][3];
        for (int i = 0; i < this.smallTentacles.length; ++i) {
            makeSmallTentacle(i);
        }
    }

    @Override
    protected void makeTentacle(byte yOffset, Random random, int num) {
        this.tentacles[num] = new ModelRenderer(this, 0, 0);

        float length = 5.333F;

        this.tentacles[num].addBox(-1.5F, 0.0F, -1.5F, 3.333F, length, 3.333F);

        if (num == 0) {
            this.tentacles[num].rotationPointX = 4.5F;
            this.tentacles[num].rotationPointZ = 4.5F;
            this.tentacles[num].rotationPointY = 23 + yOffset;
        }
        if (num == 1) {
            this.tentacles[num].rotationPointX = -4.5F;
            this.tentacles[num].rotationPointZ = 4.5F;
            this.tentacles[num].rotationPointY = 23 + yOffset;
        }
        if (num == 2) {
            this.tentacles[num].rotationPointX = 0F;
            this.tentacles[num].rotationPointZ = 0F;
            this.tentacles[num].rotationPointY = 23 + yOffset;
        }
        if (num == 3) {
            this.tentacles[num].rotationPointX = 5.5F;
            this.tentacles[num].rotationPointZ = -4.5F;
            this.tentacles[num].rotationPointY = 23 + yOffset;
        }
        if (num == 4) {
            this.tentacles[num].rotationPointX = -5.5F;
            this.tentacles[num].rotationPointZ = -4.5F;
            this.tentacles[num].rotationPointY = 23 + yOffset;
        } else if (num == 5) {
            this.tentacles[num].rotationPointX = -7.5F;
            this.tentacles[num].rotationPointY = 3.5F;
            this.tentacles[num].rotationPointZ = -1F;

            this.tentacles[num].rotateAngleZ = (float) Math.PI / 4.0F;
        } else if (num == 6) {
            this.tentacles[num].rotationPointX = -7.5F;
            this.tentacles[num].rotationPointY = -1.5F;
            this.tentacles[num].rotationPointZ = 3.5F;

            this.tentacles[num].rotateAngleZ = (float) Math.PI / 3.0F;
        } else if (num == 7) {
            this.tentacles[num].rotationPointX = 7.5F;
            this.tentacles[num].rotationPointY = 3.5F;
            this.tentacles[num].rotationPointZ = -1F;

            this.tentacles[num].rotateAngleZ = -(float) Math.PI / 4.0F;
        } else if (num == 8) {
            this.tentacles[num].rotationPointX = 7.5F;
            this.tentacles[num].rotationPointY = -1.5F;
            this.tentacles[num].rotationPointZ = 3.5F;

            this.tentacles[num].rotateAngleZ = -(float) Math.PI / 3.0F;
        }

        // goofy mid-method initializer
        if (this.subTentacles == null) {
            this.subTentacles = new ModelRenderer[tentacles.length][2];
        }

        length = 6.66F;

        this.subTentacles[num][0] = new ModelRenderer(this, 0, 3);

        this.subTentacles[num][0].addBox(-1.5F, -1.35F, -1.5F, 3.333F, length, 3.333F);
        this.subTentacles[num][0].rotationPointX = 0;
        this.subTentacles[num][0].rotationPointZ = 0;
        this.subTentacles[num][0].rotationPointY = length;

        this.tentacles[num].addChild(this.subTentacles[num][0]);

        length = 4;

        this.subTentacles[num][1] = new ModelRenderer(this, 0, 9);

        this.subTentacles[num][1].addBox(-1.5F, 1.3F, -1.5F, 3.333F, length, 3.333F);
        this.subTentacles[num][1].rotationPointX = 0;
        this.subTentacles[num][1].rotationPointZ = 0;
        this.subTentacles[num][1].rotationPointY = length;

        this.subTentacles[num][0].addChild(this.subTentacles[num][1]);


        this.body.addChild(this.tentacles[num]);
    }


    /**
     * Make one of the small tentacles
     */
    protected void makeSmallTentacle(int num) {
        ;
    }

    @Override
    public void setRotationAngles(EntityTFUrGhast entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // wave tentacles
        for (int i = 0; i < this.subTentacles.length; ++i) {

            float wiggle = Math.min(limbSwingAmount, 0.6F);

            float time = (ageInTicks + (i * 9)) / 2.0F;

            this.subTentacles[i][0].rotateAngleX = (MathHelper.cos(time * 0.6662F) - (float) Math.PI / 3.0F) * wiggle;
            this.subTentacles[i][1].rotateAngleX = MathHelper.cos(time * 0.7774F) * 1.2F * wiggle;

            this.subTentacles[i][0].rotateAngleX = 0.1F + MathHelper.cos(time * 0.3335F) * 0.15F;
            this.subTentacles[i][1].rotateAngleX = 0.1F + MathHelper.cos(time * 0.4445F) * 0.20F;

            float yTwist = 0.4F;

            this.tentacles[i].rotateAngleY = yTwist * MathHelper.sin(time * 0.3F);
        }
    }
}