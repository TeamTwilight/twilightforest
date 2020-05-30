package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFCubeOfAnnihilation;
import twilightforest.entity.EntityTFCubeOfAnnihilation;

public class RenderTFCubeOfAnnihilation<T extends EntityTFCubeOfAnnihilation> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model = new ModelTFCubeOfAnnihilation();

	public RenderTFCubeOfAnnihilation(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);

		stack.push();
		//stack.translate((float) x, (float) y, (float) z);

//		this.bindEntityTexture(entity);

		stack.scale(-1.0F, -1.0F, 1.0F);

//		RenderSystem.rotatef(MathHelper.wrapDegrees(((float) x + (float) z + entity.ticksExisted + partialTicks) * 11F), 0, 1, 0);

		RenderSystem.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		stack.translate(0F, -0.5F, 0F);
//		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, partialTicks, 0.0625F / 2F);
		RenderSystem.enableLighting();
		GlStateManager.disableBlend();

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFCubeOfAnnihilation entity) {
		return textureLoc;
	}
}
