package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.finalcastle.EntityTFCastleGuardian;

import javax.annotation.Nonnull;

public class RenderTFCastleGuardian extends RenderLivingBase<EntityTFCastleGuardian> {
	private final ResourceLocation textureLoc;

	public RenderTFCastleGuardian(RenderManager manager, ModelBase model, float shadowSize, String textureName) {
		super(manager, model, shadowSize);

		if (textureName.startsWith("textures"))
			textureLoc = new ResourceLocation(textureName);
		else
			textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + textureName);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityTFCastleGuardian entity) {
		return textureLoc;
	}
}
