package twilightforest.tileentity.spawner;

import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFHydraSpawner extends TileEntityTFBossSpawner<EntityTFHydra> {

	public TileEntityTFHydraSpawner() {
		super(TFTileEntities.HYDRA_SPAWNER.get(), TFEntities.hydra);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
