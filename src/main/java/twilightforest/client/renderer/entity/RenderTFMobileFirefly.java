package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFTinyFirefly;
import twilightforest.entity.passive.EntityTFMobileFirefly;

import java.nio.FloatBuffer;

public class RenderTFMobileFirefly<T extends EntityTFMobileFirefly> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");
	private final ModelTFTinyFirefly fireflyModel = new ModelTFTinyFirefly();

	public RenderTFMobileFirefly(EntityRendererManager manager) {
		super(manager);
	}

	private void doRenderTinyFirefly(T firefly, MatrixStack matrix, float brightness, float size) {
		matrix.push();

//		matrix.translate(x, y + 0.5D, z);

		// undo rotations so we can draw a billboarded firefly
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);

//		RenderSystem.getFloat(GL11.GL_MODELVIEW_MATRIX, modelview);

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

		GL11.glLoadMatrixf(modelview);

//		bindEntityTexture(firefly);

		RenderSystem.colorMask(true, true, true, true);

		// render the firefly glow
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.enableAlphaTest();
		RenderSystem.disableLighting();


//		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, brightness);
//		fireflyModel.glow1.render(0.0625f * size);
		RenderSystem.disableBlend();
		RenderSystem.enableLighting();

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		matrix.pop();
	}

	@Override
	public void render(T firefly, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		doRenderTinyFirefly(firefly, stack, firefly.getGlowBrightness(), 1.0F);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
