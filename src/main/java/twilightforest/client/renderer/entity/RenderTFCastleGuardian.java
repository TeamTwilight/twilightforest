package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.finalcastle.ModelTFCastleGuardian;
import twilightforest.entity.finalcastle.EntityTFCastleGuardian;

public class RenderTFCastleGuardian extends LivingRenderer<EntityTFCastleGuardian, ModelTFCastleGuardian> {

	private final ResourceLocation textureLoc;

	public RenderTFCastleGuardian(EntityRendererManager manager, ModelTFCastleGuardian model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);

		if (textureName.startsWith("textures"))
			textureLoc = new ResourceLocation(textureName);
		else
			textureLoc = TwilightForestMod.getModelTexture(textureName);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFCastleGuardian entity) {
		return textureLoc;
	}
}
