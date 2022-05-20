package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.SnowQueenLegacyModel;
import twilightforest.entity.boss.SnowQueen;

public class LegacySnowQueenRenderer extends HumanoidMobRenderer<SnowQueen, SnowQueenLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("snowqueen.png");

	public LegacySnowQueenRenderer(EntityRendererProvider.Context manager, SnowQueenLegacyModel model) {
		super(manager, model, 0.625F);
	}

	@Override
	public ResourceLocation getTextureLocation(SnowQueen entity) {
		return textureLoc;
	}

	@Override
	protected void scale(SnowQueen queen, PoseStack stack, float partialTicks) {
		float scale = 1.2F;
		stack.scale(scale, scale, scale);
	}

	@Override
	public void render(SnowQueen queen, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(queen, yaw, partialTicks, stack, buffer, light);
    }
}
