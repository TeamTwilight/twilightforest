package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFLich;
import twilightforest.entity.boss.EntityTFLich;

public class RenderTFLich extends BipedRenderer<EntityTFLich, ModelTFLich> {

	private static final ResourceLocation LICH_TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public RenderTFLich(EntityRendererManager manager, ModelTFLich modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
		addLayer(new LayerShields<>(this));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFLich entity) {
		return LICH_TEXTURE;
	}
}
