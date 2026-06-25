package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;
import twilightforest.client.renderer.block.SkullChestRenderer;
import twilightforest.init.TFDataComponents;

import java.util.function.Consumer;

public record SkullChestSpecialRenderer(KeepsakeCasketModel model, float openness) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(Direction.NORTH.getRotation());
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));
		collector.submitModel(this.model(), this.openness(), stack, SkullChestRenderer.SKULL_CHEST_TEXTURE, light, overlay, outlineColor, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Direction.NORTH.getRotation());
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked(float openness) implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<SkullChestSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(SkullChestSpecialRenderer.Unbaked::openness))
			.apply(instance, SkullChestSpecialRenderer.Unbaked::new));

		public Unbaked() {
			this(0.0F);
		}

		@Override
		public MapCodec<SkullChestSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			KeepsakeCasketModel model = new KeepsakeCasketModel(context.entityModelSet().bakeLayer(TFModelLayers.SKULL_CHEST));
			return new SkullChestSpecialRenderer(model, this.openness());
		}
	}
}
