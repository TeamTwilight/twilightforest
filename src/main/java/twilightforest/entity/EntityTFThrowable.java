package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.world.World;

public abstract class EntityTFThrowable extends ThrowableEntity implements ITFProjectile {

	public EntityTFThrowable(World worldIn) {
		super(worldIn);
	}

	public EntityTFThrowable(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityTFThrowable(World worldIn, LivingEntity throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	public void setThrower(Entity entity) {
		if (entity instanceof LivingEntity) {
			this.thrower = (LivingEntity) entity;
		}
	}
}
