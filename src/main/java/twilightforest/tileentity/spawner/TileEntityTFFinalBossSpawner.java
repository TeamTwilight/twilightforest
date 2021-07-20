package twilightforest.tileentity.spawner;

import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFPlateauBoss;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner<EntityTFPlateauBoss> {

	public TileEntityTFFinalBossSpawner() {
		super(TFTileEntities.FINAL_BOSS_SPAWNER.get(), TFEntities.plateau_boss);
	}

	@Override
	public void tick() {

	}
}
