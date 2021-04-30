package twilightforest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.inventory.ContainerTFUncrafting;
import twilightforest.network.PacketUncraftingGui;
import twilightforest.network.TFPacketHandler;

public class GuiTFGoblinCrafting extends ContainerScreen<ContainerTFUncrafting> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getGuiTexture("guigoblintinkering.png");

	public GuiTFGoblinCrafting(ContainerTFUncrafting container, PlayerInventory player, ITextComponent name) {
		super(container, player, name);
	}

	@Override
	protected void init() {
		super.init();

		this.buttons.add(new CycleButton(guiLeft + 40, guiTop + 22, true, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(0));
			container.unrecipeInCycle++;
			container.onCraftMatrixChanged(container.tinkerInput);
		}));
		this.buttons.add(new CycleButton(guiLeft + 40, guiTop + 55, false, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(1));
			container.unrecipeInCycle--;
			container.onCraftMatrixChanged(container.tinkerInput);
		}));

		//this.buttonList.add(new ModeButton(uiLeft + 7, guiTop + 57));

		this.buttons.add(new CycleButtonMini(guiLeft + 27, guiTop + 56, true, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(2));
			container.ingredientsInCycle++;
			container.onCraftMatrixChanged(container.tinkerInput);
		}));
		this.buttons.add(new CycleButtonMini(guiLeft + 27, guiTop + 63, false, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(3));
			container.ingredientsInCycle--;
			container.onCraftMatrixChanged(container.tinkerInput);
		}));

		//this.buttonList.add(new RefreshButton(uiLeft + 26, guiTop + 57));

		this.buttons.add(new CycleButton(guiLeft + 121, guiTop + 22, true, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(4));
			container.unrecipeInCycle++;
			container.onCraftMatrixChanged(container.assemblyMatrix);
		}));
		this.buttons.add(new CycleButton(guiLeft + 121, guiTop + 55, false, button -> {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(5));
			container.unrecipeInCycle--;
			container.onCraftMatrixChanged(container.assemblyMatrix);
		}));
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(ms, mouseX, mouseY); //renderHoveredToolTip
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		this.font.drawString(ms, I18n.format(TFBlocks.uncrafting_table.get().getTranslationKey()), 8, 6, 4210752);
		this.font.drawString(ms, I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1, 1, 1, 1);
		this.minecraft.getTextureManager().bindTexture(textureLoc);
		int frameX = (this.width - this.xSize) / 2;
		int frameY = (this.height - this.ySize) / 2;
		this.blit(ms, frameX, frameY, 0, 0, this.xSize, this.ySize);

		ContainerTFUncrafting tfContainer = this.container;

		// show uncrafting ingredients as background
		ms.push();
		ms.translate(guiLeft, guiTop, 0);

		for (int i = 0; i < 9; i++) {
			Slot uncrafting = tfContainer.getSlot(2 + i);
			Slot assembly = tfContainer.getSlot(11 + i);

			if (uncrafting.getHasStack()) {
				drawSlotAsBackground(ms, uncrafting, assembly);
			}
		}
		ms.pop();

		// show the costs if there are any
		FontRenderer fontRendererObj = this.minecraft.fontRenderer;
		RenderHelper.disableStandardItemLighting();

		int costVal = tfContainer.getUncraftingCost();
		if (costVal > 0) {
			if (this.minecraft.player.experienceLevel < costVal && !this.minecraft.player.abilities.isCreativeMode) {
				int color = 0xA00000;
				String cost = "" + costVal;
				fontRendererObj.drawStringWithShadow(ms, cost, frameX + 48 - fontRendererObj.getStringWidth(cost), frameY + 38, color);
			} else {
				int color = 0x80FF20;
				String cost = "" + costVal;
				fontRendererObj.drawStringWithShadow(ms, cost, frameX + 48 - fontRendererObj.getStringWidth(cost), frameY + 38, color);
			}
		}

		costVal = tfContainer.getRecraftingCost();
		if (costVal > 0) {
			if (this.minecraft.player.experienceLevel < costVal && !this.minecraft.player.abilities.isCreativeMode) {
				int color = 0xA00000;
				String cost = "" + costVal;
				fontRendererObj.drawStringWithShadow(ms, cost, frameX + 130 - fontRendererObj.getStringWidth(cost), frameY + 38, color);
			} else {
				int color = 0x80FF20;
				String cost = "" + costVal;
				fontRendererObj.drawStringWithShadow(ms, cost, frameX + 130 - fontRendererObj.getStringWidth(cost), frameY + 38, color);
			}
		}
	}

	private void drawSlotAsBackground(MatrixStack ms, Slot backgroundSlot, Slot appearSlot) {

		int screenX = appearSlot.xPos + guiLeft;
		int screenY = appearSlot.yPos + guiTop;
		ItemStack itemStackToRender = backgroundSlot.getStack();
		itemRenderer.zLevel = 50.0F;

        itemRenderer.renderItemIntoGUI(itemStackToRender, screenX, screenY);
        itemRenderer.renderItemOverlayIntoGUI(this.font, itemStackToRender, screenX, screenY, "");

		boolean itemBroken = ContainerTFUncrafting.isMarked(itemStackToRender);

		// draw 50% gray rectangle over the item
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		AbstractGui.fill(ms, appearSlot.xPos, appearSlot.yPos, appearSlot.xPos + 16, appearSlot.yPos + 16, itemBroken ? 0x80FF8b8b : 0x9f8b8b8b);
		RenderSystem.enableLighting();
		RenderSystem.enableDepthTest();

        itemRenderer.zLevel = 0.0F;
	}

	private static class CycleButton extends Button {
		private final boolean up;

		CycleButton(int x, int y, boolean up, IPressable onClick) {
			super(x, y, 14, 9, StringTextComponent.EMPTY, onClick);
			this.up = up;
		}

		@Override
		public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft.getInstance().getTextureManager().bindTexture(GuiTFGoblinCrafting.textureLoc);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

				int textureX = 176;
				int textureY = 0;

				if (this.isHovered) textureX += this.width;

				// what's up
				if (!this.up) textureY += this.height;

				this.blit(ms, this.x, this.y, textureX, textureY, this.width, this.height);
			}
		}
	}

//	private static class ModeButton extends GuiButton {
//		ModeButton(int buttonId, int x, int y) {
//			super(buttonId, x, y, 18, 12, "");
//		}

//		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//			if (this.visible) {
//				mc.getTextureManager().bindTexture(GuiTFGoblinCrafting.textureLoc);
//				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

//				int textureX = 176;
//				int textureY = 18;

//				if (this.hovered) textureX += this.width;

//				this.drawTexturedModalRect(this.x, this.y, textureX, textureY, this.width, this.height);
//			}
//		}
//	}

	private static class CycleButtonMini extends Button {
		private final boolean up;

		CycleButtonMini(int x, int y, boolean up, IPressable onClick) {
			super(x, y, 8, 6, StringTextComponent.EMPTY, onClick);
			this.up = up;
		}

		@Override
		public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft.getInstance().getTextureManager().bindTexture(GuiTFGoblinCrafting.textureLoc);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

				int textureX = 176;
				int textureY = 41;

				if (this.isHovered) textureX += this.width;

				// what's up
				if (!this.up) textureY += this.height;

				this.blit(ms, this.x, this.y, textureX, textureY, this.width, this.height);
			}
		}

		@Override
		public void onPress() {
			TFPacketHandler.CHANNEL.sendToServer(new PacketUncraftingGui(this.up ? 2 : 3));
		}
	}

//	static class RefreshButton extends GuiButton {
//		RefreshButton(int buttonId, int x, int y) {
//			super(buttonId, x, y, 8, 6, "");
//		}

//		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//			if (this.visible) {
//				mc.getTextureManager().bindTexture(GuiTFGoblinCrafting.textureLoc);
//				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

//				int textureX = 176;
//				int textureY = 30;

//				if (this.hovered) textureX += this.width;

//				this.drawTexturedModalRect(this.x, this.y, textureX, textureY, this.width, this.height);
//			}
//		}
//	}
}
