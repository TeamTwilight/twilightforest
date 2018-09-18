package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFMazeSlime extends RenderSlime {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "mazeslime.png");

	public RenderTFMazeSlime(RenderManager manager, float shadowSize) {
		super(manager);
		this.shadowSize = shadowSize;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySlime entity) {
		return textureLoc;
	}
}
