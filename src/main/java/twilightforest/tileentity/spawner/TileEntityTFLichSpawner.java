package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import twilightforest.entity.boss.EntityTFLich;

public class TileEntityTFLichSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFLichSpawner() {
		super(EntityList.getKey(EntityTFLich.class));
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.posY > pos.getY() - 4;
	}
}
