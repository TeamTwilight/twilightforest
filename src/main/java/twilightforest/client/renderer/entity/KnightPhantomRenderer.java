package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
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

	public KnightPhantomRenderer(EntityRendererProvider.Context manager, KnightPhantomModel model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new HumanoidArmorLayer<>(this, new KnightPhantomModel(manager.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new KnightPhantomModel(manager.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
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
}
