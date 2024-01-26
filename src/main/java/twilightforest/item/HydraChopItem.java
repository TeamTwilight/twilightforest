package twilightforest.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.init.TFAdvancements;

public class HydraChopItem extends Item {

	public HydraChopItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
		// if the player is at zero food, achievements
		if (living instanceof ServerPlayer player && player.getFoodData().getFoodLevel() <= 0) {
			TFAdvancements.CONSUME_HYDRA_CHOP.get().trigger(player);
		}
		// then normal effects
		return super.finishUsingItem(stack, level, living);
	}
}