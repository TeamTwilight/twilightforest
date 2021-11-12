package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Hydra;
import twilightforest.block.entity.TFBlockEntities;

public class HydraSpawnerBlockEntity extends BossSpawnerBlockEntity<Hydra> {

	public HydraSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.HYDRA_SPAWNER.get(), TFEntities.HYDRA, pos, state);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
