package twilightforest.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import twilightforest.entity.monster.BlockChainGoblin;

public class SpikeBlock extends BlockChainGoblin.MultipartGenericsAreDumb {

	@Override
	public EntityDimensions getDimensions(Pose pos) {
		return EntityDimensions.scalable(0.75F, 0.75F);
	}

	public SpikeBlock(Entity goblin) {
		super(goblin);
		realSize = EntityDimensions.scalable(0.75F, 0.75F);
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
