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
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.QuestRamModel;
import twilightforest.client.state.QuestingRamRenderState;
import twilightforest.entity.passive.QuestRam;

public class QuestRamRenderer extends MobRenderer<QuestRam, QuestingRamRenderState, QuestRamModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("questram.png");
	public static final ResourceLocation LINE_TEXTURE = TwilightForestMod.getModelTexture("questram_lines.png");

	public QuestRamRenderer(EntityRendererProvider.Context context) {
		super(context, new QuestRamModel(context.bakeLayer(TFModelLayers.QUEST_RAM)), 1.0F);
		this.addLayer(new GlowingLinesLayer(this));
	}

	@Override
	public QuestingRamRenderState createRenderState() {
		return new QuestingRamRenderState();
	}

	@Override
	public void extractRenderState(QuestRam entity, QuestingRamRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.colorFlags = entity.getColorFlags();
	}

	@Override
	public ResourceLocation getTextureLocation(QuestingRamRenderState state) {
		return TEXTURE;
	}

	public static class GlowingLinesLayer extends RenderLayer<QuestingRamRenderState, QuestRamModel> {

		public GlowingLinesLayer(RenderLayerParent<QuestingRamRenderState, QuestRamModel> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource source, int light, QuestingRamRenderState state, float netHeadYaw, float headPitch) {
			VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(LINE_TEXTURE));
			stack.scale(1.025F, 1.025F, 1.025F);
			this.getParentModel().renderToBuffer(stack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
		}
	}
}
