package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFApocalypseCube;
import twilightforest.entity.EntityTFHarbingerCube;

public class RenderTFHarbingerCube<T extends EntityTFHarbingerCube> extends MobRenderer<T, ModelTFApocalypseCube<T>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("apocalypse2.png");

	public RenderTFHarbingerCube(EntityRendererManager manager) {
		super(manager, new ModelTFApocalypseCube<>(), 1.0F);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFHarbingerCube entity) {
		return textureLoc;
	}

	@Override
	protected void scale(T entity, MatrixStack stack, float partialTicks) {
		float scale = 1.0F;
		stack.scale(scale, scale, scale);
	}
}
