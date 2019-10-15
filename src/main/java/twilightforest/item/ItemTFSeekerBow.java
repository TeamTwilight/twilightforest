package twilightforest.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import twilightforest.entity.EntitySeekerArrow;

public class ItemTFSeekerBow extends ItemTFBowBase {

	public ItemTFSeekerBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow) {
		if (arrow.shootingEntity instanceof EntityLivingBase) {
			return new EntitySeekerArrow(arrow.world, (EntityLivingBase) arrow.shootingEntity);
		}
		return arrow;
	}
}
