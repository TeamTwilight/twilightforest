package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen;

public class RenderTFSnowQueen extends RenderBiped<EntityTFSnowQueen> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "snowqueen.png");

	public RenderTFSnowQueen(RenderManager manager) {
		super(manager, new ModelTFSnowQueen(), 0.625F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFSnowQueen entity) {
		return textureLoc;
	}

	@Override
	protected void preRenderCallback(EntityTFSnowQueen queen, float partialTicks) {
		float scale = 1.2F;
		GlStateManager.scale(scale, scale, scale);
	}

	@Override
	public void doRender(EntityTFSnowQueen queen, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(queen, x, y, z, yaw, partialTicks);

		for (int i = 0; i < queen.iceArray.length; i++) {
			renderManager.renderEntityStatic(queen.iceArray[i], partialTicks, false);
		}
	}
}
