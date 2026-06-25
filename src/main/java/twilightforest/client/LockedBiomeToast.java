package twilightforest.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class LockedBiomeToast implements Toast {

	private static final Component TITLE = Component.translatable("misc.twilightforest.biome_locked");
	private static final Component DESCRIPTION = Component.translatable("misc.twilightforest.biome_locked_2");
	private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");

	private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
	private final ItemStack item;

	public LockedBiomeToast(ItemStack item) {
		this.item = item;
	}

	@Override
	public Visibility getWantedVisibility() {
		return this.wantedVisibility;
	}

	@Override
	public void update(ToastManager manager, long fullyVisibleForMs) {
		this.wantedVisibility = fullyVisibleForMs >= 10000L * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long timer) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
		graphics.item(this.item, 6, 8);
		graphics.text(font, TITLE, 25, 7, 0xffffffff, false);
		graphics.text(font, DESCRIPTION, 25, 18, 0xffffff, false);
	}
}
