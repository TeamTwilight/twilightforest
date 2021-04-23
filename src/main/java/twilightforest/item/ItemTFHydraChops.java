package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import twilightforest.advancements.TFAdvancements;

public class ItemTFHydraChops extends Item {

	public ItemTFHydraChops(Properties props) {
		super(props);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, LivingEntity living) {
		// if the player is at zero food, achievements
		if (living instanceof ServerPlayerEntity && ((ServerPlayerEntity) living).getFoodStats().getFoodLevel() <= 0) {
			TFAdvancements.CONSUME_HYDRA_CHOP.trigger((ServerPlayerEntity) living);
		}
		// then normal effects
		return super.onItemUseFinish(itemStack, world, living);
	}
}
