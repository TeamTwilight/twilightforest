package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTFSpikeBlock extends Entity {

	private EntityTFBlockGoblin goblin;

	public EntityTFSpikeBlock(World world) {
		super(world);
		setSize(0.75F, 0.75F);
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
	public void onUpdate() {
		super.onUpdate();

		this.ticksExisted++;

		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

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
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}
}
