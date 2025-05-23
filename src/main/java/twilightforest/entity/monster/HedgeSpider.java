package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

/**
 * The hedge spider is just like a normal spider, but it can spawn in the daytime.
 *
 * @author Ben
 */
public class HedgeSpider extends Spider {

	public HedgeSpider(EntityType<? extends HedgeSpider> type, Level world) {
		super(type, world);
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

	public static boolean isValidLightLevel(ServerLevelAccessor accessor, BlockPos pos, RandomSource random) {
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return LegacyLandmarkPlacements.pickLandmarkForChunk(chunkX, chunkZ, accessor) == TFStructures.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(accessor, pos, random);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HEDGE_SPIDER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HEDGE_SPIDER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HEDGE_SPIDER_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.HEDGE_SPIDER_STEP.get(), 0.15F, 1.0F);
	}

	public static boolean checkSpiderSpawnRules(EntityType<HedgeSpider> type, ServerLevelAccessor accessor, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(accessor, pos, random);
	}
}
