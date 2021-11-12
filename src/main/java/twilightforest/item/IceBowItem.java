package twilightforest.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.entity.projectile.IceArrow;

public class IceBowItem extends BowItem {

	public IceBowItem(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new IceArrow(arrow.level, arrow.getOwner());
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return toRepair.getItem() instanceof BlockItem blockItem && BlockTags.ICE.contains(blockItem.getBlock()) || super.isValidRepairItem(toRepair, repairWith);
	}
}
