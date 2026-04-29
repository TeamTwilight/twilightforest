package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
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
		public void render(PoseStack stack, MultiBufferSource source, int light, S state, float yRot, float xRot) {
			this.renderArmorPiece(stack, source, state.chestEquipment, EquipmentSlot.CHEST, light, this.getArmorModel(state, EquipmentSlot.CHEST));
			this.renderArmorPiece(stack, source, state.legsEquipment, EquipmentSlot.LEGS, light, this.getArmorModel(state, EquipmentSlot.LEGS));
			//TF: raise boots
			stack.pushPose();
			stack.translate(0.0D, -0.2D, 0.0D);
			this.renderArmorPiece(stack, source, state.feetEquipment, EquipmentSlot.FEET, light, this.getArmorModel(state, EquipmentSlot.FEET));
			stack.popPose();
			this.renderArmorPiece(stack, source, state.headEquipment, EquipmentSlot.HEAD, light, this.getArmorModel(state, EquipmentSlot.HEAD));
		}
	}
}
