package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightPhantomModel;
import twilightforest.client.state.KnightPhantomRenderState;
import twilightforest.entity.boss.KnightPhantom;

public class KnightPhantomRenderer extends HumanoidMobRenderer<KnightPhantom, KnightPhantomRenderState, KnightPhantomModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("phantomskeleton.png");

	public KnightPhantomRenderer(EntityRendererProvider.Context context) {
		super(context, new KnightPhantomModel(context.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new HumanoidArmorLayer<>(this, new KnightPhantomModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new KnightPhantomModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getEquipmentRenderer()));
	}

	@Override
	public void render(KnightPhantomRenderState state, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		if (!state.isDying) super.render(state, stack, buffer, packedLight);
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
	public ResourceLocation getTextureLocation(KnightPhantomRenderState state) {
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
