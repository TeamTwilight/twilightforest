package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.UrGhast;
import twilightforest.block.entity.TFBlockEntities;

public class UrGhastSpawnerBlockEntity extends BossSpawnerBlockEntity<UrGhast> {

	public UrGhastSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.UR_GHAST_SPAWNER.get(), TFEntities.UR_GHAST, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
