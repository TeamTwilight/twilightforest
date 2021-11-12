package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.RiderSpearAttackGoal;

import javax.annotation.Nullable;

public class LowerGoblinKnight extends Monster {

	private static final EntityDataAccessor<Boolean> ARMOR = SynchedEntityData.defineId(LowerGoblinKnight.class, EntityDataSerializers.BOOLEAN);
	private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier("Armor boost", 17, AttributeModifier.Operation.ADDITION);

	public LowerGoblinKnight(EntityType<? extends LowerGoblinKnight> type, Level world) {
		super(type, world);
		this.setHasArmor(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new RiderSpearAttackGoal(this));
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
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ARMOR, false);
	}

	public boolean hasArmor() {
		return entityData.get(ARMOR);
	}

	private void setHasArmor(boolean flag) {
		entityData.set(ARMOR, flag);

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

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("hasArmor", this.hasArmor());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setHasArmor(compound.getBoolean("hasArmor"));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag dataTag) {
		livingData = super.finalizeSpawn(worldIn, difficulty, reason, livingData, dataTag);

		UpperGoblinKnight upper = new UpperGoblinKnight(TFEntities.UPPER_GOBLIN_KNIGHT, this.level);
		upper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
		upper.finalizeSpawn(worldIn, difficulty, MobSpawnType.NATURAL, livingData, dataTag);
		upper.startRiding(this);

		return livingData;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 1.0D;
	}

	@Override
	public void customServerAiStep() {
		if (isVehicle() && getPassengers().get(0) instanceof LivingEntity && this.getTarget() == null) {
			this.setTarget(((Mob) this.getPassengers().get(0)).getTarget());
		}
		if(getTarget() instanceof Player && ((Player)getTarget()).getAbilities().invulnerable) {
			this.setTarget(null);
		}
		super.customServerAiStep();
	}

	@Override
	public boolean doHurtTarget(Entity entity) {

		if (isVehicle() && getPassengers().get(0) instanceof LivingEntity) {
			return ((LivingEntity) this.getPassengers().get(0)).doHurtTarget(entity);
		} else {
			return super.doHurtTarget(entity);
		}

	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return this.isVehicle() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_AMBIENT : TFSounds.GOBLIN_KNIGHT_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isVehicle() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_DEATH : TFSounds.GOBLIN_KNIGHT_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.isVehicle() ? TFSounds.GOBLIN_KNIGHT_MUFFLED_HURT : TFSounds.GOBLIN_KNIGHT_HURT;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		// check the angle of attack, if applicable
		Entity attacker = null;
		if (source.getEntity() != null) {
			attacker = source.getEntity();
		}

		if (source.getEntity() != null) {
			attacker = source.getEntity();
		}

		if (attacker != null && !source.isCreativePlayer()) {
			// determine angle

			double dx = this.getX() - attacker.getX();
			double dz = this.getZ() - attacker.getZ();
			float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

			float difference = Mth.abs((this.yBodyRot - angle) % 360);

			// shield?
			UpperGoblinKnight upper = null;

			if (isVehicle() && getPassengers().get(0) instanceof UpperGoblinKnight) {
				upper = (UpperGoblinKnight) this.getPassengers().get(0);
			}

			if (upper != null && upper.hasShield() && difference > 150 && difference < 230) {
				if (upper.takeHitOnShield(source, amount)) {
					return false;
				}
			}

			// break armor?
			if (this.hasArmor() && (difference > 300 || difference < 60)) {
				breakArmor();
			}
		}

		return super.hurt(source, amount);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id == 5) {
			ItemStack broken = new ItemStack(Items.IRON_CHESTPLATE);
			this.breakItem(broken);
			this.breakItem(broken);
			this.breakItem(broken);
		} else {
			super.handleEntityEvent(id);
		}
	}

	private void breakArmor() {
		level.broadcastEntityEvent(this, (byte) 5);
		this.setHasArmor(false);
	}
}
