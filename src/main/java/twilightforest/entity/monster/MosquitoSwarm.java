package twilightforest.entity.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

public class MosquitoSwarm extends Monster {

	public MosquitoSwarm(EntityType<? extends MosquitoSwarm> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 12.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 3.0D)
			.add(Attributes.STEP_HEIGHT, 2.1D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MOSQUITO.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		if (super.doHurtTarget(level, entity)) {
			if (entity instanceof LivingEntity living) {
				int duration = switch (this.level().getDifficulty()) {
					case EASY -> 7;
					case HARD -> 30;
					default -> 15;
				};

				living.addEffect(new MobEffectInstance(MobEffects.HUNGER, duration * 20, 0));
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
	}

}
