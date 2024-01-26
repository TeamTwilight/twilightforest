package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.TFPart;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.WorldUtil;

import java.util.*;

public class Hydra extends Mob implements Enemy, EnforcedHomePoint {

	private static final int TICKS_BEFORE_HEALING = 1000;
	private static final int HEAD_RESPAWN_TICKS = 140;
	private static final int HEAD_MAX_DAMAGE = 120;
	private static final float ARMOR_MULTIPLIER = 8.0F;
	private static final int MAX_HEALTH = 360;
	private static float HEADS_ACTIVITY_FACTOR = 0.3F;

	private static final int SECONDARY_FLAME_CHANCE = 10;
	private static final int SECONDARY_MORTAR_CHANCE = 16;

	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(Hydra.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<List<String>> HEAD_NAMES = SynchedEntityData.defineId(Hydra.class, TFDataSerializers.STRING_LIST.get());

	private final HydraPart[] partArray;

	public final int numHeads = 7;
	public final HydraHeadContainer[] hc = new HydraHeadContainer[numHeads];

	public final HydraSmallPart body;
	private final HydraSmallPart leftLeg;
	private final HydraSmallPart rightLeg;
	private final HydraSmallPart tail;
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
	private float randomYawVelocity = 0f;

	private int ticksSinceDamaged = 0;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public boolean renderFakeHeads = true;

	public Hydra(EntityType<? extends Hydra> type, Level world) {
		super(type, world);

		List<HydraPart> parts = new ArrayList<>();

		this.body = new HydraSmallPart(this, 6F, 6F);
		this.leftLeg = new HydraSmallPart(this, 2F, 3F);
		this.rightLeg = new HydraSmallPart(this, 2F, 3F);
		this.tail = new HydraSmallPart(this, 6.0f, 2.0f);

		parts.add(this.body);
		parts.add(this.leftLeg);
		parts.add(this.rightLeg);
		parts.add(this.tail);

		for (int i = 0; i < this.numHeads; i++) {
			this.hc[i] = new HydraHeadContainer(this, i, i < 3);
			this.hc[i].headEntity.setCustomName(Component.literal(this.getHeadNameFor(i)));
			parts.add(this.hc[i].headEntity);
			Collections.addAll(parts, this.hc[i].getNeckArray());
		}

		this.partArray = parts.toArray(new HydraPart[0]);

		this.noCulling = true;
		this.xpReward = 511;

	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(HOME_POINT, Optional.empty());
		this.getEntityData().define(HEAD_NAMES, List.of("", "", "", "", "", "", ""));
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
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			this.level().setBlockAndUpdate(this.blockPosition().offset(0, 1, 0), TFBlocks.HYDRA_BOSS_SPAWNER.get().defaultBlockState());
			this.discard();
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
	protected float tickHeadTurn(float p_110146_1_, float p_110146_2_) {
		float f = Mth.wrapDegrees(p_110146_1_ - this.yBodyRot);
		this.yBodyRot += f * 0.3F;
		float f1 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);
		boolean flag = f1 < -90.0F || f1 >= 90.0F;

		if (f1 < -75.0F) {
			f1 = -75.0F;
		}

		if (f1 >= 75.0F) {
			f1 = 75.0F;
		}

		this.yBodyRot = this.getYRot() - f1;

		if (f1 * f1 > 2500.0F) {
			this.yBodyRot += f1 * 0.2F;
		}

		if (flag) {
			p_110146_2_ *= -1.0F;
		}

		return p_110146_2_;
	}

	@Override
	public void aiStep() {
		if (this.renderFakeHeads) this.renderFakeHeads = false;
		this.clearFire();
		this.body.tick();
		this.leftLeg.tick();
		this.rightLeg.tick();

		// update all heads (maybe we should change to only active ones
		for (int i = 0; i < this.numHeads; i++) {
			this.hc[i].tick();
		}

		if (this.hurtTime > 0) {
			for (int i = 0; i < this.numHeads; i++) {
				this.hc[i].setHurtTime(this.hurtTime);
			}
		}

		this.ticksSinceDamaged++;

		if (!this.level().isClientSide() && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 5 == 0) {
			this.heal(1);
		}

		// update fight variables for difficulty setting
		this.setDifficultyVariables();

		super.aiStep();

		// set body part positions
		float angle;
		double dx, dy, dz;

		// body goes behind the actual position of the hydra
		angle = (((this.yBodyRot + 180.0F) * Mth.PI) / 180.0F);

		dx = getX() - Mth.sin(angle) * 3.0D;
		dy = getY() + 0.1D;
		dz = getZ() + Mth.cos(angle) * 3.0D;
		this.body.setPos(dx, dy, dz);

		dx = getX() - Mth.sin(angle) * 10.5D;
		dy = getY() + 0.1D;
		dz = getZ() + Mth.cos(angle) * 10.5D;
		this.tail.setPos(dx, dy, dz);

		if (hurtTime == 0) {
			this.collideWithEntities(this.level().getEntities(this, this.body.getBoundingBox()), this.body);
			this.collideWithEntities(this.level().getEntities(this, this.tail.getBoundingBox()), this.tail);
		}

		// destroy blocks
		if (!this.level().isClientSide()) {
			this.destroyBlocksInAABB(this.body.getBoundingBox());
			this.destroyBlocksInAABB(this.tail.getBoundingBox());

			for (int i = 0; i < this.numHeads; i++) {
				if (this.hc[i].headEntity != null && !this.hc[i].isDead()) {
					this.destroyBlocksInAABB(this.hc[i].headEntity.getBoundingBox());
				}
			}

			// smash blocks beneath us too
			if (this.tickCount % 20 == 0) {
				if (this.isUnsteadySurfaceBeneath()) {
					this.destroyBlocksInAABB(this.getBoundingBox().move(0, -1, 0));

				}
			}

			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		byte headData = 0;
		for (int i = 0; i < this.numHeads; i++) {
			if (this.hc[i].isActive()) {
				headData |= 1 << i;
			}
		}
		compound.putByte("NumHeads", headData);
		ListTag headNames = new ListTag();
		for (int i = 0; i < this.numHeads; i++) {
			headNames.add(StringTag.valueOf(this.getEntityData().get(HEAD_NAMES).get(i)));
		}
		compound.put("HeadNames", headNames);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound);
		this.activateHeadsOnLoad(compound.getByte("NumHeads"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		if (compound.contains("HeadNames", Tag.TAG_LIST)) {
			List<String> names = new ArrayList<>();
			ListTag list = compound.getList("HeadNames", Tag.TAG_STRING);
			for (int i = 0; i < list.size(); i++) {
				String name = list.getString(i);
				names.add(name);
				this.hc[i].headEntity.setCustomName(Component.literal(name));
			}
			this.getEntityData().set(HEAD_NAMES, names);
		}
	}

	/**
	 * Activates heads based on a byte saved to nbt.
	 * This allows all the same heads to activate on world reload as heads are randomly chosen when one is killed
	 */
	private void activateHeadsOnLoad(byte heads) {
		for (int i = 0; i < this.numHeads; i++) {
			if ((heads & 1 << i) != 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.IDLE);
				this.hc[i].endCurrentAction();
			}
		}
	}

	// TODO modernize this more (old AI copypasta still kind of here)
	private int numTicksToChaseTarget;

	@Override
	protected void customServerAiStep() {
		this.xxa = 0.0F;
		this.zza = 0.0F;
		float f = 48.0F;

		// kill heads that have taken too much damage
		for (int i = 0; i < this.numHeads; i++) {
			if (!this.hc[i].isDead() && this.hc[i].getDamageTaken() > HEAD_MAX_DAMAGE) {
				this.hc[i].setNextState(HydraHeadContainer.State.DYING);
				this.hc[i].endCurrentAction();

				// set this head and a random dead head to respawn
				this.hc[i].setRespawnCounter(HEAD_RESPAWN_TICKS);
				int otherHead = this.getRandomDeadHead();
				if (otherHead != -1) {
					this.hc[otherHead].setRespawnCounter(HEAD_RESPAWN_TICKS);
				}
			}
		}

		if (this.getRandom().nextFloat() < 0.7F) {
			Player entityplayer1 = this.level().getNearestPlayer(this, f);

			if (entityplayer1 != null && !entityplayer1.isCreative()) {
				setTarget(entityplayer1);
				this.numTicksToChaseTarget = 100 + this.getRandom().nextInt(20);
			} else {
				this.randomYawVelocity = (this.getRandom().nextFloat() - 0.5F) * 20F;
			}
		}

		if (this.getTarget() != null) {
			this.lookAt(this.getTarget(), 10.0F, this.getMaxHeadXRot());

			// have any heads not currently attacking switch to the primary target
			for (int i = 0; i < this.numHeads; i++) {
				if (!this.hc[i].isAttacking() && !this.hc[i].isSecondaryAttacking) {
					this.hc[i].setTargetEntity(this.getTarget());
				}
			}

			// let's pick an attack
			if (this.getTarget().isAlive()) {
				float distance = this.getTarget().distanceTo(this);

				if (this.getSensing().hasLineOfSight(this.getTarget())) {
					this.attackEntity(this.getTarget(), distance);
				}
			}

			if (this.numTicksToChaseTarget-- <= 0 || !this.getTarget().isAlive() || this.getTarget().distanceToSqr(this) > f * f) {
				this.setTarget(null);
			}
		} else {
			if (this.getRandom().nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.getRandom().nextFloat() - 0.5F) * 20F;
			}

			this.setYRot(this.getYRot() + this.randomYawVelocity);
			this.setXRot(0);

			// TODO: while we are idle, consider having the heads breathe fire on passive mobs

			// set idle heads to no target
			for (int i = 0; i < this.numHeads; i++) {
				if (this.hc[i].isIdle()) {
					this.hc[i].setTargetEntity(null);
				}
			}
		}

		// heads that are free at this point may consider attacking secondary targets
		this.secondaryAttacks();
	}

	private void setDifficultyVariables() {
		if (this.level().getDifficulty() != Difficulty.HARD) {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.3F;
		} else {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.5F;  // higher is harder
		}
	}

	private int getRandomDeadHead() {
		List<Integer> headIDs = new ArrayList<>();
		for (int i = 0; i < this.numHeads; i++) {
			if (this.hc[i].canRespawn()) headIDs.add(i);
		}
		return headIDs.isEmpty() ? -1 : headIDs.get(this.random.nextInt(headIDs.size()));
	}

	/**
	 * Used when re-loading from save.  Assumes three heads are active and activates more if necessary.
	 */
	private void activateNumberOfHeads(int howMany) {
		int moreHeads = howMany - this.countActiveHeads();

		for (int i = 0; i < moreHeads; i++) {
			int otherHead = this.getRandomDeadHead();
			if (otherHead != -1) {
				// move directly into not dead
				this.hc[otherHead].setNextState(HydraHeadContainer.State.IDLE);
				this.hc[otherHead].endCurrentAction();
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
			if (this.hc[i].isIdle() && !areTooManyHeadsAttacking(i)) {
				if (distance > 4 && distance < 10 && this.getRandom().nextInt(BITE_CHANCE) == 0 && this.countActiveHeads() > 2 && !this.areOtherHeadsBiting(i)) {
					this.hc[i].setNextState(HydraHeadContainer.State.BITE_BEGINNING);
				} else if (distance > 0 && distance < 20 && this.getRandom().nextInt(FLAME_CHANCE) == 0) {
					this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
				} else if (distance > 8 && distance < 32 && !targetAbove && this.getRandom().nextInt(MORTAR_CHANCE) == 0) {
					this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
				}
			}
		}

		// heads 4-7 can do everything but bite
		for (int i = 3; i < numHeads; i++) {
			if (this.hc[i].isIdle() && !this.areTooManyHeadsAttacking(i)) {
				if (distance > 0 && distance < 20 && this.getRandom().nextInt(FLAME_CHANCE) == 0) {
					this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
				} else if (distance > 8 && distance < 32 && !targetAbove && this.getRandom().nextInt(MORTAR_CHANCE) == 0) {
					this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
				}
			}
		}
	}

	private boolean areTooManyHeadsAttacking(int testHead) {
		int otherAttacks = 0;

		for (int i = 0; i < this.numHeads; i++) {
			if (i != testHead && this.hc[i].isAttacking()) {
				otherAttacks++;

				// biting heads count triple
				if (this.hc[i].isBiting()) {
					otherAttacks += 2;
				}
			}
		}

		return otherAttacks >= 1 + (countActiveHeads() * HEADS_ACTIVITY_FACTOR);
	}

	private int countActiveHeads() {
		int count = 0;

		for (int i = 0; i < this.numHeads; i++) {
			if (!this.hc[i].isDead()) {
				count++;
			}
		}

		return count;
	}

	private boolean areOtherHeadsBiting(int testHead) {
		for (int i = 0; i < this.numHeads; i++) {
			if (i != testHead && this.hc[i].isBiting()) {
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
		for (int i = 0; i < this.numHeads; i++) {
			if (this.hc[i].headEntity == null) {
				return;
			}
		}

		LivingEntity secondaryTarget = findSecondaryTarget(20);

		if (secondaryTarget != null) {
			float distance = secondaryTarget.distanceTo(this);

			for (int i = 1; i < this.numHeads; i++) {
				if (!this.hc[i].isDead() && this.hc[i].isIdle() && isTargetOnThisSide(i, secondaryTarget)) {
					if (distance > 0 && distance < 20 && this.getRandom().nextInt(SECONDARY_FLAME_CHANCE) == 0) {
						this.hc[i].setTargetEntity(secondaryTarget);
						this.hc[i].isSecondaryAttacking = true;
						this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
					} else if (distance > 8 && distance < 32 && this.getRandom().nextInt(SECONDARY_MORTAR_CHANCE) == 0) {
						this.hc[i].setTargetEntity(secondaryTarget);
						this.hc[i].isSecondaryAttacking = true;
						this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
					}
				}
			}
		}
	}

	/**
	 * Used to make sure heads don't attack across the whole body
	 */
	private boolean isTargetOnThisSide(int headNum, Entity target) {
		double headDist = distanceSqXZ(this.hc[headNum].headEntity, target);
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
		return this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(range, range, range))
				.stream()
				.filter(e -> !(e instanceof Hydra))
				.filter(e -> e != getTarget() && !isAnyHeadTargeting(e) && getSensing().hasLineOfSight(e) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e))
				.min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
	}

	private boolean isAnyHeadTargeting(Entity targetEntity) {
		for (int i = 0; i < this.numHeads; i++) {
			if (this.hc[i].targetEntity != null && this.hc[i].targetEntity.equals(targetEntity)) {
				return true;
			}
		}

		return false;
	}

	// [VanillaCopy] based on EnderDragon.knockBack
	private void collideWithEntities(List<Entity> entities, Entity part) {
		double d0 = (part.getBoundingBox().minX + part.getBoundingBox().maxX) / 2.0D;
		double d1 = (part.getBoundingBox().minZ + part.getBoundingBox().maxZ) / 2.0D;

		for (Entity entity : entities) {
			if (entity instanceof Player player && player.isCreative()) continue;
			if (entity instanceof LivingEntity) {
				double d2 = entity.getX() - d0;
				double d3 = entity.getZ() - d1;
				double d4 = Math.max(d2 * d2 + d3 * d3, 0.1D);
				entity.push(d2 / d4 * 8.0D, 0.2D, d3 / d4 * 8.0D);
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
				if (this.level().getBlockState(new BlockPos(dx, dy, dz)).isSolid()) {
					solid++;
				}
			}
		}

		return ((float) solid / (float) total) < 0.6F;
	}

	private void destroyBlocksInAABB(AABB box) {
		if (this.deathTime <= 0 && EventHooks.getMobGriefingEvent(this.level(), this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(this.level(), pos, this)) {
					this.level().destroyBlock(pos, false);
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
		if (!this.level().isClientSide() && source.is(DamageTypes.IN_WALL)) {
			this.destroyBlocksInAABB(part.getBoundingBox());
		}

		if (source.getEntity() == this || source.getDirectEntity() == this)
			return false;
		if (this.getParts() != null)
			for (PartEntity<?> partEntity : this.getParts())
				if (partEntity == source.getEntity() || partEntity == source.getDirectEntity())
					return false;

		HydraHeadContainer headCon = null;

		for (int i = 0; i < this.numHeads; i++) {
			if (this.hc[i].headEntity == part) {
				headCon = this.hc[i];
			} else if (part instanceof HydraNeck neck && this.hc[i].headEntity == neck.head && this.hc[i].isDead())
				return false;
		}

		double range = calculateRange(source);

		// Give some leeway for reflected mortars
		if (range > 400 + (source.getDirectEntity() instanceof HydraMortar ? 200 : 0)) {
			return false;
		}

		// ignore hits on dying heads, it's weird
		if (headCon != null && headCon.isDead()) {
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
		return damagesource.getEntity() != null ? this.distanceToSqr(damagesource.getEntity()) : -1;
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		if (src.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}
		return src.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && super.hurt(src, damage);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return !source.is(TFDamageTypes.HYDRA_MORTAR) && super.isInvulnerableTo(source);
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
		return this.partArray;
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
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
	protected void doPush(Entity entity) {
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HYDRA_GROWL.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HYDRA_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HYDRA_DEATH.get();
	}

	@Override
	protected float getSoundVolume() {
		return 2F;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the lair as defeated
		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.HYDRA_LAIR, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.get().trigger(player, this);
			}

			TFLootTables.entityDropsIntoContainer(this, cause, TFBlocks.MANGROVE_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.COMMON_CONFIG.bossDropChests.get();
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
		if (this.deathTime == 1) {
			for (int i = 0; i < this.numHeads; i++) {
				this.hc[i].setRespawnCounter(-1);
				if (!this.hc[i].isDead()) {
					this.hc[i].setNextState(HydraHeadContainer.State.IDLE);
					this.hc[i].endCurrentAction();
					this.hc[i].setHurtTime(200);
				}
			}
		}

		// heads die off one by one
		if (this.deathTime <= 140 && this.deathTime % 20 == 0) {
			int headToDie = (this.deathTime / 20) - 1;

			if (!this.hc[headToDie].isDead()) {
				this.hc[headToDie].setNextState(HydraHeadContainer.State.DYING);
				this.hc[headToDie].endCurrentAction();
			}
		}

		if (this.deathTime == 200) {
			if (this.level() instanceof ServerLevel && !this.wasExperienceConsumed() && (this.isAlwaysExperienceDropper() || this.lastHurtByPlayerTime > 0 && this.shouldDropExperience() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))) {
				int reward = EventHooks.getExperienceDrop(this, this.lastHurtByPlayer, this.getExperienceReward());
				ExperienceOrb.award((ServerLevel) this.level(), this.position(), reward);
			}

			this.discard();
		}

		for (int i = 0; i < 10; ++i) {
			double vx = this.getRandom().nextGaussian() * 0.02D;
			double vy = this.getRandom().nextGaussian() * 0.02D;
			double vz = this.getRandom().nextGaussian() * 0.02D;
			this.level().addParticle((this.getRandom().nextInt(2) == 0 ? ParticleTypes.EXPLOSION : ParticleTypes.POOF),
					this.getX() + this.getRandom().nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
					this.getY() + this.getRandom().nextFloat() * this.body.getBbHeight(),
					this.getZ() + this.getRandom().nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
					vx, vy, vz
			);
		}
	}

	public String getHeadNameFor(int index) {
		return this.getEntityData().get(HEAD_NAMES).get(index);
	}

	public void setHeadNameFor(int index, String name) {
		//we're working with an ImmutableList here so we need to copy and modify it
		List<String> nameCopy = new ArrayList<>(this.getEntityData().get(HEAD_NAMES));
		nameCopy.set(index, name);
		this.getEntityData().set(HEAD_NAMES, nameCopy);
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}
}
