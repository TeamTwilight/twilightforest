package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CarminiteGolemModel;
import twilightforest.entity.monster.CarminiteGolem;

public class CarminiteGolemRenderer extends MobRenderer<CarminiteGolem, IronGolemRenderState, CarminiteGolemModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("carminitegolem.png");

	public CarminiteGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CarminiteGolemModel(context.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F);
	}

	@Override
	public IronGolemRenderState createRenderState() {
		return new IronGolemRenderState();
	}

	@Override
	public void extractRenderState(CarminiteGolem golem, IronGolemRenderState state, float partialTick) {
		super.extractRenderState(golem, state, partialTick);
		state.attackTicksRemaining = golem.attackAnim > 0.0F ? golem.attackAnim - partialTick : 0.0F;
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.IronGolemRenderer#setupRotations(IronGolemRenderState, PoseStack, float, float)}
	 */
	@Override
	protected void setupRotations(IronGolemRenderState state, PoseStack stack, float partialTick, float scale) {
		super.setupRotations(state, stack, partialTick, scale);
		if (!((double)state.walkAnimationSpeed < 0.01D)) {
			float f1 = state.walkAnimationPos + 6.0F;
			float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
			stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(IronGolemRenderState state) {
		return TEXTURE;
	}
}
