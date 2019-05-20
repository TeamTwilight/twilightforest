package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFHydra;

public class RenderTFHydra extends RenderLiving<EntityTFHydra> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public RenderTFHydra(RenderManager manager, ModelBase modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	protected float getDeathMaxRotation(EntityTFHydra entity) {
		return 0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFHydra entity) {
		return textureLoc;
	}
}
