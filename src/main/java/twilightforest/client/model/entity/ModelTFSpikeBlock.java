package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFSpikeBlock extends ModelBase {
	ModelRenderer block;
	ModelRenderer[] spikes = new ModelRenderer[27];

	public ModelTFSpikeBlock() {

		block = new ModelRenderer(this, 32, 16);
		block.addBox(-4F, -8F, -4F, 8, 8, 8, 0F);
		block.setRotationPoint(0F, 0F, 0F);

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
		block.render(f5);
	}

}
