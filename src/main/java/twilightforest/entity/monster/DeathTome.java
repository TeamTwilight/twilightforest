package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.projectile.TomeBolt;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class DeathTome extends Monster implements RangedAttackMob {

	public DeathTome(EntityType<? extends DeathTome> type, Level world) {
		super(type, world);
		this.moveControl = new FlyingMoveControl(this, 10, false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1, 100, 5));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.FLYING_SPEED, 0.6D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
			public boolean isStableDestination(BlockPos pos) {
				return !this.level.getBlockState(pos.below()).isAir();
			}
		};
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(false);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		Vec3 vel = this.getDeltaMovement();
		if (!this.isOnGround() && vel.y() < 0.0D) {
			this.setDeltaMovement(vel.multiply(1.0D, 0.6D, 1.0D));
		}

		for (int i = 0; i < 1; ++i) {
			this.getLevel().addParticle(ParticleTypes.ENCHANT, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * (this.getBbHeight() - 0.75D) + 0.5D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(),
					0.0D, 0.5D, 0.0D);
		}
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		if (src.is(DamageTypeTags.IS_FIRE)) {
			damage *= 2;
		}

		if (super.hurt(src, damage)) {
			if (damage > 0) {
				if (!this.getLevel().isClientSide()) {
					LootContext ctx = createLootContext(true, src).create(LootContextParamSets.ENTITY);

					Objects.requireNonNull(this.getLevel().getServer()).getLootTables().get(TFLootTables.DEATH_TOME_HURT).getRandomItems(ctx, s -> spawnAtLocation(s, 1.0F));
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.DEATH_TOME_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.DEATH_TOME_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.DEATH_TOME_DEATH.get();
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		ThrowableProjectile projectile = new TomeBolt(TFEntities.TOME_BOLT.get(), this.getLevel(), this);
		double tx = target.getX() - this.getX();
		double ty = target.getY() + target.getEyeHeight() - 1.1D - projectile.getY();
		double tz = target.getZ() - this.getZ();
		float heightOffset = Mth.sqrt((float) (tx * tx + tz * tz)) * 0.2F;
		projectile.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.gameEvent(GameEvent.PROJECTILE_SHOOT);
		this.getLevel().addFreshEntity(projectile);
	}
}
