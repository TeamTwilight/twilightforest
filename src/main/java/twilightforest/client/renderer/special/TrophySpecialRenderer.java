package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import twilightforest.client.model.entity.TrophyBlockModel;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.config.TFConfig;
import twilightforest.enums.BossVariant;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record TrophySpecialRenderer(Function<BossVariant, TrophyBlockModel> trophy, BossVariant variant, Optional<Integer> fixedRotation, ItemDisplayContext context) implements SpecialModelRenderer<Float> {

	@Override
	public Float extractArgument(ItemStack stack) {
		if (this.context() == ItemDisplayContext.GUI && TFConfig.rotateTrophyHeadsGui && !Minecraft.getInstance().isPaused()) {
			return (float) (Util.getMillis() / 35);
		}
		return 0.0F;
	}

	@Override
	public void submit(@Nullable Float rotationArg, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		float animation = !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 30) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
		if (model != null) {
			float rotation = this.fixedRotation().isPresent() ? this.fixedRotation().get().floatValue() : (rotationArg != null ? rotationArg : 0.0F);
			if (this.context() == ItemDisplayContext.GUI) {
				stack.pushPose();
				stack.translate(0.5F, 0.1F, 0.5F);
				stack.scale(-1.0F, -1.0F, 1.0F);
				stack.mulPose(Axis.YP.rotationDegrees(rotation));
				TrophyRenderer.submitTrophy(false, model, animation, stack, collector, light, overlay, null, this.context());
				stack.popPose();
			} else if (this.context() == ItemDisplayContext.HEAD) {
				stack.translate(0.5F, 0.0F, 0.5F);
				stack.scale(-1.0F, -1.0F, -1.0F);
				TrophyRenderer.submitTrophy(false, model, animation, stack, collector, light, overlay, null, this.context());
			} else {
				stack.translate(0.5F, 0.0F, 0.5F);
				stack.scale(-1.0F, -1.0F, 1.0F);
				TrophyRenderer.submitTrophy(false, model, animation, stack, collector, light, overlay, null, this.context());
			}
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		if (model != null) {
			PoseStack poseStack = new PoseStack();
			if (this.context() == ItemDisplayContext.GUI) {
				poseStack.translate(0.5F, 0.12F, 0.5F);
				poseStack.scale(-1.0F, -1.0F, 1.0F);
			} else if (this.context() == ItemDisplayContext.HEAD) {
				poseStack.translate(0.5F, 0.0F, 0.5F);
				poseStack.scale(-1.0F, -1.0F, -1.0F);
			} else {
				poseStack.translate(0.5F, 0.0F, 0.5F);
				poseStack.scale(-1.0F, -1.0F, 1.0F);
			}
			model.setupRotationsForTrophy(0.0F, this.context() == ItemDisplayContext.GUI ? 0.35F : 0.0F);
			model.getTrophyRoot().getExtentsForGui(poseStack, output);
		}
	}

	public record Unbaked(BossVariant variant, Optional<Integer> fixedRotation, ItemDisplayContext context) implements SpecialModelRenderer.Unbaked<Float> {
		public static final MapCodec<TrophySpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BossVariant.CODEC.fieldOf("kind").forGetter(TrophySpecialRenderer.Unbaked::variant),
				Codec.INT.optionalFieldOf("fixed_rotation").forGetter(TrophySpecialRenderer.Unbaked::fixedRotation),
				ItemDisplayContext.CODEC.fieldOf("display").forGetter(TrophySpecialRenderer.Unbaked::context))
			.apply(instance, TrophySpecialRenderer.Unbaked::new));

		public Unbaked(BossVariant variant, ItemDisplayContext context) {
			this(variant, Optional.empty(), context);
		}

		@Override
		public MapCodec<TrophySpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Float> bake(BakingContext context) {
			Function<BossVariant, TrophyBlockModel> model = Util.memoize(variant -> TrophyRenderer.createTrophyModel(context.entityModelSet(), variant));
			return new TrophySpecialRenderer(model, this.variant(), this.fixedRotation(), this.context());
		}
	}
}
