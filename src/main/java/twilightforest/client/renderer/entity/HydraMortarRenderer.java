package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraMortarModel;
import twilightforest.client.state.HydraMortarRenderState;
import twilightforest.entity.boss.HydraMortar;

public class HydraMortarRenderer extends EntityRenderer<HydraMortar, HydraMortarRenderState> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydramortar.png");
	private final HydraMortarModel mortarModel;

	public HydraMortarRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.mortarModel = new HydraMortarModel(context.bakeLayer(TFModelLayers.HYDRA_MORTAR));
	}

	@Override
	public void render(HydraMortarRenderState state, PoseStack stack, MultiBufferSource source, int light) {
		stack.pushPose();
		// [VanillaCopy] TNTRenderer fuse logic
		float f = state.fuse;
		if (state.fuse < 10.0F) {
			float f1 = 1.0F - state.fuse / 10.0F;
			f1 = Mth.clamp(f1, 0.0F, 1.0F);
			f1 *= f1;
			f1 *= f1;
			float f2 = 1.0F + f1 * 0.3F;
			stack.scale(f2, f2, f2);
		}

		float alpha = (1.0F - f / 100.0F) * 0.8F;

		VertexConsumer consumer = source.getBuffer(this.mortarModel.renderType(TEXTURE));
		this.mortarModel.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		if (state.fuse / 5 % 2 == 0) {
			this.mortarModel.renderToBuffer(stack, consumer, light, OverlayTexture.pack(OverlayTexture.u(1.0F), 10), ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
		}

		stack.popPose();
	}

	@Override
	public HydraMortarRenderState createRenderState() {
		return new HydraMortarRenderState();
	}

	@Override
	public void extractRenderState(HydraMortar entity, HydraMortarRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.fuse = entity.fuse - partialTick + 1.0F;
	}
}
