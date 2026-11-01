package twilightforest.client.event;

import twilightforest.forge.attachment.Attachments;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.forge.beans.Autowired;
import twilightforest.client.overlay.ItemDisplayOverlay;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.components.item.OreScannerData;
import twilightforest.config.TFConfig;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;
import twilightforest.util.ComponentAlignment;

import java.text.DecimalFormat;
import java.util.*;

public class OverlayHandler {
	private static final ResourceLocation QUESTING_RAM_CHECK_SPRITE = TwilightForestMod.prefix("questing_ram_check");
	private static final ResourceLocation QUESTING_RAM_X_SPRITE = TwilightForestMod.prefix("questing_ram_x");
	private static final ResourceLocation FORTIFICATION_SHIELD_SPRITE = TwilightForestMod.prefix("fortification_shield");
	public static final Map<Long, OreMeterInfoCache> ORE_METER_STAT_CACHE = new HashMap<>();

	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	/**
	 * Les calques d'interface du mod, dessines apres l'interface du jeu.
	 *
	 * <p>NeoForge les place entre ceux du jeu ({@code RegisterGuiLayersEvent}) et tient la hauteur
	 * deja occupee de chaque cote de la barre d'objets ({@code leftHeight}/{@code rightHeight}).
	 * Forge 1.21.1 n'a ni l'un ni l'autre : {@code GuiMixin} appelle cette methode a la fin de
	 * {@code Gui.render}, et les deux hauteurs sont recalculees comme le fait le jeu (coeurs et
	 * armure a gauche, vie de la monture a droite).</p>
	 */
	public static void renderAll(Gui gui, GuiGraphics graphics) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) return;
		int width = graphics.guiWidth();
		int height = graphics.guiHeight();

		// Portail : le voile violet, dessine meme interface cachee, comme chez NeoForge.
		TFPortalAttachment portal = Attachments.get(player, TFDataAttachments.TF_PORTAL_COOLDOWN);
		if (portal.getPortalTimer() > 0) {
			Window window = minecraft.getWindow();
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			graphics.setColor(1.0F, 1.0F, 1.0F, (float) portal.getPortalTimer() / (float) TFPortalAttachment.MAX_TICKS);
			TextureAtlasSprite textureatlassprite = minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState());
			graphics.blit(0, 0, -90, window.getGuiScaledWidth(), window.getGuiScaledHeight(), textureatlassprite);
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		}

		if (minecraft.options.hideGui) return;

		if (TFConfig.showQuestRamCrosshairIndicator) {
			RenderSystem.enableBlend();
			renderIndicator(minecraft, graphics, gui, player, width, height);
			RenderSystem.disableBlend();
		}

		// Le jeu cache la faim sur une monture qui a une barre de vie ; le mod la remet quand la
		// monture est hostile, au-dessus des coeurs de la monture.
		if (minecraft.gameMode.canHurtPlayer() && HostileMountEvents.isRidingUnfriendly(player) && player.getVehicle() instanceof LivingEntity mount) {
			int hearts = Math.min((int) (mount.getMaxHealth() + 0.5F) / 2, 30);
			int rightHeight = 39 + Mth.ceil(hearts / 10.0F) * 10;
			gui.renderFood(graphics, player, height - rightHeight, width / 2 + 91);
		}

		if ((minecraft.gameMode.canHurtPlayer() || TFConfig.showFortificationShieldIndicatorInCreative) && Attachments.has(player, TFDataAttachments.FORTIFICATION_SHIELDS) && Attachments.get(player, TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft() > 0 && TFConfig.showFortificationShieldIndicator) {
			renderShieldCount(graphics, width, height - leftHeight(minecraft, player), Attachments.get(player, TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft());
		}

		ItemDisplayOverlay.render(graphics, minecraft, minecraft.getWindow(), gui, getCameraPlayer());

		if (!gui.getDebugOverlay().showDebugScreen() && minecraft.screen == null) {
			renderOreMeterStats(graphics, player);
		}
	}

	/** Hauteur occupee a gauche au-dessus de la barre d'objets : lignes de coeurs puis armure. */
	private static int leftHeight(Minecraft minecraft, Player player) {
		int leftHeight = 39;
		if (minecraft.gameMode.canHurtPlayer()) {
			float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), player.getHealth());
			int absorb = Mth.ceil(player.getAbsorptionAmount());
			int healthRows = Mth.ceil((maxHealth + absorb) / 2.0F / 10.0F);
			int rowHeight = Math.max(10 - (healthRows - 2), 3);
			leftHeight += healthRows * rowHeight;
			if (rowHeight != 10) leftHeight += 10 - rowHeight;
			if (player.getArmorValue() > 0) leftHeight += 10;
		}
		return leftHeight;
	}

	private static void renderIndicator(Minecraft minecraft, GuiGraphics graphics, Gui gui, Player player, int screenWidth, int screenHeight) {
		if (minecraft.options.getCameraType().isFirstPerson() && (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.canRenderCrosshairForSpectator(minecraft.hitResult)) && minecraft.crosshairPickEntity instanceof QuestRam ram) {
			ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
			if (!stack.isEmpty()) {
				for (var questEntry : questingRamCurrentContext.getContext().questItems().entrySet()) {
					if (questEntry.getValue().test(stack)) {
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						int j = ((screenHeight - 1) / 2) - 11;
						int k = ((screenWidth - 1) / 2) - 3;
						if (!ram.isColorPresent(questEntry.getKey())) {
							graphics.blitSprite(QUESTING_RAM_X_SPRITE, k, j, 7, 7);
						} else {
							graphics.blitSprite(QUESTING_RAM_CHECK_SPRITE, k, j, 7, 7);
						}
						RenderSystem.defaultBlendFunc();
						break;
					}
				}
			}
		}
	}

	private static void renderShieldCount(GuiGraphics graphics, int screenWidth, int y, int shieldCount) {
		for (int i = 0; i < Math.min(shieldCount, 10); i++) {
			graphics.blitSprite(FORTIFICATION_SHIELD_SPRITE, screenWidth / 2 - 91 + (i * 8), y, 9, 9);
		}
	}

	private static void renderOreMeterStats(GuiGraphics graphics, Player player) {
		if (player.isHolding(TFItems.ORE_METER.get())) {
			InteractionHand handToUse = player.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.ORE_METER.get()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			ItemStack selectedMeter = player.getItemInHand(handToUse);
			if (OreMeterItem.isLoading(selectedMeter)) {
				int dots = (OreMeterItem.getLoadProgress(selectedMeter) / 5) % 3;
				Component component = Component.translatable("misc.twilightforest.ore_meter_loading");
				for (int i = 0; i <= dots; i++) {
					component = component.copy().append(".");
				}
				graphics.fill(0, 0, 56, 16, 0x9b000000);
				graphics.drawString(Minecraft.getInstance().font, component, 4, 4, 16777215, false);
			} else {
				OreScannerData oreScannerData = selectedMeter.get(TFDataComponents.ORE_DATA.get());

				if (oreScannerData == null) return;

				long identifier = oreScannerData.universalId();
				if (identifier != 0L && !ORE_METER_STAT_CACHE.containsKey(identifier)) {
					initTooltips(identifier, selectedMeter.getOrDefault(TFDataComponents.ORE_RANGE.get(), 1), oreScannerData);
				}

				if (ORE_METER_STAT_CACHE.containsKey(identifier)) {
					OreMeterInfoCache info = ORE_METER_STAT_CACHE.get(identifier);

					if (info != null) {
						info.renderData(graphics);
					}
				}
			}
		}
	}

	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	private static void initTooltips(long id, int range, OreScannerData data) {
		ChunkPos pos = data.scannedChunk();
		int totalScanned = data.totalScannedBlocks();

		List<Component> headerRowTexts = ImmutableList.of(
			Component.translatable("misc.twilightforest.ore_meter_range", range, pos.x, pos.z),
			Component.translatable("misc.twilightforest.ore_meter_total", totalScanned)
		);

		ArrayList<ComponentColumn> columns = new ArrayList<>();

		List<Pair<String, Integer>> scanData = data.counts().entrySet().stream()
			.map(e -> Pair.of(e.getKey(), e.getValue())) // Convert Entries into Pairs
			.sorted(Comparator.comparing(Pair::getSecond)) // Sort Pairs by second element (quantity)
			.toList(); // Make sorted immutable list

		if (TFConfig.prettifyOreMeterGui) {
			ComponentColumn padding = ComponentColumn.padding(1);
			List<Integer> counts = scanData.stream().map(Pair::getSecond).toList();

			columns.add(nameColumn(scanData.stream().map(Pair::getFirst).toList()));
			columns.add(padding);
			columns.add(dashColumn(scanData.size()));
			columns.add(padding);
			columns.add(countColumn(counts));
			columns.add(padding);
			columns.add(ratioColumn(totalScanned, counts));
		} else {
			columns.add(withoutPrettyPrinting(totalScanned, scanData));
		}

		ORE_METER_STAT_CACHE.put(id, OreMeterInfoCache.build(headerRowTexts, columns));
	}

	private static ComponentColumn withoutPrettyPrinting(int totalScanned, List<Pair<String, Integer>> entries) {
		List<Component> tooltips = new ArrayList<>();

		for (Pair<String, Integer> entry : entries) {
			String percentage = FORMAT.format(entry.getSecond() * 100.0F / totalScanned);
			Component formattedEntry = Component.translatable(entry.getFirst())
				.append(Component.literal(" "))
				.append(Component.translatable("misc.twilightforest.ore_meter_separator"))
				.append(Component.literal(" " + entry.getSecond() + " "))
				.append(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));

			tooltips.add(formattedEntry);
		}

		return ComponentColumn.build(tooltips, ComponentAlignment.LEFT);
	}

	private static ComponentColumn nameColumn(List<String> oreNameKeys) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_block").withStyle(ChatFormatting.GRAY));

		for (String oreNameKey : oreNameKeys) {
			MutableComponent translatable = Component.translatable(oreNameKey);
			toList.add(translatable);
		}

		return ComponentColumn.build(toList.build(), ComponentAlignment.LEFT);
	}

	private static ComponentColumn dashColumn(int size) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.empty());

		MutableComponent dash = Component.translatable("misc.twilightforest.ore_meter_separator");
		for (int i = 0; i < size; i++)
			toList.add(dash);

		return ComponentColumn.build(toList.build(), ComponentAlignment.CENTER);
	}

	private static ComponentColumn countColumn(List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_count").withStyle(ChatFormatting.GRAY));

		oreCounts.stream().mapToInt(count -> count).mapToObj(count -> Component.literal(String.valueOf(count))).forEach(toList::add);

		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	private static ComponentColumn ratioColumn(int totalScanned, List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_ratio").withStyle(ChatFormatting.GRAY));

		for (int count : oreCounts) {
			var percentage = FORMAT.format(count * 100.0F / totalScanned);
			toList.add(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));
		}

		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	public record ComponentColumn(List<? extends Component> textRows, int maxPixelWidth,
								  ComponentAlignment textAlignment) {
		public static ComponentColumn build(List<? extends Component> rowTexts, ComponentAlignment textAlignment) {
			int maxColumnPixelWidth = rowTexts.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);
			return new ComponentColumn(rowTexts, maxColumnPixelWidth, textAlignment);
		}

		public static ComponentColumn padding(int forcedExtraMaxWidthBySpaces) {
			return new ComponentColumn(List.of(), forcedExtraMaxWidthBySpaces * Minecraft.getInstance().font.width(" "), ComponentAlignment.LEFT);
		}

		private int renderColumn(GuiGraphics graphics, ComponentColumn column, int xOff, int yOff, int verticalTextPixelsAdvance) {
			for (Component rowText : column.textRows) {
				int textPixelWidth = Minecraft.getInstance().font.width(rowText);
				int textXPos = xOff + this.textAlignment.getTextOffset(textPixelWidth, this.maxPixelWidth);
				graphics.drawString(Minecraft.getInstance().font, rowText, textXPos, yOff, 0x00_ff_ff_ff, false);
				yOff += verticalTextPixelsAdvance;
			}

			return column.maxPixelWidth;
		}
	}

	public record OreMeterInfoCache(int totalPixelWidth, int totalRowCount, List<Component> headerRows, List<ComponentColumn> textColumns) {
		public static OreMeterInfoCache build(List<Component> headers, List<ComponentColumn> columns) {
			// All these widths are measured in pixels, used for dealing with the font
			int summedColumnMaxWidths = columns.stream().mapToInt(ComponentColumn::maxPixelWidth).sum();
			int maxHeaderWidth = headers.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);

			int maxPixelWidth = Math.max(summedColumnMaxWidths, maxHeaderWidth);

			// Not measured in pixels, instead goes by element count - How many total rows of text will be shown in the GUI
			int totalRowCount = headers.size() + columns.stream().mapToInt(column -> column.textRows.size()).max().orElse(0);

			return new OreMeterInfoCache(maxPixelWidth, totalRowCount, ImmutableList.copyOf(headers), ImmutableList.copyOf(columns));
		}

		public void renderData(GuiGraphics graphics) {
			int verticalTextPixelsAdvance = Minecraft.getInstance().font.lineHeight + 1;

			graphics.fill(0, 0, this.totalPixelWidth + 8, this.totalRowCount * verticalTextPixelsAdvance + 6, 0x9b_00_00_00);

			int xOff = 4;
			int yOff = 4;

			for (Component headerRowText : this.headerRows) {
				graphics.drawString(Minecraft.getInstance().font, headerRowText, xOff, yOff, 0x00_ff_ff_ff, false);
				yOff += verticalTextPixelsAdvance;
			}

			for (ComponentColumn column : this.textColumns) {
				xOff += column.renderColumn(graphics, column, xOff, yOff, verticalTextPixelsAdvance);
			}
		}
	}

	@Nullable
	private static Player getCameraPlayer() {
		return Minecraft.getInstance().getCameraEntity() instanceof Player player ? player : null;
	}
}