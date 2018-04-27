package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFTinyFirefly;
import twilightforest.entity.passive.EntityTFMobileFirefly;

import java.nio.FloatBuffer;

public class RenderTFMobileFirefly extends Render<EntityTFMobileFirefly> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "firefly-tiny.png");
	private final ModelTFTinyFirefly fireflyModel = new ModelTFTinyFirefly();

	public RenderTFMobileFirefly(RenderManager manager) {
		super(manager);
	}

	private void doRenderTinyFirefly(EntityTFMobileFirefly firefly, double x, double y, double z, float brightness, float size) {
		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);

		// undo rotations so we can draw a billboarded firefly
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = i * 4 + j;
				if (i == j) {
					modelview.put(index, 1.0f);
				} else {
					modelview.put(index, 0.0f);
				}
			}
		}

		GL11.glLoadMatrix(modelview);

		bindEntityTexture(firefly);

		GlStateManager.colorMask(true, true, true, true);

		// render the firefly glow
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();


//		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, brightness);
		fireflyModel.glow1.render(0.0625f * size);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}


	@Override
	public void doRender(EntityTFMobileFirefly firefly, double d, double d1, double d2, float f, float f1) {
		doRenderTinyFirefly(firefly, d, d1, d2, firefly.getGlowBrightness(), 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFMobileFirefly par1Entity) {
		return textureLoc;
	}

}
