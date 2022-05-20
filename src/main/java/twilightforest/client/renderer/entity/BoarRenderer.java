package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.BoarModel;
import twilightforest.entity.passive.Boar;

//old renderer used to use PigModel, had to change it because of the texture size change
public class BoarRenderer extends MobRenderer<Boar, BoarModel<Boar>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wildboar.png");

	public BoarRenderer(EntityRendererProvider.Context manager, BoarModel<Boar> model) {
		super(manager, model, 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(Boar entity) {
		return textureLoc;
	}
}
