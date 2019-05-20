package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;

public abstract class EntityTFThrowable extends EntityThrowable implements ITFProjectile {

	public EntityTFThrowable(World worldIn) {
		super(worldIn);
	}

	public EntityTFThrowable(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityTFThrowable(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	public void setThrower(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			this.thrower = (EntityLivingBase) entity;
		}
	}
}
