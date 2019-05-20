package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFDeer;

public class RenderTFDeer extends RenderLiving<EntityTFDeer> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wilddeer.png");

	public RenderTFDeer(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFDeer entity) {
		return textureLoc;
	}
}
