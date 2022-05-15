package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import twilightforest.TFSounds;
import twilightforest.entity.monster.BlockChainGoblin;

public class SpikeBlock extends BlockChainGoblin.MultipartGenericsAreDumb {
	private Entity goblin;

	private boolean isCollideBlock;

	@Override
	public EntityDimensions getDimensions(Pose pos) {
		return EntityDimensions.scalable(0.75F, 0.75F);
	}

	public SpikeBlock(Entity goblin) {
		super(goblin);
		this.goblin = goblin;
		realSize = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.goblin != null && !this.goblin.isAlive()) {
			this.doFall();
		}
	}

	public void doFall() {
		if (this.onGround && !this.isCollideBlock) {
			this.playSound(TFSounds.BLOCKCHAIN_COLLIDE, 0.65F, 0.75F);
			this.isCollideBlock = true;
		} else {
			this.setDeltaMovement(0.0F, this.getDeltaMovement().y - 0.04F, 0.0F);
			this.move(MoverType.SELF, this.getDeltaMovement());
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || getParent() == entity;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}
}
