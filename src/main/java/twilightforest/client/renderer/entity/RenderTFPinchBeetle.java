package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFPinchBeetle;

import javax.annotation.Nullable;

public class RenderTFPinchBeetle extends RenderLiving<EntityTFPinchBeetle> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("pinchbeetle.png");

	public RenderTFPinchBeetle(RenderManager manager, ModelBase par1ModelBase, float shadowSize) {
		super(manager, par1ModelBase, shadowSize);
	}

	@Override
	public void doRender(EntityTFPinchBeetle entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (!Minecraft.getMinecraft().isGamePaused() && entity.isConfuse()) {
			Vec3d pos = new Vec3d(entity.posX, entity.posY + 1.0D, entity.posZ).add(new Vec3d(1.0D, 0, 0).rotateYaw((float) Math.toRadians(entity.getRNG().nextInt(360))));
			Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityTFPinchBeetle entity) {
		return textureLoc;
	}
}
