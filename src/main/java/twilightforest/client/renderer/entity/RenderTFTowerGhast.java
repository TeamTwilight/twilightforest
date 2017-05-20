package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.monster.EntityGhast;

import org.lwjgl.opengl.GL11;

import twilightforest.client.model.ModelTFGhast;
import twilightforest.entity.EntityTFTowerGhast;
import twilightforest.entity.boss.EntityTFUrGhast;

/**
 * This is a copy of the RenderGhast class that changes the model
 *
 */
public class RenderTFTowerGhast extends RenderTFMiniGhast {
	
    private float ghastScale = 8.0F;

	public RenderTFTowerGhast(RenderManager renderManager, ModelTFGhast modelTFGhast, float f) {
		super(renderManager, modelTFGhast, f);
	}

	public RenderTFTowerGhast(RenderManager renderManager, ModelTFGhast modelTFGhast, float f, float scale) {
		super(renderManager, modelTFGhast, f);
		this.ghastScale = scale;
	}
	
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
	@Override
	public void doRender(EntityTFTowerGhast entity, double d, double d1, double d2, float f, float f1) {
		super.doRender(entity, d, d1, d2, f, f1);
		// show boss health bar, if applicable
		if (entity instanceof EntityTFUrGhast && entity.ticksExisted > 0)
		{
	        BossStatus.setBossStatus((EntityTFUrGhast)entity, false);
		}
	}

    /**
     * Pre-Renders the Ghast.
     */
    protected void preRenderGhast(EntityGhast par1EntityGhast, float par2)
    {
        float scaleVariable = ((float)par1EntityGhast.prevAttackCounter + (float)(par1EntityGhast.attackCounter - par1EntityGhast.prevAttackCounter) * par2) / 20.0F;

        if (scaleVariable < 0.0F)
        {
            scaleVariable = 0.0F;
        }

        scaleVariable = 1.0F / (scaleVariable * scaleVariable * scaleVariable * scaleVariable * scaleVariable * 2.0F + 1.0F);
		float yScale = (ghastScale + scaleVariable) / 2.0F;
        float xzScale = (ghastScale + 1.0F / scaleVariable) / 2.0F;
        GlStateManager.scale(xzScale, yScale, xzScale);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityTFTowerGhast par1EntityLiving, float par2)
    {
        this.preRenderGhast(par1EntityLiving, par2);
    }


}
