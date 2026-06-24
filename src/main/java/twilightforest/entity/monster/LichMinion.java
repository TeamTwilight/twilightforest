package twilightforest.entity.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFAttributes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import java.util.List;

public class LichMinion extends Zombie {

	@Nullable
	public Lich master;

	public LichMinion(EntityType<? extends LichMinion> type, Level world) {
		super(type, world);
		this.master = null;
	}

	public LichMinion(Level world, Lich entityTFLich) {
		super(TFEntities.LICH_MINION.get(), world);
		this.master = entityTFLich;
	}

	@Override
	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Lich.class) {
			@Override
			protected boolean canAttack(@Nullable LivingEntity potentialTarget, TargetingConditions targetPredicate) {
				return !(potentialTarget instanceof Lich) && super.canAttack(potentialTarget, targetPredicate);
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public boolean hurtServer(ServerLevel server, DamageSource source, float amount) {
		LivingEntity prevTarget = getTarget();

		if (super.hurtServer(server, source, amount)) {
			if (source.getEntity() instanceof Lich) {
				// return to previous target but speed up
				this.setLastHurtByMob(prevTarget);
				this.addEffect(new MobEffectInstance(MobEffects.SPEED, 200, 2));
				this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 200, 1));
			}
			return true;
		} else return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINION_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MINION_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINION_DEATH.get();
	}

	@Override
	protected SoundEvent getStepSound() {
		return TFSounds.MINION_STEP.get();
	}

	@Override
	public void aiStep() {
		if (this.master == null) {
			this.findNewMaster();
		}
		// if we still don't have a master, or ours is dead, die.
		if (this.master == null || !this.master.isAlive()) {
			this.discard();
		}
		super.aiStep();
	}

	private void findNewMaster() {
		List<Lich> nearbyLiches = this.level().getEntitiesOfClass(Lich.class, new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).inflate(32.0D, 16.0D, 32.0D));

		for (Lich nearbyLich : nearbyLiches) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewMinion()) {
				this.master = nearbyLich;

				// animate our new linkage!
				if (!this.level().isClientSide()) {
					this.master.makeMagicTrail(this.getEyePosition(), this.master.getEyePosition(), 0.0F, 0.0F, 0.0F);
				}

				// become angry at our masters target
				this.setTarget(this.master.getTarget());

				// quit looking
				break;
			}
		}
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
		boolean baby = false;

		if (this.master != null && difficulty.getDifficulty() == Difficulty.HARD) {
			int babiesSummoned = this.master.getBabyMinionsSummoned();
			if (babiesSummoned < this.master.getAttributeValue(TFAttributes.MINION_COUNT) / 4) { // One quarter can be babies on hard, by default: 9 / 4 = 2
				baby = this.getRandom().nextInt(100) <= 20; // 20%
			}
			if (baby) this.master.setBabyMinionsSummoned(babiesSummoned + 1);
		}

		spawnGroupData = new ZombieGroupData(baby, true);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		//NO-OP
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}
}
