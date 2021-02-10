package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.ai.EntityAITFChargeAttack;
import twilightforest.entity.boss.EntityTFMinoshroom;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

public class EntityTFMinotaur extends MonsterEntity implements ITFCharger {

	private static final DataParameter<Boolean> CHARGING = EntityDataManager.createKey(EntityTFMinotaur.class, DataSerializers.BOOLEAN);

	public EntityTFMinotaur(EntityType<? extends EntityTFMinotaur> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new EntityAITFChargeAttack(this, 1.5F, this instanceof EntityTFMinoshroom));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(CHARGING, false);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
		ILivingEntityData data = super.onInitialSpawn(worldIn, difficulty, reason, livingdata, dataTag);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return data;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		int random = this.rand.nextInt(10);

		float additionalDiff = difficulty.getAdditionalDifficulty() + 1;

		int result = (int) (random / additionalDiff);

		if (result == 0)
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.minotaur_axe_gold.get()));
		else
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
	}

	@Override
	public boolean isCharging() {
		return dataManager.get(CHARGING);
	}

	@Override
	public void setCharging(boolean flag) {
		dataManager.set(CHARGING, flag);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean success = super.attackEntityAsMob(entity);

		if (success && this.isCharging()) {
			entity.addVelocity(0, 0.4, 0);
			playSound(TFSounds.MINOTAUR_ATTACK, 1.0F, 1.0F);
		}

		return success;
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (isCharging()) {
			this.limbSwingAmount += 0.6;
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
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F;
	}

}
