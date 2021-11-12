package twilightforest.client.renderer.entity.legacy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.BoarLegacyModel;
import twilightforest.entity.passive.Boar;

//old renderer used to use PigModel, had to change it because of the texture size change
public class LegacyBoarRenderer extends MobRenderer<Boar, BoarLegacyModel<Boar>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wildboar.png");

	public LegacyBoarRenderer(EntityRendererProvider.Context manager, BoarLegacyModel<Boar> model) {
		super(manager, model, 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(Boar entity) {
		return textureLoc;
	}
}
