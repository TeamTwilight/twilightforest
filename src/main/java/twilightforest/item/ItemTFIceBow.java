package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import twilightforest.entity.projectile.EntityIceArrow;

public class ItemTFIceBow extends BowItem {

	public ItemTFIceBow(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
		if (arrow.func_234616_v_() instanceof LivingEntity) {
			return new EntityIceArrow(arrow.world, (LivingEntity) arrow.func_234616_v_());
		}
		return arrow;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repairWith) {
		return repairWith.getItem() == Blocks.ICE.asItem() || super.getIsRepairable(toRepair, repairWith);
	}
}
