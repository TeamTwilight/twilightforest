package twilightforest.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;


public class TileEntityTFHydraSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFHydraSpawner() {
		this.mobID = EntityList.getKey(EntityTFHydra.class);
	}

	@Override
	public Entity getDisplayEntity() {
		if (this.displayCreature == null) {
			this.displayCreature = EntityList.createEntityByIDFromName(EntityList.getKey(EntityTFHydraHead.class), world);
		}

		return this.displayCreature;
	}
}
