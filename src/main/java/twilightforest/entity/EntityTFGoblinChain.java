package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityTFGoblinChain extends MultiPartEntityPart {
	public EntityTFGoblinChain(EntityType<? extends EntityTFGoblinChain> type, World world) {
		super(type, world);
	}

	public EntityTFGoblinChain(World world, Entity entity) {
		super(TFEntities.goblin_chain, world, entity);
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
}
