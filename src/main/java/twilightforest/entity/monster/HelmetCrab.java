package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ai.goal.AlwaysWatchTargetGoal;
import twilightforest.init.TFSounds;

public class HelmetCrab extends Monster {

	private static final EntityDataAccessor<Boolean> BLUE = SynchedEntityData.defineId(HelmetCrab.class, EntityDataSerializers.BOOLEAN);

	public float helmetRot;
	public float helmetRotO;

	public HelmetCrab(EntityType<? extends HelmetCrab> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BLUE, false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AlwaysWatchTargetGoal(this));
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.28F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 13.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.28D)
			.add(Attributes.ATTACK_DAMAGE, 3.0D)
			.add(Attributes.ARMOR, 6.0D);
	}

	@Override
	public void tick() {
		super.tick();
		this.helmetRotO = this.helmetRot;
		this.helmetRot = this.yHeadRotO;

		while (this.helmetRot - this.helmetRotO < -180.0F) {
			this.helmetRotO -= 360.0F;
		}

		while (this.helmetRot - this.helmetRotO >= 180.0F) {
			this.helmetRotO += 360.0F;
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("blue", this.isBlue());
	}

	@Override
	public void readAdditionalSaveData(ValueInput compound) {
		super.readAdditionalSaveData(compound);
		this.getEntityData().set(BLUE, compound.getBooleanOr("blue", false));
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
		this.getEntityData().set(BLUE, this.getRandom().nextInt(10000) == 0);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HELMET_CRAB_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HELMET_CRAB_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HELMET_CRAB_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.HELMET_CRAB_STEP.get(), 0.15F, 1.0F);
	}

	public boolean isBlue() {
		return this.getEntityData().get(BLUE);
	}
}
