package twilightforest.client.renderer.block.jar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.client.state.block.JarRenderState;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;

import java.util.List;

@Configurable
public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T, JarRenderState> {
	public static final Identifier MODEL = TwilightForestMod.prefix("block/jar_lid");
	public static final StandaloneModelKey<BlockStateModelPart> MODEL_KEY = new StandaloneModelKey<>(MODEL::toDebugFileName);
	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	@Autowired(dist = Dist.CLIENT)
	private JarLidResolver jarLidResolver;

	private final BlockModelResolver blockModelResolver;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.blockModelResolver = context.blockModelResolver();
	}

	@Override
	public JarRenderState createRenderState() {
		return new JarRenderState();
	}

	@Override
	public void extractRenderState(T blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		BlockState blockState = blockEntity.getBlockState();
		state.blockStateModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
		state.blockModelRenderState = new BlockModelRenderState();
		this.blockModelResolver.update(state.blockModelRenderState, blockState, BlockDisplayContext.create());
		state.lidPart = null;
		Level level = blockEntity.getLevel();
		if (level != null) {
			state.lidPart = jarLidResolver.resolve(blockEntity, level);
			state.gameTime = (float) (level.getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks;
		} else {
			state.gameTime = 0.0F;
		}
		state.lastWobbleStyle = blockEntity.lastWobbleStyle;
	}

	@Override
	public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector buffer, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.0D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5D, 0.0D, -0.5D);

		WobbleStyle wobbleStyle = state.lastWobbleStyle;
		if (wobbleStyle != null) {
			float f = state.gameTime / (float) wobbleStyle.duration;
			if (f >= 0.0F && f <= 1.0F) {
				if (wobbleStyle == WobbleStyle.POSITIVE) {
					float f1 = 0.015625F;
					float f2 = f * (float) (Math.PI * 2.0D);
					float f3 = -1.5F * (Mth.cos(f2) + 0.5F) * Mth.sin(f2 / 2.0F);
					poseStack.rotateAround(Axis.XP.rotation(f3 * f1), 0.5F, 0.0F, 0.5F);
					float f4 = Mth.sin(f2);
					poseStack.rotateAround(Axis.ZP.rotation(f4 * f1), 0.5F, 0.0F, 0.5F);
				} else {
					float f5 = Mth.sin(-f * 3.0F * (float) Math.PI) * WOBBLE_AMPLITUDE;
					float f6 = 1.0F - f;
					poseStack.rotateAround(Axis.YP.rotation(f5 * f6), 0.5F, 0.0F, 0.5F);
				}
			}
		}

		state.blockModelRenderState.submit(
			poseStack,
			buffer,
			state.lightCoords,
			OverlayTexture.NO_OVERLAY,
			0
		);

		if (state.breakProgress != null) {
			buffer.submitBreakingBlockModel(
				poseStack,
				state.blockStateModel,
				42L,
				state.breakProgress.progress()
			);
		}

		if (state.lidPart != null) {
			buffer.submitMultiLayerBlockModel(
				poseStack,
				List.of(state.lidPart),
				false,
				new int[0],
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0
			);
			if (state.breakProgress != null) {
				buffer.submitBreakingBlockModel(
					poseStack,
					singlePartModel(state.lidPart),
					42L,
					state.breakProgress.progress()
				);
			}
		}

		if (state.itemStackRenderState != null) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 0.4375D, 0.5D);
			poseStack.mulPose(Axis.YN.rotationDegrees(RotationSegment.convertToDegrees(state.itemRotation)));
			poseStack.scale(0.5F, 0.5F, 0.5F);
			state.itemStackRenderState.submit(
				poseStack,
				buffer,
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0
			);
			poseStack.popPose();
		}

		poseStack.popPose();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	private static BlockStateModel singlePartModel(
		BlockStateModelPart part
	) {
		return new BlockStateModel() {
			@Override
			public void collectParts(
				RandomSource random,
				List<BlockStateModelPart> parts
			) {
				parts.add(part);
			}

			@Override
			public Material.Baked particleMaterial() {
				return part.particleMaterial();
			}

			@Override
			public int materialFlags() {
				return part.materialFlags();
			}
		};
	}

	@Configurable
	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {

		@Autowired(dist = Dist.CLIENT)
		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

		private final ItemModelResolver itemModelResolver;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.itemModelResolver = context.itemModelResolver();
		}

		@Override
		public void extractRenderState(MasonJarBlockEntity blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
			super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

			state.itemStackRenderState = null;
			state.itemRotation = 0;

			ItemStack stack = blockEntity.getItemHandler().getItem();

			if (!stack.isEmpty()) {
				state.itemStackRenderState = new ItemStackRenderState();
				this.itemModelResolver.updateForTopItem(
					state.itemStackRenderState,
					stack,
					itemDisplayContextEnumExtension.JARRED,
					null,
					null,
					0
				);
				state.itemRotation = blockEntity.getItemRotation();
			}
		}
	}
}