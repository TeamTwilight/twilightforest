package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFCubeOfAnnihilation;
import twilightforest.entity.EntityTFRovingCube;

public class RenderTFRovingCube<T extends EntityTFRovingCube> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model = new ModelTFCubeOfAnnihilation();

	public RenderTFRovingCube(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		stack.push();
		//stack.translate(x, y, z);

		//this.bindEntityTexture(entity);
		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(textureLoc));

		stack.scale(2.0F, 2.0F, 2.0F);

		//RenderSystem.rotatef(MathHelper.wrapDegrees(((float) x + (float) z + entity.ticksExisted + partialTicks) * 11F), 0, 1, 0);

		RenderSystem.disableLighting();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		stack.translate(0F, 0.75F, 0F);
		//this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, partialTicks, 0.0625F / 2F);
		this.model.render(stack, builder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
