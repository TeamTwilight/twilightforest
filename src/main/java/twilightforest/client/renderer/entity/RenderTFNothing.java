package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.boss.EntityTFHydraPart;

public class RenderTFNothing<T extends Entity> extends EntityRenderer<T> {
	public RenderTFNothing(EntityRendererManager m) {
		super(m);
	}

	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}
