package twilightforest.entity.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class TFThrowable extends ThrowableProjectile implements ITFProjectile {

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn) {
		super(type, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, double x, double y, double z) {
		super(type, x, y, z, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, LivingEntity throwerIn) {
		super(type, throwerIn, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
