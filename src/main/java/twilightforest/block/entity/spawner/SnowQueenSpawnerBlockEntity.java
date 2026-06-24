package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;
import twilightforest.init.TFParticleType;

public class SnowQueenSpawnerBlockEntity extends BossSpawnerBlockEntity<SnowQueen> {

	public SnowQueenSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.SNOW_QUEEN_SPAWNER.get(), TFEntities.SNOW_QUEEN.get(), pos, state);
	}

	@Override
	public boolean anyPlayerInRange(Level level) {
		Player closestPlayer = level.getNearestPlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, this.getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > this.getBlockPos().getY() - 4;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return TFParticleType.SNOW.get();
	}
}
