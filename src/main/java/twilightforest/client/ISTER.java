package twilightforest.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.TFChestBlockEntity;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightmetalShieldModel;
import twilightforest.client.model.tileentity.GenericTrophyModel;
import twilightforest.client.renderer.tileentity.SkullCandleTileEntityRenderer;
import twilightforest.client.renderer.tileentity.TrophyTileEntityRenderer;
import twilightforest.components.item.CandelabraData;
import twilightforest.components.item.SkullCandles;
import twilightforest.config.TFConfig;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.item.KnightmetalShieldItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ISTER extends BlockEntityWithoutLevelRenderer {
	public static final Supplier<ISTER> INSTANCE = Suppliers.memoize(ISTER::new);
	public static final IClientItemExtensions CLIENT_ITEM_EXTENSION = Util.make(() -> new IClientItemExtensions() {
		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE.get();
		}
	});
	private final KeepsakeCasketBlockEntity casket = new KeepsakeCasketBlockEntity(BlockPos.ZERO, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState());
	private final Map<Block, TFChestBlockEntity> chestEntities = Util.make(new HashMap<>(), map -> {
		makeInstance(map, TFBlocks.TWILIGHT_OAK_CHEST);
		makeInstance(map, TFBlocks.CANOPY_CHEST);
		makeInstance(map, TFBlocks.MANGROVE_CHEST);
		makeInstance(map, TFBlocks.DARK_CHEST);
		makeInstance(map, TFBlocks.TIME_CHEST);
		makeInstance(map, TFBlocks.TRANSFORMATION_CHEST);
		makeInstance(map, TFBlocks.MINING_CHEST);
		makeInstance(map, TFBlocks.SORTING_CHEST);
	});
	private final Map<Block, TFTrappedChestBlockEntity> trappedChestEntities = Util.make(new HashMap<>(), map -> {
		makeTrappedInstance(map, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.CANOPY_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.MANGROVE_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.DARK_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.TIME_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.TRANSFORMATION_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.MINING_TRAPPED_CHEST);
		makeTrappedInstance(map, TFBlocks.SORTING_TRAPPED_CHEST);
	});
	private KnightmetalShieldModel shield = new KnightmetalShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.KNIGHTMETAL_SHIELD));
	private Map<BossVariant, GenericTrophyModel> trophies = TrophyTileEntityRenderer.createTrophyRenderers(Minecraft.getInstance().getEntityModels());
	private Map<SkullBlock.Type, SkullModelBase> skulls = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
	private final CandelabraBlockEntity candelabra = new CandelabraBlockEntity(BlockPos.ZERO, TFBlocks.CANDELABRA.get().defaultBlockState());

	// Use the cached INSTANCE.get instead
	private ISTER() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		this.shield = new KnightmetalShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.KNIGHTMETAL_SHIELD));
		this.trophies = TrophyTileEntityRenderer.createTrophyRenderers(Minecraft.getInstance().getEntityModels());
		this.skulls = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());

		TwilightForestMod.LOGGER.debug("Reloaded ISTER!");
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext camera, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			Minecraft minecraft = Minecraft.getInstance();
			if (block instanceof AbstractTrophyBlock trophyBlock) {
				BossVariant variant = trophyBlock.getVariant();
				GenericTrophyModel trophy = this.trophies.get(variant);

				if (camera == ItemDisplayContext.GUI) {
					ModelResourceLocation back = ModelResourceLocation.standalone(TwilightForestMod.prefix("item/" + ((AbstractTrophyBlock) block).getVariant().getTrophyType().getModelName()));
					BakedModel modelBack = minecraft.getItemRenderer().getItemModelShaper().getModelManager().getModel(back);

					Lighting.setupForFlatItems();
					MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
					ms.pushPose();
					Lighting.setupForFlatItems();
					ms.translate(0.5F, 0.5F, -1.5F);
					minecraft.getItemRenderer().render(TrophyTileEntityRenderer.stack, ItemDisplayContext.GUI, false, ms, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, modelBack.applyTransform(camera, ms, false));
					ms.popPose();
					bufferSource.endBatch();
					Lighting.setupFor3DItems();

					ms.pushPose();
					ms.translate(0.5F, 0.5F, 0.5F);
					if (trophyBlock.getVariant() == BossVariant.HYDRA || trophyBlock.getVariant() == BossVariant.QUEST_RAM)
						ms.scale(0.9F, 0.9F, 0.9F);
					ms.mulPose(Axis.XP.rotationDegrees(30));
					ms.mulPose(Axis.YN.rotationDegrees(TFConfig.rotateTrophyHeadsGui && !minecraft.isPaused() ? TFClientEvents.rotationTicker : -45));
					ms.translate(-0.5F, -0.5F, -0.5F);
					ms.translate(0.0F, 0.25F, 0.0F);
					if (trophyBlock.getVariant() == BossVariant.UR_GHAST) ms.translate(0.0F, 0.5F, 0.0F);
					if (trophyBlock.getVariant() == BossVariant.ALPHA_YETI) ms.translate(0.0F, -0.15F, 0.0F);
					TrophyTileEntityRenderer.render(null, 180.0F, trophy, variant, !minecraft.isPaused() ? TFClientEvents.time + minecraft.getTimer().getRealtimeDeltaTicks() : 0, ms, buffers, light, camera);
					ms.popPose();
				} else {
					TrophyTileEntityRenderer.render(null, 180.0F, trophy, variant, !minecraft.isPaused() ? TFClientEvents.time + minecraft.getTimer().getRealtimeDeltaTicks() : 0, ms, buffers, light, camera);
				}

			} else if (block instanceof KeepsakeCasketBlock) {
				minecraft.getBlockEntityRenderDispatcher().renderItem(this.casket, ms, buffers, light, overlay);
			} else if (block instanceof TFChestBlock) {
				minecraft.getBlockEntityRenderDispatcher().renderItem(this.chestEntities.get(block), ms, buffers, light, overlay);
			} else if (block instanceof TFTrappedChestBlock) {
				minecraft.getBlockEntityRenderDispatcher().renderItem(this.trappedChestEntities.get(block), ms, buffers, light, overlay);
			} else if (block instanceof AbstractSkullCandleBlock candleBlock) {
				ResolvableProfile profile = stack.get(DataComponents.PROFILE);

				if (profile != null && !profile.isResolved()) {
					stack.remove(DataComponents.PROFILE);
					profile.resolve().thenAcceptAsync(p_329787_ -> stack.set(DataComponents.PROFILE, p_329787_), minecraft);

					return;
				}

				SkullBlock.Type type = candleBlock.getType();
				SkullModelBase base = this.skulls.get(type);
				RenderType renderType = SkullCandleTileEntityRenderer.getRenderType(type, profile);
				SkullCandleTileEntityRenderer.renderSkull(null, 180.0F, 0.0F, ms, buffers, light, base, renderType);

				//we put the candle
				ms.translate(0.0F, 0.5F, 0.0F);

				SkullCandles skullCandles = stack.getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT);

				minecraft.getBlockRenderer().renderSingleBlock(
					AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(skullCandles.color()))
						.defaultBlockState().setValue(CandleBlock.CANDLES, skullCandles.count()), ms, buffers, light, overlay, ModelData.EMPTY, RenderType.cutout());
			} else if (block instanceof CandelabraBlock) {
				//we need to render the candelabra block here since we have to use builtin/entity on the item.
				//This doesnt allow us to set the item parent to the candelabra block, and without it, only the candles render, if any
				minecraft.getBlockRenderer().renderSingleBlock(TFBlocks.CANDELABRA.get().defaultBlockState(), ms, buffers, light, overlay);
				CandelabraData candelabraData = stack.get(TFDataComponents.CANDELABRA_DATA);
				if (candelabraData != null) {
					CandelabraBlockEntity copy = this.candelabra;
					CandelabraData.setCandlesOf(copy, candelabraData);
					minecraft.getBlockEntityRenderDispatcher().renderItem(copy, ms, buffers, light, overlay);
				}
			} else if (block instanceof CritterBlock critter) {
				BlockEntity blockEntity = critter.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
				if (blockEntity != null) {
					minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity).render(null, 0, ms, buffers, light, overlay);
				}
			}
		} else if (item instanceof KnightmetalShieldItem) {
			ms.pushPose();
			ms.scale(1.0F, -1.0F, -1.0F);
			Material material = new Material(Sheets.SHIELD_SHEET, TwilightForestMod.prefix("entity/knightmetal_shield"));
			VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffers, this.shield.renderType(material.atlasLocation()), true, stack.hasFoil()));
			this.shield.renderToBuffer(ms, vertexconsumer, light, overlay);
			ms.popPose();
		}
	}

	public static void makeInstance(Map<Block, TFChestBlockEntity> map, DeferredHolder<Block, ? extends ChestBlock> registryObject) {
		ChestBlock block = registryObject.get();
		map.put(block, new TFChestBlockEntity(BlockPos.ZERO, block.defaultBlockState()));
	}

	public static void makeTrappedInstance(Map<Block, TFTrappedChestBlockEntity> map, DeferredHolder<Block, ? extends ChestBlock> registryObject) {
		ChestBlock block = registryObject.get();
		map.put(block, new TFTrappedChestBlockEntity(BlockPos.ZERO, block.defaultBlockState()));
	}
}
