package twilightforest.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.entity.boss.Lich;

import java.util.List;

public class LichMinion extends Zombie {

	public Lich master;

	public LichMinion(EntityType<? extends LichMinion> type, Level world) {
		super(type, world);
		this.master = null;
	}

	public LichMinion(Level world, Lich entityTFLich) {
		super(world);
		this.master = entityTFLich;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		LivingEntity prevTarget = getTarget();

		if (super.hurt(source, amount)) {
			if (source.getEntity() instanceof Lich) {
				// return to previous target but speed up
				setLastHurtByMob(prevTarget);
				addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 4));
				addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINION_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MINION_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINION_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return TFSounds.MINION_STEP;
	}

	@Override
	public void aiStep() {
		if (master == null) {
			findNewMaster();
		}
		// if we still don't have a master, or ours is dead, die.
		if (master == null || !master.isAlive()) {
			this.setHealth(0);
		}
		super.aiStep();
	}

	private void findNewMaster() {
		List<Lich> nearbyLiches = level.getEntitiesOfClass(Lich.class, new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).inflate(32.0D, 16.0D, 32.0D));

		for (Lich nearbyLich : nearbyLiches) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewMinion()) {
				this.master = nearbyLich;

				// animate our new linkage!
				master.makeBlackMagicTrail(getX(), getY() + this.getEyeHeight(), getZ(), master.getX(), master.getY() + master.getEyeHeight(), master.getZ());

				// become angry at our masters target
				setTarget(master.getTarget());

				// quit looking
				break;
			}
		}
	}
}
