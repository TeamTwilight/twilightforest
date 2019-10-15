package twilightforest.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import twilightforest.entity.EntityIceArrow;

public class ItemTFIceBow extends ItemTFBowBase {

	public ItemTFIceBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow) {
		if (arrow.shootingEntity instanceof EntityLivingBase) {
			return new EntityIceArrow(arrow.world, (EntityLivingBase) arrow.shootingEntity);
		}
		return arrow;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repairWith) {
		return repairWith.getItem() == Item.getItemFromBlock(Blocks.ICE) || super.getIsRepairable(toRepair, repairWith);
	}
}
