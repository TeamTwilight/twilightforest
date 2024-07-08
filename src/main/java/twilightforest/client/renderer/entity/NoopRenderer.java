package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// TODO: remove, vanilla now has its own NoopRenderer class which is the exact same as this one
public class NoopRenderer<T extends Entity> extends EntityRenderer<T> {
	public NoopRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		throw new UnsupportedOperationException();
	}
}
