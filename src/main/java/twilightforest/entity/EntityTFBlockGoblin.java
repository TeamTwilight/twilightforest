package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

import java.util.List;

public class EntityTFBlockGoblin extends EntityMob implements IEntityMultiPart {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/block_goblin");
	private static final float CHAIN_SPEED = 16F;
	private static final DataParameter<Byte> DATA_CHAINLENGTH = EntityDataManager.createKey(EntityTFBlockGoblin.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_CHAINPOS = EntityDataManager.createKey(EntityTFBlockGoblin.class, DataSerializers.BYTE);

	private int recoilCounter;
	private float chainAngle;

	public final EntityTFSpikeBlock block = new EntityTFSpikeBlock(this);
	public final EntityTFGoblinChain chain1 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain2 = new EntityTFGoblinChain(this);
	public final EntityTFGoblinChain chain3 = new EntityTFGoblinChain(this);

	private final Entity[] partsArray = new Entity[]{block, chain1, chain2, chain3};

	public EntityTFBlockGoblin(World world) {
		super(world);
		setSize(0.9F, 1.4F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityTNTPrimed.class, 2.0F, 0.8F, 1.5F));
		this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0F, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_CHAINLENGTH, (byte) 0);
		dataManager.register(DATA_CHAINPOS, (byte) 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(11.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.REDCAP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return TFSounds.REDCAP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.REDCAP_AMBIENT;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
			((EntityPlayer) par1DamageSource.getSourceOfDamage()).addStat(TFAchievementPage.twilightHunter);
		}
	}

	/**
	 * How high is the chain
	 */
	public double getChainYOffset() {
		return 1.5 - this.getChainLength() / 4.0;
	}

	/**
	 * Get the block & chain position
	 */
	public Vec3d getChainPosition() {
		return this.getChainPosition(getChainAngle(), getChainLength());
	}

	/**
	 * Get the block & chain position
	 */
	public Vec3d getChainPosition(float angle, float distance) {
		double var1 = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double var3 = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vec3d(this.posX + var1, this.posY + this.getChainYOffset(), this.posZ + var3);
	}

	public boolean isSwingingChain() {
		return this.isSwingInProgress || (this.getAttackTarget() != null && this.recoilCounter == 0);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		swingArm(EnumHand.MAIN_HAND);
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		block.onUpdate();
		chain1.onUpdate();
		chain2.onUpdate();
		chain3.onUpdate();

		if (recoilCounter > 0) {
			recoilCounter--;
		}


		chainAngle += CHAIN_SPEED;
		chainAngle %= 360;

		if (!this.world.isRemote) {
			dataManager.set(DATA_CHAINLENGTH, (byte) Math.floor(getChainLength() * 127.0F));
			dataManager.set(DATA_CHAINPOS, (byte) Math.floor(getChainAngle() / 360.0F * 255.0F));
		} else {
			// synch chain pos if it's wrong
			if (Math.abs(this.chainAngle - this.getChainAngle()) > CHAIN_SPEED * 2) {
				//FMLLog.info("Fixing chain pos on client");
				this.chainAngle = getChainAngle();
			}
		}

		// set block position
		Vec3d blockPos = this.getChainPosition();
		this.block.setPosition(blockPos.xCoord, blockPos.yCoord, blockPos.zCoord);
		this.block.rotationYaw = getChainAngle();

		// interpolate chain position
		double sx = this.posX;
		double sy = this.posY + this.height - 0.1;
		double sz = this.posZ;

		double ox = sx - blockPos.xCoord;
		double oy = sy - blockPos.yCoord - (block.height / 3D);
		double oz = sz - blockPos.zCoord;

		this.chain1.setPosition(sx - ox * 0.4, sy - oy * 0.4, sz - oz * 0.4);
		this.chain2.setPosition(sx - ox * 0.5, sy - oy * 0.5, sz - oz * 0.5);
		this.chain3.setPosition(sx - ox * 0.6, sy - oy * 0.6, sz - oz * 0.6);

		// collide things with the block
		if (!world.isRemote && this.isSwingingChain()) {
			this.applyBlockCollisions(this.block);
		}

	}

	/**
	 * Check if the block is colliding with any nearby entities
	 */
	protected void applyBlockCollisions(Entity collider) {
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(collider, collider.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

		for (Entity entity : list) {
			if (entity.canBePushed()) {
				applyBlockCollision(collider, entity);
			}
		}
	}

	/**
	 * Do the effect where the block hits something
	 */
	protected void applyBlockCollision(Entity collider, Entity collided) {
		if (collided != this) {
			collided.applyEntityCollision(collider);
			if (collided instanceof EntityLivingBase) {
				if (super.attackEntityAsMob(collided)) {
					collided.motionY += 0.4;
					this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
					this.recoilCounter = 40;
				}

			}
		}
	}

	/**
	 * Angle between 0 and 360 to place the chain at
	 */
	private float getChainAngle() {
		if (!this.world.isRemote) {
			return this.chainAngle;
		} else {
			return (dataManager.get(DATA_CHAINPOS) & 0xFF) / 255.0F * 360.0F;
		}
	}

	/**
	 * Between 0.0F and 2.0F, how long is the chain right now?
	 */
	private float getChainLength() {
		if (!this.world.isRemote) {
			if (isSwingingChain()) {
				return 0.9F;
			} else {
				return 0.3F;
			}
		} else {
			return (dataManager.get(DATA_CHAINLENGTH) & 0xFF) / 127.0F;
		}
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart entitydragonpart, DamageSource damagesource, float i) {
		return false;
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Override
	public Entity[] getParts() {
		return partsArray;
	}
}
