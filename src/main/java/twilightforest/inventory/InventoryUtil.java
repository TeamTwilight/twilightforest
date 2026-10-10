package twilightforest.inventory;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InventoryUtil {
	public static void giveItemToPlayer(Player player, ItemStack stack) {
		if (player.getInventory().add(stack)) {
			player.level().playSound(
				null,
				player.getX(),
				player.getY() + 0.5,
				player.getZ(),
				SoundEvents.ITEM_PICKUP,
				SoundSource.PLAYERS,
				0.2F,
				((player.level().getRandom().nextFloat()
					- player.level().getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
			);
			return;
		}

		ItemEntity drop = player.drop(stack, false);
		if (drop == null)
			return;
		drop.setNoPickUpDelay();
		drop.setTarget(player.getUUID());
	}
}
