package twilightforest.block.entity.spawner;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.util.BoundingBoxUtils;

import java.util.*;

public abstract class SinisterSpawnerLogic extends BaseSpawner {
	private static final Codec<List<ParticleOptions>> PARTICLES_CODEC = ParticleTypes.CODEC.listOf();

	private BlockPos.@Nullable MutableBlockPos checkPos = null;
	private final List<BlockPos> spawnBuffer = new ArrayList<>();
	private int countNextToSpawn = 0;
	public int entityScanRange = 4;

	private final Set<ParticleOptions> particleOptions = new HashSet<>();

	public void setChanged() {
		Either<BlockEntity, Entity> owner = this.getOwner();
		if (owner != null) {
			owner.ifLeft(blockEntity -> {
				blockEntity.setChanged();
				if (blockEntity instanceof SinisterSpawnerBlockEntity sinisterSpawnerBlockEntity) {
					sinisterSpawnerBlockEntity.sendChanges();
				}
			});
		}
	}

	public boolean setParticles(Collection<ParticleOptions> particleOptions, boolean sendUpdate) {
		this.particleOptions.clear();
		boolean changed = this.particleOptions.addAll(particleOptions);
		if (sendUpdate && changed) this.setChanged();
		return changed;
	}

	public boolean addParticle(ParticleOptions particle, boolean sendUpdate) {
		boolean changed = this.particleOptions.add(particle);
		if (sendUpdate && changed) this.setChanged();
		return changed;
	}

	public boolean removeParticle(ParticleOptions particle, boolean sendUpdate) {
		boolean changed = this.particleOptions.remove(particle);
		if (sendUpdate && changed) this.setChanged();
		return changed;
	}

	// [VANILLA COPY] Swapped regular flame particles for this spawner's config
	@Override
	public void clientTick(Level level, BlockPos pos) {
		if (!this.isNearPlayer(level, pos)) {
			this.oSpin = this.spin;
		} else if (this.displayEntity != null) {
			RandomSource randomsource = level.getRandom();
			double pX = (double)pos.getX() + randomsource.nextDouble();
			double pY = (double)pos.getY() + randomsource.nextDouble();
			double pZ = (double)pos.getZ() + randomsource.nextDouble();
			for (ParticleOptions particleOptions : this.particleOptions) {
				level.addParticle(particleOptions, pX, pY, pZ, 0.0, 0.0, 0.0);
			}
			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			}

			this.oSpin = this.spin;
			this.spin = (this.spin + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0;
		}
	}

	// [VANILLA COPY] Lazily scans for positions to spawn monsters while charging, then places monsters from positions found.
	//  This is for better placing mobs in safer positions than to wastefully
	// 	Caveat: Does not account for a changing block environment, but that's okay for purposes of this spawner
	@Override
	public void serverTick(ServerLevel serverLevel, BlockPos blockEntityPos) {
		if (this.isNearPlayer(serverLevel, blockEntityPos)) {
			if (this.countNextToSpawn < 1) {
				this.countNextToSpawn = serverLevel.getRandom().nextInt(1 + Math.max(0, this.spawnCount));
			}

			if (this.spawnDelay <= -1) {
				this.delay(serverLevel, blockEntityPos);
			}

			this.scanSpawnPositions(serverLevel, blockEntityPos, serverLevel.getRandom());

			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			} else if (!this.spawnBuffer.isEmpty()) {
				boolean spawned = false;
				RandomSource randomsource = serverLevel.getRandom();
				SpawnData spawndata = this.getOrCreateNextSpawnData(serverLevel, randomsource, blockEntityPos);

				for (BlockPos spawnAt : new java.util.ArrayList<>(this.spawnBuffer)) {
					CompoundTag entityData = spawndata.getEntityToSpawn();
					try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this::toString, TwilightForestMod.LOGGER)) {
						ValueInput input = TagValueInput.create(reporter, serverLevel.registryAccess(), entityData);
						Optional<EntityType<?>> entityType = EntityType.by(input);
						if (entityType.isEmpty()) {
							this.delay(serverLevel, blockEntityPos);
							return;
						}

						final double spawnX = spawnAt.getX() + 0.5;
						final double spawnY = spawnAt.getY();
						final double spawnZ = spawnAt.getZ() + 0.5;
						if (serverLevel.noCollision(entityType.get().getSpawnAABB(spawnX, spawnY, spawnZ))) {
							if (spawndata.getCustomSpawnRules().isPresent()) {
								if (!entityType.get().getCategory().isFriendly() && serverLevel.getDifficulty() == Difficulty.PEACEFUL) {
									continue;
								}

								SpawnData.CustomSpawnRules spawndata$customspawnrules = spawndata.getCustomSpawnRules().get();
								if (!spawndata$customspawnrules.isValidPosition(spawnAt, serverLevel)) {
									continue;
								}
							} else if (!SpawnPlacements.checkSpawnRules(entityType.get(), serverLevel, EntitySpawnReason.SPAWNER, spawnAt, serverLevel.getRandom())) {
								continue;
							}

							Entity entity = EntityType.loadEntityRecursive(entityData, serverLevel, EntitySpawnReason.SPAWNER, spawnedEntity -> {
								spawnedEntity.snapTo(spawnX, spawnY, spawnZ, spawnedEntity.getYRot(), spawnedEntity.getXRot());
								return spawnedEntity;
							});
							if (entity == null) {
								this.delay(serverLevel, blockEntityPos);
								return;
							}

							int likeEntities = serverLevel.getEntities(
									EntityTypeTest.forExactClass(entity.getClass()),
									new AABB(
										blockEntityPos.getX(),
										blockEntityPos.getY(),
										blockEntityPos.getZ(),
										blockEntityPos.getX() + 1,
										blockEntityPos.getY() + 1,
										blockEntityPos.getZ() + 1
									).inflate(this.entityScanRange),
									EntitySelector.NO_SPECTATORS
								)
								.size();
							if (likeEntities >= this.maxNearbyEntities) {
								this.delay(serverLevel, blockEntityPos);
								return;
							}

							entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), randomsource.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof Mob mob) {
								if (!EventHooks.checkSpawnPositionSpawner(mob, serverLevel, EntitySpawnReason.SPAWNER, spawndata, this)) {
									continue;
								}
								boolean hasNoConfiguration = spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().getString("id").isPresent();
								EventHooks.finalizeMobSpawnSpawner(mob, serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.SPAWNER, null, this, hasNoConfiguration);
								spawndata.getEquipment().ifPresent(mob::equip);
								this.checkPos = blockEntityPos.mutable();
							}

							if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
								this.delay(serverLevel, blockEntityPos);
								return;
							}

							for (ParticleOptions particle : this.particleOptions) {
								serverLevel.sendParticles(particle, entity.getX(), entity.getY(0.5f), entity.getZ(), 10, entity.getBbWidth() * 0.5f, entity.getBbHeight() * 0.5f, entity.getBbWidth() * 0.5f, 0);
							}
							serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, spawnAt);
							if (entity instanceof Mob) {
								((Mob) entity).spawnAnim();
							}

							spawned = true;
						}
					}
					this.spawnBuffer.clear(); // Loop iterated, now reset

					if (spawned) {
						for (ParticleOptions particle : this.particleOptions) {
							serverLevel.sendParticles(particle, blockEntityPos.getX() + 0.5f, blockEntityPos.getY() + 0.5f, blockEntityPos.getZ() + 0.5f, 10, 1, 1, 1, 0);
						}
						this.delay(serverLevel, blockEntityPos);
					}
				}
			} else if (this.spawnDelay == 0) {
				// Spawn buffer is empty, let spawnDelay tick over into resetting delay
				this.spawnDelay--;
			}
		}
	}

	private void scanSpawnPositions(ServerLevel serverLevel, BlockPos blockEntityPos, RandomSource random) {
		if (this.spawnBuffer.size() >= this.countNextToSpawn) return; // Spawn limit reached

		if (this.checkPos == null || !BoundingBoxUtils.isPosWithinBox(blockEntityPos, this.checkPos, this.spawnRange)) {
			// Restart from beginning, under the spawning block
			BlockPos.MutableBlockPos posBelow = blockEntityPos.mutable().move(Direction.DOWN);
			if (serverLevel.getBlockState(posBelow).isAir()) {
				this.checkPos = posBelow;
			} else {
				this.checkPos = posBelow.move(Direction.UP, 2);
			}
		}

		BlockState blockBelow = serverLevel.getBlockState(this.checkPos.below());

		if (blockBelow.isAir()) {
			this.checkPos.move(Direction.DOWN);
		} else {
			if ((blockBelow.isSolid() || blockBelow.is(TFBlocks.CORONATION_CARPET.get())) && random.nextBoolean() && !this.spawnBuffer.contains(this.checkPos) && serverLevel.getBlockState(this.checkPos).isAir()) {
				// If below is solid, then maybe record this position for being clear to spawn
				this.spawnBuffer.add(this.checkPos.immutable());
			}

			Direction randomDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
			if (!serverLevel.getBlockState(this.checkPos.relative(randomDirection)).isSolid()) {
				this.checkPos.move(randomDirection);
			} else {
				this.checkPos = null;
			}
		}

		// this.debugSeePosition(serverLevel);
	}

	// Sends particles whenever it checks a particular position
	private void debugSeePosition(ServerLevel serverLevel) {
		serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.checkPos.getX() + 0.5, this.checkPos.getY() + 0.5, this.checkPos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.05f);
	}

	@Override
	public void load(@Nullable Level level, BlockPos pos, ValueInput input) {
		super.load(level, pos, input);

		this.particleOptions.clear();
		this.particleOptions.addAll(input.read("ParticleOptions", PARTICLES_CODEC).orElse(new ArrayList<>()));
		this.entityScanRange = input.getIntOr("EntityScanRange", this.spawnRange);
	}

	@Override
	public void save(ValueOutput output) {
		super.save(output);
		output.store("ParticleOptions", PARTICLES_CODEC, List.copyOf(this.particleOptions));
		output.putInt("EntityScanRange", this.entityScanRange);
	}

	@Override
	public void broadcastEvent(Level level, BlockPos pos, int eventId) {
		level.blockEvent(pos, TFBlocks.SINISTER_SPAWNER.value(), eventId, 0);
	}

	@Override
	public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData nextSpawnData) {
		super.setNextSpawnData(level, pos, nextSpawnData);
		if (level != null) {
			BlockState blockstate = level.getBlockState(pos);
			level.sendBlockUpdated(pos, blockstate, blockstate, 4);
		}
	}
}
