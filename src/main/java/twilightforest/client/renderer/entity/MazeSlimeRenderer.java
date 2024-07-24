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

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("mazeslime.png");

	@SuppressWarnings("this-escape")
	public MazeSlimeRenderer(EntityRendererProvider.Context manager, float shadowSize) {
		super(manager, new SlimeModel<>(manager.bakeLayer(TFModelLayers.MAZE_SLIME)), shadowSize);
		this.addLayer(new SlimeOuterLayer<>(this, manager.getModelSet()));
	}

	@Override
	public void render(MazeSlime entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		this.shadowRadius = 0.25F * (float) entityIn.getSize();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void scale(MazeSlime p_115983_, PoseStack p_115984_, float p_115985_) {
		p_115984_.scale(0.999F, 0.999F, 0.999F);
		p_115984_.translate(0.0D, 0.0010000000474974513D, 0.0D);
		float var5 = (float) p_115983_.getSize();
		float var6 = Mth.lerp(p_115985_, p_115983_.oSquish, p_115983_.squish) / (var5 * 0.5F + 1.0F);
		float var7 = 1.0F / (var6 + 1.0F);
		p_115984_.scale(var7 * var5, 1.0F / var7 * var5, var7 * var5);
	}

	@Override
	public ResourceLocation getTextureLocation(MazeSlime entity) {
		return textureLoc;
	}
}
