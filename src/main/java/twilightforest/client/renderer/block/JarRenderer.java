package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredBlock;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JarRenderer implements BlockEntityRenderer<JarBlockEntity, JarRenderer.JarRenderState> {
	public static final Map<Item, BlockModel> LIDS = new HashMap<>();

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

	public static class JarRenderState extends BlockEntityRenderState {
		public Item lidItem = Items.AIR;
		public float partialTicks;
		public float rotX = 0.0F;
		public float rotY = 0.0F;
		public float rotZ = 0.0F;
		public BlockState blockState;
		public JarBlockEntity blockEntity;
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
	public JarRenderState createRenderState() {
		return new JarRenderState();
	}

	@Override
	public void submit(JarRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		stack.pushPose();
		stack.translate(0.5, 0.0, 0.5);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		stack.translate(-0.5, 0.0, -0.5);

		if (state.rotX != 0.0F) stack.rotateAround(Axis.XP.rotation(state.rotX), 0.5F, 0.0F, 0.5F);
		if (state.rotY != 0.0F) stack.rotateAround(Axis.YP.rotation(state.rotY), 0.5F, 0.0F, 0.5F);
		if (state.rotZ != 0.0F) stack.rotateAround(Axis.ZP.rotation(state.rotZ), 0.5F, 0.0F, 0.5F);

		BlockModel jarModel = net.minecraft.client.Minecraft.getInstance()
			.getModelManager()
			.getBlockModelSet()
			.get(state.blockState);

		BlockModelRenderState modelState = new BlockModelRenderState();

		jarModel.update(modelState, state.blockState, BlockDisplayContext.create(), 42L);
		modelState.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, -1);
		this.renderContents(state, stack, collector);

		stack.popPose();
	}

	@Override
	public void extractRenderState(JarBlockEntity blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		state.lidItem = blockEntity.lid;
		state.blockState = blockEntity.getBlockState();
		state.blockEntity = blockEntity;

		state.rotX = 0.0F;
		state.rotY = 0.0F;
		state.rotZ = 0.0F;

		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
		Level level = blockEntity.getLevel();

		if (wobbleStyle != null && level != null) {
			float f = ((float) (level.getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / (float) wobbleStyle.duration;
			if (f >= 0.0F && f <= 1.0F) {
				if (wobbleStyle == WobbleStyle.POSITIVE) {
					float f1 = 0.015625F;
					float f2 = f * (float) (Math.PI * 2);
					state.rotX = (-1.5F * (Mth.cos(f2) + 0.5F) * Mth.sin(f2 / 2.0F)) * f1;
					state.rotZ = Mth.sin(f2) * f1;
				} else {
					float f5 = Mth.sin(-f * 3.0F * (float) Math.PI) * WOBBLE_AMPLITUDE;
					float f6 = 1.0F - f;
					state.rotY = f5 * f6;
				}
			}
		}
	}

	public void renderContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Configurable
	public static class MasonJarRenderer extends JarRenderer {

		@Autowired(dist = Dist.CLIENT)
		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

		protected final EntityRenderDispatcher entityRender;
		protected final Font font;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.entityRender = context.entityRenderer();
			this.font = context.font();
		}

		@Override
		public void renderContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
			if (!(state.blockEntity instanceof MasonJarBlockEntity masonJarBlockEntity)) return;

			ItemStack stack = masonJarBlockEntity.getItemHandler().getItem();

			if (!stack.isEmpty()) {
				poseStack.pushPose();

				poseStack.translate(0.5D, 0.4375D, 0.5D);
				poseStack.mulPose(Axis.YN.rotationDegrees(masonJarBlockEntity.getItemRotation()));
				poseStack.scale(0.5F, 0.5F, 0.5F);

				ItemStackRenderState itemState = new ItemStackRenderState();

				Identifier itemGroupId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
				ItemModel itemModel = net.minecraft.client.Minecraft.getInstance()
					.getModelManager()
					.getItemModel(itemGroupId);

				ItemModelResolver resolver = Minecraft.getInstance()
					.getItemModelResolver();

				itemModel.update(
					itemState,
					stack,
					resolver,
					itemDisplayContextEnumExtension.JARRED,
					null,
					null,
					42
				);

				itemState.submit(
					poseStack,
					collector,
					state.lightCoords,
					OverlayTexture.NO_OVERLAY,
					-1
				);

				poseStack.popPose();
			}
		}
	}
}
