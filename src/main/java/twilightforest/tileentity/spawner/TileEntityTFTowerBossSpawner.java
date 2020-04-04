package twilightforest.tileentity.spawner;

import net.minecraft.entity.player.PlayerEntity;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFUrGhast;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFTowerBossSpawner extends TileEntityTFBossSpawner<EntityTFUrGhast> {

	public TileEntityTFTowerBossSpawner() {
		super(TFTileEntities.TOWER_BOSS_SPAWNER.get(), TFEntities.ur_ghast.get());
	}

	@Override
	public boolean anyPlayerInRange() {
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > pos.getY() - 4;
	}
}
