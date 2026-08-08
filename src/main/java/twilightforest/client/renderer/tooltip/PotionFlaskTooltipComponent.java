package twilightforest.client.renderer.tooltip;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46C;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.item.PotionFlaskItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	// [VanillaCopy] the copy of deleted Potion.getName
	private static String getName(Optional<Holder<Potion>> potion, String descriptionId) {
		if (potion.isPresent()) {
			String s = potion.get().value().name();
			if (s != null) {
				return descriptionId + s;
			}
		}

		String s1 = potion.flatMap(Holder::unwrapKey).map(p_331494_ -> p_331494_.identifier().getPath()).orElse("empty");
		return descriptionId + s1;
	}

	private List<Component> getPotionTooltips() {
		if (this.component.potion().potion().isPresent()) {
			List<Component> tooltips = new ArrayList<>();
			tooltips.add(Component.translatable(getName(this.component.potion().potion(), "item.minecraft.potion.effect.")));
			PotionContents.addPotionTooltip(this.component.potion().potion().get().value().getEffects(), tooltips::add, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate());
			return tooltips;
		}
		return List.of();
	}

	private int getContentXOffset(int offs) {
		return (offs - WIDTH) / 2;
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		int offs = 113; //TODO replace with 4th param in 1.21.2+ so things properly center
		if (this.component.potion().potion().isEmpty()) {
			graphics.textWithWordWrap(font, EMPTY_DESCRIPTION, x, y, WIDTH, 11184810);
		} else {
			int height = 0;
			for (var component : this.getPotionTooltips()) {
				int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 11184810;
				if (component.getString().isEmpty()) {
					height += font.lineHeight;
				} else {
					graphics.textWithWordWrap(font, component, x, y + height, WIDTH, color);
				}
				height += font.split(component, WIDTH).size() * font.lineHeight + 1;
			}
		}
		this.drawPotionBar(x + this.getContentXOffset(offs), y + this.getDescriptionHeight(font) + 4, font, graphics);
	}

	private void drawPotionBar(int x, int y, Font font, GuiGraphicsExtractor graphics) {
		int segmentSplit = this.getWidth(font) / this.maxDoses;
		if (this.component.doses() <= 0) {
			graphics.centeredText(font, Component.translatable("item.twilightforest.flask.empty"), x + (WIDTH / 2) + 1, y + 3, 16777215);
		}

		this.renderPotion(graphics, x + 1, y + 13, this.component.doses() * segmentSplit - 1, 13, this.component.potion().getColor());
		if (this.component.breakage() > 0) {
			int xPos = x + segmentSplit * (3 - this.component.breakage());
			graphics.fill(xPos, y, xPos + (segmentSplit * this.component.breakage()), y + 13, 0xAA434343);
		}
		int widthProg = segmentSplit;
		for (int i = 1; i < this.maxDoses; i++) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, DOSE_SPRITE, x + widthProg, y, 0, 0, 1, 13, 1, 13);
			widthProg += segmentSplit;
		}

		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BORDER_SPRITE, x, y, WIDTH, 13);
	}

	private void renderPotion(GuiGraphicsExtractor guiGraphics, int xPosition, int yPosition, int desiredWidth, int desiredHeight, int color) {
		if (desiredWidth <= 0 || desiredHeight <= 0) return;

		Identifier waterLocation = Identifier.withDefaultNamespace("block/water_still");

		int startY = yPosition - desiredHeight;

		for (int x = 0; x < desiredWidth; x += 16) {
			int width = Math.min(16, desiredWidth - x);

			for (int y = 0; y < desiredHeight; y += 16) {
				int height = Math.min(16, desiredHeight - y);

				guiGraphics.blit(
					RenderPipelines.GUI_TEXTURED,
					waterLocation,
					xPosition + x,
					startY + y,
					0, 0,
					width, height,
					16, 16,
					color
				);
			}
		}
	}
}
