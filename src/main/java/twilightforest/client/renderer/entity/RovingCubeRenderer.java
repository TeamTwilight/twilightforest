package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CubeOfAnnihilationModel;
import twilightforest.entity.RovingCube;

public class RovingCubeRenderer<T extends RovingCube> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model;

	public RovingCubeRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.model = new CubeOfAnnihilationModel(manager.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();

		VertexConsumer builder = buffer.getBuffer(model.renderType(textureLoc));

		stack.scale(2.0F, 2.0F, 2.0F);
		stack.mulPose(Vector3f.YP.rotationDegrees(Mth.wrapDegrees(entity.tickCount + partialTicks) * 11F));
		stack.translate(0F, 0.75F, 0F);
		this.model.renderToBuffer(stack, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
