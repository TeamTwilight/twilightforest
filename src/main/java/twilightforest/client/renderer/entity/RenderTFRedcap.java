package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFRedcap;
import twilightforest.entity.EntityTFRedcap;

public class RenderTFRedcap<T extends EntityTFRedcap, M extends ModelTFRedcap<T>> extends MobRenderer<T, M> {

	private final ResourceLocation textureLoc;
	
	public RenderTFRedcap(EntityRendererManager renderManagerIn, M modelBipedIn, float shadowSize, String texture) {
		super(renderManagerIn, modelBipedIn, shadowSize);
		this.addLayer(new LayerRedcapHeldItem<>(this));
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.75F), new BipedModel<>(0.75F)));
		
		if (texture.startsWith("textures")) {
			textureLoc = new ResourceLocation(texture);
		} else {
			textureLoc = TwilightForestMod.getModelTexture(texture);
		}
	}
	
	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}

}
