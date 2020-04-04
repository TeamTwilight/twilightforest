package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart<T extends Entity & IEntityMultiPart> extends Entity {
	public final T parent;
	public final String partName;

	public MultiPartEntityPart(T parent, String partName, float width, float height) {
		super(parent.getType(), parent.world);
		this.parent = parent;
		this.partName = partName;
		this.size = EntitySize.flexible(width, height);
		this.recalculateSize();
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(CompoundNBT tag) {
	}

	@Override
	protected void writeAdditional(CompoundNBT tag) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && parent.attackEntityFromPart(this, source, amount);
	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || parent == entity;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		// TODO: Per EnderDragonPartEntity this throws an unsupported exception
		throw new UnsupportedOperationException();
	}

	@Override
	public EntitySize getSize(Pose p_213305_1_) {
		return size;
	}

	public void setWidth(float width) {
		setWidthAndHeight(width, size.height);
	}

	public void setHeight(float height) {
		setWidthAndHeight(size.width, height);
	}

	public void setWidthAndHeight(float value) {
		setWidthAndHeight(value, value);
	}

	public void setWidthAndHeight(float width, float height) {
		size = EntitySize.flexible(width, height);
		recalculateSize();
	}
}
