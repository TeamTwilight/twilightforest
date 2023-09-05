package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;

public record MissingAdvancementToast(Component title, ItemStack icon) implements Toast {
    public static final MissingAdvancementToast FALLBACK = new MissingAdvancementToast(Component.translatable("misc.twilightforest.advancement_hidden"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get()));
    private static final Component UPPER_TEXT = Component.translatable("misc.twilightforest.advancement_required");

    @Override
    public Toast.Visibility render(PoseStack stack, ToastComponent component, long timer) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiComponent.blit(stack, 0, 0, 0, 0, this.width(), this.height());
        component.getMinecraft().getItemRenderer().renderGuiItem(stack, this.icon(), 6, 8);
        component.getMinecraft().font.draw(stack, UPPER_TEXT, 25.0F, 7.0F, 0xffffffff);
        component.getMinecraft().font.draw(stack, this.title(), 25.0F, 18.0F, 0xffffff);

        // 10 seconds as millis
        return timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
