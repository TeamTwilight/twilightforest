package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.control.NoClipMoveControl;
import twilightforest.entity.ai.goal.SimplifiedAttackGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;
import twilightforest.util.entities.EntityUtil;

import java.util.EnumSet;
import java.util.Optional;

public class Wraith extends FlyingMob implements Enemy, EnforcedHomePoint {

	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(Wraith.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	public Wraith(EntityType<? extends Wraith> type, Level level) {
		super(type, level);
		this.moveControl = new NoClipMoveControl(this);
		this.noPhysics = true;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new MoveTowardsHomeGoal(this, 0.85D));
		this.goalSelector.addGoal(4, new SimplifiedAttackGoal(this));
		this.goalSelector.addGoal(5, new FlyTowardsTargetGoal(this));
		this.goalSelector.addGoal(6, new RandomFloatAroundGoal(this));
		this.goalSelector.addGoal(7, new LookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.5D)
			.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HOME_POINT, Optional.empty());
	}

	@Override
	public boolean isSteppingCarefully() {
		return true;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (super.hurtServer(level, source, amount)) {
			Entity entity = source.getEntity();
			if (this.getVehicle() == entity || this.getPassengers().contains(entity)) {
				return true;
			}
			if (entity != this && entity instanceof LivingEntity && !source.isCreativePlayer()) {
				this.setTarget((LivingEntity) entity);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		return EntityUtil.properlyApplyCustomDamageSource(level, this, entity, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.HAUNT, this), null);
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WRAITH_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.WRAITH_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.WRAITH_DEATH.get();
	}

	public static boolean checkMonsterSpawnRules(EntityType<? extends Wraith> entity, ServerLevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && checkMobSpawnRules(entity, world, reason, pos, random);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason type, @Nullable SpawnGroupData data) {
		if (type == EntitySpawnReason.STRUCTURE || type == EntitySpawnReason.SPAWNER) this.setRestrictionPoint(GlobalPos.of(accessor.getLevel().dimension(), this.blockPosition()));
		return super.finalizeSpawn(accessor, difficulty, type, data);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		this.saveHomePointToNbt(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.loadHomePointFromNbt(tag);
	}

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}

	static class FlyTowardsTargetGoal extends Goal {
		private final Wraith wraith;

		FlyTowardsTargetGoal(Wraith wraith) {
			this.wraith = wraith;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return wraith.getTarget() != null;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			LivingEntity target = wraith.getTarget();
			if (target != null)
				wraith.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.5F);
		}
	}

	// [VanillaCopy] Ghast.RandomFloatAroundGoal
	static class RandomFloatAroundGoal extends Goal {
		private final Wraith wraith;

		public RandomFloatAroundGoal(Wraith wraith) {
			this.wraith = wraith;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (this.wraith.getTarget() != null || !this.wraith.isMobWithinHomeArea(this.wraith))
				return false;
			MoveControl control = this.wraith.getMoveControl();
			double d0 = control.getWantedX() - this.wraith.getX();
			double d1 = control.getWantedY() - this.wraith.getY();
			double d2 = control.getWantedZ() - this.wraith.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			RandomSource random = this.wraith.getRandom();
			double d0 = this.wraith.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.wraith.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.wraith.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.wraith.getMoveControl().setWantedPosition(d0, d1, d2, 0.5D);
		}
	}

	// [VanillaCopy] Ghast.GhastLookGoal
	public static class LookAroundGoal extends Goal {
		private final Wraith wraith;

		public LookAroundGoal(Wraith wraith) {
			this.wraith = wraith;
			this.setFlags(EnumSet.of(Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public void tick() {
			if (this.wraith.getTarget() == null) {
				this.wraith.setYRot(-((float) Mth.atan2(this.wraith.getDeltaMovement().x(), this.wraith.getDeltaMovement().z())) * (180F / (float) Math.PI));
				this.wraith.setYBodyRot(this.wraith.getYRot());
			} else {
				LivingEntity entitylivingbase = this.wraith.getTarget();

				if (entitylivingbase.distanceToSqr(this.wraith) < 4096.0D) {
					double d1 = entitylivingbase.getX() - this.wraith.getX();
					double d2 = entitylivingbase.getZ() - this.wraith.getZ();
					this.wraith.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
					this.wraith.setYBodyRot(this.wraith.getYRot());
				}
			}
		}
	}

	//modified version of MoveTowardsRestrictionGoal. We're limited with what we can use since Wraiths arent PathfinderMobs
	public static class MoveTowardsHomeGoal extends Goal {
		private final Wraith mob;
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;

		public MoveTowardsHomeGoal(Wraith mob, double speedModifier) {
			this.mob = mob;
			this.speedModifier = speedModifier;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (this.mob.isMobWithinHomeArea(this.mob) || this.mob.getTarget() != null) {
				return false;
			} else {
				BlockPos pos = this.mob.getRestrictionPoint().pos()
					.relative(Direction.getRandom(this.mob.getRandom()))
					.offset(this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5), this.mob.getRandom().nextInt(5));
				if (!this.mob.level().isLoaded(pos)) {
					return false;
				} else {
					this.wantedX = pos.getX();
					this.wantedY = pos.getY();
					this.wantedZ = pos.getZ();
					return true;
				}
			}
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			this.mob.getMoveControl().setWantedPosition(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
		}
	}
}
