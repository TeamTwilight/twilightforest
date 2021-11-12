package twilightforest.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

public class OptifineWarningScreen extends Screen {

	private final Screen lastScreen;
	private int ticksUntilEnable = 20 * 10;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private MultiLineLabel suggestions = MultiLineLabel.EMPTY;
	private static final Component text = new TranslatableComponent(TwilightForestMod.ID + ".gui.optifine.message");
	private static final MutableComponent url;
	private final List<Button> exitButton = Lists.newArrayList();

	static {
		(url = new TranslatableComponent(TwilightForestMod.ID + ".gui.optifine.suggestions"))
				.withStyle(style -> style.withColor(ChatFormatting.GREEN).setUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gist.github.com/alkyaly/02830c560d15256855bc529e1e232e88")));
	}

	protected OptifineWarningScreen(Screen screen) {
		super(new TranslatableComponent(TwilightForestMod.ID + ".gui.optifine.title"));
		this.lastScreen = screen;
	}

	@Override
	public Component getNarrationMessage() {
		return CommonComponents.joinForNarration(super.getNarrationMessage(), text);
	}

	@Override
	protected void init() {
		super.init();
		this.exitButton.clear();
		this.exitButton.add(new Button(this.width / 2 - 75, this.height * 3 / 4, 150, 20, CommonComponents.GUI_PROCEED, (p_213002_1_) -> Minecraft.getInstance().setScreen(lastScreen)));

		for(Button button : this.exitButton) {
			button.active = false;
		}

		this.message = MultiLineLabel.create(this.font, text, this.width - 50);
		this.suggestions = MultiLineLabel.create(this.font, url, this.width - 50);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 30, 16777215);
		this.message.renderCentered(matrixStack, this.width / 2, 70);
		this.suggestions.renderCentered(matrixStack, this.width / 2, 160);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		for(Button button : this.exitButton) {
			button.render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (--this.ticksUntilEnable == 0) {
			for(Button button : this.exitButton) {
				button.active = true;
			}
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return this.ticksUntilEnable <= 0;
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(this.lastScreen);
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (pMouseY > 160 && pMouseY < 170) {
			Style style = this.getClickedComponentStyleAt((int) pMouseX);
			if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
				this.handleComponentClicked(style);
				return false;
			}
		}

		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Nullable
	private Style getClickedComponentStyleAt(int xPos) {
		int wid = Minecraft.getInstance().font.width(url);
		int left = this.width / 2 - wid / 2;
		int right = this.width / 2 + wid / 2;
		return xPos >= left && xPos <= right ? Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(url, xPos - left) : null;
	}
}
