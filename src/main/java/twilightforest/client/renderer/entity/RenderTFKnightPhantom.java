package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.ModelTFKnightPhantom2;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class RenderTFKnightPhantom extends RenderBiped<EntityTFKnightPhantom> {
	private static final ResourceLocation PHANTOM_TEXTURE = new ResourceLocation(TwilightForestMod.MODEL_DIR + "phantomskeleton.png");

	public RenderTFKnightPhantom(RenderManager manager, ModelTFKnightPhantom2 model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFKnightPhantom entity) {
		return PHANTOM_TEXTURE;
	}

	@Override
	protected void preRenderCallback(EntityTFKnightPhantom par1EntityLivingBase, float par2) {
		float scale = par1EntityLivingBase.isChargingAtPlayer() ? 1.8F : 1.2F;
		GlStateManager.scale(scale, scale, scale);
	}

}
