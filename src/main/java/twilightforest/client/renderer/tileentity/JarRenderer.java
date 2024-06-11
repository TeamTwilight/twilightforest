package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.init.TFBlocks;

import java.util.HashMap;
import java.util.Map;

public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T> {
	public static final Map<Item, BakedModel> LIDS = new HashMap<>();
	public static final Map<Item, ResourceLocation> LOG_LOCATION_MAP = Map.ofEntries(
		Map.entry(TFBlocks.MANGROVE_LOG.asItem(), TFBlocks.MANGROVE_LOG.getId()),
		Map.entry(TFBlocks.CANOPY_LOG.asItem(), TFBlocks.CANOPY_LOG.getId()),
		Map.entry(TFBlocks.DARK_LOG.asItem(), TFBlocks.DARK_LOG.getId()),
		Map.entry(TFBlocks.MINING_LOG.asItem(), TFBlocks.MINING_LOG.getId()),
		Map.entry(TFBlocks.SORTING_LOG.asItem(), TFBlocks.SORTING_LOG.getId()),
		Map.entry(TFBlocks.TIME_LOG.asItem(), TFBlocks.TIME_LOG.getId()),
		Map.entry(TFBlocks.TRANSFORMATION_LOG.asItem(), TFBlocks.TRANSFORMATION_LOG.getId()),
		Map.entry(TFBlocks.TWILIGHT_OAK_LOG.asItem(), TFBlocks.TWILIGHT_OAK_LOG.getId()),
		Map.entry(Items.ACACIA_LOG, new ResourceLocation("minecraft", "acacia_log")),
		Map.entry(Items.BIRCH_LOG, new ResourceLocation("minecraft", "birch_log")),
		Map.entry(Items.CHERRY_LOG, new ResourceLocation("minecraft", "cherry_log")),
		Map.entry(Items.DARK_OAK_LOG, new ResourceLocation("minecraft", "dark_oak_log")),
		Map.entry(Items.JUNGLE_LOG, new ResourceLocation("minecraft", "jungle_log")),
		Map.entry(Items.MANGROVE_LOG, new ResourceLocation("minecraft", "mangrove_log")),
		Map.entry(Items.OAK_LOG, new ResourceLocation("minecraft", "oak_log")),
		Map.entry(Items.SPRUCE_LOG, new ResourceLocation("minecraft", "spruce_log")),
		Map.entry(Items.CRIMSON_STEM, new ResourceLocation("minecraft", "crimson_stem")),
		Map.entry(Items.WARPED_STEM, new ResourceLocation("minecraft", "warped_stem")),
		Map.entry(TFBlocks.STRIPPED_MANGROVE_LOG.asItem(), TFBlocks.STRIPPED_MANGROVE_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_CANOPY_LOG.asItem(), TFBlocks.STRIPPED_CANOPY_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_DARK_LOG.asItem(), TFBlocks.STRIPPED_DARK_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_MINING_LOG.asItem(), TFBlocks.STRIPPED_MINING_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_SORTING_LOG.asItem(), TFBlocks.STRIPPED_SORTING_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_TIME_LOG.asItem(), TFBlocks.STRIPPED_TIME_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.getId()),
		Map.entry(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.getId()),
		Map.entry(Items.STRIPPED_ACACIA_LOG, new ResourceLocation("minecraft", "stripped_acacia_log")),
		Map.entry(Items.STRIPPED_BIRCH_LOG, new ResourceLocation("minecraft", "stripped_birch_log")),
		Map.entry(Items.STRIPPED_CHERRY_LOG, new ResourceLocation("minecraft", "stripped_cherry_log")),
		Map.entry(Items.STRIPPED_DARK_OAK_LOG, new ResourceLocation("minecraft", "stripped_dark_oak_log")),
		Map.entry(Items.STRIPPED_JUNGLE_LOG, new ResourceLocation("minecraft", "stripped_jungle_log")),
		Map.entry(Items.STRIPPED_MANGROVE_LOG, new ResourceLocation("minecraft", "stripped_mangrove_log")),
		Map.entry(Items.STRIPPED_OAK_LOG, new ResourceLocation("minecraft", "stripped_oak_log")),
		Map.entry(Items.STRIPPED_SPRUCE_LOG, new ResourceLocation("minecraft", "stripped_spruce_log")),
		Map.entry(Items.STRIPPED_CRIMSON_STEM, new ResourceLocation("minecraft", "stripped_crimson_stem")),
		Map.entry(Items.STRIPPED_WARPED_STEM, new ResourceLocation("minecraft", "stripped_warped_stem"))
	);

	protected final BlockRenderDispatcher blockRenderer;
	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5, 0.0, -0.5);
		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;

		if (wobbleStyle != null && blockEntity.getLevel() != null) {
			float f = ((float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTick) / (float) wobbleStyle.duration;
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

		BlockState state = blockEntity.getBlockState();
		if (LIDS.containsKey(blockEntity.lid)) renderModel(LIDS.get(blockEntity.lid), state, this.blockRenderer, poseStack, buffer, packedLight, packedOverlay);
		renderJarModel(state, this.blockRenderer, poseStack, buffer, packedLight, packedOverlay);
		this.renderContents(blockEntity, partialTick, poseStack, buffer, packedLight, packedOverlay);

		poseStack.popPose();
	}

	public static void renderJarModel(BlockState blockState, BlockRenderDispatcher blockRenderer, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		BakedModel bakedModel = blockRenderer.getBlockModel(blockState);
		renderModel(bakedModel, blockState, blockRenderer, stack, buffer, packedLight, packedOverlay);
	}

	public static void renderModel(BakedModel bakedModel, BlockState blockState, BlockRenderDispatcher blockRenderer, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		int color = blockRenderer.blockColors.getColor(blockState, null, null, 0);
		float r = (float) (color >> 16 & 0xFF) / 255.0F;
		float g = (float) (color >> 8 & 0xFF) / 255.0F;
		float b = (float) (color & 0xFF) / 255.0F;
		for (RenderType rt : bakedModel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY))
			blockRenderer.getModelRenderer()
				.renderModel(
					stack.last(),
					buffer.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)),
					blockState,
					bakedModel,
					r,
					g,
					b,
					packedLight,
					packedOverlay,
					ModelData.EMPTY,
					rt
				);
	}

	public void renderContents(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

	}

	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {
		protected final ItemRenderer itemRenderer;
		protected final EntityRenderDispatcher entityRender;
		protected final Font font;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.entityRender = context.getEntityRenderer();
			this.itemRenderer = context.getItemRenderer();
			this.font = context.getFont();
		}

		@Override
		public void renderContents(MasonJarBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
			ItemStack stack = blockEntity.getItemHandler().getItem();

			if (!stack.isEmpty()) {
				poseStack.pushPose();
				poseStack.translate(0.5D, 0.4375D, 0.5D);

				Vec3 camPos = this.entityRender.camera.getEntity() instanceof LivingEntity living ? living.getEyePosition(partialTick).subtract(living.getDeltaMovement()) : this.entityRender.camera.getPosition();
				Vec3 thisPos = blockEntity.getBlockPos().getCenter();
				poseStack.mulPose(Axis.YN.rotationDegrees((float) Math.toDegrees(Math.atan2(thisPos.z - camPos.z, thisPos.x - camPos.x)) + 90.0F));

				poseStack.pushPose();
				poseStack.scale(0.5F, 0.5F, 0.5F);
				this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
				poseStack.popPose();

				if (stack.getCount() != 1 && Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult && hitResult.getBlockPos().equals(blockEntity.getBlockPos())) {
					poseStack.pushPose();

					poseStack.translate(0.0F, 0.0F, -0.25F);
					poseStack.scale(0.025F, 0.025F, 0.025F);
					poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

					String s = String.valueOf(stack.getCount());
					float x = 10.0F - this.font.width(s);
					float y = 2.0F;

					Font.DisplayMode displayMode = this.itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0).isCustomRenderer() ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;

					// Fake the drop-shadow cuz the real one is being weird about it
					this.font.drawInBatch(s, x, y, 0xFFFFFF, false, poseStack.last().pose(), buffer, displayMode, 0, 15728880, this.font.isBidirectional());
					poseStack.translate(0.0F, 0.0F, 1F);
					this.font.drawInBatch(s, x + 1.0F, y + 1.0F, 0x3E3E3E, false, poseStack.last().pose(), buffer, displayMode, 0, 15728880, this.font.isBidirectional());

					poseStack.popPose();
				}

				poseStack.popPose();
			}
		}
	}
}
