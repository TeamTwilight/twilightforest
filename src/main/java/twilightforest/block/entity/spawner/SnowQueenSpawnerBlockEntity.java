package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.block.entity.TFBlockEntities;

public class SnowQueenSpawnerBlockEntity extends BossSpawnerBlockEntity<SnowQueen> {

	public SnowQueenSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.SNOW_QUEEN_SPAWNER.get(), TFEntities.SNOW_QUEEN, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
