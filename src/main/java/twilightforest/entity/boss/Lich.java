package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.LightableBlock;
import twilightforest.data.tags.DamageTypeTagGenerator;
import twilightforest.entity.ai.goal.*;
import twilightforest.entity.monster.LichMinion;
import twilightforest.init.*;
import twilightforest.network.ParticlePacket;
import twilightforest.util.entities.EntityUtil;

import java.util.*;

public class Lich extends BaseTFBoss {
	public static final int DEATH_ANIMATION_DURATION = 175; //How many ticks until the body disappears
	public static final int DEATH_ANIMATION_POINT_A = 50;
	private static final int DEATH_ANIMATION_POINT_B = 70;

	private static final EntityDataAccessor<Optional<UUID>> MASTER_LICH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> SHIELD_STRENGTH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MINIONS_LEFT = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);

	private static final ItemParticleOption BONE_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.BONE.getDefaultInstance());
	public static final int MAX_SHADOW_CLONES = 2;
	public static final int INITIAL_SHIELD_STRENGTH = 6;
	public static final int MAX_ACTIVE_MINIONS = 3;
	public static final int INITIAL_MINIONS_TO_SUMMON = 9;
	public static final int MAX_HEALTH = 100;

	private int attackCooldown;
	private int popCooldown;
	private int heldScepterTime;
	private int spawnTime;
	private final List<UUID> summonedClones = new ArrayList<>();
	private int previousPhase = 1;

	public Lich(EntityType<? extends Lich> type, Level level) {
		super(type, level);
		this.xpReward = 217;
	}

	@SuppressWarnings("this-escape")
	public Lich(Level level, Lich otherLich) {
		this(TFEntities.LICH.get(), level);
		this.setMasterUUID(otherLich.getUUID());
		this.getBossBar().setVisible(false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, MAX_HEALTH)
			.add(Attributes.ATTACK_DAMAGE, 3.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.45D); // Same speed as an angry enderman
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(MASTER_LICH, Optional.empty());
		builder.define(SHIELD_STRENGTH, INITIAL_SHIELD_STRENGTH);
		builder.define(MINIONS_LEFT, INITIAL_MINIONS_TO_SUMMON);
		builder.define(ATTACK_TYPE, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new AttemptToGoHomeGoal<>(this, 1.25D) {
			@Override
			public boolean canUse() {
				if (Lich.this.getRestrictionPoint() == null) {
					return false;
				}
				return Lich.this.getRestrictionPoint().pos().getY() > Lich.this.getY() + 2 || super.canUse();
			}
		});
		this.goalSelector.addGoal(1, new AlwaysWatchTargetGoal(this));
		this.goalSelector.addGoal(1, new LichPopMobsGoal(this));
		this.goalSelector.addGoal(1, new LichAbsorbMinionsGoal(this));
		this.goalSelector.addGoal(2, new LichShadowsGoal(this));
		this.goalSelector.addGoal(3, new LichMinionsGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 0.75D, true) {
			@Override
			public boolean canUse() {
				return Lich.this.getPhase() == 3 && super.canUse();
			}

			@Override
			public void start() {
				super.start();
				this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
			}
		});
		this.addRestrictionGoals(this, this.goalSelector);
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
			@Override
			public boolean canUse() {
				if (this.mob instanceof Lich main && this.mob.getLastHurtByMob() instanceof Lich lich && lich.getMaster() == main.getMaster()) {
					return false;
				}
				return super.canUse();
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getMasterUUID() != null) {
			compound.putUUID("MasterLich", this.getMasterUUID());
		}
		ListTag clonesTag = new ListTag();
		for (UUID uuid : this.summonedClones) {
			clonesTag.add(NbtUtils.createUUID(uuid));
		}
		if (!clonesTag.isEmpty()) {
			compound.put("SummonedClones", clonesTag);
		}
		compound.putInt("ShieldStrength", this.getShieldStrength());
		compound.putInt("MinionsToSummon", this.getMinionsToSummon());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("MasterLich")) {
			this.setMasterUUID(compound.getUUID("MasterLich"));
		}
		if (compound.contains("SummonedClones", Tag.TAG_LIST)) {
			this.summonedClones.clear();
			ListTag cloneList = compound.getList("SummonedClones", Tag.TAG_INT_ARRAY);
			cloneList.forEach(tag -> this.summonedClones.add(NbtUtils.loadUUID(tag)));
		}
		this.setShieldStrength(compound.getInt("ShieldStrength"));
		this.setMinionsToSummon(compound.getInt("MinionsToSummon"));
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.isDeadOrDying()) return;

		// determine the hand position
		float angle = ((this.yBodyRot * Mth.PI) / 180F);

		double dx = this.getX() + (Mth.cos(angle) * 0.65);
		double dy = this.getY() + (this.getBbHeight() * 0.94);
		double dz = this.getZ() + (Mth.sin(angle) * 0.65);


		// add particles!

		// how many particles do we want to add?!
		int factor = (80 - this.getAttackCooldown()) / 10;
		int particles = factor > 0 ? this.getRandom().nextInt(factor) : 1;


		for (int j1 = 0; j1 < particles; j1++) {
			float sparkle = 1.0F - (this.getAttackCooldown() + 1.0F) / 60.0F;
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

			this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, grn, blu), dx + (this.getRandom().nextGaussian() * 0.025), dy + (this.getRandom().nextGaussian() * 0.025), dz + (this.getRandom().nextGaussian() * 0.025), 0.0F, 0.0F, 0.0F);
		}

		if (this.getPhase() == 3)
			this.level().addParticle(ParticleTypes.ANGRY_VILLAGER,
				this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
				this.getY() + 1.0D + this.getRandom().nextFloat() * this.getBbHeight(),
				this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
				this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		// Teleport home if we get too far away from it
		if (this.getRestrictionPoint() != null && this.getRestrictionPoint().pos().distSqr(this.blockPosition()) > (this.getHomeRadius() * this.getHomeRadius()) || (this.getPhase() == 3 && this.getRestrictionPoint() != null && this.getY() < this.getRestrictionPoint().pos().below(3).getY())) {
			this.level().setBlockAndUpdate(this.getRestrictionPoint().pos().below(2), Blocks.GLASS.defaultBlockState()); // Ensure there's something to stand on, so we don't teleport infinitely
			BlockPos pos = this.getRestrictionPoint().pos();
			this.teleportToNoChecks(pos.getX(), pos.getY(), pos.getZ());
		}

		if (this.getAttackCooldown() > 0 && this.spawnTime <= 0) {
			this.attackCooldown--;
		}

		if (this.getPopCooldown() > 0 && this.getHealth() < this.getMaxHealth() && this.getScepterTimeLeft() <= 0) {
			this.popCooldown--;
		}

		if (this.getScepterTimeLeft() == 0 && this.getPopCooldown() < 30 && this.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.LIFEDRAIN_SCEPTER.get())) {
			this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this.getPhase() == 2 ? TFItems.ZOMBIE_SCEPTER.get() : Items.GOLDEN_SWORD));
		}

		if (this.getScepterTimeLeft() > 0) {
			this.heldScepterTime--;
		}

		if (this.getTarget() != null) {
			if (this.spawnTime > 0 && this.hasLineOfSight(this.getTarget())) {
				this.spawnTime--;
				if (this.spawnTime <= 0) {
					this.extinguishNearbyCandles();
				}
			}
		}
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		// if we're in a wall, teleport for gosh sakes
		if (src.is(DamageTypes.IN_WALL) && this.getTarget() != null) {
			this.teleportToSightOfEntity(this.getTarget());
		}

		if (this.isShadowClone() && !src.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			this.playSound(TFSounds.LICH_CLONE_HURT.get(), 1.0F, this.getVoicePitch() * 2.0F);
			return false;
		}

		// ignore all bolts that are not reflected
		if (src.getEntity() instanceof Lich) {
			return false;
		}

		// if our shield is up, ignore any damage that can be blocked.
		if (!src.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.getShieldStrength() > 0) {
			if (src.is(DamageTypeTagGenerator.BREAKS_LICH_SHIELDS) && damage > 2) {
				// reduce shield for magic damage greater than 1 heart
				if (this.getShieldStrength() > 0) {
					this.setShieldStrength(this.getShieldStrength() - 1);
					this.playSound(TFSounds.SHIELD_BREAK.get(), 1.0F, this.getVoicePitch() * 2.0F);
					this.gameEvent(GameEvent.ENTITY_DAMAGE);
				}
			} else {
				this.playSound(TFSounds.SHIELD_BLOCK.get(), 0.5F, this.getVoicePitch() * 2.0F);
				this.gameEvent(GameEvent.ENTITY_DAMAGE);
				if (src.getEntity() instanceof LivingEntity living) {
					this.setLastHurtByMob(living);
				}
			}

			return false;
		}

		if (super.hurt(src, damage)) {
			if (this.getRandom().nextInt(this.getPhase() == 3 ? 6 : 3) == 0) {
				this.teleportToSightOfEntity(this.getTarget());
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.isShadowClone()) {
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			if (this.getShieldStrength() > 0) {
				this.setShieldStrength(0);
				this.playSound(TFSounds.SHIELD_BREAK.get(), 1.2F, this.getVoicePitch() * 2.0F);
			}
		}
	}

	//-----------------------------------------//
	//    PROJECTILES, CLONES, AND MINIONS     //
	//-----------------------------------------//

	public void launchProjectileAt(ThrowableProjectile projectile) {
		float bodyFacingAngle = ((this.yBodyRot * Mth.PI) / 180F);
		double sx = this.getX() + (Mth.cos(bodyFacingAngle) * 0.65D);
		double sy = this.getY() + (this.getBbHeight() * 0.82D);
		double sz = this.getZ() + (Mth.sin(bodyFacingAngle) * 0.65D);

		double tx = Objects.requireNonNull(this.getTarget()).getX() - sx;
		double ty = (this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F) - (this.getY() + this.getBbHeight() / 2.0F);
		double tz = this.getTarget().getZ() - sz;

		this.playSound(TFSounds.LICH_SHOOT.get(), this.getSoundVolume(), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);

		projectile.moveTo(sx, sy, sz, this.getYRot(), this.getXRot());
		projectile.shoot(tx, ty, tz, 0.5F, 1.0F);

		this.level().addFreshEntity(projectile);
	}

	public void addClone(UUID uuid) {
		this.summonedClones.add(uuid);
	}

	public List<UUID> getClones() {
		return this.summonedClones;
	}

	@Nullable
	public UUID getMasterUUID() {
		return this.getEntityData().get(MASTER_LICH).orElse(null);
	}

	@Nullable
	public Lich getMaster() {
		if (this.level() instanceof ServerLevel server && this.getMasterUUID() != null) {
			Entity entity = server.getEntity(this.getMasterUUID());
			if (entity instanceof Lich lich) {
				return lich;
			}
		}
		return null;
	}

	public void setMasterUUID(@Nullable UUID lich) {
		this.getBossBar().setVisible(lich != null);
		this.getEntityData().set(MASTER_LICH, Optional.ofNullable(lich));
	}

	public boolean wantsNewClone(Lich clone) {
		return clone.isShadowClone() && this.countMyClones() < Lich.MAX_SHADOW_CLONES;
	}

	public int countMyClones() {
		// check if there are enough clones
		int count = 0;

		if (this.level() instanceof ServerLevel server) {
			for (UUID uuid : this.summonedClones) {
				Entity clone = server.getEntity(uuid);
				if (clone instanceof Lich lich && lich.getMaster() == this) {
					count++;
				}
			}
		}

		return count;
	}

	public boolean wantsNewMinion() {
		return this.countMyMinions() < Lich.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() {
		return (int) this.level().getEntitiesOfClass(LichMinion.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(32.0D, 16.0D, 32.0D))
			.stream()
			.filter(m -> m.master == this)
			.count();
	}

	//-----------------------------------------//
	//              TELEPORTATION              //
	//-----------------------------------------//

	public void teleportToSightOfEntity(@Nullable Entity entity) {
		if (entity == null || !this.getSensing().hasLineOfSight(entity))
			return;
		Vec3 dest = this.findVecInLOSOf(entity);

		if (dest != null) {
			this.teleportToNoChecks(dest.x(), dest.y(), dest.z());
			this.getLookControl().setLookAt(entity, 100.0F, 100.0F);
			this.yBodyRot = this.getYRot();
		}
	}

	/**
	 * Returns coords that would be good to teleport to.
	 * Returns null if we can't find anything
	 */
	@Nullable
	public Vec3 findVecInLOSOf(@Nullable Entity targetEntity) {
		if (targetEntity == null) return null;
		double origX = this.getX();
		double origY = this.getY();
		double origZ = this.getZ();

		int tries = 100;
		for (int i = 0; i < tries; i++) {
			// we abuse LivingEntity.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
			double tx = targetEntity.getX() + this.getRandom().nextGaussian() * 16D;
			double ty = targetEntity.getY();
			double tz = targetEntity.getZ() + this.getRandom().nextGaussian() * 16D;

			boolean destClear = this.randomTeleport(tx, ty, tz, true);
			boolean canSeeTargetAtDest = this.hasLineOfSight(targetEntity); // Don't use senses cache because we're in a temporary position
			this.teleportTo(origX, origY, origZ);

			if (destClear && canSeeTargetAtDest) {
				return new Vec3(tx, ty, tz);
			}
		}

		return null;
	}

	/**
	 * Does not check that the teleport destination is valid, we just go there
	 */
	private void teleportToNoChecks(double destX, double destY, double destZ) {
		// save original position
		double srcX = this.getX();
		double srcY = this.getY();
		double srcZ = this.getZ();

		// change position
		this.teleportTo(destX, destY, destZ);

		this.makeTeleportTrail(srcX, srcY, srcZ, destX, destY, destZ);
		this.playSound(TFSounds.LICH_TELEPORT.get(), 1.0F, 1.0F);
		this.gameEvent(GameEvent.TELEPORT);

		// sometimes we need to do this
		this.jumping = false;
	}

	//-----------------------------------------//
	//                PARTICLES                //
	//-----------------------------------------//

	public void makeTeleportTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 128;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
			float f1 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
			float f2 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
			double tx = srcX + (destX - srcX) * trailFactor + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2D;
			double ty = srcY + (destY - srcY) * trailFactor + this.getRandom().nextDouble() * this.getBbHeight();
			double tz = srcZ + (destZ - srcZ) * trailFactor + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2D;
			this.level().addParticle(ParticleTypes.EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public void makeMagicTrail(Vec3 source, Vec3 target, float red, float green, float blue) {
		int particles = 60;
		if (!this.level().isClientSide()) {
			for (ServerPlayer serverplayer : ((ServerLevel) this.level()).players()) {
				if (serverplayer.distanceToSqr(source) < 4096.0D) {
					ParticlePacket packet = new ParticlePacket();

					for (int i = 0; i < particles; i++) {
						double trailFactor = i / (particles - 1.0D);
						double tx = source.x() + (target.x() - source.x()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						double ty = source.y() + 0.2D + (target.y() - source.y()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						double tz = source.z() + (target.z() - source.z()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						packet.queueParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue), false, tx, ty, tz, 0.0D, 0.0D, 0.0D);
					}

					PacketDistributor.sendToPlayersTrackingEntity(this, packet);
				}
			}
		}
	}

	//TODO quickly convert candles over time instead of all at once
	private void extinguishNearbyCandles() {
		int range = 16;
		int yRange = 10;
		for (BlockPos pos : BlockPos.betweenClosed(this.blockPosition().offset(-range, 0, -range), this.blockPosition().offset(range, yRange, range))) {
			if (this.level().getBlockState(pos).getBlock() instanceof AbstractCandleBlock && this.level().getBlockState(pos).getValue(BlockStateProperties.LIT)) {
				this.level().setBlockAndUpdate(pos, this.level().getBlockState(pos).setValue(BlockStateProperties.LIT, false));
				this.level().playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 1.0F);
			} else if (this.level().getBlockState(pos).getBlock() instanceof LightableBlock && this.level().getBlockState(pos).getValue(LightableBlock.LIGHTING) == LightableBlock.Lighting.NORMAL) {
				this.level().setBlockAndUpdate(pos, this.level().getBlockState(pos).setValue(LightableBlock.LIGHTING, LightableBlock.Lighting.OMINOUS));
				this.level().playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 0.75F);
			}
		}
	}

	//-----------------------------------------//
	//       METHOD GETTERS AND SETTERS        //
	//-----------------------------------------//

	public void setExtinguishTimer() {
		this.spawnTime = 20;
	}

	/**
	 * What phase of the fight are we on?
	 * <p>
	 * 1 - reflecting bolts, shield up
	 * 2 - summoning minions
	 * 3 - melee
	 */
	public int getPhase() {
		if (this.isShadowClone() || this.getShieldStrength() > 0) {
			return 1;
		} else if (this.getMinionsToSummon() > 0 || this.countMyMinions() > 0) {
			return 2;
		} else {
			return 3;
		}
	}

	public int getAttackCooldown() {
		return this.attackCooldown;
	}

	public void setAttackCooldown(int cooldown) {
		this.attackCooldown = cooldown;
	}

	public int getPopCooldown() {
		return this.popCooldown;
	}

	public void setPopCooldown(int cooldown) {
		this.popCooldown = cooldown;
	}

	public int getScepterTimeLeft() {
		return this.heldScepterTime;
	}

	public void setScepterTime() {
		this.heldScepterTime = 20 + this.getRandom().nextInt(20);
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TFItems.LIFEDRAIN_SCEPTER.get()));
	}

	public void resetScepterTime() {
		this.heldScepterTime = 0;
	}

	public boolean isShadowClone() {
		return this.getEntityData().get(MASTER_LICH).isPresent();
	}

	public int getShieldStrength() {
		return this.isShadowClone() ? 0 : this.getEntityData().get(SHIELD_STRENGTH);
	}

	public void setShieldStrength(int shieldStrength) {
		this.getEntityData().set(SHIELD_STRENGTH, shieldStrength);
	}

	public int getMinionsToSummon() {
		return this.getEntityData().get(MINIONS_LEFT);
	}

	public void setMinionsToSummon(int minionsToSummon) {
		this.getEntityData().set(MINIONS_LEFT, minionsToSummon);
	}

	public int getNextAttackType() {
		return this.getEntityData().get(ATTACK_TYPE);
	}

	public void setNextAttackType(int attackType) {
		this.getEntityData().set(ATTACK_TYPE, attackType);
	}


	//-----------------------------------------//
	//                OVERRIDES                //
	//-----------------------------------------//

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isShadowClone() ? null : TFSounds.LICH_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LICH_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.deathTime > 1 || this.isShadowClone() ? TFSounds.LICH_DEATH.get() : TFSounds.LICH_HURT.get();
	}

	@Override
	public ResourceKey<LootTable> getDefaultLootTable() {
		return !this.isShadowClone() ? super.getDefaultLootTable() : null;
	}

	@Override
	public boolean displayFireAnimation() {
		return this.deathTime <= 0 && super.displayFireAnimation();
	}

	//as funny as left-handed liches are, it would be better if it always holds its scepter/sword in the correct hand
	@Override
	public boolean isLeftHanded() {
		return false;
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.LICH_TOWER;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return getRandom().nextBoolean() ? TFBlocks.CANOPY_CHEST.get() : TFBlocks.TWILIGHT_OAK_CHEST.get();
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.LICH_BOSS_SPAWNER.get();
	}

	@Override
	protected boolean shouldCreateSpawner() {
		return !this.isShadowClone();
	}

	@Override
	protected boolean shouldSpawnLoot() {
		return !this.isShadowClone() && super.shouldSpawnLoot();
	}

	@Override
	public boolean isDeathAnimationFinished() {
		return this.deathTime >= DEATH_ANIMATION_DURATION;
	}

	@Override
	public void tickDeathAnimation() {
		if (this.isShadowClone()) return;

		if (this.deathTime <= DEATH_ANIMATION_POINT_A) {
			boolean done = this.deathTime == DEATH_ANIMATION_POINT_A;
			boolean hurt = this.deathTime % 17 == 0;

			if (hurt) {
				SoundEvent soundevent = this.getHurtSound(this.damageSources().generic());
				if (soundevent != null) {
					this.level().playLocalSound(this, soundevent, SoundSource.HOSTILE, this.getSoundVolume(), this.getVoicePitch());
				}
			}
			if (done) {
				SoundEvent soundevent = this.getDeathSound();
				if (soundevent != null) {
					this.level().playLocalSound(this, soundevent, SoundSource.HOSTILE, this.getSoundVolume(), this.getVoicePitch());
				}
			}

			Vec3 pos = this.position();

			for (int i = 0; i < (hurt ? 12 : 3); i++) {
				double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
				double y = this.getRandom().nextDouble() * this.getBbHeight();
				double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
				this.level().addParticle(this.getRandom().nextBoolean() || hurt ? BONE_PARTICLE : ParticleTypes.SMOKE, false, pos.x() + x, pos.y() + y, pos.z() + z, 0.0D, 0.0D, 0.0D);
			}

			if (hurt) {
				double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
				double y = this.getRandom().nextDouble() * this.getBbHeight();
				double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
				for (int i = 0; i < 7; i++) {
					double x1 = x + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
					double y1 = y + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
					double z1 = z + (this.getRandom().nextDouble() - 0.5D) * 0.1D;
					this.level().addParticle(this.getRandom().nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, false, pos.x() + x1, pos.y() + y1, pos.z() + z1, 0.0D, 0.0D, 0.0D);
				}
			}

			if (done) {
				for (int i = 0; i < 32; i++) {
					double x = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
					double y = this.getRandom().nextDouble() * this.getBbHeight();
					double z = (this.getRandom().nextDouble() - 0.5D) * 0.7D;
					this.level().addParticle(this.getRandom().nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, false, pos.x() + x, pos.y() + y, pos.z() + z, 0.0D, 0.0D, 0.0D);
				}
			}
		} else if (this.deathTime == DEATH_ANIMATION_POINT_B) {
			Vec3 pos = this.position();
			for (int i = 0; i < 3; i++) {
				double x = (this.getRandom().nextDouble() - 0.5D) * 0.75D;
				double z = (this.getRandom().nextDouble() - 0.5D) * 0.75D;
				this.level().addParticle(ParticleTypes.CLOUD, false, pos.x() + x, pos.y(), pos.z() + z, 0.0D, 0.0D, 0.0D);
			}
		} else if (this.deathTime > DEATH_ANIMATION_POINT_B) {
			Vec3 start = this.position().add(0.0D, 0.45F, 0.0D);
			Vec3 end = Vec3.atCenterOf(EntityUtil.bossChestLocation(this));
			int deathTime2 = this.deathTime - DEATH_ANIMATION_POINT_B;
			double factor = (double) deathTime2 / 105.0D;
			double powFactor = Math.pow(factor, 2.0D) * 2.0D;
			double expandFactor = (Math.cos((factor + 0.5D) * Math.PI * 2) + 1.0D) * 0.5D;
			Vec3 particlePos = start.add(end.subtract(start).scale(Math.min(((double) deathTime2 / (double) DEATH_ANIMATION_POINT_B) * 1.25D, 1.0D)));

			for (double i = 0.0D; i < 1.0D; i += 0.2D) {
				double x = Math.sin((powFactor + i) * Math.PI * 2.0D) * expandFactor * 1.25D;
				double z = Math.cos((powFactor + i) * Math.PI * 2.0D) * expandFactor * 1.25D;
				this.level().addParticle(TFParticleType.OMINOUS_FLAME.get(), false, particlePos.x() + x, particlePos.y() - 0.25D, particlePos.z() + z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void tickBossBar() {
		this.getBossBar().setVisible(!this.isShadowClone());
		int phase = this.getPhase();
		if (phase == 1) this.getBossBar().setProgress((float) (this.getShieldStrength()) / (float) (INITIAL_SHIELD_STRENGTH));
		else this.getBossBar().setProgress(this.getHealth() / this.getMaxHealth());
		if (phase != this.previousPhase) this.getBossBar().updateStyle(this.getBossBarColor(), this.getBossBarOverlay(), this.previousPhase != 1);
		this.previousPhase = phase;
	}

	@Override
	public BossEvent.BossBarOverlay getBossBarOverlay() {
		if (this.getShieldStrength() > 0) return BossEvent.BossBarOverlay.NOTCHED_6;
		return super.getBossBarOverlay();
	}

	@Override
	public int getBossBarColor() {
		if (this.getShieldStrength() > 0) return 0xFFD800;
		return this.getPhase() == 2 ? 0xBE23FF : 0xFF0000;
	}
}
