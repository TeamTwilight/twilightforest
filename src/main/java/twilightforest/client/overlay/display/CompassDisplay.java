package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CompassDisplay extends SimpleTextDisplay {

	@Override
	public Component getText(ItemStack item, Minecraft minecraft, Gui gui, Player player) {
		if (item.has(DataComponents.LODESTONE_TRACKER)) {
			var tracker = item.get(DataComponents.LODESTONE_TRACKER);
			if (tracker.tracked() && tracker.target().isPresent() && tracker.target().get().dimension() == player.level().dimension()) {
				return Component.translatable("travellers_gear.modifier.twilightforest.item_display.compass.lodestone", tracker.target().get().pos().toShortString(), player.blockPosition().distManhattan(tracker.target().get().pos()));
			}
		}

		return Component.literal(player.blockPosition().toShortString());
	}
}
