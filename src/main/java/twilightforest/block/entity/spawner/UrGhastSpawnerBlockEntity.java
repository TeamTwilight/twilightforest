package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.boss.UrGhast;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;

public class UrGhastSpawnerBlockEntity extends BossSpawnerBlockEntity<UrGhast> {

	public UrGhastSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.UR_GHAST_SPAWNER.get(), TFEntities.UR_GHAST.get(), pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = this.getLevel().getNearestPlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > this.getBlockPos().getY() - 4;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return DustParticleOptions.REDSTONE;
	}
}
