package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.math.MathHelper;
import twilightforest.client.model.entity.ModelTFSnowGuardian;
import twilightforest.entity.EntityTFSnowGuardian;

public class RenderTFSnowGuardian extends RenderTFBiped<EntityTFSnowGuardian> {

	public RenderTFSnowGuardian(RenderManager manager) {
		super(manager, new ModelTFSnowGuardian(), 0.25F, "textures/entity/zombie/zombie.png");
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected void preRenderCallback(EntityTFSnowGuardian par1EntityLivingBase, float partialTick) {
		float bounce = par1EntityLivingBase.ticksExisted + partialTick;
		GlStateManager.translate(0F, MathHelper.sin((bounce) * 0.2F) * 0.15F, 0F);
	}
}
