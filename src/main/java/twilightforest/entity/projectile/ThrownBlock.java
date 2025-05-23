package twilightforest.entity.projectile;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.monster.Troll;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

public class ThrownBlock extends TFThrowable {

	private BlockState state = Blocks.STONE.defaultBlockState();

	public ThrownBlock(EntityType<? extends TFThrowable> type, Level worldIn) {
		super(type, worldIn);
	}

	public ThrownBlock(Level world, LivingEntity thrower, @Nullable BlockState state) {
		super(TFEntities.THROWN_BLOCK.get(), world, thrower);
		if (state != null) {
			this.state = state;
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("BlockState", NbtUtils.writeBlockState(this.state));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.state = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("BlockState"));
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == EntityEvent.DEATH) {
			ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, this.state);
			for (int i = 0; i < 20; i++) {
				this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, this.random.nextDouble() * 0.2D, this.random.nextGaussian() * 0.05D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected boolean canHitEntity(Entity target) {
		return !(target instanceof Troll) && super.canHitEntity(target);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LivingEntity living && this.level() instanceof ServerLevel level && living.hurtServer(level, level.damageSources().source(TFDamageTypes.THROWN_BLOCK), 6)) {
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide()) {
			this.level().broadcastEntityEvent(this, (byte) 3);
			this.gameEvent(GameEvent.BLOCK_DESTROY, this.getOwner());
			this.discard();
		}
	}

	public BlockState getBlockState() {
		return this.state;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, entity, Block.getId(this.getBlockState()));
	}

	@Override
	public Component getTypeName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.state = Block.stateById(packet.getData());
	}
}
