package twilightforest.block.entity.spawner;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBlockEntities;

import java.util.List;

public class SinisterSpawnerBlockEntity extends BlockEntity implements Spawner {
	private final SinisterSpawnerLogic spawner = new SinisterSpawnerLogic() {
		@Override
		public Either<BlockEntity, Entity> getOwner() {
			return Either.left(SinisterSpawnerBlockEntity.this);
		}
	};
	@Nullable private ResourceKey<LootTable> lootTable = null;

	public SinisterSpawnerBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.SINISTER_SPAWNER.value(), pos, blockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.spawner.load(this.level, this.worldPosition, input);
		this.lootTable = input.read("LootTable", ResourceKey.codec(Registries.LOOT_TABLE)).orElse(null);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		this.spawner.save(output);
		output.storeNullable("LootTable", ResourceKey.codec(Registries.LOOT_TABLE), this.lootTable);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, SinisterSpawnerBlockEntity blockEntity) {
		blockEntity.spawner.clientTick(level, pos);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SinisterSpawnerBlockEntity blockEntity) {
		blockEntity.spawner.serverTick((ServerLevel) level, pos);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag compoundtag = this.saveCustomOnly(registries);
		compoundtag.remove("SpawnPotentials");
		return compoundtag;
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		return this.spawner.onEventTriggered(this.level, id) || super.triggerEvent(id, type);
	}

	@Override
	public void setEntityId(EntityType<?> type, RandomSource random) {
		this.spawner.setEntityId(type, this.level, random, this.worldPosition);
		this.setChanged();
	}

	public SinisterSpawnerLogic getSpawner() {
		return this.spawner;
	}

	public boolean setParticles(List<ParticleOptions> particles, boolean sendUpdate) {
		return this.getSpawner().setParticles(particles, sendUpdate);
	}

	public boolean addParticle(ParticleOptions particle, boolean sendUpdate) {
		return this.getSpawner().addParticle(particle, sendUpdate);
	}

	public boolean removeParticle(ParticleOptions particle, boolean sendUpdate) {
		return this.getSpawner().removeParticle(particle, sendUpdate);
	}

	public void sendChanges() {
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	@Nullable
	public ResourceKey<LootTable> getLootTableId() {
		return this.lootTable;
	}

	@Nullable
	public LootTable getLootTable() {
		if (this.level == null || this.lootTable == null) return null;
		MinecraftServer server = this.level.getServer();
		if (server == null) return null;
		return server.reloadableRegistries().getLootTable(this.lootTable);
	}

	public boolean setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
		boolean updated = this.lootTable != lootTable;
		this.lootTable = lootTable;
		return updated;
	}
}
