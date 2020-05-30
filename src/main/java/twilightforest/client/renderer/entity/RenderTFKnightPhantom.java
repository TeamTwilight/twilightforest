package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFKnightPhantom2;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class RenderTFKnightPhantom<T extends EntityTFKnightPhantom, M extends ModelTFKnightPhantom2<T>> extends BipedRenderer<T, M> {

	private static final ResourceLocation PHANTOM_TEXTURE = TwilightForestMod.getModelTexture("phantomskeleton.png");

	public RenderTFKnightPhantom(EntityRendererManager manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new BipedArmorLayer<>(this, new ModelTFKnightPhantom2<>(), new ModelTFKnightPhantom2<>()));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFKnightPhantom entity) {
		return PHANTOM_TEXTURE;
	}

	@Override
	protected void scale(T entity, MatrixStack stack, float partialTicks) {
		float scale = entity.isChargingAtPlayer() ? 1.8F : 1.2F;
		stack.scale(scale, scale, scale);
	}
}
