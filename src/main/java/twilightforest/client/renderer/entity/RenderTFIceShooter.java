package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import twilightforest.client.model.entity.ModelTFIceShooter;
import twilightforest.entity.EntityTFIceShooter;

public class RenderTFIceShooter extends RenderTFBiped<EntityTFIceShooter> {

	public RenderTFIceShooter(RenderManager manager) {
		super(manager, new ModelTFIceShooter(), 0.4F, "iceshooter.png");
	}

	@Override
	protected void preRenderCallback(EntityTFIceShooter par1EntityLivingBase, float partialTick) {
		float bounce = par1EntityLivingBase.ticksExisted + partialTick;
		GlStateManager.translate(0F, MathHelper.sin((bounce) * 0.2F) * 0.15F, 0F);
	}
}
