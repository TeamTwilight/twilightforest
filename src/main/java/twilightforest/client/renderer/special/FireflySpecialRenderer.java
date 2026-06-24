package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.client.renderer.block.CicadaRenderer;
import twilightforest.client.renderer.block.FireflyRenderer;

import java.util.function.Consumer;

public record FireflySpecialRenderer(FireflyModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		FireflyRenderer.submitFirefly(this.model(), BugModelAnimationHelper.currentYaw, BugModelAnimationHelper.glowIntensity, 0.0F, Direction.NORTH, stack, collector, light, overlay, outlineColor, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			return new FireflySpecialRenderer(new FireflyModel(context.entityModelSet().bakeLayer(TFModelLayers.FIREFLY)));
		}
	}
}
