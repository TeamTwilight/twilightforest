package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.EntityTFGiantMiner;

public class RenderTFGiant extends RenderBiped<EntityTFGiantMiner> {

	private boolean typeCache = false;

	public RenderTFGiant(RenderManager manager) {
		super(manager, new ModelPlayer(0, false), 1.8F);
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFGiantMiner entity) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean type = false;
		ResourceLocation texture = DefaultPlayerSkin.getDefaultSkinLegacy();
		if (mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer client = ((AbstractClientPlayer) mc.getRenderViewEntity());
			texture = client.getLocationSkin();
			type = client.getSkinType().equals("slim");
		}
		if (type != typeCache) {
			typeCache = type;
			mainModel = new ModelPlayer(0, type);
		}
		return texture;
	}

	@Override
	protected void preRenderCallback(EntityTFGiantMiner entity, float partialTicks) {
		float scale = 4.0F;
		GlStateManager.scale(scale, scale, scale);
	}
}
