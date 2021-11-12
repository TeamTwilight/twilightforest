package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFPart;
import twilightforest.util.EntityUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hydra extends Mob implements Enemy {

	private static final int TICKS_BEFORE_HEALING = 1000;
	private static final int HEAD_RESPAWN_TICKS = 100;
	private static final int HEAD_MAX_DAMAGE = 120;
	private static final float ARMOR_MULTIPLIER = 8.0F;
	private static final int MAX_HEALTH = 360;
	private static float HEADS_ACTIVITY_FACTOR = 0.3F;

	private static final int SECONDARY_FLAME_CHANCE = 10;
	private static final int SECONDARY_MORTAR_CHANCE = 16;

	private final HydraPart[] partArray;

	public final int numHeads = 7;
	public final HydraHeadContainer[] hc = new HydraHeadContainer[numHeads];

	public final HydraSmallPart body;
	private final HydraSmallPart leftLeg;
	private final HydraSmallPart rightLeg;
	private final HydraSmallPart tail;
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
	private float randomYawVelocity = 0f;

	private int ticksSinceDamaged = 0;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public Hydra(EntityType<? extends Hydra> type, Level world) {
		super(type, world);

		List<HydraPart> parts = new ArrayList<>();

		body = new HydraSmallPart(this, 6F, 6F);
		leftLeg = new HydraSmallPart(this, 2F, 3F);
		rightLeg = new HydraSmallPart(this, 2F, 3F);
		tail = new HydraSmallPart(this, 6.0f, 2.0f);

		parts.add(body);
		parts.add(leftLeg);
		parts.add(rightLeg);
		parts.add(tail);

		for (int i = 0; i < numHeads; i++) {
			hc[i] = new HydraHeadContainer(this, i, i < 3);
			parts.add(hc[i].headEntity);
			Collections.addAll(parts, hc[i].getNeckArray());
		}

		partArray = parts.toArray(new HydraPart[0]);

		this.noCulling = true;
		this.fireImmune();
		this.xpReward = 511;

	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.MOVEMENT_SPEED, 0.28D);
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
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			level.setBlockAndUpdate(blockPosition().offset(0, 2, 0), TFBlocks.HYDRA_BOSS_SPAWNER.get().defaultBlockState());
			discard();
			for (HydraHeadContainer container : hc) {
				if (container.headEntity != null) {
					container.headEntity.discard();
				}
			}
		} else {
			super.checkDespawn();
		}
	}

	// [Vanilla Copy] from LivingEntity. Hydra doesn't like the one from EntityLiving for whatever reason
	@Override
	protected float tickHeadTurn(float p_110146_1_, float p_110146_2_)
	{
		float f = Mth.wrapDegrees(p_110146_1_ - this.yBodyRot);
		this.yBodyRot += f * 0.3F;
		float f1 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);
		boolean flag = f1 < -90.0F || f1 >= 90.0F;

		if (f1 < -75.0F)
		{
			f1 = -75.0F;
		}

		if (f1 >= 75.0F)
		{
			f1 = 75.0F;
		}

		this.yBodyRot = this.getYRot() - f1;

		if (f1 * f1 > 2500.0F)
		{
			this.yBodyRot += f1 * 0.2F;
		}

		if (flag)
		{
			p_110146_2_ *= -1.0F;
		}

		return p_110146_2_;
	}

	@Override
	public void aiStep() {
		clearFire();
		body.tick();
		leftLeg.tick();
		rightLeg.tick();

		// update all heads (maybe we should change to only active ones
		for (int i = 0; i < numHeads; i++) {
			hc[i].tick();
		}

		if (this.hurtTime > 0) {
			for (int i = 0; i < numHeads; i++) {
				hc[i].setHurtTime(this.hurtTime);
			}
		}

		this.ticksSinceDamaged++;

		if (!this.level.isClientSide && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 5 == 0) {
			this.heal(1);
		}

		// update fight variables for difficulty setting
		setDifficultyVariables();

		super.aiStep();

		// set body part positions
		float angle;
		double dx, dy, dz;

		// body goes behind the actual position of the hydra
		angle = (((yBodyRot + 180) * 3.141593F) / 180F);

		dx = getX() - Mth.sin(angle) * 3.0;
		dy = getY() + 0.1;
		dz = getZ() + Mth.cos(angle) * 3.0;
		body.setPos(dx, dy, dz);

		dx = getX() - Mth.sin(angle) * 10.5;
		dy = getY() + 0.1;
		dz = getZ() + Mth.cos(angle) * 10.5;
		tail.setPos(dx, dy, dz);

		// destroy blocks
		if (!this.level.isClientSide) {
			if (hurtTime == 0) {
				this.collideWithEntities(this.level.getEntities(this, this.body.getBoundingBox()), this.body);
				this.collideWithEntities(this.level.getEntities(this, this.tail.getBoundingBox()), this.tail);
			}

			this.destroyBlocksInAABB(this.body.getBoundingBox());
			this.destroyBlocksInAABB(this.tail.getBoundingBox());

			for (int i = 0; i < numHeads; i++) {
				if (hc[i].headEntity != null && hc[i].isActive()) {
					this.destroyBlocksInAABB(this.hc[i].headEntity.getBoundingBox());
				}
			}

			// smash blocks beneath us too
			if (this.tickCount % 20 == 0) {
				if (isUnsteadySurfaceBeneath()) {
					this.destroyBlocksInAABB(this.getBoundingBox().move(0, -1, 0));

				}
			}

			bossInfo.setProgress(getHealth() / getMaxHealth());
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putByte("NumHeads", (byte) countActiveHeads());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		activateNumberOfHeads(compound.getByte("NumHeads"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}


	// TODO modernize this more (old AI copypasta still kind of here)
	private int numTicksToChaseTarget;

	@Override
	protected void customServerAiStep() {
		xxa = 0.0F;
		zza = 0.0F;
		float f = 48F;

		// kill heads that have taken too much damage
		for (int i = 0; i < numHeads; i++) {
			if (hc[i].isActive() && hc[i].getDamageTaken() > HEAD_MAX_DAMAGE) {
				hc[i].setNextState(HydraHeadContainer.State.DYING);
				hc[i].endCurrentAction();

				// set this head and a random dead head to respawn
				hc[i].setRespawnCounter(HEAD_RESPAWN_TICKS);
				int otherHead = getRandomDeadHead();
				if (otherHead != -1) {
					hc[otherHead].setRespawnCounter(HEAD_RESPAWN_TICKS);
				}
			}
		}

		if (random.nextFloat() < 0.7F) {
			Player entityplayer1 = level.getNearestPlayer(this, f);

			if (entityplayer1 != null && !entityplayer1.isCreative()) {
				setTarget(entityplayer1);
				numTicksToChaseTarget = 100 + random.nextInt(20);
			} else {
				randomYawVelocity = (random.nextFloat() - 0.5F) * 20F;
			}
		}

		if (getTarget() != null) {
			lookAt(getTarget(), 10F, getMaxHeadXRot());

			// have any heads not currently attacking switch to the primary target
			for (int i = 0; i < numHeads; i++) {
				if (!hc[i].isAttacking() && !hc[i].isSecondaryAttacking) {
					hc[i].setTargetEntity(getTarget());
				}
			}

			// let's pick an attack
			if (this.getTarget().isAlive()) {
				float distance = this.getTarget().distanceTo(this);

				if (this.getSensing().hasLineOfSight(this.getTarget())) {
					this.attackEntity(this.getTarget(), distance);
				}
			}

			if (numTicksToChaseTarget-- <= 0 || !getTarget().isAlive() || getTarget().distanceToSqr(this) > f * f) {
				setTarget(null);
			}
		} else {
			if (random.nextFloat() < 0.05F) {
				randomYawVelocity = (random.nextFloat() - 0.5F) * 20F;
			}

			setYRot(getYRot() + randomYawVelocity);
			setXRot(0);

			// TODO: while we are idle, consider having the heads breathe fire on passive mobs

			// set idle heads to no target
			for (int i = 0; i < numHeads; i++) {
				if (hc[i].isIdle()) {
					hc[i].setTargetEntity(null);
				}
			}
		}

		// heads that are free at this point may consider attacking secondary targets
		this.secondaryAttacks();
	}

	private void setDifficultyVariables() {
		if (level.getDifficulty() != Difficulty.HARD) {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.3F;
		} else {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.5F;  // higher is harder
		}
	}

	// TODO: make random
	private int getRandomDeadHead() {
		for (int i = 0; i < numHeads; i++) {
			if (hc[i].canRespawn()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Used when re-loading from save.  Assumes three heads are active and activates more if necessary.
	 */
	private void activateNumberOfHeads(int howMany) {
		int moreHeads = howMany - this.countActiveHeads();

		for (int i = 0; i < moreHeads; i++) {
			int otherHead = getRandomDeadHead();
			if (otherHead != -1) {
				// move directly into not dead
				hc[otherHead].setNextState(HydraHeadContainer.State.IDLE);
				hc[otherHead].endCurrentAction();
			}
		}
	}

	/**
	 * Count timers, and pick an attack against the entity if our timer says go
	 */
	private void attackEntity(Entity target, float distance) {

		int BITE_CHANCE = 10;
		int FLAME_CHANCE = 100;
		int MORTAR_CHANCE = 160;

		boolean targetAbove = target.getBoundingBox().minY > this.getBoundingBox().maxY;

		// three main heads can do these kinds of attacks
		for (int i = 0; i < 3; i++) {
			if (hc[i].isIdle() && !areTooManyHeadsAttacking(i)) {
				if (distance > 4 && distance < 10 && random.nextInt(BITE_CHANCE) == 0 && this.countActiveHeads() > 2 && !areOtherHeadsBiting(i)) {
					hc[i].setNextState(HydraHeadContainer.State.BITE_BEGINNING);
				} else if (distance > 0 && distance < 20 && random.nextInt(FLAME_CHANCE) == 0) {
					hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
				} else if (distance > 8 && distance < 32 && !targetAbove && random.nextInt(MORTAR_CHANCE) == 0) {
					hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
				}
			}
		}

		// heads 4-7 can do everything but bite
		for (int i = 3; i < numHeads; i++) {
			if (hc[i].isIdle() && !areTooManyHeadsAttacking(i)) {
				if (distance > 0 && distance < 20 && random.nextInt(FLAME_CHANCE) == 0) {
					hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
				} else if (distance > 8 && distance < 32 && !targetAbove && random.nextInt(MORTAR_CHANCE) == 0) {
					hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
				}
			}
		}
	}

	private boolean areTooManyHeadsAttacking(int testHead) {
		int otherAttacks = 0;

		for (int i = 0; i < numHeads; i++) {
			if (i != testHead && hc[i].isAttacking()) {
				otherAttacks++;

				// biting heads count triple
				if (hc[i].isBiting()) {
					otherAttacks += 2;
				}
			}
		}

		return otherAttacks >= 1 + (countActiveHeads() * HEADS_ACTIVITY_FACTOR);
	}

	private int countActiveHeads() {
		int count = 0;

		for (int i = 0; i < numHeads; i++) {
			if (hc[i].isActive()) {
				count++;
			}
		}

		return count;
	}

	private boolean areOtherHeadsBiting(int testHead) {
		for (int i = 0; i < numHeads; i++) {
			if (i != testHead && hc[i].isBiting()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called sometime after the main attackEntity routine.  Finds a valid secondary target and has an unoccupied head start an attack against it.
	 * <p>
	 * The center head (head 0) does not make secondary attacks
	 */
	private void secondaryAttacks() {
		for (int i = 0; i < numHeads; i++) {
			if (hc[i].headEntity == null) {
				return;
			}
		}

		LivingEntity secondaryTarget = findSecondaryTarget(20);

		if (secondaryTarget != null) {
			float distance = secondaryTarget.distanceTo(this);

			for (int i = 1; i < numHeads; i++) {
				if (hc[i].isActive() && hc[i].isIdle() && isTargetOnThisSide(i, secondaryTarget)) {
					if (distance > 0 && distance < 20 && random.nextInt(SECONDARY_FLAME_CHANCE) == 0) {
						hc[i].setTargetEntity(secondaryTarget);
						hc[i].isSecondaryAttacking = true;
						hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
					} else if (distance > 8 && distance < 32 && random.nextInt(SECONDARY_MORTAR_CHANCE) == 0) {
						hc[i].setTargetEntity(secondaryTarget);
						hc[i].isSecondaryAttacking = true;
						hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
					}
				}
			}
		}
	}

	/**
	 * Used to make sure heads don't attack across the whole body
	 */
	private boolean isTargetOnThisSide(int headNum, Entity target) {
		double headDist = distanceSqXZ(hc[headNum].headEntity, target);
		double middleDist = distanceSqXZ(this, target);
		return headDist < middleDist;
	}

	/**
	 * Square of distance between two entities with y not a factor, just x and z
	 */
	private double distanceSqXZ(Entity headEntity, Entity target) {
		double distX = headEntity.getX() - target.getX();
		double distZ = headEntity.getZ() - target.getZ();
		return distX * distX + distZ * distZ;
	}

	@Nullable
	private LivingEntity findSecondaryTarget(double range) {
		return this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(range, range, range))
				.stream()
				.filter(e -> !(e instanceof Hydra))
				.filter(e -> e != getTarget() && !isAnyHeadTargeting(e) && getSensing().hasLineOfSight(e) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e))
				.min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
	}

	private boolean isAnyHeadTargeting(Entity targetEntity) {
		for (int i = 0; i < numHeads; i++) {
			if (hc[i].targetEntity != null && hc[i].targetEntity.equals(targetEntity)) {
				return true;
			}
		}

		return false;
	}

	// [VanillaCopy] based on EntityDragon.collideWithEntities
	private void collideWithEntities(List<Entity> entities, Entity part) {
		double d0 = (part.getBoundingBox().minX + part.getBoundingBox().maxX) / 2.0D;
		double d1 = (part.getBoundingBox().minZ + part.getBoundingBox().maxZ) / 2.0D;

		for (Entity entity : entities) {
			if (entity instanceof LivingEntity) {
				double d2 = entity.getX() - d0;
				double d3 = entity.getZ() - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.push(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
			}
		}
	}

	/**
	 * Check the surface immediately beneath us, if it is less than 80% solid
	 */
	private boolean isUnsteadySurfaceBeneath() {
		int minX = Mth.floor(this.getBoundingBox().minX);
		int minZ = Mth.floor(this.getBoundingBox().minZ);
		int maxX = Mth.floor(this.getBoundingBox().maxX);
		int maxZ = Mth.floor(this.getBoundingBox().maxZ);
		int minY = Mth.floor(this.getBoundingBox().minY);

		int solid = 0;
		int total = 0;

		int dy = minY - 1;

		for (int dx = minX; dx <= maxX; ++dx) {
			for (int dz = minZ; dz <= maxZ; ++dz) {
				total++;
				if (this.level.getBlockState(new BlockPos(dx, dy, dz)).getMaterial().isSolid()) {
					solid++;
				}
			}
		}

		return ((float) solid / (float) total) < 0.6F;
	}

	private void destroyBlocksInAABB(AABB box) {
		if (ForgeEventFactory.getMobGriefingEvent(level, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(level, pos, this)) {
					level.destroyBlock(pos, false);
				}
			}
		}
	}

	@Override
	public int getMaxHeadXRot() {
		return 500;
	}

	public boolean attackEntityFromPart(HydraPart part, DamageSource source, float damage) {
		// if we're in a wall, kill that wall
		if (!level.isClientSide && source == DamageSource.IN_WALL) {
			destroyBlocksInAABB(part.getBoundingBox());
		}

		if (source.getEntity() == this || source.getDirectEntity() == this)
			return false;
		if (getParts() != null)
			for (PartEntity<?> partEntity : getParts())
				if (partEntity == source.getEntity() || partEntity == source.getDirectEntity())
					return false;

		HydraHeadContainer headCon = null;

		for (int i = 0; i < numHeads; i++) {
			if (hc[i].headEntity == part) {
				headCon = hc[i];
			} else if (part instanceof HydraNeck && hc[i].headEntity == ((HydraNeck) part).head && !hc[i].isActive())
				return false;
		}

		double range = calculateRange(source);

		// Give some leeway for reflected mortars
		if (range > 400 + (source.getDirectEntity() instanceof HydraMortarHead ? 200 : 0)) {
			return false;
		}

		// ignore hits on dying heads, it's weird
		if (headCon != null && !headCon.isActive()) {
			return false;
		}

		boolean tookDamage;
		if (headCon != null && headCon.getCurrentMouthOpen() > 0.5) {
			tookDamage = super.hurt(source, damage);
			headCon.addDamage(damage);
		} else {
			int armoredDamage = Math.round(damage / ARMOR_MULTIPLIER);
			tookDamage = super.hurt(source, armoredDamage);

			if (headCon != null) {
				headCon.addDamage(armoredDamage);
			}
		}

		if (tookDamage) {
			this.ticksSinceDamaged = 0;
		}

		return tookDamage;
	}

	private double calculateRange(DamageSource damagesource) {
		return damagesource.getEntity() != null ? distanceToSqr(damagesource.getEntity()) : -1;
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		if(src.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
			hurtBy.add(player);
		}
		return src == DamageSource.OUT_OF_WORLD && super.hurt(src, damage);
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return partArray;
	}

	@Override
	public void recreateFromPacket(ClientboundAddMobPacket p_147206_) {
		super.recreateFromPacket(p_147206_);
		TFPart.assignPartIDs(this);
	}

	/**
	 * This is set as off for the hydra, which has an enormous bounding box, but set as on for the parts.
	 */
	@Override
	public boolean isPickable() {
		return false;
	}

	/**
	 * If this is on, the player pushes us based on our bounding box rather than it going by parts
	 */
	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HYDRA_GROWL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HYDRA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HYDRA_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2F;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the lair as defeated
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.HYDRA_LAIR);
			for (ServerPlayer player :hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}
		}
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;

		// stop any head actions on death
		if (deathTime == 1) {
			for (int i = 0; i < numHeads; i++) {
				hc[i].setRespawnCounter(-1);
				if (hc[i].isActive()) {
					hc[i].setNextState(HydraHeadContainer.State.IDLE);
					hc[i].endCurrentAction();
					hc[i].setHurtTime(200);
				}
			}
		}

		// heads die off one by one
		if (this.deathTime <= 140 && this.deathTime % 20 == 0) {
			int headToDie = (this.deathTime / 20) - 1;

			if (hc[headToDie].isActive()) {
				hc[headToDie].setNextState(HydraHeadContainer.State.DYING);
				hc[headToDie].endCurrentAction();
			}
		}

		if (this.deathTime == 200) {
			if (!this.level.isClientSide && (this.isAlwaysExperienceDropper() || this.lastHurtByPlayerTime > 0 && this.shouldDropExperience() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))) {
				int i = this.getExperienceReward(this.lastHurtByPlayer);
				i = ForgeEventFactory.getExperienceDrop(this, this.lastHurtByPlayer, i);
				while (i > 0) {
					int j = ExperienceOrb.getExperienceValue(i);
					i -= j;
					this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), j));
				}
			}

			this.discard();
		}

		for (int i = 0; i < 20; ++i) {
			double vx = this.random.nextGaussian() * 0.02D;
			double vy = this.random.nextGaussian() * 0.02D;
			double vz = this.random.nextGaussian() * 0.02D;
			this.level.addParticle((random.nextInt(2) == 0 ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION),
					this.getX() + this.random.nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
					this.getY() + this.random.nextFloat() * this.body.getBbHeight(),
					this.getZ() + this.random.nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
					vx, vy, vz
			);
		}
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}
}
