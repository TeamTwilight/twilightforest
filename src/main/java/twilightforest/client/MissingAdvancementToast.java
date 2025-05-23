package twilightforest.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;

public class MissingAdvancementToast implements Toast {
	public static final MissingAdvancementToast FALLBACK = new MissingAdvancementToast(Component.translatable("misc.twilightforest.advancement_hidden"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get()));
	private static final Component UPPER_TEXT = Component.translatable("misc.twilightforest.advancement_required");
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");

	private final Component title;
	private final ItemStack icon;
	private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;

	public MissingAdvancementToast(Component title, ItemStack icon) {
		this.title = title;
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
		graphics.drawString(font, UPPER_TEXT, 25, 7, 0xffffffff, false);
		graphics.drawString(font, this.title, 25, 18, 0xffffff, false);
	}
}
