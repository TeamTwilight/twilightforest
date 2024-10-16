package twilightforest.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TrophyBlock;
import twilightforest.block.TrophyWallBlock;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlocks;

import java.util.Map;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity> {

	private final Map<BossVariant, TrophyBlockModel> trophies;

	public TrophyRenderer(BlockEntityRendererProvider.Context context) {
		this.trophies = createTrophyRenderers(context.getModelSet());
	}

	public static Map<BossVariant, TrophyBlockModel> createTrophyRenderers(EntityModelSet set) {
		ImmutableMap.Builder<BossVariant, TrophyBlockModel> trophyList = ImmutableMap.builder();
		trophyList.put(BossVariant.NAGA, new NagaModel<>(set.bakeLayer(TFModelLayers.NAGA_TROPHY)));
		trophyList.put(BossVariant.LICH, new LichModel<>(set.bakeLayer(TFModelLayers.LICH_TROPHY)));
		trophyList.put(BossVariant.MINOSHROOM, new MinoshroomModel<>(set.bakeLayer(TFModelLayers.MINOSHROOM_TROPHY)));
		trophyList.put(BossVariant.HYDRA, new HydraHeadModel<>(set.bakeLayer(TFModelLayers.HYDRA_TROPHY)));
		trophyList.put(BossVariant.KNIGHT_PHANTOM, new KnightPhantomModel(set.bakeLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY)));
		trophyList.put(BossVariant.UR_GHAST, new UrGhastModel(set.bakeLayer(TFModelLayers.UR_GHAST_TROPHY)));
		trophyList.put(BossVariant.ALPHA_YETI, new AlphaYetiModel(set.bakeLayer(TFModelLayers.ALPHA_YETI_TROPHY)));
		trophyList.put(BossVariant.SNOW_QUEEN, new SnowQueenModel(set.bakeLayer(TFModelLayers.SNOW_QUEEN_TROPHY)));
		trophyList.put(BossVariant.QUEST_RAM, new QuestRamModel<>(set.bakeLayer(TFModelLayers.QUEST_RAM_TROPHY)));
		return trophyList.build();
	}

	public static final ItemStack stack = new ItemStack(TFBlocks.NAGA_TROPHY.get());

	@Override
	public void render(TrophyBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float f = entity.getAnimationProgress(partialTicks);
		BlockState blockstate = entity.getBlockState();
		boolean flag = blockstate.getBlock() instanceof TrophyWallBlock;
		Direction direction = flag ? blockstate.getValue(TrophyWallBlock.FACING) : null;
		float f1 = 22.5F * (flag ? (2 + direction.get2DDataValue()) * 4 : blockstate.getValue(TrophyBlock.ROTATION));
		BossVariant variant = ((AbstractTrophyBlock) blockstate.getBlock()).getVariant();
		TrophyBlockModel model = this.trophies.get(variant);
		render(direction, f1, model, variant, f, stack, buffer, light, ItemDisplayContext.NONE);
	}

	public static void render(@Nullable Direction direction, float y, TrophyBlockModel model, BossVariant variant, float animationProgress, PoseStack stack, MultiBufferSource buffer, int light, ItemDisplayContext context) {
		stack.pushPose();
		if (direction == null || variant == BossVariant.UR_GHAST) {
			stack.translate(0.5D, 0.0D, 0.5D);
		} else {
			stack.translate(0.5F - direction.getStepX() * 0.249F, 0.25D, 0.5F - direction.getStepZ() * 0.249F);
		}
		stack.scale(-1.0F, -1.0F, 1.0F);
		model.setupRotationsForTrophy(animationProgress * 4.5F, y, 0.0F, context == ItemDisplayContext.GUI ? 0.35F : direction != null ? 0.5F : 0.0F);
		model.renderTrophy(stack, buffer, light, OverlayTexture.NO_OVERLAY, -1, context);
		stack.popPose();
	}
}
