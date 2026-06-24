package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

public class DefaultArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T, ArrowRenderState> {
	public static final Identifier RES_ARROW = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");

	public DefaultArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ArrowRenderState createRenderState() {
		return new ArrowRenderState();
	}

	@Override
	public Identifier getTextureLocation(ArrowRenderState state) {
		return RES_ARROW;
	}
}
