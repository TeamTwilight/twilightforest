package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.QuestRamModel;
import twilightforest.entity.passive.QuestRam;

public class QuestRamRenderer<T extends QuestRam, M extends QuestRamModel<T>> extends MobRenderer<T, M> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("questram.png");
	public static final ResourceLocation LINE_TEXTURE = TwilightForestMod.getModelTexture("questram_lines.png");

	public QuestRamRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model, 1.0F);
		this.addLayer(new GlowingLinesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}

	public static class GlowingLinesLayer<T extends QuestRam, M extends QuestRamModel<T>> extends RenderLayer<T, M> {

		public GlowingLinesLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(LINE_TEXTURE));
			stack.scale(1.025F, 1.025F, 1.025F);
			this.getParentModel().renderToBuffer(stack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
		}
	}
}
