package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityTFSpikeBlock extends Entity {

	private EntityTFBlockGoblin goblin;
	private int deathCounter;

	public EntityTFSpikeBlock(World world) {
		super(TFEntities.blockchain_goblin, world);
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

		if(this.goblin != null && !this.goblin.isAlive() && !this.onGround){
			this.doFall();
		}
	}

	//Fall it self
	public void doFall() {
		this.setMotion(0.0F,this.getMotion().y - 0.04F,0.0F);
		this.move(MoverType.SELF, this.getMotion());
		if(onGround){
			this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.25F, 0.95F);
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
