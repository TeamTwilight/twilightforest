package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;

public class ProtectionBox extends Entity {

	public int lifeTime;

	public final int sizeX;
	public final int sizeY;
	public final int sizeZ;

	@Nullable
	private final BoundingBox sbb;

	public ProtectionBox(EntityType<?> type, Level world) {
		super(type, world);
		this.sizeX = this.sizeY = this.sizeZ = 0;
		this.sbb = null;
	}

	public ProtectionBox(Level world, BoundingBox sbb) {
		super(TFEntities.PROTECTION_BOX.get(), world);

		this.sbb = sbb;

		this.moveTo(sbb.minX(), sbb.minY(), sbb.minZ(), 0.0F, 0.0F);

		this.sizeX = sbb.getXSpan();
		this.sizeY = sbb.getYSpan();
		this.sizeZ = sbb.getZSpan();

		this.dimensions = EntityDimensions.fixed(Math.max(this.sizeX, this.sizeZ), this.sizeY);

		this.lifeTime = 60;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.lifeTime <= 1) {
			this.discard();
		} else {
			this.lifeTime--;
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		return false;
	}

	public boolean matches(BoundingBox sbb) {
		return this.sbb.minX() == sbb.minX() && this.sbb.minY() == sbb.minY() && this.sbb.minZ() == sbb.minZ()
			&& this.sbb.maxX() == sbb.maxX() && this.sbb.maxY() == sbb.maxY() && this.sbb.maxZ() == sbb.maxZ();
	}

	public void resetLifetime() {
		this.lifeTime = 60;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}
}
