package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFHydraNeck;
import twilightforest.entity.boss.EntityTFHydraNeck;

public class RenderTFHydraNeck extends MobRenderer<EntityTFHydraNeck, ModelTFHydraNeck> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public RenderTFHydraNeck(EntityRendererManager manager, ModelTFHydraNeck modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFHydraNeck entity) {
		return textureLoc;
	}
}
