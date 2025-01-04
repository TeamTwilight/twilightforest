package twilightforest.client.event;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
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
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.ISTER;
import twilightforest.client.OptifineWarningScreen;
import twilightforest.client.TFShaders;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDimension;
import twilightforest.item.*;
import twilightforest.util.HolderMatcher;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ClientEvents {
	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private static final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private static final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	private static boolean firstTitleScreenShown = false;

	public static int time = 0;
	private static float shakeIntensity = 0.0F;

	private static int aurora = 0;
	private static int lastAurora = 0;

	@Autowired(dist = Dist.CLIENT)
	private static HolderMatcher holderMatcher;

	public static void initGameEvents() {
		NeoForge.EVENT_BUS.addListener(ClientEvents::addCustomTooltips);
		NeoForge.EVENT_BUS.addListener(ClientEvents::clientTick);
		NeoForge.EVENT_BUS.addListener(ClientEvents::customizeSplashes);
		NeoForge.EVENT_BUS.addListener(ClientEvents::handleGameBootup);
		NeoForge.EVENT_BUS.addListener(ClientEvents::killVignette);
		NeoForge.EVENT_BUS.addListener(ClientEvents::removeHostileMountHealth);
		NeoForge.EVENT_BUS.addListener(ClientEvents::renderAurora);
		NeoForge.EVENT_BUS.addListener(ClientEvents::renderCustomBossbars);
		NeoForge.EVENT_BUS.addListener(ClientEvents::renderGiantBlockOutlines);
		NeoForge.EVENT_BUS.addListener(ClientEvents::setMusicInDimension);
		NeoForge.EVENT_BUS.addListener(ClientEvents::shakeCamera);
		NeoForge.EVENT_BUS.addListener(ClientEvents::translateBookAuthor);
		NeoForge.EVENT_BUS.addListener(ClientEvents::unrenderHeadWithTrophies);
		NeoForge.EVENT_BUS.addListener(ClientEvents::updateBowFOV);

		NeoForge.EVENT_BUS.addListener(CloudEvents::renderPrecipitation);
		NeoForge.EVENT_BUS.addListener(CloudEvents::tickWeatherEffects);

		NeoForge.EVENT_BUS.addListener(FogHandler::renderFog);
		NeoForge.EVENT_BUS.addListener(FogHandler::unloadFog);

		NeoForge.EVENT_BUS.addListener(LockedBiomeToastHandler::tickLockedToastLogic);
	}

	private static void handleGameBootup(ScreenEvent.Init.Post event) {
		if (firstTitleScreenShown || !(event.getScreen() instanceof TitleScreen)) return;

		// Registering this resource listener earlier than the main screen will cause a crash
		// Yes, crashing happens if registered to RegisterClientReloadListenersEvent
		if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager resourceManager) {
			resourceManager.registerReloadListener(ISTER.INSTANCE.get());
			TwilightForestMod.LOGGER.debug("Registered ISTER listener");
		}

		if (RegistrationEvents.isOptifinePresent() && !TFConfig.disableOptifineNagScreen) {
			Minecraft.getInstance().setScreen(new OptifineWarningScreen(event.getScreen()));
		}

		firstTitleScreenShown = true;
	}

	private static void customizeSplashes(ScreenEvent.Init.Post event) {
		if (event.getScreen() instanceof TitleScreen title) {
			SplashRenderer renderer = title.splash;
			if (renderer != null) {
				LocalDate date = LocalDate.now();
				if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
					RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
					renderer.splash = String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011));
				}
			}
		}
	}

	private static void setMusicInDimension(SelectMusicEvent event) {
		Music music = event.getOriginalMusic();
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFDimension.isTwilightWorldOnClient(Minecraft.getInstance().level)) {
			event.setMusic(Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).value().getBackgroundMusic().orElse(Musics.GAME));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	private static void removeHostileMountHealth(RenderGuiLayerEvent.Pre event) {
		if (VanillaGuiLayers.VEHICLE_HEALTH == event.getName()) {
			if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}
	}

	/**
	 * Render aurora effect as needed
	 */
	private static void renderAurora(RenderLevelStageEvent event) {
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

	private static void killVignette(RenderFrameEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			minecraft.gui.vignetteBrightness = 0.0F;
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}

	private static void clientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.cameraEntity != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				RegistryAccess access = mc.level.registryAccess();
				Holder<Biome> biome = mc.level.getBiome(mc.cameraEntity.blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> holderMatcher.match(c, biome)))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}

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
							BlockEntity beanstalk = beanstalksInChunk.getFirst();
							Player player = mc.player;
							shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
							if (shakeIntensity > 0) {
								player.moveTo(player.getX(), player.getY(), player.getZ(),
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

	private static void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			shakeIntensity = 0F;
		}
	}

	private static void addCustomTooltips(ItemTooltipEvent event) {
		ItemStack item = event.getItemStack();

		if (item.has(TFDataComponents.EMPERORS_CLOTH)) {
			event.getToolTip().add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (item.is(ItemTagGenerator.WIP)) {
			event.getToolTip().add(WIP_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	private static void updateBowFOV(ComputeFovModifierEvent event) {
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

	private static void unrenderHeadWithTrophies(RenderLivingEvent.Pre<?, ?> event) {
		ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.getItem() instanceof TrophyItem) && !areCuriosEquipped(event.getEntity());
		boolean isPlayer = event.getEntity() instanceof Player;
		if (event.getRenderer().getModel() instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = visible && (!isPlayer || headedModel.getHead().visible);  // some mods like Better Combat can move player's head and hide it in the first person view
			if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = visible && (!isPlayer || humanoidModel.hat.visible);
			}
		}
	}

	private static boolean areCuriosEquipped(LivingEntity entity) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof TrophyItem);
		}
		return false;
	}

	private static void translateBookAuthor(ItemTooltipEvent event) {
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

	private static void renderGiantBlockOutlines(RenderHighlightEvent.Block event) {
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

	private static void renderCustomBossbars(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}
	}
}
