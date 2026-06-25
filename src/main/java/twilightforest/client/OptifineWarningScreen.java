package twilightforest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class OptifineWarningScreen extends Screen {

	private final Screen lastScreen;
	private int ticksUntilEnable = 20 * 10;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private MultiLineLabel suggestions = MultiLineLabel.EMPTY;
	private static final Component text = Component.translatable("gui.twilightforest.optifine.message");
	private static final MutableComponent url = Component.translatable("gui.twilightforest.optifine.suggestions").withStyle(style -> style.withColor(ChatFormatting.GREEN).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/NordicGamerFE/usefulmods"))));
	private Button exitButton;

	public OptifineWarningScreen(Screen screen) {
		super(Component.translatable("gui.twilightforest.optifine.title"));
		this.lastScreen = screen;
	}

	@Override
	public Component getNarrationMessage() {
		return CommonComponents.joinForNarration(super.getNarrationMessage(), text);
	}

	@Override
	protected void init() {
		super.init();
		this.exitButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_PROCEED, (pressed) -> Minecraft.getInstance().setScreen(this.lastScreen)).bounds(this.width / 2 - 75, this.height * 3 / 4, 150, 20).build());
		this.exitButton.active = false;

		this.message = MultiLineLabel.create(this.font, text, this.width - 50);
		this.suggestions = MultiLineLabel.create(this.font, url, this.width - 50);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		// TODO: 26.1.2 - drawCenteredString no longer exists, manually center
		int titleWidth = this.font.width(this.title);
		graphics.text(this.font, this.title, this.width / 2 - titleWidth / 2, 30, 16777215);
		// TODO: 26.1.2 - renderCentered removed from MultiLineLabel; use visitLines with TextAlignment.CENTER
		this.message.visitLines(TextAlignment.CENTER, this.width / 2, 70, this.font.lineHeight, graphics.textRenderer());
		this.suggestions.visitLines(TextAlignment.CENTER, this.width / 2, 160, this.font.lineHeight, graphics.textRenderer());
		super.extractRenderState(graphics, mouseX, mouseY, partialTicks);

		this.exitButton.extractRenderState(graphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(graphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (--this.ticksUntilEnable <= 0) {
			this.exitButton.active = true;
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
	public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
		return super.mouseClicked(event, doubleClick);
	}
}
