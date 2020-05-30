package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.TFFeature;

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

	public static boolean isValidLightLevel(IWorld world, BlockPos pos, Random random) {
		int chunkX = MathHelper.floor(pos.getX()) >> 4;
		int chunkZ = MathHelper.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, world.getWorld()) == TFFeature.HEDGE_MAZE
				|| MonsterEntity.isValidLightLevel(world, pos, random);
	}

	public static boolean canSpawn(EntityType<EntityTFHedgeSpider> entity, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL &&
				isValidLightLevel(world, pos, random);
	}
}
