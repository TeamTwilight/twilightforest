package twilightforest.client.renderer.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.state.entity.BirdRenderState;
import twilightforest.entity.passive.Bird;

public class BirdRenderer<T extends Bird, M extends EntityModel<BirdRenderState>> extends AgeableMobRenderer<T, BirdRenderState, M> {

	private final Identifier texture;

	public BirdRenderer(EntityRendererProvider.Context context, M model, float shadowSize, String textureName) {
		this(context, model, model, shadowSize, textureName);
	}

	public BirdRenderer(EntityRendererProvider.Context context, M model, M babyModel, float shadowSize, String textureName) {
		super(context, model, babyModel, shadowSize);
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
	public Identifier getTextureLocation(BirdRenderState state) {
		return this.texture;
	}
}
