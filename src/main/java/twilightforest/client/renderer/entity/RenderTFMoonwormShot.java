package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFMoonworm;
import twilightforest.entity.EntityTFMoonwormShot;

public class RenderTFMoonwormShot extends Render<EntityTFMoonwormShot> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "moonworm.png");
	private final ModelTFMoonworm wormModel = new ModelTFMoonworm();
	;

	public RenderTFMoonwormShot(RenderManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityTFMoonwormShot var1, double x, double y, double z, float var8, float partialTick) {

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(90F, 1F, 0F, 1F);

		this.bindTexture(getEntityTexture(var1));
		wormModel.render(0.075F);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFMoonwormShot par1Entity) {
		return textureLoc;
	}
}
