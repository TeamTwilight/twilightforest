package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightPhantomModel;
import twilightforest.client.state.entity.KnightPhantomRenderState;
import twilightforest.entity.boss.KnightPhantom;

public class KnightPhantomRenderer extends HumanoidMobRenderer<KnightPhantom, KnightPhantomRenderState, KnightPhantomModel> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("phantomskeleton.png");

	public KnightPhantomRenderer(EntityRendererProvider.Context context) {
		super(context, new KnightPhantomModel(context.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(
			ModelLayers.PLAYER_ARMOR, context.getModelSet(), KnightPhantomModel::new
		), context.getEquipmentRenderer()));
	}

	@Override
	public void submit(KnightPhantomRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.isDying) super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	protected boolean isShaking(KnightPhantomRenderState state) {
		return super.isShaking(state) || state.deathTime > 0;
	}

	@Override
	public KnightPhantomRenderState createRenderState() {
		return new KnightPhantomRenderState();
	}

	@Override
	public void extractRenderState(KnightPhantom entity, KnightPhantomRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isDying = !entity.hasYetToDisappear();
		state.isCharging = entity.isChargingAtPlayer();
	}

	@Override
	public Identifier getTextureLocation(KnightPhantomRenderState state) {
		return TEXTURE;
	}

	@Override
	protected void scale(KnightPhantomRenderState state, PoseStack stack) {
		float scale = state.isCharging ? 1.8F : 1.2F;
		stack.scale(scale, scale, scale);
	}

	@Override
	protected float getFlipDegrees() { //Prevent the body from keeling over
		return 0.0F;
	}
}
