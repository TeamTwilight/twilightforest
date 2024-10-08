package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;
import twilightforest.util.BoundingBoxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class CursedSpawnerLogic extends BaseSpawner {
	private @Nullable BlockPos.MutableBlockPos checkPos = null;
	private final List<BlockPos> spawnBuffer = new ArrayList<>();
	private int countNextToSpawn = 0;

	// [VANILLA COPY] Swapped regular flame particles for ominous
	@Override
	public void clientTick(Level level, BlockPos pos) {
		if (!this.isNearPlayer(level, pos)) {
			this.oSpin = this.spin;
		} else if (this.displayEntity != null) {
			RandomSource randomsource = level.getRandom();
			double pX = (double)pos.getX() + randomsource.nextDouble();
			double pY = (double)pos.getY() + randomsource.nextDouble();
			double pZ = (double)pos.getZ() + randomsource.nextDouble();
			level.addParticle(ParticleTypes.SMOKE, pX, pY, pZ, 0.0, 0.0, 0.0);
			level.addParticle(TFParticleType.OMINOUS_FLAME.value(), pX, pY, pZ, 0.0, 0.0, 0.0);
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

			if (this.spawnDelay == -1) {
				this.delay(serverLevel, blockEntityPos);
			}

			this.scanSpawnPositions(serverLevel, blockEntityPos, serverLevel.getRandom());

			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			} else if (!this.spawnBuffer.isEmpty()) {
				boolean spawned = false;
				RandomSource randomsource = serverLevel.getRandom();
				SpawnData spawndata = this.getOrCreateNextSpawnData(serverLevel, randomsource, blockEntityPos);

				for (BlockPos spawnAt : this.spawnBuffer) {
					CompoundTag entityData = spawndata.getEntityToSpawn();
					Optional<EntityType<?>> possibleEntityType = EntityType.by(entityData);
					if (possibleEntityType.isEmpty()) {
						this.delay(serverLevel, blockEntityPos);
						return;
					}

					final double spawnX = spawnAt.getX() + 0.5;
					final double spawnY = spawnAt.getY();
					final double spawnZ = spawnAt.getZ() + 0.5;
					if (serverLevel.noCollision(possibleEntityType.get().getSpawnAABB(spawnX, spawnY, spawnZ))) {
						if (spawndata.getCustomSpawnRules().isPresent()) {
							if (!possibleEntityType.get().getCategory().isFriendly() && serverLevel.getDifficulty() == Difficulty.PEACEFUL) {
								continue;
							}

							SpawnData.CustomSpawnRules spawndata$customspawnrules = spawndata.getCustomSpawnRules().get();
							if (!spawndata$customspawnrules.isValidPosition(spawnAt, serverLevel)) {
								continue;
							}
						} else if (!SpawnPlacements.checkSpawnRules(possibleEntityType.get(), serverLevel, MobSpawnType.SPAWNER, spawnAt, serverLevel.getRandom())) {
							continue;
						}

						Entity entity = EntityType.loadEntityRecursive(entityData, serverLevel, spawnedEntity -> {
							spawnedEntity.moveTo(spawnX, spawnY, spawnZ, spawnedEntity.getYRot(), spawnedEntity.getXRot());
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
								).inflate(this.spawnRange),
								EntitySelector.NO_SPECTATORS
							)
							.size();
						if (likeEntities >= this.maxNearbyEntities) {
							this.delay(serverLevel, blockEntityPos);
							return;
						}

						entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), randomsource.nextFloat() * 360.0F, 0.0F);
						if (entity instanceof Mob mob) {
							if (!net.neoforged.neoforge.event.EventHooks.checkSpawnPositionSpawner(mob, serverLevel, MobSpawnType.SPAWNER, spawndata, this)) {
								continue;
							}

							boolean flag1 = spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().contains("id", 8);
							// Neo: Patch in FinalizeSpawn for spawners so it may be fired unconditionally, instead of only when vanilla would normally call it.
							// The local flag1 is the conditions under which the spawner will normally call Mob#finalizeSpawn.
							net.neoforged.neoforge.event.EventHooks.finalizeMobSpawnSpawner(mob, serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null, this, flag1);

							spawndata.getEquipment().ifPresent(mob::equip);

							this.checkPos = blockEntityPos.mutable();
						}

						if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
							this.delay(serverLevel, blockEntityPos);
							return;
						}

						serverLevel.sendParticles(TFParticleType.OMINOUS_FLAME.value(), entity.getX(), entity.getY(0.5f), entity.getZ(), 10, entity.getBbWidth() * 0.5f, entity.getBbHeight() * 0.5f, entity.getBbWidth() * 0.5f, 0);
						serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, spawnAt);
						if (entity instanceof Mob) {
							((Mob)entity).spawnAnim();
						}

						spawned = true;
					}
				}
				this.spawnBuffer.clear(); // Loop iterated, now reset

				if (spawned) {
					serverLevel.sendParticles(TFParticleType.OMINOUS_FLAME.value(), blockEntityPos.getX() + 0.5f, blockEntityPos.getY() + 0.5f, blockEntityPos.getZ() + 0.5f, 10, 1, 1, 1, 0);
					this.delay(serverLevel, blockEntityPos);
				}
			}
		}
	}

	private void scanSpawnPositions(ServerLevel serverLevel, BlockPos blockEntityPos, RandomSource random) {
		if (this.spawnBuffer.size() >= this.countNextToSpawn) return; // Spawn limit reached

		if (this.checkPos == null || !BoundingBoxUtils.isPosWithinBox(blockEntityPos, this.checkPos, this.spawnRange)) {
			// Restart from beginning, under the spawning block
			this.checkPos = blockEntityPos.mutable().move(Direction.DOWN);
		}

		BlockState blockBelow = serverLevel.getBlockState(this.checkPos.below());

		if (blockBelow.isAir()) {
			this.checkPos.move(Direction.DOWN);
		} else {
			if ((blockBelow.isSolid() || blockBelow.is(TFBlocks.ROYAL_RAGS.get())) && random.nextBoolean() && !this.spawnBuffer.contains(this.checkPos) && serverLevel.getBlockState(this.checkPos).isAir()) {
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
	}
}
