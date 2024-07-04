package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelLoader;
import twilightforest.client.model.block.doors.CastleDoorModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.leaves.BakedLeavesModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.item.TrollsteinnModel;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.*;
import twilightforest.item.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT)
public class TFClientEvents {

	@EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
			event.register(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
			event.register(TwilightForestMod.prefix("giant_block"), GiantBlockModelLoader.INSTANCE);
			event.register(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
			event.register(TwilightForestMod.prefix("castle_door"), CastleDoorModelLoader.INSTANCE);
			event.register(TwilightForestMod.prefix("noise_varying"), NoiseVaryingModelLoader.INSTANCE);
		}

		@SubscribeEvent
		public static void modelBake(ModelEvent.ModifyBakingResult event) {
			ItemProperties.register(TFItems.CUBE_OF_ANNIHILATION.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
				stack.get(TFDataComponents.THROWN_PROJECTILE) != null ? 1 : 0);

			ItemProperties.register(TFItems.KNIGHTMETAL_SHIELD.get(), ResourceLocation.parse("blocking"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.MOON_DIAL.get(), ResourceLocation.parse("phase"), new ClampedItemPropertyFunction() {
				@Override
				public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entityBase, int idk) {
					boolean flag = entityBase != null;
					Entity entity = flag ? entityBase : stack.getFrame();

					if (level == null && entity != null) level = (ClientLevel) entity.level();

					return level == null ? 0.0F : (float) (level.dimensionType().natural() ? Mth.frac(level.getMoonPhase() / 8.0f) : this.wobble(level, Math.random()));
				}

				double rotation;
				double rota;
				long lastUpdateTick;

				private double wobble(Level level, double rotation) {
					if (level.getGameTime() != this.lastUpdateTick) {
						this.lastUpdateTick = level.getGameTime();
						double delta = rotation - this.rotation;
						delta = Mth.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
						this.rota += delta * 0.1D;
						this.rota *= 0.9D;
						this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
					}
					return this.rotation;
				}
			});

			ItemProperties.register(TFItems.ORE_METER.get(), TwilightForestMod.prefix("active"), (stack, level, entity, idk) -> {
				if (OreMeterItem.isLoading(stack)) {
					int totalLoadTime = OreMeterItem.LOAD_TIME + OreMeterItem.getRange(stack) * 25;
					int progress = OreMeterItem.getLoadProgress(stack);
					return progress % 5 >= 2 + (int) (Math.random() * 2) && progress <= totalLoadTime - 15 ? 1 : 0;
				}
				return stack.has(TFDataComponents.ORE_DATA) ? 1 : 0;
			});

			ItemProperties.register(TFItems.MOONWORM_QUEEN.get(), TwilightForestMod.prefix("alt"), (stack, level, entity, idk) -> {
				if (entity != null && entity.getUseItem() == stack) {
					int useTime = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
					if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
						return 1;
					}
				}
				return 0;
			});

			ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
				if (entity == null) return 0.0F;
				else
					return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
			});

			ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
				if (entity == null) return 0.0F;
				else
					return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
			});

			ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
				if (entity == null) return 0.0F;
				else
					return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
			});

			ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
				if (entity == null) return 0.0F;
				else
					return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
			});

			ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
				if (entity == null) return 0.0F;
				else {
					ItemStack itemstack = entity.getUseItem();
					return !itemstack.isEmpty() ? (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
				}
			});

			ItemProperties.register(TFBlocks.RED_THREAD.get().asItem(), TwilightForestMod.prefix("size"), (stack, level, entity, idk) -> {
				if (stack.getCount() >= 32) {
					return 1.0F;
				} else if (stack.getCount() >= 16) {
					return 0.5F;
				} else if (stack.getCount() >= 4) {
					return 0.25F;
				}
				return 0.0F;
			});

			ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

			ItemProperties.register(TFItems.BLOCK_AND_CHAIN.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
				stack.get(TFDataComponents.THROWN_PROJECTILE) != null ? 1 : 0);

			ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.THINK, (stack, level, entity, idk) ->
				stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS).equals("think") ? 1 : 0);

			ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.FULL, (stack, level, entity, idk) ->
				stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS).equals("full") ? 1 : 0);

			ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("breakage"), (stack, level, entity, i) ->
				stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).breakage());

			ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
				stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).doses());

			ItemProperties.register(TFItems.GREATER_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
				stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).doses());

			ItemProperties.register(TFItems.CRUMBLE_HORN.get(), TwilightForestMod.prefix("tooting"), (stack, world, entity, i) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
			);



			Map<ModelResourceLocation, BakedModel> models = event.getModels();
			List<Map.Entry<ModelResourceLocation, BakedModel>> leavesModels = models.entrySet().stream()
				.filter(entry -> entry.getKey().id().getNamespace().equals(TwilightForestMod.ID) && entry.getKey().id().getPath().contains("leaves") && !entry.getKey().id().getPath().contains("dark")).toList();

			leavesModels.forEach(entry -> models.put(entry.getKey(), new BakedLeavesModel(entry.getValue())));

			BakedModel oldModel = event.getModels().get(ModelResourceLocation.inventory(TwilightForestMod.prefix("trollsteinn")));
			models.put(ModelResourceLocation.inventory(TwilightForestMod.prefix("trollsteinn")), new TrollsteinnModel(oldModel));
		}

		@SubscribeEvent
		public static void registerModels(ModelEvent.RegisterAdditional event) {
			event.register(ShieldLayer.LOC);
			event.register(ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trophy")));
			event.register(ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trophy_minor")));
			event.register(ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trophy_quest")));
			event.register(ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trollsteinn_light")));
		}

		@SubscribeEvent
		public static void registerDimEffects(RegisterDimensionSpecialEffectsEvent event) {
			TFSkyRenderer.createStars();
			event.register(TFDimension.DIMENSION_RENDERER, new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	@SubscribeEvent
	public static void preOverlay(RenderGuiLayerEvent.Pre event) {
		if (VanillaGuiLayers.VEHICLE_HEALTH == event.getName()) {
			if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		} else if (VanillaGuiLayers.CAMERA_OVERLAYS == event.getName()) {
			Entity camera = Minecraft.getInstance().cameraEntity;
			if (camera != null) {
				TFPortalAttachment portalAttachment = camera.getData(TFDataAttachments.TF_PORTAL_COOLDOWN);
				if (portalAttachment.getPortalTimer() <= 0) return;
				GuiGraphics pGuiGraphics = event.getGuiGraphics();

				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, (float) portalAttachment.getPortalTimer() / (float) TFPortalAttachment.MAX_TICKS);

				@SuppressWarnings("deprecation")
				TextureAtlasSprite textureatlassprite = Minecraft.getInstance()
					.getBlockRenderer()
					.getBlockModelShaper()
					.getParticleIcon(TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState());

				pGuiGraphics.blit(0, 0, -90, pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight(), textureatlassprite);
				RenderSystem.disableBlend();
				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Render effects in first-person perspective and aurora
	 */
	@SubscribeEvent
	public static void renderWorldLast(RenderLevelStageEvent event) {
		if (Minecraft.getInstance().level == null) return;

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && (aurora > 0 || lastAurora > 0) && TFShaders.AURORA != null) {
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

			final float scale = 2048F * (Minecraft.getInstance().gameRenderer.getRenderDistance() / 32F);
			Vec3 pos = event.getCamera().getPosition();
			float y = (float) (256F - pos.y());
			buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.setShaderColor(1F, 1F, 1F, (Mth.lerp(event.getPartialTick().getGameTimeDeltaTicks(), lastAurora, aurora)) / 60F * 0.5F);
			TFShaders.AURORA.invokeThenEndTesselator(
				Minecraft.getInstance().level == null ? 0 : Mth.abs((int) Minecraft.getInstance().level.getBiomeManager().biomeZoomSeed),
				(float) pos.x(), (float) pos.y(), (float) pos.z(), buffer);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			RenderSystem.disableDepthTest();
			RenderSystem.disableBlend();
		}
	}

	/**
	 * On the tick, we kill the vignette
	 */
	@SubscribeEvent
	public static void renderTick(RenderFrameEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			// vignette
			if (minecraft.gui != null) {
				minecraft.gui.vignetteBrightness = 0.0F;
			}
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			if (minecraft.gui != null) {
				minecraft.gui.setOverlayMessage(Component.empty(), false);
			}
		}
	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);
	private static float intensity = 0.0F;

	private static int aurora = 0;
	private static int lastAurora = 0;

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		float partial = mc.getTimer().getRealtimeDeltaTicks();

		if (!mc.isPaused()) {
			time++;

			rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
			sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

			rotationTicker = rotationTickerI + partial;
			sineTicker = sineTicker + partial;

			lastAurora = aurora;
			if (Minecraft.getInstance().level != null && Minecraft.getInstance().cameraEntity != null && !TFConfig.getValidAuroraBiomes(Minecraft.getInstance().level.registryAccess()).isEmpty()) {
				RegistryAccess access = Minecraft.getInstance().level.registryAccess();
				Holder<Biome> biome = Minecraft.getInstance().level.getBiome(Minecraft.getInstance().cameraEntity.blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).contains(access.registryOrThrow(Registries.BIOME).getKey(biome.value())))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}
		}

		if (!mc.isPaused()) {
			BugModelAnimationHelper.animate();

			if (TFConfig.firstPersonEffects && mc.level != null && mc.player != null) {
				HashSet<ChunkPos> chunksInRange = new HashSet<>();
				for (int x = -16; x <= 16; x += 16) {
					for (int z = -16; z <= 16; z += 16) {
						chunksInRange.add(new ChunkPos((int) (mc.player.getX() + x) >> 4, (int) (mc.player.getZ() + z) >> 4));
					}
				}
				for (ChunkPos pos : chunksInRange) {
					if (mc.level.getChunk(pos.x, pos.z, ChunkStatus.FULL, false) != null) {
						List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x, pos.z).getBlockEntities().values().stream()
							.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
							.toList();
						if (!beanstalksInChunk.isEmpty()) {
							BlockEntity beanstalk = beanstalksInChunk.get(0);
							Player player = mc.player;
							intensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
							if (intensity > 0) {
								player.moveTo(player.getX(), player.getY(), player.getZ(),
									player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * intensity,
									player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * intensity);
								intensity = 0.0F;
								break;
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void camera(ViewportEvent.ComputeCameraAngles event) {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && intensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity));
			intensity = 0F;
		}
	}

	private static final MutableComponent WIP_TEXT_0 = Component.translatable("misc.twilightforest.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent WIP_TEXT_1 = Component.translatable("misc.twilightforest.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent NYI_TEXT = Component.translatable("misc.twilightforest.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	@SubscribeEvent
	public static void tooltipEvent(ItemTooltipEvent event) {
		ItemStack item = event.getItemStack();

		if (item.has(TFDataComponents.EMPERORS_CLOTH)) {
			event.getToolTip().add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (!item.is(ItemTagGenerator.WIP) && !item.is(ItemTagGenerator.NYI)) return;

		if (item.is(ItemTagGenerator.WIP)) {
			event.getToolTip().add(WIP_TEXT_0);
			event.getToolTip().add(WIP_TEXT_1);
		} else {
			event.getToolTip().add(NYI_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	@SubscribeEvent
	public static void updateBowFOV(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		if (player.isUsingItem()) {
			Item useItem = player.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = player.getTicksUsingItem() / 20.0F;
				f = f > 1.0F ? 1.0F : f * f;
				event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, (event.getFovModifier() * (1.0F - f * 0.15F))));
			}
		}
	}

	@SubscribeEvent
	public static void unrenderHeadWithTrophies(RenderLivingEvent.Pre<?, ?> event) {
		ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.getItem() instanceof TrophyItem) && !(stack.getItem() instanceof SkullCandleItem) && !areCuriosEquipped(event.getEntity());

		if (event.getRenderer().getModel() instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = visible;
			if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = visible;
			}
		}
	}

	private static boolean areCuriosEquipped(LivingEntity entity) {
//		if (ModList.get().isLoaded("curios")) {
//			return CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof TrophyItem) || CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof SkullCandleItem);
//		}
		return false;
	}

	@SubscribeEvent
	public static void translateBookAuthor(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof WrittenBookItem && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
			if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
				List<Component> components = event.getToolTip();
				for (int i = 0; i < components.size(); i++) {
					Component component = components.get(i);
					if (component.toString().contains("book.byAuthor")) {
						components.set(i, (Component.translatable("book.byAuthor", Component.translatable(TwilightForestMod.ID + ".book.author"))).withStyle(component.getStyle()));
					}
				}
			}
		}
	}

	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);

	@SubscribeEvent
	public static void onRenderBlockHighlightEvent(RenderHighlightEvent.Block event) {
		BlockPos pos = event.getTarget().getBlockPos();
		BlockState state = event.getCamera().getEntity().level().getBlockState(pos);

		if (state.getBlock() instanceof MiniatureStructureBlock) {
			event.setCanceled(true);
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem || (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock))) {
			event.setCanceled(true);
			if (!state.isAir() && player.level().getWorldBorder().isWithinBounds(pos)) {
				BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
				VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
				Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(event.getCamera().getPosition());
				LevelRenderer.renderShape(event.getPoseStack(), consumer, GIANT_BLOCK, xyz.x(), xyz.y(), xyz.z(), 0.0F, 0.0F, 0.0F, 0.45F);
			}
		}
	}

	@SubscribeEvent
	public static void onBossProgressRenderEvent(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}
	}
}
