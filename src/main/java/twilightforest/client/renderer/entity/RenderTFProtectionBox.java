package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFProtectionBox;
import twilightforest.entity.EntityTFProtectionBox;

public class RenderTFProtectionBox extends Render<EntityTFProtectionBox> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "protectionbox.png");
	private final ModelTFProtectionBox boxModel = new ModelTFProtectionBox();

	public RenderTFProtectionBox(RenderManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityTFProtectionBox var1, double x, double y, double z, float var8, float partialTick) {
		GlStateManager.pushMatrix();


		GlStateManager.translate((float) x, (float) y, (float) z);

		this.bindTexture(textureLoc);

		// move texture
		float f1 = (float) var1.ticksExisted + partialTick;
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();
		float f2 = f1 * 0.05F;
		float f3 = f1 * 0.05F;
		GlStateManager.translate(f2, f3, 0.0F);
		GlStateManager.scale(1.0f, 1.0f, 1.0f);

		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		// enable transparency, go full brightness
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlpha();
		GlStateManager.disableLighting();

		float alpha = 1.0F;
		if (var1.lifeTime < 20) {
			alpha = var1.lifeTime / 20F;
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

		boxModel.render(var1, 0F, 0F, 0F, 0F, 0F, 1F / 16F);

		GlStateManager.disableBlend();
		GlStateManager.enableCull();

		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFProtectionBox par1Entity) {
		return textureLoc;
	}

}
