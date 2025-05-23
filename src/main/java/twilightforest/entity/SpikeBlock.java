package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.init.TFSounds;

public class SpikeBlock extends BlockChainGoblin.MultipartGenericsAreDumb {
	private final Entity goblin;

	private boolean isCollideBlock;

	//@Override
	//public EntityDimensions getDefaultDimensions(Pose pos) {
	//	return EntityDimensions.scalable(0.75F, 0.75F);
	//}

	public SpikeBlock(Entity goblin) {
		super(goblin);
		this.goblin = goblin;
		this.realSize = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.goblin != null && !this.goblin.isAlive()) {
			this.doFall();
		}
	}

	public void doFall() {
		if (this.onGround() && !this.isCollideBlock) {
			this.playSound(TFSounds.BLOCK_AND_CHAIN_COLLIDE.get(), 0.65F, 0.75F);
			this.gameEvent(GameEvent.HIT_GROUND);
			this.isCollideBlock = true;
		} else {
			this.setDeltaMovement(0.0F, this.getDeltaMovement().y() - 0.04F, 0.0F);
			this.move(MoverType.SELF, this.getDeltaMovement());
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
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
		return this == entity || this.getParent() == entity;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}
}
