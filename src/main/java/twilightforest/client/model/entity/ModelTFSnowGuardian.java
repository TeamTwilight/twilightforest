package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelTFSnowGuardian extends ModelBiped {


	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float partialTick) {
		//float bounce = par1EntityLiving.ticksExisted + partialTick;
		//par1EntityLiving.yOffset = 0.5F + MathHelper.sin((bounce) * 0.3F) * 0.5F;
	}
}
