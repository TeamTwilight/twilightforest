package twilightforest.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.AvoidEntityGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFRedcapLightTNT;
import twilightforest.entity.ai.EntityAITFRedcapShy;

public class EntityTFRedcap extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/redcap");

	public ItemStack heldPick = new ItemStack(Items.IRON_PICKAXE, 1);
	public ItemStack heldTNT = new ItemStack(Blocks.TNT, 1);
	public ItemStack heldFlint = new ItemStack(Items.FLINT_AND_STEEL, 1);

	public EntityTFRedcap(World world) {
		super(world);
		setSize(0.9F, 1.4F);
	}

	public EntityTFRedcap(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new AvoidEntityGoal<>(this, EntityTNTPrimed.class, 2.0F, 1.0F, 2.0F));
		this.tasks.addTask(2, new EntityAITFRedcapShy(this, 1.0F));
		this.tasks.addTask(3, new EntityAITFRedcapLightTNT(this, 1.0F)); // light TNT
		this.tasks.addTask(5, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(7, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.REDCAP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.REDCAP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.REDCAP_DEATH;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	public boolean isShy() {
		return this.recentlyHit <= 0;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);

		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);

		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.2F);
		this.setDropChance(EntityEquipmentSlot.FEET, 0.2F);

		return data;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldPick);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("TNTLeft", heldTNT.getCount());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		heldTNT.setCount(compound.getInteger("TNTLeft"));
	}
}
