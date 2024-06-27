package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.projectile.TomeBolt;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

import java.util.Objects;

public class DeathTome extends Monster implements RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_LECTERN = SynchedEntityData.defineId(DeathTome.class, EntityDataSerializers.BOOLEAN);

	public float flip;
	public float oFlip;
	public float flipT;
	public float flipA;

	@SuppressWarnings("this-escape")
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
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_LECTERN, false);
	}

	public void setOnLectern(boolean hidden) {
		this.getEntityData().set(DATA_LECTERN, hidden);
	}

	public boolean isOnLectern() {
		return this.getEntityData().get(DATA_LECTERN);
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
	public boolean isPersistenceRequired() {
		return super.isPersistenceRequired() || this.isOnLectern();
	}

	@Override
	public void aiStep() {
		if (this.isOnLectern()) {
			BlockState state = this.level().getBlockState(this.blockPosition());
			if (state.getBlock() instanceof LecternBlock) {
				Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
				this.yRotO = direction.toYRot();
				this.setYRot(this.yRotO);
				this.yBodyRot = this.yRotO;
				this.yBodyRotO = yRotO;
				this.yHeadRot = this.yRotO;
				this.yHeadRotO = this.yRotO;

				if (!this.level().isClientSide()) this.targetSelector.tick(); // Tick target selector, so that our Tome can find an enemy to ambush

				if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 20.0D) {
					this.setOnLectern(false);
					this.jumpControl.jump();
					this.performRangedAttack(this.getTarget(), 1.0F);
				}

				return;
			} else {
				this.setOnLectern(false);
			}
		}

		super.aiStep();

		Vec3 vel = this.getDeltaMovement();
		if (!this.onGround() && vel.y() < 0.0D) {
			this.setDeltaMovement(vel.multiply(1.0D, 0.6D, 1.0D));
		}

		for (int i = 0; i < 1; ++i) {
			this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * (this.getBbHeight() - 0.75D) + 0.5D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(),
				0.0D, 0.5D, 0.0D);
		}
	}

	@Override
	public void tick() {
		if (this.isOnLectern()) this.ambientSoundTime = -1; // Don't play ambient sounds if we're trying to be stealthy
		if (this.level().isClientSide) {
			float f1 = this.flipT;

			if (this.random.nextInt(this.isOnLectern() ? 120 : 30) == 0) {
				do this.flipT += (float) (this.random.nextInt(4) - this.random.nextInt(4));
				while (f1 == this.flipT);
			}

			this.oFlip = this.flip;
			float f = (this.flipT - this.flip) * 0.4F;
			f = Mth.clamp(f, -0.2F, 0.2F);
			this.flipA += (f - this.flipA) * 0.9F;
			this.flip += this.flipA;
		}

		super.tick();
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		if (this.isOnLectern()) {
			this.jumpControl.jump();
			this.setOnLectern(false);
		}

		if (src.is(DamageTypeTags.IS_FIRE)) {
			damage *= 2;
		}

		if (super.hurt(src, damage)) {
			if (damage > 0) {
				if (!this.level().isClientSide()) {
					LootParams ctx = TFLootTables.createLootParams(this, true, src).create(LootContextParamSets.ENTITY);

					Objects.requireNonNull(this.level().getServer()).reloadableRegistries().getLootTable(TFLootTables.DEATH_TOME_HURT).getRandomItems(ctx, s -> spawnAtLocation(s, 1.0F));
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
		ThrowableProjectile projectile = new TomeBolt(TFEntities.TOME_BOLT.get(), this.level(), this);
		double tx = target.getX() - this.getX();
		double ty = target.getY() + target.getEyeHeight() - 1.1D - projectile.getY();
		double tz = target.getZ() - this.getZ();
		float heightOffset = Mth.sqrt((float) (tx * tx + tz * tz)) * 0.2F;
		projectile.shoot(tx, ty + heightOffset, tz, 0.6F, 6.0F);
		this.gameEvent(GameEvent.PROJECTILE_SHOOT);
		this.level().addFreshEntity(projectile);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.entityData.set(DATA_LECTERN, tag.getBoolean("on_lectern"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("on_lectern", this.entityData.get(DATA_LECTERN));
	}
}
