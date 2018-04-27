package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFGhast;
import twilightforest.entity.EntityTFTowerGhast;


public class RenderTFUrGhast extends RenderTFTowerGhast {
	private final ResourceLocation textureLocClosed = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerboss.png");
	private final ResourceLocation textureLocOpen = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerboss_openeyes.png");
	private final ResourceLocation textureLocAttack = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerboss_fire.png");

	public RenderTFUrGhast(RenderManager manager, ModelTFGhast modelTFGhast, float shadowSize, float scale) {
		super(manager, modelTFGhast, shadowSize, scale);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFTowerGhast par1Entity) {
		switch (par1Entity.isAttacking() ? 2 : par1Entity.getAttackStatus()) {
			default:
			case 0:
				return textureLocClosed;

			case 1:
				return textureLocOpen;

			case 2:
				return textureLocAttack;
		}
	}
}
