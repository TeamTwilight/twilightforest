package twilightforest.tileentity.spawner;

import net.minecraft.entity.player.PlayerEntity;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFSnowQueenSpawner extends TileEntityTFBossSpawner<EntityTFSnowQueen> {

	public TileEntityTFSnowQueenSpawner() {
		super(TFTileEntities.SNOW_QUEEN_SPAWNER.get(), TFEntities.snow_queen.get());
	}

	@Override
	public boolean anyPlayerInRange() {
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > pos.getY() - 4;
	}
}
