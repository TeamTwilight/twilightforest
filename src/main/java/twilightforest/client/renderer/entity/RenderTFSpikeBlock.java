package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;

public class RenderTFSpikeBlock extends Render<Entity> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "blockgoblin.png");
	private final ModelBase model;

	public RenderTFSpikeBlock(RenderManager manager, ModelBase modelTFSpikeBlock) {
		super(manager);
		this.model = modelTFSpikeBlock;
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(par2, par4, par6);
		GlStateManager.rotate(180 - MathHelper.wrapDegrees(par8), 0.0F, 1.0F, 0.0F);

		float pitch = par1Entity.prevRotationPitch + (par1Entity.rotationPitch - par1Entity.prevRotationPitch) * par9;

		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
		this.bindEntityTexture(par1Entity);

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.model.render(par1Entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return textureLoc;
	}
}
