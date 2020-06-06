package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFIceCrystal;
import twilightforest.entity.boss.EntityTFIceCrystal;

public class RenderTFIceCrystal<T extends EntityTFIceCrystal> extends MobRenderer<T, ModelTFIceCrystal<T>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("icecrystal.png");

	public RenderTFIceCrystal(EntityRendererManager manager) {
		super(manager, new ModelTFIceCrystal<>(), 0.25F);
	}

	@Override
	protected void scale(T entity, MatrixStack stack, float partialTicks) {
		float bounce = entity.ticksExisted + partialTicks;
		stack.translate(0F, MathHelper.sin((bounce) * 0.2F) * 0.15F, 0F);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
