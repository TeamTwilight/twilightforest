package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.LegacyLandmarkPlacements;

public class SwarmSpider extends Spider {

	protected boolean shouldSpawn = false;

	public SwarmSpider(EntityType<? extends SwarmSpider> type, Level world) {
		this(type, world, true);
	}

	public SwarmSpider(EntityType<? extends SwarmSpider> type, Level world, boolean spawnMore) {
		super(type, world);

		this.setSpawnMore(spawnMore);
		this.xpReward = 2;
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
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true));

		// Remove default spider target player task
		this.targetSelector.availableGoals.removeIf(t -> t.getPriority() == 2 && t.getGoal() instanceof NearestAttackableTargetGoal);
		// Replace with one that doesn't care about light
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.SWARM_SPIDER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.SWARM_SPIDER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SWARM_SPIDER_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.SWARM_SPIDER_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return 0.3F;
	}

	@Override
	public void tick() {
		if (!this.level().isClientSide() && shouldSpawnMore()) {
			int more = 1 + this.getRandom().nextInt(2);
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
		return this.getRandom().nextInt(4) == 0 && super.doHurtTarget(entity);
	}

	protected boolean spawnAnother() {
		SwarmSpider another = new SwarmSpider(TFEntities.SWARM_SPIDER.get(), this.level(), false);

		double sx = this.getX() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
		double sy = this.getY();
		double sz = this.getZ() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
		another.moveTo(sx, sy, sz, this.getRandom().nextFloat() * 360.0F, 0.0F);
		if (!another.checkSpawnRules(this.level(), MobSpawnType.MOB_SUMMONED)) {
			another.discard();
			return false;
		}
		this.level().addFreshEntity(another);
		another.spawnAnim();

		return true;
	}

	public static boolean getCanSpawnHere(EntityType<? extends SwarmSpider> entity, ServerLevelAccessor accessor, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(accessor, pos, random) && checkMobSpawnRules(entity, accessor, reason, pos, random);
	}

	public static boolean isValidLightLevel(ServerLevelAccessor accessor, BlockPos pos, RandomSource random) {
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return LegacyLandmarkPlacements.pickLandmarkForChunk(chunkX, chunkZ, accessor) == TFStructures.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(accessor, pos, random);
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
		compound.putBoolean("SpawnMore", this.shouldSpawnMore());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setSpawnMore(compound.getBoolean("SpawnMore"));
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.5F;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 6;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag dataTag) {
		livingData = super.finalizeSpawn(accessor, difficulty, reason, livingData, dataTag);

		if (this.getFirstPassenger() != null || accessor.getRandom().nextInt(20) <= difficulty.getDifficulty().getId()) {
			SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(this.level());
			druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
			druid.setBaby(true);
			EventHooks.onFinalizeSpawn(druid, accessor, difficulty, MobSpawnType.JOCKEY, null, null);

			if (this.hasPassenger(e -> true)) {
				this.ejectPassengers();
			}

			druid.startRiding(this);
		}

		return livingData;
	}
}
