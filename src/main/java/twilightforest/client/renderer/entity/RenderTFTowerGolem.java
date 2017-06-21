package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFTowerGolem;

public class RenderTFTowerGolem extends RenderLiving<EntityTFTowerGolem> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "carminitegolem.png");

	public RenderTFTowerGolem(RenderManager manager, ModelBase par1ModelBase, float shadowSize) {
		super(manager, par1ModelBase, shadowSize);
	}

	@Override
	protected void applyRotations(EntityTFTowerGolem par1EntityLiving, float par2, float par3, float par4) {
		super.applyRotations(par1EntityLiving, par2, par3, par4);

		if ((double) par1EntityLiving.limbSwingAmount >= 0.01D) {
			float var5 = 13.0F;
			float var6 = par1EntityLiving.limbSwing - par1EntityLiving.limbSwingAmount * (1.0F - par4) + 6.0F;
			float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
			GlStateManager.rotate(6.5F * var7, 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFTowerGolem par1Entity) {
		return textureLoc;
	}

}
