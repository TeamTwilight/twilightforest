package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
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

public class RedcapRenderer extends HumanoidMobRenderer<Redcap, HumanoidRenderState, RedcapModel<HumanoidRenderState>> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("redcap.png");

	public RedcapRenderer(EntityRendererProvider.Context context) {
		super(context, new RedcapModel(context.bakeLayer(TFModelLayers.REDCAP)), 0.4F);
		this.addLayer(new RedcapArmorLayer<>(this, new RedcapModel(context.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel(context.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), context.getEquipmentRenderer()));
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

		public RedcapArmorLayer(RenderLayerParent<S, M> renderer, A innerModel, A outerModel, EquipmentLayerRenderer equipmentRenderer) {
			super(renderer, innerModel, outerModel, equipmentRenderer);
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
			//TODO Need Access Transformer
			this.renderArmorPiece(poseStack, submitNodeCollector, state.chestEquipment, EquipmentSlot.CHEST, state, this.getArmorModel(state, EquipmentSlot.CHEST));
			this.renderArmorPiece(poseStack, submitNodeCollector, state.legsEquipment, EquipmentSlot.LEGS, state, this.getArmorModel(state, EquipmentSlot.LEGS));
			//TF: raise boots
			poseStack.pushPose();
			poseStack.translate(0.0D, -0.2D, 0.0D);
			this.renderArmorPiece(poseStack, submitNodeCollector, state.feetEquipment, EquipmentSlot.FEET, state, this.getArmorModel(state, EquipmentSlot.FEET));
			poseStack.popPose();
			this.renderArmorPiece(poseStack, submitNodeCollector, state.headEquipment, EquipmentSlot.HEAD, state, this.getArmorModel(state, EquipmentSlot.HEAD));
		}
	}
}
