package twilightforest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record LockedBiomeToast(ItemStack item) implements Toast {

	private static final Component TITLE = Component.translatable("misc.twilightforest.biome_locked");
	private static final Component DESCRIPTION = Component.translatable("misc.twilightforest.biome_locked_2");
	private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");

	@Override
	public Visibility render(GuiGraphics graphics, ToastComponent component, long timer) {
		graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
		graphics.renderFakeItem(this.item(), 6, 8);
		graphics.drawString(component.getMinecraft().font, TITLE, 25, 7, -256, false);
		graphics.drawString(component.getMinecraft().font, DESCRIPTION, 25, 18, 16777215, false);

		return timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}
}
