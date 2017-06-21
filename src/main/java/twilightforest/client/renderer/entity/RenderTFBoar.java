package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFBoar extends RenderPig {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "wildboar.png");

	public RenderTFBoar(RenderManager manager, ModelBase model) {
		super(manager);
		this.mainModel = model;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPig par1Entity) {
		return textureLoc;
	}

}
