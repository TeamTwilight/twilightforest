package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFNaga;


public class RenderTFNaga extends RenderLiving<EntityTFNaga> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "nagahead.png");

	public RenderTFNaga(RenderManager manager, ModelBase modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void doRender(EntityTFNaga naga, double d, double d1, double d2, float f, float f1) {
		super.doRender(naga, d, d1, d2, f, f1);

		if (naga.getParts() != null) {
			for (int i = 0; i < naga.getParts().length; i++) {
				if (!naga.getParts()[i].isDead) {
					renderManager.renderEntityStatic(naga.getParts()[i], f1, false);
				}
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFNaga par1Entity) {
		return textureLoc;
	}
}
