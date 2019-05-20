package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFTowerGolem;

public class RenderTFTowerGolem extends RenderLiving<EntityTFTowerGolem> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("carminitegolem.png");

	public RenderTFTowerGolem(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected void applyRotations(EntityTFTowerGolem entity, float ageInTicks, float rotationYaw, float partialTicks) {
		super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);

		if ((double) entity.limbSwingAmount >= 0.01D) {
			float var5 = 13.0F;
			float var6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
			float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
			GlStateManager.rotate(6.5F * var7, 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFTowerGolem entity) {
		return textureLoc;
	}

}
