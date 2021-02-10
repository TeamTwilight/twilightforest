package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFFeature;
import twilightforest.TFSounds;

import java.util.Random;

/**
 * The hedge spider is just like a normal spider, but it can spawn in the daytime.
 *
 * @author Ben
 */
public class EntityTFHedgeSpider extends SpiderEntity {

	public EntityTFHedgeSpider(EntityType<? extends EntityTFHedgeSpider> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Remove default spider melee task
		this.goalSelector.goals.removeIf(t -> t.getGoal() instanceof MeleeAttackGoal);

		// Replace with one that doesn't become docile in light
		// [VanillaCopy] based on EntitySpider.AISpiderAttack
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true) {
			@Override
			protected double getAttackReachSqr(LivingEntity attackTarget) {
				return 4.0F + attackTarget.getWidth();
			}
		});

		// Remove default spider target player task
		this.targetSelector.goals.removeIf(t -> t.getPriority() == 2 && t.getGoal() instanceof NearestAttackableTargetGoal);
		// Replace with one that doesn't care about light
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static boolean isValidLightLevel(IServerWorld world, BlockPos pos, Random random) {
		int chunkX = MathHelper.floor(pos.getX()) >> 4;
		int chunkZ = MathHelper.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, (ServerWorld) world) == TFFeature.HEDGE_MAZE
				|| MonsterEntity.isValidLightLevel(world, pos, random);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HEDGE_SPIDER_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.HEDGE_SPIDER_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HEDGE_SPIDER_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(TFSounds.HEDGE_SPIDER_STEP, 0.15F, 1.0F);
	}

	public static boolean canSpawn(EntityType<EntityTFHedgeSpider> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(world, pos, random);
	}
}
