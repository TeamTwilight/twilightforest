package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public class MultiPartEntityPart extends Entity {
	private static final DataParameter<Optional<UUID>> PARENT_UUID = EntityDataManager.createKey(MultiPartEntityPart.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<OptionalInt> PARENT_ID = EntityDataManager.createKey(MultiPartEntityPart.class, DataSerializers.OPTIONAL_VARINT);


	public MultiPartEntityPart(EntityType<? extends MultiPartEntityPart> type, World world) {
		super(type, world);
	}

	public MultiPartEntityPart(EntityType<? extends MultiPartEntityPart> type, World world, Entity entity) {
		super(type, world);
		this.setParentId(entity.getEntityId());
		this.setParentUUID(entity.getUniqueID());
	}


	/*public MultiPartEntityPart(T parent, String partName, float width, float height) {
		super(parent.getType(), parent.world);
		this.setParentId(parent.getUniqueID());
		this.partName = partName;
		this.size = EntitySize.flexible(width, height);
		this.recalculateSize();
	}*/

	@Override
	public void tick() {
		super.tick();
		if (canRemove()) {
			this.remove();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(PARENT_UUID, Optional.empty());
		this.dataManager.register(PARENT_ID, OptionalInt.empty());
	}


	public void setParentUUID(@Nullable UUID id) {
		this.dataManager.set(PARENT_UUID, Optional.ofNullable(id));
	}

	public void setParentId(int id) {
		this.dataManager.set(PARENT_ID, OptionalInt.of(id));
	}

	public boolean canRemove() {
		return (getOwner() == null || !getOwner().isAlive());
	}

	@Nullable
	public Entity getOwner() {
		if (this.dataManager.get(PARENT_UUID).isPresent() && this.world instanceof ServerWorld) {
			return ((ServerWorld) this.world).getEntityByUuid(this.dataManager.get(PARENT_UUID).get());
		} else {
			return this.dataManager.get(PARENT_ID).isPresent() ? this.world.getEntityByID(this.dataManager.get(PARENT_ID).getAsInt()) : null;
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		Entity entity = getOwner();

		return !this.isInvulnerableTo(source) && entity instanceof IEntityMultiPart && ((IEntityMultiPart) entity).attackEntityFromPart(this, source, amount);
	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || getOwner() == entity;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
