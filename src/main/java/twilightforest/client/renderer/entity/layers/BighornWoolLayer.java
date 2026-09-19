package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;

public class BighornWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {

	private static final Identifier SHEEP_WOOL_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool.png");

	private final EntityModel<SheepRenderState> adultModel;
	private final EntityModel<SheepRenderState> babyModel;

	public BighornWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
		super(renderer);
		adultModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL));
		babyModel = new SheepFurModel(modelSet.bakeLayer(TFModelLayers.BIGHORN_SHEEP_BABY_WOOL));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
		if (state.isSheared)
			return;

		EntityModel<SheepRenderState> model = state.isBaby ? babyModel : adultModel;
		if (state.isInvisible) {
			if (state.appearsGlowing()) {
				submitNodeCollector.submitModel(
					model,
					state,
					poseStack,
					RenderTypes.outline(SHEEP_WOOL_LOCATION),
					lightCoords,
					LivingEntityRenderer.getOverlayCoords(state, 0.0F),
					-16777216,
					null,
					state.outlineColor,
					null
				);
			}
		} else {
			coloredCutoutModelCopyLayerRender(model, SHEEP_WOOL_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), state.isBaby ? 1 : 0);
		}
	}
}
