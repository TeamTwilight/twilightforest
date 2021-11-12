package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Lich;
import twilightforest.block.entity.TFBlockEntities;

public class LichSpawnerBlockEntity extends BossSpawnerBlockEntity<Lich> {

	public LichSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.LICH_SPAWNER.get(), TFEntities.LICH, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}

	@Override
	protected boolean spawnMyBoss(ServerLevelAccessor world) {

		Lich myCreature = makeMyCreature();

		myCreature.moveTo(worldPosition, world.getLevel().random.nextFloat() * 360F, 0.0F);
		myCreature.finalizeSpawn(world, world.getCurrentDifficultyAt(worldPosition), MobSpawnType.SPAWNER, null, null);
		myCreature.setAttackCooldown(40);
		myCreature.setExtinguishTimer();

		// set creature's home to this
		initializeCreature(myCreature);

		// spawn it
		return world.addFreshEntity(myCreature);
	}
}
