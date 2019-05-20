package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFLich;

import javax.annotation.Nonnull;

public class RenderTFLich extends RenderBiped<EntityTFLich> {

	private static final ResourceLocation LICH_TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public RenderTFLich(RenderManager manager, ModelBiped modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
		addLayer(new LayerShields());
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityTFLich entity) {
		return LICH_TEXTURE;
	}

}
