package twilightforest.client.renderer.entity.legacy;

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
import twilightforest.client.model.entity.legacy.QuestRamLegacyModel;
import twilightforest.entity.passive.QuestRam;

public class LegacyQuestRamRenderer extends MobRenderer<QuestRam, QuestRamLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("questram.png");
	private static final ResourceLocation textureLocLines = TwilightForestMod.getModelTexture("questram_lines.png");

	public LegacyQuestRamRenderer(EntityRendererProvider.Context manager, QuestRamLegacyModel model) {
		super(manager, model, 1.0F);
		addLayer(new LayerGlowingLines(this));
	}

	@Override
	public ResourceLocation getTextureLocation(QuestRam entity) {
		return textureLoc;
	}

	class LayerGlowingLines extends RenderLayer<QuestRam, QuestRamLegacyModel> {

		public LayerGlowingLines(RenderLayerParent<QuestRam, QuestRamLegacyModel> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int i, QuestRam entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(textureLocLines));
			stack.scale(1.025f, 1.025f, 1.025f);
			LegacyQuestRamRenderer.this.getModel().renderToBuffer(stack, builder, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
