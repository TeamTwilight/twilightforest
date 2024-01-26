package twilightforest.client.renderer.tileentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PiglinHeadModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.WallSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;

import java.util.Map;

//[VanillaCopy] of SkullBlockRenderer, but render a candle or candles on top of the skull/head
public class SkullCandleTileEntityRenderer<T extends SkullCandleBlockEntity> implements BlockEntityRenderer<T> {

	private final Map<SkullBlock.Type, SkullModelBase> modelByType;

	public static final Map<SkullBlock.Type, ResourceLocation> SKIN_BY_TYPE = Util.make(Maps.newHashMap(), map -> {
		map.put(SkullBlock.Types.SKELETON, new ResourceLocation("textures/entity/skeleton/skeleton.png"));
		map.put(SkullBlock.Types.WITHER_SKELETON, new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"));
		map.put(SkullBlock.Types.ZOMBIE, new ResourceLocation("textures/entity/zombie/zombie.png"));
		map.put(SkullBlock.Types.CREEPER, new ResourceLocation("textures/entity/creeper/creeper.png"));
		map.put(SkullBlock.Types.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"));
		map.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultTexture());
	});

	public static Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet set) {
		ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> map = ImmutableMap.builder();
		map.put(SkullBlock.Types.SKELETON, new SkullModel(set.bakeLayer(ModelLayers.SKELETON_SKULL)));
		map.put(SkullBlock.Types.WITHER_SKELETON, new SkullModel(set.bakeLayer(ModelLayers.WITHER_SKELETON_SKULL)));
		map.put(SkullBlock.Types.PLAYER, new SkullModel(set.bakeLayer(ModelLayers.PLAYER_HEAD)));
		map.put(SkullBlock.Types.ZOMBIE, new SkullModel(set.bakeLayer(ModelLayers.ZOMBIE_HEAD)));
		map.put(SkullBlock.Types.CREEPER, new SkullModel(set.bakeLayer(ModelLayers.CREEPER_HEAD)));
		map.put(SkullBlock.Types.PIGLIN, new PiglinHeadModel(set.bakeLayer(ModelLayers.PIGLIN_HEAD)));
		return map.build();
	}

	public SkullCandleTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.modelByType = createSkullRenderers(renderer.getModelSet());
	}

	@Override
	public void render(SkullCandleBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float animationTime = entity.getAnimation(partialTicks);
		BlockState state = entity.getBlockState();
		boolean wallSkull = state.getBlock() instanceof WallSkullCandleBlock;
		Direction direction = wallSkull ? state.getValue(WallSkullCandleBlock.FACING) : null;
		int rotation = wallSkull ? RotationSegment.convertToSegment(direction.getOpposite()) : state.getValue(SkullCandleBlock.ROTATION);
		float rotDegrees = RotationSegment.convertToDegrees(rotation);
		SkullBlock.Type type = ((AbstractSkullCandleBlock)state.getBlock()).getType();
		SkullModelBase base = this.modelByType.get(type);
		RenderType rendertype = getRenderType(type, entity.getOwnerProfile());
		renderSkull(direction, rotDegrees, animationTime, stack, buffer, light, base, rendertype);

		if (direction != null) {
			stack.translate(-direction.getStepX() * 0.25F, 0.75F, -direction.getStepZ() * 0.25F);
		} else {
			stack.translate(0.0F, 0.45F, 0.0F);
		}
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
				AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(entity.getCandleColor()))
						.defaultBlockState()
						.setValue(CandleBlock.CANDLES, Math.max(1, entity.getCandleAmount()))
						.setValue(CandleBlock.LIT, state.getValue(AbstractSkullCandleBlock.LIGHTING) == LightableBlock.Lighting.NORMAL), stack, buffer, light, overlay);
	}

	public static void renderSkull(@Nullable Direction direction, float pYRot, float animationTime, PoseStack stack, MultiBufferSource buffer, int light, SkullModelBase base, RenderType type) {
		stack.pushPose();
		if (direction == null) {
			stack.translate(0.5F, 0.0F, 0.5F);
		} else {
			stack.translate(0.5F - (float)direction.getStepX() * 0.25F, 0.25F, 0.5F - (float)direction.getStepZ() * 0.25F);
		}

		stack.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer consumer = buffer.getBuffer(type);
		base.setupAnim(animationTime, pYRot, 0.0F);
		base.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}

	public static RenderType getRenderType(SkullBlock.Type type, @Nullable GameProfile profile) {
		ResourceLocation resourcelocation = SKIN_BY_TYPE.get(type);
		if (type == SkullBlock.Types.PLAYER && profile != null) {
			SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
			return RenderType.entityTranslucent(skinmanager.getInsecureSkin(profile).texture());
		} else {
			return RenderType.entityCutoutNoCullZOffset(resourcelocation);
		}
	}
}
