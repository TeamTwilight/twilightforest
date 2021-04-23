package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFProtectionBox;
import twilightforest.entity.EntityTFProtectionBox;

public class RenderTFProtectionBox<T extends EntityTFProtectionBox> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("protectionbox.png");
	private final ModelTFProtectionBox<T> boxModel = new ModelTFProtectionBox<>();

	public RenderTFProtectionBox(EntityRendererManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		stack.push();

		/* todo 1.15
		this.bindTexture(textureLoc);

		// move texture
		float f1 = (float) entity.ticksExisted + partialTicks;
		RenderSystem.matrixMode(GL11.GL_TEXTURE);
		RenderSystem.loadIdentity();
		float f2 = f1 * 0.05F;
		float f3 = f1 * 0.05F;
		stack.translate(f2, f3, 0.0F);
		stack.scale(1.0f, 1.0f, 1.0f);

		RenderSystem.matrixMode(GL11.GL_MODELVIEW);

		// enable transparency, go full brightness
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.disableAlphaTest();
		RenderSystem.disableLighting();

		float alpha = 1.0F;
		if (entity.lifeTime < 20) {
			alpha = entity.lifeTime / 20F;
		}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

		boxModel.render(entity, 0F, 0F, 0F, 0F, 0F, 1F / 16F);

		RenderSystem.disableBlend();
		RenderSystem.enableCull();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableLighting();

		RenderSystem.matrixMode(GL11.GL_TEXTURE);
		RenderSystem.loadIdentity();
		RenderSystem.matrixMode(GL11.GL_MODELVIEW);

		 */

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
