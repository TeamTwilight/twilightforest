package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFHarbingerCube;

public class RenderTFHarbingerCube extends RenderLiving<EntityTFHarbingerCube> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "apocalypse2.png");

	public RenderTFHarbingerCube(RenderManager manager) {
		super(manager, new ModelTFApocalypseCube(), 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFHarbingerCube par1Entity) {
		return textureLoc;
	}

	@Override
	protected void preRenderCallback(EntityTFHarbingerCube par1EntityLivingBase, float par2) {
		float scale = 1.0F;
		GlStateManager.scale(scale, scale, scale);
	}
}
