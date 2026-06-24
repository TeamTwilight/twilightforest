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

	private final ItemStack item;
	private Toast.Visibility wantedVisibility = Toast.Visibility.SHOW;

	public LockedBiomeToast(ItemStack item) {
		this.item = item;
	}

	public ItemStack item() {
		return this.item;
	}

	@Override
	public Toast.Visibility getWantedVisibility() {
		return this.wantedVisibility;
	}

	@Override
	public void update(ToastManager toastManager, long timer) {
		if (timer >= 10000L) {
			this.wantedVisibility = Toast.Visibility.HIDE;
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long timer) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

		graphics.fakeItem(this.item(), 6, 8);
		graphics.text(font, TITLE, 25, 7, -256, false);
		graphics.text(font, DESCRIPTION, 25, 18, 16777215, false);
	}
}
