package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFBlockGoblin extends ModelBiped {
	public ModelRenderer helmet;

	ModelRenderer block;
	ModelRenderer[] spikes = new ModelRenderer[27];

	public ModelTFBlockGoblin() {
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(0F, 0F, 0F, 0, 0, 0, 0F);
		bipedHead.setRotationPoint(0F, 11F, 0F);

		bipedHeadwear = new ModelRenderer(this, 0, 0);
		bipedHeadwear.addBox(0F, 0F, 0F, 0, 0, 0, 0.5F);
		bipedHeadwear.setRotationPoint(0F, 11F, 0F);

		this.helmet = new ModelRenderer(this, 24, 0);
		this.helmet.addBox(-2.5F, -9.0F, -2.5F, 5, 9, 5);
		this.helmet.rotateAngleY = 45F / (180F / (float) Math.PI);

		this.bipedHeadwear.addChild(helmet);

		bipedBody = new ModelRenderer(this, 0, 21);
		bipedBody.addBox(-3.5F, 0F, -2F, 7, 7, 4, 0F);
		bipedBody.setRotationPoint(0F, 11F, 0F);

		bipedRightArm = new ModelRenderer(this, 52, 0);
		bipedRightArm.addBox(-3F, -1F, -2F, 3, 12, 3, 0F);
		bipedRightArm.setRotationPoint(-3.5F, 12F, 0F);

		bipedLeftArm = new ModelRenderer(this, 52, 0);
		bipedLeftArm.addBox(0F, -1F, -1.5F, 3, 12, 3, 0F);
		bipedLeftArm.setRotationPoint(3.5F, 12F, 0F);

		bipedRightLeg = new ModelRenderer(this, 0, 12);
		bipedRightLeg.addBox(-1.5F, 0F, -1.5F, 3, 6, 3, 0F);
		bipedRightLeg.setRotationPoint(-2F, 18F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 0, 12);
		bipedLeftLeg.addBox(-1.5F, 0F, -1.5F, 3, 6, 3, 0F);
		bipedLeftLeg.setRotationPoint(2F, 18F, 0F);

		block = new ModelRenderer(this, 32, 16);
		block.addBox(-4F, -8F, -4F, 8, 8, 8, 0F);
		block.setRotationPoint(6F, 0F, 0F);

		for (int i = 0; i < spikes.length; i++) {
			spikes[i] = new ModelRenderer(this, 56, 16);
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
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);

		//block.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float yaw, float pitch, float time, Entity entity) {
		super.setRotationAngles(f, f1, f2, yaw, pitch, time, entity);

		bipedHead.rotationPointY = 11.0F;
		bipedHeadwear.rotationPointY = 11.0F;
		bipedBody.rotationPointY = 11F;

		bipedRightLeg.rotationPointY = 18F;
		bipedLeftLeg.rotationPointY = 18F;

		bipedRightArm.setRotationPoint(-3.5F, 12F, 0F);

		bipedRightArm.rotateAngleX += Math.PI;

		bipedLeftArm.setRotationPoint(3.5F, 12F, 0F);


		bipedLeftArm.rotateAngleX += Math.PI;

		float angle = f2 / 4F;
		float length = 0;//16F;

		block.rotationPointX = (float) Math.sin(angle) * length;
		block.rotationPointZ = (float) -Math.cos(angle) * length;


		block.rotateAngleY = -angle;

	}
}
