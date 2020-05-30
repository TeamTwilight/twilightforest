package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFMoonworm;
import twilightforest.entity.projectile.EntityTFMoonwormShot;

public class RenderTFMoonwormShot<T extends EntityTFMoonwormShot> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("moonworm.png");
	private final ModelTFMoonworm wormModel = new ModelTFMoonworm();

	public RenderTFMoonwormShot(EntityRendererManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		stack.push();
		//stack.translate(x, y, z);
		RenderSystem.rotatef(90F, 1F, 0F, 1F);

		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(getEntityTexture(entity)));
		wormModel.render(stack, builder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.075F);

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
