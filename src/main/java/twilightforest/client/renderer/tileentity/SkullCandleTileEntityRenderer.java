package twilightforest.client.renderer.tileentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.AbstractLightableBlock;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.WallSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;

import javax.annotation.Nullable;
import java.util.Map;

//[VanillaCopy] of SkullBlockRenderer, but add le candel
public class SkullCandleTileEntityRenderer<T extends SkullCandleBlockEntity> implements BlockEntityRenderer<T> {

	private final Map<SkullBlock.Type, SkullModelBase> modelByType;

	public static final Map<SkullBlock.Type, ResourceLocation> SKIN_BY_TYPE = Util.make(Maps.newHashMap(), (p_112552_) -> {
		p_112552_.put(SkullBlock.Types.SKELETON, new ResourceLocation("textures/entity/skeleton/skeleton.png"));
		p_112552_.put(SkullBlock.Types.WITHER_SKELETON, new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"));
		p_112552_.put(SkullBlock.Types.ZOMBIE, new ResourceLocation("textures/entity/zombie/zombie.png"));
		p_112552_.put(SkullBlock.Types.CREEPER, new ResourceLocation("textures/entity/creeper/creeper.png"));
		p_112552_.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultSkin());
	});

	public static Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet p_173662_) {
		ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> var1 = ImmutableMap.builder();
		var1.put(SkullBlock.Types.SKELETON, new SkullModel(p_173662_.bakeLayer(ModelLayers.SKELETON_SKULL)));
		var1.put(SkullBlock.Types.WITHER_SKELETON, new SkullModel(p_173662_.bakeLayer(ModelLayers.WITHER_SKELETON_SKULL)));
		var1.put(SkullBlock.Types.PLAYER, new SkullModel(p_173662_.bakeLayer(ModelLayers.PLAYER_HEAD)));
		var1.put(SkullBlock.Types.ZOMBIE, new SkullModel(p_173662_.bakeLayer(ModelLayers.ZOMBIE_HEAD)));
		var1.put(SkullBlock.Types.CREEPER, new SkullModel(p_173662_.bakeLayer(ModelLayers.CREEPER_HEAD)));
		return var1.build();
	}

	public SkullCandleTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.modelByType = createSkullRenderers(renderer.getModelSet());
	}

	@Override
	public void render(SkullCandleBlockEntity tile, float ticks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		BlockState state = tile.getBlockState();
		boolean flag = state.getBlock() instanceof WallSkullCandleBlock;
		Direction dir = flag ? state.getValue(WallSkullCandleBlock.FACING) : null;
		float offset = 22.5F * (float) (flag ? (2 + dir.get2DDataValue()) * 4 : state.getValue(SkullCandleBlock.ROTATION));
		SkullBlock.Type type = ((AbstractSkullCandleBlock) state.getBlock()).getType();
		SkullModelBase base = this.modelByType.get(type);
		RenderType render = getRenderType(type, tile.getOwnerProfile());
		renderSkull(dir, offset, 0.0F, ms, buffer, light, base, render);

		if (dir != null) {
			ms.translate(-(float) dir.getStepX() * 0.25F, 0.0D, -(float) dir.getStepZ() * 0.25F);
		}
		ms.translate(0.0F, flag ? 0.75F : 0.45F, 0.0F);
		if (tile.candleAmount <= 0) tile.candleAmount = 1;
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
				AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(tile.candleColor).getSerializedName())
						.defaultBlockState()
						.setValue(CandleBlock.CANDLES, tile.candleAmount)
						.setValue(CandleBlock.LIT, state.getValue(AbstractSkullCandleBlock.LIGHTING) == AbstractLightableBlock.Lighting.NORMAL), ms, buffer, light, overlay);

	}

	public static void renderSkull(@Nullable Direction dir, float ticks, float animTime, PoseStack ms, MultiBufferSource buffer, int light, SkullModelBase base, RenderType render) {

		ms.pushPose();
		if (dir == null) {
			ms.translate(0.5D, 0.0D, 0.5D);
		} else {
			ms.translate(0.5F - (float)dir.getStepX() * 0.25F, 0.25D, 0.5F - (float)dir.getStepZ() * 0.25F);
		}

		ms.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer consumer = buffer.getBuffer(render);
		base.setupAnim(animTime, ticks, 0.0F);
		base.renderToBuffer(ms, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.popPose();
	}

	public static RenderType getRenderType(SkullBlock.Type pSkullType, @Nullable GameProfile pGameProfile) {
		ResourceLocation resourcelocation = SKIN_BY_TYPE.get(pSkullType);
		if (pSkullType == SkullBlock.Types.PLAYER && pGameProfile != null) {
			Minecraft minecraft = Minecraft.getInstance();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(pGameProfile);
			return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderType.entityTranslucent(minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(pGameProfile)));
		} else {
			return RenderType.entityCutoutNoCullZOffset(resourcelocation);
		}
	}
}
