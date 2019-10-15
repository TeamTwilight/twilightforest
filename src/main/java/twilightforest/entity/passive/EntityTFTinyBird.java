package twilightforest.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFBirdFly;
import twilightforest.entity.ai.EntityAITFTempt;

public class EntityTFTinyBird extends EntityTFBird {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/tiny_bird");
	private static final DataParameter<Byte> DATA_BIRDTYPE = EntityDataManager.createKey(EntityTFTinyBird.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_BIRDFLAGS = EntityDataManager.createKey(EntityTFTinyBird.class, DataSerializers.BYTE);

	// [VanillaCopy] EntityBat field
	private BlockPos spawnPosition;
	private int currentFlightTime;

	public EntityTFTinyBird(World world) {
		super(world);
		this.setSize(0.3F, 0.3F);
		setBirdType(rand.nextInt(4));
		setIsBirdLanded(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.5F));
		this.tasks.addTask(2, new EntityAITFBirdFly(this));
		this.tasks.addTask(3, new EntityAITFTempt(this, 1.0F, true, SEEDS));
		this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_BIRDTYPE, (byte) 0);
		dataManager.register(DATA_BIRDFLAGS, (byte) 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000001192092896D);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("BirdType", this.getBirdType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setBirdType(compound.getInteger("BirdType"));
	}

	public int getBirdType() {
		return dataManager.get(DATA_BIRDTYPE);
	}

	public void setBirdType(int type) {
		dataManager.set(DATA_BIRDTYPE, (byte) type);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.TINYBIRD_CHIRP;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.TINYBIRD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.TINYBIRD_HURT;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.7F;
	}

	@Override
	public float getRenderSizeModifier() {
		return 0.3F;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		// prefer standing on leaves
		Material underMaterial = this.world.getBlockState(pos.down()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return 200.0F;
		}
		if (underMaterial == Material.WOOD) {
			return 15.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 9.0F;
		}
		// default to just preferring lighter areas
		return this.world.getLightBrightness(pos) - 0.5F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		// while we are flying, try to level out somewhat
		if (!this.isBirdLanded()) {
			this.motionY *= 0.6000000238418579D;
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		if (this.isBirdLanded()) {
			this.currentFlightTime = 0;

			if (isSpooked() || isInWater() || world.containsAnyLiquid(getEntityBoundingBox()) || (this.rand.nextInt(200) == 0 && !isLandableBlock(new BlockPos(posX, posY - 1, posZ)))) {
				this.setIsBirdLanded(false);
				this.world.playEvent(1025, new BlockPos(this), 0);
				this.motionY = 0.4;
			}
		} else {
			this.currentFlightTime++;

			// [VanillaCopy] Modified version of last half of EntityBat.updateAITasks. Edits noted
			if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
				this.spawnPosition = null;
			}

			if (isInWater() || world.containsAnyLiquid(getEntityBoundingBox())) {
				currentFlightTime = 0; // reset timer for MAX FLIGHT :v
				motionY = 0.1F;
			}

			if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double) ((int) this.posX), (double) ((int) this.posY), (double) ((int) this.posZ)) < 4.0D) {
				// TF - modify shift factor of Y
				int yTarget = this.currentFlightTime < 100 ? 2 : 4;
				this.spawnPosition = new BlockPos((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - yTarget, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
			}

			double d0 = (double) this.spawnPosition.getX() + 0.5D - this.posX;
			double d1 = (double) this.spawnPosition.getY() + 0.1D - this.posY;
			double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.posZ;

			this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
			this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
			this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;

			float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
			float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
			this.moveForward = 0.5F;
			this.rotationYaw += f1;

			// TF - change chance 100 -> 10; change check to isLandable
			if (this.rand.nextInt(100) == 0 && isLandableBlock(new BlockPos(posX, posY - 1, posZ))) //this.world.getBlockState(blockpos1).isNormalCube())
			{
				// this.setIsBatHanging(true); TF - land the bird
				setIsBirdLanded(true);
				motionY = 0;
			}
			// End copy
		}
	}

	public boolean isSpooked() {
		if (this.hurtTime > 0) return true;
		EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity(this, 4.0D);
		return closestPlayer != null
				&& !SEEDS.apply(closestPlayer.getHeldItemMainhand())
				&& !SEEDS.apply(closestPlayer.getHeldItemOffhand());
	}

	public boolean isLandableBlock(BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		return !block.isAir(state, world, pos)
				&& (block.isLeaves(state, world, pos) || state.isSideSolid(world, pos, EnumFacing.UP));
	}

	@Override
	public boolean isBirdLanded() {
		return (dataManager.get(DATA_BIRDFLAGS) & 1) != 0;
	}

	public void setIsBirdLanded(boolean landed) {
		byte flags = dataManager.get(DATA_BIRDFLAGS);
		dataManager.set(DATA_BIRDFLAGS, (byte) (landed ? flags | 1 : flags & ~1));
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entity) {}

	@Override
	protected void collideWithNearbyEntities() {}
}
