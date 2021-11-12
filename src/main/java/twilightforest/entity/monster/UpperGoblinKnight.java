package twilightforest.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.entity.ai.HeavySpearAttackGoal;

import java.util.List;

public class UpperGoblinKnight extends Monster {

	private static final int SHIELD_DAMAGE_THRESHOLD = 10;
	private static final EntityDataAccessor<Byte> DATA_EQUIP = SynchedEntityData.defineId(UpperGoblinKnight.class, EntityDataSerializers.BYTE);
	private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier("Armor boost", 20, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier("Heavy spear attack boost", 12, AttributeModifier.Operation.ADDITION);
	public static final int HEAVY_SPEAR_TIMER_START = 60;

	private int shieldHits = 0;
	public int heavySpearTimer;

	public UpperGoblinKnight(EntityType<? extends UpperGoblinKnight> type, Level world) {
		super(type, world);

		this.setHasArmor(true);
		this.setHasShield(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new HeavySpearAttackGoal(this));
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
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
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_EQUIP, (byte) 0);
	}

	public boolean hasArmor() {
		return (entityData.get(DATA_EQUIP) & 1) > 0;
	}

	private void setHasArmor(boolean flag) {
		byte otherFlags = entityData.get(DATA_EQUIP);
		entityData.set(DATA_EQUIP, flag ? (byte) (otherFlags | 1) : (byte) (otherFlags & ~1));

		if (!level.isClientSide) {
			if (flag) {
				if (!getAttribute(Attributes.ARMOR).hasModifier(ARMOR_MODIFIER)) {
					getAttribute(Attributes.ARMOR).addTransientModifier(ARMOR_MODIFIER);
				}
			} else {
				getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER);
			}
		}
	}

	public boolean hasShield() {
		return (entityData.get(DATA_EQUIP) & 2) > 0;
	}

	public void setHasShield(boolean flag) {
		byte otherFlags = entityData.get(DATA_EQUIP);
		entityData.set(DATA_EQUIP, flag ? (byte) (otherFlags | 2) : (byte) (otherFlags & ~2));
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
		if ((level.isClientSide || !isNoAi()) && heavySpearTimer > 0) {
			--heavySpearTimer;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.GOBLIN_KNIGHT_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.GOBLIN_KNIGHT_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.GOBLIN_KNIGHT_HURT;
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();

		if (this.isAlive()) {
			// synch target with lower goblin
			if (getVehicle() instanceof LivingEntity && this.getTarget() == null) {
				this.setTarget(((Mob) this.getVehicle()).getTarget());
			}

			if(getTarget() instanceof Player && ((Player)getTarget()).getAbilities().invulnerable) {
				this.setTarget(null);
			}

			if (!isPassenger() && this.hasShield()) {
				this.breakShield();
			}

			if (heavySpearTimer > 0) {
				if (!getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(DAMAGE_MODIFIER)) {
					getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(DAMAGE_MODIFIER);
				}
			} else {
				getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER.getId());
			}
		}
	}

	public void landHeavySpearAttack() {
		// find vector in front of us
		Vec3 vector = this.getLookAngle();

		double dist = 1.25;
		double px = this.getX() + vector.x * dist;
		double py = this.getBoundingBox().minY - 0.75;
		double pz = this.getZ() + vector.z * dist;


		for (int i = 0; i < 50; i++) {
			level.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, (random.nextFloat() - random.nextFloat()) * 0.25F, 0, (random.nextFloat() - random.nextFloat()) * 0.25F);
		}

		// damage things in front that aren't us or our "mount"
		double radius = 1.5D;

		AABB spearBB = new AABB(px - radius, py - radius, pz - radius, px + radius, py + radius, pz + radius);

		List<Entity> inBox = level.getEntities(this, spearBB, e -> e != this.getVehicle());

		for (Entity entity : inBox) {
			super.doHurtTarget(entity);
		}

		if (!inBox.isEmpty()) {
			playSound(SoundEvents.PLAYER_ATTACK_CRIT, getSoundVolume(), getVoicePitch());
		}
	}

	@Override
	public void rideTick() {
		super.rideTick();
		if (getVehicle() instanceof LivingEntity) {
			this.yBodyRot = ((LivingEntity) this.getVehicle()).yBodyRot;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
		} else if (id == 5) {
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

		if (random.nextInt(2) == 0) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
			this.level.broadcastEntityEvent(this, (byte) 4);
			return false;
		}

		this.swing(InteractionHand.MAIN_HAND);
		return super.doHurtTarget(entity);
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		// don't take suffocation damage while riding
		if (damageSource == DamageSource.IN_WALL && this.getVehicle() != null) {
			return false;
		}

		Entity attacker = damageSource.getEntity();

		if (attacker != null && !damageSource.isCreativePlayer()) {
			double dx = this.getX() - attacker.getX();
			double dz = this.getZ() - attacker.getZ();
			float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

			float difference = Mth.abs((this.yBodyRot - angle) % 360);

			if (this.hasShield() && difference > 150 && difference < 230) {
				if (takeHitOnShield(damageSource, amount)) {
					return false;
				}
			} else {
				if (this.hasShield() && random.nextBoolean()) {
					damageShield();
				}
			}

			if (this.hasArmor() && (difference > 300 || difference < 60)) {
				breakArmor();
			}
		}

		return super.hurt(damageSource, amount);
	}

	private void breakArmor() {
		level.broadcastEntityEvent(this, (byte) 5);
		this.setHasArmor(false);
	}

	private void breakShield() {
		level.broadcastEntityEvent(this, (byte) 5);
		this.setHasShield(false);
	}


	public boolean takeHitOnShield(DamageSource source, float amount) {
		if (amount > SHIELD_DAMAGE_THRESHOLD && !this.level.isClientSide) {
			damageShield();
		} else {
			playSound(SoundEvents.ITEM_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}

		// knock back slightly
		LivingEntity toKnockback = (getVehicle() instanceof LivingEntity) ? (LivingEntity) getVehicle() : this;

		if (source.getEntity() != null) {
			double d0 = source.getEntity().getX() - this.getX();
			double d1;

			for (d1 = source.getEntity().getZ() - this.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
				d0 = (Math.random() - Math.random()) * 0.01D;
			}

			toKnockback.knockback(0, d0 / 4D, d1 / 4D);

			// also set revenge target
			if (source.getEntity() instanceof LivingEntity) {
				this.setLastHurtByMob((LivingEntity) source.getEntity());
			}
		}

		return true;
	}


	private void damageShield() {
		playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 0.25F, 0.25F);

		this.shieldHits++;

		if (!level.isClientSide && this.shieldHits >= 3) {
			this.breakShield();
		}
	}
}
