package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.nbt.CompoundNBT;

public class EntityTFGoblinChain extends EntityTFBlockGoblin.MultipartGenericsAreDumb {

	public EntityTFGoblinChain(Entity parent) {
		super(parent);
		size = EntitySize.flexible(0.75F, 0.75F);
	}

	@Override
	protected void registerData() {

	}

	@Override
	public void tick() {
		super.tick();

		this.ticksExisted++;

		lastTickPosX = getPosX();
		lastTickPosY = getPosY();
		lastTickPosZ = getPosZ();

		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
		}
		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
		}
		for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
		}
		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {

	}
}
