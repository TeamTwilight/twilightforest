package twilightforest.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFBirdFly;

public class EntityTFTinyBird extends EntityTFBird {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/tiny_bird");
	private static final DataParameter<Byte> DATA_BIRDTYPE = EntityDataManager.createKey(EntityTFTinyBird.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_BIRDFLAGS = EntityDataManager.createKey(EntityTFTinyBird.class, DataSerializers.BYTE);

	// [VanillaCopy] EntityBat field
	private BlockPos spawnPosition;
	private int currentFlightTime;

	public EntityTFTinyBird(EntityType<? extends EntityTFTinyBird> type, World world) {
		super(type, world);
		setBirdType(rand.nextInt(4));
		setIsBirdLanded(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
		this.goalSelector.addGoal(2, new EntityAITFBirdFly(this));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0F, true, SEEDS));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_BIRDTYPE, (byte) 0);
		dataManager.register(DATA_BIRDFLAGS, (byte) 0);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000001192092896D);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("BirdType", this.getBirdType());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setBirdType(compound.getInt("BirdType"));
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
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.7F;
	}

	//TODO: Move to Renderer?
//	@Override
//	public float getRenderSizeModifier() {
//		return 0.3F;
//	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
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
		if (underMaterial == Material.ORGANIC) {
			return 9.0F;
		}
		// default to just preferring lighter areas
		return this.world.getLight(pos) - 0.5F;
	}

	@Override
	public void tick() {
		super.tick();
		// while we are flying, try to level out somewhat
		if (!this.isBirdLanded()) {
			this.setMotion(this.getMotion().mul(1.0F, 0.6000000238418579D, 1.0F));
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		if (this.isBirdLanded()) {
			this.currentFlightTime = 0;

			if (isSpooked() || isInWater() || world.containsAnyLiquid(getBoundingBox()) || (this.rand.nextInt(200) == 0 && !isLandableBlock(new BlockPos(getX(), getY() - 1, getZ())))) {
				this.setIsBirdLanded(false);
				this.world.playEvent(1025, new BlockPos(this), 0);
				this.setMotion(this.getMotion().getX(), 0.4F, this.getMotion().getZ());
			}
		} else {
			this.currentFlightTime++;

			// [VanillaCopy] Modified version of last half of EntityBat.updateAITasks. Edits noted
			if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
				this.spawnPosition = null;
			}

			if (isInWater() || world.containsAnyLiquid(getBoundingBox())) {
				currentFlightTime = 0; // reset timer for MAX FLIGHT :v
				this.setMotion(this.getMotion().getX(), 0.1F, this.getMotion().getZ());
			}

			if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq(new Vec3i(((int) this.getX()), ((int) this.getY()), ((int) this.getZ()))) < 4.0D) {
				// TF - modify shift factor of Y
				int yTarget = this.currentFlightTime < 100 ? 2 : 4;
				this.spawnPosition = new BlockPos((int) this.getX() + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.getY() + this.rand.nextInt(6) - yTarget, (int) this.getZ() + this.rand.nextInt(7) - this.rand.nextInt(7));
			}

			double d0 = (double) this.spawnPosition.getX() + 0.5D - this.getX();
			double d1 = (double) this.spawnPosition.getY() + 0.1D - this.getY();
			double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.getZ();

//			this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
//			this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
//			this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
			this.getMotion().add(new Vec3d(
					(Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D,
					(Math.signum(d1) * 0.699999988079071D - this.getMotion().getY()) * 0.10000000149011612D,
					(Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D
			));

			float f = (float) (MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
			float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
			this.moveForward = 0.5F;
			this.rotationYaw += f1;

			// TF - change chance 100 -> 10; change check to isLandable
			if (this.rand.nextInt(100) == 0 && isLandableBlock(new BlockPos(getX(), getY() - 1, getZ()))) //this.world.getBlockState(blockpos1).isNormalCube())
			{
				// this.setIsBatHanging(true); TF - land the bird
				setIsBirdLanded(true);
				this.setMotion(this.getMotion().getX(), 0.0F, this.getMotion().getZ());
			}
			// End copy
		}
	}

	public boolean isSpooked() {
		if (this.hurtTime > 0) return true;
		PlayerEntity closestPlayer = this.world.getClosestPlayer(this, 4.0D);
		return closestPlayer != null
				&& !SEEDS.test(closestPlayer.getHeldItemMainhand())
				&& !SEEDS.test(closestPlayer.getHeldItemOffhand());
	}

	public boolean isLandableBlock(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		return !block.isAir(state, world, pos)
				&& (block.isIn(BlockTags.LEAVES) || state.isSideSolidFullSquare(world, pos, Direction.UP));
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
	protected void collideWithEntity(Entity entity) {
	}

	@Override
	protected void collideWithNearbyEntities() {
	}
}
