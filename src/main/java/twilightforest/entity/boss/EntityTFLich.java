package twilightforest.entity.boss;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFSwarmSpider;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.EntityAITFLichMinions;
import twilightforest.entity.ai.EntityAITFLichShadows;
import twilightforest.enums.BossVariant;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class EntityTFLich extends MonsterEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/lich");
	//TODO: Think these could be EntityType?
	private static final Set<Class<? extends Entity>> POPPABLE = ImmutableSet.of(SkeletonEntity.class, ZombieEntity.class, EndermanEntity.class, SpiderEntity.class, CreeperEntity.class, EntityTFSwarmSpider.class);

	private static final DataParameter<Boolean> DATA_ISCLONE = EntityDataManager.createKey(EntityTFLich.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> DATA_SHIELDSTRENGTH = EntityDataManager.createKey(EntityTFLich.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_MINIONSLEFT = EntityDataManager.createKey(EntityTFLich.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_ATTACKTYPE = EntityDataManager.createKey(EntityTFLich.class, DataSerializers.BYTE);

	public static final int MAX_SHADOW_CLONES = 2;
	public static final int INITIAL_SHIELD_STRENGTH = 5;
	public static final int MAX_ACTIVE_MINIONS = 3;
	public static final int INITIAL_MINIONS_TO_SUMMON = 9;
	public static final int MAX_HEALTH = 100;

	private EntityTFLich masterLich;
	private int attackCooldown;
	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6);

	public EntityTFLich(EntityType<? extends EntityTFLich> type, World world) {
		super(type, world);

		setShadowClone(false);
		this.masterLich = null;
		this.isImmuneToFire();
		this.experienceValue = 217;
	}

	public EntityTFLich(World world, EntityTFLich otherLich) {
		this(TFEntities.lich, world);

		setShadowClone(true);
		this.masterLich = otherLich;
	}

	public EntityTFLich getMasterLich(){
		return masterLich;
	}

	public int getAttackCooldown() {
		return attackCooldown;
	}

	public void setAttackCooldown(int cooldown) {
		attackCooldown = cooldown;
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAITFLichShadows(this));
		this.goalSelector.addGoal(2, new EntityAITFLichMinions(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.75D, true) {
			@Override
			public boolean shouldExecute() {
				return getPhase() == 3 && super.shouldExecute();
			}

			@Override
			public void startExecuting() {
				super.startExecuting();
				setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			}
		});

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_ISCLONE, false);
		dataManager.register(DATA_SHIELDSTRENGTH, (byte) INITIAL_SHIELD_STRENGTH);
		dataManager.register(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
		dataManager.register(DATA_ATTACKTYPE, (byte) 0);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, MAX_HEALTH)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.45000001788139344D); // Same speed as an angry enderman
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
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL && !isShadowClone()) {
			if (detachHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner_lich.get().getDefaultState());
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	/**
	 * What phase of the fight are we on?
	 * <p>
	 * 1 - reflecting bolts, shield up
	 * 2 - summoning minions
	 * 3 - melee
	 */
	public int getPhase() {
		if (isShadowClone() || getShieldStrength() > 0) {
			return 1;
		} else if (getMinionsToSummon() > 0 || countMyMinions() > 0) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public void livingTick() {
		// determine the hand position
		float angle = ((renderYawOffset * 3.141593F) / 180F);

		double dx = getPosX() + (MathHelper.cos(angle) * 0.65);
		double dy = getPosY() + (getHeight() * 0.94);
		double dz = getPosZ() + (MathHelper.sin(angle) * 0.65);


		// add particles!

		// how many particles do we want to add?!
		int factor = (80 - attackCooldown) / 10;
		int particles = factor > 0 ? rand.nextInt(factor) : 1;


		for (int j1 = 0; j1 < particles; j1++) {
			float sparkle = 1.0F - (attackCooldown + 1.0F) / 60.0F;
			sparkle *= sparkle;

			float red = 0.37F * sparkle;
			float grn = 0.99F * sparkle;
			float blu = 0.89F * sparkle;

			// change color for fireball
			if (this.getNextAttackType() != 0) {
				red = 0.99F * sparkle;
				grn = 0.47F * sparkle;
				blu = 0.00F * sparkle;
			}

			world.addParticle(ParticleTypes.ENTITY_EFFECT, dx + (rand.nextGaussian() * 0.025), dy + (rand.nextGaussian() * 0.025), dz + (rand.nextGaussian() * 0.025), red, grn, blu);
		}

		if (this.getPhase() == 3)
			world.addParticle(ParticleTypes.ANGRY_VILLAGER,
				this.getPosX() + this.rand.nextFloat() * this.getWidth() * 2.0F - this.getWidth(),
				this.getPosY() + 1.0D + this.rand.nextFloat() * this.getHeight(),
				this.getPosZ() + this.rand.nextFloat() * this.getWidth() * 2.0F - this.getWidth(),
				this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);

		if (!world.isRemote) {
			if (this.getPhase() == 1) {
				bossInfo.setPercent((float) (getShieldStrength() + 1) / (float) (INITIAL_SHIELD_STRENGTH + 1));
			} else {
				bossInfo.setOverlay(BossInfo.Overlay.PROGRESS);
				bossInfo.setPercent(getHealth() / getMaxHealth());
				if (this.getPhase() == 2)
					bossInfo.setColor(BossInfo.Color.PURPLE);
				else
					bossInfo.setColor(BossInfo.Color.RED);
			}
		}

		super.livingTick();
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {
		// if we're in a wall, teleport for gosh sakes
		if ("inWall".equals(src.getDamageType()) && getAttackTarget() != null) {
			teleportToSightOfEntity(getAttackTarget());
		}

		if (isShadowClone() && src != DamageSource.OUT_OF_WORLD) {
			playSound(TFSounds.LICH_CLONE_HURT, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			return false;
		}

		// ignore all bolts that are not reflected
		if (src.getTrueSource() instanceof EntityTFLich) {
			return false;
		}

		// if our shield is up, ignore any damage that can be blocked.
		if (src != DamageSource.OUT_OF_WORLD && getShieldStrength() > 0) {
			if (src.isMagicDamage() && damage > 2) {
				// reduce shield for magic damage greater than 1 heart
				if (getShieldStrength() > 0) {
					setShieldStrength(getShieldStrength() - 1);
					playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
			} else {
				playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				if (src.getTrueSource() instanceof LivingEntity) {
					setRevengeTarget((LivingEntity) src.getTrueSource());
				}
			}

			return false;
		}

		if (super.attackEntityFrom(src, damage)) {
			// Prevent AIHurtByTarget from targeting our own companions
			if (getRevengeTarget() instanceof EntityTFLich && ((EntityTFLich) getRevengeTarget()).masterLich == this.masterLich) {
				setRevengeTarget(null);
			}

			if (this.getPhase() < 3 || rand.nextInt(4) == 0) {
				this.teleportToSightOfEntity(getAttackTarget());
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		if (getAttackTarget() == null) {
			return;
		}

		if (attackCooldown > 0) {
			attackCooldown--;
		}

		// TODO: AI task?
		if (!isShadowClone() && attackCooldown % 15 == 0) {
			popNearbyMob();
		}

		// always watch our target
		// TODO: make into AI task
		this.getLookController().setLookPositionWithEntity(getAttackTarget(), 100F, 100F);
	}

	public void launchBoltAt() {
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = getPosX() + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = getPosY() + (getHeight() * 0.82);
		double sz = getPosZ() + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().getPosX() - sx;
		double ty = (getAttackTarget().getBoundingBox().minY + getAttackTarget().getHeight() / 2.0F) - (getPosY() + getHeight() / 2.0F);
		double tz = getAttackTarget().getPosZ() - sz;

		playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityTFLichBolt projectile = new EntityTFLichBolt(TFEntities.lich_bolt, world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.shoot(tx, ty, tz, 0.5F, 1.0F);

		world.addEntity(projectile);
	}

	public void launchBombAt() {
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = getPosX() + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = getPosY() + (getHeight() * 0.82);
		double sz = getPosZ() + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().getPosX() - sx;
		double ty = (getAttackTarget().getBoundingBox().minY + getAttackTarget().getHeight() / 2.0F) - (getPosY() + getHeight() / 2.0F);
		double tz = getAttackTarget().getPosZ() - sz;

		playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityTFLichBomb projectile = new EntityTFLichBomb(TFEntities.lich_bomb, world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.shoot(tx, ty, tz, 0.35F, 1.0F);

		world.addEntity(projectile);
	}

	private void popNearbyMob() {
		List<MobEntity> nearbyMobs = world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 16.0D, 32.0D), e -> POPPABLE.contains(e.getClass()));

		for (MobEntity mob : nearbyMobs) {
			if (getEntitySenses().canSee(mob)) {
				mob.spawnExplosionParticle();
				mob.remove();
				// play death sound
//					world.playSoundAtEntity(mob, mob.getDeathSound(), mob.getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

				// make trail so it's clear that we did it
				makeRedMagicTrail(mob.getPosX(), mob.getPosY() + mob.getHeight() / 2.0, mob.getPosZ(), this.getPosX(), this.getPosY() + this.getHeight() / 2.0, this.getPosZ());

				break;
			}
		}
	}

	public boolean wantsNewClone(EntityTFLich clone) {
		return clone.isShadowClone() && countMyClones() < EntityTFLich.MAX_SHADOW_CLONES;
	}

	public void setMaster(EntityTFLich lich) {
		masterLich = lich;
	}

	public int countMyClones() {
		// check if there are enough clones.  we check a 32x16x32 area
		int count = 0;

		for (EntityTFLich nearbyLich : getNearbyLiches()) {
			if (nearbyLich.isShadowClone() && nearbyLich.getMasterLich() == this) {
				count++;
			}
		}

		return count;
	}

	public List<EntityTFLich> getNearbyLiches() {
		return world.getEntitiesWithinAABB(getClass(), new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 16.0D, 32.0D));
	}

	public boolean wantsNewMinion() {
		return countMyMinions() < EntityTFLich.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() {
		return (int) world.getEntitiesWithinAABB(EntityTFLichMinion.class, new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 16.0D, 32.0D))
				.stream()
				.filter(m -> m.master == this)
				.count();
	}

	public void teleportToSightOfEntity(Entity entity) {
		Vector3d dest = findVecInLOSOf(entity);
		double srcX = getPosX();
		double srcY = getPosY();
		double srcZ = getPosZ();

		if (dest != null) {
			teleportToNoChecks(dest.x, dest.y, dest.z);
			this.getLookController().setLookPositionWithEntity(entity, 100F, 100F);
			this.renderYawOffset = this.rotationYaw;

			if (!this.getEntitySenses().canSee(entity)) {
				teleportToNoChecks(srcX, srcY, srcZ);
			}
		}
	}

	/**
	 * Returns coords that would be good to teleport to.
	 * Returns null if we can't find anything
	 */
	@Nullable
	public Vector3d findVecInLOSOf(Entity targetEntity) {
		if (targetEntity == null) return null;
		double origX = getPosX();
		double origY = getPosY();
		double origZ = getPosZ();

		int tries = 100;
		for (int i = 0; i < tries; i++) {
			// we abuse LivingEntity.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
			double tx = targetEntity.getPosX() + rand.nextGaussian() * 16D;
			double ty = targetEntity.getPosY();
			double tz = targetEntity.getPosZ() + rand.nextGaussian() * 16D;

			boolean destClear = attemptTeleport(tx, ty, tz, true);
			boolean canSeeTargetAtDest = canEntityBeSeen(targetEntity); // Don't use senses cache because we're in a temporary position
			setPositionAndUpdate(origX, origY, origZ);

			if (destClear && canSeeTargetAtDest) {
				return new Vector3d(tx, ty, tz);
			}
		}

		return null;
	}

	/**
	 * Does not check that the teleport destination is valid, we just go there
	 */
	private void teleportToNoChecks(double destX, double destY, double destZ) {
		// save original position
		double srcX = getPosX();
		double srcY = getPosY();
		double srcZ = getPosZ();

		// change position
		setPositionAndUpdate(destX, destY, destZ);

		makeTeleportTrail(srcX, srcY, srcZ, destX, destY, destZ);
		this.world.playSound(null, srcX, srcY, srcZ, TFSounds.LICH_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
		this.playSound(TFSounds.LICH_TELEPORT, 1.0F, 1.0F);

		// sometimes we need to do this
		this.isJumping = false;
	}

	public void makeTeleportTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 128;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = (rand.nextFloat() - 0.5F) * 0.2F;
			float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
			float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
			double tx = srcX + (destX - srcX) * trailFactor + (rand.nextDouble() - 0.5D) * getWidth() * 2D;
			double ty = srcY + (destY - srcY) * trailFactor + rand.nextDouble() * getHeight();
			double tz = srcZ + (destZ - srcZ) * trailFactor + (rand.nextDouble() - 0.5D) * getWidth() * 2D;
			world.addParticle(ParticleTypes.EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	private void makeRedMagicTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 1.0F;
			float f1 = 0.5F;
			float f2 = 0.5F;
			double tx = srcX + (destX - srcX) * trailFactor + rand.nextGaussian() * 0.005;
			double ty = srcY + (destY - srcY) * trailFactor + rand.nextGaussian() * 0.005;
			double tz = srcZ + (destZ - srcZ) * trailFactor + rand.nextGaussian() * 0.005;
			world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public void makeBlackMagicTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 0.2F;
			float f1 = 0.2F;
			float f2 = 0.2F;
			double tx = srcX + (destX - srcX) * trailFactor + rand.nextGaussian() * 0.005;
			double ty = srcY + (destY - srcY) * trailFactor + rand.nextGaussian() * 0.005;
			double tz = srcZ + (destZ - srcZ) * trailFactor + rand.nextGaussian() * 0.005;
			world.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public boolean isShadowClone() {
		return dataManager.get(DATA_ISCLONE);
	}

	public void setShadowClone(boolean shadowClone) {
		bossInfo.setVisible(!shadowClone);
		dataManager.set(DATA_ISCLONE, shadowClone);
	}

	public byte getShieldStrength() {
		return dataManager.get(DATA_SHIELDSTRENGTH);
	}

	public void setShieldStrength(int shieldStrength) {
		dataManager.set(DATA_SHIELDSTRENGTH, (byte) shieldStrength);
	}

	public byte getMinionsToSummon() {
		return dataManager.get(DATA_MINIONSLEFT);
	}

	public void setMinionsToSummon(int minionsToSummon) {
		dataManager.set(DATA_MINIONSLEFT, (byte) minionsToSummon);
	}

	public byte getNextAttackType() {
		return dataManager.get(DATA_ATTACKTYPE);
	}

	public void setNextAttackType(int attackType) {
		dataManager.set(DATA_ATTACKTYPE, (byte) attackType);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.LICH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LICH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.LICH_DEATH;
	}

	@Override
	public ResourceLocation getLootTable() {
		return !isShadowClone() ? LOOT_TABLE : null;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("ShadowClone", isShadowClone());
		compound.putByte("ShieldStrength", getShieldStrength());
		compound.putByte("MinionsToSummon", getMinionsToSummon());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		setShadowClone(compound.getBoolean("ShadowClone"));
		setShieldStrength(compound.getByte("ShieldStrength"));
		setMinionsToSummon(compound.getByte("MinionsToSummon"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		// mark the tower as defeated
		if (!world.isRemote && !this.isShadowClone()) {
			TFGenerationSettings.markStructureConquered(world, new BlockPos(this.getPosition()), TFFeature.LICH_TOWER);
		}
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimension() {
		return false;
	}
}
