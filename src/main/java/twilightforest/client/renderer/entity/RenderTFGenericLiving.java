package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFGenericLiving<T extends EntityLiving> extends RenderLiving<T> {
	private final ResourceLocation textureLoc;

	public RenderTFGenericLiving(RenderManager manager, ModelBase model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);

		if (textureName.startsWith("textures")) {
			textureLoc = new ResourceLocation(textureName);
		} else {
			textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + textureName);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}

}
