package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.overlay.display.ItemDisplay;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.ItemDisplays;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemDisplayOverlay {
	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Window window, Gui gui, Player player) {
		if (player == null || gui.getDebugOverlay().showDebugScreen() || minecraft.options.hideGui)
			return;

		ItemStack goggles = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!TravellersModifiersManager.isModifierActive(player, goggles, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
			return;

		ItemDisplayContents contents = goggles.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null || contents.isEmpty())
			return;

		List<DisplayHolder> typesToRender = new ArrayList<>();
		int widest = fillDisplayHolders(typesToRender, contents, minecraft, gui, player);
		if (typesToRender.isEmpty())
			return;

		renderHolders(graphics, minecraft, gui, player, typesToRender, widest);
	}

	private static void renderHolders(GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, List<DisplayHolder> typesToRender, int widest) {
		typesToRender.sort(Comparator.comparing(holder -> holder.display().displayPosition()));
		for (DisplayHolder holder : typesToRender) {
			holder.display().render(holder.stack(), graphics, minecraft, gui, player, widest);
		}
	}

	private static int fillDisplayHolders(List<DisplayHolder> typesToRender, ItemDisplayContents contents, Minecraft minecraft, Gui gui, Player player) {
		int widest = 0;

		NonNullList<ItemStack> items = contents.items();
		int slots = Math.min(ItemDisplayContents.LAYOUT.size(), items.size());
		int activeMapSlot = contents.findActiveMapSlot();

		for (int i = 0; i < slots; i++) {
			ItemStack stack = items.get(i);
			if (stack.isEmpty()) continue;

			ItemDisplayType type = ItemDisplayContents.LAYOUT.get(i).get();
			if (type == ItemDisplays.MAP.get() && i != activeMapSlot)
				continue;

			if (!ItemDisplayContents.LAYOUT.get(i).get().validItems().test(stack))
				continue;

			ItemDisplay display = type.display().get();
			ItemDisplay.Bounds bounds = display.getWidgetSize(stack, minecraft, gui, player, widest);
			widest = Math.max(widest, bounds.width());
			typesToRender.add(new DisplayHolder(stack, display, bounds));
		}
		return widest;
	}

	public record DisplayHolder(ItemStack stack, ItemDisplay display, ItemDisplay.Bounds bounds) {

	}
}
