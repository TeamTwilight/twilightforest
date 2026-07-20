package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.registries.DeferredBlock;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.client.state.block.JarRenderState;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO I ideally want to move the jar lids to be data driven
public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T, JarRenderState> {
	public static final Map<Item, ItemModel> LIDS = new HashMap<>();

	public record LidResource(Item lid, Identifier identifier, @Nullable String customPath) {
		public LidResource(DeferredBlock<?> lid) {
			this(lid.asItem(), lid.getId(), null);
		}

		public LidResource(Item item, String path) {
			this(item, Identifier.fromNamespaceAndPath("minecraft", path), null);
		}

		public LidResource(Item item, String path, String customPath) {
			this(item, Identifier.fromNamespaceAndPath("minecraft", path), customPath);
		}
	}

	public static final Lazy<List<LidResource>> LID_LOCATION_LIST = Lazy.of(() -> List.of(
		new LidResource(TFBlocks.MANGROVE_LOG),
		new LidResource(TFBlocks.CANOPY_LOG),
		new LidResource(TFBlocks.DARK_LOG),
		new LidResource(TFBlocks.MINING_LOG),
		new LidResource(TFBlocks.SORTING_LOG),
		new LidResource(TFBlocks.TIME_LOG),
		new LidResource(TFBlocks.TRANSFORMATION_LOG),
		new LidResource(TFBlocks.TWILIGHT_OAK_LOG),
		new LidResource(Items.ACACIA_LOG, "acacia_log"),
		new LidResource(Items.BIRCH_LOG, "birch_log"),
		new LidResource(Items.CHERRY_LOG, "cherry_log"),
		new LidResource(Items.DARK_OAK_LOG, "dark_oak_log"),
		new LidResource(Items.JUNGLE_LOG, "jungle_log"),
		new LidResource(Items.MANGROVE_LOG, "mangrove_log", "vanilla_mangrove_log"),
		new LidResource(Items.OAK_LOG, "oak_log"),
		new LidResource(Items.SPRUCE_LOG, "spruce_log"),
		new LidResource(Items.CRIMSON_STEM, "crimson_stem"),
		new LidResource(Items.WARPED_STEM, "warped_stem"),
		new LidResource(TFBlocks.STRIPPED_MANGROVE_LOG),
		new LidResource(TFBlocks.STRIPPED_CANOPY_LOG),
		new LidResource(TFBlocks.STRIPPED_DARK_LOG),
		new LidResource(TFBlocks.STRIPPED_MINING_LOG),
		new LidResource(TFBlocks.STRIPPED_SORTING_LOG),
		new LidResource(TFBlocks.STRIPPED_TIME_LOG),
		new LidResource(TFBlocks.STRIPPED_TRANSFORMATION_LOG),
		new LidResource(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG),
		new LidResource(Items.STRIPPED_ACACIA_LOG, "stripped_acacia_log"),
		new LidResource(Items.STRIPPED_BIRCH_LOG, "stripped_birch_log"),
		new LidResource(Items.STRIPPED_CHERRY_LOG, "stripped_cherry_log"),
		new LidResource(Items.STRIPPED_DARK_OAK_LOG, "stripped_dark_oak_log"),
		new LidResource(Items.STRIPPED_JUNGLE_LOG, "stripped_jungle_log"),
		new LidResource(Items.STRIPPED_MANGROVE_LOG, "stripped_mangrove_log", "vanilla_stripped_mangrove_log"),
		new LidResource(Items.STRIPPED_OAK_LOG, "stripped_oak_log"),
		new LidResource(Items.STRIPPED_SPRUCE_LOG, "stripped_spruce_log"),
		new LidResource(Items.STRIPPED_CRIMSON_STEM, "stripped_crimson_stem"),
		new LidResource(Items.STRIPPED_WARPED_STEM, "stripped_warped_stem"),
		new LidResource(TFBlocks.CINDER_LOG),
		new LidResource(Items.PUMPKIN, "pumpkin"),
		new LidResource(Items.BAMBOO_BLOCK, "bamboo_block"),
		new LidResource(Items.STRIPPED_BAMBOO_BLOCK, "stripped_bamboo_block")
	));

	protected final BlockEntityRenderDispatcher blockRenderer;
	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderer = context.blockEntityRenderDispatcher();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5, 0.0, -0.5);
		WobbleStyle wobbleStyle = state.lastWobbleStyle;

		if (wobbleStyle != null && state.blockState != null) {
			float f = ((float) (state.level.getGameTime() - state.wobbleStartedAtTick) + state.partialTick) / (float) wobbleStyle.duration;
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

		BlockState blockState = state.blockState;
		if (LIDS.containsKey(state.lid)) renderModel(LIDS.get(state.lid), blockState, this.blockRenderer, poseStack, buffer, packedLight, packedOverlay);
		renderJarModel(blockState, this.blockRenderer, poseStack, submitNodeCollector, cameraRenderState);
		this.renderContents(state, poseStack, submitNodeCollector, state.lightCoords, state.overlayCoords, ARGB.color(0, 0, 0), new int[0], List.of());

		poseStack.popPose();
	}

	@Override
	public JarRenderState createRenderState() {
		return new JarRenderState();
	}

	public static void renderJarModel(BlockState blockState, BlockEntityRenderDispatcher blockRenderer, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		blockRenderer.
		renderModel(bakedModel, blockState, blockRenderer, stack, submitNodeCollector, cameraRenderState);
	}

	public static void renderModel(BakedModel bakedModel, BlockState blockState, BlockEntityRenderDispatcher blockRenderer, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		int color = blockRenderer.blockColors.getColor(blockState, null, null, 0);
		float r = (float) (color >> 16 & 0xFF) / 255.0F;
		float g = (float) (color >> 8 & 0xFF) / 255.0F;
		float b = (float) (color & 0xFF) / 255.0F;
		for (RenderType rt : bakedModel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY))
			submitNodeCollector.submitBlockModel(
				stack,
				RenderTypes
			);
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

	public void renderContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads) {

	}

	@Configurable
	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {

		@Autowired(dist = Dist.CLIENT)
		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

		protected final ItemModelResolver itemModelResolver;
		protected final EntityRenderDispatcher entityRender;
		protected final Font font;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.entityRender = context.entityRenderer();
			this.itemModelResolver = context.itemModelResolver();
			this.font = context.font();
		}

		@Override
		public void renderContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads) {
			ItemStack stack = state.itemInside;

			if (!stack.isEmpty()) {
				poseStack.pushPose();
				poseStack.translate(0.5D, 0.4375D, 0.5D);

				poseStack.mulPose(Axis.YN.rotationDegrees(RotationSegment.convertToDegrees(state.itemRotation)));

				poseStack.scale(0.5F, 0.5F, 0.5F);
				collector.submitItem(
					poseStack,
					ItemDisplayContext.GROUND,
					lightCoords,
					overlayCoords,
					outlineColor,
					tintLayers,
					quads,
					stack.hasFoil() ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE
				);

				poseStack.popPose();
			}
		}
	}
}
