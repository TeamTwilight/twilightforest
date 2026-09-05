package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import twilightforest.client.model.entity.TrophyBlockModel;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.config.TFConfig;
import twilightforest.enums.BossVariant;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record TrophySpecialRenderer(Function<BossVariant, TrophyBlockModel> trophy, BossVariant variant, Optional<Integer> fixedRotation, ItemDisplayContext context) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		Minecraft minecraft = Minecraft.getInstance();
		float animation = !minecraft.isPaused() ? (int) (Util.getMillis() / 30) + minecraft.getDeltaTracker().getGameTimeDeltaTicks() : 0;
		stack.pushPose();
		if (this.context() == ItemDisplayContext.GUI) {
			stack.translate(0.5F, 0.5F, 0.5F);
			if (this.fixedRotation().isPresent()) {
				stack.mulPose(Axis.YN.rotationDegrees(this.fixedRotation().get()));
			} else if (TFConfig.rotateTrophyHeadsGui && !minecraft.isPaused()) {
				stack.mulPose(Axis.YN.rotationDegrees(45.0F + (int) (Util.getMillis() / 35) % 360));
			}
			stack.translate(0.0F, -0.25F, 0.0F);
		} else {
			stack.translate(0.5F, 0.0F, 0.5F);
		}
		stack.scale(-1.0F, -1.0F, 1.0F);
		TrophyRenderer.submitTrophy(false, model, animation, stack, collector, light, overlay, null, this.context());
		stack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		for (int corner = 0; corner < 8; corner++) {
			output.accept(new Vector3f(
				(corner & 1) == 0 ? -0.25F : 1.25F,
				(corner & 2) == 0 ? -0.25F : 1.25F,
				(corner & 4) == 0 ? -0.25F : 1.25F));
		}
	}

	public record Unbaked(BossVariant variant, Optional<Integer> fixedRotation, ItemDisplayContext context) implements NoDataSpecialModelRenderer.Unbaked {
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
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			Function<BossVariant, TrophyBlockModel> model = Util.memoize(variant -> TrophyRenderer.createTrophyModel(context.entityModelSet(), variant));
			return new TrophySpecialRenderer(model, this.variant(), this.fixedRotation(), this.context());
		}
	}
}
