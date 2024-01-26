package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.TomeSpawnerBlock;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;

import java.util.Optional;

public class TomeSpawnerBlockEntity extends BlockEntity {

	private int elapsedTime;

	//controlled variables via nbt
	private String entityType;
	private int spawnTime;
	private int playerDistance;

	public TomeSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TOME_SPAWNER.get(), pos, state);
		if (state.getValue(TomeSpawnerBlock.SPAWNER)) {
			this.entityType = "twilightforest:death_tome";
			this.spawnTime = 400;
			this.playerDistance = 8;
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TomeSpawnerBlockEntity te) {
		if (!level.isClientSide() && !(level.getDifficulty() == Difficulty.PEACEFUL) && te.isNearPlayer(level, pos)) {
            if (te.elapsedTime < te.spawnTime) {
                te.elapsedTime++;
            } else {
                te.elapsedTime = 0;

				if (!te.attemptSpawnTome((ServerLevel) level, pos, false, null)) return;

                int tomesLeft = state.getValue(TomeSpawnerBlock.BOOK_STAGES);

                if (tomesLeft <= 1) {
                    te.invalidateCapabilities();
                    level.setBlockAndUpdate(pos, TFBlocks.EMPTY_CANOPY_BOOKSHELF.get().defaultBlockState());
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(TomeSpawnerBlock.BOOK_STAGES, tomesLeft - 1));
                }
            }
        }
	}

	public boolean attemptSpawnTome(ServerLevel level, BlockPos pos, boolean fire, @Nullable LivingEntity assailant) {
		Optional<EntityType<?>> nbtType = EntityType.byString(this.entityType);

		for (Direction dir : Direction.Plane.HORIZONTAL) {
			if (level.isEmptyBlock(pos.relative(dir))) {
				for (int i = 0; i < 5; i++) {
					double x = pos.relative(dir).getX() + (level.getRandom().nextDouble() - level.getRandom().nextDouble()) * 2.0D;
					double y = (double) pos.getY() + (level.getRandom().nextDouble() - level.getRandom().nextDouble());
					double z = pos.relative(dir).getZ() + (level.getRandom().nextDouble() - level.getRandom().nextDouble()) * 2.0D;

					EntityType<?> type = nbtType.orElse(TFEntities.DEATH_TOME.get());
					if (level.noCollision(type.getAABB(x, y, z))) {
                        Entity entity = type.create(level);
						entity.moveTo(BlockPos.containing(x, y, z), entity.getYRot(), entity.getXRot());

						if (fire)
							entity.setSecondsOnFire(10);

						if (assailant != null && entity instanceof Mob mob)
							mob.setTarget(assailant);

						level.addFreshEntity(entity);
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean isNearPlayer(Level level, BlockPos pos) {
		return level.hasNearbyAlivePlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, this.playerDistance);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("EntityType", this.entityType);
		tag.putInt("SpawnDelay", this.spawnTime);
		tag.putInt("MaxPlayerDistance", this.playerDistance);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.entityType = tag.getString("EntityType");
		this.spawnTime = tag.getInt("SpawnDelay");
		this.playerDistance = tag.getInt("MaxPlayerDistance");
	}
}
