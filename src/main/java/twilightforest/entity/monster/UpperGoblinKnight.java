package twilightforest.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.goal.HeavySpearAttackGoal;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;

import java.util.List;
import java.util.Objects;

public class UpperGoblinKnight extends Monster {

	private static final int SHIELD_DAMAGE_THRESHOLD = 10;
	private static final EntityDataAccessor<Byte> DATA_EQUIP = SynchedEntityData.defineId(UpperGoblinKnight.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> SHIELD_DISABLED = SynchedEntityData.defineId(UpperGoblinKnight.class, EntityDataSerializers.BOOLEAN);

	private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(TwilightForestMod.prefix("armor_boost"), 20, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier(TwilightForestMod.prefix("spear_attack_boost"), 12, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	public static final int HEAVY_SPEAR_TIMER_START = 60;

	private int shieldHits = 0;
	private int shieldDisabledTicks;
	public int heavySpearTimer;

	@SuppressWarnings("this-escape")
	public UpperGoblinKnight(EntityType<? extends UpperGoblinKnight> type, Level level) {
		super(type, level);

		this.setHasArmor(true);
		this.setHasShield(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new HeavySpearAttackGoal(this));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false) {
			@Override
			public boolean canUse() {
				return !(((UpperGoblinKnight) this.mob).heavySpearTimer > 0) && super.canUse();
			}
		});
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.28D)
			.add(Attributes.ATTACK_DAMAGE, 8.0D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_EQUIP, (byte) 0);
		builder.define(SHIELD_DISABLED, false);
	}

	public boolean hasArmor() {
		return (this.getEntityData().get(DATA_EQUIP) & 1) > 0;
	}

	private void setHasArmor(boolean flag) {
		byte otherFlags = this.getEntityData().get(DATA_EQUIP);
		this.getEntityData().set(DATA_EQUIP, flag ? (byte) (otherFlags | 1) : (byte) (otherFlags & ~1));

		if (!this.level().isClientSide()) {
			if (flag) {
				if (!Objects.requireNonNull(getAttribute(Attributes.ARMOR)).hasModifier(ARMOR_MODIFIER.id())) {
					Objects.requireNonNull(getAttribute(Attributes.ARMOR)).addTransientModifier(ARMOR_MODIFIER);
				}
			} else {
				Objects.requireNonNull(getAttribute(Attributes.ARMOR)).removeModifier(ARMOR_MODIFIER.id());
			}
		}
	}

	public boolean hasShield() {
		return (this.getEntityData().get(DATA_EQUIP) & 2) != 0;
	}

	public void setHasShield(boolean flag) {
		byte otherFlags = this.getEntityData().get(DATA_EQUIP);
		this.getEntityData().set(DATA_EQUIP, flag ? (byte) (otherFlags | 2) : (byte) (otherFlags & ~2));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("hasArmor", this.hasArmor());
		compound.putBoolean("hasShield", this.hasShield());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setHasArmor(compound.getBoolean("hasArmor"));
		this.setHasShield(compound.getBoolean("hasShield"));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		// Must be decremented on client as well for rendering
		if ((this.level().isClientSide() || !this.isNoAi()) && this.heavySpearTimer > 0) {
			--this.heavySpearTimer;
		}

		if (this.isShieldDisabled()) {
			this.level().addParticle(ParticleTypes.SPLASH,
				this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.25D,
				this.getY() + this.getEyeHeight(),
				this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.25D,
				(this.getRandom().nextFloat() - 0.5F) * 0.75F,
				0,
				(this.getRandom().nextFloat() - 0.5F) * 0.75F);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.GOBLIN_KNIGHT_AMBIENT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.GOBLIN_KNIGHT_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.GOBLIN_KNIGHT_HURT.get();
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();

		if (this.isShieldDisabled() && this.shieldDisabledTicks++ >= 100) {
			this.shieldDisabledTicks = 0;
			this.getEntityData().set(SHIELD_DISABLED, false);
		}

		if (this.isAlive()) {
			// synch target with lower goblin
			if (this.getVehicle() instanceof Mob mob && this.getTarget() == null) {
				this.setTarget(mob.getTarget());
			}

			if (!this.isPassenger() && this.hasShield()) {
				this.breakShield();
			}

			if (this.heavySpearTimer > 0) {
				if (!Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).hasModifier(DAMAGE_MODIFIER.id())) {
					Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).addTransientModifier(DAMAGE_MODIFIER);
				}
			} else {
				Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(DAMAGE_MODIFIER.id());
			}
		}
	}

	public void landHeavySpearAttack() {
		// find vector in front of us
		Vec3 vector = this.getLookAngle();

		double dist = 1.25;
		double px = this.getX() + vector.x() * dist;
		double py = this.getBoundingBox().minY - (this.isPassenger() ? 0.75D : 0.0D);
		double pz = this.getZ() + vector.z() * dist;

		if (this.level() instanceof ServerLevel) {
			ParticlePacket particlePacket = new ParticlePacket();
			for (int i = 0; i < 50; i++) {
				particlePacket.queueParticle(ParticleTypes.LARGE_SMOKE, false,
					px + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.25F * this.getRandom().nextGaussian(),
					py,
					pz + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.25F * this.getRandom().nextGaussian(),
					0, 0, 0);
			}
			PacketDistributor.sendToPlayersTrackingEntity(this, particlePacket);
		}

		// damage things in front that aren't us or our "mount"
		double radius = 1.5D;

		AABB spearBB = new AABB(px - radius, py - radius, pz - radius, px + radius, py + radius, pz + radius);

		List<Entity> inBox = this.level().getEntities(this, spearBB, e -> e != this.getVehicle());

		for (Entity entity : inBox) {
			super.doHurtTarget(entity);
		}

		if (!inBox.isEmpty()) {
			this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, getSoundVolume(), getVoicePitch());
		}

		this.gameEvent(GameEvent.HIT_GROUND);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == EntityEvent.START_ATTACKING) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
		} else if (id == EntityEvent.STOP_ATTACKING) {
			ItemStack broken = new ItemStack(Items.IRON_CHESTPLATE);
			this.breakItem(broken);
			this.breakItem(broken);
			this.breakItem(broken);
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	public boolean doHurtTarget(Entity entity) {

		if (this.heavySpearTimer > 0) {
			return false;
		}

		if (this.getRandom().nextInt(2) == 0) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
			this.level().broadcastEntityEvent(this, (byte) 4);
			return false;
		}

		this.swing(InteractionHand.MAIN_HAND);
		return super.doHurtTarget(entity);
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		// don't take suffocation damage while riding
		if (damageSource.is(DamageTypes.IN_WALL) && this.getVehicle() != null) {
			return false;
		}

		Entity attacker = damageSource.getEntity();

		if (attacker != null) {
			double dx = this.getX() - attacker.getX();
			double dz = this.getZ() - attacker.getZ();
			float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

			float difference = Mth.abs((this.yBodyRot - angle) % 360);

			if (this.hasShield() && difference > 150 && difference < 230) {
				if (this.takeHitOnShield(damageSource, amount)) {
					return false;
				}
			} else {
				if (this.hasShield() && this.getRandom().nextBoolean()) {
					this.damageShield();
				}
			}

			if (this.hasArmor() && (difference > 300 || difference < 60)) {
				this.breakArmor();
			}
		}

		return super.hurt(damageSource, amount);
	}

	private void breakArmor() {
		this.level().broadcastEntityEvent(this, (byte) 5);
		this.setHasArmor(false);
	}

	private void breakShield() {
		this.level().broadcastEntityEvent(this, (byte) 5);
		this.setHasShield(false);
	}

	public boolean isShieldDisabled() {
		return this.getEntityData().get(SHIELD_DISABLED);
	}

	public boolean takeHitOnShield(DamageSource source, float amount) {
		if (this.isShieldDisabled()) return false;

		if (source.getEntity() instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof AxeItem && !this.level().isClientSide()) {
			this.getEntityData().set(SHIELD_DISABLED, true);
			this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
			return true;
		}

		if (amount > SHIELD_DAMAGE_THRESHOLD && !this.level().isClientSide()) {
			this.damageShield();
		} else {
			this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
		}

		// knock back slightly
		LivingEntity toKnockback = (this.getVehicle() instanceof LivingEntity) ? (LivingEntity) this.getVehicle() : this;

		if (source.getEntity() != null) {
			double d0 = source.getEntity().getX() - this.getX();
			double d1;

			for (d1 = source.getEntity().getZ() - this.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
				d0 = (Math.random() - Math.random()) * 0.01D;
			}

			toKnockback.knockback(0, d0 / 4D, d1 / 4D);

			// also set revenge target
			if (source.getEntity() instanceof LivingEntity living) {
				this.setLastHurtByMob(living);
			}
		}

		return true;
	}


	private void damageShield() {
		this.playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 0.25F, 0.25F);

		this.shieldHits++;

		if (!this.level().isClientSide() && this.shieldHits >= 3) {
			this.breakShield();
		}
	}
}
