package twilightforest.tileentity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import twilightforest.entity.boss.EntityTFYetiAlpha;

public class TileEntityTFAlphaYetiSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFAlphaYetiSpawner() {
		this.mobID = EntityList.getKey(EntityTFYetiAlpha.class);
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 9D, false);

		return closestPlayer != null && closestPlayer.posY > pos.getY() - 4;
	}

}
