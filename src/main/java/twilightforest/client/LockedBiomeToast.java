package twilightforest.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LockedBiomeToast implements Toast {

	private static final Component TITLE = Component.translatable("misc.twilightforest.biome_locked");
	private static final Component DESCRIPTION = Component.translatable("misc.twilightforest.biome_locked_2");
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");
	private final ItemStack icon;
	private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;

	public LockedBiomeToast(ItemStack icon) {
		this.icon = icon;
	}

	@Override
	public Visibility getWantedVisibility() {
		return this.wantedVisibility;
	}

	@Override
	public void update(ToastManager manager, long timer) {
		this.wantedVisibility = timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

	@Override
	public void render(GuiGraphics graphics, Font font, long timer) {
		graphics.blitSprite(RenderType::guiTextured, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
		graphics.renderFakeItem(this.icon, 6, 8);
		graphics.drawString(font, TITLE, 25, 7, -256, false);
		graphics.drawString(font, DESCRIPTION, 25, 18, 16777215, false);
	}
}
