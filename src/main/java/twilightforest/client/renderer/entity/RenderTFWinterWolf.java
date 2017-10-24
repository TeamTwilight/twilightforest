package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFWinterWolf extends RenderWolf {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "winterwolf.png");

	public RenderTFWinterWolf(RenderManager manager) {
		super(manager);
		this.shadowSize = 1.0F;
	}

	@Override
	protected void preRenderCallback(EntityWolf par1EntityLiving, float par2) {
		float wolfScale = 1.9F;
		GlStateManager.scale(wolfScale, wolfScale, wolfScale);

//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        //GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        //GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_ALPHA);
//        
//        float misty = par1EntityLiving.getBrightness(0F) * 3F + 0.25F;
//        misty = Math.min(1F, misty);
//
//        float smoky = par1EntityLiving.getBrightness(0F) * 2F + 0.6F;
//
//        GlStateManager.color(misty, misty, misty, smoky);

	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
//        GL11.glFogf(GL11.GL_FOG_START, 1.0f);	
//        GL11.glFogf(GL11.GL_FOG_END, 5.0f);


		return -1;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWolf par1Entity) {
		return textureLoc;
	}

}
