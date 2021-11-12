package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CubeOfAnnihilationModel;
import twilightforest.entity.CubeOfAnnihilation;

public class CubeOfAnnihilationRenderer extends EntityRenderer<CubeOfAnnihilation> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model;

	public CubeOfAnnihilationRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		model = new CubeOfAnnihilationModel(manager.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public void render(CubeOfAnnihilation entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);

		stack.pushPose();

		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.mulPose(Vector3f.YP.rotationDegrees(Mth.wrapDegrees((entity.tickCount + partialTicks) * 11F)));
		stack.translate(0F, -0.5F, 0F);
		model.renderToBuffer(stack, buffer.getBuffer(model.renderType(textureLoc)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(CubeOfAnnihilation entity) {
		return textureLoc;
	}
}
