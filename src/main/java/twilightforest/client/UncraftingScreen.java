package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.network.UncraftingGuiPacket;

public class UncraftingScreen extends AbstractContainerScreen<UncraftingMenu> implements RecipeUpdateListener {
	private static final ResourceLocation TEXTURE = TwilightForestMod.getGuiTexture("guigoblintinkering.png");
	private final RecipeBookComponent recipeBookComponent = new UncraftingRecipeBookComponent();
	private boolean widthTooNarrow;

	public UncraftingScreen(UncraftingMenu container, Inventory player, Component name) {
		super(container, player, name);
	}

	@Override
	protected void init() {
		super.init();

		this.widthTooNarrow = this.width < 379;
		this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
		this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		this.addRenderableWidget(new ImageButton(this.leftPos + 145, this.topPos + 7, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
			this.recipeBookComponent.toggleVisibility();
			this.repositionElements();
		}));
		this.addWidget(this.recipeBookComponent);
		this.setInitialFocus(this.recipeBookComponent);

		this.addRenderableWidget(new CycleButton(this.leftPos + 40, this.topPos + 22, true, button -> {
			PacketDistributor.sendToServer(new UncraftingGuiPacket(0));
			this.menu.unrecipeInCycle++;
			this.menu.slotsChanged(this.menu.tinkerInput);
		}, Component.translatable("container.twilightforest.uncrafting_table.cycle_next_uncraft")));
		this.addRenderableWidget(new CycleButton(this.leftPos + 40, this.topPos + 55, false, button -> {
			PacketDistributor.sendToServer(new UncraftingGuiPacket(1));
			this.menu.unrecipeInCycle--;
			this.menu.slotsChanged(this.menu.tinkerInput);
		}, Component.translatable("container.twilightforest.uncrafting_table.cycle_back_uncraft")));

		if (!TFConfig.disableIngredientSwitching) {
			this.addRenderableWidget(new CycleButtonMini(this.leftPos + 27, this.topPos + 56, true, button -> {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(2));
				this.menu.ingredientsInCycle++;
				this.menu.slotsChanged(this.menu.tinkerInput);
			}, Component.translatable("container.twilightforest.uncrafting_table.cycle_next_ingredient")));
			this.addRenderableWidget(new CycleButtonMini(this.leftPos + 27, this.topPos + 63, false, button -> {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(3));
				this.menu.ingredientsInCycle--;
				this.menu.slotsChanged(this.menu.tinkerInput);
			}, Component.translatable("container.twilightforest.uncrafting_table.cycle_back_ingredient")));
		}

		this.addRenderableWidget(new CycleButton(this.leftPos + 121, this.topPos + 22, true, button -> {
			PacketDistributor.sendToServer(new UncraftingGuiPacket(4));
			this.menu.recipeInCycle++;
			this.menu.slotsChanged(this.menu.assemblyMatrix);
		}, Component.translatable("container.twilightforest.uncrafting_table.cycle_next_recipe")));
		this.addRenderableWidget(new CycleButton(this.leftPos + 121, this.topPos + 55, false, button -> {
			PacketDistributor.sendToServer(new UncraftingGuiPacket(5));
			this.menu.recipeInCycle--;
			this.menu.slotsChanged(this.menu.assemblyMatrix);
		}, Component.translatable("container.twilightforest.uncrafting_table.cycle_back_recipe")));
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.recipeBookComponent.tick();
	}

	@Override
	public boolean mouseScrolled(double x, double y, double vertScroll, double horizScroll) {
		boolean scrolled = super.mouseScrolled(x, y, vertScroll, horizScroll);

		//ingredient buttons
		if (!TFConfig.disableIngredientSwitching) {
			if (x > this.leftPos + 27 && x < this.leftPos + 33 && y > this.topPos + 56 && y < this.topPos + 69) {
				if (vertScroll > 0) {
					PacketDistributor.sendToServer(new UncraftingGuiPacket(2));
					this.menu.ingredientsInCycle++;
				} else {
					PacketDistributor.sendToServer(new UncraftingGuiPacket(3));
					this.menu.ingredientsInCycle--;
				}
				this.menu.slotsChanged(this.menu.tinkerInput);
			}
		}

		//uncrafting recipe buttons
		if (x > this.leftPos + 40 && x < this.leftPos + 54 && y > this.topPos + 22 && y < this.topPos + 64) {
			if (vertScroll > 0) {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(0));
				this.menu.unrecipeInCycle++;
			} else {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(1));
				this.menu.unrecipeInCycle--;
			}
			this.menu.slotsChanged(this.menu.tinkerInput);
		}

		//recrafting recipe buttons
		if (x > this.leftPos + 121 && x < this.leftPos + 135 && y > this.topPos + 22 && y < this.topPos + 64) {
			if (vertScroll > 0) {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(4));
				this.menu.recipeInCycle++;
			} else {
				PacketDistributor.sendToServer(new UncraftingGuiPacket(5));
				this.menu.recipeInCycle--;
			}
			this.menu.slotsChanged(this.menu.assemblyMatrix);
		}

		return scrolled;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.renderBackground(graphics, mouseX, mouseY, partialTicks);
			this.recipeBookComponent.render(graphics, mouseX, mouseY, partialTicks);
		} else {
			super.render(graphics, mouseX, mouseY, partialTicks);
			this.recipeBookComponent.render(graphics, mouseX, mouseY, partialTicks);
			this.recipeBookComponent.renderGhostRecipe(graphics, this.leftPos, this.topPos, true, partialTicks);
		}

		this.renderTooltip(graphics, mouseX, mouseY);
		this.recipeBookComponent.renderTooltip(graphics, this.leftPos, this.topPos, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(this.font, this.title, 6, 6, 4210752, false);
		if (TFConfig.disableUncraftingOnly) {
			graphics.drawString(this.font, Component.translatable("container.twilightforest.uncrafting_table.uncrafting_disabled").withStyle(ChatFormatting.DARK_RED), 6, this.imageHeight - 96 + 2, 4210752, false);
		} else {
			graphics.drawString(this.font, I18n.get("container.inventory"), 7, this.imageHeight - 96 + 2, 4210752, false);
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		int frameX = this.leftPos;
		int frameY = (this.height - this.imageHeight) / 2;
		graphics.blit(TEXTURE, frameX, frameY, 0, 0, this.imageWidth, this.imageHeight);

		UncraftingMenu tfContainer = this.menu;

		// show uncrafting ingredients as background
		graphics.pose().pushPose();
		graphics.pose().translate(this.leftPos, this.topPos, 0);

		for (int i = 0; i < 9; i++) {
			Slot uncrafting = tfContainer.getSlot(2 + i);
			Slot assembly = tfContainer.getSlot(11 + i);

			if (uncrafting.hasItem()) {
				this.drawSlotAsBackground(graphics, uncrafting, assembly);
			}
		}
		graphics.pose().popPose();

		int costVal = tfContainer.getUncraftingCost();
		if (costVal > 0) {
			int color;
			String cost = "" + costVal;
			if (this.minecraft.player.experienceLevel < costVal && !this.minecraft.player.getAbilities().instabuild) {
				color = 0xA00000;
			} else {
				color = 0x80FF20;
			}
			graphics.drawString(this.font, cost, frameX + 48 - this.font.width(cost), frameY + 38, color);
		}

		costVal = tfContainer.getRecraftingCost();
		if (costVal > 0) {
			int color;
			String cost = "" + costVal;
			if (this.minecraft.player.experienceLevel < costVal && !this.minecraft.player.getAbilities().instabuild) {
				color = 0xA00000;
			} else {
				color = 0x80FF20;
			}
			graphics.drawString(this.font, cost, frameX + 130 - this.font.width(cost), frameY + 38, color);
		}
	}

	@Override
	protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
		return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
			this.setFocused(this.recipeBookComponent);
			return true;
		} else {
			return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
		return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop, mouseButton);
	}

	private void drawSlotAsBackground(GuiGraphics graphics, Slot backgroundSlot, Slot appearSlot) {

		int screenX = appearSlot.x;
		int screenY = appearSlot.y;
		ItemStack itemStackToRender = backgroundSlot.getItem();

		graphics.renderFakeItem(itemStackToRender, screenX, screenY);

		boolean itemBroken = UncraftingMenu.isMarked(itemStackToRender);

		// draw 50% gray rectangle over the item
		RenderSystem.disableDepthTest();
		graphics.pose().pushPose();
		graphics.pose().translate(0.0D, 0.0D, 200.0D);
		graphics.fill(appearSlot.x, appearSlot.y, appearSlot.x + 16, appearSlot.y + 16, itemBroken ? 0x80FF8b8b : 0x9f8b8b8b);
		graphics.pose().popPose();
		RenderSystem.enableDepthTest();
	}

	@Override
	protected void renderTooltip(GuiGraphics graphics, int pX, int pY) {
		UncraftingMenu container = this.menu;

		for (int i = 0; i < 9; i++) {
			if (container.getCarried().isEmpty() && container.slots.get(2 + i).hasItem() && this.hoveredSlot == container.slots.get(11 + i) && !container.slots.get(11 + i).hasItem()) {
				graphics.renderTooltip(this.font, container.slots.get(2 + i).getItem(), pX, pY);
			}
		}

		//check if we're hovering over a banned uncraftable item
		if (container.slots.getFirst().hasItem() && container.slots.getFirst().getItem().is(ItemTagGenerator.BANNED_UNCRAFTABLES) && container.slots.getFirst().equals(this.hoveredSlot)) {
			graphics.renderTooltip(this.font, Component.translatable("container.twilightforest.uncrafting_table.disabled_item").withStyle(ChatFormatting.RED), pX, pY);
		} else {
			super.renderTooltip(graphics, pX, pY);
		}
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
		super.slotClicked(slot, slotId, mouseButton, type);
		this.recipeBookComponent.slotClicked(slot);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookComponent.recipesUpdated();
	}

	@Override
	public RecipeBookComponent getRecipeBookComponent() {
		return this.recipeBookComponent;
	}

	private static class CycleButton extends Button {
		private final boolean up;

		CycleButton(int x, int y, boolean up, OnPress onClick, MutableComponent tooltip) {
			super(x, y, 14, 9, Component.empty(), onClick, message -> Component.empty());
			this.up = up;
			this.setTooltip(Tooltip.create(tooltip));
		}

		@Override
		public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

				int textureX = 176;
				int textureY = 0;

				if (this.isHovered) textureX += this.width;

				// what's up
				if (!this.up) textureY += this.height;

				graphics.blit(TEXTURE, this.getX(), this.getY(), textureX, textureY, this.width, this.height);
			}
		}
	}

	private static class CycleButtonMini extends Button {
		private final boolean up;

		CycleButtonMini(int x, int y, boolean up, OnPress onClick, MutableComponent tooltip) {
			super(x, y, 8, 6, Component.empty(), onClick, message -> Component.empty());
			this.up = up;
			this.setTooltip(Tooltip.create(tooltip));
		}

		@Override
		public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

				int textureX = 176;
				int textureY = 41;

				if (this.isHovered) textureX += this.width;

				// what's up
				if (!this.up) textureY += this.height;

				graphics.blit(TEXTURE, this.getX(), this.getY(), textureX, textureY, this.width, this.height);
			}
		}
	}
}
