package twilightforest.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class IceCrystal extends BaseIceMob {

	private int crystalAge;
	private int maxCrystalAge = -1;

	public IceCrystal(EntityType<? extends IceCrystal> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 10.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 4;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_CORE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_CORE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_CORE_DEATH.get();
	}

	public void setToDieIn30Seconds() {
		this.maxCrystalAge = 600;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.level().isClientSide()) {
			this.crystalAge++;
			if (this.maxCrystalAge > 0 && this.crystalAge >= this.maxCrystalAge) {
				this.discard();
			}
		}
	}
}
