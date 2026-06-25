package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.TwilightForestMod;
import twilightforest.client.state.entity.LichRenderState;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFDataAttachments;

public class ShieldLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

	private static final Identifier SHIELD_FRAME = TwilightForestMod.prefix("textures/item/lich_shield_frame.png");
	private static final Identifier SHIELD_FILL = TwilightForestMod.prefix("textures/item/lich_shield_fill.png");
	private static final net.minecraft.client.renderer.rendertype.RenderType FRAME_RENDER_TYPE = RenderTypes.entityCutout(SHIELD_FRAME);
	private static final net.minecraft.client.renderer.rendertype.RenderType FILL_RENDER_TYPE = RenderTypes.entityTranslucent(SHIELD_FILL);

	public static ContextKey<Integer> SHIELD_COUNT_KEY = new ContextKey<>(TwilightForestMod.prefix("shield_count"));

	public ShieldLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector submitNodeCollector, int light, S state, float netHeadYaw, float headPitch) {
		int count = getShieldCountFromState(state);
		if (count > 0) {
			this.renderShields(stack, submitNodeCollector, state, count, light);
		}
	}

	private static int getShieldCountFromState(LivingEntityRenderState state) {
		if (state instanceof LichRenderState lichState) {
			return lichState.shieldCount;
		}
		Integer count = state.getRenderData(SHIELD_COUNT_KEY);
		return count != null ? count : 0;
	}

	public static int getShieldCount(LivingEntity entity) {
		return entity instanceof Lich lich
			? (lich.getTeleportInvisibility() > 0 ? 0 : lich.getShieldStrength())
			: entity.getData(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft();
	}

	private void renderShields(PoseStack stack, SubmitNodeCollector submitNodeCollector, S state, int count, int light) {
		float age = state.ageInTicks;
		float rotateAngleY = age / -5.0F;
		float rotateAngleX = Mth.sin(age / 5.0F) / 4.0F;
		float rotateAngleZ = Mth.cos(age / 5.0F) / 4.0F;

		for (int c = 0; c < count; c++) {
			stack.pushPose();

			stack.mulPose(Axis.YP.rotationDegrees(rotateAngleY * (180.0F / Mth.PI) + (c * (360.0F / count))));
			stack.mulPose(Axis.XP.rotationDegrees(rotateAngleX * (180.0F / Mth.PI)));
			stack.mulPose(Axis.ZP.rotationDegrees(rotateAngleZ * (180.0F / Mth.PI)));

			stack.translate(0.0F, 0.4F, -0.7F);

			renderShieldQuad(stack, submitNodeCollector, FILL_RENDER_TYPE, 0xF000F0);
			renderShieldQuad(stack, submitNodeCollector, FRAME_RENDER_TYPE, 0xF000F0);

			stack.popPose();
		}
	}

	private static void renderShieldQuad(PoseStack stack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.rendertype.RenderType renderType, int light) {
		submitNodeCollector.submitCustomGeometry(stack, renderType, (pose, buffer) -> {
			buffer.addVertex(pose, -0.5F, 0.5F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, 0.5F, 0.5F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, 0.5F, -0.5F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, -0.5F, -0.5F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);

			buffer.addVertex(pose, -0.5F, -0.5F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, 0.5F, -0.5F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, 0.5F, 0.5F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, -0.5F, 0.5F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
		});
	}
}
