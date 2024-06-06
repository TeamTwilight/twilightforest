package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.block.entity.MasonJarBlockEntity;

public class MasonJarRenderer implements BlockEntityRenderer<MasonJarBlockEntity> {
	protected final EntityRenderDispatcher entityRender;
	private final ItemRenderer itemRenderer;
	private final BlockRenderDispatcher blockRenderer;
	private static final float WOBBLE_AMPLITUDE = 0.125F;

	public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
		this.entityRender = context.getEntityRenderer();
		this.itemRenderer = context.getItemRenderer();
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(MasonJarBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5, 0.0, -0.5);
		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;

		if (wobbleStyle != null && blockEntity.getLevel() != null) {
			float f = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTick) / (float)wobbleStyle.duration;
			if (f >= 0.0F && f <= 1.0F) {
				if (wobbleStyle == WobbleStyle.POSITIVE) {
					float f1 = 0.015625F;
					float f2 = f * (float) (Math.PI * 2);
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

		renderJarModel(blockEntity.getBlockState(), this.blockRenderer, poseStack, buffer, packedLight, packedOverlay);

		ItemStack stack = blockEntity.getItemHandler().getItem();

		if (!stack.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 0.4375D, 0.5D);
			poseStack.scale(0.5F, 0.5F, 0.5F);

			Vec3 camPos = this.entityRender.camera.getEntity() instanceof LivingEntity living ? living.getEyePosition(partialTick).subtract(living.getDeltaMovement()) : this.entityRender.camera.getPosition();

			Vec3 thisPos = blockEntity.getBlockPos().getCenter();
			poseStack.mulPose(Axis.YN.rotationDegrees((float) Math.toDegrees(Math.atan2(thisPos.z - camPos.z, thisPos.x - camPos.x)) + 90.0F));
			this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
			poseStack.popPose();
		}

		poseStack.popPose();
	}

	public static void renderJarModel(BlockState blockState, BlockRenderDispatcher blockRenderer, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		BakedModel bakedmodel = blockRenderer.getBlockModel(blockState);
		int color = blockRenderer.blockColors.getColor(blockState, null, null, 0);
		float r = (float)(color >> 16 & 0xFF) / 255.0F;
		float g = (float)(color >> 8 & 0xFF) / 255.0F;
		float b = (float)(color & 0xFF) / 255.0F;
		for (RenderType rt : bakedmodel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY))
			blockRenderer.getModelRenderer()
				.renderModel(
					stack.last(),
					buffer.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)),
					blockState,
					bakedmodel,
					r,
					g,
					b,
					packedLight,
					packedOverlay,
					ModelData.EMPTY,
					rt
				);
	}
}
