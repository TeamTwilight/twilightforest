package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.EntityTFGiantMiner;

public class RenderTFGiant<T extends EntityTFGiantMiner> extends BipedRenderer<T, PlayerModel<T>> {

	private boolean typeCache = false;

	public RenderTFGiant(EntityRendererManager manager) {
		super(manager, new PlayerModel<>(0, false), 1.8F);
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(0.5F)));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFGiantMiner entity) {
		Minecraft mc = Minecraft.getInstance();
		boolean type = false;
		ResourceLocation texture = DefaultPlayerSkin.getDefaultSkinLegacy();
		if (mc.getRenderViewEntity() instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity client = ((AbstractClientPlayerEntity) mc.getRenderViewEntity());
			texture = client.getLocationSkin();
			type = client.getSkinType().equals("slim");
		}
		if (type != typeCache) {
			typeCache = type;
			entityModel = new PlayerModel<>(0, type);
		}
		return texture;
	}

	@Override
	public void scale(T entitylivingbaseIn, MatrixStack stack, float partialTickTime) {
		float scale = 4.0F;
		stack.scale(scale, scale, scale);
	}
}
