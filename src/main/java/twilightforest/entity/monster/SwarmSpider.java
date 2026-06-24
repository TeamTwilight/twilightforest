package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

public class SwarmSpider extends Spider {

	public SwarmSpider(EntityType<? extends SwarmSpider> type, Level world) {
		super(type, world);
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
	public boolean doHurtTarget(ServerLevel server, Entity entity) {
		return this.getRandom().nextInt(4) == 0 && super.doHurtTarget(server, entity);
	}

	public EntityType<? extends SwarmSpider> getReinforcementType() {
		return TFEntities.SWARM_SPIDER.get();
	}

	public static boolean getCanSpawnHere(EntityType<? extends SwarmSpider> entity, ServerLevelAccessor accessor, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(accessor, pos, random) && checkMobSpawnRules(entity, accessor, reason, pos, random);
	}

	public static boolean isValidLightLevel(ServerLevelAccessor accessor, BlockPos pos, RandomSource random) {
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return LegacyLandmarkPlacements.pickLandmarkForChunk(chunkX, chunkZ, accessor) == TFStructures.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(accessor, pos, random);
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
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(accessor, difficulty, reason, data);
		this.summonJockey(accessor, difficulty);
		return data;
	}

	public void summonJockey(ServerLevelAccessor accessor, DifficultyInstance difficulty) {
		if (this.getFirstPassenger() != null || accessor.getRandom().nextInt(200) == 0) {
			if (this.level() instanceof ServerLevel server) {
				SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(server, EntitySpawnReason.JOCKEY);
				if (druid != null) {
					druid.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
					druid.setBaby(true);
					EventHooks.finalizeMobSpawn(druid, accessor, difficulty, EntitySpawnReason.JOCKEY, null);

					if (this.hasPassenger(e -> true)) this.ejectPassengers();
					druid.startRiding(this);
				}
			}
		}
	}
}
