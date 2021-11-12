package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.block.TomeSpawnerBlock;
import twilightforest.entity.TFEntities;

import java.util.Optional;

public class TomeSpawnerBlockEntity extends BlockEntity {

	private int elapsedTime;

	//controlled variables via nbt
	private String entityType;
	private int tomesLeft;
	private int spawnTime;
	private int playerDistance;

	public TomeSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TOME_SPAWNER.get(), pos, state);
		if(state.getValue(TomeSpawnerBlock.SPAWNER)) {
			this.entityType = "twilightforest:death_tome";
			this.tomesLeft = 5;
			this.spawnTime = 400;
			this.playerDistance = 8;
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TomeSpawnerBlockEntity te) {
		if(!level.isClientSide && !(level.getDifficulty() == Difficulty.PEACEFUL)) {
			if(te.tomesLeft > 10) te.tomesLeft = 10;

			if (te.isNearPlayer(level, pos)) {
				if (te.elapsedTime < te.spawnTime) {
					te.elapsedTime++;
				} else {
					te.elapsedTime = 0;
					te.attemptSpawnTome((ServerLevel) level, pos);
				}
			}

			if (te.tomesLeft <= 0) {
				te.invalidateCaps();
				level.setBlockAndUpdate(pos, TFBlocks.EMPTY_CANOPY_BOOKSHELF.get().defaultBlockState());
			} else {
				level.setBlockAndUpdate(pos, state.setValue(TomeSpawnerBlock.BOOK_STAGES, te.tomesLeft));
			}
		}

	}

	private void attemptSpawnTome(ServerLevel level, BlockPos pos) {
		Optional<EntityType<?>> mob = EntityType.byString(this.entityType);
		boolean spawnedOne = false;
		for(Direction dir: Direction.Plane.HORIZONTAL) {
			if(level.isEmptyBlock(pos.relative(dir))) {
				for (int i = 0; i < 5; i++) {
					double x = pos.relative(dir).getX() + (level.random.nextDouble() - level.random.nextDouble()) * 2.0D;
					double y = (double) pos.getY() + (level.random.nextDouble() - level.random.nextDouble());
					double z = pos.relative(dir).getZ() + (level.random.nextDouble() - level.random.nextDouble()) * 2.0D;

					if (level.noCollision(mob.orElse(TFEntities.DEATH_TOME).getAABB(x, y, z)) && mob.isPresent()) {
						Entity entity = mob.orElse(TFEntities.DEATH_TOME).create(level);
						entity.moveTo(new BlockPos(x, y, z), entity.getYRot(), entity.getXRot());
						level.addFreshEntity(entity);
						tomesLeft--;
						spawnedOne = true;
						break;
					}
				}
			}
			if(spawnedOne) break;
		}
	}

	private boolean isNearPlayer(Level pLevel, BlockPos pPos) {
		return pLevel.hasNearbyAlivePlayer((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, playerDistance);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);
		tag.putString("EntityType", this.entityType);
		tag.putInt("MobSpawnsLeft", this.tomesLeft);
		tag.putInt("SpawnDelay", this.spawnTime);
		tag.putInt("MaxPlayerDistance", this.playerDistance);
		return tag;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.entityType = tag.getString("EntityType");
		this.tomesLeft = tag.getInt("MobSpawnsLeft");
		this.spawnTime = tag.getInt("SpawnDelay");
		this.playerDistance = tag.getInt("MaxPlayerDistance");
	}
}
