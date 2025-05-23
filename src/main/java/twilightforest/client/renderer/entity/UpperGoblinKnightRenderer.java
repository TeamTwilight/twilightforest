package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.UpperGoblinKnightModel;
import twilightforest.client.state.UpperGoblinKnightRenderState;
import twilightforest.entity.monster.UpperGoblinKnight;

public class UpperGoblinKnightRenderer extends HumanoidMobRenderer<UpperGoblinKnight, UpperGoblinKnightRenderState, UpperGoblinKnightModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("doublegoblin.png");

	public UpperGoblinKnightRenderer(EntityRendererProvider.Context context) {
		super(context, new UpperGoblinKnightModel(context.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F);
	}

	@Override
	protected void setupRotations(UpperGoblinKnightRenderState state, PoseStack stack, float rotationYaw, float scale) {
		super.setupRotations(state, stack, rotationYaw, scale);

		if (state.spearTimer > 0) {
			stack.mulPose(Axis.XP.rotationDegrees(state.getPitchForAttack()));
		}
	}

	@Override
	public UpperGoblinKnightRenderState createRenderState() {
		return new UpperGoblinKnightRenderState();
	}

	@Override
	public void extractRenderState(UpperGoblinKnight entity, UpperGoblinKnightRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.spearTimer = entity.heavySpearTimer;
		state.hasArmor = entity.hasArmor();
		state.hasShield = entity.hasShield();
		state.isShieldDisabled = entity.isShieldDisabled();
	}

	@Override
	public ResourceLocation getTextureLocation(UpperGoblinKnightRenderState state) {
		return TEXTURE;
	}
}
