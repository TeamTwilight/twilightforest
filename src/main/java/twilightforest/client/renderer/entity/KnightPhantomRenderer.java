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
import twilightforest.client.model.entity.KnightPhantomModel;
import twilightforest.entity.boss.KnightPhantom;

public class KnightPhantomRenderer extends HumanoidMobRenderer<KnightPhantom, KnightPhantomModel> {

	private static final ResourceLocation PHANTOM_TEXTURE = TwilightForestMod.getModelTexture("phantomskeleton.png");

	@SuppressWarnings("this-escape")
	public KnightPhantomRenderer(EntityRendererProvider.Context context, KnightPhantomModel model, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new HumanoidArmorLayer<>(this, new KnightPhantomModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new KnightPhantomModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public void render(KnightPhantom phantom, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (phantom.hasYetToDisappear()) super.render(phantom, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	protected boolean isShaking(KnightPhantom pEntity) {
		return super.isShaking(pEntity) || pEntity.isDeadOrDying();
	}

	@Override
	public ResourceLocation getTextureLocation(KnightPhantom entity) {
		return PHANTOM_TEXTURE;
	}

	@Override
	protected void scale(KnightPhantom entity, PoseStack stack, float partialTicks) {
		float scale = entity.isChargingAtPlayer() ? 1.8F : 1.2F;
		stack.scale(scale, scale, scale);
	}

	@Override
	protected float getFlipDegrees(KnightPhantom phantom) { //Prevent the body from keeling over
		return phantom.isDeadOrDying() ? 0.0F : super.getFlipDegrees(phantom);
	}
}
