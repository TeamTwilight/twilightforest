package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SnowQueenModel;
import twilightforest.client.state.entity.SnowQueenRenderState;
import twilightforest.entity.boss.SnowQueen;

public class SnowQueenRenderer extends HumanoidMobRenderer<SnowQueen, SnowQueenRenderState, SnowQueenModel> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("snowqueen.png");

	public SnowQueenRenderer(EntityRendererProvider.Context context) {
		super(context, new SnowQueenModel(context.bakeLayer(TFModelLayers.SNOW_QUEEN)), 0.625F);
	}

	@Override
	protected void scale(SnowQueenRenderState state, PoseStack stack) {
		stack.scale(1.2F, 1.2F, 1.2F);
	}

	@Override
	public SnowQueenRenderState createRenderState() {
		return new SnowQueenRenderState();
	}

	@Override
	public void extractRenderState(SnowQueen entity, SnowQueenRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.breathing = entity.isBreathing();
		state.phase = entity.getCurrentPhase();
	}

	@Override
	public Identifier getTextureLocation(SnowQueenRenderState state) {
		return TEXTURE;
	}
}
