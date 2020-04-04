package twilightforest.tileentity.spawner;

import net.minecraft.entity.player.PlayerEntity;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFMinoshroom;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFMinoshroomSpawner extends TileEntityTFBossSpawner<EntityTFMinoshroom> {

	public TileEntityTFMinoshroomSpawner() {
		super(TFTileEntities.MINOSHROOM_SPAWNER.get(), TFEntities.minoshroom.get());
	}

	@Override
	public boolean anyPlayerInRange() {
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > pos.getY() - 4;
	}
}
