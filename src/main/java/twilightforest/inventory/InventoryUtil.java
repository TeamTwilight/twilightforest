package twilightforest.inventory;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class InventoryUtil {

	public static void giveItemToPlayer(Player player, ItemStack stack) {
		giveItemToPlayer(player, stack, false);
	}

	public static void giveItemToPlayerAndPlaySound(Player player, ItemStack stack) {
		giveItemToPlayer(player, stack, true);
	}

	private static void giveItemToPlayer(Player player, ItemStack stack, boolean playSound) {
		if (player.getInventory().add(stack)) {
			if (playSound) {
				Level level = player.level();
				level.playSound(
					null,
					player.getX(),
					player.getY() + 0.5,
					player.getZ(),
					SoundEvents.ITEM_PICKUP,
					SoundSource.PLAYERS,
					0.2F,
					((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
				);
			}
			return;
		}

		ItemEntity drop = player.drop(stack, false);
		if (drop == null)
			return;
		drop.setNoPickUpDelay();
		drop.setTarget(player.getUUID());
	}
}
