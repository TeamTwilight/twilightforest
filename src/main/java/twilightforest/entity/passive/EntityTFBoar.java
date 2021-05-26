package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;

public class EntityTFBoar extends PigEntity {

	public EntityTFBoar(EntityType<? extends EntityTFBoar> type, World world) {
		super(type, world);
	}

	@Override
	public PigEntity func_241840_a(ServerWorld world, AgeableEntity entityanimal) {
		return TFEntities.wild_boar.create(world);
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
