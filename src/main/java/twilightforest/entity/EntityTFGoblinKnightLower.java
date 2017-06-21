package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.entity.ai.EntityAITFRiderSpearAttack;

public class EntityTFGoblinKnightLower extends EntityMob {
	private static final DataParameter<Boolean> ARMOR = EntityDataManager.createKey(EntityTFGoblinKnightLower.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier("Armor boost", 17, 0).setSaved(false);

	public EntityTFGoblinKnightLower(World par1World) {
		super(par1World);
		setSize(0.7F, 1.1F);
		this.setHasArmor(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAITFRiderSpearAttack(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(ARMOR, false);
	}

	public boolean hasArmor() {
		return dataManager.get(ARMOR);
	}

	private void setHasArmor(boolean flag) {
		dataManager.set(ARMOR, flag);

		if (!world.isRemote) {
			if (flag) {
				if (!getEntityAttribute(SharedMonsterAttributes.ARMOR).hasModifier(ARMOR_MODIFIER)) {
					getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(ARMOR_MODIFIER);
				}
			} else {
				getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("hasArmor", this.hasArmor());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setHasArmor(par1NBTTagCompound.getBoolean("hasArmor"));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData par1EntityLivingData) {
		Object par1EntityLivingData1 = super.onInitialSpawn(difficulty, par1EntityLivingData);

		EntityTFGoblinKnightUpper upper = new EntityTFGoblinKnightUpper(this.world);
		upper.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
		upper.onInitialSpawn(difficulty, null);
		this.world.spawnEntity(upper);
		upper.startRiding(this);

		return (IEntityLivingData) par1EntityLivingData1;
	}

	@Override
	public double getMountedYOffset() {
		return 1.0D;
	}

	@Override
	public void updateAITasks() {
		super.updateAITasks();

		if (isBeingRidden() && getPassengers().get(0) instanceof EntityLiving && this.getAttackTarget() == null) {
			this.setAttackTarget(((EntityLiving) this.getPassengers().get(0)).getAttackTarget());
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {

		if (isBeingRidden() && getPassengers().get(0) instanceof EntityLiving) {
			return ((EntityLiving) this.getPassengers().get(0)).attackEntityAsMob(par1Entity);
		} else {
			return super.attackEntityAsMob(par1Entity);
		}

	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float damageAmount) {
		// check the angle of attack, if applicable
		Entity attacker = null;
		if (par1DamageSource.getSourceOfDamage() != null) {
			attacker = par1DamageSource.getSourceOfDamage();
		}

		if (par1DamageSource.getEntity() != null) {
			attacker = par1DamageSource.getEntity();
		}

		if (attacker != null) {
			// determine angle

			double dx = this.posX - attacker.posX;
			double dz = this.posZ - attacker.posZ;
			float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

			float difference = MathHelper.abs((this.renderYawOffset - angle) % 360);

			// shield?
			EntityTFGoblinKnightUpper upper = null;

			if (isBeingRidden() && getPassengers().get(0) instanceof EntityTFGoblinKnightUpper) {
				upper = (EntityTFGoblinKnightUpper) this.getPassengers().get(0);
			}

			if (upper != null && upper.hasShield() && difference > 150 && difference < 230) {
				if (upper.takeHitOnShield(par1DamageSource, damageAmount)) {
					return false;
				}
			}

			// break armor?
			if (this.hasArmor() && (difference > 300 || difference < 60)) {
				breakArmor();
			}
		}

		return super.attackEntityFrom(par1DamageSource, damageAmount);
	}

	private void breakArmor() {
		this.renderBrokenItemStack(new ItemStack(Items.IRON_CHESTPLATE));
		this.renderBrokenItemStack(new ItemStack(Items.IRON_CHESTPLATE));
		this.renderBrokenItemStack(new ItemStack(Items.IRON_CHESTPLATE));

		this.setHasArmor(false);
	}

}
