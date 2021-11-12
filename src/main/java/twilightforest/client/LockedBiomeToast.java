package twilightforest.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public record LockedBiomeToast(ItemStack item) implements Toast {

	@Override
	public Visibility render(PoseStack stack, ToastComponent component, long timer) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		component.blit(stack, 0, 0, 0, 0, this.width(), this.height());
		component.getMinecraft().getItemRenderer().renderGuiItem(item, 6, 8);
		component.getMinecraft().font.draw(stack, new TranslatableComponent("twilightforest.ui.biome_locked"), 25.0F, 7.0F, -256);
		component.getMinecraft().font.draw(stack, new TranslatableComponent("twilightforest.ui.biome_locked_2"), 25.0F, 18.0F, 16777215);

		return timer >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}
}
