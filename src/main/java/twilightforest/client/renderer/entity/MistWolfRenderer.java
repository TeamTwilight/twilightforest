package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HostileWolfModel;
import twilightforest.entity.monster.HostileWolf;

public class MistWolfRenderer extends MobRenderer<HostileWolf, HostileWolfModel<HostileWolf>> {

	private static HostileWolf parent; // This is a huge hack

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("mistwolf.png");

	public MistWolfRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HostileWolfModel<>(manager.bakeLayer(TFModelLayers.HOSTILE_WOLF)) {
			@Override
			public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay, int color) {
				float brightness = parent.level().getMaxLocalRawBrightness(parent.blockPosition());
				float misty = Math.min(1F, brightness * 3F + 0.25F);
				float smoky = brightness * 2F + 0.6F;
				super.renderToBuffer(poseStack, buffer, light, overlay, FastColor.ARGB32.colorFromFloat(smoky, misty, misty, misty));
			}
		}, 0.5F);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(HostileWolf entity, PoseStack stack, float partialTicks) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);
	}

	@Override
	public void render(HostileWolf wolf, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
		parent = wolf;
		super.render(wolf, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		parent = null;
	}

	@Override
	protected float getBob(HostileWolf p_116528_, float p_116529_) {
		return p_116528_.getTailAngle();
	}

	@Nullable
	@Override
	protected RenderType getRenderType(HostileWolf entity, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}

	@Override
	public ResourceLocation getTextureLocation(HostileWolf entity) {
		return textureLoc;
	}
}
