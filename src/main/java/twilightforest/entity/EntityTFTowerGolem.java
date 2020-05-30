package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.SoundEvents;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityTFTowerGolem extends MonsterEntity {

	private int attackTimer;

	public EntityTFTowerGolem(EntityType<? extends EntityTFTowerGolem> type, World world) {
		super(type, world);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		this.attackTimer = 10;
		this.world.setEntityState(this, (byte) 4);
		boolean attackSuccess = super.attackEntityAsMob(entity);

		if (attackSuccess) {
			entity.getMotion().add(0.0D, 0.4000000059604645D, 0.0D);
		}

		return attackSuccess;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		// [VanillaCopy] last half of EntityIronGolem.onLivingUpdate
		if (this.getMotion().getX() * this.getMotion().getX() + this.getMotion().getZ() * this.getMotion().getZ() > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY() - 0.20000000298023224D);
			int k = MathHelper.floor(this.getZ());
			BlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));

			if (iblockstate.getMaterial() != Material.AIR) {
				this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), this.getX() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), this.getBoundingBox().minY + 0.1D, this.getZ() + ((double) this.rand.nextFloat() - 0.5D) * (double) this.getWidth(), 4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D, ((double) this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
		// End copy

		if (this.rand.nextBoolean()) {
			this.world.addParticle(new RedstoneParticleData(1.0F, 0.0F, 0.0F, 1.0F), this.getX() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getY() + this.rand.nextDouble() * (double) this.getHeight() - 0.25D, this.getZ() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 0, 0, 0);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 4) {
			this.attackTimer = 10;
			this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
		} else {
			super.handleStatusUpdate(id);
		}
	}

	public int getAttackTimer() {
		return this.attackTimer;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 16;
	}
}
