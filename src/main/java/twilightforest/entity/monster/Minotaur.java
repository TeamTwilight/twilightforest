package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFSounds;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.ai.ChargeAttackGoal;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.item.TFItems;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;

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
		entityData.define(CHARGING, false);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(worldIn, difficulty, reason, livingdata, dataTag);
		this.populateDefaultEquipmentSlots(difficulty);
		this.populateDefaultEquipmentEnchantments(difficulty);
		return data;
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		int random = this.random.nextInt(10);

		float additionalDiff = difficulty.getEffectiveDifficulty() + 1;

		int result = (int) (random / additionalDiff);

		if (result == 0)
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.GOLDEN_MINOTAUR_AXE.get()));
		else
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
	}

	@Override
	public boolean isCharging() {
		return entityData.get(CHARGING);
	}

	@Override
	public void setCharging(boolean flag) {
		entityData.set(CHARGING, flag);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		entity.hurt(TFDamageSources.axing(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
		boolean success = super.doHurtTarget(entity);
		if (success && this.isCharging()) {
			entity.hurt(TFDamageSources.axing(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
			entity.push(0, 0.4, 0);
			playSound(TFSounds.MINOTAUR_ATTACK, 1.0F, 1.0F);
		}

		return success;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (isCharging()) {
			this.animationSpeed += 0.6;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINOTAUR_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.MINOTAUR_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINOTAUR_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.MINOTAUR_STEP, 0.15F, 0.8F);
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
	}

}
