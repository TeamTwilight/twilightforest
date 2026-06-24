package twilightforest.block.entity.bookshelf;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.extensions.IOwnedSpawner;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BookshelfSpawner implements IOwnedSpawner {
	public static final List<Pair<Integer, BooleanProperty>> SLOT_PROPERTIES_AND_INDEXES = List.of(
		Pair.of(0, BlockStateProperties.SLOT_0_OCCUPIED),
		Pair.of(1, BlockStateProperties.SLOT_1_OCCUPIED),
		Pair.of(2, BlockStateProperties.SLOT_2_OCCUPIED),
		Pair.of(3, BlockStateProperties.SLOT_3_OCCUPIED),
		Pair.of(4, BlockStateProperties.SLOT_4_OCCUPIED),
		Pair.of(5, BlockStateProperties.SLOT_5_OCCUPIED));
	public int maxNearbyEntities = 4;
	public int spawnRange = 4;
	public int spawnCheckRange = 12;
	private int spawnDelay = 20;
	private WeightedList<SpawnData> spawnPotentials = WeightedList.of();
	@Nullable
	private SpawnData nextSpawnData;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 400;
	private int requiredPlayerRange = 8;
	private final PlayerDetector detector = PlayerDetector.INCLUDING_CREATIVE_PLAYERS;

	public void setEntityId(EntityType<?> type, @Nullable Level level, RandomSource random, BlockPos pos) {
		this.getOrCreateNextSpawnData(level, random, pos)
			.getEntityToSpawn()
			.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
	}

	private boolean isNearPlayer(ServerLevel level, BlockPos pos) {
		return !this.detector.detect(level, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL, pos, this.requiredPlayerRange, true).isEmpty();
	}

	public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
		if (this.isNearPlayer(level, pos)) {
			if (this.spawnDelay == -1) {
				this.delay(level, pos);
			}

			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			} else {
				List<Pair<Integer, BooleanProperty>> filledSlots = new ArrayList<>(SLOT_PROPERTIES_AND_INDEXES);
				filledSlots.removeIf(pair -> !state.getValue(pair.getSecond()));
				Collections.shuffle(filledSlots);

				for (Pair<Integer, BooleanProperty> filledSlot : filledSlots) {
					BooleanProperty property = filledSlot.getSecond();
					if (state.hasProperty(property) && state.getValue(property)) {
						if (this.attemptSpawnTome(filledSlot.getFirst(), level, pos, false, null, 0)) {
							this.delay(level, pos);
							break;
						}
					}
				}

				int fullSlots = 0;
				for (BooleanProperty property : ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES) {
					if (state.hasProperty(property) && state.getValue(property)) {
						fullSlots++;
					}
				}

				if (fullSlots == 0) {
					level.setBlockAndUpdate(pos, state.setValue(ChiseledCanopyShelfBlock.SPAWNER, false));
				}
			}
		}
	}

	private void delay(Level level, BlockPos pos) {
		RandomSource randomsource = level.getRandom();
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			this.spawnDelay = this.minSpawnDelay + randomsource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
		}

		this.spawnPotentials.getRandom(randomsource).ifPresent(data -> this.setNextSpawnData(level, pos, data));
		this.broadcastEvent(level, pos, 1);
	}

	public void load(@Nullable Level level, BlockPos pos, ValueInput input) {
		this.spawnDelay = input.getShortOr("Delay", (short) 20);
		input.read("SpawnData", SpawnData.CODEC).ifPresent(nextSpawnData -> this.setNextSpawnData(level, pos, nextSpawnData));
		this.spawnPotentials = input.read("SpawnPotentials", SpawnData.LIST_CODEC).orElseGet(() -> WeightedList.of(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData()));
		this.minSpawnDelay = input.getShortOr("MinSpawnDelay", (short) 200);
		this.maxSpawnDelay = input.getShortOr("MaxSpawnDelay", (short) 400);
		this.maxNearbyEntities = input.getShortOr("MaxNearbyEntities", (short) 4);
		this.requiredPlayerRange = input.getShortOr("RequiredPlayerRange", (short) 8);
		this.spawnRange = input.getShortOr("SpawnRange", (short) 4);
		this.spawnCheckRange = input.getShortOr("SpawnCheckRange", (short) 12);
	}

	public void save(ValueOutput output) {
		output.putShort("Delay", (short) this.spawnDelay);
		output.putShort("MinSpawnDelay", (short) this.minSpawnDelay);
		output.putShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
		output.putShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
		output.putShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
		output.putShort("SpawnRange", (short) this.spawnRange);
		output.putShort("SpawnCheckRange", (short) this.spawnCheckRange);
		output.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
		output.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
	}

	public boolean onEventTriggered(Level level, int id) {
		if (id == 1) {
			if (level.isClientSide()) {
				this.spawnDelay = this.minSpawnDelay;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData data) {
		this.nextSpawnData = data;
	}

	@Nullable
	public SpawnData getNextSpawnData() {
		return this.nextSpawnData;
	}

	private SpawnData getOrCreateNextSpawnData(@Nullable Level level, RandomSource pRandom, BlockPos pos) {
		if (this.nextSpawnData == null) {
			this.setNextSpawnData(level, pos, this.spawnPotentials.getRandom(pRandom).orElseGet(SpawnData::new));
		}
		return this.nextSpawnData;
	}

	public abstract void broadcastEvent(Level level, BlockPos pos, int id);

	public boolean attemptSpawnTome(int slot, ServerLevel level, BlockPos pos, boolean fire, @Nullable LivingEntity assailant, int maxTries) {
		RandomSource random = level.getRandom();
		SpawnData data = this.getOrCreateNextSpawnData(level, random, pos);
		CompoundTag tag = data.entityToSpawn();
		BlockState shelf = level.getBlockState(pos);
		Direction facing = shelf.getValue(HorizontalDirectionalBlock.FACING);
		try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this::toString, TwilightForestMod.LOGGER)) {
			ValueInput input = TagValueInput.create(reporter, level.registryAccess(), this.nextSpawnData.getEntityToSpawn());
			Optional<EntityType<?>> entityType = EntityType.by(input);
			//if the assigned entity doesn't exist or the bookshelf is blocked off, fail early
			if (entityType.isEmpty() || !level.getBlockState(pos.relative(facing)).canBeReplaced()) {
				this.delay(level, pos);
				return false;
			}

			//pick random spot in front of the shelf
			double x = pos.relative(facing).getX() + (random.nextDouble() - random.nextDouble()) * 2.0D;
			double y = (double) pos.getY() + (random.nextDouble() - random.nextDouble());
			double z = pos.relative(facing).getZ() + (random.nextDouble() - random.nextDouble()) * 2.0D;

			//apply spawning logic like vanilla spawners do
			if (level.noCollision(entityType.get().getSpawnAABB(x, y, z))) {
				boolean difficultyPreventsSpawn = !entityType.get().getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL;

				BlockPos blockpos = BlockPos.containing(x, y, z);
				if (data.getCustomSpawnRules().isPresent()) {
					if (difficultyPreventsSpawn) {
						return false;
					}

					SpawnData.CustomSpawnRules rules = data.getCustomSpawnRules().get();
					if (!rules.isValidPosition(blockpos, level) && !fire) {
						return false;
					}
				} else if (difficultyPreventsSpawn) {
					this.delay(level, pos);
					return false;
				}

				Entity entity = EntityType.loadEntityRecursive(tag, level, EntitySpawnReason.SPAWNER, processed -> {
					processed.snapTo(x, y, z, processed.getYRot(), processed.getXRot());
					//set entity on fire if told to do so
					if (fire) {
						processed.setRemainingFireTicks(200);
					}

					//target whoever was responsible for spawning the mob
					if (assailant != null && processed instanceof Mob mob) {
						mob.setTarget(assailant);
					}

					return processed;
				});
				if (entity == null) {
					this.delay(level, pos);
					return false;
				}

				int k = level.getEntities(EntityTypeTest.forExactClass(entity.getClass()), new AABB(pos).inflate(this.spawnCheckRange), EntitySelector.NO_SPECTATORS).size();
				if (k >= this.maxNearbyEntities && !fire) {
					this.delay(level, pos);
					return false;
				}

				entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
				if (entity instanceof Mob mob) {
					boolean hasNoConfiguration = data.getEntityToSpawn().size() == 1 && data.getEntityToSpawn().getString("id").isPresent();
					EventHooks.finalizeMobSpawnSpawner(mob, level, level.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.SPAWNER, null, this, hasNoConfiguration);

					data.getEquipment().ifPresent(mob::equip);
				}

				if (!level.tryAddFreshEntityWithPassengers(entity)) {
					this.delay(level, pos);
					return false;
				}

				level.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
				if (entity instanceof Mob mob) {
					mob.spawnAnim();
				}

				//after mob is spawned, clear that book's spot from the shelf
				if (level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity be) {
					be.setItem(slot, ItemStack.EMPTY);
				}
				return true;
			} else {
				if (maxTries != 0) {
					this.attemptSpawnTome(slot, level, pos, fire, assailant, maxTries - 1);
				}
			}
		}
		return false;
	}
}
