package twilightforest.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.world.World;

public abstract class EntityTFThrowable extends ThrowableEntity implements ITFProjectile {

	public EntityTFThrowable(EntityType<? extends EntityTFThrowable> type, World worldIn) {
		super(type, worldIn);
	}

	public EntityTFThrowable(EntityType<? extends EntityTFThrowable> type, World worldIn, double x, double y, double z) {
		super(type, x, y, z, worldIn);
	}

	public EntityTFThrowable(EntityType<? extends EntityTFThrowable> type, World worldIn, LivingEntity throwerIn) {
		super(type, throwerIn, worldIn);
	}

	// TODO: Isn't actually an override
	public void setThrower(Entity entity) {
		if (entity instanceof LivingEntity) {
			this.owner = (LivingEntity) entity;
		}
	}
}
