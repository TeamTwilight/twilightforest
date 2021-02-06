package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFBighorn extends SheepRenderer {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("bighorn.png");

	@SuppressWarnings("unchecked")
	public RenderTFBighorn(EntityRendererManager manager, SheepModel<? extends SheepEntity> baseModel, EntityModel<?> coatModel, float shadowSize) {
		super(manager);
		this.shadowSize = shadowSize;
		this.entityModel = (SheepModel<SheepEntity>) baseModel;
		this.addLayer(new SheepWoolLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture(SheepEntity ent) {
		return textureLoc;
	}
}
