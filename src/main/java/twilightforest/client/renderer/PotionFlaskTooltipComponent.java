package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.item.BrittleFlaskItem;

import java.util.ArrayList;
import java.util.List;

public class PotionFlaskTooltipComponent implements ClientTooltipComponent {

	private static final ResourceLocation BORDER_SPRITE = TwilightForestMod.prefix("flask_bar_border");
	private static final ResourceLocation DOSE_SPRITE = TwilightForestMod.prefix("flask_dose_bar");
	private static final Component EMPTY_DESCRIPTION = Component.translatable("item.twilightforest.flask.empty_description");

	public static final int WIDTH = 115; //hehe

	private final PotionFlaskComponent component;
	private final int maxDoses;

	public PotionFlaskTooltipComponent(BrittleFlaskItem.Tooltip tooltip) {
		this.component = tooltip.component();
		this.maxDoses = tooltip.maxDoses();
	}

	@Override
	public int getHeight() {
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
			tooltips.add(Component.translatable(Potion.getName(this.component.potion().potion(), "item.minecraft.potion.effect.")));
			this.component.potion().addPotionTooltip(tooltips::add, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate());
			return tooltips;
		}
		return List.of();
	}

	private int getContentXOffset(int offs) {
		return (offs - WIDTH) / 2;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		int offs = 113; //TODO replace with 4th param in 1.21.2+ so things properly center
		if (this.component.potion().potion().isEmpty()) {
			graphics.drawWordWrap(font, EMPTY_DESCRIPTION, x, y, WIDTH, 11184810);
		} else {
			int height = 0;
			for (var component : this.getPotionTooltips()) {
				int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 11184810;
				if (component.getString().isEmpty()) {
					height += font.lineHeight;
				} else {
					graphics.drawWordWrap(font, component, x, y + height, WIDTH, color);
				}
				height += font.split(component, WIDTH).size() * font.lineHeight + 1;
			}
		}
		this.drawPotionBar(x + this.getContentXOffset(offs), y + this.getDescriptionHeight(font) + 4, font, graphics);
	}

	private void drawPotionBar(int x, int y, Font font, GuiGraphics graphics) {
		int segmentSplit = this.getWidth(font) / this.maxDoses;
		if (this.component.doses() <= 0) {
			graphics.drawCenteredString(font, Component.translatable("item.twilightforest.flask.empty"), x + 57, y + 3, 16777215);
		} else {
			this.renderPotion(graphics.pose(), x + 1, y + 13, this.component.doses() * segmentSplit - 1, 13, this.component.potion().getColor());
			if (this.component.breakage() > 0) {
				int xPos = x + segmentSplit * (3 - this.component.breakage());
				RenderSystem.enableBlend();
				graphics.fill(xPos, y, xPos + (segmentSplit * this.component.breakage()), y + 13, 0xAA434343);
				RenderSystem.disableBlend();
			}
			int widthProg = segmentSplit;
			for (int i = 1; i < this.maxDoses; i++) {
				graphics.blitSprite(DOSE_SPRITE, x + widthProg, y, 1, 13);
				widthProg += segmentSplit;
			}
		}
		graphics.blitSprite(BORDER_SPRITE, x, y, WIDTH, 13);
	}

	private void renderPotion(PoseStack stack, int xPosition, int yPosition, int desiredWidth, int desiredHeight, int color) {
		int red = (color >> 16) & 255;
		int green = (color >> 8) & 255;
		int blue = color & 255;
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(Fluids.WATER).getStillTexture());
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		int xTileCount = desiredWidth / 16;
		int xRemainder = desiredWidth - (xTileCount * 16);
		int yTileCount = desiredHeight / 16;
		int yRemainder = desiredHeight - (yTileCount * 16);
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(red / 255.0F, green / 255.0F, blue / 255.0F, 1.0F);
		BufferBuilder vertexBuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		Matrix4f matrix4f = stack.last().pose();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int width = (xTile == xTileCount) ? xRemainder : 16;
			if (width == 0) {
				break;
			}
			int x = xPosition + (xTile * 16);
			int maskRight = 16 - width;
			int shiftedX = x + 16 - maskRight;
			float uLocalDif = uDif * maskRight / 16;

			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int height = (yTile == yTileCount) ? yRemainder : 16;
				if (height == 0) {
					break;
				}
				int y = yPosition - ((yTile + 1) * 16);
				int maskTop = 16 - height;
				float vLocalDif = vDif * maskTop / 16;

				vertexBuffer.addVertex(matrix4f, x, y + 16, 0).setUv(uMin + uLocalDif, vMax).setColor(color);
				vertexBuffer.addVertex(matrix4f, shiftedX, y + 16, 0).setUv(uMax, vMax).setColor(color);
				vertexBuffer.addVertex(matrix4f, shiftedX, y + maskTop, 0).setUv(uMax, vMin + vLocalDif).setColor(color);
				vertexBuffer.addVertex(matrix4f, x, y + maskTop, 0).setUv(uMin + uLocalDif, vMin + vLocalDif).setColor(color);
			}
		}
		BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
