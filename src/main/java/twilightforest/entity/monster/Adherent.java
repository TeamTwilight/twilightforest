package twilightforest.entity.monster;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.projectile.NatureBolt;

public class Adherent extends Monster implements RangedAttackMob, ITFCharger {

	private static final EntityDataAccessor<Boolean> CHARGE_FLAG = SynchedEntityData.defineId(Adherent.class, EntityDataSerializers.BOOLEAN);

	public Adherent(EntityType<? extends Adherent> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CHARGE_FLAG, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	@Override
	public void performRangedAttack(LivingEntity attackTarget, float extraDamage) {
		NatureBolt natureBolt = new NatureBolt(this.level(), this);
		this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

		// [VanillaCopy] adapted from EntitySnowman, with lower velocity and inaccuracy calculation
		double d0 = attackTarget.getY() + attackTarget.getEyeHeight() - 1.100000023841858D;
		double d1 = attackTarget.getX() - this.getX();
		double d2 = d0 - natureBolt.getY();
		double d3 = attackTarget.getZ() - this.getZ();
		float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
		natureBolt.shoot(d1, d2 + f, d3, 0.6F, 10 - this.level().getDifficulty().getId() * 4);

		this.level().addFreshEntity(natureBolt);

	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean isCharging() {
		return this.getEntityData().get(CHARGE_FLAG);
	}

	@Override
	public void setCharging(boolean flag) {
		this.getEntityData().set(CHARGE_FLAG, flag);
	}
}
