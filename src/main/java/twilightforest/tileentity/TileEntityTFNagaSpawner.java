package twilightforest.tileentity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import twilightforest.entity.boss.EntityTFNaga;

public class TileEntityTFNagaSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFNagaSpawner() {
		this.mobID = EntityList.getKey(EntityTFNaga.class);
	}

	@Override
	public boolean anyPlayerInRange() {
		return world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 50D, false) != null;
	}

	@Override
	protected void initializeCreature(EntityLiving myCreature) {

		if (myCreature instanceof EntityCreature) {
			((EntityCreature) myCreature).setHomePosAndDistance(pos, 46);
		}
	}

	@Override
	protected int getRange() {
		return 50;
	}
}
