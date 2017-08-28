package twilightforest.entity.boss;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFSwarmSpider;
import twilightforest.entity.ai.EntityAITFLichMinions;
import twilightforest.entity.ai.EntityAITFLichShadows;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

import java.util.List;
import java.util.Set;

public class EntityTFLich extends EntityMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/lich");
	private static final Set<Class<? extends Entity>> POPPABLE = ImmutableSet.of(EntitySkeleton.class, EntityZombie.class, EntityEnderman.class, EntitySpider.class, EntityCreeper.class, EntityTFSwarmSpider.class);
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
	private final BossInfoServer bossInfo = new BossInfoServer(new TextComponentTranslation("entity." + EntityList.getKey(this) + ".name"), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);

	public EntityTFLich(World world) {
		super(world);
		setSize(1.1F, 2.5F);

		setShadowClone(false);
		this.masterLich = null;
		this.isImmuneToFire = true;
		this.experienceValue = 217;
	}

	public EntityTFLich(World world, EntityTFLich otherLich) {
		this(world);

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
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAITFLichShadows(this));
		this.tasks.addTask(2, new EntityAITFLichMinions(this));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, true) {
			@Override
			public boolean shouldExecute() {
				return getPhase() == 3 && super.shouldExecute();
			}

			@Override
			public void startExecuting() {
				super.startExecuting();
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			}
		});

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_ISCLONE, false);
		dataManager.register(DATA_SHIELDSTRENGTH, (byte) INITIAL_SHIELD_STRENGTH);
		dataManager.register(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
		dataManager.register(DATA_ATTACKTYPE, (byte) 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45000001788139344D); // Same speed as an angry enderman
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void setInWeb() {
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	@Override
	public boolean isInWater() {
		return false;
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
		} else if (getMinionsToSummon() > 0) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public void onLivingUpdate() {
		// determine the hand position
		float angle = ((renderYawOffset * 3.141593F) / 180F);

		double dx = posX + (MathHelper.cos(angle) * 0.65);
		double dy = posY + (height * 0.94);
		double dz = posZ + (MathHelper.sin(angle) * 0.65);


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

			world.spawnParticle(EnumParticleTypes.SPELL_MOB, dx + (rand.nextGaussian() * 0.025), dy + (rand.nextGaussian() * 0.025), dz + (rand.nextGaussian() * 0.025), red, grn, blu);
		}

		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());
		}

		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {
		// if we're in a wall, teleport for gosh sakes
		if ("inWall".equals(src.getDamageType()) && getAttackTarget() != null) {
			teleportToSightOfEntity(getAttackTarget());
		}

		if (isShadowClone()) {
			playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			return false;
		}

		// ignore all bolts that are not reflected
		if (src.getTrueSource() instanceof EntityTFLich) {
			return false;
		}

		// if our shield is up, ignore any damage that can be blocked.
		if (getShieldStrength() > 0) {
			if (src.isMagicDamage() && damage > 2) {
				// reduce shield for magic damage greater than 1 heart
				if (getShieldStrength() > 0) {
					setShieldStrength(getShieldStrength() - 1);
					playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
			} else {
				playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				if (src.getTrueSource() instanceof EntityLivingBase) {
					setRevengeTarget((EntityLivingBase) src.getTrueSource());
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
		this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 100F, 100F);
	}

	public void launchBoltAt() {
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = posX + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = posY + (height * 0.82);
		double sz = posZ + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().posX - sx;
		double ty = (getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F)) - (posY + height / 2.0F);
		double tz = getAttackTarget().posZ - sz;

		playSound(SoundEvents.ENTITY_GHAST_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityTFLichBolt projectile = new EntityTFLichBolt(world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.setThrowableHeading(tx, ty, tz, 0.5F, 1.0F);

		world.spawnEntity(projectile);
	}

	public void launchBombAt() {
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = posX + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = posY + (height * 0.82);
		double sz = posZ + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().posX - sx;
		double ty = (getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F)) - (posY + height / 2.0F);
		double tz = getAttackTarget().posZ - sz;

		playSound(SoundEvents.ENTITY_GHAST_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityTFLichBomb projectile = new EntityTFLichBomb(world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.setThrowableHeading(tx, ty, tz, 0.35F, 1.0F);

		world.spawnEntity(projectile);
	}

	private void popNearbyMob() {
		List<EntityLiving> nearbyMobs = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D), e -> POPPABLE.contains(e.getClass()));

		for (EntityLiving mob : nearbyMobs) {
			if (getEntitySenses().canSee(mob)) {
				mob.spawnExplosionParticle();
				mob.setDead();
				// play death sound
//					world.playSoundAtEntity(mob, mob.getDeathSound(), mob.getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

				// make trail so it's clear that we did it
				makeRedMagicTrail(mob.posX, mob.posY + mob.height / 2.0, mob.posZ, this.posX, this.posY + this.height / 2.0, this.posZ);

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
		return world.getEntitiesWithinAABB(getClass(), new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D));
	}

	public boolean wantsNewMinion(EntityTFLichMinion minion) {
		return countMyMinions() < EntityTFLich.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() {
		return (int) world.getEntitiesWithinAABB(EntityTFLichMinion.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D))
				.stream()
				.filter(m -> m.master == this)
				.count();
	}

	public void teleportToSightOfEntity(Entity entity) {
		Vec3d dest = findVecInLOSOf(entity);
		double srcX = posX;
		double srcY = posY;
		double srcZ = posZ;

		if (dest != null) {
			teleportToNoChecks(dest.x, dest.y, dest.z);
			this.getLookHelper().setLookPositionWithEntity(entity, 100F, 100F);
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
	public Vec3d findVecInLOSOf(Entity targetEntity) {
		if (targetEntity == null) return null;
		double origX = posX;
		double origY = posY;
		double origZ = posZ;

		int tries = 100;
		for (int i = 0; i < tries; i++) {
			// we abuse EntityLivingBase.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
			double tx = targetEntity.posX + rand.nextGaussian() * 16D;
			double ty = targetEntity.posY;
			double tz = targetEntity.posZ + rand.nextGaussian() * 16D;

			boolean destClear = attemptTeleport(tx, ty, tz);
			boolean canSeeTargetAtDest = canEntityBeSeen(targetEntity); // Don't use senses cache because we're in a temporary position
			setPositionAndUpdate(origX, origY, origZ);

			if (destClear && canSeeTargetAtDest) {
				return new Vec3d(tx, ty, tz);
			}
		}

		return null;
	}

	/**
	 * Does not check that the teleport destination is valid, we just go there
	 */
	private void teleportToNoChecks(double destX, double destY, double destZ) {
		// save original position
		double srcX = posX;
		double srcY = posY;
		double srcZ = posZ;

		// change position
		setPositionAndUpdate(destX, destY, destZ);

		makeTeleportTrail(srcX, srcY, srcZ, destX, destY, destZ);
		this.world.playSound(null, srcX, srcY, srcZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
		this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

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
			double tx = srcX + (destX - srcX) * trailFactor + (rand.nextDouble() - 0.5D) * width * 2D;
			double ty = srcY + (destY - srcY) * trailFactor + rand.nextDouble() * height;
			double tz = srcZ + (destZ - srcZ) * trailFactor + (rand.nextDouble() - 0.5D) * width * 2D;
			world.spawnParticle(EnumParticleTypes.SPELL, tx, ty, tz, f, f1, f2);
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
			world.spawnParticle(EnumParticleTypes.SPELL_MOB, tx, ty, tz, f, f1, f2);
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
			world.spawnParticle(EnumParticleTypes.SPELL_MOB, tx, ty, tz, f, f1, f2);
		}
	}

	public boolean isShadowClone() {
		return dataManager.get(DATA_ISCLONE);
	}

	public void setShadowClone(boolean par1) {
		bossInfo.setVisible(!par1);
		dataManager.set(DATA_ISCLONE, par1);
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
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BLAZE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}

	@Override
	public ResourceLocation getLootTable() {
		return !isShadowClone() ? LOOT_TABLE : null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("ShadowClone", isShadowClone());
		nbttagcompound.setByte("ShieldStrength", getShieldStrength());
		nbttagcompound.setByte("MinionsToSummon", getMinionsToSummon());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		setShadowClone(nbttagcompound.getBoolean("ShadowClone"));
		setShieldStrength(nbttagcompound.getByte("ShieldStrength"));
		setMinionsToSummon(nbttagcompound.getByte("MinionsToSummon"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		// mark the tower as defeated
		if (!world.isRemote && !this.isShadowClone()) {
			int dx = MathHelper.floor(this.posX);
			int dy = MathHelper.floor(this.posY);
			int dz = MathHelper.floor(this.posZ);

			if (TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
				ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
				TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

				if (nearbyFeature == TFFeature.lichTower) {
					generator.setStructureConquered(dx, dy, dz, true);
				}
			}
		}
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

}
