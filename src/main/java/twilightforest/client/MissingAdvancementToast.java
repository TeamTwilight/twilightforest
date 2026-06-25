package twilightforest.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;

public class MissingAdvancementToast implements Toast {
        public static final MissingAdvancementToast FALLBACK = new MissingAdvancementToast(Component.translatable("misc.twilightforest.advancement_hidden"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.asItem()));
        private static final Component UPPER_TEXT = Component.translatable("misc.twilightforest.advancement_required");
        private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");

        private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
        private final Component title;
        private final ItemStack icon;

        public MissingAdvancementToast(Component title, ItemStack icon) {
                this.title = title;
                this.icon = icon;
        }

        @Override
        public Visibility getWantedVisibility() {
                return this.wantedVisibility;
        }

        @Override
        public void update(ToastManager manager, long fullyVisibleForMs) {
                this.wantedVisibility = fullyVisibleForMs >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long timer) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
                graphics.item(this.icon, 6, 8);
                graphics.text(font, UPPER_TEXT, 25, 7, 0xffffffff, false);
                graphics.text(font, this.title, 25, 18, 0xffffff, false);
        }
}
