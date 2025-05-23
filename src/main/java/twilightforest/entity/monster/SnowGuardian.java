package twilightforest.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

public class SnowGuardian extends BaseIceMob {

	public SnowGuardian(EntityType<? extends SnowGuardian> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 3.0D)
			.add(Attributes.MAX_HEALTH, 10.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.SNOW_GUARDIAN_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.SNOW_GUARDIAN_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.SNOW_GUARDIAN_DEATH.get();
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.8F;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		int type = random.nextInt(4);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.makeItemForSlot(EquipmentSlot.MAINHAND, type)));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(this.makeItemForSlot(EquipmentSlot.CHEST, type)));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.makeItemForSlot(EquipmentSlot.HEAD, type)));
	}

	private Item makeItemForSlot(EquipmentSlot slot, int type) {
		return switch (slot) {
			default -> switch (type) {
				default -> TFItems.IRONWOOD_SWORD.get();
				case 1 -> TFItems.STEELEAF_SWORD.get();
				case 2, 3 -> TFItems.KNIGHTMETAL_SWORD.get();
			};
			case FEET -> switch (type) {
				default -> TFItems.IRONWOOD_BOOTS.get();
				case 1 -> TFItems.STEELEAF_BOOTS.get();
				case 2 -> TFItems.KNIGHTMETAL_BOOTS.get();
				case 3 -> TFItems.ARCTIC_BOOTS.get();
			};
			case LEGS -> switch (type) {
				default -> TFItems.IRONWOOD_LEGGINGS.get();
				case 1 -> TFItems.STEELEAF_LEGGINGS.get();
				case 2 -> TFItems.KNIGHTMETAL_LEGGINGS.get();
				case 3 -> TFItems.ARCTIC_LEGGINGS.get();
			};
			case CHEST -> switch (type) {
				default -> TFItems.IRONWOOD_CHESTPLATE.get();
				case 1 -> TFItems.STEELEAF_CHESTPLATE.get();
				case 2 -> TFItems.KNIGHTMETAL_CHESTPLATE.get();
				case 3 -> TFItems.ARCTIC_CHESTPLATE.get();
			};
			case HEAD -> switch (type) {
				default -> TFItems.IRONWOOD_HELMET.get();
				case 1 -> TFItems.STEELEAF_HELMET.get();
				case 2 -> TFItems.KNIGHTMETAL_HELMET.get();
				case 3 -> TFItems.ARCTIC_HELMET.get();
			};
		};
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
		SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnDataIn);
		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor, accessor.getRandom(), difficulty);
		return data;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level().isClientSide()) {
			for (int i = 0; i < 3; i++) {
				float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
				float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;

				this.level().addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
			}
		}
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}
}
