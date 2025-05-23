package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.util.ARGB;

public class LichMinionRenderer extends ZombieRenderer {

	public LichMinionRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected int getModelTint(ZombieRenderState state) {
		return ARGB.colorFromFloat(1.0F, 0.5F, 1.0F, 0.5F);
	}
}
