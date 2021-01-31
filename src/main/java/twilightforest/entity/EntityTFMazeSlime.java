package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class EntityTFMazeSlime extends SlimeEntity {

	private static final AttributeModifier DOUBLE_HEALTH = new AttributeModifier("Maze slime double health", 1, AttributeModifier.Operation.MULTIPLY_BASE);

	public EntityTFMazeSlime(EntityType<? extends EntityTFMazeSlime> type, World world) {
		super(type, world);
	}

	@Override
	public void setSlimeSize(int size, boolean resetHealth) {
		super.setSlimeSize(size, resetHealth);
		this.experienceValue += 3;
	}

	public static boolean getCanSpawnHere(EntityType<EntityTFMazeSlime> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && canSpawnOn(entity, world, reason, pos, random) && MonsterEntity.isValidLightLevel(world, pos, random);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH)/*.applyModifier(DOUBLE_HEALTH) TODO: Move to initial spawn?*/;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return this.isSmallSlime() ? TFSounds.MAZE_SLIME_HURT_SMALL : TFSounds.MAZE_SLIME_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return this.isSmallSlime() ? TFSounds.MAZE_SLIME_DEATH_SMALL : TFSounds.MAZE_SLIME_DEATH;
	   }

	@Override
	protected SoundEvent getSquishSound() {
	      return this.isSmallSlime() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
	   }
	
	@Override
	protected SoundEvent getJumpSound() {
	      return this.isSmallSlime() ? TFSounds.MAZE_SLIME_SQUISH_SMALL : TFSounds.MAZE_SLIME_SQUISH;
	   }

	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

//	@Override
//	protected int getAttackStrength() {
//		return super.getAttackStrength() + 3;
//	}

	@Override
	protected boolean spawnCustomParticles() {
		// [VanillaCopy] from super tick with own particles
		int i = getSlimeSize();
		for (int j = 0; j < i * 8; ++j) {
			float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
			float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
			float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
			float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
			World world = this.world;
			// ParticleTypes ParticleTypes = this.getParticleType();
			double d0 = this.getPosX() + (double) f2;
			double d1 = this.getPosZ() + (double) f3;
			BlockState state = TFBlocks.maze_stone_brick.get().getDefaultState();
			world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
		}
		return true;
	}

	@Override
	protected float getSoundVolume() {
		// OH MY GOD, SHUT UP
		return 0.1F * this.getSlimeSize();
	}
}
