package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.spider.Spider;
import twilightforest.TwilightForestMod;

public class TFSpiderRenderer<T extends Spider> extends SpiderRenderer<T> {

	private final Identifier texture;
	private final float scale;

	public TFSpiderRenderer(EntityRendererProvider.Context context, float shadowSize, String texture, float scale) {
		super(context);
		this.shadowRadius = shadowSize;
		this.texture = TwilightForestMod.getModelTexture(texture);
		this.scale = scale;
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState entity) {
		return this.texture;
	}

	@Override
	protected void scale(LivingEntityRenderState entity, PoseStack stack) {
		stack.scale(this.scale, this.scale, this.scale);
	}
}
