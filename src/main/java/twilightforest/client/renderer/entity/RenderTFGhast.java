package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFTowerGhast;

public class RenderTFGhast<T extends EntityTFTowerGhast> extends RenderLiving<T> {

	private static final ResourceLocation textureLocClosed = TwilightForestMod.getModelTexture("towerghast.png");
	private static final ResourceLocation textureLocOpen   = TwilightForestMod.getModelTexture("towerghast_openeyes.png");
	private static final ResourceLocation textureLocAttack = TwilightForestMod.getModelTexture("towerghast_fire.png");

	public RenderTFGhast(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFTowerGhast entity) {
		switch (entity.isAttacking() ? 2 : entity.getAttackStatus()) {
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
