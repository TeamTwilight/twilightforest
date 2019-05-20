package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelTFIceCrystal extends ModelBase {

	public ModelRenderer[] spikes = new ModelRenderer[16];


	public ModelTFIceCrystal() {
		this.textureWidth = 32;
		this.textureHeight = 32;

		float par1 = 0F;
		float par2 = 0F;

		// spikes
		for (int i = 0; i < spikes.length; i++) {
			this.spikes[i] = new ModelRenderer(this, 0, 16);

			int spikeLength = i % 2 == 0 ? 6 : 8;

			this.spikes[i].addBox(-1.0F, -1.0F, -1.0F, 2, spikeLength, 2, par1);
			this.spikes[i].setRotationPoint(0.0F, 0.0F + par2, 0.0F);

			ModelRenderer cube = new ModelRenderer(this, 8, 16);
			cube.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
			cube.setRotationPoint(0.0F, spikeLength, 0.0F);

			cube.rotateAngleZ = (float) (Math.PI / 4F);

			this.spikes[i].addChild(cube);

		}
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		//this.bipedHead.render(scale);

		for (int i = 0; i < spikes.length; i++) {

			if (entity.isEntityAlive()) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GlStateManager.color(1F, 1F, 1F, 0.6F);
			}

			this.spikes[i].render(scale);

			GlStateManager.disableBlend();
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		for (int i = 0; i < spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].rotateAngleX = MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
			this.spikes[i].rotateAngleY = (entity.ticksExisted + partialTicks) / 5.0F;
			this.spikes[i].rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

			this.spikes[i].rotateAngleX += i * (Math.PI / 8F);

			if (i % 4 == 0) {
				this.spikes[i].rotateAngleY += 1;
			} else if (i % 4 == 2) {
				this.spikes[i].rotateAngleY -= 1;
			}

			//this.spikes[i].rotateAngleY += i * (Math.PI / 8F);
			//this.spikes[i].rotateAngleZ += i * (Math.PI / 8F);


//        	this.spikes[i].rotationPointX = MathHelper.cos((entity.ticksExisted + partialTicks) / (float)i) * 3F;
//        	this.spikes[i].rotationPointY = 5F + MathHelper.sin((entity.ticksExisted + partialTicks) / (float)i) * 3F;
//        	this.spikes[i].rotationPointZ = MathHelper.sin((entity.ticksExisted + partialTicks) / (float)i) * 3F;

			//((ModelRenderer)this.spikes[i].childModels.get(0)).rotationPointY = 10 + MathHelper.sin((i + entity.ticksExisted + partialTicks) / i) * 3F;

		}
	}
}
