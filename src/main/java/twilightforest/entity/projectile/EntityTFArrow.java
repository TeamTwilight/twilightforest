package twilightforest.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public abstract class EntityTFArrow extends AbstractArrowEntity implements ITFProjectile {

	public EntityTFArrow(EntityType<? extends EntityTFArrow> type, World worldIn) {
		super(type, worldIn);
	}

	public EntityTFArrow(EntityType<? extends EntityTFArrow> type, World worldIn, LivingEntity shooter) {
		super(type, shooter, worldIn);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(Items.ARROW);
	}

	// TODO: These are replaced by the getShooter/setShooter
/*	@Override
	public Entity getThrower() {
		return this.shootingEntity;
	}

	@Override
	public void setThrower(Entity entity) {
		this.shootingEntity = entity;
	}*/
}
