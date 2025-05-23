package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ai.goal.AvoidAnyEntityGoal;
import twilightforest.entity.ai.goal.RedcapLightTNTGoal;
import twilightforest.entity.ai.goal.RedcapShyGoal;
import twilightforest.init.TFSounds;

public class Redcap extends Monster {

	public ItemStack heldPick = new ItemStack(Items.IRON_PICKAXE);
	public final ItemStack heldTNT = new ItemStack(Blocks.TNT);
	public final ItemStack heldFlint = new ItemStack(Items.FLINT_AND_STEEL);

	public Redcap(EntityType<? extends Redcap> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AvoidAnyEntityGoal<>(this, PrimedTnt.class, 2.0F, 1.0F, 2.0F));
		this.goalSelector.addGoal(2, new RedcapShyGoal(this, 1.0F));
		this.goalSelector.addGoal(3, new RedcapLightTNTGoal(this, 1.0F)); // light TNT
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.28D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.REDCAP_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.REDCAP_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.REDCAP_DEATH.get();
	}

	public boolean isShy() {
		return this.lastHurtByPlayerTime <= 0;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
		SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnDataIn);

		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor, accessor.getRandom(), difficulty);

		this.setDropChance(EquipmentSlot.MAINHAND, 0.2F);
		this.setDropChance(EquipmentSlot.FEET, 0.2F);

		return data;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, this.heldPick);
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("TNTLeft", this.heldTNT.getCount());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.heldTNT.setCount(compound.getInt("TNTLeft"));
	}
}
