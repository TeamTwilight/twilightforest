package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import twilightforest.entity.projectile.EntitySeekerArrow;

public class ItemTFSeekerBow extends ItemTFBowBase {

	public ItemTFSeekerBow(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
		//TODO: Must be UUID
		if (arrow.shootingEntity instanceof LivingEntity) {
			return new EntitySeekerArrow(arrow.world, (LivingEntity) arrow.shootingEntity);
		}
		return arrow;
	}
}
