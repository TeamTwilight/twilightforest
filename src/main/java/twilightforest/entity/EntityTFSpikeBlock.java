package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTFSpikeBlock extends Entity {

	private EntityTFBlockGoblin goblin;

	public EntityTFSpikeBlock(World world) {
		super(TFEntities.blockchain_goblin.get(), world);
	}

	@Override
	public EntitySize getSize(Pose pos) {
		return EntitySize.flexible(0.75F, 0.75F);
	}

	public EntityTFSpikeBlock(EntityTFBlockGoblin goblin) {
		this(goblin.getWorld());
		this.goblin = goblin;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
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

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || this.goblin == entity;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
	}
}
