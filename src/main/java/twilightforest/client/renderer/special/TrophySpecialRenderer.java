package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.model.entity.TrophyBlockModel;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.config.TFConfig;
import twilightforest.enums.BossVariant;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public record TrophySpecialRenderer(Function<BossVariant, TrophyBlockModel> trophy, BossVariant variant, Optional<Integer> fixedRotation) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		float rotation = this.fixedRotation.orElse(TFConfig.rotateTrophyHeadsGui && !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 35) : 0);
		float animation = !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 30) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
		if (model != null) {
			if (context == ItemDisplayContext.GUI) {
				stack.pushPose();
				stack.translate(0.5F, 0.5F, 0.5F);
				stack.mulPose(Axis.YN.rotationDegrees(rotation));
				stack.translate(-0.5F, -0.5F, -0.5F);
				TrophyRenderer.render(null, 180.0F, model, false, animation, stack, source, light, overlay, context);
				stack.popPose();
			} else {
				TrophyRenderer.render(null, 180.0F, model, false, animation, stack, source, light, overlay, context);
			}
		}
	}

	public record Unbaked(BossVariant variant, Optional<Integer> fixedRotation) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<TrophySpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BossVariant.CODEC.fieldOf("kind").forGetter(TrophySpecialRenderer.Unbaked::variant),
				Codec.INT.optionalFieldOf("fixed_rotation").forGetter(TrophySpecialRenderer.Unbaked::fixedRotation))
			.apply(instance, TrophySpecialRenderer.Unbaked::new));

		public Unbaked(BossVariant variant) {
			this(variant, Optional.empty());
		}

		@Override
		public MapCodec<TrophySpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			Function<BossVariant, TrophyBlockModel> model = Util.memoize(variant -> TrophyRenderer.createTrophyModel(set, variant));
			return new TrophySpecialRenderer(model, variant, this.fixedRotation());
		}
	}
}
