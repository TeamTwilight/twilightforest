package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.ai.goal.ChargeAttackGoal;
import twilightforest.entity.ai.goal.GroundAttackGoal;
import twilightforest.entity.monster.Minotaur;
import twilightforest.init.*;
import twilightforest.util.entities.EntityUtil;

public class Minoshroom extends BaseTFBoss implements ITFCharger {

	private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> GROUND_ATTACK = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> GROUND_CHARGE = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.INT);

	public float prevClientSideChargeAnimation;
	public float clientSideChargeAnimation;
	private boolean groundSmashState = false;

	public Minoshroom(EntityType<? extends Minoshroom> type, Level level) {
		super(type, level);
		this.xpReward = 100;
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new GroundAttackGoal(this));
		this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 1.5F, true));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.addRestrictionGoals(this, this.goalSelector);
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CHARGING, false);
		builder.define(GROUND_ATTACK, false);
		builder.define(GROUND_CHARGE, 0);
	}

	@Override
	public boolean isCharging() {
		return this.getEntityData().get(CHARGING);
	}

	@Override
	public void setCharging(boolean flag) {
		this.getEntityData().set(CHARGING, flag);
	}

	public boolean isGroundAttackCharge() {
		return this.getEntityData().get(GROUND_ATTACK);
	}

	public void setGroundAttackCharge(boolean flag) {
		this.getEntityData().set(GROUND_ATTACK, flag);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Minotaur.registerAttributes()
			.add(Attributes.MAX_HEALTH, 120.0D)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
			if (this.isGroundAttackCharge()) {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation + (1.0F / ((float) this.getEntityData().get(GROUND_CHARGE)) * 6.0F), 0.0F, 6.0F);
				this.groundSmashState = true;
			} else {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
				if (this.groundSmashState) {
					BlockState block = this.level().getBlockState(this.blockPosition().below());

					for (int i = 0; i < 80; i++) {
						double cx = this.blockPosition().getX() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;
						double cy = this.getBoundingBox().minY + 0.1F + level().getRandom().nextFloat() * 0.3F;
						double cz = this.blockPosition().getZ() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;

						this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), cx, cy, cz, 0.0D, 0.0D, 0.0D);
					}
					this.groundSmashState = false;
				}
			}
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.isCharging()) {
			this.walkAnimation.setSpeed(this.walkAnimation.speed() + 0.6F);
		}
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
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		return EntityUtil.properlyApplyCustomDamageSource(level, this, entity, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.AXING, this), TFSounds.MINOSHROOM_ATTACK.get());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINOSHROOM_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.MINOSHROOM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINOSHROOM_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.MINOSHROOM_STEP.get(), 0.15F, 0.8F);
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.7F;
	}

	public void setMaxCharge(int charge) {
		this.getEntityData().set(GROUND_CHARGE, charge);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(source, difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.DIAMOND_MINOTAUR_AXE.get()));
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.LABYRINTH;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return TFBlocks.MANGROVE_CHEST.get();
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.MINOSHROOM_BOSS_SPAWNER.get();
	}

	@Override
	public int getBossBarColor() {
		return 0xFF0000;
	}
}
