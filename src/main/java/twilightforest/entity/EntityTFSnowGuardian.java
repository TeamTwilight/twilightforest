package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

public class EntityTFSnowGuardian extends EntityTFIceMob {

	public EntityTFSnowGuardian(EntityType<? extends EntityTFSnowGuardian> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_GUARDIAN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_GUARDIAN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_GUARDIAN_DEATH;
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		int type = rand.nextInt(4);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(this.makeItemForSlot(EquipmentSlotType.MAINHAND, type)));
		this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(this.makeItemForSlot(EquipmentSlotType.CHEST, type)));
		this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(this.makeItemForSlot(EquipmentSlotType.HEAD, type)));
	}

	private Item makeItemForSlot(EquipmentSlotType slot, int type) {
		switch (slot) {
			case MAINHAND:
			default:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_sword.get();
					case 1:
						return TFItems.steeleaf_sword.get();
					case 2:
						return TFItems.knightmetal_sword.get();
					case 3:
						return TFItems.knightmetal_sword.get();
				}
			case FEET:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_boots.get();
					case 1:
						return TFItems.steeleaf_boots.get();
					case 2:
						return TFItems.knightmetal_boots.get();
					case 3:
						return TFItems.arctic_boots.get();
				}
			case LEGS:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_leggings.get();
					case 1:
						return TFItems.steeleaf_leggings.get();
					case 2:
						return TFItems.knightmetal_leggings.get();
					case 3:
						return TFItems.arctic_leggings.get();
				}
			case CHEST:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_chestplate.get();
					case 1:
						return TFItems.steeleaf_chestplate.get();
					case 2:
						return TFItems.knightmetal_chestplate.get();
					case 3:
						return TFItems.arctic_chestplate.get();
				}
			case HEAD:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_helmet.get();
					case 1:
						return TFItems.steeleaf_helmet.get();
					case 2:
						return TFItems.knightmetal_helmet.get();
					case 3:
						return TFItems.arctic_helmet.get();
				}
		}
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		ILivingEntityData data = super.onInitialSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return data;
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (this.world.isRemote) {
			for (int i = 0; i < 3; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

				world.addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
			}
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}
}
