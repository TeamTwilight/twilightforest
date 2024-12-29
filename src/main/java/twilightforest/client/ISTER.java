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
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.block.*;
import twilightforest.block.entity.*;
import twilightforest.client.event.ClientEvents;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightmetalShieldModel;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.model.entity.TrophyBlockModel;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.client.renderer.block.SkullCandleRenderer;
import twilightforest.client.renderer.block.SkullChestRenderer;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.client.renderer.entity.LichRenderer;
import twilightforest.components.item.CandelabraData;
import twilightforest.components.item.JarLid;
import twilightforest.components.item.SkullCandles;
import twilightforest.config.TFConfig;
import twilightforest.enums.BossVariant;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.item.KnightmetalShieldItem;
import twilightforest.item.MysticCrownItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ISTER extends BlockEntityWithoutLevelRenderer {

	@Autowired(dist = Dist.CLIENT)
	private static TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

	public static final Supplier<ISTER> INSTANCE = Suppliers.memoize(ISTER::new);
	public static final IClientItemExtensions CLIENT_ITEM_EXTENSION = Util.make(() -> new IClientItemExtensions() {
		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE.get();
		}
	});
	private final KeepsakeCasketBlockEntity skullChest = KeepsakeCasketBlockEntity.createSkullChestBE(BlockPos.ZERO, TFBlocks.SKULL_CHEST.get().defaultBlockState());
	private final KeepsakeCasketBlockEntity skullCasket = KeepsakeCasketBlockEntity.createKeepsakeCasketBE(BlockPos.ZERO, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState());
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
	private Map<BossVariant, TrophyBlockModel> trophies = TrophyRenderer.createTrophyRenderers(Minecraft.getInstance().getEntityModels());
	private Map<SkullBlock.Type, SkullModelBase> skulls = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
	private final CandelabraBlockEntity candelabra = new CandelabraBlockEntity(BlockPos.ZERO, TFBlocks.CANDELABRA.get().defaultBlockState());
	private final BrazierBlockEntity brazier = new BrazierBlockEntity(BlockPos.ZERO, TFBlocks.BRAZIER.get().defaultBlockState());

	// Use the cached INSTANCE.get instead
	private ISTER() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		this.shield = new KnightmetalShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.KNIGHTMETAL_SHIELD));
		this.trophies = TrophyRenderer.createTrophyRenderers(Minecraft.getInstance().getEntityModels());
		this.skulls = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());

		TwilightForestMod.LOGGER.debug("Reloaded ISTER!");
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext camera, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			Minecraft minecraft = Minecraft.getInstance();
			if (block instanceof AbstractTrophyBlock trophyBlock) {
				BossVariant variant = trophyBlock.getVariant();
				TrophyBlockModel trophy = this.trophies.get(variant);

				if (camera == ItemDisplayContext.GUI) {
					ModelResourceLocation back = ModelResourceLocation.standalone(TwilightForestMod.prefix("item/" + ((AbstractTrophyBlock) block).getVariant().getTrophyType().getModelName()));
					BakedModel modelBack = minecraft.getItemRenderer().getItemModelShaper().getModelManager().getModel(back);

					Lighting.setupForFlatItems();
					MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
					pose.pushPose();
					Lighting.setupForFlatItems();
					pose.translate(0.5F, 0.5F, -1.5F);
					minecraft.getItemRenderer().render(TrophyRenderer.stack, ItemDisplayContext.GUI, false, pose, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, modelBack.applyTransform(camera, pose, false));
					pose.popPose();
					bufferSource.endBatch();
					Lighting.setupFor3DItems();

					pose.pushPose();
					pose.translate(0.5F, 0.5F, 0.5F);
					pose.mulPose(Axis.XP.rotationDegrees(30));
					pose.mulPose(Axis.YN.rotationDegrees(TFConfig.rotateTrophyHeadsGui && !minecraft.isPaused() ? ClientEvents.time % 360 : -45));
					pose.translate(-0.5F, -0.5F, -0.5F);
					pose.translate(0.0F, 0.25F, 0.0F);
					if (trophyBlock.getVariant() == BossVariant.UR_GHAST) pose.translate(0.0F, 0.5F, 0.0F);
					if (trophyBlock.getVariant() == BossVariant.ALPHA_YETI) pose.translate(0.0F, -0.15F, 0.0F);
					TrophyRenderer.render(null, 180.0F, trophy, variant, !minecraft.isPaused() ? ClientEvents.time + minecraft.getTimer().getRealtimeDeltaTicks() : 0, pose, buffers, light, camera);
					pose.popPose();
				} else {
					TrophyRenderer.render(null, 180.0F, trophy, variant, !minecraft.isPaused() ? ClientEvents.time + minecraft.getTimer().getRealtimeDeltaTicks() : 0, pose, buffers, light, camera);
				}

			} else if (block instanceof SkullChestBlock) {
				int damage = stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0);

				BlockEntity chestEntity = block == TFBlocks.KEEPSAKE_CASKET.value() ? this.skullCasket : this.skullChest;
				if (minecraft.getBlockEntityRenderDispatcher().getRenderer(chestEntity) instanceof SkullChestRenderer<?> renderer) {
					renderer.renderCasket(0.0F, pose, buffers, light, overlay, renderer.getTextureLocation(damage), Direction.NORTH);
				}
			} else if (block instanceof TFChestBlock) {
				minecraft.getBlockEntityRenderDispatcher().renderItem(this.chestEntities.get(block), pose, buffers, light, overlay);
			} else if (block instanceof TFTrappedChestBlock) {
				minecraft.getBlockEntityRenderDispatcher().renderItem(this.trappedChestEntities.get(block), pose, buffers, light, overlay);
			} else if (block instanceof AbstractSkullCandleBlock candleBlock) {
				ResolvableProfile profile = stack.get(DataComponents.PROFILE);

				if (profile != null && !profile.isResolved()) {
					stack.remove(DataComponents.PROFILE);
					profile.resolve().thenAcceptAsync(p_329787_ -> stack.set(DataComponents.PROFILE, p_329787_), minecraft);

					return;
				}

				SkullBlock.Type type = candleBlock.getType();
				SkullModelBase base = this.skulls.get(type);
				RenderType renderType = SkullCandleRenderer.getRenderType(type, profile);
				SkullCandleRenderer.renderSkull(null, 180.0F, 0.0F, pose, buffers, light, base, renderType);

				//we put the candle
				pose.translate(0.0F, 0.5F, 0.0F);

				SkullCandles skullCandles = stack.getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT);

				minecraft.getBlockRenderer().renderSingleBlock(
					AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(skullCandles.color()))
						.defaultBlockState().setValue(CandleBlock.CANDLES, skullCandles.count()), pose, buffers, light, overlay, ModelData.EMPTY, RenderType.cutout());
			} else if (block instanceof CandelabraBlock) {
				//we need to render the candelabra block here since we have to use builtin/entity on the item.
				//This doesnt allow us to set the item parent to the candelabra block, and without it, only the candles render, if any
				minecraft.getBlockRenderer().renderSingleBlock(TFBlocks.CANDELABRA.get().defaultBlockState(), pose, buffers, light, overlay);
				CandelabraData candelabraData = stack.get(TFDataComponents.CANDELABRA_DATA);
				if (candelabraData != null) {
					CandelabraBlockEntity copy = this.candelabra;
					copy.setData(candelabraData);
					minecraft.getBlockEntityRenderDispatcher().renderItem(copy, pose, buffers, light, overlay);
				}
			} else if (block instanceof CritterBlock critter) {
				BlockEntity blockEntity = critter.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
				if (blockEntity != null) {
					minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity).render(null, 0, pose, buffers, light, overlay);
				}
			} else if (block instanceof JarBlock jarBlock) {
				JarRenderer.renderJarModel(block.defaultBlockState(), minecraft.getBlockRenderer(), pose, buffers, light, overlay);
				JarLid jarLid = stack.getComponents().get(TFDataComponents.JAR_LID.get());
				Item lid = jarLid == null || !JarRenderer.LIDS.containsKey(jarLid.lid()) ? jarBlock.getDefaultLid() : jarLid.lid();
				JarRenderer.renderModel(JarRenderer.LIDS.get(lid), block.defaultBlockState(), minecraft.getBlockRenderer(), pose, buffers, light, overlay);

				if (jarBlock instanceof MasonJarBlock) {
					ItemContainerContents contents = stack.getComponents().get(DataComponents.CONTAINER);
					if (contents != null) {
						MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
						pose.pushPose();
						pose.translate(0.5D, 0.4375D, 0.5D);
						pose.scale(0.5F, 0.5F, 0.5F);
						minecraft.getItemRenderer().render(contents.copyOne(), itemDisplayContextEnumExtension.JARRED, false, pose, bufferSource, light, OverlayTexture.NO_OVERLAY, minecraft.getItemRenderer().getModel(contents.copyOne(), null, null, 1));
						pose.popPose();
						bufferSource.endBatch();
					}
				}
			} else if (block instanceof BrazierBlock brazierBlock) {
				BlockEntity blockEntity = brazierBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
				if (blockEntity != null) {
					minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity).render(blockEntity, 0, pose, buffers, light, overlay);
				}
			}
		} else if (item instanceof KnightmetalShieldItem) {
			pose.pushPose();
			pose.scale(1.0F, -1.0F, -1.0F);
			Material material = new Material(Sheets.SHIELD_SHEET, TwilightForestMod.prefix("entity/knightmetal_shield"));
			VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffers, this.shield.renderType(material.atlasLocation()), true, stack.hasFoil()));
			this.shield.renderToBuffer(pose, vertexconsumer, light, overlay);
			pose.popPose();
		} else if (item instanceof MysticCrownItem && this.trophies.get(BossVariant.LICH) instanceof LichModel<?> lichModel) {
			lichModel.hat.yRot = 0;
			pose.pushPose();
			pose.scale(1.0F, -1.0F, -1.0F);
			if (camera == ItemDisplayContext.GUI) {
				pose.translate(0.5F, -0.1F, 0.0F);
				pose.mulPose(Axis.XP.rotationDegrees(30));
				pose.mulPose(Axis.YP.rotationDegrees(45));
			} else {
				pose.translate(0.5F, 0.0F, -0.5F);
				pose.scale(1.05F, 1.05F, 1.05F);
			}
			lichModel.hat.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(LichRenderer.TEXTURE)), light, overlay);
			pose.popPose();
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
