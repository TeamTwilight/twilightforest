package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.client.state.MoonwormShotRenderState;
import twilightforest.entity.projectile.MoonwormShot;

public class MoonwormShotRenderer extends EntityRenderer<MoonwormShot, MoonwormShotRenderState> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("moonworm.png");
	private final MoonwormModel model;

	public MoonwormShotRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		this.model = new MoonwormModel(context.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void render(MoonwormShotRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.translate(0.0F, 0.5F, 0.0F);
		stack.scale(-1.0F, -1.0F, -1.0F);

		stack.mulPose(Axis.YP.rotationDegrees(state.yRot - 180.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(state.xRot));

		VertexConsumer consumer = buffer.getBuffer(this.model.renderType(TEXTURE));
		this.model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		stack.popPose();
	}

	@Override
	public MoonwormShotRenderState createRenderState() {
		return new MoonwormShotRenderState();
	}

	@Override
	public void extractRenderState(MoonwormShot entity, MoonwormShotRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.xRot = entity.getXRot(partialTick);
		state.yRot = entity.getYRot(partialTick);
	}
}
