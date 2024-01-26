package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class DefaultArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T> {
	public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");

	public DefaultArrowRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return RES_ARROW;
	}
}
