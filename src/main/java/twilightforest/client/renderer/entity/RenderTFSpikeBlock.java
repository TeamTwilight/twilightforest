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

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");
	private final ModelBase model;

	public RenderTFSpikeBlock(RenderManager manager, ModelBase modelTFSpikeBlock) {
		super(manager);
		this.model = modelTFSpikeBlock;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(180 - MathHelper.wrapDegrees(yaw), 0.0F, 1.0F, 0.0F);

		float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
		this.bindEntityTexture(entity);

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return textureLoc;
	}
}
