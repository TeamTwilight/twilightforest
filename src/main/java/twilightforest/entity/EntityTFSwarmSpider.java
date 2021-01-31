package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
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

public class EntityTFSwarmSpider extends SpiderEntity {

	protected boolean shouldSpawn = false;

	public EntityTFSwarmSpider(EntityType<? extends EntityTFSwarmSpider> type, World world) {
		this(type, world, true);
	}

	public EntityTFSwarmSpider(EntityType<? extends EntityTFSwarmSpider> type, World world, boolean spawnMore) {
		super(type, world);

		setSpawnMore(spawnMore);
		experienceValue = 2;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return SpiderEntity.func_234305_eI_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 3.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D);
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
	public float getEyeHeight(Pose pose) {
		return 0.3F;
	}

	@Override
	public void tick() {
		if (!world.isRemote && shouldSpawnMore()) {
			int more = 1 + rand.nextInt(2);
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
	public boolean attackEntityAsMob(Entity entity) {
		return rand.nextInt(4) == 0 && super.attackEntityAsMob(entity);
	}

	protected boolean spawnAnother() {
		EntityTFSwarmSpider another = new EntityTFSwarmSpider(TFEntities.swarm_spider, world, false);

		double sx = getPosX() + (rand.nextBoolean() ? 0.9 : -0.9);
		double sy = getPosY();
		double sz = getPosZ() + (rand.nextBoolean() ? 0.9 : -0.9);
		another.setLocationAndAngles(sx, sy, sz, rand.nextFloat() * 360F, 0.0F);
		if (!another.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
			another.remove();
			return false;
		}
		world.addEntity(another);
		another.spawnExplosionParticle();

		return true;
	}

	public static boolean getCanSpawnHere(EntityType<? extends EntityTFSwarmSpider> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(world, pos, random) && canSpawnOn(entity, world, reason, pos, random);
	}

	public static boolean isValidLightLevel(IServerWorld world, BlockPos pos, Random random) {
		int chunkX = MathHelper.floor(pos.getX()) >> 4;
		int chunkZ = MathHelper.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, (ServerWorld) world) == TFFeature.HEDGE_MAZE || MonsterEntity.isValidLightLevel(world, pos, random);
	}

	public boolean shouldSpawnMore() {
		return shouldSpawn;
	}

	public void setSpawnMore(boolean flag) {
		this.shouldSpawn = flag;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("SpawnMore", shouldSpawnMore());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		setSpawnMore(compound.getBoolean("SpawnMore"));
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 16;
	}
}
