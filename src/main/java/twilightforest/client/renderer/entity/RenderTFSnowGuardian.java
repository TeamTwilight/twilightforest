package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import twilightforest.client.model.ModelTFSnowGuardian;
import twilightforest.entity.EntityTFSnowGuardian;

public class RenderTFSnowGuardian extends RenderTFBiped<EntityTFSnowGuardian> {

	public RenderTFSnowGuardian(RenderManager manager) {
		super(manager, new ModelTFSnowGuardian(), 1.0F, "textures/entity/zombie/zombie.png");
	}

	@Override
	protected void preRenderCallback(EntityTFSnowGuardian par1EntityLivingBase, float partialTick) {
		float bounce = par1EntityLivingBase.ticksExisted + partialTick;
		GlStateManager.translate(0F, MathHelper.sin((bounce) * 0.2F) * 0.15F, 0F);
	}
}
