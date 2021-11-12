package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HarbingerCubeModel;
import twilightforest.entity.monster.HarbingerCube;

public class HarbingerCubeRenderer<T extends HarbingerCube> extends MobRenderer<T, HarbingerCubeModel<T>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("apocalypse2.png");

	public HarbingerCubeRenderer(EntityRendererProvider.Context manager) {
		super(manager, new HarbingerCubeModel<>(manager.bakeLayer(TFModelLayers.HARBINGER_CUBE)), 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(HarbingerCube entity) {
		return textureLoc;
	}

	@Override
	protected void scale(T entity, PoseStack stack, float partialTicks) {
		float scale = 1.0F;
		stack.scale(scale, scale, scale);
	}
}
