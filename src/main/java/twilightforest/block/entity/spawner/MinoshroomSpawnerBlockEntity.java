package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;

public class MinoshroomSpawnerBlockEntity extends BossSpawnerBlockEntity<Minoshroom> {

	public MinoshroomSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MINOSHROOM_SPAWNER.get(), TFEntities.MINOSHROOM.get(), pos, state);
	}

	@Override
	public boolean anyPlayerInRange(Level level) {
		Player closestPlayer = level.getNearestPlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() < this.getBlockPos().getY() + 4;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return ParticleTypes.CRIMSON_SPORE;
	}
}
