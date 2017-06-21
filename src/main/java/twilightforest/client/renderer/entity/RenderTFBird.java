package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFBird;


public class RenderTFBird extends RenderLiving<EntityTFBird> {
	private final ResourceLocation textureLoc;

	public RenderTFBird(RenderManager manager, ModelBase model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);
		textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + textureName);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(EntityTFBird living, float time) {
		float var3 = living.lastFlapLength + (living.flapLength - living.lastFlapLength) * time;
		float var4 = living.lastFlapIntensity + (living.flapIntensity - living.lastFlapIntensity) * time;
		return (MathHelper.sin(var3) + 1.0F) * var4;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFBird par1Entity) {
		return textureLoc;
	}
}
