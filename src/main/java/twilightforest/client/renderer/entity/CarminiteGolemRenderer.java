package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.CarminiteGolemModel;
import twilightforest.entity.monster.CarminiteGolem;

public class CarminiteGolemRenderer<T extends CarminiteGolem, M extends CarminiteGolemModel<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("carminitegolem.png");

	public CarminiteGolemRenderer(EntityRendererProvider.Context manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.IronGolemRenderer}
	 */
	@Override
	protected void setupRotations(T entity, PoseStack ms, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(entity, ms, ageInTicks, rotationYaw, partialTicks);

		if (!(entity.animationSpeed < 0.01D)) {
			float f = 13.0F;
			float f1 = entity.animationPosition - entity.animationSpeed * (1.0F - partialTicks) + 6.0F;
			float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
			ms.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
