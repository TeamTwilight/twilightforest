package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import twilightforest.entity.RovingCube;

public class RovingCubeRenderer extends EntityRenderer<RovingCube, EntityRenderState> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final Model model;

	public RovingCubeRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CubeOfAnnihilationModel(context.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public void render(EntityRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();

		VertexConsumer consumer = buffer.getBuffer(this.model.renderType(TEXTURE));

		stack.scale(2.0F, 2.0F, 2.0F);
		stack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(state.ageInTicks) * 11.0F));
		stack.translate(0.0F, 0.75F, 0.0F);
		this.model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		stack.popPose();
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}
