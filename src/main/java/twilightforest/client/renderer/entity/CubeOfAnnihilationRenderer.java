package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CubeOfAnnihilationModel;
import twilightforest.entity.projectile.CubeOfAnnihilation;

public class CubeOfAnnihilationRenderer extends EntityRenderer<CubeOfAnnihilation, EntityRenderState> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model;

	public CubeOfAnnihilationRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CubeOfAnnihilationModel(context.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}

	@Override
	public void render(EntityRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(state, stack, buffer, light);

		stack.pushPose();
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(state.ageInTicks * 11.0F)));
		stack.translate(0.0F, -0.5F, 0.0F);
		this.model.renderToBuffer(stack, buffer.getBuffer(this.model.renderType(TEXTURE)), light, OverlayTexture.NO_OVERLAY);
		stack.popPose();
	}
}
