package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.block.entity.TFBlockEntities;

public class AlphaYetiSpawnerBlockEntity extends BossSpawnerBlockEntity<AlphaYeti> {

	public AlphaYetiSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.ALPHA_YETI_SPAWNER.get(), TFEntities.ALPHA_YETI, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 4;
	}
}
