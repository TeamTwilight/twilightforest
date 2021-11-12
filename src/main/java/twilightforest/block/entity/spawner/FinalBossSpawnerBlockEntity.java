package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.PlateauBoss;
import twilightforest.block.entity.TFBlockEntities;

public class FinalBossSpawnerBlockEntity extends BossSpawnerBlockEntity<PlateauBoss> {

	public FinalBossSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.FINAL_BOSS_SPAWNER.get(), TFEntities.PLATEAU_BOSS, pos, state);
	}

	//no spawning for you
	@Override
	protected boolean spawnMyBoss(ServerLevelAccessor world) {
		return false;
	}
}
