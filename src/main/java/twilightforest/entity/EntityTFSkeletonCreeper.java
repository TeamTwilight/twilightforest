package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.ai.EntityAITFSkeletonCreeperSwell;

import javax.annotation.Nullable;
import java.util.Collection;

public class EntityTFSkeletonCreeper extends EntityMob {

	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityTFSkeletonCreeper.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean>createKey(EntityTFSkeletonCreeper.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IGNITED = EntityDataManager.<Boolean>createKey(EntityTFSkeletonCreeper.class, DataSerializers.BOOLEAN);
	/**
	 * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
	 * weird)
	 */
	private int lastActiveTime;
	/**
	 * The amount of time since the creeper was close enough to the player to ignite
	 */
	private int timeSinceIgnited;
	private int fuseTime = 30;
	/**
	 * Explosion radius for this creeper.
	 */
	private int explosionRadius = 3;
	private int droppedSkulls;

	public EntityTFSkeletonCreeper(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.7F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAITFSkeletonCreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	/**
	 * The maximum height from where the entity is alowed to jump (used in pathfinder)
	 */
	@Override
	public int getMaxFallHeight() {
		return this.getAttackTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		this.timeSinceIgnited = (int) ((float) this.timeSinceIgnited + distance * 1.5F);

		if (this.timeSinceIgnited > this.fuseTime - 5) {
			this.timeSinceIgnited = this.fuseTime - 5;
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(STATE, Integer.valueOf(-1));
		this.dataManager.register(POWERED, Boolean.valueOf(false));
		this.dataManager.register(IGNITED, Boolean.valueOf(false));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if (((Boolean) this.dataManager.get(POWERED)).booleanValue()) {
			compound.setBoolean("powered", true);
		}

		compound.setShort("Fuse", (short) this.fuseTime);
		compound.setByte("ExplosionRadius", (byte) this.explosionRadius);
		compound.setBoolean("ignited", this.hasIgnited());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(POWERED, Boolean.valueOf(compound.getBoolean("powered")));

		if (compound.hasKey("Fuse", 99)) {
			this.fuseTime = compound.getShort("Fuse");
		}

		if (compound.hasKey("ExplosionRadius", 99)) {
			this.explosionRadius = compound.getByte("ExplosionRadius");
		}

		if (compound.getBoolean("ignited")) {
			this.ignite();
		}
	}

	// [VanillaCopy] These were referenced to creeper classes
	@Override
	public void onUpdate() {
		if (this.isEntityAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
		}

		super.onUpdate();
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		if (this.world.getGameRules().getBoolean("doMobLoot")) {
			if (cause.getTrueSource() instanceof EntitySkeleton) {
				int i = Item.getIdFromItem(Items.RECORD_13);
				int j = Item.getIdFromItem(Items.RECORD_WAIT);
				int k = i + this.rand.nextInt(j - i + 1);
				this.dropItem(Item.getItemById(k), 1);
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		return true;
	}

	/**
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	public boolean getPowered() {
		return ((Boolean) this.dataManager.get(POWERED)).booleanValue();
	}

	@SideOnly(Side.CLIENT)
	public float getCreeperFlashIntensity(float p_70831_1_) {
		return ((float) this.lastActiveTime + (float) (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (float) (this.fuseTime - 2);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_CREEPER;
	}

	public int getCreeperState() {
		return ((Integer) this.dataManager.get(STATE)).intValue();
	}

	public void setCreeperState(int state) {
		this.dataManager.set(STATE, Integer.valueOf(state));
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		super.onStruckByLightning(lightningBolt);
		this.dataManager.set(POWERED, Boolean.valueOf(true));
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
			this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			player.swingArm(hand);

			if (!this.world.isRemote) {
				this.ignite();
				itemstack.damageItem(1, player);
				return true;
			}
		}

		return super.processInteract(player, hand);
	}

	private void explode() {
		this.playSound(SoundEvents.ENTITY_SKELETON_HURT, 2.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));

		if (!this.world.isRemote) {
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			float f = this.getPowered() ? 1.8F : 0.8F;

			//when powered, Bones will be scattered more
			float boneAmount = this.getPowered() ? 2F : 1F;

			this.dead = true;

			for (int i = 0; i < 10 * boneAmount; ++i) {
				EntityTFBoneShard arrow = new EntityTFBoneShard(this.world, this);

				double d0 = (rand.nextFloat() * 2) - 1.0F;
				double d1 = (rand.nextFloat() * 2) - 1.0F;
				double d2 = (rand.nextFloat() * 2) - 1.0F;
				double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
				arrow.shoot(d0 * 2, d1 + d3 * 0.20000000298023224D, d2 * 2, 1.0F, (float) (14 - this.world.getDifficulty().getId() * 4));
				//spawn some bone,not sans bone
				this.world.spawnEntity(arrow);
			}

			this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, flag);
			this.setDead();
			this.spawnLingeringCloud();
		}
	}

	private void spawnLingeringCloud() {
		Collection<PotionEffect> collection = this.getActivePotionEffects();

		if (!collection.isEmpty()) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
			entityareaeffectcloud.setRadius(2.5F);
			entityareaeffectcloud.setRadiusOnUse(-0.5F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
			entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());

			for (PotionEffect potioneffect : collection) {
				entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
			}

			this.world.spawnEntity(entityareaeffectcloud);
		}
	}

	public boolean hasIgnited() {
		return ((Boolean) this.dataManager.get(IGNITED)).booleanValue();
	}

	public void ignite() {
		this.dataManager.set(IGNITED, Boolean.valueOf(true));
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}
}