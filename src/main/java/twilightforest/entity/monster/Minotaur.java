package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.ai.goal.ChargeAttackGoal;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.entities.EntityUtil;

public class Minotaur extends Monster implements ITFCharger {

	private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(Minotaur.class, EntityDataSerializers.BOOLEAN);

	public Minotaur(EntityType<? extends Minotaur> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 1.5F, false));
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
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CHARGING, false);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(accessor, difficulty, reason, data);
		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor, accessor.getRandom(), difficulty);
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

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		return EntityUtil.properlyApplyCustomDamageSource(level, this, entity, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.AXING, this), TFSounds.MINOTAUR_ATTACK.get());
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
