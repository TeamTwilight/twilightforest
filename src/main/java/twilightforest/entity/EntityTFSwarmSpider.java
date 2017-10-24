package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.util.PlayerHelper;


public class EntityTFSwarmSpider extends EntitySpider {

	protected boolean shouldSpawn = false;

	public EntityTFSwarmSpider(World world) {
		this(world, true);
	}

	public EntityTFSwarmSpider(World world, boolean spawnMore) {
		super(world);

		setSize(0.8F, 0.4F);
		setSpawnMore(spawnMore);
		experienceValue = 2;
	}

	public EntityTFSwarmSpider(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		// Remove default spider melee task
		this.tasks.taskEntries.removeIf(t -> t.action instanceof EntityAIAttackMelee);

		// Replace with one that doesn't become docile in light
		// [VanillaCopy] based on EntitySpider.AISpiderAttack
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1, true) {
			@Override
			protected double getAttackReachSqr(EntityLivingBase attackTarget) {
				return 4.0F + attackTarget.width;
			}
		});

		// Remove default spider target player task
		this.targetTasks.taskEntries.removeIf(t -> t.priority == 2 && t.action instanceof EntityAINearestAttackableTarget);
		// Replace with one that doesn't care about light
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	public float getRenderSizeModifier() {
		return 0.5F;
	}

	@Override
	public float getEyeHeight() {
		return 0.3F;
	}

	@Override
	public void onUpdate() {
		if (!world.isRemote && shouldSpawnMore()) {
			int more = 1 + rand.nextInt(2);
			for (int i = 0; i < more; i++) {
				// try twice to spawn
				if (!spawnAnother()) {
					spawnAnother();
				}
			}
			setSpawnMore(false);
		}

		super.onUpdate();
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return rand.nextInt(4) == 0 && super.attackEntityAsMob(entity);
	}

	protected boolean spawnAnother() {
		EntityTFSwarmSpider another = new EntityTFSwarmSpider(world, false);


		double sx = posX + (rand.nextBoolean() ? 0.9 : -0.9);
		double sy = posY;
		double sz = posZ + (rand.nextBoolean() ? 0.9 : -0.9);
		another.setLocationAndAngles(sx, sy, sz, rand.nextFloat() * 360F, 0.0F);
		if (!another.getCanSpawnHere()) {
			another.setDead();
			return false;
		}
		world.spawnEntity(another);
		another.spawnExplosionParticle();

		return true;
	}

	@Override
	protected boolean isValidLightLevel() {
		int chunkX = MathHelper.floor(posX) >> 4;
		int chunkZ = MathHelper.floor(posZ) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.hedgeMaze || super.isValidLightLevel();
	}

	public boolean shouldSpawnMore() {
		return shouldSpawn;
	}

	public void setSpawnMore(boolean flag) {
		this.shouldSpawn = flag;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("SpawnMore", shouldSpawnMore());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		setSpawnMore(nbttagcompound.getBoolean("SpawnMore"));
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (source.getTrueSource() instanceof EntityPlayerMP) {
			// are we in a hedge maze?
			int chunkX = MathHelper.floor(posX) >> 4;
			int chunkZ = MathHelper.floor(posZ) >> 4;
			if (TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.hedgeMaze) {
				PlayerHelper.grantCriterion((EntityPlayerMP) source.getTrueSource(), new ResourceLocation(TwilightForestMod.ID, "hedge"), "swarm_spider");
			}
		}
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 16;
	}

}
