package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFNaga;

public class RenderTFNaga extends RenderLiving<EntityTFNaga> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");

	public RenderTFNaga(RenderManager manager, ModelBase modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
		this.addLayer(new NagaEyelidsLayer(this));
	}

	@Override
	public void doRender(EntityTFNaga entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (!Minecraft.getMinecraft().isGamePaused() && entity.isDazed()) {
			Vec3d pos = new Vec3d(entity.posX, entity.posY + 3.15D, entity.posZ).add(new Vec3d(1.5D, 0, 0).rotateYaw((float) Math.toRadians(entity.getRNG().nextInt(360))));
			Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFNaga entity) {
		return textureLoc;
	}
}
