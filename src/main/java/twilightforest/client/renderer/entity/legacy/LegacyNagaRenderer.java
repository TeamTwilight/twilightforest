package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.model.entity.legacy.NagaLegacyModel;
import twilightforest.entity.boss.Naga;

public class LegacyNagaRenderer<M extends NagaLegacyModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");

	public LegacyNagaRenderer(EntityRendererProvider.Context manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
		this.addLayer(new NagaEyelidsLayer<>(this));
	}

	@Override
	public void render(Naga entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
		if (!Minecraft.getInstance().isPaused() && entity.isDazed()) {
			Vec3 pos = new Vec3(entity.getX(), entity.getY() + 3.15D, entity.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))));
			Minecraft.getInstance().level.addParticle(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	@Override
	protected void scale(Naga entity, PoseStack stack, float p_225620_3_) {
		super.scale(entity, stack, p_225620_3_);
		//make size adjustment
		stack.translate(0.0F, 1.75F, 0.0F);
		stack.scale(2.0F, 2.0F, 2.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Naga entity) {
		return textureLoc;
	}

	public static class NagaEyelidsLayer<T extends Naga, M extends NagaLegacyModel<T>> extends RenderLayer<T, M> {
		private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");

		public NagaEyelidsLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int i, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if(entitylivingbaseIn.isDazed()) {
				VertexConsumer vertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocDazed));
				this.getParentModel().renderToBuffer(stack, vertex, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
}
