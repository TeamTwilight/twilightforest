package twilightforest.entity.boss;

import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.TFPart;
import twilightforest.entity.monster.IceCrystal;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.ai.HoverBeamGoal;
import twilightforest.entity.ai.HoverSummonGoal;
import twilightforest.entity.ai.HoverThenDropGoal;
import twilightforest.util.TFDamageSources;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class SnowQueen extends Monster implements IBreathAttacker {

	private static final int MAX_SUMMONS = 6;
	private static final EntityDataAccessor<Boolean> BEAM_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> PHASE_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BYTE);
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
	private static final int MAX_DAMAGE_WHILE_BEAMING = 25;
	private static final float BREATH_DAMAGE = 4.0F;

	public enum Phase {SUMMON, DROP, BEAM}

	public final SnowQueenIceShield[] iceArray = new SnowQueenIceShield[7];

	private int summonsRemaining = 0;
	private int successfulDrops;
	private int maxDrops;
	private int damageWhileBeaming;

	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public SnowQueen(EntityType<? extends SnowQueen> type, Level world) {
		super(type, world);

		for (int i = 0; i < this.iceArray.length; i++) {
			this.iceArray[i] = new SnowQueenIceShield(this);
		}

		this.setCurrentPhase(Phase.SUMMON);

		this.fireImmune();
		this.xpReward = 317;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new HoverSummonGoal(this));
		this.goalSelector.addGoal(2, new HoverThenDropGoal(this, 80, 20));
		this.goalSelector.addGoal(3, new HoverBeamGoal(this, 80, 100));
		this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 40.0D)
				.add(Attributes.MAX_HEALTH, 200.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(BEAM_FLAG, false);
		entityData.define(PHASE_FLAG, (byte) 0);
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
	public void aiStep() {
		super.aiStep();
		if (!level.isClientSide) {
			bossInfo.setProgress(getHealth() / getMaxHealth());
		} else {
			spawnParticles();
		}
	}

	private void spawnParticles() {
		// make snow particles
		for (int i = 0; i < 3; i++) {
			float px = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;
			float py = this.getEyeHeight() + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;
			float pz = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;

			level.addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
		}

		// during drop phase, all the ice blocks should make particles
		if (this.getCurrentPhase() == Phase.DROP) {
			for (Entity ice : this.iceArray) {
				float px = (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;
				float py = (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;
				float pz = (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;

				level.addParticle(TFParticleType.SNOW_WARNING.get(), ice.xOld + px, ice.yOld + py, ice.zOld + pz, 0, 0, 0);
			}
		}

		// when ice beaming, spew particles
		if (isBreathing() && this.isAlive()) {
			Vec3 look = this.getLookAngle();

			double dist = 0.5;
			double px = this.getX() + look.x * dist;
			double py = this.getY() + 1.7F + look.y * dist;
			double pz = this.getZ() + look.z * dist;

			for (int i = 0; i < 10; i++) {
				double dx = look.x;
				double dy = 0;//look.y;
				double dz = look.z;

				double spread = 2 + this.getRandom().nextDouble() * 2.5;
				double velocity = 2.0 + this.getRandom().nextDouble() * 0.15;

				// beeeam
				dx += this.getRandom().nextGaussian() * 0.0075D * spread;
				dy += this.getRandom().nextGaussian() * 0.0075D * spread;
				dz += this.getRandom().nextGaussian() * 0.0075D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				level.addParticle(TFParticleType.ICE_BEAM.get(), px, py, pz, dx, dy, dz);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		for (int i = 0; i < this.iceArray.length; i++) {
			iceArray[i].tick();
			if (i < this.iceArray.length - 1) {
				// set block position
				Vec3 blockPos = this.getIceShieldPosition(i);

				this.iceArray[i].setPos(blockPos.x, blockPos.y, blockPos.z);
			} else {
				// last block beneath
				this.iceArray[i].setPos(this.getX(), this.getY() - 1, this.getZ());
			}
			this.iceArray[i].setYRot(this.getIceShieldAngle(i));

			// collide things with the block
			if (!level.isClientSide) {
				this.applyShieldCollisions(this.iceArray[i]);
			}
		}

		// death animation
		if (deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				level.addParticle(random.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION, (getX() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), getY() + random.nextFloat() * getBbHeight(), (getZ() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), d, d1, d2);
			}
		}
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (getRestrictCenter() != BlockPos.ZERO) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, this.blockPosition(), TFFeature.ICE_TOWER);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}
		}
	}

	private void applyShieldCollisions(Entity collider) {
		List<Entity> list = this.level.getEntities(collider, collider.getBoundingBox().inflate(-0.2F, -0.2F, -0.2F));

		for (Entity collided : list) {
			if (collided.isPushable()) {
				applyShieldCollision(collider, collided);
			}
		}
	}

	/**
	 * Do the effect where the shield hits something
	 */
	private void applyShieldCollision(Entity collider, Entity collided) {
		if (collided != this) {
			collided.push(collider);
			if (collided instanceof LivingEntity && super.doHurtTarget(collided)) {
				Vec3 motion = collided.getDeltaMovement();
				collided.setDeltaMovement(motion.x, motion.y + 0.4, motion.z);
				this.playSound(TFSounds.SNOW_QUEEN_ATTACK, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

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
	public boolean hurt(DamageSource source, float damage) {
		boolean result = super.hurt(TFDamageSources.SQUISH, damage);

		if (result && this.getCurrentPhase() == Phase.BEAM) {
			this.damageWhileBeaming += damage;
		}

		if(source.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
			hurtBy.add(player);
		}
		return result;

	}

	private Vec3 getIceShieldPosition(int idx) {
		return this.getIceShieldPosition(getIceShieldAngle(idx), 1F);
	}

	private float getIceShieldAngle(int idx) {
		return 60F * idx + (this.tickCount * 5F);
	}

	private Vec3 getIceShieldPosition(float angle, float distance) {
		double dx = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dz = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vec3(this.getX() + dx, this.getY() + this.getShieldYOffset(), this.getZ() + dz);
	}

	private double getShieldYOffset() {
		return 0.1F;
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource cause) {
		return false;
	}

	public void destroyBlocksInAABB(AABB box) {
		if (ForgeEventFactory.getMobGriefingEvent(level, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				BlockState state = level.getBlockState(pos);
				if (state.getBlock() == Blocks.ICE || state.getBlock() == Blocks.PACKED_ICE) {
					level.destroyBlock(pos, false);
				}
			}
		}
	}

	@Override
	public boolean isBreathing() {
		return entityData.get(BEAM_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		entityData.set(BEAM_FLAG, flag);
	}

	public Phase getCurrentPhase() {
		return Phase.values()[entityData.get(PHASE_FLAG)];
	}

	public void setCurrentPhase(Phase currentPhase) {
		entityData.set(PHASE_FLAG, (byte) currentPhase.ordinal());

		// set variables for current phase
		if (currentPhase == Phase.SUMMON) {
			this.setSummonsRemaining(MAX_SUMMONS);
		}
		if (currentPhase == Phase.DROP) {
			this.successfulDrops = 0;
			this.maxDrops = 2 + this.random.nextInt(3);
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
		IceCrystal minion = new IceCrystal(level);
		minion.absMoveTo(getX(), getY(), getZ(), 0, 0);

		level.addFreshEntity(minion);

		for (int i = 0; i < 100; i++) {
			double attemptX;
			double attemptY;
			double attemptZ;
			if (getRestrictCenter() != BlockPos.ZERO) {
				BlockPos home = getRestrictCenter();
				attemptX = home.getX() + random.nextGaussian() * 7D;
				attemptY = home.getY() + random.nextGaussian() * 2D;
				attemptZ = home.getZ() + random.nextGaussian() * 7D;
			} else {
				attemptX = targetedEntity.getX() + random.nextGaussian() * 16D;
				attemptY = targetedEntity.getY() + random.nextGaussian() * 8D;
				attemptZ = targetedEntity.getZ() + random.nextGaussian() * 16D;
			}
			if (minion.randomTeleport(attemptX, attemptY, attemptZ, true)) {
				break;
			}
		}

		minion.setTarget(targetedEntity);
		minion.setToDieIn30Seconds(); // don't stick around

		this.summonsRemaining--;
	}

	public int countMyMinions() {
		return level.getEntitiesOfClass(IceCrystal.class, new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).inflate(32.0D, 16.0D, 32.0D)).size();
	}

	public void incrementSuccessfulDrops() {
		this.successfulDrops++;
	}

	@Override
	public void doBreathAttack(Entity target) {
		target.hurt(TFDamageSources.CHILLING_BREATH, BREATH_DAMAGE);
		// TODO: slow target?
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName())
			this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddMobPacket p_147206_) {
		super.recreateFromPacket(p_147206_);
		TFPart.assignPartIDs(this);
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return iceArray;
	}
}
