package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.ai.goal.ChargeAttackGoal;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

public class Minotaur extends Monster implements ITFCharger {

	private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(Minotaur.class, EntityDataSerializers.BOOLEAN);

	public Minotaur(EntityType<? extends Minotaur> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 1.5F, this instanceof Minoshroom));
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
				.add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(CHARGING, false);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, reason, data, tag);
		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor.getRandom(), difficulty);
		return data;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
		int random = this.getRandom().nextInt(10);
		float additionalDiff = difficulty.getEffectiveDifficulty() + 1;
		int result = (int) (random / additionalDiff);
		if (result == 0)
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.GOLDEN_MINOTAUR_AXE.get()));
		else
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
	}

	@Override
	public boolean isCharging() {
		return this.getEntityData().get(CHARGING);
	}

	@Override
	public void setCharging(boolean flag) {
		this.getEntityData().set(CHARGING, flag);
	}

	//[VanillaCopy] of Mob.doHurtTarget, edits noted
	@Override
	public boolean doHurtTarget(Entity entity) {
		float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		if (entity instanceof LivingEntity living) {
			f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), living.getMobType());
			f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
		}

		int i = EnchantmentHelper.getFireAspect(this);
		if (i > 0) {
			entity.setSecondsOnFire(i * 4);
		}

		//TF: change damage source to minotaur one
		boolean flag = entity.hurt(TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.AXING, this), f);
		if (flag) {
			if (f1 > 0.0F && entity instanceof LivingEntity living) {
				living.knockback(f1 * 0.5F, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			}

			//TF: check if we're charging. If we are, throw the player upwards and play a sound
			if (this.isCharging()) {
				entity.push(this.getDirection().getStepX(), 0.35D, this.getDirection().getStepZ());
				this.playSound(this.getChargeSound(), 1.0F, 1.0F);
			}

			if (entity instanceof Player player) {
				this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
			}

			this.doEnchantDamageEffects(this, entity);
			this.setLastHurtMob(entity);
		}

		return flag;
	}

	protected SoundEvent getChargeSound() {
		return TFSounds.MINOTAUR_ATTACK.get();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.isCharging()) {
			this.walkAnimation.setSpeed(this.walkAnimation.speed() + 0.6F);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINOTAUR_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.MINOTAUR_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINOTAUR_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(TFSounds.MINOTAUR_STEP.get(), 0.15F, 0.8F);
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.7F;
	}
}
