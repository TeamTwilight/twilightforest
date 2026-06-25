package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.client.renderer.block.SkullCandleRenderer;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFDataComponents;

import java.util.Optional;
import java.util.function.Consumer;

public record SkullCandleSpecialRenderer(PlayerSkinRenderCache playerSkinRenderCache, SkullModelBase model, float animation, @Nullable RenderType type) implements SpecialModelRenderer<Pair<PlayerSkinRenderCache.RenderInfo, SkullCandles>> {

	@Override
	public Pair<PlayerSkinRenderCache.RenderInfo, SkullCandles> extractArgument(ItemStack stack) {
		ResolvableProfile profile = stack.get(DataComponents.PROFILE);
		PlayerSkinRenderCache.RenderInfo info = profile == null ? null : this.playerSkinRenderCache.getOrDefault(profile);
		return Pair.of(info, stack.get(TFDataComponents.SKULL_CANDLES));
	}

	@Override
	public void submit(@Nullable Pair<PlayerSkinRenderCache.RenderInfo, SkullCandles> info, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		RenderType rendertype = this.type();
		if (rendertype == null) {
			rendertype = info.getFirst() != null ? info.getFirst().renderType() : PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
		}
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.scale(-1.0F, -1.0F, 1.0F);
		SkullModelBase.State modelState = new SkullModelBase.State();
		modelState.animationPos = this.animation();
		modelState.yRot = 180.0F;
		collector.submitModel(this.model(), modelState, stack, rendertype, light, OverlayTexture.NO_OVERLAY, outlineColor, null);
		stack.popPose();

		SkullCandles skullCandles = info.getSecond();

		if (skullCandles != null) {
			stack.translate(0.0F, 0.5F, 0.0F);
			BlockModelRenderState state = new BlockModelRenderState();
			SkullCandleRenderer.updateSkullCandle(skullCandles, Minecraft.getInstance().getBlockModelResolver(), state, false);
			SkullCandleRenderer.submitCandles(state, stack, collector, light, overlay, outlineColor);
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		SkullModelBase.State modelState = new SkullModelBase.State();
		modelState.yRot = 180.0F;
		this.model.setupAnim(modelState);
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked(SkullBlock.Type kind, Optional<Identifier> textureOverride, float animation) implements SpecialModelRenderer.Unbaked<Pair<PlayerSkinRenderCache.RenderInfo, SkullCandles>> {
		public static final MapCodec<SkullCandleSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				SkullBlock.Type.CODEC.fieldOf("kind").forGetter(SkullCandleSpecialRenderer.Unbaked::kind),
				Identifier.CODEC.optionalFieldOf("texture").forGetter(SkullCandleSpecialRenderer.Unbaked::textureOverride),
				Codec.FLOAT.optionalFieldOf("animation", 0.0F).forGetter(SkullCandleSpecialRenderer.Unbaked::animation))
			.apply(instance, SkullCandleSpecialRenderer.Unbaked::new));

		public Unbaked(SkullBlock.Type kind) {
			this(kind, Optional.empty(), 0.0F);
		}

		@Override
		public MapCodec<SkullCandleSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Nullable
		@Override
		public SpecialModelRenderer<Pair<PlayerSkinRenderCache.RenderInfo, SkullCandles>> bake(BakingContext context) {
			SkullModelBase model = SkullBlockRenderer.createModel(context.entityModelSet(), this.kind);
			if (model == null) {
				return null;
			} else {
				Identifier textureOverride = this.textureOverride.map(t -> t.withPath(p -> "textures/entity/" + p + ".png")).orElse(null);
				RenderType renderType = this.kind() == SkullBlock.Types.PLAYER ? null : SkullBlockRenderer.getSkullRenderType(this.kind, textureOverride);
				return new SkullCandleSpecialRenderer(context.playerSkinRenderCache(), model, this.animation, renderType);
			}
		}
	}
}
