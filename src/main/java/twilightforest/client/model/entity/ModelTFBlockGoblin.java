package twilightforest.client.model.entity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.EntityTFBlockGoblin;

/**
 * ModelMaceGoblin - MCVinnyq
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelTFBlockGoblin<T extends EntityTFBlockGoblin> extends BipedModel<T> {
    public ModelRenderer helmet;
    public ModelRenderer horns;

    ModelRenderer block;
    ModelRenderer[] spikes = new ModelRenderer[27];

    public ModelTFBlockGoblin() {
        super(0, 0, 64, 48);
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, 12.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(52, 2).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bipedRightArm, 0.0F, 0.0F, 3.0543261909900767F);
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(5.0F, 12.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(52, 17).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bipedLeftArm, 0.0F, 0.0F, -3.0543261909900767F);
        this.horns = new ModelRenderer(this, 0, 0);
        this.horns.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horns.setTextureOffset(0, 18).addBox(-7.5F, -9.0F, -2.03F, 15.0F, 10.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horns, 0.0F, -0.7853981633974483F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 0.0F);
        this.bipedRightLeg.setTextureOffset(0, 33).addBox(-1.4F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 0.0F);
        this.bipedLeftLeg.setTextureOffset(12, 33).addBox(-1.6F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 0);
        this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedBody.setTextureOffset(28, 6).addBox(-3.5F, 1.0F, -2.0F, 7.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.helmet = new ModelRenderer(this, 0, 0);
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet.setTextureOffset(0, 5).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(helmet, 0.0F, 0.7853981633974483F, 0.0F);
        //kill the head and headwear to prevent issues
        bipedHead = new ModelRenderer(this, 0, 0);
        bipedHead.addBox(0F, 0F, 0F, 0, 0, 0, 0F);
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.addBox(0, 0, 0, 0, 0, 0);

        this.helmet.addChild(this.horns);
        this.bipedHeadwear.addChild(helmet);

        block = new ModelRenderer(this, 32, 32);
        block.addBox(-4F, -8F, -4F, 8, 8, 8, 0F);
        block.setRotationPoint(0F, 0F, 0F);

        for (int i = 0; i < spikes.length; i++) {
            spikes[i] = new ModelRenderer(this, 56, 36);
            spikes[i].addBox(-1F, -1F, -1F, 2, 2, 2, 0F);
            block.addChild(spikes[i]);
        }

        // X
        spikes[2].rotationPointX = 4;
        spikes[3].rotationPointX = 4;
        spikes[4].rotationPointX = 4;
        spikes[11].rotationPointX = 4;
        spikes[12].rotationPointX = 5;
        spikes[13].rotationPointX = 4;
        spikes[20].rotationPointX = 4;
        spikes[21].rotationPointX = 4;
        spikes[22].rotationPointX = 4;

        spikes[6].rotationPointX = -4;
        spikes[7].rotationPointX = -4;
        spikes[8].rotationPointX = -4;
        spikes[15].rotationPointX = -4;
        spikes[16].rotationPointX = -5;
        spikes[17].rotationPointX = -4;
        spikes[24].rotationPointX = -4;
        spikes[25].rotationPointX = -4;
        spikes[26].rotationPointX = -4;

        // Y
        spikes[0].rotationPointY = -9;
        spikes[1].rotationPointY = -8;
        spikes[2].rotationPointY = -8;
        spikes[3].rotationPointY = -8;
        spikes[4].rotationPointY = -8;
        spikes[5].rotationPointY = -8;
        spikes[6].rotationPointY = -8;
        spikes[7].rotationPointY = -8;
        spikes[8].rotationPointY = -8;

        spikes[9].rotationPointY = -4; // this spike is not really there
        spikes[10].rotationPointY = -4;
        spikes[11].rotationPointY = -4;
        spikes[12].rotationPointY = -4;
        spikes[13].rotationPointY = -4;
        spikes[14].rotationPointY = -4;
        spikes[15].rotationPointY = -4;
        spikes[16].rotationPointY = -4;
        spikes[17].rotationPointY = -4;

        spikes[18].rotationPointY = 1;

        // Z
        spikes[1].rotationPointZ = 4;
        spikes[2].rotationPointZ = 4;
        spikes[8].rotationPointZ = 4;
        spikes[10].rotationPointZ = 4;
        spikes[11].rotationPointZ = 5;
        spikes[17].rotationPointZ = 4;
        spikes[19].rotationPointZ = 4;
        spikes[20].rotationPointZ = 4;
        spikes[26].rotationPointZ = 4;

        spikes[4].rotationPointZ = -4;
        spikes[5].rotationPointZ = -4;
        spikes[6].rotationPointZ = -4;
        spikes[13].rotationPointZ = -4;
        spikes[14].rotationPointZ = -5;
        spikes[15].rotationPointZ = -4;
        spikes[22].rotationPointZ = -4;
        spikes[23].rotationPointZ = -4;
        spikes[24].rotationPointZ = -4;

        // rotation
        float fourtyFive = (float) (Math.PI / 4F);

        spikes[1].rotateAngleX = fourtyFive;
        spikes[5].rotateAngleX = fourtyFive;
        spikes[19].rotateAngleX = fourtyFive;
        spikes[23].rotateAngleX = fourtyFive;

        spikes[11].rotateAngleY = fourtyFive;
        spikes[13].rotateAngleY = fourtyFive;
        spikes[15].rotateAngleY = fourtyFive;
        spikes[17].rotateAngleY = fourtyFive;

        spikes[3].rotateAngleZ = fourtyFive;
        spikes[7].rotateAngleZ = fourtyFive;
        spikes[21].rotateAngleZ = fourtyFive;
        spikes[25].rotateAngleZ = fourtyFive;

        spikes[2].rotateAngleX = -55F / (180F / (float) Math.PI);
        spikes[2].rotateAngleY = fourtyFive;
        spikes[24].rotateAngleX = -55F / (180F / (float) Math.PI);
        spikes[24].rotateAngleY = fourtyFive;

        spikes[4].rotateAngleX = -35F / (180F / (float) Math.PI);
        spikes[4].rotateAngleY = -fourtyFive;
        spikes[26].rotateAngleX = -35F / (180F / (float) Math.PI);
        spikes[26].rotateAngleY = -fourtyFive;

        spikes[6].rotateAngleY = fourtyFive;
        spikes[6].rotateAngleX = -35F / (180F / (float) Math.PI);
        spikes[20].rotateAngleY = fourtyFive;
        spikes[20].rotateAngleX = -35F / (180F / (float) Math.PI);

        spikes[8].rotateAngleX = -55F / (180F / (float) Math.PI);
        spikes[8].rotateAngleY = -fourtyFive;
        spikes[22].rotateAngleX = -55F / (180F / (float) Math.PI);
        spikes[22].rotateAngleY = -fourtyFive;
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        bipedHead.rotationPointY = 11.0F;
        bipedHeadwear.rotationPointY = 11.0F;
        bipedBody.rotationPointY = 11F;

        bipedRightLeg.rotationPointY = 18F;
        bipedLeftLeg.rotationPointY = 18F;

        bipedRightArm.setRotationPoint(-3.5F, 12F, 0F);
        bipedRightArm.rotateAngleX += Math.PI;

        bipedLeftArm.setRotationPoint(3.5F, 12F, 0F);
        bipedLeftArm.rotateAngleX += Math.PI;

        float angle = ageInTicks / 4F;
        float length = 0;//16F;

        block.rotationPointX = (float) Math.sin(angle) * length;
        block.rotationPointZ = (float) -Math.cos(angle) * length;

        block.rotateAngleY = -angle;
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
