package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.goal.HoverBeamGoal;
import twilightforest.entity.ai.goal.HoverSummonGoal;
import twilightforest.entity.ai.goal.HoverThenDropGoal;
import twilightforest.entity.monster.IceCrystal;
import twilightforest.init.*;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.WorldUtil;

import java.util.List;

public class SnowQueen extends BaseTFBoss implements IBreathAttacker {

	private static final int MAX_SUMMONS = 6;
	private static final EntityDataAccessor<Boolean> BEAM_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> PHASE_FLAG = SynchedEntityData.defineId(SnowQueen.class, EntityDataSerializers.BYTE);

	private static final int MAX_DAMAGE_WHILE_BEAMING = 25;
	private static final float BREATH_DAMAGE = 4.0F;

	public enum Phase {SUMMON, DROP, BEAM}

	public final SnowQueenIceShield[] iceArray = new SnowQueenIceShield[7];

	private int summonsRemaining = 0;
	private int successfulDrops;
	private int maxDrops;
	private int damageWhileBeaming;

	public SnowQueen(EntityType<? extends SnowQueen> type, Level level) {
		super(type, level);

		for (int i = 0; i < this.iceArray.length; i++) {
			this.iceArray[i] = new SnowQueenIceShield(this);
		}

		this.setCurrentPhase(Phase.SUMMON);

		this.xpReward = 317;
		this.moveControl = new FlyingMoveControl(this, 10, true);
		this.setNoGravity(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new HoverSummonGoal(this));
		this.goalSelector.addGoal(2, new HoverThenDropGoal(this, 80, 20));
		this.goalSelector.addGoal(3, new HoverBeamGoal(this, 80, 100));
		this.addRestrictionGoals(this, this.goalSelector);
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
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.FLYING_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 7.0D)
			.add(Attributes.FOLLOW_RANGE, 40.0D)
			.add(Attributes.MAX_HEALTH, 200.0D)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BEAM_FLAG, false);
		builder.define(PHASE_FLAG, (byte) 0);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.SNOW_QUEEN_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.SNOW_QUEEN_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SNOW_QUEEN_DEATH.get();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.level().isClientSide()) {
			this.spawnParticles();
		}
	}

	private void spawnParticles() {
		// make snow particles
		for (int i = 0; i < 3; i++) {
			float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
			float py = this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
			float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;

			this.level().addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0.0D, 0.0D, 0.0D);
		}

		// during drop phase, all the ice blocks should make particles
		if (this.getCurrentPhase() == Phase.DROP) {
			for (Entity ice : this.iceArray) {
				float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
				float py = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
				float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;

				this.level().addParticle(TFParticleType.SNOW_WARNING.get(), ice.xOld + px, ice.yOld + py, ice.zOld + pz, 0.0D, 0.0D, 0.0D);
			}
		}

		// when ice beaming, spew particles
		if (this.isBreathing() && this.isAlive()) {
			Vec3 look = this.getLookAngle();

			double dist = 0.5;
			double px = this.getX() + look.x() * dist;
			double py = this.getY() + 1.7F + look.y() * dist;
			double pz = this.getZ() + look.z() * dist;

			for (int i = 0; i < 10; i++) {
				double dx = look.x();
				double dy = 0;
				double dz = look.z();

				double spread = 2.0D + this.getRandom().nextDouble() * 2.5D;
				double velocity = 2.0D + this.getRandom().nextDouble() * 0.15D;

				// beeeam
				dx += this.getRandom().nextGaussian() * 0.0075D * spread;
				dy += this.getRandom().nextGaussian() * 0.0075D * spread;
				dz += this.getRandom().nextGaussian() * 0.0075D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				this.level().addParticle(TFParticleType.ICE_BEAM.get(), px, py, pz, dx, dy, dz);
			}
		}
	}

	@Override
	public void tick() {
		this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() - 0.05D, this.getDeltaMovement().z());
		super.tick();

		for (int i = 0; i < this.iceArray.length; i++) {
			this.iceArray[i].tick();
			if (i < this.iceArray.length - 1) {
				// set block position
				Vec3 blockPos = this.getIceShieldPosition(i);

				this.iceArray[i].setPos(blockPos.x(), blockPos.y(), blockPos.z());
			} else {
				// last block beneath
				this.iceArray[i].setPos(this.getX(), this.getY() - 1, this.getZ());
			}
			this.iceArray[i].setYRot(this.getIceShieldAngle(i));

			// collide things with the block
			if (this.level() instanceof ServerLevel level) {
				this.applyShieldCollisions(level, this.iceArray[i]);
			}
		}

		// death animation
		if (this.deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.level().addParticle(this.getRandom().nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.POOF,
					(this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(),
					this.getY() + this.getRandom().nextFloat() * this.getBbHeight(),
					(this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - this.getBbWidth(), d, d1, d2);
			}
		}
	}

	private void applyShieldCollisions(ServerLevel level, Entity collider) {
		List<Entity> list = this.level().getEntities(collider, collider.getBoundingBox().inflate(-0.2F, -0.2F, -0.2F));

		for (Entity collided : list) {
			if (collided.isPushable()) {
				this.applyShieldCollision(level, collider, collided);
			}
		}
	}

	/**
	 * Do the effect where the shield hits something
	 */
	private void applyShieldCollision(ServerLevel level, Entity collider, Entity collided) {
		if (collided != this) {
			collided.push(collider);
			if (collided instanceof LivingEntity && this.doHurtTarget(level, collided)) {
				Vec3 motion = collided.getDeltaMovement();
				collided.setDeltaMovement(motion.x(), motion.y() + 0.4, motion.z());
				this.playSound(TFSounds.SNOW_QUEEN_ATTACK.get(), 1.0F, 1.0F);
			}
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		DamageSource source = this.getCurrentPhase() == Phase.DROP ? TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.SQUISH, this, TFEntities.SNOW_QUEEN.get()) : this.level().damageSources().mobAttack(this);
		return EntityUtil.properlyApplyCustomDamageSource(level, this, entity, source, null);
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);

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
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		boolean result = super.hurtServer(level, source, damage);

		if (result && this.getCurrentPhase() == Phase.BEAM) {
			this.damageWhileBeaming += (int) damage;
		}
		return result;
	}

	private Vec3 getIceShieldPosition(int idx) {
		return this.getIceShieldPosition(this.getIceShieldAngle(idx), 1.0F);
	}

	private float getIceShieldAngle(int idx) {
		return 60F * idx + (this.tickCount * 5.0F);
	}

	private Vec3 getIceShieldPosition(float angle, float distance) {
		double dx = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dz = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vec3(this.getX() + dx, this.getY() + this.getShieldYOffset(), this.getZ() + dz);
	}

	private double getShieldYOffset() {
		return 0.1F;
	}

	public void destroyBlocksInAABB(ServerLevel level, AABB box) {
		if (EventHooks.canEntityGrief(level, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				BlockState state = this.level().getBlockState(pos);
				if (state.is(BlockTags.ICE)) {
					this.level().destroyBlock(pos, false);
					this.gameEvent(GameEvent.BLOCK_DESTROY);
				}
			}
		}
	}

	@Override
	public boolean isBreathing() {
		return this.getEntityData().get(BEAM_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		this.getEntityData().set(BEAM_FLAG, flag);
	}

	public Phase getCurrentPhase() {
		return Phase.values()[this.getEntityData().get(PHASE_FLAG)];
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.getEntityData().set(PHASE_FLAG, (byte) currentPhase.ordinal());

		// set variables for current phase
		if (currentPhase == Phase.SUMMON) {
			this.setSummonsRemaining(MAX_SUMMONS);
		}
		if (currentPhase == Phase.DROP) {
			this.successfulDrops = 0;
			this.maxDrops = 2 + this.getRandom().nextInt(3);
		}
		if (currentPhase == Phase.BEAM) {
			this.damageWhileBeaming = 0;
		}
	}

	public int getSummonsRemaining() {
		return this.summonsRemaining;
	}

	public void setSummonsRemaining(int summonsRemaining) {
		this.summonsRemaining = summonsRemaining;
	}

	public void summonMinionAt(LivingEntity targetedEntity) {
		IceCrystal minion = TFEntities.ICE_CRYSTAL.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		minion.absMoveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

		this.level().addFreshEntity(minion);

		for (int i = 0; i < 100; i++) {
			double attemptX;
			double attemptY;
			double attemptZ;
			if (this.isRestrictionPointValid(this.level().dimension())) {
				BlockPos home = this.getRestrictionPoint().pos();
				attemptX = home.getX() + this.getRandom().nextGaussian() * 3.0D;
				attemptY = home.getY() + this.getRandom().nextGaussian() * 2.0D;
				attemptZ = home.getZ() + this.getRandom().nextGaussian() * 3.0D;
			} else {
				attemptX = targetedEntity.getX() + this.getRandom().nextGaussian() * 6.0D;
				attemptY = targetedEntity.getY() + this.getRandom().nextGaussian() * 8.0D;
				attemptZ = targetedEntity.getZ() + this.getRandom().nextGaussian() * 6.0D;
			}
			if (minion.randomTeleport(attemptX, attemptY, attemptZ, true)) {
				this.gameEvent(GameEvent.ENTITY_PLACE, minion);
				break;
			}
		}

		minion.setTarget(targetedEntity);
		minion.setToDieIn30Seconds(); // don't stick around

		this.summonsRemaining--;
	}

	public int countMyMinions() {
		return this.level().getEntitiesOfClass(IceCrystal.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(32.0D, 16.0D, 32.0D)).size();
	}

	public void incrementSuccessfulDrops() {
		this.successfulDrops++;
	}

	@Override
	public void doBreathAttack(ServerLevel level, Entity target) {
		target.hurtServer(level, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.CHILLING_BREATH, this, TFEntities.SNOW_QUEEN.get()), BREATH_DAMAGE);
		// TODO: slow target?
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		TFPart.assignPartIDs(this);
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return this.iceArray;
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.AURORA_PALACE;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return TFBlocks.TWILIGHT_OAK_CHEST.get();
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get();
	}

	@Override
	public int getBossBarColor() {
		return 0x8CF0F0;
	}
}
