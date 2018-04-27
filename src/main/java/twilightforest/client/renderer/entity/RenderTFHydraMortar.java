package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFHydraMortar;
import twilightforest.entity.boss.EntityTFHydraMortar;

public class RenderTFHydraMortar extends Render<EntityTFHydraMortar> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "hydramortar.png");
	private final ModelTFHydraMortar mortarModel = new ModelTFHydraMortar();

	public RenderTFHydraMortar(RenderManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityTFHydraMortar mortar, double x, double y, double z, float var8, float partialTick) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		float var10;

		if ((float) mortar.fuse - partialTick + 1.0F < 10.0F) {
			var10 = 1.0F - ((float) mortar.fuse - partialTick + 1.0F) / 10.0F;

			if (var10 < 0.0F) {
				var10 = 0.0F;
			}

			if (var10 > 1.0F) {
				var10 = 1.0F;
			}

			var10 *= var10;
			var10 *= var10;
			float var11 = 1.0F + var10 * 0.3F;
			GlStateManager.scale(var11, var11, var11);
		}

		var10 = (1.0F - ((float) mortar.fuse - partialTick + 1.0F) / 100.0F) * 0.8F;
		this.bindTexture(textureLoc);

		mortarModel.render(0.075F);

		if (mortar.fuse / 5 % 2 == 0) {
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, var10);

			mortarModel.render(0.075F);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
		}

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFHydraMortar par1Entity) {
		return textureLoc;
	}
}
