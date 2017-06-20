package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFMiniGhast;
import twilightforest.entity.EntityTFTowerGhast;

public class RenderTFGhast<T extends EntityTFTowerGhast> extends RenderLiving<T> {
	
	private static final ResourceLocation textureLocClosed = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerghast.png");
	private static final ResourceLocation textureLocOpen = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerghast_openeyes.png");
	private static final ResourceLocation textureLocAttack = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerghast_fire.png");
	
	public RenderTFGhast(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFTowerGhast par1Entity)
	{
		switch (par1Entity.isAttacking() ? 2 : par1Entity.getAttackStatus())
        {
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
