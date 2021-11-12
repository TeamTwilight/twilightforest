package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import twilightforest.block.TFBlocks;

public record MissingAdvancementToast(Component title, ItemStack icon) implements Toast {
    public static final MissingAdvancementToast FALLBACK = new MissingAdvancementToast(new TranslatableComponent(".ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get()));
    private static final TranslatableComponent UPPER_TEXT = new TranslatableComponent("twilightforest.ui.advancement.required");

    @Override
    public Toast.Visibility render(PoseStack stack, ToastComponent component, long timer) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        component.blit(stack, 0, 0, 0, 0, this.width(), this.height());
        component.getMinecraft().getItemRenderer().renderGuiItem(this.icon(), 6, 8);
        component.getMinecraft().font.draw(stack, UPPER_TEXT, 25.0F, 7.0F, 0xffffffff);
        component.getMinecraft().font.draw(stack, this.title(), 25.0F, 18.0F, 0xffffff);

        // 10 seconds as millis
        return timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
