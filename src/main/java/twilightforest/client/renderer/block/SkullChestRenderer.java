package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.SkullChestBlock;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.state.block.SkullChestRenderState;

import java.util.Map;

public class SkullChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T, SkullChestRenderState> {

	private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, SkullChestRenderer::createModelTransformation);
	public static final Identifier SKULL_CHEST_TEXTURE = TwilightForestMod.getModelTexture("casket/skull_chest.png");

	private final KeepsakeCasketModel model;

	public SkullChestRenderer(BlockEntityRendererProvider.Context context) {
		this(context, TFModelLayers.SKULL_CHEST);
	}

	public SkullChestRenderer(BlockEntityRendererProvider.Context context, ModelLayerLocation layer) {
		this.model = new KeepsakeCasketModel(context.bakeLayer(layer));
	}

	@Override
	public void submit(SkullChestRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(state.facing.getRotation());
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));

		float lidRotation = state.open;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;

		collector.submitModel(this.model, lidRotation, stack, this.model.renderType(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		stack.popPose();
	}

	@Override
	public SkullChestRenderState createRenderState() {
		return new SkullChestRenderState();
	}

	@Override
	public void extractRenderState(T blockEntity, SkullChestRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facing = blockEntity.getBlockState().getValue(SkullChestBlock.FACING);
		state.texture = this.getTextureLocation(blockEntity.getBlockState());
		state.open = blockEntity.getOpenNess(partialTicks);
	}

	private static Transformation createModelTransformation(Direction facing) {
		return new Transformation(new Matrix4f().rotationAround(Axis.YP.rotationDegrees(-facing.toYRot()), 0.5F, 0.0F, 0.5F));
	}

	protected Identifier getTextureLocation(BlockState blockstate) {
		return SKULL_CHEST_TEXTURE;
	}
}
