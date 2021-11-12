package twilightforest.entity.passive;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;

public class Boar extends Pig {

	public Boar(EntityType<? extends Boar> type, Level world) {
		super(type, world);
	}

	@Override
	public Pig getBreedOffspring(ServerLevel world, AgeableMob entityanimal) {
		return TFEntities.BOAR.create(world);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BOAR_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.BOAR_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BOAR_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(TFSounds.BOAR_STEP, 0.15F, 1.0F);
	}
}
