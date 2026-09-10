package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.client.state.block.JarRenderState;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO I ideally want to move the jar lids to be data driven
public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T, JarRenderState> {

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

		public Identifier modelLocation() {
			return TwilightForestMod.prefix("block/lid/" + (this.customPath() != null ? this.customPath() : this.identifier().getPath()));
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

	public static final Identifier JAR_MODEL_LOCATION = TwilightForestMod.prefix("block/mason_jar");
	public static final StandaloneModelKey<BlockStateModelPart> JAR_MODEL = new StandaloneModelKey<>(JAR_MODEL_LOCATION::toDebugFileName);

	public static final Lazy<Map<Item, StandaloneModelKey<BlockStateModelPart>>> LIDS = Lazy.of(() -> {
		Map<Item, StandaloneModelKey<BlockStateModelPart>> lids = new HashMap<>();
		for (LidResource lid : LID_LOCATION_LIST.get()) {
			Identifier location = lid.modelLocation();
			lids.put(lid.lid(), new StandaloneModelKey<>(location::toDebugFileName));
		}
		return Map.copyOf(lids);
	});

	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public JarRenderState createRenderState() {
		return new JarRenderState();
	}

	@Override
	public void extractRenderState(T blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.lastWobbleStyle = blockEntity.lastWobbleStyle;
		state.wobbleTicks = blockEntity.getLevel() != null ? (float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks : 0.0F;
		state.lid = LIDS.get().get(blockEntity.lid);
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.0D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5D, 0.0D, -0.5D);
		WobbleStyle wobbleStyle = state.lastWobbleStyle;

		if (wobbleStyle != null) {
			float f = state.wobbleTicks / (float) wobbleStyle.duration;
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

		if (state.lid != null)
			submitModel(state.lid, poseStack, collector, state.lightCoords);
		submitModel(JAR_MODEL, poseStack, collector, state.lightCoords);
		this.submitContents(state, poseStack, collector);

		poseStack.popPose();
	}

	public static void submitModel(StandaloneModelKey<BlockStateModelPart> model, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
		BlockStateModelPart part = Minecraft.getInstance().getModelManager().getStandaloneModel(model);
		if (part == null)
			return;

		boolean translucent = (part.materialFlags() & BakedQuad.FLAG_TRANSLUCENT) != 0;
		collector.submitMultiLayerBlockModel(poseStack, List.of(part), translucent, BlockModelRenderState.EMPTY_TINTS, lightCoords, OverlayTexture.NO_OVERLAY, 0);
	}

	public void submitContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {

	}

	@Configurable
	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {

		@Autowired(dist = Dist.CLIENT)
		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

		private final ItemModelResolver resolver;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.resolver = context.itemModelResolver();
		}

		@Override
		public void extractRenderState(MasonJarBlockEntity blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
			super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
			state.itemRotation = RotationSegment.convertToDegrees(blockEntity.getItemRotation());
			this.resolver.updateForTopItem(state.item, blockEntity.getItemHandler().getItem(), this.itemDisplayContextEnumExtension.JARRED, blockEntity.getLevel(), null, 0);
		}

		@Override
		public void submitContents(JarRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
			if (state.item.isEmpty())
				return;

			poseStack.pushPose();
			poseStack.translate(0.5D, 0.4375D, 0.5D);
			poseStack.mulPose(Axis.YN.rotationDegrees(state.itemRotation));
			poseStack.scale(0.5F, 0.5F, 0.5F);
			state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}
}
