package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFBoar;
import twilightforest.entity.passive.EntityTFBoar;

//old renderer used to use PigModel, had to change it because of the texture size change
public class RenderTFBoar extends MobRenderer<EntityTFBoar, ModelTFBoar<EntityTFBoar>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wildboar.png");

	@SuppressWarnings("unchecked")
	public RenderTFBoar(EntityRendererManager manager, ModelTFBoar<EntityTFBoar> model) {
		super(manager, new ModelTFBoar<>(), 0.7F);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFBoar entity) {
		return textureLoc;
	}
}
