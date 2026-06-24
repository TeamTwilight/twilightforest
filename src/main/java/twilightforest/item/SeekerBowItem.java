package twilightforest.item;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem {

	public SeekerBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return new SeekerArrow(arrow, projectileStack.copyWithCount(1), weaponStack);
	}
}