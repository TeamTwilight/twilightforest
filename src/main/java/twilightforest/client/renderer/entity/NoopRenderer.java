package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

public class NoopRenderer<T extends Entity> extends EntityRenderer<T> {
	public NoopRenderer(EntityRendererProvider.Context mgr) {
		super(mgr);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		throw new UnsupportedOperationException();
	}
}
