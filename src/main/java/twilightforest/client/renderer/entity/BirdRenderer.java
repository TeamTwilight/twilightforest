package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.Bird;

public class BirdRenderer<T extends Bird, M extends EntityModel<T>> extends MobRenderer<T, M> {

	private final ResourceLocation textureLoc;

	public BirdRenderer(EntityRendererProvider.Context manager, M model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);
		textureLoc = TwilightForestMod.getModelTexture(textureName);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float getBob(T living, float time) {
		float var3 = living.lastFlapLength + (living.flapLength - living.lastFlapLength) * time;
		float var4 = living.lastFlapIntensity + (living.flapIntensity - living.lastFlapIntensity) * time;
		return (Mth.sin(var3) + 1.0F) * var4;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
