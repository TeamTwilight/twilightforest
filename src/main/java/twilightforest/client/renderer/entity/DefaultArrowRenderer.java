package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class DefaultArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T, ArrowRenderState> {
	public static final ResourceLocation RES_ARROW = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");

	public DefaultArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ArrowRenderState createRenderState() {
		return new ArrowRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(ArrowRenderState state) {
		return RES_ARROW;
	}
}
