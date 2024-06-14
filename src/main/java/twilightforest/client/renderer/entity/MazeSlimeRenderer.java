package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.MazeSlime;

public class MazeSlimeRenderer extends MobRenderer<MazeSlime, SlimeModel<MazeSlime>> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("mazeslime.png");

	public MazeSlimeRenderer(EntityRendererProvider.Context context, float shadowSize) {
		super(context, new SlimeModel<>(context.bakeLayer(TFModelLayers.MAZE_SLIME)), shadowSize);
		this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
	}

	@Override
	public void render(MazeSlime entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		this.shadowRadius = 0.25F * (float) entity.getSize();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	@Override
	protected void scale(MazeSlime entity, PoseStack stack, float partialTicks) {
		stack.scale(0.999F, 0.999F, 0.999F);
		stack.translate(0.0D, 0.0010000000474974513D, 0.0D);
		float size = entity.getSize();
		float squishFactor = Mth.lerp(partialTicks, entity.oSquish, entity.squish) / (size * 0.5F + 1.0F);
		float scaledSquish = 1.0F / (squishFactor + 1.0F);
		stack.scale(scaledSquish * size, 1.0F / scaledSquish * size, scaledSquish * size);
	}

	@Override
	public ResourceLocation getTextureLocation(MazeSlime entity) {
		return TEXTURE;
	}
}
