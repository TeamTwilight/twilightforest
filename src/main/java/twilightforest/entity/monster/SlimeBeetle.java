package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.projectile.SlimeProjectile;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class SlimeBeetle extends Monster implements RangedAttackMob {

	public SlimeBeetle(EntityType<? extends SlimeBeetle> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 3.0F, 1.25F, 2.0F));
		this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1, 30, 10));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 25.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 4);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.SLIME_BEETLE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SLIME_BEETLE_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(TFSounds.SLIME_BEETLE_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return 0.25F;
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		ThrowableProjectile projectile = new SlimeProjectile(TFEntities.SLIME_BLOB.get(), this.level(), this);
		playSound(TFSounds.SLIME_BEETLE_SQUISH.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		double tx = target.getX() - this.getX();
		double ty = target.getY() + target.getEyeHeight() - 1.1D - projectile.getY();
		double tz = target.getZ() - this.getZ();
		float heightOffset = Mth.sqrt((float) (tx * tx + tz * tz)) * 0.2F;
		projectile.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.level().addFreshEntity(projectile);
	}
}
