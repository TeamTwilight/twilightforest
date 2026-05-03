package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TrophyBlock;
import twilightforest.block.TrophyWallBlock;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.state.block.TrophyRenderState;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFEntities;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity, TrophyRenderState> {

	protected final Function<BossVariant, TrophyBlockModel> modelByType;

	public TrophyRenderer(BlockEntityRendererProvider.Context context) {
		this.modelByType = Util.memoize(variant -> createTrophyModel(context.entityModelSet(), variant));
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
	public void submit(TrophyRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		TrophyBlockModel model = this.modelByType.apply(state.variant);
		stack.pushPose();
		stack.mulPose(state.transformation);
		submitTrophy(state.wall, model, state.animationProgress, stack, collector, state.lightCoords, ItemDisplayContext.NONE);
		stack.popPose();
	}

	public static void submitTrophy(boolean wall, TrophyBlockModel model, float animationProgress, PoseStack stack, SubmitNodeCollector collector, int light, ItemDisplayContext context) {
		model.setupRotationsForTrophy(animationProgress, context == ItemDisplayContext.GUI ? 0.35F : wall ? 0.5F : 0.0F);
		model.renderTrophy(stack, collector, light, context);
	}

	@Override
	public TrophyRenderState createRenderState() {
		return new TrophyRenderState();
	}

	@Override
	public void extractRenderState(TrophyBlockEntity blockEntity, TrophyRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.wall = blockEntity.getBlockState().getBlock() instanceof TrophyWallBlock;
		state.animationProgress = blockEntity.getAnimationProgress(partialTicks);
		state.variant = blockEntity.getBlockState().getBlock() instanceof AbstractTrophyBlock trophy ? trophy.getVariant() : BossVariant.NAGA;

		if (state.wall) {
			state.transformation = state.variant == BossVariant.UR_GHAST ?
				createUnmountedWallTransformation(blockEntity.getBlockState().getValue(TrophyWallBlock.FACING)) :
				createWallTransformation(blockEntity.getBlockState().getValue(TrophyWallBlock.FACING));
		} else {
			state.transformation = createGroundTransformation(blockEntity.getBlockState().getValue(TrophyBlock.ROTATION));
		}
	}

	private static Transformation createWallTransformation(Direction wallDirection) {
		return new Transformation(
			new Vector3f(0.5F - wallDirection.getStepX() * 0.25F, 0.25F, 0.5F - wallDirection.getStepZ() * 0.25F),
			Axis.YP.rotationDegrees(-wallDirection.getOpposite().toYRot()),
			new Vector3f(-1.0F, -1.0F, 1.0F),
			null
		);
	}

	private static Transformation createUnmountedWallTransformation(Direction wallDirection) {
		return new Transformation(
			new Vector3f(0.5F, 0.25F, 0.5F),
			Axis.YP.rotationDegrees(-wallDirection.getOpposite().toYRot()),
			new Vector3f(-1.0F, -1.0F, 1.0F),
			null
		);
	}

	private static Transformation createGroundTransformation(int segment) {
		return new Transformation(
			new Matrix4f().translation(0.5F, 0.0F, 0.5F).rotate(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(segment))).scale(-1.0F, -1.0F, 1.0F)
		);
	}
}
