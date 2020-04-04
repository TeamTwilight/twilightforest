package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart<T extends Entity & IEntityMultiPart> extends Entity {
  public final T parent;
  public final String partName;
  private final EntitySize size;

  public MultiPartEntityPart(T parent, String partName, float width, float height) {
    super(parent.getType(), parent.world);
    this.parent = parent;
    this.partName = partName;
    this.size = EntitySize.flexible(width, height);
    this.recalculateSize();
  }

  protected void registerData() {
  }

  protected void readAdditional(CompoundNBT tag) {
  }

  protected void writeAdditional(CompoundNBT tag) {
  }

  public boolean canBeCollidedWith() {
    return true;
  }

  public boolean attackEntityFrom(DamageSource source, float amount) {
    return !this.isInvulnerableTo(source) && parent.attackEntityFromPart(this, source, amount);
  }

  public boolean isEntityEqual(Entity entity) {
    return this == entity || parent == entity;
  }

  public IPacket<?> createSpawnPacket() {
    throw new UnsupportedOperationException();
  }

  public EntitySize getSize(Pose p_213305_1_) {
    return size;
  }
}
