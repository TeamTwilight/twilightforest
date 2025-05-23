package twilightforest.client.renderer.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.state.BirdRenderState;
import twilightforest.entity.passive.Bird;

public class BirdRenderer<T extends Bird, M extends EntityModel<BirdRenderState>> extends MobRenderer<T, BirdRenderState, M> {

	private final ResourceLocation texture;

	public BirdRenderer(EntityRendererProvider.Context context, M model, float shadowSize, String textureName) {
		super(context, model, shadowSize);
		this.texture = TwilightForestMod.getModelTexture(textureName);
	}

	@Override
	public BirdRenderState createRenderState() {
		return new BirdRenderState();
	}

	@Override
	public void extractRenderState(T entity, BirdRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.flap = Mth.lerp(partialTick, entity.lastFlapLength, entity.flapLength);
		state.flapSpeed = Mth.lerp(partialTick, entity.lastFlapIntensity, entity.flapIntensity);
	}

	@Override
	public ResourceLocation getTextureLocation(BirdRenderState state) {
		return this.texture;
	}
}
