package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.RedcapModel;
import twilightforest.entity.monster.Redcap;

public class RedcapRenderer extends HumanoidMobRenderer<Redcap, HumanoidRenderState, RedcapModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("redcap.png");

	public RedcapRenderer(EntityRendererProvider.Context context) {
		super(context, new RedcapModel(context.bakeLayer(TFModelLayers.REDCAP)), 0.4F);
		this.addLayer(new RedcapArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), RedcapModel::new), context.getEquipmentRenderer()));
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}

	public static class RedcapArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends HumanoidArmorLayer<S, M, A> {

		public RedcapArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, EquipmentLayerRenderer equipmentRenderer) {
			super(renderer, modelSet, equipmentRenderer);
		}

		@Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
            // renderArmorPiece and getArmorModel are now private in HumanoidArmorLayer in 26.1.2.
            // Call super.submit() which handles all armor pieces without the boots offset.
            super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
        }
	}
}
