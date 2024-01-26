package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.entity.passive.DwarfRabbit;

public class BunnyRenderer extends MobRenderer<DwarfRabbit, BunnyModel> {

	public BunnyRenderer(EntityRendererProvider.Context manager, BunnyModel model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getTextureLocation(DwarfRabbit entity) {
		return entity.getVariant().texture();
	}
}
