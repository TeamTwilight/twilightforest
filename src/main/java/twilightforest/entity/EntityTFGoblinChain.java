package twilightforest.entity;

import net.minecraft.entity.Entity;

public class EntityTFGoblinChain extends MultiPartEntityPart {
	public EntityTFGoblinChain(Entity goblin) {
		super(goblin, "chain", 0.1F, 0.1F);
	}

	@Override
	public void tick() {
		super.tick();

		this.ticksExisted++;

		lastTickPosX = getX();
		lastTickPosY = getY();
		lastTickPosZ = getZ();

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
}
