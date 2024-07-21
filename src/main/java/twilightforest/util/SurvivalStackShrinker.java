package twilightforest.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.beans.Component;

@Component
public class SurvivalStackShrinker {

	public void shrink(ItemStack stack, Player player, int decrement) {
		if (!player.isCreative())
			stack.shrink(decrement);
	}

}
