package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.client.renderer.block.MoonwormRenderer;
import twilightforest.client.state.block.MoonwormRenderState;

import java.util.function.Consumer;

public record MoonwormSpecialRenderer(MoonwormModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
		MoonwormRenderState state = createItemState(partialTick);
		MoonwormRenderer.submitMoonworm(this.model(), state, stack, collector, light, overlay, outlineColor, null);
	}

	private static MoonwormRenderState createItemState(float partialTick) {
		MoonwormRenderState state = new MoonwormRenderState();
		state.facing = Direction.NORTH;
		state.yaw = BugModelAnimationHelper.currentRotation;
		state.rotation = 0.0F;
		state.wiggleRotation =
			(BugModelAnimationHelper.desiredRotation
				- BugModelAnimationHelper.currentRotation)
				- partialTick;
		state.delay = BugModelAnimationHelper.yawWriggleDelay;
		return state;
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<MoonwormSpecialRenderer.Unbaked> MAP_CODEC =
			MapCodec.unit(MoonwormSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<MoonwormSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			return new MoonwormSpecialRenderer(
				new MoonwormModel(context.entityModelSet().bakeLayer(TFModelLayers.MOONWORM))
			);
		}
	}
}