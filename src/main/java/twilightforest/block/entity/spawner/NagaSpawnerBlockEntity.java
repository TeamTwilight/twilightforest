package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.Naga;
import twilightforest.block.entity.TFBlockEntities;

public class NagaSpawnerBlockEntity extends BossSpawnerBlockEntity<Naga> {

	public NagaSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.NAGA_SPAWNER.get(), TFEntities.NAGA, pos, state);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
