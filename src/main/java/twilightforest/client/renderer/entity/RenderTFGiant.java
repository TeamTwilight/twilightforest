package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.EntityTFGiantMiner;

public class RenderTFGiant extends RenderBiped<EntityTFGiantMiner> {
	public RenderTFGiant(RenderManager manager) {
		super(manager, new ModelPlayer(0, false), 0.625F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFGiantMiner entity) {
		Minecraft mc = Minecraft.getMinecraft();

		if (!(mc.getRenderViewEntity() instanceof AbstractClientPlayer)) {
			return DefaultPlayerSkin.getDefaultSkinLegacy();
		} else {
			// todo might look wrong if player has alex skin, because we hardcode a steve model in constructor
			return ((AbstractClientPlayer) mc.getRenderViewEntity()).getLocationSkin();
		}
	}

	@Override
	protected void preRenderCallback(EntityTFGiantMiner entity, float partialTicks) {
		float scale = 4.0F;
		GlStateManager.scale(scale, scale, scale);
	}
}
