package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;
import twilightforest.init.TFDataComponents;

import java.util.function.Consumer;

public record KeepsakeCasketSpecialRenderer(KeepsakeCasketModel model, float openness) implements SpecialModelRenderer<Integer> {

	@Override
	public Integer extractArgument(ItemStack stack) {
		return stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0);
	}

	@Override
	public void submit(@Nullable Integer argument, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(Direction.NORTH.getRotation());
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));
		collector.submitModel(this.model(), this.openness(), stack, KeepsakeCasketRenderer.getTextureLocation(argument), light, overlay, outlineColor, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Direction.NORTH.getRotation());
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked(float openness) implements SpecialModelRenderer.Unbaked<Integer> {
		public static final MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(KeepsakeCasketSpecialRenderer.Unbaked::openness))
			.apply(instance, KeepsakeCasketSpecialRenderer.Unbaked::new));

		public Unbaked() {
			this(0.0F);
		}

		@Override
		public MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Integer> bake(BakingContext context) {
			KeepsakeCasketModel model = new KeepsakeCasketModel(context.entityModelSet().bakeLayer(TFModelLayers.KEEPSAKE_CASKET));
			return new KeepsakeCasketSpecialRenderer(model, this.openness);
		}
	}
}
