package twilightforest.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.OverlayHandler;

/**
 * Dessine les calques d'interface du mod : Forge 1.21.1 n'a pas d'evenement pour en ajouter
 * (voir {@link OverlayHandler#renderAll}).
 */
@Mixin(Gui.class)
public abstract class GuiMixin {
	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("TAIL"))
	private void twilightforest$renderOverlays(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		OverlayHandler.renderAll((Gui) (Object) this, graphics);
	}
}
