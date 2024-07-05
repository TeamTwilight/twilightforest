package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.entity.monster.Wraith;

public class WraithRenderer extends HumanoidMobRenderer<Wraith, WraithModel> {

	private static final ResourceLocation textureWraith = TwilightForestMod.getModelTexture("ghost.png");

	public WraithRenderer(EntityRendererProvider.Context manager, WraithModel modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
	}

	@Nullable
	@Override
	protected RenderType getRenderType(Wraith entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}

	@Override
	public ResourceLocation getTextureLocation(Wraith wraith) {
		return textureWraith;
	}
}
