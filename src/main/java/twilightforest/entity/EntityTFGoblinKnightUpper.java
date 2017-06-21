package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFHeavySpearAttack;

import java.util.List;

public class EntityTFGoblinKnightUpper extends EntityMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/goblin_knight");
	private static final int SHIELD_DAMAGE_THRESHOLD = 10;
	private static final DataParameter<Byte> DATA_EQUIP = EntityDataManager.createKey(EntityTFGoblinKnightUpper.class, DataSerializers.BYTE);
	private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier("Armor boost", 20, 0).setSaved(false);
	private static final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier("Heavy spear attack boost", 12, 0).setSaved(false);
	public static final int HEAVY_SPEAR_TIMER_START = 60;

	private int shieldHits = 0;
	public int heavySpearTimer;


	public EntityTFGoblinKnightUpper(World par1World) {
		super(par1World);
		setSize(1.1F, 1.3F);

		this.setHasArmor(true);
		this.setHasShield(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAITFHeavySpearAttack(this));
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_EQUIP, (byte) 0);
	}

	public boolean hasArmor() {
		return (dataManager.get(DATA_EQUIP) & 1) > 0;
	}

	private void setHasArmor(boolean flag) {
		byte otherFlags = dataManager.get(DATA_EQUIP);
		dataManager.set(DATA_EQUIP, flag ? (byte) (otherFlags | 1) : (byte) (otherFlags & ~1));

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

	public boolean hasShield() {
		return (dataManager.get(DATA_EQUIP) & 2) > 0;
	}

	public void setHasShield(boolean flag) {
		byte otherFlags = dataManager.get(DATA_EQUIP);
		dataManager.set(DATA_EQUIP, flag ? (byte) (otherFlags | 2) : (byte) (otherFlags & ~2));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("hasArmor", this.hasArmor());
		par1NBTTagCompound.setBoolean("hasShield", this.hasShield());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setHasArmor(par1NBTTagCompound.getBoolean("hasArmor"));
		this.setHasShield(par1NBTTagCompound.getBoolean("hasShield"));
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		// Must be decremented on client as well for rendering
		if ((world.isRemote || !isAIDisabled()) && heavySpearTimer > 0) {
			--heavySpearTimer;
		}
	}

	@Override
	public void updateAITasks() {
		super.updateAITasks();

		if (this.isEntityAlive()) {
			// synch target with lower goblin
			if (getRidingEntity() instanceof EntityLiving && this.getAttackTarget() == null) {
				this.setAttackTarget(((EntityLiving) this.getRidingEntity()).getAttackTarget());
			}

			if (!isRiding() && this.hasShield()) {
				this.breakShield();
			}

			if (heavySpearTimer > 0) {
				if (!getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(DAMAGE_MODIFIER)) {
					getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(DAMAGE_MODIFIER);
				}
			} else {
				getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER.getID());
			}
		}
	}

	public void landHeavySpearAttack() {
		// find vector in front of us
		Vec3d vector = this.getLookVec();

		double dist = 1.25;
		double px = this.posX + vector.xCoord * dist;
		double py = this.getEntityBoundingBox().minY - 0.75;
		double pz = this.posZ + vector.zCoord * dist;


		for (int i = 0; i < 50; i++) {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, px, py, pz, (rand.nextFloat() - rand.nextFloat()) * 0.25F, 0, (rand.nextFloat() - rand.nextFloat()) * 0.25F);
		}

		// damage things in front that aren't us or our "mount"
		double radius = 1.5D;

		AxisAlignedBB spearBB = new AxisAlignedBB(px - radius, py - radius, pz - radius, px + radius, py + radius, pz + radius);

		List<Entity> inBox = world.getEntitiesInAABBexcluding(this, spearBB, e -> e != this.getRidingEntity());

		for (Entity entity : inBox) {
			super.attackEntityAsMob(entity);
		}

		if (!inBox.isEmpty()) {
			playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, getSoundVolume(), getSoundPitch());
		}
	}

	@Override
	public void updateRidden() {
		super.updateRidden();
		if (isRiding()) {
			this.renderYawOffset = ((EntityLiving) this.getRidingEntity()).renderYawOffset;
		}
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte par1) {
		if (par1 == 4) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
		} else if (par1 == 5) {
			ItemStack broken = new ItemStack(Items.IRON_CHESTPLATE);
			this.renderBrokenItemStack(broken);
			this.renderBrokenItemStack(broken);
			this.renderBrokenItemStack(broken);
		} else {
			super.handleStatusUpdate(par1);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {

		if (this.heavySpearTimer > 0) {
			return false;
		}

		if (rand.nextInt(2) == 0) {
			this.heavySpearTimer = HEAVY_SPEAR_TIMER_START;
			this.world.setEntityState(this, (byte) 4);
			return false;
		}

		this.swingArm(EnumHand.MAIN_HAND);
		return super.attackEntityAsMob(par1Entity);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float damageAmount) {
		// don't take suffocation damage while riding
		if (par1DamageSource == DamageSource.IN_WALL && !this.getPassengers().isEmpty()) {
			return false;
		}

		Entity attacker = par1DamageSource.getSourceOfDamage();

		if (attacker != null) {
			double dx = this.posX - attacker.posX;
			double dz = this.posZ - attacker.posZ;
			float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

			float difference = MathHelper.abs((this.renderYawOffset - angle) % 360);

			if (this.hasShield() && difference > 150 && difference < 230) {
				if (takeHitOnShield(par1DamageSource, damageAmount)) {
					return false;
				}
			} else {
				if (this.hasShield() && rand.nextBoolean()) {
					damageShield();
				}
			}

			if (this.hasArmor() && (difference > 300 || difference < 60)) {
				breakArmor();
			}
		}

		return super.attackEntityFrom(par1DamageSource, damageAmount);
	}

	private void breakArmor() {
		world.setEntityState(this, (byte) 5);
		this.setHasArmor(false);
	}

	private void breakShield() {
		world.setEntityState(this, (byte) 5);
		this.setHasShield(false);
	}


	public boolean takeHitOnShield(DamageSource par1DamageSource, float damageAmount) {
		if (damageAmount > SHIELD_DAMAGE_THRESHOLD && !this.world.isRemote) {
			damageShield();
		} else {
			playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}


		// knock back slightly
		EntityLiving toKnockback = (getRidingEntity() instanceof EntityLiving) ? (EntityLiving) getRidingEntity() : this;

		if (par1DamageSource.getEntity() != null) {
			double d0 = par1DamageSource.getEntity().posX - this.posX;
			double d1;

			for (d1 = par1DamageSource.getEntity().posZ - this.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
				d0 = (Math.random() - Math.random()) * 0.01D;
			}

			toKnockback.knockBack(par1DamageSource.getEntity(), 0, d0 / 4D, d1 / 4D);

			// also set revenge target
			if (par1DamageSource.getEntity() instanceof EntityLiving) {
				this.setRevengeTarget((EntityLiving) par1DamageSource.getEntity());
			}
		}


		return true;
	}


	private void damageShield() {
		playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.25F, 0.25F);

		this.shieldHits++;

		if (!world.isRemote && this.shieldHits >= 3) {
			this.breakShield();
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
