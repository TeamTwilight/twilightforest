package twilightforest.client.renderer.tooltip;

import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.minecraft.world.level.material.Fluids;

import twilightforest.TwilightForestMod;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.item.PotionFlaskItem;

import java.util.ArrayList;
import java.util.List;

public class PotionFlaskTooltipComponent implements ClientTooltipComponent {

	private static final Identifier BORDER_SPRITE = TwilightForestMod.prefix("flask_bar_border");
	private static final Identifier DOSE_SPRITE = TwilightForestMod.prefix("flask_dose_bar");
	private static final Component EMPTY_DESCRIPTION = Component.translatable("item.twilightforest.flask.empty_description");

	public static final int WIDTH = 115; //hehe

	private final PotionFlaskComponent component;
	private final int maxDoses;

	public PotionFlaskTooltipComponent(PotionFlaskItem.Tooltip tooltip) {
		this.component = tooltip.component();
		this.maxDoses = tooltip.maxDoses();
	}

	@Override
	public int getHeight(Font font) {
		return this.getDescriptionHeight(Minecraft.getInstance().font) + 13 + 8;
	}

	@Override
	public int getWidth(Font font) {
		return WIDTH;
	}

	private int getDescriptionHeight(Font font) {
		if (this.component.potion().potion().isPresent()) {
			var height = 0;
			for (var component : this.getPotionTooltips()) {
				if (component.getString().isEmpty()) {
					height += font.lineHeight;
				}
				height += font.split(component, WIDTH).size() * font.lineHeight + 1;
			}

			return height;
		}
		return font.split(EMPTY_DESCRIPTION, WIDTH).size() * font.lineHeight + 1;
	}

	private List<Component> getPotionTooltips() {
		if (this.component.potion().potion().isPresent()) {
			List<Component> tooltips = new ArrayList<>();
			tooltips.add(this.component.potion().getName("item.minecraft.potion.effect."));
			// 26.1.2: PotionContents.addPotionTooltip signature uses (Iterable, Consumer, float, float)
			PotionContents.addPotionTooltip(this.component.potion().getAllEffects(), tooltips::add, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate());
			return tooltips;
		}
		return List.of();
	}

	private int getContentXOffset(int offs) {
		return (offs - WIDTH) / 2;
	}

	@Override
	public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
		int offs = 113; //TODO replace with 4th param in 1.21.2+ so things properly center
		if (this.component.potion().potion().isEmpty()) {
			// 26.1.2: drawWordWrap replaced by textWithWordWrap
			graphics.textWithWordWrap(font, EMPTY_DESCRIPTION, x, y, WIDTH, 11184810);
		} else {
			int h = 0;
			for (var component : this.getPotionTooltips()) {
				int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 11184810;
				if (component.getString().isEmpty()) {
					h += font.lineHeight;
				} else {
					// 26.1.2: drawWordWrap replaced by textWithWordWrap
					graphics.textWithWordWrap(font, component, x, y + h, WIDTH, color);
				}
				h += font.split(component, WIDTH).size() * font.lineHeight + 1;
			}
		}
		this.drawPotionBar(x + this.getContentXOffset(offs), y + this.getDescriptionHeight(font) + 4, font, graphics);
	}

	private void drawPotionBar(int x, int y, Font font, GuiGraphicsExtractor graphics) {
		int segmentSplit = this.getWidth(font) / this.maxDoses;
		if (this.component.doses() <= 0) {
			// 26.1.2: drawCenteredString replaced by centeredText
			graphics.centeredText(font, Component.translatable("item.twilightforest.flask.empty"), x + (WIDTH / 2) + 1, y + 3, 16777215);
		}

		// 26.1.2: graphics.pose() returns Matrix3x2fStack; renderPotion uses GuiGraphicsExtractor directly
		this.renderPotion(graphics, x + 1, y + 13, this.component.doses() * segmentSplit - 1, 13, this.component.potion().getColor());
		if (this.component.breakage() > 0) {
			int xPos = x + segmentSplit * (3 - this.component.breakage());
			// 26.1.2: RenderSystem methods removed; fill handles blending internally
			graphics.fill(xPos, y, xPos + (segmentSplit * this.component.breakage()), y + 13, 0xAA434343);
		}
		int widthProg = segmentSplit;
		for (int i = 1; i < this.maxDoses; i++) {
			// 26.1.2: blitSprite now requires RenderPipeline as first parameter
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DOSE_SPRITE, x + widthProg, y, 1, 13);
			widthProg += segmentSplit;
		}

		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BORDER_SPRITE, x, y, WIDTH, 13);
	}

	private void renderPotion(GuiGraphicsExtractor graphics, int xPosition, int yPosition, int desiredWidth, int desiredHeight, int color) {
		// 26.1.2: Use GuiGraphicsExtractor.fill for simple color fill; for tiled sprite rendering, use blitTiledSprite
		graphics.fill(xPosition, yPosition, xPosition + desiredWidth, yPosition + desiredHeight, color | 0xFF000000);
	}
}





