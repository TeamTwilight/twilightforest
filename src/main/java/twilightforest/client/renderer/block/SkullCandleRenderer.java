package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.blockentity.WallAndGroundTransformations;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.client.state.block.SkullCandleRenderState;
import twilightforest.components.item.SkullCandles;

import java.util.function.Function;

//[VanillaCopy] of SkullBlockRenderer, but render a candle or candles on top of the skull/head
public class SkullCandleRenderer implements BlockEntityRenderer<@NotNull SkullCandleBlockEntity, @NotNull SkullCandleRenderState> {
	public static final WallAndGroundTransformations<@NotNull Transformation> CANDLE_TRANSFORMS = new WallAndGroundTransformations<>(
		SkullCandleRenderer::createWallTransformation, SkullCandleRenderer::createGroundTransformation, 16
	);
	private final Function<SkullBlock.Type, SkullModelBase> modelByType;
	private final BlockModelResolver blockResolver;
	private final PlayerSkinRenderCache playerSkinRenderCache;

	public SkullCandleRenderer(BlockEntityRendererProvider.Context context) {
		this.playerSkinRenderCache = context.playerSkinRenderCache();
		this.blockResolver = context.blockModelResolver();
		this.modelByType = Util.memoize(type -> SkullBlockRenderer.createModel(context.entityModelSet(), type));
	}

	@Override
	public void submit(SkullCandleRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		SkullModelBase model = this.modelByType.apply(state.skullType);
		stack.pushPose();
		stack.mulPose(state.transformation);
		SkullBlockRenderer.submitSkull(state.animationProgress, stack, collector, state.lightCoords, model, state.renderType, 0, state.breakProgress);

		stack.mulPose(state.candleTransformation);
		submitCandles(state.candle, stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		stack.popPose();

	}

	public static void submitCandles(BlockModelRenderState state, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, int outline) {
		state.submit(stack, collector, light, overlay, outline);
	}

	@Override
	public SkullCandleRenderState createRenderState() {
		return new SkullCandleRenderState();
	}

	@Override
	public void extractRenderState(SkullCandleBlockEntity blockEntity, SkullCandleRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.animationProgress = blockEntity.getAnimation(partialTicks);
		BlockState blockState = blockEntity.getBlockState();
		if (blockState.getBlock() instanceof WallSkullBlock) {
			Direction facing = blockState.getValue(WallSkullBlock.FACING);
			state.transformation = SkullBlockRenderer.TRANSFORMATIONS.wallTransformation(facing);
			state.candleTransformation = CANDLE_TRANSFORMS.wallTransformation(facing);
		} else {
			state.transformation = SkullBlockRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(SkullBlock.ROTATION));
			state.candleTransformation = CANDLE_TRANSFORMS.freeTransformations(0);
		}

		state.skullType = ((SkullCandleBlock)blockState.getBlock()).getType();
		state.renderType = this.resolveSkullRenderType(state.skullType, blockEntity);

		updateSkullCandle(blockEntity.candleInfo, this.blockResolver, state.candle, blockState.getValue(AbstractSkullCandleBlock.LIGHTING) != LightableBlock.Lighting.NONE);
	}

	public static void updateSkullCandle(SkullCandles info, BlockModelResolver resolver, BlockModelRenderState state, boolean lit) {
		resolver.update(state,
			AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(info.color()))
				.defaultBlockState()
				.setValue(CandleBlock.CANDLES, Math.max(1, info.count()))
				.setValue(CandleBlock.LIT, lit),
			BlockDisplayContext.create());
	}

	private static Transformation createWallTransformation(Direction wallDirection) {
		return new Transformation(new Matrix4f().translation(wallDirection.getStepX() * 0.25F, 0.75F, wallDirection.getStepZ() * 0.25F));
	}

	private static Transformation createGroundTransformation(int segment) {
		return new Transformation(new Matrix4f().translation(0.0F, 0.0F, 0.0F));
	}

	private RenderType resolveSkullRenderType(SkullBlock.Type type, SkullBlockEntity entity) {
		if (type == SkullBlock.Types.PLAYER) {
			ResolvableProfile ownerProfile = entity.getOwnerProfile();
			if (ownerProfile != null) {
				return this.playerSkinRenderCache.getOrDefault(ownerProfile).renderType();
			}
		}

		return SkullBlockRenderer.getSkullRenderType(type, null);
	}
}
