package twilightforest.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.item.TFItems;

public class EntityTFSnowGuardian extends EntityMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/snow_guardian");

	public EntityTFSnowGuardian(World par1World) {
		super(par1World);
		this.setSize(0.6F, 1.8F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_DEATH;
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		int type = rand.nextInt(4);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(this.makeItemForSlot(EntityEquipmentSlot.MAINHAND, type)));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(this.makeItemForSlot(EntityEquipmentSlot.CHEST, type)));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.makeItemForSlot(EntityEquipmentSlot.HEAD, type)));
	}

	private Item makeItemForSlot(EntityEquipmentSlot slot, int type) {
		switch (slot) {
			case MAINHAND:
			default:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_sword;
					case 1:
						return TFItems.steeleaf_sword;
					case 2:
						return TFItems.knightmetal_sword;
					case 3:
						return TFItems.knightmetal_sword;
				}
			case FEET:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_boots;
					case 1:
						return TFItems.steeleaf_boots;
					case 2:
						return TFItems.knightmetal_boots;
					case 3:
						return TFItems.arctic_boots;
				}
			case LEGS:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_leggings;
					case 1:
						return TFItems.steeleaf_leggings;
					case 2:
						return TFItems.knightmetal_leggings;
					case 3:
						return TFItems.arctic_leggings;
				}
			case CHEST:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_chestplate;
					case 1:
						return TFItems.steeleaf_chestplate;
					case 2:
						return TFItems.knightmetal_chestplate;
					case 3:
						return TFItems.arctic_chestplate;
				}
			case HEAD:
				switch (type) {
					case 0:
					default:
						return TFItems.ironwood_helmet;
					case 1:
						return TFItems.steeleaf_helmet;
					case 2:
						return TFItems.knightmetal_helmet;
					case 3:
						return TFItems.arctic_helmet;
				}
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingData);
		this.setEquipmentBasedOnDifficulty(difficulty);
		return data;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		for (int i = 0; i < 3; i++) {
			float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
			float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
			float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.SNOW_GUARDIAN, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}
}
