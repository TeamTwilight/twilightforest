package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import twilightforest.TFSounds;

import java.util.List;

public class EntityTFLichMinion extends ZombieEntity {

	EntityTFLich master;

	public EntityTFLichMinion(EntityType<? extends EntityTFLichMinion> type, World world) {
		super(type, world);
		this.master = null;
	}

	public EntityTFLichMinion(World world, EntityTFLich entityTFLich) {
		super(world);
		this.master = entityTFLich;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		LivingEntity prevTarget = getAttackTarget();

		if (super.attackEntityFrom(source, amount)) {
			if (source.getTrueSource() instanceof EntityTFLich) {
				// return to previous target but speed up
				setRevengeTarget(prevTarget);
				addPotionEffect(new EffectInstance(Effects.SPEED, 200, 4));
				addPotionEffect(new EffectInstance(Effects.STRENGTH, 200, 1));
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
	public void livingTick() {
		if (master == null) {
			findNewMaster();
		}
		// if we still don't have a master, or ours is dead, die.
		if (master == null || !master.isAlive()) {
			this.setHealth(0);
		}
		super.livingTick();
	}

	private void findNewMaster() {
		List<EntityTFLich> nearbyLiches = world.getEntitiesWithinAABB(EntityTFLich.class, new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 16.0D, 32.0D));

		for (EntityTFLich nearbyLich : nearbyLiches) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewMinion()) {
				this.master = nearbyLich;

				// animate our new linkage!
				master.makeBlackMagicTrail(getPosX(), getPosY() + this.getEyeHeight(), getPosZ(), master.getPosX(), master.getPosY() + master.getEyeHeight(), master.getPosZ());

				// become angry at our masters target
				setAttackTarget(master.getAttackTarget());

				// quit looking
				break;
			}
		}
	}
}
