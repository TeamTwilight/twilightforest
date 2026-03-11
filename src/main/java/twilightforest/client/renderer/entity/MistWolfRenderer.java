package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MistWolfModel;
import twilightforest.entity.monster.MistWolf;

public class MistWolfRenderer extends MobRenderer<MistWolf, MistWolfModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("mistwolf.png");

	public MistWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new MistWolfModel(context.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 1.0F);
	}

	@Override
	protected void scale(MistWolf entity, PoseStack stack, float partialTicks) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);
	}

	@Override
	protected float getBob(MistWolf entity, float partialTicks) {
		return entity.getTailAngle();
	}

	@Override
	public ResourceLocation getTextureLocation(MistWolf entity) {
		return TEXTURE;
	}
}
