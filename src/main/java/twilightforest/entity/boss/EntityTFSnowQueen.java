package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.EntityAITFHoverBeam;
import twilightforest.entity.ai.EntityAITFHoverSummon;
import twilightforest.entity.ai.EntityAITFHoverThenDrop;
import twilightforest.enums.BossVariant;
import twilightforest.util.WorldUtil;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.List;

public class EntityTFSnowQueen extends MonsterEntity implements IBreathAttacker {

	private static final int MAX_SUMMONS = 6;
	private static final DataParameter<Boolean> BEAM_FLAG = EntityDataManager.createKey(EntityTFSnowQueen.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> PHASE_FLAG = EntityDataManager.createKey(EntityTFSnowQueen.class, DataSerializers.BYTE);
	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private static final int MAX_DAMAGE_WHILE_BEAMING = 25;
	private static final float BREATH_DAMAGE = 4.0F;

	public enum Phase {SUMMON, DROP, BEAM}

	public final EntityTFSnowQueenIceShield[] iceArray = new EntityTFSnowQueenIceShield[7];

	private int summonsRemaining = 0;
	private int successfulDrops;
	private int maxDrops;
	private int damageWhileBeaming;

	public EntityTFSnowQueen(EntityType<? extends EntityTFSnowQueen> type, World world) {
		super(type, world);

		for (int i = 0; i < this.iceArray.length; i++) {
			this.iceArray[i] = new EntityTFSnowQueenIceShield(this);
		}

		this.setCurrentPhase(Phase.SUMMON);

		this.isImmuneToFire();
		this.experienceValue = 317;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAITFHoverSummon(this, 1.0D));
		this.goalSelector.addGoal(2, new EntityAITFHoverThenDrop(this, 80, 20));
		this.goalSelector.addGoal(3, new EntityAITFHoverBeam(this, 80, 100));
		this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 40.0D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 200.0D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(BEAM_FLAG, false);
		dataManager.register(PHASE_FLAG, (byte) 0);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.SNOW_QUEEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.SNOW_QUEEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SNOW_QUEEN_DEATH;
	}

	@Override
	public void livingTick() {
		super.livingTick();
		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());
		} else {
			spawnParticles();
		}
	}

	private void spawnParticles() {
		// make snow particles
		for (int i = 0; i < 3; i++) {
			float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
			float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
			float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

			world.addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
		}

		// during drop phase, all the ice blocks should make particles
		if (this.getCurrentPhase() == Phase.DROP) {
			for (Entity ice : this.iceArray) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float py = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;

				world.addParticle(TFParticleType.SNOW_WARNING.get(), ice.lastTickPosX + px, ice.lastTickPosY + py, ice.lastTickPosZ + pz, 0, 0, 0);
			}
		}

		// when ice beaming, spew particles
		if (isBreathing() && this.isAlive()) {
			Vector3d look = this.getLookVec();

			double dist = 0.5;
			double px = this.getPosX() + look.x * dist;
			double py = this.getPosY() + 1.7F + look.y * dist;
			double pz = this.getPosZ() + look.z * dist;

			for (int i = 0; i < 10; i++) {
				double dx = look.x;
				double dy = 0;//look.y;
				double dz = look.z;

				double spread = 2 + this.getRNG().nextDouble() * 2.5;
				double velocity = 2.0 + this.getRNG().nextDouble() * 0.15;

				// beeeam
				dx += this.getRNG().nextGaussian() * 0.0075D * spread;
				dy += this.getRNG().nextGaussian() * 0.0075D * spread;
				dz += this.getRNG().nextGaussian() * 0.0075D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				world.addParticle(TFParticleType.ICE_BEAM.get(), px, py, pz, dx, dy, dz);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		for (int i = 0; i < this.iceArray.length; i++) {
			if (i < this.iceArray.length - 1) {
				// set block position
				Vector3d blockPos = this.getIceShieldPosition(i);

				this.iceArray[i].setPosition(blockPos.x, blockPos.y, blockPos.z);
				this.iceArray[i].rotationYaw = this.getIceShieldAngle(i);
			} else {
				// last block beneath
				this.iceArray[i].setPosition(this.getPosX(), this.getPosY() - 1, this.getPosZ());
				this.iceArray[i].rotationYaw = this.getIceShieldAngle(i);
			}

			// collide things with the block
			if (!world.isRemote) {
				this.applyShieldCollisions(this.iceArray[i]);
			}
		}

		// death animation
		if (deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				world.addParticle(rand.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION, (getPosX() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), getPosY() + rand.nextFloat() * getHeight(), (getPosZ() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), d, d1, d2);
			}
		}
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (getHomePosition() != BlockPos.ZERO) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.get().getDefaultState().with(BlockTFBossSpawner.VARIANT, BossVariant.SNOW_QUEEN));
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		// mark the tower as defeated
		if (!world.isRemote) {
			TFGenerationSettings.markStructureConquered(world, this.getPosition(), TFFeature.ICE_TOWER);
		}
	}

	private void applyShieldCollisions(Entity collider) {
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(collider, collider.getBoundingBox().grow(-0.2F, -0.2F, -0.2F));

		for (Entity collided : list) {
			if (collided.canBePushed()) {
				applyShieldCollision(collider, collided);
			}
		}
	}

	/**
	 * Do the effect where the shield hits something
	 */
	private void applyShieldCollision(Entity collider, Entity collided) {
		if (collided != this) {
			collided.applyEntityCollision(collider);
			if (collided instanceof LivingEntity && super.attackEntityAsMob(collided)) {
				Vector3d motion = collided.getMotion();
				collided.setMotion(motion.x, motion.y + 0.4, motion.z);
				this.playSound(TFSounds.SNOW_QUEEN_ATTACK, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		// switch phases
		if (this.getCurrentPhase() == Phase.SUMMON && this.getSummonsRemaining() == 0 && this.countMyMinions() <= 0) {
			this.setCurrentPhase(Phase.DROP);
		}
		if (this.getCurrentPhase() == Phase.DROP && this.successfulDrops >= this.maxDrops) {
			this.setCurrentPhase(Phase.BEAM);
		}
		if (this.getCurrentPhase() == Phase.BEAM && this.damageWhileBeaming >= MAX_DAMAGE_WHILE_BEAMING) {
			this.setCurrentPhase(Phase.SUMMON);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		boolean result = super.attackEntityFrom(source, damage);

		if (result && this.getCurrentPhase() == Phase.BEAM) {
			this.damageWhileBeaming += damage;
		}

		return result;

	}

	private Vector3d getIceShieldPosition(int idx) {
		return this.getIceShieldPosition(getIceShieldAngle(idx), 1F);
	}

	private float getIceShieldAngle(int idx) {
		return 60F * idx + (this.ticksExisted * 5F);
	}

	private Vector3d getIceShieldPosition(float angle, float distance) {
		double dx = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dz = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vector3d(this.getPosX() + dx, this.getPosY() + this.getShieldYOffset(), this.getPosZ() + dz);
	}

	private double getShieldYOffset() {
		return 0.1F;
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return iceArray;
	}

	public void destroyBlocksInAABB(AxisAlignedBB box) {
		if (ForgeEventFactory.getMobGriefingEvent(world, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				BlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.ICE || state.getBlock() == Blocks.PACKED_ICE) {
					world.destroyBlock(pos, false);
				}
			}
		}
	}

	@Override
	public boolean isBreathing() {
		return dataManager.get(BEAM_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		dataManager.set(BEAM_FLAG, flag);
	}

	public Phase getCurrentPhase() {
		return Phase.values()[dataManager.get(PHASE_FLAG)];
	}

	public void setCurrentPhase(Phase currentPhase) {
		dataManager.set(PHASE_FLAG, (byte) currentPhase.ordinal());

		// set variables for current phase
		if (currentPhase == Phase.SUMMON) {
			this.setSummonsRemaining(MAX_SUMMONS);
		}
		if (currentPhase == Phase.DROP) {
			this.successfulDrops = 0;
			this.maxDrops = 2 + this.rand.nextInt(3);
		}
		if (currentPhase == Phase.BEAM) {
			this.damageWhileBeaming = 0;
		}
	}

	public int getSummonsRemaining() {
		return summonsRemaining;
	}

	public void setSummonsRemaining(int summonsRemaining) {
		this.summonsRemaining = summonsRemaining;
	}

	public void summonMinionAt(LivingEntity targetedEntity) {
		EntityTFIceCrystal minion = new EntityTFIceCrystal(world);
		minion.setPositionAndRotation(getPosX(), getPosY(), getPosZ(), 0, 0);

		world.addEntity(minion);

		for (int i = 0; i < 100; i++) {
			double attemptX;
			double attemptY;
			double attemptZ;
			if (getHomePosition() != BlockPos.ZERO) {
				BlockPos home = getHomePosition();
				attemptX = home.getX() + rand.nextGaussian() * 7D;
				attemptY = home.getY() + rand.nextGaussian() * 2D;
				attemptZ = home.getZ() + rand.nextGaussian() * 7D;
			} else {
				attemptX = targetedEntity.getPosX() + rand.nextGaussian() * 16D;
				attemptY = targetedEntity.getPosY() + rand.nextGaussian() * 8D;
				attemptZ = targetedEntity.getPosZ() + rand.nextGaussian() * 16D;
			}
			if (minion.attemptTeleport(attemptX, attemptY, attemptZ, true)) {
				break;
			}
		}

		minion.setAttackTarget(targetedEntity);
		minion.setToDieIn30Seconds(); // don't stick around

		this.summonsRemaining--;
	}

	public int countMyMinions() {
		return world.getEntitiesWithinAABB(EntityTFIceCrystal.class, new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 16.0D, 32.0D)).size();
	}

	public void incrementSuccessfulDrops() {
		this.successfulDrops++;
	}

	@Override
	public void doBreathAttack(Entity target) {
		target.attackEntityFrom(DamageSource.causeMobDamage(this), BREATH_DAMAGE);
		// TODO: slow target?
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (this.hasCustomName())
			this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
