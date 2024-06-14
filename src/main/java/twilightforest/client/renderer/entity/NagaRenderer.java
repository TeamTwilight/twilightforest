package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.Naga;

public class NagaRenderer<T extends Naga, M extends NagaModel<T>> extends MobRenderer<T, M> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("nagahead.png");
	public static final ResourceLocation CHARGING_TEXTURE = TwilightForestMod.getModelTexture("nagahead_charging.png");
	public static final ResourceLocation DAZED_TEXTURE = TwilightForestMod.getModelTexture("nagahead_dazed.png");

	public NagaRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Override
	protected void scale(T entity, PoseStack stack, float partialTicks) {
		super.scale(entity, stack, partialTicks);
		//make size adjustment
		if (!JappaPackReloadListener.INSTANCE.isJappaPackLoaded()) {
			stack.scale(2.01F, 2.01F, 2.01F);
		}
		stack.translate(0.0F, entity.isDazed() ? 1.075F : 0.75F, entity.isDazed() ? 0.175F : 0.0F);
	}

	@Override
	protected float getFlipDegrees(T naga) { //Prevent the body from keeling over
		return naga.isDeadOrDying() ? 0.0F : super.getFlipDegrees(naga);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if (entity.isDazed()) {
			return DAZED_TEXTURE;
		} else if (entity.isCharging() || entity.isDeadOrDying()) {
			return CHARGING_TEXTURE;
		} else {
			return TEXTURE;
		}
	}
}
