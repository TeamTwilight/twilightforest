package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class ModelTFTowerBoss extends ModelTFGhast {

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
		this.tentacles[num] = new ModelRenderer(this, num % 3, 0);

		int length = 5;

		this.tentacles[num].addBox(-1.5F, 0.0F, -1.5F, 3, length, 3);


		if (num == 0) {
			this.tentacles[num].rotationPointX = 4.5F;
			this.tentacles[num].rotationPointZ = 4.5F;
			this.tentacles[num].rotationPointY = (float) (23 + yOffset);
		}
		if (num == 1) {
			this.tentacles[num].rotationPointX = -4.5F;
			this.tentacles[num].rotationPointZ = 4.5F;
			this.tentacles[num].rotationPointY = (float) (23 + yOffset);
		}
		if (num == 2) {
			this.tentacles[num].rotationPointX = 0F;
			this.tentacles[num].rotationPointZ = 0F;
			this.tentacles[num].rotationPointY = (float) (23 + yOffset);
		}
		if (num == 3) {
			this.tentacles[num].rotationPointX = 5.5F;
			this.tentacles[num].rotationPointZ = -4.5F;
			this.tentacles[num].rotationPointY = (float) (23 + yOffset);
		}
		if (num == 4) {
			this.tentacles[num].rotationPointX = -5.5F;
			this.tentacles[num].rotationPointZ = -4.5F;
			this.tentacles[num].rotationPointY = (float) (23 + yOffset);
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
			this.subTentacles = new ModelRenderer[tentacles.length][3];
		}

		for (int i = 0; i < 3; i++) {
			length = 4;

			this.subTentacles[num][i] = new ModelRenderer(this, num % 4, (i * 5) - 1);

			this.subTentacles[num][i].addBox(-1.5F, -0.5F, -1.5F, 3, length, 3);
			this.subTentacles[num][i].rotationPointX = 0;
			this.subTentacles[num][i].rotationPointZ = 0;
			this.subTentacles[num][i].rotationPointY = length;

			if (i == 0) {
				this.tentacles[num].addChild(this.subTentacles[num][i]);
			} else {
				this.subTentacles[num][i - 1].addChild(this.subTentacles[num][i]);
			}
		}

		this.body.addChild(this.tentacles[num]);

	}


	/**
	 * Make one of the small tentacles
	 *
	 * @param i
	 */
	protected void makeSmallTentacle(int num) {
		;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float timeAlive, float yaw, float pitch, float par6, Entity par7Entity) {
		super.setRotationAngles(par1, par2, timeAlive, yaw, pitch, par6, par7Entity);

		// wave tentacles
		for (int i = 0; i < this.subTentacles.length; ++i) {
//            for (int j = 0; j < this.subTentacles[i].length; ++j)
//            {
//            	this.subTentacles[i][j].rotateAngleX = 0.8F * MathHelper.sin(i * 2.3F) + 0.3F * MathHelper.sin(j) + 0.2F;
//            	
//            }

			float wiggle = Math.min(par2, 0.6F);

			float time = (timeAlive + (i * 9)) / 2.0F;

			this.subTentacles[i][0].rotateAngleX = (MathHelper.cos(time * 0.6662F) * 1.0F - (float) Math.PI / 3.0F) * wiggle;
			this.subTentacles[i][1].rotateAngleX = MathHelper.cos(time * 0.7774F) * 1.2F * wiggle;
			this.subTentacles[i][2].rotateAngleX = MathHelper.cos(time * 0.8886F + (float) Math.PI / 2.0F) * 1.4F * wiggle;

			this.subTentacles[i][0].rotateAngleX = 0.2F + MathHelper.cos(time * 0.3335F) * 0.15F;
			this.subTentacles[i][1].rotateAngleX = 0.1F + MathHelper.cos(time * 0.4445F) * 0.20F;
			this.subTentacles[i][2].rotateAngleX = 0.1F + MathHelper.cos(time * 0.5555F) * 0.25F;


			float yTwist = 0.4F;

			this.tentacles[i].rotateAngleY = yTwist * MathHelper.sin(time * 0.3F);

		}
	}

}
