package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;

import javax.annotation.Nullable;
import java.util.Random;

public class SwarmSpider extends Spider {

	protected boolean shouldSpawn = false;

	public SwarmSpider(EntityType<? extends SwarmSpider> type, Level world) {
		this(type, world, true);
	}

	public SwarmSpider(EntityType<? extends SwarmSpider> type, Level world, boolean spawnMore) {
		super(type, world);

		setSpawnMore(spawnMore);
		xpReward = 2;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Spider.createAttributes()
				.add(Attributes.MAX_HEALTH, 3.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Remove default spider melee task
		this.goalSelector.availableGoals.removeIf(t -> t.getGoal() instanceof MeleeAttackGoal);

		// Replace with one that doesn't become docile in light
		// [VanillaCopy] based on EntitySpider.AISpiderAttack
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true) {
			@Override
			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return 4.0F + attackTarget.getBbWidth();
			}
		});

		// Remove default spider target player task
		this.targetSelector.availableGoals.removeIf(t -> t.getPriority() == 2 && t.getGoal() instanceof NearestAttackableTargetGoal);
		// Replace with one that doesn't care about light
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.SWARM_SPIDER_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.SWARM_SPIDER_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.SWARM_SPIDER_DEATH;
	   }

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
	      this.playSound(TFSounds.SWARM_SPIDER_STEP, 0.15F, 1.0F);
	   }

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
	      return 0.3F;
	   }

	@Override
	public void tick() {
		if (!level.isClientSide && shouldSpawnMore()) {
			int more = 1 + random.nextInt(2);
			for (int i = 0; i < more; i++) {
				// try twice to spawn
				if (!spawnAnother()) {
					spawnAnother();
				}
			}
			setSpawnMore(false);
		}

		super.tick();
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		return random.nextInt(4) == 0 && super.doHurtTarget(entity);
	}

	protected boolean spawnAnother() {
		SwarmSpider another = new SwarmSpider(TFEntities.SWARM_SPIDER, level, false);

		double sx = getX() + (random.nextBoolean() ? 0.9 : -0.9);
		double sy = getY();
		double sz = getZ() + (random.nextBoolean() ? 0.9 : -0.9);
		another.moveTo(sx, sy, sz, random.nextFloat() * 360F, 0.0F);
		if (!another.checkSpawnRules(level, MobSpawnType.MOB_SUMMONED)) {
			another.discard();
			return false;
		}
		level.addFreshEntity(another);
		another.spawnAnim();

		return true;
	}

	public static boolean getCanSpawnHere(EntityType<? extends SwarmSpider> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(world, pos, random) && checkMobSpawnRules(entity, world, reason, pos, random);
	}

	public static boolean isValidLightLevel(ServerLevelAccessor world, BlockPos pos, Random random) {
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, (ServerLevel) world) == TFFeature.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(world, pos, random);
	}

	public boolean shouldSpawnMore() {
		return shouldSpawn;
	}

	public void setSpawnMore(boolean flag) {
		this.shouldSpawn = flag;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("SpawnMore", shouldSpawnMore());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setSpawnMore(compound.getBoolean("SpawnMore"));
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 16;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag dataTag) {
		livingData = super.finalizeSpawn(worldIn, difficulty, reason, livingData, dataTag);

		if (this.getFirstPassenger() != null || worldIn.getRandom().nextInt(20) <= difficulty.getDifficulty().getId()) {
			SkeletonDruid druid = TFEntities.SKELETON_DRUID.create(this.level);
			druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
			druid.setBaby(true);
			druid.finalizeSpawn(worldIn, difficulty, MobSpawnType.JOCKEY, null, null);

			if (this.hasPassenger(e -> true)) {
				this.ejectPassengers();
			}

			druid.startRiding(this);
		}

		return livingData;
	}
}
