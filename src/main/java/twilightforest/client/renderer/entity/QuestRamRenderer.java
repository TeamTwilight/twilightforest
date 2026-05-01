package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.QuestRamModel;
import twilightforest.client.state.QuestingRamRenderState;
import twilightforest.entity.passive.QuestRam;

public class QuestRamRenderer<T extends QuestRam, M extends QuestRamModel<QuestingRamRenderState>> extends MobRenderer<T, QuestingRamRenderState, M> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("questram.png");
	public static final Identifier LINE_TEXTURE = TwilightForestMod.getModelTexture("questram_lines.png");

	public QuestRamRenderer(EntityRendererProvider.Context context, M model) {
		super(context, model, 1.0F);
		this.addLayer(new GlowingLinesLayer<>(this));
	}

	@Override
	public Identifier getTextureLocation(QuestingRamRenderState entity) {
		return TEXTURE;
	}

	@Override
	public AABB getBoundingBoxForCulling(T entity) {
		return super.getBoundingBoxForCulling(entity).inflate(3.0D);
	}

	@Override
	public QuestingRamRenderState createRenderState() {
		return new QuestingRamRenderState();
	}

	@Override
	public void extractRenderState(T entity, QuestingRamRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.colorFlags = entity.getColorFlags();
	}

	public static class GlowingLinesLayer<T extends QuestingRamRenderState, M extends QuestRamModel<T>> extends RenderLayer<T, M> {

		public GlowingLinesLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, T state, float v, float v1) {
			poseStack.scale(1.025F, 1.025F, 1.025F);
			submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityTranslucent(LINE_TEXTURE), 0xF000F0, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		}

	}
}
