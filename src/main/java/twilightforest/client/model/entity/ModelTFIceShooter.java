package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelTFIceShooter extends ModelTFIceExploder {


	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		for (int i = 0; i < spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].rotateAngleY = (3.14159F / 2F) + (MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) * 0.5F);
			this.spikes[i].rotateAngleX = (entity.ticksExisted + partialTicks) / 5.0F;
			this.spikes[i].rotateAngleZ = MathHelper.cos(i / 5.0F) / 4.0F;

			this.spikes[i].rotateAngleX += i * (Math.PI / 8F);
			//this.spikes[i].rotateAngleY += i * 2.5f;
			//this.spikes[i].rotateAngleZ += i * 3;

			((ModelRenderer) this.spikes[i].childModels.get(0)).rotationPointY = 9.5F + MathHelper.sin((i + entity.ticksExisted + partialTicks) / 3F) * 3F;


			//((ModelBox)this.spikes[i].cubeList.get(0)). = 6 + MathHelper.sin((entity.ticksExisted + partialTicks) / (float)i) * 3F;

		}
	}
}
