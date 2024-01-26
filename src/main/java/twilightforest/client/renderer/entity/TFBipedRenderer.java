package twilightforest.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import twilightforest.TwilightForestMod;

public class TFBipedRenderer<T extends Mob, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, M> {

	private final ResourceLocation textureLoc;

	public TFBipedRenderer(EntityRendererProvider.Context manager, M modelBiped, float shadowSize, String textureName) {
		super(manager, modelBiped, shadowSize);

		if (textureName.startsWith("textures")) {
			textureLoc = new ResourceLocation(textureName);
		} else {
			textureLoc = TwilightForestMod.getModelTexture(textureName);
		}
	}

	public TFBipedRenderer(EntityRendererProvider.Context context, M modelBiped, M armorModel1, M armorModel2, float shadowSize, String textureName) {
		this(context, modelBiped, shadowSize, textureName);
		this.addLayer(new HumanoidArmorLayer<>(this, armorModel1, armorModel2, context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
