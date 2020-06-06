package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFBird;

public class RenderTFBird<T extends EntityTFBird, M extends EntityModel<T>> extends MobRenderer<T, M> {

	private final ResourceLocation textureLoc;

	public RenderTFBird(EntityRendererManager manager, M model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);
		textureLoc = TwilightForestMod.getModelTexture(textureName);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(T living, float time) {
		float var3 = living.lastFlapLength + (living.flapLength - living.lastFlapLength) * time;
		float var4 = living.lastFlapIntensity + (living.flapIntensity - living.lastFlapIntensity) * time;
		return (MathHelper.sin(var3) + 1.0F) * var4;
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
