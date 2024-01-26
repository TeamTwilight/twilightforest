package twilightforest.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;

public record MissingAdvancementToast(Component title, ItemStack icon) implements Toast {
    public static final MissingAdvancementToast FALLBACK = new MissingAdvancementToast(Component.translatable("misc.twilightforest.advancement_hidden"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get()));
    private static final Component UPPER_TEXT = Component.translatable("misc.twilightforest.advancement_required");
    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent component, long timer) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        graphics.renderFakeItem(this.icon(), 6, 8);
        graphics.drawString(component.getMinecraft().font, UPPER_TEXT, 25, 7, 0xffffffff, false);
        graphics.drawString(component.getMinecraft().font, this.title(), 25, 18, 0xffffff, false);

        // 10 seconds as millis
        return timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
