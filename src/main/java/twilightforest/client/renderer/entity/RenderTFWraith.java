package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFWraith;
import twilightforest.entity.EntityTFWraith;

import javax.annotation.Nullable;

public class RenderTFWraith extends BipedRenderer<EntityTFWraith, ModelTFWraith> {

	private static final ResourceLocation textureWraith = TwilightForestMod.getModelTexture("ghost.png");

	public RenderTFWraith(EntityRendererManager manager, ModelTFWraith modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
	}

	@Nullable
	@Override
	protected RenderType func_230496_a_(EntityTFWraith entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.getEntityTranslucent(getEntityTexture(entity));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFWraith wraith) {
		return textureWraith;
	}
}
