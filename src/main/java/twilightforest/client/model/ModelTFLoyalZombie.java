package twilightforest.client.model;


import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ModelTFLoyalZombie extends ModelZombie {
	
    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float time)
    {
    	// GREEEEN
        GlStateManager.color(0.25F, 2.0F, 0.25F);
    }

}
