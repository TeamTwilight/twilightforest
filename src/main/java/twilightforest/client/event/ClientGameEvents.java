package twilightforest.client.event;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import tamaized.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.OptifineWarningScreen;
import twilightforest.client.TFShaders;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.TFConfig;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.*;
import twilightforest.item.*;
import twilightforest.tags.TFItemTags;
import twilightforest.util.HolderMatcher;
import twilightforest.util.entities.EntityRenderingUtil;

import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@tamaized.beanification.Component(dist = Dist.CLIENT)
public class ClientGameEvents {
	private final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	private boolean firstTitleScreenShown = false;

	public static int time = 0;
	private float shakeIntensity = 0.0F;

	private int aurora = 0;
	private int lastAurora = 0;

	private static Field skyFlashTimeField;

	@Autowired(dist = Dist.CLIENT)
	private HolderMatcher holderMatcher;

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::addCustomTooltips);
		NeoForge.EVENT_BUS.addListener(this::clientTick);
		NeoForge.EVENT_BUS.addListener(this::customizeSplashes);
		NeoForge.EVENT_BUS.addListener(this::clearEntityRenderUtilMap);
		NeoForge.EVENT_BUS.addListener(this::handleGameBootup);
		NeoForge.EVENT_BUS.addListener(this::killVignette);
		NeoForge.EVENT_BUS.addListener(this::removeHostileMountHealth);
		NeoForge.EVENT_BUS.addListener(this::renderAurora);
		NeoForge.EVENT_BUS.addListener(this::renderCustomBossbars);
		NeoForge.EVENT_BUS.addListener(this::renderGiantBlockOutlines);
		NeoForge.EVENT_BUS.addListener(this::setMusicInDimension);
		NeoForge.EVENT_BUS.addListener(this::shakeCamera);
		NeoForge.EVENT_BUS.addListener(this::translateBookAuthor);
		NeoForge.EVENT_BUS.addListener(this::unrenderHeadWithTrophies);
		NeoForge.EVENT_BUS.addListener(this::updateBowFOV);

//		NeoForge.EVENT_BUS.addListener(CloudEvents::renderPrecipitation);
//		NeoForge.EVENT_BUS.addListener(CloudEvents::tickWeatherEffects);

		NeoForge.EVENT_BUS.addListener(FogHandler::renderFog);
		NeoForge.EVENT_BUS.addListener(FogHandler::unloadFog);

		NeoForge.EVENT_BUS.addListener(LockedBiomeToastHandler::tickLockedToastLogic);

		NeoForge.EVENT_BUS.addListener(TFSkyRenderer::extractLevelRender);
	}

	private void handleGameBootup(ScreenEvent.Init.Post event) {
		if (firstTitleScreenShown || !(event.getScreen() instanceof TitleScreen)) return;

		if (ClientRegistrationEvents.isOptifinePresent() && !TFConfig.disableOptifineNagScreen) {
			Minecraft.getInstance().setScreen(new OptifineWarningScreen(event.getScreen()));
		}

		firstTitleScreenShown = true;
	}

	private void customizeSplashes(ScreenEvent.Init.Post event) {
		if (event.getScreen() instanceof TitleScreen title) {
			SplashRenderer renderer = title.splash;
			if (renderer != null) {
				LocalDate date = LocalDate.now();
				if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
					RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
					renderer.splash = Component.literal(String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011)));
				}
			}
		}
	}

	private void clearEntityRenderUtilMap(ScreenEvent.Closing event) {
		if (!EntityRenderingUtil.ENTITY_MAP.isEmpty()) EntityRenderingUtil.ENTITY_MAP.clear();
	}

	private void setMusicInDimension(SelectMusicEvent event) {
		Music music = event.getOriginalMusic();
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFDimension.isTwilightWorldOnClient(Minecraft.getInstance().level)) {
			var biomeHolder = Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition());
			var biomeKeyOpt = biomeHolder.unwrapKey();
			if (biomeKeyOpt.isPresent()) {
				var biomeKey = biomeKeyOpt.get();
				Music selectedMusic = Musics.GAME; // Default fallback
				String keyString = biomeKey.toString();
				if (keyString.contains("lake") || keyString.contains("swamp")) {
					selectedMusic = Musics.UNDER_WATER;
				} else if (keyString.contains("enchanted")) {
					selectedMusic = Musics.CREDITS;
				}
				event.setMusic(selectedMusic);
			}
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	private void removeHostileMountHealth(RenderGuiLayerEvent.Pre event) {
		if (VanillaGuiLayers.VEHICLE_HEALTH == event.getName()) {
			if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}
	}

	/**
	 * Render aurora effect as needed
	 */
	private void renderAurora(RenderLevelStageEvent.AfterWeather event) {
		if (Minecraft.getInstance().level == null) return;

		if (aurora > 0 || lastAurora > 0) {
			var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

			VertexConsumer consumer = bufferSource.getBuffer(TFShaders.AURORA);

			float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
			float alpha = (Mth.lerp(partialTicks, lastAurora, aurora)) / 60F * 0.5F;

			final float scale = 2048F * (Minecraft.getInstance().options.renderDistance().get() / 32F);
			Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
			float y = (float) (256F - pos.y());

			PoseStack poseStack = event.getPoseStack();
			poseStack.pushPose();

			PoseStack.Pose lastPose = poseStack.last();

			consumer.addVertex(lastPose.pose(), -scale, y, scale).setColor(1F, 1F, 1F, alpha);
			consumer.addVertex(lastPose.pose(), -scale, y, -scale).setColor(1F, 1F, 1F, alpha);
			consumer.addVertex(lastPose.pose(), scale, y, -scale).setColor(1F, 1F, 1F, alpha);
			consumer.addVertex(lastPose.pose(), scale, y, scale).setColor(1F, 1F, 1F, alpha);

			poseStack.popPose();
			bufferSource.endBatch(TFShaders.AURORA);
		}
	}

	private void killVignette(RenderFrameEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			minecraft.gui.vignetteBrightness = 0.0F;
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}

	private void clientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.getCameraEntity() != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				RegistryAccess access = mc.level.registryAccess();
				Holder<Biome> biome = mc.level.getBiome(mc.getCameraEntity().blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> holderMatcher.match(c, biome)))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}

			BugModelAnimationHelper.animate();

			if (mc.level != null) {
				Integer flashTime = ObfuscationReflectionHelper.getPrivateValue(ClientLevel.class, mc.level, "skyFlashTime");

				if (flashTime != null && flashTime > 0) {
					MagicPaintingRenderer.lastLightning = mc.level.getGameTime();
				}

				if (TFConfig.firstPersonEffects && mc.player != null) {
					HashSet<ChunkPos> chunksInRange = new HashSet<>();
					for (int x = -16; x <= 16; x += 16) {
						for (int z = -16; z <= 16; z += 16) {
							chunksInRange.add(new ChunkPos((int) (mc.player.getX() + x) >> 4, (int) (mc.player.getZ() + z) >> 4));
						}
					}
					for (ChunkPos pos : chunksInRange) {
						if (mc.level.getChunk(pos.x(), pos.z(), ChunkStatus.FULL, false) != null) {
							List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x(), pos.z()).getBlockEntities().values().stream()
								.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
								.toList();
							if (!beanstalksInChunk.isEmpty()) {
								BlockEntity beanstalk = beanstalksInChunk.getFirst();
								Player player = mc.player;
								shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
								if (shakeIntensity > 0) {
									player.absSnapTo(player.getX(), player.getY(), player.getZ(),
										player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * shakeIntensity,
										player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * shakeIntensity);
									shakeIntensity = 0.0F;
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			shakeIntensity = 0F;
		}
	}

	private void addCustomTooltips(ItemTooltipEvent event) {
		ItemStack item = event.getItemStack();

		if (item.has(TFDataComponents.EMPERORS_CLOTH)) {
			event.getToolTip().add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (item.is(TFItemTags.WIP)) {
			event.getToolTip().add(WIP_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	private void updateBowFOV(ComputeFovModifierEvent event) {
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

	private void unrenderHeadWithTrophies(RenderLivingEvent.Pre<?, ?, ?> event) {
		LocalPlayer localPlayer = Minecraft.getInstance().player;
		if (localPlayer == null) {
			return;
		}
		ItemStack stack = localPlayer.getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.is(TFItemTags.TROPHIES)) && !areCuriosEquipped(localPlayer);
		boolean isPlayer = true;
		if (event.getRenderer().getModel() instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = visible && (!isPlayer || headedModel.getHead().visible);
			if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = visible && (!isPlayer || humanoidModel.hat.visible);
			}
		}
	}

	private boolean areCuriosEquipped(LivingEntity entity) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.is(TFItemTags.TROPHIES));
		}
		return false;
	}

	private void translateBookAuthor(ItemTooltipEvent event) {
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

	private void renderGiantBlockOutlines(ExtractBlockOutlineRenderStateEvent event) {
		BlockPos pos = event.getBlockPos();
		BlockState state = event.getBlockState();

		boolean isMiniature = state.getBlock() instanceof MiniatureStructureBlock;

		LocalPlayer player = Minecraft.getInstance().player;
		boolean isHoldingGiantTool = player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem
			|| (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock));

		if (isMiniature || (isHoldingGiantTool && !state.isAir() && player.level().getWorldBorder().isWithinBounds(pos))) {
			Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
			BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
			Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(cameraPos);

			int outlineColor = 0x7F000000;
			event.addCustomRenderer((_, bufferSource, poseStack, _, _) -> {

				if (isHoldingGiantTool && !isMiniature) {
					VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.lines());
					ShapeRenderer.renderShape(
						poseStack,
						consumer,
						GIANT_BLOCK,
						xyz.x(), xyz.y(), xyz.z(),
						outlineColor, 1.0F
					);
				}

				return false;
			});
		}
	}

	private void renderCustomBossbars(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}
	}
}
