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

public record TrophySpecialRenderer(TrophyBlockModel trophy, Optional<Integer> fixedRotation) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		float rotation = this.fixedRotation.orElse(TFConfig.rotateTrophyHeadsGui && !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 35) : 0);
		float animation = !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 30) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
		if (context == ItemDisplayContext.GUI) {
			stack.pushPose();
			stack.translate(0.5F, 0.5F, 0.5F);
			stack.mulPose(Axis.YN.rotationDegrees(rotation));
			stack.translate(-0.5F, -0.5F, -0.5F);
			TrophyRenderer.render(null, 180.0F, this.trophy, false, animation, stack, source, light, overlay, context);
			stack.popPose();
		} else {
			TrophyRenderer.render(null, 180.0F, this.trophy, false, animation, stack, source, light, overlay, context);
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

		@Nullable
		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			TrophyBlockModel model = TrophyRenderer.createTrophyModel(set, this.variant());
			return model != null ? new TrophySpecialRenderer(model, this.fixedRotation()) : null;
		}
	}
}
