package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.AbstractLightableBlock;
import twilightforest.data.tags.DamageTypeTagGenerator;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.goal.*;
import twilightforest.entity.monster.LichMinion;
import twilightforest.init.*;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lich extends Monster implements EnforcedHomePoint, IBossLootBuffer {

	private static final EntityDataAccessor<Boolean> IS_CLONE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> SHIELD_STRENGTH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MINIONS_LEFT = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.INT);
	private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(27, ItemStack.EMPTY);

	public static final int MAX_SHADOW_CLONES = 2;
	public static final int INITIAL_SHIELD_STRENGTH = 6;
	public static final int MAX_ACTIVE_MINIONS = 3;
	public static final int INITIAL_MINIONS_TO_SUMMON = 9;
	public static final int MAX_HEALTH = 100;

	@Nullable
	private Lich masterLich;
	private int attackCooldown;
	private int popCooldown;
	private int heldScepterTime;
	private int spawnTime;
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_6);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public Lich(EntityType<? extends Lich> type, Level world) {
		super(type, world);

		this.setShadowClone(false);
		this.masterLich = null;
		this.xpReward = 217;
	}

	public Lich(Level level, Lich otherLich) {
		this(TFEntities.LICH.get(), level);

		this.setShadowClone(true);
		this.masterLich = otherLich;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.45D); // Same speed as an angry enderman
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(IS_CLONE, false);
		this.entityData.define(SHIELD_STRENGTH, INITIAL_SHIELD_STRENGTH);
		this.entityData.define(MINIONS_LEFT, INITIAL_MINIONS_TO_SUMMON);
		this.entityData.define(ATTACK_TYPE, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AlwaysWatchTargetGoal(this));
		this.goalSelector.addGoal(1, new LichPopMobsGoal(this));
		this.goalSelector.addGoal(1, new LichAbsorbMinionsGoal(this));
		this.goalSelector.addGoal(2, new LichShadowsGoal(this));
		this.goalSelector.addGoal(3, new LichMinionsGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 0.75D, true) {
			@Override
			public boolean canUse() {
				return getPhase() == 3 && super.canUse();
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
				if (this.mob instanceof Lich main && this.mob.getLastHurtByMob() instanceof Lich lich && lich.masterLich == main.masterLich) {
					return false;
				}
				return super.canUse();
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		compound.putBoolean("ShadowClone", this.isShadowClone());
		compound.putInt("ShieldStrength", this.getShieldStrength());
		compound.putInt("MinionsToSummon", this.getMinionsToSummon());
		this.addDeathItemsSaveData(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.readDeathItemsSaveData(compound);
		this.loadHomePointFromNbt(compound, 20);
		this.setShadowClone(compound.getBoolean("ShadowClone"));
		this.setShieldStrength(compound.getInt("ShieldStrength"));
		this.setMinionsToSummon(compound.getInt("MinionsToSummon"));

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
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
	public void aiStep() {
		if (!this.getLevel().isClientSide()) {
			if (this.getPhase() == 1) {
				this.bossInfo.setProgress((float) (this.getShieldStrength()) / (float) (INITIAL_SHIELD_STRENGTH));
			} else {
				this.bossInfo.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
				this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
				if (this.getPhase() == 2)
					this.bossInfo.setColor(BossEvent.BossBarColor.PURPLE);
				else
					this.bossInfo.setColor(BossEvent.BossBarColor.RED);
			}
		}

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

			this.getLevel().addParticle(ParticleTypes.ENTITY_EFFECT, dx + (this.getRandom().nextGaussian() * 0.025), dy + (this.getRandom().nextGaussian() * 0.025), dz + (this.getRandom().nextGaussian() * 0.025), red, grn, blu);
		}

		if (this.getPhase() == 3)
			this.getLevel().addParticle(ParticleTypes.ANGRY_VILLAGER,
					this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
					this.getY() + 1.0D + this.getRandom().nextFloat() * this.getBbHeight(),
					this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
					this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

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
			teleportToSightOfEntity(this.getTarget());
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
				this.playSound(TFSounds.SHIELD_BREAK.get(), 1.0F, this.getVoicePitch() * 2.0F);
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

			if (src.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
				this.hurtBy.add(player);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void lavaHurt() {
		if (!this.fireImmune()) {
			this.setSecondsOnFire(5);
			if (this.hurt(this.damageSources().lava(), 4F)) {
				this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
				EntityUtil.killLavaAround(this);
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (this.getLevel() instanceof ServerLevel serverLevel && !this.isShadowClone()) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.getLevel(), this, TFStructures.LICH_TOWER, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			IBossLootBuffer.saveDropsIntoBoss(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), serverLevel);
		}
	}

	private static final ItemParticleOption BONE_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.BONE.getDefaultInstance());

	@Override
	protected void tickDeath() {
		++this.deathTime;

		if (this.level instanceof ServerLevel) {
			int maxDeath = 175;//How many ticks until the body disappears
			if (this.deathTime <= 50) {
				boolean done = this.deathTime == 50;
				boolean hurt = this.deathTime % 17 == 0;

				if (hurt) this.playHurtSound(this.damageSources().generic());
				if (done) {
					SoundEvent soundevent = this.getDeathSound();
					if (soundevent != null) {
						this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
					}
				}

				Vec3 pos = this.position();
				ParticlePacket particlePacket = new ParticlePacket();

				for (int i = 0; i < (hurt ? 12 : 3); i++) {
					double x = (this.random.nextDouble() - 0.5D) * 0.7D;
					double y = this.random.nextDouble() * this.getBbHeight();
					double z = (this.random.nextDouble() - 0.5D) * 0.7D;
					particlePacket.queueParticle(this.random.nextBoolean() || hurt ? BONE_PARTICLE : ParticleTypes.SMOKE, false, pos.add(x, y, z), Vec3.ZERO);
				}

				if (hurt) {
					double x = (this.random.nextDouble() - 0.5D) * 0.7D;
					double y = this.random.nextDouble() * this.getBbHeight();
					double z = (this.random.nextDouble() - 0.5D) * 0.7D;
					for (int i = 0; i < 7; i++) {
						double x1 = x + (this.random.nextDouble() - 0.5D) * 0.1D;
						double y1 = y + (this.random.nextDouble() - 0.5D) * 0.1D;
						double z1 = z + (this.random.nextDouble() - 0.5D) * 0.1D;
						particlePacket.queueParticle(this.random.nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, false, pos.add(x1, y1, z1), Vec3.ZERO);
					}
				}

				if (done) {
					for (int i = 0; i < 32; i++) {
						double x = (this.random.nextDouble() - 0.5D) * 0.7D;
						double y = this.random.nextDouble() * this.getBbHeight();
						double z = (this.random.nextDouble() - 0.5D) * 0.7D;
						particlePacket.queueParticle(this.random.nextBoolean() ? BONE_PARTICLE : ParticleTypes.CLOUD, false, pos.add(x, y, z), Vec3.ZERO);
					}
				}

				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), particlePacket);
			} else if (this.deathTime == 70) {
				ParticlePacket particlePacket = new ParticlePacket();
				for (int i = 0; i < 3; i++) {
					double x = (this.random.nextDouble() - 0.5D) * 0.75D;
					double z = (this.random.nextDouble() - 0.5D) * 0.75D;
					particlePacket.queueParticle(ParticleTypes.CLOUD, false, this.position().add(x, 0.0D, z), Vec3.ZERO);
				}

				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), particlePacket);
			} else if (this.deathTime > 70) {
				boolean flag = this.deathTime >= maxDeath && !this.isRemoved();

				Vec3 start = this.position().add(0.0D, 0.45F, 0.0D);
				Vec3 end = Vec3.atCenterOf(EntityUtil.bossChestLocation(this));
				int deathTime2 = this.deathTime - 70;
				double factor = (double) deathTime2 / 105.0D;
				double powFactor = Math.pow(factor, 2.0D) * 2.0D;
				double expandFactor = (Math.cos((factor + 0.5D) * Math.PI * 2) + 1.0D) * 0.5D;
				Vec3 particlePos = start.add(end.subtract(start).scale(Math.min(((double) deathTime2 / 70.0D) * 1.25D, 1.0D)));
				ParticlePacket particlePacket = new ParticlePacket();
				if (this.deathTime >= maxDeath - 3) {
					for (int i = 0; i < 40; i++) {
						double x = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						double y = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						double z = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						particlePacket.queueParticle(this.random.nextBoolean() ? TFParticleType.OMINOUS_FLAME.get() : ParticleTypes.POOF, false, end.add(x, y, z), Vec3.ZERO);
					}
				}
				if (flag) {
					for (int i = 0; i < 16; i++) {
						double x = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						double y = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						double z = (this.random.nextDouble() - 0.5D) * 0.075D * i;
						particlePacket.queueParticle(ParticleTypes.POOF, false, start.add(x, y, z), Vec3.ZERO);
					}
				}
				for (double i = 0.0D; i < 1.0D; i += 0.2D) {
					double x = Math.sin((powFactor + i) * Math.PI * 2.0D) * expandFactor * 1.25D;
					double z = Math.cos((powFactor + i) * Math.PI * 2.0D) * expandFactor * 1.25D;
					particlePacket.queueParticle(TFParticleType.OMINOUS_FLAME.get(), false, particlePos.add(x, -0.25D, z), Vec3.ZERO);
				}

				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), particlePacket);

				if (flag) this.remove(RemovalReason.KILLED);
			}
		}
	}

	@Override
	public void remove(RemovalReason removalReason) {
		if (removalReason.equals(RemovalReason.KILLED) && this.level instanceof ServerLevel serverLevel) {
			IBossLootBuffer.depositDropsIntoChest(this, this.random.nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get().defaultBlockState() : TFBlocks.CANOPY_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this), serverLevel);
		}
		super.remove(removalReason);
	}

	@Override
	public void checkDespawn() {
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL && !this.isShadowClone()) {
			if (this.hasRestriction()) {
				this.getLevel().setBlockAndUpdate(getRestrictCenter(), TFBlocks.LICH_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	//-----------------------------------------//
	//    PROJECTILES, CLONES, AND MINIONS     //
	//-----------------------------------------//

	public void launchProjectileAt(ThrowableProjectile projectile) {
		float bodyFacingAngle = ((this.yBodyRot * Mth.PI) / 180F);
		double sx = getX() + (Mth.cos(bodyFacingAngle) * 0.65D);
		double sy = getY() + (this.getBbHeight() * 0.82D);
		double sz = getZ() + (Mth.sin(bodyFacingAngle) * 0.65D);

		double tx = Objects.requireNonNull(this.getTarget()).getX() - sx;
		double ty = (this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F) - (this.getY() + this.getBbHeight() / 2.0F);
		double tz = this.getTarget().getZ() - sz;

		this.playSound(TFSounds.LICH_SHOOT.get(), this.getSoundVolume(), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);

		projectile.moveTo(sx, sy, sz, getYRot(), getXRot());
		projectile.shoot(tx, ty, tz, 0.5F, 1.0F);

		this.getLevel().addFreshEntity(projectile);
	}

	public boolean wantsNewClone(Lich clone) {
		return clone.isShadowClone() && this.countMyClones() < Lich.MAX_SHADOW_CLONES;
	}

	public int countMyClones() {
		// check if there are enough clones.  we check a 32x16x32 area
		int count = 0;

		for (Lich nearbyLich : this.getNearbyLiches()) {
			if (nearbyLich.isShadowClone() && nearbyLich.getMasterLich() == this) {
				count++;
			}
		}

		return count;
	}

	public List<? extends Lich> getNearbyLiches() {
		return this.getLevel().getEntitiesOfClass(getClass(), new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(32.0D, 16.0D, 32.0D));
	}

	public boolean wantsNewMinion() {
		return countMyMinions() < Lich.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() {
		return (int) this.getLevel().getEntitiesOfClass(LichMinion.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(32.0D, 16.0D, 32.0D))
				.stream()
				.filter(m -> m.master == this)
				.count();
	}

	//-----------------------------------------//
	//              TELEPORTATION              //
	//-----------------------------------------//

	public void teleportToSightOfEntity(@Nullable Entity entity) {
		Vec3 dest = this.findVecInLOSOf(entity);
		double srcX = getX();
		double srcY = getY();
		double srcZ = getZ();

		if (dest != null && entity != null) {
			this.teleportToNoChecks(dest.x(), dest.y(), dest.z());
			this.getLookControl().setLookAt(entity, 100.0F, 100.0F);
			this.yBodyRot = this.getYRot();

			if (!this.getSensing().hasLineOfSight(entity)) {
				this.teleportToNoChecks(srcX, srcY, srcZ);
			}
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
		this.getLevel().playSound(null, srcX, srcY, srcZ, TFSounds.LICH_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
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
			this.getLevel().addParticle(ParticleTypes.EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public void makeMagicTrail(Vec3 source, Vec3 target, float red, float green, float blue) {
		int particles = 60;
		if (!this.getLevel().isClientSide()) {
			for (ServerPlayer serverplayer : ((ServerLevel) this.getLevel()).players()) {
				if (serverplayer.distanceToSqr(source) < 4096.0D) {
					ParticlePacket packet = new ParticlePacket();

					for (int i = 0; i < particles; i++) {
						double trailFactor = i / (particles - 1.0D);
						double tx = source.x() + (target.x() - source.x()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						double ty = source.y() + 0.2D + (target.y() - source.y()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						double tz = source.z() + (target.z() - source.z()) * trailFactor + this.getRandom().nextGaussian() * 0.005D;
						packet.queueParticle(ParticleTypes.ENTITY_EFFECT, false, tx, ty, tz, red, green, blue);
					}

					TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverplayer), packet);
				}
			}
		}
	}

	private void extinguishNearbyCandles() {
		for (BlockPos pos : WorldUtil.getAllAround(this.blockPosition(), 10)) {
			if (level.getBlockState(pos).getBlock() instanceof AbstractCandleBlock && level.getBlockState(pos).getValue(BlockStateProperties.LIT)) {
				level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(BlockStateProperties.LIT, false));
				level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 1.0F);
			} else if (level.getBlockState(pos).getBlock() instanceof AbstractLightableBlock && level.getBlockState(pos).getValue(AbstractLightableBlock.LIGHTING) == AbstractLightableBlock.Lighting.NORMAL) {
				level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(AbstractLightableBlock.LIGHTING, AbstractLightableBlock.Lighting.OMINOUS));
				level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 0.75F);
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

	@Nullable
	public Lich getMasterLich() {
		return this.masterLich;
	}

	public void setMaster(Lich lich) {
		this.masterLich = lich;
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
		return this.entityData.get(IS_CLONE);
	}

	public void setShadowClone(boolean shadowClone) {
		this.bossInfo.setVisible(!shadowClone);
		this.entityData.set(IS_CLONE, shadowClone);
	}

	public int getShieldStrength() {
		return this.entityData.get(SHIELD_STRENGTH);
	}

	public void setShieldStrength(int shieldStrength) {
		this.entityData.set(SHIELD_STRENGTH, shieldStrength);
	}

	public int getMinionsToSummon() {
		return this.entityData.get(MINIONS_LEFT);
	}

	public void setMinionsToSummon(int minionsToSummon) {
		this.entityData.set(MINIONS_LEFT, minionsToSummon);
	}

	public int getNextAttackType() {
		return this.entityData.get(ATTACK_TYPE);
	}

	public void setNextAttackType(int attackType) {
		this.entityData.set(ATTACK_TYPE, attackType);
	}


	//-----------------------------------------//
	//                OVERRIDES                //
	//-----------------------------------------//

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.LICH_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LICH_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.deathTime > 1 ? TFSounds.LICH_DEATH.get() : TFSounds.LICH_HURT.get();
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		return !this.isShadowClone() ? super.getDefaultLootTable() : null;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
	}

	@Override
	public boolean displayFireAnimation() {
		return this.deathTime <= 0 && super.displayFireAnimation();
	}

	//as funny as left handed liches are, it would be better if it always holds its scepter/sword in the correct hand
	@Override
	public boolean isLeftHanded() {
		return false;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
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
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public BlockPos getRestrictionCenter() {
		return this.getRestrictCenter();
	}

	@Override
	public void setRestriction(BlockPos pos, int dist) {
		this.restrictTo(pos, dist);
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.dyingInventory;
	}
}
