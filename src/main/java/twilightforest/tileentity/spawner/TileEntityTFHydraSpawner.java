package twilightforest.tileentity.spawner;

import net.minecraft.entity.Entity;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFHydraSpawner extends TileEntityTFBossSpawner<EntityTFHydra> {

	public TileEntityTFHydraSpawner() {
		super(TFTileEntities.HYDRA_SPAWNER.get(), TFEntities.hydra);
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}

	@Override
	public Entity getDisplayEntity() {
		if (this.displayCreature == null) {
			this.displayCreature = new EntityTFHydraHead(null);
		}
		return this.displayCreature;
	}
}
