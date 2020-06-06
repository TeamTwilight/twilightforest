package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFDeer;
import twilightforest.entity.passive.EntityTFDeer;

public class RenderTFDeer<T extends EntityTFDeer, M extends ModelTFDeer<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wilddeer.png");

	public RenderTFDeer(EntityRendererManager manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
