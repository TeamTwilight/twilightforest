package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFNagaSegment;

public class RenderTFNagaSegment extends Render<EntityTFNagaSegment> {
    private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "nagasegment.png");
    private final ModelBase model;

	public RenderTFNagaSegment(RenderManager manager, ModelBase model) {
	    super(manager);
	    this.model = model;
	}

    /**
     * The render method used in RenderBoat that renders the boat model.
     */
    public void renderMe(Entity par1Entity, double par2, double par4, double par6, float par8, float time)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)par2, (float)par4, (float)par6);
        GlStateManager.rotate(180 - MathHelper.wrapDegrees(par1Entity.rotationYaw), 0.0F, 1.0F, 0.0F);
        
        // pitch
        float pitch = par1Entity.rotationPitch;
        
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);


//        float f4 = 0.75F;
//        GlStateManager.scale(f4, f4, f4);
//        GlStateManager.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
        //this.loadTexture(par1Entity.getTexture());
        this.bindTexture(textureLoc);

        
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        this.model.render(par1Entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    public void doRender(EntityTFNagaSegment par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderMe(par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTFNagaSegment par1Entity)
    {
        return textureLoc;
    }
}
