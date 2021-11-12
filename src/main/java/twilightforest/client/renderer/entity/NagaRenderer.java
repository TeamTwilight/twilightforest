package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.Naga;

//added charging texture for new renderer
//FIXME legacy: scaling was commented out since textures are bigger now
public class NagaRenderer<M extends NagaModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");
	private static final ResourceLocation textureLocCharging = TwilightForestMod.getModelTexture("nagahead_charge.png");

	public NagaRenderer(EntityRendererProvider.Context manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void render(Naga entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
		if (!Minecraft.getInstance().isPaused() && entity.isDazed()) {
			Vec3 pos = new Vec3(entity.getX(), entity.getY() + 3.15D, entity.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))));
			Minecraft.getInstance().level.addParticle(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

//	@Override
//	protected void preRenderCallback(EntityTFNaga entity, MatrixStack stack, float p_225620_3_) {
//		super.preRenderCallback(entity, stack, p_225620_3_);
//		//make size adjustment
//		stack.translate(0.0F, 1.75F, 0.0F);
//		stack.scale(2.0F, 2.0F, 2.0F);
//	}

	@Override
	public ResourceLocation getTextureLocation(Naga entity) {
		if (entity.isDazed()) {
			return textureLocDazed;
		} else if (entity.isCharging()) {
			return textureLocCharging;
		} else {
			return textureLoc;
		}
	}
}
