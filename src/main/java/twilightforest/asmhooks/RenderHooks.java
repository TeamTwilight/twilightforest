package twilightforest.asmhooks;

import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.context.ContextKey;
import twilightforest.TwilightForestMod;

@SuppressWarnings({"JavadocReference", "unused"})
public class RenderHooks {

	public static final ContextKey<Boolean> HIDE_HEAD_KEY = new ContextKey<>(TwilightForestMod.prefix("hide_head"));

	/**
	 * {@link twilightforest.asm.transformers.render.HideHeadUnderTrophyTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.feature.ModelFeatureRenderer#renderModel(SubmitNodeStorage.ModelSubmit, net.minecraft.client.renderer.rendertype.RenderType, com.mojang.blaze3d.vertex.VertexConsumer, net.minecraft.client.renderer.OutlineBufferSource, net.minecraft.client.renderer.MultiBufferSource.BufferSource)}
	 * Targets: {@link Model#setupAnim(Object)}
	 */
	public static void applyHeadVisibility(SubmitNodeStorage.ModelSubmit<?> submit) {
		setHeadVisibility(submit, false);
	}

	/**
	 * {@link twilightforest.asm.transformers.render.HideHeadUnderTrophyTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.feature.ModelFeatureRenderer#renderModel(SubmitNodeStorage.ModelSubmit, net.minecraft.client.renderer.rendertype.RenderType, com.mojang.blaze3d.vertex.VertexConsumer, net.minecraft.client.renderer.OutlineBufferSource, net.minecraft.client.renderer.MultiBufferSource.BufferSource)}
	 * Targets: {@link com.mojang.blaze3d.vertex.PoseStack#popPose()}
	 */
	public static void restoreHeadVisibility(SubmitNodeStorage.ModelSubmit<?> submit) {
		setHeadVisibility(submit, true);
	}

	private static void setHeadVisibility(SubmitNodeStorage.ModelSubmit<?> submit, boolean visible) {
		if (!(submit.model() instanceof HeadedModel headed) || !(submit.state() instanceof EntityRenderState state) || !Boolean.TRUE.equals(state.getRenderData(HIDE_HEAD_KEY)))
			return;

		headed.getHead().visible = visible;
	}
}
