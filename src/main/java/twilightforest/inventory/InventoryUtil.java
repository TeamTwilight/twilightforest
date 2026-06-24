package twilightforest.inventory;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InventoryUtil {
	public static void giveItemToPlayer(Player player, ItemStack stack) {
		if (player.getInventory().add(stack))
			return;

		ItemEntity drop = player.drop(stack, false);
		if (drop == null)
			return;
		drop.setNoPickUpDelay();
		drop.setTarget(player.getUUID());
	}
}
