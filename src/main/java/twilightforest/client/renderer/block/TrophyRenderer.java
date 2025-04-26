package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TrophyBlock;
import twilightforest.block.TrophyWallBlock;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFEntities;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity> {

	protected final Function<BossVariant, TrophyBlockModel> modelByType;

	public TrophyRenderer(BlockEntityRendererProvider.Context context) {
		this.modelByType = Util.memoize(variant -> createTrophyModel(context.getModelSet(), variant));
	}

	@Nullable
	public static TrophyBlockModel createTrophyModel(EntityModelSet set, BossVariant variant) {
		return createTrophyModel((type, layer) -> {
			//holy fucking shit what
			try {
				return (TrophyBlockModel) ((LivingEntityRenderer<?, ?, ?>)
					Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(type)).getModel().getClass().getDeclaredConstructor(ModelPart.class).newInstance(set.bakeLayer(layer));
			} catch (Exception e) {
				TwilightForestMod.LOGGER.warn("Failed to create trophy renderer for entity {}, using fallback", type.getDescription().getString());
				return createFallback(set, variant);
			}
		}, variant);
	}

	@Nullable
	public static TrophyBlockModel createTrophyModel(BiFunction<EntityType<?>, ModelLayerLocation, TrophyBlockModel> modelFunction, BossVariant variant) {
		return switch (variant) {
			case NAGA -> modelFunction.apply(TFEntities.NAGA.get(), TFModelLayers.NAGA_TROPHY);
			case LICH -> modelFunction.apply(TFEntities.LICH.get(), TFModelLayers.LICH_TROPHY);
			case MINOSHROOM -> modelFunction.apply(TFEntities.MINOSHROOM.get(), TFModelLayers.MINOSHROOM_TROPHY);
			case HYDRA -> new HydraHeadModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.HYDRA_TROPHY)); //special case: doesn't use the base entity model
			case KNIGHT_PHANTOM -> modelFunction.apply(TFEntities.KNIGHT_PHANTOM.get(),TFModelLayers.KNIGHT_PHANTOM_TROPHY);
			case UR_GHAST -> modelFunction.apply(TFEntities.UR_GHAST.get(), TFModelLayers.UR_GHAST_TROPHY);
			case ALPHA_YETI -> modelFunction.apply(TFEntities.ALPHA_YETI.get(), TFModelLayers.ALPHA_YETI_TROPHY);
			case SNOW_QUEEN -> modelFunction.apply(TFEntities.SNOW_QUEEN.get(), TFModelLayers.SNOW_QUEEN_TROPHY);
			case QUEST_RAM -> modelFunction.apply(TFEntities.QUEST_RAM.get(), TFModelLayers.QUEST_RAM_TROPHY);
			case FINAL_BOSS -> null; //lol
		};
	}

	@Nullable
	public static TrophyBlockModel createFallback(EntityModelSet set, BossVariant variant) {
		return switch (variant) {
			case NAGA -> new NagaModel<>(set.bakeLayer(TFModelLayers.NAGA_TROPHY));
			case LICH -> new LichModel(set.bakeLayer(TFModelLayers.LICH_TROPHY));
			case MINOSHROOM -> new MinoshroomModel(set.bakeLayer(TFModelLayers.MINOSHROOM_TROPHY));
			case HYDRA -> new HydraHeadModel(set.bakeLayer(TFModelLayers.HYDRA_TROPHY));
			case KNIGHT_PHANTOM -> new KnightPhantomModel(set.bakeLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY));
			case UR_GHAST -> new UrGhastModel(set.bakeLayer(TFModelLayers.UR_GHAST_TROPHY));
			case ALPHA_YETI -> new AlphaYetiModel(set.bakeLayer(TFModelLayers.ALPHA_YETI_TROPHY));
			case SNOW_QUEEN -> new SnowQueenModel(set.bakeLayer(TFModelLayers.SNOW_QUEEN_TROPHY));
			case QUEST_RAM -> new QuestRamModel(set.bakeLayer(TFModelLayers.QUEST_RAM_TROPHY));
			case FINAL_BOSS -> null; //lol
		};
	}

	@Override
	public void render(TrophyBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float f = entity.getAnimationProgress(partialTicks);
		BlockState blockstate = entity.getBlockState();
		boolean flag = blockstate.getBlock() instanceof TrophyWallBlock;
		Direction direction = flag ? blockstate.getValue(TrophyWallBlock.FACING) : null;
		float f1 = 22.5F * (flag ? (2 + direction.get2DDataValue()) * 4 : blockstate.getValue(TrophyBlock.ROTATION));
		BossVariant variant = ((AbstractTrophyBlock) blockstate.getBlock()).getVariant();
		TrophyBlockModel model = this.modelByType.apply(variant);
		render(direction, f1, model, variant != BossVariant.UR_GHAST, f, stack, buffer, light, overlay, ItemDisplayContext.NONE);
	}

	public static void render(@Nullable Direction direction, float y, TrophyBlockModel model, boolean snapToWalls, float animationProgress, PoseStack stack, MultiBufferSource buffer, int light, int overlay, ItemDisplayContext context) {
		stack.pushPose();
		if (direction == null || !snapToWalls) {
			stack.translate(0.5D, 0.0D, 0.5D);
		} else {
			stack.translate(0.5F - direction.getStepX() * 0.249F, 0.25D, 0.5F - direction.getStepZ() * 0.249F);
		}
		stack.scale(-1.0F, -1.0F, 1.0F);
		model.setupRotationsForTrophy(animationProgress * 4.5F, y, 0.0F, context == ItemDisplayContext.GUI ? 0.35F : direction != null ? 0.5F : 0.0F);
		model.renderTrophy(stack, buffer, light, overlay, -1, context);
		stack.popPose();
	}
}
