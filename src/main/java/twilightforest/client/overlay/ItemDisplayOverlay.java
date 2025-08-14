package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.client.overlay.display.ItemDisplay;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.*;

public class ItemDisplayOverlay {

	public static void render(GuiGraphics graphics, Minecraft minecraft, Window window, Gui gui, Player player) {
		if (player == null || gui.getDebugOverlay().showDebugScreen() || minecraft.options.hideGui || !player.getData(TFDataAttachments.TRAVELLERS_GOGGLES_ITEM_DISPLAY)) return;
		ItemStack goggles = player.getItemBySlot(EquipmentSlot.HEAD);
		if (TravellersModifiersManager.isModifierActive(player.registryAccess(), goggles, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER)) {
			var contents = goggles.get(TFDataComponents.ITEM_DISPLAY);
			List<DisplayHolder> typesToRender = new ArrayList<>();
			if (!contents.isEmpty()) {
				int widest = 0;
				graphics.pose().pushPose();
				graphics.pose().translate(TFConfig.itemDisplayXOffs, TFConfig.itemDisplayYOffs, 0.0D);
				graphics.pose().scale((float) TFConfig.itemDisplayScale, (float) TFConfig.itemDisplayScale, (float) TFConfig.itemDisplayScale);
				for (ItemDisplayType type : TFRegistries.ITEM_DISPLAY_TYPE) {
					for (ItemStack stack : contents.items()) {
						if (type.validItems().test(stack)) {
							ItemDisplay display = type.display().get();
							ItemDisplay.Bounds bounds = display.getWidgetSize(stack, minecraft, gui, player, widest);
							widest = Math.max(widest, bounds.width());
							typesToRender.add(new DisplayHolder(stack, display, bounds));
							break;
						}
					}
				}


				if (!typesToRender.isEmpty()) {
					typesToRender.sort(Comparator.comparing(holder -> holder.display().displayPosition()));
					for (DisplayHolder holder : typesToRender) {
						graphics.pose().pushPose();
						holder.display().render(holder.stack(), graphics, minecraft, gui, player, widest);
						//debug fill to see widget sizes
						//graphics.fill(holder.bounds().startX(), holder.bounds().startY(), holder.bounds().startX() + holder.bounds().width(), holder.bounds().startY() + holder.bounds().height(), 0x80FF0000);
						graphics.pose().popPose();
						graphics.pose().translate(0.0F, holder.bounds().height(), 0.0F);
					}
				}
				graphics.pose().popPose();
			}
		}
	}

	public record DisplayHolder(ItemStack stack, ItemDisplay display, ItemDisplay.Bounds bounds) {

	}
}
