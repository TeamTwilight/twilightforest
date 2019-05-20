package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import twilightforest.entity.boss.EntityTFNaga;

public class TileEntityTFNagaSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFNagaSpawner() {
		super(EntityList.getKey(EntityTFNaga.class));
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
