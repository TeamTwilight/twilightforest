package twilightforest.entity.monster;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class MazeSlime extends Slime {

	private static final AttributeModifier DOUBLE_HEALTH = new AttributeModifier("Maze slime double health", 1, AttributeModifier.Operation.MULTIPLY_BASE);

	public MazeSlime(EntityType<? extends MazeSlime> type, Level world) {
		super(type, world);
	}

	@Override
	public void setSize(int size, boolean resetHealth) {
		super.setSize(size, resetHealth);
		this.xpReward += 3;
	}

	public static boolean getCanSpawnHere(EntityType<MazeSlime> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entity, world, reason, pos, random) && Monster.isDarkEnoughToSpawn(world, pos, random);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
		health.addPermanentModifier(DOUBLE_HEALTH);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return this.isTiny() ? TFSounds.MAZE_SLIME_HURT_SMALL : TFSounds.MAZE_SLIME_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return this.isTiny() ? TFSounds.MAZE_SLIME_DEATH_SMALL : TFSounds.MAZE_SLIME_DEATH;
	   }

	@Override
	protected SoundEvent getSquishSound() {
	      return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
	   }
	
	@Override
	protected SoundEvent getJumpSound() {
	      return this.isTiny() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
	   }

	@Override
	protected boolean isDealsDamage() {
		return true;
	}

	@Override
	protected boolean spawnCustomParticles() {
		// [VanillaCopy] from super tick with own particles
		int i = getSize();
		for (int j = 0; j < i * 8; ++j) {
			float f = this.random.nextFloat() * ((float) Math.PI * 2F);
			float f1 = this.random.nextFloat() * 0.5F + 0.5F;
			float f2 = Mth.sin(f) * i * 0.5F * f1;
			float f3 = Mth.cos(f) * i * 0.5F * f1;
			Level world = this.level;
			double d0 = this.getX() + f2;
			double d1 = this.getZ() + f3;
			BlockState state = TFBlocks.MAZESTONE_BRICK.get().defaultBlockState();
			world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
		}
		return true;
	}

	@Override
	protected float getSoundVolume() {
		// OH MY GOD, SHUT UP
		return 0.1F * this.getSize();
	}
}
