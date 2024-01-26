package twilightforest.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.IceSnowball;
import twilightforest.init.TFSounds;

public class StableIceCore extends BaseIceMob implements RangedAttackMob {

	public StableIceCore(EntityType<? extends StableIceCore> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.6F;
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

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		IceSnowball snowball = new IceSnowball(this.level(), this);
		snowball.setPos(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());

		// [VanillaCopy] Adapted from SlowGolem
		double d0 = target.getY() + target.getEyeHeight() - 1.4D;
		double d1 = target.getX() - this.getX();
		double d2 = d0 - snowball.getY();
		double d3 = target.getZ() - this.getZ();
		float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
		//accuracy of a normal difficulty skeleton
		snowball.shoot(d1, d2 + f, d3, 1.6F, 6.0F);

		this.playSound(TFSounds.ICE_CORE_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level().addFreshEntity(snowball);
	}
}
