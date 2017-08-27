package twilightforest.tileentity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import twilightforest.entity.boss.EntityTFMinoshroom;

public class TileEntityTFMinoshroomSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFMinoshroomSpawner() {
		this.mobID = EntityList.getKey(EntityTFMinoshroom.class);
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 9D, false);

		return closestPlayer != null && closestPlayer.posY > pos.getY() - 4;
	}

}
