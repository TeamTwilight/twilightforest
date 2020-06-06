package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFTowerGolem;
import twilightforest.entity.EntityTFTowerGolem;

public class RenderTFTowerGolem<T extends EntityTFTowerGolem, M extends ModelTFTowerGolem<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("carminitegolem.png");

	public RenderTFTowerGolem(EntityRendererManager manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected void setupTransforms(T entity, MatrixStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupTransforms(entity, stack, ageInTicks, rotationYaw, partialTicks);

		if ((double) entity.limbSwingAmount >= 0.01D) {
			float var5 = 13.0F;
			float var6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
			float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
			RenderSystem.rotatef(6.5F * var7, 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
